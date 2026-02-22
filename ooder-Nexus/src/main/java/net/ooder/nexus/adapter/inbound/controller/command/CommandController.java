package net.ooder.nexus.adapter.inbound.controller.command;

import net.ooder.nexus.common.ResultModel;
import net.ooder.nexus.core.skill.NexusSkill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * 命令体系管理控制器
 * 处理命令发送、队列管理、状态查询、网络链接等操作
 *
 * @author ooder Team
 * @version 0.7.0
 * @since 0.7.0
 */
@RestController
@RequestMapping("/api/command")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS})
public class CommandController {

    private static final Logger log = LoggerFactory.getLogger(CommandController.class);

    @Autowired
    private NexusSkill nexusSkill;

    private final PriorityBlockingQueue<Command> commandQueue = new PriorityBlockingQueue<>(1000, Comparator.comparing(Command::getPriority).reversed());
    private final Map<String, CommandStatus> commandStatusMap = new ConcurrentHashMap<>();
    private final Map<String, NetworkLink> networkLinks = new ConcurrentHashMap<>();

    @PostMapping("/send")
    @ResponseBody
    public ResultModel<Map<String, Object>> sendCommand(@RequestBody CommandRequest request) {
        log.info("Command send requested: {}", request);

        try {
            String commandId = "cmd-" + UUID.randomUUID().toString().substring(0, 8);

            Command command = new Command();
            command.setId(commandId);
            command.setType(request.getCommandType());
            command.setTargetAgent(request.getTargetAgent());
            command.setParameters(request.getParameters());
            command.setTimeout(request.getTimeout());
            command.setPriority(request.getPriority());
            command.setSubmitTime(System.currentTimeMillis());
            command.setStatus(CommandStatus.Status.PENDING);

            commandQueue.offer(command);

            CommandStatus status = new CommandStatus();
            status.setCommandId(commandId);
            status.setStatus(CommandStatus.Status.PENDING);
            status.setSubmitTime(command.getSubmitTime());
            status.setQueuePosition(commandQueue.size());
            commandStatusMap.put(commandId, status);

            processCommandAsync(command);

            Map<String, Object> data = new HashMap<>();
            data.put("commandId", commandId);
            data.put("queuePosition", commandQueue.size());
            data.put("estimatedTime", "~" + (commandQueue.size() * 100) + "ms");

            return ResultModel.success(data);
        } catch (Exception e) {
            log.error("Error sending command: {}", e.getMessage(), e);
            return ResultModel.error("命令发送失败: " + e.getMessage(), 500);
        }
    }

    @PostMapping("/queue")
    @ResponseBody
    public ResultModel<Map<String, Object>> getCommandQueue() {
        log.info("Command queue requested");

        try {
            List<CommandInfo> queueInfo = new ArrayList<>();

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

            List<CommandStatus> processingCommands = new ArrayList<>();
            for (CommandStatus status : commandStatusMap.values()) {
                if (status.getStatus() == CommandStatus.Status.PROCESSING) {
                    processingCommands.add(status);
                }
            }

            Map<String, Object> stats = new HashMap<>();
            stats.put("totalCommands", commandStatusMap.size());
            stats.put("pendingCommands", queueArray.length);
            stats.put("processingCommands", processingCommands.size());
            stats.put("completedCommands", commandStatusMap.size() - queueArray.length - processingCommands.size());

            Map<String, Object> data = new HashMap<>();
            data.put("queue", queueInfo);
            data.put("stats", stats);

            return ResultModel.success(data);
        } catch (Exception e) {
            log.error("Error getting command queue: {}", e.getMessage(), e);
            return ResultModel.error("获取命令队列失败: " + e.getMessage(), 500);
        }
    }

    @PostMapping("/status")
    @ResponseBody
    public ResultModel<CommandStatus> getCommandStatus(@RequestBody Map<String, String> request) {
        String commandId = request.get("commandId");
        log.info("Command status requested: {}", commandId);

        try {
            CommandStatus status = commandStatusMap.get(commandId);

            if (status == null) {
                return ResultModel.error("命令不存在", 404);
            }

            return ResultModel.success(status);
        } catch (Exception e) {
            log.error("Error getting command status: {}", e.getMessage(), e);
            return ResultModel.error("获取命令状态失败: " + e.getMessage(), 500);
        }
    }

    @PostMapping("/cancel")
    @ResponseBody
    public ResultModel<Boolean> cancelCommand(@RequestBody Map<String, String> request) {
        String commandId = request.get("commandId");
        log.info("Command cancel requested: {}", commandId);

        try {
            CommandStatus status = commandStatusMap.get(commandId);

            if (status == null) {
                return ResultModel.error("命令不存在", 404);
            }

            if (status.getStatus() == CommandStatus.Status.COMPLETED || status.getStatus() == CommandStatus.Status.CANCELLED) {
                return ResultModel.error("命令无法取消", 400);
            }

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

            status.setStatus(CommandStatus.Status.CANCELLED);
            status.setCompleteTime(System.currentTimeMillis());

            return ResultModel.success(true);
        } catch (Exception e) {
            log.error("Error cancelling command: {}", e.getMessage(), e);
            return ResultModel.error("取消命令失败: " + e.getMessage(), 500);
        }
    }

