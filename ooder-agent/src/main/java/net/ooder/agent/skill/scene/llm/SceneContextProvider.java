package net.ooder.mvp.skill.scene.llm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SceneContextProvider {

    private static final Logger log = LoggerFactory.getLogger(SceneContextProvider.class);

    @Autowired
    private ModuleApiRegistry moduleApiRegistry;

    private final ThreadLocal<Map<String, Object>> pageStateHolder = new ThreadLocal<>();

    public void setPageState(String module, Map<String, Object> state) {
        Map<String, Object> pageState = pageStateHolder.get();
        if (pageState == null) {
            pageState = new HashMap<>();
            pageStateHolder.set(pageState);
        }
        pageState.put(module, state);
        log.debug("[setPageState] Module: {}, state keys: {}", module, state.keySet());
    }

    public Map<String, Object> getPageState(String module) {
        Map<String, Object> pageState = pageStateHolder.get();
        if (pageState == null) {
            return new HashMap<>();
        }
        Map<String, Object> moduleState = (Map<String, Object>) pageState.get(module);
        return moduleState != null ? moduleState : new HashMap<>();
    }

    public Map<String, Object> buildDiscoveryContext() {
        Map<String, Object> context = new HashMap<>();
        
        context.put("module", "discovery");
        context.put("availableApis", moduleApiRegistry.getAvailableApis("discovery"));
        context.put("pageState", getPageState("discovery"));
        
        return context;
    }

    public Map<String, Object> buildInstallContext() {
        Map<String, Object> context = new HashMap<>();
        
        context.put("module", "install");
        context.put("availableApis", moduleApiRegistry.getAvailableApis("install"));
        context.put("pageState", getPageState("install"));
        
        return context;
    }

    public Map<String, Object> buildActivationContext() {
        Map<String, Object> context = new HashMap<>();
        
        context.put("module", "activation");
        context.put("availableApis", moduleApiRegistry.getAvailableApis("activation"));
        context.put("pageState", getPageState("activation"));
        
        return context;
    }

    public Map<String, Object> buildCurrentModuleContext() {
        String currentModule = moduleApiRegistry.getCurrentModule();
        if (currentModule == null) {
            return new HashMap<>();
        }
        
        switch (currentModule) {
            case "discovery":
                return buildDiscoveryContext();
            case "install":
                return buildInstallContext();
            case "activation":
                return buildActivationContext();
            default:
                Map<String, Object> context = new HashMap<>();
                context.put("module", currentModule);
                context.put("availableApis", moduleApiRegistry.getAvailableApis(currentModule));
                context.put("pageState", getPageState(currentModule));
                return context;
        }
    }

    public void clearPageState() {
        pageStateHolder.remove();
    }

    public void updatePageStateField(String module, String key, Object value) {
        Map<String, Object> pageState = pageStateHolder.get();
        if (pageState == null) {
            pageState = new HashMap<>();
            pageStateHolder.set(pageState);
        }
        
        @SuppressWarnings("unchecked")
        Map<String, Object> moduleState = (Map<String, Object>) pageState.computeIfAbsent(module, k -> new HashMap<>());
        moduleState.put(key, value);
        
        log.debug("[updatePageStateField] Module: {}, key: {}, value: {}", module, key, value);
    }

    public Object getPageStateField(String module, String key) {
        Map<String, Object> moduleState = getPageState(module);
        return moduleState.get(key);
    }
}
