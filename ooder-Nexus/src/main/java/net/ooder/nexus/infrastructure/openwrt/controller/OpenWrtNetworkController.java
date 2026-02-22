package net.ooder.nexus.infrastructure.openwrt.controller;

import net.ooder.nexus.infrastructure.openwrt.service.OpenWrtNetworkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OpenWrt 网络高级管理 REST API 控制器
 *
 * <p>提供 OpenWrt 路由器的深度网络管理接口：</p>
 * <ul>
 *   <li>端口映射管理（Port Forwarding）</li>
 *   <li>UPnP/NAT-PMP 管理</li>
 *   <li>防火墙规则管理</li>
 *   <li>内网穿透配置（FRP、ZeroTier、Tailscale、WireGuard）</li>
 *   <li>DDNS 动态域名管理</li>
 *   <li>流量控制和 QoS</li>
 *   <li>OoderAgent 软硬一体化协议</li>
 * </ul>
 *
 * <p><strong>基础路径：</strong> {@code /api/openwrt/network}</p>
 *
 * @author ooder Team
 * @version 2.0.0
 * @since 2.0.0
 * @see OpenWrtNetworkService
 */
@RestController
@RequestMapping("/api/openwrt/network")
public class OpenWrtNetworkController {

    private static final Logger log = LoggerFactory.getLogger(OpenWrtNetworkController.class);

    /** OpenWrt 网络服务 */
    private final OpenWrtNetworkService networkService;

    /**
     * 构造函数
     *
     * @param networkService OpenWrt 网络服务实例
     */
    @Autowired
    public OpenWrtNetworkController(OpenWrtNetworkService networkService) {
        this.networkService = networkService;
    }

    // ==================== 端口映射管理 ====================

    /**
     * 获取所有端口映射规则
     *
     * @return 端口映射规则列表
     */
    @GetMapping("/port-forwarding")
    public ResponseEntity<Map<String, Object>> getPortForwardingRules() {
        log.info("Getting port forwarding rules");
        Map<String, Object> result = networkService.getPortForwardingRules();
        return ResponseEntity.ok(wrapResponse(result));
    }

    /**
     * 添加端口映射规则
     *
     * @param ruleData 规则数据
     * @return 操作结果
     */
    @PostMapping("/port-forwarding")
    public ResponseEntity<Map<String, Object>> addPortForwardingRule(@RequestBody Map<String, Object> ruleData) {
        log.info("Adding port forwarding rule: {}", ruleData);
        Map<String, Object> result = networkService.addPortForwardingRule(ruleData);
        return ResponseEntity.ok(wrapResponse(result));
    }

    /**
     * 更新端口映射规则
     *
     * @param ruleId   规则ID
     * @param ruleData 规则数据
     * @return 操作结果
     */
    @PutMapping("/port-forwarding/{ruleId}")
    public ResponseEntity<Map<String, Object>> updatePortForwardingRule(
            @PathVariable String ruleId,
            @RequestBody Map<String, Object> ruleData) {
        log.info("Updating port forwarding rule: {}", ruleId);
        Map<String, Object> result = networkService.updatePortForwardingRule(ruleId, ruleData);
        return ResponseEntity.ok(wrapResponse(result));
    }

    /**
     * 删除端口映射规则
     *
     * @param ruleId 规则ID
     * @return 操作结果
     */
    @DeleteMapping("/port-forwarding/{ruleId}")
    public ResponseEntity<Map<String, Object>> deletePortForwardingRule(@PathVariable String ruleId) {
        log.info("Deleting port forwarding rule: {}", ruleId);
        Map<String, Object> result = networkService.deletePortForwardingRule(ruleId);
        return ResponseEntity.ok(wrapResponse(result));
    }

    /**
     * 批量添加端口映射规则
     *
     * @param rulesData 规则数据列表
     * @return 操作结果
     */
    @PostMapping("/port-forwarding/batch")
    public ResponseEntity<Map<String, Object>> batchAddPortForwardingRules(
            @RequestBody List<Map<String, Object>> rulesData) {
        log.info("Batch adding port forwarding rules");
        Map<String, Object> result = networkService.batchAddPortForwardingRules(rulesData);
        return ResponseEntity.ok(wrapResponse(result));
    }

