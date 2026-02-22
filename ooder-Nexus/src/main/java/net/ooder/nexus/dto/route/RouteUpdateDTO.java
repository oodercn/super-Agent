package net.ooder.nexus.dto.route;

import java.io.Serializable;

/**
 * Route update request DTO
 */
public class RouteUpdateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Route status
     */
    private String status;

    /**
     * Route type
     */
    private String type;

    /**
     * Route description
     */
    private String description;

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
