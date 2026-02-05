package net.ooder.sdk.network.packet.params;

import com.alibaba.fastjson.annotation.JSONField;
import java.util.Map;

/**
 * END_EXECUTE命令参数类
 */
public class EndExecuteParams {
    @JSONField(name = "command")
    private String command;

    @JSONField(name = "args")
    private Map<String, Object> args;

    // 默认构造方法
    public EndExecuteParams() {
    }

    // 带参数的构造方法
    public EndExecuteParams(String command, Map<String, Object> args) {
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
        return "EndExecuteParams{" +
                "command='" + command + '\'' +
                ", args=" + args +
                '}';
    }
}