package net.ooder.nexus.model;

public class DeviceResult {
    private int deviceId;
    private String deviceName;
    private String deviceType;
    private String status;
    private String ipAddress;
    private String macAddress;
    private String lastOnline;
    private String message;
    private boolean success;

    public DeviceResult() {
    }

    public DeviceResult(int deviceId, String deviceName, String deviceType, String status, String ipAddress, String macAddress, String lastOnline, String message, boolean success) {
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.deviceType = deviceType;
        this.status = status;
        this.ipAddress = ipAddress;
        this.macAddress = macAddress;
        this.lastOnline = lastOnline;
        this.message = message;
        this.success = success;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getLastOnline() {
        return lastOnline;
    }

    public void setLastOnline(String lastOnline) {
        this.lastOnline = lastOnline;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}