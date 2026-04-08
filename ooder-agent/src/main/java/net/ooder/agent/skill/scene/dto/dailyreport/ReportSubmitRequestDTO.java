package net.ooder.mvp.skill.scene.dto.dailyreport;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public class ReportSubmitRequestDTO {
    
    @NotBlank(message = "场景组ID不能为空")
    private String sceneGroupId;
    
    @NotBlank(message = "用户ID不能为空")
    private String userId;
    
    @NotBlank(message = "报告内容不能为空")
    private String content;
    
    private List<String> attachments;
    
    public ReportSubmitRequestDTO() {}
    
    public String getSceneGroupId() {
        return sceneGroupId;
    }
    
    public void setSceneGroupId(String sceneGroupId) {
        this.sceneGroupId = sceneGroupId;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public List<String> getAttachments() {
        return attachments;
    }
    
    public void setAttachments(List<String> attachments) {
        this.attachments = attachments;
    }
}
