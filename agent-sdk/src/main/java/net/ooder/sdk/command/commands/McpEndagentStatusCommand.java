package net.ooder.sdk.command.commands;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.sdk.command.api.Command;
import net.ooder.sdk.command.model.CommandType;

/**
 * MCP_ENDAGENT_STATUS命令的实现类
 * 用于MCP服务查询终端代理状态
 */
public class McpEndagentStatusCommand extends Command {
    /**
     * 终端代理ID可选为空时查询所有终端代理
     */
    @JSONField(name = "endagentId")
    private String endagentId;

    /**
     * 是否需要详细状态
     */
    @JSONField(name = "detailed")
    private boolean detailed;

    /**
     * 默认构造方法
     */
    public McpEndagentStatusCommand() {
        super(CommandType.MCP_ENDAGENT_STATUS);
    }

    /**
     * 带参数的构造方法
     * @param endagentId 终端代理ID可选
     * @param detailed 是否需要详细状态
     */
    public McpEndagentStatusCommand(String endagentId, boolean detailed) {
        super(CommandType.MCP_ENDAGENT_STATUS);
        this.endagentId = endagentId;
        this.detailed = detailed;
    }

    // Getter和Setter方法
    public String getEndagentId() {
        return endagentId;
    }

    public void setEndagentId(String endagentId) {
        this.endagentId = endagentId;
    }

    public boolean isDetailed() {
        return detailed;
    }

    public void setDetailed(boolean detailed) {
        this.detailed = detailed;
    }

    @Override
    public String toString() {
        return "McpEndagentStatusCommand{" +
                "commandId='" + getCommandId() + "'" +
                ", commandType=" + getCommandType() +
                ", timestamp=" + getTimestamp() +
                ", senderId='" + getSenderId() + "'" +
                ", receiverId='" + getReceiverId() + "'" +
                ", endagentId='" + endagentId + "'" +
                ", detailed=" + detailed +
                '}';
    }
}









