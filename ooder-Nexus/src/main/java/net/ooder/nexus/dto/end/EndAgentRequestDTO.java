package net.ooder.nexus.dto.end;

import java.io.Serializable;
import java.util.Map;

/**
 * End agent request DTO
 */
public class EndAgentRequestDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Agent name
     */
    private String name;

    /**
     * Agent type
     */
    private String type;

    /**
     * Host address
     */
    private String host;

    /**
     * Port number
     */
    private Integer port;

    /**
     * Protocol (http, https, tcp)
     */
    private String protocol;

    /**
     * Additional properties
     */
    private Map<String, Object> properties;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getHost() { return host; }
    public void setHost(String host) { this.host = host; }
    public Integer getPort() { return port; }
    public void setPort(Integer port) { this.port = port; }
    public String getProtocol() { return protocol; }
    public void setProtocol(String protocol) { this.protocol = protocol; }
    public Map<String, Object> getProperties() { return properties; }
    public void setProperties(Map<String, Object> properties) { this.properties = properties; }
}
