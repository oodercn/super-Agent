package net.ooder.mcpagent.controller;

import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/home")
public class HomeController {
    
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
    public Map<String, Object> getDevices(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String search
    ) {
        List<Map<String, Object>> filteredDevices = new ArrayList<>(devices);
        
        // 按状态过滤
        if (status != null && !status.equals("all")) {
            filteredDevices.removeIf(device -> !device.get("status").equals(status));
        }
        
        // 按类型过滤
        if (type != null && !type.equals("all")) {
            filteredDevices.removeIf(device -> !device.get("type").equals(type));
        }
        
        // 按搜索词过滤
        if (search != null && !search.isEmpty()) {
            String searchLower = search.toLowerCase();
            filteredDevices.removeIf(device -> {
                String name = ((String) device.get("name")).toLowerCase();
                String location = ((String) device.get("location")).toLowerCase();
                return !name.contains(searchLower) && !location.contains(searchLower);
            });
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("status", "success");
        result.put("devices", filteredDevices);
        result.put("total", filteredDevices.size());
        return result;
    }
    
    @GetMapping("/devices/{id}")
    public Map<String, Object> getDevice(@PathVariable int id) {
        Optional<Map<String, Object>> device = devices.stream()
                .filter(d -> d.get("id").equals(id))
                .findFirst();
        
        Map<String, Object> result = new HashMap<>();
        if (device.isPresent()) {
            result.put("status", "success");
            result.put("device", device.get());
        } else {
            result.put("status", "error");
            result.put("message", "设备不存在");
        }
        return result;
    }
    
    @PostMapping("/devices")
    public Map<String, Object> addDevice(@RequestBody Map<String, Object> deviceData) {
        // 生成新设备ID
        int newId = devices.stream()
                .mapToInt(d -> (int) d.get("id"))
                .max()
                .orElse(0) + 1;
        
        // 创建新设备
        Map<String, Object> newDevice = new HashMap<>(deviceData);
        newDevice.put("id", newId);
        newDevice.put("status", "offline");
        newDevice.put("lastActive", new Date().toString());
        
        devices.add(newDevice);
        
        Map<String, Object> result = new HashMap<>();
        result.put("status", "success");
        result.put("device", newDevice);
        return result;
    }
    
    @PutMapping("/devices/{id}")
    public Map<String, Object> updateDevice(@PathVariable int id, @RequestBody Map<String, Object> deviceData) {
        Optional<Map<String, Object>> deviceOptional = devices.stream()
                .filter(d -> d.get("id").equals(id))
                .findFirst();
        
        Map<String, Object> result = new HashMap<>();
        if (deviceOptional.isPresent()) {
            Map<String, Object> device = deviceOptional.get();
            device.putAll(deviceData);
            device.put("lastActive", new Date().toString());
            
            result.put("status", "success");
            result.put("device", device);
        } else {
            result.put("status", "error");
            result.put("message", "设备不存在");
        }
        return result;
    }
    
    @DeleteMapping("/devices/{id}")
    public Map<String, Object> deleteDevice(@PathVariable int id) {
        boolean removed = devices.removeIf(d -> d.get("id").equals(id));
        
        Map<String, Object> result = new HashMap<>();
        if (removed) {
            result.put("status", "success");
            result.put("message", "设备删除成功");
        } else {
            result.put("status", "error");
            result.put("message", "设备不存在");
        }
        return result;
    }
    
    @PostMapping("/devices/{id}/control")
    public Map<String, Object> controlDevice(@PathVariable int id, @RequestBody Map<String, Object> controlData) {
        Optional<Map<String, Object>> deviceOptional = devices.stream()
                .filter(d -> d.get("id").equals(id))
                .findFirst();
        
        Map<String, Object> result = new HashMap<>();
        if (deviceOptional.isPresent()) {
            Map<String, Object> device = deviceOptional.get();
            String action = (String) controlData.get("action");
            
            // 模拟设备控制
            device.put("status", "online");
            device.put("lastActive", new Date().toString());
            
            result.put("status", "success");
            result.put("message", "设备控制成功");
            result.put("action", action);
            result.put("device", device);
        } else {
            result.put("status", "error");
            result.put("message", "设备不存在");
        }
        return result;
    }
    
    @GetMapping("/security")
    public Map<String, Object> getSecurityStatus() {
        Map<String, Object> result = new HashMap<>();
        result.put("status", "success");
        result.put("security", securityStatus);
        return result;
    }
    
    @GetMapping("/dashboard")
    public Map<String, Object> getHomeDashboard() {
        int totalDevices = devices.size();
        int onlineDevices = (int) devices.stream()
                .filter(d -> d.get("status").equals("online"))
                .count();
        int offlineDevices = totalDevices - onlineDevices;
        
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> dashboard = new HashMap<>();
        dashboard.put("totalDevices", totalDevices);
        dashboard.put("onlineDevices", onlineDevices);
        dashboard.put("offlineDevices", offlineDevices);
        dashboard.put("securityScore", securityStatus.get("securityScore"));
        dashboard.put("networkUsage", "45%");
        
        result.put("status", "success");
        result.put("dashboard", dashboard);
        return result;
    }
}
