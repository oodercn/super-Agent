package net.ooder.nexus.skillcenter.controller;

import net.ooder.nexus.skillcenter.dto.task.*;
import net.ooder.nexus.skillcenter.model.ResultModel;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/task")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS})
public class TaskController extends BaseController {

    private final Map<String, TaskDTO> taskMap;
    private final Map<String, TaskResultDTO> resultMap;
    private final Map<String, List<TaskDTO>> groupTaskMap;

    public TaskController() {
        this.taskMap = new ConcurrentHashMap<>();
        this.resultMap = new ConcurrentHashMap<>();
        this.groupTaskMap = new ConcurrentHashMap<>();
    }

    @PostMapping("/create")
    public ResultModel<TaskDTO> createTask(@RequestBody TaskDTO request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("createTask", request);

        try {
            if (request.getGroupId() == null || request.getGroupId().isEmpty()) {
                return ResultModel.badRequest("场景组ID不能为空");
            }

            String taskId = "task-" + UUID.randomUUID().toString().substring(0, 8);
            
            TaskDTO task = new TaskDTO();
            task.setTaskId(taskId);
            task.setGroupId(request.getGroupId());
            task.setSceneId(request.getSceneId());
            task.setTaskType(request.getTaskType() != null ? request.getTaskType() : "execution");
            task.setTaskName(request.getTaskName() != null ? request.getTaskName() : "未命名任务");
            task.setDescription(request.getDescription());
            task.setParameters(request.getParameters());
            task.setPriority(request.getPriority() > 0 ? request.getPriority() : 5);
            task.setCreatedAt(System.currentTimeMillis());
            task.setDeadline(request.getDeadline());
            task.setStatus("pending");
            task.setCreatedBy(request.getCreatedBy());
            task.setRequiredCapabilities(request.getRequiredCapabilities());
            task.setMetadata(request.getMetadata());

            taskMap.put(taskId, task);
            
            groupTaskMap.computeIfAbsent(task.getGroupId(), k -> new ArrayList<>()).add(task);

            logRequestEnd("createTask", taskId, System.currentTimeMillis() - startTime);
            return ResultModel.success("任务创建成功", task);
        } catch (Exception e) {
            logRequestError("createTask", e);
            return ResultModel.error(500, "创建任务失败: " + e.getMessage());
        }
    }

    @PostMapping("/assign")
    public ResultModel<TaskDTO> assignTask(@RequestBody TaskAssignmentDTO request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("assignTask", request);

        try {
            String taskId = request.getTaskId();
            if (taskId == null || taskId.isEmpty()) {
                return ResultModel.badRequest("任务ID不能为空");
            }

            TaskDTO task = taskMap.get(taskId);
            if (task == null) {
                return ResultModel.notFound("任务不存在");
            }

            task.setAssignedTo(request.getAssignedTo());
            task.setAssignedRole(request.getAssignedRole());
            task.setStatus("assigned");

            logRequestEnd("assignTask", taskId, System.currentTimeMillis() - startTime);
            return ResultModel.success("任务分配成功", task);
        } catch (Exception e) {
            logRequestError("assignTask", e);
            return ResultModel.error(500, "分配任务失败: " + e.getMessage());
        }
    }

    @PostMapping("/auto-assign")
    public ResultModel<List<TaskDTO>> autoAssignTasks(@RequestBody Map<String, String> request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("autoAssignTasks", request);

        try {
            String groupId = request.get("groupId");
            if (groupId == null || groupId.isEmpty()) {
                return ResultModel.badRequest("场景组ID不能为空");
            }

            List<TaskDTO> groupTasks = groupTaskMap.getOrDefault(groupId, new ArrayList<>());
            List<TaskDTO> pendingTasks = groupTasks.stream()
                    .filter(t -> "pending".equals(t.getStatus()))
                    .collect(Collectors.toList());

            List<String> members = getGroupMembers(groupId);
            int memberIndex = 0;

            for (TaskDTO task : pendingTasks) {
                if (!members.isEmpty()) {
                    task.setAssignedTo(members.get(memberIndex % members.size()));
                    task.setAssignedRole("MEMBER");
                    task.setStatus("assigned");
                    memberIndex++;
                }
            }

            logRequestEnd("autoAssignTasks", pendingTasks.size() + " tasks assigned", System.currentTimeMillis() - startTime);
            return ResultModel.success(pendingTasks);
        } catch (Exception e) {
            logRequestError("autoAssignTasks", e);
            return ResultModel.error(500, "自动分配失败: " + e.getMessage());
        }
    }

