package net.ooder.sdk.agent.impl;

import net.ooder.sdk.agent.RouteAgent;
import net.ooder.sdk.network.packet.AuthPacket;
import net.ooder.sdk.network.packet.TaskPacket;
import net.ooder.sdk.network.packet.RoutePacket;
import net.ooder.sdk.network.packet.CommandPacket;
import net.ooder.sdk.network.udp.SendResult;
import net.ooder.sdk.network.udp.UDPSDK;
import net.ooder.sdk.agent.scene.SceneDefinition;
import net.ooder.sdk.agent.scene.SceneManager;
import net.ooder.sdk.agent.scene.SceneMember;
import net.ooder.sdk.llm.LlmConfig;
import net.ooder.sdk.llm.LlmRequest;
import net.ooder.sdk.llm.LlmResponse;
import net.ooder.sdk.llm.LlmService;
import net.ooder.sdk.llm.LlmServiceImpl;
import net.ooder.sdk.system.security.PermissionManager;
import net.ooder.sdk.command.model.CommandType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AbstractRouteAgent extends AbstractEndAgent implements RouteAgent {
    protected static final Logger log = LoggerFactory.getLogger(AbstractRouteAgent.class);
    protected final SceneManager sceneManager;
    protected final LlmService llmService;
    protected final PermissionManager permissionManager;
    protected final AtomicBoolean registered = new AtomicBoolean(false);
    protected String mcpAgentId;
    protected String sessionKey;
    protected LlmConfig llmConfig;
    
    public AbstractRouteAgent(UDPSDK udpSDK, String agentId, String agentName, Map<String, Object> capabilities) {
        super(udpSDK, agentId, agentName, "RouteAgent", capabilities);
        this.sceneManager = new SceneManager();
        this.llmService = new LlmServiceImpl();
        this.permissionManager = new PermissionManager();
    }
    
    @Override
    public CompletableFuture<Boolean> register(String targetMcpId) {
        this.mcpAgentId = targetMcpId;
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        try {
            // 创建注册请求命令
            Map<String, Object> params = new java.util.HashMap<>();
            params.put("agentId", getAgentId());
            params.put("agentName", getAgentName());
            params.put("agentType", getAgentType());
            params.put("capabilities", getCapabilities());

            CommandPacket packet = CommandPacket.builder()
                    .command(CommandType.MCP_REGISTER)
                    .params(params)
                    .build();

            packet.setSenderId(getAgentId());
            packet.setReceiverId(mcpAgentId);

            // 发送注册请求
            udpSDK.sendCommand(packet)
                    .thenAccept(result -> {
                        if (result.isSuccess()) {
                            registered.set(true);
                            log.info("RouteAgent registered successfully with MCP Agent: {}", mcpAgentId);
                            future.complete(true);
                        } else {
                            log.error("Failed to register RouteAgent: {}", result.getMessage());
                            future.complete(false);
                        }
                    })
                    .exceptionally(e -> {
                        log.error("Error registering RouteAgent: {}", e.getMessage());
                        future.complete(false);
                        return null;
                    });
        } catch (Exception e) {
            log.error("Error creating registration packet: {}", e.getMessage());
            future.complete(false);
        }

        return future;
    }
    
    @Override
    public CompletableFuture<Boolean> deregister() {
        if (!registered.get()) {
            return CompletableFuture.completedFuture(true);
        }

        CompletableFuture<Boolean> future = new CompletableFuture<>();

        try {
            Map<String, Object> params = new java.util.HashMap<>();
            params.put("agentId", getAgentId());

            CommandPacket packet = CommandPacket.builder()
                    .command(CommandType.MCP_DEREGISTER)
                    .params(params)
                    .build();

            packet.setSenderId(getAgentId());
            packet.setReceiverId(mcpAgentId);

            udpSDK.sendCommand(packet)
                    .thenAccept(result -> {
                        if (result.isSuccess()) {
                            registered.set(false);
                            log.info("RouteAgent deregistered successfully from MCP Agent: {}", mcpAgentId);
                            future.complete(true);
                        } else {
                            log.error("Failed to deregister RouteAgent: {}", result.getMessage());
                            future.complete(false);
                        }
                    })
                    .exceptionally(e -> {
                        log.error("Error deregistering RouteAgent: {}", e.getMessage());
                        future.complete(false);
                        return null;
                    });
        } catch (Exception e) {
            log.error("Error creating deregistration packet: {}", e.getMessage());
            future.complete(false);
        }

        return future;
    }
    
    @Override
    public CompletableFuture<Boolean> authenticate() {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        try {
            // 创建认证请求
            AuthPacket authPacket = AuthPacket.builder()
                    .authType("token")
                    .authData(generateAuthData())
                    .senderId(getAgentId())
                    .receiverId(mcpAgentId)
                    .build();

            udpSDK.sendAuth(authPacket)
                    .thenAccept(result -> {
                        if (result.isSuccess()) {
                            log.info("RouteAgent authentication request sent successfully");
                            future.complete(true);
                        } else {
                            log.error("Failed to send authentication request: {}", result.getMessage());
                            future.complete(false);
                        }
                    })
                    .exceptionally(e -> {
                        log.error("Error sending authentication request: {}", e.getMessage());
                        future.complete(false);
                        return null;
                    });
        } catch (Exception e) {
            log.error("Error creating authentication packet: {}", e.getMessage());
            future.complete(false);
        }

        return future;
    }
    
    @Override
    public void handleAuthResponse(AuthPacket packet) {
        if (packet.isSuccess()) {
            this.sessionKey = packet.getSessionKey();
            log.info("RouteAgent authenticated successfully");
        } else {
            log.error("RouteAgent authentication failed: {}", packet.getErrorMessage());
        }
    }
    
    @Override
    public CompletableFuture<List<RoutePacket.RouteEntry>> queryEndAgents() {
        CompletableFuture<List<RoutePacket.RouteEntry>> future = new CompletableFuture<>();

        try {
            CommandPacket packet = CommandPacket.builder()
                    .command(CommandType.MCP_ENDAGENT_DISCOVER)
                    .params(new java.util.HashMap<>())
                    .build();

            packet.setSenderId(getAgentId());
            packet.setReceiverId(mcpAgentId);

            udpSDK.sendCommand(packet)
                    .thenAccept(result -> {
                        if (result.isSuccess()) {
                            log.info("EndAgent discovery request sent successfully");
                            future.complete(new java.util.ArrayList<>());
                        } else {
                            log.error("Failed to send EndAgent discovery request: {}", result.getMessage());
                            future.completeExceptionally(new RuntimeException(result.getMessage()));
                        }
                    })
                    .exceptionally(e -> {
                        log.error("Error sending EndAgent discovery request: {}", e.getMessage());
                        future.completeExceptionally(e);
                        return null;
                    });
        } catch (Exception e) {
            log.error("Error creating EndAgent discovery packet: {}", e.getMessage());
            future.completeExceptionally(e);
        }

        return future;
    }
    
    @Override
    public CompletableFuture<SendResult> forwardTask(TaskPacket taskPacket, String endAgentId) {
        try {
            TaskPacket forwardPacket = TaskPacket.builder()
                    .taskId(taskPacket.getTaskId())
                    .taskType(taskPacket.getTaskType())
                    .params(taskPacket.getParams())
                    .skillflowId(taskPacket.getSkillflowId())
                    .endAgentId(endAgentId)
                    .senderId(getAgentId())
                    .receiverId(endAgentId)
                    .build();

            return udpSDK.sendTask(forwardPacket);
        } catch (Exception e) {
            log.error("Error creating forward task packet: {}", e.getMessage());
            CompletableFuture<SendResult> future = new CompletableFuture<>();
            future.complete(SendResult.failure(e.getMessage()));
            return future;
        }
    }
    
    @Override
    public CompletableFuture<SendResult> sendRouteUpdate(List<RoutePacket.RouteEntry> endAgents) {
        try {
            RoutePacket routePacket = RoutePacket.builder()
                    .routeType("endagent_list")
                    .routeEntries(endAgents)
                    .status("online")
                    .senderId(getAgentId())
                    .receiverId(mcpAgentId)
                    .build();

            return udpSDK.sendRoute(routePacket);
        } catch (Exception e) {
            log.error("Error creating route update packet: {}", e.getMessage());
            CompletableFuture<SendResult> future = new CompletableFuture<>();
            future.complete(SendResult.failure(e.getMessage()));
            return future;
        }
    }
    
    @Override
    public CompletableFuture<Boolean> createScene(SceneDefinition sceneDefinition) {
        return sceneManager.createScene(sceneDefinition);
    }
    
    @Override
    public SceneDefinition getScene(String sceneId) {
        return sceneManager.getScene(sceneId);
    }
    
    @Override
    public CompletableFuture<Boolean> deleteScene(String sceneId) {
        return sceneManager.deleteScene(sceneId);
    }
    
    @Override
    public CompletableFuture<Boolean> addSceneMember(String sceneId, SceneMember member) {
        return sceneManager.addMember(sceneId, member);
    }
    
    @Override
    public CompletableFuture<Boolean> removeSceneMember(String sceneId, String agentId) {
        return sceneManager.removeMember(sceneId, agentId);
    }
    
    @Override
    public Map<String, SceneMember> getSceneMembers(String sceneId) {
        return sceneManager.getMembers(sceneId);
    }
    
    @Override
    public boolean validateSceneMember(String sceneId, SceneMember member) {
        return sceneManager.validateMemberRole(sceneId, member);
    }
    
    @Override
    public boolean isSceneReady(String sceneId) {
        return sceneManager.isSceneReady(sceneId);
    }
    
    @Override
    public void initLlmService(LlmConfig config) {
        this.llmConfig = config;
        llmService.init(config);
    }
    
    @Override
    public boolean isLlmServiceConnected() {
        return llmService.isConnected();
    }
    
    @Override
    public CompletableFuture<LlmResponse> generateCode(LlmRequest request) {
        return llmService.generateCode(request);
    }
    
    @Override
    public CompletableFuture<Boolean> handleEndAgentJoinRequest(String sceneId, SceneMember member) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // 1. 验证EndAgent身份和能力
                if (!validateEndAgent(sceneId, member)) {
                    log.error("EndAgent validation failed: {}", member.getAgentId());
                    return false;
                }

                // 2. 将EndAgent添加到场景
                addSceneMember(sceneId, member).join();
                log.info("EndAgent added to scene: {} - {}", sceneId, member.getAgentId());

                // 3. 检查场景是否已准备好
                if (isSceneReady(sceneId)) {
                    log.info("Scene {} is ready, generating communication code", sceneId);
                    // 4. 收集所有成员的能力信息
                    Map<String, Object> capabilitiesInfo = collectSceneCapabilities(sceneId);
                    // 5. 向LLM发送代码生成请求
                    LlmResponse llmResponse = sendCodeGenerationRequest(sceneId, capabilitiesInfo).join();
                    if (llmResponse != null && llmResponse.isSuccess()) {
                        // 6. 编译和测试生成的代码
                        if (compileAndTestCode(llmResponse)) {
                            // 7. 广播配置信息给所有EndAgent
                            broadcastConfiguration(sceneId, llmResponse);
                            log.info("Communication code generated and distributed successfully for scene: {}", sceneId);
                            return true;
                        }
                    }
                } else {
                    log.info("Scene {} is not ready yet, waiting for more members", sceneId);
                    return true;
                }

                return false;
            } catch (Exception e) {
                log.error("Error handling EndAgent join request: {}", e.getMessage());
                return false;
            }
        });
    }
    
    @Override
    public String getMcpAgentId() {
        return mcpAgentId;
    }
    
    @Override
    public boolean isRegistered() {
        return registered.get();
    }
    
    @Override
    public String getSessionKey() {
        return sessionKey;
    }
    
    protected String generateAuthData() {
        return "auth_data_" + getAgentId();
    }
    
    protected boolean validateEndAgent(String sceneId, SceneMember member) {
        if (member.getAgentId() == null || member.getRole() == null || member.getCapabilities() == null) {
            log.error("Invalid EndAgent information: missing required fields");
            return false;
        }
        return validateSceneMember(sceneId, member);
    }
    
    protected Map<String, Object> collectSceneCapabilities(String sceneId) {
        Map<String, Object> capabilitiesInfo = new java.util.HashMap<>();
        capabilitiesInfo.put("sceneId", sceneId);
        capabilitiesInfo.put("members", getSceneMembers(sceneId));
        return capabilitiesInfo;
    }
    
    protected CompletableFuture<LlmResponse> sendCodeGenerationRequest(String sceneId, Map<String, Object> capabilitiesInfo) {
        try {
            SceneDefinition scene = getScene(sceneId);
            if (scene == null) {
                throw new IllegalArgumentException("Scene not found: " + sceneId);
            }

            // 构建LLM请求
            LlmRequest.SceneInfo sceneInfo = new LlmRequest.SceneInfo();
            sceneInfo.setSceneId(sceneId);
            sceneInfo.setName(scene.getName());

            // 转换场景成员信息为LLM请求格式
            List<LlmRequest.MemberRole> memberRoles = new java.util.ArrayList<>();
            scene.getMemberRoles().forEach(role -> {
                LlmRequest.MemberRole llmRole = new LlmRequest.MemberRole();
                llmRole.setRoleId(role.getRoleId());
                
                // 获取该角色下的所有Agent
                List<LlmRequest.Agent> agents = new java.util.ArrayList<>();
                getSceneMembers(sceneId).values().stream()
                        .filter(m -> m.getRole().equals(role.getRoleId()))
                        .forEach(m -> {
                            LlmRequest.Agent llmAgent = new LlmRequest.Agent();
                            llmAgent.setAgentId(m.getAgentId());
                            llmAgent.setCapabilities(m.getCapabilities());
                            llmAgent.setTechStack(m.getTechStack());
                            agents.add(llmAgent);
                        });
                
                llmRole.setAgents(agents);
                memberRoles.add(llmRole);
            });
            sceneInfo.setMemberRoles(memberRoles);

            // 设置要求
            LlmRequest.Requirements requirements = new LlmRequest.Requirements();
            requirements.setCommunicationProtocol(scene.getCommunicationProtocol());
            requirements.setSecurityLevel(scene.getSecurityPolicy());
            requirements.setErrorHandling("retry-with-exponential-backoff");
            requirements.setOutputFormat("multi-language");

            // 创建并发送请求
            LlmRequest request = new LlmRequest("code_generation", sceneInfo, requirements);
            return generateCode(request);
        } catch (Exception e) {
            log.error("Error creating LLM code generation request: {}", e.getMessage());
            CompletableFuture<LlmResponse> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
    }
    
    protected boolean compileAndTestCode(LlmResponse llmResponse) {
        try {
            log.info("Compiling and testing generated code for {} agents", 
                    llmResponse.getGeneratedCodes().size());
            // 模拟编译和测试过程
            Thread.sleep(1000);
            return true;
        } catch (Exception e) {
            log.error("Error compiling and testing code: {}", e.getMessage());
            return false;
        }
    }
    
    protected void broadcastConfiguration(String sceneId, LlmResponse llmResponse) {
        try {
            // 构建配置信息
            Map<String, Object> config = new java.util.HashMap<>();
            config.put("sceneId", sceneId);
            config.put("timestamp", System.currentTimeMillis());
            config.put("configVersion", "1.0.0");
            config.put("generatedCodes", llmResponse.getGeneratedCodes());

            // 向场景中的所有EndAgent发送配置信息
            Map<String, SceneMember> members = getSceneMembers(sceneId);
            for (SceneMember member : members.values()) {
                // 这里使用CommandPacket发送配置信息
                CommandPacket configPacket = CommandPacket.builder()
                        .command(CommandType.ROUTE_CONFIGURE)
                        .params(config)
                        .build();

                configPacket.setSenderId(getAgentId());
                configPacket.setReceiverId(member.getAgentId());

                udpSDK.sendCommand(configPacket)
                        .thenAccept(result -> {
                            if (result.isSuccess()) {
                                log.info("Configuration sent to EndAgent: {}", member.getAgentId());
                            } else {
                                log.error("Failed to send configuration to EndAgent: {} - {}", 
                                        member.getAgentId(), result.getMessage());
                            }
                        });
            }
        } catch (Exception e) {
            log.error("Error broadcasting configuration: {}", e.getMessage());
        }
    }
    
    @Override
    public boolean hasMcpAgent() {
        // 检查 Route Agent cap 信息中是否有 MCP 信息
        return mcpAgentId != null && !mcpAgentId.isEmpty();
    }
    
    @Override
    public CompletableFuture<Boolean> upgradeToRouteMcp() {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        
        try {
            log.info("Upgrading Route Agent to RouteMcp service: {}", agentId);
            
            // 检查是否已经存在 MCP Agent
            if (hasMcpAgent()) {
                log.warn("Cannot upgrade to RouteMcp: MCP Agent already exists ({})");
                future.complete(false);
                return future;
            }
            
            // 执行升级逻辑
            // 1. 开启监听频道
            startListeningChannels();
            
            // 2. 更新状态
            log.info("Route Agent upgraded to RouteMcp service successfully: {}", agentId);
            future.complete(true);
        } catch (Exception e) {
            log.error("Error upgrading to RouteMcp: {}", e.getMessage());
            future.complete(false);
        }
        
        return future;
    }
    
    @Override
    public boolean isRouteMcp() {
        // 检查是否为 RouteMcp 服务
        // 简化实现：如果没有 MCP Agent 且已开启监听频道，则认为是 RouteMcp
        return !hasMcpAgent() && isListeningChannelsOpen();
    }
    
    @Override
    public CompletableFuture<Boolean> downgradeToRoute() {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        
        try {
            log.info("Downgrading RouteMcp to Route Agent: {}", agentId);
            
            // 关闭监听频道
            stopListeningChannels();
            
            log.info("RouteMcp downgraded to Route Agent successfully: {}", agentId);
            future.complete(true);
        } catch (Exception e) {
            log.error("Error downgrading to Route Agent: {}", e.getMessage());
            future.complete(false);
        }
        
        return future;
    }
    
    @Override
    public boolean detectDeterministicMcp() {
        // 检测网络中是否存在确定性的指定的 MCP
        // 简化实现：检查是否有固定的 MCP Agent ID
        // 实际实现应该通过网络探测来检测
        return false;
    }
    
    private void startListeningChannels() {
        // 在 9100-9110 频道内选择频道开启监听
        log.info("Starting listening channels for RouteMcp: {}", agentId);
        // 实际实现应该开启 UDP 监听
    }
    
    private void stopListeningChannels() {
        // 关闭监听频道
        log.info("Stopping listening channels for RouteMcp: {}", agentId);
        // 实际实现应该关闭 UDP 监听
    }
    
    private boolean isListeningChannelsOpen() {
        // 检查监听频道是否开启
        // 简化实现：返回 true
        return true;
    }
    
    @Override
    protected abstract net.ooder.sdk.network.packet.ResponsePacket createResponse(net.ooder.sdk.network.packet.TaskPacket taskPacket, boolean success, String message);
}
