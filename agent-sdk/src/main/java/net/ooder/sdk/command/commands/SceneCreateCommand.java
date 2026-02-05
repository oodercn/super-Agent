package net.ooder.sdk.command.commands;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.sdk.command.api.Command;
import net.ooder.sdk.command.model.CommandType;

import java.util.Map;
import java.util.List;

/**
 * SCENE_CREATE
 * 
 */
public class SceneCreateCommand extends Command {
    /**
     * ID
     */
    @JSONField(name = "sceneId")
    private String sceneId;

    /**
     * 
     */
    @JSONField(name = "sceneName")
    private String sceneName;

    /**
     * 
     */
    @JSONField(name = "description")
    private String description;

    /**
     * 
     */
    @JSONField(name = "sceneConfig")
    private Map<String, Object> sceneConfig;

    /**
     * 豸
     */
    @JSONField(name = "deviceIds")
    private List<String> deviceIds;

    /**
     * 
     */
    public SceneCreateCommand() {
        super(CommandType.SCENE_CREATE);
    }

    /**
     * 
     * @param sceneId ID
     * @param sceneName 
     * @param description 
     * @param sceneConfig 
     * @param deviceIds 豸
     */
    public SceneCreateCommand(String sceneId, String sceneName, String description, Map<String, Object> sceneConfig, List<String> deviceIds) {
        super(CommandType.SCENE_CREATE);
        this.sceneId = sceneId;
        this.sceneName = sceneName;
        this.description = description;
        this.sceneConfig = sceneConfig;
        this.deviceIds = deviceIds;
    }

    // GetterSetter
    public String getSceneId() {
        return sceneId;
    }

    public void setSceneId(String sceneId) {
        this.sceneId = sceneId;
    }

    public String getSceneName() {
        return sceneName;
    }

    public void setSceneName(String sceneName) {
        this.sceneName = sceneName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, Object> getSceneConfig() {
        return sceneConfig;
    }

    public void setSceneConfig(Map<String, Object> sceneConfig) {
        this.sceneConfig = sceneConfig;
    }

    public List<String> getDeviceIds() {
        return deviceIds;
    }

    public void setDeviceIds(List<String> deviceIds) {
        this.deviceIds = deviceIds;
    }

    @Override
    public String toString() {
        return "SceneCreateCommand{" +
                "commandId='" + getCommandId() + "'" +
                ", commandType=" + getCommandType() +
                ", timestamp=" + getTimestamp() +
                ", senderId='" + getSenderId() + "'" +
                ", receiverId='" + getReceiverId() + "'" +
                ", sceneId='" + sceneId + "'" +
                ", sceneName='" + sceneName + "'" +
                ", description='" + description + "'" +
                ", sceneConfig=" + sceneConfig +
                ", deviceIds=" + deviceIds +
                '}';
    }
}













