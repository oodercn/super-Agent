package net.ooder.examples.sdkdemo;

import net.ooder.sdk.AgentConfig;
import net.ooder.sdk.AgentSDK;
import net.ooder.sdk.enums.CommandType;
import net.ooder.sdk.packet.CommandPacket;
import net.ooder.sdk.udp.UDPMessageHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class SdkDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SdkDemoApplication.class, args);
    }

    @Configuration
    static class SdkConfig {
        @Bean
        public AgentSDK agentSDK() throws Exception {
            AgentConfig config = AgentConfig.builder()
                    .agentId("demo-agent-001")
                    .agentName("DemoAgent")
                    .agentType("skill")
                    .endpoint("http://localhost:8080")
                    .udpPort(9001)
                    .heartbeatInterval(30000)
                    .build();

            AgentSDK sdk = new AgentSDK(config);
            
            // Register custom message handler
            sdk.getUdpSDK().registerMessageHandler(new CustomMessageHandler());
            
            return sdk;
        }
    }

    /**
     * Example custom message handler
     */
    static class CustomMessageHandler implements UDPMessageHandler {
        @Override
        public void onCommand(CommandPacket packet) {
            System.out.println("Received command: " + packet.getCommand());
            
            // Handle different command types
            if (packet.getCommand() == CommandType.SKILL_INVOKE) {
                handleSkillInvoke(packet);
            } else if (packet.getCommand() == CommandType.SKILL_STATUS) {
                handleSkillStatus(packet);
            }
        }

        private void handleSkillInvoke(CommandPacket packet) {
            System.out.println("Handling skill invoke command with params: " + packet.getParams());
            // Implement skill logic here
        }

        private void handleSkillStatus(CommandPacket packet) {
            System.out.println("Handling skill status command");
            // Implement status check logic here
        }

        @Override
        public void onHeartbeat(net.ooder.sdk.packet.HeartbeatPacket packet) {
            System.out.println("Received heartbeat from: " + packet.getAgentId());
        }

        @Override
        public void onStatusReport(net.ooder.sdk.packet.StatusReportPacket packet) {
            System.out.println("Received status report: " + packet.getReportType());
        }

        @Override
        public void onError(net.ooder.sdk.packet.UDPPacket packet, Exception e) {
            System.err.println("UDP error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}