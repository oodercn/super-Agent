package net.ooder.mcpagent;

import net.ooder.mcpagent.skill.McpAgentSkill;
import net.ooder.mcpagent.skill.impl.McpAgentSkillImpl;
import net.ooder.sdk.agent.model.AgentConfig;
import net.ooder.sdk.AgentSDK;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MCP Agent主应用类
 */
public class McpAgentApplication {
    
    private static final Logger log = LoggerFactory.getLogger(McpAgentApplication.class);
    
    public static void main(String[] args) {
        log.info("Starting Independent MCP Agent...");
        
        try {
            // 1. 创建Agent配置
            AgentConfig config = AgentConfig.builder()
                    .agentId("mcp-agent-001")
                    .agentName("Independent MCP Agent")
                    .agentType("mcp")
                    .endpoint("localhost:9876")
                    .udpPort(9876)
                    .heartbeatInterval(30000)
                    .build();
            
            // 2. 创建AgentSDK实例
            AgentSDK agentSDK = new AgentSDK(config);
            
            // 3. 创建并初始化MCP Agent技能
            McpAgentSkill mcpAgentSkill = new McpAgentSkillImpl();
            mcpAgentSkill.initialize(agentSDK);
            
            // 4. 启动技能和Agent SDK
            agentSDK.start();
            mcpAgentSkill.start();
            
            log.info("Independent MCP Agent started successfully!");
            
            // 5. 保持应用运行
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                log.info("Shutting down Independent MCP Agent...");
                mcpAgentSkill.stop();
                agentSDK.stop();
                log.info("Independent MCP Agent stopped successfully!");
            }));
            
            // 主线程等待，防止应用退出
            synchronized (McpAgentApplication.class) {
                McpAgentApplication.class.wait();
            }
            
        } catch (Exception e) {
            log.error("Failed to start Independent MCP Agent: {}", e.getMessage(), e);
            System.exit(1);
        }
    }
}