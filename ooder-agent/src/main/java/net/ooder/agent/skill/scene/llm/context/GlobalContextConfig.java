package net.ooder.mvp.skill.scene.llm.context;

import java.util.List;
import java.util.Map;

public class GlobalContextConfig {
    
    private List<Map<String, Object>> menuItems;
    private List<Map<String, Object>> globalTools;
    private String systemBasePrompt;
    private Map<String, Object> globalVariables;

    public GlobalContextConfig() {}

    public List<Map<String, Object>> getMenuItems() { return menuItems; }
    public void setMenuItems(List<Map<String, Object>> menuItems) { this.menuItems = menuItems; }
    
    public List<Map<String, Object>> getGlobalTools() { return globalTools; }
    public void setGlobalTools(List<Map<String, Object>> globalTools) { this.globalTools = globalTools; }
    
    public String getSystemBasePrompt() { return systemBasePrompt; }
    public void setSystemBasePrompt(String systemBasePrompt) { this.systemBasePrompt = systemBasePrompt; }
    
    public Map<String, Object> getGlobalVariables() { return globalVariables; }
    public void setGlobalVariables(Map<String, Object> globalVariables) { this.globalVariables = globalVariables; }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final GlobalContextConfig config = new GlobalContextConfig();

        public Builder menuItems(List<Map<String, Object>> menuItems) {
            config.setMenuItems(menuItems);
            return this;
        }

        public Builder globalTools(List<Map<String, Object>> globalTools) {
            config.setGlobalTools(globalTools);
            return this;
        }

        public Builder systemBasePrompt(String prompt) {
            config.setSystemBasePrompt(prompt);
            return this;
        }

        public Builder globalVariables(Map<String, Object> variables) {
            config.setGlobalVariables(variables);
            return this;
        }

        public GlobalContextConfig build() {
            return config;
        }
    }
}
