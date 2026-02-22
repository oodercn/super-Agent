package net.ooder.nexus.skillcenter.dto.task;

import java.util.List;
import java.util.Map;

public class TaskAssignmentDTO {
    
    private String taskId;
    private String groupId;
    private String assignedTo;
    private String assignedRole;
    private String assignmentType;
    private Map<String, Object> constraints;
    private List<String> requiredCapabilities;

    public String getTaskId() { return taskId; }
    public void setTaskId(String taskId) { this.taskId = taskId; }

    public String getGroupId() { return groupId; }
    public void setGroupId(String groupId) { this.groupId = groupId; }

    public String getAssignedTo() { return assignedTo; }
    public void setAssignedTo(String assignedTo) { this.assignedTo = assignedTo; }

    public String getAssignedRole() { return assignedRole; }
    public void setAssignedRole(String assignedRole) { this.assignedRole = assignedRole; }

    public String getAssignmentType() { return assignmentType; }
    public void setAssignmentType(String assignmentType) { this.assignmentType = assignmentType; }

    public Map<String, Object> getConstraints() { return constraints; }
    public void setConstraints(Map<String, Object> constraints) { this.constraints = constraints; }

    public List<String> getRequiredCapabilities() { return requiredCapabilities; }
    public void setRequiredCapabilities(List<String> requiredCapabilities) { this.requiredCapabilities = requiredCapabilities; }
}
