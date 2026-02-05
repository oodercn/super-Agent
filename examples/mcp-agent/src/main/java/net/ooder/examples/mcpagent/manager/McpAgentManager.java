package net.ooder.examples.mcpagent.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.ooder.sdk.enums.CommandType;
import net.ooder.sdk.packet.*;
import net.ooder.sdk.scene.*;
import net.ooder.sdk.udp.UDPSDK;
import net.ooder.sdk.udp.UDPConfig;
import net.ooder.sdk.udp.SendResult;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * MCP Agent Manager
 * 实现MCP Agent的核心功能，包括北上协议、南下协议、数据结构管理等
 */
public class McpAgentManager {
    private static final Logger log = LoggerFactory.getLogger(McpAgentManager.class);
    private final UDPSDK udpSDK;
    private final String mcpAgentId;
    private final String mcpAgentName;
    private final Map<String, Object> capabilities;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final Map<String, RouteAgentInfo> routeAgents = new HashMap<>();
    private final Map<String, EndAgentInfo> endAgents = new HashMap<>();
    private final SceneManager sceneManager;

    public McpAgentManager(String mcpAgentId, String mcpAgentName, Map<String, Object> capabilities) {
        this(mcpAgentId, mcpAgentName, capabilities, null);
    }

    public McpAgentManager(String mcpAgentId, String mcpAgentName, Map<String, Object> capabilities, UDPSDK udpSDK) {
        this.mcpAgentId = mcpAgentId;
        this.mcpAgentName = mcpAgentName;
        this.capabilities = capabilities != null ? capabilities : new HashMap<>();
        
        // 初始化UDP SDK（支持依赖注入）
        if (udpSDK != null) {
            this.udpSDK = udpSDK;
        } else {
            try {
                UDPConfig udpConfig = UDPConfig.builder()
                        .port(9010) // 默认MCP Agent端口
                        .bufferSize(4096)
                        .timeout(5000)
                        .maxPacketSize(65535)
                        .strongTypeEnforcement(true)
                        .allowCustomFormat(false)
                        .build();
                this.udpSDK = new UDPSDK(udpConfig);
            } catch (Exception e) {
                log.error("Error creating UDPSDK: {}", e.getMessage());
                throw new RuntimeException("Failed to initialize UDPSDK", e);
            }
        }
        
        // 初始化场景管理器
        this.sceneManager = new SceneManager();
        
        // 设置消息处理器
        setupMessageHandlers();
    }

    private void setupMessageHandlers() {
        // 注册消息处理器的基本框架
        udpSDK.registerMessageHandler(new net.ooder.sdk.udp.UDPMessageHandler() {
            @Override
            public void onHeartbeat(HeartbeatPacket packet) {
                handleHeartbeat(packet);
            }

            @Override
            public void onCommand(CommandPacket packet) {
                handleCommand(packet);
            }

            @Override
            public void onStatusReport(StatusReportPacket packet) {
                log.debug("Received status report: {}", packet);
            }

            @Override
            public void onAuth(AuthPacket packet) {
                log.debug("Received auth packet: {}", packet);
            }

            @Override
            public void onTask(TaskPacket packet) {
                log.debug("Received task packet: {}", packet);
            }

            @Override
            public void onRoute(RoutePacket packet) {
                log.debug("Received route packet: {}", packet);
            }

            @Override
            public void onError(UDPPacket packet, Exception e) {
                log.error("Error handling packet: {}, exception: {}", packet, e.getMessage());
            }
        });
    }

