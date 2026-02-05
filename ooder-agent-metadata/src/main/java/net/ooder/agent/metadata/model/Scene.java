package net.ooder.agent.metadata.model;

import net.ooder.agent.metadata.enums.SceneTypeEnum;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 场景模型
 * 用于场景组管理
 */
public class Scene implements Serializable {
    private static final long serialVersionUID = 1L;

    private String sceneId;
    private String name;
    private String description;
    private SceneTypeEnum type;
    private List<String> supportedCapabilities;
    private Map<String, Object> rules;
    private Map<String, Object> targets;
    private Map<String, Object> constraints;
    private List<Group> groups;
    private List<Agent> participants;
    private Map<String, String> metadata;
    private long createdAt;
    private long updatedAt;

    public Scene() {
        this.type = SceneTypeEnum.GENERAL;
        this.supportedCapabilities = new CopyOnWriteArrayList<>();
        this.rules = new HashMap<>();
        this.targets = new HashMap<>();
        this.constraints = new HashMap<>();
        this.groups = new CopyOnWriteArrayList<>();
        this.participants = new CopyOnWriteArrayList<>();
        this.metadata = new HashMap<>();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }

    public Scene(String sceneId, String name, String description) {
        this();
        this.sceneId = sceneId;
        this.name = name;
        this.description = description;
    }

    public String getSceneId() {
        return sceneId;
    }

    public void setSceneId(String sceneId) {
        this.sceneId = sceneId;
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

    public SceneTypeEnum getType() {
        return type;
    }

    public void setType(SceneTypeEnum type) {
        this.type = type;
        this.updatedAt = System.currentTimeMillis();
    }

    public List<String> getSupportedCapabilities() {
        return new ArrayList<>(supportedCapabilities);
    }

    public void addSupportedCapability(String capabilityId) {
        this.supportedCapabilities.add(capabilityId);
        this.updatedAt = System.currentTimeMillis();
    }

    public void removeSupportedCapability(String capabilityId) {
        this.supportedCapabilities.remove(capabilityId);
        this.updatedAt = System.currentTimeMillis();
    }

    public Map<String, Object> getRules() {
        return new HashMap<>(rules);
    }

    public void setRules(Map<String, Object> rules) {
        this.rules = new HashMap<>(rules);
        this.updatedAt = System.currentTimeMillis();
    }

    public void addRule(String key, Object value) {
        this.rules.put(key, value);
        this.updatedAt = System.currentTimeMillis();
    }

    public Map<String, Object> getTargets() {
        return new HashMap<>(targets);
    }

    public void setTargets(Map<String, Object> targets) {
        this.targets = new HashMap<>(targets);
        this.updatedAt = System.currentTimeMillis();
    }

    public void addTarget(String key, Object value) {
        this.targets.put(key, value);
        this.updatedAt = System.currentTimeMillis();
    }

    public Map<String, Object> getConstraints() {
        return new HashMap<>(constraints);
    }

    public void setConstraints(Map<String, Object> constraints) {
        this.constraints = new HashMap<>(constraints);
        this.updatedAt = System.currentTimeMillis();
    }

    public void addConstraint(String key, Object value) {
        this.constraints.put(key, value);
        this.updatedAt = System.currentTimeMillis();
    }

    public List<Group> getGroups() {
        return new ArrayList<>(groups);
    }

    public void addGroup(Group group) {
        this.groups.add(group);
        this.updatedAt = System.currentTimeMillis();
    }

    public void removeGroup(String groupId) {
        this.groups.removeIf(group -> group.getGroupId().equals(groupId));
        this.updatedAt = System.currentTimeMillis();
    }

    public Group getGroup(String groupId) {
        return groups.stream()
                .filter(group -> group.getGroupId().equals(groupId))
                .findFirst()
                .orElse(null);
    }

    public List<Agent> getParticipants() {
        return new ArrayList<>(participants);
    }

    public void addParticipant(Agent agent) {
        this.participants.add(agent);
        this.updatedAt = System.currentTimeMillis();
    }

    public void removeParticipant(String agentId) {
        this.participants.removeIf(agent -> agent.getAgentId().equals(agentId));
        this.updatedAt = System.currentTimeMillis();
    }

    public boolean containsParticipant(String agentId) {
        return participants.stream().anyMatch(agent -> agent.getAgentId().equals(agentId));
    }

    public Agent getParticipant(String agentId) {
        return participants.stream()
                .filter(agent -> agent.getAgentId().equals(agentId))
                .findFirst()
                .orElse(null);
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
        return "Scene{" +
                "sceneId='" + sceneId + '\'' +
                ", name='" + name + '\'' +
                ", type=" + type.getCode() +
                ", groupsCount=" + groups.size() +
                ", participantsCount=" + participants.size() +
                '}';
    }
}