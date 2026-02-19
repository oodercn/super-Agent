package net.ooder.sdk.api.skill;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SkillDefinition implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    public static final String TYPE_ORG = "skill-org";
    public static final String TYPE_VFS = "skill-vfs";
    public static final String TYPE_MSG = "skill-msg";
    public static final String TYPE_AGENT = "skill-agent";
    
    private String skillId;
    private String skillType;
    private String sceneId;
    private String groupId;
    private String name;
    private String description;
    private String version;
    private Map<String, Object> config;
    private Map<String, Object> capabilities;
    private List<String> supportedOperations;
    private int timeout;
    private boolean asyncSupported;
    private Map<String, Object> metadata;
    
    public SkillDefinition() {
        this.config = new ConcurrentHashMap<>();
        this.capabilities = new ConcurrentHashMap<>();
        this.supportedOperations = new ArrayList<>();
        this.metadata = new ConcurrentHashMap<>();
        this.timeout = 30000;
        this.asyncSupported = true;
    }
    
    public SkillDefinition(String skillId, String skillType) {
        this();
        this.skillId = skillId;
        this.skillType = skillType;
    }
    
    public static SkillDefinition create(String skillId, String skillType) {
        return new SkillDefinition(skillId, skillType);
    }
    
    public String getSkillId() {
        return skillId;
    }
    
    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }
    
    public String getSkillType() {
        return skillType;
    }
    
    public void setSkillType(String skillType) {
        this.skillType = skillType;
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
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getVersion() {
        return version;
    }
    
    public void setVersion(String version) {
        this.version = version;
    }
    
    public Map<String, Object> getConfig() {
        return config;
    }
    
    public void setConfig(Map<String, Object> config) {
        this.config = config != null ? config : new ConcurrentHashMap<>();
    }
    
    public Object getConfig(String key) {
        return config.get(key);
    }
    
    public void setConfig(String key, Object value) {
        this.config.put(key, value);
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
    
    public Map<String, Object> getCapabilities() {
        return capabilities;
    }
    
    public void setCapabilities(Map<String, Object> capabilities) {
        this.capabilities = capabilities != null ? capabilities : new ConcurrentHashMap<>();
    }
    
    public void addCapability(String name, Object capability) {
        this.capabilities.put(name, capability);
    }
    
    public List<String> getSupportedOperations() {
        return supportedOperations;
    }
    
    public void setSupportedOperations(List<String> supportedOperations) {
        this.supportedOperations = supportedOperations != null ? supportedOperations : new ArrayList<>();
    }
    
    public void addSupportedOperation(String operation) {
        if (!this.supportedOperations.contains(operation)) {
            this.supportedOperations.add(operation);
        }
    }
    
    public boolean supportsOperation(String operation) {
        return supportedOperations.contains(operation);
    }
    
    public int getTimeout() {
        return timeout;
    }
    
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
    
    public boolean isAsyncSupported() {
        return asyncSupported;
    }
    
    public void setAsyncSupported(boolean asyncSupported) {
        this.asyncSupported = asyncSupported;
    }
    
    public Map<String, Object> getMetadata() {
        return metadata;
    }
    
    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata != null ? metadata : new ConcurrentHashMap<>();
    }
    
    public SkillDefinition sceneId(String sceneId) {
        this.sceneId = sceneId;
        return this;
    }
    
    public SkillDefinition groupId(String groupId) {
        this.groupId = groupId;
        return this;
    }
    
    public SkillDefinition name(String name) {
        this.name = name;
        return this;
    }
    
    public SkillDefinition description(String description) {
        this.description = description;
        return this;
    }
    
    public SkillDefinition version(String version) {
        this.version = version;
        return this;
    }
    
    public SkillDefinition timeout(int timeout) {
        this.timeout = timeout;
        return this;
    }
    
    @Override
    public String toString() {
        return "SkillDefinition{" +
                "skillId='" + skillId + '\'' +
                ", skillType='" + skillType + '\'' +
                ", sceneId='" + sceneId + '\'' +
                ", groupId='" + groupId + '\'' +
                '}';
    }
}
