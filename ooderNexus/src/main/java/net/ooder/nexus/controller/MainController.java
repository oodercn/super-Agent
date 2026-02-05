package net.ooder.nexus.controller;

import net.ooder.nexus.service.NexusService;
import net.ooder.nexus.console.ConsoleController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class MainController {
    
    @Autowired
    private NexusService nexusService;
    
    @Autowired(required = false)
    private ConsoleController consoleController;
    
    public void refreshStatus() {
        nexusService.refreshStatus();
    }
    
    public void addLink(String source, String target, String type) {
        nexusService.addLink(source, target, type);
    }
    
    public void removeLink(String linkId) {
        nexusService.removeLink(linkId);
    }
    
    public void saveConfig(String config) {
        nexusService.saveConfig(config);
    }
    
    public void resetConfig() {
        nexusService.resetConfig();
    }
    
    // 控制台相关功能
    public void initializeConsole() {
        System.out.println("Initializing console...");
        if (consoleController != null) {
            System.out.println("Console controller initialized successfully");
        } else {
            System.out.println("Console controller not available");
        }
    }
    
    public Object getDashboardData() {
        if (consoleController != null) {
            return consoleController.getDashboardData();
        }
        return null;
    }
}
