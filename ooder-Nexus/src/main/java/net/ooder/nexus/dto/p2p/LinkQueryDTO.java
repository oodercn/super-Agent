package net.ooder.nexus.dto.p2p;

import java.io.Serializable;

public class LinkQueryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String linkId;
    private String sourceId;
    private String targetId;

    public String getLinkId() {
        return linkId;
    }

    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }

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
}
