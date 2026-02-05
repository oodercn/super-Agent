package net.ooder.sdk.command.commands;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.sdk.command.api.Command;
import net.ooder.sdk.command.model.CommandType;

/**
 * SKILL_STOP
 * 
 */
public class SkillStopCommand extends Command {
    /**
     * ID
     */
    @JSONField(name = "skillId")
    private String skillId;

    /**
     * 
     */
    @JSONField(name = "force")
    private boolean force;

    /**
     * 
     */
    @JSONField(name = "waitForStop")
    private boolean waitForStop;

    /**
     * 
     */
    public SkillStopCommand() {
        super(CommandType.SKILL_STOP);
    }

    /**
     * 
     * @param skillId ID
     * @param force 
     * @param waitForStop 
     */
    public SkillStopCommand(String skillId, boolean force, boolean waitForStop) {
        super(CommandType.SKILL_STOP);
        this.skillId = skillId;
        this.force = force;
        this.waitForStop = waitForStop;
    }

    // GetterSetter
    public String getSkillId() {
        return skillId;
    }

    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }

    public boolean isForce() {
        return force;
    }

    public void setForce(boolean force) {
        this.force = force;
    }

    public boolean isWaitForStop() {
        return waitForStop;
    }

    public void setWaitForStop(boolean waitForStop) {
        this.waitForStop = waitForStop;
    }

    @Override
    public String toString() {
        return "SkillStopCommand{" +
                "commandId='" + getCommandId() + '\'' +
                ", commandType=" + getCommandType() +
                ", timestamp=" + getTimestamp() +
                ", senderId='" + getSenderId() + '\'' +
                ", receiverId='" + getReceiverId() + '\'' +
                ", skillId='" + skillId + '\'' +
                ", force=" + force +
                ", waitForStop=" + waitForStop +
                '}';
    }
}
















