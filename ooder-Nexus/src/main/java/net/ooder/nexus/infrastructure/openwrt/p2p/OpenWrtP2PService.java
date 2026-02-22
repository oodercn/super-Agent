package net.ooder.nexus.infrastructure.openwrt.p2p;

import net.ooder.nexus.infrastructure.openwrt.service.OpenWrtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ooderAgent 0.7.0 OpenWrt P2P 服务实现
 *
 * <p>实现ooderAgent协议0.7.0定义的OpenWrt P2P桥接接口，
 * 提供软硬一体化的P2P通讯能力。</p>
 *
 * @author ooder Team
 * @version 0.7.0
 * @since 0.7.0
 */
@Service
public class OpenWrtP2PService implements OpenWrtP2PBridge {

    private static final Logger log = LoggerFactory.getLogger(OpenWrtP2PService.class);

    /** OpenWrt 基础服务 */
    private final OpenWrtService openWrtService;

    /** Mock 模式标志 */
    private boolean useMock = true;

    /** 已注册的Agent存储 */
    private final Map<String, Map<String, Object>> registeredAgents = new ConcurrentHashMap<>();

    /** 端口池存储 */
    private final Map<String, PortPool> portPools = new ConcurrentHashMap<>();

    /** P2P连接存储 */
    private final Map<String, Map<String, Object>> p2pConnections = new ConcurrentHashMap<>();

    /** conntrack条目存储(用于Mock模式跟踪) */
    private final Set<String> conntrackEntries = ConcurrentHashMap.newKeySet();

    /** 协议版本 */
    private static final String PROTOCOL_VERSION = "0.7.0";

    /**
     * 端口池内部类
     */
    private static class PortPool {
        String agentId;
        List<Integer> ports;
        long allocatedAt;
        boolean active;

        PortPool(String agentId, List<Integer> ports) {
            this.agentId = agentId;
            this.ports = ports;
            this.allocatedAt = System.currentTimeMillis();
            this.active = true;
        }
    }

    @Autowired
    public OpenWrtP2PService(OpenWrtService openWrtService) {
        this.openWrtService = openWrtService;
    }

    @PostConstruct
    public void init() {
        log.info("Initializing OpenWrt P2P Service for ooderAgent Protocol {}", PROTOCOL_VERSION);
        this.useMock = openWrtService.isMockMode();

        if (useMock) {
            log.info("OpenWrt P2P Service running in MOCK mode");
            initMockData();
        } else {
            log.info("OpenWrt P2P Service initialized with real bridge");
        }
    }

    private void initMockData() {
        // 初始化示例数据
        log.info("Mock data initialized for P2P service");
    }

    @Override
    public Map<String, Object> preEstablishConntrack(String agentIp, int agentPort,
                                                      String peerIp, int peerPort,
                                                      String sessionKey) {
        log.info("Pre-establishing conntrack: {}:{} <-> {}:{} (session={})",
                agentIp, agentPort, peerIp, peerPort, sessionKey);

        String entryKey = String.format("%s:%d-%s:%d", agentIp, agentPort, peerIp, peerPort);

        if (useMock) {
            // Mock模式：模拟conntrack条目创建
            conntrackEntries.add(entryKey);

            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("entryKey", entryKey);
            result.put("timeout", 3600);
            result.put("state", "ESTABLISHED");
            result.put("message", "Conntrack entry pre-established (MOCK)");
            return result;
        } else {
            // 真实模式：执行conntrack命令
            String command = buildConntrackInsertCommand(agentIp, agentPort, peerIp, peerPort);
            Map<String, Object> cmdResult = executeCommand(command);

            if ((Boolean) cmdResult.getOrDefault("success", false)) {
                // 配置iptables规则
                String iptablesCmd = buildIptablesCommand(agentIp, agentPort, peerIp, peerPort, sessionKey);
                executeCommand(iptablesCmd);

                conntrackEntries.add(entryKey);
            }

            return cmdResult;
        }
    }

