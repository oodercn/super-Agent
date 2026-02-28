package net.ooder.nexus.adapter.inbound.controller.device;

import net.ooder.config.ResultModel;
import net.ooder.config.ListResultModel;
import net.ooder.nexus.domain.end.model.Device;
import net.ooder.nexus.domain.end.model.DeviceOperationLog;
import net.ooder.nexus.dto.device.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/devices")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
public class DeviceController {

    private static final Logger log = LoggerFactory.getLogger(DeviceController.class);

    private final ConcurrentHashMap<String, Device> devices = new ConcurrentHashMap<>();
    private final List<DeviceOperationLog> operationLogs = new ArrayList<>();

    public DeviceController() {
        initializeDefaultDevices();
    }

    @GetMapping("")
    public ResultModel<List<Device>> getDevicesGet(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status) {
        log.info("GET devices requested: type={}, status={}", type, status);
        ResultModel<List<Device>> result = new ResultModel<>();
        try {
            List<Device> filteredDevices = devices.values().stream()
                    .filter(device -> (status == null || device.getStatus().equals(status)))
                    .filter(device -> (type == null || device.getType().equals(type)))
                    .collect(Collectors.toList());
            result.setData(filteredDevices);
            result.setRequestStatus(200);
            result.setMessage("获取成功");
        } catch (Exception e) {
            log.error("Error getting devices: {}", e.getMessage(), e);
            result.setRequestStatus(500);
            result.setMessage("获取设备列表失败: " + e.getMessage());
        }
        return result;
    }

    @GetMapping("/list")
    public ResultModel<List<Device>> getDevicesListGet(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status) {
        return getDevicesGet(type, status);
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
    public ResultModel<DeviceListResultDTO> getDevices(@RequestBody(required = false) DeviceQueryDTO request) {
        String status = request != null ? request.getStatus() : null;
        String type = request != null ? request.getType() : null;
        String location = request != null ? request.getLocation() : null;

        log.info("Get devices requested: status={}, type={}, location={}", status, type, location);

        ResultModel<DeviceListResultDTO> result = new ResultModel<>();
        try {
            List<Device> filteredDevices = devices.values().stream()
                    .filter(device -> (status == null || device.getStatus().equals(status)))
                    .filter(device -> (type == null || device.getType().equals(type)))
                    .filter(device -> (location == null || device.getLocation().equals(location)))
                    .collect(Collectors.toList());

            DeviceListResultDTO data = new DeviceListResultDTO();
            data.setDevices(filteredDevices);
            
            DeviceListResultDTO.DeviceStatsDTO stats = new DeviceListResultDTO.DeviceStatsDTO();
            stats.setTotal(devices.size());
            stats.setOnline(devices.values().stream().filter(d -> "online".equals(d.getStatus())).count());
            stats.setOffline(devices.values().stream().filter(d -> "offline".equals(d.getStatus())).count());
            stats.setTypes(devices.values().stream().map(Device::getType).distinct().count());
            data.setStats(stats);

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
    public ResultModel<Device> getDeviceDetail(@RequestBody DeviceIdDTO request) {
        String deviceId = request.getDeviceId();
        log.info("Get device detail requested: {}", deviceId);

        ResultModel<Device> result = new ResultModel<>();
        try {
            Device device = devices.get(deviceId);
            if (device == null) {
                result.setRequestStatus(404);
                result.setMessage("设备不存在");
                return result;
            }

            result.setData(device);
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
    public ResultModel<DeviceControlResultDTO> controlDevice(@RequestBody DeviceControlDTO request) {
        String deviceId = request.getDeviceId();
        String command = request.getCommand();
        Map<String, Object> parameters = request.getParameters() != null ? request.getParameters() : new HashMap<>();

        log.info("Control device requested: {}, command: {}", deviceId, command);

        ResultModel<DeviceControlResultDTO> result = new ResultModel<>();
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

            DeviceControlResultDTO data = new DeviceControlResultDTO();
            data.setDeviceId(deviceId);
            data.setDeviceName(device.getName());
            data.setDeviceStatus(device.getStatus());
            data.setDevicePower(device.isPoweredOn());

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
    public ListResultModel<List<DeviceOperationLog>> getDeviceLogs(@RequestBody DeviceLogQueryDTO request) {
        int limit = request.getLimit() != null ? request.getLimit() : 50;
        String deviceId = request.getDeviceId();

        log.info("Get device operation logs requested: limit={}, deviceId={}", limit, deviceId);

        ListResultModel<List<DeviceOperationLog>> result = new ListResultModel<>();
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

            result.setData(pagedLogs);
            result.setSize(pagedLogs.size());
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
    public ResultModel<DeviceTypesDTO> getDeviceTypes() {
        log.info("Get device types requested");

        ResultModel<DeviceTypesDTO> result = new ResultModel<>();
        try {
            List<String> types = devices.values().stream()
                    .map(Device::getType)
                    .distinct()
                    .collect(Collectors.toList());

            Map<String, Long> typeCounts = new HashMap<>();
            for (String type : types) {
                typeCounts.put(type, devices.values().stream().filter(d -> type.equals(d.getType())).count());
            }

            DeviceTypesDTO data = new DeviceTypesDTO();
            data.setTypes(types);
            data.setTypeCounts(typeCounts);

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
    public ResultModel<DeviceAddResultDTO> addDevice(@RequestBody DeviceAddDTO request) {
        String name = request.getName();
        String type = request.getType();
        String ip = request.getIp();
        String mac = request.getMac();
        String location = request.getLocation();

        log.info("Add device requested: name={}, type={}", name, type);

        ResultModel<DeviceAddResultDTO> result = new ResultModel<>();
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
            
            DeviceAddResultDTO data = new DeviceAddResultDTO();
            data.setId(deviceId);
            data.setName(name);
            data.setStatus("online");
            
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
    public ResultModel<Boolean> updateDevice(@RequestBody DeviceUpdateDTO request) {
        String deviceId = request.getId();
        String name = request.getName();
        String type = request.getType();
        String ip = request.getIp();
        String mac = request.getMac();
        String location = request.getLocation();

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
    public ResultModel<Boolean> deleteDevice(@RequestBody DeviceIdDTO request) {
        String deviceId = request.getDeviceId();

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
