package net.ooder.sdk.northbound.protocol.model;

public class InvitationRequest {
    
    private String targetId;
    private String targetName;
    private String message;
    
    public String getTargetId() { return targetId; }
    public void setTargetId(String targetId) { this.targetId = targetId; }
    
    public String getTargetName() { return targetName; }
    public void setTargetName(String targetName) { this.targetName = targetName; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
