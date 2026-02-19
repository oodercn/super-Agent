package net.ooder.sdk.core.protocol;

public class ProtocolException extends Exception {
    
    private String protocolType;
    private String operation;
    
    public ProtocolException(String protocolType, String operation, String message) {
        super(message);
        this.protocolType = protocolType;
        this.operation = operation;
    }
    
    public ProtocolException(String protocolType, String operation, Throwable cause) {
        super(cause);
        this.protocolType = protocolType;
        this.operation = operation;
    }
    
    public String getProtocolType() { return protocolType; }
    public String getOperation() { return operation; }
}
