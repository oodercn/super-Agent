package net.ooder.mvp.skill.scene.capability.model;

import java.io.Serializable;

public class CollaborativeCapabilityRef implements Serializable {
    private static final long serialVersionUID = 1L;

    private String capabilityId;
    private String role;
    private String interfaceRef;
    private boolean autoStart;

    public CollaborativeCapabilityRef() {
        this.autoStart = false;
    }

    public String getCapabilityId() {
        return capabilityId;
    }

    public void setCapabilityId(String capabilityId) {
        this.capabilityId = capabilityId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getInterfaceRef() {
        return interfaceRef;
    }

    public void setInterfaceRef(String interfaceRef) {
        this.interfaceRef = interfaceRef;
    }

    public boolean isAutoStart() {
        return autoStart;
    }

    public void setAutoStart(boolean autoStart) {
        this.autoStart = autoStart;
    }
}
