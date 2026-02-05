package net.ooder.sdk.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "ooder.sdk.agent")
public class AgentProperties {
    
    private int endagentDefaultPort;
    private int routeagentDefaultPort;
    private int mcpagentDefaultPort;
    
    public int getEndagentDefaultPort() {
        return endagentDefaultPort;
    }
    
    public void setEndagentDefaultPort(int endagentDefaultPort) {
        this.endagentDefaultPort = endagentDefaultPort;
    }
    
    public int getRouteagentDefaultPort() {
        return routeagentDefaultPort;
    }
    
    public void setRouteagentDefaultPort(int routeagentDefaultPort) {
        this.routeagentDefaultPort = routeagentDefaultPort;
    }
    
    public int getMcpagentDefaultPort() {
        return mcpagentDefaultPort;
    }
    
    public void setMcpagentDefaultPort(int mcpagentDefaultPort) {
        this.mcpagentDefaultPort = mcpagentDefaultPort;
    }
}
