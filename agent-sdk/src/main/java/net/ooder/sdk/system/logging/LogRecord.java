package net.ooder.sdk.system.logging;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Map;

/**
 * 日志记录类，用于持久化存储日志信息
 */
public class LogRecord {

    /**
     * 日志记录ID，唯一标识一条日志记录
     */
    @JSONField(name = "logId")
    private String logId;

    /**
     * 日志时间戳
     */
    @JSONField(name = "timestamp")
    private long timestamp;

    /**
     * 日志级别
     */
    @JSONField(name = "level")
    private String level;

    /**
     * 日志来源（如类名、组件名）
     */
    @JSONField(name = "source")
    private String source;

    /**
     * 日志消息
     */
    @JSONField(name = "message")
    private String message;

    /**
     * 关联的命令ID（可选）
     */
    @JSONField(name = "commandId")
    private String commandId;

    /**
     * 关联的技能ID（可选）
     */
    @JSONField(name = "skillId")
    private String skillId;

    /**
     * 关联的路由ID（可选）
     */
    @JSONField(name = "routeId")
    private String routeId;

    /**
     * 错误信息（可选）
     */
    @JSONField(name = "error")
    private String error;

    /**
     * 堆栈跟踪信息（可选）
     */
    @JSONField(name = "stackTrace")
    private String stackTrace;

    /**
     * 附加信息（可选）
     */
    @JSONField(name = "extra")
    private Map<String, Object> extra;

    /**
     * 构造方法
     */
    public LogRecord() {
        this.timestamp = System.currentTimeMillis();
    }

    // Getter and Setter methods

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCommandId() {
        return commandId;
    }

    public void setCommandId(String commandId) {
        this.commandId = commandId;
    }

    public String getSkillId() {
        return skillId;
    }

    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    public Map<String, Object> getExtra() {
        return extra;
    }

    public void setExtra(Map<String, Object> extra) {
        this.extra = extra;
    }

    @Override
    public String toString() {
        return "LogRecord{" +
                "logId='" + logId + '\'' +
                ", timestamp=" + timestamp +
                ", level='" + level + '\'' +
                ", source='" + source + '\'' +
                ", message='" + message + '\'' +
                ", commandId='" + commandId + '\'' +
                '}';
    }
}
