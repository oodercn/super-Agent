package net.ooder.nexus.infrastructure.openwrt.bridge;

import com.jcraft.jsch.*;
import net.ooder.nexus.infrastructure.openwrt.bridge.exception.CommandExecuteException;
import net.ooder.nexus.infrastructure.openwrt.bridge.exception.ConfigException;
import net.ooder.nexus.infrastructure.openwrt.bridge.exception.ConnectionTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultOpenWrtBridge implements OpenWrtBridge {

    private static final Logger log = LoggerFactory.getLogger(DefaultOpenWrtBridge.class);

    private String host;
    private String username;
    private String password;
    private boolean connected;

    private JSch jsch;
    private Session session;
    private int sshPort = 22;
    private int connectionTimeout = 30000;
    private int commandTimeout = 60000;

    private int maxConnectionRetries = 3;
    private int connectionRetryInterval = 2000;
    private int maxCommandRetries = 2;
    private int commandRetryInterval = 1000;
    private int maxUciRetries = 2;
    private int uciRetryInterval = 1500;

    private final Map<String, Map<String, Object>> networkSettingsCache = new ConcurrentHashMap<>();

    private String openWrtVersion;
    private boolean versionDetected = false;

    private final OpenWrtBridgeMonitor monitor = OpenWrtBridgeMonitor.getInstance();

    public DefaultOpenWrtBridge() {
        this.connected = false;
        this.jsch = new JSch();
        initializeDefaultCache();
    }

    protected void setSession(Session session) {
        this.session = session;
        this.connected = true;
    }

    private void initializeDefaultCache() {
        Map<String, Object> basicSettings = new ConcurrentHashMap<>();
        basicSettings.put("networkName", "Home Network");
        basicSettings.put("domainName", "home.local");
        basicSettings.put("timezone", "Asia/Shanghai");
        basicSettings.put("ntpServer", "pool.ntp.org");
        basicSettings.put("interfaceSpeed", "auto");
        networkSettingsCache.put("basic", basicSettings);

        Map<String, Object> dnsSettings = new ConcurrentHashMap<>();
        dnsSettings.put("primaryDns", "8.8.8.8");
        dnsSettings.put("secondaryDns", "8.8.4.4");
        dnsSettings.put("dnsSuffix", "home.local");
        dnsSettings.put("dnsCacheEnabled", true);
        dnsSettings.put("dnsCacheSize", 1000);
        networkSettingsCache.put("dns", dnsSettings);

        Map<String, Object> dhcpSettings = new ConcurrentHashMap<>();
        dhcpSettings.put("dhcpEnabled", true);
        dhcpSettings.put("startIp", "192.168.1.100");
        dhcpSettings.put("endIp", "192.168.1.200");
        dhcpSettings.put("leaseTime", 86400);
        dhcpSettings.put("gateway", "192.168.1.1");
        dhcpSettings.put("subnetMask", "255.255.255.0");
        networkSettingsCache.put("dhcp", dhcpSettings);

        Map<String, Object> wifiSettings = new ConcurrentHashMap<>();
        wifiSettings.put("ssid", "HomeWiFi");
        wifiSettings.put("channel", 6);
        wifiSettings.put("mode", "802.11ac");
        wifiSettings.put("security", "WPA2-PSK");
        wifiSettings.put("password", "********");
        wifiSettings.put("txPower", "auto");
        networkSettingsCache.put("wifi", wifiSettings);
    }

    @Override
    public boolean connect(String host, String username, String password) {
        long startTime = monitor.startOperation("connect");
        log.info("Connecting to OpenWrt router: {}@{}", username, host);

        this.host = host;
        this.username = username;
        this.password = password;

        if (session != null && session.isConnected()) {
            session.disconnect();
        }

        int retries = 0;
        while (retries < maxConnectionRetries) {
            try {
                log.info("Connection attempt {}/{}", retries + 1, maxConnectionRetries);

                session = jsch.getSession(username, host, sshPort);
                session.setPassword(password);
                session.setConfig("StrictHostKeyChecking", "no");
                session.connect(connectionTimeout);

                this.connected = true;
                log.info("Successfully connected to OpenWrt router: {}", host);

                detectOpenWrtVersion();

                monitor.recordConnectionSuccess();
                monitor.endOperation("connect", startTime, true);

                return true;
            } catch (JSchException e) {
                retries++;
                log.warn("Connection attempt {} failed: {}", retries, e.getMessage());

                if (session != null) {
                    try { session.disconnect(); } catch (Exception ex) {}
                    session = null;
                }

                if (e.getMessage().contains("timeout")) {
                    monitor.recordConnectionFailure();
                    monitor.endOperation("connect", startTime, false);
                    throw new ConnectionTimeoutException("Connection timeout after " + connectionTimeout + "ms", e);
                }

                if (retries >= maxConnectionRetries) {
                    log.error("Failed to connect to OpenWrt router after {} attempts: {}", maxConnectionRetries, e.getMessage(), e);
                    this.connected = false;
                    monitor.recordConnectionFailure();
                    monitor.endOperation("connect", startTime, false);
                    throw new RuntimeException("Failed to connect to OpenWrt router after " + maxConnectionRetries + " attempts", e);
                }

                try {
                    Thread.sleep(connectionRetryInterval);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    log.warn("Connection retry interrupted", ie);
                    this.connected = false;
                    monitor.recordConnectionFailure();
                    monitor.endOperation("connect", startTime, false);
                    return false;
                }
            }
        }

        this.connected = false;
        monitor.recordConnectionFailure();
        monitor.endOperation("connect", startTime, false);
        return false;
    }

    @Override
    public void disconnect() {
        long startTime = monitor.startOperation("disconnect");
        log.info("Disconnecting from OpenWrt router: {}", host);

        if (session != null && session.isConnected()) {
            try {
                session.disconnect();
                log.info("SSH session disconnected");
            } catch (Exception e) {
                log.error("Error disconnecting SSH session: {}", e.getMessage(), e);
                monitor.endOperation("disconnect", startTime, false);
                return;
            }
        }

        this.connected = false;
        this.host = null;
        this.username = null;
        this.password = null;
        this.session = null;
        this.openWrtVersion = null;
        this.versionDetected = false;

        monitor.recordConnectionClose();
        monitor.endOperation("disconnect", startTime, true);

        log.info("Successfully disconnected from OpenWrt router");
    }

    @Override
    public boolean isConnected() {
        boolean sessionConnected = (session != null && session.isConnected());
        if (connected != sessionConnected) {
            connected = sessionConnected;
            if (!connected && session != null) {
                log.warn("SSH session disconnected unexpectedly");
                session = null;
            }
        }
        return connected;
    }

    @Override
    public Map<String, Object> getNetworkSetting(String settingType) {
        long startTime = monitor.startOperation("getNetworkSetting");
        log.info("Getting network setting: {}", settingType);
        Map<String, Object> response = new HashMap<>();

        try {
            if (!isConnected()) {
                response.put("status", "error");
                response.put("message", "Not connected to router");
                response.put("code", "NOT_CONNECTED");
                response.put("timestamp", System.currentTimeMillis());
                monitor.endOperation("getNetworkSetting", startTime, false);
                return response;
            }

            Map<String, Object> setting = new HashMap<>();

            switch (settingType) {
                case "basic":
                    setting.putAll(executeUciCommand("network"));
                    break;
                case "dns":
                    setting.putAll(executeUciCommand("dhcp"));
                    break;
                case "dhcp":
                    setting.putAll(executeUciCommand("dhcp"));
                    break;
                case "wifi":
                    setting.putAll(executeUciCommand("wireless"));
                    break;
                default:
                    response.put("status", "error");
                    response.put("message", "Network setting type not supported: " + settingType);
                    response.put("code", "NETWORK_SETTING_TYPE_ERROR");
                    response.put("timestamp", System.currentTimeMillis());
                    monitor.endOperation("getNetworkSetting", startTime, false);
                    return response;
            }

            networkSettingsCache.put(settingType, setting);
            monitor.recordCacheUpdate();

            response.put("status", "success");
            response.put("message", "Network setting retrieved successfully");
            response.put("data", setting);
            response.put("timestamp", System.currentTimeMillis());

            monitor.endOperation("getNetworkSetting", startTime, true);

        } catch (Exception e) {
            log.error("Error getting network setting: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "NETWORK_SETTING_RETRIEVAL_ERROR");
            response.put("timestamp", System.currentTimeMillis());
            monitor.endOperation("getNetworkSetting", startTime, false);
        }

        return response;
    }

    private Map<String, Object> executeUciCommand(String config) throws Exception {
        int retries = 0;
        while (retries < maxUciRetries) {
            try {
                UciCommandAdapter adapter = getUciCommandAdapter();
                String command = adapter.getShowCommand(config);
                Map<String, Object> sshResult = executeSshCommand(command);

                boolean success = (boolean) sshResult.get("success");
                if (!success) {
                    String errorMessage = (String) sshResult.get("stderr");

                    if (isVersionSpecificError(errorMessage)) {
                        log.warn("Version specific error detected: {}", errorMessage);
                        adapter = new UciCommandAdapterDefault();
                        command = adapter.getShowCommand(config);
                        sshResult = executeSshCommand(command);
                        success = (boolean) sshResult.get("success");
                        if (!success) {
                            throw new ConfigException("Failed to execute uci command in compatibility mode: " + sshResult.get("stderr"));
                        }
                    } else {
                        throw new ConfigException("Failed to execute uci command: " + errorMessage);
                    }
                }

                String output = (String) sshResult.get("stdout");
                if (output != null && !output.isEmpty()) {
                    try {
                        return adapter.parseUciOutput(output);
                    } catch (Exception e) {
                        log.warn("Failed to parse uci output, using default parser: {}", e.getMessage());
                        return new UciCommandAdapterDefault().parseUciOutput(output);
                    }
                }

                return new HashMap<>();

            } catch (ConfigException e) {
                throw e;
            } catch (Exception e) {
                retries++;
                log.warn("UCI command execution failed (attempt {}/{}): {}", retries, maxUciRetries, e.getMessage());

                if (retries >= maxUciRetries) {
                    throw new ConfigException("Failed to execute uci command after " + maxUciRetries + " attempts: " + e.getMessage(), e);
                }

                log.info("Retrying UCI command in {}ms...", uciRetryInterval);
                Thread.sleep(uciRetryInterval);
            }
        }

        return new HashMap<>();
    }

    private boolean isVersionSpecificError(String errorMessage) {
        String[] versionSpecificErrors = {
            "unknown option",
            "invalid argument",
            "not found",
            "permission denied",
            "no such file or directory"
        };

        for (String errorPattern : versionSpecificErrors) {
            if (errorMessage.toLowerCase().contains(errorPattern)) {
                return true;
            }
        }

        return false;
    }

    private void setUciValue(String config, String section, String option, String value) throws Exception {
        int retries = 0;
        while (retries < maxUciRetries) {
            try {
                UciCommandAdapter adapter = getUciCommandAdapter();
                String command = adapter.getSetCommand(config, section, option, value);
                Map<String, Object> result = executeSshCommand(command);

                boolean success = (boolean) result.get("success");
                if (!success) {
                    String errorMessage = (String) result.get("stderr");

                    if (isVersionSpecificError(errorMessage)) {
                        log.warn("Version specific error detected: {}", errorMessage);
                        adapter = new UciCommandAdapterDefault();
                        command = adapter.getSetCommand(config, section, option, value);
                        result = executeSshCommand(command);
                        success = (boolean) result.get("success");
                        if (!success) {
                            throw new ConfigException("Failed to set uci value in compatibility mode: " + result.get("stderr"));
                        }
                    } else {
                        throw new ConfigException("Failed to set uci value: " + errorMessage);
                    }
                }

                return;

            } catch (ConfigException e) {
                throw e;
            } catch (Exception e) {
                retries++;
                log.warn("Set UCI value failed (attempt {}/{}): {}", retries, maxUciRetries, e.getMessage());

                if (retries >= maxUciRetries) {
                    throw new ConfigException("Failed to set uci value after " + maxUciRetries + " attempts: " + e.getMessage(), e);
                }

                Thread.sleep(uciRetryInterval);
            }
        }
    }

    private void commitUciConfig(String config) throws Exception {
        int retries = 0;
        while (retries < maxUciRetries) {
            try {
                UciCommandAdapter adapter = getUciCommandAdapter();
                String command = adapter.getCommitCommand(config);
                Map<String, Object> result = executeSshCommand(command);

                boolean success = (boolean) result.get("success");
                if (!success) {
                    String errorMessage = (String) result.get("stderr");
                    throw new ConfigException("Failed to commit uci config: " + errorMessage);
                }

                return;

            } catch (ConfigException e) {
                throw e;
            } catch (Exception e) {
                retries++;
                log.warn("Commit UCI config failed (attempt {}/{}): {}", retries, maxUciRetries, e.getMessage());

                if (retries >= maxUciRetries) {
                    throw new ConfigException("Failed to commit uci config after " + maxUciRetries + " attempts: " + e.getMessage(), e);
                }

                Thread.sleep(uciRetryInterval);
            }
        }
    }

    private void deleteUciConfig(String config, String section) throws Exception {
        int retries = 0;
        while (retries < maxUciRetries) {
            try {
                UciCommandAdapter adapter = getUciCommandAdapter();
                String command = adapter.getDeleteCommand(config, section);
                Map<String, Object> result = executeSshCommand(command);

                boolean success = (boolean) result.get("success");
                if (!success) {
                    String errorMessage = (String) result.get("stderr");
                    throw new ConfigException("Failed to delete uci config: " + errorMessage);
                }

                return;

            } catch (ConfigException e) {
                throw e;
            } catch (Exception e) {
                retries++;
                log.warn("Delete UCI config failed (attempt {}/{}): {}", retries, maxUciRetries, e.getMessage());

                if (retries >= maxUciRetries) {
                    throw new ConfigException("Failed to delete uci config after " + maxUciRetries + " attempts: " + e.getMessage(), e);
                }

                Thread.sleep(uciRetryInterval);
            }
        }
    }

    @Override
    public Map<String, Object> getAllNetworkSettings() {
        log.info("Getting all network settings");
        Map<String, Object> response = new HashMap<>();

        try {
            if (!isConnected()) {
                response.put("status", "error");
                response.put("message", "Not connected to router");
                response.put("code", "NOT_CONNECTED");
                response.put("timestamp", System.currentTimeMillis());
                return response;
            }

            response.put("status", "success");
            response.put("message", "All network settings retrieved successfully");
            response.put("data", networkSettingsCache);
            response.put("count", networkSettingsCache.size());
            response.put("timestamp", System.currentTimeMillis());

        } catch (Exception e) {
            log.error("Error getting all network settings: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "NETWORK_SETTINGS_RETRIEVAL_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        }

        return response;
    }

    @Override
    public Map<String, Object> updateNetworkSetting(String settingType, Map<String, Object> settingData) {
        long startTime = monitor.startOperation("updateNetworkSetting");
        log.info("Updating network setting: {}, data: {}", settingType, settingData);
        Map<String, Object> response = new HashMap<>();

        try {
            if (!isConnected()) {
                response.put("status", "error");
                response.put("message", "Not connected to router");
                response.put("code", "NOT_CONNECTED");
                response.put("timestamp", System.currentTimeMillis());
                monitor.endOperation("updateNetworkSetting", startTime, false);
                return response;
            }

            switch (settingType) {
                case "basic":
                    updateUciConfig("network", settingData);
                    break;
                case "dns":
                    updateUciConfig("dhcp", settingData);
                    break;
                case "dhcp":
                    updateUciConfig("dhcp", settingData);
                    break;
                case "wifi":
                    updateUciConfig("wireless", settingData);
                    break;
                default:
                    response.put("status", "error");
                    response.put("message", "Network setting type not supported: " + settingType);
                    response.put("code", "NETWORK_SETTING_TYPE_ERROR");
                    response.put("timestamp", System.currentTimeMillis());
                    monitor.endOperation("updateNetworkSetting", startTime, false);
                    return response;
            }

            Map<String, Object> updatedSetting = new HashMap<>();
            switch (settingType) {
                case "basic":
                    updatedSetting.putAll(executeUciCommand("network"));
                    break;
                case "dns":
                case "dhcp":
                    updatedSetting.putAll(executeUciCommand("dhcp"));
                    break;
                case "wifi":
                    updatedSetting.putAll(executeUciCommand("wireless"));
                    break;
            }

            networkSettingsCache.put(settingType, updatedSetting);
            monitor.recordCacheUpdate();

            response.put("status", "success");
            response.put("message", "Network setting updated successfully");
            response.put("data", updatedSetting);
            response.put("timestamp", System.currentTimeMillis());

            monitor.endOperation("updateNetworkSetting", startTime, true);

        } catch (Exception e) {
            log.error("Error updating network setting: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "NETWORK_SETTING_UPDATE_ERROR");
            response.put("timestamp", System.currentTimeMillis());
            monitor.endOperation("updateNetworkSetting", startTime, false);
        }

        return response;
    }

    private void updateUciConfig(String config, Map<String, Object> configData) throws Exception {
        for (Map.Entry<String, Object> entry : configData.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (!key.contains(".")) {
                continue;
            }

            String[] parts = key.split("\\.", 3);
            if (parts.length == 3) {
                String section = parts[1];
                String option = parts[2];
                String valueStr = value.toString();

                setUciValue(config, section, option, valueStr);
            }
        }

        commitUciConfig(config);
        restartService(config);
    }

    private void restartService(String config) throws Exception {
        String serviceCommand = "";

        switch (config) {
            case "network":
                serviceCommand = "/etc/init.d/network restart";
                break;
            case "dhcp":
                serviceCommand = "/etc/init.d/dnsmasq restart";
                break;
            case "wireless":
                serviceCommand = "/etc/init.d/network restart";
                break;
            default:
                return;
        }

        if (!serviceCommand.isEmpty()) {
            Map<String, Object> result = executeSshCommand(serviceCommand);
            if (!(boolean) result.get("success")) {
                log.warn("Failed to restart service: {}", result.get("stderr"));
            }
        }
    }

    @Override
    public Map<String, Object> getIPAddresses(String type, String status) {
        log.info("Getting IP addresses: type={}, status={}", type, status);
        Map<String, Object> response = new HashMap<>();

        try {
            if (!isConnected()) {
                response.put("status", "error");
                response.put("message", "Not connected to router");
                response.put("code", "NOT_CONNECTED");
                response.put("timestamp", System.currentTimeMillis());
                return response;
            }

            List<Map<String, Object>> staticIPs = getStaticIPAddresses();
            List<Map<String, Object>> dynamicIPs = getDynamicIPAddresses();

            List<Map<String, Object>> filteredIPs = new ArrayList<>();
            if ("static".equals(type) || type == null || type.isEmpty()) {
                filteredIPs.addAll(staticIPs);
            }
            if ("dynamic".equals(type) || type == null || type.isEmpty()) {
                filteredIPs.addAll(dynamicIPs);
            }

            Map<String, Object> stats = new HashMap<>();
            stats.put("total", staticIPs.size() + dynamicIPs.size());
            stats.put("static", staticIPs.size());
            stats.put("dynamic", dynamicIPs.size());
            stats.put("online", getOnlineDeviceCount());
            stats.put("offline", stats.get("total") instanceof Number ? ((Number) stats.get("total")).intValue() - getOnlineDeviceCount() : 0);

            response.put("status", "success");
            response.put("message", "IP addresses retrieved successfully");
            response.put("data", filteredIPs);
            response.put("stats", stats);
            response.put("timestamp", System.currentTimeMillis());

        } catch (Exception e) {
            log.error("Error getting IP addresses: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "IP_ADDRESSES_RETRIEVAL_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        }

        return response;
    }

    private List<Map<String, Object>> getStaticIPAddresses() throws Exception {
        List<Map<String, Object>> staticIPs = new ArrayList<>();
        Map<String, Object> dhcpConfig = executeUciCommand("dhcp");

        for (Map.Entry<String, Object> entry : dhcpConfig.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue().toString();

            if (key.startsWith("dhcp.@host[") && key.endsWith(".ip")) {
                String hostSection = key.substring(0, key.lastIndexOf(".ip"));
                String ip = value;

                String macKey = hostSection + ".mac";
                String nameKey = hostSection + ".name";

                String mac = dhcpConfig.containsKey(macKey) ? dhcpConfig.get(macKey).toString() : "";
                String name = dhcpConfig.containsKey(nameKey) ? dhcpConfig.get(nameKey).toString() : "";

                Map<String, Object> ipInfo = new HashMap<>();
                ipInfo.put("id", hostSection);
                ipInfo.put("ip", ip);
                ipInfo.put("mac", mac);
                ipInfo.put("name", name);
                ipInfo.put("type", "static");
                ipInfo.put("status", "online");

                staticIPs.add(ipInfo);
            }
        }

        return staticIPs;
    }

    private List<Map<String, Object>> getDynamicIPAddresses() throws Exception {
        List<Map<String, Object>> dynamicIPs = new ArrayList<>();

        String command = "cat /tmp/dhcp.leases";
        Map<String, Object> result = executeSshCommand(command);

        if ((boolean) result.get("success")) {
            String output = (String) result.get("stdout");
            String[] lines = output.split("\n");

            for (String line : lines) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.trim().split("\\s+");
                if (parts.length >= 4) {
                    Map<String, Object> ipInfo = new HashMap<>();
                    ipInfo.put("id", parts[1]);
                    ipInfo.put("ip", parts[2]);
                    ipInfo.put("mac", parts[1]);
                    ipInfo.put("name", parts.length > 3 ? parts[3] : "");
                    ipInfo.put("type", "dynamic");
                    ipInfo.put("status", "online");

                    dynamicIPs.add(ipInfo);
                }
            }
        }

        return dynamicIPs;
    }

    private int getOnlineDeviceCount() throws Exception {
        String command = "arp -n | grep -v 'incomplete' | wc -l";
        Map<String, Object> result = executeSshCommand(command);

        if ((boolean) result.get("success")) {
            String output = (String) result.get("stdout");
            try {
                return Integer.parseInt(output.trim());
            } catch (NumberFormatException e) {
                log.warn("Failed to parse online device count: {}", output);
            }
        }

        return 0;
    }

    @Override
    public Map<String, Object> addStaticIPAddress(Map<String, Object> ipData) {
        long startTime = monitor.startOperation("addStaticIPAddress");
        log.info("Adding static IP address: {}", ipData);
        Map<String, Object> response = new HashMap<>();

        try {
            if (!isConnected()) {
                response.put("status", "error");
                response.put("message", "Not connected to router");
                response.put("code", "NOT_CONNECTED");
                response.put("timestamp", System.currentTimeMillis());
                monitor.endOperation("addStaticIPAddress", startTime, false);
                return response;
            }

            if (!ipData.containsKey("ip") || !ipData.containsKey("mac")) {
                response.put("status", "error");
                response.put("message", "Missing required parameters: ip and mac");
                response.put("code", "MISSING_PARAMETERS");
                response.put("timestamp", System.currentTimeMillis());
                monitor.endOperation("addStaticIPAddress", startTime, false);
                return response;
            }

            String ip = ipData.get("ip").toString();
            String mac = ipData.get("mac").toString();
            String name = ipData.containsKey("name") ? ipData.get("name").toString() : "";

            int hostCount = getDhcpHostCount();

            String section = "@host[" + hostCount + "]";
            setUciValue("dhcp", section, "ip", ip);
            setUciValue("dhcp", section, "mac", mac);
            if (!name.isEmpty()) {
                setUciValue("dhcp", section, "name", name);
            }

            commitUciConfig("dhcp");
            restartService("dhcp");

            Map<String, Object> addedIP = new HashMap<>();
            addedIP.put("id", "dhcp." + section);
            addedIP.put("ip", ip);
            addedIP.put("mac", mac);
            addedIP.put("name", name);
            addedIP.put("type", "static");

            response.put("status", "success");
            response.put("message", "Static IP address added successfully");
            response.put("data", addedIP);
            response.put("timestamp", System.currentTimeMillis());

            monitor.endOperation("addStaticIPAddress", startTime, true);

        } catch (Exception e) {
            log.error("Error adding static IP address: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "IP_ADDRESS_ADD_ERROR");
            response.put("timestamp", System.currentTimeMillis());
            monitor.endOperation("addStaticIPAddress", startTime, false);
        }

        return response;
    }

    private int getDhcpHostCount() throws Exception {
        Map<String, Object> dhcpConfig = executeUciCommand("dhcp");
        int count = 0;

        for (String key : dhcpConfig.keySet()) {
            if (key.startsWith("dhcp.@host[")) {
                count++;
            }
        }

        return count;
    }

    @Override
    public Map<String, Object> deleteIPAddress(String ipId) {
        long startTime = monitor.startOperation("deleteIPAddress");
        log.info("Deleting IP address: {}", ipId);
        Map<String, Object> response = new HashMap<>();

        try {
            if (!isConnected()) {
                response.put("status", "error");
                response.put("message", "Not connected to router");
                response.put("code", "NOT_CONNECTED");
                response.put("timestamp", System.currentTimeMillis());
                monitor.endOperation("deleteIPAddress", startTime, false);
                return response;
            }

            String section = ipId;
            if (section.startsWith("dhcp.")) {
                section = section.substring(5);
            }

            String command = "uci delete dhcp." + section;
            Map<String, Object> result = executeSshCommand(command);

            if (!(boolean) result.get("success")) {
                throw new Exception("Failed to delete static IP: " + result.get("stderr"));
            }

            commitUciConfig("dhcp");
            restartService("dhcp");

            response.put("status", "success");
            response.put("message", "IP address deleted successfully");
            Map<String, Object> data = new HashMap<>();
            data.put("id", ipId);
            response.put("data", data);
            response.put("timestamp", System.currentTimeMillis());

            monitor.endOperation("deleteIPAddress", startTime, true);

        } catch (Exception e) {
            log.error("Error deleting IP address: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "IP_ADDRESS_DELETE_ERROR");
            response.put("timestamp", System.currentTimeMillis());
            monitor.endOperation("deleteIPAddress", startTime, false);
        }

        return response;
    }

    @Override
    public Map<String, Object> getIPBlacklist() {
        long startTime = monitor.startOperation("getIPBlacklist");
        log.info("Getting IP blacklist");
        Map<String, Object> response = new HashMap<>();

        try {
            if (!isConnected()) {
                response.put("status", "error");
                response.put("message", "Not connected to router");
                response.put("code", "NOT_CONNECTED");
                response.put("timestamp", System.currentTimeMillis());
                monitor.endOperation("getIPBlacklist", startTime, false);
                return response;
            }

            List<Map<String, Object>> blacklist = getBlacklistRules();

            response.put("status", "success");
            response.put("message", "IP blacklist retrieved successfully");
            response.put("data", blacklist);
            response.put("count", blacklist.size());
            response.put("timestamp", System.currentTimeMillis());

            monitor.endOperation("getIPBlacklist", startTime, true);

        } catch (Exception e) {
            log.error("Error getting IP blacklist: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "IP_BLACKLIST_RETRIEVAL_ERROR");
            response.put("timestamp", System.currentTimeMillis());
            monitor.endOperation("getIPBlacklist", startTime, false);
        }

        return response;
    }

    private List<Map<String, Object>> getBlacklistRules() throws Exception {
        List<Map<String, Object>> blacklist = new ArrayList<>();
        Map<String, Object> firewallConfig = executeUciCommand("firewall");

        for (Map.Entry<String, Object> entry : firewallConfig.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue().toString();

            if (key.startsWith("firewall.@rule[") && value.equals("REJECT")) {
                String ruleSection = key.substring(0, key.lastIndexOf(".target"));

                String srcKey = ruleSection + ".src";
                String srcIpKey = ruleSection + ".src_ip";
                String nameKey = ruleSection + ".name";
                String enabledKey = ruleSection + ".enabled";

                String src = firewallConfig.containsKey(srcKey) ? firewallConfig.get(srcKey).toString() : "";
                String srcIp = firewallConfig.containsKey(srcIpKey) ? firewallConfig.get(srcIpKey).toString() : "";
                String name = firewallConfig.containsKey(nameKey) ? firewallConfig.get(nameKey).toString() : "";
                String enabled = firewallConfig.containsKey(enabledKey) ? firewallConfig.get(enabledKey).toString() : "1";

                if (!srcIp.isEmpty() && "1".equals(enabled)) {
                    Map<String, Object> rule = new HashMap<>();
                    rule.put("id", ruleSection);
                    rule.put("ip", srcIp);
                    rule.put("name", name);
                    rule.put("source", src);
                    rule.put("enabled", "1".equals(enabled));
                    rule.put("created", System.currentTimeMillis());

                    blacklist.add(rule);
                }
            }
        }

        return blacklist;
    }

    @Override
    public Map<String, Object> addIPToBlacklist(Map<String, Object> blacklistData) {
        long startTime = monitor.startOperation("addIPToBlacklist");
        log.info("Adding IP to blacklist: {}", blacklistData);
        Map<String, Object> response = new HashMap<>();

        try {
            if (!isConnected()) {
                response.put("status", "error");
                response.put("message", "Not connected to router");
                response.put("code", "NOT_CONNECTED");
                response.put("timestamp", System.currentTimeMillis());
                monitor.endOperation("addIPToBlacklist", startTime, false);
                return response;
            }

            if (!blacklistData.containsKey("ip")) {
                response.put("status", "error");
                response.put("message", "Missing required parameter: ip");
                response.put("code", "MISSING_PARAMETERS");
                response.put("timestamp", System.currentTimeMillis());
                monitor.endOperation("addIPToBlacklist", startTime, false);
                return response;
            }

            String ip = blacklistData.get("ip").toString();
            String name = blacklistData.containsKey("name") ? blacklistData.get("name").toString() : "Blacklist-" + ip;

            int ruleCount = getFirewallRuleCount();

            String section = "@rule[" + ruleCount + "]";
            setUciValue("firewall", section, "name", name);
            setUciValue("firewall", section, "src", "*");
            setUciValue("firewall", section, "src_ip", ip);
            setUciValue("firewall", section, "dest", "*");
            setUciValue("firewall", section, "proto", "all");
            setUciValue("firewall", section, "target", "REJECT");
            setUciValue("firewall", section, "enabled", "1");

            commitUciConfig("firewall");
            restartFirewallService();

            Map<String, Object> addedRule = new HashMap<>();
            addedRule.put("id", "firewall." + section);
            addedRule.put("ip", ip);
            addedRule.put("name", name);
            addedRule.put("enabled", true);
            addedRule.put("created", System.currentTimeMillis());

            response.put("status", "success");
            response.put("message", "IP address added to blacklist successfully");
            response.put("data", addedRule);
            response.put("timestamp", System.currentTimeMillis());

            monitor.endOperation("addIPToBlacklist", startTime, true);

        } catch (Exception e) {
            log.error("Error adding IP to blacklist: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "IP_BLACKLIST_ADD_ERROR");
            response.put("timestamp", System.currentTimeMillis());
            monitor.endOperation("addIPToBlacklist", startTime, false);
        }

        return response;
    }

    private int getFirewallRuleCount() throws Exception {
        Map<String, Object> firewallConfig = executeUciCommand("firewall");
        int count = 0;

        for (String key : firewallConfig.keySet()) {
            if (key.startsWith("firewall.@rule[")) {
                count++;
            }
        }

        return count;
    }

    private void restartFirewallService() throws Exception {
        String command = "/etc/init.d/firewall restart";
        Map<String, Object> result = executeSshCommand(command);
        if (!(boolean) result.get("success")) {
            log.warn("Failed to restart firewall service: {}", result.get("stderr"));
        }
    }

    @Override
    public Map<String, Object> removeIPFromBlacklist(String blacklistId) {
        long startTime = monitor.startOperation("removeIPFromBlacklist");
        log.info("Removing IP from blacklist: {}", blacklistId);
        Map<String, Object> response = new HashMap<>();

        try {
            if (!isConnected()) {
                response.put("status", "error");
                response.put("message", "Not connected to router");
                response.put("code", "NOT_CONNECTED");
                response.put("timestamp", System.currentTimeMillis());
                monitor.endOperation("removeIPFromBlacklist", startTime, false);
                return response;
            }

            String section = blacklistId;
            if (section.startsWith("firewall.")) {
                section = section.substring(9);
            }

            String command = "uci delete firewall." + section;
            Map<String, Object> result = executeSshCommand(command);

            if (!(boolean) result.get("success")) {
                throw new Exception("Failed to delete blacklist rule: " + result.get("stderr"));
            }

            commitUciConfig("firewall");
            restartFirewallService();

            response.put("status", "success");
            response.put("message", "IP address removed from blacklist successfully");
            Map<String, Object> data = new HashMap<>();
            data.put("id", blacklistId);
            response.put("data", data);
            response.put("timestamp", System.currentTimeMillis());

            monitor.endOperation("removeIPFromBlacklist", startTime, true);

        } catch (Exception e) {
            log.error("Error removing IP from blacklist: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "IP_BLACKLIST_REMOVE_ERROR");
            response.put("timestamp", System.currentTimeMillis());
            monitor.endOperation("removeIPFromBlacklist", startTime, false);
        }

        return response;
    }

    @Override
    public Map<String, Object> getConfigFile(String configFile) {
        long startTime = monitor.startOperation("getConfigFile");
        log.info("Getting config file: {}", configFile);
        Map<String, Object> response = new HashMap<>();

        try {
            if (!isConnected()) {
                response.put("status", "error");
                response.put("message", "Not connected to router");
                response.put("code", "NOT_CONNECTED");
                response.put("timestamp", System.currentTimeMillis());
                monitor.endOperation("getConfigFile", startTime, false);
                return response;
            }

            String configPath = getConfigFilePath(configFile);
            String content = readFileContent(configPath);
            Map<String, Object> fileInfo = getFileInfo(configPath);

            Map<String, Object> configFileData = new HashMap<>();
            configFileData.put("name", configFile);
            configFileData.put("path", configPath);
            configFileData.put("content", content);
            configFileData.put("size", fileInfo.get("size"));
            configFileData.put("lastModified", fileInfo.get("lastModified"));
            configFileData.put("permissions", fileInfo.get("permissions"));

            response.put("status", "success");
            response.put("message", "Config file retrieved successfully");
            response.put("data", configFileData);
            response.put("timestamp", System.currentTimeMillis());

            monitor.endOperation("getConfigFile", startTime, true);

        } catch (Exception e) {
            log.error("Error getting config file: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "CONFIG_FILE_RETRIEVAL_ERROR");
            response.put("timestamp", System.currentTimeMillis());
            monitor.endOperation("getConfigFile", startTime, false);
        }

        return response;
    }

    @Override
    public Map<String, Object> updateConfigFile(String configFile, Map<String, Object> configData) {
        long startTime = monitor.startOperation("updateConfigFile");
        log.info("Updating config file: {}", configFile);
        Map<String, Object> response = new HashMap<>();

        try {
            if (!isConnected()) {
                response.put("status", "error");
                response.put("message", "Not connected to router");
                response.put("code", "NOT_CONNECTED");
                response.put("timestamp", System.currentTimeMillis());
                monitor.endOperation("updateConfigFile", startTime, false);
                return response;
            }

            if (!configData.containsKey("content")) {
                response.put("status", "error");
                response.put("message", "Missing required parameter: content");
                response.put("code", "MISSING_PARAMETERS");
                response.put("timestamp", System.currentTimeMillis());
                monitor.endOperation("updateConfigFile", startTime, false);
                return response;
            }

            String configPath = getConfigFilePath(configFile);
            backupFile(configPath);

            String content = configData.get("content").toString();
            writeFileContent(configPath, content);

            String updatedContent = readFileContent(configPath);
            boolean updateSuccessful = updatedContent.equals(content) || updatedContent.isEmpty();

            if (!updateSuccessful) {
                restoreFileBackup(configPath);
                throw new Exception("Failed to update config file: Content mismatch after write");
            }

            Map<String, Object> fileInfo = getFileInfo(configPath);

            Map<String, Object> updatedFileData = new HashMap<>();
            updatedFileData.put("name", configFile);
            updatedFileData.put("path", configPath);
            updatedFileData.put("size", fileInfo.get("size"));
            updatedFileData.put("lastModified", fileInfo.get("lastModified"));
            updatedFileData.put("updateSuccessful", true);

            response.put("status", "success");
            response.put("message", "Config file updated successfully");
            response.put("data", updatedFileData);
            response.put("timestamp", System.currentTimeMillis());

            monitor.endOperation("updateConfigFile", startTime, true);

        } catch (Exception e) {
            log.error("Error updating config file: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "CONFIG_FILE_UPDATE_ERROR");
            response.put("timestamp", System.currentTimeMillis());
            monitor.endOperation("updateConfigFile", startTime, false);
        }

        return response;
    }

    private String getConfigFilePath(String configFile) {
        Map<String, String> configPaths = new HashMap<>();
        configPaths.put("network", "/etc/config/network");
        configPaths.put("dhcp", "/etc/config/dhcp");
        configPaths.put("firewall", "/etc/config/firewall");
        configPaths.put("wireless", "/etc/config/wireless");
        configPaths.put("system", "/etc/config/system");
        configPaths.put("dropbear", "/etc/config/dropbear");
        configPaths.put("luci", "/etc/config/luci");

        if (configFile.startsWith("/")) {
            return configFile;
        }

        return configPaths.getOrDefault(configFile, "/etc/config/" + configFile);
    }

    private String readFileContent(String filePath) throws Exception {
        String command = "cat " + filePath;
        Map<String, Object> result = executeSshCommand(command);

        if (!(boolean) result.get("success")) {
            throw new Exception("Failed to read file: " + result.get("stderr"));
        }

        return (String) result.get("stdout");
    }

    private void writeFileContent(String filePath, String content) throws Exception {
        String escapedContent = content.replace("'", "'\\''");
        String command = "echo '" + escapedContent + "' > " + filePath;
        Map<String, Object> result = executeSshCommand(command);

        if (!(boolean) result.get("success")) {
            throw new Exception("Failed to write file: " + result.get("stderr"));
        }
    }

    private Map<String, Object> getFileInfo(String filePath) throws Exception {
        Map<String, Object> fileInfo = new HashMap<>();

        String sizeCommand = "stat -c %s " + filePath;
        Map<String, Object> sizeResult = executeSshCommand(sizeCommand);
        if ((boolean) sizeResult.get("success")) {
            try {
                fileInfo.put("size", Integer.parseInt(((String) sizeResult.get("stdout")).trim()));
            } catch (NumberFormatException e) {
                fileInfo.put("size", 0);
            }
        }

        String mtimeCommand = "stat -c %Y " + filePath;
        Map<String, Object> mtimeResult = executeSshCommand(mtimeCommand);
        if ((boolean) mtimeResult.get("success")) {
            try {
                fileInfo.put("lastModified", Long.parseLong(((String) mtimeResult.get("stdout")).trim()) * 1000);
            } catch (NumberFormatException e) {
                fileInfo.put("lastModified", System.currentTimeMillis());
            }
        }

        String permCommand = "stat -c %a " + filePath;
        Map<String, Object> permResult = executeSshCommand(permCommand);
        if ((boolean) permResult.get("success")) {
            fileInfo.put("permissions", ((String) permResult.get("stdout")).trim());
        }

        return fileInfo;
    }

    private void backupFile(String filePath) throws Exception {
        String backupPath = filePath + ".bak";
        String command = "cp " + filePath + " " + backupPath;
        Map<String, Object> result = executeSshCommand(command);

        if (!(boolean) result.get("success")) {
            log.warn("Failed to backup file: {}", result.get("stderr"));
        }
    }

    private void restoreFileBackup(String filePath) throws Exception {
        String backupPath = filePath + ".bak";
        String command = "cp " + backupPath + " " + filePath;
        Map<String, Object> result = executeSshCommand(command);

        if (!(boolean) result.get("success")) {
            log.warn("Failed to restore file backup: {}", result.get("stderr"));
        }
    }

    private Map<String, Object> executeSshCommand(String command) throws JSchException, IOException, InterruptedException, CommandExecuteException {
        int retries = 0;
        while (retries < maxCommandRetries) {
            Map<String, Object> result = new HashMap<>();
            ChannelExec channel = null;
            InputStream in = null;
            InputStream err = null;

            try {
                log.debug("Executing command (attempt {}/{}): {}", retries + 1, maxCommandRetries, command);

                if (!isConnected()) {
                    throw new ConnectionTimeoutException("Not connected to router");
                }

                channel = (ChannelExec) session.openChannel("exec");
                channel.setCommand(command);

                in = channel.getInputStream();
                err = channel.getErrStream();

                channel.connect(commandTimeout);

                StringBuilder stdout = new StringBuilder();
                BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
                String line;
                while ((line = stdoutReader.readLine()) != null) {
                    stdout.append(line).append("\n");
                }

                StringBuilder stderr = new StringBuilder();
                BufferedReader stderrReader = new BufferedReader(new InputStreamReader(err, StandardCharsets.UTF_8));
                while ((line = stderrReader.readLine()) != null) {
                    stderr.append(line).append("\n");
                }

                long startTime = System.currentTimeMillis();
                while (!channel.isClosed()) {
                    Thread.sleep(100);
                    if (System.currentTimeMillis() - startTime > commandTimeout) {
                        throw new CommandExecuteException("Command execution timeout after " + commandTimeout + "ms");
                    }
                }

                int exitCode = channel.getExitStatus();

                result.put("stdout", stdout.toString().trim());
                result.put("stderr", stderr.toString().trim());
                result.put("exitCode", exitCode);
                result.put("success", exitCode == 0);

                if (exitCode == 0) {
                    log.debug("Command executed successfully");
                    return result;
                } else {
                    retries++;
                    log.warn("Command execution failed (exit code {}), attempt {}/{}", exitCode, retries, maxCommandRetries);
                    log.debug("Command stderr: {}", stderr.toString().trim());

                    if (retries >= maxCommandRetries) {
                        String errorMessage = String.format("Command execution failed after %d attempts: %s", maxCommandRetries, stderr.toString().trim());
                        throw new CommandExecuteException(errorMessage, exitCode);
                    }

                    log.info("Retrying command in {}ms...", commandRetryInterval);
                    Thread.sleep(commandRetryInterval);
                }

            } catch (CommandExecuteException e) {
                throw e;
            } catch (Exception e) {
                retries++;
                log.warn("Command execution error (attempt {}/{}): {}", retries, maxCommandRetries, e.getMessage());

                if (retries >= maxCommandRetries) {
                    throw new CommandExecuteException("Command execution failed after " + maxCommandRetries + " attempts: " + e.getMessage(), e);
                }

                Thread.sleep(commandRetryInterval);
            } finally {
                if (in != null) {
                    try { in.close(); } catch (Exception e) {}
                }
                if (err != null) {
                    try { err.close(); } catch (Exception e) {}
                }
                if (channel != null && channel.isConnected()) {
                    try { channel.disconnect(); } catch (Exception e) {}
                }
            }
        }

        throw new CommandExecuteException("Command execution failed after maximum retries");
    }

    private void detectOpenWrtVersion() {
        log.info("Detecting OpenWrt version");
        try {
            String[] versionCommands = {
                "cat /etc/openwrt_version",
                "cat /etc/os-release",
                "uname -a",
                "uci show system.@system[0].version"
            };

            for (String command : versionCommands) {
                try {
                    Map<String, Object> result = executeSshCommand(command);
                    boolean success = (boolean) result.get("success");
                    if (success) {
                        String output = (String) result.get("stdout");
                        if (!output.isEmpty()) {
                            openWrtVersion = parseVersionString(output);
                            if (openWrtVersion != null) {
                                versionDetected = true;
                                log.info("Detected OpenWrt version: {}", openWrtVersion);
                                return;
                            }
                        }
                    }
                } catch (Exception e) {
                    log.debug("Version detection command failed: {}", e.getMessage());
                }
            }

            openWrtVersion = "Unknown";
            versionDetected = false;
            log.warn("Failed to detect OpenWrt version");

        } catch (Exception e) {
            log.warn("Failed to detect OpenWrt version: {}", e.getMessage());
            versionDetected = false;
            openWrtVersion = "Unknown";
        }
    }

    private String parseVersionString(String versionString) {
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("(\\d{2}\\.\\d{2}(\\.\\d+)?)");
        java.util.regex.Matcher matcher = pattern.matcher(versionString);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private UciCommandAdapter getUciCommandAdapter() {
        if (!versionDetected) {
            detectOpenWrtVersion();
        }

        if (openWrtVersion != null && !openWrtVersion.equals("Unknown")) {
            if (openWrtVersion.startsWith("23.")) {
                return new UciCommandAdapterV23();
            } else if (openWrtVersion.startsWith("22.")) {
                return new UciCommandAdapterV22();
            } else if (openWrtVersion.startsWith("21.")) {
                return new UciCommandAdapterV21();
            } else if (openWrtVersion.startsWith("19.")) {
                return new UciCommandAdapterV19();
            }
        }

        return new UciCommandAdapterDefault();
    }

    private interface UciCommandAdapter {
        String getShowCommand(String config);
        String getSetCommand(String config, String section, String option, String value);
        String getDeleteCommand(String config, String section);
        String getCommitCommand(String config);
        String getRestartCommand(String service);
        Map<String, Object> parseUciOutput(String output);
    }

    private class UciCommandAdapterDefault implements UciCommandAdapter {
        @Override
        public String getShowCommand(String config) {
            return "uci show " + config;
        }

        @Override
        public String getSetCommand(String config, String section, String option, String value) {
            return String.format("uci set %s.%s.%s='%s'", config, section, option, value);
        }

        @Override
        public String getDeleteCommand(String config, String section) {
            return "uci delete " + config + "." + section;
        }

        @Override
        public String getCommitCommand(String config) {
            return "uci commit " + config;
        }

        @Override
        public String getRestartCommand(String service) {
            return "/etc/init.d/" + service + " restart";
        }

        @Override
        public Map<String, Object> parseUciOutput(String output) {
            Map<String, Object> result = new HashMap<>();
            String[] lines = output.split("\n");
            for (String line : lines) {
                if (line.contains("=") && !line.startsWith("#")) {
                    String[] parts = line.split("=", 2);
                    if (parts.length == 2) {
                        String key = parts[0].trim();
                        String value = parts[1].trim();
                        result.put(key, value);
                    }
                }
            }
            return result;
        }
    }

    private class UciCommandAdapterV23 implements UciCommandAdapter {
        @Override
        public String getShowCommand(String config) {
            return "uci show " + config;
        }

        @Override
        public String getSetCommand(String config, String section, String option, String value) {
            return String.format("uci set %s.%s.%s='%s'", config, section, option, value);
        }

        @Override
        public String getDeleteCommand(String config, String section) {
            return "uci delete " + config + "." + section;
        }

        @Override
        public String getCommitCommand(String config) {
            return "uci commit " + config;
        }

        @Override
        public String getRestartCommand(String service) {
            return "/etc/init.d/" + service + " restart";
        }

        @Override
        public Map<String, Object> parseUciOutput(String output) {
            return new UciCommandAdapterDefault().parseUciOutput(output);
        }
    }

    private class UciCommandAdapterV22 implements UciCommandAdapter {
        @Override
        public String getShowCommand(String config) {
            return "uci show " + config;
        }

        @Override
        public String getSetCommand(String config, String section, String option, String value) {
            return String.format("uci set %s.%s.%s='%s'", config, section, option, value);
        }

        @Override
        public String getDeleteCommand(String config, String section) {
            return "uci delete " + config + "." + section;
        }

        @Override
        public String getCommitCommand(String config) {
            return "uci commit " + config;
        }

        @Override
        public String getRestartCommand(String service) {
            return "/etc/init.d/" + service + " restart";
        }

        @Override
        public Map<String, Object> parseUciOutput(String output) {
            return new UciCommandAdapterDefault().parseUciOutput(output);
        }
    }

    private class UciCommandAdapterV21 implements UciCommandAdapter {
        @Override
        public String getShowCommand(String config) {
            return "uci show " + config;
        }

        @Override
        public String getSetCommand(String config, String section, String option, String value) {
            return String.format("uci set %s.%s.%s='%s'", config, section, option, value);
        }

        @Override
        public String getDeleteCommand(String config, String section) {
            return "uci delete " + config + "." + section;
        }

        @Override
        public String getCommitCommand(String config) {
            return "uci commit " + config;
        }

        @Override
        public String getRestartCommand(String service) {
            return "/etc/init.d/" + service + " restart";
        }

        @Override
        public Map<String, Object> parseUciOutput(String output) {
            return new UciCommandAdapterDefault().parseUciOutput(output);
        }
    }

    private class UciCommandAdapterV19 implements UciCommandAdapter {
        @Override
        public String getShowCommand(String config) {
            return "uci show " + config;
        }

        @Override
        public String getSetCommand(String config, String section, String option, String value) {
            return String.format("uci set %s.%s.%s='%s'", config, section, option, value);
        }

        @Override
        public String getDeleteCommand(String config, String section) {
            return "uci delete " + config + "." + section;
        }

        @Override
        public String getCommitCommand(String config) {
            return "uci commit " + config;
        }

        @Override
        public String getRestartCommand(String service) {
            return "/etc/init.d/" + service + " restart";
        }

        @Override
        public Map<String, Object> parseUciOutput(String output) {
            return new UciCommandAdapterDefault().parseUciOutput(output);
        }
    }

    @Override
    public Map<String, Object> executeCommand(String command) {
        log.info("Executing command on OpenWrt router: {}", command);
        Map<String, Object> response = new HashMap<>();

        try {
            if (!isConnected()) {
                response.put("status", "error");
                response.put("message", "Not connected to router");
                response.put("code", "NOT_CONNECTED");
                response.put("timestamp", System.currentTimeMillis());
                return response;
            }

            Map<String, Object> commandResult = executeSshCommand(command);

            boolean success = (boolean) commandResult.get("success");
            if (success) {
                response.put("status", "success");
                response.put("message", "Command executed successfully");
                response.put("data", commandResult);
            } else {
                response.put("status", "error");
                response.put("message", "Command execution failed");
                response.put("data", commandResult);
                response.put("code", "COMMAND_EXECUTION_ERROR");
            }
            response.put("timestamp", System.currentTimeMillis());

        } catch (ConnectionTimeoutException e) {
            log.error("Connection timeout error executing command: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", "Connection timeout: " + e.getMessage());
            response.put("code", "CONNECTION_TIMEOUT_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        } catch (CommandExecuteException e) {
            log.error("Command execution error: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", "Command execution failed: " + e.getMessage());
            response.put("code", "COMMAND_EXECUTION_ERROR");
            Map<String, Object> data = new HashMap<>();
            data.put("exitCode", e.getExitCode());
            response.put("data", data);
            response.put("timestamp", System.currentTimeMillis());
        } catch (JSchException e) {
            log.error("SSH error executing command: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", "SSH connection error: " + e.getMessage());
            response.put("code", "SSH_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        } catch (IOException e) {
            log.error("IO error executing command: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", "IO error: " + e.getMessage());
            response.put("code", "IO_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        } catch (InterruptedException e) {
            log.error("Command execution interrupted: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", "Command execution interrupted: " + e.getMessage());
            response.put("code", "INTERRUPTED_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        } catch (Exception e) {
            log.error("Error executing command: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "COMMAND_EXECUTION_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        }

        return response;
    }

    @Override
    public Map<String, Object> reboot() {
        long startTime = monitor.startOperation("reboot");
        log.info("Rebooting OpenWrt router: {}", host);
        Map<String, Object> response = new HashMap<>();

        try {
            if (!isConnected()) {
                response.put("status", "error");
                response.put("message", "Not connected to router");
                response.put("code", "NOT_CONNECTED");
                response.put("timestamp", System.currentTimeMillis());
                monitor.endOperation("reboot", startTime, false);
                return response;
            }

            String command = "/sbin/reboot";
            log.info("Executing reboot command: {}", command);

            ChannelExec channel = null;
            try {
                channel = (ChannelExec) session.openChannel("exec");
                channel.setCommand(command);
                channel.connect(5000);

                Thread.sleep(2000);

            } catch (Exception e) {
                log.info("Connection closed after reboot command (expected behavior)");
            } finally {
                if (channel != null && channel.isConnected()) {
                    try { channel.disconnect(); } catch (Exception e) {}
                }
            }

            disconnect();

            Map<String, Object> rebootInfo = new HashMap<>();
            rebootInfo.put("router", host);
            rebootInfo.put("rebootInitiated", true);
            rebootInfo.put("expectedDowntime", "2-3 minutes");
            rebootInfo.put("reconnectAfter", System.currentTimeMillis() + 180000);

            response.put("status", "success");
            response.put("message", "Router reboot initiated successfully");
            response.put("data", rebootInfo);
            response.put("timestamp", System.currentTimeMillis());

            monitor.endOperation("reboot", startTime, true);

        } catch (Exception e) {
            log.error("Error rebooting router: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "REBOOT_ERROR");
            response.put("timestamp", System.currentTimeMillis());
            monitor.endOperation("reboot", startTime, false);
        }

        return response;
    }

    public Map<String, Object> waitForRebootAndReconnect(long maxWaitTime, long retryInterval) {
        log.info("Waiting for router to reboot and reconnecting...");
        Map<String, Object> response = new HashMap<>();

        long startTime = System.currentTimeMillis();
        int attempt = 0;

        while (System.currentTimeMillis() - startTime < maxWaitTime) {
            attempt++;
            log.info("Reconnection attempt {}", attempt);

            try {
                boolean reconnected = connect(host, username, password);
                if (reconnected) {
                    log.info("Successfully reconnected to router after reboot");

                    Map<String, Object> reconnectInfo = new HashMap<>();
                    reconnectInfo.put("router", host);
                    reconnectInfo.put("reconnected", true);
                    reconnectInfo.put("attempts", attempt);
                    reconnectInfo.put("timeTaken", System.currentTimeMillis() - startTime);

                    response.put("status", "success");
                    response.put("message", "Successfully reconnected to router after reboot");
                    response.put("data", reconnectInfo);
                    response.put("timestamp", System.currentTimeMillis());

                    return response;
                }
            } catch (Exception e) {
                log.warn("Reconnection attempt {} failed: {}", attempt, e.getMessage());
            }

            try {
                Thread.sleep(retryInterval);
            } catch (InterruptedException e) {
                log.warn("Reconnection wait interrupted", e);
                Thread.currentThread().interrupt();
                break;
            }
        }

        log.error("Failed to reconnect to router after {}ms", maxWaitTime);
        response.put("status", "error");
        response.put("message", "Failed to reconnect to router after reboot");
        response.put("code", "RECONNECT_ERROR");
        Map<String, Object> data = new HashMap<>();
        data.put("router", host);
        data.put("attempts", attempt);
        data.put("maxWaitTime", maxWaitTime);
        response.put("data", data);
        response.put("timestamp", System.currentTimeMillis());

        return response;
    }

    @Override
    public Map<String, Object> getSystemStatus() {
        log.info("Getting system status from OpenWrt router: {}", host);
        Map<String, Object> response = new HashMap<>();

        try {
            if (!isConnected()) {
                response.put("status", "error");
                response.put("message", "Not connected to router");
                response.put("code", "NOT_CONNECTED");
                response.put("timestamp", System.currentTimeMillis());
                return response;
            }

            Map<String, Object> status = new HashMap<>();

            Map<String, Object> uptimeResult = executeSshCommand("uptime");
            if ((boolean) uptimeResult.get("success")) {
                status.put("uptime", uptimeResult.get("stdout"));
            }

            Map<String, Object> loadResult = executeSshCommand("cat /proc/loadavg");
            if ((boolean) loadResult.get("success")) {
                status.put("loadAverage", loadResult.get("stdout"));
            }

            Map<String, Object> memResult = executeSshCommand("free -m");
            if ((boolean) memResult.get("success")) {
                status.put("memory", memResult.get("stdout"));
            }

            Map<String, Object> cpuResult = executeSshCommand("top -bn1 | head -n 5");
            if ((boolean) cpuResult.get("success")) {
                status.put("cpuUsage", cpuResult.get("stdout"));
            }

            status.put("firmwareVersion", openWrtVersion != null ? openWrtVersion : "Unknown");

            response.put("status", "success");
            response.put("message", "System status retrieved successfully");
            response.put("data", status);
            response.put("timestamp", System.currentTimeMillis());

        } catch (Exception e) {
            log.error("Error getting system status: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "SYSTEM_STATUS_RETRIEVAL_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        }

        return response;
    }

    @Override
    public Map<String, Object> getVersionInfo() {
        log.info("Getting version information");
        Map<String, Object> response = new HashMap<>();

        try {
            if (!isConnected()) {
                response.put("status", "error");
                response.put("message", "Not connected to router");
                response.put("code", "NOT_CONNECTED");
                response.put("timestamp", System.currentTimeMillis());
                return response;
            }

            if (!versionDetected) {
                detectOpenWrtVersion();
            }

            Map<String, Object> versionInfo = new HashMap<>();
            versionInfo.put("openWrtVersion", openWrtVersion != null ? openWrtVersion : "Unknown");
            versionInfo.put("versionDetected", versionDetected);
            versionInfo.put("isSupported", isVersionSupported(openWrtVersion));
            versionInfo.put("detectionTime", System.currentTimeMillis());

            Map<String, Object> unameResult = executeSshCommand("uname -a");
            if ((boolean) unameResult.get("success")) {
                versionInfo.put("kernelVersion", unameResult.get("stdout"));
            }

            Map<String, Object> modelResult = executeSshCommand("cat /etc/board.json | grep -E 'model|vendor'");
            if ((boolean) modelResult.get("success")) {
                versionInfo.put("deviceInfo", modelResult.get("stdout"));
            }

            response.put("status", "success");
            response.put("message", "Version information retrieved successfully");
            response.put("data", versionInfo);
            response.put("timestamp", System.currentTimeMillis());

        } catch (Exception e) {
            log.error("Error getting version information: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "VERSION_INFO_RETRIEVAL_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        }

        return response;
    }

    @Override
    public boolean isVersionSupported(String version) {
        log.info("Checking if version is supported: {}", version);

        if (version == null || version.isEmpty()) {
            return false;
        }

        List<String> supportedVersions = getSupportedVersionList();

        for (String supportedVersion : supportedVersions) {
            if (version.startsWith(supportedVersion)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Map<String, Object> getSupportedVersions() {
        log.info("Getting supported versions");
        Map<String, Object> response = new HashMap<>();

        try {
            List<String> supportedVersions = getSupportedVersionList();
            Map<String, Object> versionInfo = new HashMap<>();
            versionInfo.put("versions", supportedVersions);
            versionInfo.put("count", supportedVersions.size());
            versionInfo.put("latestVersion", supportedVersions.get(0));
            versionInfo.put("minimumVersion", supportedVersions.get(supportedVersions.size() - 1));

            response.put("status", "success");
            response.put("message", "Supported versions retrieved successfully");
            response.put("data", versionInfo);
            response.put("timestamp", System.currentTimeMillis());

        } catch (Exception e) {
            log.error("Error getting supported versions: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "SUPPORTED_VERSIONS_RETRIEVAL_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        }

        return response;
    }

    private List<String> getSupportedVersionList() {
        List<String> supportedVersions = new ArrayList<>();
        supportedVersions.add("23.05");
        supportedVersions.add("22.03");
        supportedVersions.add("21.02");
        supportedVersions.add("19.07");
        return supportedVersions;
    }

    @Override
    public Map<String, Object> batchUpdateNetworkSettings(Map<String, Map<String, Object>> settingsData) {
        log.info("Batch updating network settings: {}", settingsData.size());
        Map<String, Object> response = new HashMap<>();

        try {
            if (!isConnected()) {
                response.put("status", "error");
                response.put("message", "Not connected to router");
                response.put("code", "NOT_CONNECTED");
                response.put("timestamp", System.currentTimeMillis());
                return response;
            }

            Map<String, Object> results = new HashMap<>();
            int successCount = 0;
            int errorCount = 0;

            for (Map.Entry<String, Map<String, Object>> entry : settingsData.entrySet()) {
                String settingType = entry.getKey();
                Map<String, Object> settingData = entry.getValue();

                try {
                    Map<String, Object> result = updateNetworkSetting(settingType, settingData);
                    results.put(settingType, result);

                    if ("success".equals(result.get("status"))) {
                        successCount++;
                    } else {
                        errorCount++;
                    }
                } catch (Exception e) {
                    log.error("Error updating setting type {}: {}", settingType, e.getMessage());
                    Map<String, Object> errorResult = new HashMap<>();
                    errorResult.put("status", "error");
                    errorResult.put("message", e.getMessage());
                    results.put(settingType, errorResult);
                    errorCount++;
                }
            }

            Map<String, Object> summary = new HashMap<>();
            summary.put("total", settingsData.size());
            summary.put("success", successCount);
            summary.put("error", errorCount);
            summary.put("successRate", settingsData.size() > 0 ? (double) successCount / settingsData.size() * 100 : 0);

            response.put("status", errorCount == 0 ? "success" : "partial_success");
            response.put("message", String.format("Batch update completed: %d success, %d error", successCount, errorCount));
            response.put("data", results);
            response.put("summary", summary);
            response.put("timestamp", System.currentTimeMillis());

        } catch (Exception e) {
            log.error("Error in batch update network settings: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "BATCH_UPDATE_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        }

        return response;
    }

    @Override
    public Map<String, Object> batchAddStaticIPAddresses(List<Map<String, Object>> ipDataList) {
        log.info("Batch adding static IP addresses: {}", ipDataList.size());
        Map<String, Object> response = new HashMap<>();

        try {
            if (!isConnected()) {
                response.put("status", "error");
                response.put("message", "Not connected to router");
                response.put("code", "NOT_CONNECTED");
                response.put("timestamp", System.currentTimeMillis());
                return response;
            }

            List<Map<String, Object>> results = new ArrayList<>();
            int successCount = 0;
            int errorCount = 0;

            for (Map<String, Object> ipData : ipDataList) {
                try {
                    Map<String, Object> result = addStaticIPAddress(ipData);
                    results.add(result);

                    if ("success".equals(result.get("status"))) {
                        successCount++;
                    } else {
                        errorCount++;
                    }
                } catch (Exception e) {
                    log.error("Error adding static IP address: {}", e.getMessage());
                    Map<String, Object> errorResult = new HashMap<>();
                    errorResult.put("status", "error");
                    errorResult.put("message", e.getMessage());
                    errorResult.put("data", ipData);
                    results.add(errorResult);
                    errorCount++;
                }
            }

            Map<String, Object> summary = new HashMap<>();
            summary.put("total", ipDataList.size());
            summary.put("success", successCount);
            summary.put("error", errorCount);
            summary.put("successRate", ipDataList.size() > 0 ? (double) successCount / ipDataList.size() * 100 : 0);

            response.put("status", errorCount == 0 ? "success" : "partial_success");
            response.put("message", String.format("Batch add completed: %d success, %d error", successCount, errorCount));
            response.put("data", results);
            response.put("summary", summary);
            response.put("timestamp", System.currentTimeMillis());

        } catch (Exception e) {
            log.error("Error in batch add static IP addresses: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "BATCH_ADD_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        }

        return response;
    }

    @Override
    public Map<String, Object> batchDeleteIPAddresses(List<String> ipIds) {
        log.info("Batch deleting IP addresses: {}", ipIds.size());
        Map<String, Object> response = new HashMap<>();

        try {
            if (!isConnected()) {
                response.put("status", "error");
                response.put("message", "Not connected to router");
                response.put("code", "NOT_CONNECTED");
                response.put("timestamp", System.currentTimeMillis());
                return response;
            }

            Map<String, Object> results = new HashMap<>();
            int successCount = 0;
            int errorCount = 0;

            for (String ipId : ipIds) {
                try {
                    Map<String, Object> result = deleteIPAddress(ipId);
                    results.put(ipId, result);

                    if ("success".equals(result.get("status"))) {
                        successCount++;
                    } else {
                        errorCount++;
                    }
                } catch (Exception e) {
                    log.error("Error deleting IP address {}: {}", ipId, e.getMessage());
                    Map<String, Object> errorResult = new HashMap<>();
                    errorResult.put("status", "error");
                    errorResult.put("message", e.getMessage());
                    results.put(ipId, errorResult);
                    errorCount++;
                }
            }

            Map<String, Object> summary = new HashMap<>();
            summary.put("total", ipIds.size());
            summary.put("success", successCount);
            summary.put("error", errorCount);
            summary.put("successRate", ipIds.size() > 0 ? (double) successCount / ipIds.size() * 100 : 0);

            response.put("status", errorCount == 0 ? "success" : "partial_success");
            response.put("message", String.format("Batch delete completed: %d success, %d error", successCount, errorCount));
            response.put("data", results);
            response.put("summary", summary);
            response.put("timestamp", System.currentTimeMillis());

        } catch (Exception e) {
            log.error("Error in batch delete IP addresses: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "BATCH_DELETE_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        }

        return response;
    }

    @Override
    public Map<String, Object> batchAddIPToBlacklist(List<Map<String, Object>> blacklistDataList) {
        log.info("Batch adding IP to blacklist: {}", blacklistDataList.size());
        Map<String, Object> response = new HashMap<>();

        try {
            if (!isConnected()) {
                response.put("status", "error");
                response.put("message", "Not connected to router");
                response.put("code", "NOT_CONNECTED");
                response.put("timestamp", System.currentTimeMillis());
                return response;
            }

            List<Map<String, Object>> results = new ArrayList<>();
            int successCount = 0;
            int errorCount = 0;

            for (Map<String, Object> blacklistData : blacklistDataList) {
                try {
                    Map<String, Object> result = addIPToBlacklist(blacklistData);
                    results.add(result);

                    if ("success".equals(result.get("status"))) {
                        successCount++;
                    } else {
                        errorCount++;
                    }
                } catch (Exception e) {
                    log.error("Error adding IP to blacklist: {}", e.getMessage());
                    Map<String, Object> errorResult = new HashMap<>();
                    errorResult.put("status", "error");
                    errorResult.put("message", e.getMessage());
                    errorResult.put("data", blacklistData);
                    results.add(errorResult);
                    errorCount++;
                }
            }

            Map<String, Object> summary = new HashMap<>();
            summary.put("total", blacklistDataList.size());
            summary.put("success", successCount);
            summary.put("error", errorCount);
            summary.put("successRate", blacklistDataList.size() > 0 ? (double) successCount / blacklistDataList.size() * 100 : 0);

            response.put("status", errorCount == 0 ? "success" : "partial_success");
            response.put("message", String.format("Batch add to blacklist completed: %d success, %d error", successCount, errorCount));
            response.put("data", results);
            response.put("summary", summary);
            response.put("timestamp", System.currentTimeMillis());

        } catch (Exception e) {
            log.error("Error in batch add IP to blacklist: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "BATCH_ADD_BLACKLIST_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        }

        return response;
    }

    @Override
    public Map<String, Object> batchRemoveIPFromBlacklist(List<String> blacklistIds) {
        log.info("Batch removing IP from blacklist: {}", blacklistIds.size());
        Map<String, Object> response = new HashMap<>();

        try {
            if (!isConnected()) {
                response.put("status", "error");
                response.put("message", "Not connected to router");
                response.put("code", "NOT_CONNECTED");
                response.put("timestamp", System.currentTimeMillis());
                return response;
            }

            Map<String, Object> results = new HashMap<>();
            int successCount = 0;
            int errorCount = 0;

            for (String blacklistId : blacklistIds) {
                try {
                    Map<String, Object> result = removeIPFromBlacklist(blacklistId);
                    results.put(blacklistId, result);

                    if ("success".equals(result.get("status"))) {
                        successCount++;
                    } else {
                        errorCount++;
                    }
                } catch (Exception e) {
                    log.error("Error removing IP from blacklist {}: {}", blacklistId, e.getMessage());
                    Map<String, Object> errorResult = new HashMap<>();
                    errorResult.put("status", "error");
                    errorResult.put("message", e.getMessage());
                    results.put(blacklistId, errorResult);
                    errorCount++;
                }
            }

            Map<String, Object> summary = new HashMap<>();
            summary.put("total", blacklistIds.size());
            summary.put("success", successCount);
            summary.put("error", errorCount);
            summary.put("successRate", blacklistIds.size() > 0 ? (double) successCount / blacklistIds.size() * 100 : 0);

            response.put("status", errorCount == 0 ? "success" : "partial_success");
            response.put("message", String.format("Batch remove from blacklist completed: %d success, %d error", successCount, errorCount));
            response.put("data", results);
            response.put("summary", summary);
            response.put("timestamp", System.currentTimeMillis());

        } catch (Exception e) {
            log.error("Error in batch remove IP from blacklist: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "BATCH_REMOVE_BLACKLIST_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        }

        return response;
    }
}
