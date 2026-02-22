package net.ooder.nexus.dto.network;

import java.io.Serializable;

/**
 * Network link request DTO
 */
public class NetworkLinkRequestDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Link ID (optional, auto-generated if not provided)
     */
    private String linkId;

    /**
     * Source agent ID
     */
    private String sourceAgentId;

    /**
     * Target agent ID
     */
    private String targetAgentId;

    /**
     * Link type (direct, relay, vpn)
     */
    private String linkType;

    public String getLinkId() { return linkId; }
    public void setLinkId(String linkId) { this.linkId = linkId; }
    public String getSourceAgentId() { return sourceAgentId; }
    public void setSourceAgentId(String sourceAgentId) { this.sourceAgentId = sourceAgentId; }
    public String getTargetAgentId() { return targetAgentId; }
    public void setTargetAgentId(String targetAgentId) { this.targetAgentId = targetAgentId; }
    public String getLinkType() { return linkType; }
    public void setLinkType(String linkType) { this.linkType = linkType; }
}
