package net.ooder.mvp.skill.scene.llm.assistant;

public class ScriptGenerationResultDto implements ProgramAssistantSkill.ScriptGenerationResult {

    private boolean success;
    private String script;
    private String scriptType;
    private String module;
    private String explanation;
    private String error;

    public ScriptGenerationResultDto() {}

    public ScriptGenerationResultDto(boolean success, String script, String scriptType) {
        this.success = success;
        this.script = script;
        this.scriptType = scriptType;
    }

    public static ScriptGenerationResultDto success(String script, String scriptType, String module, String explanation) {
        ScriptGenerationResultDto result = new ScriptGenerationResultDto(true, script, scriptType);
        result.setModule(module);
        result.setExplanation(explanation);
        return result;
    }

    public static ScriptGenerationResultDto failure(String error) {
        ScriptGenerationResultDto result = new ScriptGenerationResultDto(false, null, null);
        result.setError(error);
        return result;
    }

    @Override
    public boolean isSuccess() {
        return success;
    }

    @Override
    public String getScript() {
        return script;
    }

    @Override
    public String getScriptType() {
        return scriptType;
    }

    @Override
    public String getModule() {
        return module;
    }

    @Override
    public String getExplanation() {
        return explanation;
    }

    @Override
    public String getError() {
        return error;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public void setScriptType(String scriptType) {
        this.scriptType = scriptType;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public void setError(String error) {
        this.error = error;
    }
}
