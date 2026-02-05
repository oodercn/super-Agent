package net.ooder.agent.metadata.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Skill模型
 * 提供特定功能的服务单元
 */
public class Skill implements Serializable {
    private static final long serialVersionUID = 1L;

    private String skillId;
    private String name;
    private String description;
    private String version;
    private String type;
    private List<Endpoint> endpoints;
    private List<Capability> capabilities;
    private Map<String, String> metadata;
    private long createdAt;
    private long updatedAt;

    public Skill() {
        this.endpoints = new ArrayList<>();
        this.capabilities = new ArrayList<>();
        this.metadata = new HashMap<>();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }

    public Skill(String skillId, String name, String type) {
        this();
        this.skillId = skillId;
        this.name = name;
        this.type = type;
    }

    public String getSkillId() {
        return skillId;
    }

    public void setSkillId(String skillId) {
        this.skillId = skillId;
        this.updatedAt = System.currentTimeMillis();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.updatedAt = System.currentTimeMillis();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        this.updatedAt = System.currentTimeMillis();
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
        this.updatedAt = System.currentTimeMillis();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
        this.updatedAt = System.currentTimeMillis();
    }

    public List<Endpoint> getEndpoints() {
        return new ArrayList<>(endpoints);
    }

    public void setEndpoints(List<Endpoint> endpoints) {
        this.endpoints = new ArrayList<>(endpoints);
        this.updatedAt = System.currentTimeMillis();
    }

    public void addEndpoint(Endpoint endpoint) {
        this.endpoints.add(endpoint);
        this.updatedAt = System.currentTimeMillis();
    }

    public List<Capability> getCapabilities() {
        return new ArrayList<>(capabilities);
    }

    public void setCapabilities(List<Capability> capabilities) {
        this.capabilities = new ArrayList<>(capabilities);
        this.updatedAt = System.currentTimeMillis();
    }

    public void addCapability(Capability capability) {
        this.capabilities.add(capability);
        this.updatedAt = System.currentTimeMillis();
    }

    public Map<String, String> getMetadata() {
        return new HashMap<>(metadata);
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = new HashMap<>(metadata);
        this.updatedAt = System.currentTimeMillis();
    }

    public void addMetadata(String key, String value) {
        this.metadata.put(key, value);
        this.updatedAt = System.currentTimeMillis();
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public String toString() {
        return "Skill{" +
                "skillId='" + skillId + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", endpointsCount=" + endpoints.size() +
                ", capabilitiesCount=" + capabilities.size() +
                '}';
    }
}
