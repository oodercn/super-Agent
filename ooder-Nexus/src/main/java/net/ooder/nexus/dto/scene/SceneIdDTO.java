package net.ooder.nexus.dto.scene;

import java.io.Serializable;

public class SceneIdDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String sceneId;

    public String getSceneId() {
        return sceneId;
    }

    public void setSceneId(String sceneId) {
        this.sceneId = sceneId;
    }
}
