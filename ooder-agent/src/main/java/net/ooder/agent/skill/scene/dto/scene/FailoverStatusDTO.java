package net.ooder.mvp.skill.scene.dto.scene;

public class FailoverStatusDTO {
    private String sceneGroupId;
    private String status;
    private String failedParticipantId;
    private String recoveryAction;
    private long timestamp;

    public String getSceneGroupId() { return sceneGroupId; }
    public void setSceneGroupId(String sceneGroupId) { this.sceneGroupId = sceneGroupId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getFailedParticipantId() { return failedParticipantId; }
    public void setFailedParticipantId(String failedParticipantId) { this.failedParticipantId = failedParticipantId; }
    public String getRecoveryAction() { return recoveryAction; }
    public void setRecoveryAction(String recoveryAction) { this.recoveryAction = recoveryAction; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
