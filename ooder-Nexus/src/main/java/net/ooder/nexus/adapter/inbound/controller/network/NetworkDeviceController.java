package net.ooder.nexus.adapter.inbound.controller.network;

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
@RequestMapping("/api/network/devices")
public class NetworkDeviceController {

    private static final Logger log = LoggerFactory.getLogger(NetworkDeviceController.class);
    
    // 网络设备存储
    private final ConcurrentHashMap<String, NetworkDevice> networkDevices = new ConcurrentHashMap<>();
    
    // 设备操作日志
    private final List<DeviceOperationLog> operationLogs = new ArrayList<>();
    
    // 初始化默认网络设备
    public NetworkDeviceController() {
        initializeDefaultNetworkDevices();
    }
    
    private void initializeDefaultNetworkDevices() {
        // 添加默认网络设备
        Map<String, Object> props1 = new ConcurrentHashMap<>();
        props1.put("dhcpEnabled", true);
        props1.put("dnsServer", "8.8.8.8");
        props1.put("subnetMask", "255.255.255.0");
        networkDevices.put("device-1", new NetworkDevice(
            "device-1", "主路由器", "router", "online", "192.168.1.1", 
            "AA:BB:CC:DD:EE:01", "1000Mbps", 100, 50, 
            "Huawei", "AR1220", "v2.0.0", "网关", 
            props1
        ));
        
        Map<String, Object> props2 = new ConcurrentHashMap<>();
        props2.put("ports", 24);
        props2.put("vlanEnabled", false);
        props2.put("poeEnabled", false);
        networkDevices.put("device-2", new NetworkDevice(
            "device-2", "交换机", "switch", "online", "192.168.1.2", 
            "AA:BB:CC:DD:EE:02", "1000Mbps", 80, 30, 
            "TP-Link", "TL-SG1024D", "v1.0.0", "局域网", 
            props2
        ));
        
        Map<String, Object> props3 = new ConcurrentHashMap<>();
        props3.put("ssid", "HomeWiFi");
        props3.put("channel", 6);
        props3.put("security", "WPA2-PSK");
        networkDevices.put("device-3", new NetworkDevice(
            "device-3", "AP接入点", "access_point", "online", "192.168.1.3", 
            "AA:BB:CC:DD:EE:03", "867Mbps", 60, 20, 
            "Netgear", "WNAP320", "v3.0.0", "客厅", 
            props3
        ));
        
        Map<String, Object> props4 = new ConcurrentHashMap<>();
        props4.put("storageCapacity", "8TB");
        props4.put("volumes", 2);
        props4.put("raidLevel", "RAID 1");
        networkDevices.put("device-4", new NetworkDevice(
            "device-4", "网络存储", "nas", "offline", "192.168.1.4", 
            "AA:BB:CC:DD:EE:04", "1000Mbps", 0, 0, 
            "Synology", "DS218+", "v6.2.0", "书房", 
            props4
        ));
        
        Map<String, Object> props5 = new ConcurrentHashMap<>();
        props5.put("macAddress", "AA:BB:CC:DD:EE:05");
        props5.put("hostname", "Samsung-TV");
        props5.put("connectionType", "WiFi");
        networkDevices.put("device-5", new NetworkDevice(
            "device-5", "智能电视", "client", "online", "192.168.1.100", 
            "AA:BB:CC:DD:EE:05", "100Mbps", 20, 10, 
            "Samsung", "QLED 4K", "v1.5.0", "客厅", 
            props5
        ));
    }
    
