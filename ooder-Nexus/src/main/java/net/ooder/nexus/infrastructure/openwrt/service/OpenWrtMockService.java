package net.ooder.nexus.infrastructure.openwrt.service;

import net.ooder.nexus.infrastructure.openwrt.bridge.OpenWrtBridge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * OpenWrt Mock 数据服务
 * 提供模拟的 OpenWrt 路由器数据，用于前端开发和测试
 */
@Service
public class OpenWrtMockService implements OpenWrtBridge {

    private static final Logger log = LoggerFactory.getLogger(OpenWrtMockService.class);

    private boolean connected = false;
    private final Map<String, Map<String, Object>> networkSettingsCache = new ConcurrentHashMap<>();
    private final List<Map<String, Object>> ipAddresses = new ArrayList<>();
    private final List<Map<String, Object>> ipBlacklist = new ArrayList<>();
    private final Map<String, String> configFiles = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        log.info("Initializing OpenWrt Mock Service");
        initializeMockData();
        log.info("OpenWrt Mock Service initialized");
    }

    private void initializeMockData() {
        // 初始化网络设置
        initializeNetworkSettings();
        // 初始化 IP 地址
        initializeIPAddresses();
        // 初始化黑名单
        initializeBlacklist();
        // 初始化配置文件
        initializeConfigFiles();
    }

    private void initializeNetworkSettings() {
        Map<String, Object> basicSettings = new HashMap<>();
        basicSettings.put("networkName", "Home Network");
        basicSettings.put("domainName", "home.local");
        basicSettings.put("timezone", "Asia/Shanghai");
        basicSettings.put("ntpServer", "pool.ntp.org");
        basicSettings.put("interfaceSpeed", "auto");
        networkSettingsCache.put("basic", basicSettings);

        Map<String, Object> dnsSettings = new HashMap<>();
        dnsSettings.put("primaryDns", "8.8.8.8");
        dnsSettings.put("secondaryDns", "8.8.4.4");
        dnsSettings.put("dnsSuffix", "home.local");
        dnsSettings.put("dnsCacheEnabled", true);
        dnsSettings.put("dnsCacheSize", 1000);
        networkSettingsCache.put("dns", dnsSettings);

        Map<String, Object> dhcpSettings = new HashMap<>();
        dhcpSettings.put("dhcpEnabled", true);
        dhcpSettings.put("startIp", "192.168.1.100");
        dhcpSettings.put("endIp", "192.168.1.200");
        dhcpSettings.put("leaseTime", 86400);
        dhcpSettings.put("gateway", "192.168.1.1");
        dhcpSettings.put("subnetMask", "255.255.255.0");
        networkSettingsCache.put("dhcp", dhcpSettings);

        Map<String, Object> wifiSettings = new HashMap<>();
        wifiSettings.put("ssid", "HomeWiFi");
        wifiSettings.put("channel", 6);
        wifiSettings.put("mode", "802.11ac");
        wifiSettings.put("security", "WPA2-PSK");
        wifiSettings.put("password", "********");
        wifiSettings.put("txPower", "auto");
        wifiSettings.put("bandwidth", "80MHz");
        wifiSettings.put("hidden", false);
        networkSettingsCache.put("wifi", wifiSettings);
    }

    private void initializeIPAddresses() {
        // 静态 IP
        ipAddresses.add(createIPRecord("1", "192.168.1.10", "AA:BB:CC:DD:EE:01", "客厅电视", "static", "online"));
        ipAddresses.add(createIPRecord("2", "192.168.1.11", "AA:BB:CC:DD:EE:02", "卧室电脑", "static", "online"));
        ipAddresses.add(createIPRecord("3", "192.168.1.12", "AA:BB:CC:DD:EE:03", "书房打印机", "static", "offline"));

        // 动态 IP
        ipAddresses.add(createIPRecord("4", "192.168.1.101", "AA:BB:CC:DD:EE:04", "iPhone-12", "dynamic", "online"));
        ipAddresses.add(createIPRecord("5", "192.168.1.102", "AA:BB:CC:DD:EE:05", "MacBook-Pro", "dynamic", "online"));
        ipAddresses.add(createIPRecord("6", "192.168.1.103", "AA:BB:CC:DD:EE:06", "iPad-Air", "dynamic", "offline"));
        ipAddresses.add(createIPRecord("7", "192.168.1.104", "AA:BB:CC:DD:EE:07", "小米手机", "dynamic", "online"));
        ipAddresses.add(createIPRecord("8", "192.168.1.105", "AA:BB:CC:DD:EE:08", "华为平板", "dynamic", "online"));
    }

    private Map<String, Object> createIPRecord(String id, String ip, String mac, String name, String type, String status) {
        Map<String, Object> record = new HashMap<>();
        record.put("id", id);
        record.put("ip", ip);
        record.put("mac", mac);
        record.put("name", name);
        record.put("type", type);
        record.put("status", status);
        record.put("lastActive", System.currentTimeMillis() - (long) (Math.random() * 3600000));
        return record;
    }

    private void initializeBlacklist() {
        ipBlacklist.add(createBlacklistRecord("1", "192.168.1.200", "可疑设备-1", "*", true));
        ipBlacklist.add(createBlacklistRecord("2", "192.168.1.201", "可疑设备-2", "*", true));
    }

    private Map<String, Object> createBlacklistRecord(String id, String ip, String name, String source, boolean enabled) {
        Map<String, Object> record = new HashMap<>();
        record.put("id", id);
        record.put("ip", ip);
        record.put("name", name);
        record.put("source", source);
        record.put("enabled", enabled);
        record.put("created", System.currentTimeMillis() - (long) (Math.random() * 86400000));
        return record;
    }

    private void initializeConfigFiles() {
        configFiles.put("network", "config interface 'loopback'\n\toption ifname 'lo'\n\toption proto 'static'\n\toption ipaddr '127.0.0.1'\n\toption netmask '255.0.0.0'\n\nconfig interface 'lan'\n\toption ifname 'eth0'\n\toption proto 'static'\n\toption ipaddr '192.168.1.1'\n\toption netmask '255.255.255.0'\n");

        configFiles.put("dhcp", "config dnsmasq\n\toption domainneeded '1'\n\toption boguspriv '1'\n\toption filterwin2k '0'\n\toption localise_queries '1'\n\toption rebind_protection '1'\n\toption rebind_localhost '1'\n\toption local '/lan/'\n\toption domain 'lan'\n\toption expandhosts '1'\n\toption nonegcache '0'\n\toption authoritative '1'\n\toption readethers '1'\n\toption leasefile '/tmp/dhcp.leases'\n\toption resolvfile '/tmp/resolv.conf.auto'\n\toption nonwildcard '1'\n\toption localservice '1'\n");

        configFiles.put("firewall", "config defaults\n\toption syn_flood '1'\n\toption input 'ACCEPT'\n\toption output 'ACCEPT'\n\toption forward 'REJECT'\n\nconfig zone\n\toption name 'lan'\n\tlist network 'lan'\n\toption input 'ACCEPT'\n\toption output 'ACCEPT'\n\toption forward 'ACCEPT'\n");

        configFiles.put("wireless", "config wifi-device 'radio0'\n\toption type 'mac80211'\n\toption channel '6'\n\toption hwmode '11g'\n\toption path 'platform/qca953x_wmac'\n\toption htmode 'HT20'\n\nconfig wifi-iface 'default_radio0'\n\toption device 'radio0'\n\toption network 'lan'\n\toption mode 'ap'\n\toption ssid 'OpenWrt'\n\toption encryption 'none'\n");
    }

    @Override
    public boolean connect(String host, String username, String password) {
        log.info("[MOCK] Connecting to OpenWrt router: {}@{}", username, host);
        this.connected = true;
        return true;
    }

    @Override
    public void disconnect() {
        log.info("[MOCK] Disconnecting from OpenWrt router");
        this.connected = false;
    }

    @Override
    public boolean isConnected() {
        return connected;
    }

    @Override
    public Map<String, Object> getNetworkSetting(String settingType) {
        log.info("[MOCK] Getting network setting: {}", settingType);
        Map<String, Object> response = new HashMap<>();

        if (networkSettingsCache.containsKey(settingType)) {
            response.put("status", "success");
            response.put("message", "Network setting retrieved successfully");
            response.put("data", networkSettingsCache.get(settingType));
        } else {
            response.put("status", "error");
            response.put("message", "Network setting type not found: " + settingType);
            response.put("code", "SETTING_NOT_FOUND");
        }
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }

    @Override
    public Map<String, Object> getAllNetworkSettings() {
        log.info("[MOCK] Getting all network settings");
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "All network settings retrieved successfully");
        response.put("data", networkSettingsCache);
        response.put("count", networkSettingsCache.size());
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }

    @Override
    public Map<String, Object> updateNetworkSetting(String settingType, Map<String, Object> settingData) {
        log.info("[MOCK] Updating network setting: {}", settingType);
        Map<String, Object> response = new HashMap<>();

        if (networkSettingsCache.containsKey(settingType)) {
            networkSettingsCache.get(settingType).putAll(settingData);
            response.put("status", "success");
            response.put("message", "Network setting updated successfully");
            response.put("data", networkSettingsCache.get(settingType));
        } else {
            response.put("status", "error");
            response.put("message", "Network setting type not found: " + settingType);
            response.put("code", "SETTING_NOT_FOUND");
        }
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }

    @Override
    public Map<String, Object> batchUpdateNetworkSettings(Map<String, Map<String, Object>> settingsData) {
        log.info("[MOCK] Batch updating network settings");
        Map<String, Object> response = new HashMap<>();
        int successCount = 0;

        for (Map.Entry<String, Map<String, Object>> entry : settingsData.entrySet()) {
            if (networkSettingsCache.containsKey(entry.getKey())) {
                networkSettingsCache.get(entry.getKey()).putAll(entry.getValue());
                successCount++;
            }
        }

        response.put("status", "success");
        response.put("message", "Batch update completed: " + successCount + " settings updated");
        response.put("updatedCount", successCount);
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }

    @Override
    public Map<String, Object> getIPAddresses(String type, String status) {
        log.info("[MOCK] Getting IP addresses: type={}, status={}", type, status);
        Map<String, Object> response = new HashMap<>();

        List<Map<String, Object>> filtered = new ArrayList<>();
        for (Map<String, Object> ip : ipAddresses) {
            boolean typeMatch = type == null || type.isEmpty() || type.equals(ip.get("type"));
            boolean statusMatch = status == null || status.isEmpty() || status.equals(ip.get("status"));
            if (typeMatch && statusMatch) {
                filtered.add(ip);
            }
        }

        Map<String, Object> stats = new HashMap<>();
        stats.put("total", ipAddresses.size());
        stats.put("static", ipAddresses.stream().filter(ip -> "static".equals(ip.get("type"))).count());
        stats.put("dynamic", ipAddresses.stream().filter(ip -> "dynamic".equals(ip.get("type"))).count());
        stats.put("online", ipAddresses.stream().filter(ip -> "online".equals(ip.get("status"))).count());
        stats.put("offline", ipAddresses.stream().filter(ip -> "offline".equals(ip.get("status"))).count());

        response.put("status", "success");
        response.put("message", "IP addresses retrieved successfully");
        response.put("data", filtered);
        response.put("stats", stats);
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }

    @Override
    public Map<String, Object> addStaticIPAddress(Map<String, Object> ipData) {
        log.info("[MOCK] Adding static IP address: {}", ipData);
        Map<String, Object> response = new HashMap<>();

        String id = String.valueOf(ipAddresses.size() + 1);
        Map<String, Object> newRecord = new HashMap<>();
        newRecord.put("id", id);
        newRecord.put("ip", ipData.get("ip"));
        newRecord.put("mac", ipData.get("mac"));
        newRecord.put("name", ipData.getOrDefault("name", "Unknown"));
        newRecord.put("type", "static");
        newRecord.put("status", "online");
        newRecord.put("lastActive", System.currentTimeMillis());

        ipAddresses.add(newRecord);

        response.put("status", "success");
        response.put("message", "Static IP address added successfully");
        response.put("data", newRecord);
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }

    @Override
    public Map<String, Object> deleteIPAddress(String ipId) {
        log.info("[MOCK] Deleting IP address: {}", ipId);
        Map<String, Object> response = new HashMap<>();

        boolean removed = ipAddresses.removeIf(ip -> ipId.equals(ip.get("id")));

        if (removed) {
            response.put("status", "success");
            response.put("message", "IP address deleted successfully");
        } else {
            response.put("status", "error");
            response.put("message", "IP address not found");
            response.put("code", "IP_NOT_FOUND");
        }
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }

    @Override
    public Map<String, Object> batchAddStaticIPAddresses(List<Map<String, Object>> ipDataList) {
        log.info("[MOCK] Batch adding static IP addresses: {}", ipDataList.size());
        Map<String, Object> response = new HashMap<>();
        List<Map<String, Object>> added = new ArrayList<>();

        for (Map<String, Object> ipData : ipDataList) {
            String id = String.valueOf(ipAddresses.size() + 1);
            Map<String, Object> newRecord = new HashMap<>();
            newRecord.put("id", id);
            newRecord.put("ip", ipData.get("ip"));
            newRecord.put("mac", ipData.get("mac"));
            newRecord.put("name", ipData.getOrDefault("name", "Unknown"));
            newRecord.put("type", "static");
            newRecord.put("status", "online");
            newRecord.put("lastActive", System.currentTimeMillis());
            ipAddresses.add(newRecord);
            added.add(newRecord);
        }

        response.put("status", "success");
        response.put("message", "Batch add completed: " + added.size() + " IP addresses added");
        response.put("data", added);
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }

    @Override
    public Map<String, Object> batchDeleteIPAddresses(List<String> ipIds) {
        log.info("[MOCK] Batch deleting IP addresses: {}", ipIds.size());
        Map<String, Object> response = new HashMap<>();
        int deletedCount = 0;

        for (String ipId : ipIds) {
            if (ipAddresses.removeIf(ip -> ipId.equals(ip.get("id")))) {
                deletedCount++;
            }
        }

        response.put("status", "success");
        response.put("message", "Batch delete completed: " + deletedCount + " IP addresses deleted");
        response.put("deletedCount", deletedCount);
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }

    @Override
    public Map<String, Object> getIPBlacklist() {
        log.info("[MOCK] Getting IP blacklist");
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "IP blacklist retrieved successfully");
        response.put("data", ipBlacklist);
        response.put("count", ipBlacklist.size());
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }

    @Override
    public Map<String, Object> addIPToBlacklist(Map<String, Object> blacklistData) {
        log.info("[MOCK] Adding IP to blacklist: {}", blacklistData);
        Map<String, Object> response = new HashMap<>();

        String id = String.valueOf(ipBlacklist.size() + 1);
        Map<String, Object> newRecord = new HashMap<>();
        newRecord.put("id", id);
        newRecord.put("ip", blacklistData.get("ip"));
        newRecord.put("name", blacklistData.getOrDefault("name", "Blocked Device"));
        newRecord.put("source", blacklistData.getOrDefault("source", "*"));
        newRecord.put("enabled", true);
        newRecord.put("created", System.currentTimeMillis());

        ipBlacklist.add(newRecord);

        response.put("status", "success");
        response.put("message", "IP address added to blacklist successfully");
        response.put("data", newRecord);
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }

    @Override
    public Map<String, Object> removeIPFromBlacklist(String blacklistId) {
        log.info("[MOCK] Removing IP from blacklist: {}", blacklistId);
        Map<String, Object> response = new HashMap<>();

        boolean removed = ipBlacklist.removeIf(item -> blacklistId.equals(item.get("id")));

        if (removed) {
            response.put("status", "success");
            response.put("message", "IP address removed from blacklist successfully");
        } else {
            response.put("status", "error");
            response.put("message", "Blacklist entry not found");
            response.put("code", "BLACKLIST_ENTRY_NOT_FOUND");
        }
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }

    @Override
    public Map<String, Object> batchAddIPToBlacklist(List<Map<String, Object>> blacklistDataList) {
        log.info("[MOCK] Batch adding IPs to blacklist: {}", blacklistDataList.size());
        Map<String, Object> response = new HashMap<>();
        List<Map<String, Object>> added = new ArrayList<>();

        for (Map<String, Object> data : blacklistDataList) {
            String id = String.valueOf(ipBlacklist.size() + 1);
            Map<String, Object> newRecord = new HashMap<>();
            newRecord.put("id", id);
            newRecord.put("ip", data.get("ip"));
            newRecord.put("name", data.getOrDefault("name", "Blocked Device"));
            newRecord.put("source", data.getOrDefault("source", "*"));
            newRecord.put("enabled", true);
            newRecord.put("created", System.currentTimeMillis());
            ipBlacklist.add(newRecord);
            added.add(newRecord);
        }

        response.put("status", "success");
        response.put("message", "Batch add to blacklist completed: " + added.size() + " entries added");
        response.put("data", added);
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }

    @Override
    public Map<String, Object> batchRemoveIPFromBlacklist(List<String> blacklistIds) {
        log.info("[MOCK] Batch removing IPs from blacklist: {}", blacklistIds.size());
        Map<String, Object> response = new HashMap<>();
        int removedCount = 0;

        for (String id : blacklistIds) {
            if (ipBlacklist.removeIf(item -> id.equals(item.get("id")))) {
                removedCount++;
            }
        }

        response.put("status", "success");
        response.put("message", "Batch remove from blacklist completed: " + removedCount + " entries removed");
        response.put("removedCount", removedCount);
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }

    @Override
    public Map<String, Object> getConfigFile(String configFile) {
        log.info("[MOCK] Getting config file: {}", configFile);
        Map<String, Object> response = new HashMap<>();

        String content = configFiles.getOrDefault(configFile, "# Configuration file not found\n");

        Map<String, Object> fileInfo = new HashMap<>();
        fileInfo.put("name", configFile);
        fileInfo.put("path", "/etc/config/" + configFile);
        fileInfo.put("content", content);
        fileInfo.put("size", content.length());
        fileInfo.put("lastModified", System.currentTimeMillis());
        fileInfo.put("permissions", "644");

        response.put("status", "success");
        response.put("message", "Config file retrieved successfully");
        response.put("data", fileInfo);
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }

    @Override
    public Map<String, Object> updateConfigFile(String configFile, Map<String, Object> configData) {
        log.info("[MOCK] Updating config file: {}", configFile);
        Map<String, Object> response = new HashMap<>();

        if (configData.containsKey("content")) {
            configFiles.put(configFile, (String) configData.get("content"));

            Map<String, Object> fileInfo = new HashMap<>();
            fileInfo.put("name", configFile);
            fileInfo.put("path", "/etc/config/" + configFile);
            fileInfo.put("size", ((String) configData.get("content")).length());
            fileInfo.put("lastModified", System.currentTimeMillis());

            response.put("status", "success");
            response.put("message", "Config file updated successfully");
            response.put("data", fileInfo);
        } else {
            response.put("status", "error");
            response.put("message", "Missing content parameter");
            response.put("code", "MISSING_CONTENT");
        }
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }

    @Override
    public Map<String, Object> executeCommand(String command) {
        log.info("[MOCK] Executing command: {}", command);
        Map<String, Object> result = new HashMap<>();
        result.put("output", "Mock command output for: " + command);
        result.put("exitCode", 0);
        result.put("success", true);
        result.put("timestamp", System.currentTimeMillis());
        return result;
    }

    @Override
    public Map<String, Object> reboot() {
        log.info("[MOCK] Rebooting router");
        Map<String, Object> response = new HashMap<>();

        Map<String, Object> rebootInfo = new HashMap<>();
        rebootInfo.put("router", "192.168.1.1");
        rebootInfo.put("rebootInitiated", true);
        rebootInfo.put("expectedDowntime", "2-3 minutes");
        rebootInfo.put("reconnectAfter", System.currentTimeMillis() + 180000);

        response.put("status", "success");
        response.put("message", "Router reboot initiated successfully");
        response.put("data", rebootInfo);
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }

    @Override
    public Map<String, Object> getSystemStatus() {
        log.info("[MOCK] Getting system status");
        Map<String, Object> status = new HashMap<>();
        status.put("cpu", "15%");
        status.put("memory", "50%");
        status.put("temperature", "45°C");
        status.put("uptime", "3 days, 12:34");
        status.put("loadAverage", "0.12 0.15 0.18");
        status.put("firmwareVersion", "OpenWrt 23.05.0");
        status.put("kernelVersion", "Linux 5.15.0");
        status.put("deviceModel", "TP-Link TL-WDR7660");
        status.put("timestamp", System.currentTimeMillis());
        return status;
    }

    @Override
    public Map<String, Object> getVersionInfo() {
        log.info("[MOCK] Getting version info");
        Map<String, Object> versionInfo = new HashMap<>();
        versionInfo.put("version", "23.05.0");
        versionInfo.put("openWrtVersion", "23.05.0");
        versionInfo.put("versionDetected", true);
        versionInfo.put("isSupported", true);
        versionInfo.put("kernelVersion", "Linux 5.15.0");
        versionInfo.put("deviceInfo", "TP-Link TL-WDR7660");
        versionInfo.put("timestamp", System.currentTimeMillis());
        return versionInfo;
    }

    @Override
    public boolean isVersionSupported(String version) {
        log.info("[MOCK] Checking if version is supported: {}", version);
        return version != null && (version.startsWith("23.") || version.startsWith("22.") || version.startsWith("21."));
    }

    @Override
    public Map<String, Object> getSupportedVersions() {
        log.info("[MOCK] Getting supported versions");
        Map<String, Object> response = new HashMap<>();

        List<String> versions = Arrays.asList("23.05", "22.03", "21.02", "19.07");
        Map<String, Object> versionInfo = new HashMap<>();
        versionInfo.put("versions", versions);
        versionInfo.put("count", versions.size());
        versionInfo.put("latestVersion", versions.get(0));
        versionInfo.put("minimumVersion", versions.get(versions.size() - 1));

        response.put("status", "success");
        response.put("message", "Supported versions retrieved successfully");
        response.put("data", versionInfo);
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }
}
