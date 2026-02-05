package net.ooder.nexus.model.network;

import java.io.Serializable;

public class IPAddress implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String ipAddress;
    private String type;
    private String status;
    private String deviceName;
    private String macAddress;
    private String deviceType;
    private String leaseTime;
    private long assignedAt;
    private long lastSeen;

    public IPAddress() {
    }

    public IPAddress(String id, String ipAddress, String type, String status, String deviceName, String macAddress, String deviceType, String leaseTime) {
        this.id = id;
        this.ipAddress = ipAddress;
        this.type = type;
        this.status = status;
        this.deviceName = deviceName;
        this.macAddress = macAddress;
        this.deviceType = deviceType;
        this.leaseTime = leaseTime;
        this.assignedAt = System.currentTimeMillis();
        this.lastSeen = System.currentTimeMillis();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getLeaseTime() {
        return leaseTime;
    }

    public void setLeaseTime(String leaseTime) {
        this.leaseTime = leaseTime;
    }

    public long getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(long assignedAt) {
        this.assignedAt = assignedAt;
    }

    public long getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(long lastSeen) {
        this.lastSeen = lastSeen;
    }
}
