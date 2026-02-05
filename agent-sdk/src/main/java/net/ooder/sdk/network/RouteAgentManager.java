package net.ooder.sdk.network;

import net.ooder.sdk.command.model.CommandType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.ooder.sdk.llm.LlmConfig;
import net.ooder.sdk.llm.LlmRequest;
import net.ooder.sdk.llm.LlmResponse;
import net.ooder.sdk.llm.LlmService;
import net.ooder.sdk.llm.LlmServiceImpl;
import net.ooder.sdk.network.packet.AuthPacket;
import net.ooder.sdk.network.packet.CommandPacket;
import net.ooder.sdk.network.packet.RoutePacket;
import net.ooder.sdk.network.packet.TaskPacket;
import net.ooder.sdk.agent.scene.SceneDefinition;
import net.ooder.sdk.agent.scene.SceneManager;
import net.ooder.sdk.agent.scene.SceneMember;
import net.ooder.sdk.system.security.CodePermission;
import net.ooder.sdk.system.security.PermissionManager;
import net.ooder.sdk.system.security.ResourcePermission;
import net.ooder.sdk.system.security.SecurityUtils;
import net.ooder.sdk.network.udp.SendResult;
import net.ooder.sdk.network.udp.UDPSDK;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

public class RouteAgentManager {
    private static final Logger log = LoggerFactory.getLogger(RouteAgentManager.class);
    private final UDPSDK udpSDK;
    private final String routeAgentId;
    private final String routeAgentName;
    private final Map<String, Object> capabilities;
    private final AtomicBoolean registered = new AtomicBoolean(false);
    private final SceneManager sceneManager;
    private final LlmService llmService;
    private final PermissionManager permissionManager;
    private String mcpAgentId;
    private String sessionKey;
    private LlmConfig llmConfig;

    public RouteAgentManager(UDPSDK udpSDK, String routeAgentId, String routeAgentName, Map<String, Object> capabilities) {
        this.udpSDK = udpSDK;
        this.routeAgentId = routeAgentId;
        this.routeAgentName = routeAgentName;
        this.capabilities = capabilities != null ? capabilities : new HashMap<>();
        this.sceneManager = new SceneManager();
        this.llmService = new LlmServiceImpl();
        this.permissionManager = new PermissionManager();
    }

