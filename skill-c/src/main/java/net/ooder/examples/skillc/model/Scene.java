/**
 * 作者：ooderAI agent team
 * 版本：V0.6.0
 * 日期：2026-01-18
 */
package net.ooder.examples.skillc.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class Scene {
    private String name;
    private String description;
    private String sceneId;
    private String type;
    private List<String> supportedCapabilities;
    private Map<String, Object> rules;
    private Map<String, Object> targets;
    private Map<String, Object> constraints;
    private List<Group> groups;
    private List<Agent> participants;
    private long createdAt;
    private long updatedAt;

    public Scene(String name, String description, String sceneId) {
        this.name = name;
        this.description = description;
        this.sceneId = sceneId;
        this.type = "general";
        this.supportedCapabilities = new CopyOnWriteArrayList<>();
        this.rules = new HashMap<>();
        this.targets = new HashMap<>();
        this.constraints = new HashMap<>();
        this.groups = new CopyOnWriteArrayList<>();
        this.participants = new CopyOnWriteArrayList<>();
        this.createdAt = System.currentTimeMillis();
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

    public String getSceneId() {
        return sceneId;
    }

    public List<Agent> getParticipants() {
        return new ArrayList<>(participants);
    }

    public void addParticipant(Agent agent) {
        participants.add(agent);
        this.updatedAt = System.currentTimeMillis();
    }

    public void removeParticipant(String agentId) {
        participants.removeIf(agent -> agent.getAgentId().equals(agentId));
        this.updatedAt = System.currentTimeMillis();
    }

    public boolean containsParticipant(String agentId) {
        return participants.stream().anyMatch(agent -> agent.getAgentId().equals(agentId));
    }

    public void updateParticipant(Agent agent) {
        participants.removeIf(existingAgent -> existingAgent.getAgentId().equals(agent.getAgentId()));
        participants.add(agent);
        this.updatedAt = System.currentTimeMillis();
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
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
        this.rules = rules;
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
        this.targets = targets;
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
        this.constraints = constraints;
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
}