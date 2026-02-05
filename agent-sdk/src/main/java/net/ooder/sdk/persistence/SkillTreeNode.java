package net.ooder.sdk.persistence;

import net.ooder.sdk.skill.SkillStatus;

import java.util.List;
import java.util.Map;

/**
 * 技能树节点，表示技能在树结构中的位置
 */
public class SkillTreeNode {
    private String skillId;
    private String skillName;
    private String description;
    private String routeAgentId;
    private SkillStatus status;
    private Map<String, Object> parameters;
    private List<SkillTreeNode> children;
    private List<String> dependencies;
    private long lastUpdated;
    private String version;
    
    // 构造函数
    public SkillTreeNode() {
    }
    
    public SkillTreeNode(String skillId, String skillName, String routeAgentId) {
        this.skillId = skillId;
        this.skillName = skillName;
        this.routeAgentId = routeAgentId;
        this.lastUpdated = System.currentTimeMillis();
    }
    
    // Getter和Setter方法
    public String getSkillId() {
        return skillId;
    }
    
    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }
    
    public String getSkillName() {
        return skillName;
    }
    
    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getRouteAgentId() {
        return routeAgentId;
    }
    
    public void setRouteAgentId(String routeAgentId) {
        this.routeAgentId = routeAgentId;
    }
    
    public SkillStatus getStatus() {
        return status;
    }
    
    public void setStatus(SkillStatus status) {
        this.status = status;
    }
    
    public Map<String, Object> getParameters() {
        return parameters;
    }
    
    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }
    
    public List<SkillTreeNode> getChildren() {
        return children;
    }
    
    public void setChildren(List<SkillTreeNode> children) {
        this.children = children;
    }
    
    public List<String> getDependencies() {
        return dependencies;
    }
    
    public void setDependencies(List<String> dependencies) {
        this.dependencies = dependencies;
    }
    
    public long getLastUpdated() {
        return lastUpdated;
    }
    
    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
    
    public String getVersion() {
        return version;
    }
    
    public void setVersion(String version) {
        this.version = version;
    }
    
    // 辅助方法
    public void addChild(SkillTreeNode child) {
        if (children == null) {
            children = new java.util.ArrayList<>();
        }
        children.add(child);
    }
    
    public void addDependency(String dependencySkillId) {
        if (dependencies == null) {
            dependencies = new java.util.ArrayList<>();
        }
        dependencies.add(dependencySkillId);
    }
    
    public void updateTimestamp() {
        this.lastUpdated = System.currentTimeMillis();
    }
}
