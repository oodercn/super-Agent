package net.ooder.nexus.service;

public interface NexusService {
    
    void refreshStatus();
    
    void addLink(String source, String target, String type);
    
    void removeLink(String linkId);
    
    void saveConfig(String config);
    
    void resetConfig();
}
