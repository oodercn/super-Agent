package net.ooder.skillcenter.service.impl;

import net.ooder.skillcenter.dto.*;
import net.ooder.skillcenter.manager.*;
import net.ooder.skillcenter.model.Skill;
import net.ooder.skillcenter.service.PersonalService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Service
@ConditionalOnProperty(name = "skillcenter.sdk.mode", havingValue = "sdk")
public class PersonalServiceSdkImpl implements PersonalService {

    private SkillManager skillManager;
    private GroupManager groupManager;
    private UserManager userManager;

    @PostConstruct
    public void init() {
        skillManager = SkillManager.getInstance();
        groupManager = GroupManager.getInstance();
        userManager = UserManager.getInstance();
    }

    private SkillDTO convertToDTO(Skill skill) {
        if (skill == null) return null;
        SkillDTO dto = new SkillDTO();
        dto.setId(skill.getId());
        dto.setName(skill.getName());
        dto.setDescription(skill.getDescription());
        dto.setAvailable(skill.isAvailable());
        if (skill instanceof SkillManager.SkillInfo) {
            SkillManager.SkillInfo info = (SkillManager.SkillInfo) skill;
            dto.setCategory(info.getCategory());
            dto.setStatus(info.getStatus());
        }
        return dto;
    }

    @Override
    public Map<String, Object> getPersonalDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalSkills", skillManager.getAllSkills().size());
        stats.put("totalGroups", groupManager.getAllGroups().size());
        return stats;
    }

    @Override
    public List<SkillDTO> getMySkills() {
        return skillManager.getAllSkills().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Override
    public SkillDTO getSkillDetail(String skillId) {
        return convertToDTO(skillManager.getSkill(skillId));
    }

    @Override
    public SkillDTO createSkill(SkillDTO skillDTO) {
        SkillManager.SkillInfo info = new SkillManager.SkillInfo();
        info.setName(skillDTO.getName());
        info.setDescription(skillDTO.getDescription());
        info.setCategory(skillDTO.getCategory() != null ? skillDTO.getCategory() : "general");
        info.setStatus("active");
        info.setAvailable(true);
        skillManager.registerSkill(info);
        return convertToDTO(info);
    }

    @Override
    public SkillDTO updateSkill(String skillId, SkillDTO skillDTO) {
        Skill skill = skillManager.getSkill(skillId);
        if (skill == null) return null;
        return convertToDTO(skill);
    }

    @Override
    public boolean deleteSkill(String skillId) {
        skillManager.unregisterSkill(skillId);
        return true;
    }

    @Override
    public SkillResultDTO executeSkill(String skillId, Map<String, Object> parameters) {
        SkillResultDTO result = new SkillResultDTO();
        result.setSkillId(skillId);
        result.setStatus("success");
        result.setOutput("Executed successfully");
        result.setExecutedAt(new Date());
        return result;
    }

    @Override
    public List<GroupDTO> getMyGroups() {
        return groupManager.getAllGroups().stream()
            .map(g -> {
                GroupDTO dto = new GroupDTO();
                dto.setId(g.getId());
                dto.setName(g.getName());
                dto.setDescription(g.getDescription());
                dto.setMemberCount(g.getMemberCount());
                return dto;
            })
            .collect(Collectors.toList());
    }

    @Override
    public GroupDTO getGroupById(String groupId) {
        GroupManager.Group g = groupManager.getGroup(groupId);
        if (g == null) return null;
        GroupDTO dto = new GroupDTO();
        dto.setId(g.getId());
        dto.setName(g.getName());
        dto.setDescription(g.getDescription());
        dto.setMemberCount(g.getMemberCount());
        return dto;
    }

    @Override
    public GroupDTO createGroup(GroupDTO group) {
        GroupManager.Group g = new GroupManager.Group();
        g.setName(group.getName());
        g.setDescription(group.getDescription());
        g.setMembers(new ArrayList<>());
        g.setSkills(new ArrayList<>());
        GroupManager.Group created = groupManager.createGroup(g);
        GroupDTO dto = new GroupDTO();
        dto.setId(created.getId());
        dto.setName(created.getName());
        dto.setDescription(created.getDescription());
        return dto;
    }

    @Override
    public GroupDTO updateGroup(String groupId, GroupDTO group) {
        GroupManager.Group g = groupManager.getGroup(groupId);
        if (g == null) return null;
        g.setName(group.getName());
        g.setDescription(group.getDescription());
        GroupManager.Group updated = groupManager.updateGroup(g);
        GroupDTO dto = new GroupDTO();
        dto.setId(updated.getId());
        dto.setName(updated.getName());
        dto.setDescription(updated.getDescription());
        return dto;
    }

    @Override
    public boolean deleteGroup(String groupId) {
        return groupManager.deleteGroup(groupId);
    }

    @Override
    public List<GroupSkillDTO> getAllGroupSkills() {
        return Collections.emptyList();
    }

    @Override
    public List<GroupSkillDTO> getGroupSkills(String groupId) {
        return Collections.emptyList();
    }

    @Override
    public GroupSkillDTO addGroupSkill(GroupSkillDTO groupSkill) {
        return groupSkill;
    }

    @Override
    public boolean deleteGroupSkill(String skillId) {
        return true;
    }

    @Override
    public List<GroupMemberDTO> getGroupMembers(String groupId) {
        return Collections.emptyList();
    }

    @Override
    public GroupMemberDTO addGroupMember(String groupId, GroupMemberDTO member) {
        return member;
    }

    @Override
    public GroupMemberDTO updateGroupMember(String groupId, String memberId, GroupMemberDTO member) {
        return member;
    }

    @Override
    public boolean deleteGroupMember(String groupId, String memberId) {
        return true;
    }

    @Override
    public List<ExecutionRecordDTO> getExecutionHistory() {
        return Collections.emptyList();
    }

    @Override
    public ExecutionRecordDTO getExecutionById(String executionId) {
        return null;
    }

    @Override
    public boolean deleteExecution(String executionId) {
        return true;
    }

    @Override
    public boolean clearExecutionHistory() {
        return true;
    }

    @Override
    public UserDTO getPersonalIdentity() {
        UserDTO dto = new UserDTO();
        dto.setId("current-user");
        dto.setUsername("CurrentUser");
        dto.setEmail("user@example.com");
        return dto;
    }

    @Override
    public UserDTO updatePersonalIdentity(UserDTO user) {
        return user;
    }

    @Override
    public List<IdentityMappingDTO> getIdentityMappings() {
        return Collections.emptyList();
    }

    @Override
    public Map<String, Object> getHelp() {
        Map<String, Object> help = new HashMap<>();
        help.put("documentation", "https://gitee.com/ooderCN");
        return help;
    }

    @Override
    public Map<String, Object> getSettings() {
        Map<String, Object> settings = new HashMap<>();
        settings.put("theme", "light");
        settings.put("language", "zh-CN");
        return settings;
    }

    @Override
    public boolean updateSettings(Map<String, Object> settings) {
        return true;
    }

    @Override
    public Map<String, Object> getFeatureFlags() {
        Map<String, Object> flags = new HashMap<>();
        flags.put("p2pEnabled", true);
        flags.put("marketEnabled", true);
        return flags;
    }
}
