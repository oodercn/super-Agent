package net.ooder.nexus.dto.scene;

import java.io.Serializable;
import java.util.Map;

public class SceneInstanceCreateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String sceneId;
    private String instanceName;
    private Map<String, Object> config;

    public String getSceneId() {
        return sceneId;
    }

    public void setSceneId(String sceneId) {
        this.sceneId = sceneId;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public Map<String, Object> getConfig() {
        return config;
    }

    public void setConfig(Map<String, Object> config) {
        this.config = config;
    }
}
