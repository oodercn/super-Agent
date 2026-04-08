package net.ooder.mvp.skill.scene.llm.assistant;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ScriptGenerationRequestDto implements ProgramAssistantSkill.ScriptGenerationRequest {

    private String language;
    private String module;
    private String intent;
    private Map<String, Object> context = new HashMap<>();
    private Set<String> allowedApis;

    public ScriptGenerationRequestDto() {}

    public ScriptGenerationRequestDto(String language, String module, String intent) {
        this.language = language;
        this.module = module;
        this.intent = intent;
    }

    @Override
    public String getLanguage() {
        return language;
    }

    @Override
    public String getModule() {
        return module;
    }

    @Override
    public String getIntent() {
        return intent;
    }

    @Override
    public Map<String, Object> getContext() {
        return context;
    }

    @Override
    public Set<String> getAllowedApis() {
        return allowedApis;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public void setContext(Map<String, Object> context) {
        this.context = context != null ? context : new HashMap<>();
    }

    public void setAllowedApis(Set<String> allowedApis) {
        this.allowedApis = allowedApis;
    }

    public ScriptGenerationRequestDto addContext(String key, Object value) {
        this.context.put(key, value);
        return this;
    }
}
