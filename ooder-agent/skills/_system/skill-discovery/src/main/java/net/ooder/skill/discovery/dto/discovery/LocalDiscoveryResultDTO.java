package net.ooder.skill.discovery.dto.discovery;

import java.util.List;
import java.util.Map;

public class LocalDiscoveryResultDTO {
    
    private List<CapabilityDTO> capabilities;
    
    private Integer total;
    
    private String source;
    
    private Long timestamp;
    
    private Map<String, ?> stats;
    
    private String errorMessage;
    
    private String message;

    public LocalDiscoveryResultDTO() {}

    public List<CapabilityDTO> getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(List<CapabilityDTO> capabilities) {
        this.capabilities = capabilities;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Map<String, ?> getStats() {
        return stats;
    }

    public void setStats(Map<String, ?> stats) {
        this.stats = stats;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
