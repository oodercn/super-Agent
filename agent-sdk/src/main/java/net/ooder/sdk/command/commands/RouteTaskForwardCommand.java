package net.ooder.sdk.command.commands;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.sdk.command.api.Command;
import net.ooder.sdk.command.model.CommandType;

import java.util.Map;

/**
 * ROUTE_TASK_FORWARD
 * 
 */
public class RouteTaskForwardCommand extends Command {
    /**
     * ID
     */
    @JSONField(name = "taskId")
    private String taskId;

    /**
     * 
     */
    @JSONField(name = "taskType")
    private String taskType;

    /**
     * 
     */
    @JSONField(name = "taskContent")
    private Map<String, Object> taskContent;

    /**
     * 
     */
    @JSONField(name = "priority")
    private String priority;

    /**
     * 
     */
    public RouteTaskForwardCommand() {
        super(CommandType.ROUTE_TASK_FORWARD);
    }

    /**
     * 
     * @param taskId ID
     * @param taskType 
     * @param taskContent 
     * @param priority 
     */
    public RouteTaskForwardCommand(String taskId, String taskType, Map<String, Object> taskContent, String priority) {
        super(CommandType.ROUTE_TASK_FORWARD);
        this.taskId = taskId;
        this.taskType = taskType;
        this.taskContent = taskContent;
        this.priority = priority;
    }

    // GetterSetter
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

    @Override
    public String toString() {
        return "RouteTaskForwardCommand{" +
                "commandId='" + getCommandId() + "'" +
                ", commandType=" + getCommandType() +
                ", timestamp=" + getTimestamp() +
                ", senderId='" + getSenderId() + "'" +
                ", receiverId='" + getReceiverId() + "'" +
                ", taskId='" + taskId + "'" +
                ", taskType='" + taskType + "'" +
                ", taskContent=" + taskContent +
                ", priority=" + priority +
                '}';
    }
}















