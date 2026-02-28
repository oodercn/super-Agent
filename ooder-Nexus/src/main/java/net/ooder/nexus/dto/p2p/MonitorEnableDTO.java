package net.ooder.nexus.dto.p2p;

import java.io.Serializable;

public class MonitorEnableDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long intervalMs;

    public Long getIntervalMs() {
        return intervalMs;
    }

    public void setIntervalMs(Long intervalMs) {
        this.intervalMs = intervalMs;
    }
}
