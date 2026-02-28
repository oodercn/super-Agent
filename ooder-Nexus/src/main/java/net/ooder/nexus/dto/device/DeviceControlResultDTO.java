package net.ooder.nexus.dto.device;

import java.io.Serializable;

public class DeviceControlResultDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String deviceId;
    private String deviceName;
    private String deviceStatus;
    private Boolean devicePower;

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

    public String getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(String deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    public Boolean getDevicePower() {
        return devicePower;
    }

    public void setDevicePower(Boolean devicePower) {
        this.devicePower = devicePower;
    }
}
