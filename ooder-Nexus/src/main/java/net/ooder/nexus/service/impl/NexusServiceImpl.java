package net.ooder.nexus.service.impl;

import net.ooder.nexus.service.NexusService;
import org.springframework.stereotype.Service;

@Service
public class NexusServiceImpl implements NexusService {
    
    @Override
    public void refreshStatus() {
        System.out.println("Refreshing agent status...");
    }
    
    @Override
    public void addLink(String source, String target, String type) {
        System.out.println("Adding link: " + source + " -> " + target + " (" + type + ")");
    }
    
    @Override
    public void removeLink(String linkId) {
        System.out.println("Removing link: " + linkId);
    }
    
    @Override
    public void saveConfig(String config) {
        System.out.println("Saving config: " + config);
    }
    
    @Override
    public void resetConfig() {
        System.out.println("Resetting config...");
    }
}
