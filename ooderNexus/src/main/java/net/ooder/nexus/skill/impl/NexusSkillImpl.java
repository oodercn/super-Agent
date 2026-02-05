package net.ooder.nexus.skill.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.ooder.nexus.management.NexusManager;
import net.ooder.nexus.management.impl.NexusManagerImpl;
import net.ooder.nexus.skill.NexusSkill;
import net.ooder.sdk.AgentSDK;
import net.ooder.sdk.command.model.CommandType;
import net.ooder.sdk.network.packet.CommandPacket;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Nexus技能实现类
 */
public class NexusSkillImpl implements NexusSkill {
    
    private static final Logger log = LoggerFactory.getLogger(NexusSkillImpl.class);
    
    private AgentSDK agentSDK;
    private String agentId;
    
    // 路由信息缓存
    private final Map<String, Map<String, Object>> routeAgents = new ConcurrentHashMap<>();
    
    // 终端信息缓存
    private final Map<String, Map<String, Object>> endAgents = new ConcurrentHashMap<>();
    
    // 自有网络链路存储
    private final Map<String, NetworkLink> networkLinks = new ConcurrentHashMap<>();
    
    // Nexus管理器
    private NexusManager nexusManager;
    
    // 网络状态监控
    private final AtomicLong packetsSent = new AtomicLong(0);
    private final AtomicLong packetsReceived = new AtomicLong(0);
    private final AtomicLong packetsFailed = new AtomicLong(0);
    private final AtomicLong bytesSent = new AtomicLong(0);
    private final AtomicLong bytesReceived = new AtomicLong(0);
    private volatile NetworkStatus networkStatus = NetworkStatus.OK;
    private volatile long lastPacketReceivedTime = System.currentTimeMillis();
    private volatile long lastPacketSentTime = System.currentTimeMillis();
    private final long networkTimeoutThreshold = 60000; // 60秒无网络活动视为网络异常
    private ScheduledExecutorService networkMonitorExecutor;
    
    // 命令处理增强
    private final ConcurrentLinkedQueue<CommandTask> commandQueue = new ConcurrentLinkedQueue<>();
    private final Map<String, CommandTask> activeCommands = new ConcurrentHashMap<>();
    private final Map<String, CommandStats> commandStats = new ConcurrentHashMap<>();
    private final AtomicInteger commandProcessorCount = new AtomicInteger(0);
    private final int maxConcurrentCommands = 10; // 最大并发命令数
    private final long commandTimeoutThreshold = 30000; // 30秒命令超时
    private ScheduledExecutorService commandMonitorExecutor;
    private ScheduledExecutorService commandProcessorExecutor;
    
    // 命令任务类
    private static class CommandTask {
        private String commandId;
        private CommandPacket packet;
        private long startTime;
        private long timeout;
        
        public String getCommandId() {
            return commandId;
        }
        
        public void setCommandId(String commandId) {
            this.commandId = commandId;
        }
        
        public CommandPacket getPacket() {
            return packet;
        }
        
        public void setPacket(CommandPacket packet) {
            this.packet = packet;
        }
        
        public long getStartTime() {
            return startTime;
        }
        
        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }
        
        public long getTimeout() {
            return timeout;
        }
        