    @PostMapping("/network/links")
    @ResponseBody
    public ResultModel<Map<String, Object>> getNetworkLinks() {
        log.info("Network links requested");

        try {
            List<NetworkLink> links = new ArrayList<>(networkLinks.values());

            Map<String, Object> data = new HashMap<>();
            data.put("links", links);
            data.put("count", links.size());

            return ResultModel.success(data);
        } catch (Exception e) {
            log.error("Error getting network links: {}", e.getMessage(), e);
            return ResultModel.error("获取网络链接失败: " + e.getMessage(), 500);
        }
    }

    @PostMapping("/network/link/add")
    @ResponseBody
    public ResultModel<Map<String, Object>> addNetworkLink(@RequestBody NetworkLinkRequest request) {
        log.info("Add network link requested: {}", request);

        try {
            String linkId = "link-" + UUID.randomUUID().toString().substring(0, 8);

            NetworkLink link = new NetworkLink();
            link.setId(linkId);
            link.setSourceAgent(request.getSourceAgent());
            link.setTargetAgent(request.getTargetAgent());
            link.setType(request.getType());
            link.setStatus(NetworkLink.Status.ACTIVE);
            link.setCreateTime(System.currentTimeMillis());

            networkLinks.put(linkId, link);

            Map<String, Object> data = new HashMap<>();
            data.put("linkId", linkId);

            return ResultModel.success(data);
        } catch (Exception e) {
            log.error("Error adding network link: {}", e.getMessage(), e);
            return ResultModel.error("添加网络链接失败: " + e.getMessage(), 500);
        }
    }

    @PostMapping("/stats")
    @ResponseBody
    public ResultModel<Map<String, Object>> getCommandStats(@RequestBody Map<String, Long> request) {
        Long startTime = request.get("startTime");
        Long endTime = request.get("endTime");
        long requestTime = System.currentTimeMillis();

        log.info("Command stats requested: startTime={}, endTime={}", startTime, endTime);

        try {
            if (startTime == null) {
                startTime = requestTime - (24 * 60 * 60 * 1000);
            }
            if (endTime == null) {
                endTime = requestTime;
            }

            Map<String, Integer> typeDistribution = new HashMap<>();
            Map<CommandStatus.Status, Integer> statusDistribution = new HashMap<>();
            List<ExecutionTrend> trends = new ArrayList<>();

            for (CommandStatus status : commandStatusMap.values()) {
                if (status.getSubmitTime() >= startTime && status.getSubmitTime() <= endTime) {
                    String commandType = status.getCommandType() != null ? status.getCommandType() : "UNKNOWN";
                    typeDistribution.merge(commandType, 1, Integer::sum);
                    statusDistribution.merge(status.getStatus(), 1, Integer::sum);
                }
            }

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
            stats.put("successRate", 0.95);
            stats.put("averageExecutionTime", 150);

            Map<String, Object> timeRange = new HashMap<>();
            timeRange.put("start", startTime);
            timeRange.put("end", endTime);

            Map<String, Object> data = new HashMap<>();
            data.put("stats", stats);
            data.put("timeRange", timeRange);

            return ResultModel.success(data);
        } catch (Exception e) {
            log.error("Error getting command stats: {}", e.getMessage(), e);
            return ResultModel.error("获取命令统计失败: " + e.getMessage(), 500);
        }
    }

    private void processCommandAsync(Command command) {
        new Thread(() -> {
            try {
                Thread.sleep(1000 + new Random().nextInt(2000));

                CommandStatus status = commandStatusMap.get(command.getId());
                if (status != null) {
                    status.setStatus(CommandStatus.Status.PROCESSING);
                    status.setStartTime(System.currentTimeMillis());
                }

                Thread.sleep(500 + new Random().nextInt(1500));

                if (status != null) {
                    status.setStatus(CommandStatus.Status.COMPLETED);
                    status.setCompleteTime(System.currentTimeMillis());
                    status.setExecutionTime(status.getCompleteTime() - status.getStartTime());
                    status.setResult("Command executed successfully");
                }

                log.info("Command processed: {}", command.getId());
            } catch (Exception e) {
                log.error("Error processing command: {}", e.getMessage(), e);

                CommandStatus status = commandStatusMap.get(command.getId());
                if (status != null) {
                    status.setStatus(CommandStatus.Status.ERROR);
                    status.setCompleteTime(System.currentTimeMillis());
                    status.setError(e.getMessage());
                }
            }
        }).start();
    }

    public static class CommandRequest {
        private String commandType;
        private String targetAgent;
        private Map<String, Object> parameters;
        private long timeout;
        private String priority;

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

        public long getTime() { return time; }
        public void setTime(long time) { this.time = time; }
        public int getCount() { return count; }
        public void setCount(int count) { this.count = count; }
    }
}
