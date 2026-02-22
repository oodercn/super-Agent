package net.ooder.nexus.infrastructure.openwrt.p2p;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ooderAgent 0.7.0 OpenWrt P2P REST API 控制器
 *
 * <p>提供ooderAgent协议0.7.0定义的OpenWrt P2P接口的RESTful API，
 * 支持与上层控制器的北向协议交互。</p>
 *
 * @author ooder Team
 * @version 0.7.0
 * @since 0.7.0
 */
@RestController
@RequestMapping("/api/openwrt/p2p")
public class OpenWrtP2PController {

    private static final Logger log = LoggerFactory.getLogger(OpenWrtP2PController.class);

    private final OpenWrtP2PBridge p2pBridge;

    @Autowired
    public OpenWrtP2PController(OpenWrtP2PBridge p2pBridge) {
        this.p2pBridge = p2pBridge;
    }

    // ==================== 协议版本接口 ====================

    @GetMapping("/version")
    public ResponseEntity<Map<String, Object>> getProtocolVersion() {
        log.debug("GET /api/openwrt/p2p/version");
        return ResponseEntity.ok(p2pBridge.getProtocolVersion());
    }

    // ==================== Agent注册接口 ====================

    @PostMapping("/agents/register")
    public ResponseEntity<Map<String, Object>> registerAgent(@RequestBody Map<String, Object> agentInfo) {
        log.info("POST /api/openwrt/p2p/agents/register: {}", agentInfo);
        return ResponseEntity.ok(p2pBridge.registerAgent(agentInfo));
    }

    @PostMapping("/agents/{wid}/unregister")
    public ResponseEntity<Map<String, Object>> unregisterAgent(@PathVariable String wid) {
        log.info("POST /api/openwrt/p2p/agents/{}/unregister", wid);
        return ResponseEntity.ok(p2pBridge.unregisterAgent(wid));
    }

    @GetMapping("/agents")
    public ResponseEntity<Map<String, Object>> getRegisteredAgents() {
        log.debug("GET /api/openwrt/p2p/agents");
        return ResponseEntity.ok(p2pBridge.getRegisteredAgents());
    }

    @PostMapping("/agents/{wid}/verify")
    public ResponseEntity<Map<String, Object>> verifyAgentIdentity(
            @PathVariable String wid,
            @RequestBody(required = false) Map<String, String> request) {
        log.info("POST /api/openwrt/p2p/agents/{}/verify", wid);
        String agentCap = request != null ? request.get("agentCap") : null;
        return ResponseEntity.ok(p2pBridge.verifyAgentIdentity(wid, agentCap));
    }

    // ==================== conntrack预建立接口 ====================

    @PostMapping("/conntrack/pre-establish")
    public ResponseEntity<Map<String, Object>> preEstablishConntrack(@RequestBody Map<String, Object> request) {
        log.info("POST /api/openwrt/p2p/conntrack/pre-establish");

        String agentIp = (String) request.get("agentIp");
        Integer agentPort = (Integer) request.get("agentPort");
        String peerIp = (String) request.get("peerIp");
        Integer peerPort = (Integer) request.get("peerPort");
        String sessionKey = (String) request.get("sessionKey");

        if (agentIp == null || agentPort == null || peerIp == null || peerPort == null) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", "Missing required parameters: agentIp, agentPort, peerIp, peerPort");
            return ResponseEntity.badRequest().body(error);
        }

