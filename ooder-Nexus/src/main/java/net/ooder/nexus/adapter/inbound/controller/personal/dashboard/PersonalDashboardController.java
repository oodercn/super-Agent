package net.ooder.nexus.adapter.inbound.controller.personal.dashboard;

import net.ooder.config.ResultModel;
import net.ooder.sdk.api.skill.SkillPackageManager;
import net.ooder.sdk.api.skill.InstalledSkill;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/personal/dashboard")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS})
public class PersonalDashboardController {

    private static final Logger log = LoggerFactory.getLogger(PersonalDashboardController.class);

    private final SkillPackageManager skillPackageManager;

    @Autowired
    public PersonalDashboardController(@Autowired(required = false) SkillPackageManager skillPackageManager) {
        this.skillPackageManager = skillPackageManager;
    }

    @PostMapping("/overview")
    @ResponseBody
    public ResultModel<Map<String, Object>> getOverview() {
        log.info("Personal dashboard overview requested");
        ResultModel<Map<String, Object>> result = new ResultModel<Map<String, Object>>();

        try {
            Map<String, Object> overview = new HashMap<String, Object>();
            
            if (skillPackageManager != null) {
                List<InstalledSkill> installedSkills = skillPackageManager.listInstalled().get();
                overview.put("totalSkills", installedSkills.size());
                overview.put("runningSkills", countByStatus(installedSkills, "RUNNING"));
                overview.put("stoppedSkills", countByStatus(installedSkills, "STOPPED"));
            } else {
                overview.put("totalSkills", 0);
                overview.put("runningSkills", 0);
                overview.put("stoppedSkills", 0);
            }
            
            result.setData(overview);
            result.setRequestStatus(200);
            result.setMessage("Success");
        } catch (Exception e) {
            log.error("Error getting dashboard overview", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to get overview: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/skills/recent")
    @ResponseBody
    public ResultModel<List<Map<String, Object>>> getRecentSkills(@RequestBody(required = false) Map<String, Object> request) {
        log.info("Recent skills requested");
        ResultModel<List<Map<String, Object>>> result = new ResultModel<List<Map<String, Object>>>();

        try {
            int limit = 10;
            if (request != null && request.get("limit") != null) {
                limit = ((Number) request.get("limit")).intValue();
            }

            List<Map<String, Object>> recentSkills = new ArrayList<Map<String, Object>>();
            
            if (skillPackageManager != null) {
                List<InstalledSkill> skills = skillPackageManager.listInstalled().get();
                
                for (int i = 0; i < Math.min(limit, skills.size()); i++) {
                    InstalledSkill skill = skills.get(i);
                    Map<String, Object> skillMap = new HashMap<String, Object>();
                    skillMap.put("skillId", skill.getSkillId());
                    skillMap.put("name", skill.getName());
                    skillMap.put("version", skill.getVersion());
                    skillMap.put("status", skill.getStatus());
                    recentSkills.add(skillMap);
                }
            }

            result.setData(recentSkills);
            result.setRequestStatus(200);
            result.setMessage("Success");
        } catch (Exception e) {
            log.error("Error getting recent skills", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to get recent skills: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/skills/by-status")
    @ResponseBody
    public ResultModel<Map<String, List<Map<String, Object>>>> getSkillsByStatus() {
        log.info("Skills by status requested");
        ResultModel<Map<String, List<Map<String, Object>>>> result = new ResultModel<Map<String, List<Map<String, Object>>>>();

        try {
            Map<String, List<Map<String, Object>>> byStatus = new HashMap<String, List<Map<String, Object>>>();

            if (skillPackageManager != null) {
                List<InstalledSkill> skills = skillPackageManager.listInstalled().get();

                for (InstalledSkill skill : skills) {
                    String status = skill.getStatus();
                    if (status == null) status = "UNKNOWN";
                    
                    if (!byStatus.containsKey(status)) {
                        byStatus.put(status, new ArrayList<Map<String, Object>>());
                    }
                    
                    Map<String, Object> skillMap = new HashMap<String, Object>();
                    skillMap.put("skillId", skill.getSkillId());
                    skillMap.put("name", skill.getName());
                    skillMap.put("version", skill.getVersion());
                    byStatus.get(status).add(skillMap);
                }
            }

            result.setData(byStatus);
            result.setRequestStatus(200);
            result.setMessage("Success");
        } catch (Exception e) {
            log.error("Error getting skills by status", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to get skills by status: " + e.getMessage());
        }

        return result;
    }

    private int countByStatus(List<InstalledSkill> skills, String status) {
        int count = 0;
        for (InstalledSkill skill : skills) {
            if (status.equals(skill.getStatus())) {
                count++;
            }
        }
        return count;
    }
}
