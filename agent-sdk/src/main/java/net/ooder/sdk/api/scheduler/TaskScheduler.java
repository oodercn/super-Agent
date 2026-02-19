package net.ooder.sdk.api.scheduler;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import net.ooder.sdk.api.storage.StorageService;

/**
 * Task Scheduler Interface
 *
 * <p>Provides task scheduling capabilities including:</p>
 * <ul>
 *   <li>One-time delayed tasks</li>
 *   <li>Fixed-rate recurring tasks</li>
 *   <li>Cron expression based scheduling</li>
 *   <li>Task persistence and recovery</li>
 * </ul>
 *
 * <h3>Usage Example:</h3>
 * <pre>
 * TaskScheduler scheduler = new TaskSchedulerImpl();
 *
 * // One-time task
 * String taskId = scheduler.schedule(() -> {
 *     log.info("Task executed!");
 * }, 5000); // 5 seconds delay
 *
 * // Fixed-rate task
 * String periodicId = scheduler.scheduleAtFixedRate(() -> {
 *     log.info("Periodic task at {}", System.currentTimeMillis());
 * }, 0, 60000); // every minute
 *
 * // Cron task
 * String cronId = scheduler.scheduleWithCron(() -> {
 *     log.info("Daily task");
 * }, "0 0 8 * * ?"); // every day at 8:00 AM
 *
 * // Cancel task
 * scheduler.cancel(taskId);
 *
 * // Pause/Resume
 * scheduler.pause(taskId);
 * scheduler.resume(taskId);
 * </pre>
 *
 * @author ooder Team
 * @since 0.7.1
 */
public interface TaskScheduler {

    // ==================== Scheduling ====================

    /**
     * Schedule a one-time task with delay
     *
     * @param task    the task to execute
     * @param delayMs delay in milliseconds
     * @return task ID
     */
    String schedule(Runnable task, long delayMs);

    /**
     * Schedule a named one-time task
     *
     * @param name    task name
     * @param task    the task to execute
     * @param delayMs delay in milliseconds
     * @return task ID
     */
    String schedule(String name, Runnable task, long delayMs);

    /**
     * Schedule a fixed-rate recurring task
     *
     * @param task          the task to execute
     * @param initialDelayMs initial delay in milliseconds
     * @param periodMs      period between executions in milliseconds
     * @return task ID
     */
    String scheduleAtFixedRate(Runnable task, long initialDelayMs, long periodMs);

    /**
     * Schedule a named fixed-rate recurring task
     *
     * @param name          task name
     * @param task          the task to execute
     * @param initialDelayMs initial delay in milliseconds
     * @param periodMs      period between executions in milliseconds
     * @return task ID
     */
    String scheduleAtFixedRate(String name, Runnable task, long initialDelayMs, long periodMs);

    /**
     * Schedule a task with cron expression
     *
     * @param task           the task to execute
     * @param cronExpression cron expression (e.g., "0 0 8 * * ?" for daily at 8:00)
     * @return task ID
     */
    String scheduleWithCron(Runnable task, String cronExpression);

    /**
     * Schedule a named task with cron expression
     *
     * @param name           task name
     * @param task           the task to execute
     * @param cronExpression cron expression
     * @return task ID
     */
    String scheduleWithCron(String name, Runnable task, String cronExpression);

    /**
     * Schedule an async task
     *
     * @param task    the task to execute
     * @param delayMs delay in milliseconds
     * @return CompletableFuture with task ID
     */
    CompletableFuture<String> scheduleAsync(Runnable task, long delayMs);

    // ==================== Task Control ====================

    /**
     * Cancel a task
     *
     * @param taskId the task ID
     */
    void cancel(String taskId);

    /**
     * Pause a task
     *
     * @param taskId the task ID
     * @return true if paused successfully
     */
    boolean pause(String taskId);

    /**
     * Resume a paused task
     *
     * @param taskId the task ID
     * @return true if resumed successfully
     */
    boolean resume(String taskId);

    // ==================== Query ====================

    /**
     * Get task status
     *
     * @param taskId the task ID
     * @return task status
     */
    TaskStatus getStatus(String taskId);

    /**
     * Get task info
     *
     * @param taskId the task ID
     * @return task info, or null if not found
     */
    TaskInfo getTaskInfo(String taskId);

    /**
     * Get all tasks
     *
     * @return list of all tasks
     */
    List<TaskInfo> getAllTasks();

    /**
     * Get tasks by status
     *
     * @param status the status to filter by
     * @return list of matching tasks
     */
    List<TaskInfo> getTasksByStatus(TaskStatus status);

    /**
     * Get task count
     *
     * @return total task count
     */
    int getTaskCount();

    // ==================== Persistence ====================

    /**
     * Enable task persistence
     *
     * @param storage the storage service
     */
    void enablePersistence(StorageService storage);

    /**
     * Recover persisted tasks after restart
     */
    void recoverTasks();

    // ==================== Lifecycle ====================

    /**
     * Shutdown the scheduler
     */
    void shutdown();

    /**
     * Check if scheduler is running
     *
     * @return true if running
     */
    boolean isRunning();
}
