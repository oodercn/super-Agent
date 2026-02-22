package net.ooder.nexus.skillcenter.dto.system;

import net.ooder.nexus.skillcenter.dto.BaseDTO;

public class SystemStatusDTO extends BaseDTO {

    private String status;
    private String service;
    private Long timestamp;

    public SystemStatusDTO() {}

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
