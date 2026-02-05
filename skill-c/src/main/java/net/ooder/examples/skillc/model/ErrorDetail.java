package net.ooder.examples.skillc.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ErrorDetail implements Serializable {
    private String errorId;
    private Map<String, Object> sceneGroupInfo;
    private Map<String, Object> additionalInfo;

    public String getErrorId() {
        return errorId;
    }

    public void setErrorId(String errorId) {
        this.errorId = errorId;
    }

    public Map<String, Object> getSceneGroupInfo() {
        return sceneGroupInfo;
    }

    public void setSceneGroupInfo(Map<String, Object> sceneGroupInfo) {
        this.sceneGroupInfo = sceneGroupInfo;
    }

    public void setSceneGroupInfo(String sceneId, String groupId) {
        Map<String, Object> info = new HashMap<>();
        info.put("scene_id", sceneId);
        info.put("group_id", groupId);
        this.sceneGroupInfo = info;
    }

    public Map<String, Object> getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(Map<String, Object> additionalInfo) {
        this.additionalInfo = additionalInfo;
    }
}