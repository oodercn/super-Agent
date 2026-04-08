package net.ooder.mvp.skill.scene.dto.llmscript;

import java.util.Map;

public class ScriptGenerateRequestDTO {
    
    private String intent;
    
    private Map<String, Object> context;

    public ScriptGenerateRequestDTO() {}

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public Map<String, Object> getContext() {
        return context;
    }

    public void setContext(Map<String, Object> context) {
        this.context = context;
    }
}
