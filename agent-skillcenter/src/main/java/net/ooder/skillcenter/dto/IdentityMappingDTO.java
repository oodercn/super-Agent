package net.ooder.skillcenter.dto;

import java.util.Date;

/**
 * 身份映射数据传输对象
 */
public class IdentityMappingDTO {

    private String id;
    private String type;
    private String identifier;
    private String status;
    private Date linkedAt;

    public IdentityMappingDTO() {}

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getIdentifier() { return identifier; }
    public void setIdentifier(String identifier) { this.identifier = identifier; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Date getLinkedAt() { return linkedAt; }
    public void setLinkedAt(Date linkedAt) { this.linkedAt = linkedAt; }
}
