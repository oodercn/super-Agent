package net.ooder.nexus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import net.ooder.nexus.model.Result;
import net.ooder.nexus.model.DeviceResult;
import net.ooder.nexus.model.SecurityStatusResult;
import net.ooder.nexus.model.DashboardResult;
import net.ooder.nexus.service.INexusService;
import net.ooder.nexus.service.NexusServiceFactory;

import java.util.*;

@RestController
@RequestMapping("/api/home")
public class HomeController {
    
    @Autowired
    private NexusServiceFactory serviceFactory;
    
    // 模拟设备数据
    private final List<Map<String, Object>> devices = new ArrayList<>();
    
    // 安全状态数据
    private final Map<String, Object> securityStatus = new HashMap<>();
    
    // 初始化数据
    public HomeController() {
        // 初始化设备数据
        devices.add(createDevice(1, "智能灯泡", "light", "客厅", "online", "2026-01-30 19:00", "192.168.1.101"));
        devices.add(createDevice(2, "智能插座", "socket", "卧室", "online", "2026-01-30 18:30", "192.168.1.102"));
        devices.add(createDevice(3, "智能门锁", "lock", "前门", "online", "2026-01-30 18:00", "192.168.1.103"));
        devices.add(createDevice(4, "摄像头", "camera", "后院", "offline", "2026-01-30 17:00", "192.168.1.104"));
        devices.add(createDevice(5, "智能窗帘", "other", "客厅", "online", "2026-01-30 16:30", "192.168.1.105"));
        
        // 初始化安全状态数据
        securityStatus.put("deviceSecurity", "good");
        securityStatus.put("networkSecurity", "good");
        securityStatus.put("systemSecurity", "warning");
        securityStatus.put("securityScore", 95);
    }
    
    private INexusService getService() {
        return serviceFactory.getService();
    }
    
    // 创建设备地图
    private Map<String, Object> createDevice(int id, String name, String type, String location, String status, String lastActive, String ip) {
        Map<String, Object> device = new HashMap<>();
        device.put("id", id);
        device.put("name", name);
        device.put("type", type);
        device.put("location", location);
        device.put("status", status);
        device.put("lastActive", lastActive);
        device.put("ip", ip);
        return device;
    }
    
    @GetMapping("/devices")
    public Result<List<DeviceResult>> getDevices(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String search
    ) {
        List<DeviceResult> filteredDevices = new ArrayList<>();
        
        for (Map<String, Object> deviceMap : devices) {
            // 按状态过滤
            if (status != null && !status.equals("all") && !deviceMap.get("status").equals(status)) {
                continue;
            }
            
            // 按类型过滤
            if (type != null && !type.equals("all") && !deviceMap.get("type").equals(type)) {
                continue;
            }
            
            // 按搜索词过滤
            if (search != null && !search.isEmpty()) {
                String searchLower = search.toLowerCase();
                String name = ((String) deviceMap.get("name")).toLowerCase();
                String location = ((String) deviceMap.get("location")).toLowerCase();
                if (!name.contains(searchLower) && !location.contains(searchLower)) {
                    continue;
                }
            }
            
            DeviceResult device = new DeviceResult();
            device.setDeviceId((int) deviceMap.get("id"));
            device.setDeviceName((String) deviceMap.get("name"));
            device.setDeviceType((String) deviceMap.get("type"));
            device.setStatus((String) deviceMap.get("status"));
            device.setIpAddress((String) deviceMap.get("ip"));
            device.setMacAddress("00:00:00:00:00:00");
            device.setLastOnline((String) deviceMap.get("lastActive"));
            device.setMessage("Device retrieved successfully");
            device.setSuccess(true);
            filteredDevices.add(device);
        }
        
        return Result.success("Devices retrieved successfully", filteredDevices);
    }
    
    @GetMapping("/devices/{id}")
    public Result<DeviceResult> getDevice(@PathVariable int id) {
        Optional<Map<String, Object>> deviceOptional = devices.stream()
                .filter(d -> d.get("id").equals(id))
                .findFirst();
        
        if (deviceOptional.isPresent()) {
            Map<String, Object> deviceMap = deviceOptional.get();
            DeviceResult device = new DeviceResult();
            device.setDeviceId((int) deviceMap.get("id"));
            device.setDeviceName((String) deviceMap.get("name"));
            device.setDeviceType((String) deviceMap.get("type"));
            device.setStatus((String) deviceMap.get("status"));
            device.setIpAddress((String) deviceMap.get("ip"));
            device.setMacAddress("00:00:00:00:00:00");
            device.setLastOnline((String) deviceMap.get("lastActive"));
            device.setMessage("Device retrieved successfully");
            device.setSuccess(true);
            return Result.success("Device retrieved successfully", device);
        } else {
            return Result.error("设备不存在");
        }
    }
    
