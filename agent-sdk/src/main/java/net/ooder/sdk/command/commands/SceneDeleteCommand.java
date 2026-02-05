package net.ooder.sdk.command.commands;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.sdk.command.api.Command;import net.ooder.sdk.command.model.CommandType;

/**
 * SCENE_DELETE
 * 
 */
public class SceneDeleteCommand extends Command {
    /**
     * ID
     */
    @JSONField(name = "sceneId")
    private String sceneId;

    /**
     * 
     */
    public SceneDeleteCommand() {
        super(CommandType.SCENE_DELETE);
    }

    /**
     * 
     * @param sceneId ID
     */
    public SceneDeleteCommand(String sceneId) {
        super(CommandType.SCENE_DELETE);
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
        return "SceneDeleteCommand{" +
                "commandId='" + getCommandId() + "'" +
                ", commandType=" + getCommandType() +
                ", timestamp=" + getTimestamp() +
                ", senderId='" + getSenderId() + "'" +
                ", receiverId='" + getReceiverId() + "'" +
                ", sceneId='" + sceneId + "'" +
                '}';
    }
}















