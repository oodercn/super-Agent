package net.ooder.skillcenter.dto.scene;

public class FailoverStatusDTO {
    private String sceneGroupId;
    private boolean inProgress;
    private String failedMemberId;
    private String newPrimaryId;
    private long startTime;
    private String phase;

    public String getSceneGroupId() { return sceneGroupId; }
    public void setSceneGroupId(String sceneGroupId) { this.sceneGroupId = sceneGroupId; }
    public boolean isInProgress() { return inProgress; }
    public void setInProgress(boolean inProgress) { this.inProgress = inProgress; }
    public String getFailedMemberId() { return failedMemberId; }
    public void setFailedMemberId(String failedMemberId) { this.failedMemberId = failedMemberId; }
    public String getNewPrimaryId() { return newPrimaryId; }
    public void setNewPrimaryId(String newPrimaryId) { this.newPrimaryId = newPrimaryId; }
    public long getStartTime() { return startTime; }
    public void setStartTime(long startTime) { this.startTime = startTime; }
    public String getPhase() { return phase; }
    public void setPhase(String phase) { this.phase = phase; }
}
