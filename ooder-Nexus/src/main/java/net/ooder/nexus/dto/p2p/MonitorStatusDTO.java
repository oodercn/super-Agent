package net.ooder.nexus.dto.p2p;

import java.io.Serializable;

public class MonitorStatusDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Boolean enabled;
    private Integer linkCount;

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Integer getLinkCount() {
        return linkCount;
    }

    public void setLinkCount(Integer linkCount) {
        this.linkCount = linkCount;
    }
}