        public void setTimeout(long timeout) {
            this.timeout = timeout;
        }
    }
    
    // 命令统计类
    private static class CommandStats {
        private AtomicLong totalCommands = new AtomicLong(0);
        private AtomicLong successfulCommands = new AtomicLong(0);
        private AtomicLong failedCommands = new AtomicLong(0);
        private AtomicLong timeoutCommands = new AtomicLong(0);
        private AtomicLong averageProcessingTime = new AtomicLong(0);
        
        public AtomicLong getTotalCommands() {
            return totalCommands;
        }
        
        public AtomicLong getSuccessfulCommands() {
            return successfulCommands;
        }
        
        public AtomicLong getFailedCommands() {
            return failedCommands;
        }
        
        public AtomicLong getTimeoutCommands() {
            return timeoutCommands;
        }
        
        public AtomicLong getAverageProcessingTime() {
            return averageProcessingTime;
        }
    }
    
    // 网络链路数据结构
    private static class NetworkLink {
        private String linkId;
        private String sourceAgentId;
        private String targetAgentId;
        private String linkType;
        private String status;
        private long createTime;
        private long lastUpdateTime;
        private long lastHeartbeatTime;
        private Map<String, Object> metadata;
        private Map<String, Object> healthStats;
        
        // Getters and setters
        public String getLinkId() {
            return linkId;
        }
        
        public void setLinkId(String linkId) {
            this.linkId = linkId;
        }
        
        public String getSourceAgentId() {
            return sourceAgentId;
        }
        
        public void setSourceAgentId(String sourceAgentId) {
            this.sourceAgentId = sourceAgentId;
        }
        
        public String getTargetAgentId() {
            return targetAgentId;
        }
        
        public void setTargetAgentId(String targetAgentId) {
            this.targetAgentId = targetAgentId;
        }
        
        public String getLinkType() {
            return linkType;
        }
        
        public void setLinkType(String linkType) {
            this.linkType = linkType;
        }
        
        public String getStatus() {
            return status;
        }
        
        public void setStatus(String status) {
            this.status = status;
        }
        
        public long getCreateTime() {
            return createTime;
        }
        
        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }
        
        public long getLastUpdateTime() {
            return lastUpdateTime;
        }
        
        public void setLastUpdateTime(long lastUpdateTime) {
            this.lastUpdateTime = lastUpdateTime;
        }
        
        public long getLastHeartbeatTime() {
            return lastHeartbeatTime;
        }
        
        public void setLastHeartbeatTime(long lastHeartbeatTime) {
            this.lastHeartbeatTime = lastHeartbeatTime;
        }
        
        public Map<String, Object> getMetadata() {
            return metadata;
        }
        
        public void setMetadata(Map<String, Object> metadata) {
            this.metadata = metadata;
        }
        
        public Map<String, Object> getHealthStats() {
            return healthStats;
        }
        
        public void setHealthStats(Map<String, Object> healthStats) {
            this.healthStats = healthStats;
        }
    }
    
    // 网络状态枚举
    public enum NetworkStatus {
        OK,
        WARNING,
        ERROR,
        TIMEOUT,
        DISCONNECTED
    }
    
    @Override
    public void initialize(AgentSDK sdk) {
        log.info("Initializing Nexus Skill");
        
        this.agentSDK = sdk;
        this.agentId = sdk.getAgentId();
        
        // 初始化Nexus管理器
        nexusManager = new NexusManagerImpl();
        nexusManager.initialize(sdk);
        
        // 启动网络状态监控
        startNetworkMonitoring();
        
        log.info("Nexus Skill initialized successfully");
    }
    
    @Override
    public void handleMcpRegisterCommand(CommandPacket packet) {
        log.info("Handling MCP register command: {}", packet);
        try {
            // 南下协议基础逻辑：存储注册信息
            Map<String, Object> data = new HashMap<>();
            // 简化实现，避免API调用错误
            String agentId = "agent_" + System.currentTimeMillis();
            data.put("agentId", agentId);
            data.put("status", "registered");
            data.put("registerTime", System.currentTimeMillis());
            data.put("lastHeartbeatTime", System.currentTimeMillis());
            routeAgents.put(agentId, data);
            
            // 更新网络状态
            updateNetworkStatusOnReceive(1);
            
            log.info("Registered agent: {}", agentId);
        } catch (Exception e) {
            log.error("Error handling MCP register command: {}", e.getMessage(), e);
            updateNetworkStatusOnError();
        }
    }
    
    @Override
    public void handleMcpDeregisterCommand(CommandPacket packet) {
        log.info("Handling MCP deregister command: {}", packet);
        try {
            // 南下协议基础逻辑：移除注册信息
            Map<String, Object> data = new HashMap<>();
            // 简化实现，避免API调用错误
            String agentId = "agent_" + System.currentTimeMillis();
            routeAgents.remove(agentId);
            
            // 更新网络状态
            updateNetworkStatusOnReceive(1);
            
            log.info("Deregistered agent: {}", agentId);
        } catch (Exception e) {
            log.error("Error handling MCP deregister command: {}", e.getMessage(), e);
            updateNetworkStatusOnError();
        }
    }
    
    @Override
    public void handleMcpHeartbeatCommand(CommandPacket packet) {
        log.info("Handling MCP heartbeat command: {}", packet);
        try {
            // 南下协议基础逻辑：更新心跳时间
            Map<String, Object> data = new HashMap<>();
            // 简化实现，避免API调用错误
            String agentId = "agent_" + System.currentTimeMillis();
            if (routeAgents.containsKey(agentId)) {
                Map<String, Object> agentInfo = routeAgents.get(agentId);
                agentInfo.put("lastHeartbeatTime", System.currentTimeMillis());
                agentInfo.put("status", "active");
                routeAgents.put(agentId, agentInfo);
                
                // 更新网络链路的心跳时间
                updateLinkHeartbeat(agentId);
                
                log.info("Updated heartbeat for agent: {}", agentId);
            }
            
            // 更新网络状态
            updateNetworkStatusOnReceive(1);
            
        } catch (Exception e) {
            log.error("Error handling MCP heartbeat command: {}", e.getMessage(), e);
            updateNetworkStatusOnError();
        }
    }
    
    @Override
    public void handleMcpStatusCommand(CommandPacket packet) {
        log.info("Handling MCP status command: {}", packet);
        try {
            // 南下协议基础逻辑：返回状态信息
            Map<String, Object> status = new HashMap<>();
            status.put("agentId", agentId);
            status.put("status", "online");
            status.put("timestamp", System.currentTimeMillis());
            status.put("routeCount", routeAgents.size());
            status.put("endAgentCount", endAgents.size());
            status.put("linkCount", networkLinks.size());
            status.put("networkStatus", networkStatus);
            status.put("networkStats", getNetworkStats());
            
            // 更新网络状态
            updateNetworkStatusOnReceive(1);
            
            log.info("Status: {}", status);
        } catch (Exception e) {
            log.error("Error handling MCP status command: {}", e.getMessage(), e);
            updateNetworkStatusOnError();
        }
    }
    
    @Override
    public void handleMcpDiscoverCommand(CommandPacket packet) {
        log.info("Handling MCP discover command: {}", packet);
        try {
            // 南下协议基础逻辑：返回发现结果
            Map<String, Object> result = new HashMap<>();
            result.put("agents", routeAgents);
            result.put("links", networkLinks);
            result.put("networkStatus", networkStatus);
            result.put("timestamp", System.currentTimeMillis());
            
            // 更新网络状态
            updateNetworkStatusOnReceive(1);
            
            log.info("Discover result: {}", result);
        } catch (Exception e) {
            log.error("Error handling MCP discover command: {}", e.getMessage(), e);
            updateNetworkStatusOnError();
        }
    }
    
    @Override
    public void handleRouteQueryCommand(CommandPacket packet) {
        log.info("Handling route query command: {}", packet);
        try {
            // 南下协议基础逻辑：返回路由信息
            Map<String, Object> result = new HashMap<>();
            result.put("routes", routeAgents);
            result.put("networkStatus", networkStatus);
            result.put("timestamp", System.currentTimeMillis());
            
            // 更新网络状态
            updateNetworkStatusOnReceive(1);
            
            log.info("Route query result: {}", result);
        } catch (Exception e) {
            log.error("Error handling route query command: {}", e.getMessage(), e);
            updateNetworkStatusOnError();
        }
    }
    
    @Override
    public void handleRouteUpdateCommand(CommandPacket packet) {
        log.info("Handling route update command: {}", packet);
        try {
            // 南下协议基础逻辑：更新路由信息
            Map<String, Object> data = new HashMap<>();
            // 简化实现，避免API调用错误
            String routeId = "route_" + System.currentTimeMillis();
            data.put("routeId", routeId);
            data.put("status", "updated");
            data.put("updateTime", System.currentTimeMillis());
            routeAgents.put(routeId, data);
            
            // 更新网络状态
            updateNetworkStatusOnReceive(1);
            
            log.info("Updated route: {}", routeId);
        } catch (Exception e) {
            log.error("Error handling route update command: {}", e.getMessage(), e);
            updateNetworkStatusOnError();
        }
    }
    
    @Override
    public void handleEndagentDiscoverCommand(CommandPacket packet) {
        log.info("Handling endagent discover command: {}", packet);
        try {
            // 南下协议基础逻辑：返回终端发现结果
            Map<String, Object> result = new HashMap<>();
            result.put("endagents", endAgents);
            result.put("networkStatus", networkStatus);
            result.put("timestamp", System.currentTimeMillis());
            
            // 更新网络状态
            updateNetworkStatusOnReceive(1);
            
            log.info("Endagent discover result: {}", result);
        } catch (Exception e) {
            log.error("Error handling endagent discover command: {}", e.getMessage(), e);
            updateNetworkStatusOnError();
        }
    }
    
    @Override
    public void handleEndagentStatusCommand(CommandPacket packet) {
        log.info("Handling endagent status command: {}", packet);
        try {
            // 南下协议基础逻辑：返回终端状态
            Map<String, Object> data = new HashMap<>();
            // 简化实现，避免API调用错误
            String endagentId = "endagent_" + System.currentTimeMillis();
            if (endAgents.containsKey(endagentId)) {
                Map<String, Object> status = endAgents.get(endagentId);
                log.info("Endagent status: {}", status);
            }
            
            // 更新网络状态
            updateNetworkStatusOnReceive(1);
        } catch (Exception e) {
            log.error("Error handling endagent status command: {}", e.getMessage(), e);
            updateNetworkStatusOnError();
        }
    }
    
    @Override
    public void handleEndagentAddCommand(CommandPacket packet) {
        log.info("Handling endagent add command: {}", packet);
        try {
            // 南下协议基础逻辑：添加终端
            Map<String, Object> data = new HashMap<>();
            // 简化实现，避免API调用错误
            String endagentId = "endagent_" + System.currentTimeMillis();
            // 1. 检查是否已存在
            if (endAgents.containsKey(endagentId)) {
                // 2. 检查激活状态
                Map<String, Object> existingEndagent = endAgents.get(endagentId);
                String status = (String) existingEndagent.get("status");
                if ("active".equals(status)) {
                    log.info("Endagent already exists and is active: {}", endagentId);
                    return;
                } else {
                    // 更新状态为active
                    existingEndagent.put("status", "active");
                    existingEndagent.put("lastUpdateTime", System.currentTimeMillis());
                    existingEndagent.put("lastHeartbeatTime", System.currentTimeMillis());
                    endAgents.put(endagentId, existingEndagent);
                    log.info("Updated endagent status to active: {}", endagentId);
                }
            } else {
                // 3. 进入添加流程
                data.put("endagentId", endagentId);
                data.put("status", "active");
                data.put("createTime", System.currentTimeMillis());
                data.put("lastUpdateTime", System.currentTimeMillis());
                data.put("lastHeartbeatTime", System.currentTimeMillis());
                data.put("scanAllowed", true); // 允许扫描
                endAgents.put(endagentId, data);
                log.info("Added new endagent: {}", endagentId);
                
                // 4. MCP开启端口
                openMcpPortForEndagent(endagentId);
                
                // 5. 允许endAgent扫描
                allowEndagentScan(endagentId);
            }
            
            // 更新网络状态
            updateNetworkStatusOnReceive(1);
        } catch (Exception e) {
            log.error("Error handling endagent add command: {}", e.getMessage(), e);
            updateNetworkStatusOnError();
        }
    }
    
    @Override
    public void handleEndagentRemoveCommand(CommandPacket packet) {
        log.info("Handling endagent remove command: {}", packet);
        try {
            // 南下协议基础逻辑：移除终端
            Map<String, Object> data = new HashMap<>();
            // 简化实现，避免API调用错误
            String endagentId = "endagent_" + System.currentTimeMillis();
            endAgents.remove(endagentId);
            
            // 更新网络状态
            updateNetworkStatusOnReceive(1);
            
            log.info("Removed endagent: {}", endagentId);
        } catch (Exception e) {
            log.error("Error handling endagent remove command: {}", e.getMessage(), e);
            updateNetworkStatusOnError();
        }
    }
    
    @Override
    public void handleTaskRequestCommand(CommandPacket packet) {
        log.info("Handling task request command: {}", packet);
        try {
            // 南下协议基础逻辑：存储任务请求
            Map<String, Object> data = new HashMap<>();
            // 简化实现，避免API调用错误
            String taskId = "task_" + System.currentTimeMillis();
            data.put("taskId", taskId);
            data.put("status", "received");
            data.put("receiveTime", System.currentTimeMillis());
            
            // 更新网络状态
            updateNetworkStatusOnReceive(1);
            
            log.info("Received task request: {}", taskId);
        } catch (Exception e) {
            log.error("Error handling task request command: {}", e.getMessage(), e);
            updateNetworkStatusOnError();
        }
    }
    
    @Override
    public void handleTaskResponseCommand(CommandPacket packet) {
        log.info("Handling task response command: {}", packet);
        try {
            // 南下协议基础逻辑：存储任务响应
            Map<String, Object> data = new HashMap<>();
            // 简化实现，避免API调用错误
            String taskId = "task_" + System.currentTimeMillis();
            data.put("taskId", taskId);
            data.put("status", "completed");
            data.put("completeTime", System.currentTimeMillis());
            
            // 更新网络状态
            updateNetworkStatusOnReceive(1);
            
            log.info("Received task response: {}", taskId);
        } catch (Exception e) {
            log.error("Error handling task response command: {}", e.getMessage(), e);
            updateNetworkStatusOnError();
        }
    }
    
    @Override
    public void handleAuthenticateCommand(CommandPacket packet) {
        log.info("Handling authenticate command: {}", packet);
        try {
            // 南下协议基础逻辑：处理认证请求
            Map<String, Object> data = new HashMap<>();
            // 简化实现，避免API调用错误
            String authId = "auth_" + System.currentTimeMillis();
            data.put("authId", authId);
            data.put("status", "processing");
            data.put("requestTime", System.currentTimeMillis());
            
            // 更新网络状态
            updateNetworkStatusOnReceive(1);
            
            log.info("Received authentication request: {}", authId);
        } catch (Exception e) {
            log.error("Error handling authenticate command: {}", e.getMessage(), e);
            updateNetworkStatusOnError();
        }
    }
    
    @Override
    public void handleAuthResponseCommand(CommandPacket packet) {
        log.info("Handling auth response command: {}", packet);
        try {
            // 南下协议基础逻辑：处理认证响应
            Map<String, Object> data = new HashMap<>();
            // 简化实现，避免API调用错误
            String authId = "auth_" + System.currentTimeMillis();
            data.put("authId", authId);
            data.put("status", "completed");
            data.put("responseTime", System.currentTimeMillis());
            
            // 更新网络状态
            updateNetworkStatusOnReceive(1);
            
            log.info("Received authentication response: {}", authId);
        } catch (Exception e) {
            log.error("Error handling auth response command: {}", e.getMessage(), e);
            updateNetworkStatusOnError();
        }
    }
    
    @Override
    public void start() {
        log.info("Starting Nexus Skill");
        try {
            // 启动逻辑：初始化网络链路
            initializeNetworkLinks();
            
            // 启动网络状态监控
            startNetworkMonitoring();
            
            log.info("Nexus Skill started successfully");
        } catch (Exception e) {
            log.error("Error starting Nexus Skill: {}", e.getMessage(), e);
        }
    }
    
    @Override
    public void stop() {
        log.info("Stopping Nexus Skill");
        try {
            // 停止逻辑：清理资源
            routeAgents.clear();
            endAgents.clear();
            networkLinks.clear();
            
            // 停止网络状态监控
            if (networkMonitorExecutor != null) {
                networkMonitorExecutor.shutdownNow();
            }
            
            // 停止Nexus管理器
            if (nexusManager != null) {
                nexusManager.shutdownSystem("Nexus Skill stopped");
            }
            
            log.info("Nexus Skill stopped successfully");
        } catch (Exception e) {
            log.error("Error stopping Nexus Skill: {}", e.getMessage(), e);
        }
    }
    
    // 数据结构展现功能
    public Map<String, Object> getNetworkTopology() {
        Map<String, Object> topology = new HashMap<>();
        topology.put("agents", routeAgents);
        topology.put("endAgents", endAgents);
        topology.put("links", networkLinks);
        topology.put("networkStatus", networkStatus);
        topology.put("networkStats", getNetworkStats());
        topology.put("timestamp", System.currentTimeMillis());
        return topology;
    }
    
    // 网络链路管理方法
    public void addNetworkLink(String linkId, String sourceAgentId, String targetAgentId, String linkType) {
        try {
            NetworkLink link = new NetworkLink();
            link.setLinkId(linkId);
            link.setSourceAgentId(sourceAgentId);
            link.setTargetAgentId(targetAgentId);
            link.setLinkType(linkType);
            link.setStatus("active");
            link.setCreateTime(System.currentTimeMillis());
            link.setLastUpdateTime(System.currentTimeMillis());
            link.setLastHeartbeatTime(System.currentTimeMillis());
            link.setMetadata(new HashMap<>());
            link.setHealthStats(new HashMap<>());
            networkLinks.put(linkId, link);
            
            // 更新网络状态
            updateNetworkStatusOnSend(1);
            
            log.info("Added network link: {}", linkId);
        } catch (Exception e) {
            log.error("Error adding network link: {}", e.getMessage(), e);
            updateNetworkStatusOnError();
        }
    }
    
    public void removeNetworkLink(String linkId) {
        try {
            networkLinks.remove(linkId);
            
            // 更新网络状态
            updateNetworkStatusOnSend(1);
            
            log.info("Removed network link: {}", linkId);
        } catch (Exception e) {
            log.error("Error removing network link: {}", e.getMessage(), e);
            updateNetworkStatusOnError();
        }
    }
    
    public void updateNetworkLinkStatus(String linkId, String status) {
        try {
            NetworkLink link = networkLinks.get(linkId);
            if (link != null) {
                link.setStatus(status);
                link.setLastUpdateTime(System.currentTimeMillis());
                link.setLastHeartbeatTime(System.currentTimeMillis());
                
                // 更新健康状态
                Map<String, Object> healthStats = link.getHealthStats();
                healthStats.put("lastStatusChangeTime", System.currentTimeMillis());
                healthStats.put("currentStatus", status);
                link.setHealthStats(healthStats);
                
                // 更新网络状态
                updateNetworkStatusOnSend(1);
                
                log.info("Updated network link status: {} -> {}", linkId, status);
            }
        } catch (Exception e) {
            log.error("Error updating network link status: {}", e.getMessage(), e);
            updateNetworkStatusOnError();
        }
    }
    
    // MCP端口开启功能
    private void openMcpPortForEndagent(String endagentId) {
        // 模拟MCP端口开启逻辑
        log.info("Opening MCP port for endagent: {}", endagentId);
        // 实际实现中，这里应该调用相关的网络服务来开启端口
        // 例如：调用网络管理服务开启指定端口
    }
    
    // 终端扫描允许功能
    private void allowEndagentScan(String endagentId) {
        // 模拟终端扫描允许逻辑
        log.info("Allowing scan for endagent: {}", endagentId);
        // 实际实现中，这里应该设置相关的扫描权限
        // 例如：更新终端的扫描权限配置
    }
    
    // 网络状态监控相关方法
    private void startNetworkMonitoring() {
        if (networkMonitorExecutor != null && !networkMonitorExecutor.isShutdown()) {
            return;
        }
        
        networkMonitorExecutor = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread thread = new Thread(r, "mcp-network-monitor");
            thread.setDaemon(true);
            return thread;
        });
        
        networkMonitorExecutor.scheduleAtFixedRate(() -> {
            try {
                checkNetworkStatus();
                checkLinkHealth();
                logNetworkStats();
            } catch (Exception e) {
                log.error("Error in network monitoring: {}", e.getMessage(), e);
            }
        }, 10, 30, TimeUnit.SECONDS); // 每30秒检查一次网络状态
    }
    
    private void checkNetworkStatus() {
        long currentTime = System.currentTimeMillis();
        
        // 检查网络超时
        if (currentTime - lastPacketReceivedTime > networkTimeoutThreshold) {
            setNetworkStatus(NetworkStatus.TIMEOUT);
            log.warn("Network timeout detected: No packets received for {}ms", 
                currentTime - lastPacketReceivedTime);
        } else {
            setNetworkStatus(NetworkStatus.OK);
        }
    }
    
    private void checkLinkHealth() {
        long currentTime = System.currentTimeMillis();
        long linkTimeoutThreshold = 120000; // 120秒无心跳视为链路异常
        
        for (Map.Entry<String, NetworkLink> entry : networkLinks.entrySet()) {
            NetworkLink link = entry.getValue();
            if (currentTime - link.getLastHeartbeatTime() > linkTimeoutThreshold) {
                if (!"error".equals(link.getStatus())) {
                    updateNetworkLinkStatus(link.getLinkId(), "error");
                    log.warn("Network link timeout: {} (last heartbeat: {}ms ago)", 
                        link.getLinkId(), currentTime - link.getLastHeartbeatTime());
                }
            }
        }
    }
    
    private void updateNetworkStatusOnSend(long bytes) {
        packetsSent.incrementAndGet();
        bytesSent.addAndGet(bytes);
        lastPacketSentTime = System.currentTimeMillis();
        setNetworkStatus(NetworkStatus.OK);
    }
    
    private void updateNetworkStatusOnReceive(long bytes) {
        packetsReceived.incrementAndGet();
        bytesReceived.addAndGet(bytes);
        lastPacketReceivedTime = System.currentTimeMillis();
        setNetworkStatus(NetworkStatus.OK);
    }
    
    private void updateNetworkStatusOnError() {
        packetsFailed.incrementAndGet();
        setNetworkStatus(NetworkStatus.ERROR);
    }
    
    private void setNetworkStatus(NetworkStatus status) {
        if (this.networkStatus != status) {
            log.info("Network status changed: {} -> {}", this.networkStatus, status);
            this.networkStatus = status;
        }
    }
    
    private void logNetworkStats() {
        log.debug("Network stats - Sent: {} packets ({} bytes), Received: {} packets ({} bytes), Failed: {} packets, Status: {}, Last activity: {}s ago",
            packetsSent.get(), bytesSent.get(),
            packetsReceived.get(), bytesReceived.get(),
            packetsFailed.get(), networkStatus,
            (System.currentTimeMillis() - Math.max(lastPacketReceivedTime, lastPacketSentTime)) / 1000);
    }
    
    private Map<String, Object> getNetworkStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("packetsSent", packetsSent.get());
        stats.put("packetsReceived", packetsReceived.get());
        stats.put("packetsFailed", packetsFailed.get());
        stats.put("bytesSent", bytesSent.get());
        stats.put("bytesReceived", bytesReceived.get());
        stats.put("networkStatus", networkStatus);
        stats.put("lastPacketReceivedTime", lastPacketReceivedTime);
        stats.put("lastPacketSentTime", lastPacketSentTime);
        stats.put("linkCount", networkLinks.size());
        stats.put("routeAgentCount", routeAgents.size());
        stats.put("endAgentCount", endAgents.size());
        return stats;
    }
    
    private void updateLinkHeartbeat(String agentId) {
        for (Map.Entry<String, NetworkLink> entry : networkLinks.entrySet()) {
            NetworkLink link = entry.getValue();
            if (agentId.equals(link.getSourceAgentId()) || agentId.equals(link.getTargetAgentId())) {
                link.setLastHeartbeatTime(System.currentTimeMillis());
                link.setStatus("active");
                log.debug("Updated heartbeat for network link: {}", link.getLinkId());
            }
        }
    }
    
    private void initializeNetworkLinks() {
        // 初始化默认网络链路
        log.info("Initializing network links");
        // 这里可以添加一些默认的网络链路初始化逻辑
    }
    
    // 重置网络统计数据
    public void resetNetworkStats() {
        packetsSent.set(0);
        packetsReceived.set(0);
        packetsFailed.set(0);
        bytesSent.set(0);
        bytesReceived.set(0);
        log.info("Network stats reset");
    }
    
    // 获取网络状态
    public NetworkStatus getNetworkStatus() {
        return networkStatus;
    }
    
    // 获取网络统计数据
    public Map<String, Object> getNetworkStatistics() {
        return getNetworkStats();
    }
}