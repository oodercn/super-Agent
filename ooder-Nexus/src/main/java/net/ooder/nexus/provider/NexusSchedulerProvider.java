package net.ooder.nexus.provider;

import net.ooder.scene.skill.SchedulerProvider;
import net.ooder.scene.core.SceneEngine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * SchedulerProvider 实现
 *
 * <p>基于内存实现 SchedulerProvider 接口</p>
 *
 * @author ooder Team
 * @version 0.7.3
 * @since SDK 0.7.3
 */
@Component
public class NexusSchedulerProvider implements SchedulerProvider {

    private static final Logger log = LoggerFactory.getLogger(NexusSchedulerProvider.class);
    private static final String PROVIDER_TYPE = "nexus-memory";

    private SceneEngine sceneEngine;
    private boolean initialized = false;
    private boolean running = false;

    private final Map<String, TaskInfo> taskStore = new ConcurrentHashMap<String, TaskInfo>();
    private final Map<String, List<TaskExecution>> executionHistory = new ConcurrentHashMap<String, List<TaskExecution>>();
    private final AtomicLong taskIdCounter = new AtomicLong(0);
    private final AtomicLong executionIdCounter = new AtomicLong(0);

    public void initialize(SceneEngine engine) {
        this.sceneEngine = engine;
        this.initialized = true;
        initDefaultTasks();
        log.info("NexusSchedulerProvider initialized");
    }

    public void start() {
        this.running = true;
        log.info("NexusSchedulerProvider started");
    }

    public void stop() {
        this.running = false;
        log.info("NexusSchedulerProvider stopped");
    }

    public boolean isInitialized() {
        return initialized;
    }

    public boolean isRunning() {
        return running;
    }

    private void initDefaultTasks() {
        schedule("系统健康检查", "0 */5 * * * ?", null, null);
        schedule("日志清理任务", "0 0 2 * * ?", null, null);
        schedule("数据备份任务", "0 0 3 * * ?", null, null);
    }

    @Override
    public String getProviderType() {
        return PROVIDER_TYPE;
    }

    @Override
    public String schedule(String taskName, String cronExpression, Object taskData, Map<String, Object> options) {
        log.info("Scheduling task: {}, cron: {}", taskName, cronExpression);

        String taskId = "task-" + taskIdCounter.incrementAndGet();

        TaskInfo task = new TaskInfo();
        task.setTaskId(taskId);
        task.setTaskName(taskName);
        task.setCronExpression(cronExpression);
        task.setStatus("running");
        task.setLastExecutionTime(0);
        task.setNextExecutionTime(System.currentTimeMillis() + 300000);
        task.setExecutionCount(0);
        if (taskData != null) {
            @SuppressWarnings("unchecked")
            Map<String, Object> data = (taskData instanceof Map) ? (Map<String, Object>) taskData : new HashMap<String, Object>();
            task.setTaskData(data);
        } else {
            task.setTaskData(new HashMap<String, Object>());
        }

        taskStore.put(taskId, task);
        executionHistory.put(taskId, new ArrayList<TaskExecution>());

        return taskId;
    }

    @Override
    public boolean cancel(String taskId) {
        log.info("Canceling task: {}", taskId);

        TaskInfo task = taskStore.get(taskId);
        if (task == null) {
            return false;
        }

        taskStore.remove(taskId);
        executionHistory.remove(taskId);

        return true;
    }

    @Override
    public boolean pause(String taskId) {
        log.info("Pausing task: {}", taskId);

        TaskInfo task = taskStore.get(taskId);
        if (task == null) {
            return false;
        }

        task.setStatus("paused");
        return true;
    }

    @Override
    public boolean resume(String taskId) {
        log.info("Resuming task: {}", taskId);

        TaskInfo task = taskStore.get(taskId);
        if (task == null) {
            return false;
        }

        task.setStatus("running");
        return true;
    }

    @Override
    public TaskInfo getTask(String taskId) {
        log.debug("Getting task: {}", taskId);
        return taskStore.get(taskId);
    }

    @Override
    public TaskListResult listTasks(String status, int page, int pageSize) {
        log.debug("Listing tasks: status={}, page={}, size={}", status, page, pageSize);

        List<TaskInfo> filteredTasks = new ArrayList<TaskInfo>();

        for (TaskInfo task : taskStore.values()) {
            if (status == null || status.isEmpty() || status.equals(task.getStatus())) {
                filteredTasks.add(task);
            }
        }

        int total = filteredTasks.size();
        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, total);

        List<TaskInfo> pageData = new ArrayList<TaskInfo>();
        if (start < total) {
            pageData = filteredTasks.subList(start, end);
        }

        TaskListResult result = new TaskListResult(pageData, total);
        result.setPage(page);
        result.setPageSize(pageSize);

        return result;
    }

    @Override
    public boolean triggerNow(String taskId) {
        log.info("Triggering task: {}", taskId);

        TaskInfo task = taskStore.get(taskId);
        if (task == null) {
            return false;
        }

        TaskExecution execution = new TaskExecution();
        execution.setExecutionId("exec-" + executionIdCounter.incrementAndGet());
        execution.setTaskId(taskId);
        execution.setStartTime(System.currentTimeMillis());
        execution.setEndTime(System.currentTimeMillis() + 100);
        execution.setStatus("success");
        execution.setResult("Task executed successfully");
        execution.setErrorMessage(null);

        List<TaskExecution> history = executionHistory.get(taskId);
        if (history != null) {
            history.add(0, execution);
            if (history.size() > 100) {
                history.remove(history.size() - 1);
            }
        }

        task.setLastExecutionTime(execution.getStartTime());
        task.setNextExecutionTime(System.currentTimeMillis() + 300000);
        task.setExecutionCount(task.getExecutionCount() + 1);

        return true;
    }

    @Override
    public boolean updateCron(String taskId, String cronExpression) {
        log.info("Updating task cron: {}, new cron: {}", taskId, cronExpression);

        TaskInfo task = taskStore.get(taskId);
        if (task == null) {
            return false;
        }

        task.setCronExpression(cronExpression);
        task.setNextExecutionTime(System.currentTimeMillis() + 300000);

        return true;
    }

    @Override
    public List<TaskExecution> getExecutionHistory(String taskId, int limit) {
        log.debug("Getting execution history: taskId={}, limit={}", taskId, limit);

        List<TaskExecution> history = executionHistory.get(taskId);
        if (history == null) {
            return new ArrayList<TaskExecution>();
        }

        if (history.size() <= limit) {
            return new ArrayList<TaskExecution>(history);
        }

        return new ArrayList<TaskExecution>(history.subList(0, limit));
    }
}
