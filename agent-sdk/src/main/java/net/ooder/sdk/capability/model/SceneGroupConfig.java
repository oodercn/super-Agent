package net.ooder.sdk.capability.model;

import java.util.Map;

public class SceneGroupConfig {
    
    private String name;
    private String sceneId;
    private int minMembers;
    private int maxMembers;
    private String primarySelectionPolicy;
    private Map<String, Object> properties;
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getSceneId() { return sceneId; }
    public void setSceneId(String sceneId) { this.sceneId = sceneId; }
    
    public int getMinMembers() { return minMembers; }
    public void setMinMembers(int minMembers) { this.minMembers = minMembers; }
    
    public int getMaxMembers() { return maxMembers; }
    public void setMaxMembers(int maxMembers) { this.maxMembers = maxMembers; }
    
    public String getPrimarySelectionPolicy() { return primarySelectionPolicy; }
    public void setPrimarySelectionPolicy(String primarySelectionPolicy) { this.primarySelectionPolicy = primarySelectionPolicy; }
    
    public Map<String, Object> getProperties() { return properties; }
    public void setProperties(Map<String, Object> properties) { this.properties = properties; }
}
