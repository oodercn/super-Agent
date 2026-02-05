package net.ooder.nexus.model.mcp;

import java.io.Serializable;

public class Capability implements Serializable {
    private static final long serialVersionUID = 1L;

    private String capabilityId;
    private String name;
    private String description;
    private String version;
    private boolean enabled;
    private long registeredAt;

    public Capability() {
    }

    public Capability(String capabilityId, String name, String description) {
        this.capabilityId = capabilityId;
        this.name = name;
        this.description = description;
        this.version = "1.0.0";
        this.enabled = true;
        this.registeredAt = System.currentTimeMillis();
    }

    public String getCapabilityId() {
        return capabilityId;
    }

    public void setCapabilityId(String capabilityId) {
        this.capabilityId = capabilityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public long getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(long registeredAt) {
        this.registeredAt = registeredAt;
    }
}
