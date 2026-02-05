package net.ooder.mcpagent.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import net.ooder.mcpagent.skill.McpAgentSkill;
import net.ooder.mcpagent.skill.impl.McpAgentSkillImpl;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * 命令体系管理控制台控制器
 */
@RestController
@RequestMapping("/api/command")
public class CommandController {

    private static final Logger log = LoggerFactory.getLogger(CommandController.class);

    @Autowired
    private McpAgentSkill mcpAgentSkill;

    // 命令队列 - 模拟实现
    private final PriorityBlockingQueue<Command> commandQueue = new PriorityBlockingQueue<>(1000, Comparator.comparing(Command::getPriority).reversed());
    
    // 命令状态存储
    private final Map<String, CommandStatus> commandStatusMap = new ConcurrentHashMap<>();
    
    // 网络链接存储
    private final Map<String, NetworkLink> networkLinks = new ConcurrentHashMap<>();

    /**
     * 发送命令
     */
    @PostMapping("/send")
    public Map<String, Object> sendCommand(@RequestBody CommandRequest request) {
        long startTime = System.currentTimeMillis();
        log.info("Command send requested: {}", request);
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 生成命令ID
            String commandId = "cmd-" + UUID.randomUUID().toString().substring(0, 8);
            
            // 创建命令对象
            Command command = new Command();
            command.setId(commandId);
            command.setType(request.getCommandType());
            command.setTargetAgent(request.getTargetAgent());
            command.setParameters(request.getParameters());
            command.setTimeout(request.getTimeout());
            command.setPriority(request.getPriority());
            command.setSubmitTime(System.currentTimeMillis());
            command.setStatus(CommandStatus.Status.PENDING);
            
            // 添加到命令队列
            commandQueue.offer(command);
            
            // 存储命令状态
            CommandStatus status = new CommandStatus();
            status.setCommandId(commandId);
            status.setStatus(CommandStatus.Status.PENDING);
            status.setSubmitTime(command.getSubmitTime());
            status.setQueuePosition(commandQueue.size());
            commandStatusMap.put(commandId, status);
            
            // 模拟异步处理命令
            processCommandAsync(command);
            
            response.put("status", "success");
            response.put("commandId", commandId);
            response.put("message", "命令发送成功");
            response.put("queuePosition", commandQueue.size());
            response.put("estimatedTime", "~" + (commandQueue.size() * 100) + "ms");
            response.put("timestamp", System.currentTimeMillis());
            
            log.info("Command sent successfully: {}, type: {}, target: {}", commandId, request.getCommandType(), request.getTargetAgent());
        } catch (Exception e) {
            log.error("Error sending command: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "COMMAND_SEND_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("Command send processing completed in {}ms", endTime - startTime);
        }
        
        return response;
    }

    /**
     * 获取命令队列
     */
    @GetMapping("/queue")
    public Map<String, Object> getCommandQueue() {
        long startTime = System.currentTimeMillis();
        log.info("Command queue requested");
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<CommandInfo> queueInfo = new ArrayList<>();
            
            // 复制队列内容以避免并发修改
            Command[] queueArray = commandQueue.toArray(new Command[0]);
            for (Command command : queueArray) {
                CommandInfo info = new CommandInfo();
                info.setId(command.getId());
                info.setType(command.getType());
                info.setTargetAgent(command.getTargetAgent());
                info.setPriority(command.getPriority());
                info.setStatus(command.getStatus().name());
                info.setSubmitTime(command.getSubmitTime());
                info.setEstimatedTime("~" + (commandQueue.size() * 100) + "ms");
                queueInfo.add(info);
            }
            
            // 获取执行中的命令
            List<CommandStatus> processingCommands = new ArrayList<>();
            for (CommandStatus status : commandStatusMap.values()) {
                if (status.getStatus() == CommandStatus.Status.PROCESSING) {
                    processingCommands.add(status);
                }
            }
            
            // 计算统计信息
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalCommands", commandStatusMap.size());
            stats.put("pendingCommands", queueArray.length);
            stats.put("processingCommands", processingCommands.size());
            stats.put("completedCommands", commandStatusMap.size() - queueArray.length - processingCommands.size());
            
            response.put("status", "success");
            response.put("data", queueInfo);
            response.put("stats", stats);
            response.put("timestamp", System.currentTimeMillis());
        } catch (Exception e) {
            log.error("Error getting command queue: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "QUEUE_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("Command queue query completed in {}ms", endTime - startTime);
        }
        
        return response;
    }

