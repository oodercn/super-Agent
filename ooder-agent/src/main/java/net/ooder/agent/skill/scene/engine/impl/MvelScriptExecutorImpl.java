package net.ooder.mvp.skill.scene.engine.impl;

import net.ooder.mvp.skill.scene.engine.ScriptExecutor;
import org.mvel2.MVEL;
import org.mvel2.ParserContext;
import org.mvel2.integration.VariableResolverFactory;
import org.mvel2.integration.impl.MapVariableResolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@Component
public class MvelScriptExecutorImpl implements ScriptExecutor {

    private static final Logger log = LoggerFactory.getLogger(MvelScriptExecutorImpl.class);

    private final Map<String, Object> globalVariables = new ConcurrentHashMap<>();
    private final Map<String, Function<Map<String, Object>, Object>> apiHandlers = new ConcurrentHashMap<>();
    private final Set<String> forbiddenPatterns;

    public MvelScriptExecutorImpl() {
        forbiddenPatterns = new HashSet<>();
        forbiddenPatterns.add("Runtime");
        forbiddenPatterns.add("Process");
        forbiddenPatterns.add("System.exit");
        forbiddenPatterns.add("FileInputStream");
        forbiddenPatterns.add("FileOutputStream");
        forbiddenPatterns.add("Socket");
        forbiddenPatterns.add("Class.forName");
        forbiddenPatterns.add("ClassLoader");
        forbiddenPatterns.add("exec");
        forbiddenPatterns.add("getRuntime");
    }

    public void registerApiHandler(String apiName, Function<Map<String, Object>, Object> handler) {
        apiHandlers.put(apiName, handler);
        log.info("[MvelScriptExecutor] Registered API handler: {}", apiName);
    }

    @Override
    public ScriptResult execute(ScriptRequest request) {
        long startTime = System.currentTimeMillis();
        String script = request.getScript();
        
        log.info("[MvelScriptExecutor] Executing script: {}", script);

        if (!validate(script, request.getAllowedApis())) {
            return new ScriptResultImpl(false, null, "Script validation failed: contains forbidden operations", 0);
        }

        try {
            Map<String, Object> executionContext = new HashMap<>();
            executionContext.putAll(globalVariables);
            executionContext.putAll(request.getContext());

            ScriptApiBridge apiBridge = new ScriptApiBridge(request.getAllowedApis(), apiHandlers);
            executionContext.put("_api", apiBridge);

            String wrappedScript = wrapScriptWithApiBridge(script, request.getAllowedApis());

            ParserContext parserContext = new ParserContext();
            parserContext.addInput("_api", ScriptApiBridge.class);
            
            Serializable compiled = MVEL.compileExpression(wrappedScript, parserContext);
            
            Object result = MVEL.executeExpression(compiled, executionContext);
            
            long executionTime = System.currentTimeMillis() - startTime;
            log.info("[MvelScriptExecutor] Script executed successfully in {}ms", executionTime);
            
            return new ScriptResultImpl(true, result, null, executionTime);
            
        } catch (Exception e) {
            long executionTime = System.currentTimeMillis() - startTime;
            log.error("[MvelScriptExecutor] Script execution failed", e);
            return new ScriptResultImpl(false, null, e.getMessage(), executionTime);
        }
    }

    private String wrapScriptWithApiBridge(String script, Set<String> allowedApis) {
        StringBuilder wrapped = new StringBuilder();
        
        for (String api : allowedApis) {
            wrapped.append("def ").append(api).append("(args) { _api.call('").append(api).append("', args); }\n");
        }
        
        wrapped.append(script);
        return wrapped.toString();
    }

    @Override
    public boolean validate(String script, Set<String> allowedApis) {
        if (script == null || script.isEmpty()) {
            return false;
        }

        for (String pattern : forbiddenPatterns) {
            if (script.contains(pattern)) {
                log.warn("[MvelScriptExecutor] Script contains forbidden pattern: {}", pattern);
                return false;
            }
        }

        return true;
    }

    @Override
    public void registerFunction(String name, Object function) {
        log.info("[MvelScriptExecutor] registerFunction called: {}", name);
    }

    @Override
    public void setGlobalVariable(String name, Object value) {
        globalVariables.put(name, value);
        log.debug("[MvelScriptExecutor] Set global variable: {} = {}", name, value);
    }

    public static class ScriptApiBridge {
        private final Set<String> allowedApis;
        private final Map<String, Function<Map<String, Object>, Object>> handlers;

        public ScriptApiBridge(Set<String> allowedApis, Map<String, Function<Map<String, Object>, Object>> handlers) {
            this.allowedApis = allowedApis;
            this.handlers = handlers;
        }

        public Object call(String apiName, Object args) {
            if (!allowedApis.contains(apiName)) {
                throw new RuntimeException("API not allowed: " + apiName);
            }

            Function<Map<String, Object>, Object> handler = handlers.get(apiName);
            if (handler == null) {
                Map<String, Object> result = new HashMap<>();
                result.put("api", apiName);
                result.put("args", args);
                result.put("status", "simulated");
                result.put("timestamp", System.currentTimeMillis());
                return result;
            }

            Map<String, Object> params = new HashMap<>();
            if (args instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> argsMap = (Map<String, Object>) args;
                params.putAll(argsMap);
            } else if (args != null) {
                params.put("value", args);
            }
            
            return handler.apply(params);
        }
    }

    private static class ScriptResultImpl implements ScriptResult {
        private final boolean success;
        private final Object result;
        private final String error;
        private final long executionTime;

        ScriptResultImpl(boolean success, Object result, String error, long executionTime) {
            this.success = success;
            this.result = result;
            this.error = error;
            this.executionTime = executionTime;
        }

        @Override public boolean isSuccess() { return success; }
        @Override public Object getResult() { return result; }
        @Override public String getError() { return error; }
        @Override public long getExecutionTime() { return executionTime; }
    }
}
