
package net.ooder.sdk.infra.exception;

public class FailoverException extends SDKException {
    
    private static final long serialVersionUID = 1L;
    
    private final String sceneGroupId;
    private final String failedMemberId;
    private final String newPrimaryId;
    
    public FailoverException(String sceneGroupId, String message) {
        super("FAILOVER_ERROR", message);
        this.sceneGroupId = sceneGroupId;
        this.failedMemberId = null;
        this.newPrimaryId = null;
    }
    
    public FailoverException(String sceneGroupId, String failedMemberId, String newPrimaryId, String message) {
        super("FAILOVER_ERROR", message);
        this.sceneGroupId = sceneGroupId;
        this.failedMemberId = failedMemberId;
        this.newPrimaryId = newPrimaryId;
    }
    
    public FailoverException(String sceneGroupId, String message, Throwable cause) {
        super("FAILOVER_ERROR", message, cause);
        this.sceneGroupId = sceneGroupId;
        this.failedMemberId = null;
        this.newPrimaryId = null;
    }
    
    public String getSceneGroupId() {
        return sceneGroupId;
    }
    
    public String getFailedMemberId() {
        return failedMemberId;
    }
    
    public String getNewPrimaryId() {
        return newPrimaryId;
    }
}
