package net.ooder.examples.mcp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.ooder.sdk.agent.model.AgentConfig;
import net.ooder.sdk.AgentSDK;
import net.ooder.sdk.enums.CommandType;
import net.ooder.sdk.network.packet.CommandPacket;
import net.ooder.sdk.network.packet.StatusReportPacket;
import net.ooder.sdk.network.udp.SendResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;


@SpringBootApplication(
        scanBasePackages = "net.ooder.examples.mcp",
        exclude = {
                DataSourceAutoConfiguration.class,
                HibernateJpaAutoConfiguration.class
        }
)
public class McpAgentApplication implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(McpAgentApplication.class);

    private AgentSDK agentSDK;

    public static void main(String[] args) {
        System.setProperty("server.port", "8090");
        SpringApplication.run(McpAgentApplication.class, args);
    }

    @Bean
    public AgentConfig agentSdkConfig() {
        return AgentConfig.builder()
                .agentId("mcp-agent-hierarchy-001")
                .agentName("McpAgent-Hierarchy")
                .agentType("mcp")
                .endpoint("http://localhost:8090")
                .udpPort(9010)
                .heartbeatInterval(30000)
                .build();
    }

    @Autowired
    private AgentConfig agentSdkConfig;

    @Override
    public void run(String... args) throws Exception {
        log.info("MCP Agent - Hierarchy Test Application Starting");

        agentSDK = AgentSDK.builder().config(agentSdkConfig).build();
        agentSDK.start();

        log.info("MCP Agent started successfully");
        log.info("Agent ID: {}", agentSdkConfig.getAgentId());
        log.info("Agent Type: {}", agentSdkConfig.getAgentType());
        log.info("Endpoint: {}", agentSdkConfig.getEndpoint());
        log.info("UDP Port: {}", agentSdkConfig.getUdpPort());
        log.info("Ready to manage Route Agents");
    }

    public void sendTestCommand() {
        Map<String, Object> params = new HashMap<>();
        params.put("action", "ping");

        CompletableFuture<SendResult> future = agentSDK.sendCommand(CommandType.SYSTEM_COMMAND, params);
        future.thenAccept(result -> {
            if (result.isSuccess()) {
                log.info("Command sent successfully: {}", result.getMessage());
            } else {
                log.error("Command send failed: {}", result.getMessage());
            }
        }).exceptionally(e -> {
            log.error("Command send exception", e);
            return null;
        });
    }

    public void sendStatusReport() {
        StatusReportPacket packet = StatusReportPacket.builder()
                .reportType("agent_status")
                .entityType("agent")
                .entityId(agentSdkConfig.getAgentId())
                .statusType("running")
                .currentStatus("running")
                .build();

        agentSDK.sendStatusReport(packet).thenAccept(result -> {
            if (result.isSuccess()) {
                log.info("Status report sent successfully");
            } else {
                log.error("Status report send failed: {}", result.getMessage());
            }
        });
    }

    public void shutdown() {
        if (agentSDK != null) {
            agentSDK.stop();
            log.info("MCP Agent stopped");
        }
    }
}