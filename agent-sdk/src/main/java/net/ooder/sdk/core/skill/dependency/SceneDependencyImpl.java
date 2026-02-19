
package net.ooder.sdk.core.skill.dependency;

import net.ooder.sdk.api.skill.SceneDependency;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SceneDependencyImpl implements SceneDependency {
    
    private String sceneName;
    private String description;
    private boolean required = true;
    private List<String> requiredCapabilities = new ArrayList<>();
    private Map<String, Object> config = new HashMap<>();
    private DependencyStatus status = DependencyStatus.PENDING;
    
    public SceneDependencyImpl() {}
    
    public SceneDependencyImpl(String sceneName) {
        this.sceneName = sceneName;
    }
    
    @Override
    public String getSceneName() { return sceneName; }
    public void setSceneName(String sceneName) { this.sceneName = sceneName; }
    
    @Override
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    @Override
    public boolean isRequired() { return required; }
    public void setRequired(boolean required) { this.required = required; }
    
    @Override
    public List<String> getRequiredCapabilities() { return requiredCapabilities; }
    public void setRequiredCapabilities(List<String> requiredCapabilities) {
        this.requiredCapabilities = requiredCapabilities != null ? requiredCapabilities : new ArrayList<>();
    }
    
    @Override
    public Map<String, Object> getConfig() { return config; }
    public void setConfig(Map<String, Object> config) {
        this.config = config != null ? config : new HashMap<>();
    }
    
    @Override
    public DependencyStatus getStatus() { return status; }
    
    @Override
    public void setStatus(DependencyStatus status) { this.status = status; }
}
