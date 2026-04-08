package net.ooder.mvp.skill.scene.llm;

import net.ooder.scene.skill.LlmProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class FunctionCallingRegistry {

    private static final Logger log = LoggerFactory.getLogger(FunctionCallingRegistry.class);

    private final Map<String, FunctionDefinition> functions = new ConcurrentHashMap<>();
    private final Map<String, FunctionExecutor> executors = new ConcurrentHashMap<>();

    public interface FunctionDefinition {
        String getName();
        String getDescription();
        Map<String, ParamDefinition> getParameters();
        String getModule();
    }

    public interface ParamDefinition {
        String getType();
        String getDescription();
        boolean isRequired();
        List<String> getEnumValues();
    }

    public interface FunctionExecutor {
        Object execute(Map<String, Object> args);
    }

    public void registerFunction(String name, String description, String module,
                                 Map<String, ParamDefinition> parameters,
                                 FunctionExecutor executor) {
        FunctionDefinition def = new FunctionDefinition() {
            @Override public String getName() { return name; }
            @Override public String getDescription() { return description; }
            @Override public Map<String, ParamDefinition> getParameters() { return parameters; }
            @Override public String getModule() { return module; }
        };
        
        functions.put(name, def);
        executors.put(name, executor);
        log.info("[FunctionCallingRegistry] Registered function: {} for module: {}", name, module);
    }

    public List<Map<String, Object>> getToolsForModule(String module) {
        List<Map<String, Object>> tools = new ArrayList<>();
        
        for (FunctionDefinition func : functions.values()) {
            if (module == null || module.equals(func.getModule())) {
                Map<String, Object> tool = new HashMap<>();
                tool.put("type", "function");
                
                Map<String, Object> function = new HashMap<>();
                function.put("name", func.getName());
                function.put("description", func.getDescription());
                
                Map<String, Object> parameters = new HashMap<>();
                parameters.put("type", "object");
                
                Map<String, Object> properties = new HashMap<>();
                List<String> required = new ArrayList<>();
                
                for (Map.Entry<String, ParamDefinition> entry : func.getParameters().entrySet()) {
                    Map<String, Object> prop = new HashMap<>();
                    prop.put("type", entry.getValue().getType());
                    prop.put("description", entry.getValue().getDescription());
                    
                    List<String> enumValues = entry.getValue().getEnumValues();
                    if (enumValues != null && !enumValues.isEmpty()) {
                        prop.put("enum", enumValues);
                    }
                    
                    properties.put(entry.getKey(), prop);
                    
                    if (entry.getValue().isRequired()) {
                        required.add(entry.getKey());
                    }
                }
                
                parameters.put("properties", properties);
                parameters.put("required", required);
                
                function.put("parameters", parameters);
                tool.put("function", function);
                
                tools.add(tool);
            }
        }
        
        return tools;
    }

    public Object executeFunction(String name, Map<String, Object> args) {
        FunctionExecutor executor = executors.get(name);
        if (executor == null) {
            log.warn("[FunctionCallingRegistry] Function not found: {}", name);
            Map<String, Object> errorResult = new HashMap<String, Object>();
            errorResult.put("error", "Function not found: " + name);
            return errorResult;
        }
        
        try {
            log.info("[FunctionCallingRegistry] Executing function: {} with args: {}", name, args);
            Object result = executor.execute(args);
            log.info("[FunctionCallingRegistry] Function {} executed successfully", name);
            return result;
        } catch (Exception e) {
            log.error("[FunctionCallingRegistry] Function {} execution failed", name, e);
            Map<String, Object> errorResult = new HashMap<String, Object>();
            errorResult.put("error", e.getMessage());
            return errorResult;
        }
    }

    public boolean hasFunction(String name) {
        return functions.containsKey(name);
    }

    public Set<String> getFunctionNames() {
        return functions.keySet();
    }
}
