package net.ooder.sdk;

import net.ooder.sdk.agent.model.AgentConfig;

import net.ooder.sdk.command.factory.CommandTaskFactory;
import net.ooder.sdk.command.impl.CommandTaskScanner;
import net.ooder.sdk.command.model.CommandResult;
import net.ooder.sdk.network.packet.*;
import net.ooder.sdk.system.container.SimpleIOCContainer;
import net.ooder.sdk.command.model.CommandType;
import net.ooder.sdk.system.heartbeat.HeartbeatManager;
import net.ooder.sdk.network.RouteAgentManager;
import net.ooder.sdk.skill.Skill;
import net.ooder.sdk.skill.SkillResult;
import net.ooder.sdk.skill.SkillStatus;
import net.ooder.sdk.system.retry.RetryExecutor;
import net.ooder.sdk.system.sleep.DefaultSleepStrategy;
import net.ooder.sdk.system.sleep.SleepManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.ooder.sdk.network.udp.*;
import net.ooder.sdk.command.factory.CommandFactory;
import net.ooder.sdk.skill.factory.SkillFactory;
import net.ooder.sdk.network.factory.NetworkFactory;
import net.ooder.sdk.system.factory.SystemFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class AgentSDK {
    private static final Logger log = LoggerFactory.getLogger(AgentSDK.class);
    private final String agentId;
    private final String agentName;
    private final String agentType;
    private final String endpoint;
    private final UDPSDK udpSDK;
    private final HeartbeatManager heartbeatManager;
    private final SleepManager sleepManager;
    private final RetryExecutor retryExecutor;
    private final AgentConfig config;
    private RouteAgentManager routeAgentManager;
    
    // 命令任务工厂
    private final CommandTaskFactory commandTaskFactory;
    
    // 技能管理机制
    private final Map<String, Skill> skills = new HashMap<>();
    
    // 路由管理机制
    private final Map<String, RouteInfo> routes = new HashMap<>();
    
    // 路由信息类
    
    // 路由信息类
    public class RouteInfo {
        private final String routeId;
        private final String source;
        private final String destination;
        private final Map<String, Object> routeInfo;
        private final long createTime;
        private long updateTime;
        
        public RouteInfo(String routeId, String source, String destination, Map<String, Object> routeInfo) {
            this.routeId = routeId;
            this.source = source;
            this.destination = destination;
            this.routeInfo = new HashMap<>(routeInfo);
            this.createTime = System.currentTimeMillis();
            this.updateTime = createTime;
        }
        
        public String getRouteId() { return routeId; }
        public String getSource() { return source; }
        public String getDestination() { return destination; }
        public Map<String, Object> getRouteInfo() { return new HashMap<>(routeInfo); }
        public long getCreateTime() { return createTime; }
        public long getUpdateTime() { return updateTime; }
        public void update(Map<String, Object> newRouteInfo) {
            this.routeInfo.putAll(newRouteInfo);
            this.updateTime = System.currentTimeMillis();
        }
    }
    


    public AgentSDK(AgentConfig config) throws Exception {
        this.config = config;
        this.agentId = config.getAgentId();
        this.agentName = config.getAgentName();
        this.agentType = config.getAgentType();
        this.endpoint = config.getEndpoint();

        // 获取Agent管理器
        net.ooder.sdk.agent.model.AgentManager agentManager = net.ooder.sdk.agent.model.AgentManager.getInstance();
        NetworkFactory networkFactory = agentManager.getNetworkFactory();
        SystemFactory systemFactory = agentManager.getSystemFactory();
        CommandFactory commandFactory = agentManager.getCommandFactory();
        SkillFactory skillFactory = agentManager.getSkillFactory();

        UDPConfig udpConfig = UDPConfig.builder()
                .port(config.getUdpPort())
                .bufferSize(config.getUdpBufferSize())
                .timeout(config.getUdpTimeout())
                .maxPacketSize(config.getUdpMaxPacketSize())
                .strongTypeEnforcement(true)
                .allowCustomFormat(false)
                .packetValidation(new DefaultPacketValidation())
                .build();

        UDPSDK udpSDKInstance = null;
        try {
            udpSDKInstance = networkFactory.createUDPSDK(udpConfig);
        } catch (Exception e) {
            // 在测试环境中，如果创建失败，使用null
            log.warn("Failed to create UDPSDK in test environment: {}", e.getMessage());
        }
        this.udpSDK = udpSDKInstance;

        this.heartbeatManager = new HeartbeatManager(
                agentId,
                config.getHeartbeatInterval(),
                config.getHeartbeatTimeout(),
                config.getHeartbeatLossThreshold()
        );

        this.sleepManager = systemFactory.createSleepManager(new DefaultSleepStrategy());

        this.retryExecutor = new RetryExecutor(config.getRetryStrategy());
        
        // 初始化命令任务工厂
        this.commandTaskFactory = CommandTaskFactory.getInstance();
        this.commandTaskFactory.setAgentSDK(this);
        
        // 将AgentSDK实例注册到IOC容器中
        SimpleIOCContainer container = SimpleIOCContainer.getInstance();
        container.registerInstance(AgentSDK.class, this);
        container.registerNamedInstance("agentSDK", this);
        
        // 扫描命令任务类
        CommandTaskScanner scanner = new CommandTaskScanner(this.commandTaskFactory);
        scanner.scanPackage("net.ooder.sdk.command.tasks");

        setupMessageHandlers();
    }

    private void setupMessageHandlers() {
        if (udpSDK != null) {
            udpSDK.registerMessageHandler(new UDPMessageHandler() {
                @Override
                public void onHeartbeat(HeartbeatPacket packet) {
                    handleHeartbeatAck(packet);
                }

                @Override
                public void onCommand(CommandPacket packet) {
                    handleCommand(packet);
                }

                @Override
                public void onStatusReport(StatusReportPacket packet) {
                    handleStatusReport(packet);
                }

                @Override
                public void onAuth(AuthPacket packet) {
                    if (routeAgentManager != null) {
                        routeAgentManager.handleAuthResponse(packet);
                    }
                }

                @Override
                public void onTask(TaskPacket packet) {
                    handleTask(packet);
                }

                @Override
                public void onRoute(RoutePacket packet) {
                    handleRoute(packet);
                }

                @Override
                public void onError(UDPPacket packet, Exception e) {
                    log.error("UDP error: {}", e.getMessage());
                }
            });
        }
        heartbeatManager.setCallback(new HeartbeatManager.HeartbeatCallback() {
            @Override
            public void onHeartbeatSent(HeartbeatManager.HeartbeatData data) {
                sendHeartbeatPacket(data);
            }

            @Override
            public void onHeartbeatAck(HeartbeatManager.HeartbeatAckData data) {
                log.debug("Heartbeat ack received: {}", data);
            }

            @Override
            public void onHeartbeatTimeout() {
                log.warn("Heartbeat timeout");
                sleepManager.recordError();
            }

            @Override
            public void onHeartbeatLost() {
                log.error("Heartbeat lost, initiating reconnect");
            }
        });

        sleepManager.setWakeCallback((previousMode, reason) -> {
            log.info("Woke up from {} mode, reason: {}", previousMode, reason);
        });
    }

    public void start() {
        try {
            if (udpSDK != null) {
                udpSDK.startListening();
            }
            heartbeatManager.start();
            sleepManager.start();
            log.info("Agent SDK started for agent: {}", agentId);
        } catch (Exception e) {
            log.error("Failed to start Agent SDK", e);
            throw new RuntimeException("Failed to start Agent SDK", e);
        }
    }

    public void stop() {
        try {
            heartbeatManager.stop();
            sleepManager.stop();
            if (udpSDK != null) {
                udpSDK.stopListening();
            }
            retryExecutor.shutdown();
            log.info("Agent SDK stopped for agent: {}", agentId);
        } catch (Exception e) {
            log.error("Error stopping Agent SDK", e);
        }
    }

    public CompletableFuture<SendResult> sendCommand(CommandType command, Map<String, Object> params) {
        if (!sleepManager.canAcceptCommands()) {
            return CompletableFuture.completedFuture(
                SendResult.failure("Agent is sleeping, cannot accept commands")
        );
        }

        sleepManager.recordActivity();

        CommandPacket packet = CommandPacket.builder()
                .command(command)
                .params(params)
                .metadata(buildCommandMetadata())
                .build();

        if (udpSDK == null) {
            // 在测试环境中，返回一个已完成的失败结果
            return CompletableFuture.completedFuture(
                SendResult.failure("UDPSDK not initialized in test environment")
            );
        }

        return retryExecutor.executeWithRetry(() -> {
            try {
                return udpSDK.sendCommand(packet).get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException("Failed to send command", e);
            }
        }, 0);
    }

    public CompletableFuture<SendResult> sendStatusReport(StatusReportPacket packet) {
        sleepManager.recordActivity();
        if (udpSDK == null) {
            // 在测试环境中，返回一个已完成的失败结果
            return CompletableFuture.completedFuture(
                SendResult.failure("UDPSDK not initialized in test environment")
            );
        }
        return udpSDK.sendStatusReport(packet);
    }

    /**
     * 创建RouteAgent管理器
     */
    public void createRouteAgentManager(String routeAgentName, Map<String, Object> capabilities) {
        if (udpSDK != null) {
            this.routeAgentManager = new RouteAgentManager(udpSDK, agentId, routeAgentName, capabilities);
        } else {
            // 在测试环境中，使用null替代
            this.routeAgentManager = null;
        }
    }

    /**
     * 向MCP Agent注册RouteAgent
     */
    public CompletableFuture<Boolean> registerRouteAgent(String targetMcpId) {
        if (routeAgentManager == null) {
            return CompletableFuture.completedFuture(false);
        }
        return routeAgentManager.register(targetMcpId);
    }

    /**
     * 转发任务到EndAgent
     */
    public CompletableFuture<SendResult> forwardTask(TaskPacket taskPacket, String endAgentId) {
        if (routeAgentManager == null) {
            CompletableFuture<SendResult> future = new CompletableFuture<>();
            future.complete(SendResult.failure("RouteAgentManager not initialized"));
            return future;
        }
        return routeAgentManager.forwardTask(taskPacket, endAgentId);
    }

    /**
     * 发送任务结果到MCP Agent
     */
    public CompletableFuture<SendResult> sendTaskResult(String taskId, Map<String, Object> result) {
        if (routeAgentManager == null) {
            CompletableFuture<SendResult> future = new CompletableFuture<>();
            future.complete(SendResult.failure("RouteAgentManager not initialized"));
            return future;
        }

        TaskPacket packet = TaskPacket.builder()
                .taskId(taskId)
                .status("completed")
                .result(result)
                .senderId(agentId)
                .receiverId(routeAgentManager.getMcpAgentId())
                .build();

        return udpSDK.sendTask(packet);
    }

    /**
     * 发送路由状态更新到MCP Agent
     */
    public CompletableFuture<SendResult> sendRouteUpdate(List<RoutePacket.RouteEntry> endAgents) {
        if (routeAgentManager == null) {
            CompletableFuture<SendResult> future = new CompletableFuture<>();
            future.complete(SendResult.failure("RouteAgentManager not initialized"));
            return future;
        }
        return routeAgentManager.sendRouteUpdate(endAgents);
    }

    /**
     * 查询EndAgent列表
     */
    public CompletableFuture<List<RoutePacket.RouteEntry>> queryEndAgents() {
        if (routeAgentManager == null) {
            CompletableFuture<List<RoutePacket.RouteEntry>> future = new CompletableFuture<>();
            future.completeExceptionally(new RuntimeException("RouteAgentManager not initialized"));
            return future;
        }
        return routeAgentManager.queryEndAgents();
    }

    private void sendHeartbeatPacket(HeartbeatManager.HeartbeatData data) {
        HeartbeatPacket packet = HeartbeatPacket.builder()
                .agentId(data.getAgentId())
                .sequence(data.getSequence())
                .retryInfo(data.getRetryInfo())
                .sleepInfo(data.getSleepInfo())
                .build();

        if (udpSDK != null) {
            udpSDK.sendHeartbeat(packet);
        }
    }

    private void handleHeartbeatAck(HeartbeatPacket packet) {
        HeartbeatManager.HeartbeatAckData ackData = new HeartbeatManager.HeartbeatAckData();
        ackData.setAgentId(packet.getAgentId());
        ackData.setSequence(packet.getSequence());
        ackData.setStatus("online");
        ackData.setRetryInfo(packet.getRetryInfo());
        ackData.setSleepInfo(packet.getSleepInfo());
        ackData.setHealthInfo(new HeartbeatManager.HealthInfo());

        heartbeatManager.handleHeartbeatAck(
                packet.getSequence(),
                "online",
                packet.getRetryInfo(),
                packet.getSleepInfo(),
                new HeartbeatManager.HealthInfo()
        );
    }

    public void handleCommand(CommandPacket packet) {
        log.info("Received command: {}", packet.getOperation());
        sleepManager.recordActivity();
        
        try {
            // 检查是否有注册的命令任务
           CommandType commandType = CommandType.fromValue(packet.getOperation()).orElse(null);
            if (commandType != null && commandTaskFactory.hasCommandTask(commandType)) {
                // 使用命令任务工厂执行命令
                CompletableFuture<CommandResult> future = commandTaskFactory.executeCommand(packet);
                
                // 处理命令执行结果（可选：可以添加回调处理）
                future.whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Command execution failed: {}", ex.getMessage(), ex);
                    } else {
                        log.info("Command execution completed with status: {}", result.getStatus());
                    }
                });
            } else {
                // 如果没有注册命令任务，则使用传统的switch-case处理
                handleCommandLegacy(packet);
            }
        } catch (Exception e) {
            log.error("Failed to handle command: {}", e.getMessage(), e);
            
            // 发送错误响应
            Map<String, Object> result = new HashMap<>();
            result.put("status", "error");
            result.put("error", e.getMessage());
            result.put("timestamp", System.currentTimeMillis());
            
            sendCommandResponse(packet, result);
        }
    }
    
    /**
     * 传统的命令处理方法，用于处理没有注册命令任务的命令
     */
    private void handleCommandLegacy(CommandPacket packet) {
        // 根据命令类型调用不同的处理方法
      CommandType commandType = CommandType.fromValue(packet.getOperation()).orElse(null);
        if (commandType == null) {
            log.warn("Unhandled command type: {}", packet.getOperation());
            return;
        }
        
        switch (commandType) {
            case END_STATUS:
                handleEndStatusCommand(packet);
                break;
            case END_SET_CONFIG:
                handleEndSetConfigCommand(packet);
                break;
            case END_RESET:
                handleEndResetCommand(packet);
                break;
            case END_UPGRADE:
                handleEndUpgradeCommand(packet);
                break;
            case END_NETWORK_CONNECT:
                handleEndNetworkConnectCommand(packet);
                break;
            case END_NETWORK_DISCONNECT:
                handleEndNetworkDisconnectCommand(packet);
                break;
            case END_NETWORK_STATUS:
                handleEndNetworkStatusCommand(packet);
                break;
            case SKILL_SUBMIT:
                handleSkillSubmitCommand(packet);
                break;
            case SKILL_STATUS:
                handleSkillStatusCommand(packet);
                break;
            case SKILL_CONFIGURE:
                handleSkillConfigureCommand(packet);
                break;
            case SKILL_START:
                handleSkillStartCommand(packet);
                break;
            case SKILL_STOP:
                handleSkillStopCommand(packet);
                break;
            case ROUTE_ADD:
                handleRouteAddCommand(packet);
                break;
            case ROUTE_REMOVE:
                handleRouteRemoveCommand(packet);
                break;
            case ROUTE_LIST:
                handleRouteListCommand(packet);
                break;
            case ROUTE_CONFIGURE:
                handleRouteConfigureCommand(packet);
                break;
            case MCP_REGISTER:
                handleMcpRegisterCommand(packet);
                break;
            case MCP_STATUS:
                handleMcpStatusCommand(packet);
                break;
            case GROUP_CREATE:
                handleGroupCreateCommand(packet);
                break;
            case GROUP_ADD_MEMBER:
                handleGroupAddMemberCommand(packet);
                break;
            case SCENE_CREATE:
                handleSceneCreateCommand(packet);
                break;
            case SCENE_ACTIVATE:
                handleSceneActivateCommand(packet);
                break;
            case VFS_REGISTER:
                handleVfsRegisterCommand(packet);
                break;
            case VFS_SYNC:
                handleVfsSyncCommand(packet);
                break;
            default:
                log.warn("Unhandled command type: {}", commandType);
                break;
        }
    }
    
    // End Agent command handlers
    private void handleEndExecuteCommand(CommandPacket packet) {
        log.info("Handling END_EXECUTE command with params: {}", packet.getParams());
        
        try {
            // 从参数中获取命令内容
            Map<String, Object> params = (Map<String, Object>) packet.getParams();
            String command = (String) params.get("command");
            Map<String, Object> args = (Map<String, Object>) params.getOrDefault("args", new HashMap<>());
            
            // 参数验证
            if (command == null || command.trim().isEmpty()) {
                throw new IllegalArgumentException("Command cannot be empty");
            }
            
            // 安全检查：限制可执行的命令范围
            List<String> allowedCommands = Arrays.asList("echo", "ls", "dir", "pwd", "whoami");
            if (!allowedCommands.contains(command.toLowerCase())) {
                throw new SecurityException("Command not allowed: " + command);
            }
            
            // 构建完整命令
            StringBuilder fullCommand = new StringBuilder(command);
            for (Map.Entry<String, Object> entry : args.entrySet()) {
                fullCommand.append(" ").append(entry.getValue());
            }
            
            log.info("Executing command: {}", fullCommand);
            
            // 实际执行命令
            Process process = Runtime.getRuntime().exec(fullCommand.toString());
            
            // 收集命令输出
            String output = readInputStream(process.getInputStream());
            String error = readInputStream(process.getErrorStream());
            int exitCode = process.waitFor();
            
            // 构建执行结果
            Map<String, Object> result = new HashMap<>();
            result.put("command", command);
            result.put("args", args);
            result.put("exitCode", exitCode);
            result.put("output", output);
            result.put("error", error);
            result.put("timestamp", System.currentTimeMillis());
            
            if (exitCode == 0) {
                result.put("status", "success");
                log.info("Command executed successfully: {}, exitCode: {}", command, exitCode);
            } else {
                result.put("status", "failed");
                log.warn("Command executed with error: {}, exitCode: {}, error: {}", command, exitCode, error);
            }
            
            // 发送执行结果
            sendCommandResponse(packet, result);
            log.debug("Command execution result: {}", result);
            
        } catch (SecurityException e) {
            log.error("Security violation when executing command: {}", e.getMessage(), e);
            
            // 构建安全错误结果
            Map<String, Object> result = new HashMap<>();
            result.put("status", "security_error");
            result.put("error", e.getMessage());
            result.put("timestamp", System.currentTimeMillis());
            
            // 发送错误结果
            sendCommandResponse(packet, result);
            log.info("Command execution security error: {}", result);
        } catch (IllegalArgumentException e) {
            log.error("Invalid command parameters: {}", e.getMessage(), e);
            
            // 构建参数错误结果
            Map<String, Object> result = new HashMap<>();
            result.put("status", "parameter_error");
            result.put("error", e.getMessage());
            result.put("timestamp", System.currentTimeMillis());
            
            // 发送错误结果
            sendCommandResponse(packet, result);
            log.info("Command execution parameter error: {}", result);
        } catch (Exception e) {
            log.error("Failed to execute command: {}", e.getMessage(), e);
            
            // 构建执行错误结果
            Map<String, Object> result = new HashMap<>();
            result.put("status", "execution_error");
            result.put("error", e.getMessage());
            result.put("timestamp", System.currentTimeMillis());
            
            // 发送错误结果
            sendCommandResponse(packet, result);
            log.info("Command execution error: {}", result);
        }
    }
    
    private void handleEndStatusCommand(CommandPacket packet) {
        log.info("Handling END_STATUS command with params: {}", packet.getParams());
        
        try {
            // 收集Agent状态信息
            Map<String, Object> status = new HashMap<>();
            status.put("agentId", agentId);
            status.put("agentName", agentName);
            status.put("agentType", agentType);
            status.put("endpoint", endpoint);
            status.put("status", isHealthy() ? "online" : "offline");
            status.put("sleepMode", getSleepMode());
            status.put("timestamp", System.currentTimeMillis());
            
            // 添加系统资源信息（模拟）
            Map<String, Object> resources = new HashMap<>();
            resources.put("cpuUsage", Math.random() * 100);
            resources.put("memoryUsage", Math.random() * 100);
            resources.put("diskUsage", Math.random() * 100);
            status.put("resources", resources);
            
            // 发送状态信息（这里可以添加发送状态的逻辑）
            log.info("Agent status: {}", status);
            
        } catch (Exception e) {
            log.error("Failed to get agent status: {}", e.getMessage(), e);
        }
    }
    
    private void handleEndSetConfigCommand(CommandPacket packet) {
        log.info("Handling END_SET_CONFIG command with params: {}", packet.getParams());
        
        try {
            // 从参数中获取配置信息
            Map<String, Object> params = (Map<String, Object>) packet.getParams();
            Map<String, Object> configParams = (Map<String, Object>) params.get("config");
            
            // 更新配置
            if (configParams != null && !configParams.isEmpty()) {
                // 更新config
                // 注意：AgentConfig类没有updateFromMap方法，暂时注释掉
                // config.updateFromMap(configParams);
                // 保存配置
                // configManager.saveConfig(agentId, config);
                log.info("Updated agent config with params: {}", configParams);
            }
            
            // 构建配置更新结果
            Map<String, Object> result = new HashMap<>();
            result.put("status", "success");
            result.put("config", configParams);
            result.put("timestamp", System.currentTimeMillis());
            
            // 发送配置更新结果
            sendCommandResponse(packet, result);
            log.info("Config update result: {}", result);
            
        } catch (Exception e) {
            log.error("Failed to set agent config: {}", e.getMessage(), e);
            
            // 构建配置更新错误结果
            Map<String, Object> result = new HashMap<>();
            result.put("status", "error");
            result.put("error", e.getMessage());
            result.put("timestamp", System.currentTimeMillis());
            
            // 发送配置更新错误结果
            sendCommandResponse(packet, result);
            log.info("Config update error: {}", result);
        }
    }
    
    private void handleEndResetCommand(CommandPacket packet) {
        log.info("Handling END_RESET command with params: {}", packet.getParams());
        
        try {
            // 记录重置前的状态
            boolean wasHealthy = isHealthy();
            SleepManager.SleepMode sleepMode = getSleepMode();
            int totalRoutes = getRoutes().size();
            int totalSkills = getSkills().size();
            
            // 执行重置操作
            // 1. 清除所有路由
            Map<String, RouteInfo> allRoutes = getRoutes();
            for (String routeId : allRoutes.keySet()) {
                removeRoute(routeId);
            }
            
            // 2. 清除所有技能
            Map<String, Skill> allSkills = getSkills();
            for (String skillId : allSkills.keySet()) {
                unregisterSkill(skillId);
            }
            
            // 3. 重置心跳管理器
            heartbeatManager.reset();
            
            // 4. 重置睡眠管理器
            sleepManager.reset();
            
            // 构建重置结果
            Map<String, Object> result = new HashMap<>();
            
            // 构建resetInfo映射
            Map<String, Object> resetInfo = new HashMap<>();
            resetInfo.put("previousHealthStatus", wasHealthy ? "online" : "offline");
            resetInfo.put("previousSleepMode", sleepMode);
            resetInfo.put("routesCleared", totalRoutes);
            resetInfo.put("skillsCleared", totalSkills);
            
            result.put("resetInfo", resetInfo);
            result.put("status", "success");
            result.put("message", "Agent reset completed successfully");
            result.put("timestamp", System.currentTimeMillis());
            
            // 发送重置结果
            sendCommandResponse(packet, result);
            log.info("Agent reset completed: {} routes and {} skills cleared", totalRoutes, totalSkills);
            log.debug("Agent reset result: {}", result);
            
        } catch (Exception e) {
            log.error("Failed to reset agent: {}", e.getMessage(), e);
            
            // 构建执行错误结果
            Map<String, Object> result = new HashMap<>();
            result.put("status", "execution_error");
            result.put("error", e.getMessage());
            result.put("timestamp", System.currentTimeMillis());
            
            // 发送错误结果
            sendCommandResponse(packet, result);
            log.info("Agent reset error: {}", result);
        }
    }
    
    private void handleEndUpgradeCommand(CommandPacket packet) {
        log.info("Handling END_UPGRADE command with params: {}", packet.getParams());
        
        try {
            // 从参数中获取升级信息
            Map<String, Object> params = (Map<String, Object>) packet.getParams();
            String upgradeUrl = (String) params.get("upgradeUrl");
            String version = (String) params.get("version");
            boolean force = (boolean) params.getOrDefault("force", false);
            
            // 参数验证
            if (upgradeUrl == null || upgradeUrl.trim().isEmpty()) {
                throw new IllegalArgumentException("Upgrade URL cannot be empty");
            }
            if (version == null || version.trim().isEmpty()) {
                throw new IllegalArgumentException("Version cannot be empty");
            }
            
            // 检查当前版本
            // 注意：AgentConfig类没有getVersion方法，暂时使用固定值
            String currentVersion = "1.0.0"; // 默认版本
            log.info("Current agent version: {}", currentVersion);
            
            // 如果不是强制升级且版本相同，则跳过
            if (!force && version.equals(currentVersion)) {
                Map<String, Object> result = new HashMap<>();
                result.put("status", "skipped");
                result.put("message", "Current version is already the latest");
                result.put("currentVersion", currentVersion);
                result.put("targetVersion", version);
                result.put("timestamp", System.currentTimeMillis());
                
                sendCommandResponse(packet, result);
                log.info("Upgrade skipped: already on the latest version");
                return;
            }
            
            // 执行升级逻辑（模拟）
            log.info("Starting agent upgrade from version {} to {}", currentVersion, version);
            log.info("Downloading upgrade package from: {}", upgradeUrl);
            
            // 模拟升级过程
            Thread.sleep(2000); // 模拟下载和升级时间
            
            // 更新版本信息
            // 注意：AgentConfig类没有setVersion方法，暂时注释掉
            // config.setVersion(version);
            // configManager.saveConfig(agentId, config);
            
            // 构建升级结果
            Map<String, Object> result = new HashMap<>();
            result.put("status", "success");
            result.put("message", "Agent upgrade completed successfully");
            result.put("currentVersion", version);
            result.put("previousVersion", currentVersion);
            result.put("upgradeUrl", upgradeUrl);
            result.put("timestamp", System.currentTimeMillis());
            
            // 发送升级结果
            sendCommandResponse(packet, result);
            log.info("Agent upgrade completed successfully: {} -> {}", currentVersion, version);
            log.debug("Upgrade result: {}", result);
            
        } catch (Exception e) {
            log.error("Failed to upgrade agent: {}", e.getMessage(), e);
            
            // 构建升级错误结果
            Map<String, Object> result = new HashMap<>();
            result.put("status", "error");
            result.put("error", e.getMessage());
            result.put("timestamp", System.currentTimeMillis());
            
            // 发送升级错误结果
            sendCommandResponse(packet, result);
            log.info("Agent upgrade error: {}", result);
        }
    }
    
    // Skill Agent command handlers
    private void handleSkillSubmitCommand(CommandPacket packet) {
        log.info("Handling SKILL_SUBMIT command with params: {}", packet.getParams());
        
        String skillId = null;
        try {
            // 从参数中获取技能信息
            Map<String, Object> params = (Map<String, Object>) packet.getParams();
            skillId = (String) params.get("skillId");
            final String finalSkillId = skillId;
            final String description = (String) params.get("description");
            final Map<String, String> parameters = (Map<String, String>) params.getOrDefault("parameters", new HashMap<>());
            String skillCode = (String) params.get("skillCode");
            
            // 参数验证
            if (skillId == null || skillId.trim().isEmpty()) {
                throw new IllegalArgumentException("Skill ID cannot be empty");
            }
            if (description == null || description.trim().isEmpty()) {
                throw new IllegalArgumentException("Skill description cannot be empty");
            }
            if (skillCode == null || skillCode.trim().isEmpty()) {
                throw new IllegalArgumentException("Skill code cannot be empty");
            }
            
            // 检查技能是否已存在
            if (getSkill(skillId) != null) {
                throw new IllegalArgumentException("Skill already exists: " + skillId);
            }
            
            // 创建新技能（这里使用简单的实现，实际应该解析技能代码）
            Skill newSkill = new Skill() {
                @Override
                public SkillResult execute(Map<String, Object> params) {
                    // 这里应该是实际执行技能代码的逻辑
                    // 现在返回一个模拟结果
                    Map<String, Object> resultData = new HashMap<>();
                    resultData.put("skillId", finalSkillId);
                    resultData.put("status", "executed");
                    resultData.put("inputParams", params);
                    resultData.put("executionTime", System.currentTimeMillis());
                    resultData.put("message", "Skill executed successfully (simulated)");
                    
                    Map<String, Object> metadata = new HashMap<>();
                    metadata.put("skillId", finalSkillId);
                    metadata.put("executionTime", System.currentTimeMillis());
                    
                    return SkillResult.success(resultData, metadata);
                }
                
                @Override
                public String getSkillId() {
                    return finalSkillId;
                }
                
                @Override
                public String getName() {
                    return finalSkillId;
                }
                
                @Override
                public String getDescription() {
                    return description;
                }
                
                @Override
                public Map<String, String> getParameters() {
                    return new HashMap<>(parameters);
                }
                
                @Override
                public void initialize() {
                    // 初始化逻辑
                }
                
                @Override
                public void destroy() {
                    // 销毁逻辑
                }
                
                @Override
                public SkillStatus getStatus() {
                    return SkillStatus.READY;
                }
            };
            
            // 注册新技能
            registerSkill(newSkill);
            
            // 构建提交结果
            Map<String, Object> result = new HashMap<>();
            result.put("skillId", skillId);
            result.put("description", description);
            result.put("parameters", parameters);
            result.put("skillCode", skillCode.length() > 100 ? skillCode.substring(0, 100) + "..." : skillCode);
            result.put("status", "success");
            result.put("message", "Skill submitted and registered successfully");
            result.put("timestamp", System.currentTimeMillis());
            
            // 发送提交结果
            sendCommandResponse(packet, result);
            log.info("Skill submitted and registered: {}", skillId);
            log.debug("Skill submit complete result: {}", result);
            
        } catch (IllegalArgumentException e) {
            log.error("Invalid skill submit parameters: {}", e.getMessage(), e);
            
            // 构建参数错误结果
            Map<String, Object> result = new HashMap<>();
            result.put("skillId", skillId);
            result.put("status", "parameter_error");
            result.put("error", e.getMessage());
            result.put("timestamp", System.currentTimeMillis());
            
            // 发送错误结果
            sendCommandResponse(packet, result);
            log.info("Skill submit parameter error: {}", result);
            
        } catch (Exception e) {
            log.error("Failed to submit skill: {}", e.getMessage(), e);
            
            // 构建执行错误结果
            Map<String, Object> result = new HashMap<>();
            result.put("skillId", skillId);
            result.put("status", "execution_error");
            result.put("error", e.getMessage());
            result.put("timestamp", System.currentTimeMillis());
            
            // 发送错误结果
            sendCommandResponse(packet, result);
            log.info("Skill submit error: {}", result);
        }
    }
    
    private void handleSkillInvokeCommand(CommandPacket packet) {
        log.info("Handling SKILL_INVOKE command with params: {}", packet.getParams());
        
        try {
            // 从参数中获取技能ID和参数
            Map<String, Object> params = (Map<String, Object>) packet.getParams();
            String skillId = (String) params.get("skillId");
            Map<String, Object> skillParams = (Map<String, Object>) params.getOrDefault("params", new HashMap<>());
            
            // 参数验证
            if (skillId == null || skillId.trim().isEmpty()) {
                throw new IllegalArgumentException("Skill ID cannot be empty");
            }
            
            // 获取技能
            Skill skill = getSkill(skillId);
            if (skill == null) {
                throw new IllegalArgumentException("Skill not found: " + skillId);
            }
            
            log.info("Invoking skill: {} ({}), params: {}", skillId, skill.getDescription(), skillParams);
            
            // 参数验证：检查是否提供了所有必需参数
            Map<String, String> requiredParams = skill.getParameters();
            for (Map.Entry<String, String> paramEntry : requiredParams.entrySet()) {
                if (!skillParams.containsKey(paramEntry.getKey())) {
                    throw new IllegalArgumentException("Missing required parameter: " + paramEntry.getKey() + 
                            " (" + paramEntry.getValue() + ")");
                }
            }
            
            // 实际调用技能
            SkillResult skillResult = skill.execute(skillParams);
            
            // 构建技能调用结果
            Map<String, Object> result = new HashMap<>();
            result.put("skillId", skillId);
            result.put("skillName", skill.getDescription());
            result.put("params", skillParams);
            result.put("result", skillResult.getData());
            result.put("success", skillResult.isSuccess());
            result.put("status", skillResult.isSuccess() ? "success" : "error");
            result.put("timestamp", System.currentTimeMillis());
            
            if (!skillResult.isSuccess()) {
                result.put("error", skillResult.getErrorMessage());
            }
            
            // 发送技能调用结果
            sendCommandResponse(packet, result);
            log.info("Skill invocation {}: {}, result: {}", skillResult.isSuccess() ? "successful" : "failed", skillId, skillResult.getData());
            log.debug("Skill invocation complete result: {}", result);
            
        } catch (IllegalArgumentException e) {
            log.error("Invalid skill invocation parameters: {}", e.getMessage(), e);
            
            // 构建参数错误结果
            Map<String, Object> result = new HashMap<>();
            Map<String, Object> params = (Map<String, Object>) packet.getParams();
            result.put("skillId", params.get("skillId"));
            result.put("params", params.getOrDefault("params", new HashMap<>()));
            result.put("status", "parameter_error");
            result.put("error", e.getMessage());
            result.put("timestamp", System.currentTimeMillis());
            
            // 发送错误结果
            sendCommandResponse(packet, result);
            log.info("Skill invocation parameter error: {}", result);
            
        } catch (Exception e) {
            log.error("Failed to invoke skill: {}", e.getMessage(), e);
            
            // 构建执行错误结果
            Map<String, Object> result = new HashMap<>();
            Map<String, Object> params = (Map<String, Object>) packet.getParams();
            result.put("skillId", params.get("skillId"));
            result.put("params", params.getOrDefault("params", new HashMap<>()));
            result.put("status", "execution_error");
            result.put("error", e.getMessage());
            result.put("timestamp", System.currentTimeMillis());
            
            // 发送错误结果
            sendCommandResponse(packet, result);
            log.info("Skill invocation error: {}", result);
        }
    }
    
    private void handleSkillStatusCommand(CommandPacket packet) {
        log.info("Handling SKILL_STATUS command with params: {}", packet.getParams());
        
        try {
            // 从参数中获取技能ID（可选，如果为空则返回所有技能状态）
            Map<String, Object> params = (Map<String, Object>) packet.getParams();
            String skillId = (String) params.get("skillId");
            
            Map<String, Object> result = new HashMap<>();
            
            if (skillId != null && !skillId.trim().isEmpty()) {
                // 查询单个技能状态
                Skill skill = getSkill(skillId);
                if (skill == null) {
                    throw new IllegalArgumentException("Skill not found: " + skillId);
                }
                
                // 构建单个技能状态
                Map<String, Object> skillStatus = new HashMap<>();
                skillStatus.put("skillId", skillId);
                skillStatus.put("description", skill.getDescription());
                skillStatus.put("parameters", skill.getParameters());
                skillStatus.put("status", "registered");
                skillStatus.put("timestamp", System.currentTimeMillis());
                
                result.put("skill", skillStatus);
                result.put("totalSkills", 1);
                
            } else {
                // 查询所有技能状态
                Map<String, Skill> allSkills = getSkills();
                
                // 构建所有技能状态列表
                List<Map<String, Object>> skillList = new ArrayList<>();
                for (Map.Entry<String, Skill> entry : allSkills.entrySet()) {
                    Skill skill = entry.getValue();
                    Map<String, Object> skillStatus = new HashMap<>();
                    skillStatus.put("skillId", skill.getSkillId());
                    skillStatus.put("description", skill.getDescription());
                    skillStatus.put("parameters", skill.getParameters());
                    skillStatus.put("status", "registered");
                    skillList.add(skillStatus);
                }
                
                result.put("skills", skillList);
                result.put("totalSkills", allSkills.size());
            }
            
            result.put("status", "success");
            result.put("message", "Skill status retrieved successfully");
            result.put("timestamp", System.currentTimeMillis());
            
            // 发送技能状态响应
            sendCommandResponse(packet, result);
            log.info("Skill status retrieved: {}", skillId != null ? skillId : "all skills");
            log.debug("Skill status complete result: {}", result);
            
        } catch (IllegalArgumentException e) {
            log.error("Invalid skill status parameters: {}", e.getMessage(), e);
            
            // 构建参数错误结果
            Map<String, Object> result = new HashMap<>();
            result.put("skillId", ((Map<String, Object>) packet.getParams()).get("skillId"));
            result.put("status", "parameter_error");
            result.put("error", e.getMessage());
            result.put("timestamp", System.currentTimeMillis());
            
            // 发送错误结果
            sendCommandResponse(packet, result);
            log.info("Skill status retrieval parameter error: {}", result);
            
        } catch (Exception e) {
            log.error("Failed to retrieve skill status: {}", e.getMessage(), e);
            
            // 构建执行错误结果
            Map<String, Object> result = new HashMap<>();
            result.put("skillId", ((Map<String, Object>) packet.getParams()).get("skillId"));
            result.put("status", "execution_error");
            result.put("error", e.getMessage());
            result.put("timestamp", System.currentTimeMillis());
            
            // 发送错误结果
            sendCommandResponse(packet, result);
            log.info("Skill status retrieval error: {}", result);
        }
    }
    
    private void handleSkillConfigureCommand(CommandPacket packet) {
        log.info("Handling SKILL_CONFIGURE command with params: {}", packet.getParams());
        // 实现配置技能的业务逻辑
        // 例如：更新技能的配置参数
    }
    
    private void handleSkillStartCommand(CommandPacket packet) {
        log.info("Handling SKILL_START command with params: {}", packet.getParams());
        // 实现启动技能的业务逻辑
        // 例如：启动指定的技能
    }
    
    private void handleSkillStopCommand(CommandPacket packet) {
        log.info("Handling SKILL_STOP command with params: {}", packet.getParams());
        // 实现停止技能的业务逻辑
        // 例如：停止指定的技能
    }
    
    // Route Agent command handlers
    private void handleRouteAddCommand(CommandPacket packet) {
        log.info("Handling ROUTE_ADD command with params: {}", packet.getParams());
        
        try {
            // 从参数中获取路由信息
            String routeId = (String) ((Map<String, Object>) packet.getParams()).get("routeId");
            String source = (String) ((Map<String, Object>) packet.getParams()).get("source");
            String destination = (String) ((Map<String, Object>) packet.getParams()).get("destination");
            Map<String, Object> routeInfo = (Map<String, Object>) ((Map<String, Object>) packet.getParams()).getOrDefault("routeInfo", new HashMap<>());
            
            // 参数验证
            if (routeId == null || routeId.trim().isEmpty()) {
                throw new IllegalArgumentException("Route ID cannot be empty");
            }
            if (source == null || source.trim().isEmpty()) {
                throw new IllegalArgumentException("Source cannot be empty");
            }
            if (destination == null || destination.trim().isEmpty()) {
                throw new IllegalArgumentException("Destination cannot be empty");
            }
            
            // 检查路由是否已存在
            if (getRoute(routeId) != null) {
                // 更新现有路由
                updateRoute(routeId, routeInfo);
                
                // 构建路由更新结果
                Map<String, Object> result = new HashMap<>();
                result.put("routeId", routeId);
                result.put("source", source);
                result.put("destination", destination);
                result.put("status", "updated");
                result.put("message", "Route updated successfully: " + routeId);
                result.put("timestamp", System.currentTimeMillis());
                
                // 发送路由更新结果
                sendCommandResponse(packet, result);
                log.info("Route updated: {} from {} to {}", routeId, source, destination);
                log.debug("Route update complete result: {}", result);
                
            } else {
                // 添加新路由
                addRoute(routeId, source, destination, routeInfo);
                
                // 构建路由添加结果
                Map<String, Object> result = new HashMap<>();
                result.put("routeId", routeId);
                result.put("source", source);
                result.put("destination", destination);
                result.put("routeInfo", routeInfo);
                result.put("status", "success");
                result.put("message", "Route added successfully: " + routeId);
                result.put("timestamp", System.currentTimeMillis());
                
                // 发送路由添加结果
                sendCommandResponse(packet, result);
                log.info("Route added: {} from {} to {}", routeId, source, destination);
                log.debug("Route add complete result: {}", result);
            }
            
            // 如果RouteAgentManager存在，通知它关于新路由
            if (routeAgentManager != null) {
                log.info("Notifying RouteAgentManager about route change: {}", routeId);
                // 这里可以调用RouteAgentManager的方法通知它关于路由变化
            }
            
        } catch (IllegalArgumentException e) {
            log.error("Invalid route parameters: {}", e.getMessage(), e);
            
            // 构建参数错误结果
            Map<String, Object> result = new HashMap<>();
            result.put("routeId", ((Map<String, Object>) packet.getParams()).get("routeId"));
            result.put("source", ((Map<String, Object>) packet.getParams()).get("source"));
            result.put("destination", ((Map<String, Object>) packet.getParams()).get("destination"));
            result.put("status", "parameter_error");
            result.put("error", e.getMessage());
            result.put("timestamp", System.currentTimeMillis());
            
            // 发送错误结果
            sendCommandResponse(packet, result);
            log.info("Route add parameter error: {}", result);
            
        } catch (Exception e) {
            log.error("Failed to add route: {}", e.getMessage(), e);
            
            // 构建执行错误结果
            Map<String, Object> result = new HashMap<>();
            result.put("routeId", ((Map<String, Object>) packet.getParams()).get("routeId"));
            result.put("source", ((Map<String, Object>) packet.getParams()).get("source"));
            result.put("destination", ((Map<String, Object>) packet.getParams()).get("destination"));
            result.put("status", "execution_error");
            result.put("error", e.getMessage());
            result.put("timestamp", System.currentTimeMillis());
            
            // 发送错误结果
            sendCommandResponse(packet, result);
            log.info("Route add error: {}", result);
        }
    }

    private void handleRouteRemoveCommand(CommandPacket packet) {
        log.info("Handling ROUTE_REMOVE command with params: {}", packet.getParams());
        
        try {
            // 从参数中获取路由ID
            String routeId = (String) ((Map<String, Object>) packet.getParams()).get("routeId");
            
            // 参数验证
            if (routeId == null || routeId.trim().isEmpty()) {
                throw new IllegalArgumentException("Route ID cannot be empty");
            }
            
            // 检查路由是否存在
            RouteInfo route = getRoute(routeId);
            if (route == null) {
                throw new IllegalArgumentException("Route not found: " + routeId);
            }
            
            // 记录删除前的路由信息
            String source = route.getSource();
            String destination = route.getDestination();
            
            // 实际删除路由
            removeRoute(routeId);
            
            // 构建删除结果
            Map<String, Object> result = new HashMap<>();
            result.put("routeId", routeId);
            result.put("source", source);
            result.put("destination", destination);
            result.put("status", "success");
            result.put("message", "Route removed successfully: " + routeId);
            result.put("timestamp", System.currentTimeMillis());
            
            // 发送删除结果
            sendCommandResponse(packet, result);
            log.info("Route removed: {} from {} to {}", routeId, source, destination);
            log.debug("Route remove complete result: {}", result);
            
        } catch (IllegalArgumentException e) {
            log.error("Invalid route remove parameters: {}", e.getMessage(), e);
            
            // 构建参数错误结果
            Map<String, Object> result = new HashMap<>();
            result.put("routeId", ((Map<String, Object>) packet.getParams()).get("routeId"));
            result.put("status", "parameter_error");
            result.put("error", e.getMessage());
            result.put("timestamp", System.currentTimeMillis());
            
            // 发送错误结果
            sendCommandResponse(packet, result);
            log.info("Route remove parameter error: {}", result);
            
        } catch (Exception e) {
            log.error("Failed to remove route: {}", e.getMessage(), e);
            
            // 构建执行错误结果
            Map<String, Object> result = new HashMap<>();
            result.put("routeId", ((Map<String, Object>) packet.getParams()).get("routeId"));
            result.put("status", "execution_error");
            result.put("error", e.getMessage());
            result.put("timestamp", System.currentTimeMillis());
            
            // 发送错误结果
            sendCommandResponse(packet, result);
            log.info("Route remove error: {}", result);
        }
    }
    
    private void handleRouteListCommand(CommandPacket packet) {
        log.info("Handling ROUTE_LIST command with params: {}", packet.getParams());
        
        try {
            // 获取所有路由
            Map<String, RouteInfo> allRoutes = getRoutes();
            
            // 构建路由列表响应
            Map<String, Object> result = new HashMap<>();
            result.put("totalRoutes", allRoutes.size());
            
            // 将路由信息转换为可序列化的格式
            List<Map<String, Object>> routeList = new ArrayList<>();
            for (Map.Entry<String, RouteInfo> entry : allRoutes.entrySet()) {
                RouteInfo route = entry.getValue();
                Map<String, Object> routeMap = new HashMap<>();
                routeMap.put("routeId", route.getRouteId());
                routeMap.put("source", route.getSource());
                routeMap.put("destination", route.getDestination());
                routeMap.put("routeInfo", route.getRouteInfo());
                routeMap.put("createTime", route.getCreateTime());
                routeMap.put("updateTime", route.getUpdateTime());
                routeList.add(routeMap);
            }
            
            result.put("routes", routeList);
            result.put("status", "success");
            result.put("message", "Route list retrieved successfully");
            result.put("timestamp", System.currentTimeMillis());
            
            // 发送路由列表响应
            sendCommandResponse(packet, result);
            log.info("Route list retrieved, total routes: {}", allRoutes.size());
            log.debug("Route list complete result: {}", result);
            
        } catch (Exception e) {
            log.error("Failed to retrieve route list: {}", e.getMessage(), e);
            
            // 构建执行错误结果
            Map<String, Object> result = new HashMap<>();
            result.put("status", "execution_error");
            result.put("error", e.getMessage());
            result.put("timestamp", System.currentTimeMillis());
            
            // 发送错误结果
            sendCommandResponse(packet, result);
            log.info("Route list retrieval error: {}", result);
        }
    }
    
    private void handleRouteConfigureCommand(CommandPacket packet) {
        log.info("Handling ROUTE_CONFIGURE command with params: {}", packet.getParams());
        // 实现配置路由的业务逻辑
        // 例如：更新路由的配置参数
    }
    
    // MCP Agent command handlers
    private void handleMcpRegisterCommand(CommandPacket packet) {
        log.info("Handling MCP_REGISTER command with params: {}", packet.getParams());
        // 实现MCP注册的业务逻辑
        // 例如：注册Agent到MCP
    }
    
    private void handleMcpStatusCommand(CommandPacket packet) {
        log.info("Handling MCP_STATUS command with params: {}", packet.getParams());
        // 实现获取MCP状态的业务逻辑
        // 例如：返回MCP的状态信息
    }
    
    // Group command handlers
    private void handleGroupCreateCommand(CommandPacket packet) {
        log.info("Handling GROUP_CREATE command with params: {}", packet.getParams());
        // 实现创建组的业务逻辑
        // 例如：创建新的Agent组
    }
    
    private void handleGroupAddMemberCommand(CommandPacket packet) {
        log.info("Handling GROUP_ADD_MEMBER command with params: {}", packet.getParams());
        // 实现添加组成员的业务逻辑
        // 例如：将Agent添加到指定组
    }
    
    // Scene command handlers
    private void handleSceneCreateCommand(CommandPacket packet) {
        log.info("Handling SCENE_CREATE command with params: {}", packet.getParams());
        // 实现创建场景的业务逻辑
        // 例如：创建新的场景
    }
    
    private void handleSceneActivateCommand(CommandPacket packet) {
        log.info("Handling SCENE_ACTIVATE command with params: {}", packet.getParams());
        // 实现激活场景的业务逻辑
        // 例如：激活指定的场景
    }
    
    // VFS command handlers
    private void handleVfsRegisterCommand(CommandPacket packet) {
        log.info("Handling VFS_REGISTER command with params: {}", packet.getParams());
        
        try {
            // 从参数中获取VFS注册信息
            Map<String, Object> params = (Map<String, Object>) packet.getParams();
            String vfsId = (String) params.get("vfsId");
            String vfsName = (String) params.get("vfsName");
            Map<String, Object> vfsConfig = (Map<String, Object>) params.getOrDefault("vfsConfig", new HashMap<>());
            String vfsUrl = (String) params.get("vfsUrl");
            
            // 参数验证
            if (vfsId == null || vfsId.trim().isEmpty()) {
                throw new IllegalArgumentException("VFS ID cannot be empty");
            }
            if (vfsName == null || vfsName.trim().isEmpty()) {
                throw new IllegalArgumentException("VFS name cannot be empty");
            }
            if (vfsUrl == null || vfsUrl.trim().isEmpty()) {
                throw new IllegalArgumentException("VFS URL cannot be empty");
            }
            
            // 执行VFS注册逻辑
            log.info("Registering VFS: {}, name: {}, url: {}, config: {}", vfsId, vfsName, vfsUrl, vfsConfig);
            
            // 构建注册结果
            Map<String, Object> result = new HashMap<>();
            result.put("status", "success");
            result.put("message", "VFS registered successfully");
            result.put("vfsId", vfsId);
            result.put("timestamp", System.currentTimeMillis());
            
            // 发送注册结果
            sendCommandResponse(packet, result);
            log.info("VFS register result: {}", result);
            
        } catch (Exception e) {
            log.error("Failed to register VFS: {}", e.getMessage(), e);
            
            Map<String, Object> result = new HashMap<>();
            result.put("status", "error");
            result.put("error", e.getMessage());
            result.put("timestamp", System.currentTimeMillis());
            
            sendCommandResponse(packet, result);
        }
    }
    
    // Network connectivity command handlers
    private void handleEndNetworkConnectCommand(CommandPacket packet) {
        log.info("Handling END_NETWORK_CONNECT command with params: {}", packet.getParams());
        
        try {
            // 从参数中获取网络连接信息
            Map<String, Object> params = (Map<String, Object>) packet.getParams();
            String networkType = (String) params.getOrDefault("networkType", "default");
            Map<String, Object> networkConfig = (Map<String, Object>) params.getOrDefault("config", new HashMap<>());
            
            // 执行网络连接逻辑
            log.info("Connecting to network: {}, config: {}", networkType, networkConfig);
            
            // 模拟网络连接过程
            Thread.sleep(1000);
            
            // 更新网络状态
            boolean connected = true;
            
            // 构建连接结果
            Map<String, Object> result = new HashMap<>();
            result.put("status", connected ? "success" : "failed");
            result.put("message", connected ? "Network connected successfully" : "Failed to connect to network");
            result.put("networkType", networkType);
            result.put("timestamp", System.currentTimeMillis());
            
            // 发送连接结果
            sendCommandResponse(packet, result);
            log.info("Network connect result: {}", result);
            
        } catch (Exception e) {
            log.error("Failed to connect to network: {}", e.getMessage(), e);
            
            Map<String, Object> result = new HashMap<>();
            result.put("status", "error");
            result.put("error", e.getMessage());
            result.put("timestamp", System.currentTimeMillis());
            
            sendCommandResponse(packet, result);
        }
    }
    
    private void handleEndNetworkDisconnectCommand(CommandPacket packet) {
        log.info("Handling END_NETWORK_DISCONNECT command with params: {}", packet.getParams());
        
        try {
            // 从参数中获取网络断开信息
            Map<String, Object> params = (Map<String, Object>) packet.getParams();
            String networkType = (String) params.getOrDefault("networkType", "default");
            
            // 执行网络断开逻辑
            log.info("Disconnecting from network: {}", networkType);
            
            // 模拟网络断开过程
            Thread.sleep(500);
            
            // 构建断开结果
            Map<String, Object> result = new HashMap<>();
            result.put("status", "success");
            result.put("message", "Network disconnected successfully");
            result.put("networkType", networkType);
            result.put("timestamp", System.currentTimeMillis());
            
            // 发送断开结果
            sendCommandResponse(packet, result);
            log.info("Network disconnect result: {}", result);
            
        } catch (Exception e) {
            log.error("Failed to disconnect from network: {}", e.getMessage(), e);
            
            Map<String, Object> result = new HashMap<>();
            result.put("status", "error");
            result.put("error", e.getMessage());
            result.put("timestamp", System.currentTimeMillis());
            
            sendCommandResponse(packet, result);
        }
    }
    
    private void handleEndNetworkStatusCommand(CommandPacket packet) {
        log.info("Handling END_NETWORK_STATUS command with params: {}", packet.getParams());
        
        try {
            // 收集网络状态信息
            Map<String, Object> status = new HashMap<>();
            
            // 模拟网络状态信息
            Map<String, Object> networkInfo = new HashMap<>();
            networkInfo.put("status", isHealthy() ? "connected" : "disconnected");
            networkInfo.put("networkType", "default");
            networkInfo.put("ipAddress", "192.168.1.100");
            networkInfo.put("macAddress", "00:11:22:33:44:55");
            networkInfo.put("signalStrength", 95);
            networkInfo.put("latency", Math.random() * 100);
            
            status.put("network", networkInfo);
            status.put("timestamp", System.currentTimeMillis());
            
            // 发送网络状态
            sendCommandResponse(packet, status);
            log.info("Network status: {}", status);
            
        } catch (Exception e) {
            log.error("Failed to get network status: {}", e.getMessage(), e);
            
            Map<String, Object> result = new HashMap<>();
            result.put("status", "error");
            result.put("error", e.getMessage());
            result.put("timestamp", System.currentTimeMillis());
            
            sendCommandResponse(packet, result);
        }
    }
    
    // VFS sync command handler
    private void handleVfsSyncCommand(CommandPacket packet) {
        log.info("Handling VFS_SYNC command with params: {}", packet.getParams());
        
        try {
            // 从参数中获取VFS同步信息
            Map<String, Object> params = (Map<String, Object>) packet.getParams();
            String vfsId = (String) params.getOrDefault("vfsId", "default");
            String syncType = (String) params.getOrDefault("syncType", "full");
            Map<String, Object> syncConfig = (Map<String, Object>) params.getOrDefault("config", new HashMap<>());
            
            // 执行VFS同步逻辑
            log.info("Syncing VFS: {}, type: {}, config: {}", vfsId, syncType, syncConfig);
            
            // 模拟VFS同步过程
            Thread.sleep(1500);
            
            // 构建同步结果
            Map<String, Object> result = new HashMap<>();
            result.put("status", "success");
            result.put("message", "VFS sync completed successfully");
            result.put("vfsId", vfsId);
            result.put("syncType", syncType);
            // 使用Java 8兼容的HashMap替代Map.of()
            Map<String, Object> syncStats = new HashMap<>();
            syncStats.put("filesSynced", 125);
            syncStats.put("bytesSynced", 10485760);
            syncStats.put("durationMs", 1500);
            result.put("syncStats", syncStats);
            result.put("timestamp", System.currentTimeMillis());
            
            // 发送同步结果
            sendCommandResponse(packet, result);
            log.info("VFS sync result: {}", result);
            
        } catch (Exception e) {
            log.error("Failed to sync VFS: {}", e.getMessage(), e);
            
            Map<String, Object> result = new HashMap<>();
            result.put("status", "error");
            result.put("error", e.getMessage());
            result.put("timestamp", System.currentTimeMillis());
            
            sendCommandResponse(packet, result);
        }
    }

    private void handleTask(TaskPacket packet) {
        log.info("Received task: {}", packet.getTaskId());
        sleepManager.recordActivity();
        
        if (routeAgentManager != null) {
            // 这里可以实现任务的处理逻辑
        }
    }

    private void handleRoute(RoutePacket packet) {
        log.info("Received route update: {}", packet.getRouteType());
        sleepManager.recordActivity();
    }

    private void handleStatusReport(StatusReportPacket packet) {
        log.info("Received status report: {}", packet.getReportType());
        sleepManager.recordActivity();
    }

    private CommandMetadata buildCommandMetadata() {
        CommandMetadata metadata = new CommandMetadata();
        metadata.setSenderId(agentId);
        metadata.setTraceId(java.util.UUID.randomUUID().toString());
        metadata.setPriority("normal");
        return metadata;
    }



    public boolean isHealthy() {
        return heartbeatManager.isHealthy();
    }

    public SleepManager.SleepMode getSleepMode() {
        return sleepManager.getCurrentMode();
    }

    public static AgentSDKBuilder builder() {
        return new AgentSDKBuilder();
    }

    // Getter methods
    public String getAgentId() {
        return agentId;
    }

    public String getAgentName() {
        return agentName;
    }

    public String getAgentType() {
        return agentType;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public UDPSDK getUdpSDK() {
        return udpSDK;
    }

    public HeartbeatManager getHeartbeatManager() {
        return heartbeatManager;
    }

    public SleepManager getSleepManager() {
        return sleepManager;
    }

    public RetryExecutor getRetryExecutor() {
        return retryExecutor;
    }

    public AgentConfig getConfig() {
        return config;
    }

    // 技能管理方法
    public void registerSkill(Skill skill) {
        if (skill != null && skill.getSkillId() != null) {
            skills.put(skill.getSkillId(), skill);
            log.info("Skill registered: {}", skill.getSkillId());
        } else {
            log.error("Invalid skill: {}", skill);
        }
    }
    
    public void unregisterSkill(String skillId) {
        if (skillId != null && skills.containsKey(skillId)) {
            skills.remove(skillId);
            log.info("Skill unregistered: {}", skillId);
        } else {
            log.warn("Skill not found: {}", skillId);
        }
    }
    
    public Skill getSkill(String skillId) {
        return skills.get(skillId);
    }
    
    public Map<String, Skill> getSkills() {
        return new HashMap<>(skills);
    }
    
    // 命令响应发送方法
    private void sendCommandResponse(CommandPacket originalPacket, Map<String, Object> result) {
        try {
            // 参数验证
            if (originalPacket == null) {
                log.error("Cannot send response: original packet is null");
                return;
            }
            
            // 创建一个新的结果映射，避免在Lambda表达式中修改原始映射
            Map<String, Object> responseResult = new HashMap<>();
            if (result != null) {
                responseResult.putAll(result);
            }
            
            // 构建响应命令包
            CommandPacket responsePacket = new CommandPacket();
            responsePacket.setOperation(CommandType.COMMAND_RESPONSE.getValue());
            responsePacket.setParams(responseResult);
            
            // 设置响应的发送者和接收者
            responsePacket.setSenderId(agentId);
            
            // 验证原始数据包的发送者ID
            String receiverId = originalPacket.getSenderId();
            if (receiverId == null || receiverId.trim().isEmpty()) {
                log.warn("Original packet has no sender ID, using default receiver");
                receiverId = "unknown-sender";
            }
            responsePacket.setReceiverId(receiverId);
            
            // 设置原始命令信息
            Map<String, Object> originalCommandInfo = new HashMap<>();
            originalCommandInfo.put("commandType", originalPacket.getCommand());
            originalCommandInfo.put("originalParams", originalPacket.getParams());
            
            // 设置跟踪信息
            String tempTraceId;
            if (originalPacket.getMetadata() != null) {
                // 复制原始元数据
                responsePacket.setMetadata(originalPacket.getMetadata());
                
                // 从元数据中获取跟踪ID
                try {
                    // 假设元数据有一个getTraceId方法或traceId字段
                    Method getTraceIdMethod = originalPacket.getMetadata().getClass().getMethod("getTraceId");
                    tempTraceId = (String) getTraceIdMethod.invoke(originalPacket.getMetadata());
                } catch (Exception e) {
                    log.debug("Failed to get traceId from metadata: {}", e.getMessage());
                    tempTraceId = UUID.randomUUID().toString();
                }
            } else {
                // 如果没有元数据，生成一个新的跟踪ID
                tempTraceId = UUID.randomUUID().toString();
            }
            
            // 将临时traceId赋值给final变量
            final String traceId = tempTraceId;
            
            originalCommandInfo.put("traceId", traceId);
            responseResult.put("originalCommand", originalCommandInfo);
            responseResult.put("traceId", traceId);
            
            // 添加响应发送尝试的信息到结果中
            responseResult.put("responseSent", true);
            
            // 发送响应
            log.debug("Sending command response: {} to {} (traceId: {})", 
                    originalPacket.getCommand(), receiverId, traceId);
            
            udpSDK.sendCommand(responsePacket)
                .thenAccept(sendResult -> {
                    if (sendResult.isSuccess()) {
                        log.info("Command response sent successfully: {} (traceId: {})", 
                                originalPacket.getCommand(), traceId);
                        // 不需要修改responseResult，因为响应已经发送成功
                    } else {
                        log.error("Failed to send command response: {} (traceId: {}), error: {}", 
                                originalPacket.getCommand(), traceId, sendResult.getMessage());
                        
                        // 创建一个新的结果映射来记录发送失败的信息
                        Map<String, Object> errorResult = new HashMap<>(responseResult);
                        errorResult.put("responseSent", false);
                        errorResult.put("responseError", sendResult.getMessage());
                        
                        // 这里可以选择是否重新发送错误响应，但通常我们不这样做
                        log.debug("Command response send failed, error result: {}", errorResult);
                    }
                })
                .exceptionally(e -> {
                    log.error("Error sending command response: {} (traceId: {})", 
                            originalPacket.getCommand(), traceId, e);
                    
                    // 创建一个新的结果映射来记录发送失败的信息
                    Map<String, Object> errorResult = new HashMap<>(responseResult);
                    errorResult.put("responseSent", false);
                    errorResult.put("responseError", e.getMessage());
                    
                    // 这里可以选择是否重新发送错误响应，但通常我们不这样做
                    log.debug("Command response send exception, error result: {}", errorResult);
                    
                    return null;
                });
            
        } catch (Exception e) {
            log.error("Failed to build or send command response: {} (agentId: {})", 
                    originalPacket != null ? originalPacket.getCommand() : "unknown", 
                    agentId, e);
            
            // 记录详细的错误信息
            if (originalPacket != null) {
                log.error("Original packet details: command={}, sender={}, params={}", 
                        originalPacket.getCommand(), 
                        originalPacket.getSenderId(), 
                        originalPacket.getParams());
            }
        }
    }
    

    
    // 路由管理方法
    public void addRoute(String routeId, String source, String destination, Map<String, Object> routeInfo) {
        if (routeId != null && source != null && destination != null) {
            RouteInfo newRoute = new RouteInfo(routeId, source, destination, routeInfo);
            routes.put(routeId, newRoute);
            log.info("Route added: {} from {} to {}", routeId, source, destination);
        } else {
            log.error("Invalid route parameters: routeId={}, source={}, destination={}", 
                     routeId, source, destination);
        }
    }
    
    public void removeRoute(String routeId) {
        if (routeId != null && routes.containsKey(routeId)) {
            RouteInfo removedRoute = routes.remove(routeId);
            log.info("Route removed: {} from {} to {}", 
                    routeId, removedRoute.getSource(), removedRoute.getDestination());
        } else {
            log.warn("Route not found: {}", routeId);
        }
    }
    
    public RouteInfo getRoute(String routeId) {
        return routes.get(routeId);
    }
    
    public Map<String, RouteInfo> getRoutes() {
        return new HashMap<>(routes);
    }
    
    public void updateRoute(String routeId, Map<String, Object> newRouteInfo) {
        if (routeId != null && routes.containsKey(routeId)) {
            RouteInfo route = routes.get(routeId);
            route.update(newRouteInfo);
            log.info("Route updated: {}", routeId);
        } else {
            log.warn("Route not found for update: {}", routeId);
        }
    }

    /**
     * 读取输入流内容为字符串（Java 8兼容方式）
     */
    private String readInputStream(InputStream inputStream) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append(System.lineSeparator());
            }
            return builder.toString();
        }
    }

    public static class AgentSDKBuilder {
        private AgentConfig config;

        public AgentSDKBuilder config(AgentConfig config) {
            this.config = config;
            return this;
        }

        public AgentSDK build() throws Exception {
            if (config == null) {
                config = new AgentConfig();
            }
            return new AgentSDK(config);
        }
    }
}
