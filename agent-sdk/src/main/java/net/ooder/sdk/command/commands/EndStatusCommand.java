package net.ooder.sdk.command.commands;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.sdk.command.api.Command;
import net.ooder.sdk.command.model.CommandType;

/**
 * END_STATUS命令的具体实现类
 * 用于获取End Agent的状态信息
 */
public class EndStatusCommand extends Command {
    /**
     * 状态查询类型
     */
    @JSONField(name = "queryType")
    private String queryType;

    /**
     * 状态查询参数
     */
    @JSONField(name = "queryParams")
    private String queryParams;

    /**
     * 默认构造方法
     */
    public EndStatusCommand() {
        super(CommandType.END_STATUS);
    }

    /**
     * 带参数的构造方法
     * @param queryType 状态查询类型
     * @param queryParams 状态查询参数
     */
    public EndStatusCommand(String queryType, String queryParams) {
        super(CommandType.END_STATUS);
        this.queryType = queryType;
        this.queryParams = queryParams;
    }

    // Getter和Setter方法
    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }

    public String getQueryParams() {
        return queryParams;
    }

    public void setQueryParams(String queryParams) {
        this.queryParams = queryParams;
    }

    @Override
    public String toString() {
        return "EndStatusCommand{" +
                "commandId='" + getCommandId() + '\'' +
                ", commandType=" + getCommandType() +
                ", timestamp=" + getTimestamp() +
                ", senderId='" + getSenderId() + '\'' +
                ", receiverId='" + getReceiverId() + '\'' +
                ", queryType='" + queryType + '\'' +
                ", queryParams='" + queryParams + '\'' +
                '}';
    }
}













