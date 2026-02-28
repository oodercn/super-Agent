package net.ooder.nexus.dto.p2p;

import java.io.Serializable;

public class LinkCreateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String sourceId;
    private String targetId;
    private String type;

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
