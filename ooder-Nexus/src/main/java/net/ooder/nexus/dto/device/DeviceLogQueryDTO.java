package net.ooder.nexus.dto.device;

import java.io.Serializable;

public class DeviceLogQueryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer limit;
    private String deviceId;

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
