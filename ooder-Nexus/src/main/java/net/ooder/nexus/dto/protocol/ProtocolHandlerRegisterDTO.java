package net.ooder.nexus.dto.protocol;

import java.io.Serializable;
import java.util.Map;

/**
 * Protocol handler register DTO
 * Used for registering protocol handlers
 */
public class ProtocolHandlerRegisterDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String protocolType;
    private String handlerName;
    private String description;
    private Map<String, Object> config;
    private Integer priority;

    public String getProtocolType() { return protocolType; }
    public void setProtocolType(String protocolType) { this.protocolType = protocolType; }
    public String getHandlerName() { return handlerName; }
    public void setHandlerName(String handlerName) { this.handlerName = handlerName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Map<String, Object> getConfig() { return config; }
    public void setConfig(Map<String, Object> config) { this.config = config; }
    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }
}
