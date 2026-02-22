package net.ooder.nexus.dto.protocol;

import java.io.Serializable;
import java.util.Map;

/**
 * Protocol command DTO
 * Used for executing protocol commands
 */
public class ProtocolCommandDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String protocolType;
    private String commandType;
    private Map<String, Object> payload;
    private String targetId;
    private Integer timeout;

    public ProtocolCommandDTO() {
        this.timeout = 30;
    }

    public String getProtocolType() { return protocolType; }
    public void setProtocolType(String protocolType) { this.protocolType = protocolType; }
    public String getCommandType() { return commandType; }
    public void setCommandType(String commandType) { this.commandType = commandType; }
    public Map<String, Object> getPayload() { return payload; }
    public void setPayload(Map<String, Object> payload) { this.payload = payload; }
    public String getTargetId() { return targetId; }
    public void setTargetId(String targetId) { this.targetId = targetId; }
    public Integer getTimeout() { return timeout; }
    public void setTimeout(Integer timeout) { this.timeout = timeout; }
}
