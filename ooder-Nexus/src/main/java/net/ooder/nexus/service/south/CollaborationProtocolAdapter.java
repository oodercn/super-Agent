package net.ooder.nexus.service.south;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 协作协议适配器接口
 *
 * <p>提供场景组协作能力，支持任务分配、状态同步。</p>
 *
 * @author ooder Team
 * @version 2.0
 * @since SDK 0.7.2
 */
public interface CollaborationProtocolAdapter {

    /**
     * 加入场景组
     *
     * @param groupId 场景组 ID
     * @param request 加入请求
     * @return 场景组信息
     */
    CompletableFuture<SceneGroupInfoDTO> joinSceneGroup(String groupId, JoinRequestDTO request);

    /**
     * 离开场景组
     *
     * @param groupId 场景组 ID
     * @return 离开结果
     */
    CompletableFuture<Void> leaveSceneGroup(String groupId);

    /**
     * 接受邀请
     *
     * @param invitationId 邀请 ID
     * @return 接受结果
     */
    CompletableFuture<Void> acceptInvitation(String invitationId);

    /**
     * 拒绝邀请
     *
     * @param invitationId 邀请 ID
     * @return 拒绝结果
     */
    CompletableFuture<Void> declineInvitation(String invitationId);

    /**
     * 获取待处理邀请
     *
     * @return 邀请列表
     */
    CompletableFuture<List<InvitationDTO>> getPendingInvitations();

    /**
     * 接收任务
     *
     * @param groupId 场景组 ID
     * @return 任务信息
     */
    CompletableFuture<TaskDTO> receiveTask(String groupId);

    /**
     * 提交任务结果
     *
     * @param groupId 场景组 ID
     * @param taskId 任务 ID
     * @param result 任务结果
     * @return 提交结果
     */
    CompletableFuture<Void> submitTaskResult(String groupId, String taskId, TaskResultDTO result);

    /**
     * 获取待处理任务
     *
     * @param groupId 场景组 ID
     * @return 任务列表
     */
    CompletableFuture<List<TaskDTO>> getPendingTasks(String groupId);

    /**
     * 同步状态
     *
     * @param groupId 场景组 ID
     * @param state 状态数据
     * @return 同步结果
     */
    CompletableFuture<Void> syncState(String groupId, Map<String, Object> state);

    /**
     * 获取场景组状态
     *
     * @param groupId 场景组 ID
     * @return 状态数据
     */
    CompletableFuture<Map<String, Object>> getGroupState(String groupId);

    /**
     * 获取场景组成员
     *
     * @param groupId 场景组 ID
     * @return 成员列表
     */
    CompletableFuture<List<MemberDTO>> getGroupMembers(String groupId);

    /**
     * 添加协作事件监听器
     *
     * @param listener 监听器
     */
    void addCollaborationListener(CollaborationEventListener listener);

    /**
     * 移除协作事件监听器
     *
     * @param listener 监听器
     */
    void removeCollaborationListener(CollaborationEventListener listener);

    /**
     * 场景组信息 DTO
     */
    class SceneGroupInfoDTO {
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

    /**
     * 加入请求 DTO
     */
    class JoinRequestDTO {
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

    /**
     * 邀请 DTO
     */
    class InvitationDTO {
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

    /**
     * 任务 DTO
     */
    class TaskDTO {
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

    /**
     * 任务结果 DTO
     */
    class TaskResultDTO {
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

    /**
     * 成员 DTO
     */
    class MemberDTO {
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

    /**
     * 协作事件监听器
     */
    interface CollaborationEventListener {
        void onInvitationReceived(InvitationDTO invitation);
        void onGroupJoined(SceneGroupInfoDTO group);
        void onGroupLeft(String groupId);
        void onTaskAssigned(TaskDTO task);
        void onTaskCompleted(String taskId, TaskResultDTO result);
        void onMemberJoined(String groupId, MemberDTO member);
        void onMemberLeft(String groupId, String memberId);
        void onStateChanged(String groupId, Map<String, Object> state);
    }
}
