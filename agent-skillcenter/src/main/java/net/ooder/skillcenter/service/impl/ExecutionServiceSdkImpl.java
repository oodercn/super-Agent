package net.ooder.skillcenter.service.impl;

import net.ooder.skillcenter.dto.SkillResultDTO;
import net.ooder.skillcenter.execution.SkillExecutorEngine;
import net.ooder.skillcenter.execution.SkillExecutionStats;
import net.ooder.skillcenter.manager.SkillManager;
import net.ooder.skillcenter.model.Skill;
import net.ooder.skillcenter.model.SkillContext;
import net.ooder.skillcenter.model.SkillResult;
import net.ooder.skillcenter.service.ExecutionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Service
@ConditionalOnProperty(name = "skillcenter.sdk.mode", havingValue = "sdk")
public class ExecutionServiceSdkImpl implements ExecutionService {

    private static final Logger log = LoggerFactory.getLogger(ExecutionServiceSdkImpl.class);

    private SkillManager skillManager;
    private SkillExecutorEngine executorEngine;
    private final Map<String, SkillResultDTO> executionResults = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        this.skillManager = SkillManager.getInstance();
        this.executorEngine = SkillExecutorEngine.getInstance();
        log.info("[ExecutionServiceSdkImpl] Initialized with SDK mode using SkillManager");
    }

    @Override
    public SkillResultDTO executeSkill(String skillId, Map<String, Object> parameters) {
        log.info("[ExecutionServiceSdkImpl] Executing skill: {}", skillId);

        String executionId = UUID.randomUUID().toString();
        SkillResultDTO resultDTO = new SkillResultDTO();
        resultDTO.setExecutionId(executionId);
        resultDTO.setSkillId(skillId);

        try {
            Skill skill = skillManager.getSkill(skillId);
            if (skill == null) {
                log.warn("[ExecutionServiceSdkImpl] Skill not found: {}", skillId);
                resultDTO.setStatus("failed");
                resultDTO.setOutput("Skill not found: " + skillId);
                resultDTO.setExecutedAt(new Date());
                executionResults.put(executionId, resultDTO);
                return resultDTO;
            }

            SkillContext context = new SkillContext();
            context.setParameters(parameters);
            context.setExecutionId(executionId);

            long startTime = System.currentTimeMillis();
            SkillResult result = executorEngine.executeSkill(skill, context);
            long executionTime = System.currentTimeMillis() - startTime;

            resultDTO.setStatus("success");
            resultDTO.setOutput(result.getMessage());
            resultDTO.setExecutedAt(new Date());
            resultDTO.setExecutionTime((int) executionTime);

            log.info("[ExecutionServiceSdkImpl] Skill {} executed successfully in {}ms", skillId, executionTime);

        } catch (Exception e) {
            log.error("[ExecutionServiceSdkImpl] Skill execution failed: {}", e.getMessage(), e);
            resultDTO.setStatus("failed");
            resultDTO.setOutput("Execution failed: " + e.getMessage());
            resultDTO.setExecutedAt(new Date());
        }

        executionResults.put(executionId, resultDTO);
        return resultDTO;
    }

    @Override
    public String executeSkillAsync(String skillId, Map<String, Object> parameters) {
        log.info("[ExecutionServiceSdkImpl] Async executing skill: {}", skillId);

        String executionId = UUID.randomUUID().toString();

        SkillResultDTO pendingResult = new SkillResultDTO();
        pendingResult.setExecutionId(executionId);
        pendingResult.setSkillId(skillId);
        pendingResult.setStatus("pending");
        pendingResult.setExecutedAt(new Date());
        executionResults.put(executionId, pendingResult);

        CompletableFuture.runAsync(() -> {
            try {
                Skill skill = skillManager.getSkill(skillId);
                if (skill == null) {
                    SkillResultDTO failResult = new SkillResultDTO();
                    failResult.setExecutionId(executionId);
                    failResult.setSkillId(skillId);
                    failResult.setStatus("failed");
                    failResult.setOutput("Skill not found: " + skillId);
                    failResult.setExecutedAt(new Date());
                    executionResults.put(executionId, failResult);
                    return;
                }

                SkillContext context = new SkillContext();
                context.setParameters(parameters);
                context.setExecutionId(executionId);

                long startTime = System.currentTimeMillis();
                SkillResult result = executorEngine.executeSkill(skill, context);
                long executionTime = System.currentTimeMillis() - startTime;

                SkillResultDTO successResult = new SkillResultDTO();
                successResult.setExecutionId(executionId);
                successResult.setSkillId(skillId);
                successResult.setStatus("success");
                successResult.setOutput(result.getMessage());
                successResult.setExecutedAt(new Date());
                successResult.setExecutionTime((int) executionTime);
                executionResults.put(executionId, successResult);

                log.info("[ExecutionServiceSdkImpl] Async skill {} executed in {}ms", skillId, executionTime);

            } catch (Exception e) {
                log.error("[ExecutionServiceSdkImpl] Async execution failed: {}", e.getMessage(), e);
                SkillResultDTO failResult = new SkillResultDTO();
                failResult.setExecutionId(executionId);
                failResult.setSkillId(skillId);
                failResult.setStatus("failed");
                failResult.setOutput("Execution failed: " + e.getMessage());
                failResult.setExecutedAt(new Date());
                executionResults.put(executionId, failResult);
            }
        });

        return executionId;
    }

    @Override
    public SkillResultDTO getExecutionResult(String executionId) {
        return executionResults.get(executionId);
    }

    @Override
    public String getExecutionStatus(String executionId) {
        SkillResultDTO result = executionResults.get(executionId);
        if (result == null) {
            return "NOT_FOUND";
        }
        return result.getStatus().toUpperCase();
    }

    @Override
    public boolean clearExecutionResult(String executionId) {
        return executionResults.remove(executionId) != null;
    }

    @Override
    public Map<String, Object> getExecutionStats() {
        SkillExecutionStats stats = executorEngine.getExecutionStats();
        Map<String, Object> result = new ConcurrentHashMap<>();
        result.put("totalExecutions", executionResults.size());
        result.put("successfulExecutions", executionResults.values().stream()
            .filter(r -> "success".equals(r.getStatus())).count());
        result.put("failedExecutions", executionResults.values().stream()
            .filter(r -> "failed".equals(r.getStatus())).count());
        result.put("pendingExecutions", executionResults.values().stream()
            .filter(r -> "pending".equals(r.getStatus())).count());
        return result;
    }
}
