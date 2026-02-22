package net.ooder.nexus.domain.scene.model;

import java.util.Map;

public class CapabilityRef {
    private String capId;
    private String name;
    private boolean required;
    private int priority;
    private Map<String, Object> constraints;

    public CapabilityRef() {}

    public String getCapId() {
        return capId;
    }

    public void setCapId(String capId) {
        this.capId = capId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Map<String, Object> getConstraints() {
        return constraints;
    }

    public void setConstraints(Map<String, Object> constraints) {
        this.constraints = constraints;
    }
}
