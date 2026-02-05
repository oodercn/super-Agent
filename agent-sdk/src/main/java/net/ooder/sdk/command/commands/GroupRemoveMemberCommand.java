package net.ooder.sdk.command.commands;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.sdk.command.api.Command;
import net.ooder.sdk.command.model.CommandType;

import java.util.List;

/**
 * GROUP_REMOVE_MEMBER
 * 
 */
public class GroupRemoveMemberCommand extends Command {
    /**
     * ID
     */
    @JSONField(name = "groupId")
    private String groupId;

    /**
     * ID
     */
    @JSONField(name = "memberIds")
    private List<String> memberIds;

    /**
     * 
     */
    @JSONField(name = "notify")
    private boolean notify;

    /**
     * 
     */
    @JSONField(name = "reason")
    private String reason;

    /**
     * 
     */
    public GroupRemoveMemberCommand() {
        super(CommandType.GROUP_REMOVE_MEMBER);
    }

    /**
     * 
     * @param groupId ID
     * @param memberIds ID
     * @param notify 
     * @param reason 
     */
    public GroupRemoveMemberCommand(String groupId, List<String> memberIds, boolean notify, String reason) {
        super(CommandType.GROUP_REMOVE_MEMBER);
        this.groupId = groupId;
        this.memberIds = memberIds;
        this.notify = notify;
        this.reason = reason;
    }

    // GetterSetter
    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public List<String> getMemberIds() {
        return memberIds;
    }

    public void setMemberIds(List<String> memberIds) {
        this.memberIds = memberIds;
    }

    public boolean isNotify() {
        return notify;
    }

    public void setNotify(boolean notify) {
        this.notify = notify;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "GroupRemoveMemberCommand{" +
                "commandId='" + getCommandId() + "'" +
                ", commandType=" + getCommandType() +
                ", timestamp=" + getTimestamp() +
                ", senderId='" + getSenderId() + "'" +
                ", receiverId='" + getReceiverId() + "'" +
                ", groupId='" + groupId + "'" +
                ", memberIds=" + memberIds +
                ", notify=" + notify +
                ", reason='" + reason + "'" +
                '}';
    }
}













