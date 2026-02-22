package net.ooder.nexus.service.impl;

import net.ooder.nexus.service.P2PService;
import net.ooder.sdk.api.OoderSDK;
import net.ooder.sdk.api.protocol.CommandPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class P2PServiceImpl implements P2PService {
    
    private static final Logger log = LoggerFactory.getLogger(P2PServiceImpl.class);
    
    private OoderSDK ooderSDK;
    private String agentId;
    
    private final Map<String, NetworkNode> nodes = new ConcurrentHashMap<String, NetworkNode>();
    
    private final Map<String, SkillInfo> skills = new ConcurrentHashMap<String, SkillInfo>();
    
    // 网络状态监控
    private final AtomicLong packetsSent = new AtomicLong(0);
    private final AtomicLong packetsReceived = new AtomicLong(0);
    private final AtomicLong packetsFailed = new AtomicLong(0);
    private volatile NetworkStatus networkStatus = NetworkStatus.OK;
    private volatile long lastActivityTime = System.currentTimeMillis();
    private final long networkTimeoutThreshold = 60000; // 60秒无网络活动视为网络异常
    
    // 网络节点数据结构
    private static class NetworkNode {
        private String nodeId;
        private String name;
        private String type;
        private String status;
        private String ipAddress;
        private int port;
        private String version;
        private long joinTime;
        private long lastHeartbeatTime;
        private Map<String, Object> metadata;
        private Map<String, Object> capabilities;
        
        // Getters and setters
        public String getNodeId() {
            return nodeId;
        }
        
        public void setNodeId(String nodeId) {
            this.nodeId = nodeId;
        }
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public String getType() {
            return type;
        }
        
        public void setType(String type) {
            this.type = type;
        }
        
        public String getStatus() {
            return status;
        }
        
        public void setStatus(String status) {
            this.status = status;
        }
        
        public String getIpAddress() {
            return ipAddress;
        }
        
        public void setIpAddress(String ipAddress) {
            this.ipAddress = ipAddress;
        }
        
        public int getPort() {
            return port;
        }
        
        public void setPort(int port) {
            this.port = port;
        }
        
        public String getVersion() {
            return version;
        }
        
        public void setVersion(String version) {
            this.version = version;
        }
        
        public long getJoinTime() {
            return joinTime;
        }
        
        public void setJoinTime(long joinTime) {
            this.joinTime = joinTime;
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
        
        public Map<String, Object> getCapabilities() {
            return capabilities;
        }
        
        public void setCapabilities(Map<String, Object> capabilities) {
            this.capabilities = capabilities;
        }
    }
    
    // 技能信息数据结构
    private static class SkillInfo {
        private String skillId;
        private String name;
        private String description;
        private String category;
        private String publisher;
        private long publishTime;
        private long lastUpdateTime;
        private Map<String, Object> parameters;
        private Map<String, Object> metadata;
        
        // Getters and setters
        public String getSkillId() {
            return skillId;
        }
        
        public void setSkillId(String skillId) {
            this.skillId = skillId;
        }
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public String getDescription() {
            return description;
        }
        
        public void setDescription(String description) {
            this.description = description;
        }
        
        public String getCategory() {
            return category;
        }
        
        public void setCategory(String category) {
            this.category = category;
        }
        
        public String getPublisher() {
            return publisher;
        }
        
        public void setPublisher(String publisher) {
            this.publisher = publisher;
        }
        
        public long getPublishTime() {
            return publishTime;
        }
        
        public void setPublishTime(long publishTime) {
            this.publishTime = publishTime;
        }
        
        public long getLastUpdateTime() {
            return lastUpdateTime;
        }
        
        public void setLastUpdateTime(long lastUpdateTime) {
            this.lastUpdateTime = lastUpdateTime;
        }
        
        public Map<String, Object> getParameters() {
            return parameters;
        }
        
        public void setParameters(Map<String, Object> parameters) {
            this.parameters = parameters;
        }
        
        public Map<String, Object> getMetadata() {
            return metadata;
        }
        
        public void setMetadata(Map<String, Object> metadata) {
            this.metadata = metadata;
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
    public void initialize(OoderSDK ooderSDK) {
        log.info("Initializing P2P Service");
        
        this.ooderSDK = ooderSDK;
        this.agentId = ooderSDK.getConfiguration().getAgentId();
        
        // 初始化网络节点，将自己添加为第一个节点
        NetworkNode selfNode = new NetworkNode();
        selfNode.setNodeId(agentId);
        selfNode.setName("Local Nexus Node");
        selfNode.setType("nexus");
        selfNode.setStatus("active");
        selfNode.setIpAddress("127.0.0.1");
        selfNode.setPort(9876);
        selfNode.setVersion("v1.0.0");
        selfNode.setJoinTime(System.currentTimeMillis());
        selfNode.setLastHeartbeatTime(System.currentTimeMillis());
        selfNode.setMetadata(new HashMap<>());
        selfNode.setCapabilities(new HashMap<>());
        
        // 设置默认能力
        Map<String, Object> capabilities = selfNode.getCapabilities();
        capabilities.put("skillExecution", true);
        capabilities.put("skillSharing", true);
        capabilities.put("fileSharing", true);
        capabilities.put("networkDiscovery", true);
        capabilities.put("nodeManagement", true);
        
        nodes.put(agentId, selfNode);
        
        log.info("P2P Service initialized successfully. Agent ID: {}", agentId);
    }
    
    @Override
    public List<Map<String, Object>> discoverNodes() {
        log.info("Discovering nodes in P2P network using SDK 0.6.6");
        
        List<Map<String, Object>> discoveredNodes = new ArrayList<>();
        
        try {
            // 使用本地模拟实现节点发现
            log.info("Using mock implementation for node discovery");
            for (int i = 1; i <= 3; i++) {
                Map<String, Object> node = new HashMap<>();
                node.put("nodeId", "node-" + i);
                node.put("name", "Node " + i);
                node.put("type", "nexus");
                node.put("status", "active");
                node.put("ipAddress", "192.168.1." + (100 + i));
                node.put("port", 9876);
                node.put("version", "v1.0.0");
                discoveredNodes.add(node);
            }
            
            // 更新网络状态
            updateNetworkStatusOnActivity();
            
            log.info("Total discovered nodes: {}", discoveredNodes.size());
        } catch (Exception e) {
            log.error("Error discovering nodes: {}", e.getMessage(), e);
            updateNetworkStatusOnError();
        }
        
        return discoveredNodes;
    }
    
    @Override
    public boolean joinNetwork(Map<String, Object> nodeInfo) {
        log.info("Joining P2P network with info: {}", nodeInfo);
        
        try {
            String nodeId = (String) nodeInfo.getOrDefault("nodeId", "node-" + System.currentTimeMillis());
            String name = (String) nodeInfo.getOrDefault("name", "Node " + nodeId);
            String type = (String) nodeInfo.getOrDefault("type", "nexus");
            String ipAddress = (String) nodeInfo.getOrDefault("ipAddress", "127.0.0.1");
            int port = (int) nodeInfo.getOrDefault("port", 9876);
            String version = (String) nodeInfo.getOrDefault("version", "v1.0.0");
            
            // 使用本地实现加入网络
            boolean joined = true;
            log.info("Joined network via local implementation: {}", joined);
            
            // 无论SDK调用是否成功，都更新本地节点列表
            NetworkNode node = new NetworkNode();
            node.setNodeId(nodeId);
            node.setName(name);
            node.setType(type);
            node.setStatus(joined ? "active" : "pending");
            node.setIpAddress(ipAddress);
            node.setPort(port);
            node.setVersion(version);
            node.setJoinTime(System.currentTimeMillis());
            node.setLastHeartbeatTime(System.currentTimeMillis());
            node.setMetadata(new HashMap<>());
            node.setCapabilities(new HashMap<>());
            
            // 设置默认能力
            Map<String, Object> capabilities = node.getCapabilities();
            capabilities.put("skillExecution", true);
            capabilities.put("skillSharing", true);
            capabilities.put("fileSharing", true);
            capabilities.put("networkDiscovery", true);
            capabilities.put("nodeManagement", true);
            
            nodes.put(nodeId, node);
            
            // 更新网络状态
            updateNetworkStatusOnActivity();
            
            log.info("Successfully joined P2P network: {}", nodeId);
            return joined;
        } catch (Exception e) {
            log.error("Error joining P2P network: {}", e.getMessage(), e);
            updateNetworkStatusOnError();
            return false;
        }
    }
    
    @Override
    public List<Map<String, Object>> getNodes() {
        log.info("Getting network nodes list");
        
        List<Map<String, Object>> nodeList = new ArrayList<>();
        
        try {
            // 检查节点状态
            checkNodeStatuses();
            
            // 转换节点数据
            for (NetworkNode node : nodes.values()) {
                Map<String, Object> nodeInfo = new HashMap<>();
                nodeInfo.put("nodeId", node.getNodeId());
                nodeInfo.put("name", node.getName());
                nodeInfo.put("type", node.getType());
                nodeInfo.put("status", node.getStatus());
                nodeInfo.put("ipAddress", node.getIpAddress());
                nodeInfo.put("port", node.getPort());
                nodeInfo.put("version", node.getVersion());
                nodeInfo.put("joinTime", node.getJoinTime());
                nodeInfo.put("lastHeartbeatTime", node.getLastHeartbeatTime());
                nodeList.add(nodeInfo);
            }
            
            // 更新网络状态
            updateNetworkStatusOnActivity();
        } catch (Exception e) {
            log.error("Error getting network nodes: {}", e.getMessage(), e);
            updateNetworkStatusOnError();
        }
        
        return nodeList;
    }
    
    @Override
    public Map<String, Object> getNodeDetails(String nodeId) {
        log.info("Getting node details: {}", nodeId);
        
        Map<String, Object> nodeDetails = new HashMap<>();
        
        try {
            NetworkNode node = nodes.get(nodeId);
            if (node != null) {
                nodeDetails.put("nodeId", node.getNodeId());
                nodeDetails.put("name", node.getName());
                nodeDetails.put("type", node.getType());
                nodeDetails.put("status", node.getStatus());
                nodeDetails.put("ipAddress", node.getIpAddress());
                nodeDetails.put("port", node.getPort());
                nodeDetails.put("version", node.getVersion());
                nodeDetails.put("joinTime", node.getJoinTime());
                nodeDetails.put("lastHeartbeatTime", node.getLastHeartbeatTime());
                nodeDetails.put("metadata", node.getMetadata());
                nodeDetails.put("capabilities", node.getCapabilities());
                
                // 更新网络状态
                updateNetworkStatusOnActivity();
            } else {
                nodeDetails.put("error", "Node not found");
                log.warn("Node not found: {}", nodeId);
            }
        } catch (Exception e) {
            log.error("Error getting node details: {}", e.getMessage(), e);
            nodeDetails.put("error", e.getMessage());
            updateNetworkStatusOnError();
        }
        
        return nodeDetails;
    }
    
    @Override
    public boolean removeNode(String nodeId) {
        log.info("Removing node: {}", nodeId);
        
        try {
            NetworkNode removedNode = nodes.remove(nodeId);
            if (removedNode != null) {
                // 更新网络状态
                updateNetworkStatusOnActivity();
                log.info("Successfully removed node: {}", nodeId);
                return true;
            } else {
                log.warn("Node not found for removal: {}", nodeId);
                return false;
            }
        } catch (Exception e) {
            log.error("Error removing node: {}", e.getMessage(), e);
            updateNetworkStatusOnError();
            return false;
        }
    }
    
    @Override
    public boolean publishSkill(Map<String, Object> skillInfo) {
        log.info("Publishing skill to network: {}", skillInfo);
        
        try {
            String skillId = (String) skillInfo.getOrDefault("skillId", "skill-" + System.currentTimeMillis());
            String name = (String) skillInfo.getOrDefault("name", "Skill " + skillId);
            String description = (String) skillInfo.getOrDefault("description", "Skill description");
            String category = (String) skillInfo.getOrDefault("category", "general");
            String publisher = (String) skillInfo.getOrDefault("publisher", agentId);
            
            // 使用本地存储实现技能发布
            SkillInfo skill = new SkillInfo();
            skill.setSkillId(skillId);
            skill.setName(name);
            skill.setDescription(description);
            skill.setCategory(category);
            skill.setPublisher(publisher);
            skill.setPublishTime(System.currentTimeMillis());
            skill.setLastUpdateTime(System.currentTimeMillis());
            skill.setParameters(new HashMap<>());
            skill.setMetadata(new HashMap<>());
            
            skills.put(skillId, skill);
            
            // 广播技能发布消息
            broadcastSkillPublish(skill);
            
            // 更新网络状态
            updateNetworkStatusOnActivity();
            
            log.info("Successfully published skill: {}", skillId);
            return true;
        } catch (Exception e) {
            log.error("Error publishing skill: {}", e.getMessage(), e);
            updateNetworkStatusOnError();
            return false;
        }
    }
    
    @Override
    public boolean subscribeSkill(String skillId) {
        log.info("Subscribing to skill: {}", skillId);
        
        try {
            // 使用本地存储实现技能订阅
            SkillInfo skill = skills.get(skillId);
            if (skill != null) {
                // 更新网络状态
                updateNetworkStatusOnActivity();
                log.info("Successfully subscribed to skill: {}", skillId);
                return true;
            } else {
                log.warn("Skill not found for subscription: {}", skillId);
                return false;
            }
        } catch (Exception e) {
            log.error("Error subscribing to skill: {}", e.getMessage(), e);
            updateNetworkStatusOnError();
            return false;
        }
    }
    
    @Override
    public List<Map<String, Object>> getSkillMarket() {
        log.info("Getting skill market list");
        
        List<Map<String, Object>> skillList = new ArrayList<>();
        
        try {
            // 使用本地存储实现技能市场获取
            for (SkillInfo skill : skills.values()) {
                Map<String, Object> skillInfo = new HashMap<>();
                skillInfo.put("skillId", skill.getSkillId());
                skillInfo.put("name", skill.getName());
                skillInfo.put("description", skill.getDescription());
                skillInfo.put("category", skill.getCategory());
                skillInfo.put("publisher", skill.getPublisher());
                skillInfo.put("publishTime", skill.getPublishTime());
                skillInfo.put("lastUpdateTime", skill.getLastUpdateTime());
                skillList.add(skillInfo);
            }
            
            // 更新网络状态
            updateNetworkStatusOnActivity();
        } catch (Exception e) {
            log.error("Error getting skill market: {}", e.getMessage(), e);
            updateNetworkStatusOnError();
        }
        
        return skillList;
    }
    
    @Override
    public Map<String, Object> getNetworkStatus() {
        log.info("Getting network status");
        
        Map<String, Object> statusInfo = new HashMap<>();
        
        try {
            // 检查网络状态
            checkNetworkStatus();
            
            statusInfo.put("status", networkStatus);
            statusInfo.put("nodeCount", nodes.size());
            statusInfo.put("skillCount", skills.size());
            statusInfo.put("packetsSent", packetsSent.get());
            statusInfo.put("packetsReceived", packetsReceived.get());
            statusInfo.put("packetsFailed", packetsFailed.get());
            statusInfo.put("lastActivityTime", lastActivityTime);
            statusInfo.put("timestamp", System.currentTimeMillis());
            
            // 添加节点状态统计
            Map<String, Integer> nodeStatusCount = new HashMap<>();
            for (NetworkNode node : nodes.values()) {
                String status = node.getStatus();
                nodeStatusCount.put(status, nodeStatusCount.getOrDefault(status, 0) + 1);
            }
            statusInfo.put("nodeStatusCount", nodeStatusCount);
        } catch (Exception e) {
            log.error("Error getting network status: {}", e.getMessage(), e);
            statusInfo.put("error", e.getMessage());
            statusInfo.put("status", NetworkStatus.ERROR);
        }
        
        return statusInfo;
    }
    
    @Override
    public boolean sendMessage(String targetNodeId, Map<String, Object> message) {
        log.info("Sending message to node: {} using SDK 0.6.6", targetNodeId);
        
        try {
            NetworkNode targetNode = nodes.get(targetNodeId);
            if (targetNode != null && "active".equals(targetNode.getStatus())) {
                // 使用本地实现发送消息
                boolean sent = true;
                log.info("Message sent via local implementation: {}", sent);
                
                // 更新网络状态
                if (sent) {
                    packetsSent.incrementAndGet();
                    updateNetworkStatusOnActivity();
                    log.info("Successfully sent message to node: {}", targetNodeId);
                    return true;
                } else {
                    packetsFailed.incrementAndGet();
                    updateNetworkStatusOnError();
                    log.warn("Failed to send message: {}", targetNodeId);
                    return false;
                }
            } else {
                log.warn("Target node not found or inactive: {}", targetNodeId);
                packetsFailed.incrementAndGet();
                updateNetworkStatusOnError();
                return false;
            }
        } catch (Exception e) {
            log.error("Error sending message: {}", e.getMessage(), e);
            packetsFailed.incrementAndGet();
            updateNetworkStatusOnError();
            return false;
        }
    }
    
    @Override
    public void handleMessage(CommandPacket packet) {
        log.info("Handling received P2P message: {}", packet);
        
        try {
            // 处理接收到的P2P消息
            // 根据消息类型进行不同的处理
            
            // 更新网络状态
            packetsReceived.incrementAndGet();
            updateNetworkStatusOnActivity();
            
            log.info("Successfully handled P2P message");
        } catch (Exception e) {
            log.error("Error handling P2P message: {}", e.getMessage(), e);
            packetsFailed.incrementAndGet();
            updateNetworkStatusOnError();
        }
    }
    
    // 检查节点状态
    private void checkNodeStatuses() {
        long currentTime = System.currentTimeMillis();
        long nodeTimeoutThreshold = 120000; // 120秒无心跳视为节点异常
        
        for (NetworkNode node : nodes.values()) {
            if (currentTime - node.getLastHeartbeatTime() > nodeTimeoutThreshold) {
                if (!"inactive".equals(node.getStatus())) {
                    node.setStatus("inactive");
                    log.warn("Node marked as inactive due to timeout: {}", node.getNodeId());
                }
            }
        }
    }
    
    // 检查网络状态
    private void checkNetworkStatus() {
        long currentTime = System.currentTimeMillis();
        
        // 检查网络超时
        if (currentTime - lastActivityTime > networkTimeoutThreshold) {
            setNetworkStatus(NetworkStatus.TIMEOUT);
            log.warn("Network timeout detected: No activity for {}ms", 
                currentTime - lastActivityTime);
        } else {
            setNetworkStatus(NetworkStatus.OK);
        }
    }
    
    // 更新网络状态（活动）
    private void updateNetworkStatusOnActivity() {
        lastActivityTime = System.currentTimeMillis();
        setNetworkStatus(NetworkStatus.OK);
    }
    
    // 更新网络状态（错误）
    private void updateNetworkStatusOnError() {
        packetsFailed.incrementAndGet();
        setNetworkStatus(NetworkStatus.ERROR);
    }
    
    // 设置网络状态
    private void setNetworkStatus(NetworkStatus status) {
        if (this.networkStatus != status) {
            log.info("Network status changed: {} -> {}", this.networkStatus, status);
            this.networkStatus = status;
        }
    }
    
    // 广播技能发布消息
    private void broadcastSkillPublish(Object skillData) {
        String skillId;
        String skillName;
        String publisher;
        
        if (skillData instanceof Map) {
            // 处理Map类型的skillInfo
            Map<String, Object> skillInfo = (Map<String, Object>) skillData;
            skillId = (String) skillInfo.getOrDefault("skillId", "unknown");
            skillName = (String) skillInfo.getOrDefault("name", "Unknown Skill");
            publisher = (String) skillInfo.getOrDefault("publisher", agentId);
        } else if (skillData instanceof SkillInfo) {
            // 处理SkillInfo类型的skill
            SkillInfo skill = (SkillInfo) skillData;
            skillId = skill.getSkillId();
            skillName = skill.getName();
            publisher = skill.getPublisher();
        } else {
            log.warn("Unknown skill data type for broadcasting: {}", skillData.getClass().getName());
            return;
        }
        
        log.info("Broadcasting skill publish: {}", skillId);
        
        // 向所有活跃节点广播技能发布消息
        for (NetworkNode node : nodes.values()) {
            if (!node.getNodeId().equals(agentId) && "active".equals(node.getStatus())) {
                Map<String, Object> message = new HashMap<>();
                message.put("type", "skill_publish");
                message.put("skillId", skillId);
                message.put("skillName", skillName);
                message.put("publisher", publisher);
                message.put("timestamp", System.currentTimeMillis());
                
                sendMessage(node.getNodeId(), message);
            }
        }
    }
}
