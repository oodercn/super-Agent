package net.ooder.examples.skillc;

import net.ooder.sdk.command.model.CommandType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.ooder.sdk.agent.model.AgentConfig;
import net.ooder.sdk.AgentSDK;
import net.ooder.sdk.network.packet.StatusReportPacket;
import net.ooder.sdk.network.udp.SendResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@SpringBootApplication(
        scanBasePackages = "net.ooder.examples.skillc",
        exclude = {
                DataSourceAutoConfiguration.class,
                HibernateJpaAutoConfiguration.class
        }
)
public class SkillCApplication implements CommandLineRunner, ApplicationListener<ContextClosedEvent> {

    private static final Logger log = LoggerFactory.getLogger(SkillCApplication.class);

    private AgentSDK agentSDK;

    @Autowired
    private ApplicationContext applicationContext;

    public static void main(String[] args) {
        SpringApplication.run(SkillCApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Skill C - Route Agent Application Starting");

        AgentConfig config = AgentConfig.builder()
                .agentId("skill-c-001")
                .agentName("SkillC")
                .agentType("route")
                .endpoint("http://localhost:9010")
                .udpPort(9011)
                .heartbeatInterval(30000)
                .build();

        agentSDK = AgentSDK.builder().config(config).build();

        agentSDK.start();

        log.info("Skill C started successfully");
        log.info("Agent ID: {}", config.getAgentId());
        log.info("Agent Type: {}", config.getAgentType());
        log.info("Endpoint: {}", config.getEndpoint());
    }

    public void sendTestCommand() {
        Map<String, Object> params = new HashMap<>();
        params.put("route", "test route");

        CompletableFuture<SendResult> future = agentSDK.sendCommand(CommandType.ROUTE_FORWARD, params);
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
                .reportType("route_status")
                .entityType("route")
                .entityId("skill-c-001")
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
            log.info("Skill C stopped");
        }
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        log.info("Application context closing, performing cleanup");
        
        // 关闭SkillCommandHandler的线程池
        try {
            SkillCommandHandler commandHandler = applicationContext.getBean(SkillCommandHandler.class);
            if (commandHandler != null) {
                commandHandler.shutdown();
            }
        } catch (Exception e) {
            log.error("Failed to shutdown SkillCommandHandler", e);
        }
        
        // 关闭AgentSDK
        shutdown();
        
        log.info("Cleanup completed");
    }
}