    @PostMapping("/devices")
    public Result<DeviceResult> addDevice(@RequestBody Map<String, Object> deviceData) {
        // 生成新设备ID
        int newId = devices.stream()
                .mapToInt(d -> (int) d.get("id"))
                .max()
                .orElse(0) + 1;
        
        // 创建新设备
        Map<String, Object> newDeviceMap = new HashMap<>(deviceData);
        newDeviceMap.put("id", newId);
        newDeviceMap.put("status", "offline");
        newDeviceMap.put("lastActive", new Date().toString());
        newDeviceMap.put("ip", "192.168.1." + (100 + newId));
        
        devices.add(newDeviceMap);
        
        DeviceResult device = new DeviceResult();
        device.setDeviceId(newId);
        device.setDeviceName((String) newDeviceMap.get("name"));
        device.setDeviceType((String) newDeviceMap.get("type"));
        device.setStatus((String) newDeviceMap.get("status"));
        device.setIpAddress((String) newDeviceMap.get("ip"));
        device.setMacAddress("00:00:00:00:00:00");
        device.setLastOnline((String) newDeviceMap.get("lastActive"));
        device.setMessage("Device added successfully");
        device.setSuccess(true);
        return Result.success("Device added successfully", device);
    }
    
    @PutMapping("/devices/{id}")
    public Result<DeviceResult> updateDevice(@PathVariable int id, @RequestBody Map<String, Object> deviceData) {
        Optional<Map<String, Object>> deviceOptional = devices.stream()
                .filter(d -> d.get("id").equals(id))
                .findFirst();
        
        if (deviceOptional.isPresent()) {
            Map<String, Object> deviceMap = deviceOptional.get();
            deviceMap.putAll(deviceData);
            deviceMap.put("lastActive", new Date().toString());
            
            DeviceResult device = new DeviceResult();
            device.setDeviceId((int) deviceMap.get("id"));
            device.setDeviceName((String) deviceMap.get("name"));
            device.setDeviceType((String) deviceMap.get("type"));
            device.setStatus((String) deviceMap.get("status"));
            device.setIpAddress((String) deviceMap.get("ip"));
            device.setMacAddress("00:00:00:00:00:00");
            device.setLastOnline((String) deviceMap.get("lastActive"));
            device.setMessage("Device updated successfully");
            device.setSuccess(true);
            return Result.success("Device updated successfully", device);
        } else {
            return Result.error("设备不存在");
        }
    }
    
    @DeleteMapping("/devices/{id}")
    public Result<DeviceResult> deleteDevice(@PathVariable int id) {
        boolean removed = devices.removeIf(d -> d.get("id").equals(id));
        
        if (removed) {
            DeviceResult device = new DeviceResult();
            device.setDeviceId(id);
            device.setMessage("设备删除成功");
            device.setSuccess(true);
            return Result.success("Device deleted successfully", device);
        } else {
            return Result.error("设备不存在");
        }
    }
    
    @PostMapping("/devices/{id}/control")
    public Result<DeviceResult> controlDevice(@PathVariable int id, @RequestBody Map<String, Object> controlData) {
        Optional<Map<String, Object>> deviceOptional = devices.stream()
                .filter(d -> d.get("id").equals(id))
                .findFirst();
        
        if (deviceOptional.isPresent()) {
            Map<String, Object> deviceMap = deviceOptional.get();
            String action = (String) controlData.get("action");
            
            // 模拟设备控制
            deviceMap.put("status", "online");
            deviceMap.put("lastActive", new Date().toString());
            
            DeviceResult device = new DeviceResult();
            device.setDeviceId((int) deviceMap.get("id"));
            device.setDeviceName((String) deviceMap.get("name"));
            device.setDeviceType((String) deviceMap.get("type"));
            device.setStatus((String) deviceMap.get("status"));
            device.setIpAddress((String) deviceMap.get("ip"));
            device.setMacAddress("00:00:00:00:00:00");
            device.setLastOnline((String) deviceMap.get("lastActive"));
            device.setMessage("设备控制成功: " + action);
            device.setSuccess(true);
            return Result.success("Device controlled successfully", device);
        } else {
            return Result.error("设备不存在");
        }
    }
    
    @GetMapping("/security")
    public Result<SecurityStatusResult> getSecurityStatus() {
        SecurityStatusResult result = new SecurityStatusResult();
        result.setStatus((String) securityStatus.get("deviceSecurity"));
        result.setSecure(((String) securityStatus.get("deviceSecurity")).equals("good") && 
                       ((String) securityStatus.get("networkSecurity")).equals("good") && 
                       ((String) securityStatus.get("systemSecurity")).equals("good"));
        result.setActiveThreats(0);
        result.setPendingUpdates(0);
        result.setLastScanTime(new Date().toString());
        result.setMessage("Security status retrieved successfully");
        return Result.success("Security status retrieved successfully", result);
    }
    
    @GetMapping("/dashboard")
    public Result<DashboardResult> getHomeDashboard() {
        int totalDevices = devices.size();
        int onlineDevices = (int) devices.stream()
                .filter(d -> d.get("status").equals("online"))
                .count();
        int offlineDevices = totalDevices - onlineDevices;
        
        DashboardResult result = new DashboardResult();
        result.setTotalDevices(totalDevices);
        result.setOnlineDevices(onlineDevices);
        result.setOfflineDevices(offlineDevices);
        result.setSystemStatus("normal");
        result.setActiveAlerts(0);
        result.setPendingTasks(0);
        result.setLastUpdateTime(new Date().toString());
        result.setMessage("Home dashboard retrieved successfully");
        return Result.success("Home dashboard retrieved successfully", result);
    }
}
