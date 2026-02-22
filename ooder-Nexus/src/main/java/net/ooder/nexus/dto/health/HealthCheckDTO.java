package net.ooder.nexus.dto.health;

import java.io.Serializable;
import java.util.List;

/**
 * Health check DTO
 * Used for running health checks
 */
public class HealthCheckDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Check type: system, service, network, database, all
     */
    private String checkType;

    /**
     * Whether to perform detailed check
     */
    private Boolean detailed;

    /**
     * Timeout in seconds
     */
    private Integer timeout;

    /**
     * Specific check items to run
     */
    private List<String> checkItems;

    /**
     * Whether to include performance metrics
     */
    private Boolean includeMetrics;

    public HealthCheckDTO() {
        this.checkType = "all";
        this.detailed = false;
        this.timeout = 30;
        this.includeMetrics = true;
    }

    public String getCheckType() {
        return checkType;
    }

    public void setCheckType(String checkType) {
        this.checkType = checkType;
    }

    public Boolean getDetailed() {
        return detailed;
    }

    public void setDetailed(Boolean detailed) {
        this.detailed = detailed;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public List<String> getCheckItems() {
        return checkItems;
    }

    public void setCheckItems(List<String> checkItems) {
        this.checkItems = checkItems;
    }

    public Boolean getIncludeMetrics() {
        return includeMetrics;
    }

    public void setIncludeMetrics(Boolean includeMetrics) {
        this.includeMetrics = includeMetrics;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private HealthCheckDTO dto = new HealthCheckDTO();

        public Builder checkType(String checkType) {
            dto.setCheckType(checkType);
            return this;
        }

        public Builder detailed(Boolean detailed) {
            dto.setDetailed(detailed);
            return this;
        }

        public Builder timeout(Integer timeout) {
            dto.setTimeout(timeout);
            return this;
        }

        public Builder checkItems(List<String> checkItems) {
            dto.setCheckItems(checkItems);
            return this;
        }

        public Builder includeMetrics(Boolean includeMetrics) {
            dto.setIncludeMetrics(includeMetrics);
            return this;
        }

        public HealthCheckDTO build() {
            return dto;
        }
    }
}
