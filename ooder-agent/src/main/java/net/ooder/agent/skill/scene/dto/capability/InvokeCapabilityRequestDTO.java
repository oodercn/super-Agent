package net.ooder.mvp.skill.scene.dto.capability;

import jakarta.validation.constraints.NotBlank;
import java.util.Map;

public class InvokeCapabilityRequestDTO {
    
    @NotBlank(message = "能力ID不能为空")
    private String capabilityId;
    
    private Map<String, Object> params;
    
    private String sceneId;
    
    private String userId;

    public InvokeCapabilityRequestDTO() {}

    public String getCapabilityId() {
        return capabilityId;
    }

    public void setCapabilityId(String capabilityId) {
        this.capabilityId = capabilityId;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public String getSceneId() {
        return sceneId;
    }

    public void setSceneId(String sceneId) {
        this.sceneId = sceneId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
