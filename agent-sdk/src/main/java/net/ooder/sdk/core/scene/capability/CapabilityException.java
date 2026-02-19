
package net.ooder.sdk.core.scene.capability;

public class CapabilityException extends Exception {
    
    private static final long serialVersionUID = 1L;
    
    private String capabilityId;
    private String errorCode;
    
    public CapabilityException(String message) {
        super(message);
    }
    
    public CapabilityException(String capabilityId, String message) {
        super(message);
        this.capabilityId = capabilityId;
    }
    
    public CapabilityException(String capabilityId, String errorCode, String message) {
        super(message);
        this.capabilityId = capabilityId;
        this.errorCode = errorCode;
    }
    
    public CapabilityException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public String getCapabilityId() { return capabilityId; }
    public String getErrorCode() { return errorCode; }
}
