package net.ooder.nexus.skillcenter.dto.protocol;

import java.util.List;
import java.util.Map;

public class CollaborationDTO {

    public static class SceneGroupInfoDTO {
        private String groupId;
        private String groupName;
        private String sceneId;
        private String primaryId;
        private List<String> memberIds;
        private int memberCount;
        private String status;
        private long createdAt;
        private Map<String, Object> config;

        public String getGroupId() { return groupId; }
        public void setGroupId(String groupId) { this.groupId = groupId; }
        public String getGroupName() { return groupName; }
        public void setGroupName(String groupName) { this.groupName = groupName; }
        public String getSceneId() { return sceneId; }
        public void setSceneId(String sceneId) { this.sceneId = sceneId; }
        public String getPrimaryId() { return primaryId; }
        public void setPrimaryId(String primaryId) { this.primaryId = primaryId; }
        public List<String> getMemberIds() { return memberIds; }
        public void setMemberIds(List<String> memberIds) { this.memberIds = memberIds; }
        public int getMemberCount() { return memberCount; }
        public void setMemberCount(int memberCount) { this.memberCount = memberCount; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public long getCreatedAt() { return createdAt; }
        public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
        public Map<String, Object> getConfig() { return config; }
        public void setConfig(Map<String, Object> config) { this.config = config; }
    }

    public static class JoinRequestDTO {
        private String agentId;
        private String inviteCode;
        private Map<String, Object> capabilities;

        public String getAgentId() { return agentId; }
        public void setAgentId(String agentId) { this.agentId = agentId; }
        public String getInviteCode() { return inviteCode; }
        public void setInviteCode(String inviteCode) { this.inviteCode = inviteCode; }
        public Map<String, Object> getCapabilities() { return capabilities; }
        public void setCapabilities(Map<String, Object> capabilities) { this.capabilities = capabilities; }
    }

    public static class InvitationDTO {
        private String invitationId;
        private String groupId;
        private String groupName;
        private String inviterId;
        private String inviterName;
        private long createdAt;
        private long expiresAt;
        private String status;

        public String getInvitationId() { return invitationId; }
        public void setInvitationId(String invitationId) { this.invitationId = invitationId; }
        public String getGroupId() { return groupId; }
        public void setGroupId(String groupId) { this.groupId = groupId; }
        public String getGroupName() { return groupName; }
        public void setGroupName(String groupName) { this.groupName = groupName; }
        public String getInviterId() { return inviterId; }
        public void setInviterId(String inviterId) { this.inviterId = inviterId; }
        public String getInviterName() { return inviterName; }
        public void setInviterName(String inviterName) { this.inviterName = inviterName; }
        public long getCreatedAt() { return createdAt; }
        public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
        public long getExpiresAt() { return expiresAt; }
        public void setExpiresAt(long expiresAt) { this.expiresAt = expiresAt; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

    public static class TaskDTO {
        private String taskId;
        private String groupId;
        private String taskType;
        private String taskName;
        private Map<String, Object> parameters;
        private int priority;
        private long createdAt;
        private long deadline;
        private String status;

        public String getTaskId() { return taskId; }
        public void setTaskId(String taskId) { this.taskId = taskId; }
        public String getGroupId() { return groupId; }
        public void setGroupId(String groupId) { this.groupId = groupId; }
        public String getTaskType() { return taskType; }
        public void setTaskType(String taskType) { this.taskType = taskType; }
        public String getTaskName() { return taskName; }
        public void setTaskName(String taskName) { this.taskName = taskName; }
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
    }

    public static class TaskResultDTO {
        private String taskId;
        private boolean success;
        private Map<String, Object> output;
        private String errorMessage;
        private long completedAt;

        public String getTaskId() { return taskId; }
        public void setTaskId(String taskId) { this.taskId = taskId; }
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public Map<String, Object> getOutput() { return output; }
        public void setOutput(Map<String, Object> output) { this.output = output; }
        public String getErrorMessage() { return errorMessage; }
        public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
        public long getCompletedAt() { return completedAt; }
        public void setCompletedAt(long completedAt) { this.completedAt = completedAt; }
    }

    public static class MemberDTO {
        private String memberId;
        private String memberName;
        private String role;
        private String status;
        private long joinedAt;
        private long lastActiveAt;

        public String getMemberId() { return memberId; }
        public void setMemberId(String memberId) { this.memberId = memberId; }
        public String getMemberName() { return memberName; }
        public void setMemberName(String memberName) { this.memberName = memberName; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public long getJoinedAt() { return joinedAt; }
        public void setJoinedAt(long joinedAt) { this.joinedAt = joinedAt; }
        public long getLastActiveAt() { return lastActiveAt; }
        public void setLastActiveAt(long lastActiveAt) { this.lastActiveAt = lastActiveAt; }
    }
}
