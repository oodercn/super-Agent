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
@RequestMapping("/api/devices")
public class DeviceController {

    private static final Logger log = LoggerFactory.getLogger(DeviceController.class);
    
    // 设备存储
    private final ConcurrentHashMap<String, Device> devices = new ConcurrentHashMap<>();
    
    // 设备操作日志
    private final List<DeviceOperationLog> operationLogs = new ArrayList<>();
    
    // 初始化默认设备
    public DeviceController() {
        initializeDefaultDevices();
    }
    
    private void initializeDefaultDevices() {
        // 添加默认设备
        Map<String, Object> props1 = new ConcurrentHashMap<>();
        props1.put("brightness", 80);
        props1.put("color", "#FFFFFF");
        props1.put("mode", "normal");
        devices.put("device-1", new Device(
            "device-1", "智能灯泡", "light", "online", "客厅", 
            50, true, props1,
            "2024-01-01", "SmartHome Inc.", "v1.0.0"
        ));
        
        Map<String, Object> props2 = new ConcurrentHashMap<>();
        props2.put("power", 120);
        props2.put("voltage", 220);
        props2.put("current", 0.5);
        devices.put("device-2", new Device(
            "device-2", "智能插座", "socket", "online", "卧室", 
            10, true, props2,
            "2024-01-02", "SmartHome Inc.", "v1.0.0"
        ));
        
        Map<String, Object> props3 = new ConcurrentHashMap<>();
        props3.put("battery", 75);
        props3.put("locked", true);
        props3.put("lastUnlock", "2024-01-10 10:30");
        devices.put("device-3", new Device(
            "device-3", "智能门锁", "lock", "offline", "前门", 
            20, false, props3,
            "2024-01-03", "SecureHome Ltd.", "v2.1.0"
        ));
        
        Map<String, Object> props4 = new ConcurrentHashMap<>();
        props4.put("temperature", 25.5);
        props4.put("humidity", 45);
        props4.put("battery", 90);
        devices.put("device-4", new Device(
            "device-4", "温湿度传感器", "sensor", "online", "客厅", 
            5, true, props4,
            "2024-01-04", "SensorTech Co.", "v1.5.0"
        ));
    }
    
    /**
     * 获取设备列表
     */
    @GetMapping("/list")
    public Map<String, Object> getDevices(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String location) {
        log.info("Get devices requested: status={}, type={}, location={}", status, type, location);
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Device> filteredDevices = devices.values().stream()
                    .filter(device -> (status == null || device.getStatus().equals(status)))
                    .filter(device -> (type == null || device.getType().equals(type)))
                    .filter(device -> (location == null || device.getLocation().equals(location)))
                    .collect(Collectors.toList());
            
            // 转换为响应格式
            List<Map<String, Object>> deviceList = new ArrayList<>();
            for (Device device : filteredDevices) {
                deviceList.add(device.toSummaryMap());
            }
            
            // 设备统计
            Map<String, Object> stats = new HashMap<>();
            stats.put("total", devices.size());
            stats.put("online", devices.values().stream().filter(d -> "online".equals(d.getStatus())).count());
            stats.put("offline", devices.values().stream().filter(d -> "offline".equals(d.getStatus())).count());
            stats.put("types", devices.values().stream().map(Device::getType).distinct().count());
            
            response.put("status", "success");
            response.put("message", "Devices retrieved successfully");
            response.put("data", deviceList);
            response.put("stats", stats);
            response.put("timestamp", System.currentTimeMillis());
            
        } catch (Exception e) {
            log.error("Error getting devices: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "DEVICES_RETRIEVAL_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        }
        
        return response;
    }
    
