package net.ooder.nexus.dto.device;

import java.io.Serializable;

public class DeviceIdDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String deviceId;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
