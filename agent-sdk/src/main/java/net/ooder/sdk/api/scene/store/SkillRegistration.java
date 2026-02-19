package net.ooder.sdk.api.scene.store;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SkillRegistration implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String skillId;
    private String sceneId;
    private String groupId;
    private String skillType;
    private Map<String, Object> endpoints;
    private long registerTime;
    private long lastHeartbeat;
    private String status;
    private Map<String, Object> metadata;
    
    public SkillRegistration() {
        this.endpoints = new ConcurrentHashMap<>();
        this.metadata = new ConcurrentHashMap<>();
        this.registerTime = System.currentTimeMillis();
        this.lastHeartbeat = System.currentTimeMillis();
        this.status = "registered";
    }
    
    public SkillRegistration(String skillId, String sceneId, String groupId, String skillType) {
        this();
        this.skillId = skillId;
        this.sceneId = sceneId;
        this.groupId = groupId;
        this.skillType = skillType;
    }
    
    public String getSkillId() {
        return skillId;
    }
    
    public void setSkillId(String skillId) {
        this.skillId = skillId;
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
    
    public String getSkillType() {
        return skillType;
    }
    
    public void setSkillType(String skillType) {
        this.skillType = skillType;
    }
    
    public Map<String, Object> getEndpoints() {
        return endpoints;
    }
    
    public void setEndpoints(Map<String, Object> endpoints) {
        this.endpoints = endpoints != null ? endpoints : new ConcurrentHashMap<>();
    }
    
    public void addEndpoint(String name, String endpoint) {
        this.endpoints.put(name, endpoint);
    }
    
    public String getEndpoint(String name) {
        Object endpoint = endpoints.get(name);
        return endpoint != null ? endpoint.toString() : null;
    }
    
    public long getRegisterTime() {
        return registerTime;
    }
    
    public void setRegisterTime(long registerTime) {
        this.registerTime = registerTime;
    }
    
    public long getLastHeartbeat() {
        return lastHeartbeat;
    }
    
    public void setLastHeartbeat(long lastHeartbeat) {
        this.lastHeartbeat = lastHeartbeat;
    }
    
    public void updateHeartbeat() {
        this.lastHeartbeat = System.currentTimeMillis();
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Map<String, Object> getMetadata() {
        return metadata;
    }
    
    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata != null ? metadata : new ConcurrentHashMap<>();
    }
    
    public boolean isAlive(long timeoutMs) {
        return (System.currentTimeMillis() - lastHeartbeat) < timeoutMs;
    }
    
    @Override
    public String toString() {
        return "SkillRegistration{" +
                "skillId='" + skillId + '\'' +
                ", sceneId='" + sceneId + '\'' +
                ", groupId='" + groupId + '\'' +
                ", skillType='" + skillType + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
