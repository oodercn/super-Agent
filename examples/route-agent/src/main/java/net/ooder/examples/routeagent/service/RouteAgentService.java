package net.ooder.examples.routeagent.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import net.ooder.sdk.AgentConfig;
import net.ooder.sdk.AgentSDK;
import net.ooder.sdk.packet.TaskPacket;
import net.ooder.sdk.packet.RoutePacket;
import net.ooder.sdk.udp.SendResult;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class RouteAgentService {
    private static final Logger log = LoggerFactory.getLogger(RouteAgentService.class);
    private AgentSDK agentSDK;
    private final String routeAgentId = "test-route-agent-001";
    private final String routeAgentName = "TestRouteAgent";
    private final String mcpAgentId = "test-mcp-agent-001";

    @PostConstruct
    public void init() {
        try {
            // 创建Agent配置
            AgentConfig config = AgentConfig.builder()
                    .agentId(routeAgentId)
                    .agentName(routeAgentName)
                    .agentType("RouteAgent")
                    .endpoint("http://localhost:9002")
                    .udpPort(9002)
                    .build();

            // 初始化AgentSDK
            agentSDK = AgentSDK.builder()
                    .config(config)
                    .build();

            // 创建RouteAgent管理器，设置能力
            Map<String, Object> capabilities = new HashMap<>();
            capabilities.put("forwarding", true);
            capabilities.put("loadBalancing", true);
            capabilities.put("maxConnections", 100);
            capabilities.put("supportEncryption", true);

            agentSDK.createRouteAgentManager(routeAgentName, capabilities);

            // 启动Agent
            agentSDK.start();
            log.info("RouteAgentService initialized successfully");

            // 注册到MCP Agent
            registerToMcpAgent();

        } catch (Exception e) {
            log.error("Failed to initialize RouteAgentService: {}", e.getMessage());
        }
    }

    /**
     * 向MCP Agent注册RouteAgent
     */
    private void registerToMcpAgent() {
        CompletableFuture<Boolean> future = agentSDK.registerRouteAgent(mcpAgentId);
        future.thenAccept(success -> {
            if (success) {
                log.info("RouteAgent registered successfully to MCP Agent: {}", mcpAgentId);
                // 注册成功后，可以开始处理任务
                startTaskProcessing();
            } else {
                log.error("Failed to register RouteAgent to MCP Agent: {}", mcpAgentId);
            }
        });
    }

    /**
     * 开始处理任务
     */
    private void startTaskProcessing() {
        // 模拟接收并转发任务
        log.info("RouteAgent is ready to process tasks");
        
        // 示例：定期查询EndAgent列表
        CompletableFuture.runAsync(() -> {
            while (true) {
                try {
                    queryEndAgents();
                    Thread.sleep(60000); // 每分钟查询一次
                } catch (InterruptedException e) {
                    log.error("Error in EndAgent query loop: {}", e.getMessage());
                    break;
                }
            }
        });
    }

    /**
     * 查询EndAgent列表
     */
    public void queryEndAgents() {
        CompletableFuture<List<RoutePacket.RouteEntry>> future = agentSDK.queryEndAgents();
        future.thenAccept(endAgents -> {
            if (endAgents != null && !endAgents.isEmpty()) {
                log.info("Found {} EndAgents:", endAgents.size());
                for (RoutePacket.RouteEntry entry : endAgents) {
                    log.info("  - {} ({}) at {}", entry.getAgentId(), entry.getAgentType(), entry.getEndpoint());
                }
            } else {
                log.info("No EndAgents found");
            }
        });
    }

    /**
     * 转发任务到EndAgent
     */
    public void forwardTaskToEndAgent(String taskId, String skillflowId, Map<String, Object> params, String endAgentId) {
        TaskPacket taskPacket = TaskPacket.builder()
                .taskId(taskId)
                .taskType("execute")
                .params(params)
                .skillflowId(skillflowId)
                .build();

        CompletableFuture<SendResult> future = agentSDK.forwardTask(taskPacket, endAgentId);
        future.thenAccept(result -> {
            if (result.isSuccess()) {
                log.info("Task {} forwarded successfully to EndAgent: {}", taskId, endAgentId);
            } else {
                log.error("Failed to forward task {} to EndAgent {}: {}", taskId, endAgentId, result.getErrorMessage());
            }
        });
    }

    /**
     * 发送任务结果到MCP Agent
     */
    public void sendTaskResult(String taskId, Map<String, Object> result) {
        CompletableFuture<SendResult> future = agentSDK.sendTaskResult(taskId, result);
        future.thenAccept(sendResult -> {
            if (sendResult.isSuccess()) {
                log.info("Task result for task {} sent successfully to MCP Agent", taskId);
            } else {
                log.error("Failed to send task result for task {}: {}", taskId, sendResult.getErrorMessage());
            }
        });
    }

    @PreDestroy
    public void destroy() {
        if (agentSDK != null) {
            agentSDK.stop();
            log.info("RouteAgentService destroyed successfully");
        }
    }
}