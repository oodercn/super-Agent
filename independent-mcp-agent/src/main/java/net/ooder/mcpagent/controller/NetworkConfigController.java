package net.ooder.mcpagent.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/network/config")
public class NetworkConfigController {

    private static final Logger log = LoggerFactory.getLogger(NetworkConfigController.class);
    
    // 网络设置存储
    private final Map<String, NetworkSetting> networkSettings = new ConcurrentHashMap<>();
    
    // IP地址管理
    private final List<IPAddress> ipAddresses = new ArrayList<>();
    
    // IP黑名单
    private final List<IPBlacklist> ipBlacklist = new ArrayList<>();
    
    // 初始化默认网络配置
    public NetworkConfigController() {
        initializeDefaultNetworkSettings();
        initializeDefaultIPAddresses();
        initializeDefaultIPBlacklist();
    }
    
    private void initializeDefaultNetworkSettings() {
        // 添加默认网络设置
        Map<String, Object> basicSettings = new ConcurrentHashMap<>();
        basicSettings.put("networkName", "Home Network");
        basicSettings.put("domainName", "home.local");
        basicSettings.put("timezone", "Asia/Shanghai");
        basicSettings.put("ntpServer", "pool.ntp.org");
        basicSettings.put("interfaceSpeed", "auto");
        networkSettings.put("basic", new NetworkSetting(
            "basic", "基本网络设置", 
            basicSettings
        ));
        
        Map<String, Object> dnsSettings = new ConcurrentHashMap<>();
        dnsSettings.put("primaryDns", "8.8.8.8");
        dnsSettings.put("secondaryDns", "8.8.4.4");
        dnsSettings.put("dnsSuffix", "home.local");
        dnsSettings.put("dnsCacheEnabled", true);
        dnsSettings.put("dnsCacheSize", 1000);
        networkSettings.put("dns", new NetworkSetting(
            "dns", "DNS配置", 
            dnsSettings
        ));
        
        Map<String, Object> dhcpSettings = new ConcurrentHashMap<>();
        dhcpSettings.put("dhcpEnabled", true);
        dhcpSettings.put("startIp", "192.168.1.100");
        dhcpSettings.put("endIp", "192.168.1.200");
        dhcpSettings.put("leaseTime", 86400); // 24小时
        dhcpSettings.put("gateway", "192.168.1.1");
        dhcpSettings.put("subnetMask", "255.255.255.0");
        networkSettings.put("dhcp", new NetworkSetting(
            "dhcp", "DHCP配置", 
            dhcpSettings
        ));
        
        Map<String, Object> wifiSettings = new ConcurrentHashMap<>();
        wifiSettings.put("ssid", "HomeWiFi");
        wifiSettings.put("channel", 6);
        wifiSettings.put("mode", "802.11ac");
        wifiSettings.put("security", "WPA2-PSK");
        wifiSettings.put("password", "********");
        wifiSettings.put("txPower", "auto");
        networkSettings.put("wifi", new NetworkSetting(
            "wifi", "WiFi配置", 
            wifiSettings
        ));
    }
    
    private void initializeDefaultIPAddresses() {
        // 添加默认IP地址
        ipAddresses.add(new IPAddress(
            "ip-1", "192.168.1.1", "static", "online", "主路由器", 
            "AA:BB:CC:DD:EE:01", "router", "永久", System.currentTimeMillis()
        ));
        
        ipAddresses.add(new IPAddress(
            "ip-2", "192.168.1.2", "static", "online", "交换机", 
            "AA:BB:CC:DD:EE:02", "switch", "永久", System.currentTimeMillis()
        ));
        
        ipAddresses.add(new IPAddress(
            "ip-3", "192.168.1.3", "static", "online", "AP接入点", 
            "AA:BB:CC:DD:EE:03", "access_point", "永久", System.currentTimeMillis()
        ));
        
        ipAddresses.add(new IPAddress(
            "ip-100", "192.168.1.100", "dynamic", "online", "智能电视", 
            "AA:BB:CC:DD:EE:05", "client", "24小时", System.currentTimeMillis()
        ));
        
        ipAddresses.add(new IPAddress(
            "ip-101", "192.168.1.101", "dynamic", "offline", "笔记本电脑", 
            "AA:BB:CC:DD:EE:06", "client", "24小时", System.currentTimeMillis() - 3600000
        ));
    }
    
    private void initializeDefaultIPBlacklist() {
        // 添加默认IP黑名单
        ipBlacklist.add(new IPBlacklist(
            "blacklist-1", "192.168.1.254", "可疑IP", "手动添加", System.currentTimeMillis()
        ));
        
        ipBlacklist.add(new IPBlacklist(
            "blacklist-2", "10.0.0.1", "未授权访问", "自动检测", System.currentTimeMillis() - 86400000
        ));
    }
    
