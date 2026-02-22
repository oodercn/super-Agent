package net.ooder.nexus.skillcenter.controller;

import net.ooder.skillcenter.dto.PageResult;
import net.ooder.skillcenter.dto.SkillDTO;
import net.ooder.skillcenter.dto.GroupDTO;
import net.ooder.skillcenter.service.SkillService;
import net.ooder.skillcenter.service.GroupService;
import net.ooder.nexus.skillcenter.model.ResultModel;
import net.ooder.nexus.skillcenter.dto.personal.*;
import net.ooder.nexus.skillcenter.dto.common.PaginationDTO;
import net.ooder.nexus.skillcenter.dto.common.OperationResultDTO;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/personal")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS})
public class PersonalController extends BaseController {

    private final SkillService skillService;
    private final GroupService groupService;

    public PersonalController(SkillService skillService, GroupService groupService) {
        this.skillService = skillService;
        this.groupService = groupService;
    }

    @PostMapping("/dashboard/stats")
    public ResultModel<PersonalDashboardStatsDTO> getDashboardStats() {
        long startTime = System.currentTimeMillis();
        logRequestStart("getDashboardStats", null);

        try {
            PersonalDashboardStatsDTO stats = new PersonalDashboardStatsDTO();
            stats.setTotalSkills(skillService.getSkillCount());
            stats.setTotalExecutions(skillService.getExecutionCount());
            stats.setSuccessfulExecutions(skillService.getSuccessfulExecutionCount());
            stats.setFailedExecutions(skillService.getFailedExecutionCount());

            List<PersonalDashboardStatsDTO.ActivityDTO> activities = new ArrayList<>();
            stats.setRecentActivities(activities);

            logRequestEnd("getDashboardStats", stats, System.currentTimeMillis() - startTime);
            return ResultModel.success(stats);
        } catch (Exception e) {
            logRequestError("getDashboardStats", e);
            return ResultModel.error(500, "获取仪表盘统计数据失败: " + e.getMessage());
        }
    }

