package net.ooder.nexus.skillcenter.dto.system;

import net.ooder.nexus.skillcenter.dto.BaseDTO;

public class SystemOperationDTO extends BaseDTO {

    private String id;
    private String type;
    private String description;
    private Long timestamp;
    private String status;

    public SystemOperationDTO() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
