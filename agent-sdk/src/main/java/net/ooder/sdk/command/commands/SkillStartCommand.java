package net.ooder.sdk.command.commands;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.sdk.command.api.Command;
import net.ooder.sdk.command.model.CommandType;

import java.util.Map;

/**
 * SKILL_START
 * 
 */
public class SkillStartCommand extends Command {
    /**
     * ID
     */
    @JSONField(name = "skillId")
    private String skillId;

    /**
     * 
     */
    @JSONField(name = "startParams")
    private Map<String, Object> startParams;

    /**
     * 
     */
    @JSONField(name = "async")
    private boolean async;

    /**
     * 
     */
    public SkillStartCommand() {
        super(CommandType.SKILL_START);
    }

    /**
     * 
     * @param skillId ID
     * @param startParams 
     * @param async 
     */
    public SkillStartCommand(String skillId, Map<String, Object> startParams, boolean async) {
        super(CommandType.SKILL_START);
        this.skillId = skillId;
        this.startParams = startParams;
        this.async = async;
    }

    // GetterSetter
    public String getSkillId() {
        return skillId;
    }

    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }

    public Map<String, Object> getStartParams() {
        return startParams;
    }

    public void setStartParams(Map<String, Object> startParams) {
        this.startParams = startParams;
    }

    public boolean isAsync() {
        return async;
    }

    public void setAsync(boolean async) {
        this.async = async;
    }

    @Override
    public String toString() {
        return "SkillStartCommand{" +
                "commandId='" + getCommandId() + '\'' +
                ", commandType=" + getCommandType() +
                ", timestamp=" + getTimestamp() +
                ", senderId='" + getSenderId() + '\'' +
                ", receiverId='" + getReceiverId() + '\'' +
                ", skillId='" + skillId + '\'' +
                ", startParams=" + startParams +
                ", async=" + async +
                '}';
    }
}