    /**
     * 获取命令状态
     */
    @GetMapping("/status/{commandId}")
    public Map<String, Object> getCommandStatus(@PathVariable String commandId) {
        long startTime = System.currentTimeMillis();
        log.info("Command status requested: {}", commandId);
        Map<String, Object> response = new HashMap<>();
        
        try {
            CommandStatus status = commandStatusMap.get(commandId);
            
            if (status == null) {
                response.put("status", "error");
                response.put("message", "Command not found");
                response.put("code", "COMMAND_NOT_FOUND");
            } else {
                response.put("status", "success");
                response.put("data", status);
                response.put("timestamp", System.currentTimeMillis());
            }
        } catch (Exception e) {
            log.error("Error getting command status: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "STATUS_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("Command status query completed in {}ms", endTime - startTime);
        }
        
        return response;
    }

    /**
     * 取消命令
     */
    @PostMapping("/cancel/{commandId}")
    public Map<String, Object> cancelCommand(@PathVariable String commandId) {
        long startTime = System.currentTimeMillis();
        log.info("Command cancel requested: {}", commandId);
        Map<String, Object> response = new HashMap<>();
        
        try {
            CommandStatus status = commandStatusMap.get(commandId);
            
            if (status == null) {
                response.put("status", "error");
                response.put("message", "Command not found");
                response.put("code", "COMMAND_NOT_FOUND");
            } else if (status.getStatus() == CommandStatus.Status.COMPLETED || status.getStatus() == CommandStatus.Status.CANCELLED) {
                response.put("status", "error");
                response.put("message", "Command cannot be cancelled");
                response.put("code", "COMMAND_CANCEL_ERROR");
            } else {
                // 从队列中移除
                Command commandToRemove = null;
                for (Command command : commandQueue) {
                    if (command.getId().equals(commandId)) {
                        commandToRemove = command;
                        break;
                    }
                }
                if (commandToRemove != null) {
                    commandQueue.remove(commandToRemove);
                }
                
                // 更新状态
                status.setStatus(CommandStatus.Status.CANCELLED);
                status.setCompleteTime(System.currentTimeMillis());
                
                response.put("status", "success");
                response.put("message", "Command cancelled successfully");
                response.put("commandId", commandId);
                response.put("timestamp", System.currentTimeMillis());
            }
        } catch (Exception e) {
            log.error("Error cancelling command: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "CANCEL_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("Command cancel processing completed in {}ms", endTime - startTime);
        }
        
        return response;
    }

    /**
     * 获取网络链接列表
     */
    @GetMapping("/network/links")
    public Map<String, Object> getNetworkLinks() {
        long startTime = System.currentTimeMillis();
        log.info("Network links requested");
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<NetworkLink> links = new ArrayList<>(networkLinks.values());
            
            response.put("status", "success");
            response.put("data", links);
            response.put("count", links.size());
            response.put("timestamp", System.currentTimeMillis());
        } catch (Exception e) {
            log.error("Error getting network links: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "LINKS_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("Network links query completed in {}ms", endTime - startTime);
        }
        
        return response;
    }

    /**
     * 添加网络链接
     */
    @PostMapping("/network/link")
    public Map<String, Object> addNetworkLink(@RequestBody NetworkLinkRequest request) {
        long startTime = System.currentTimeMillis();
        log.info("Add network link requested: {}", request);
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 生成链接ID
            String linkId = "link-" + UUID.randomUUID().toString().substring(0, 8);
            
            // 创建网络链接
            NetworkLink link = new NetworkLink();
            link.setId(linkId);
            link.setSourceAgent(request.getSourceAgent());
            link.setTargetAgent(request.getTargetAgent());
            link.setType(request.getType());
            link.setStatus(NetworkLink.Status.ACTIVE);
            link.setCreateTime(System.currentTimeMillis());
            
            // 存储链接
            networkLinks.put(linkId, link);
            
            response.put("status", "success");
            response.put("message", "Network link added successfully");
            response.put("linkId", linkId);
            response.put("timestamp", System.currentTimeMillis());
        } catch (Exception e) {
            log.error("Error adding network link: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "LINK_ADD_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("Add network link processing completed in {}ms", endTime - startTime);
        }
        
        return response;
    }

    /**
     * 获取命令统计分析
     */
    @GetMapping("/stats")
    public Map<String, Object> getCommandStats(@RequestParam(required = false) Long startTime, 
                                              @RequestParam(required = false) Long endTime) {
        long requestTime = System.currentTimeMillis();
        log.info("Command stats requested: startTime={}, endTime={}", startTime, endTime);
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 计算时间范围
            if (startTime == null) {
                startTime = requestTime - (24 * 60 * 60 * 1000); // 默认24小时
            }
            if (endTime == null) {
                endTime = requestTime;
            }
            
            // 统计命令类型分布
            Map<String, Integer> typeDistribution = new HashMap<>();
            
            // 统计命令状态分布
            Map<CommandStatus.Status, Integer> statusDistribution = new HashMap<>();
            
            // 统计执行时间趋势
            List<ExecutionTrend> trends = new ArrayList<>();
            
            // 模拟统计数据
            for (CommandStatus status : commandStatusMap.values()) {
                if (status.getSubmitTime() >= startTime && status.getSubmitTime() <= endTime) {
                    // 类型分布
                    String commandType = status.getCommandType() != null ? status.getCommandType() : "UNKNOWN";
                    typeDistribution.merge(commandType, 1, Integer::sum);
                    
                    // 状态分布
                    statusDistribution.merge(status.getStatus(), 1, Integer::sum);
                }
            }
            
            // 生成趋势数据
            for (int i = 23; i >= 0; i--) {
                long time = requestTime - (i * 60 * 60 * 1000);
                ExecutionTrend trend = new ExecutionTrend();
                trend.setTime(time);
                trend.setCount(new Random().nextInt(10) + 1);
                trends.add(trend);
            }
            
            Map<String, Object> stats = new HashMap<>();
            stats.put("typeDistribution", typeDistribution);
            stats.put("statusDistribution", statusDistribution);
            stats.put("executionTrend", trends);
            stats.put("totalCommands", commandStatusMap.size());
            stats.put("successRate", 0.95); // 模拟成功率
            stats.put("averageExecutionTime", 150); // 模拟平均执行时间(ms)
            
            response.put("status", "success");
            response.put("data", stats);
            Map<String, Long> timeRange = new HashMap<>();
            timeRange.put("start", startTime);
            timeRange.put("end", endTime);
            response.put("timeRange", timeRange);
            response.put("timestamp", System.currentTimeMillis());
        } catch (Exception e) {
            log.error("Error getting command stats: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "STATS_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        } finally {
            long finishTime = System.currentTimeMillis();
            log.info("Command stats query completed in {}ms", finishTime - requestTime);
        }
        
        return response;
    }

    /**
     * 异步处理命令
     */
    private void processCommandAsync(Command command) {
        new Thread(() -> {
            try {
                // 模拟命令处理
                Thread.sleep(1000 + new Random().nextInt(2000)); // 1-3秒
                
                // 更新命令状态
                CommandStatus status = commandStatusMap.get(command.getId());
                if (status != null) {
                    status.setStatus(CommandStatus.Status.PROCESSING);
                    status.setStartTime(System.currentTimeMillis());
                }
                
                // 模拟命令执行
                Thread.sleep(500 + new Random().nextInt(1500)); // 0.5-2秒
                
                // 完成命令
                if (status != null) {
                    status.setStatus(CommandStatus.Status.COMPLETED);
                    status.setCompleteTime(System.currentTimeMillis());
                    status.setExecutionTime(status.getCompleteTime() - status.getStartTime());
                    status.setResult("Command executed successfully");
                }
                
                log.info("Command processed: {}", command.getId());
            } catch (Exception e) {
                log.error("Error processing command: {}", e.getMessage(), e);
                
                // 更新为错误状态
                CommandStatus status = commandStatusMap.get(command.getId());
                if (status != null) {
                    status.setStatus(CommandStatus.Status.ERROR);
                    status.setCompleteTime(System.currentTimeMillis());
                    status.setError(e.getMessage());
                }
            }
        }).start();
    }

    // 内部类定义
    public static class CommandRequest {
        private String commandType;
        private String targetAgent;
        private Map<String, Object> parameters;
        private long timeout;
        private String priority;

        // getters and setters
        public String getCommandType() { return commandType; }
        public void setCommandType(String commandType) { this.commandType = commandType; }
        public String getTargetAgent() { return targetAgent; }
        public void setTargetAgent(String targetAgent) { this.targetAgent = targetAgent; }
        public Map<String, Object> getParameters() { return parameters; }
        public void setParameters(Map<String, Object> parameters) { this.parameters = parameters; }
        public long getTimeout() { return timeout; }
        public void setTimeout(long timeout) { this.timeout = timeout; }
        public String getPriority() { return priority; }
        public void setPriority(String priority) { this.priority = priority; }
    }

    public static class Command {
        private String id;
        private String type;
        private String targetAgent;
        private Map<String, Object> parameters;
        private long timeout;
        private String priority;
        private long submitTime;
        private CommandStatus.Status status;

        // getters and setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getTargetAgent() { return targetAgent; }
        public void setTargetAgent(String targetAgent) { this.targetAgent = targetAgent; }
        public Map<String, Object> getParameters() { return parameters; }
        public void setParameters(Map<String, Object> parameters) { this.parameters = parameters; }
        public long getTimeout() { return timeout; }
        public void setTimeout(long timeout) { this.timeout = timeout; }
        public String getPriority() { return priority; }
        public void setPriority(String priority) { this.priority = priority; }
        public long getSubmitTime() { return submitTime; }
        public void setSubmitTime(long submitTime) { this.submitTime = submitTime; }
        public CommandStatus.Status getStatus() { return status; }
        public void setStatus(CommandStatus.Status status) { this.status = status; }
    }

    public static class CommandStatus {
        private String commandId;
        private String commandType;
        private Status status;
        private long submitTime;
        private long startTime;
        private long completeTime;
        private long executionTime;
        private int queuePosition;
        private String result;
        private String error;

        public enum Status {
            PENDING, PROCESSING, COMPLETED, ERROR, CANCELLED
        }

        // getters and setters
        public String getCommandId() { return commandId; }
        public void setCommandId(String commandId) { this.commandId = commandId; }
        public String getCommandType() { return commandType; }
        public void setCommandType(String commandType) { this.commandType = commandType; }
        public Status getStatus() { return status; }
        public void setStatus(Status status) { this.status = status; }
        public long getSubmitTime() { return submitTime; }
        public void setSubmitTime(long submitTime) { this.submitTime = submitTime; }
        public long getStartTime() { return startTime; }
        public void setStartTime(long startTime) { this.startTime = startTime; }
        public long getCompleteTime() { return completeTime; }
        public void setCompleteTime(long completeTime) { this.completeTime = completeTime; }
        public long getExecutionTime() { return executionTime; }
        public void setExecutionTime(long executionTime) { this.executionTime = executionTime; }
        public int getQueuePosition() { return queuePosition; }
        public void setQueuePosition(int queuePosition) { this.queuePosition = queuePosition; }
        public String getResult() { return result; }
        public void setResult(String result) { this.result = result; }
        public String getError() { return error; }
        public void setError(String error) { this.error = error; }
    }

    public static class CommandInfo {
        private String id;
        private String type;
        private String targetAgent;
        private String priority;
        private String status;
        private long submitTime;
        private String estimatedTime;

        // getters and setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getTargetAgent() { return targetAgent; }
        public void setTargetAgent(String targetAgent) { this.targetAgent = targetAgent; }
        public String getPriority() { return priority; }
        public void setPriority(String priority) { this.priority = priority; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public long getSubmitTime() { return submitTime; }
        public void setSubmitTime(long submitTime) { this.submitTime = submitTime; }
        public String getEstimatedTime() { return estimatedTime; }
        public void setEstimatedTime(String estimatedTime) { this.estimatedTime = estimatedTime; }
    }

    public static class NetworkLink {
        private String id;
        private String sourceAgent;
        private String targetAgent;
        private String type;
        private Status status;
        private long createTime;

        public enum Status {
            ACTIVE, INACTIVE, FAILED
        }

        // getters and setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getSourceAgent() { return sourceAgent; }
        public void setSourceAgent(String sourceAgent) { this.sourceAgent = sourceAgent; }
        public String getTargetAgent() { return targetAgent; }
        public void setTargetAgent(String targetAgent) { this.targetAgent = targetAgent; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public Status getStatus() { return status; }
        public void setStatus(Status status) { this.status = status; }
        public long getCreateTime() { return createTime; }
        public void setCreateTime(long createTime) { this.createTime = createTime; }
    }

    public static class NetworkLinkRequest {
        private String sourceAgent;
        private String targetAgent;
        private String type;

        // getters and setters
        public String getSourceAgent() { return sourceAgent; }
        public void setSourceAgent(String sourceAgent) { this.sourceAgent = sourceAgent; }
        public String getTargetAgent() { return targetAgent; }
        public void setTargetAgent(String targetAgent) { this.targetAgent = targetAgent; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
    }

    public static class ExecutionTrend {
        private long time;
        private int count;

        // getters and setters
        public long getTime() { return time; }
        public void setTime(long time) { this.time = time; }
        public int getCount() { return count; }
        public void setCount(int count) { this.count = count; }
    }
}
