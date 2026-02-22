package net.ooder.nexus.debug.simulator;

import net.ooder.nexus.debug.model.ExecutionResult;
import net.ooder.nexus.debug.model.StepResult;
import net.ooder.nexus.debug.model.Scenario;
import net.ooder.nexus.debug.model.ScenarioStep;
import net.ooder.nexus.debug.model.Simulator;
import net.ooder.nexus.debug.model.SimulatorType;

/**
 * 协议模拟器接口
 */
public interface ProtocolSimulator {

    /**
     * 获取模拟器ID
     */
    String getSimulatorId();

    /**
     * 获取模拟器类型
     */
    SimulatorType getType();

    /**
     * 获取支持的协议类型
     */
    String getProtocolType();

    /**
     * 获取模拟器名称
     */
    String getName();

    /**
     * 初始化模拟器
     */
    void initialize(Simulator config);

    /**
     * 启动模拟器
     */
    void start();

    /**
     * 停止模拟器
     */
    void stop();

    /**
     * 执行单个命令
     */
    ExecutionResult executeCommand(String commandType, java.util.Map<String, Object> payload);

    /**
     * 执行场景
     */
    ExecutionResult executeScenario(Scenario scenario);

    /**
     * 执行单个步骤
     */
    StepResult executeStep(ScenarioStep step);

    /**
     * 获取模拟器状态
     */
    SimulatorStatus getStatus();

    /**
     * 获取执行日志
     */
    java.util.List<ExecutionLog> getLogs();

    /**
     * 清空日志
     */
    void clearLogs();

    /**
     * 模拟器状态
     */
    class SimulatorStatus {
        private String status;
        private long uptime;
        private int commandsExecuted;
        private int errors;

        public SimulatorStatus() {
            this.status = "STOPPED";
            this.uptime = 0;
            this.commandsExecuted = 0;
            this.errors = 0;
        }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public long getUptime() { return uptime; }
        public void setUptime(long uptime) { this.uptime = uptime; }
        public int getCommandsExecuted() { return commandsExecuted; }
        public void setCommandsExecuted(int commandsExecuted) { this.commandsExecuted = commandsExecuted; }
        public int getErrors() { return errors; }
        public void setErrors(int errors) { this.errors = errors; }
    }

    /**
     * 执行日志
     */
    class ExecutionLog {
        private long timestamp;
        private String level;
        private String message;
        private String commandType;
        private long duration;

        public ExecutionLog() {
            this.timestamp = System.currentTimeMillis();
        }

        public ExecutionLog(String level, String message) {
            this();
            this.level = level;
            this.message = message;
        }

        public ExecutionLog(String level, String message, String commandType, long duration) {
            this(level, message);
            this.commandType = commandType;
            this.duration = duration;
        }

        public long getTimestamp() { return timestamp; }
        public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
        public String getLevel() { return level; }
        public void setLevel(String level) { this.level = level; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public String getCommandType() { return commandType; }
        public void setCommandType(String commandType) { this.commandType = commandType; }
        public long getDuration() { return duration; }
        public void setDuration(long duration) { this.duration = duration; }
    }
}
