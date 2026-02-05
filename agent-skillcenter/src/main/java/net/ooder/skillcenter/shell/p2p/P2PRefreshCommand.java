package net.ooder.skillcenter.shell.p2p;

import net.ooder.skillcenter.shell.AbstractCommand;
import net.ooder.skillcenter.p2p.discovery.SkillsAgentDiscoveryService;
import net.ooder.skillcenter.p2p.refresh.SkillsAgentRefreshEngine;
import net.ooder.skillcenter.p2p.refresh.SkillsAgentRefreshEngine.RefreshResult;
import net.ooder.skillcenter.p2p.discovery.impl.SkillsAgentDiscoveryServiceImpl;
import net.ooder.skillcenter.p2p.refresh.impl.SkillsAgentRefreshEngineImpl;
import java.util.List;

public class P2PRefreshCommand extends AbstractCommand {
    
    private SkillsAgentDiscoveryService discoveryService;
    private SkillsAgentRefreshEngine refreshEngine;
    
    public P2PRefreshCommand() {
        super();
        this.discoveryService = new SkillsAgentDiscoveryServiceImpl();
        this.refreshEngine = new SkillsAgentRefreshEngineImpl();
    }
    
    @Override
    public String getName() {
        return "p2p refresh";
    }
    
    @Override
    public String getDescription() {
        return "刷新 Skills-Agent 信息";
    }
    
    @Override
    public void execute(String[] args) throws Exception {
        boolean force = false;
        String agentId = null;
        
        for (String arg : args) {
            if (arg.equals("--force")) {
                force = true;
            } else if (!arg.startsWith("--")) {
                agentId = arg;
            }
        }
        
        if (agentId != null) {
            println("刷新 Skills-Agent: " + agentId);
            
            if (force) {
                println("强制刷新模式");
            }
            
            RefreshResult result = refreshEngine.refreshAgent(agentId);
            
            if (result.isSuccess()) {
                success("刷新成功");
                println("Agent ID: " + result.getAgentId());
                println("刷新时间: " + formatTimestamp(result.getTimestamp()));
                
                if (result.getUpdatedCapabilities() != null && !result.getUpdatedCapabilities().isEmpty()) {
                    println("更新的能力:");
                    for (String capability : result.getUpdatedCapabilities()) {
                        println("  - " + capability);
                    }
                }
            } else {
                error("刷新失败: " + result.getMessage());
            }
        } else {
            println("刷新所有 Skills-Agent");
            
            if (force) {
                println("强制刷新模式");
            }
            
            List<RefreshResult> results = refreshEngine.refreshAllAgents();
            
            println("==============================================");
            println("刷新结果");
            println("==============================================");
            
            int successCount = 0;
            int failureCount = 0;
            
            for (RefreshResult result : results) {
                if (result.isSuccess()) {
                    successCount++;
                    println("✓ " + result.getAgentId() + " - 成功");
                } else {
                    failureCount++;
                    println("✗ " + result.getAgentId() + " - 失败: " + result.getMessage());
                }
            }
            
            println("==============================================");
            println("总计: " + results.size() + " 个 Skills-Agent");
            println("成功: " + successCount + " 个");
            println("失败: " + failureCount + " 个");
        }
    }
    
    private String formatTimestamp(long timestamp) {
        if (timestamp == 0) {
            return "N/A";
        }
        return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            .format(new java.util.Date(timestamp));
    }
}
