package net.ooder.sdk.command.commands;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.sdk.command.api.Command;
import net.ooder.sdk.command.model.CommandType;

/**
 * SCENE_DEACTIVATE
 * 
 */
public class SceneDeactivateCommand extends Command {
    /**
     * ID
     */
    @JSONField(name = "sceneId")
    private String sceneId;

    /**
     * 
     */
    public SceneDeactivateCommand() {
        super(CommandType.SCENE_DEACTIVATE);
    }

    /**
     * 
     * @param sceneId ID
     */
    public SceneDeactivateCommand(String sceneId) {
        super(CommandType.SCENE_DEACTIVATE);
        this.sceneId = sceneId;
    }

    // GetterSetter
    public String getSceneId() {
        return sceneId;
    }

    public void setSceneId(String sceneId) {
        this.sceneId = sceneId;
    }

    @Override
    public String toString() {
        return "SceneDeactivateCommand{" +
                "commandId='" + getCommandId() + "'" +
                ", commandType=" + getCommandType() +
                ", timestamp=" + getTimestamp() +
                ", senderId='" + getSenderId() + "'" +
                ", receiverId='" + getReceiverId() + "'" +
                ", sceneId='" + sceneId + "'" +
                '}';
    }
}













