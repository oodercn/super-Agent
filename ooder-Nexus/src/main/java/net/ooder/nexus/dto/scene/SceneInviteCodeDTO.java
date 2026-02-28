package net.ooder.nexus.dto.scene;

import java.io.Serializable;

public class SceneInviteCodeDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String instanceId;
    private String inviteCode;

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }
}
