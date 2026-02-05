package net.ooder.sdk.command.commands;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.sdk.command.api.Command;
import net.ooder.sdk.command.model.CommandType;

import java.util.Map;

/**
 * SKILL_SUBMIT
 * 
 */
public class SkillSubmitCommand extends Command {
    /**
     * ID
     */
    @JSONField(name = "skillId")
    private String skillId;

    /**
     * 
     */
    @JSONField(name = "skillParams")
    private Map<String, Object> skillParams;

    /**
     * ID
     */
    @JSONField(name = "taskId")
    private String taskId;

    /**
     * 
     */
    @JSONField(name = "taskPriority")
    private String taskPriority;

    /**
     * 
     */
    public SkillSubmitCommand() {
        super(CommandType.SKILL_SUBMIT);
    }

    /**
     * 
     * @param skillId ID
     * @param skillParams 
     * @param taskId ID
     * @param taskPriority 
     */
    public SkillSubmitCommand(String skillId, Map<String, Object> skillParams, String taskId, String taskPriority) {
        super(CommandType.SKILL_SUBMIT);
        this.skillId = skillId;
        this.skillParams = skillParams;
        this.taskId = taskId;
        this.taskPriority = taskPriority;
    }

    // GetterSetter
    public String getSkillId() {
        return skillId;
    }

    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }

    public Map<String, Object> getSkillParams() {
        return skillParams;
    }

    public void setSkillParams(Map<String, Object> skillParams) {
        this.skillParams = skillParams;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskPriority() {
        return taskPriority;
    }

    public void setTaskPriority(String taskPriority) {
        this.taskPriority = taskPriority;
    }

    @Override
    public String toString() {
        return "SkillSubmitCommand{" +
                "commandId='" + getCommandId() + '\'' +
                ", commandType=" + getCommandType() +
                ", timestamp=" + getTimestamp() +
                ", senderId='" + getSenderId() + '\'' +
                ", receiverId='" + getReceiverId() + '\'' +
                ", skillId='" + skillId + '\'' +
                ", skillParams=" + skillParams +
                ", taskId='" + taskId + '\'' +
                ", taskPriority='" + taskPriority + '\'' +
                '}';
    }
}














