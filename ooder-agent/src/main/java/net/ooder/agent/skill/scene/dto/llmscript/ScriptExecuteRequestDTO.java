package net.ooder.mvp.skill.scene.dto.llmscript;

import java.util.Map;

public class ScriptExecuteRequestDTO {
    
    private String script;
    
    private String scriptType;
    
    private String module;
    
    private Boolean requireConfirm;
    
    private Boolean syncContext;

    public ScriptExecuteRequestDTO() {}

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public String getScriptType() {
        return scriptType;
    }

    public void setScriptType(String scriptType) {
        this.scriptType = scriptType;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
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
