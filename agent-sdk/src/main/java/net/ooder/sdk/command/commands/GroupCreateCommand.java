package net.ooder.sdk.command.commands;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.sdk.command.api.Command;
import net.ooder.sdk.command.model.CommandType;

import java.util.Map;

/**
 * GROUP_CREATE
 * 
 */
public class GroupCreateCommand extends Command {
    /**
     * ID
     */
    @JSONField(name = "groupId")
    private String groupId;

    /**
     * 
     */
    @JSONField(name = "groupName")
    private String groupName;

    /**
     * 
     */
    @JSONField(name = "description")
    private String description;

    /**
     * 
     */
    @JSONField(name = "groupConfig")
    private Map<String, Object> groupConfig;

    /**
     * 
     */
    public GroupCreateCommand() {
        super(CommandType.GROUP_CREATE);
    }

    /**
     * 
     * @param groupId ID
     * @param groupName 
     * @param description 
     * @param groupConfig 
     */
    public GroupCreateCommand(String groupId, String groupName, String description, Map<String, Object> groupConfig) {
        super(CommandType.GROUP_CREATE);
        this.groupId = groupId;
        this.groupName = groupName;
        this.description = description;
        this.groupConfig = groupConfig;
    }

    // GetterSetter
    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, Object> getGroupConfig() {
        return groupConfig;
    }

    public void setGroupConfig(Map<String, Object> groupConfig) {
        this.groupConfig = groupConfig;
    }

    @Override
    public String toString() {
        return "GroupCreateCommand{" +
                "commandId='" + getCommandId() + "'" +
                ", commandType=" + getCommandType() +
                ", timestamp=" + getTimestamp() +
                ", senderId='" + getSenderId() + "'" +
                ", receiverId='" + getReceiverId() + "'" +
                ", groupId='" + groupId + "'" +
                ", groupName='" + groupName + "'" +
                ", description='" + description + "'" +
                ", groupConfig=" + groupConfig +
                '}';
    }
}















