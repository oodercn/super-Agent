package net.ooder.sdk.command.commands;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.sdk.command.api.Command;
import net.ooder.sdk.command.model.CommandType;

import java.util.List;
import java.util.Map;

/**
 * GROUP_ADD_MEMBER
 * 
 */
public class GroupAddMemberCommand extends Command {
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
    @JSONField(name = "memberInfo")
    private Map<String, Object> memberInfo;

    /**
     * 
     */
    @JSONField(name = "notify")
    private boolean notify;

    /**
     * 
     */
    public GroupAddMemberCommand() {
        super(CommandType.GROUP_ADD_MEMBER);
    }

    /**
     * 
     * @param groupId ID
     * @param memberIds ID
     * @param memberInfo 
     * @param notify 
     */
    public GroupAddMemberCommand(String groupId, List<String> memberIds, Map<String, Object> memberInfo, boolean notify) {
        super(CommandType.GROUP_ADD_MEMBER);
        this.groupId = groupId;
        this.memberIds = memberIds;
        this.memberInfo = memberInfo;
        this.notify = notify;
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

    public Map<String, Object> getMemberInfo() {
        return memberInfo;
    }

    public void setMemberInfo(Map<String, Object> memberInfo) {
        this.memberInfo = memberInfo;
    }

    public boolean isNotify() {
        return notify;
    }

    public void setNotify(boolean notify) {
        this.notify = notify;
    }

    @Override
    public String toString() {
        return "GroupAddMemberCommand{" +
                "commandId='" + getCommandId() + "'" +
                ", commandType=" + getCommandType() +
                ", timestamp=" + getTimestamp() +
                ", senderId='" + getSenderId() + "'" +
                ", receiverId='" + getReceiverId() + "'" +
                ", groupId='" + groupId + "'" +
                ", memberIds=" + memberIds +
                ", memberInfo=" + memberInfo +
                ", notify=" + notify +
                '}';
    }
}















