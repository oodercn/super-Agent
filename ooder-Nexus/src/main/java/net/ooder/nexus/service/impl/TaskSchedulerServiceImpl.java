package net.ooder.nexus.service.impl;

import net.ooder.nexus.service.TaskSchedulerService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class TaskSchedulerServiceImpl implements TaskSchedulerService {

    private static final Logger log = LoggerFactory.getLogger(TaskSchedulerServiceImpl.class);

    private final Map<String, CollaborationTask> pendingTasks = new ConcurrentHashMap<String, CollaborationTask>();
    private final Map<String, TaskExecution> executions = new ConcurrentHashMap<String, TaskExecution>();
    private final List<TaskExecution> executionHistory = new CopyOnWriteArrayList<TaskExecution>();
    private final List<TaskStateListener> listeners = new CopyOnWriteArrayList<TaskStateListener>();

    public TaskSchedulerServiceImpl() {
        log.info("TaskSchedulerServiceImpl initialized with SDK 0.7.2");
    }

    @Override
    public CompletableFuture<CollaborationTask> receiveTask(Map<String, Object> taskData) {
        log.info("Receiving task: {}", taskData.get("taskName"));
        
        return CompletableFuture.supplyAsync(() -> {
            CollaborationTask task = new CollaborationTask();
            task.setTaskId("task-" + System.currentTimeMillis());
            task.setTaskName((String) taskData.getOrDefault("taskName", "未命名任务"));
            task.setTaskType((String) taskData.getOrDefault("taskType", "GENERAL"));
            task.setDescription((String) taskData.getOrDefault("description", ""));
            task.setSourceId((String) taskData.getOrDefault("sourceId", "unknown"));
            task.setSceneGroupId((String) taskData.getOrDefault("sceneGroupId", ""));
            task.setPriority(taskData.containsKey("priority") ? ((Number) taskData.get("priority")).intValue() : 5);
            task.setCreateTime(System.currentTimeMillis());
            task.setDeadline(taskData.containsKey("deadline") ? ((Number) taskData.get("deadline")).longValue() : System.currentTimeMillis() + 3600000);
            task.setParameters(taskData);
            task.setStatus("PENDING");
            
            pendingTasks.put(task.getTaskId(), task);
            
            notifyTaskReceived(task);
            log.info("Task received: {} ({})", task.getTaskName(), task.getTaskId());
            return task;
        });
    }

    @Override
    public CompletableFuture<List<CollaborationTask>> getPendingTasks() {
        log.info("Getting pending tasks");
        return CompletableFuture.completedFuture(new ArrayList<CollaborationTask>(pendingTasks.values()));
    }

    @Override
    public CompletableFuture<CollaborationTask> getTaskDetail(String taskId) {
        log.info("Getting task detail: {}", taskId);
        return CompletableFuture.completedFuture(pendingTasks.get(taskId));
    }

    @Override
    public CompletableFuture<TaskExecution> startTask(String taskId) {
        log.info("Starting task: {}", taskId);
        
        return CompletableFuture.supplyAsync(() -> {
            CollaborationTask task = pendingTasks.get(taskId);
            if (task == null) {
                throw new RuntimeException("Task not found: " + taskId);
            }
            
            String oldState = task.getStatus();
            task.setStatus("RUNNING");
            
            TaskExecution execution = new TaskExecution();
            execution.setExecutionId("exec-" + System.currentTimeMillis());
            execution.setTaskId(taskId);
            execution.setStatus("RUNNING");
            execution.setProgress(0);
            execution.setCurrentStage("初始化");
            execution.setStartTime(System.currentTimeMillis());
            execution.setExecutorId("nexus-001");
            
            executions.put(taskId, execution);
            
            notifyStateChanged(taskId, oldState, "RUNNING");
            notifyProgress(taskId, 0, "任务开始执行");
            
            simulateTaskExecution(taskId, execution);
            
            return execution;
        });
    }

    private void simulateTaskExecution(String taskId, TaskExecution execution) {
        CompletableFuture.runAsync(() -> {
            try {
                for (int i = 1; i <= 100; i += 10) {
                    Thread.sleep(500);
                    execution.setProgress(i);
                    execution.setCurrentStage("执行中 " + i + "%");
                    notifyProgress(taskId, i, "执行中 " + i + "%");
                }
                
                execution.setStatus("COMPLETED");
                execution.setEndTime(System.currentTimeMillis());
                execution.setProgress(100);
                execution.setCurrentStage("完成");
                
                CollaborationTask task = pendingTasks.get(taskId);
                if (task != null) {
                    String oldState = task.getStatus();
                    task.setStatus("COMPLETED");
                    notifyStateChanged(taskId, oldState, "COMPLETED");
                }
                
                executionHistory.add(0, execution);
                if (executionHistory.size() > 100) {
                    executionHistory.remove(executionHistory.size() - 1);
                }
                
                TaskResult result = new TaskResult();
                result.setResultId("result-" + System.currentTimeMillis());
                result.setTaskId(taskId);
                result.setSuccess(true);
                result.setData("执行完成");
                result.setCompletionTime(System.currentTimeMillis());
                notifyTaskCompleted(taskId, result);
                
                log.info("Task completed: {}", taskId);
            } catch (InterruptedException e) {
                execution.setStatus("FAILED");
                execution.setErrorMessage("执行被中断");
                log.warn("Task interrupted: {}", taskId);
            }
        });
    }

    @Override
    public CompletableFuture<Void> pauseTask(String taskId) {
        log.info("Pausing task: {}", taskId);
        
        return CompletableFuture.runAsync(() -> {
            CollaborationTask task = pendingTasks.get(taskId);
            if (task != null) {
                String oldState = task.getStatus();
                task.setStatus("PAUSED");
                notifyStateChanged(taskId, oldState, "PAUSED");
            }
        });
    }

    @Override
    public CompletableFuture<Void> resumeTask(String taskId) {
        log.info("Resuming task: {}", taskId);
        
        return CompletableFuture.runAsync(() -> {
            CollaborationTask task = pendingTasks.get(taskId);
            if (task != null) {
                String oldState = task.getStatus();
                task.setStatus("RUNNING");
                notifyStateChanged(taskId, oldState, "RUNNING");
            }
        });
    }

    @Override
    public CompletableFuture<Void> cancelTask(String taskId, String reason) {
        log.info("Cancelling task: {} - {}", taskId, reason);
        
        return CompletableFuture.runAsync(() -> {
            CollaborationTask task = pendingTasks.get(taskId);
            if (task != null) {
                String oldState = task.getStatus();
                task.setStatus("CANCELLED");
                notifyStateChanged(taskId, oldState, "CANCELLED");
            }
            
            TaskExecution execution = executions.get(taskId);
            if (execution != null) {
                execution.setStatus("CANCELLED");
                execution.setErrorMessage(reason);
                execution.setEndTime(System.currentTimeMillis());
            }
        });
    }

    @Override
    public CompletableFuture<ResultSubmission> submitResult(String taskId, TaskResult result) {
        log.info("Submitting result for task: {}", taskId);
        
        return CompletableFuture.supplyAsync(() -> {
            ResultSubmission submission = new ResultSubmission();
            submission.setSubmissionId("submit-" + System.currentTimeMillis());
            submission.setTaskId(taskId);
            submission.setStatus("CONFIRMED");
            submission.setSubmitTime(System.currentTimeMillis());
            submission.setConfirmationCode("CONF-" + System.currentTimeMillis());
            
            CollaborationTask task = pendingTasks.get(taskId);
            if (task != null) {
                String oldState = task.getStatus();
                task.setStatus("SUBMITTED");
                notifyStateChanged(taskId, oldState, "SUBMITTED");
            }
            
            log.info("Result submitted for task: {}", taskId);
            return submission;
        });
    }

    @Override
    public CompletableFuture<TaskExecution> getTaskExecution(String taskId) {
        log.info("Getting task execution: {}", taskId);
        return CompletableFuture.completedFuture(executions.get(taskId));
    }

    @Override
    public CompletableFuture<List<TaskExecution>> getExecutionHistory(int limit) {
        log.info("Getting execution history, limit: {}", limit);
        
        return CompletableFuture.supplyAsync(() -> {
            if (limit <= 0 || limit > executionHistory.size()) {
                return new ArrayList<TaskExecution>(executionHistory);
            }
            return new ArrayList<TaskExecution>(executionHistory.subList(0, limit));
        });
    }

    @Override
    public void addTaskListener(TaskStateListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeTaskListener(TaskStateListener listener) {
        listeners.remove(listener);
    }

    private void notifyTaskReceived(CollaborationTask task) {
        for (TaskStateListener listener : listeners) {
            try {
                listener.onTaskReceived(task);
            } catch (Exception e) {
                log.warn("Listener error: {}", e.getMessage());
            }
        }
    }

    private void notifyStateChanged(String taskId, String oldState, String newState) {
        for (TaskStateListener listener : listeners) {
            try {
                listener.onTaskStateChanged(taskId, oldState, newState);
            } catch (Exception e) {
                log.warn("Listener error: {}", e.getMessage());
            }
        }
    }

    private void notifyProgress(String taskId, int progress, String stage) {
        for (TaskStateListener listener : listeners) {
            try {
                listener.onTaskProgress(taskId, progress, stage);
            } catch (Exception e) {
                log.warn("Listener error: {}", e.getMessage());
            }
        }
    }

    private void notifyTaskCompleted(String taskId, TaskResult result) {
        for (TaskStateListener listener : listeners) {
            try {
                listener.onTaskCompleted(taskId, result);
            } catch (Exception e) {
                log.warn("Listener error: {}", e.getMessage());
            }
        }
    }
}
