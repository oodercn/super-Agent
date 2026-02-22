package net.ooder.nexus.service.scene;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 场景组服务接口
 *
 * <p>提供场景组管理能力，支持创建、加入、心跳监控。</p>
 *
 * @author ooder Team
 * @version 2.0
 * @since SDK 0.7.2
 */
public interface SceneGroupService {

    /**
     * 创建场景定义
     *
     * @param definition 场景定义
     * @return 创建结果
     */
    CompletableFuture<SceneDefinitionDTO> createSceneDefinition(SceneDefinitionDTO definition);

    /**
     * 获取场景定义
     *
     * @param sceneId 场景 ID
     * @return 场景定义
     */
    CompletableFuture<SceneDefinitionDTO> getSceneDefinition(String sceneId);

    /**
     * 列出场景定义
     *
     * @return 场景定义列表
     */
    CompletableFuture<List<SceneDefinitionDTO>> listSceneDefinitions();

    /**
     * 创建场景组
     *
     * @param sceneId 场景 ID
     * @param config 场景组配置
     * @return 场景组信息
     */
    CompletableFuture<SceneGroupDTO> createSceneGroup(String sceneId, SceneGroupConfigDTO config);

    /**
     * 获取场景组
     *
     * @param groupId 场景组 ID
     * @return 场景组信息
     */
    CompletableFuture<SceneGroupDTO> getSceneGroup(String groupId);

    /**
     * 列出场景组
     *
     * @return 场景组列表
     */
    CompletableFuture<List<SceneGroupDTO>> listSceneGroups();

    /**
     * 生成邀请码
     *
     * @param groupId 场景组 ID
     * @return 邀请码
     */
    CompletableFuture<String> generateInviteCode(String groupId);

    /**
     * 邀请成员
     *
     * @param groupId 场景组 ID
     * @param peerId 目标节点 ID
     * @return 邀请结果
     */
    CompletableFuture<Void> inviteMember(String groupId, String peerId);

    /**
     * 移除成员
     *
     * @param groupId 场景组 ID
     * @param memberId 成员 ID
     * @return 移除结果
     */
    CompletableFuture<Void> removeMember(String groupId, String memberId);

    /**
     * 发送心跳
     *
     * @param groupId 场景组 ID
     * @return 心跳结果
     */
    CompletableFuture<HeartbeatResultDTO> sendHeartbeat(String groupId);

    /**
     * 获取场景组状态
     *
     * @param groupId 场景组 ID
     * @return 状态信息
     */
    CompletableFuture<SceneGroupStateDTO> getSceneGroupState(String groupId);

    /**
     * 添加场景组事件监听器
     *
     * @param listener 监听器
     */
    void addSceneGroupListener(SceneGroupEventListener listener);

    /**
     * 移除场景组事件监听器
     *
     * @param listener 监听器
     */
    void removeSceneGroupListener(SceneGroupEventListener listener);

    /**
     * 场景定义 DTO
     */
    class SceneDefinitionDTO {
        private String sceneId;
        private String sceneName;
        private String description;
        private List<CapabilityRequirementDTO> capabilityRequirements;
        private List<RoleDefinitionDTO> roleDefinitions;
        private Map<String, Object> config;
        private long createdAt;

        public String getSceneId() { return sceneId; }
        public void setSceneId(String sceneId) { this.sceneId = sceneId; }
        public String getSceneName() { return sceneName; }
        public void setSceneName(String sceneName) { this.sceneName = sceneName; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public List<CapabilityRequirementDTO> getCapabilityRequirements() { return capabilityRequirements; }
        public void setCapabilityRequirements(List<CapabilityRequirementDTO> capabilityRequirements) { this.capabilityRequirements = capabilityRequirements; }
        public List<RoleDefinitionDTO> getRoleDefinitions() { return roleDefinitions; }
        public void setRoleDefinitions(List<RoleDefinitionDTO> roleDefinitions) { this.roleDefinitions = roleDefinitions; }
        public Map<String, Object> getConfig() { return config; }
        public void setConfig(Map<String, Object> config) { this.config = config; }
        public long getCreatedAt() { return createdAt; }
        public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
    }

    /**
     * 能力需求 DTO
     */
    class CapabilityRequirementDTO {
        private String capabilityId;
        private String capabilityName;
        private int minCount;
        private int maxCount;
        private List<String> requiredSkills;

        public String getCapabilityId() { return capabilityId; }
        public void setCapabilityId(String capabilityId) { this.capabilityId = capabilityId; }
        public String getCapabilityName() { return capabilityName; }
        public void setCapabilityName(String capabilityName) { this.capabilityName = capabilityName; }
        public int getMinCount() { return minCount; }
        public void setMinCount(int minCount) { this.minCount = minCount; }
        public int getMaxCount() { return maxCount; }
        public void setMaxCount(int maxCount) { this.maxCount = maxCount; }
        public List<String> getRequiredSkills() { return requiredSkills; }
        public void setRequiredSkills(List<String> requiredSkills) { this.requiredSkills = requiredSkills; }
    }

    /**
     * 角色定义 DTO
     */
    class RoleDefinitionDTO {
        private String roleId;
        private String roleName;
        private String description;
        private List<String> permissions;