    /**
     * 获取网络设置
     */
    @GetMapping("/settings/{settingType}")
    public Map<String, Object> getNetworkSetting(@PathVariable String settingType) {
        log.info("Get network setting requested: {}", settingType);
        Map<String, Object> response = new HashMap<>();
        
        try {
            NetworkSetting setting = networkSettings.get(settingType);
            if (setting == null) {
                response.put("status", "error");
                response.put("message", "Network setting not found");
                response.put("code", "NETWORK_SETTING_NOT_FOUND");
                response.put("timestamp", System.currentTimeMillis());
                return response;
            }
            
            response.put("status", "success");
            response.put("message", "Network setting retrieved successfully");
            response.put("data", setting.toMap());
            response.put("timestamp", System.currentTimeMillis());
            
        } catch (Exception e) {
            log.error("Error getting network setting: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "NETWORK_SETTING_RETRIEVAL_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        }
        
        return response;
    }
    
    /**
     * 获取所有网络设置
     */
    @GetMapping("/settings")
    public Map<String, Object> getAllNetworkSettings() {
        log.info("Get all network settings requested");
        Map<String, Object> response = new HashMap<>();
        
        try {
            Map<String, Object> settingsMap = new HashMap<>();
            for (Map.Entry<String, NetworkSetting> entry : networkSettings.entrySet()) {
                settingsMap.put(entry.getKey(), entry.getValue().toMap());
            }
            
            response.put("status", "success");
            response.put("message", "All network settings retrieved successfully");
            response.put("data", settingsMap);
            response.put("count", networkSettings.size());
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
    
    /**
     * 更新网络设置
     */
    @PutMapping("/settings/{settingType}")
    public Map<String, Object> updateNetworkSetting(
            @PathVariable String settingType, @RequestBody Map<String, Object> settingData) {
        log.info("Update network setting requested: {}, data: {}", settingType, settingData);
        Map<String, Object> response = new HashMap<>();
        
        try {
            NetworkSetting setting = networkSettings.get(settingType);
            if (setting == null) {
                response.put("status", "error");
                response.put("message", "Network setting not found");
                response.put("code", "NETWORK_SETTING_NOT_FOUND");
                response.put("timestamp", System.currentTimeMillis());
                return response;
            }
            
            // 更新设置
            setting.updateSettings(settingData);
            
            response.put("status", "success");
            response.put("message", "Network setting updated successfully");
            response.put("data", setting.toMap());
            response.put("timestamp", System.currentTimeMillis());
            
        } catch (Exception e) {
            log.error("Error updating network setting: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "NETWORK_SETTING_UPDATE_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        }
        
        return response;
    }
    
    /**
     * 获取IP地址列表
     */
    @GetMapping("/ip/addresses")
    public Map<String, Object> getIPAddresses(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status) {
        log.info("Get IP addresses requested: type={}, status={}", type, status);
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<IPAddress> filteredIPs = ipAddresses.stream()
                    .filter(ip -> (type == null || ip.getType().equals(type)))
                    .filter(ip -> (status == null || ip.getStatus().equals(status)))
                    .collect(Collectors.toList());
            
            // 转换为响应格式
            List<Map<String, Object>> ipList = new ArrayList<>();
            for (IPAddress ip : filteredIPs) {
                ipList.add(ip.toMap());
            }
            
            // IP地址统计
            Map<String, Object> stats = new HashMap<>();
            stats.put("total", ipAddresses.size());
            stats.put("static", ipAddresses.stream().filter(ip -> "static".equals(ip.getType())).count());
            stats.put("dynamic", ipAddresses.stream().filter(ip -> "dynamic".equals(ip.getType())).count());
            stats.put("online", ipAddresses.stream().filter(ip -> "online".equals(ip.getStatus())).count());
            stats.put("offline", ipAddresses.stream().filter(ip -> "offline".equals(ip.getStatus())).count());
            
            response.put("status", "success");
            response.put("message", "IP addresses retrieved successfully");
            response.put("data", ipList);
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
    
    /**
     * 添加静态IP地址
     */
    @PostMapping("/ip/addresses")
    public Map<String, Object> addStaticIPAddress(@RequestBody Map<String, Object> ipData) {
        log.info("Add static IP address requested: {}", ipData);
        Map<String, Object> response = new HashMap<>();
        
        try {
            String ip = (String) ipData.get("ipAddress");
            String deviceName = (String) ipData.getOrDefault("deviceName", "Unknown Device");
            String macAddress = (String) ipData.getOrDefault("macAddress", "");
            String deviceType = (String) ipData.getOrDefault("deviceType", "client");
            String leaseTime = (String) ipData.getOrDefault("leaseTime", "永久");
            
            // 检查IP是否已存在
            boolean ipExists = ipAddresses.stream()
                    .anyMatch(existingIp -> existingIp.getIpAddress().equals(ip));
            
            if (ipExists) {
                response.put("status", "error");
                response.put("message", "IP address already exists");
                response.put("code", "IP_ADDRESS_EXISTS");
                response.put("timestamp", System.currentTimeMillis());
                return response;
            }
            
            IPAddress newIP = new IPAddress(
                "ip-" + (ipAddresses.size() + 1),
                ip,
                "static",
                "online",
                deviceName,
                macAddress,
                deviceType,
                leaseTime,
                System.currentTimeMillis()
            );
            
            ipAddresses.add(newIP);
            
            response.put("status", "success");
            response.put("message", "Static IP address added successfully");
            response.put("data", newIP.toMap());
            response.put("timestamp", System.currentTimeMillis());
            
        } catch (Exception e) {
            log.error("Error adding static IP address: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "IP_ADDRESS_ADD_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        }
        
        return response;
    }
    
    /**
     * 删除IP地址
     */
    @DeleteMapping("/ip/addresses/{ipId}")
    public Map<String, Object> deleteIPAddress(@PathVariable String ipId) {
        log.info("Delete IP address requested: {}", ipId);
        Map<String, Object> response = new HashMap<>();
        
        try {
            IPAddress ipToDelete = ipAddresses.stream()
                    .filter(ip -> ip.getId().equals(ipId))
                    .findFirst()
                    .orElse(null);
            
            if (ipToDelete == null) {
                response.put("status", "error");
                response.put("message", "IP address not found");
                response.put("code", "IP_ADDRESS_NOT_FOUND");
                response.put("timestamp", System.currentTimeMillis());
                return response;
            }
            
            // 只允许删除动态IP
            if ("static".equals(ipToDelete.getType())) {
                response.put("status", "error");
                response.put("message", "Cannot delete static IP address");
                response.put("code", "STATIC_IP_DELETE_ERROR");
                response.put("timestamp", System.currentTimeMillis());
                return response;
            }
            
            ipAddresses.remove(ipToDelete);
            
            response.put("status", "success");
            response.put("message", "IP address deleted successfully");
            response.put("data", ipToDelete.toMap());
            response.put("timestamp", System.currentTimeMillis());
            
        } catch (Exception e) {
            log.error("Error deleting IP address: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "IP_ADDRESS_DELETE_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        }
        
        return response;
    }
    
    /**
     * 获取IP黑名单
     */
    @GetMapping("/ip/blacklist")
    public Map<String, Object> getIPBlacklist() {
        log.info("Get IP blacklist requested");
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 转换为响应格式
            List<Map<String, Object>> blacklist = new ArrayList<>();
            for (IPBlacklist item : ipBlacklist) {
                blacklist.add(item.toMap());
            }
            
            response.put("status", "success");
            response.put("message", "IP blacklist retrieved successfully");
            response.put("data", blacklist);
            response.put("count", blacklist.size());
            response.put("timestamp", System.currentTimeMillis());
            
        } catch (Exception e) {
            log.error("Error getting IP blacklist: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "IP_BLACKLIST_RETRIEVAL_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        }
        
        return response;
    }
    
    /**
     * 添加IP到黑名单
     */
    @PostMapping("/ip/blacklist")
    public Map<String, Object> addIPToBlacklist(@RequestBody Map<String, Object> blacklistData) {
        log.info("Add IP to blacklist requested: {}", blacklistData);
        Map<String, Object> response = new HashMap<>();
        
        try {
            String ip = (String) blacklistData.get("ipAddress");
            String reason = (String) blacklistData.getOrDefault("reason", "未指定");
            String source = (String) blacklistData.getOrDefault("source", "手动添加");
            
            // 检查IP是否已在黑名单中
            boolean ipExists = ipBlacklist.stream()
                    .anyMatch(item -> item.getIpAddress().equals(ip));
            
            if (ipExists) {
                response.put("status", "error");
                response.put("message", "IP address already in blacklist");
                response.put("code", "IP_BLACKLIST_EXISTS");
                response.put("timestamp", System.currentTimeMillis());
                return response;
            }
            
            IPBlacklist newBlacklistItem = new IPBlacklist(
                "blacklist-" + (ipBlacklist.size() + 1),
                ip,
                reason,
                source,
                System.currentTimeMillis()
            );
            
            ipBlacklist.add(newBlacklistItem);
            
            response.put("status", "success");
            response.put("message", "IP address added to blacklist successfully");
            response.put("data", newBlacklistItem.toMap());
            response.put("timestamp", System.currentTimeMillis());
            
        } catch (Exception e) {
            log.error("Error adding IP to blacklist: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "IP_BLACKLIST_ADD_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        }
        
        return response;
    }
    
    /**
     * 从黑名单移除IP
     */
    @DeleteMapping("/ip/blacklist/{blacklistId}")
    public Map<String, Object> removeIPFromBlacklist(@PathVariable String blacklistId) {
        log.info("Remove IP from blacklist requested: {}", blacklistId);
        Map<String, Object> response = new HashMap<>();
        
        try {
            IPBlacklist itemToRemove = ipBlacklist.stream()
                    .filter(item -> item.getId().equals(blacklistId))
                    .findFirst()
                    .orElse(null);
            
            if (itemToRemove == null) {
                response.put("status", "error");
                response.put("message", "Blacklist item not found");
                response.put("code", "IP_BLACKLIST_ITEM_NOT_FOUND");
                response.put("timestamp", System.currentTimeMillis());
                return response;
            }
            
            ipBlacklist.remove(itemToRemove);
            
            response.put("status", "success");
            response.put("message", "IP address removed from blacklist successfully");
            response.put("data", itemToRemove.toMap());
            response.put("timestamp", System.currentTimeMillis());
            
        } catch (Exception e) {
            log.error("Error removing IP from blacklist: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "IP_BLACKLIST_REMOVE_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        }
        
        return response;
    }
    
    // 网络设置类
    private static class NetworkSetting {
        private final String id;
        private final String name;
        private Map<String, Object> settings;
        private long lastUpdated;
        
        public NetworkSetting(String id, String name, Map<String, Object> settings) {
            this.id = id;
            this.name = name;
            this.settings = settings;
            this.lastUpdated = System.currentTimeMillis();
        }
        
        public void updateSettings(Map<String, Object> newSettings) {
            this.settings.putAll(newSettings);
            this.lastUpdated = System.currentTimeMillis();
        }
        
        public Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("id", id);
            map.put("name", name);
            map.put("settings", settings);
            map.put("lastUpdated", lastUpdated);
            return map;
        }
        
        public String getId() { return id; }
        public String getName() { return name; }
    }
    
    // IP地址类
    private static class IPAddress {
        private final String id;
        private final String ipAddress;
        private final String type;
        private String status;
        private final String deviceName;
        private final String macAddress;
        private final String deviceType;
        private final String leaseTime;
        private final long assignedAt;
        private long lastSeen;
        
        public IPAddress(String id, String ipAddress, String type, String status, String deviceName,
                        String macAddress, String deviceType, String leaseTime, long assignedAt) {
            this.id = id;
            this.ipAddress = ipAddress;
            this.type = type;
            this.status = status;
            this.deviceName = deviceName;
            this.macAddress = macAddress;
            this.deviceType = deviceType;
            this.leaseTime = leaseTime;
            this.assignedAt = assignedAt;
            this.lastSeen = System.currentTimeMillis();
        }
        
        public Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("id", id);
            map.put("ipAddress", ipAddress);
            map.put("type", type);
            map.put("status", status);
            map.put("deviceName", deviceName);
            map.put("macAddress", macAddress);
            map.put("deviceType", deviceType);
            map.put("leaseTime", leaseTime);
            map.put("assignedAt", assignedAt);
            map.put("lastSeen", lastSeen);
            return map;
        }
        
        public String getId() { return id; }
        public String getIpAddress() { return ipAddress; }
        public String getType() { return type; }
        public String getStatus() { return status; }
    }
    
    // IP黑名单类
    private static class IPBlacklist {
        private final String id;
        private final String ipAddress;
        private final String reason;
        private final String source;
        private final long addedAt;
        
        public IPBlacklist(String id, String ipAddress, String reason, String source, long addedAt) {
            this.id = id;
            this.ipAddress = ipAddress;
            this.reason = reason;
            this.source = source;
            this.addedAt = addedAt;
        }
        
        public Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("id", id);
            map.put("ipAddress", ipAddress);
            map.put("reason", reason);
            map.put("source", source);
            map.put("addedAt", addedAt);
            return map;
        }
        
        public String getId() { return id; }
        public String getIpAddress() { return ipAddress; }
    }
}
