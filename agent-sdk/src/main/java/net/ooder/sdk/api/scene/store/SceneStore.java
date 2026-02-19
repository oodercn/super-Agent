package net.ooder.sdk.api.scene.store;

import java.util.List;
import java.util.Map;

public interface SceneStore {
    
    void saveScene(String sceneId, Map<String, Object> config);
    
    Map<String, Object> loadScene(String sceneId);
    
    void deleteScene(String sceneId);
    
    List<String> listScenes();
    
    boolean sceneExists(String sceneId);
    
    void updateSceneConfig(String sceneId, String key, Object value);
    
    Object getSceneConfigValue(String sceneId, String key);
}
