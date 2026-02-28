package net.ooder.nexus.dto.scene;

import java.io.Serializable;

public class SceneInstanceQueryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String instanceId;

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }
}
