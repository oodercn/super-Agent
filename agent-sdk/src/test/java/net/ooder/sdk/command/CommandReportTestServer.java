package net.ooder.sdk.command;

import net.ooder.sdk.AgentSDK;
import net.ooder.sdk.agent.model.AgentConfig;
import net.ooder.sdk.network.udp.UDPMessageHandler;
import net.ooder.sdk.network.packet.AuthPacket;
import net.ooder.sdk.network.packet.CommandPacket;
import net.ooder.sdk.network.packet.HeartbeatPacket;
import net.ooder.sdk.network.packet.RoutePacket;
import net.ooder.sdk.network.packet.StatusReportPacket;
import net.ooder.sdk.network.packet.TaskPacket;
import net.ooder.sdk.network.packet.UDPPacket;
import net.ooder.sdk.skill.Skill;
import net.ooder.sdk.skill.SkillResult;
import net.ooder.sdk.skill.SkillStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * 命令报告测试服务器，用于接收UDP命令并返回报告状态
 */
public class CommandReportTestServer {
    private static final Logger log = LoggerFactory.getLogger(CommandReportTestServer.class);
    private static final int SERVER_PORT = 5680;
    private static final String AGENT_ID = "test-report-agent-server";
    
    private AgentSDK agentSDK;
    private CountDownLatch shutdownLatch = new CountDownLatch(1);

    public static void main(String[] args) throws Exception {
        CommandReportTestServer server = new CommandReportTestServer();
        server.start();
        System.out.println("Command Report Test Server started on port " + SERVER_PORT);
        System.out.println("Waiting for commands...");
        System.out.println("Press Enter to shutdown");
        
        // 等待用户输入以关闭服务器
        System.in.read();
        
        server.shutdown();
        System.out.println("Command Report Test Server shutdown");
    }

    public void start() throws Exception {
        // 创建Agent配置
        AgentConfig config = new AgentConfig();
        config.setAgentId(AGENT_ID);
        config.setAgentName("Test Report Agent Server");
        config.setAgentType("end");
        config.setEndpoint("localhost:" + SERVER_PORT);
        config.setUdpPort(SERVER_PORT);
        config.setHeartbeatInterval(10000); // 减少心跳频率
        
        // 初始化AgentSDK
        agentSDK = new AgentSDK(config);
        
        // 添加状态报告监听器
        agentSDK.getUdpSDK().registerMessageHandler(new UDPMessageHandler() {
            @Override
            public void onHeartbeat(HeartbeatPacket packet) {
                // 空实现
            }

            @Override
            public void onCommand(CommandPacket packet) {
                // 空实现
            }

            @Override
            public void onStatusReport(StatusReportPacket packet) {
                log.info("Received status report: type={}, status={}", 
                        packet.getReportType(), packet.getCurrentStatus());
                System.out.println("[REPORT] " + packet.getReportType() + " - " + packet.getCurrentStatus());
            }

            @Override
            public void onAuth(AuthPacket packet) {
                // 空实现
            }

            @Override
            public void onTask(TaskPacket packet) {
                // 空实现
            }

            @Override
            public void onRoute(RoutePacket packet) {
                // 空实现
            }

            @Override
            public void onError(UDPPacket packet, Exception e) {
                log.error("UDP error: {}", e.getMessage(), e);
                System.out.println("[ERROR] UDP error: " + e.getMessage());
            }
        });
        
        // 启动Agent
        agentSDK.start();
        log.info("Command Report Test Server started with agent ID: {}", AGENT_ID);
    }

    public void shutdown() {
        if (agentSDK != null) {
            agentSDK.stop();
        }
        shutdownLatch.countDown();
    }

    public AgentSDK getAgentSDK() {
        return agentSDK;
    }
    
    /**
     * 内部类：测试用的技能实现
     */
    public static class TestSkill implements Skill {
        private final String skillId;
        private final String description;
        private final Map<String, String> parameters;
        private SkillStatus status = SkillStatus.UNINITIALIZED;
        
        public TestSkill(String skillId, String description) {
            this.skillId = skillId;
            this.description = description;
            this.parameters = new HashMap<>();
            this.parameters.put("param1", "string - 测试参数1");
            this.parameters.put("param2", "integer - 测试参数2");
        }

        @Override
        public String getSkillId() {
            return skillId;
        }

        @Override
        public String getName() {
            return description; // 使用description作为名称
        }

        @Override
        public String getDescription() {
            return description;
        }

        @Override
        public Map<String, String> getParameters() {
            return parameters;
        }

        @Override
        public SkillResult execute(Map<String, Object> params) {
            status = SkillStatus.EXECUTING;
            Map<String, Object> resultData = new HashMap<>();
            resultData.put("skillId", skillId);
            resultData.put("inputParams", params);
            resultData.put("message", "Skill invoked successfully: " + description);
            resultData.put("timestamp", System.currentTimeMillis());
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("skillId", skillId);
            metadata.put("executionTime", System.currentTimeMillis());
            status = SkillStatus.READY;
            return SkillResult.success(resultData, metadata);
        }

        @Override
        public void initialize() {
            status = SkillStatus.READY;
        }

        @Override
        public void destroy() {
            status = SkillStatus.DESTROYED;
        }

        @Override
        public SkillStatus getStatus() {
            return status;
        }
    }
}