    /**
     * 向MCP Agent注册RouteAgent
     */
    public CompletableFuture<Boolean> register(String targetMcpId) {
        this.mcpAgentId = targetMcpId;
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        try {
            // 创建注册请求命令
            Map<String, Object> params = new HashMap<>();
            params.put("agentId", routeAgentId);
            params.put("agentName", routeAgentName);
            params.put("agentType", "RouteAgent");
            params.put("capabilities", capabilities);

            CommandPacket packet = CommandPacket.builder()
                    .command(CommandType.MCP_REGISTER)
                    .params(params)
                    .build();

            packet.setSenderId(routeAgentId);
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

    /**
     * 向MCP Agent注销RouteAgent
     */
    public CompletableFuture<Boolean> deregister() {
        if (!registered.get()) {
            return CompletableFuture.completedFuture(true);
        }

        CompletableFuture<Boolean> future = new CompletableFuture<>();

        try {
            Map<String, Object> params = new HashMap<>();
            params.put("agentId", routeAgentId);

            CommandPacket packet = CommandPacket.builder()
                    .command(CommandType.MCP_DEREGISTER)
                    .params(params)
                    .build();

            packet.setSenderId(routeAgentId);
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

    /**
     * 认证RouteAgent
     */
    public CompletableFuture<Boolean> authenticate() {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        try {
            // 创建认证请求
            AuthPacket authPacket = AuthPacket.builder()
                    .authType("token")
                    .authData(generateAuthData())
                    .senderId(routeAgentId)
                    .receiverId(mcpAgentId)
                    .build();

            udpSDK.sendAuth(authPacket)
                    .thenAccept(result -> {
                        if (result.isSuccess()) {
                            log.info("RouteAgent authentication request sent successfully");
                            // 等待认证响应
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

    /**
     * 处理认证响应
     */
    public void handleAuthResponse(AuthPacket packet) {
        if (packet.isSuccess()) {
            this.sessionKey = packet.getSessionKey();
            log.info("RouteAgent authenticated successfully");
        } else {
            log.error("RouteAgent authentication failed: {}", packet.getErrorMessage());
        }
    }

    /**
     * 查询EndAgent列表
     */
    public CompletableFuture<List<RoutePacket.RouteEntry>> queryEndAgents() {
        CompletableFuture<List<RoutePacket.RouteEntry>> future = new CompletableFuture<>();

        try {
            CommandPacket packet = CommandPacket.builder()
                    .command(CommandType.MCP_ENDAGENT_DISCOVER)
                    .params(new HashMap<>())
                    .build();

            packet.setSenderId(routeAgentId);
            packet.setReceiverId(mcpAgentId);

            udpSDK.sendCommand(packet)
                    .thenAccept(result -> {
                        if (result.isSuccess()) {
                            log.info("EndAgent discovery request sent successfully");
                            // 等待响应处理
                            future.complete(null);
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

    /**
     * 转发任务到EndAgent
     */
    public CompletableFuture<SendResult> forwardTask(TaskPacket taskPacket, String endAgentId) {
        try {
            TaskPacket forwardPacket = TaskPacket.builder()
                    .taskId(taskPacket.getTaskId())
                    .taskType(taskPacket.getTaskType())
                    .params(taskPacket.getParams())
                    .skillflowId(taskPacket.getSkillflowId())
                    .endAgentId(endAgentId)
                    .senderId(routeAgentId)
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

    /**
     * 发送路由状态更新
     */
    public CompletableFuture<SendResult> sendRouteUpdate(List<RoutePacket.RouteEntry> endAgents) {
        try {
            RoutePacket routePacket = RoutePacket.builder()
                    .routeType("endagent_list")
                    .routeEntries(endAgents)
                    .status("online")
                    .senderId(routeAgentId)
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

    /**
     * 生成认证数据
     */
    private String generateAuthData() {
        // 使用SecurityUtils生成更安全的认证令牌
        return SecurityUtils.generateAuthToken(routeAgentId, routeAgentName);
    }

    /**
     * 获取当前注册状态
     */
    public boolean isRegistered() {
        return registered.get();
    }

    /**
     * 获取会话密钥
     */
    public String getSessionKey() {
        return sessionKey;
    }

    /**
     * 获取RouteAgent ID
     */
    public String getRouteAgentId() {
        return routeAgentId;
    }

    /**
     * 创建新场景
     */
    public CompletableFuture<Boolean> createScene(SceneDefinition sceneDefinition) {
        return sceneManager.createScene(sceneDefinition);
    }

    /**
     * 获取场景定义
     */
    public SceneDefinition getScene(String sceneId) {
        return sceneManager.getScene(sceneId);
    }

    /**
     * 删除场景
     */
    public CompletableFuture<Boolean> deleteScene(String sceneId) {
        return sceneManager.deleteScene(sceneId);
    }

    /**
     * 向场景添加成员
     */
    public CompletableFuture<Boolean> addSceneMember(String sceneId, SceneMember member) {
        return sceneManager.addMember(sceneId, member);
    }

    /**
     * 从场景移除成员
     */
    public CompletableFuture<Boolean> removeSceneMember(String sceneId, String agentId) {
        return sceneManager.removeMember(sceneId, agentId);
    }

    /**
     * 获取场景中的成员
     */
    public Map<String, SceneMember> getSceneMembers(String sceneId) {
        return sceneManager.getMembers(sceneId);
    }

    /**
     * 验证成员角色是否符合场景要求
     */
    public boolean validateSceneMember(String sceneId, SceneMember member) {
        return sceneManager.validateMemberRole(sceneId, member);
    }

    /**
     * 检查场景是否已满足所有必填角色要求
     */
    public boolean isSceneReady(String sceneId) {
        return sceneManager.isSceneReady(sceneId);
    }

    /**
     * 初始化LLM Service
     */
    public void initLlmService(LlmConfig config) {
        this.llmConfig = config;
        llmService.init(config);
    }

    /**
     * 检查LLM Service连接状态
     */
    public boolean isLlmServiceConnected() {
        return llmService.isConnected();
    }

    /**
     * 发送代码生成请求到LLM Service
     */
    public CompletableFuture<LlmResponse> generateCode(LlmRequest request) {
        return llmService.generateCode(request);
    }

    /**
     * 获取MCP Agent ID
     */
    public String getMcpAgentId() {
        return mcpAgentId;
    }

    /**
     * 处理EndAgent加入请求
     */
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
                    return true; // EndAgent added but scene not ready yet
                }

                return false;
            } catch (Exception e) {
                log.error("Error handling EndAgent join request: {}", e.getMessage());
                return false;
            }
        });
    }

    /**
     * 验证EndAgent身份和能力
     */
    private boolean validateEndAgent(String sceneId, SceneMember member) {
        // 实现身份验证逻辑
        if (member.getAgentId() == null || member.getRole() == null || member.getCapabilities() == null) {
            log.error("Invalid EndAgent information: missing required fields");
            return false;
        }

        // 验证角色和能力是否符合场景要求
        return validateSceneMember(sceneId, member);
    }

    /**
     * 收集场景中所有成员的能力信息
     */
    private Map<String, Object> collectSceneCapabilities(String sceneId) {
        Map<String, Object> capabilitiesInfo = new HashMap<>();
        capabilitiesInfo.put("sceneId", sceneId);
        capabilitiesInfo.put("members", getSceneMembers(sceneId));
        return capabilitiesInfo;
    }

    /**
     * 向LLM发送代码生成请求
     */
    private CompletableFuture<LlmResponse> sendCodeGenerationRequest(String sceneId, Map<String, Object> capabilitiesInfo) {
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
            List<LlmRequest.MemberRole> memberRoles = new ArrayList<>();
            scene.getMemberRoles().forEach(role -> {
                LlmRequest.MemberRole llmRole = new LlmRequest.MemberRole();
                llmRole.setRoleId(role.getRoleId());
                
                // 获取该角色下的所有Agent
                List<LlmRequest.Agent> agents = new ArrayList<>();
                getSceneMembers(sceneId).values().stream()
                        .filter(member -> member.getRole().equals(role.getRoleId()))
                        .forEach(member -> {
                            LlmRequest.Agent llmAgent = new LlmRequest.Agent();
                            llmAgent.setAgentId(member.getAgentId());
                            llmAgent.setCapabilities(member.getCapabilities());
                            llmAgent.setTechStack(member.getTechStack());
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

    /**
     * 编译和测试生成的代码
     */
    private boolean compileAndTestCode(LlmResponse llmResponse) {
        // 这里只是一个简化的实现，实际需要调用代码编译和测试模块
        // 在后续任务中会实现完整的代码编译与测试机制
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

    /**
     * 广播配置信息给所有EndAgent
     */
    private void broadcastConfiguration(String sceneId, LlmResponse llmResponse) {
        try {
            // 构建配置信息
            Map<String, Object> config = new HashMap<>();
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

                configPacket.setSenderId(routeAgentId);
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

    /**
     * 添加代码权限
     */
    public CompletableFuture<Boolean> addCodePermission(CodePermission permission) {
        return permissionManager.addCodePermission(permission);
    }

    /**
     * 获取代码权限
     */
    public CompletableFuture<CodePermission> getCodePermission(String permissionId) {
        return permissionManager.getCodePermission(permissionId);
    }

    /**
     * 删除代码权限
     */
    public CompletableFuture<Boolean> removeCodePermission(String permissionId) {
        return permissionManager.removeCodePermission(permissionId);
    }

    /**
     * 获取Agent的所有代码权限
     */
    public CompletableFuture<Map<String, CodePermission>> getAgentCodePermissions(String agentId) {
        return permissionManager.getAgentCodePermissions(agentId);
    }

    /**
     * 验证代码操作权限
     */
    public CompletableFuture<Boolean> verifyCodePermission(String agentId, String codeId, String operation) {
        return permissionManager.verifyCodePermission(agentId, codeId, operation);
    }

    /**
     * 添加资源权限
     */
    public CompletableFuture<Boolean> addResourcePermission(ResourcePermission permission) {
        return permissionManager.addResourcePermission(permission);
    }

    /**
     * 获取资源权限
     */
    public CompletableFuture<ResourcePermission> getResourcePermission(String permissionId) {
        return permissionManager.getResourcePermission(permissionId);
    }

    /**
     * 删除资源权限
     */
    public CompletableFuture<Boolean> removeResourcePermission(String permissionId) {
        return permissionManager.removeResourcePermission(permissionId);
    }

    /**
     * 获取Agent的所有资源权限
     */
    public CompletableFuture<Map<String, ResourcePermission>> getAgentResourcePermissions(String agentId) {
        return permissionManager.getAgentResourcePermissions(agentId);
    }

    /**
     * 验证资源操作权限
     */
    public CompletableFuture<Boolean> verifyResourcePermission(String agentId, String resourceId, String action) {
        return permissionManager.verifyResourcePermission(agentId, resourceId, action);
    }

    /**
     * 验证资源使用限制
     */
    public CompletableFuture<Boolean> verifyResourceUsage(String agentId, String resourceId, long usage) {
        return permissionManager.verifyResourceUsage(agentId, resourceId, usage);
    }

    /**
     * 关闭所有服务连接
     */
    public void close() {
        llmService.close();
    }
}