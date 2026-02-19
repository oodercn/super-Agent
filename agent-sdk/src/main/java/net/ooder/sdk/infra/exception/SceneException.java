
package net.ooder.sdk.infra.exception;

public class SceneException extends SDKException {
    
    private static final long serialVersionUID = 1L;
    
    private final String sceneId;
    
    public SceneException(String sceneId, String message) {
        super("SCENE_ERROR", message);
        this.sceneId = sceneId;
    }
    
    public SceneException(String sceneId, String errorCode, String message) {
        super(errorCode, message);
        this.sceneId = sceneId;
    }
    
    public SceneException(String sceneId, String message, Throwable cause) {
        super("SCENE_ERROR", message, cause);
        this.sceneId = sceneId;
    }
    
    public SceneException(String sceneId, String errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
        this.sceneId = sceneId;
    }
    
    public String getSceneId() {
        return sceneId;
    }
}
