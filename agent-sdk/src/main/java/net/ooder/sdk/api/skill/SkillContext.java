package net.ooder.sdk.api.skill;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SkillContext implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String sceneId;
    private String groupId;
    private String agentId;
    private String skillId;
    private Map<String, Object> config;
    private Map<String, Object> attributes;
    private Map<String, Object> endpoints;
    private long createTime;
    
    public SkillContext() {
        this.config = new HashMap<>();
        this.attributes = new HashMap<>();
        this.endpoints = new HashMap<>();
        this.createTime = System.currentTimeMillis();
    }
    
    public static SkillContext create(String sceneId, String groupId) {
        SkillContext context = new SkillContext();
        context.setSceneId(sceneId);
        context.setGroupId(groupId);
        return context;
    }
    
    public static SkillContext create(String sceneId, String groupId, String skillId) {
        SkillContext context = create(sceneId, groupId);
        context.setSkillId(skillId);
        return context;
    }
    
    public String getSceneId() {
        return sceneId;
    }
    
    public void setSceneId(String sceneId) {
        this.sceneId = sceneId;
    }
    
    public String getGroupId() {
        return groupId;
    }
    
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
    
    public String getAgentId() {
        return agentId;
    }
    
    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }
    
    public String getSkillId() {
        return skillId;
    }
    
    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }
    
    public Map<String, Object> getConfig() {
        return config;
    }
    
    public void setConfig(Map<String, Object> config) {
        this.config = config != null ? config : new HashMap<>();
    }
    
    public Object getConfig(String key) {
        return config.get(key);
    }
    
    public String getConfigString(String key) {
        Object value = config.get(key);
        return value != null ? value.toString() : null;
    }
    
    public int getConfigInt(String key, int defaultValue) {
        Object value = config.get(key);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return defaultValue;
    }
    
    public long getConfigLong(String key, long defaultValue) {
        Object value = config.get(key);
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        return defaultValue;
    }
    
    public boolean getConfigBoolean(String key, boolean defaultValue) {
        Object value = config.get(key);
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        return defaultValue;
    }
    
    public void setConfig(String key, Object value) {
        this.config.put(key, value);
    }
    
    public Map<String, Object> getAttributes() {
        return attributes;
    }
    
    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes != null ? attributes : new HashMap<>();
    }
    
    public Object getAttribute(String key) {
        return attributes.get(key);
    }
    
    public void setAttribute(String key, Object value) {
        this.attributes.put(key, value);
    }
    
    public Map<String, Object> getEndpoints() {
        return endpoints;
    }
    
    public void setEndpoints(Map<String, Object> endpoints) {
        this.endpoints = endpoints != null ? endpoints : new HashMap<>();
    }
    
    public void addEndpoint(String name, String endpoint) {
        this.endpoints.put(name, endpoint);
    }
    
    public String getEndpoint(String name) {
        Object endpoint = endpoints.get(name);
        return endpoint != null ? endpoint.toString() : null;
    }
    
    public long getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
    
    public String getFullGroupId() {
        return sceneId + ":" + groupId;
    }
    
    @Override
    public String toString() {
        return "SkillContext{" +
                "sceneId='" + sceneId + '\'' +
                ", groupId='" + groupId + '\'' +
                ", skillId='" + skillId + '\'' +
                ", agentId='" + agentId + '\'' +
                '}';
    }
}
