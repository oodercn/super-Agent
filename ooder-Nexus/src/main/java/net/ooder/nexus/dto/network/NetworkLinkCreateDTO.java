package net.ooder.nexus.dto.network;

import java.io.Serializable;

/**
 * Network link create request DTO
 */
public class NetworkLinkCreateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Link ID (optional, auto-generated if not provided)
     */
    private String linkId;

    /**
     * Source agent ID (required)
     */
    private String sourceAgentId;

    /**
     * Target agent ID (required)
     */
    private String targetAgentId;

    /**
     * Link type (optional, default: direct)
     */
    private String type;

    public String getLinkId() { return linkId; }
    public void setLinkId(String linkId) { this.linkId = linkId; }
    public String getSourceAgentId() { return sourceAgentId; }
    public void setSourceAgentId(String sourceAgentId) { this.sourceAgentId = sourceAgentId; }
    public String getTargetAgentId() { return targetAgentId; }
    public void setTargetAgentId(String targetAgentId) { this.targetAgentId = targetAgentId; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
