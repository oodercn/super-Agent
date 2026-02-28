package net.ooder.nexus.dto.scene;

import java.io.Serializable;

public class SceneJoinDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String inviteCode;
    private String memberId;

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }
}
