package net.ooder.nexus.adapter.inbound.controller.skill;

import net.ooder.nexus.domain.skill.model.DatabaseConnection;
import net.ooder.nexus.domain.skill.model.SkillConfig;
import net.ooder.nexus.service.skill.SkillConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/skills/config")
public class SkillConfigController {

    private static final Logger log = LoggerFactory.getLogger(SkillConfigController.class);

    @Autowired
    private SkillConfigService skillConfigService;

    @GetMapping("/overview")
    public Map<String, Object> getConfigOverview() {
        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, Object> overview = skillConfigService.getConfigOverview();
            result.put("requestStatus", 200);
            result.put("data", overview);
        } catch (Exception e) {
            log.error("Failed to get skill config overview", e);
            result.put("requestStatus", 500);
            result.put("message", "获取概览失败: " + e.getMessage());
        }
        return result;
    }

    @GetMapping("/{skillId}")
    public Map<String, Object> getSkillConfig(@PathVariable String skillId) {
        Map<String, Object> result = new HashMap<>();
        try {
            SkillConfig config = skillConfigService.getSkillConfig(skillId);
            if (config != null) {
                result.put("requestStatus", 200);
                result.put("data", config);
            } else {
                result.put("requestStatus", 404);
                result.put("message", "Skill不存在");
            }
        } catch (Exception e) {
            log.error("Failed to get skill config", e);
            result.put("requestStatus", 500);
            result.put("message", "获取配置失败: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/{skillId}/update")
    public Map<String, Object> updateSkillConfig(
            @PathVariable String skillId,
            @RequestBody Map<String, Object> request) {
        Map<String, Object> result = new HashMap<>();
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> config = (Map<String, Object>) request.get("config");
            boolean testConnection = Boolean.TRUE.equals(request.get("testConnection"));

            SkillConfig updated = skillConfigService.updateSkillConfig(skillId, config, testConnection);
            if (updated != null) {
                result.put("requestStatus", 200);
                result.put("message", "配置保存成功");
                
                Map<String, Object> data = new HashMap<>();
                data.put("skillId", skillId);
                data.put("status", updated.getStatus());
                
                if (updated.getConnectionInfo() != null) {
                    Map<String, Object> testResult = new HashMap<>();
                    testResult.put("success", updated.getConnectionInfo().isConnected());
                    testResult.put("responseTime", updated.getConnectionInfo().getResponseTime());
                    testResult.put("message", updated.getConnectionInfo().getError() != null ? 
                        updated.getConnectionInfo().getError() : "连接成功");
                    data.put("connectionTest", testResult);
                }
                
                result.put("data", data);
            } else {
                result.put("requestStatus", 404);
                result.put("message", "Skill不存在");
            }
        } catch (Exception e) {
            log.error("Failed to update skill config", e);
            result.put("requestStatus", 500);
            result.put("message", "更新配置失败: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/{skillId}/test")
    public Map<String, Object> testSkillConnection(
            @PathVariable String skillId,
            @RequestBody Map<String, Object> request) {
        Map<String, Object> result = new HashMap<>();
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> config = (Map<String, Object>) request.get("config");

            Map<String, Object> testResult = skillConfigService.testSkillConnection(skillId, config);
            result.put("requestStatus", 200);
            result.put("data", testResult);
        } catch (Exception e) {
            log.error("Failed to test skill connection", e);
            result.put("requestStatus", 500);
            result.put("message", "连接测试失败: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/{skillId}/enable")
    public Map<String, Object> enableSkill(@PathVariable String skillId) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean enabled = skillConfigService.enableSkill(skillId);
            if (enabled) {
                result.put("requestStatus", 200);
                result.put("message", "Skill已启用");
            } else {
                result.put("requestStatus", 400);
                result.put("message", "启用失败，请先配置Skill");
            }
        } catch (Exception e) {
            log.error("Failed to enable skill", e);
            result.put("requestStatus", 500);
            result.put("message", "启用失败: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/{skillId}/disable")
    public Map<String, Object> disableSkill(@PathVariable String skillId) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean disabled = skillConfigService.disableSkill(skillId);
            if (disabled) {
                result.put("requestStatus", 200);
                result.put("message", "Skill已停用");
            } else {
                result.put("requestStatus", 404);
                result.put("message", "Skill不存在");
            }
        } catch (Exception e) {
            log.error("Failed to disable skill", e);
            result.put("requestStatus", 500);
            result.put("message", "停用失败: " + e.getMessage());
        }
        return result;
    }
}
