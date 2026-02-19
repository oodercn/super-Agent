package net.ooder.sdk.api.skill;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface SkillService {
    
    String SCENE_SYS = "SYS";
    
    String getSkillId();
    
    String getSkillType();
    
    String getSceneId();
    
    String getGroupId();
    
    void initialize(SkillContext context);
    
    void start();
    
    void stop();
    
    Map<String, Object> getSkillInfo();
    
    Map<String, Object> getCapabilities();
    
    Object execute(SkillRequest request);
    
    CompletableFuture<Object> executeAsync(SkillRequest request);
    
    void executeAsync(SkillRequest request, SkillCallback callback);
    
    boolean isRunning();
    
    String getStatus();
    
    default boolean supportsOperation(String operation) {
        return true;
    }
    
    default int getDefaultTimeout() {
        return 30000;
    }
    
    default String getDeclaredSceneId() {
        return getSceneId();
    }
    
    default String getDeclaredSceneName() {
        return getDeclaredSceneId();
    }
    
    default String getDeclaredSceneType() {
        return "default";
    }
    
    default boolean isRootService() {
        return false;
    }
    
    default List<Map<String, Object>> getCollaborativeGroups() {
        return java.util.Collections.emptyList();
    }
    
    default List<Map<String, Object>> getLinks() {
        return java.util.Collections.emptyList();
    }
    
    default SkillManifest getManifest() {
        return null;
    }
    
    default Map<String, Object> getSceneConfig() {
        return java.util.Collections.emptyMap();
    }
}
