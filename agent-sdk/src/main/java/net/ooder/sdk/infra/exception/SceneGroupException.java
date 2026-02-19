
package net.ooder.sdk.infra.exception;

public class SceneGroupException extends SDKException {
    
    private static final long serialVersionUID = 1L;
    
    private final String sceneGroupId;
    
    public SceneGroupException(String sceneGroupId, String message) {
        super("SCENE_GROUP_ERROR", message);
        this.sceneGroupId = sceneGroupId;
    }
    
    public SceneGroupException(String sceneGroupId, String errorCode, String message) {
        super(errorCode, message);
        this.sceneGroupId = sceneGroupId;
    }
    
    public SceneGroupException(String sceneGroupId, String message, Throwable cause) {
        super("SCENE_GROUP_ERROR", message, cause);
        this.sceneGroupId = sceneGroupId;
    }
    
    public SceneGroupException(String sceneGroupId, String errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
        this.sceneGroupId = sceneGroupId;
    }
    
    public String getSceneGroupId() {
        return sceneGroupId;
    }
}
