package net.ooder.nexus.model;

/**
 * 设备控制结果实体Bean
 * 用于DeviceController中controlDevice方法的返回类型
 */
public class DeviceControlResult {
    
    private String deviceId;
    private String deviceName;
    private String deviceStatus;
    private boolean devicePower;

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

    public boolean isDevicePower() {
        return devicePower;
    }

    public void setDevicePower(boolean devicePower) {
        this.devicePower = devicePower;
    }
}
