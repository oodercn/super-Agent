package net.ooder.sdk.command.commands;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.sdk.command.api.Command;
import net.ooder.sdk.command.model.CommandType;

/**
 * GROUP_LIST_MEMBERS命令的实现类
 * 用于获取群组成员列表
 */
public class GroupListMembersCommand extends Command {
    /**
     * 群组ID
     */
    @JSONField(name = "groupId")
    private String groupId;

    /**
     * 页码
     */
    @JSONField(name = "page")
    private int page;

    /**
     * 每页数量
     */
    @JSONField(name = "pageSize")
    private int pageSize;

    /**
     * 默认构造方法
     */
    public GroupListMembersCommand() {
        super(CommandType.GROUP_LIST_MEMBERS);
        this.page = 1;
        this.pageSize = 20;
    }

    /**
     * 带参数的构造方法
     * @param groupId 群组ID
     * @param page 页码
     * @param pageSize 每页数量
     */
    public GroupListMembersCommand(String groupId, int page, int pageSize) {
        super(CommandType.GROUP_LIST_MEMBERS);
        this.groupId = groupId;
        this.page = page;
        this.pageSize = pageSize;
    }

    // GetterSetter
    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return "GroupListMembersCommand{" +
                "commandId='" + getCommandId() + "'" +
                ", commandType=" + getCommandType() +
                ", timestamp=" + getTimestamp() +
                ", senderId='" + getSenderId() + "'" +
                ", receiverId='" + getReceiverId() + "'" +
                ", groupId='" + groupId + "'" +
                ", page=" + page +
                ", pageSize=" + pageSize +
                '}';
    }
}















