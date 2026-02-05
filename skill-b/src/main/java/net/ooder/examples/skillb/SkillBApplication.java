package net.ooder.examples.skillb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.ooder.sdk.agent.model.AgentConfig;
import net.ooder.sdk.AgentSDK;
import net.ooder.sdk.command.model.CommandType;
import net.ooder.sdk.network.packet.StatusReportPacket;
import net.ooder.sdk.network.udp.SendResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;


@SpringBootApplication(
        scanBasePackages = "net.ooder.examples.skillb",
        exclude = {
                DataSourceAutoConfiguration.class,
                HibernateJpaAutoConfiguration.class
        }
)
public class SkillBApplication implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(SkillBApplication.class);

    private AgentSDK agentSDK;

    @Bean
    public net.ooder.sdk.agent.model.AgentConfig agentSdkConfig() {
        return net.ooder.sdk.agent.model.AgentConfig.builder()
                .agentId("skill-b-001")
                .agentName("SkillB")
                .agentType("end")
                .endpoint("http://localhost:9012")
                .udpPort(9003)
                .heartbeatInterval(30000)
                .build();
    }

    @Autowired
    private net.ooder.sdk.agent.model.AgentConfig agentSdkConfig;

    @Override
    public void run(String... args) throws Exception {
        log.info("Skill B - Data Submission Application Starting");

        agentSDK = new AgentSDK(agentSdkConfig);
        agentSDK.start();

        log.info("Skill B started successfully");
        log.info("Agent ID: {}", agentSdkConfig.getAgentId());
        log.info("Agent Type: {}", agentSdkConfig.getAgentType());
        log.info("Endpoint: {}", agentSdkConfig.getEndpoint());
        log.info("UDP Port: {}", agentSdkConfig.getUdpPort());
    }

    public void sendTestCommand() {
        Map<String, Object> params = new HashMap<>();
        params.put("data", "test data");

        CompletableFuture<SendResult> future = agentSDK.sendCommand(CommandType.SKILL_SUBMIT, params);
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
                .reportType("skill_status")
                .entityType("skill")
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
            log.info("Skill B stopped");
        }
    }

    public static void main(String[] args) {
        System.setProperty("server.port", "9012");
        SpringApplication.run(SkillBApplication.class, args);
    }
}