    /**
     * 获取网络设备列表
     */
    @GetMapping("/list")
    public Map<String, Object> getNetworkDevices(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String location) {
        log.info("Get network devices requested: status={}, type={}, location={}", status, type, location);
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<NetworkDevice> filteredDevices = networkDevices.values().stream()
                    .filter(device -> (status == null || device.getStatus().equals(status)))
                    .filter(device -> (type == null || device.getType().equals(type)))
                    .filter(device -> (location == null || device.getLocation().equals(location)))
                    .collect(Collectors.toList());
            
            // 转换为响应格式
            List<Map<String, Object>> deviceList = new ArrayList<>();
            for (NetworkDevice device : filteredDevices) {
                deviceList.add(device.toSummaryMap());
            }
            
            // 设备统计
            Map<String, Object> stats = new HashMap<>();
            stats.put("total", networkDevices.size());
            stats.put("online", networkDevices.values().stream().filter(d -> "online".equals(d.getStatus())).count());
            stats.put("offline", networkDevices.values().stream().filter(d -> "offline".equals(d.getStatus())).count());
            stats.put("types", networkDevices.values().stream().map(NetworkDevice::getType).distinct().count());
            
            // 按类型统计
            Map<String, Long> typeStats = new HashMap<>();
            for (NetworkDevice device : networkDevices.values()) {
                typeStats.put(device.getType(), typeStats.getOrDefault(device.getType(), 0L) + 1);
            }
            stats.put("typeStats", typeStats);
            
            response.put("status", "success");
            response.put("message", "Network devices retrieved successfully");
            response.put("data", deviceList);
            response.put("stats", stats);
            response.put("timestamp", System.currentTimeMillis());
            
        } catch (Exception e) {
            log.error("Error getting network devices: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "NETWORK_DEVICES_RETRIEVAL_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        }
        
        return response;
    }
    
