package net.ooder.nexus.dto.scene;

import java.io.Serializable;

public class SceneDeleteDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String sceneId;
    private Boolean confirm;

    public String getSceneId() {
        return sceneId;
    }

    public void setSceneId(String sceneId) {
        this.sceneId = sceneId;
    }

    public Boolean getConfirm() {
        return confirm;
    }

    public void setConfirm(Boolean confirm) {
        this.confirm = confirm;
    }
}
