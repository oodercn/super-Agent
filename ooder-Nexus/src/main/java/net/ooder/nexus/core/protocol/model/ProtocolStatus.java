package net.ooder.nexus.core.protocol.model;

import java.io.Serializable;

/**
 * 协议状态
 */
public class ProtocolStatus implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 状态枚举
     */
    public enum Status {
        INITIALIZING,   // 初始化中
        RUNNING,        // 运行中
        PAUSED,         // 已暂停
        ERROR,          // 错误状态
        STOPPED         // 已停止
    }

    /**
     * 协议类型
     */
    private String protocolType;

    /**
     * 当前状态
     */
    private Status status;

    /**
     * 状态描述
     */
    private String description;

    /**
     * 最后错误信息
     */
    private String lastError;

    /**
     * 启动时间
     */
    private long startTime;

    /**
     * 最后活动时间
     */
    private long lastActiveTime;

    public ProtocolStatus() {
        this.status = Status.INITIALIZING;
        this.startTime = System.currentTimeMillis();
        this.lastActiveTime = this.startTime;
    }

    public ProtocolStatus(String protocolType) {
        this();
        this.protocolType = protocolType;
    }

    public boolean isRunning() {
        return status == Status.RUNNING;
    }

    public boolean isError() {
        return status == Status.ERROR;
    }

    public void markRunning() {
        this.status = Status.RUNNING;
        this.lastActiveTime = System.currentTimeMillis();
    }

    public void markError(String error) {
        this.status = Status.ERROR;
        this.lastError = error;
        this.lastActiveTime = System.currentTimeMillis();
    }

    public void markStopped() {
        this.status = Status.STOPPED;
        this.lastActiveTime = System.currentTimeMillis();
    }

    public void markPaused() {
        this.status = Status.PAUSED;
        this.lastActiveTime = System.currentTimeMillis();
    }

    public String getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(String protocolType) {
        this.protocolType = protocolType;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLastError() {
        return lastError;
    }

    public void setLastError(String lastError) {
        this.lastError = lastError;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getLastActiveTime() {
        return lastActiveTime;
    }

    public void setLastActiveTime(long lastActiveTime) {
        this.lastActiveTime = lastActiveTime;
    }

    @Override
    public String toString() {
        return "ProtocolStatus{" +
                "protocolType='" + protocolType + '\'' +
                ", status=" + status +
                ", description='" + description + '\'' +
                ", lastError='" + lastError + '\'' +
                ", startTime=" + startTime +
                ", lastActiveTime=" + lastActiveTime +
                '}';
    }
}
