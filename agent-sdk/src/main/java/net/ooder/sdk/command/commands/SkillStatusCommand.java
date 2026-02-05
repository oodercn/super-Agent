package net.ooder.sdk.command.commands;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.sdk.command.api.Command;
import net.ooder.sdk.command.model.CommandType;

/**
 * SKILL_STATUS
 * 
 */
public class SkillStatusCommand extends Command {
    /**
     * ID
     */
    @JSONField(name = "skillId")
    private String skillId;

    /**
     * 
     */
    @JSONField(name = "detailed")
    private boolean detailed;

    /**
     * 
     */
    @JSONField(name = "statusType")
    private String statusType;

    /**
     * 
     */
    public SkillStatusCommand() {
        super(CommandType.SKILL_STATUS);
    }

    /**
     * 
     * @param skillId ID
     * @param detailed 
     * @param statusType 
     */
    public SkillStatusCommand(String skillId, boolean detailed, String statusType) {
        super(CommandType.SKILL_STATUS);
        this.skillId = skillId;
        this.detailed = detailed;
        this.statusType = statusType;
    }

    // GetterSetter
    public String getSkillId() {
        return skillId;
    }

    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }

    public boolean isDetailed() {
        return detailed;
    }

    public void setDetailed(boolean detailed) {
        this.detailed = detailed;
    }

    public String getStatusType() {
        return statusType;
    }

    public void setStatusType(String statusType) {
        this.statusType = statusType;
    }

    @Override
    public String toString() {
        return "SkillStatusCommand{" +
                "commandId='" + getCommandId() + '\'' +
                ", commandType=" + getCommandType() +
                ", timestamp=" + getTimestamp() +
                ", senderId='" + getSenderId() + '\'' +
                ", receiverId='" + getReceiverId() + '\'' +
                ", skillId='" + skillId + '\'' +
                ", detailed=" + detailed +
                ", statusType='" + statusType + '\'' +
                '}';
    }
}
