    /**
     * 启动MCP Agent
     */
    public CompletableFuture<Boolean> start() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                udpSDK.startListening();
                running.set(true);
                log.info("MCP Agent started successfully: {}", mcpAgentId);
                return true;
            } catch (Exception e) {
                log.error("Error starting MCP Agent: {}", e.getMessage());
                return false;
            }
        });
    }

    /**
     * 停止MCP Agent
     */
    public void stop() {
        running.set(false);
        udpSDK.stopListening();
        log.info("MCP Agent stopped: {}", mcpAgentId);
    }

    /**
     * 处理心跳包（北上协议）
     */
    private void handleHeartbeat(HeartbeatPacket packet) {
        String senderId = packet.getSenderId();
        log.debug("Received heartbeat from: {}", senderId);
        
        // 检查是RouteAgent还是EndAgent
        if (routeAgents.containsKey(senderId)) {
            RouteAgentInfo routeAgent = routeAgents.get(senderId);
            routeAgent.updateLastHeartbeat();
            log.debug("Updated RouteAgent heartbeat: {}", senderId);
        } else if (endAgents.containsKey(senderId)) {
            EndAgentInfo endAgent = endAgents.get(senderId);
            endAgent.updateLastHeartbeat();
            log.debug("Updated EndAgent heartbeat: {}", senderId);
        }
        
        // 发送心跳响应
        HeartbeatPacket response = new HeartbeatPacket();
        response.setSenderId(mcpAgentId);
        response.setReceiverId(senderId);
        response.setTimestamp(System.currentTimeMillis());
    }

    /**
     * 处理命令包（北上协议）
     */
    public void handleCommand(CommandPacket packet) {
        String senderId = packet.getSenderId();
        CommandType command = packet.getCommand();
        Map<String, Object> params = packet.getParams();
        
        log.debug("Received command from {}: {}, params: {}", senderId, command, params);
        
        // 根据命令类型处理
        switch (command) {
            case MCP_REGISTER:
                handleRouteAgentRegister(senderId, params);
                break;
            case ROUTE_ENDAGENT_REGISTER:
                handleEndAgentRegister(senderId, params);
                break;
            case MCP_ENDAGENT_DISCOVER:
                // 处理EndAgent发现
                break;
            case SCENE_CREATE:
                handleSceneCreate(senderId, params);
                break;
            case SKILL_INVOKE:
                // 处理技能调用
                break;
            case VFS_REGISTER:
                // 处理VFS注册
                break;
            case VFS_STATUS:
                // 处理VFS状态查询
                break;
            default:
                log.warn("Unknown command received: {}", command);
        }
    }

    /**
     * 处理RouteAgent注册请求
     */
    private void handleRouteAgentRegister(String senderId, Map<String, Object> params) {
        try {
            String agentName = (String) params.get("agentName");
            String agentType = (String) params.get("agentType");
            Map<String, Object> agentCapabilities = (Map<String, Object>) params.get("capabilities");
            
            RouteAgentInfo routeAgent = new RouteAgentInfo(senderId, agentName, agentType, agentCapabilities);
            routeAgents.put(senderId, routeAgent);
            
            log.info("RouteAgent registered successfully: {} ({})");
            
        } catch (Exception e) {
            log.error("Error registering RouteAgent: {}", e.getMessage());
        }
    }

    /**
     * 处理EndAgent注册请求
     */
    private void handleEndAgentRegister(String senderId, Map<String, Object> params) {
        try {
            String agentName = (String) params.get("agentName");
            String agentType = (String) params.get("agentType");
            Map<String, Object> agentCapabilities = (Map<String, Object>) params.get("capabilities");
            String routeAgentId = (String) params.get("routeAgentId");
            
            EndAgentInfo endAgent = new EndAgentInfo(senderId, agentName, agentType, agentCapabilities, routeAgentId);
            endAgents.put(senderId, endAgent);
            
            log.info("EndAgent registered successfully: {} ({})");
            
        } catch (Exception e) {
            log.error("Error registering EndAgent: {}", e.getMessage());
        }
    }

    /**
     * 处理场景创建请求
     */
    private void handleSceneCreate(String senderId, Map<String, Object> params) {
        try {
            String sceneId = (String) params.get("sceneId");
            String sceneName = (String) params.get("sceneName");
            String description = (String) params.get("description");
            
            // 简化：创建基本场景定义
            SceneDefinition sceneDefinition = new SceneDefinition();
            sceneDefinition.setSceneId(sceneId);
            sceneDefinition.setName(sceneName);
            sceneDefinition.setDescription(description);
            
            // 保存场景
            sceneManager.createScene(sceneDefinition);
            
            log.info("Scene created successfully: {} by {}", sceneId, senderId);
            
        } catch (Exception e) {
            log.error("Error creating scene: {}", e.getMessage());
        }
    }

    /**
     * 南下协议：向RouteAgent发送命令
     */
    public CompletableFuture<SendResult> sendToRouteAgent(String routeAgentId, CommandPacket packet) {
        packet.setSenderId(mcpAgentId);
        packet.setReceiverId(routeAgentId);
        return udpSDK.sendCommand(packet);
    }

    /**
     * 南下协议：向EndAgent发送命令
     */
    public CompletableFuture<SendResult> sendToEndAgent(String endAgentId, CommandPacket packet) {
        // 查找EndAgent所属的RouteAgent
        EndAgentInfo endAgent = endAgents.get(endAgentId);
        if (endAgent == null) {
            log.error("EndAgent not found: {}", endAgentId);
            return CompletableFuture.completedFuture(new SendResult(false, "EndAgent not found"));
        }
        
        // 通过RouteAgent转发命令
        packet.setSenderId(mcpAgentId);
        packet.setReceiverId(endAgent.getRouteAgentId());
        // 添加EndAgent目标信息
        packet.getParams().put("targetEndAgentId", endAgentId);
        
        return udpSDK.sendCommand(packet);
    }

    /**
     * 获取RouteAgent列表
     */
    public List<RouteAgentInfo> getRouteAgents() {
        return new ArrayList<>(routeAgents.values());
    }

    /**
     * 获取EndAgent列表
     */
    public List<EndAgentInfo> getEndAgents() {
        return new ArrayList<>(endAgents.values());
    }

    /**
     * 获取场景列表
     */
    public List<SceneDefinition> getScenes() {
        // 注意：SceneManager可能没有getAllScenes方法，需要根据实际API调整
        return new ArrayList<>();
    }

    /**
     * 数据结构：RouteAgent信息
     */
    public static class RouteAgentInfo {
        private String agentId;
        private String agentName;
        private String agentType;
        private Map<String, Object> capabilities;
        private long lastHeartbeat;
        private String status;
        private String error;
        private Map<String, Object> metrics;

        public RouteAgentInfo(String agentId, String agentName, String agentType, Map<String, Object> capabilities) {
            this.agentId = agentId;
            this.agentName = agentName;
            this.agentType = agentType;
            this.capabilities = capabilities;
            this.lastHeartbeat = System.currentTimeMillis();
            this.status = "ONLINE";
            this.error = null;
            this.metrics = new HashMap<>();
        }

        public void updateLastHeartbeat() {
            this.lastHeartbeat = System.currentTimeMillis();
        }

        public void updateStatus(String status, String error, Map<String, Object> metrics) {
            this.status = status;
            this.error = error;
            if (metrics != null) {
                this.metrics.putAll(metrics);
            }
        }

        // Getters and setters
        public String getAgentId() { return agentId; }
        public String getAgentName() { return agentName; }
        public String getAgentType() { return agentType; }
        public Map<String, Object> getCapabilities() { return capabilities; }
        public long getLastHeartbeat() { return lastHeartbeat; }
        public String getStatus() { return status; }
        public String getError() { return error; }
        public Map<String, Object> getMetrics() { return metrics; }
    }

    /**
     * 数据结构：EndAgent信息
     */
    public static class EndAgentInfo {
        private String agentId;
        private String agentName;
        private String agentType;
        private Map<String, Object> capabilities;
        private String routeAgentId;
        private long lastHeartbeat;
        private String status;
        private String error;
        private Map<String, Object> metrics;

        public EndAgentInfo(String agentId, String agentName, String agentType, Map<String, Object> capabilities, String routeAgentId) {
            this.agentId = agentId;
            this.agentName = agentName;
            this.agentType = agentType;
            this.capabilities = capabilities;
            this.routeAgentId = routeAgentId;
            this.lastHeartbeat = System.currentTimeMillis();
            this.status = "ONLINE";
            this.error = null;
            this.metrics = new HashMap<>();
        }

        public void updateLastHeartbeat() {
            this.lastHeartbeat = System.currentTimeMillis();
        }

        public void updateStatus(String status, String error, Map<String, Object> metrics) {
            this.status = status;
            this.error = error;
            if (metrics != null) {
                this.metrics.putAll(metrics);
            }
        }

        // Getters and setters
        public String getAgentId() { return agentId; }
        public String getAgentName() { return agentName; }
        public String getAgentType() { return agentType; }
        public Map<String, Object> getCapabilities() { return capabilities; }
        public String getRouteAgentId() { return routeAgentId; }
        public long getLastHeartbeat() { return lastHeartbeat; }
        public String getStatus() { return status; }
        public String getError() { return error; }
        public Map<String, Object> getMetrics() { return metrics; }
    }
}
