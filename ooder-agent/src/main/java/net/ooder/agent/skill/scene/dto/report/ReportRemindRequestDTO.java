package net.ooder.mvp.skill.scene.dto.report;

import java.util.List;

public class ReportRemindRequestDTO {
    private String sceneGroupId;
    private List<String> userIds;
    private String message;

    public String getSceneGroupId() { return sceneGroupId; }
    public void setSceneGroupId(String sceneGroupId) { this.sceneGroupId = sceneGroupId; }
    public List<String> getUserIds() { return userIds; }
    public void setUserIds(List<String> userIds) { this.userIds = userIds; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
