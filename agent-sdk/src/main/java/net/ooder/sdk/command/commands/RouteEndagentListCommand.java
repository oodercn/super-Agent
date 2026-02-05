package net.ooder.sdk.command.commands;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.sdk.command.api.Command;
import net.ooder.sdk.command.model.CommandType;

/**
 * ROUTE_ENDAGENT_LIST
 * 
 */
public class RouteEndagentListCommand extends Command {
    /**
     * 
     */
    @JSONField(name = "detailed")
    private boolean detailed;

    /**
     * 
     */
    @JSONField(name = "filter")
    private String filter;

    /**
     * 
     */
    @JSONField(name = "pageSize")
    private int pageSize;

    /**
     * 
     */
    @JSONField(name = "pageNumber")
    private int pageNumber;

    /**
     * 
     */
    public RouteEndagentListCommand() {
        super(CommandType.ROUTE_ENDAGENT_LIST);
    }

    /**
     * 
     * @param detailed 
     * @param filter 
     * @param pageSize 
     * @param pageNumber 
     */
    public RouteEndagentListCommand(boolean detailed, String filter, int pageSize, int pageNumber) {
        super(CommandType.ROUTE_ENDAGENT_LIST);
        this.detailed = detailed;
        this.filter = filter;
        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
    }

    // GetterSetter
    public boolean isDetailed() {
        return detailed;
    }

    public void setDetailed(boolean detailed) {
        this.detailed = detailed;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    @Override
    public String toString() {
        return "RouteEndagentListCommand{" +
                "commandId='" + getCommandId() + "'" +
                ", commandType=" + getCommandType() +
                ", timestamp=" + getTimestamp() +
                ", senderId='" + getSenderId() + "'" +
                ", receiverId='" + getReceiverId() + "'" +
                ", detailed=" + detailed +
                ", filter='" + filter + "'" +
                ", pageSize=" + pageSize +
                ", pageNumber=" + pageNumber +
                '}';
    }
}













