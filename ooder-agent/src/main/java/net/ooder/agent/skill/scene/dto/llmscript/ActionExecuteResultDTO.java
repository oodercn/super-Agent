package net.ooder.mvp.skill.scene.dto.llmscript;

import java.util.Map;

public class ActionExecuteResultDTO {
    
    private Boolean success;
    
    private Boolean needConfirm;
    
    private String confirmMessage;
    
    private Map<String, Object> pendingAction;
    
    private Object result;
    
    private String error;

    public ActionExecuteResultDTO() {}

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Boolean getNeedConfirm() {
        return needConfirm;
    }

    public void setNeedConfirm(Boolean needConfirm) {
        this.needConfirm = needConfirm;
    }

    public String getConfirmMessage() {
        return confirmMessage;
    }

    public void setConfirmMessage(String confirmMessage) {
        this.confirmMessage = confirmMessage;
    }

    public Map<String, Object> getPendingAction() {
        return pendingAction;
    }

    public void setPendingAction(Map<String, Object> pendingAction) {
        this.pendingAction = pendingAction;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