    /**
     * 启用/禁用端口映射规则
     *
     * @param ruleId  规则ID
     * @param enabled 启用状态
     * @return 操作结果
     */
    @PutMapping("/port-forwarding/{ruleId}/toggle")
    public ResponseEntity<Map<String, Object>> togglePortForwardingRule(
            @PathVariable String ruleId,
            @RequestParam boolean enabled) {
        log.info("Toggling port forwarding rule: {} -> {}", ruleId, enabled);
        Map<String, Object> result = networkService.togglePortForwardingRule(ruleId, enabled);
        return ResponseEntity.ok(wrapResponse(result));
    }

    /**
     * 验证端口映射规则
     *
     * @param ruleData 规则数据
     * @return 验证结果
     */
    @PostMapping("/port-forwarding/validate")
    public ResponseEntity<Map<String, Object>> validatePortForwardingRule(@RequestBody Map<String, Object> ruleData) {
        log.info("Validating port forwarding rule");
        Map<String, Object> result = networkService.validatePortForwardingRule(ruleData);
        return ResponseEntity.ok(wrapResponse(result));
    }

    // ==================== UPnP/NAT-PMP 管理 ====================

    /**
     * 获取 UPnP 状态
     *
     * @return UPnP 服务状态
     */
    @GetMapping("/upnp/status")
    public ResponseEntity<Map<String, Object>> getUPnPStatus() {
        log.info("Getting UPnP status");
        Map<String, Object> result = networkService.getUPnPStatus();
        return ResponseEntity.ok(wrapResponse(result));
    }

    /**
     * 启用/禁用 UPnP 服务
     *
     * @param enabled 启用状态
     * @return 操作结果
     */
    @PutMapping("/upnp/enable")
    public ResponseEntity<Map<String, Object>> setUPnPEnabled(@RequestParam boolean enabled) {
        log.info("Setting UPnP enabled: {}", enabled);
        Map<String, Object> result = networkService.setUPnPEnabled(enabled);
        return ResponseEntity.ok(wrapResponse(result));
    }

    /**
     * 获取 UPnP 端口映射列表
     *
     * @return UPnP 端口映射列表
     */
    @GetMapping("/upnp/mappings")
    public ResponseEntity<Map<String, Object>> getUPnPPortMappings() {
        log.info("Getting UPnP port mappings");
        Map<String, Object> result = networkService.getUPnPPortMappings();
        return ResponseEntity.ok(wrapResponse(result));
    }

    /**
     * 删除 UPnP 端口映射
     *
     * @param mappingId 映射ID
     * @return 操作结果
     */
    @DeleteMapping("/upnp/mappings/{mappingId}")
    public ResponseEntity<Map<String, Object>> deleteUPnPPortMapping(@PathVariable String mappingId) {
        log.info("Deleting UPnP port mapping: {}", mappingId);
        Map<String, Object> result = networkService.deleteUPnPPortMapping(mappingId);
        return ResponseEntity.ok(wrapResponse(result));
    }

    /**
     * 配置 UPnP 安全规则
     *
     * @param securityConfig 安全配置
     * @return 操作结果
     */
    @PutMapping("/upnp/security")
    public ResponseEntity<Map<String, Object>> configureUPnPSecurity(@RequestBody Map<String, Object> securityConfig) {
        log.info("Configuring UPnP security");
        Map<String, Object> result = networkService.configureUPnPSecurity(securityConfig);
        return ResponseEntity.ok(wrapResponse(result));
    }

    // ==================== 防火墙规则管理 ====================

    /**
     * 获取防火墙规则列表
     *
     * @return 防火墙规则列表
     */
    @GetMapping("/firewall")
    public ResponseEntity<Map<String, Object>> getFirewallRules() {
        log.info("Getting firewall rules");
        Map<String, Object> result = networkService.getFirewallRules();
        return ResponseEntity.ok(wrapResponse(result));
    }

    /**
     * 添加防火墙规则
     *
     * @param ruleData 规则数据
     * @return 操作结果
     */
    @PostMapping("/firewall")
    public ResponseEntity<Map<String, Object>> addFirewallRule(@RequestBody Map<String, Object> ruleData) {
        log.info("Adding firewall rule");
        Map<String, Object> result = networkService.addFirewallRule(ruleData);
        return ResponseEntity.ok(wrapResponse(result));
    }

