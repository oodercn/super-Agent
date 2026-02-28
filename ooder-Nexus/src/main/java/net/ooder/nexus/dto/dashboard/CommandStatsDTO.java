package net.ooder.nexus.dto.dashboard;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class CommandStatsDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long total;
    private Long success;
    private Long failed;
    private Long pending;
    private Double successRate;
    private Map<String, Long> byType;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Long getSuccess() {
        return success;
    }

    public void setSuccess(Long success) {
        this.success = success;
    }

    public Long getFailed() {
        return failed;
    }

    public void setFailed(Long failed) {
        this.failed = failed;
    }

    public Long getPending() {
        return pending;
    }

    public void setPending(Long pending) {
        this.pending = pending;
    }

    public Double getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(Double successRate) {
        this.successRate = successRate;
    }

    public Map<String, Long> getByType() {
        return byType;
    }

    public void setByType(Map<String, Long> byType) {
        this.byType = byType;
    }
}
