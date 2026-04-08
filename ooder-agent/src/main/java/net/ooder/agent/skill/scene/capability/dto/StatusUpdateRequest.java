package net.ooder.mvp.skill.scene.capability.dto;

import java.io.Serializable;

public class StatusUpdateRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
