package net.ooder.nexus.model.device;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class DeviceOperationLog implements Serializable {
    private static final long serialVersionUID = 1L;

    private String deviceId;
    private String deviceName;
    private String operation;
    private String status;
    private String message;
    private long timestamp;

    public DeviceOperationLog() {
    }

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

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}