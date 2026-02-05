package net.ooder.sdk.command.commands;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.sdk.command.api.Command;
import net.ooder.sdk.command.model.CommandType;

/**
 * END_RESET
 * End Agent
 */
public class EndResetCommand extends Command {
    /**
     * 
     */
    @JSONField(name = "force")
    private boolean force;

    /**
     * 
     */
    @JSONField(name = "resetScope")
    private String resetScope;

    /**
     * 
     */
    public EndResetCommand() {
        super(CommandType.END_RESET);
    }

    /**
     * 
     * @param force 
     * @param resetScope 
     */
    public EndResetCommand(boolean force, String resetScope) {
        super(CommandType.END_RESET);
        this.force = force;
        this.resetScope = resetScope;
    }

    // GetterSetter
    public boolean isForce() {
        return force;
    }

    public void setForce(boolean force) {
        this.force = force;
    }

    public String getResetScope() {
        return resetScope;
    }

    public void setResetScope(String resetScope) {
        this.resetScope = resetScope;
    }

    @Override
    public String toString() {
        return "EndResetCommand{" +
                "commandId='" + getCommandId() + '\'' +
                ", commandType=" + getCommandType() +
                ", timestamp=" + getTimestamp() +
                ", senderId='" + getSenderId() + '\'' +
                ", receiverId='" + getReceiverId() + '\'' +
                ", force=" + force +
                ", resetScope='" + resetScope + '\'' +
                '}';
    }
}
