        return ResponseEntity.ok(p2pBridge.preEstablishConntrack(agentIp, agentPort, peerIp, peerPort, sessionKey));
    }

    @PostMapping("/conntrack/batch-pre-establish")
    public ResponseEntity<Map<String, Object>> batchPreEstablishConntrack(@RequestBody Map<String, Object> request) {
        log.info("POST /api/openwrt/p2p/conntrack/batch-pre-establish");

        String agentIp = (String) request.get("agentIp");
        @SuppressWarnings("unchecked")
        List<Integer> agentPorts = (List<Integer>) request.get("agentPorts");
        String peerIp = (String) request.get("peerIp");
        @SuppressWarnings("unchecked")
        List<Integer> peerPorts = (List<Integer>) request.get("peerPorts");
        String sessionKey = (String) request.get("sessionKey");

        if (agentIp == null || agentPorts == null || peerIp == null || peerPorts == null) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", "Missing required parameters");
            return ResponseEntity.badRequest().body(error);
        }

        return ResponseEntity.ok(p2pBridge.batchPreEstablishConntrack(agentIp, agentPorts, peerIp, peerPorts, sessionKey));
    }

    @PostMapping("/conntrack/release")
    public ResponseEntity<Map<String, Object>> releaseConntrack(@RequestBody Map<String, Object> request) {
        log.info("POST /api/openwrt/p2p/conntrack/release");

        String agentIp = (String) request.get("agentIp");
        Integer agentPort = (Integer) request.get("agentPort");
        String peerIp = (String) request.get("peerIp");
        Integer peerPort = (Integer) request.get("peerPort");

        if (agentIp == null || agentPort == null || peerIp == null || peerPort == null) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", "Missing required parameters");
            return ResponseEntity.badRequest().body(error);
        }

        return ResponseEntity.ok(p2pBridge.releaseConntrack(agentIp, agentPort, peerIp, peerPort));
    }

    // ==================== 端口池管理接口 ====================

    @PostMapping("/port-pools/allocate")
    public ResponseEntity<Map<String, Object>> allocatePortPool(@RequestBody Map<String, Object> request) {
        log.info("POST /api/openwrt/p2p/port-pools/allocate: {}", request);

        String agentId = (String) request.get("agentId");
        Integer poolSize = (Integer) request.getOrDefault("poolSize", 8);
        Integer basePort = (Integer) request.getOrDefault("basePort", 0);

        if (agentId == null) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", "Missing required parameter: agentId");
            return ResponseEntity.badRequest().body(error);
        }

        return ResponseEntity.ok(p2pBridge.allocatePortPool(agentId, poolSize, basePort));
    }

    @PostMapping("/port-pools/{agentId}/release")
    public ResponseEntity<Map<String, Object>> releasePortPool(@PathVariable String agentId) {
        log.info("POST /api/openwrt/p2p/port-pools/{}/release", agentId);
        return ResponseEntity.ok(p2pBridge.releasePortPool(agentId));
    }

    @GetMapping("/port-pools/{agentId}/status")
    public ResponseEntity<Map<String, Object>> getPortPoolStatus(@PathVariable String agentId) {
        log.debug("GET /api/openwrt/p2p/port-pools/{}/status", agentId);
        return ResponseEntity.ok(p2pBridge.getPortPoolStatus(agentId));
    }

    // ==================== QoS配置接口 ====================

    @PostMapping("/qos/hardware")
    public ResponseEntity<Map<String, Object>> configureHardwareQoS(@RequestBody Map<String, Object> request) {
        log.info("POST /api/openwrt/p2p/qos/hardware: {}", request);

        String agentIp = (String) request.get("agentIp");
        Integer agentPort = (Integer) request.get("agentPort");
        @SuppressWarnings("unchecked")
        Map<String, Object> qosProfile = (Map<String, Object>) request.get("qosProfile");

        if (agentIp == null || agentPort == null || qosProfile == null) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", "Missing required parameters");
            return ResponseEntity.badRequest().body(error);
        }

        return ResponseEntity.ok(p2pBridge.configureHardwareQoS(agentIp, agentPort, qosProfile));
    }

    @PostMapping("/qos/traffic-marking")
    public ResponseEntity<Map<String, Object>> configureTrafficMarking(@RequestBody Map<String, Object> request) {
        log.info("POST /api/openwrt/p2p/qos/traffic-marking: {}", request);

        String agentId = (String) request.get("agentId");
        @SuppressWarnings("unchecked")
        List<Integer> ports = (List<Integer>) request.get("ports");
        Integer dscpValue = (Integer) request.get("dscpValue");

        if (agentId == null || ports == null || dscpValue == null) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", "Missing required parameters");
            return ResponseEntity.badRequest().body(error);
        }

        return ResponseEntity.ok(p2pBridge.configureTrafficMarking(agentId, ports, dscpValue));
    }

    @GetMapping("/qos/traffic-stats/{agentId}")
    public ResponseEntity<Map<String, Object>> getRealtimeTrafficStats(@PathVariable String agentId) {
        log.debug("GET /api/openwrt/p2p/qos/traffic-stats/{}", agentId);
        return ResponseEntity.ok(p2pBridge.getRealtimeTrafficStats(agentId));
    }

    @PostMapping("/qos/bandwidth-limit/{agentId}")
    public ResponseEntity<Map<String, Object>> setBandwidthLimit(
            @PathVariable String agentId,
            @RequestBody Map<String, Long> request) {
        log.info("POST /api/openwrt/p2p/qos/bandwidth-limit/{}: {}", agentId, request);

        Long downloadLimit = request.getOrDefault("downloadLimit", 0L);
        Long uploadLimit = request.getOrDefault("uploadLimit", 0L);

        return ResponseEntity.ok(p2pBridge.setBandwidthLimit(agentId, downloadLimit, uploadLimit));
    }

    // ==================== NAT检测接口 ====================

    @GetMapping("/nat/detect")
    public ResponseEntity<Map<String, Object>> detectNATType() {
        log.info("GET /api/openwrt/p2p/nat/detect");
        return ResponseEntity.ok(p2pBridge.detectNATType());
    }

    @GetMapping("/nat/mappings")
    public ResponseEntity<Map<String, Object>> getNATMappings() {
        log.debug("GET /api/openwrt/p2p/nat/mappings");
        return ResponseEntity.ok(p2pBridge.getNATMappings());
    }

    @GetMapping("/nat/predict-ports")
    public ResponseEntity<Map<String, Object>> predictNATPorts(
            @RequestParam(defaultValue = "10") int count) {
        log.info("GET /api/openwrt/p2p/nat/predict-ports?count={}", count);
        return ResponseEntity.ok(p2pBridge.predictNATPorts(count));
    }

    // ==================== P2P连接管理接口 ====================

    @PostMapping("/connections/establish")
    public ResponseEntity<Map<String, Object>> establishP2PConnection(@RequestBody Map<String, Object> request) {
        log.info("POST /api/openwrt/p2p/connections/establish: {}", request);

        String localWid = (String) request.get("localWid");
        String remoteWid = (String) request.get("remoteWid");
        @SuppressWarnings("unchecked")
        Map<String, Object> connectionParams = (Map<String, Object>) request.get("connectionParams");

        if (localWid == null || remoteWid == null) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", "Missing required parameters: localWid, remoteWid");
            return ResponseEntity.badRequest().body(error);
        }

        return ResponseEntity.ok(p2pBridge.establishP2PConnection(localWid, remoteWid, connectionParams));
    }

    @PostMapping("/connections/{connectionId}/close")
    public ResponseEntity<Map<String, Object>> closeP2PConnection(@PathVariable String connectionId) {
        log.info("POST /api/openwrt/p2p/connections/{}/close", connectionId);
        return ResponseEntity.ok(p2pBridge.closeP2PConnection(connectionId));
    }

    @GetMapping("/connections/{connectionId}/status")
    public ResponseEntity<Map<String, Object>> getP2PConnectionStatus(@PathVariable String connectionId) {
        log.debug("GET /api/openwrt/p2p/connections/{}/status", connectionId);
        return ResponseEntity.ok(p2pBridge.getP2PConnectionStatus(connectionId));
    }

    @GetMapping("/connections/agent/{wid}")
    public ResponseEntity<Map<String, Object>> getAgentP2PConnections(@PathVariable String wid) {
        log.debug("GET /api/openwrt/p2p/connections/agent/{}", wid);
        return ResponseEntity.ok(p2pBridge.getAgentP2PConnections(wid));
    }

    // ==================== 诊断与监控接口 ====================

    @PostMapping("/diagnostics/connectivity")
    public ResponseEntity<Map<String, Object>> testP2PConnectivity(@RequestBody Map<String, Object> request) {
        log.info("POST /api/openwrt/p2p/diagnostics/connectivity: {}", request);

        String targetIp = (String) request.get("targetIp");
        Integer targetPort = (Integer) request.get("targetPort");
        String protocol = (String) request.getOrDefault("protocol", "udp");

        if (targetIp == null || targetPort == null) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", "Missing required parameters: targetIp, targetPort");
            return ResponseEntity.badRequest().body(error);
        }

        return ResponseEntity.ok(p2pBridge.testP2PConnectivity(targetIp, targetPort, protocol));
    }

    @GetMapping("/diagnostics/{wid}")
    public ResponseEntity<Map<String, Object>> getP2PDiagnostics(@PathVariable String wid) {
        log.info("GET /api/openwrt/p2p/diagnostics/{}", wid);
        return ResponseEntity.ok(p2pBridge.getP2PDiagnostics(wid));
    }

    @GetMapping("/system/resources")
    public ResponseEntity<Map<String, Object>> getSystemResources() {
        log.debug("GET /api/openwrt/p2p/system/resources");
        return ResponseEntity.ok(p2pBridge.getSystemResources());
    }

    // ==================== 北向协议接口 ====================

    @PostMapping("/northbound/command")
    public ResponseEntity<Map<String, Object>> executeNorthboundCommand(@RequestBody Map<String, Object> request) {
        log.info("POST /api/openwrt/p2p/northbound/command: {}", request);

        String command = (String) request.get("command");
        @SuppressWarnings("unchecked")
        Map<String, Object> params = (Map<String, Object>) request.get("params");

        if (command == null) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", "Missing required parameter: command");
            return ResponseEntity.badRequest().body(error);
        }

        return ResponseEntity.ok(p2pBridge.executeNorthboundCommand(command, params));
    }
}
