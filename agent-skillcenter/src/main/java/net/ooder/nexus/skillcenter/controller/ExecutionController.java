package net.ooder.nexus.skillcenter.controller;

import net.ooder.skillcenter.execution.SkillExecutionMonitor;
import net.ooder.skillcenter.execution.SkillExecutionStats;
import net.ooder.skillcenter.manager.SkillManager;
import net.ooder.skillcenter.model.SkillContext;
import net.ooder.skillcenter.model.SkillException;
import net.ooder.skillcenter.model.SkillResult;
import net.ooder.nexus.skillcenter.model.ResultModel;
import net.ooder.nexus.skillcenter.dto.execution.ExecuteSkillRequestDTO;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/api/execution")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS})
public class ExecutionController extends BaseController {

    private final SkillManager skillManager;
    private final SkillExecutionMonitor executionMonitor;
    private final Map<String, SkillResult> executionResults;
    private final Map<String, Future<?>> executionFutures;
    private final Map<String, String> executionStatus;
    private final ExecutorService executorService;

    public ExecutionController() {
        this.skillManager = SkillManager.getInstance();
        this.executionMonitor = new SkillExecutionMonitor();
        this.executionResults = new ConcurrentHashMap<>();
        this.executionFutures = new ConcurrentHashMap<>();
        this.executionStatus = new ConcurrentHashMap<>();
        this.executorService = Executors.newFixedThreadPool(10, new SkillExecutionThreadFactory());
    }

