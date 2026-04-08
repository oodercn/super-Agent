package net.ooder.mvp.skill.scene.dto.llmscript;

import java.util.Map;

public class ActionExecuteRequestDTO {
    
    private String action;
    
    private String module;
    
    private Map<String, Object> params;
    
    private Boolean requireConfirm;
    
    private Boolean syncContext;

    public ActionExecuteRequestDTO() {}

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public Boolean getRequireConfirm() {
        return requireConfirm;
    }

    public void setRequireConfirm(Boolean requireConfirm) {
        this.requireConfirm = requireConfirm;
    }

    public Boolean getSyncContext() {
        return syncContext;
    }

    public void setSyncContext(Boolean syncContext) {
        this.syncContext = syncContext;
    }
}
