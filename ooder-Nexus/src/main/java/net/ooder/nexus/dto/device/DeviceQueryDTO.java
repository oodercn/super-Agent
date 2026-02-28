package net.ooder.nexus.dto.device;

import java.io.Serializable;

public class DeviceQueryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String status;
    private String type;
    private String location;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
