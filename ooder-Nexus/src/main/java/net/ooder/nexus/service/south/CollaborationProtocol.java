package net.ooder.nexus.service.south;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 协作协议接口
 *
 * <p>SDK 0.7.2 南向协议，提供场景组协作能力。</p>
 *
 * @author ooder Team
 * @version 2.0
 * @since SDK 0.7.2
 */
public interface CollaborationProtocol {

    CompletableFuture<SceneGroupInfo> joinSceneGroup(String groupId, JoinRequest request);

    CompletableFuture<Void> leaveSceneGroup(String groupId);

    CompletableFuture<TaskInfo> receiveTask(String groupId);

    CompletableFuture<Void> submitTaskResult(String groupId, String taskId, TaskResult result);

    CompletableFuture<List<MemberInfo>> getGroupMembers(String groupId);

    void addCollaborationListener(CollaborationListener listener);

    void removeCollaborationListener(CollaborationListener listener);

    class SceneGroupInfo {
        private String groupId;
        private String groupName;
        private String sceneId;
        private String primaryId;
        private List<String> memberIds;
        private int memberCount;
        private String status;
        private long createdAt;

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
    }

    class JoinRequest {
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

    class TaskInfo {
        private String taskId;
        private String groupId;
        private String taskType;
        private String taskName;
        private Map<String, Object> parameters;
        private int priority;
        private long createdAt;

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
    }

    class TaskResult {
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

    class MemberInfo {
        private String memberId;
        private String memberName;
        private String role;
        private String status;
        private long joinedAt;

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
    }

    interface CollaborationListener {
        void onGroupJoined(SceneGroupInfo group);
        void onGroupLeft(String groupId);
        void onTaskAssigned(TaskInfo task);
        void onMemberJoined(String groupId, MemberInfo member);
        void onMemberLeft(String groupId, String memberId);
    }
}
