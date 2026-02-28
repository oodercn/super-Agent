package net.ooder.nexus.dto.network;

import java.io.Serializable;

public class NetworkLinkCreateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String linkId;
    private String sourceAgentId;
    private String targetAgentId;
    private String type;

    public String getLinkId() {
        return linkId;
    }

    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }

    public String getSourceAgentId() {
        return sourceAgentId;
    }

    public void setSourceAgentId(String sourceAgentId) {
        this.sourceAgentId = sourceAgentId;
    }

    public String getTargetAgentId() {
        return targetAgentId;
    }

    public void setTargetAgentId(String targetAgentId) {
        this.targetAgentId = targetAgentId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
