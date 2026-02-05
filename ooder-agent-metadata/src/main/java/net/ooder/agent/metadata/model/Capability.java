package net.ooder.agent.metadata.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Agent能力模型
 */
public class Capability implements Serializable {
    private static final long serialVersionUID = 1L;

    private String capabilityId;
    private String name;
    private String type;
    private String description;
    private Map<String, Object> parameters;
    private Map<String, String> metadata;

    public Capability() {
        this.parameters = new HashMap<>();
        this.metadata = new HashMap<>();
    }

    public Capability(String capabilityId, String name, String type) {
        this();
        this.capabilityId = capabilityId;
        this.name = name;
        this.type = type;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, Object> getParameters() {
        return new HashMap<>(parameters);
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = new HashMap<>(parameters);
    }

    public void addParameter(String key, Object value) {
        this.parameters.put(key, value);
    }

    public Map<String, String> getMetadata() {
        return new HashMap<>(metadata);
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = new HashMap<>(metadata);
    }

    public void addMetadata(String key, String value) {
        this.metadata.put(key, value);
    }

    @Override
    public String toString() {
        return "Capability{" +
                "capabilityId='" + capabilityId + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}