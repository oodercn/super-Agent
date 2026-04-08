package net.ooder.mvp.skill.scene.dto.scene;

import java.util.List;
import java.util.Map;

public class SceneGroupDTO {
    private String sceneGroupId;
    private String templateId;
    private String name;
    private String description;
    private SceneGroupStatus status;
    private String creatorId;
    private SceneGroupConfigDTO.CreatorType creatorType;
    private SceneGroupConfigDTO config;
    private int memberCount;
    private String primaryAgentId;
    private long createTime;
    private long lastUpdateTime;
    private List<SceneParticipantDTO> participants;
    private List<CapabilityBindingDTO> capabilityBindings;
    private List<KnowledgeBindingDTO> knowledgeBases;
    private Map<String, Object> context;

    public String getSceneGroupId() { return sceneGroupId; }
    public void setSceneGroupId(String sceneGroupId) { this.sceneGroupId = sceneGroupId; }
    public String getTemplateId() { return templateId; }
    public void setTemplateId(String templateId) { this.templateId = templateId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public SceneGroupStatus getStatus() { return status; }
    public void setStatus(SceneGroupStatus status) { this.status = status; }
    public String getCreatorId() { return creatorId; }
    public void setCreatorId(String creatorId) { this.creatorId = creatorId; }
    public SceneGroupConfigDTO.CreatorType getCreatorType() { return creatorType; }
    public void setCreatorType(SceneGroupConfigDTO.CreatorType creatorType) { this.creatorType = creatorType; }
    public SceneGroupConfigDTO getConfig() { return config; }
    public void setConfig(SceneGroupConfigDTO config) { this.config = config; }
    public int getMemberCount() { return memberCount; }
    public void setMemberCount(int memberCount) { this.memberCount = memberCount; }
    public String getPrimaryAgentId() { return primaryAgentId; }
    public void setPrimaryAgentId(String primaryAgentId) { this.primaryAgentId = primaryAgentId; }
    public long getCreateTime() { return createTime; }
    public void setCreateTime(long createTime) { this.createTime = createTime; }
    public long getLastUpdateTime() { return lastUpdateTime; }
    public void setLastUpdateTime(long lastUpdateTime) { this.lastUpdateTime = lastUpdateTime; }
    public List<SceneParticipantDTO> getParticipants() { return participants; }
    public void setParticipants(List<SceneParticipantDTO> participants) { this.participants = participants; }
    public List<CapabilityBindingDTO> getCapabilityBindings() { return capabilityBindings; }
    public void setCapabilityBindings(List<CapabilityBindingDTO> capabilityBindings) { this.capabilityBindings = capabilityBindings; }
    public List<KnowledgeBindingDTO> getKnowledgeBases() { return knowledgeBases; }
    public void setKnowledgeBases(List<KnowledgeBindingDTO> knowledgeBases) { this.knowledgeBases = knowledgeBases; }
    public Map<String, Object> getContext() { return context; }
    public void setContext(Map<String, Object> context) { this.context = context; }
}
