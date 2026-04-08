package net.ooder.mvp.skill.scene.dto.dailyreport;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public class ReminderRequestDTO {
    
    @NotBlank(message = "场景组ID不能为空")
    private String sceneGroupId;
    
    private List<String> targetUsers;
    
    private String message;

    public String getSceneGroupId() {
        return sceneGroupId;
    }

    public void setSceneGroupId(String sceneGroupId) {
        this.sceneGroupId = sceneGroupId;
    }

    public List<String> getTargetUsers() {
        return targetUsers;
    }

    public void setTargetUsers(List<String> targetUsers) {
        this.targetUsers = targetUsers;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
