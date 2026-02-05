package net.ooder.skillcenter.shell.p2p;

import net.ooder.skillcenter.shell.AbstractCommand;
import net.ooder.skillcenter.p2p.discovery.SkillsAgentDiscoveryService;
import net.ooder.skillcenter.p2p.discovery.SkillsAgentDiscoveryService.SkillsAgentInfo;
import net.ooder.skillcenter.p2p.discovery.impl.SkillsAgentDiscoveryServiceImpl;
import java.util.List;

public class P2PDiscoverCommand extends AbstractCommand {
    
    private SkillsAgentDiscoveryService discoveryService;
    
    public P2PDiscoverCommand() {
        super();
        this.discoveryService = new SkillsAgentDiscoveryServiceImpl();
    }
    
    @Override
    public String getName() {
        return "p2p discover";
    }
    
    @Override
    public String getDescription() {
        return "发现 P2P 网络中的 Skills-Agent";
    }
    
    @Override
    public void execute(String[] args) throws Exception {
        String capability = null;
        Integer timeout = 30;
        boolean continuous = false;
        
        for (String arg : args) {
            if (arg.startsWith("--capability=")) {
                capability = arg.substring(13);
            } else if (arg.startsWith("--timeout=")) {
                timeout = Integer.parseInt(arg.substring(10));
            } else if (arg.equals("--continuous")) {
                continuous = true;
            }
        }
        
        if (continuous) {
            println("启动持续发现模式 (按 Ctrl+C 停止)...");
            discoveryService.startDiscovery();
            
            while (true) {
                Thread.sleep(5000);
                List<SkillsAgentInfo> agents = discoveryService.discoverSkillsAgents();
                println("已发现 " + agents.size() + " 个 Skills-Agent");
            }
        } else {
            println("开始发现 Skills-Agent (超时: " + timeout + " 秒)...");
            
            if (capability != null) {
                println("按能力筛选: " + capability);
            }
            
            discoveryService.startDiscovery();
            Thread.sleep(timeout * 1000);
            
            List<SkillsAgentInfo> agents;
            if (capability != null) {
                agents = discoveryService.discoverSkillsAgentsByCapability(capability);
            } else {
                agents = discoveryService.discoverSkillsAgents();
            }
            
            println("==============================================");
            println("发现结果");
            println("==============================================");
            
            if (agents.isEmpty()) {
                println("未发现任何 Skills-Agent");
            } else {
                println(String.format("%-20s %-20s %-15s %-15s %-15s", 
                    "Agent ID", "Agent Name", "IP 地址", "端口", "状态"));
                println("--------------------------------------------------------------------------");
                
                for (SkillsAgentInfo agent : agents) {
                    println(String.format("%-20s %-20s %-15s %-15s %-15s",
                        agent.getAgentId(),
                        agent.getAgentName(),
                        agent.getIp(),
                        String.valueOf(agent.getPort()),
                        agent.getStatus()));
                }
                
                println("==============================================");
                println("总计: " + agents.size() + " 个 Skills-Agent");
            }
        }
    }
}
