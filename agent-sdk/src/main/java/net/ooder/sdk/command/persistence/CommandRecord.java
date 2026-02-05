package net.ooder.sdk.command.persistence;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.util.DateUtility;
import net.ooder.sdk.command.model.CommandType;
import net.ooder.sdk.system.enums.CommandStatus;

import java.util.Map;

/**
 * 命令执行记录类，用于持久化存储命令执行状态和结果
 */
public class CommandRecord {

    /**
     * 命令记录ID，唯一标识一条命令记录
     */
    @JSONField(name = "recordId")
    private String recordId;

    /**
     * 命令ID，对应Command对象的commandId
     */
    @JSONField(name = "commandId")
    private String commandId;

    /**
     * 命令类型
     */
    @JSONField(name = "commandType")
    private CommandType commandType;

    /**
     * 命令发送者ID
     */
    @JSONField(name = "senderId")
    private String senderId;

    /**
     * 命令接收者ID
     */
    @JSONField(name = "receiverId")
    private String receiverId;

    /**
     * 命令创建时间戳
     */
    @JSONField(name = "createdTime")
    private String createdTime;

    /**
     * 命令更新时间戳
     */
    @JSONField(name = "updatedTime")
    private long updatedTime;

    /**
     * 命令执行状态
     */
    @JSONField(name = "status")
    private CommandStatus status;

    /**
     * 执行结果
     */
    @JSONField(name = "result")
    private Map<String, Object> result;

    /**
     * 错误信息
     */
    @JSONField(name = "errorMessage")
    private String errorMessage;

    /**
     * 重试次数
     */
    @JSONField(name = "retryCount")
    private int retryCount;

    /**
     * 最大重试次数
     */
    @JSONField(name = "maxRetryCount")
    private int maxRetryCount;

    /**
     * 下一次重试时间戳
     */
    @JSONField(name = "nextRetryTime")
    private long nextRetryTime;

    /**
     * 命令参数
     */
    @JSONField(name = "parameters")
    private Map<String, Object> parameters;

    /**
     * 命令优先级
     */
    @JSONField(name = "priority")
    private String priority;

    /**
     * 命令超时时间
     */
    @JSONField(name = "timeout")
    private long timeout;

    /**
     * 构造方法
     */
    public CommandRecord() {
        this.createdTime =DateUtility.getCurrentTime();
        this.updatedTime = System.currentTimeMillis();
        this.status = CommandStatus.PENDING;
        this.retryCount = 0;
        this.maxRetryCount = 3; // 默认最大重试次数
    }

    // Getter and Setter methods

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getCommandId() {
        return commandId;
    }

    public void setCommandId(String commandId) {
        this.commandId = commandId;
    }

    public CommandType getCommandType() {
        return commandType;
    }

    public void setCommandType(CommandType commandType) {
        this.commandType = commandType;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public long getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(long updatedTime) {
        this.updatedTime = updatedTime;
    }

    public CommandStatus getStatus() {
        return status;
    }

    public void setStatus(CommandStatus status) {
        this.status = status;
    }

    public Map<String, Object> getResult() {
        return result;
    }

    public void setResult(Map<String, Object> result) {
        this.result = result;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public int getMaxRetryCount() {
        return maxRetryCount;
    }

    public void setMaxRetryCount(int maxRetryCount) {
        this.maxRetryCount = maxRetryCount;
    }

    public long getNextRetryTime() {
        return nextRetryTime;
    }

    public void setNextRetryTime(long nextRetryTime) {
        this.nextRetryTime = nextRetryTime;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    @Override
    public String toString() {
        return "CommandRecord{" +
                "recordId='" + recordId + '\'' +
                ", commandId='" + commandId + '\'' +
                ", commandType='" + commandType + '\'' +
                ", senderId='" + senderId + '\'' +
                ", receiverId='" + receiverId + '\'' +
                ", createdTime=" + createdTime +
                ", updatedTime=" + updatedTime +
                ", status=" + status +
                ", retryCount=" + retryCount +
                ", maxRetryCount=" + maxRetryCount +
                '}';
    }
}
