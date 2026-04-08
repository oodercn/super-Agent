package net.ooder.mvp.skill.scene.dto.discovery;

import java.util.List;

public class GitDiscoveryResultDTO {
    
    private List<CapabilityDTO> capabilities;
    
    private Integer total;
    
    private String source;
    
    private String repoUrl;
    
    private String branch;
    
    private Long timestamp;
    
    private String errorMessage;
    
    private boolean fromCache;

    public GitDiscoveryResultDTO() {}

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

    public String getRepoUrl() {
        return repoUrl;
    }

    public void setRepoUrl(String repoUrl) {
        this.repoUrl = repoUrl;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isFromCache() {
        return fromCache;
    }

    public void setFromCache(boolean fromCache) {
        this.fromCache = fromCache;
    }
}
