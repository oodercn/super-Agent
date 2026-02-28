package net.ooder.nexus.dto.p2p;

import java.io.Serializable;

public class PathQueryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String sourceId;
    private String targetId;
    private Integer maxPaths;

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

    public Integer getMaxPaths() {
        return maxPaths;
    }

    public void setMaxPaths(Integer maxPaths) {
        this.maxPaths = maxPaths;
    }
}
