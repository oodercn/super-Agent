package net.ooder.skillcenter.service;

import net.ooder.skillcenter.dto.*;

import java.util.List;
import java.util.Map;

/**
 * 个人中心服务接口
 */
public interface PersonalService {

    // ==================== 个人仪表盘 ====================
    Map<String, Object> getPersonalDashboardStats();

    // ==================== 我的技能 ====================
    List<SkillDTO> getMySkills();
    SkillDTO getSkillDetail(String skillId);
    SkillDTO createSkill(SkillDTO skillDTO);
    SkillDTO updateSkill(String skillId, SkillDTO skillDTO);
    boolean deleteSkill(String skillId);
    SkillResultDTO executeSkill(String skillId, Map<String, Object> parameters);

    // ==================== 我的群组 ====================
    List<GroupDTO> getMyGroups();
    GroupDTO getGroupById(String groupId);
    GroupDTO createGroup(GroupDTO group);
    GroupDTO updateGroup(String groupId, GroupDTO group);
    boolean deleteGroup(String groupId);

    // ==================== 群组技能 ====================
    List<GroupSkillDTO> getAllGroupSkills();
    List<GroupSkillDTO> getGroupSkills(String groupId);
    GroupSkillDTO addGroupSkill(GroupSkillDTO groupSkill);
    boolean deleteGroupSkill(String skillId);

    // ==================== 群组成员 ====================
    List<GroupMemberDTO> getGroupMembers(String groupId);
    GroupMemberDTO addGroupMember(String groupId, GroupMemberDTO member);
    GroupMemberDTO updateGroupMember(String groupId, String memberId, GroupMemberDTO member);
    boolean deleteGroupMember(String groupId, String memberId);

    // ==================== 执行历史 ====================
    List<ExecutionRecordDTO> getExecutionHistory();
    ExecutionRecordDTO getExecutionById(String executionId);
    boolean deleteExecution(String executionId);
    boolean clearExecutionHistory();

    // ==================== 个人身份 ====================
    UserDTO getPersonalIdentity();
    UserDTO updatePersonalIdentity(UserDTO user);
    List<IdentityMappingDTO> getIdentityMappings();

    // ==================== 帮助与支持 ====================
    Map<String, Object> getHelp();

    // ==================== 设置 ====================
    Map<String, Object> getSettings();
    boolean updateSettings(Map<String, Object> settings);

    // ==================== 功能开关 ====================
    Map<String, Object> getFeatureFlags();
}
