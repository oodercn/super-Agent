package net.ooder.nexus.adapter.inbound.controller.scheduler;

import net.ooder.config.ListResultModel;
import net.ooder.config.ResultModel;
import net.ooder.nexus.provider.NexusSchedulerProvider;
import net.ooder.scene.skill.SchedulerProvider.TaskInfo;
import net.ooder.scene.skill.SchedulerProvider.TaskExecution;
import net.ooder.scene.skill.SchedulerProvider.TaskListResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 任务调度控制器
 *
 * <p>提供任务调度 API，委托给 SchedulerProvider 实现：</p>
 * <ul>
 *   <li>任务调度</li>
 *   <li>任务管理</li>
 *   <li>执行历史</li>
 * </ul>
 *
 * @author ooder Team
 * @version 0.7.3
 * @since SDK 0.7.3
 */
@RestController
@RequestMapping(value = "/api/scheduler", produces = "application/json;charset=UTF-8")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class SchedulerController {

    private static final Logger log = LoggerFactory.getLogger(SchedulerController.class);

    @Autowired(required = false)
    private NexusSchedulerProvider schedulerProvider;

    /**
     * 调度任务
     */
    @PostMapping("/schedule")
    @ResponseBody
    public ResultModel<Map<String, Object>> schedule(@RequestBody Map<String, Object> request) {
        String taskName = (String) request.get("taskName");
        String cronExpression = (String) request.get("cronExpression");
        @SuppressWarnings("unchecked")
        Map<String, Object> taskData = (Map<String, Object>) request.get("taskData");
        @SuppressWarnings("unchecked")
        Map<String, Object> options = (Map<String, Object>) request.get("options");

        log.info("Schedule task: {}, cron: {}", taskName, cronExpression);
        ResultModel<Map<String, Object>> result = new ResultModel<>();
        try {
            if (schedulerProvider != null) {
                String taskId = schedulerProvider.schedule(taskName, cronExpression, taskData, options);
                Map<String, Object> data = new HashMap<String, Object>();
                data.put("taskId", taskId);
                data.put("taskName", taskName);
                data.put("cronExpression", cronExpression);
                data.put("status", "running");
                data.put("createdAt", System.currentTimeMillis());
                result.setData(data);
                result.setRequestStatus(200);
                result.setMessage("调度成功");
            } else {
                Map<String, Object> data = new HashMap<String, Object>();
                data.put("taskId", "task-" + System.currentTimeMillis());
                data.put("taskName", taskName);
                data.put("cronExpression", cronExpression);
                data.put("status", "running");
                data.put("createdAt", System.currentTimeMillis());
                result.setData(data);
                result.setRequestStatus(200);
                result.setMessage("调度成功(模拟)");
            }
        } catch (Exception e) {
            log.error("Error scheduling task", e);
            result.setRequestStatus(500);
            result.setMessage("调度任务失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 取消任务
     */
    @DeleteMapping("/{taskId}")
    @ResponseBody
    public ResultModel<Boolean> cancel(@PathVariable String taskId) {
        log.info("Cancel task: {}", taskId);
        ResultModel<Boolean> result = new ResultModel<>();
        try {
            if (schedulerProvider != null) {
                boolean success = schedulerProvider.cancel(taskId);
                result.setData(success);
                result.setRequestStatus(success ? 200 : 404);
                result.setMessage(success ? "取消成功" : "任务不存在");
            } else {
                result.setData(true);
                result.setRequestStatus(200);
                result.setMessage("取消成功(模拟)");
            }
        } catch (Exception e) {
            log.error("Error canceling task", e);
            result.setRequestStatus(500);
            result.setMessage("取消任务失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 暂停任务
     */
    @PostMapping("/{taskId}/pause")
    @ResponseBody
    public ResultModel<Boolean> pause(@PathVariable String taskId) {
        log.info("Pause task: {}", taskId);
        ResultModel<Boolean> result = new ResultModel<>();
        try {
            if (schedulerProvider != null) {
                boolean success = schedulerProvider.pause(taskId);
                result.setData(success);
                result.setRequestStatus(success ? 200 : 404);
                result.setMessage(success ? "暂停成功" : "任务不存在");
            } else {
                result.setData(true);
                result.setRequestStatus(200);
                result.setMessage("暂停成功(模拟)");
            }
        } catch (Exception e) {
            log.error("Error pausing task", e);
            result.setRequestStatus(500);
            result.setMessage("暂停任务失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 恢复任务
     */
    @PostMapping("/{taskId}/resume")
    @ResponseBody
    public ResultModel<Boolean> resume(@PathVariable String taskId) {
        log.info("Resume task: {}", taskId);
        ResultModel<Boolean> result = new ResultModel<>();
        try {
            if (schedulerProvider != null) {
                boolean success = schedulerProvider.resume(taskId);
                result.setData(success);
                result.setRequestStatus(success ? 200 : 404);
                result.setMessage(success ? "恢复成功" : "任务不存在");
            } else {
                result.setData(true);
                result.setRequestStatus(200);
                result.setMessage("恢复成功(模拟)");
            }
        } catch (Exception e) {
            log.error("Error resuming task", e);
            result.setRequestStatus(500);
            result.setMessage("恢复任务失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 获取任务列表
     */
    @GetMapping("")
    @ResponseBody
    public ListResultModel<List<Map<String, Object>>> listTasks(
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize) {
        log.info("List tasks: status={}, page={}, size={}", status, page, pageSize);
        ListResultModel<List<Map<String, Object>>> result = new ListResultModel<>();
        try {
            if (schedulerProvider != null) {
                TaskListResult taskResult = schedulerProvider.listTasks(status, page, pageSize);
                List<Map<String, Object>> tasks = new ArrayList<Map<String, Object>>();
                for (TaskInfo task : taskResult.getTasks()) {
                    tasks.add(convertTaskToMap(task));
                }
                result.setData(tasks);
                result.setSize(taskResult.getTotal());
                result.setRequestStatus(200);
                result.setMessage("获取成功");
            } else {
                List<Map<String, Object>> mockTasks = new ArrayList<Map<String, Object>>();
                Map<String, Object> task1 = new HashMap<String, Object>();
                task1.put("taskId", "task-1");
                task1.put("taskName", "系统健康检查");
                task1.put("cronExpression", "0 */5 * * * ?");
                task1.put("status", "running");
                mockTasks.add(task1);
                result.setData(mockTasks);
                result.setSize(1);
                result.setRequestStatus(200);
                result.setMessage("获取成功(模拟)");
            }
        } catch (Exception e) {
            log.error("Error listing tasks", e);
            result.setRequestStatus(500);
            result.setMessage("获取任务列表失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 获取任务详情
     */
    @GetMapping("/{taskId}")
    @ResponseBody
    public ResultModel<Map<String, Object>> getTask(@PathVariable String taskId) {
        log.info("Get task: {}", taskId);
        ResultModel<Map<String, Object>> result = new ResultModel<>();
        try {
            if (schedulerProvider != null) {
                TaskInfo task = schedulerProvider.getTask(taskId);
                if (task != null) {
                    result.setData(convertTaskToMap(task));
                    result.setRequestStatus(200);
                    result.setMessage("获取成功");
                } else {
                    result.setRequestStatus(404);
                    result.setMessage("任务不存在");
                }
            } else {
                Map<String, Object> data = new HashMap<String, Object>();
                data.put("taskId", taskId);
                data.put("taskName", "模拟任务");
                data.put("status", "running");
                result.setData(data);
                result.setRequestStatus(200);
                result.setMessage("获取成功(模拟)");
            }
        } catch (Exception e) {
            log.error("Error getting task", e);
            result.setRequestStatus(500);
            result.setMessage("获取任务失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 立即执行任务
     */
    @PostMapping("/{taskId}/trigger")
    @ResponseBody
    public ResultModel<Boolean> triggerNow(@PathVariable String taskId) {
        log.info("Trigger task: {}", taskId);
        ResultModel<Boolean> result = new ResultModel<>();
        try {
            if (schedulerProvider != null) {
                boolean success = schedulerProvider.triggerNow(taskId);
                result.setData(success);
                result.setRequestStatus(success ? 200 : 404);
                result.setMessage(success ? "触发成功" : "任务不存在");
            } else {
                result.setData(true);
                result.setRequestStatus(200);
                result.setMessage("触发成功(模拟)");
            }
        } catch (Exception e) {
            log.error("Error triggering task", e);
            result.setRequestStatus(500);
            result.setMessage("触发任务失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 更新任务 Cron 表达式
     */
    @PutMapping("/{taskId}/cron")
    @ResponseBody
    public ResultModel<Boolean> updateCron(@PathVariable String taskId,
                                           @RequestBody Map<String, String> request) {
        String cronExpression = request.get("cronExpression");
        log.info("Update task cron: {}, new cron: {}", taskId, cronExpression);
        ResultModel<Boolean> result = new ResultModel<>();
        try {
            if (schedulerProvider != null) {
                boolean success = schedulerProvider.updateCron(taskId, cronExpression);
                result.setData(success);
                result.setRequestStatus(success ? 200 : 404);
                result.setMessage(success ? "更新成功" : "任务不存在");
            } else {
                result.setData(true);
                result.setRequestStatus(200);
                result.setMessage("更新成功(模拟)");
            }
        } catch (Exception e) {
            log.error("Error updating task cron", e);
            result.setRequestStatus(500);
            result.setMessage("更新Cron失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 获取任务执行历史
     */
    @GetMapping("/{taskId}/history")
    @ResponseBody
    public ListResultModel<List<Map<String, Object>>> getExecutionHistory(
            @PathVariable String taskId,
            @RequestParam(defaultValue = "20") int limit) {
        log.info("Get execution history: taskId={}, limit={}", taskId, limit);
        ListResultModel<List<Map<String, Object>>> result = new ListResultModel<>();
        try {
            if (schedulerProvider != null) {
                List<TaskExecution> history = schedulerProvider.getExecutionHistory(taskId, limit);
                List<Map<String, Object>> executions = new ArrayList<Map<String, Object>>();
                for (TaskExecution exec : history) {
                    executions.add(convertExecutionToMap(exec));
                }
                result.setData(executions);
                result.setSize(executions.size());
                result.setRequestStatus(200);
                result.setMessage("获取成功");
            } else {
                List<Map<String, Object>> mockHistory = new ArrayList<Map<String, Object>>();
                Map<String, Object> exec = new HashMap<String, Object>();
                exec.put("executionId", "exec-1");
                exec.put("taskId", taskId);
                exec.put("startTime", System.currentTimeMillis() - 3600000);
                exec.put("endTime", System.currentTimeMillis() - 3599000);
                exec.put("status", "success");
                mockHistory.add(exec);
                result.setData(mockHistory);
                result.setSize(1);
                result.setRequestStatus(200);
                result.setMessage("获取成功(模拟)");
            }
        } catch (Exception e) {
            log.error("Error getting execution history", e);
            result.setRequestStatus(500);
            result.setMessage("获取执行历史失败: " + e.getMessage());
        }
        return result;
    }

    private Map<String, Object> convertTaskToMap(TaskInfo task) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("taskId", task.getTaskId());
        map.put("taskName", task.getTaskName());
        map.put("cronExpression", task.getCronExpression());
        map.put("status", task.getStatus());
        map.put("lastExecutionTime", task.getLastExecutionTime());
        map.put("nextExecutionTime", task.getNextExecutionTime());
        map.put("executionCount", task.getExecutionCount());
        map.put("taskData", task.getTaskData());
        return map;
    }

    private Map<String, Object> convertExecutionToMap(TaskExecution exec) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("executionId", exec.getExecutionId());
        map.put("taskId", exec.getTaskId());
        map.put("startTime", exec.getStartTime());
        map.put("endTime", exec.getEndTime());
        map.put("status", exec.getStatus());
        map.put("result", exec.getResult());
        map.put("errorMessage", exec.getErrorMessage());
        return map;
    }
}
