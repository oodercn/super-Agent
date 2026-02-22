package net.ooder.nexus.adapter.inbound.controller.home;

import net.ooder.nexus.common.ResultModel;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 首页控制器
 * 处理设备管理、安全状态、仪表盘等操作
 *
 * @author ooder Team
 * @version 0.7.0
 * @since 0.7.0
 */
@RestController
@RequestMapping("/api/home")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS})
public class HomeController {

    private final List<Map<String, Object>> devices = new ArrayList<>();
    private final Map<String, Object> securityStatus = new HashMap<>();

    public HomeController() {
        devices.add(createDevice(1, "智能灯泡", "light", "客厅", "online", "2026-01-30 19:00", "192.168.1.101"));
        devices.add(createDevice(2, "智能插座", "socket", "卧室", "online", "2026-01-30 18:30", "192.168.1.102"));
        devices.add(createDevice(3, "智能门锁", "lock", "前门", "online", "2026-01-30 18:00", "192.168.1.103"));
        devices.add(createDevice(4, "摄像头", "camera", "后院", "offline", "2026-01-30 17:00", "192.168.1.104"));
        devices.add(createDevice(5, "智能窗帘", "other", "客厅", "online", "2026-01-30 16:30", "192.168.1.105"));

        securityStatus.put("deviceSecurity", "good");
        securityStatus.put("networkSecurity", "good");
        securityStatus.put("systemSecurity", "warning");
        securityStatus.put("securityScore", 95);
    }

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

    @PostMapping("/devices/list")
    @ResponseBody
    public ResultModel<List<Map<String, Object>>> getDevices(@RequestBody Map<String, Object> request) {
        String status = (String) request.get("status");
        String type = (String) request.get("type");
        String search = (String) request.get("search");

        List<Map<String, Object>> filteredDevices = new ArrayList<>();

        for (Map<String, Object> deviceMap : devices) {
            if (status != null && !status.equals("all") && !deviceMap.get("status").equals(status)) {
                continue;
            }

            if (type != null && !type.equals("all") && !deviceMap.get("type").equals(type)) {
                continue;
            }

            if (search != null && !search.isEmpty()) {
                String searchLower = search.toLowerCase();
                String name = ((String) deviceMap.get("name")).toLowerCase();
                String location = ((String) deviceMap.get("location")).toLowerCase();
                if (!name.contains(searchLower) && !location.contains(searchLower)) {
                    continue;
                }
            }

            filteredDevices.add(deviceMap);
        }

        return ResultModel.success(filteredDevices);
    }

    @PostMapping("/devices/detail")
    @ResponseBody
    public ResultModel<Map<String, Object>> getDevice(@RequestBody Map<String, Integer> request) {
        int id = request.get("id");

        Optional<Map<String, Object>> deviceOptional = devices.stream()
                .filter(d -> d.get("id").equals(id))
                .findFirst();

        if (deviceOptional.isPresent()) {
            return ResultModel.success(deviceOptional.get());
        } else {
            return ResultModel.error("设备不存在", 404);
        }
    }

    @PostMapping("/devices/create")
    @ResponseBody
    public ResultModel<Map<String, Object>> addDevice(@RequestBody Map<String, Object> deviceData) {
        int newId = devices.stream()
                .mapToInt(d -> (int) d.get("id"))
                .max()
                .orElse(0) + 1;

        Map<String, Object> newDeviceMap = new HashMap<>(deviceData);
        newDeviceMap.put("id", newId);
        newDeviceMap.put("status", "offline");
        newDeviceMap.put("lastActive", new Date().toString());
        newDeviceMap.put("ip", "192.168.1." + (100 + newId));

        devices.add(newDeviceMap);

        return ResultModel.success(newDeviceMap);
    }

    @PostMapping("/devices/update")
    @ResponseBody
    public ResultModel<Map<String, Object>> updateDevice(@RequestBody Map<String, Object> request) {
        int id = (Integer) request.get("id");
        Map<String, Object> deviceData = (Map<String, Object>) request.get("device");

        Optional<Map<String, Object>> deviceOptional = devices.stream()
                .filter(d -> d.get("id").equals(id))
                .findFirst();

        if (deviceOptional.isPresent()) {
            Map<String, Object> deviceMap = deviceOptional.get();
            deviceMap.putAll(deviceData);
            deviceMap.put("lastActive", new Date().toString());

            return ResultModel.success(deviceMap);
        } else {
            return ResultModel.error("设备不存在", 404);
        }
    }

    @PostMapping("/devices/delete")
    @ResponseBody
    public ResultModel<Boolean> deleteDevice(@RequestBody Map<String, Integer> request) {
        int id = request.get("id");
        boolean removed = devices.removeIf(d -> d.get("id").equals(id));

        if (removed) {
            return ResultModel.success(true);
        } else {
            return ResultModel.error("设备不存在", 404);
        }
    }

    @PostMapping("/devices/control")
    @ResponseBody
    public ResultModel<Map<String, Object>> controlDevice(@RequestBody Map<String, Object> request) {
        int id = (Integer) request.get("id");
        String action = (String) request.get("action");

        Optional<Map<String, Object>> deviceOptional = devices.stream()
                .filter(d -> d.get("id").equals(id))
                .findFirst();

        if (deviceOptional.isPresent()) {
            Map<String, Object> deviceMap = deviceOptional.get();
            deviceMap.put("status", "online");
            deviceMap.put("lastActive", new Date().toString());

            return ResultModel.success(deviceMap);
        } else {
            return ResultModel.error("设备不存在", 404);
        }
    }

    @PostMapping("/security")
    @ResponseBody
    public ResultModel<Map<String, Object>> getSecurityStatus() {
        Map<String, Object> result = new HashMap<>();
        result.put("deviceSecurity", securityStatus.get("deviceSecurity"));
        result.put("networkSecurity", securityStatus.get("networkSecurity"));
        result.put("systemSecurity", securityStatus.get("systemSecurity"));
        result.put("securityScore", securityStatus.get("securityScore"));
        result.put("isSecure", "good".equals(securityStatus.get("deviceSecurity")) &&
                               "good".equals(securityStatus.get("networkSecurity")) &&
                               "good".equals(securityStatus.get("systemSecurity")));
        result.put("activeThreats", 0);
        result.put("pendingUpdates", 0);
        result.put("lastScanTime", new Date().toString());

        return ResultModel.success(result);
    }

    @PostMapping("/dashboard")
    @ResponseBody
    public ResultModel<Map<String, Object>> getHomeDashboard() {
        int totalDevices = devices.size();
        int onlineDevices = (int) devices.stream()
                .filter(d -> d.get("status").equals("online"))
                .count();
        int offlineDevices = totalDevices - onlineDevices;

        Map<String, Object> result = new HashMap<>();
        result.put("totalDevices", totalDevices);
        result.put("onlineDevices", onlineDevices);
        result.put("offlineDevices", offlineDevices);
        result.put("systemStatus", "normal");
        result.put("activeAlerts", 0);
        result.put("pendingTasks", 0);
        result.put("lastUpdateTime", new Date().toString());

        return ResultModel.success(result);
    }
}
