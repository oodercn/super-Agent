package net.ooder.sdk.command.commands;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.sdk.command.api.Command;
import net.ooder.sdk.command.model.CommandType;

/**
 * GROUP_DELETE
 * 
 */
public class GroupDeleteCommand extends Command {
    /**
     * ID
     */
    @JSONField(name = "groupId")
    private String groupId;

    /**
     * 
     */
    @JSONField(name = "force")
    private boolean force;

    /**
     * 
     */
    @JSONField(name = "reason")
    private String reason;

    /**
     * 
     */
    public GroupDeleteCommand() {
        super(CommandType.GROUP_DELETE);
    }

    /**
     * 
     * @param groupId ID
     * @param force 
     * @param reason 
     */
    public GroupDeleteCommand(String groupId, boolean force, String reason) {
        super(CommandType.GROUP_DELETE);
        this.groupId = groupId;
        this.force = force;
        this.reason = reason;
    }

    // GetterSetter
    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public boolean isForce() {
        return force;
    }

    public void setForce(boolean force) {
        this.force = force;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "GroupDeleteCommand{" +
                "commandId='" + getCommandId() + "'" +
                ", commandType=" + getCommandType() +
                ", timestamp=" + getTimestamp() +
                ", senderId='" + getSenderId() + "'" +
                ", receiverId='" + getReceiverId() + "'" +
                ", groupId='" + groupId + "'" +
                ", force=" + force +
                ", reason='" + reason + "'" +
                '}';
    }
}













