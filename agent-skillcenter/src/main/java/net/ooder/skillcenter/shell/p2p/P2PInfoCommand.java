package net.ooder.skillcenter.shell.p2p;

import net.ooder.skillcenter.shell.AbstractCommand;
import net.ooder.skillcenter.p2p.discovery.SkillsAgentDiscoveryService;
import net.ooder.skillcenter.p2p.discovery.SkillsAgentDiscoveryService.SkillsAgentInfo;
import net.ooder.skillcenter.p2p.discovery.impl.SkillsAgentDiscoveryServiceImpl;
import java.util.List;
import java.util.Map;

public class P2PInfoCommand extends AbstractCommand {
    
    private SkillsAgentDiscoveryService discoveryService;
    
    public P2PInfoCommand() {
        super();
        this.discoveryService = new SkillsAgentDiscoveryServiceImpl();
    }
    
    @Override
    public String getName() {
        return "p2p info";
    }
    
    @Override
    public String getDescription() {
        return "显示指定 Skills-Agent 的详细信息";
    }
    
    @Override
    public void execute(String[] args) throws Exception {
        if (args.length < 1) {
            error("用法: p2p info <agentId>");
            return;
        }
        
        String agentId = args[0];
        List<SkillsAgentInfo> agents = discoveryService.discoverSkillsAgents();
        
        SkillsAgentInfo targetAgent = null;
        for (SkillsAgentInfo agent : agents) {
            if (agent.getAgentId().equals(agentId)) {
                targetAgent = agent;
                break;
            }
        }
        
        if (targetAgent == null) {
            error("Skills-Agent 不存在: " + agentId);
            return;
        }
        
        println("==============================================");
        println("Skills-Agent 详细信息");
        println("==============================================");
        println("Agent ID:       " + targetAgent.getAgentId());
        println("Agent Name:     " + targetAgent.getAgentName());
        println("Agent Type:     " + targetAgent.getAgentType());
        println("IP 地址:        " + targetAgent.getIp());
        println("端口:           " + targetAgent.getPort());
        println("状态:           " + targetAgent.getStatus());
        println("最后发现时间:   " + formatTimestamp(targetAgent.getLastSeen()));
        
        if (targetAgent.getCapabilities() != null && !targetAgent.getCapabilities().isEmpty()) {
            println("能力列表:");
            for (String capability : targetAgent.getCapabilities()) {
                println("  - " + capability);
            }
        }
        
        if (targetAgent.getMetadata() != null && !targetAgent.getMetadata().isEmpty()) {
            println("元数据:");
            for (Map.Entry<String, Object> entry : targetAgent.getMetadata().entrySet()) {
                println("  " + entry.getKey() + ": " + entry.getValue());
            }
        }
        
        println("==============================================");
    }
    
    private String formatTimestamp(long timestamp) {
        if (timestamp == 0) {
            return "N/A";
        }
        return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            .format(new java.util.Date(timestamp));
    }
}
