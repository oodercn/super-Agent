/**
 * 作者：ooderAI agent team
 * 版本：V0.6.0
 * 日期：2026-01-18
 */
package net.ooder.examples.skillc.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Group {
    private String groupId;
    private String name;
    private String description;
    private String sceneId;
    private String type;
    private List<Agent> members;
    private long createdAt;
    private long updatedAt;

    public Group(String name, String description, String groupId, String sceneId) {
        this.name = name;
        this.description = description;
        this.groupId = groupId;
        this.sceneId = sceneId;
        this.type = "general";
        this.members = new CopyOnWriteArrayList<>();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }

    public String getGroupId() {
        return groupId;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
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

    public long getCreatedAt() {
        return createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }
}