    private static class SkillExecutionThreadFactory implements ThreadFactory {
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private static final String NAME_PREFIX = "skill-exec-";

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, NAME_PREFIX + threadNumber.getAndIncrement());
            t.setDaemon(true);
            return t;
        }
    }

    @PostMapping("/stats")
    public ResultModel<SkillExecutionStats> getExecutionStats() {
        long startTime = System.currentTimeMillis();
        logRequestStart("getExecutionStats", null);

        try {
            SkillExecutionStats stats = executionMonitor.getStats();
            logRequestEnd("getExecutionStats", stats, System.currentTimeMillis() - startTime);
            return ResultModel.success(stats);
        } catch (Exception e) {
            logRequestError("getExecutionStats", e);
            return ResultModel.error(500, "获取执行统计信息失败: " + e.getMessage());
        }
    }

    @PostMapping("/execute/{skillId}")
    public ResultModel<SkillResult> executeSkill(@PathVariable String skillId, @RequestBody ExecuteSkillRequestDTO request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("executeSkill", request);

        String executionId = UUID.randomUUID().toString();

        try {
            executionMonitor.onExecutionStart(executionId, skillId);

            SkillContext context = new SkillContext();
            if (request.getParameters() != null) {
                for (Map.Entry<String, Object> entry : request.getParameters().entrySet()) {
                    context.addParameter(entry.getKey(), entry.getValue());
                }
            }

            SkillResult result = skillManager.executeSkill(skillId, context);

            executionResults.put(executionId, result);

            executionMonitor.onExecutionSuccess(executionId, skillId, result);

            logRequestEnd("executeSkill", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (SkillException e) {
            SkillResult result = new SkillResult(SkillResult.Status.FAILED, e.getMessage());
            result.setException(e);
            executionResults.put(executionId, result);

            if (e instanceof net.ooder.skillcenter.model.SkillException) {
                executionMonitor.onExecutionFailure(executionId, skillId, (net.ooder.skillcenter.model.SkillException) e);
            } else {
                executionMonitor.onExecutionFailure(executionId, skillId, new net.ooder.skillcenter.model.SkillException(skillId, e.getMessage(), net.ooder.skillcenter.model.SkillException.ErrorCode.EXECUTION_EXCEPTION, e));
            }

            logRequestError("executeSkill", e);
            return ResultModel.error(500, e.getMessage());
        } catch (Exception e) {
            SkillResult result = new SkillResult(SkillResult.Status.FAILED, e.getMessage());
            result.setException(e);
            executionResults.put(executionId, result);

            if (e instanceof net.ooder.skillcenter.model.SkillException) {
                executionMonitor.onExecutionFailure(executionId, skillId, (net.ooder.skillcenter.model.SkillException) e);
            } else {
                executionMonitor.onExecutionFailure(executionId, skillId, new net.ooder.skillcenter.model.SkillException(skillId, e.getMessage(), net.ooder.skillcenter.model.SkillException.ErrorCode.EXECUTION_EXCEPTION, e));
            }

            logRequestError("executeSkill", e);
            return ResultModel.error(500, "执行技能失败: " + e.getMessage());
        }
    }

    @PostMapping("/execute-async/{skillId}")
    public ResultModel<String> executeSkillAsync(@PathVariable String skillId, @RequestBody ExecuteSkillRequestDTO request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("executeSkillAsync", request);

        String executionId = UUID.randomUUID().toString();

        executionMonitor.onExecutionStart(executionId, skillId);
        executionStatus.put(executionId, "RUNNING");

        Future<?> future = executorService.submit(() -> {
            try {
                SkillContext context = new SkillContext();
                if (request.getParameters() != null) {
                    for (Map.Entry<String, Object> entry : request.getParameters().entrySet()) {
                        context.addParameter(entry.getKey(), entry.getValue());
                    }
                }

                if ("CANCELLED".equals(executionStatus.get(executionId))) {
                    SkillResult result = new SkillResult(SkillResult.Status.FAILED, "Execution cancelled");
                    executionResults.put(executionId, result);
                    return;
                }

                SkillResult result = skillManager.executeSkill(skillId, context);
                executionResults.put(executionId, result);
                executionStatus.put(executionId, "SUCCESS");

                executionMonitor.onExecutionSuccess(executionId, skillId, result);
            } catch (SkillException e) {
                SkillResult result = new SkillResult(SkillResult.Status.FAILED, e.getMessage());
                result.setException(e);
                executionResults.put(executionId, result);
                executionStatus.put(executionId, "FAILED");

                executionMonitor.onExecutionFailure(executionId, skillId, e);
            } catch (Exception e) {
                SkillResult result = new SkillResult(SkillResult.Status.FAILED, e.getMessage());
                result.setException(e);
                executionResults.put(executionId, result);
                executionStatus.put(executionId, "FAILED");

                if (e instanceof net.ooder.skillcenter.model.SkillException) {
                    executionMonitor.onExecutionFailure(executionId, skillId, (net.ooder.skillcenter.model.SkillException) e);
                } else {
                    executionMonitor.onExecutionFailure(executionId, skillId, new net.ooder.skillcenter.model.SkillException(skillId, e.getMessage(), net.ooder.skillcenter.model.SkillException.ErrorCode.EXECUTION_EXCEPTION, e));
                }
            }
        });

        executionFutures.put(executionId, future);

        logRequestEnd("executeSkillAsync", executionId, System.currentTimeMillis() - startTime);
        return ResultModel.success(executionId);
    }

    @PostMapping("/result/{executionId}")
    public ResultModel<SkillResult> getExecutionResult(@PathVariable String executionId) {
        long startTime = System.currentTimeMillis();
        logRequestStart("getExecutionResult", executionId);

        try {
            SkillResult result = executionResults.get(executionId);
            if (result == null) {
                logRequestEnd("getExecutionResult", "Not found", System.currentTimeMillis() - startTime);
                return ResultModel.notFound("Execution result not found");
            }
            logRequestEnd("getExecutionResult", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("getExecutionResult", e);
            return ResultModel.error(500, "获取执行结果失败: " + e.getMessage());
        }
    }

    @PostMapping("/status/{executionId}")
    public ResultModel<String> getExecutionStatus(@PathVariable String executionId) {
        long startTime = System.currentTimeMillis();
        logRequestStart("getExecutionStatus", executionId);

        try {
            String status = executionStatus.get(executionId);
            if (status == null) {
                status = "UNKNOWN";
            }
            logRequestEnd("getExecutionStatus", status, System.currentTimeMillis() - startTime);
            return ResultModel.success(status);
        } catch (Exception e) {
            logRequestError("getExecutionStatus", e);
            return ResultModel.error(500, "获取执行状态失败: " + e.getMessage());
        }
    }

    @PostMapping("/cancel/{executionId}")
    public ResultModel<Boolean> cancelExecution(@PathVariable String executionId) {
        long startTime = System.currentTimeMillis();
        logRequestStart("cancelExecution", executionId);

        try {
            String status = executionStatus.get(executionId);
            if (status == null) {
                return ResultModel.notFound("执行任务不存在");
            }

            if ("SUCCESS".equals(status) || "FAILED".equals(status) || "CANCELLED".equals(status)) {
                return ResultModel.error(400, "执行已完成，无法取消");
            }

            Future<?> future = executionFutures.get(executionId);
            if (future != null && !future.isDone()) {
                future.cancel(true);
            }

            executionStatus.put(executionId, "CANCELLED");
            
            SkillResult result = new SkillResult(SkillResult.Status.FAILED, "Execution cancelled by user");
            executionResults.put(executionId, result);

            logRequestEnd("cancelExecution", true, System.currentTimeMillis() - startTime);
            return ResultModel.success("执行已取消", true);
        } catch (Exception e) {
            logRequestError("cancelExecution", e);
            return ResultModel.error(500, "取消执行失败: " + e.getMessage());
        }
    }

    @PostMapping("/list-running")
    public ResultModel<List<Map<String, Object>>> listRunningExecutions() {
        long startTime = System.currentTimeMillis();
        logRequestStart("listRunningExecutions", null);

        try {
            List<Map<String, Object>> runningList = new ArrayList<>();
            
            for (Map.Entry<String, String> entry : executionStatus.entrySet()) {
                if ("RUNNING".equals(entry.getValue())) {
                    Map<String, Object> info = new HashMap<>();
                    info.put("executionId", entry.getKey());
                    info.put("status", entry.getValue());
                    runningList.add(info);
                }
            }

            logRequestEnd("listRunningExecutions", runningList.size() + " running", System.currentTimeMillis() - startTime);
            return ResultModel.success(runningList);
        } catch (Exception e) {
            logRequestError("listRunningExecutions", e);
            return ResultModel.error(500, "获取运行中执行列表失败: " + e.getMessage());
        }
    }

    @PostMapping("/clear/{executionId}")
    public ResultModel<Boolean> clearExecutionResult(@PathVariable String executionId) {
        long startTime = System.currentTimeMillis();
        logRequestStart("clearExecutionResult", executionId);

        try {
            boolean result = executionResults.remove(executionId) != null;
            logRequestEnd("clearExecutionResult", result, System.currentTimeMillis() - startTime);
            return ResultModel.success("清理执行结果成功", result);
        } catch (Exception e) {
            logRequestError("clearExecutionResult", e);
            return ResultModel.error(500, "清理执行结果失败: " + e.getMessage());
        }
    }

    @PostMapping("/history")
    public ResultModel<Map<String, SkillResult>> getExecutionHistory() {
        long startTime = System.currentTimeMillis();
        logRequestStart("getExecutionHistory", null);

        try {
            logRequestEnd("getExecutionHistory", executionResults.size() + " results", System.currentTimeMillis() - startTime);
            return ResultModel.success(executionResults);
        } catch (Exception e) {
            logRequestError("getExecutionHistory", e);
            return ResultModel.error(500, "获取执行历史失败: " + e.getMessage());
        }
    }
}
