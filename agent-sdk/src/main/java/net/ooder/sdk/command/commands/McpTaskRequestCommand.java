package net.ooder.sdk.command.commands;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.sdk.command.api.Command;
import net.ooder.sdk.command.model.CommandType;

import java.util.Map;

/**
 * MCP_TASK_REQUEST命令的实现类
 * 用于MCP任务请求操作
 */
public class McpTaskRequestCommand extends Command {
    /**
     * 任务ID
     */
    @JSONField(name = "taskId")
    private String taskId;

    /**
     * 任务类型
     */
    @JSONField(name = "taskType")
    private String taskType;

    /**
     * 任务内容
     */
    @JSONField(name = "taskContent")
    private Map<String, Object> taskContent;

    /**
     * 任务优先级
     */
    @JSONField(name = "priority")
    private String priority;

    /**
     * 超时时间(毫秒)
     */
    @JSONField(name = "timeout")
    private long timeout;

    /**
     * 默认构造方法
     */
    public McpTaskRequestCommand() {
        super(CommandType.MCP_TASK_REQUEST);
    }

    /**
     * 带参数的构造方法
     * @param taskId 任务ID
     * @param taskType 任务类型
     * @param taskContent 任务内容
     * @param priority 任务优先级
     * @param timeout 超时时间(毫秒)
     */
    public McpTaskRequestCommand(String taskId, String taskType, Map<String, Object> taskContent, String priority, long timeout) {
        super(CommandType.MCP_TASK_REQUEST);
        this.taskId = taskId;
        this.taskType = taskType;
        this.taskContent = taskContent;
        this.priority = priority;
        this.timeout = timeout;
    }

    // Getter和Setter方法
    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public Map<String, Object> getTaskContent() {
        return taskContent;
    }

    public void setTaskContent(Map<String, Object> taskContent) {
        this.taskContent = taskContent;
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
        return "McpTaskRequestCommand{" +
                "commandId='" + getCommandId() + "'" +
                ", commandType=" + getCommandType() +
                ", timestamp=" + getTimestamp() +
                ", senderId='" + getSenderId() + "'" +
                ", receiverId='" + getReceiverId() + "'" +
                ", taskId='" + taskId + "'" +
                ", taskType='" + taskType + "'" +
                ", taskContent=" + taskContent +
                ", priority=" + priority +
                ", timeout=" + timeout +
                '}';
    }
}