    @PostMapping("/start/{taskId}")
    public ResultModel<TaskDTO> startTask(@PathVariable String taskId) {
        long startTime = System.currentTimeMillis();
        logRequestStart("startTask", taskId);

        try {
            TaskDTO task = taskMap.get(taskId);
            if (task == null) {
                return ResultModel.notFound("任务不存在");
            }

            task.setStatus("running");

            logRequestEnd("startTask", taskId, System.currentTimeMillis() - startTime);
            return ResultModel.success("任务已启动", task);
        } catch (Exception e) {
            logRequestError("startTask", e);
            return ResultModel.error(500, "启动任务失败: " + e.getMessage());
        }
    }

    @PostMapping("/complete/{taskId}")
    public ResultModel<TaskResultDTO> completeTask(@PathVariable String taskId, @RequestBody TaskResultDTO result) {
        long startTime = System.currentTimeMillis();
        logRequestStart("completeTask", taskId);

        try {
            TaskDTO task = taskMap.get(taskId);
            if (task == null) {
                return ResultModel.notFound("任务不存在");
            }

            String resultId = "result-" + UUID.randomUUID().toString().substring(0, 8);
            result.setResultId(resultId);
            result.setTaskId(taskId);
            result.setGroupId(task.getGroupId());
            result.setEndTime(System.currentTimeMillis());

            if (result.getStartTime() > 0) {
                result.setDuration(result.getEndTime() - result.getStartTime());
            }

            resultMap.put(resultId, result);
            
            task.setStatus(result.isSuccess() ? "completed" : "failed");

            logRequestEnd("completeTask", resultId, System.currentTimeMillis() - startTime);
            return ResultModel.success("任务已完成", result);
        } catch (Exception e) {
            logRequestError("completeTask", e);
            return ResultModel.error(500, "完成任务失败: " + e.getMessage());
        }
    }

    @PostMapping("/cancel/{taskId}")
    public ResultModel<TaskDTO> cancelTask(@PathVariable String taskId) {
        long startTime = System.currentTimeMillis();
        logRequestStart("cancelTask", taskId);

        try {
            TaskDTO task = taskMap.get(taskId);
            if (task == null) {
                return ResultModel.notFound("任务不存在");
            }

            if ("completed".equals(task.getStatus()) || "failed".equals(task.getStatus())) {
                return ResultModel.error(400, "任务已完成，无法取消");
            }

            task.setStatus("cancelled");

            logRequestEnd("cancelTask", taskId, System.currentTimeMillis() - startTime);
            return ResultModel.success("任务已取消", task);
        } catch (Exception e) {
            logRequestError("cancelTask", e);
            return ResultModel.error(500, "取消任务失败: " + e.getMessage());
        }
    }

    @PostMapping("/get/{taskId}")
    public ResultModel<TaskDTO> getTask(@PathVariable String taskId) {
        long startTime = System.currentTimeMillis();
        logRequestStart("getTask", taskId);

        try {
            TaskDTO task = taskMap.get(taskId);
            if (task == null) {
                return ResultModel.notFound("任务不存在");
            }

            logRequestEnd("getTask", taskId, System.currentTimeMillis() - startTime);
            return ResultModel.success(task);
        } catch (Exception e) {
            logRequestError("getTask", e);
            return ResultModel.error(500, "获取任务失败: " + e.getMessage());
        }
    }

    @PostMapping("/list")
    public ResultModel<List<TaskDTO>> listTasks(@RequestBody Map<String, String> request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("listTasks", request);

        try {
            String groupId = request.get("groupId");
            String status = request.get("status");
            String assignedTo = request.get("assignedTo");

            List<TaskDTO> tasks;

            if (groupId != null && !groupId.isEmpty()) {
                tasks = new ArrayList<>(groupTaskMap.getOrDefault(groupId, new ArrayList<>()));
            } else {
                tasks = new ArrayList<>(taskMap.values());
            }

            if (status != null && !status.isEmpty()) {
                tasks = tasks.stream()
                        .filter(t -> status.equals(t.getStatus()))
                        .collect(Collectors.toList());
            }

            if (assignedTo != null && !assignedTo.isEmpty()) {
                tasks = tasks.stream()
                        .filter(t -> assignedTo.equals(t.getAssignedTo()))
                        .collect(Collectors.toList());
            }

            logRequestEnd("listTasks", tasks.size() + " tasks", System.currentTimeMillis() - startTime);
            return ResultModel.success(tasks);
        } catch (Exception e) {
            logRequestError("listTasks", e);
            return ResultModel.error(500, "获取任务列表失败: " + e.getMessage());
        }
    }

