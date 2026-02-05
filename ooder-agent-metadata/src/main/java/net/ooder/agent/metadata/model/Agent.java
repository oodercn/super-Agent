package net.ooder.agent.metadata.model;

import net.ooder.agent.metadata.enums.AgentStatusEnum;
import net.ooder.agent.metadata.enums.AgentTypeEnum;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 统一的Agent模型
 * 支持endAgent、routeAgent、mapAgent等
 */
public class Agent implements Serializable {
    private static final long serialVersionUID = 1L;

    private String agentId;
    private String name;
    private AgentTypeEnum type;
    private AgentStatusEnum status;
    private String endpoint;
    private String network;
    private String description;
    private List<Capability> capabilities;
    private Map<String, String> metadata;
    private long joinedAt;
    private long lastActiveAt;

    public Agent() {
        this.status = AgentStatusEnum.PENDING;
        this.capabilities = new CopyOnWriteArrayList<>();
        this.metadata = new HashMap<>();
        this.joinedAt = System.currentTimeMillis();
        this.lastActiveAt = System.currentTimeMillis();
    }

    public Agent(String agentId, String name, AgentTypeEnum type) {
        this();
        this.agentId = agentId;
        this.name = name;
        this.type = type;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.lastActiveAt = System.currentTimeMillis();
    }

    public AgentTypeEnum getType() {
        return type;
    }

    public void setType(AgentTypeEnum type) {
        this.type = type;
        this.lastActiveAt = System.currentTimeMillis();
    }

    public AgentStatusEnum getStatus() {
        return status;
    }

    public void setStatus(AgentStatusEnum status) {
        this.status = status;
        this.lastActiveAt = System.currentTimeMillis();
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
        this.lastActiveAt = System.currentTimeMillis();
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
        this.lastActiveAt = System.currentTimeMillis();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        this.lastActiveAt = System.currentTimeMillis();
    }

    public List<Capability> getCapabilities() {
        return new ArrayList<>(capabilities);
    }

    public void setCapabilities(List<Capability> capabilities) {
        this.capabilities = new CopyOnWriteArrayList<>(capabilities);
        this.lastActiveAt = System.currentTimeMillis();
    }

    public void addCapability(Capability capability) {
        this.capabilities.add(capability);
        this.lastActiveAt = System.currentTimeMillis();
    }

    public void removeCapability(String capabilityId) {
        this.capabilities.removeIf(capability -> capability.getCapabilityId().equals(capabilityId));
        this.lastActiveAt = System.currentTimeMillis();
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

    public Map<String, String> getMetadata() {
        return new HashMap<>(metadata);
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = new HashMap<>(metadata);
        this.lastActiveAt = System.currentTimeMillis();
    }

    public void addMetadata(String key, String value) {
        this.metadata.put(key, value);
        this.lastActiveAt = System.currentTimeMillis();
    }

    public long getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(long joinedAt) {
        this.joinedAt = joinedAt;
    }

    public long getLastActiveAt() {
        return lastActiveAt;
    }

    public void setLastActiveAt(long lastActiveAt) {
        this.lastActiveAt = lastActiveAt;
    }

    @Override
    public String toString() {
        return "Agent{" +
                "agentId='" + agentId + '\'' +
                ", name='" + name + '\'' +
                ", type=" + type.getCode() +
                ", status=" + status.getCode() +
                ", endpoint='" + endpoint + '\'' +
                ", network='" + network + '\'' +
                ", capabilitiesCount=" + capabilities.size() +
                '}';
    }
}