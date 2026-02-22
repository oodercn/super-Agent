package net.ooder.nexus.skillcenter.dto.task;

import java.util.List;
import java.util.Map;

public class TaskDTO {
    
    private String taskId;
    private String groupId;
    private String sceneId;
    private String taskType;
    private String taskName;
    private String description;
    private Map<String, Object> parameters;
    private int priority;
    private long createdAt;
    private long deadline;
    private String status;
    private String assignedTo;
    private String assignedRole;
    private String createdBy;
    private List<String> requiredCapabilities;
    private Map<String, Object> metadata;

    public String getTaskId() { return taskId; }
    public void setTaskId(String taskId) { this.taskId = taskId; }

    public String getGroupId() { return groupId; }
    public void setGroupId(String groupId) { this.groupId = groupId; }

    public String getSceneId() { return sceneId; }
    public void setSceneId(String sceneId) { this.sceneId = sceneId; }

    public String getTaskType() { return taskType; }
    public void setTaskType(String taskType) { this.taskType = taskType; }

    public String getTaskName() { return taskName; }
    public void setTaskName(String taskName) { this.taskName = taskName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Map<String, Object> getParameters() { return parameters; }
    public void setParameters(Map<String, Object> parameters) { this.parameters = parameters; }

    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

    public long getDeadline() { return deadline; }
    public void setDeadline(long deadline) { this.deadline = deadline; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getAssignedTo() { return assignedTo; }
    public void setAssignedTo(String assignedTo) { this.assignedTo = assignedTo; }

    public String getAssignedRole() { return assignedRole; }
    public void setAssignedRole(String assignedRole) { this.assignedRole = assignedRole; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public List<String> getRequiredCapabilities() { return requiredCapabilities; }
    public void setRequiredCapabilities(List<String> requiredCapabilities) { this.requiredCapabilities = requiredCapabilities; }

    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
}
