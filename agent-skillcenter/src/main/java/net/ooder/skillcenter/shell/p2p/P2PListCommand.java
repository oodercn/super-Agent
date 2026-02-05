package net.ooder.skillcenter.shell.p2p;

import net.ooder.skillcenter.shell.AbstractCommand;
import net.ooder.skillcenter.p2p.discovery.SkillsAgentDiscoveryService;
import net.ooder.skillcenter.p2p.discovery.SkillsAgentDiscoveryService.SkillsAgentInfo;
import net.ooder.skillcenter.p2p.discovery.impl.SkillsAgentDiscoveryServiceImpl;
import java.util.List;

public class P2PListCommand extends AbstractCommand {
    
    private SkillsAgentDiscoveryService discoveryService;
    
    public P2PListCommand() {
        super();
        this.discoveryService = new SkillsAgentDiscoveryServiceImpl();
    }
    
    @Override
    public String getName() {
        return "p2p list";
    }
    
    @Override
    public String getDescription() {
        return "列出已发现的 Skills-Agent";
    }
    
    @Override
    public void execute(String[] args) throws Exception {
        String statusFilter = null;
        String typeFilter = null;
        String capabilityFilter = null;
        
        for (String arg : args) {
            if (arg.startsWith("--status=")) {
                statusFilter = arg.substring(9);
            } else if (arg.startsWith("--type=")) {
                typeFilter = arg.substring(7);
            } else if (arg.startsWith("--capability=")) {
                capabilityFilter = arg.substring(13);
            }
        }
        
        List<SkillsAgentInfo> agents = discoveryService.discoverSkillsAgents();
        
        if (agents.isEmpty()) {
            println("没有已发现的 Skills-Agent");
            return;
        }
        
        println("==============================================");
        println("Skills-Agent 列表");
        println("==============================================");
        
        if (statusFilter != null) {
            println("状态筛选: " + statusFilter);
        }
        if (typeFilter != null) {
            println("类型筛选: " + typeFilter);
        }
        if (capabilityFilter != null) {
            println("能力筛选: " + capabilityFilter);
        }
        
        println(String.format("%-20s %-20s %-15s %-15s %-15s %-20s", 
            "Agent ID", "Agent Name", "IP 地址", "端口", "状态", "类型"));
        println("------------------------------------------------------------------------------------");
        
        int count = 0;
        for (SkillsAgentInfo agent : agents) {
            if (statusFilter != null && !agent.getStatus().equalsIgnoreCase(statusFilter)) {
                continue;
            }
            if (typeFilter != null && !agent.getAgentType().equalsIgnoreCase(typeFilter)) {
                continue;
            }
            if (capabilityFilter != null && !agent.getCapabilities().contains(capabilityFilter)) {
                continue;
            }
            
            println(String.format("%-20s %-20s %-15s %-15s %-15s %-20s",
                agent.getAgentId(),
                agent.getAgentName(),
                agent.getIp(),
                String.valueOf(agent.getPort()),
                agent.getStatus(),
                agent.getAgentType()));
            
            count++;
        }
        
        println("==============================================");
        println("总计: " + count + " 个 Skills-Agent");
    }
}
