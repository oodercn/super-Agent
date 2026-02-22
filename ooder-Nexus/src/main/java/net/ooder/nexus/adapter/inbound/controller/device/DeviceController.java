package net.ooder.nexus.adapter.inbound.controller.device;

import net.ooder.config.ResultModel;
import net.ooder.config.ListResultModel;
import net.ooder.nexus.domain.end.model.Device;
import net.ooder.nexus.domain.end.model.DeviceOperationLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/devices")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS})
public class DeviceController {

    private static final Logger log = LoggerFactory.getLogger(DeviceController.class);

    private final ConcurrentHashMap<String, Device> devices = new ConcurrentHashMap<>();
    private final List<DeviceOperationLog> operationLogs = new ArrayList<>();

    public DeviceController() {
        initializeDefaultDevices();
    }

    private void initializeDefaultDevices() {
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

    @PostMapping("/list")
    @ResponseBody
    public ResultModel<Map<String, Object>> getDevices(@RequestBody(required = false) Map<String, Object> request) {
        String status = request != null ? (String) request.get("status") : null;
        String type = request != null ? (String) request.get("type") : null;
        String location = request != null ? (String) request.get("location") : null;

        log.info("Get devices requested: status={}, type={}, location={}", status, type, location);

        ResultModel<Map<String, Object>> result = new ResultModel<>();
        try {
            List<Device> filteredDevices = devices.values().stream()
                    .filter(device -> (status == null || device.getStatus().equals(status)))
                    .filter(device -> (type == null || device.getType().equals(type)))
                    .filter(device -> (location == null || device.getLocation().equals(location)))
                    .collect(Collectors.toList());

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
                if (device.getProperties() != null) {
                    summary.put("ip", device.getProperties().get("ip"));
                    summary.put("mac", device.getProperties().get("mac"));
                }
                deviceList.add(summary);
            }

            Map<String, Object> stats = new HashMap<>();
            stats.put("total", devices.size());
            stats.put("online", devices.values().stream().filter(d -> "online".equals(d.getStatus())).count());
            stats.put("offline", devices.values().stream().filter(d -> "offline".equals(d.getStatus())).count());
            stats.put("types", devices.values().stream().map(Device::getType).distinct().count());

            Map<String, Object> data = new HashMap<>();
            data.put("devices", deviceList);
            data.put("stats", stats);

            result.setData(data);
            result.setRequestStatus(200);
            result.setMessage("获取成功");
        } catch (Exception e) {
            log.error("Error getting devices: {}", e.getMessage(), e);
            result.setRequestStatus(500);
            result.setMessage("获取设备列表失败: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/detail")
    @ResponseBody
    public ResultModel<Map<String, Object>> getDeviceDetail(@RequestBody Map<String, String> request) {
        String deviceId = request.get("deviceId");
        log.info("Get device detail requested: {}", deviceId);

        ResultModel<Map<String, Object>> result = new ResultModel<>();
        try {
            Device device = devices.get(deviceId);
            if (device == null) {
                result.setRequestStatus(404);
                result.setMessage("设备不存在");
                return result;
            }

            Map<String, Object> detail = new HashMap<>();
            detail.put("id", device.getId());
            detail.put("name", device.getName());
            detail.put("type", device.getType());
            detail.put("status", device.getStatus());
            detail.put("location", device.getLocation());
            detail.put("powerConsumption", device.getPowerConsumption());
            detail.put("poweredOn", device.isPoweredOn());
            detail.put("properties", device.getProperties());
            detail.put("installationDate", device.getInstallationDate());
            detail.put("manufacturer", device.getManufacturer());
            detail.put("firmwareVersion", device.getFirmwareVersion());
            detail.put("lastUpdated", device.getLastUpdated());

            result.setData(detail);
            result.setRequestStatus(200);
            result.setMessage("获取成功");
        } catch (Exception e) {
            log.error("Error getting device detail: {}", e.getMessage(), e);
            result.setRequestStatus(500);
            result.setMessage("获取设备详情失败: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/control")
    @ResponseBody
    public ResultModel<Map<String, Object>> controlDevice(@RequestBody Map<String, Object> request) {
        String deviceId = (String) request.get("deviceId");
        String command = (String) request.get("command");
        @SuppressWarnings("unchecked")
        Map<String, Object> parameters = (Map<String, Object>) request.getOrDefault("parameters", new HashMap<>());

        log.info("Control device requested: {}, command: {}", deviceId, command);

        ResultModel<Map<String, Object>> result = new ResultModel<>();
        try {
            Device device = devices.get(deviceId);
            if (device == null) {
                result.setRequestStatus(404);
                result.setMessage("设备不存在");
                return result;
            }

            String opResult = "success";
            String message = "设备控制成功";

            switch (command) {
                case "turnOn":
                    device.turnOn();
                    message = "设备已开启";
                    break;
                case "turnOff":
                    device.turnOff();
                    message = "设备已关闭";
                    break;
                case "setProperties":
                    device.setProperties(parameters);
                    message = "设备属性已更新";
                    break;
                case "reset":
                    device.reset();
                    message = "设备已重置";
                    break;
                default:
                    opResult = "error";
                    message = "未知命令";
            }

            addOperationLog(deviceId, device.getName(), command, opResult, message);

            Map<String, Object> data = new HashMap<>();
            data.put("deviceId", deviceId);
            data.put("deviceName", device.getName());
            data.put("deviceStatus", device.getStatus());
            data.put("devicePower", device.isPoweredOn());

            if ("error".equals(opResult)) {
                result.setRequestStatus(400);
                result.setMessage(message);
            } else {
                result.setData(data);
                result.setRequestStatus(200);
                result.setMessage(message);
            }
        } catch (Exception e) {
            log.error("Error controlling device: {}", e.getMessage(), e);
            result.setRequestStatus(500);
            result.setMessage("控制设备失败: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/logs")
    @ResponseBody
    public ListResultModel<List<Map<String, Object>>> getDeviceLogs(@RequestBody Map<String, Object> request) {
        int limit = request.get("limit") != null ? (Integer) request.get("limit") : 50;
        String deviceId = (String) request.get("deviceId");

        log.info("Get device operation logs requested: limit={}, deviceId={}", limit, deviceId);

        ListResultModel<List<Map<String, Object>>> result = new ListResultModel<>();
        try {
            List<DeviceOperationLog> filteredLogs = new ArrayList<>();
            for (DeviceOperationLog logEntry : operationLogs) {
                if (deviceId == null || logEntry.getDeviceId().equals(deviceId)) {
                    filteredLogs.add(logEntry);
                }
            }

            List<DeviceOperationLog> pagedLogs = filteredLogs.stream()
                    .sorted((l1, l2) -> Long.compare(l2.getTimestamp(), l1.getTimestamp()))
                    .limit(limit)
                    .collect(Collectors.toList());

            List<Map<String, Object>> logList = new ArrayList<>();
            for (DeviceOperationLog logEntry : pagedLogs) {
                logList.add(logEntry.toMap());
            }

            result.setData(logList);
            result.setSize(logList.size());
            result.setRequestStatus(200);
            result.setMessage("获取成功");
        } catch (Exception e) {
            log.error("Error getting device logs: {}", e.getMessage(), e);
            result.setRequestStatus(500);
            result.setMessage("获取设备日志失败: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/types")
    @ResponseBody
    public ResultModel<Map<String, Object>> getDeviceTypes() {
        log.info("Get device types requested");

        ResultModel<Map<String, Object>> result = new ResultModel<>();
        try {
            List<String> types = devices.values().stream()
                    .map(Device::getType)
                    .distinct()
                    .collect(Collectors.toList());

            Map<String, Long> typeCounts = new HashMap<>();
            for (String type : types) {
                typeCounts.put(type, devices.values().stream().filter(d -> type.equals(d.getType())).count());
            }

            Map<String, Object> data = new HashMap<>();
            data.put("types", types);
            data.put("typeCounts", typeCounts);

            result.setData(data);
            result.setRequestStatus(200);
            result.setMessage("获取成功");
        } catch (Exception e) {
            log.error("Error getting device types: {}", e.getMessage(), e);
            result.setRequestStatus(500);
            result.setMessage("获取设备类型失败: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/add")
    @ResponseBody
    public ResultModel<Map<String, Object>> addDevice(@RequestBody Map<String, Object> request) {
        String name = (String) request.get("name");
        String type = (String) request.get("type");
        String ip = (String) request.get("ip");
        String mac = (String) request.get("mac");
        String location = (String) request.get("location");

        log.info("Add device requested: name={}, type={}", name, type);

        ResultModel<Map<String, Object>> result = new ResultModel<>();
        try {
            String deviceId = "device-" + System.currentTimeMillis();
            
            Map<String, Object> props = new ConcurrentHashMap<>();
            props.put("ip", ip);
            props.put("mac", mac);
            
            Device device = new Device(
                deviceId, name, type != null ? type : "other", "online", 
                location != null ? location : "未知",
                0, true, props,
                java.time.LocalDate.now().toString(), "User Added", "v1.0.0"
            );
            
            devices.put(deviceId, device);
            
            Map<String, Object> data = new HashMap<>();
            data.put("id", deviceId);
            data.put("name", name);
            data.put("status", "online");
            
            addOperationLog(deviceId, name, "add", "success", "设备添加成功");
            
            result.setData(data);
            result.setRequestStatus(200);
            result.setMessage("添加成功");
        } catch (Exception e) {
            log.error("Error adding device: {}", e.getMessage(), e);
            result.setRequestStatus(500);
            result.setMessage("添加设备失败: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/update")
    @ResponseBody
    public ResultModel<Boolean> updateDevice(@RequestBody Map<String, Object> request) {
        String deviceId = (String) request.get("id");
        String name = (String) request.get("name");
        String type = (String) request.get("type");
        String ip = (String) request.get("ip");
        String mac = (String) request.get("mac");
        String location = (String) request.get("location");

        log.info("Update device requested: id={}", deviceId);

        ResultModel<Boolean> result = new ResultModel<>();
        try {
            Device device = devices.get(deviceId);
            if (device == null) {
                result.setRequestStatus(404);
                result.setMessage("设备不存在");
                result.setData(false);
                return result;
            }

            Map<String, Object> props = new ConcurrentHashMap<>(device.getProperties());
            if (ip != null) props.put("ip", ip);
            if (mac != null) props.put("mac", mac);

            Device updatedDevice = new Device(
                deviceId, 
                name != null ? name : device.getName(), 
                type != null ? type : device.getType(), 
                device.getStatus(),
                location != null ? location : device.getLocation(),
                device.getPowerConsumption(),
                device.isPoweredOn(),
                props,
                device.getInstallationDate(),
                device.getManufacturer(),
                device.getFirmwareVersion()
            );
            
            devices.put(deviceId, updatedDevice);
            
            addOperationLog(deviceId, updatedDevice.getName(), "update", "success", "设备更新成功");
            
            result.setData(true);
            result.setRequestStatus(200);
            result.setMessage("更新成功");
        } catch (Exception e) {
            log.error("Error updating device: {}", e.getMessage(), e);
            result.setRequestStatus(500);
            result.setMessage("更新设备失败: " + e.getMessage());
            result.setData(false);
        }
        return result;
    }

    @PostMapping("/delete")
    @ResponseBody
    public ResultModel<Boolean> deleteDevice(@RequestBody Map<String, String> request) {
        String deviceId = request.get("id");

        log.info("Delete device requested: id={}", deviceId);

        ResultModel<Boolean> result = new ResultModel<>();
        try {
            Device device = devices.remove(deviceId);
            if (device == null) {
                result.setRequestStatus(404);
                result.setMessage("设备不存在");
                result.setData(false);
                return result;
            }

            addOperationLog(deviceId, device.getName(), "delete", "success", "设备删除成功");
            
            result.setData(true);
            result.setRequestStatus(200);
            result.setMessage("删除成功");
        } catch (Exception e) {
            log.error("Error deleting device: {}", e.getMessage(), e);
            result.setRequestStatus(500);
            result.setMessage("删除设备失败: " + e.getMessage());
            result.setData(false);
        }
        return result;
    }

    private void addOperationLog(String deviceId, String deviceName, String operation, String status, String message) {
        DeviceOperationLog logEntry = new DeviceOperationLog(
            deviceId, deviceName, operation, status, message
        );
        operationLogs.add(logEntry);

        if (operationLogs.size() > 1000) {
            operationLogs.remove(0);
        }
    }
}
