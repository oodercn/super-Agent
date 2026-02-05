package net.ooder.examples.skilla;

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
        scanBasePackages = "net.ooder.examples.skilla",
        exclude = {
                DataSourceAutoConfiguration.class,
                HibernateJpaAutoConfiguration.class
        }
)
public class SkillAApplication implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(SkillAApplication.class);

    private AgentSDK agentSDK;

    public static void main(String[] args) {
        System.setProperty("server.port", "8081");
        SpringApplication.run(SkillAApplication.class, args);
    }

    @Bean
    public AgentConfig agentSdkConfig() {
        return AgentConfig.builder()
                .agentId("skill-a-service-discovery-001")
                .agentName("SkillA-Service-Discovery")
                .agentType("end")
                .endpoint("http://localhost:8081")
                .udpPort(9001)
                .heartbeatInterval(30000)
                .build();
    }

    @Autowired
    private AgentConfig agentSdkConfig;

    @Bean
    public net.ooder.examples.skilla.skill.InformationRetrievalSkill informationRetrievalSkill() {
        return new net.ooder.examples.skilla.skill.InformationRetrievalSkill();
    }

    @Autowired
    private net.ooder.examples.skilla.skill.InformationRetrievalSkill informationRetrievalSkill;

    @Override
    public void run(String... args) throws Exception {
        log.info("Skill A - Service Discovery Test Application Starting");

        agentSDK = AgentSDK.builder().config(agentSdkConfig).build();
        agentSDK.start();

        // Register the skill with AgentSDK
        informationRetrievalSkill.initialize();
        agentSDK.registerSkill(informationRetrievalSkill);

        log.info("Skill A Service Discovery Test started successfully");
        log.info("Agent ID: {}", agentSdkConfig.getAgentId());
        log.info("Agent Type: {}", agentSdkConfig.getAgentType());
        log.info("Endpoint: {}", agentSdkConfig.getEndpoint());
        log.info("UDP Port: {}", agentSdkConfig.getUdpPort());
        log.info("Information Retrieval Skill registered successfully");
    }

    public void sendTestCommand() {
        Map<String, Object> params = new HashMap<>();
        params.put("query", "test information");

        CompletableFuture<SendResult> future = agentSDK.sendCommand(CommandType.SKILL_INVOKE, params);
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
            // Destroy the skill before stopping the SDK
            if (informationRetrievalSkill != null) {
                informationRetrievalSkill.destroy();
                log.info("Information Retrieval Skill destroyed");
            }
            agentSDK.stop();
            log.info("Skill A Service Discovery Test stopped");
        }
    }
}