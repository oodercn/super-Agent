package net.ooder.skillcenter.controller;

import net.ooder.skillcenter.execution.SkillExecutionMonitor;
import net.ooder.skillcenter.execution.SkillExecutionStats;
import net.ooder.skillcenter.execution.SkillExecutorEngine;
import net.ooder.skillcenter.manager.SkillManager;
import net.ooder.skillcenter.model.SkillContext;
import net.ooder.skillcenter.model.SkillException;
import net.ooder.skillcenter.model.SkillResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 技能执行REST API控制器
 */
@RestController
@RequestMapping("/api/execution")
public class ExecutionController {

    private final SkillManager skillManager;
    private final SkillExecutionMonitor executionMonitor;
    private final Map<String, SkillResult> executionResults;

    /**
     * 构造方法，初始化执行管理器
     */
    public ExecutionController() {
        this.skillManager = SkillManager.getInstance();
        this.executionMonitor = new SkillExecutionMonitor();
        this.executionResults = new ConcurrentHashMap<>();
    }

    /**
     * 获取执行统计信息
     * @return 执行统计信息
     */
    @GetMapping("/stats")
    public ResponseEntity<SkillExecutionStats> getExecutionStats() {
        SkillExecutionStats stats = executionMonitor.getStats();
        return ResponseEntity.ok(stats);
    }

    /**
     * 执行技能（同步）
     * @param skillId 技能ID
     * @param request 执行请求，包含参数和上下文信息
     * @return 执行结果
     */
    @PostMapping("/execute/{skillId}")
    public ResponseEntity<SkillResult> executeSkill(@PathVariable String skillId, @RequestBody ExecuteSkillRequest request) {
        String executionId = UUID.randomUUID().toString();
        
        try {
            // 通知监控器执行开始
            executionMonitor.onExecutionStart(executionId, skillId);
            
            // 构建执行上下文
            SkillContext context = new SkillContext();
            if (request.getParameters() != null) {
                for (Map.Entry<String, Object> entry : request.getParameters().entrySet()) {
                    context.addParameter(entry.getKey(), entry.getValue());
                }
            }
            
            // 执行技能
            long startTime = System.currentTimeMillis();
            SkillResult result = skillManager.executeSkill(skillId, context);
            long executionTime = System.currentTimeMillis() - startTime;
            
            // 保存执行结果
            executionResults.put(executionId, result);
            
            // 通知监控器执行成功
            executionMonitor.onExecutionSuccess(executionId, skillId, result);
            
            return ResponseEntity.ok(result);
        } catch (SkillException e) {
            SkillResult result = new SkillResult(SkillResult.Status.FAILED, e.getMessage());
            result.setException(e);
            executionResults.put(executionId, result);
            
            // 通知监控器执行失败
            executionMonitor.onExecutionFailure(executionId, skillId, e);
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    /**
     * 执行技能（异步）
     * @param skillId 技能ID
     * @param request 执行请求，包含参数和上下文信息
     * @return 执行ID
     */
    @PostMapping("/execute-async/{skillId}")
    public ResponseEntity<String> executeSkillAsync(@PathVariable String skillId, @RequestBody ExecuteSkillRequest request) {
        String executionId = UUID.randomUUID().toString();
        
        // 通知监控器执行开始
        executionMonitor.onExecutionStart(executionId, skillId);
        
        // 异步执行技能
        new Thread(() -> {
            try {
                // 构建执行上下文
                SkillContext context = new SkillContext();
                if (request.getParameters() != null) {
                    for (Map.Entry<String, Object> entry : request.getParameters().entrySet()) {
                        context.addParameter(entry.getKey(), entry.getValue());
                    }
                }
                
                // 执行技能
                SkillResult result = skillManager.executeSkill(skillId, context);
                executionResults.put(executionId, result);
                
                // 通知监控器执行成功
                executionMonitor.onExecutionSuccess(executionId, skillId, result);
            } catch (SkillException e) {
                SkillResult result = new SkillResult(SkillResult.Status.FAILED, e.getMessage());
                result.setException(e);
                executionResults.put(executionId, result);
                
                // 通知监控器执行失败
                executionMonitor.onExecutionFailure(executionId, skillId, e);
            }
        }).start();
        
        return ResponseEntity.ok(executionId);
    }

    /**
     * 获取执行结果
     * @param executionId 执行ID
     * @return 执行结果
     */
    @GetMapping("/result/{executionId}")
    public ResponseEntity<SkillResult> getExecutionResult(@PathVariable String executionId) {
        SkillResult result = executionResults.get(executionId);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(result);
    }

    /**
     * 获取执行状态
     * @param executionId 执行ID
     * @return 执行状态
     */
    @GetMapping("/status/{executionId}")
    public ResponseEntity<String> getExecutionStatus(@PathVariable String executionId) {
        SkillResult result = executionResults.get(executionId);
        if (result == null) {
            return ResponseEntity.ok("PENDING");
        } else if (result.getStatus() == SkillResult.Status.SUCCESS) {
            return ResponseEntity.ok("SUCCESS");
        } else {
            return ResponseEntity.ok("FAILED");
        }
    }

    /**
     * 清理执行结果
     * @param executionId 执行ID
     * @return 清理结果
     */
    @DeleteMapping("/result/{executionId}")
    public ResponseEntity<Boolean> clearExecutionResult(@PathVariable String executionId) {
        boolean result = executionResults.remove(executionId) != null;
        return ResponseEntity.ok(result);
    }

    /**
     * 获取执行历史
     * @return 执行历史列表
     */
    @GetMapping("/history")
    public ResponseEntity<Map<String, SkillResult>> getExecutionHistory() {
        return ResponseEntity.ok(executionResults);
    }

    /**
     * 执行技能请求体
     */
    static class ExecuteSkillRequest {
        private Map<String, Object> parameters;
        private Map<String, Object> attributes;

        // Getters and setters
        public Map<String, Object> getParameters() {
            return parameters;
        }

        public void setParameters(Map<String, Object> parameters) {
            this.parameters = parameters;
        }

        public Map<String, Object> getAttributes() {
            return attributes;
        }

        public void setAttributes(Map<String, Object> attributes) {
            this.attributes = attributes;
        }
    }
}
