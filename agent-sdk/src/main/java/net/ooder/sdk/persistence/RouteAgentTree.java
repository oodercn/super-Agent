package net.ooder.sdk.persistence;

import java.util.List;

/**
 * 路由代理树，表示路由代理层次结构
 */
public class RouteAgentTree {
    private String agentId;
    private String agentName;
    private String agentType;
    private String parentAgentId;
    private List<RouteAgentTree> childAgents;
    private List<SkillTreeNode> skills;
    private long lastUpdated;
    private String status;
    
    // 构造函数
    public RouteAgentTree() {
    }
    
    public RouteAgentTree(String agentId, String agentName, String agentType) {
        this.agentId = agentId;
        this.agentName = agentName;
        this.agentType = agentType;
        this.lastUpdated = System.currentTimeMillis();
        this.status = "active";
    }
    
    // Getter和Setter方法
    public String getAgentId() {
        return agentId;
    }
    
    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }
    
    public String getAgentName() {
        return agentName;
    }
    
    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }
    
    public String getAgentType() {
        return agentType;
    }
    
    public void setAgentType(String agentType) {
        this.agentType = agentType;
    }
    
    public String getParentAgentId() {
        return parentAgentId;
    }
    
    public void setParentAgentId(String parentAgentId) {
        this.parentAgentId = parentAgentId;
    }
    
    public List<RouteAgentTree> getChildAgents() {
        return childAgents;
    }
    
    public void setChildAgents(List<RouteAgentTree> childAgents) {
        this.childAgents = childAgents;
    }
    
    public List<SkillTreeNode> getSkills() {
        return skills;
    }
    
    public void setSkills(List<SkillTreeNode> skills) {
        this.skills = skills;
    }
    
    public long getLastUpdated() {
        return lastUpdated;
    }
    
    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    // 辅助方法
    public void addChildAgent(RouteAgentTree childAgent) {
        if (childAgents == null) {
            childAgents = new java.util.ArrayList<>();
        }
        childAgent.setParentAgentId(this.agentId);
        childAgents.add(childAgent);
    }
    
    public void addSkill(SkillTreeNode skill) {
        if (skills == null) {
            skills = new java.util.ArrayList<>();
        }
        skills.add(skill);
    }
    
    public void updateTimestamp() {
        this.lastUpdated = System.currentTimeMillis();
    }
}
