package net.ooder.nexus.adapter.inbound.controller;

import net.ooder.nexus.common.ResultModel;
import net.ooder.nexus.dto.task.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/task")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TaskManagementController {

    private final Map<Long, TaskDTO> tasks = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public TaskManagementController() {
        initMockTasks();
    }

    private void initMockTasks() {
        addTask("数据抽取-用户表", "extract", "daily", "00:00", true);
        addTask("数据抽取-订单表", "extract", "hourly", "*/30 * * * *", true);
        addTask("数据同步-库存", "sync", "daily", "06:00", false);
        addTask("日志清理", "cleanup", "weekly", "02:00", true);
        addTask("报表生成", "report", "daily", "08:00", true);
    }

    @GetMapping("/list")
    public ResultModel<List<TaskDTO>> getTaskList() {
        return ResultModel.success("获取成功", new ArrayList<>(tasks.values()));
    }

    @PostMapping("/create")
    public ResultModel<TaskDTO> createTask(@RequestBody TaskCreateDTO request) {
        TaskDTO task = addTask(
            request.getName(), 
            request.getType(), 
            request.getSchedule(), 
            request.getScheduleTime(), 
            request.getEnabled() != null ? request.getEnabled() : true
        );
        return ResultModel.success("创建成功", task);
    }

    @PostMapping("/update")
    public ResultModel<TaskDTO> updateTask(@RequestBody TaskUpdateDTO request) {
        Long id = request.getId();
        TaskDTO task = tasks.get(id);
        if (task == null) {
            return ResultModel.error("任务不存在");
        }
        
        if (request.getName() != null) task.setName(request.getName());
        if (request.getType() != null) task.setType(request.getType());
        if (request.getSchedule() != null) task.setSchedule(request.getSchedule());
        if (request.getScheduleTime() != null) task.setScheduleTime(request.getScheduleTime());
        if (request.getEnabled() != null) task.setEnabled(request.getEnabled());
        task.setUpdateTime(new Date());
        
        return ResultModel.success("更新成功", task);
    }

    @PostMapping("/delete")
    public ResultModel<Boolean> deleteTask(@RequestParam Long id) {
        TaskDTO removed = tasks.remove(id);
        if (removed == null) {
            return ResultModel.error("任务不存在");
        }
        return ResultModel.success("删除成功", true);
    }

    @PostMapping("/execute")
    public ResultModel<TaskExecuteResultDTO> executeTask(@RequestParam Long id) {
        TaskDTO task = tasks.get(id);
        if (task == null) {
            return ResultModel.error("任务不存在");
        }
        
        TaskExecuteResultDTO result = new TaskExecuteResultDTO();
        result.setTaskId(id);
        result.setTaskName(task.getName());
        result.setExecuteTime(new Date());
        result.setStatus("completed");
        result.setMessage("任务执行成功");
        
        return ResultModel.success("执行成功", result);
    }

    @GetMapping("/stats")
    public ResultModel<TaskStatsDTO> getTaskStats() {
        TaskStatsDTO stats = new TaskStatsDTO();
        stats.setTotal(tasks.size());
        stats.setEnabled(tasks.values().stream().filter(t -> Boolean.TRUE.equals(t.getEnabled())).count());
        stats.setDisabled(tasks.values().stream().filter(t -> Boolean.FALSE.equals(t.getEnabled())).count());
        
        Map<String, Long> byType = new HashMap<>();
        for (TaskDTO task : tasks.values()) {
            String type = task.getType();
            byType.put(type, byType.getOrDefault(type, 0L) + 1);
        }
        stats.setByType(byType);
        
        return ResultModel.success("获取成功", stats);
    }

    private TaskDTO addTask(String name, String type, String schedule, String scheduleTime, boolean enabled) {
        Long id = idGenerator.getAndIncrement();
        TaskDTO task = new TaskDTO();
        task.setId(id);
        task.setName(name);
        task.setType(type);
        task.setSchedule(schedule);
        task.setScheduleTime(scheduleTime);
        task.setEnabled(enabled);
        task.setCreateTime(new Date());
        task.setUpdateTime(new Date());
        task.setLastExecuteTime(null);
        task.setNextExecuteTime(calculateNextExecuteTime(schedule, scheduleTime));
        tasks.put(id, task);
        return task;
    }

    private Date calculateNextExecuteTime(String schedule, String scheduleTime) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, 1);
        return cal.getTime();
    }
}
