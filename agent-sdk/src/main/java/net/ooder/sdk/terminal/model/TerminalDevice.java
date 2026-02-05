package net.ooder.sdk.terminal.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Map;
import java.util.UUID;

public class TerminalDevice {
    @JSONField(name = "deviceId")
    private String deviceId;
    
    @JSONField(name = "deviceName")
    private String deviceName;
    
    @JSONField(name = "deviceType")
    private String deviceType;
    
    @JSONField(name = "ipAddress")
    private String ipAddress;
    
    @JSONField(name = "macAddress")
    private String macAddress;
    
    @JSONField(name = "status")
    private TerminalStatus status;
    
    @JSONField(name = "lastSeen")
    private long lastSeen;
    
    @JSONField(name = "metadata")
    private Map<String, Object> metadata;
    
    @JSONField(name = "registered")
    private boolean registered;
    
    public TerminalDevice() {
        this.deviceId = UUID.randomUUID().toString();
        this.status = TerminalStatus.OFFLINE;
        this.lastSeen = System.currentTimeMillis();
        this.registered = false;
    }
    
    public TerminalDevice(String deviceName, String deviceType, String ipAddress, String macAddress) {
        this();
        this.deviceName = deviceName;
        this.deviceType = deviceType;
        this.ipAddress = ipAddress;
        this.macAddress = macAddress;
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

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public TerminalStatus getStatus() {
        return status;
    }

    public void setStatus(TerminalStatus status) {
        this.status = status;
        this.lastSeen = System.currentTimeMillis();
    }

    public long getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(long lastSeen) {
        this.lastSeen = lastSeen;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }
    
    public void updateMetadata(Map<String, Object> metadata) {
        if (this.metadata == null) {
            this.metadata = new java.util.HashMap<>();
        }
        this.metadata.putAll(metadata);
    }

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }

    @Override
    public String toString() {
        return "TerminalDevice{" +
                "deviceId='" + deviceId + '\'' +
                ", deviceName='" + deviceName + '\'' +
                ", deviceType='" + deviceType + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", macAddress='" + macAddress + '\'' +
                ", status=" + status +
                ", lastSeen=" + lastSeen +
                ", registered=" + registered +
                '}';
    }
}
