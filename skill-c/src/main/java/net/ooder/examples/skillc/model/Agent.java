package net.ooder.examples.skillc.model;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Agent {
    private String agentId;
    private String name;
    private String type;
    private String endpoint;
    private String status;
    private List<Capability> capabilities;
    private long joinedAt;
    private Map<String, String> metadata;

    public Agent(String agentId, String name, String type, String endpoint) {
        this.agentId = agentId;
        this.name = name;
        this.type = type;
        this.endpoint = endpoint;
        this.status = "JOINED";
        this.capabilities = new CopyOnWriteArrayList<>();
        this.joinedAt = System.currentTimeMillis();
        this.metadata = new HashMap<>();
    }

    public String getAgentId() {
        return agentId;
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

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Capability> getCapabilities() {
        return new ArrayList<>(capabilities);
    }

    public void setCapabilities(List<Capability> capabilities) {
        this.capabilities = new CopyOnWriteArrayList<>(capabilities);
    }

    public void addCapability(Capability capability) {
        capabilities.add(capability);
    }

    public void removeCapability(String capabilityId) {
        capabilities.removeIf(capability -> capability.getCapabilityId().equals(capabilityId));
    }

    public Capability getCapability(String capabilityId) {
        return capabilities.stream()
            .filter(capability -> capability.getCapabilityId().equals(capabilityId))
            .findFirst()
            .orElse(null);
    }

    public boolean hasCapability(String capabilityId) {
        return capabilities.stream()
            .anyMatch(capability -> capability.getCapabilityId().equals(capabilityId));
    }

    public long getJoinedAt() {
        return joinedAt;
    }

    public Map<String, String> getMetadata() {
        return new HashMap<>(metadata);
    }

    public void addMetadata(String key, String value) {
        metadata.put(key, value);
    }
}