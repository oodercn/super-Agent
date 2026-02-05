package net.ooder.nexus.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import net.ooder.nexus.model.Result;
import net.ooder.nexus.model.device.Device;
import net.ooder.nexus.model.device.DeviceOperationLog;
import net.ooder.nexus.model.device.DeviceListResult;
import net.ooder.nexus.model.device.DeviceDetailResult;
import net.ooder.nexus.model.device.DeviceControlResult;
import net.ooder.nexus.model.device.DeviceLogsResult;
import net.ooder.nexus.model.device.DeviceTypesResult;
import net.ooder.nexus.service.INexusService;
import net.ooder.nexus.service.NexusServiceFactory;

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
    
    @Autowired
    private NexusServiceFactory serviceFactory;
    
    // 设备存储
    private final ConcurrentHashMap<String, Device> devices = new ConcurrentHashMap<>();
    
    // 设备操作日志
    private final List<DeviceOperationLog> operationLogs = new ArrayList<>();
    
    // 初始化默认设备
    public DeviceController() {
        initializeDefaultDevices();
    }
    
    private INexusService getService() {
        return serviceFactory.getService();
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
    public Result<DeviceListResult> getDevices(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String location) {
        log.info("Get devices requested: status={}, type={}, location={}", status, type, location);
        
        try {
            List<Device> filteredDevices = devices.values().stream()
                    .filter(device -> (status == null || device.getStatus().equals(status)))
                    .filter(device -> (type == null || device.getType().equals(type)))
                    .filter(device -> (location == null || device.getLocation().equals(location)))
                    .collect(Collectors.toList());
            
            // 转换为响应格式
            List<Map<String, Object>> deviceList = new ArrayList<>();
            for (Device device : filteredDevices) {
                Map<String, Object> summary = new HashMap<>();
                summary.put("id", device.getId());
                summary.put("name", device.getName());
                summary.put("type", device.getType());
                summary.put("status", device.getStatus());
                summary.put("location", device.getLocation());
                summary.put("poweredOn", device.isPoweredOn());
                summary.put("lastUpdated", device.getLastUpdated());
                deviceList.add(summary);
            }
            
            // 设备统计
            Map<String, Object> stats = new HashMap<>();
            stats.put("total", devices.size());
            stats.put("online", devices.values().stream().filter(d -> "online".equals(d.getStatus())).count());
            stats.put("offline", devices.values().stream().filter(d -> "offline".equals(d.getStatus())).count());
            stats.put("types", devices.values().stream().map(Device::getType).distinct().count());
            
            DeviceListResult result = new DeviceListResult();
            result.setDevices(deviceList);
            result.setStats(stats);
            
            return Result.success("Devices retrieved successfully", result);
        } catch (Exception e) {
            log.error("Error getting devices: {}", e.getMessage(), e);
            return Result.error("Failed to get devices: " + e.getMessage());
        }
    }
    
    /**
     * 获取设备详情
     */
    @GetMapping("/detail/{deviceId}")
    public Result<DeviceDetailResult> getDeviceDetail(@PathVariable String deviceId) {
        log.info("Get device detail requested: {}", deviceId);
        
        try {
            Device device = devices.get(deviceId);
            if (device == null) {
                return Result.error("Device not found");
            }
            
            DeviceDetailResult detail = new DeviceDetailResult();
            detail.setId(device.getId());
            detail.setName(device.getName());
            detail.setType(device.getType());
            detail.setStatus(device.getStatus());
            detail.setLocation(device.getLocation());
            detail.setPowerConsumption(device.getPowerConsumption());
            detail.setPoweredOn(device.isPoweredOn());
            detail.setProperties(device.getProperties());
            detail.setInstallationDate(device.getInstallationDate());
            detail.setManufacturer(device.getManufacturer());
            detail.setFirmwareVersion(device.getFirmwareVersion());
            detail.setLastUpdated(device.getLastUpdated());
            
            return Result.success("Device detail retrieved successfully", detail);
        } catch (Exception e) {
            log.error("Error getting device detail: {}", e.getMessage(), e);
            return Result.error("Failed to get device detail: " + e.getMessage());
        }
    }
    
    /**
     * 控制设备
     */
    @PostMapping("/control/{deviceId}")
    public Result<DeviceControlResult> controlDevice(@PathVariable String deviceId, @RequestBody Map<String, Object> controlData) {
        log.info("Control device requested: {}, data: {}", deviceId, controlData);
        
        try {
            Device device = devices.get(deviceId);
            if (device == null) {
                return Result.error("Device not found");
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
            
            DeviceControlResult data = new DeviceControlResult();
            data.setDeviceId(deviceId);
            data.setDeviceName(device.getName());
            data.setDeviceStatus(device.getStatus());
            data.setDevicePower(device.isPoweredOn());
            
            if ("error".equals(result)) {
                return Result.error(message);
            } else {
                return Result.success(message, data);
            }
        } catch (Exception e) {
            log.error("Error controlling device: {}", e.getMessage(), e);
            return Result.error("Failed to control device: " + e.getMessage());
        }
    }
    
    /**
     * 获取设备操作日志
     */
    @GetMapping("/logs")
    public Result<DeviceLogsResult> getDeviceLogs(
            @RequestParam(defaultValue = "50") int limit,
            @RequestParam(required = false) String deviceId) {
        log.info("Get device operation logs requested: limit={}, deviceId={}", limit, deviceId);
        
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
            
            DeviceLogsResult result = new DeviceLogsResult();
            result.setLogs(logList);
            result.setCount(logList.size());
            
            return Result.success("Device operation logs retrieved successfully", result);
        } catch (Exception e) {
            log.error("Error getting device logs: {}", e.getMessage(), e);
            return Result.error("Failed to get device logs: " + e.getMessage());
        }
    }
    
    /**
     * 获取设备类型列表
     */
    @GetMapping("/types")
    public Result<DeviceTypesResult> getDeviceTypes() {
        log.info("Get device types requested");
        
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
            
            DeviceTypesResult result = new DeviceTypesResult();
            result.setTypes(types);
            result.setTypeCounts(typeCounts);
            
            return Result.success("Device types retrieved successfully", result);
        } catch (Exception e) {
            log.error("Error getting device types: {}", e.getMessage(), e);
            return Result.error("Failed to get device types: " + e.getMessage());
        }
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
    

}
