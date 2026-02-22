package net.ooder.nexus.common.exception;

/**
 * 协议相关异常
 */
public class ProtocolException extends NexusException {

    private final String protocolType;

    public ProtocolException(String message) {
        super("PROTOCOL_ERROR", message);
        this.protocolType = null;
    }

    public ProtocolException(String protocolType, String message) {
        super("PROTOCOL_ERROR", message);
        this.protocolType = protocolType;
    }

    public ProtocolException(String protocolType, String message, int httpStatus) {
        super("PROTOCOL_ERROR", message, httpStatus);
        this.protocolType = protocolType;
    }

    public ProtocolException(String protocolType, String message, Throwable cause) {
        super("PROTOCOL_ERROR", message, cause);
        this.protocolType = protocolType;
    }

    public String getProtocolType() {
        return protocolType;
    }
}
