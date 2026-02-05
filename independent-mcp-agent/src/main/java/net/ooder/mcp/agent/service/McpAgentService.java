package net.ooder.mcp.agent.service;

public interface McpAgentService {
    
    void refreshStatus();
    
    void addLink(String source, String target, String type);
    
    void removeLink(String linkId);
    
    void saveConfig(String config);
    
    void resetConfig();
}
