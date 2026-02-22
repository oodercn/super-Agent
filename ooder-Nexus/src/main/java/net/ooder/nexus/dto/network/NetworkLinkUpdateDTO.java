package net.ooder.nexus.dto.network;

import java.io.Serializable;

/**
 * Network link update request DTO
 */
public class NetworkLinkUpdateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Link status
     */
    private String status;

    /**
     * Link type
     */
    private String type;

    /**
     * Link description
     */
    private String description;

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
