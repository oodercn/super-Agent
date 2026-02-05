package net.ooder.sdk.command.commands;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.sdk.command.api.Command;
import net.ooder.sdk.command.model.CommandType;

/**
 * GROUP_STATUS
 * 
 */
public class GroupStatusCommand extends Command {
    /**
     * ID
     */
    @JSONField(name = "groupId")
    private String groupId;

    /**
     * 
     */
    @JSONField(name = "detailed")
    private boolean detailed;

    /**
     * 
     */
    public GroupStatusCommand() {
        super(CommandType.GROUP_STATUS);
    }

    /**
     * 
     * @param groupId ID
     * @param detailed 
     */
    public GroupStatusCommand(String groupId, boolean detailed) {
        super(CommandType.GROUP_STATUS);
        this.groupId = groupId;
        this.detailed = detailed;
    }

    // GetterSetter
    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public boolean isDetailed() {
        return detailed;
    }

    public void setDetailed(boolean detailed) {
        this.detailed = detailed;
    }

    @Override
    public String toString() {
        return "GroupStatusCommand{" +
                "commandId='" + getCommandId() + "'" +
                ", commandType=" + getCommandType() +
                ", timestamp=" + getTimestamp() +
                ", senderId='" + getSenderId() + "'" +
                ", receiverId='" + getReceiverId() + "'" +
                ", groupId='" + groupId + "'" +
                ", detailed=" + detailed +
                '}';
    }
}















