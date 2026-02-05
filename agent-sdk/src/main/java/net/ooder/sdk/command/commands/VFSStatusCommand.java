package net.ooder.sdk.command.commands;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.sdk.command.api.Command;
import net.ooder.sdk.command.model.CommandType;

import java.util.List;

/**
 * VFS_STATUS
 * VFS
 */
public class VFSStatusCommand extends Command {
    /**
     * VFSIDVFS
     */
    @JSONField(name = "vfsIds")
    private List<String> vfsIds;
    
    /**
     * false
     */
    @JSONField(name = "detailed")
    private boolean detailed;

    /**
     * 
     */
    public VFSStatusCommand() {
        super(CommandType.VFS_STATUS);
    }

    /**
     * 
     * @param vfsIds VFSID
     */
    public VFSStatusCommand(List<String> vfsIds) {
        super(CommandType.VFS_STATUS);
        this.vfsIds = vfsIds;
    }
    
    /**
     * 
     * @param vfsIds VFSID
     * @param detailed 
     */
    public VFSStatusCommand(List<String> vfsIds, boolean detailed) {
        super(CommandType.VFS_STATUS);
        this.vfsIds = vfsIds;
        this.detailed = detailed;
    }

    // GetterSetter
    public List<String> getVfsIds() {
        return vfsIds;
    }

    public void setVfsIds(List<String> vfsIds) {
        this.vfsIds = vfsIds;
    }
    
    public boolean isDetailed() {
        return detailed;
    }
    
    public void setDetailed(boolean detailed) {
        this.detailed = detailed;
    }

    @Override
    public String toString() {
        return "VFSStatusCommand{" +
                "commandId='" + getCommandId() + "'" +
                ", commandType=" + getCommandType() +
                ", timestamp=" + getTimestamp() +
                ", senderId='" + getSenderId() + "'" +
                ", receiverId='" + getReceiverId() + "'" +
                ", vfsIds=" + vfsIds +
                ", detailed=" + detailed +
                '}';
    }
}