    /**
     * 获取设备详情
     */
    @GetMapping("/detail/{deviceId}")
    public Map<String, Object> getDeviceDetail(@PathVariable String deviceId) {
        log.info("Get device detail requested: {}", deviceId);
        Map<String, Object> response = new HashMap<>();
        
        try {
            Device device = devices.get(deviceId);
            if (device == null) {
                response.put("status", "error");
                response.put("message", "Device not found");
                response.put("code", "DEVICE_NOT_FOUND");
                response.put("timestamp", System.currentTimeMillis());
                return response;
            }
            
            response.put("status", "success");
            response.put("message", "Device detail retrieved successfully");
            response.put("data", device.toDetailMap());
            response.put("timestamp", System.currentTimeMillis());
            
        } catch (Exception e) {
            log.error("Error getting device detail: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "DEVICE_DETAIL_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        }
        
        return response;
    }
    
    /**
     * 控制设备
     */
    @PostMapping("/control/{deviceId}")
    public Map<String, Object> controlDevice(@PathVariable String deviceId, @RequestBody Map<String, Object> controlData) {
        log.info("Control device requested: {}, data: {}", deviceId, controlData);
        Map<String, Object> response = new HashMap<>();
        
        try {
            Device device = devices.get(deviceId);
            if (device == null) {
                response.put("status", "error");
                response.put("message", "Device not found");
                response.put("code", "DEVICE_NOT_FOUND");
                response.put("timestamp", System.currentTimeMillis());
                return response;
            }
            
            // 处理控制命令
            String command = (String) controlData.get("command");
            Map<String, Object> parameters = (Map<String, Object>) controlData.getOrDefault("parameters", new HashMap<>());
            
            String result = "success";
            String message = "Device controlled successfully";
            
            switch (command) {
                case "turnOn":
                    device.turnOn();
                    message = "Device turned on successfully";
                    break;
                case "turnOff":
                    device.turnOff();
                    message = "Device turned off successfully";
                    break;
                case "setProperties":
                    device.setProperties(parameters);
                    message = "Device properties updated successfully";
                    break;
                case "reset":
                    device.reset();
                    message = "Device reset successfully";
                    break;
                default:
                    result = "error";
                    message = "Unknown command";
            }
            
            // 记录操作日志
            addOperationLog(deviceId, device.getName(), command, result, message);
            
            response.put("status", result);
            response.put("message", message);
            response.put("deviceId", deviceId);
            response.put("deviceName", device.getName());
            response.put("deviceStatus", device.getStatus());
            response.put("devicePower", device.isPoweredOn());
            response.put("timestamp", System.currentTimeMillis());
            
        } catch (Exception e) {
            log.error("Error controlling device: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "DEVICE_CONTROL_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        }
        
        return response;
    }
    
    /**
     * 获取设备操作日志
     */
    @GetMapping("/logs")
    public Map<String, Object> getDeviceLogs(
            @RequestParam(defaultValue = "50") int limit,
            @RequestParam(required = false) String deviceId) {
        log.info("Get device operation logs requested: limit={}, deviceId={}", limit, deviceId);
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<DeviceOperationLog> filteredLogs = new ArrayList<>();
            for (DeviceOperationLog log : operationLogs) {
                if (deviceId == null || log.getDeviceId().equals(deviceId)) {
                    filteredLogs.add(log);
                }
            }
            
            // 限制数量并按时间倒序排序
            List<DeviceOperationLog> pagedLogs = filteredLogs.stream()
                    .sorted((l1, l2) -> Long.compare(l2.getTimestamp(), l1.getTimestamp()))
                    .limit(limit)
                    .collect(Collectors.toList());
            
            // 转换为响应格式
            List<Map<String, Object>> logList = new ArrayList<>();
            for (DeviceOperationLog log : pagedLogs) {
                logList.add(log.toMap());
            }
            
            response.put("status", "success");
            response.put("message", "Device operation logs retrieved successfully");
            response.put("data", logList);
            response.put("count", logList.size());
            response.put("timestamp", System.currentTimeMillis());
            
        } catch (Exception e) {
            log.error("Error getting device logs: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "LOGS_RETRIEVAL_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        }
        
        return response;
    }
    
    /**
     * 获取设备类型列表
     */
    @GetMapping("/types")
    public Map<String, Object> getDeviceTypes() {
        log.info("Get device types requested");
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<String> types = devices.values().stream()
                    .map(Device::getType)
                    .distinct()
                    .collect(Collectors.toList());
            
            // 统计每种类型的设备数量
            Map<String, Long> typeCounts = new HashMap<>();
            for (String type : types) {
                typeCounts.put(type, devices.values().stream().filter(d -> type.equals(d.getType())).count());
            }
            
            response.put("status", "success");
            response.put("message", "Device types retrieved successfully");
            response.put("data", types);
            response.put("typeCounts", typeCounts);
            response.put("timestamp", System.currentTimeMillis());
            
        } catch (Exception e) {
            log.error("Error getting device types: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "TYPES_RETRIEVAL_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        }
        
        return response;
    }
    
    /**
     * 添加设备操作日志
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
    
    // 设备类
    private static class Device {
        private final String id;
        private final String name;
        private final String type;
        private String status;
        private final String location;
        private final int powerConsumption;
        private boolean poweredOn;
        private Map<String, Object> properties;
        private final String installationDate;
        private final String manufacturer;
        private final String firmwareVersion;
        private long lastUpdated;
        
        public Device(String id, String name, String type, String status, String location,
                     int powerConsumption, boolean poweredOn, Map<String, Object> properties,
                     String installationDate, String manufacturer, String firmwareVersion) {
            this.id = id;
            this.name = name;
            this.type = type;
            this.status = status;
            this.location = location;
            this.powerConsumption = powerConsumption;
            this.poweredOn = poweredOn;
            this.properties = properties;
            this.installationDate = installationDate;
            this.manufacturer = manufacturer;
            this.firmwareVersion = firmwareVersion;
            this.lastUpdated = System.currentTimeMillis();
        }
        
        public void turnOn() {
            this.poweredOn = true;
            this.status = "online";
            this.lastUpdated = System.currentTimeMillis();
        }
        
        public void turnOff() {
            this.poweredOn = false;
            this.lastUpdated = System.currentTimeMillis();
        }
        
        public void setProperties(Map<String, Object> properties) {
            this.properties.putAll(properties);
            this.lastUpdated = System.currentTimeMillis();
        }
        
        public void reset() {
            this.properties.clear();
            this.poweredOn = false;
            this.lastUpdated = System.currentTimeMillis();
        }
        
        public Map<String, Object> toSummaryMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("id", id);
            map.put("name", name);
            map.put("type", type);
            map.put("status", status);
            map.put("location", location);
            map.put("poweredOn", poweredOn);
            map.put("lastUpdated", lastUpdated);
            return map;
        }
        
        public Map<String, Object> toDetailMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("id", id);
            map.put("name", name);
            map.put("type", type);
            map.put("status", status);
            map.put("location", location);
            map.put("powerConsumption", powerConsumption);
            map.put("poweredOn", poweredOn);
            map.put("properties", properties);
            map.put("installationDate", installationDate);
            map.put("manufacturer", manufacturer);
            map.put("firmwareVersion", firmwareVersion);
            map.put("lastUpdated", lastUpdated);
            return map;
        }
        
        public String getId() { return id; }
        public String getName() { return name; }
        public String getType() { return type; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public boolean isPoweredOn() { return poweredOn; }
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
