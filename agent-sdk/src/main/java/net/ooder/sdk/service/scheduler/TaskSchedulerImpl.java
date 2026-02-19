package net.ooder.sdk.service.scheduler;

import net.ooder.sdk.api.scheduler.TaskInfo;
import net.ooder.sdk.api.scheduler.TaskScheduler;
import net.ooder.sdk.api.scheduler.TaskStatus;
import net.ooder.sdk.api.storage.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Task Scheduler Implementation
 *
 * @author ooder Team
 * @since 0.7.1
 */
public class TaskSchedulerImpl implements TaskScheduler {

    private static final Logger log = LoggerFactory.getLogger(TaskSchedulerImpl.class);

    private final ScheduledExecutorService executor;
    private final Map<String, ScheduledTask> tasks;
    private final Map<String, ScheduledFuture<?>> futures;
    private StorageService storage;
    private final AtomicBoolean running;

    public TaskSchedulerImpl() {
        this.executor = Executors.newScheduledThreadPool(4);
        this.tasks = new ConcurrentHashMap<String, ScheduledTask>();
        this.futures = new ConcurrentHashMap<String, ScheduledFuture<?>>();
        this.running = new AtomicBoolean(true);
        log.info("TaskSchedulerImpl initialized");
    }

    private static class ScheduledTask {
        TaskInfo info;
        Runnable task;
        Runnable wrappedTask;

        ScheduledTask(TaskInfo info, Runnable task) {
            this.info = info;
            this.task = task;
        }
    }

    @Override
    public String schedule(Runnable task, long delayMs) {
        return schedule(null, task, delayMs);
    }

    @Override
    public String schedule(String name, Runnable task, long delayMs) {
        String taskId = "task_" + UUID.randomUUID().toString().substring(0, 8);
        
        TaskInfo info = new TaskInfo();
        info.setTaskId(taskId);
        info.setName(name != null ? name : taskId);
        info.setDelayMs(delayMs);
        info.setRecurring(false);
        info.setScheduledAt(System.currentTimeMillis() + delayMs);
        
        ScheduledTask scheduledTask = new ScheduledTask(info, task);
        tasks.put(taskId, scheduledTask);
        
        ScheduledFuture<?> future = executor.schedule(new Runnable() {
            @Override
            public void run() {
                executeTask(taskId);
            }
        }, delayMs, TimeUnit.MILLISECONDS);
        futures.put(taskId, future);
        
        log.debug("Scheduled task: {} with delay: {}ms", taskId, delayMs);
        return taskId;
    }

    @Override
    public String scheduleAtFixedRate(Runnable task, long initialDelayMs, long periodMs) {
        return scheduleAtFixedRate(null, task, initialDelayMs, periodMs);
    }

