package net.ooder.mvp.skill.scene.dto.report;

public class AiGenerateRequestDTO {
    private String userId;
    private String sceneGroupId;
    private String context;
    private String generateType;

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getSceneGroupId() { return sceneGroupId; }
    public void setSceneGroupId(String sceneGroupId) { this.sceneGroupId = sceneGroupId; }
    public String getContext() { return context; }
    public void setContext(String context) { this.context = context; }
    public String getGenerateType() { return generateType; }
    public void setGenerateType(String generateType) { this.generateType = generateType; }
}
