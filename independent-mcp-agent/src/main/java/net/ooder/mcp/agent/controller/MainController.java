package net.ooder.mcp.agent.controller;

import net.ooder.mcp.agent.service.McpAgentService;
import net.ooder.mcpagent.console.ConsoleController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class MainController {
    
    @Autowired
    private McpAgentService mcpAgentService;
    
    @Autowired(required = false)
    private ConsoleController consoleController;
    
    public void refreshStatus() {
        mcpAgentService.refreshStatus();
    }
    
    public void addLink(String source, String target, String type) {
        mcpAgentService.addLink(source, target, type);
    }
    
    public void removeLink(String linkId) {
        mcpAgentService.removeLink(linkId);
    }
    
    public void saveConfig(String config) {
        mcpAgentService.saveConfig(config);
    }
    
    public void resetConfig() {
        mcpAgentService.resetConfig();
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
