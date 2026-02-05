package net.ooder.sdk.command.commands;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.sdk.command.api.Command;
import net.ooder.sdk.command.model.CommandType;

import java.util.Map;

/**
 * VFS_REGISTER
 * VFS
 */
public class VFSRegisterCommand extends Command {
    /**
     * VFSID
     */
    @JSONField(name = "vfsId")
    private String vfsId;
    
    /**
     * VFS
     */
    @JSONField(name = "vfsName")
    private String vfsName;
    
    /**
     * VFS
     */
    @JSONField(name = "vfsConfig")
    private Map<String, Object> vfsConfig;
    
    /**
     * VFS
     */
    @JSONField(name = "vfsUrl")
    private String vfsUrl;

    /**
     * 
     */
    public VFSRegisterCommand() {
        super(CommandType.VFS_REGISTER);
    }

    /**
     * 
     * @param vfsId VFSID
     * @param vfsName VFS
     * @param vfsConfig VFS
     * @param vfsUrl VFS
     */
    public VFSRegisterCommand(String vfsId, String vfsName, Map<String, Object> vfsConfig, String vfsUrl) {
        super(CommandType.VFS_REGISTER);
        this.vfsId = vfsId;
        this.vfsName = vfsName;
        this.vfsConfig = vfsConfig;
        this.vfsUrl = vfsUrl;
    }

    // GetterSetter
    public String getVfsId() {
        return vfsId;
    }

    public void setVfsId(String vfsId) {
        this.vfsId = vfsId;
    }
    
    public String getVfsName() {
        return vfsName;
    }
    
    public void setVfsName(String vfsName) {
        this.vfsName = vfsName;
    }
    
    public Map<String, Object> getVfsConfig() {
        return vfsConfig;
    }
    
    public void setVfsConfig(Map<String, Object> vfsConfig) {
        this.vfsConfig = vfsConfig;
    }
    
    public String getVfsUrl() {
        return vfsUrl;
    }
    
    public void setVfsUrl(String vfsUrl) {
        this.vfsUrl = vfsUrl;
    }

    @Override
    public String toString() {
        return "VFSRegisterCommand{" +
                "commandId='" + getCommandId() + "'" +
                ", commandType=" + getCommandType() +
                ", timestamp=" + getTimestamp() +
                ", senderId='" + getSenderId() + "'" +
                ", receiverId='" + getReceiverId() + "'" +
                ", vfsId='" + vfsId + "'" +
                ", vfsName='" + vfsName + "'" +
                ", vfsUrl='" + vfsUrl + "'" +
                ", vfsConfig=" + vfsConfig +
                '}';
    }
}













