package net.ooder.mvp.skill.scene.config.service;

import net.ooder.mvp.skill.scene.config.sdk.ConfigNode;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface ConfigLoaderService {

    ConfigNode loadSystemConfig();
    
    ConfigNode loadSkillConfig(String skillId, boolean resolveInheritance);
    
    ConfigNode loadSceneConfig(String sceneId, boolean resolveInheritance);
    
    ConfigNode loadInternalSkillConfig(String sceneId, String skillId);
    
    ConfigInheritanceChain getInheritanceChain(String targetType, String targetId);
    
    void saveConfig(String targetType, String targetId, ConfigNode config);
    
    void resetConfig(String targetType, String targetId, String key);
    
    void updateConfig(String targetType, String targetId, String path, Object value);
    
    Map<String, Object> getCapabilityConfig(String targetType, String targetId, String capabilityAddress);
    
    void updateCapabilityConfig(String targetType, String targetId, String capabilityAddress, Map<String, Object> config);
    
    void switchProfile(String profile);
    
    // ==================== SE SDK 集成方法 ====================
    
    ConfigHistory getConfigHistorySync(String configId);
    
    boolean rollbackConfigSync(String configId, int version);
    
    String exportConfigSync(String configId, String format);
    
    boolean importConfigSync(String configId, String format, String content);
    
    CompletableFuture<ConfigHistory> getConfigHistory(String configId);
    
    CompletableFuture<Boolean> rollbackConfig(String configId, int version);
    
    CompletableFuture<String> exportConfig(String configId, String format);
    
    CompletableFuture<Boolean> importConfig(String configId, String format, String content);
    
    public static class ConfigHistory {
        private String configId;
        private List<ConfigVersion> versions;
        
        public String getConfigId() { return configId; }
        public void setConfigId(String configId) { this.configId = configId; }
        public List<ConfigVersion> getVersions() { return versions; }
        public void setVersions(List<ConfigVersion> versions) { this.versions = versions; }
    }
    
    public static class ConfigVersion {
        private int version;
        private long timestamp;
        private String operator;
        private String description;
        
        public int getVersion() { return version; }
        public void setVersion(int version) { this.version = version; }
        public long getTimestamp() { return timestamp; }
        public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
        public String getOperator() { return operator; }
        public void setOperator(String operator) { this.operator = operator; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }
}
