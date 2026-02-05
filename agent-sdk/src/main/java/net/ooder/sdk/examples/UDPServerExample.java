package net.ooder.sdk.examples;

import net.ooder.sdk.AgentSDK;
import net.ooder.sdk.agent.model.AgentConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * UDP服务器示例，用于测试命令的接收和处理
 */
public class UDPServerExample {
    private static final Logger log = LoggerFactory.getLogger(UDPServerExample.class);
    private static final int SERVER_PORT = 5680;
    
    public static void main(String[] args) {
        try {
            // 创建Agent配置
            AgentConfig config = new AgentConfig();
            config.setAgentId("test-udp-server");
            config.setAgentName("UDP Server Example");
            config.setAgentType("end");
            config.setEndpoint("localhost:" + SERVER_PORT);
            config.setUdpPort(SERVER_PORT);
            config.setHeartbeatInterval(30000); // 减少心跳频率
            
            // 初始化AgentSDK
            AgentSDK agentSDK = new AgentSDK(config);
            
            // 启动Agent
            agentSDK.start();
            
            System.out.println("UDP Server Example started on port " + SERVER_PORT);
            System.out.println("Waiting for commands...");
            System.out.println("Press Ctrl+C to exit");
            
            // 保持服务器运行
            CountDownLatch latch = new CountDownLatch(1);
            latch.await();
            
        } catch (Exception e) {
            log.error("Failed to start UDP server: {}", e.getMessage(), e);
        }
    }
}