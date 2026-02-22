package net.ooder.nexus.domain.end.model;

import java.io.Serializable;

/**
 * Device Control Result
 */
public class DeviceControlResult implements Serializable {
    private String deviceId;
    private String deviceName;
    private String deviceStatus;
    private boolean devicePower;

    public DeviceControlResult() {
    }

    public DeviceControlResult(String deviceId, String deviceName, String deviceStatus, boolean devicePower) {
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.deviceStatus = deviceStatus;
        this.devicePower = devicePower;
    }

    public String getDeviceId() { return deviceId; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }
    public String getDeviceName() { return deviceName; }
    public void setDeviceName(String deviceName) { this.deviceName = deviceName; }
    public String getDeviceStatus() { return deviceStatus; }
    public void setDeviceStatus(String deviceStatus) { this.deviceStatus = deviceStatus; }
    public boolean isDevicePower() { return devicePower; }
    public void setDevicePower(boolean devicePower) { this.devicePower = devicePower; }
}