    /**
     * 更新防火墙规则
     *
     * @param ruleId   规则ID
     * @param ruleData 规则数据
     * @return 操作结果
     */
    @PutMapping("/firewall/{ruleId}")
    public ResponseEntity<Map<String, Object>> updateFirewallRule(
            @PathVariable String ruleId,
            @RequestBody Map<String, Object> ruleData) {
        log.info("Updating firewall rule: {}", ruleId);
        Map<String, Object> result = networkService.updateFirewallRule(ruleId, ruleData);
        return ResponseEntity.ok(wrapResponse(result));
    }

    /**
     * 删除防火墙规则
     *
     * @param ruleId 规则ID
     * @return 操作结果
     */
    @DeleteMapping("/firewall/{ruleId}")
    public ResponseEntity<Map<String, Object>> deleteFirewallRule(@PathVariable String ruleId) {
        log.info("Deleting firewall rule: {}", ruleId);
        Map<String, Object> result = networkService.deleteFirewallRule(ruleId);
        return ResponseEntity.ok(wrapResponse(result));
    }

    // ==================== 内网穿透管理 ====================

    /**
     * 获取内网穿透配置
     *
     * @return 内网穿透配置
     */
    @GetMapping("/tunnels")
    public ResponseEntity<Map<String, Object>> getTunnelConfigs() {
        log.info("Getting tunnel configs");
        Map<String, Object> result = networkService.getTunnelConfigs();
        return ResponseEntity.ok(wrapResponse(result));
    }

    /**
     * 配置 FRP 客户端
     *
     * @param config FRP 配置
     * @return 操作结果
     */
    @PostMapping("/tunnels/frp")
    public ResponseEntity<Map<String, Object>> configureFRP(@RequestBody Map<String, Object> config) {
        log.info("Configuring FRP");
        Map<String, Object> result = networkService.configureFRP(config);
        return ResponseEntity.ok(wrapResponse(result));
    }

    /**
     * 配置 ZeroTier 网络
     *
     * @param config ZeroTier 配置
     * @return 操作结果
     */
    @PostMapping("/tunnels/zerotier")
    public ResponseEntity<Map<String, Object>> configureZeroTier(@RequestBody Map<String, Object> config) {
        log.info("Configuring ZeroTier");
        Map<String, Object> result = networkService.configureZeroTier(config);
        return ResponseEntity.ok(wrapResponse(result));
    }

    /**
     * 配置 Tailscale 网络
     *
     * @param config Tailscale 配置
     * @return 操作结果
     */
    @PostMapping("/tunnels/tailscale")
    public ResponseEntity<Map<String, Object>> configureTailscale(@RequestBody Map<String, Object> config) {
        log.info("Configuring Tailscale");
        Map<String, Object> result = networkService.configureTailscale(config);
        return ResponseEntity.ok(wrapResponse(result));
    }

    /**
     * 配置 WireGuard VPN
     *
     * @param config WireGuard 配置
     * @return 操作结果
     */
    @PostMapping("/tunnels/wireguard")
    public ResponseEntity<Map<String, Object>> configureWireGuard(@RequestBody Map<String, Object> config) {
        log.info("Configuring WireGuard");
        Map<String, Object> result = networkService.configureWireGuard(config);
        return ResponseEntity.ok(wrapResponse(result));
    }

    /**
     * 获取隧道连接状态
     *
     * @return 隧道状态列表
     */
    @GetMapping("/tunnels/status")
    public ResponseEntity<Map<String, Object>> getTunnelStatus() {
        log.info("Getting tunnel status");
        Map<String, Object> result = networkService.getTunnelStatus();
        return ResponseEntity.ok(wrapResponse(result));
    }

    /**
     * 启动/停止隧道
     *
     * @param tunnelId 隧道ID
     * @param action   操作 (start/stop)
     * @return 操作结果
     */
    @PostMapping("/tunnels/{tunnelId}/control")
    public ResponseEntity<Map<String, Object>> controlTunnel(
            @PathVariable String tunnelId,
            @RequestParam String action) {
        log.info("Controlling tunnel: {} -> {}", tunnelId, action);
        Map<String, Object> result = networkService.controlTunnel(tunnelId, action);
        return ResponseEntity.ok(wrapResponse(result));
    }

    // ==================== DDNS 管理 ====================