        public String getRoleId() { return roleId; }
        public void setRoleId(String roleId) { this.roleId = roleId; }
        public String getRoleName() { return roleName; }
        public void setRoleName(String roleName) { this.roleName = roleName; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public List<String> getPermissions() { return permissions; }
        public void setPermissions(List<String> permissions) { this.permissions = permissions; }
    }

    /**
     * 场景组配置 DTO
     */
    class SceneGroupConfigDTO {
        private String groupName;
        private int maxMembers;
        private int heartbeatInterval;
        private int heartbeatTimeout;
        private boolean autoFailover;

        public String getGroupName() { return groupName; }
        public void setGroupName(String groupName) { this.groupName = groupName; }
        public int getMaxMembers() { return maxMembers; }
        public void setMaxMembers(int maxMembers) { this.maxMembers = maxMembers; }
        public int getHeartbeatInterval() { return heartbeatInterval; }
        public void setHeartbeatInterval(int heartbeatInterval) { this.heartbeatInterval = heartbeatInterval; }
        public int getHeartbeatTimeout() { return heartbeatTimeout; }
        public void setHeartbeatTimeout(int heartbeatTimeout) { this.heartbeatTimeout = heartbeatTimeout; }
        public boolean isAutoFailover() { return autoFailover; }
        public void setAutoFailover(boolean autoFailover) { this.autoFailover = autoFailover; }
    }

    /**
     * 场景组 DTO
     */
    class SceneGroupDTO {
        private String groupId;
        private String sceneId;
        private String sceneName;
        private String groupName;
        private String primaryId;
        private List<SceneMemberDTO> members;
        private String inviteCode;
        private String status;
        private long createdAt;

        public String getGroupId() { return groupId; }
        public void setGroupId(String groupId) { this.groupId = groupId; }
        public String getSceneId() { return sceneId; }
        public void setSceneId(String sceneId) { this.sceneId = sceneId; }
        public String getSceneName() { return sceneName; }
        public void setSceneName(String sceneName) { this.sceneName = sceneName; }
        public String getGroupName() { return groupName; }
        public void setGroupName(String groupName) { this.groupName = groupName; }
        public String getPrimaryId() { return primaryId; }
        public void setPrimaryId(String primaryId) { this.primaryId = primaryId; }
        public List<SceneMemberDTO> getMembers() { return members; }
        public void setMembers(List<SceneMemberDTO> members) { this.members = members; }
        public String getInviteCode() { return inviteCode; }
        public void setInviteCode(String inviteCode) { this.inviteCode = inviteCode; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public long getCreatedAt() { return createdAt; }
        public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
    }

    /**
     * 场景成员 DTO
     */
    class SceneMemberDTO {
        private String memberId;
        private String memberName;
        private String role;
        private String status;
        private long joinedAt;
        private long lastHeartbeat;

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
        public long getLastHeartbeat() { return lastHeartbeat; }
        public void setLastHeartbeat(long lastHeartbeat) { this.lastHeartbeat = lastHeartbeat; }
    }

    /**
     * 心跳结果 DTO
     */
    class HeartbeatResultDTO {
        private String groupId;
        private boolean success;
        private List<String> activeMembers;
        private List<String> inactiveMembers;
        private String primaryId;

        public String getGroupId() { return groupId; }
        public void setGroupId(String groupId) { this.groupId = groupId; }
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public List<String> getActiveMembers() { return activeMembers; }
        public void setActiveMembers(List<String> activeMembers) { this.activeMembers = activeMembers; }
        public List<String> getInactiveMembers() { return inactiveMembers; }
        public void setInactiveMembers(List<String> inactiveMembers) { this.inactiveMembers = inactiveMembers; }
        public String getPrimaryId() { return primaryId; }
        public void setPrimaryId(String primaryId) { this.primaryId = primaryId; }
    }

    /**
     * 场景组状态 DTO
     */
    class SceneGroupStateDTO {
        private String groupId;
        private String status;
        private int memberCount;
        private int activeCount;
        private String primaryId;
        private Map<String, Object> sharedState;
        private long lastUpdate;

        public String getGroupId() { return groupId; }
        public void setGroupId(String groupId) { this.groupId = groupId; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public int getMemberCount() { return memberCount; }
        public void setMemberCount(int memberCount) { this.memberCount = memberCount; }
        public int getActiveCount() { return activeCount; }
        public void setActiveCount(int activeCount) { this.activeCount = activeCount; }
        public String getPrimaryId() { return primaryId; }
        public void setPrimaryId(String primaryId) { this.primaryId = primaryId; }
        public Map<String, Object> getSharedState() { return sharedState; }
        public void setSharedState(Map<String, Object> sharedState) { this.sharedState = sharedState; }
        public long getLastUpdate() { return lastUpdate; }
        public void setLastUpdate(long lastUpdate) { this.lastUpdate = lastUpdate; }
    }

    /**
     * 场景组事件监听器
     */
    interface SceneGroupEventListener {
        void onSceneGroupCreated(SceneGroupDTO group);
        void onMemberJoined(String groupId, SceneMemberDTO member);
        void onMemberLeft(String groupId, String memberId);
        void onPrimaryChanged(String groupId, String oldPrimaryId, String newPrimaryId);
        void onHeartbeatTimeout(String groupId, String memberId);
        void onStateChanged(String groupId, Map<String, Object> state);
    }
}
