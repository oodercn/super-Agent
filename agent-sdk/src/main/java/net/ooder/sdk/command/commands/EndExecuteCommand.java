package net.ooder.sdk.command.commands;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.sdk.command.api.Command;
import net.ooder.sdk.command.model.CommandType;

import java.util.Map;

/**
 * END_EXECUTE命令的具体实现类
 * 采用贫血模型设计仅包含数据字段和getter/setter方法
 */
public class EndExecuteCommand extends Command {
    /**
     * 要执行的命令
     */
    @JSONField(name = "command")
    private String command;

    /**
     * 命令参数
     */
    @JSONField(name = "args")
    private Map<String, Object> args;

    /**
     * 默认构造方法
     */
    public EndExecuteCommand() {
        super(CommandType.END_EXECUTE);
    }

    /**
     * 带参数的构造方法
     * @param command 要执行的命令
     * @param args 命令参数
     */
    public EndExecuteCommand(String command, Map<String, Object> args) {
        super(CommandType.END_EXECUTE);
        this.command = command;
        this.args = args;
    }

    // Getter和Setter方法
    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public Map<String, Object> getArgs() {
        return args;
    }

    public void setArgs(Map<String, Object> args) {
        this.args = args;
    }

    @Override
    public String toString() {
        return "EndExecuteCommand{" +
                "commandId='" + getCommandId() + '\'' +
                ", commandType=" + getCommandType() +
                ", timestamp=" + getTimestamp() +
                ", senderId='" + getSenderId() + '\'' +
                ", receiverId='" + getReceiverId() + '\'' +
                ", command='" + command + '\'' +
                ", args=" + args +
                '}';
    }
}