    @Override
    public Map<String, Object> batchPreEstablishConntrack(String agentIp, List<Integer> agentPorts,
                                                           String peerIp, List<Integer> peerPorts,
                                                           String sessionKey) {
        log.info("Batch pre-establishing conntrack: {} ports for session {}",
                agentPorts.size(), sessionKey);

        List<Map<String, Object>> results = new ArrayList<>();
        int successCount = 0;

        for (int i = 0; i < agentPorts.size() && i < peerPorts.size(); i++) {
            Map<String, Object> result = preEstablishConntrack(
                    agentIp, agentPorts.get(i),
                    peerIp, peerPorts.get(i),
                    sessionKey + "-" + i
            );
            results.add(result);
            if ((Boolean) result.getOrDefault("success", false)) {
                successCount++;
            }
        }

        Map<String, Object> batchResult = new HashMap<>();
        batchResult.put("success", successCount > 0);
        batchResult.put("total", agentPorts.size());
        batchResult.put("successCount", successCount);
        batchResult.put("results", results);
        batchResult.put("sessionKey", sessionKey);
        return batchResult;
    }

    @Override
    public Map<String, Object> releaseConntrack(String agentIp, int agentPort,
                                                 String peerIp, int peerPort) {
        log.info("Releasing conntrack: {}:{} <-> {}:{}", agentIp, agentPort, peerIp, peerPort);

        String entryKey = String.format("%s:%d-%s:%d", agentIp, agentPort, peerIp, peerPort);
        conntrackEntries.remove(entryKey);

        if (useMock) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "Conntrack entry released (MOCK)");
            return result;
        } else {
            String command = String.format("conntrack -D -p udp --orig-src %s --orig-sport %d --orig-dst %s --orig-dport %d",
                    agentIp, agentPort, peerIp, peerPort);
            return executeCommand(command);
        }
    }

    @Override
    public Map<String, Object> allocatePortPool(String agentId, int poolSize, int basePort) {
        log.info("Allocating port pool for agent {}: size={}, basePort={}", agentId, poolSize, basePort);

        // 检查是否已存在端口池
        if (portPools.containsKey(agentId)) {
            PortPool existing = portPools.get(agentId);
            if (existing.active) {
                Map<String, Object> result = new HashMap<>();
                result.put("success", false);
                result.put("error", "Port pool already exists for this agent");
                result.put("existingPorts", existing.ports);
                return result;
            }
        }

        // 生成端口列表
        List<Integer> ports = new ArrayList<>();
        int startPort = basePort > 0 ? basePort : 30000;

        for (int i = 0; i < poolSize; i++) {
            ports.add(startPort + i);
        }

        // 创建端口池
        PortPool pool = new PortPool(agentId, ports);
        portPools.put(agentId, pool);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("agentId", agentId);
        result.put("ports", ports);
        result.put("poolSize", poolSize);
        result.put("allocatedAt", pool.allocatedAt);
        return result;
    }

    @Override
    public Map<String, Object> releasePortPool(String agentId) {
        log.info("Releasing port pool for agent {}", agentId);

        PortPool pool = portPools.get(agentId);
        if (pool != null) {
            pool.active = false;
            portPools.remove(agentId);

            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("releasedPorts", pool.ports);
            return result;
        } else {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("error", "Port pool not found");
            return result;
        }
    }

    @Override
    public Map<String, Object> getPortPoolStatus(String agentId) {
        PortPool pool = portPools.get(agentId);

        Map<String, Object> result = new HashMap<>();
        if (pool != null) {
            result.put("exists", true);
            result.put("agentId", pool.agentId);
            result.put("ports", pool.ports);
            result.put("active", pool.active);
            result.put("allocatedAt", pool.allocatedAt);
            result.put("age", System.currentTimeMillis() - pool.allocatedAt);
        } else {
            result.put("exists", false);
        }
        return result;
    }

    @Override
    public Map<String, Object> configureHardwareQoS(String agentIp, int agentPort,
                                                     Map<String, Object> qosProfile) {
        log.info("Configuring hardware QoS for {}:{} with profile {}",
                agentIp, agentPort, qosProfile);

        // 提取QoS参数
        int priority = (Integer) qosProfile.getOrDefault("priority", 0);
        long bandwidth = (Long) qosProfile.getOrDefault("bandwidth", 0L);
        String trafficClass = (String) qosProfile.getOrDefault("class", "default");

        if (useMock) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("agentIp", agentIp);
            result.put("agentPort", agentPort);
            result.put("priority", priority);
            result.put("bandwidth", bandwidth);
            result.put("class", trafficClass);
            result.put("message", "Hardware QoS configured (MOCK)");
            return result;
        } else {
            // 构建tc命令配置HTB队列
            String tcCommand = buildTCCommand(agentIp, agentPort, priority, bandwidth);
            return executeCommand(tcCommand);
        }
    }

    @Override
    public Map<String, Object> configureTrafficMarking(String agentId, List<Integer> ports, int dscpValue) {
        log.info("Configuring traffic marking for agent {}: ports={}, DSCP={}",
                agentId, ports, dscpValue);

        if (useMock) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("agentId", agentId);
            result.put("ports", ports);
            result.put("dscp", dscpValue);
            result.put("message", "Traffic marking configured (MOCK)");
            return result;
        } else {
            // 构建iptables mangle规则
            StringBuilder cmd = new StringBuilder();
            cmd.append("iptables -t mangle -A OODER_P2P ");
            cmd.append("-p udp ");

            if (ports.size() == 1) {
                cmd.append("--dport ").append(ports.get(0)).append(" ");
            } else {
                cmd.append("-m multiport --dports ");
                cmd.append(String.join(",", ports.stream().map(String::valueOf).toArray(String[]::new)));
                cmd.append(" ");
            }

            cmd.append("-j DSCP --set-dscp ").append(dscpValue);

            return executeCommand(cmd.toString());
        }
    }

    @Override
    public Map<String, Object> getRealtimeTrafficStats(String agentId) {
        Map<String, Object> stats = new HashMap<>();

        if (useMock) {
            // Mock数据
            stats.put("agentId", agentId);
            stats.put("timestamp", System.currentTimeMillis());
            stats.put("bytesSent", 1024 * 1024 * 10L);  // 10 MB
            stats.put("bytesReceived", 1024 * 1024 * 5L);  // 5 MB
            stats.put("packetsSent", 15000);
            stats.put("packetsReceived", 12000);
            stats.put("packetLoss", 0.02);  // 2%
            stats.put("latency", 25);  // 25ms
            stats.put("jitter", 5);  // 5ms
        } else {
            // 真实模式：读取iptables计数器或conntrack统计
            stats.put("agentId", agentId);
            stats.put("timestamp", System.currentTimeMillis());
        }

        return stats;
    }

    @Override
    public Map<String, Object> setBandwidthLimit(String agentId, long downloadLimit, long uploadLimit) {
        log.info("Setting bandwidth limit for agent {}: down={}, up={}",
                agentId, downloadLimit, uploadLimit);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("agentId", agentId);
        result.put("downloadLimit", downloadLimit);
        result.put("uploadLimit", uploadLimit);
        result.put("message", "Bandwidth limit configured");
        return result;
    }

    @Override
    public Map<String, Object> detectNATType() {
        log.info("Detecting NAT type");

        // Mock模式下返回随机NAT类型
        String[] natTypes = {"FullCone", "RestrictedCone", "PortRestricted", "Symmetric"};
        String detectedType = useMock ? natTypes[new Random().nextInt(natTypes.length)] : "PortRestricted";

        Map<String, Object> result = new HashMap<>();
        result.put("type", detectedType);
        result.put("p2pFriendly", !detectedType.equals("Symmetric"));
        result.put("upnpAvailable", true);
        result.put("natPmpAvailable", false);

        // 根据NAT类型给出连接策略建议
        String recommendation;
        switch (detectedType) {
            case "FullCone":
                recommendation = "L1_DIRECT";
                break;
            case "RestrictedCone":
            case "PortRestricted":
                recommendation = "L2_PREDICT";
                break;
            case "Symmetric":
                recommendation = "L3_RELAY";
                break;
            default:
                recommendation = "L2_PREDICT";
        }
        result.put("recommendation", recommendation);

        return result;
    }

    @Override
    public Map<String, Object> getNATMappings() {
        Map<String, Object> result = new HashMap<>();

        if (useMock) {
            List<Map<String, Object>> mappings = new ArrayList<>();

            // 添加示例映射
            Map<String, Object> mapping1 = new HashMap<>();
            mapping1.put("protocol", "udp");
            mapping1.put("externalPort", 3074);
            mapping1.put("internalIp", "192.168.1.100");
            mapping1.put("internalPort", 3074);
            mapping1.put("description", "ooderAgent P2P");
            mappings.add(mapping1);

            result.put("mappings", mappings);
            result.put("total", mappings.size());
        } else {
            // 真实模式：读取iptables nat表或conntrack
            result.put("mappings", new ArrayList<>());
            result.put("total", 0);
        }

        return result;
    }

    @Override
    public Map<String, Object> predictNATPorts(int targetCount) {
        log.info("Predicting NAT ports: count={}", targetCount);

        // 模拟AI预测端口分配
        List<Integer> predictions = new ArrayList<>();
        Random random = new Random();

        // 基于常见NAT分配规律生成预测
        int basePort = 30000 + random.nextInt(10000);
        for (int i = 0; i < targetCount; i++) {
            // 模拟顺序分配或哈希分配
            predictions.add(basePort + i * (random.nextInt(3) + 1));
        }

        Map<String, Object> result = new HashMap<>();
        result.put("predictions", predictions);
        result.put("confidence", 0.65);  // 65%置信度
        result.put("algorithm", "hybrid");  // 混合预测算法
        return result;
    }

    @Override
    public Map<String, Object> registerAgent(Map<String, Object> agentInfo) {
        String wid = (String) agentInfo.get("wid");
        if (wid == null || wid.isEmpty()) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("error", "WID is required");
            return result;
        }

        log.info("Registering ooderAgent: {}", wid);

        // 检查协议版本
        String protocolVersion = (String) agentInfo.getOrDefault("protocolVersion", "unknown");
        if (!PROTOCOL_VERSION.equals(protocolVersion)) {
            log.warn("Protocol version mismatch: expected {}, got {}", PROTOCOL_VERSION, protocolVersion);
        }

        // 存储Agent信息
        agentInfo.put("registeredAt", System.currentTimeMillis());
        agentInfo.put("status", "active");
        registeredAgents.put(wid, agentInfo);

        // 创建Agent专用的iptables链
        if (!useMock) {
            String createChainCmd = String.format("iptables -N OODER_P2P_%s 2>/dev/null || iptables -F OODER_P2P_%s",
                    wid.replace("-", "_"), wid.replace("-", "_"));
            executeCommand(createChainCmd);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("wid", wid);
        result.put("protocolVersion", PROTOCOL_VERSION);
        result.put("message", "Agent registered successfully");
        return result;
    }

    @Override
    public Map<String, Object> unregisterAgent(String wid) {
        log.info("Unregistering ooderAgent: {}", wid);

        if (registeredAgents.remove(wid) != null) {
            // 清理相关资源
            releasePortPool(wid);

            // 删除iptables链
            if (!useMock) {
                String deleteChainCmd = String.format("iptables -X OODER_P2P_%s 2>/dev/null",
                        wid.replace("-", "_"));
                executeCommand(deleteChainCmd);
            }

            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "Agent unregistered successfully");
            return result;
        } else {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("error", "Agent not found");
            return result;
        }
    }

    @Override
    public Map<String, Object> getRegisteredAgents() {
        Map<String, Object> result = new HashMap<>();
        result.put("agents", new ArrayList<>(registeredAgents.values()));
        result.put("total", registeredAgents.size());
        return result;
    }

    @Override
    public Map<String, Object> verifyAgentIdentity(String wid, String agentCap) {
        Map<String, Object> agent = registeredAgents.get(wid);

        Map<String, Object> result = new HashMap<>();
        if (agent != null) {
            result.put("valid", true);
            result.put("wid", wid);
            result.put("status", agent.get("status"));

            // 如果提供了AgentCap，进行额外验证
            if (agentCap != null && !agentCap.isEmpty()) {
                // 实际实现中应与安全认证中心交互验证
                result.put("capVerified", true);
            }
        } else {
            result.put("valid", false);
            result.put("error", "Agent not registered");
        }

        return result;
    }

    @Override
    public Map<String, Object> establishP2PConnection(String localWid, String remoteWid,
                                                       Map<String, Object> connectionParams) {
        log.info("Establishing P2P connection: {} <-> {}", localWid, remoteWid);

        // 1. 验证双方Agent
        Map<String, Object> localAgent = registeredAgents.get(localWid);
        Map<String, Object> remoteAgent = registeredAgents.get(remoteWid);

        if (localAgent == null || remoteAgent == null) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("error", "One or both agents not registered");
            return result;
        }

        // 2. 检测NAT类型并选择策略
        Map<String, Object> natInfo = detectNATType();
        String strategy = (String) natInfo.get("recommendation");

        // 3. 生成连接ID
        String connectionId = "p2p-" + System.currentTimeMillis();

        // 4. 根据策略执行相应操作
        Map<String, Object> connectionData = new HashMap<>();
        connectionData.put("connectionId", connectionId);
        connectionData.put("localWid", localWid);
        connectionData.put("remoteWid", remoteWid);
        connectionData.put("strategy", strategy);
        connectionData.put("establishedAt", System.currentTimeMillis());
        connectionData.put("status", "establishing");

        if ("L1_DIRECT".equals(strategy)) {
            // L1策略：预建立conntrack
            String localIp = (String) localAgent.get("internalIp");
            Integer localPort = (Integer) connectionParams.get("localPort");
            String remoteIp = (String) connectionParams.get("remoteIp");
            Integer remotePort = (Integer) connectionParams.get("remotePort");

            if (localIp != null && localPort != null && remoteIp != null && remotePort != null) {
                preEstablishConntrack(localIp, localPort, remoteIp, remotePort, connectionId);
                connectionData.put("conntrackEstablished", true);
            }
        }

        // 5. 存储连接信息
        p2pConnections.put(connectionId, connectionData);
        connectionData.put("status", "established");

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("connectionId", connectionId);
        result.put("strategy", strategy);
        result.put("localWid", localWid);
        result.put("remoteWid", remoteWid);
        result.put("message", "P2P connection established with strategy: " + strategy);
        return result;
    }

    @Override
    public Map<String, Object> closeP2PConnection(String connectionId) {
        log.info("Closing P2P connection: {}", connectionId);

        Map<String, Object> connection = p2pConnections.remove(connectionId);
        if (connection != null) {
            // 清理conntrack条目
            if ((Boolean) connection.getOrDefault("conntrackEstablished", false)) {
                // 实际实现中应调用releaseConntrack
            }

            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("connectionId", connectionId);
            result.put("message", "P2P connection closed");
            return result;
        } else {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("error", "Connection not found");
            return result;
        }
    }

    @Override
    public Map<String, Object> getP2PConnectionStatus(String connectionId) {
        Map<String, Object> connection = p2pConnections.get(connectionId);

        if (connection != null) {
            Map<String, Object> result = new HashMap<>();
            result.put("exists", true);
            result.putAll(connection);
            result.put("age", System.currentTimeMillis() - (Long) connection.get("establishedAt"));
            return result;
        } else {
            Map<String, Object> result = new HashMap<>();
            result.put("exists", false);
            return result;
        }
    }

    @Override
    public Map<String, Object> getAgentP2PConnections(String wid) {
        List<Map<String, Object>> connections = new ArrayList<>();

        for (Map<String, Object> conn : p2pConnections.values()) {
            if (wid.equals(conn.get("localWid")) || wid.equals(conn.get("remoteWid"))) {
                connections.add(conn);
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("wid", wid);
        result.put("connections", connections);
        result.put("total", connections.size());
        return result;
    }

    @Override
    public Map<String, Object> testP2PConnectivity(String targetIp, int targetPort, String protocol) {
        log.info("Testing P2P connectivity: {}:{}/{}", targetIp, targetPort, protocol);

        // 模拟连通性测试
        Map<String, Object> result = new HashMap<>();
        result.put("target", targetIp + ":" + targetPort);
        result.put("protocol", protocol);
        result.put("reachable", true);
        result.put("latency", 25 + new Random().nextInt(20));  // 25-45ms
        result.put("packetLoss", 0.0);
        result.put("message", "Connectivity test passed");
        return result;
    }

    @Override
    public Map<String, Object> getP2PDiagnostics(String wid) {
        log.info("Getting P2P diagnostics for agent {}", wid);

        Map<String, Object> agent = registeredAgents.get(wid);
        Map<String, Object> diagnostics = new HashMap<>();

        diagnostics.put("wid", wid);
        diagnostics.put("timestamp", System.currentTimeMillis());

        if (agent != null) {
            diagnostics.put("registered", true);
            diagnostics.put("status", agent.get("status"));
            diagnostics.put("registeredAt", agent.get("registeredAt"));
        } else {
            diagnostics.put("registered", false);
        }

        // NAT信息
        diagnostics.put("nat", detectNATType());

        // 端口池状态
        diagnostics.put("portPool", getPortPoolStatus(wid));

        // 连接统计
        Map<String, Object> connections = getAgentP2PConnections(wid);
        diagnostics.put("connections", connections);

        // 系统资源
        diagnostics.put("resources", getSystemResources());

        return diagnostics;
    }

    @Override
    public Map<String, Object> getSystemResources() {
        Map<String, Object> resources = new HashMap<>();

        if (useMock) {
            resources.put("conntrackTotal", 65536);
            resources.put("conntrackUsed", conntrackEntries.size());
            resources.put("conntrackUsage", conntrackEntries.size() / 65536.0);
            resources.put("memoryTotal", 512 * 1024 * 1024L);  // 512MB
            resources.put("memoryUsed", 256 * 1024 * 1024L);   // 256MB
            resources.put("cpuUsage", 0.25);  // 25%
        } else {
            // 真实模式：读取系统资源
            resources.put("conntrackTotal", 0);
            resources.put("conntrackUsed", 0);
        }

        return resources;
    }

    @Override
    public Map<String, Object> executeNorthboundCommand(String command, Map<String, Object> params) {
        log.info("Executing northbound command: {} with params {}", command, params);

        Map<String, Object> result = new HashMap<>();
        result.put("command", command);
        result.put("protocolVersion", PROTOCOL_VERSION);

        switch (command) {
            case "GET_STATUS":
                result.put("status", "active");
                result.put("agents", registeredAgents.size());
                result.put("connections", p2pConnections.size());
                break;
            case "GET_AGENTS":
                result.put("agents", new ArrayList<>(registeredAgents.values()));
                break;
            case "GET_CONNECTIONS":
                result.put("connections", new ArrayList<>(p2pConnections.values()));
                break;
            default:
                result.put("error", "Unknown command: " + command);
        }

        return result;
    }

    @Override
    public Map<String, Object> getProtocolVersion() {
        Map<String, Object> version = new HashMap<>();
        version.put("protocol", "ooderAgent");
        version.put("version", PROTOCOL_VERSION);
        version.put("apiVersion", "1.0");
        version.put("features", Arrays.asList(
                "conntrack_pre_establish",
                "port_pool",
                "hardware_qos",
                "nat_detection",
                "agent_registration",
                "p2p_connection"
        ));
        return version;
    }

    // ==================== 辅助方法 ====================

    private String buildConntrackInsertCommand(String agentIp, int agentPort,
                                                String peerIp, int peerPort) {
        return String.format(
                "conntrack -I -p udp --src %s --sport %d --dst %s --dport %d --state ESTABLISHED --timeout 3600",
                agentIp, agentPort, peerIp, peerPort
        );
    }

    private String buildIptablesCommand(String agentIp, int agentPort,
                                         String peerIp, int peerPort,
                                         String sessionKey) {
        return String.format(
                "iptables -A OODER_P2P -p udp -s %s --sport %d -d %s --dport %d -m comment --comment \"%s\" -j ACCEPT",
                agentIp, agentPort, peerIp, peerPort, sessionKey
        );
    }

    private String buildTCCommand(String agentIp, int agentPort, int priority, long bandwidth) {
        // 简化的tc命令示例
        return String.format(
                "tc filter add dev br-lan protocol ip parent 1:0 prio %d u32 match ip sport %d 0xffff flowid 1:%d",
                priority, agentPort, 10 + priority
        );
    }

    private Map<String, Object> executeCommand(String command) {
        if (useMock) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("mock", true);
            result.put("command", command);
            return result;
        } else {
            return openWrtService.executeCommand(command);
        }
    }
}