    /**
     * 获取网络设备详情
     */
    @GetMapping("/detail/{deviceId}")
    public Map<String, Object> getNetworkDeviceDetail(@PathVariable String deviceId) {
        log.info("Get network device detail requested: {}", deviceId);
        Map<String, Object> response = new HashMap<>();
        
        try {
            NetworkDevice device = networkDevices.get(deviceId);
            if (device == null) {
                response.put("status", "error");
                response.put("message", "Network device not found");
                response.put("code", "NETWORK_DEVICE_NOT_FOUND");
                response.put("timestamp", System.currentTimeMillis());
                return response;
            }
            
            // 获取设备详情
            Map<String, Object> deviceDetail = device.toDetailMap();
            
            // 添加网络连接信息
            deviceDetail.put("networkInfo", device.getNetworkInfo());
            
            // 添加设备性能信息
            Map<String, Object> performance = new ConcurrentHashMap<>();
            performance.put("throughput", device.getThroughput());
            performance.put("signalStrength", device.getSignalStrength());
            performance.put("uptime", device.getUptime());
            deviceDetail.put("performance", performance);
            
            response.put("status", "success");
            response.put("message", "Network device detail retrieved successfully");
            response.put("data", deviceDetail);
            response.put("timestamp", System.currentTimeMillis());
            
        } catch (Exception e) {
            log.error("Error getting network device detail: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "NETWORK_DEVICE_DETAIL_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        }
        
        return response;
    }
    
    /**
     * 刷新网络设备状态
     */
    @PostMapping("/refresh/{deviceId}")
    public Map<String, Object> refreshNetworkDeviceStatus(@PathVariable String deviceId) {
        log.info("Refresh network device status requested: {}", deviceId);
        Map<String, Object> response = new HashMap<>();
        
        try {
            NetworkDevice device = networkDevices.get(deviceId);
            if (device == null) {
                response.put("status", "error");
                response.put("message", "Network device not found");
                response.put("code", "NETWORK_DEVICE_NOT_FOUND");
                response.put("timestamp", System.currentTimeMillis());
                return response;
            }
            
            // 模拟刷新设备状态
            device.refreshStatus();
            
            // 记录操作日志
            addOperationLog(deviceId, device.getName(), "refresh", "success", "Device status refreshed");
            
            response.put("status", "success");
            response.put("message", "Network device status refreshed successfully");
            response.put("data", device.toSummaryMap());
            response.put("timestamp", System.currentTimeMillis());
            
        } catch (Exception e) {
            log.error("Error refreshing network device status: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "NETWORK_DEVICE_REFRESH_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        }
        
        return response;
    }
    
    /**
     * 获取网络设备类型列表
     */
    @GetMapping("/types")
    public Map<String, Object> getNetworkDeviceTypes() {
        log.info("Get network device types requested");
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<String> types = networkDevices.values().stream()
                    .map(NetworkDevice::getType)
                    .distinct()
                    .collect(Collectors.toList());
            
            // 统计每种类型的设备数量
            Map<String, Long> typeCounts = new HashMap<>();
            for (String type : types) {
                typeCounts.put(type, networkDevices.values().stream().filter(d -> type.equals(d.getType())).count());
            }
            
            // 设备类型描述
            Map<String, String> typeDescriptions = new ConcurrentHashMap<>();
            typeDescriptions.put("router", "路由器");
            typeDescriptions.put("switch", "交换机");
            typeDescriptions.put("access_point", "无线接入点");
            typeDescriptions.put("nas", "网络存储");
            typeDescriptions.put("client", "客户端设备");
            
            response.put("status", "success");
            response.put("message", "Network device types retrieved successfully");
            response.put("data", types);
            response.put("typeCounts", typeCounts);
            response.put("typeDescriptions", typeDescriptions);
            response.put("timestamp", System.currentTimeMillis());
            
        } catch (Exception e) {
            log.error("Error getting network device types: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "NETWORK_DEVICE_TYPES_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        }
        
        return response;
    }
    
    /**
     * 获取设备操作日志
     */
    @GetMapping("/logs")
    public Map<String, Object> getDeviceOperationLogs(
            @RequestParam(defaultValue = "50") int limit,
            @RequestParam(required = false) String deviceId) {
        log.info("Get device operation logs requested: limit={}, deviceId={}", limit, deviceId);
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<DeviceOperationLog> filteredLogs = operationLogs.stream()
                    .filter(log -> (deviceId == null || log.getDeviceId().equals(deviceId)))
                    .sorted((l1, l2) -> Long.compare(l2.getTimestamp(), l1.getTimestamp()))
                    .limit(limit)
                    .collect(Collectors.toList());
            
            // 转换为响应格式
            List<Map<String, Object>> logList = new ArrayList<>();
            for (DeviceOperationLog log : filteredLogs) {
                logList.add(log.toMap());
            }
            
            response.put("status", "success");
            response.put("message", "Device operation logs retrieved successfully");
            response.put("data", logList);
            response.put("count", logList.size());
            response.put("timestamp", System.currentTimeMillis());
            
        } catch (Exception e) {
            log.error("Error getting device operation logs: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "LOGS_RETRIEVAL_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        }
        
        return response;
    }
    
    /**
     * 获取设备状态
     */
    @GetMapping("/device-status")
    public Map<String, Object> getNetworkDevices() {
        log.info("Get network devices status requested");
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 获取所有网络设备
            List<Map<String, Object>> deviceList = new ArrayList<>();
            for (NetworkDevice device : networkDevices.values()) {
                deviceList.add(device.toSummaryMap());
            }
            
            // 设备统计
            Map<String, Object> stats = new HashMap<>();
            stats.put("total", networkDevices.size());
            stats.put("online", networkDevices.values().stream().filter(d -> "online".equals(d.getStatus())).count());
            stats.put("offline", networkDevices.values().stream().filter(d -> "offline".equals(d.getStatus())).count());
            stats.put("types", networkDevices.values().stream().map(NetworkDevice::getType).distinct().count());
            
            response.put("status", "success");
            response.put("message", "Network devices status retrieved successfully");
            response.put("data", deviceList);
            response.put("stats", stats);
            response.put("timestamp", System.currentTimeMillis());
            
        } catch (Exception e) {
            log.error("Error getting network devices status: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "NETWORK_DEVICES_STATUS_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        }
        
        return response;
    }
    
    /**
     * 添加操作日志
     */
    private void addOperationLog(String deviceId, String deviceName, String operation, String status, String message) {
        DeviceOperationLog log = new DeviceOperationLog(
            deviceId, deviceName, operation, status, message
        );
        operationLogs.add(log);
        
        // 限制日志数量
        if (operationLogs.size() > 1000) {
            operationLogs.remove(0);
        }
    }
    
    // 网络设备类
    private static class NetworkDevice {
        private final String id;
        private final String name;
        private final String type;
        private String status;
        private final String ipAddress;
        private final String macAddress;
        private final String speed;
        private final int throughput;
        private final int signalStrength;
        private final String manufacturer;
        private final String model;
        private final String firmwareVersion;
        private final String location;
        private final Map<String, Object> properties;
        private long lastUpdated;
        private long uptime;
        
        public NetworkDevice(String id, String name, String type, String status, String ipAddress,
                           String macAddress, String speed, int throughput, int signalStrength,
                           String manufacturer, String model, String firmwareVersion, String location,
                           Map<String, Object> properties) {
            this.id = id;
            this.name = name;
            this.type = type;
            this.status = status;
            this.ipAddress = ipAddress;
            this.macAddress = macAddress;
            this.speed = speed;
            this.throughput = throughput;
            this.signalStrength = signalStrength;
            this.manufacturer = manufacturer;
            this.model = model;
            this.firmwareVersion = firmwareVersion;
            this.location = location;
            this.properties = properties;
            this.lastUpdated = System.currentTimeMillis();
            this.uptime = System.currentTimeMillis() - 3600000; // 1小时
        }
        
        public void refreshStatus() {
            // 模拟刷新设备状态
            this.status = Math.random() > 0.1 ? "online" : "offline"; // 90% 概率在线
            this.lastUpdated = System.currentTimeMillis();
        }
        
        public Map<String, Object> toSummaryMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("id", id);
            map.put("name", name);
            map.put("type", type);
            map.put("status", status);
            map.put("ipAddress", ipAddress);
            map.put("macAddress", macAddress);
            map.put("location", location);
            map.put("lastUpdated", lastUpdated);
            return map;
        }
        
        public Map<String, Object> toDetailMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("id", id);
            map.put("name", name);
            map.put("type", type);
            map.put("status", status);
            map.put("ipAddress", ipAddress);
            map.put("macAddress", macAddress);
            map.put("speed", speed);
            map.put("throughput", throughput);
            map.put("signalStrength", signalStrength);
            map.put("manufacturer", manufacturer);
            map.put("model", model);
            map.put("firmwareVersion", firmwareVersion);
            map.put("location", location);
            map.put("properties", properties);
            map.put("lastUpdated", lastUpdated);
            map.put("uptime", uptime);
            return map;
        }
        
        public Map<String, Object> getNetworkInfo() {
            Map<String, Object> networkInfo = new HashMap<>();
            networkInfo.put("ipAddress", ipAddress);
            networkInfo.put("macAddress", macAddress);
            networkInfo.put("speed", speed);
            networkInfo.put("subnetMask", properties.getOrDefault("subnetMask", "255.255.255.0"));
            networkInfo.put("gateway", properties.getOrDefault("gateway", ipAddress));
            networkInfo.put("dnsServer", properties.getOrDefault("dnsServer", "8.8.8.8"));
            return networkInfo;
        }
        
        public String getId() { return id; }
        public String getName() { return name; }
        public String getType() { return type; }
        public String getStatus() { return status; }
        public String getIpAddress() { return ipAddress; }
        public String getMacAddress() { return macAddress; }
        public int getThroughput() { return throughput; }
        public int getSignalStrength() { return signalStrength; }
        public long getUptime() { return uptime; }
        public String getLocation() { return location; }
    }
    
    // 设备操作日志类
    private static class DeviceOperationLog {
        private final String deviceId;
        private final String deviceName;
        private final String operation;
        private final String status;
        private final String message;
        private final long timestamp;
        
        public DeviceOperationLog(String deviceId, String deviceName, String operation, String status, String message) {
            this.deviceId = deviceId;
            this.deviceName = deviceName;
            this.operation = operation;
            this.status = status;
            this.message = message;
            this.timestamp = System.currentTimeMillis();
        }
        
        public Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("deviceId", deviceId);
            map.put("deviceName", deviceName);
            map.put("operation", operation);
            map.put("status", status);
            map.put("message", message);
            map.put("timestamp", timestamp);
            return map;
        }
        
        public String getDeviceId() { return deviceId; }
        public long getTimestamp() { return timestamp; }
    }
}
