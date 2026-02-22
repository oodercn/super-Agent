package net.ooder.skillcenter.service;

import net.ooder.skillcenter.dto.SkillResultDTO;

import java.util.Map;

public interface ExecutionService {
    SkillResultDTO executeSkill(String skillId, Map<String, Object> parameters);
    String executeSkillAsync(String skillId, Map<String, Object> parameters);
    SkillResultDTO getExecutionResult(String executionId);
    String getExecutionStatus(String executionId);
    boolean clearExecutionResult(String executionId);
    Map<String, Object> getExecutionStats();
}
