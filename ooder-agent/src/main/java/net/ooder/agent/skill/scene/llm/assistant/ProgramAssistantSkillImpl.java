package net.ooder.mvp.skill.scene.llm.assistant;

import net.ooder.mvp.skill.scene.llm.ModuleApiRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ProgramAssistantSkillImpl implements ProgramAssistantSkill {

    private static final Logger log = LoggerFactory.getLogger(ProgramAssistantSkillImpl.class);

    private final ModuleApiRegistry moduleApiRegistry;

    @Autowired
    public ProgramAssistantSkillImpl(ModuleApiRegistry moduleApiRegistry) {
        this.moduleApiRegistry = moduleApiRegistry;
    }

    @Override
    public ScriptGenerationResult generateScript(ScriptGenerationRequest request) {
        log.info("[ProgramAssistant] Generating script for module: {}, intent: {}", 
            request.getModule(), request.getIntent());

        try {
            String language = request.getLanguage() != null ? request.getLanguage().toLowerCase() : "mvel";
            String module = request.getModule();
            String intent = request.getIntent();
            Set<String> allowedApis = request.getAllowedApis();
            Map<String, Object> context = request.getContext();

            if (allowedApis == null || allowedApis.isEmpty()) {
                allowedApis = moduleApiRegistry.getAvailableApis(module);
            }

            String script = generateScriptFromIntent(language, module, intent, allowedApis, context);
            String explanation = generateExplanation(script, intent);

            return ScriptGenerationResultDto.success(script, language, module, explanation);

        } catch (Exception e) {
            log.error("[ProgramAssistant] Failed to generate script", e);
            return ScriptGenerationResultDto.failure(e.getMessage());
        }
    }

    private String generateScriptFromIntent(String language, String module, String intent, 
                                           Set<String> allowedApis, Map<String, Object> context) {
        IntentPattern pattern = matchIntentPattern(intent);
        
        if (pattern == null) {
            pattern = IntentPattern.GENERIC_ACTION;
        }

        switch (language) {
            case "mvel":
                return generateMvelScript(module, pattern, allowedApis, context);
            case "javascript":
                return generateJavaScriptScript(module, pattern, allowedApis, context);
            default:
                return generateMvelScript(module, pattern, allowedApis, context);
        }
    }

    private IntentPattern matchIntentPattern(String intent) {
        if (intent == null || intent.isEmpty()) {
            return IntentPattern.GENERIC_ACTION;
        }
        
        String lowerIntent = intent.toLowerCase();

        log.debug("[ProgramAssistant] Matching intent: {}", lowerIntent);

        if (lowerIntent.contains("filter") || lowerIntent.contains("筛选") || lowerIntent.contains("过滤")) {
            return IntentPattern.FILTER;
        }
        if (lowerIntent.contains("select") || lowerIntent.contains("选择")) {
            return IntentPattern.SELECT;
        }
        if (lowerIntent.contains("scan") || lowerIntent.contains("discover") || lowerIntent.contains("扫描") || lowerIntent.contains("发现")) {
            return IntentPattern.SCAN;
        }
        if (lowerIntent.contains("install") || lowerIntent.contains("安装")) {
            return IntentPattern.INSTALL;
        }
        if (lowerIntent.contains("config") || lowerIntent.contains("配置")) {
            return IntentPattern.CONFIG;
        }
        if (lowerIntent.contains("execute") || lowerIntent.contains("run") || lowerIntent.contains("执行") || lowerIntent.contains("运行")) {
            return IntentPattern.EXECUTE;
        }
        if (lowerIntent.contains("next") || lowerIntent.contains("下一步")) {
            return IntentPattern.NEXT;
        }
        if (lowerIntent.contains("prev") || lowerIntent.contains("previous") || lowerIntent.contains("上一步") || lowerIntent.contains("返回")) {
            return IntentPattern.PREV;
        }
        if (lowerIntent.contains("confirm") || lowerIntent.contains("确认")) {
            return IntentPattern.CONFIRM;
        }
        if (lowerIntent.contains("cancel") || lowerIntent.contains("取消")) {
            return IntentPattern.CANCEL;
        }
        if (lowerIntent.contains("detail") || lowerIntent.contains("info") || lowerIntent.contains("详情") || lowerIntent.contains("查看")) {
            return IntentPattern.DETAIL;
        }

        log.debug("[ProgramAssistant] No pattern matched, using GENERIC_ACTION");
        return IntentPattern.GENERIC_ACTION;
    }

    private String generateMvelScript(String module, IntentPattern pattern, 
                                     Set<String> allowedApis, Map<String, Object> context) {
        StringBuilder script = new StringBuilder();

        switch (pattern) {
            case FILTER:
                script.append(generateFilterScript(module, allowedApis, context));
                break;
            case SELECT:
                script.append(generateSelectScript(module, allowedApis, context));
                break;
            case SCAN:
                script.append(generateScanScript(module, allowedApis, context));
                break;
            case INSTALL:
                script.append(generateInstallScript(module, allowedApis, context));
                break;
            case CONFIG:
                script.append(generateConfigScript(module, allowedApis, context));
                break;
            case EXECUTE:
                script.append(generateExecuteScript(module, allowedApis, context));
                break;
            case NEXT:
                script.append(generateNextScript(module, allowedApis, context));
                break;
            case PREV:
                script.append(generatePrevScript(module, allowedApis, context));
                break;
            case CONFIRM:
                script.append(generateConfirmScript(module, allowedApis, context));
                break;
            case CANCEL:
                script.append(generateCancelScript(module, allowedApis, context));
                break;
            case DETAIL:
                script.append(generateDetailScript(module, allowedApis, context));
                break;
            default:
                script.append(generateGenericScript(module, allowedApis, context));
        }

        return script.toString();
    }

    private String generateFilterScript(String module, Set<String> allowedApis, Map<String, Object> context) {
        if (allowedApis.contains("filterCapabilities")) {
            StringBuilder sb = new StringBuilder("filterCapabilities([");
            
            List<String> params = new ArrayList<>();
            if (context.containsKey("keyword")) {
                params.add("'keyword': '" + context.get("keyword") + "'");
            }
            if (context.containsKey("type")) {
                params.add("'type': '" + context.get("type") + "'");
            }
            if (context.containsKey("installed")) {
                params.add("'installed': " + context.get("installed"));
            }
            
            sb.append(String.join(", ", params));
            sb.append("])");
            return sb.toString();
        }
        return "// filterCapabilities API not available in module: " + module;
    }

    private String generateSelectScript(String module, Set<String> allowedApis, Map<String, Object> context) {
        if (allowedApis.contains("selectCapability")) {
            String capabilityId = context.containsKey("capabilityId") 
                ? "'" + context.get("capabilityId") + "'" 
                : "selectedCapabilityId";
            String capabilityName = context.containsKey("capabilityName") 
                ? ", '" + context.get("capabilityName") + "'" 
                : "";
            return "selectCapability(" + capabilityId + capabilityName + ")";
        }
        return "// selectCapability API not available in module: " + module;
    }

    private String generateScanScript(String module, Set<String> allowedApis, Map<String, Object> context) {
        if (allowedApis.contains("startScan")) {
            String method = context.containsKey("method") 
                ? "'" + context.get("method") + "'" 
                : "'AUTO'";
            String forceRefresh = context.containsKey("forceRefresh") 
                ? ", " + context.get("forceRefresh") 
                : "";
            return "startScan(" + method + forceRefresh + ")";
        }
        return "// startScan API not available in module: " + module;
    }

    private String generateInstallScript(String module, Set<String> allowedApis, Map<String, Object> context) {
        if (allowedApis.contains("startInstall")) {
            String capabilityId = context.containsKey("capabilityId") 
                ? "'" + context.get("capabilityId") + "'" 
                : "selectedCapabilityId";
            return "startInstall(" + capabilityId + ")";
        }
        return "// startInstall API not available in module: " + module;
    }

    private String generateConfigScript(String module, Set<String> allowedApis, Map<String, Object> context) {
        if (allowedApis.contains("setConfig")) {
            String key = context.containsKey("key") 
                ? "'" + context.get("key") + "'" 
                : "'configKey'";
            String value = context.containsKey("value") 
                ? "'" + context.get("value") + "'" 
                : "'configValue'";
            return "setConfig(" + key + ", " + value + ")";
        }
        return "// setConfig API not available in module: " + module;
    }

    private String generateExecuteScript(String module, Set<String> allowedApis, Map<String, Object> context) {
        if (allowedApis.contains("executeStep")) {
            String stepId = context.containsKey("stepId") 
                ? "'" + context.get("stepId") + "'" 
                : "'step1'";
            return "executeStep(" + stepId + ")";
        }
        return "// executeStep API not available in module: " + module;
    }

    private String generateNextScript(String module, Set<String> allowedApis, Map<String, Object> context) {
        if (allowedApis.contains("nextStep")) {
            return "nextStep()";
        }
        return "// nextStep API not available in module: " + module;
    }

    private String generatePrevScript(String module, Set<String> allowedApis, Map<String, Object> context) {
        if (allowedApis.contains("prevStep")) {
            return "prevStep()";
        }
        return "// prevStep API not available in module: " + module;
    }

    private String generateConfirmScript(String module, Set<String> allowedApis, Map<String, Object> context) {
        if (allowedApis.contains("confirm")) {
            return "confirm()";
        }
        return "// confirm API not available in module: " + module;
    }

    private String generateCancelScript(String module, Set<String> allowedApis, Map<String, Object> context) {
        if (allowedApis.contains("cancel")) {
            return "cancel()";
        }
        return "// cancel API not available in module: " + module;
    }

    private String generateDetailScript(String module, Set<String> allowedApis, Map<String, Object> context) {
        if (allowedApis.contains("getCapabilityDetail")) {
            String capabilityId = context.containsKey("capabilityId") 
                ? "'" + context.get("capabilityId") + "'" 
                : "selectedCapabilityId";
            return "getCapabilityDetail(" + capabilityId + ")";
        }
        return "// getCapabilityDetail API not available in module: " + module;
    }

    private String generateGenericScript(String module, Set<String> allowedApis, Map<String, Object> context) {
        return "// Generic action for module: " + module + "\n// Available APIs: " + allowedApis;
    }

    private String generateJavaScriptScript(String module, IntentPattern pattern, 
                                           Set<String> allowedApis, Map<String, Object> context) {
        String mvelScript = generateMvelScript(module, pattern, allowedApis, context);
        
        return "(async function() {\n" +
               "    // Module: " + module + "\n" +
               "    // Pattern: " + pattern + "\n" +
               "    const result = await executeMvel('" + mvelScript + "');\n" +
               "    return result;\n" +
               "})();";
    }

    private String generateExplanation(String script, String intent) {
        return "Generated script for intent: " + intent + "\nScript: " + script;
    }

    @Override
    public CodeCompletionResult completeCode(CodeCompletionRequest request) {
        return new CodeCompletionResultDto(false, null, "Code completion not implemented yet", "NOT_IMPLEMENTED");
    }

    @Override
    public CodeExplanationResult explainCode(CodeExplanationRequest request) {
        return new CodeExplanationResultDto(false, "Code explanation not implemented yet", "NOT_IMPLEMENTED");
    }

    @Override
    public DebugSuggestionResult suggestDebug(DebugSuggestionRequest request) {
        return new DebugSuggestionResultDto(false, "Debug suggestion not implemented yet", null, "NOT_IMPLEMENTED");
    }

    private enum IntentPattern {
        FILTER,
        SELECT,
        SCAN,
        INSTALL,
        CONFIG,
        EXECUTE,
        NEXT,
        PREV,
        CONFIRM,
        CANCEL,
        DETAIL,
        CREATE,
        UPDATE,
        DELETE,
        QUERY,
        HELP,
        GENERIC_ACTION
    }

    private static class CodeCompletionResultDto implements CodeCompletionResult {
        private final boolean success;
        private final String completion;
        private final String explanation;
        private final String error;

        CodeCompletionResultDto(boolean success, String completion, String explanation, String error) {
            this.success = success;
            this.completion = completion;
            this.explanation = explanation;
            this.error = error;
        }

        @Override public boolean isSuccess() { return success; }
        @Override public String getCompletion() { return completion; }
        @Override public String getExplanation() { return explanation; }
        @Override public String getError() { return error; }
    }

    private static class CodeExplanationResultDto implements CodeExplanationResult {
        private final boolean success;
        private final String explanation;
        private final String error;

        CodeExplanationResultDto(boolean success, String explanation, String error) {
            this.success = success;
            this.explanation = explanation;
            this.error = error;
        }

        @Override public boolean isSuccess() { return success; }
        @Override public String getExplanation() { return explanation; }
        @Override public String getError() { return error; }
    }

    private static class DebugSuggestionResultDto implements DebugSuggestionResult {
        private final boolean success;
        private final String suggestion;
        private final String fixedCode;
        private final String error;

        DebugSuggestionResultDto(boolean success, String suggestion, String fixedCode, String error) {
            this.success = success;
            this.suggestion = suggestion;
            this.fixedCode = fixedCode;
            this.error = error;
        }

        @Override public boolean isSuccess() { return success; }
        @Override public String getSuggestion() { return suggestion; }
        @Override public String getFixedCode() { return fixedCode; }
        @Override public String getError() { return error; }
    }
}