    /**
     * 获取 DDNS 配置
     *
     * @return DDNS 配置列表
     */
    @GetMapping("/ddns")
    public ResponseEntity<Map<String, Object>> getDDNSConfigs() {
        log.info("Getting DDNS configs");
        Map<String, Object> result = networkService.getDDNSConfigs();
        return ResponseEntity.ok(wrapResponse(result));
    }

    /**
     * 添加 DDNS 配置
     *
     * @param config DDNS 配置
     * @return 操作结果
     */
    @PostMapping("/ddns")
    public ResponseEntity<Map<String, Object>> addDDNSConfig(@RequestBody Map<String, Object> config) {
        log.info("Adding DDNS config");
        Map<String, Object> result = networkService.addDDNSConfig(config);
        return ResponseEntity.ok(wrapResponse(result));
    }

    /**
     * 更新 DDNS 配置
     *
     * @param configId 配置ID
     * @param config   配置数据
     * @return 操作结果
     */
    @PutMapping("/ddns/{configId}")
    public ResponseEntity<Map<String, Object>> updateDDNSConfig(
            @PathVariable String configId,
            @RequestBody Map<String, Object> config) {
        log.info("Updating DDNS config: {}", configId);
        Map<String, Object> result = networkService.updateDDNSConfig(configId, config);
        return ResponseEntity.ok(wrapResponse(result));
    }

    /**
     * 删除 DDNS 配置
     *
     * @param configId 配置ID
     * @return 操作结果
     */
    @DeleteMapping("/ddns/{configId}")
    public ResponseEntity<Map<String, Object>> deleteDDNSConfig(@PathVariable String configId) {
        log.info("Deleting DDNS config: {}", configId);
        Map<String, Object> result = networkService.deleteDDNSConfig(configId);
        return ResponseEntity.ok(wrapResponse(result));
    }

    /**
     * 强制更新 DDNS
     *
     * @param configId 配置ID
     * @return 操作结果
     */
    @PostMapping("/ddns/{configId}/force-update")
    public ResponseEntity<Map<String, Object>> forceUpdateDDNS(@PathVariable String configId) {
        log.info("Force updating DDNS: {}", configId);
        Map<String, Object> result = networkService.forceUpdateDDNS(configId);
        return ResponseEntity.ok(wrapResponse(result));
    }

    // ==================== QoS 和流量控制 ====================

    /**
     * 获取 QoS 配置
     *
     * @return QoS 配置
     */
    @GetMapping("/qos")
    public ResponseEntity<Map<String, Object>> getQoSConfig() {
        log.info("Getting QoS config");
        Map<String, Object> result = networkService.getQoSConfig();
        return ResponseEntity.ok(wrapResponse(result));
    }

    /**
     * 配置 QoS
     *
     * @param config QoS 配置
     * @return 操作结果
     */
    @PutMapping("/qos")
    public ResponseEntity<Map<String, Object>> configureQoS(@RequestBody Map<String, Object> config) {
        log.info("Configuring QoS");
        Map<String, Object> result = networkService.configureQoS(config);
        return ResponseEntity.ok(wrapResponse(result));
    }

    /**
     * 获取实时流量统计
     *
     * @return 流量统计数据
     */
    @GetMapping("/traffic/realtime")
    public ResponseEntity<Map<String, Object>> getRealtimeTrafficStats() {
        log.info("Getting realtime traffic stats");
        Map<String, Object> result = networkService.getRealtimeTrafficStats();
        return ResponseEntity.ok(wrapResponse(result));
    }

    /**
     * 获取设备流量统计
     *
     * @return 设备流量统计
     */
    @GetMapping("/traffic/devices")
    public ResponseEntity<Map<String, Object>> getDeviceTrafficStats() {
        log.info("Getting device traffic stats");
        Map<String, Object> result = networkService.getDeviceTrafficStats();
        return ResponseEntity.ok(wrapResponse(result));
    }

    // ==================== OoderAgent 软硬一体化协议 ====================

    /**
     * 注册 OoderAgent
     *
     * @param agentInfo Agent 信息
     * @return 注册结果
     */
    @PostMapping("/agents/register")
    public ResponseEntity<Map<String, Object>> registerOoderAgent(@RequestBody Map<String, Object> agentInfo) {
        log.info("Registering OoderAgent");
        Map<String, Object> result = networkService.registerOoderAgent(agentInfo);
        return ResponseEntity.ok(wrapResponse(result));
    }