    @PostMapping("/skills/list")
    public ResultModel<PageResult<SkillDTO>> getSkills(@RequestBody PaginationDTO pagination) {
        long startTime = System.currentTimeMillis();
        logRequestStart("getSkills", pagination);

        try {
            PageResult<SkillDTO> result = skillService.getAllSkills(null, null, null, 
                pagination.getPageNum(), pagination.getPageSize());
            logRequestEnd("getSkills", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("getSkills", e);
            return ResultModel.error(500, "获取技能列表失败: " + e.getMessage());
        }
    }

    @PostMapping("/skills/add")
    public ResultModel<SkillDTO> addSkill(@RequestBody SkillDTO skillDTO) {
        long startTime = System.currentTimeMillis();
        logRequestStart("addSkill", skillDTO);

        try {
            SkillDTO result = skillService.addSkill(skillDTO);
            logRequestEnd("addSkill", result, System.currentTimeMillis() - startTime);
            return ResultModel.success("添加技能成功", result);
        } catch (Exception e) {
            logRequestError("addSkill", e);
            return ResultModel.error(500, "添加技能失败: " + e.getMessage());
        }
    }

    @PostMapping("/groups/list")
    public ResultModel<PageResult<GroupDTO>> getGroups(@RequestBody PaginationDTO pagination) {
        long startTime = System.currentTimeMillis();
        logRequestStart("getGroups", pagination);

        try {
            PageResult<GroupDTO> result = groupService.getAllGroups(pagination.getPageNum(), pagination.getPageSize());
            logRequestEnd("getGroups", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("getGroups", e);
            return ResultModel.error(500, "获取群组列表失败: " + e.getMessage());
        }
    }

    @PostMapping("/groups/add")
    public ResultModel<GroupDTO> createGroup(@RequestBody GroupDTO groupDTO) {
        long startTime = System.currentTimeMillis();
        logRequestStart("createGroup", groupDTO);

        try {
            GroupDTO result = groupService.createGroup(groupDTO);
            logRequestEnd("createGroup", result, System.currentTimeMillis() - startTime);
            return ResultModel.success("创建群组成功", result);
        } catch (Exception e) {
            logRequestError("createGroup", e);
            return ResultModel.error(500, "创建群组失败: " + e.getMessage());
        }
    }

    @PostMapping("/groups/{groupId}/get")
    public ResultModel<GroupDTO> getGroup(@PathVariable String groupId) {
        long startTime = System.currentTimeMillis();
        logRequestStart("getGroup", groupId);

        try {
            GroupDTO group = groupService.getGroupById(groupId);
            if (group == null) {
                return ResultModel.notFound("群组不存在");
            }
            logRequestEnd("getGroup", group, System.currentTimeMillis() - startTime);
            return ResultModel.success(group);
        } catch (Exception e) {
            logRequestError("getGroup", e);
            return ResultModel.error(500, "获取群组失败: " + e.getMessage());
        }
    }

    @PostMapping("/groups/{groupId}/update")
    public ResultModel<GroupDTO> updateGroup(@PathVariable String groupId, @RequestBody GroupDTO groupDTO) {
        long startTime = System.currentTimeMillis();
        logRequestStart("updateGroup", groupDTO);

        try {
            GroupDTO result = groupService.updateGroup(groupId, groupDTO);
            if (result == null) {
                return ResultModel.notFound("群组不存在");
            }
            logRequestEnd("updateGroup", result, System.currentTimeMillis() - startTime);
            return ResultModel.success("更新群组成功", result);
        } catch (Exception e) {
            logRequestError("updateGroup", e);
            return ResultModel.error(500, "更新群组失败: " + e.getMessage());
        }
    }

    @PostMapping("/groups/{groupId}/delete")
    public ResultModel<OperationResultDTO> deleteGroup(@PathVariable String groupId) {
        long startTime = System.currentTimeMillis();
        logRequestStart("deleteGroup", groupId);

        try {
            boolean success = groupService.deleteGroup(groupId);
            OperationResultDTO result = success ? 
                OperationResultDTO.success("删除群组成功") : 
                OperationResultDTO.failure("删除群组失败");
            logRequestEnd("deleteGroup", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("deleteGroup", e);
            return ResultModel.error(500, "删除群组失败: " + e.getMessage());
        }
    }

    @PostMapping("/groups/skills/list")
    public ResultModel<PageResult<SkillDTO>> getGroupSkills(@RequestBody PaginationDTO pagination) {
        long startTime = System.currentTimeMillis();
        logRequestStart("getGroupSkills", pagination);

        try {
            PageResult<SkillDTO> result = skillService.getAllSkills(null, null, null, 
                pagination.getPageNum(), pagination.getPageSize());
            logRequestEnd("getGroupSkills", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("getGroupSkills", e);
            return ResultModel.error(500, "获取群组技能列表失败: " + e.getMessage());
        }
    }

    @PostMapping("/groups/skills/add")
    public ResultModel<OperationResultDTO> addSkillToGroup(@RequestBody SkillDTO skillDTO) {
        long startTime = System.currentTimeMillis();
        logRequestStart("addSkillToGroup", skillDTO);

        try {
            OperationResultDTO result = OperationResultDTO.success("添加技能到群组成功");
            logRequestEnd("addSkillToGroup", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("addSkillToGroup", e);
            return ResultModel.error(500, "添加技能到群组失败: " + e.getMessage());
        }
    }

    @PostMapping("/identity/get")
    public ResultModel<IdentityMappingDTO> getIdentity() {
        long startTime = System.currentTimeMillis();
        logRequestStart("getIdentity", null);

        try {
            IdentityMappingDTO identity = new IdentityMappingDTO();
            identity.setType("default");
            identity.setIdentifier("user-001");
            identity.setStatus("active");
            identity.setLinkedAt("2024-01-01T00:00:00Z");

            logRequestEnd("getIdentity", identity, System.currentTimeMillis() - startTime);
            return ResultModel.success(identity);
        } catch (Exception e) {
            logRequestError("getIdentity", e);
            return ResultModel.error(500, "获取身份信息失败: " + e.getMessage());
        }
    }

    @PostMapping("/identity/update")
    public ResultModel<OperationResultDTO> updateIdentity(@RequestBody IdentityMappingDTO identityDTO) {
        long startTime = System.currentTimeMillis();
        logRequestStart("updateIdentity", identityDTO);

        try {
            OperationResultDTO result = OperationResultDTO.success("更新身份信息成功");
            logRequestEnd("updateIdentity", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("updateIdentity", e);
            return ResultModel.error(500, "更新身份信息失败: " + e.getMessage());
        }
    }

    @PostMapping("/identity/mappings/list")
    public ResultModel<List<IdentityMappingDTO>> getIdentityMappings() {
        long startTime = System.currentTimeMillis();
        logRequestStart("getIdentityMappings", null);

        try {
            List<IdentityMappingDTO> mappings = new ArrayList<>();
            
            IdentityMappingDTO mapping1 = new IdentityMappingDTO();
            mapping1.setType("email");
            mapping1.setIdentifier("user@example.com");
            mapping1.setStatus("active");
            mapping1.setLinkedAt("2024-01-01T00:00:00Z");
            mappings.add(mapping1);

            IdentityMappingDTO mapping2 = new IdentityMappingDTO();
            mapping2.setType("phone");
            mapping2.setIdentifier("+86-138****1234");
            mapping2.setStatus("active");
            mapping2.setLinkedAt("2024-01-02T00:00:00Z");
            mappings.add(mapping2);

            logRequestEnd("getIdentityMappings", mappings.size() + " mappings", System.currentTimeMillis() - startTime);
            return ResultModel.success(mappings);
        } catch (Exception e) {
            logRequestError("getIdentityMappings", e);
            return ResultModel.error(500, "获取身份映射列表失败: " + e.getMessage());
        }
    }

    @PostMapping("/help/get")
    public ResultModel<HelpContentDTO> getHelp() {
        long startTime = System.currentTimeMillis();
        logRequestStart("getHelp", null);

        try {
            HelpContentDTO help = new HelpContentDTO();
            
            List<HelpContentDTO.QuickStartDTO> quickStartList = new ArrayList<>();
            HelpContentDTO.QuickStartDTO quickStart = new HelpContentDTO.QuickStartDTO();
            quickStart.setId("qs-1");
            quickStart.setTitle("快速入门");
            quickStart.setDescription("了解如何快速开始使用技能中心");
            quickStart.setIcon("rocket");
            quickStartList.add(quickStart);
            help.setQuickStart(quickStartList);

            List<HelpContentDTO.FaqDTO> faqList = new ArrayList<>();
            HelpContentDTO.FaqDTO faq = new HelpContentDTO.FaqDTO();
            faq.setId("faq-1");
            faq.setQuestion("如何创建技能?");
            faq.setAnswer("在技能管理页面点击'添加技能'按钮即可创建新技能");
            faqList.add(faq);
            help.setFaq(faqList);

            HelpContentDTO.SupportDTO support = new HelpContentDTO.SupportDTO();
            support.setEmail("support@example.com");
            support.setPhone("400-123-4567");
            support.setHours("周一至周五 9:00-18:00");
            help.setSupport(support);

            logRequestEnd("getHelp", help, System.currentTimeMillis() - startTime);
            return ResultModel.success(help);
        } catch (Exception e) {
            logRequestError("getHelp", e);
            return ResultModel.error(500, "获取帮助信息失败: " + e.getMessage());
        }
    }

    @PostMapping("/settings/get")
    public ResultModel<UserSettingsDTO> getSettings() {
        long startTime = System.currentTimeMillis();
        logRequestStart("getSettings", null);

        try {
            UserSettingsDTO settings = new UserSettingsDTO();

            UserSettingsDTO.NotificationSettingsDTO notifications = new UserSettingsDTO.NotificationSettingsDTO();
            notifications.setEmail(true);
            notifications.setPush(true);
            notifications.setSms(false);
            settings.setNotifications(notifications);

            UserSettingsDTO.PrivacySettingsDTO privacy = new UserSettingsDTO.PrivacySettingsDTO();
            privacy.setPublicProfile(true);
            privacy.setShowSkills(true);
            privacy.setShowActivity(false);
            settings.setPrivacy(privacy);

            UserSettingsDTO.InterfaceSettingsDTO interfaceSettings = new UserSettingsDTO.InterfaceSettingsDTO();
            interfaceSettings.setTheme("light");
            interfaceSettings.setLanguage("zh-CN");
            interfaceSettings.setCompactMode(false);
            settings.setInterfaceSettings(interfaceSettings);

            logRequestEnd("getSettings", settings, System.currentTimeMillis() - startTime);
            return ResultModel.success(settings);
        } catch (Exception e) {
            logRequestError("getSettings", e);
            return ResultModel.error(500, "获取设置信息失败: " + e.getMessage());
        }
    }

    @PostMapping("/settings/update")
    public ResultModel<OperationResultDTO> updateSettings(@RequestBody UserSettingsDTO settingsDTO) {
        long startTime = System.currentTimeMillis();
        logRequestStart("updateSettings", settingsDTO);

        try {
            OperationResultDTO result = OperationResultDTO.success("更新设置成功");
            logRequestEnd("updateSettings", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("updateSettings", e);
            return ResultModel.error(500, "更新设置失败: " + e.getMessage());
        }
    }

    @PostMapping("/features/get")
    public ResultModel<FeatureFlagsDTO> getFeatures() {
        long startTime = System.currentTimeMillis();
        logRequestStart("getFeatures", null);

        try {
            FeatureFlagsDTO features = new FeatureFlagsDTO();
            features.setSkillPublishing(true);
            features.setSkillMarket(true);
            features.setSkillExecution(true);
            features.setGroupManagement(true);
            features.setSkillAuthentication(true);
            features.setPersonalIdentity(true);

            List<String> featureList = new ArrayList<>();
            featureList.add("skill_publishing");
            featureList.add("skill_market");
            featureList.add("skill_execution");
            featureList.add("group_management");
            featureList.add("skill_authentication");
            featureList.add("personal_identity");
            features.setFeatureList(featureList);

            logRequestEnd("getFeatures", features, System.currentTimeMillis() - startTime);
            return ResultModel.success(features);
        } catch (Exception e) {
            logRequestError("getFeatures", e);
            return ResultModel.error(500, "获取功能开关失败: " + e.getMessage());
        }
    }
}
