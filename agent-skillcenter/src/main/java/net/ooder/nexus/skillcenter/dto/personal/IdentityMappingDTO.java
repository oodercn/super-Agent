package net.ooder.nexus.skillcenter.dto.personal;

import net.ooder.nexus.skillcenter.dto.BaseDTO;

public class IdentityMappingDTO extends BaseDTO {

    private String type;
    private String identifier;
    private String status;
    private String linkedAt;

    public IdentityMappingDTO() {}

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLinkedAt() {
        return linkedAt;
    }

    public void setLinkedAt(String linkedAt) {
        this.linkedAt = linkedAt;
    }
}
