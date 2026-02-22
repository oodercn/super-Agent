package net.ooder.nexus.service.group;

import net.ooder.nexus.domain.group.model.GroupExt;
import net.ooder.nexus.dto.group.GroupCreateDTO;

import java.util.List;

/**
 * 组织群组服务接口
 * 
 * <p>提供组织机构与群组的关联管理功能。</p>
 * 
 * @author ooder Team
 * @version 0.7.3
 * @since SDK 0.7.3
 */
public interface OrgGroupService {

    /**
     * 创建群组
     *
     * @param dto 创建参数
     * @return 创建的群组
     */
    GroupExt createGroup(GroupCreateDTO dto);

    /**
     * 获取群组详情
     *
     * @param groupId 群组ID
     * @return 群组信息
     */
    GroupExt getGroup(String groupId);

    /**
     * 获取组织下的群组列表
     *
     * @param orgId 组织ID
     * @return 群组列表
     */
    List<GroupExt> getGroupsByOrg(String orgId);

    /**
     * 获取部门下的群组列表
     *
     * @param departmentId 部门ID
     * @return 群组列表
     */
    List<GroupExt> getGroupsByDepartment(String departmentId);

    /**
     * 获取用户所在的群组列表
     *
     * @param userId 用户ID
     * @return 群组列表
     */
    List<GroupExt> getGroupsByUser(String userId);

    /**
     * 更新群组信息
     *
     * @param groupId 群组ID
     * @param name 名称
     * @param description 描述
     * @return 更新后的群组
     */
    GroupExt updateGroup(String groupId, String name, String description);

    /**
     * 删除群组
     *
     * @param groupId 群组ID
     * @param operatorId 操作者ID
     * @return 操作结果
     */
    boolean deleteGroup(String groupId, String operatorId);

    /**
     * 添加群组成员
     *
     * @param groupId 群组ID
     * @param userId 用户ID
     * @param userName 用户名
     * @param role 角色
     * @return 操作结果
     */
    boolean addMember(String groupId, String userId, String userName, String role);

    /**
     * 移除群组成员
     *
     * @param groupId 群组ID
     * @param userId 用户ID
     * @param operatorId 操作者ID
     * @return 操作结果
     */
    boolean removeMember(String groupId, String userId, String operatorId);

    /**
     * 获取群组成员列表
     *
     * @param groupId 群组ID
     * @return 成员列表
     */
    List<GroupMember> getMembers(String groupId);

    /**
     * 生成邀请码
     *
     * @param groupId 群组ID
     * @return 邀请码
     */
    String generateInviteCode(String groupId);

    /**
     * 通过邀请码加入群组
     *
     * @param inviteCode 邀请码
     * @param userId 用户ID
     * @param userName 用户名
     * @return 加入的群组
     */
    GroupExt joinByInviteCode(String inviteCode, String userId, String userName);

    /**
     * 群组成员模型
     */
    class GroupMember {
        private String memberId;
        private String memberName;
        private String role;
        private String status;
        private long joinedAt;

        public String getMemberId() {
            return memberId;
        }

        public void setMemberId(String memberId) {
            this.memberId = memberId;
        }

        public String getMemberName() {
            return memberName;
        }

        public void setMemberName(String memberName) {
            this.memberName = memberName;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public long getJoinedAt() {
            return joinedAt;
        }

        public void setJoinedAt(long joinedAt) {
            this.joinedAt = joinedAt;
        }
    }
}
