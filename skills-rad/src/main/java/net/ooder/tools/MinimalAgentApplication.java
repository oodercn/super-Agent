package net.ooder.tools;

import net.ooder.JDSInit;
import net.ooder.sdk.AgentSDK;
import net.ooder.sdk.agent.model.AgentConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

//@SpringBootApplication
//@ComponentScan(basePackages = {"net.ooder.tools"})
public class MinimalAgentApplication {

    private static AgentSDK agentSDK;

    public static void main(String[] args) {
        SpringApplication.run(MinimalAgentApplication.class, args);
        SpringApplication application = new SpringApplication(JDSInit.class);
        // 初始化Agent SDK
        initAgentSDK();


        System.out.println("=== Minimal Ooder Agent RAD Started Successfully! ===");
        System.out.println("Agent ID: " + agentSDK.getAgentId());
        System.out.println("Agent Name: " + agentSDK.getAgentName());
        System.out.println("Agent Type: " + agentSDK.getAgentType());
        System.out.println("Agent Endpoint: " + agentSDK.getEndpoint());
    }

    private static void initAgentSDK() {
        try {
            // 创建Agent配置
            AgentConfig config = new AgentConfig();
            config.setAgentId("agent-rad-001");
            config.setAgentName("AgentRAD");
            config.setAgentType("skillAgent");
            config.setEndpoint("http://localhost:9006");
            config.setUdpPort(9007);
            config.setHeartbeatInterval(30000);
            
            // 初始化Agent SDK
            agentSDK = new AgentSDK(config);
            
            // 启动Agent
            agentSDK.start();
            
            System.out.println("=== Agent SDK Initialized Successfully! ===");
            
        } catch (Exception e) {
            System.err.println("Failed to initialize Agent SDK: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static AgentSDK getAgentSDK() {
        return agentSDK;
    }
}