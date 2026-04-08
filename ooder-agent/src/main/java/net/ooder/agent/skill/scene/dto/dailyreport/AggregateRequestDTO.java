package net.ooder.mvp.skill.scene.dto.dailyreport;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public class AggregateRequestDTO {
    
    @NotBlank(message = "场景组ID不能为空")
    private String sceneGroupId;
    
    private List<String> userIds;
    
    private String startDate;
    
    private String endDate;
    
    public AggregateRequestDTO() {}
    
    public String getSceneGroupId() {
        return sceneGroupId;
    }
    
    public void setSceneGroupId(String sceneGroupId) {
        this.sceneGroupId = sceneGroupId;
    }
    
    public List<String> getUserIds() {
        return userIds;
    }
    
    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }
    
    public String getStartDate() {
        return startDate;
    }
    
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    
    public String getEndDate() {
        return endDate;
    }
    
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
