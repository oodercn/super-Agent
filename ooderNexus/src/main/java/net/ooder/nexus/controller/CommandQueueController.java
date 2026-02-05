package net.ooder.nexus.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/command/queue")
public class CommandQueueController {

    private static final Logger log = LoggerFactory.getLogger(CommandQueueController.class);
    
    // 命令队列
    private final ConcurrentLinkedQueue<Command> commandQueue = new ConcurrentLinkedQueue<>();
    
    // 命令状态存储
    private final Map<String, Command> commandStatus = new HashMap<>();
    
    // 命令ID生成器
    private final AtomicLong commandIdGenerator = new AtomicLong(1);
    
    // 初始化默认命令队列
    public CommandQueueController() {
        initializeDefaultCommands();
    }
    
    private void initializeDefaultCommands() {
        // 添加默认命令
        Map<String, Object> params1 = new ConcurrentHashMap<>();
        params1.put("timeout", 5000);
        params1.put("count", 4);
        addCommandToQueue(new CommandData(
            "ping",
            "192.168.1.1",
            params1,
            "system"
        ));
        
        Map<String, Object> params2 = new ConcurrentHashMap<>();
        params2.put("delay", 0);
        addCommandToQueue(new CommandData(
            "reboot",
            "device-1",
            params2,
            "admin"
        ));
        
        Map<String, Object> params3 = new ConcurrentHashMap<>();
        addCommandToQueue(new CommandData(
            "status",
            "service-1",
            params3,
            "system"
        ));
    }
    
    /**
     * 获取命令队列
     */
    @GetMapping("/list")
    public Map<String, Object> getCommandQueue(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String type) {
        log.info("Get command queue requested: status={}, type={}", status, type);
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Command> filteredCommands = new ArrayList<>();
            
            // 从队列和状态存储中获取所有命令
            filteredCommands.addAll(commandQueue);
            filteredCommands.addAll(commandStatus.values());
            
            // 过滤命令
            if (status != null) {
                filteredCommands = filteredCommands.stream()
                        .filter(cmd -> cmd.getStatus().equals(status))
                        .collect(Collectors.toList());
            }
            
            if (type != null) {
                filteredCommands = filteredCommands.stream()
                        .filter(cmd -> cmd.getType().equals(type))
                        .collect(Collectors.toList());
            }
            
            // 转换为响应格式
            List<Map<String, Object>> commandList = new ArrayList<>();
            for (Command cmd : filteredCommands) {
                commandList.add(cmd.toMap());
            }
            
            // 命令队列统计
            Map<String, Object> stats = new HashMap<>();
            stats.put("total", filteredCommands.size());
            stats.put("queued", filteredCommands.stream().filter(cmd -> "queued".equals(cmd.getStatus())).count());
            stats.put("processing", filteredCommands.stream().filter(cmd -> "processing".equals(cmd.getStatus())).count());
            stats.put("completed", filteredCommands.stream().filter(cmd -> "completed".equals(cmd.getStatus())).count());
            stats.put("failed", filteredCommands.stream().filter(cmd -> "failed".equals(cmd.getStatus())).count());
            stats.put("cancelled", filteredCommands.stream().filter(cmd -> "cancelled".equals(cmd.getStatus())).count());
            
            response.put("status", "success");
            response.put("message", "Command queue retrieved successfully");
            response.put("data", commandList);
            response.put("stats", stats);
            response.put("queueSize", commandQueue.size());
            response.put("timestamp", System.currentTimeMillis());
            
        } catch (Exception e) {
            log.error("Error getting command queue: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "COMMAND_QUEUE_RETRIEVAL_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        }
        
        return response;
    }
    
