package net.ooder.sdk.command.api;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.sdk.command.model.CommandType;
import net.ooder.sdk.command.model.CommandTypeSerializer;
import net.ooder.sdk.command.model.CommandTypeDeserializer;

import java.util.UUID;

/**
 * 命令抽象类，所有具体命令的父类
 * 采用贫血模型设计，仅包含数据字段和getter/setter方法
 */
public abstract class Command {
    /**
     * 命令ID，唯一标识一个命令
     */
    @JSONField(name = "commandId")
    private String commandId;

    /**
     * 命令类型
     */
    @JSONField(name = "commandType", serializeUsing = CommandTypeSerializer.class, deserializeUsing = CommandTypeDeserializer.class)
    private CommandType commandType;

    /**
     * 命令创建时间戳
     */
    @JSONField(name = "timestamp")
    private long timestamp;

    /**
     * 发送者ID
     */
    @JSONField(name = "senderId")
    private String senderId;

    /**
     * 接收者ID
     */
    @JSONField(name = "receiverId")
    private String receiverId;

    /**
     * 命令优先级
     */
    @JSONField(name = "priority")
    private String priority;

    /**
     * 命令超时时间（毫秒）
     */
    @JSONField(name = "timeout")
    private long timeout;

    /**
     * 默认构造方法
     */
    protected Command() {
        this.commandId = UUID.randomUUID().toString();
        this.timestamp = System.currentTimeMillis();
        this.priority = "normal";
        this.timeout = 30000; // 默认超时30秒
    }

    /**
     * 带命令类型的构造方法
     * @param commandType 命令类型
     */
    protected Command(CommandType commandType) {
        this();
        this.commandType = commandType;
    }

    // Getter和Setter方法
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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
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
        return "Command{" +
                "commandId='" + commandId + '\'' +
                ", commandType=" + commandType +
                ", timestamp=" + timestamp +
                ", senderId='" + senderId + '\'' +
                ", receiverId='" + receiverId + '\'' +
                ", priority='" + priority + '\'' +
                ", timeout=" + timeout +
                '}';
    }
}