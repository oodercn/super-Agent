package net.ooder.mvp.skill.scene.dto.scene;

import java.util.Map;

public class SceneGroupConfigDTO {
    
    public enum CreatorType {
        USER, AGENT, SYSTEM
    }
    
    private String name;
    private String description;
    private String creatorId;
    private CreatorType creatorType;
    private Integer minMembers;
    private Integer maxMembers;
    private String securityPolicy;
    private Long heartbeatInterval;
    private Long heartbeatTimeout;
    private Map<String, Object> extendedConfig;
    private Integer knowledgeTopK;
    private Double knowledgeThreshold;
    private Boolean crossLayerSearch;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCreatorId() { return creatorId; }
    public void setCreatorId(String creatorId) { this.creatorId = creatorId; }
    public CreatorType getCreatorType() { return creatorType; }
    public void setCreatorType(CreatorType creatorType) { this.creatorType = creatorType; }
    public Integer getMinMembers() { return minMembers; }
    public void setMinMembers(Integer minMembers) { this.minMembers = minMembers; }
    public Integer getMaxMembers() { return maxMembers; }
    public void setMaxMembers(Integer maxMembers) { this.maxMembers = maxMembers; }
    public String getSecurityPolicy() { return securityPolicy; }
    public void setSecurityPolicy(String securityPolicy) { this.securityPolicy = securityPolicy; }
    public Long getHeartbeatInterval() { return heartbeatInterval; }
    public void setHeartbeatInterval(Long heartbeatInterval) { this.heartbeatInterval = heartbeatInterval; }
    public Long getHeartbeatTimeout() { return heartbeatTimeout; }
    public void setHeartbeatTimeout(Long heartbeatTimeout) { this.heartbeatTimeout = heartbeatTimeout; }
    public Map<String, Object> getExtendedConfig() { return extendedConfig; }
    public void setExtendedConfig(Map<String, Object> extendedConfig) { this.extendedConfig = extendedConfig; }
    public Integer getKnowledgeTopK() { return knowledgeTopK; }
    public void setKnowledgeTopK(Integer knowledgeTopK) { this.knowledgeTopK = knowledgeTopK; }
    public Double getKnowledgeThreshold() { return knowledgeThreshold; }
    public void setKnowledgeThreshold(Double knowledgeThreshold) { this.knowledgeThreshold = knowledgeThreshold; }
    public Boolean getCrossLayerSearch() { return crossLayerSearch; }
    public void setCrossLayerSearch(Boolean crossLayerSearch) { this.crossLayerSearch = crossLayerSearch; }
}