    @PostMapping("/summary/{groupId}")
    public ResultModel<TaskSummaryDTO> getTaskSummary(@PathVariable String groupId) {
        long startTime = System.currentTimeMillis();
        logRequestStart("getTaskSummary", groupId);

        try {
            List<TaskDTO> groupTasks = groupTaskMap.getOrDefault(groupId, new ArrayList<>());

            TaskSummaryDTO summary = new TaskSummaryDTO();
            summary.setGroupId(groupId);
            summary.setTotalTasks(groupTasks.size());

            int pending = 0, running = 0, completed = 0, failed = 0, cancelled = 0;
            long totalDuration = 0;
            int durationCount = 0;

            for (TaskDTO task : groupTasks) {
                switch (task.getStatus()) {
                    case "pending": pending++; break;
                    case "running": running++; break;
                    case "completed": completed++; break;
                    case "failed": failed++; break;
                    case "cancelled": cancelled++; break;
                }
            }

            for (TaskResultDTO result : resultMap.values()) {
                if (groupId.equals(result.getGroupId())) {
                    totalDuration += result.getDuration();
                    durationCount++;
                }
            }

            summary.setPendingTasks(pending);
            summary.setRunningTasks(running);
            summary.setCompletedTasks(completed);
            summary.setFailedTasks(failed);
            summary.setCancelledTasks(cancelled);

            if (groupTasks.size() > 0) {
                summary.setCompletionRate((double) (completed + failed + cancelled) / groupTasks.size() * 100);
            }

            if ((completed + failed) > 0) {
                summary.setSuccessRate((double) completed / (completed + failed) * 100);
            }

            summary.setTotalDuration(totalDuration);
            summary.setAverageDuration(durationCount > 0 ? (double) totalDuration / durationCount : 0);

            List<TaskDTO> recentTasks = groupTasks.stream()
                    .sorted((a, b) -> Long.compare(b.getCreatedAt(), a.getCreatedAt()))
                    .limit(10)
                    .collect(Collectors.toList());
            summary.setRecentTasks(recentTasks);

            logRequestEnd("getTaskSummary", summary, System.currentTimeMillis() - startTime);
            return ResultModel.success(summary);
        } catch (Exception e) {
            logRequestError("getTaskSummary", e);
            return ResultModel.error(500, "获取任务汇总失败: " + e.getMessage());
        }
    }

    @PostMapping("/results/{groupId}")
    public ResultModel<List<TaskResultDTO>> getTaskResults(@PathVariable String groupId) {
        long startTime = System.currentTimeMillis();
        logRequestStart("getTaskResults", groupId);

        try {
            List<TaskResultDTO> results = resultMap.values().stream()
                    .filter(r -> groupId.equals(r.getGroupId()))
                    .collect(Collectors.toList());

            logRequestEnd("getTaskResults", results.size() + " results", System.currentTimeMillis() - startTime);
            return ResultModel.success(results);
        } catch (Exception e) {
            logRequestError("getTaskResults", e);
            return ResultModel.error(500, "获取任务结果失败: " + e.getMessage());
        }
    }

    @PostMapping("/result/{resultId}")
    public ResultModel<TaskResultDTO> getResult(@PathVariable String resultId) {
        long startTime = System.currentTimeMillis();
        logRequestStart("getResult", resultId);

        try {
            TaskResultDTO result = resultMap.get(resultId);
            if (result == null) {
                return ResultModel.notFound("结果不存在");
            }

            logRequestEnd("getResult", resultId, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("getResult", e);
            return ResultModel.error(500, "获取结果失败: " + e.getMessage());
        }
    }

    private List<String> getGroupMembers(String groupId) {
        List<String> members = new ArrayList<>();
        members.add("member-001");
        members.add("member-002");
        members.add("member-003");
        return members;
    }
}
