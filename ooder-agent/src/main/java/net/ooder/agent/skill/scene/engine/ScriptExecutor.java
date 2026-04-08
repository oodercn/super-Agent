package net.ooder.mvp.skill.scene.engine;

import java.util.Map;
import java.util.Set;

public interface ScriptExecutor {
    ScriptResult execute(ScriptRequest request);
    boolean validate(String script, Set<String> allowedApis);
    void registerFunction(String name, Object function);
    void setGlobalVariable(String name, Object value);

    interface ScriptRequest {
        String getScriptType();
        String getScript();
        Map<String, Object> getContext();
        Set<String> getAllowedApis();
        long getTimeout();
    }

    interface ScriptResult {
        boolean isSuccess();
        Object getResult();
        String getError();
        long getExecutionTime();
    }
}
