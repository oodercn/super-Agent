package net.ooder.skill.chat.dto;

public class SendMessageRequest {
    
    private String content;
    private Boolean useKnowledge;
    private Boolean enableTools;
    private String skillId;
    private String userId;

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Boolean getUseKnowledge() { return useKnowledge; }
    public void setUseKnowledge(Boolean useKnowledge) { this.useKnowledge = useKnowledge; }
    public Boolean getEnableTools() { return enableTools; }
    public void setEnableTools(Boolean enableTools) { this.enableTools = enableTools; }
    public String getSkillId() { return skillId; }
    public void setSkillId(String skillId) { this.skillId = skillId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
}
