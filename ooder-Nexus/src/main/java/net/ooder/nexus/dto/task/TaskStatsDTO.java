package net.ooder.nexus.dto.task;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public class TaskStatsDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer total;
    private Long enabled;
    private Long disabled;
    private Map<String, Long> byType;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Long getEnabled() {
        return enabled;
    }

    public void setEnabled(Long enabled) {
        this.enabled = enabled;
    }

    public Long getDisabled() {
        return disabled;
    }

    public void setDisabled(Long disabled) {
        this.disabled = disabled;
    }

    public Map<String, Long> getByType() {
        return byType;
    }

    public void setByType(Map<String, Long> byType) {
        this.byType = byType;
    }
}
