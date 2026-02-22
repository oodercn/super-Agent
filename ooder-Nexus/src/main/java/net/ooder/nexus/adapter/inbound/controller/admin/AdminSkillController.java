package net.ooder.nexus.adapter.inbound.controller.admin;

import net.ooder.config.ResultModel;
import net.ooder.config.ListResultModel;
import net.ooder.nexus.service.AdminSkillService;
import net.ooder.sdk.api.skill.InstalledSkill;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/admin/skills")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS})
public class AdminSkillController {

    private static final Logger log = LoggerFactory.getLogger(AdminSkillController.class);

    @Autowired
    private AdminSkillService adminSkillService;

    @PostMapping("/list")
    @ResponseBody
    public ListResultModel<List<Map<String, Object>>> getList() {
        log.info("Get admin skill list requested");
        ListResultModel<List<Map<String, Object>>> result = new ListResultModel<List<Map<String, Object>>>();

        try {
            List<InstalledSkill> skills = adminSkillService.getAllSkills();
            List<Map<String, Object>> skillList = new ArrayList<Map<String, Object>>();

            for (InstalledSkill skill : skills) {
                Map<String, Object> skillMap = new HashMap<String, Object>();
                skillMap.put("id", skill.getSkillId());
                skillMap.put("name", skill.getName());
                skillMap.put("version", skill.getVersion());
                skillMap.put("status", skill.getStatus());
                skillMap.put("sceneId", skill.getSceneId());
                skillList.add(skillMap);
            }

            result.setData(skillList);
            result.setSize(skillList.size());
            result.setRequestStatus(200);
            result.setMessage("Success");
        } catch (Exception e) {
            log.error("Error getting skill list", e);
            result.setRequestStatus(500);
            result.setMessage("Error: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/get")
    @ResponseBody
    public ResultModel<Map<String, Object>> getSkill(@RequestBody Map<String, String> request) {
        log.info("Get skill detail requested: {}", request.get("id"));
        ResultModel<Map<String, Object>> result = new ResultModel<Map<String, Object>>();

        try {
            String skillId = request.get("id");
            InstalledSkill skill = adminSkillService.getSkillById(skillId);

            if (skill == null) {
                result.setRequestStatus(404);
                result.setMessage("Skill not found");
                return result;
            }

            Map<String, Object> skillMap = new HashMap<String, Object>();
            skillMap.put("id", skill.getSkillId());
            skillMap.put("name", skill.getName());
            skillMap.put("version", skill.getVersion());
            skillMap.put("status", skill.getStatus());
            skillMap.put("sceneId", skill.getSceneId());
            skillMap.put("installPath", skill.getInstallPath());
            skillMap.put("installTime", skill.getInstallTime());
            skillMap.put("invokeCount", skill.getInvokeCount());
            skillMap.put("config", skill.getConfig());
            skillMap.put("dependencies", skill.getDependencies());

            result.setData(skillMap);
            result.setRequestStatus(200);
            result.setMessage("Success");
        } catch (Exception e) {
            log.error("Error getting skill detail", e);
            result.setRequestStatus(500);
            result.setMessage("Error: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/approve")
    @ResponseBody
    public ResultModel<Map<String, Object>> approveSkill(@RequestBody Map<String, String> request) {
        log.info("Approve skill requested: {}", request.get("id"));
        ResultModel<Map<String, Object>> result = new ResultModel<Map<String, Object>>();

        try {
            String skillId = request.get("id");
            InstalledSkill skill = adminSkillService.approveSkill(skillId).get();

            if (skill == null) {
                result.setRequestStatus(404);
                result.setMessage("Skill not found");
                return result;
            }

            Map<String, Object> skillMap = new HashMap<String, Object>();
            skillMap.put("id", skill.getSkillId());
            skillMap.put("status", skill.getStatus() != null ? skill.getStatus() : "APPROVED");

            result.setData(skillMap);
            result.setRequestStatus(200);
            result.setMessage("Skill approved successfully");
        } catch (Exception e) {
            log.error("Error approving skill", e);
            result.setRequestStatus(500);
            result.setMessage("Error: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/reject")
    @ResponseBody
    public ResultModel<Map<String, Object>> rejectSkill(@RequestBody Map<String, String> request) {
        log.info("Reject skill requested: {}", request.get("id"));
        ResultModel<Map<String, Object>> result = new ResultModel<Map<String, Object>>();

        try {
            String skillId = request.get("id");
            InstalledSkill skill = adminSkillService.rejectSkill(skillId).get();

            Map<String, Object> skillMap = new HashMap<String, Object>();
            skillMap.put("id", skillId);
            skillMap.put("status", "REJECTED");

            result.setData(skillMap);
            result.setRequestStatus(200);
            result.setMessage("Skill rejected successfully");
        } catch (Exception e) {
            log.error("Error rejecting skill", e);
            result.setRequestStatus(500);
            result.setMessage("Error: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/publish")
    @ResponseBody
    public ResultModel<Map<String, Object>> publishSkill(@RequestBody Map<String, Object> request) {
        log.info("Publish skill requested: {}", request.get("id"));
        ResultModel<Map<String, Object>> result = new ResultModel<Map<String, Object>>();

        try {
            String skillId = (String) request.get("id");
            @SuppressWarnings("unchecked")
            Map<String, String> config = (Map<String, String>) request.get("config");
            
            InstalledSkill skill = adminSkillService.publishSkill(skillId, config).get();

            Map<String, Object> skillMap = new HashMap<String, Object>();
            skillMap.put("id", skillId);
            skillMap.put("status", "PUBLISHED");

            result.setData(skillMap);
            result.setRequestStatus(200);
            result.setMessage("Skill published successfully");
        } catch (Exception e) {
            log.error("Error publishing skill", e);
            result.setRequestStatus(500);
            result.setMessage("Error: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/unpublish")
    @ResponseBody
    public ResultModel<Boolean> unpublishSkill(@RequestBody Map<String, String> request) {
        log.info("Unpublish skill requested: {}", request.get("id"));
        ResultModel<Boolean> result = new ResultModel<Boolean>();

        try {
            String skillId = request.get("id");
            boolean success = adminSkillService.unpublishSkill(skillId).get();

            result.setData(success);
            result.setRequestStatus(200);
            result.setMessage("Skill unpublished successfully");
        } catch (Exception e) {
            log.error("Error unpublishing skill", e);
            result.setRequestStatus(500);
            result.setMessage("Error: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/delete")
    @ResponseBody
    public ResultModel<Boolean> deleteSkill(@RequestBody Map<String, String> request) {
        log.info("Delete skill requested: {}", request.get("id"));
        ResultModel<Boolean> result = new ResultModel<Boolean>();

        try {
            String skillId = request.get("id");
            boolean success = adminSkillService.deleteSkill(skillId).get();

            result.setData(success);
            result.setRequestStatus(200);
            result.setMessage("Skill deleted successfully");
        } catch (Exception e) {
            log.error("Error deleting skill", e);
            result.setRequestStatus(500);
            result.setMessage("Error: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/pending")
    @ResponseBody
    public ListResultModel<List<Map<String, Object>>> getPendingSkills() {
        log.info("Get pending skills requested");
        ListResultModel<List<Map<String, Object>>> result = new ListResultModel<List<Map<String, Object>>>();

        try {
            List<InstalledSkill> skills = adminSkillService.getPendingSkills();
            List<Map<String, Object>> skillList = new ArrayList<Map<String, Object>>();

            for (InstalledSkill skill : skills) {
                Map<String, Object> skillMap = new HashMap<String, Object>();
                skillMap.put("id", skill.getSkillId());
                skillMap.put("name", skill.getName());
                skillMap.put("version", skill.getVersion());
                skillList.add(skillMap);
            }

            result.setData(skillList);
            result.setSize(skillList.size());
            result.setRequestStatus(200);
            result.setMessage("Success");
        } catch (Exception e) {
            log.error("Error getting pending skills", e);
            result.setRequestStatus(500);
            result.setMessage("Error: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/statistics")
    @ResponseBody
    public ResultModel<AdminSkillService.SkillStatistics> getStatistics() {
        log.info("Get skill statistics requested");
        ResultModel<AdminSkillService.SkillStatistics> result = new ResultModel<AdminSkillService.SkillStatistics>();

        try {
            AdminSkillService.SkillStatistics stats = adminSkillService.getStatistics();
            result.setData(stats);
            result.setRequestStatus(200);
            result.setMessage("Success");
        } catch (Exception e) {
            log.error("Error getting statistics", e);
            result.setRequestStatus(500);
            result.setMessage("Error: " + e.getMessage());
        }

        return result;
    }
}
