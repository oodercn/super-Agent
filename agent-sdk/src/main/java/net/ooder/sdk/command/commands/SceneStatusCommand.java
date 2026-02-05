package net.ooder.sdk.command.commands;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.sdk.command.api.Command;
import net.ooder.sdk.command.model.CommandType;

import java.util.List;

/**
 * SCENE_STATUS
 * 
 */
public class SceneStatusCommand extends Command {
    /**
     * ID
     */
    @JSONField(name = "sceneIds")
    private List<String> sceneIds;

    /**
     * 
     */
    public SceneStatusCommand() {
        super(CommandType.SCENE_STATUS);
    }

    /**
     * 
     * @param sceneIds ID
     */
    public SceneStatusCommand(List<String> sceneIds) {
        super(CommandType.SCENE_STATUS);
        this.sceneIds = sceneIds;
    }

    // GetterSetter
    public List<String> getSceneIds() {
        return sceneIds;
    }

    public void setSceneIds(List<String> sceneIds) {
        this.sceneIds = sceneIds;
    }

    @Override
    public String toString() {
        return "SceneStatusCommand{" +
                "commandId='" + getCommandId() + "'" +
                ", commandType=" + getCommandType() +
                ", timestamp=" + getTimestamp() +
                ", senderId='" + getSenderId() + "'" +
                ", receiverId='" + getReceiverId() + "'" +
                ", sceneIds=" + sceneIds +
                '}';
    }
}















