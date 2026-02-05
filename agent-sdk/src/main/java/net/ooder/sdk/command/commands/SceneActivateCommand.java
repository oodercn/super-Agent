package net.ooder.sdk.command.commands;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.sdk.command.api.Command;
import net.ooder.sdk.command.model.CommandType;

import java.util.Map;

/**
 * SCENE_ACTIVATE
 * 
 */
public class SceneActivateCommand extends Command {
    /**
     * ID
     */
    @JSONField(name = "sceneId")
    private String sceneId;
    
    /**
     * 
     */
    @JSONField(name = "activateParams")
    private Map<String, Object> activateParams;

    /**
     * 
     */
    public SceneActivateCommand() {
        super(CommandType.SCENE_ACTIVATE);
    }

    /**
     * 
     * @param sceneId ID
     */
    public SceneActivateCommand(String sceneId) {
        super(CommandType.SCENE_ACTIVATE);
        this.sceneId = sceneId;
    }
    
    /**
     * 
     * @param sceneId ID
     * @param activateParams 
     */
    public SceneActivateCommand(String sceneId, Map<String, Object> activateParams) {
        super(CommandType.SCENE_ACTIVATE);
        this.sceneId = sceneId;
        this.activateParams = activateParams;
    }

    // GetterSetter
    public String getSceneId() {
        return sceneId;
    }

    public void setSceneId(String sceneId) {
        this.sceneId = sceneId;
    }
    
    public Map<String, Object> getActivateParams() {
        return activateParams;
    }
    
    public void setActivateParams(Map<String, Object> activateParams) {
        this.activateParams = activateParams;
    }

    @Override
    public String toString() {
        return "SceneActivateCommand{" +
                "commandId='" + getCommandId() + "'" +
                ", commandType=" + getCommandType() +
                ", timestamp=" + getTimestamp() +
                ", senderId='" + getSenderId() + "'" +
                ", receiverId='" + getReceiverId() + "'" +
                ", sceneId='" + sceneId + "'" +
                ", activateParams=" + activateParams +
                '}';
    }
}













