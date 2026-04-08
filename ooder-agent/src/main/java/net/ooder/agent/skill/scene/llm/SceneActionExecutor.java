package net.ooder.mvp.skill.scene.llm;

import net.ooder.mvp.skill.scene.engine.ContextTrustLayer;
import net.ooder.mvp.skill.scene.engine.ScriptExecutor;
import net.ooder.mvp.skill.scene.llm.assistant.ProgramAssistantSkill;
import net.ooder.mvp.skill.scene.llm.assistant.ScriptGenerationRequestDto;
import net.ooder.mvp.skill.scene.llm.assistant.ScriptGenerationResultDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class SceneActionExecutor {

    private static final Logger log = LoggerFactory.getLogger(SceneActionExecutor.class);

    private final ModuleApiRegistry moduleApiRegistry;
    private final SceneContextProvider contextProvider;
    private final ProgramAssistantSkill programAssistant;
    private final ScriptExecutor scriptExecutor;
    private final ContextTrustLayer contextTrustLayer;

    @Autowired
    public SceneActionExecutor(ModuleApiRegistry moduleApiRegistry, 
                               SceneContextProvider contextProvider,
                               ProgramAssistantSkill programAssistant,
                               ScriptExecutor scriptExecutor,
                               ContextTrustLayer contextTrustLayer) {
        this.moduleApiRegistry = moduleApiRegistry;
        this.contextProvider = contextProvider;
        this.programAssistant = programAssistant;
        this.scriptExecutor = scriptExecutor;
        this.contextTrustLayer = contextTrustLayer;
    }

    public ActionResult execute(ActionRequest request) {
        log.info("[SceneActionExecutor] Executing action: {} for module: {}", 
            request.getAction(), request.getModule());

        String currentModule = moduleApiRegistry.getCurrentModule();
        String targetModule = request.getModule();

        if (targetModule != null && !targetModule.equals(currentModule)) {
            if (request.isRequireConfirm()) {
                return ActionResult.needConfirm(
                    "跨模块操作：当前模块[" + currentModule + "]，目标模块[" + targetModule + "]，是否继续？",
                    request
                );
            } else {
                return ActionResult.failure("模块不匹配：当前模块[" + currentModule + "]无法执行[" + targetModule + "]模块的操作");
            }
        }

        Set<String> allowedApis = moduleApiRegistry.getAvailableApis(currentModule);
        
        if (!validateApiCall(request.getAction(), allowedApis)) {
            return ActionResult.failure("API不允许调用: " + request.getAction() + 
                "，当前模块[" + currentModule + "]可用API: " + allowedApis);
        }

        try {
            Object result = executeApiCall(request.getAction(), request.getParams(), currentModule);
            
            if (request.isSyncContext()) {
                syncContext(currentModule, result);
            }
            
            return ActionResult.success(result);
            
        } catch (Exception e) {
            log.error("[SceneActionExecutor] Failed to execute action: {}", request.getAction(), e);
            return ActionResult.failure(e.getMessage());
        }
    }

    public ScriptResult executeScript(ScriptRequest request) {
        log.info("[SceneActionExecutor] Executing script of type: {} for module: {}", 
            request.getScriptType(), request.getModule());

        String currentModule = moduleApiRegistry.getCurrentModule();
        String targetModule = request.getModule();

        if (targetModule != null && !targetModule.equals(currentModule)) {
            if (request.isRequireConfirm()) {
                return ScriptResult.needConfirm(
                    "跨模块操作：当前模块[" + currentModule + "]，目标模块[" + targetModule + "]，是否继续？",
                    request
                );
            } else {
                return ScriptResult.failure("模块不匹配：当前模块[" + currentModule + "]无法执行[" + targetModule + "]模块的脚本");
            }
        }

        Set<String> allowedApis = moduleApiRegistry.getAvailableApis(currentModule);

        if (!scriptExecutor.validate(request.getScript(), allowedApis)) {
            return ScriptResult.failure("脚本安全验证失败：包含不允许的操作");
        }

        try {
            ScriptExecutor.ScriptRequest scriptRequest = createScriptRequest(request, allowedApis);
            ScriptExecutor.ScriptResult result = scriptExecutor.execute(scriptRequest);
            
            if (result.isSuccess()) {
                if (request.isSyncContext()) {
                    syncContext(currentModule, result.getResult());
                }
                return ScriptResult.success(result.getResult());
            } else {
                return ScriptResult.failure(result.getError());
            }
            
        } catch (Exception e) {
            log.error("[SceneActionExecutor] Failed to execute script", e);
            return ScriptResult.failure(e.getMessage());
        }
    }

    private ScriptExecutor.ScriptRequest createScriptRequest(ScriptRequest request, Set<String> allowedApis) {
        final String script = request.getScript();
        final String scriptType = request.getScriptType();
        final Set<String> apis = allowedApis;
        
        return new ScriptExecutor.ScriptRequest() {
            @Override
            public String getScriptType() {
                return scriptType;
            }
            
            @Override
            public String getScript() {
                return script;
            }
            
            @Override
            public Map<String, Object> getContext() {
                Map<String, Object> ctx = new HashMap<>();
                ctx.put("currentModule", request.getModule());
                ctx.put("allowedApis", apis);
                return ctx;
            }
            
            @Override
            public Set<String> getAllowedApis() {
                return apis;
            }
            
            @Override
            public long getTimeout() {
                return 5000;
            }
        };
    }

    public ScriptGenerationResultDto generateScriptForIntent(String intent, Map<String, Object> context) {
        String currentModule = moduleApiRegistry.getCurrentModule();
        Set<String> allowedApis = moduleApiRegistry.getAvailableApis(currentModule);

        ScriptGenerationRequestDto request = new ScriptGenerationRequestDto("mvel", currentModule, intent);
        request.setAllowedApis(allowedApis);
        request.setContext(context);

        return (ScriptGenerationResultDto) programAssistant.generateScript(request);
    }

    private boolean validateApiCall(String action, Set<String> allowedApis) {
        return allowedApis.contains(action);
    }

    private Object executeApiCall(String action, Map<String, Object> params, String module) {
        log.debug("[SceneActionExecutor] Executing API: {} with params: {}", action, params);

        switch (module) {
            case "discovery":
                return executeDiscoveryApi(action, params);
            case "install":
                return executeInstallApi(action, params);
            case "activation":
                return executeActivationApi(action, params);
            default:
                throw new UnsupportedOperationException("Unknown module: " + module);
        }
    }

    private Object executeDiscoveryApi(String action, Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        result.put("action", action);
        result.put("module", "discovery");
        result.put("params", params);
        result.put("status", "executed");
        result.put("timestamp", System.currentTimeMillis());
        
        return result;
    }

    private Object executeInstallApi(String action, Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        result.put("action", action);
        result.put("module", "install");
        result.put("params", params);
        result.put("status", "executed");
        result.put("timestamp", System.currentTimeMillis());
        
        return result;
    }

    private Object executeActivationApi(String action, Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        result.put("action", action);
        result.put("module", "activation");
        result.put("params", params);
        result.put("status", "executed");
        result.put("timestamp", System.currentTimeMillis());
        
        return result;
    }

    private void syncContext(String module, Object result) {
        log.debug("[SceneActionExecutor] Syncing context for module: {}", module);
        
        Map<String, Object> syncData = new HashMap<>();
        syncData.put("lastResult", result);
        syncData.put("lastAction", System.currentTimeMillis());
        
        String sessionId = contextTrustLayer.getOrCreateSession("current-user");
        contextTrustLayer.updateContext(sessionId, module, syncData);
    }

    public static class ActionRequest {
        private String action;
        private String module;
        private Map<String, Object> params = new HashMap<>();
        private boolean requireConfirm = false;
        private boolean syncContext = false;

        public String getAction() { return action; }
        public void setAction(String action) { this.action = action; }
        public String getModule() { return module; }
        public void setModule(String module) { this.module = module; }
        public Map<String, Object> getParams() { return params; }
        public void setParams(Map<String, Object> params) { this.params = params; }
        public boolean isRequireConfirm() { return requireConfirm; }
        public void setRequireConfirm(boolean requireConfirm) { this.requireConfirm = requireConfirm; }
        public boolean isSyncContext() { return syncContext; }
        public void setSyncContext(boolean syncContext) { this.syncContext = syncContext; }
    }

    public static class ActionResult {
        private final boolean success;
        private final Object result;
        private final String error;
        private final boolean needConfirm;
        private final String confirmMessage;
        private final ActionRequest pendingAction;

        private ActionResult(boolean success, Object result, String error, 
                            boolean needConfirm, String confirmMessage, ActionRequest pendingAction) {
            this.success = success;
            this.result = result;
            this.error = error;
            this.needConfirm = needConfirm;
            this.confirmMessage = confirmMessage;
            this.pendingAction = pendingAction;
        }

        public static ActionResult success(Object result) {
            return new ActionResult(true, result, null, false, null, null);
        }

        public static ActionResult failure(String error) {
            return new ActionResult(false, null, error, false, null, null);
        }

        public static ActionResult needConfirm(String message, ActionRequest pendingAction) {
            return new ActionResult(false, null, null, true, message, pendingAction);
        }

        public boolean isSuccess() { return success; }
        public Object getResult() { return result; }
        public String getError() { return error; }
        public boolean isNeedConfirm() { return needConfirm; }
        public String getConfirmMessage() { return confirmMessage; }
        public ActionRequest getPendingAction() { return pendingAction; }
    }

    public static class ScriptRequest {
        private String script;
        private String scriptType;
        private String module;
        private boolean requireConfirm = false;
        private boolean syncContext = false;

        public String getScript() { return script; }
        public void setScript(String script) { this.script = script; }
        public String getScriptType() { return scriptType; }
        public void setScriptType(String scriptType) { this.scriptType = scriptType; }
        public String getModule() { return module; }
        public void setModule(String module) { this.module = module; }
        public boolean isRequireConfirm() { return requireConfirm; }
        public void setRequireConfirm(boolean requireConfirm) { this.requireConfirm = requireConfirm; }
        public boolean isSyncContext() { return syncContext; }
        public void setSyncContext(boolean syncContext) { this.syncContext = syncContext; }
    }

    public static class ScriptResult {
        private final boolean success;
        private final Object result;
        private final String error;
        private final boolean needConfirm;
        private final String confirmMessage;
        private final ScriptRequest pendingScript;

        private ScriptResult(boolean success, Object result, String error,
                            boolean needConfirm, String confirmMessage, ScriptRequest pendingScript) {
            this.success = success;
            this.result = result;
            this.error = error;
            this.needConfirm = needConfirm;
            this.confirmMessage = confirmMessage;
            this.pendingScript = pendingScript;
        }

        public static ScriptResult success(Object result) {
            return new ScriptResult(true, result, null, false, null, null);
        }

        public static ScriptResult failure(String error) {
            return new ScriptResult(false, null, error, false, null, null);
        }

        public static ScriptResult needConfirm(String message, ScriptRequest pendingScript) {
            return new ScriptResult(false, null, null, true, message, pendingScript);
        }

        public boolean isSuccess() { return success; }
        public Object getResult() { return result; }
        public String getError() { return error; }
        public boolean isNeedConfirm() { return needConfirm; }
        public String getConfirmMessage() { return confirmMessage; }
        public ScriptRequest getPendingScript() { return pendingScript; }
    }
}