    /**
     * 获取命令详情
     */
    @GetMapping("/detail/{commandId}")
    public Map<String, Object> getCommandDetail(@PathVariable String commandId) {
        log.info("Get command detail requested: {}", commandId);
        Map<String, Object> response = new HashMap<>();
        
        try {
            Command command = null;
            
            // 先从队列中查找
            for (Command cmd : commandQueue) {
                if (cmd.getId().equals(commandId)) {
                    command = cmd;
                    break;
                }
            }
            
            // 如果队列中没有，从状态存储中查找
            if (command == null) {
                command = commandStatus.get(commandId);
            }
            
            if (command == null) {
                response.put("status", "error");
                response.put("message", "Command not found");
                response.put("code", "COMMAND_NOT_FOUND");
                response.put("timestamp", System.currentTimeMillis());
                return response;
            }
            
            response.put("status", "success");
            response.put("message", "Command detail retrieved successfully");
            response.put("data", command.toMap());
            response.put("timestamp", System.currentTimeMillis());
            
        } catch (Exception e) {
            log.error("Error getting command detail: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "COMMAND_DETAIL_RETRIEVAL_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        }
        
        return response;
    }
    
    /**
     * 添加命令到队列
     */
    @PostMapping("/add")
    public Map<String, Object> addCommand(@RequestBody CommandData commandData) {
        log.info("Add command to queue requested: {}", commandData);
        Map<String, Object> response = new HashMap<>();
        
        try {
            Command command = addCommandToQueue(commandData);
            
            response.put("status", "success");
            response.put("message", "Command added to queue successfully");
            response.put("data", command.toMap());
            response.put("timestamp", System.currentTimeMillis());
            
        } catch (Exception e) {
            log.error("Error adding command to queue: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "COMMAND_ADD_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        }
        
        return response;
    }
    
    /**
     * 取消命令
     */
    @PostMapping("/cancel/{commandId}")
    public Map<String, Object> cancelCommand(@PathVariable String commandId) {
        log.info("Cancel command requested: {}", commandId);
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 从队列中移除
            Command commandToRemove = null;
            for (Command cmd : commandQueue) {
                if (cmd.getId().equals(commandId)) {
                    commandToRemove = cmd;
                    break;
                }
            }
            
            if (commandToRemove != null) {
                commandQueue.remove(commandToRemove);
                commandToRemove.setStatus("cancelled");
                commandToRemove.setMessage("Command cancelled by user");
                commandStatus.put(commandId, commandToRemove);
            } else {
                // 检查是否在状态存储中
                Command command = commandStatus.get(commandId);
                if (command != null && ("queued".equals(command.getStatus()) || "processing".equals(command.getStatus()))) {
                    command.setStatus("cancelled");
                    command.setMessage("Command cancelled by user");
                } else {
                    response.put("status", "error");
                    response.put("message", "Command not found or cannot be cancelled");
                    response.put("code", "COMMAND_CANCEL_ERROR");
                    response.put("timestamp", System.currentTimeMillis());
                    return response;
                }
            }
            
            response.put("status", "success");
            response.put("message", "Command cancelled successfully");
            response.put("commandId", commandId);
            response.put("timestamp", System.currentTimeMillis());
            
        } catch (Exception e) {
            log.error("Error cancelling command: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "COMMAND_CANCEL_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        }
        
        return response;
    }
    
    /**
     * 重试命令
     */
    @PostMapping("/retry/{commandId}")
    public Map<String, Object> retryCommand(@PathVariable String commandId) {
        log.info("Retry command requested: {}", commandId);
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 查找命令
            Command command = commandStatus.get(commandId);
            if (command == null) {
                response.put("status", "error");
                response.put("message", "Command not found");
                response.put("code", "COMMAND_NOT_FOUND");
                response.put("timestamp", System.currentTimeMillis());
                return response;
            }
            
            // 创建新的命令数据
            CommandData commandData = new CommandData(
                command.getType(),
                command.getTarget(),
                command.getParameters(),
                command.getSubmittedBy()
            );
            
            // 添加到队列
            Command newCommand = addCommandToQueue(commandData);
            
            response.put("status", "success");
            response.put("message", "Command retried successfully");
            response.put("data", newCommand.toMap());
            response.put("timestamp", System.currentTimeMillis());
            
        } catch (Exception e) {
            log.error("Error retrying command: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "COMMAND_RETRY_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        }
        
        return response;
    }
    
    /**
     * 清空队列
     */
    @PostMapping("/clear")
    public Map<String, Object> clearQueue() {
        log.info("Clear command queue requested");
        Map<String, Object> response = new HashMap<>();
        
        try {
            int queueSize = commandQueue.size();
            
            // 清空队列
            commandQueue.clear();
            
            response.put("status", "success");
            response.put("message", "Command queue cleared successfully");
            response.put("clearedCount", queueSize);
            response.put("timestamp", System.currentTimeMillis());
            
        } catch (Exception e) {
            log.error("Error clearing command queue: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "QUEUE_CLEAR_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        }
        
        return response;
    }
    
    /**
     * 获取队列统计信息
     */
    @GetMapping("/stats")
    public Map<String, Object> getQueueStats() {
        log.info("Get queue stats requested");
        Map<String, Object> response = new HashMap<>();
        
        try {
            Map<String, Object> stats = new HashMap<>();
            
            // 队列当前状态
            stats.put("queueSize", commandQueue.size());
            stats.put("totalCommands", commandQueue.size() + commandStatus.size());
            
            // 状态分布
            Map<String, Long> statusCounts = new HashMap<>();
            for (Command cmd : commandQueue) {
                statusCounts.put(cmd.getStatus(), statusCounts.getOrDefault(cmd.getStatus(), 0L) + 1);
            }
            for (Command cmd : commandStatus.values()) {
                statusCounts.put(cmd.getStatus(), statusCounts.getOrDefault(cmd.getStatus(), 0L) + 1);
            }
            stats.put("statusCounts", statusCounts);
            
            // 类型分布
            Map<String, Long> typeCounts = new HashMap<>();
            for (Command cmd : commandQueue) {
                typeCounts.put(cmd.getType(), typeCounts.getOrDefault(cmd.getType(), 0L) + 1);
            }
            for (Command cmd : commandStatus.values()) {
                typeCounts.put(cmd.getType(), typeCounts.getOrDefault(cmd.getType(), 0L) + 1);
            }
            stats.put("typeCounts", typeCounts);
            
            response.put("status", "success");
            response.put("message", "Queue stats retrieved successfully");
            response.put("data", stats);
            response.put("timestamp", System.currentTimeMillis());
            
        } catch (Exception e) {
            log.error("Error getting queue stats: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "QUEUE_STATS_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        }
        
        return response;
    }
    
    /**
     * 添加命令到队列的内部方法
     */
    private Command addCommandToQueue(CommandData commandData) {
        String commandId = "cmd-" + commandIdGenerator.incrementAndGet();
        Command command = new Command(
            commandId,
            commandData.getType(),
            commandData.getTarget(),
            commandData.getParameters(),
            "queued",
            "Command added to queue",
            commandData.getSubmittedBy(),
            System.currentTimeMillis()
        );
        
        commandQueue.offer(command);
        
        return command;
    }
    
    // 命令数据传输对象
    private static class CommandData {
        private final String type;
        private final String target;
        private final Map<String, Object> parameters;
        private final String submittedBy;
        
        public CommandData(String type, String target, Map<String, Object> parameters, String submittedBy) {
            this.type = type;
            this.target = target;
            this.parameters = parameters;
            this.submittedBy = submittedBy;
        }
        
        public String getType() { return type; }
        public String getTarget() { return target; }
        public Map<String, Object> getParameters() { return parameters; }
        public String getSubmittedBy() { return submittedBy; }
        
        @Override
        public String toString() {
            return "CommandData{type='" + type + "', target='" + target + "', parameters=" + parameters + "}";
        }
    }
    
    // 命令类
    private static class Command {
        private final String id;
        private final String type;
        private final String target;
        private final Map<String, Object> parameters;
        private String status;
        private String message;
        private final String submittedBy;
        private final long submittedAt;
        private long startedAt;
        private long completedAt;
        private int retryCount;
        
        public Command(String id, String type, String target, Map<String, Object> parameters,
                      String status, String message, String submittedBy, long submittedAt) {
            this.id = id;
            this.type = type;
            this.target = target;
            this.parameters = parameters;
            this.status = status;
            this.message = message;
            this.submittedBy = submittedBy;
            this.submittedAt = submittedAt;
            this.startedAt = 0;
            this.completedAt = 0;
            this.retryCount = 0;
        }
        
        public void setStatus(String status) {
            this.status = status;
            if ("processing".equals(status)) {
                this.startedAt = System.currentTimeMillis();
            } else if ("completed".equals(status) || "failed".equals(status) || "cancelled".equals(status)) {
                this.completedAt = System.currentTimeMillis();
            }
        }
        
        public void setMessage(String message) {
            this.message = message;
        }
        
        public void incrementRetryCount() {
            this.retryCount++;
        }
        
        public Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("id", id);
            map.put("type", type);
            map.put("target", target);
            map.put("parameters", parameters);
            map.put("status", status);
            map.put("message", message);
            map.put("submittedBy", submittedBy);
            map.put("submittedAt", submittedAt);
            map.put("startedAt", startedAt);
            map.put("completedAt", completedAt);
            map.put("retryCount", retryCount);
            
            // 计算执行时间
            if (startedAt > 0 && completedAt > 0) {
                map.put("executionTime", completedAt - startedAt);
            }
            
            return map;
        }
        
        public String getId() { return id; }
        public String getType() { return type; }
        public String getTarget() { return target; }
        public Map<String, Object> getParameters() { return parameters; }
        public String getStatus() { return status; }
        public String getMessage() { return message; }
        public String getSubmittedBy() { return submittedBy; }
        public long getSubmittedAt() { return submittedAt; }
    }
}