    /**
     * 获取已注册的 OoderAgent 列表
     *
     * @return Agent 列表
     */
    @GetMapping("/agents")
    public ResponseEntity<Map<String, Object>> getRegisteredOoderAgents() {
        log.info("Getting registered OoderAgents");
        Map<String, Object> result = networkService.getRegisteredOoderAgents();
        return ResponseEntity.ok(wrapResponse(result));
    }

    /**
     * 为 Agent 自动配置端口映射
     *
     * @param agentId  Agent ID
     * @param services 服务列表
     * @return 配置结果
     */
    @PostMapping("/agents/{agentId}/auto-configure-ports")
    public ResponseEntity<Map<String, Object>> autoConfigureAgentPorts(
            @PathVariable String agentId,
            @RequestBody List<Map<String, Object>> services) {
        log.info("Auto configuring ports for agent: {}", agentId);
        Map<String, Object> result = networkService.autoConfigureAgentPorts(agentId, services);
        return ResponseEntity.ok(wrapResponse(result));
    }

    /**
     * 获取 Agent 网络诊断信息
     *
     * @param agentId Agent ID
     * @return 诊断信息
     */
    @GetMapping("/agents/{agentId}/diagnostics")
    public ResponseEntity<Map<String, Object>> getAgentNetworkDiagnostics(@PathVariable String agentId) {
        log.info("Getting network diagnostics for agent: {}", agentId);
        Map<String, Object> result = networkService.getAgentNetworkDiagnostics(agentId);
        return ResponseEntity.ok(wrapResponse(result));
    }

    /**
     * 同步 Agent 网络配置
     *
     * @param agentId Agent ID
     * @return 同步结果
     */
    @PostMapping("/agents/{agentId}/sync")
    public ResponseEntity<Map<String, Object>> syncAgentNetworkConfig(@PathVariable String agentId) {
        log.info("Syncing network config for agent: {}", agentId);
        Map<String, Object> result = networkService.syncAgentNetworkConfig(agentId);
        return ResponseEntity.ok(wrapResponse(result));
    }

    // ==================== 网络诊断工具 ====================

    /**
     * 测试端口连通性
     *
     * @param externalPort 外部端口
     * @param protocol     协议
     * @return 测试结果
     */
    @GetMapping("/diagnostics/port-test")
    public ResponseEntity<Map<String, Object>> testPortConnectivity(
            @RequestParam int externalPort,
            @RequestParam(defaultValue = "tcp") String protocol) {
        log.info("Testing port connectivity: {}/{}", externalPort, protocol);
        Map<String, Object> result = networkService.testPortConnectivity(externalPort, protocol);
        return ResponseEntity.ok(wrapResponse(result));
    }

    /**
     * 获取公网 IP 信息
     *
     * @return 公网 IP 信息
     */
    @GetMapping("/diagnostics/public-ip")
    public ResponseEntity<Map<String, Object>> getPublicIPInfo() {
        log.info("Getting public IP info");
        Map<String, Object> result = networkService.getPublicIPInfo();
        return ResponseEntity.ok(wrapResponse(result));
    }

    /**
     * 执行 traceroute
     *
     * @param target 目标地址
     * @return traceroute 结果
     */
    @GetMapping("/diagnostics/traceroute")
    public ResponseEntity<Map<String, Object>> traceroute(@RequestParam String target) {
        log.info("Executing traceroute to: {}", target);
        Map<String, Object> result = networkService.traceroute(target);
        return ResponseEntity.ok(wrapResponse(result));
    }

    /**
     * 获取 NAT 类型
     *
     * @return NAT 类型信息
     */
    @GetMapping("/diagnostics/nat-type")
    public ResponseEntity<Map<String, Object>> getNATType() {
        log.info("Getting NAT type");
        Map<String, Object> result = networkService.getNATType();
        return ResponseEntity.ok(wrapResponse(result));
    }

    // ==================== 辅助方法 ====================

    /**
     * 包装响应数据
     *
     * @param data 原始数据
     * @return 包装后的响应
     */
    private Map<String, Object> wrapResponse(Map<String, Object> data) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "success");
        response.put("data", data);
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }
}
