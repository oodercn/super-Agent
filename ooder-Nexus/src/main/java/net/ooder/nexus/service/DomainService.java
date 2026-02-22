package net.ooder.nexus.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 组织域服务接口
 *
 * <p>SDK 0.7.2 新增接口，提供组织域接入和管理能力。</p>
 *
 * <p>主要功能：</p>
 * <ul>
 *   <li>组织域邀请处理</li>
 *   <li>域接入与退出</li>
 *   <li>域成员管理</li>
 *   <li>域资源访问</li>
 * </ul>
 *
 * @author ooder Team
 * @version 1.0
 * @since SDK 0.7.2
 */
public interface DomainService {

    /**
     * 处理组织域邀请
     *
     * @param inviteCode 邀请码
     * @return 邀请详情
     */
    CompletableFuture<DomainInvitation> parseInvitation(String inviteCode);

    /**
     * 接受组织域邀请
     *
     * @param inviteCode 邀请码
     * @return 接入结果
     */
    CompletableFuture<DomainMembership> acceptInvitation(String inviteCode);

    /**
     * 拒绝组织域邀请
     *
     * @param inviteCode 邀请码
     * @return 拒绝结果
     */
    CompletableFuture<Void> rejectInvitation(String inviteCode);

    /**
     * 获取当前所属域列表
     *
     * @return 域列表
     */
    CompletableFuture<List<DomainInfo>> listDomains();

    /**
     * 获取域详情
     *
     * @param domainId 域ID
     * @return 域详情
     */
    CompletableFuture<DomainInfo> getDomainInfo(String domainId);

    /**
     * 退出组织域
     *
     * @param domainId 域ID
     * @return 退出结果
     */
    CompletableFuture<Void> leaveDomain(String domainId);

    /**
     * 获取域成员列表
     *
     * @param domainId 域ID
     * @return 成员列表
     */
    CompletableFuture<List<DomainMember>> listDomainMembers(String domainId);

    /**
     * 获取域资源列表
     *
     * @param domainId 域ID
     * @return 资源列表
     */
    CompletableFuture<List<DomainResource>> listDomainResources(String domainId);

    /**
     * 请求访问域资源
     *
     * @param domainId 域ID
     * @param resourceId 资源ID
     * @return 访问令牌
     */
    CompletableFuture<ResourceAccessToken> requestResourceAccess(String domainId, String resourceId);

    /**
     * 检查是否已加入域
     *
     * @return 是否已加入域
     */
    boolean hasJoinedDomain();

    /**
     * 获取当前活跃域
     *
     * @return 当前活跃域
     */
    DomainInfo getCurrentDomain();

    /**
     * 组织域邀请信息
     */
    class DomainInvitation {
        private String inviteCode;
        private String domainId;
        private String domainName;
        private String inviterId;
        private String inviterName;
        private long expireTime;
        private String role;

        public String getInviteCode() { return inviteCode; }
        public void setInviteCode(String inviteCode) { this.inviteCode = inviteCode; }
        public String getDomainId() { return domainId; }
        public void setDomainId(String domainId) { this.domainId = domainId; }
        public String getDomainName() { return domainName; }
        public void setDomainName(String domainName) { this.domainName = domainName; }
        public String getInviterId() { return inviterId; }
        public void setInviterId(String inviterId) { this.inviterId = inviterId; }
        public String getInviterName() { return inviterName; }
        public void setInviterName(String inviterName) { this.inviterName = inviterName; }
        public long getExpireTime() { return expireTime; }
        public void setExpireTime(long expireTime) { this.expireTime = expireTime; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
    }

    /**
     * 域成员关系
     */
    class DomainMembership {
        private String membershipId;
        private String domainId;
        private String memberId;
        private String role;
        private long joinTime;

        public String getMembershipId() { return membershipId; }
        public void setMembershipId(String membershipId) { this.membershipId = membershipId; }
        public String getDomainId() { return domainId; }
        public void setDomainId(String domainId) { this.domainId = domainId; }
        public String getMemberId() { return memberId; }
        public void setMemberId(String memberId) { this.memberId = memberId; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
        public long getJoinTime() { return joinTime; }
        public void setJoinTime(long joinTime) { this.joinTime = joinTime; }
    }

    /**
     * 域信息
     */
    class DomainInfo {
        private String domainId;
        private String domainName;
        private String description;
        private String ownerId;
        private int memberCount;
        private long createTime;

        public String getDomainId() { return domainId; }
        public void setDomainId(String domainId) { this.domainId = domainId; }
        public String getDomainName() { return domainName; }
        public void setDomainName(String domainName) { this.domainName = domainName; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getOwnerId() { return ownerId; }
        public void setOwnerId(String ownerId) { this.ownerId = ownerId; }
        public int getMemberCount() { return memberCount; }
        public void setMemberCount(int memberCount) { this.memberCount = memberCount; }
        public long getCreateTime() { return createTime; }
        public void setCreateTime(long createTime) { this.createTime = createTime; }
    }

    /**
     * 域成员
     */
    class DomainMember {
        private String memberId;
        private String memberName;
        private String role;
        private String status;
        private long joinTime;

        public String getMemberId() { return memberId; }
        public void setMemberId(String memberId) { this.memberId = memberId; }
        public String getMemberName() { return memberName; }
        public void setMemberName(String memberName) { this.memberName = memberName; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public long getJoinTime() { return joinTime; }
        public void setJoinTime(long joinTime) { this.joinTime = joinTime; }
    }

    /**
     * 域资源
     */
    class DomainResource {
        private String resourceId;
        private String resourceName;
        private String resourceType;
        private String description;
        private String accessLevel;

        public String getResourceId() { return resourceId; }
        public void setResourceId(String resourceId) { this.resourceId = resourceId; }
        public String getResourceName() { return resourceName; }
        public void setResourceName(String resourceName) { this.resourceName = resourceName; }
        public String getResourceType() { return resourceType; }
        public void setResourceType(String resourceType) { this.resourceType = resourceType; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getAccessLevel() { return accessLevel; }
        public void setAccessLevel(String accessLevel) { this.accessLevel = accessLevel; }
    }

    /**
     * 资源访问令牌
     */
    class ResourceAccessToken {
        private String token;
        private String resourceId;
        private long expireTime;
        private String accessLevel;

        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
        public String getResourceId() { return resourceId; }
        public void setResourceId(String resourceId) { this.resourceId = resourceId; }
        public long getExpireTime() { return expireTime; }
        public void setExpireTime(long expireTime) { this.expireTime = expireTime; }
        public String getAccessLevel() { return accessLevel; }
        public void setAccessLevel(String accessLevel) { this.accessLevel = accessLevel; }
    }
}