    @Override
    public String scheduleAtFixedRate(String name, Runnable task, long initialDelayMs, long periodMs) {
        String taskId = "task_" + UUID.randomUUID().toString().substring(0, 8);
        
        TaskInfo info = new TaskInfo();
        info.setTaskId(taskId);
        info.setName(name != null ? name : taskId);
        info.setDelayMs(initialDelayMs);
        info.setPeriodMs(periodMs);
        info.setRecurring(true);
        info.setScheduledAt(System.currentTimeMillis() + initialDelayMs);
        
        ScheduledTask scheduledTask = new ScheduledTask(info, task);
        tasks.put(taskId, scheduledTask);
        
        ScheduledFuture<?> future = executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                executeTask(taskId);
            }
        }, initialDelayMs, periodMs, TimeUnit.MILLISECONDS);
        futures.put(taskId, future);
        
        log.debug("Scheduled recurring task: {} with period: {}ms", taskId, periodMs);
        return taskId;
    }

    @Override
    public String scheduleWithCron(Runnable task, String cronExpression) {
        return scheduleWithCron(null, task, cronExpression);
    }

    @Override
    public String scheduleWithCron(String name, Runnable task, String cronExpression) {
        String taskId = "task_" + UUID.randomUUID().toString().substring(0, 8);
        
        long delayMs = calculateCronDelay(cronExpression);
        
        TaskInfo info = new TaskInfo();
        info.setTaskId(taskId);
        info.setName(name != null ? name : taskId);
        info.setCronExpression(cronExpression);
        info.setRecurring(true);
        info.setScheduledAt(System.currentTimeMillis() + delayMs);
        
        ScheduledTask scheduledTask = new ScheduledTask(info, task);
        tasks.put(taskId, scheduledTask);
        
        scheduleCronTask(taskId, cronExpression);
        
        log.debug("Scheduled cron task: {} with expression: {}", taskId, cronExpression);
        return taskId;
    }

    private long calculateCronDelay(String cronExpression) {
        if (cronExpression == null || cronExpression.isEmpty()) {
            log.warn("Empty cron expression, using default delay");
            return 60000L;
        }
        
        try {
            String[] parts = cronExpression.trim().split("\\s+");
            if (parts.length < 5 || parts.length > 6) {
                log.warn("Invalid cron expression format: {}", cronExpression);
                return 60000L;
            }
            
            java.util.Calendar now = java.util.Calendar.getInstance();
            java.util.Calendar next = java.util.Calendar.getInstance();
            
            int second = parts.length == 6 ? parseCronPart(parts[0], 0, 59) : 0;
            int minute = parseCronPart(parts[parts.length == 6 ? 1 : 0], 0, 59);
            int hour = parseCronPart(parts[parts.length == 6 ? 2 : 1], 0, 23);
            int dayOfMonth = parseCronPart(parts[parts.length == 6 ? 3 : 2], 1, 31);
            int month = parseCronPart(parts[parts.length == 6 ? 4 : 3], 1, 12);
            int dayOfWeek = parseCronPart(parts[parts.length == 6 ? 5 : 4], 0, 6);
            
            next.set(java.util.Calendar.SECOND, second);
            next.set(java.util.Calendar.MINUTE, minute);
            next.set(java.util.Calendar.HOUR_OF_DAY, hour);
            
            if (next.getTimeInMillis() <= now.getTimeInMillis()) {
                next.add(java.util.Calendar.DAY_OF_MONTH, 1);
            }
            
            long delay = next.getTimeInMillis() - now.getTimeInMillis();
            
            log.debug("Cron expression '{}' next execution in {}ms", cronExpression, delay);
            return delay;
            
        } catch (Exception e) {
            log.warn("Failed to parse cron expression '{}': {}", cronExpression, e.getMessage());
            return 60000L;
        }
    }
    
    private int parseCronPart(String part, int min, int max) {
        if (part.equals("*")) {
            return min;
        }
        
        if (part.contains("/")) {
            String[] rangeParts = part.split("/");
            int step = Integer.parseInt(rangeParts[1]);
            return min + step;
        }
        
        if (part.contains("-")) {
            String[] rangeParts = part.split("-");
            return Integer.parseInt(rangeParts[0]);
        }
        
        if (part.contains(",")) {
            String[] values = part.split(",");
            return Integer.parseInt(values[0]);
        }
        
        return Integer.parseInt(part);
    }

    private void scheduleCronTask(String taskId, String cronExpression) {
        ScheduledTask scheduledTask = tasks.get(taskId);
        if (scheduledTask == null) return;
        
        long delayMs = calculateCronDelay(cronExpression);
        
        ScheduledFuture<?> future = executor.schedule(new Runnable() {
            @Override
            public void run() {
                executeTask(taskId);
                if (tasks.containsKey(taskId) && scheduledTask.info.isRecurring()) {
                    scheduleCronTask(taskId, cronExpression);
                }
            }
        }, delayMs, TimeUnit.MILLISECONDS);
        futures.put(taskId, future);
    }

    private void executeTask(String taskId) {
        ScheduledTask scheduledTask = tasks.get(taskId);
        if (scheduledTask == null) return;
        
        TaskInfo info = scheduledTask.info;
        if (info.getStatus() == TaskStatus.PAUSED) {
            log.debug("Task {} is paused, skipping", taskId);
            return;
        }
        
        info.setStatus(TaskStatus.RUNNING);
        info.setExecutedAt(System.currentTimeMillis());
        info.incrementExecutionCount();
        
        log.debug("Executing task: {}", taskId);
        
        try {
            scheduledTask.task.run();
            info.setStatus(info.isRecurring() ? TaskStatus.PENDING : TaskStatus.COMPLETED);
            info.setCompletedAt(System.currentTimeMillis());
            
            if (!info.isRecurring()) {
                tasks.remove(taskId);
                futures.remove(taskId);
            }
            
            persistTask(info);
        } catch (Exception e) {
            log.error("Task {} failed", taskId, e);
            info.setStatus(TaskStatus.FAILED);
            info.setError(e.getMessage());
            info.setCompletedAt(System.currentTimeMillis());
            persistTask(info);
        }
    }

    private void persistTask(TaskInfo info) {
        if (storage != null) {
            try {
                storage.saveAsync("task:" + info.getTaskId(), info);
            } catch (Exception e) {
                log.warn("Failed to persist task: {}", info.getTaskId(), e);
            }
        }
    }

    @Override
    public CompletableFuture<String> scheduleAsync(Runnable task, long delayMs) {
        return CompletableFuture.supplyAsync(() -> schedule(task, delayMs));
    }

    @Override
    public void cancel(String taskId) {
        log.debug("Cancelling task: {}", taskId);
        
        ScheduledFuture<?> future = futures.remove(taskId);
        if (future != null) {
            future.cancel(false);
        }
        
        ScheduledTask scheduledTask = tasks.remove(taskId);
        if (scheduledTask != null) {
            scheduledTask.info.setStatus(TaskStatus.CANCELLED);
        }
        
        log.debug("Task cancelled: {}", taskId);
    }

    @Override
    public boolean pause(String taskId) {
        ScheduledTask scheduledTask = tasks.get(taskId);
        if (scheduledTask == null) {
            return false;
        }
        
        if (scheduledTask.info.getStatus() == TaskStatus.RUNNING) {
            return false;
        }
        
        scheduledTask.info.setStatus(TaskStatus.PAUSED);
        log.debug("Task paused: {}", taskId);
        return true;
    }

    @Override
    public boolean resume(String taskId) {
        ScheduledTask scheduledTask = tasks.get(taskId);
        if (scheduledTask == null) {
            return false;
        }
        
        if (scheduledTask.info.getStatus() != TaskStatus.PAUSED) {
            return false;
        }
        
        scheduledTask.info.setStatus(TaskStatus.PENDING);
        log.debug("Task resumed: {}", taskId);
        return true;
    }

    @Override
    public TaskStatus getStatus(String taskId) {
        ScheduledTask scheduledTask = tasks.get(taskId);
        return scheduledTask != null ? scheduledTask.info.getStatus() : null;
    }

    @Override
    public TaskInfo getTaskInfo(String taskId) {
        ScheduledTask scheduledTask = tasks.get(taskId);
        return scheduledTask != null ? scheduledTask.info : null;
    }

    @Override
    public List<TaskInfo> getAllTasks() {
        List<TaskInfo> result = new ArrayList<TaskInfo>();
        for (ScheduledTask task : tasks.values()) {
            result.add(task.info);
        }
        return result;
    }

    @Override
    public List<TaskInfo> getTasksByStatus(TaskStatus status) {
        List<TaskInfo> result = new ArrayList<TaskInfo>();
        for (ScheduledTask task : tasks.values()) {
            if (task.info.getStatus() == status) {
                result.add(task.info);
            }
        }
        return result;
    }

    @Override
    public int getTaskCount() {
        return tasks.size();
    }

    @Override
    public void enablePersistence(StorageService storage) {
        this.storage = storage;
        log.info("Task persistence enabled");
    }

    @Override
    public void recoverTasks() {
        if (storage == null) {
            log.warn("Storage not configured, cannot recover tasks");
            return;
        }
        
        log.info("Recovering persisted tasks...");
        List<String> taskKeys = storage.listKeys("task:");
        int recoveredCount = 0;
        
        for (String key : taskKeys) {
            try {
                TaskInfo info = storage.load(key, TaskInfo.class).orElse(null);
                if (info != null && info.getStatus() != TaskStatus.COMPLETED 
                        && info.getStatus() != TaskStatus.CANCELLED) {
                    
                    String taskId = info.getTaskId();
                    
                    Runnable placeholderTask = new Runnable() {
                        @Override
                        public void run() {
                            log.info("Recovered task executed: {}", taskId);
                        }
                    };
                    
                    ScheduledTask scheduledTask = new ScheduledTask(info, placeholderTask);
                    tasks.put(taskId, scheduledTask);
                    
                    long delay = info.getScheduledTime() - System.currentTimeMillis();
                    if (delay < 0) {
                        delay = 0;
                    }
                    
                    ScheduledFuture<?> future = executor.schedule(placeholderTask, delay, TimeUnit.MILLISECONDS);
                    futures.put(taskId, future);
                    info.setStatus(TaskStatus.SCHEDULED);
                    
                    recoveredCount++;
                    log.info("Recovered and rescheduled task: {}", taskId);
                }
            } catch (Exception e) {
                log.warn("Failed to recover task: {}", key, e);
            }
        }
        
        log.info("Task recovery completed: {} tasks recovered", recoveredCount);
    }

    @Override
    public void shutdown() {
        log.info("Shutting down TaskScheduler");
        running.set(false);
        
        for (ScheduledFuture<?> future : futures.values()) {
            future.cancel(false);
        }
        
        executor.shutdown();
        try {
            if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
        
        tasks.clear();
        futures.clear();
        log.info("TaskScheduler shutdown complete");
    }

    @Override
    public boolean isRunning() {
        return running.get();
    }
}
