package net.ooder.nexus.provider;

import net.ooder.scene.core.Result;
import net.ooder.scene.core.PageResult;
import net.ooder.scene.core.SceneEngine;
import net.ooder.scene.provider.BaseProvider;

import java.util.List;
import java.util.Map;

/**
 * 终端代理Provider接口
 *
 * <p>定义终端代理管理相关的操作接口</p>
 * <p>注：此接口在 scene-engine 0.7.3 中不存在，由 ooderNexus 自行定义</p>
 */
public interface AgentProvider extends BaseProvider {

    /**
     * 获取代理状态
     */
    Result<AgentStatus> getStatus();

    /**
     * 获取代理统计
     */
    Result<AgentStats> getStats();

    /**
     * 分页获取代理列表
     */
    Result<PageResult<EndAgent>> listAgents(int page, int size);

    /**
     * 获取代理详情
     */
    Result<EndAgent> getAgent(String agentId);

    /**
     * 注册代理
     */
    Result<EndAgent> registerAgent(Map<String, Object> agentData);

    /**
     * 更新代理
     */
    Result<EndAgent> updateAgent(String agentId, Map<String, Object> agentData);

    /**
     * 注销代理
     */
    Result<Boolean> unregisterAgent(String agentId);

    /**
     * 执行命令
     */
    Result<CommandResult> executeCommand(String agentId, String command);

    /**
     * 测试命令
     */
    Result<CommandResult> testCommand(Map<String, Object> commandData);

    /**
     * 获取代理日志
     */
    Result<PageResult<AgentLog>> getAgentLogs(String agentId, int page, int size);

    /**
     * 代理状态
     */
    class AgentStatus {
        private String status;
        private int totalAgents;
        private int activeAgents;
        private int inactiveAgents;
        private long lastUpdated;

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public int getTotalAgents() { return totalAgents; }
        public void setTotalAgents(int totalAgents) { this.totalAgents = totalAgents; }
        public int getActiveAgents() { return activeAgents; }
        public void setActiveAgents(int activeAgents) { this.activeAgents = activeAgents; }
        public int getInactiveAgents() { return inactiveAgents; }
        public void setInactiveAgents(int inactiveAgents) { this.inactiveAgents = inactiveAgents; }
        public long getLastUpdated() { return lastUpdated; }
        public void setLastUpdated(long lastUpdated) { this.lastUpdated = lastUpdated; }
    }

    /**
     * 代理统计
     */
    class AgentStats {
        private long totalAgents;
        private long activeAgents;
        private long inactiveAgents;
        private long totalCommands;
        private long successCommands;
        private long failedCommands;

        public long getTotalAgents() { return totalAgents; }
        public void setTotalAgents(long totalAgents) { this.totalAgents = totalAgents; }
        public long getActiveAgents() { return activeAgents; }
        public void setActiveAgents(long activeAgents) { this.activeAgents = activeAgents; }
        public long getInactiveAgents() { return inactiveAgents; }
        public void setInactiveAgents(long inactiveAgents) { this.inactiveAgents = inactiveAgents; }
        public long getTotalCommands() { return totalCommands; }
        public void setTotalCommands(long totalCommands) { this.totalCommands = totalCommands; }
        public long getSuccessCommands() { return successCommands; }
        public void setSuccessCommands(long successCommands) { this.successCommands = successCommands; }
        public long getFailedCommands() { return failedCommands; }
        public void setFailedCommands(long failedCommands) { this.failedCommands = failedCommands; }
    }

    /**
     * 终端代理
     */
    class EndAgent {
        private String agentId;
        private String name;
        private String type;
        private String ip;
        private String mac;
        private String status;
        private Map<String, Object> properties;
        private long createdAt;
        private long updatedAt;

        public String getAgentId() { return agentId; }
        public void setAgentId(String agentId) { this.agentId = agentId; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getIp() { return ip; }
        public void setIp(String ip) { this.ip = ip; }
        public String getMac() { return mac; }
        public void setMac(String mac) { this.mac = mac; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public Map<String, Object> getProperties() { return properties; }
        public void setProperties(Map<String, Object> properties) { this.properties = properties; }
        public long getCreatedAt() { return createdAt; }
        public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
        public long getUpdatedAt() { return updatedAt; }
        public void setUpdatedAt(long updatedAt) { this.updatedAt = updatedAt; }
    }

    /**
     * 命令执行结果
     */
    class CommandResult {
        private String commandId;
        private String agentId;
        private String command;
        private int exitCode;
        private String output;
        private String error;
        private long duration;
        private long timestamp;

        public String getCommandId() { return commandId; }
        public void setCommandId(String commandId) { this.commandId = commandId; }
        public String getAgentId() { return agentId; }
        public void setAgentId(String agentId) { this.agentId = agentId; }
        public String getCommand() { return command; }
        public void setCommand(String command) { this.command = command; }
        public int getExitCode() { return exitCode; }
        public void setExitCode(int exitCode) { this.exitCode = exitCode; }
        public String getOutput() { return output; }
        public void setOutput(String output) { this.output = output; }
        public String getError() { return error; }
        public void setError(String error) { this.error = error; }
        public long getDuration() { return duration; }
        public void setDuration(long duration) { this.duration = duration; }
        public long getTimestamp() { return timestamp; }
        public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    }

    /**
     * 代理日志
     */
    class AgentLog {
        private String logId;
        private String agentId;
        private String level;
        private String message;
        private String source;
        private long timestamp;

        public String getLogId() { return logId; }
        public void setLogId(String logId) { this.logId = logId; }
        public String getAgentId() { return agentId; }
        public void setAgentId(String agentId) { this.agentId = agentId; }
        public String getLevel() { return level; }
        public void setLevel(String level) { this.level = level; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public String getSource() { return source; }
        public void setSource(String source) { this.source = source; }
        public long getTimestamp() { return timestamp; }
        public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    }
}
