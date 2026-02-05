package net.ooder.agent.metadata.model;

import net.ooder.agent.metadata.enums.GroupTypeEnum;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 组模型
 * 用于场景内的组管理
 */
public class Group implements Serializable {
    private static final long serialVersionUID = 1L;

    private String groupId;
    private String name;
    private String description;
    private String sceneId;
    private GroupTypeEnum type;
    private List<Agent> members;
    private Map<String, String> metadata;
    private long createdAt;
    private long updatedAt;

    public Group() {
        this.type = GroupTypeEnum.GENERAL;
        this.members = new CopyOnWriteArrayList<>();
        this.metadata = new HashMap<>();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }

    public Group(String groupId, String name, String description, String sceneId) {
        this();
        this.groupId = groupId;
        this.name = name;
        this.description = description;
        this.sceneId = sceneId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
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

    public void setSceneId(String sceneId) {
        this.sceneId = sceneId;
        this.updatedAt = System.currentTimeMillis();
    }

    public GroupTypeEnum getType() {
        return type;
    }

    public void setType(GroupTypeEnum type) {
        this.type = type;
        this.updatedAt = System.currentTimeMillis();
    }

    public List<Agent> getMembers() {
        return new ArrayList<>(members);
    }

    public void addMember(Agent agent) {
        this.members.add(agent);
        this.updatedAt = System.currentTimeMillis();
    }

    public void removeMember(String agentId) {
        this.members.removeIf(agent -> agent.getAgentId().equals(agentId));
        this.updatedAt = System.currentTimeMillis();
    }

    public boolean containsMember(String agentId) {
        return this.members.stream().anyMatch(agent -> agent.getAgentId().equals(agentId));
    }

    public Agent getMember(String agentId) {
        return members.stream()
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
        return "Group{" +
                "groupId='" + groupId + '\'' +
                ", name='" + name + '\'' +
                ", sceneId='" + sceneId + '\'' +
                ", type=" + type.getCode() +
                ", membersCount=" + members.size() +
                '}';
    }
}