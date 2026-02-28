package net.ooder.nexus.adapter.inbound.controller.skill;

import net.ooder.nexus.domain.skill.model.SkillConfig;
import net.ooder.nexus.dto.skill.*;
import net.ooder.nexus.model.ApiResponse;
import net.ooder.nexus.service.skill.SkillConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/skills/config")
public class SkillConfigController {

    private static final Logger log = LoggerFactory.getLogger(SkillConfigController.class);

    @Autowired
    private SkillConfigService skillConfigService;

    @GetMapping("/overview")
    public ApiResponse<SkillConfigOverviewDTO> getConfigOverview() {
        try {
            Map<String, Object> overview = skillConfigService.getConfigOverview();
            SkillConfigOverviewDTO dto = convertToOverviewDTO(overview);
            return ApiResponse.success(dto);
        } catch (Exception e) {
            log.error("Failed to get skill config overview", e);
            return ApiResponse.error("获取概览失败: " + e.getMessage());
        }
    }

    @GetMapping("/{skillId}")
    public ApiResponse<SkillConfig> getSkillConfig(@PathVariable String skillId) {
        try {
            SkillConfig config = skillConfigService.getSkillConfig(skillId);
            if (config != null) {
                return ApiResponse.success(config);
            } else {
                return ApiResponse.notFound("Skill不存在");
            }
        } catch (Exception e) {
            log.error("Failed to get skill config", e);
            return ApiResponse.error("获取配置失败: " + e.getMessage());
        }
    }

    @PostMapping("/{skillId}/update")
    public ApiResponse<SkillConfigUpdateResultDTO> updateSkillConfig(
            @PathVariable String skillId,
            @RequestBody SkillConfigUpdateRequestDTO request) {
        try {
            Map<String, Object> config = request.getConfig();
            boolean testConnection = Boolean.TRUE.equals(request.getTestConnection());

            SkillConfig updated = skillConfigService.updateSkillConfig(skillId, config, testConnection);
            if (updated != null) {
                SkillConfigUpdateResultDTO result = new SkillConfigUpdateResultDTO();
                result.setSkillId(skillId);
                result.setStatus(updated.getStatus());
                
                if (updated.getConnectionInfo() != null && testConnection) {
                    SkillConfigUpdateResultDTO.ConnectionTestResultDTO testResult = 
                        new SkillConfigUpdateResultDTO.ConnectionTestResultDTO();
                    testResult.setSuccess(updated.getConnectionInfo().isConnected());
                    testResult.setResponseTime(updated.getConnectionInfo().getResponseTime());
                    testResult.setMessage(updated.getConnectionInfo().getError() != null ? 
                        updated.getConnectionInfo().getError() : "连接成功");
                    result.setConnectionTest(testResult);
                }
                
                return ApiResponse.success("配置保存成功", result);
            } else {
                return ApiResponse.notFound("Skill不存在");
            }
        } catch (Exception e) {
            log.error("Failed to update skill config", e);
            return ApiResponse.error("更新配置失败: " + e.getMessage());
        }
    }

    @PostMapping("/{skillId}/test")
    public ApiResponse<SkillConnectionTestResultDTO> testSkillConnection(
            @PathVariable String skillId,
            @RequestBody SkillConnectionTestRequestDTO request) {
        try {
            Map<String, Object> config = request.getConfig();

            Map<String, Object> testResult = skillConfigService.testSkillConnection(skillId, config);
            SkillConnectionTestResultDTO dto = convertToTestResultDTO(testResult);
            return ApiResponse.success(dto);
        } catch (Exception e) {
            log.error("Failed to test skill connection", e);
            return ApiResponse.error("连接测试失败: " + e.getMessage());
        }
    }

    @PostMapping("/{skillId}/enable")
    public ApiResponse<Void> enableSkill(@PathVariable String skillId) {
        try {
            boolean enabled = skillConfigService.enableSkill(skillId);
            if (enabled) {
                return ApiResponse.success("Skill已启用");
            } else {
                return ApiResponse.badRequest("启用失败，请先配置Skill");
            }
        } catch (Exception e) {
            log.error("Failed to enable skill", e);
            return ApiResponse.error("启用失败: " + e.getMessage());
        }
    }

    @PostMapping("/{skillId}/disable")
    public ApiResponse<Void> disableSkill(@PathVariable String skillId) {
        try {
            boolean disabled = skillConfigService.disableSkill(skillId);
            if (disabled) {
                return ApiResponse.success("Skill已停用");
            } else {
                return ApiResponse.notFound("Skill不存在");
            }
        } catch (Exception e) {
            log.error("Failed to disable skill", e);
            return ApiResponse.error("停用失败: " + e.getMessage());
        }
    }

    private SkillConfigOverviewDTO convertToOverviewDTO(Map<String, Object> map) {
        SkillConfigOverviewDTO dto = new SkillConfigOverviewDTO();
        
        Object totalSkills = map.get("totalSkills");
        if (totalSkills instanceof Number) {
            dto.setTotalSkills(((Number) totalSkills).intValue());
        }
        
        Object configuredSkills = map.get("configuredSkills");
        if (configuredSkills instanceof Number) {
            dto.setConfiguredSkills(((Number) configuredSkills).intValue());
        }
        
        Object activeSkills = map.get("activeSkills");
        if (activeSkills instanceof Number) {
            dto.setActiveSkills(((Number) activeSkills).intValue());
        }
        
        return dto;
    }

    private SkillConnectionTestResultDTO convertToTestResultDTO(Map<String, Object> map) {
        SkillConnectionTestResultDTO dto = new SkillConnectionTestResultDTO();
        
        Object success = map.get("success");
        if (success instanceof Boolean) {
            dto.setSuccess((Boolean) success);
        }
        
        dto.setMessage((String) map.get("message"));
        
        Object responseTime = map.get("responseTime");
        if (responseTime instanceof Number) {
            dto.setResponseTime(((Number) responseTime).longValue());
        }
        
        dto.setError((String) map.get("error"));
        
        return dto;
    }
}
