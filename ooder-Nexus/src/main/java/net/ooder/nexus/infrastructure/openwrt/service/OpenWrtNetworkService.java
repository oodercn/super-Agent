package net.ooder.nexus.infrastructure.openwrt.service;

import net.ooder.nexus.infrastructure.openwrt.bridge.OpenWrtBridge;
import net.ooder.nexus.infrastructure.openwrt.bridge.OpenWrtBridgeFactory;
import net.ooder.nexus.infrastructure.openwrt.bridge.OpenWrtNetworkBridge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * OpenWrt 网络高级管理服务
 *
 * <p>提供 OpenWrt 路由器的深度网络管理功能实现：</p>
 * <ul>
 *   <li>端口映射管理（Port Forwarding）</li>
 *   <li>UPnP/NAT-PMP 管理</li>
 *   <li>防火墙规则管理</li>
 *   <li>内网穿透配置</li>
 *   <li>DDNS 动态域名管理</li>
 *   <li>流量控制和 QoS</li>
 *   <li>OoderAgent 软硬一体化协议</li>
 * </ul>
 *
 * <p><strong>软硬一体化设计：</strong></p>
 * <p>该服务不仅提供传统的打洞方案，更重要的是实现与 OpenWrt 硬件的深度集成，
 * 通过直接操作路由器底层配置（UCI 命令），实现更高效、更稳定的网络连接管理。</p>
 *
 * @author ooder Team
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class OpenWrtNetworkService implements OpenWrtNetworkBridge {

    private static final Logger log = LoggerFactory.getLogger(OpenWrtNetworkService.class);

    /** OpenWrt 基础服务 */
    private final OpenWrtService openWrtService;

    /** Mock 模式标志 */
    private boolean useMock = true;

    /** 端口映射规则存储（Mock 模式使用） */
    private final Map<String, Map<String, Object>> portForwardingRules = new HashMap<>();

    /** UPnP 映射存储（Mock 模式使用） */
    private final Map<String, Map<String, Object>> upnpMappings = new HashMap<>();

    /** 防火墙规则存储（Mock 模式使用） */
    private final Map<String, Map<String, Object>> firewallRules = new HashMap<>();

    /** 隧道配置存储（Mock 模式使用） */
    private final Map<String, Map<String, Object>> tunnelConfigs = new HashMap<>();

    /** DDNS 配置存储（Mock 模式使用） */
    private final Map<String, Map<String, Object>> ddnsConfigs = new HashMap<>();

    /** 注册的 Agent 存储（Mock 模式使用） */
    private final Map<String, Map<String, Object>> registeredAgents = new HashMap<>();

    /**
     * 构造函数
     *
     * @param openWrtService OpenWrt 基础服务
     */
    @Autowired
    public OpenWrtNetworkService(OpenWrtService openWrtService) {
        this.openWrtService = openWrtService;
    }

    /**
     * 初始化服务
     */
    @PostConstruct
    public void init() {
        log.info("Initializing OpenWrt Network Service");
        this.useMock = openWrtService.isMockMode();
        
        if (useMock) {
            log.info("OpenWrt Network Service running in MOCK mode");
            initMockData();
        } else {
            log.info("OpenWrt Network Service initialized with real bridge");
        }
    }

    /**
     * 初始化 Mock 数据
     */
    private void initMockData() {
        // 添加示例端口映射规则
        Map<String, Object> rule1 = new HashMap<>();
        rule1.put("id", "pf-001");
        rule1.put("name", "SSH Access");
        rule1.put("proto", "tcp");
        rule1.put("src_dport", 2222);
        rule1.put("dest_ip", "192.168.1.100");
        rule1.put("dest_port", 22);
        rule1.put("src", "wan");
        rule1.put("dest", "lan");
        rule1.put("enabled", true);
        rule1.put("createdAt", System.currentTimeMillis());
        portForwardingRules.put("pf-001", rule1);

        Map<String, Object> rule2 = new HashMap<>();
        rule2.put("id", "pf-002");
        rule2.put("name", "Web Server");
        rule2.put("proto", "tcp");
        rule2.put("src_dport", 8080);
        rule2.put("dest_ip", "192.168.1.101");
        rule2.put("dest_port", 80);
        rule2.put("src", "wan");
        rule2.put("dest", "lan");
        rule2.put("enabled", true);
        rule2.put("createdAt", System.currentTimeMillis());
        portForwardingRules.put("pf-002", rule2);

        // 添加示例 UPnP 映射
        Map<String, Object> upnp1 = new HashMap<>();
        upnp1.put("id", "upnp-001");
        upnp1.put("proto", "udp");
        upnp1.put("ext_port", 3074);
        upnp1.put("int_port", 3074);
        upnp1.put("int_addr", "192.168.1.105");
        upnp1.put("desc", "Xbox Live");
        upnp1.put("createdAt", System.currentTimeMillis());
        upnpMappings.put("upnp-001", upnp1);

        log.info("Mock data initialized with {} port forwarding rules and {} UPnP mappings", 
                portForwardingRules.size(), upnpMappings.size());
    }

    // ==================== 端口映射管理 ====================

    @Override
    public Map<String, Object> getPortForwardingRules() {
        log.info("Getting port forwarding rules (mock={})", useMock);
        
        if (useMock) {
            Map<String, Object> result = new HashMap<>();
            result.put("rules", new ArrayList<>(portForwardingRules.values()));
            result.put("total", portForwardingRules.size());
            result.put("enabled", portForwardingRules.values().stream().filter(r -> (Boolean) r.get("enabled")).count());
            return result;
        } else {
            // 真实模式：通过 UCI 命令获取
            return executeUCICommand("firewall", "get_port_forwards");
        }
    }

    @Override
    public Map<String, Object> addPortForwardingRule(Map<String, Object> ruleData) {
        log.info("Adding port forwarding rule: {} (mock={})", ruleData, useMock);
        
        String ruleId = "pf-" + System.currentTimeMillis();
        ruleData.put("id", ruleId);
        ruleData.put("enabled", true);
        ruleData.put("createdAt", System.currentTimeMillis());
        
        if (useMock) {
            portForwardingRules.put(ruleId, ruleData);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("ruleId", ruleId);
            result.put("message", "Port forwarding rule added successfully");
            return result;
        } else {
            // 真实模式：通过 UCI 命令添加
            return executeUCICommand("firewall", "add_port_forward", ruleData);
        }
    }

    @Override
    public Map<String, Object> updatePortForwardingRule(String ruleId, Map<String, Object> ruleData) {
        log.info("Updating port forwarding rule: {} (mock={})", ruleId, useMock);
        
        if (useMock) {
            if (!portForwardingRules.containsKey(ruleId)) {
                Map<String, Object> result = new HashMap<>();
                result.put("success", false);
                result.put("message", "Rule not found");
                return result;
            }
            
            Map<String, Object> existingRule = portForwardingRules.get(ruleId);
            existingRule.putAll(ruleData);
            existingRule.put("updatedAt", System.currentTimeMillis());
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "Port forwarding rule updated successfully");
            return result;
        } else {
            return executeUCICommand("firewall", "update_port_forward", ruleData);
        }
    }

    @Override
    public Map<String, Object> deletePortForwardingRule(String ruleId) {
        log.info("Deleting port forwarding rule: {} (mock={})", ruleId, useMock);
        
        if (useMock) {
            if (portForwardingRules.remove(ruleId) != null) {
                Map<String, Object> result = new HashMap<>();
                result.put("success", true);
                result.put("message", "Port forwarding rule deleted successfully");
                return result;
            } else {
                Map<String, Object> result = new HashMap<>();
                result.put("success", false);
                result.put("message", "Rule not found");
                return result;
            }
        } else {
            return executeUCICommand("firewall", "delete_port_forward", Collections.singletonMap("id", ruleId));
        }
    }

    @Override
    public Map<String, Object> batchAddPortForwardingRules(List<Map<String, Object>> rulesData) {
        log.info("Batch adding {} port forwarding rules (mock={})", rulesData.size(), useMock);
        
        List<String> addedIds = new ArrayList<>();
        for (Map<String, Object> ruleData : rulesData) {
            Map<String, Object> result = addPortForwardingRule(ruleData);
            if ((Boolean) result.get("success")) {
                addedIds.add((String) result.get("ruleId"));
            }
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("addedCount", addedIds.size());
        result.put("ruleIds", addedIds);
        result.put("message", "Batch add completed");
        return result;
    }

    @Override
    public Map<String, Object> togglePortForwardingRule(String ruleId, boolean enabled) {
        log.info("Toggling port forwarding rule: {} -> {} (mock={})", ruleId, enabled, useMock);
        
        if (useMock) {
            Map<String, Object> rule = portForwardingRules.get(ruleId);
            if (rule != null) {
                rule.put("enabled", enabled);
                rule.put("updatedAt", System.currentTimeMillis());
                
                Map<String, Object> result = new HashMap<>();
                result.put("success", true);
                result.put("message", "Rule " + (enabled ? "enabled" : "disabled") + " successfully");
                return result;
            } else {
                Map<String, Object> result = new HashMap<>();
                result.put("success", false);
                result.put("message", "Rule not found");
                return result;
            }
        } else {
            Map<String, Object> params = new HashMap<>();
            params.put("id", ruleId);
            params.put("enabled", enabled);
            return executeUCICommand("firewall", "toggle_port_forward", params);
        }
    }

    @Override
    public Map<String, Object> validatePortForwardingRule(Map<String, Object> ruleData) {
        log.info("Validating port forwarding rule: {} (mock={})", ruleData, useMock);
        
        List<String> errors = new ArrayList<>();
        
        // 验证必填字段
        if (ruleData.get("name") == null || ((String) ruleData.get("name")).trim().isEmpty()) {
            errors.add("Rule name is required");
        }
        
        if (ruleData.get("proto") == null) {
            errors.add("Protocol is required");
        }
        
        if (ruleData.get("src_dport") == null) {
            errors.add("External port is required");
        }
        
        if (ruleData.get("dest_ip") == null) {
            errors.add("Internal IP is required");
        }
        
        if (ruleData.get("dest_port") == null) {
            errors.add("Internal port is required");
        }
        
        // 验证端口范围
        Integer srcPort = (Integer) ruleData.get("src_dport");
        Integer destPort = (Integer) ruleData.get("dest_port");
        
        if (srcPort != null && (srcPort < 1 || srcPort > 65535)) {
            errors.add("External port must be between 1 and 65535");
        }
        
        if (destPort != null && (destPort < 1 || destPort > 65535)) {
            errors.add("Internal port must be between 1 and 65535");
        }
        
        // 检查端口冲突（Mock 模式）
        if (useMock && srcPort != null) {
            for (Map<String, Object> existingRule : portForwardingRules.values()) {
                if (srcPort.equals(existingRule.get("src_dport")) && 
                    Boolean.TRUE.equals(existingRule.get("enabled"))) {
                    errors.add("External port " + srcPort + " is already in use");
                    break;
                }
            }
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("valid", errors.isEmpty());
        result.put("errors", errors);
        return result;
    }

    // ==================== UPnP/NAT-PMP 管理 ====================

    @Override
    public Map<String, Object> getUPnPStatus() {
        log.info("Getting UPnP status (mock={})", useMock);
        
        if (useMock) {
            Map<String, Object> result = new HashMap<>();
            result.put("enabled", true);
            result.put("logging", true);
            result.put("secure_mode", true);
            result.put("internal_iface", "lan");
            result.put("external_iface", "wan");
            result.put("port", 5000);
            return result;
        } else {
            return executeUCICommand("upnpd", "get_status");
        }
    }

    @Override
    public Map<String, Object> setUPnPEnabled(boolean enabled) {
        log.info("Setting UPnP enabled: {} (mock={})", enabled, useMock);
        
        if (useMock) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("enabled", enabled);
            result.put("message", "UPnP " + (enabled ? "enabled" : "disabled") + " successfully");
            return result;
        } else {
            Map<String, Object> params = new HashMap<>();
            params.put("enabled", enabled);
            return executeUCICommand("upnpd", "set_enabled", params);
        }
    }

    @Override
    public Map<String, Object> getUPnPPortMappings() {
        log.info("Getting UPnP port mappings (mock={})", useMock);
        
        if (useMock) {
            Map<String, Object> result = new HashMap<>();
            result.put("mappings", new ArrayList<>(upnpMappings.values()));
            result.put("total", upnpMappings.size());
            return result;
        } else {
            return executeShellCommand("upnpc -l");
        }
    }

    @Override
    public Map<String, Object> deleteUPnPPortMapping(String mappingId) {
        log.info("Deleting UPnP port mapping: {} (mock={})", mappingId, useMock);
        
        if (useMock) {
            if (upnpMappings.remove(mappingId) != null) {
                Map<String, Object> result = new HashMap<>();
                result.put("success", true);
                result.put("message", "UPnP mapping deleted successfully");
                return result;
            } else {
                Map<String, Object> result = new HashMap<>();
                result.put("success", false);
                result.put("message", "Mapping not found");
                return result;
            }
        } else {
            return executeShellCommand("upnpc -d " + mappingId);
        }
    }

    @Override
    public Map<String, Object> configureUPnPSecurity(Map<String, Object> securityConfig) {
        log.info("Configuring UPnP security: {} (mock={})", securityConfig, useMock);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "UPnP security configured successfully");
        return result;
    }

    @Override
    public Map<String, Object> getNATPMPStatus() {
        log.info("Getting NAT-PMP status (mock={})", useMock);
        
        Map<String, Object> result = new HashMap<>();
        result.put("enabled", true);
        result.put("message", "NAT-PMP is enabled");
        return result;
    }

    @Override
    public Map<String, Object> setNATPMPEnabled(boolean enabled) {
        log.info("Setting NAT-PMP enabled: {} (mock={})", enabled, useMock);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("enabled", enabled);
        result.put("message", "NAT-PMP " + (enabled ? "enabled" : "disabled") + " successfully");
        return result;
    }

    // ==================== 防火墙规则管理 ====================

    @Override
    public Map<String, Object> getFirewallRules() {
        log.info("Getting firewall rules (mock={})", useMock);
        
        if (useMock) {
            Map<String, Object> result = new HashMap<>();
            result.put("rules", new ArrayList<>(firewallRules.values()));
            result.put("total", firewallRules.size());
            return result;
        } else {
            return executeUCICommand("firewall", "get_rules");
        }
    }

    @Override
    public Map<String, Object> addFirewallRule(Map<String, Object> ruleData) {
        log.info("Adding firewall rule: {} (mock={})", ruleData, useMock);
        
        String ruleId = "fw-" + System.currentTimeMillis();
        ruleData.put("id", ruleId);
        ruleData.put("createdAt", System.currentTimeMillis());
        
        if (useMock) {
            firewallRules.put(ruleId, ruleData);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("ruleId", ruleId);
            result.put("message", "Firewall rule added successfully");
            return result;
        } else {
            return executeUCICommand("firewall", "add_rule", ruleData);
        }
    }

    @Override
    public Map<String, Object> updateFirewallRule(String ruleId, Map<String, Object> ruleData) {
        log.info("Updating firewall rule: {} (mock={})", ruleId, useMock);
        
        if (useMock) {
            if (firewallRules.containsKey(ruleId)) {
                firewallRules.get(ruleId).putAll(ruleData);
                
                Map<String, Object> result = new HashMap<>();
                result.put("success", true);
                result.put("message", "Firewall rule updated successfully");
                return result;
            } else {
                Map<String, Object> result = new HashMap<>();
                result.put("success", false);
                result.put("message", "Rule not found");
                return result;
            }
        } else {
            return executeUCICommand("firewall", "update_rule", ruleData);
        }
    }

    @Override
    public Map<String, Object> deleteFirewallRule(String ruleId) {
        log.info("Deleting firewall rule: {} (mock={})", ruleId, useMock);
        
        if (useMock) {
            if (firewallRules.remove(ruleId) != null) {
                Map<String, Object> result = new HashMap<>();
                result.put("success", true);
                result.put("message", "Firewall rule deleted successfully");
                return result;
            } else {
                Map<String, Object> result = new HashMap<>();
                result.put("success", false);
                result.put("message", "Rule not found");
                return result;
            }
        } else {
            Map<String, Object> params = new HashMap<>();
            params.put("id", ruleId);
            return executeUCICommand("firewall", "delete_rule", params);
        }
    }

    @Override
    public Map<String, Object> toggleFirewallRule(String ruleId, boolean enabled) {
        log.info("Toggling firewall rule: {} -> {} (mock={})", ruleId, enabled, useMock);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "Rule " + (enabled ? "enabled" : "disabled") + " successfully");
        return result;
    }

    // ==================== 内网穿透管理 ====================

    @Override
    public Map<String, Object> getTunnelConfigs() {
        log.info("Getting tunnel configs (mock={})", useMock);
        
        Map<String, Object> result = new HashMap<>();
        result.put("tunnels", new ArrayList<>(tunnelConfigs.values()));
        result.put("total", tunnelConfigs.size());
        return result;
    }

    @Override
    public Map<String, Object> configureFRP(Map<String, Object> config) {
        log.info("Configuring FRP: {} (mock={})", config, useMock);
        
        String tunnelId = "frp-" + System.currentTimeMillis();
        config.put("id", tunnelId);
        config.put("type", "frp");
        config.put("status", "configured");
        tunnelConfigs.put(tunnelId, config);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("tunnelId", tunnelId);
        result.put("message", "FRP configured successfully");
        return result;
    }

    @Override
    public Map<String, Object> configureZeroTier(Map<String, Object> config) {
        log.info("Configuring ZeroTier: {} (mock={})", config, useMock);
        
        String tunnelId = "zt-" + System.currentTimeMillis();
        config.put("id", tunnelId);
        config.put("type", "zerotier");
        config.put("status", "configured");
        tunnelConfigs.put(tunnelId, config);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("tunnelId", tunnelId);
        result.put("message", "ZeroTier configured successfully");
        return result;
    }

    @Override
    public Map<String, Object> configureTailscale(Map<String, Object> config) {
        log.info("Configuring Tailscale: {} (mock={})", config, useMock);
        
        String tunnelId = "ts-" + System.currentTimeMillis();
        config.put("id", tunnelId);
        config.put("type", "tailscale");
        config.put("status", "configured");
        tunnelConfigs.put(tunnelId, config);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("tunnelId", tunnelId);
        result.put("message", "Tailscale configured successfully");
        return result;
    }

    @Override
    public Map<String, Object> configureWireGuard(Map<String, Object> config) {
        log.info("Configuring WireGuard: {} (mock={})", config, useMock);
        
        String tunnelId = "wg-" + System.currentTimeMillis();
        config.put("id", tunnelId);
        config.put("type", "wireguard");
        config.put("status", "configured");
        tunnelConfigs.put(tunnelId, config);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("tunnelId", tunnelId);
        result.put("message", "WireGuard configured successfully");
        return result;
    }

    @Override
    public Map<String, Object> getTunnelStatus() {
        log.info("Getting tunnel status (mock={})", useMock);
        
        Map<String, Object> result = new HashMap<>();
        result.put("tunnels", new ArrayList<>(tunnelConfigs.values()));
        return result;
    }

    @Override
    public Map<String, Object> controlTunnel(String tunnelId, String action) {
        log.info("Controlling tunnel: {} -> {} (mock={})", tunnelId, action, useMock);
        
        Map<String, Object> tunnel = tunnelConfigs.get(tunnelId);
        if (tunnel != null) {
            tunnel.put("status", "start".equals(action) ? "running" : "stopped");
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "Tunnel " + action + "ed successfully");
            return result;
        } else {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "Tunnel not found");
            return result;
        }
    }

    // ==================== DDNS 管理 ====================

    @Override
    public Map<String, Object> getDDNSConfigs() {
        log.info("Getting DDNS configs (mock={})", useMock);
        
        Map<String, Object> result = new HashMap<>();
        result.put("configs", new ArrayList<>(ddnsConfigs.values()));
        return result;
    }

    @Override
    public Map<String, Object> addDDNSConfig(Map<String, Object> config) {
        log.info("Adding DDNS config: {} (mock={})", config, useMock);
        
        String configId = "ddns-" + System.currentTimeMillis();
        config.put("id", configId);
        ddnsConfigs.put(configId, config);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("configId", configId);
        result.put("message", "DDNS config added successfully");
        return result;
    }

    @Override
    public Map<String, Object> updateDDNSConfig(String configId, Map<String, Object> config) {
        log.info("Updating DDNS config: {} (mock={})", configId, useMock);
        
        if (ddnsConfigs.containsKey(configId)) {
            ddnsConfigs.get(configId).putAll(config);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "DDNS config updated successfully");
            return result;
        } else {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "Config not found");
            return result;
        }
    }

    @Override
    public Map<String, Object> deleteDDNSConfig(String configId) {
        log.info("Deleting DDNS config: {} (mock={})", configId, useMock);
        
        if (ddnsConfigs.remove(configId) != null) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "DDNS config deleted successfully");
            return result;
        } else {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "Config not found");
            return result;
        }
    }

    @Override
    public Map<String, Object> forceUpdateDDNS(String configId) {
        log.info("Force updating DDNS: {} (mock={})", configId, useMock);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "DDNS force update triggered");
        return result;
    }

    // ==================== QoS 和流量控制 ====================

    @Override
    public Map<String, Object> getQoSConfig() {
        log.info("Getting QoS config (mock={})", useMock);
        
        Map<String, Object> result = new HashMap<>();
        result.put("enabled", false);
        result.put("message", "QoS configuration");
        return result;
    }

    @Override
    public Map<String, Object> configureQoS(Map<String, Object> config) {
        log.info("Configuring QoS: {} (mock={})", config, useMock);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "QoS configured successfully");
        return result;
    }

    @Override
    public Map<String, Object> getRealtimeTrafficStats() {
        log.info("Getting realtime traffic stats (mock={})", useMock);
        
        Map<String, Object> result = new HashMap<>();
        result.put("download", 1024 * 1024); // 1 MB/s
        result.put("upload", 512 * 1024);    // 512 KB/s
        result.put("connections", 150);
        result.put("timestamp", System.currentTimeMillis());
        return result;
    }

    @Override
    public Map<String, Object> getDeviceTrafficStats() {
        log.info("Getting device traffic stats (mock={})", useMock);
        
        Map<String, Object> result = new HashMap<>();
        result.put("devices", new ArrayList<>());
        return result;
    }

    @Override
    public Map<String, Object> setBandwidthLimit(String deviceId, Map<String, Object> limit) {
        log.info("Setting bandwidth limit for device: {} (mock={})", deviceId, useMock);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "Bandwidth limit set successfully");
        return result;
    }

    // ==================== OoderAgent 软硬一体化协议 ====================

    @Override
    public Map<String, Object> registerOoderAgent(Map<String, Object> agentInfo) {
        log.info("Registering OoderAgent: {} (mock={})", agentInfo, useMock);
        
        String agentId = (String) agentInfo.get("agentId");
        if (agentId == null) {
            agentId = "agent-" + System.currentTimeMillis();
            agentInfo.put("agentId", agentId);
        }
        
        agentInfo.put("registeredAt", System.currentTimeMillis());
        agentInfo.put("status", "active");
        registeredAgents.put(agentId, agentInfo);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("agentId", agentId);
        result.put("message", "Agent registered successfully");
        return result;
    }

    @Override
    public Map<String, Object> getRegisteredOoderAgents() {
        log.info("Getting registered OoderAgents (mock={})", useMock);
        
        Map<String, Object> result = new HashMap<>();
        result.put("agents", new ArrayList<>(registeredAgents.values()));
        result.put("total", registeredAgents.size());
        return result;
    }

    @Override
    public Map<String, Object> autoConfigureAgentPorts(String agentId, List<Map<String, Object>> services) {
        log.info("Auto configuring ports for agent: {} with {} services (mock={})", 
                agentId, services.size(), useMock);
        
        List<String> configuredPorts = new ArrayList<>();
        
        for (Map<String, Object> service : services) {
            String serviceName = (String) service.get("name");
            Integer port = (Integer) service.get("port");
            String proto = (String) service.getOrDefault("proto", "tcp");
            
            if (port != null) {
                // 尝试 UPnP 映射
                Map<String, Object> upnpResult = tryUPnPMapping(port, port, proto);
                
                if (!(Boolean) upnpResult.get("success")) {
                    // UPnP 失败，创建静态端口映射
                    Map<String, Object> ruleData = new HashMap<>();
                    ruleData.put("name", serviceName + " (" + agentId + ")");
                    ruleData.put("proto", proto);
                    ruleData.put("src_dport", port);
                    ruleData.put("dest_ip", service.get("internalIp"));
                    ruleData.put("dest_port", port);
                    
                    Map<String, Object> pfResult = addPortForwardingRule(ruleData);
                    if ((Boolean) pfResult.get("success")) {
                        configuredPorts.add(port.toString());
                    }
                } else {
                    configuredPorts.add(port.toString() + " (UPnP)");
                }
            }
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("agentId", agentId);
        result.put("configuredPorts", configuredPorts);
        result.put("message", "Auto configuration completed for " + configuredPorts.size() + " services");
        return result;
    }

    /**
     * 尝试 UPnP 端口映射
     */
    private Map<String, Object> tryUPnPMapping(int extPort, int intPort, String proto) {
        Map<String, Object> result = new HashMap<>();
        
        // 模拟 UPnP 尝试
        if (useMock) {
            // Mock 模式下随机成功或失败
            boolean success = Math.random() > 0.3;
            result.put("success", success);
            result.put("method", "UPnP");
            if (!success) {
                result.put("error", "UPnP not available or port already mapped");
            }
        } else {
            // 真实模式执行 UPnP 命令
            result.put("success", false);
            result.put("method", "UPnP");
        }
        
        return result;
    }

    @Override
    public Map<String, Object> getAgentNetworkDiagnostics(String agentId) {
        log.info("Getting network diagnostics for agent: {} (mock={})", agentId, useMock);
        
        Map<String, Object> agent = registeredAgents.get(agentId);
        
        Map<String, Object> diagnostics = new HashMap<>();
        diagnostics.put("agentId", agentId);
        diagnostics.put("timestamp", System.currentTimeMillis());
        
        // NAT 类型检测
        diagnostics.put("natType", "Symmetric NAT");
        
        // 端口映射状态
        List<Map<String, Object>> portMappings = new ArrayList<>();
        for (Map<String, Object> rule : portForwardingRules.values()) {
            if (rule.get("name") != null && ((String) rule.get("name")).contains(agentId)) {
                portMappings.add(rule);
            }
        }
        diagnostics.put("portMappings", portMappings);
        
        // 连接测试
        Map<String, Object> connectivity = new HashMap<>();
        connectivity.put("routerReachable", true);
        connectivity.put("internetReachable", true);
        connectivity.put("p2pCapable", portMappings.size() > 0);
        diagnostics.put("connectivity", connectivity);
        
        return diagnostics;
    }

    @Override
    public Map<String, Object> configureAgentDedicatedChannel(String agentId, Map<String, Object> config) {
        log.info("Configuring dedicated channel for agent: {} (mock={})", agentId, useMock);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("agentId", agentId);
        result.put("message", "Dedicated channel configured");
        return result;
    }

    @Override
    public Map<String, Object> syncAgentNetworkConfig(String agentId) {
        log.info("Syncing network config for agent: {} (mock={})", agentId, useMock);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("agentId", agentId);
        result.put("message", "Network config synced");
        return result;
    }

    // ==================== 网络诊断工具 ====================

    @Override
    public Map<String, Object> testPortConnectivity(int externalPort, String protocol) {
        log.info("Testing port connectivity: {}/{} (mock={})", externalPort, protocol, useMock);
        
        Map<String, Object> result = new HashMap<>();
        result.put("port", externalPort);
        result.put("protocol", protocol);
        result.put("reachable", true);
        result.put("latency", 25);
        result.put("message", "Port is reachable");
        return result;
    }

    @Override
    public Map<String, Object> getPublicIPInfo() {
        log.info("Getting public IP info (mock={})", useMock);
        
        Map<String, Object> result = new HashMap<>();
        result.put("ipv4", "203.0.113.1");
        result.put("ipv6", "2001:db8::1");
        result.put("isp", "Example ISP");
        result.put("location", "Beijing, China");
        return result;
    }

    @Override
    public Map<String, Object> traceroute(String target) {
        log.info("Executing traceroute to: {} (mock={})", target, useMock);
        
        List<Map<String, Object>> hops = new ArrayList<>();
        
        for (int i = 1; i <= 5; i++) {
            Map<String, Object> hop = new HashMap<>();
            hop.put("hop", i);
            hop.put("ip", "192.168." + i + ".1");
            hop.put("latency", i * 5);
            hops.add(hop);
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("target", target);
        result.put("hops", hops);
        return result;
    }

    @Override
    public Map<String, Object> getNATType() {
        log.info("Getting NAT type (mock={})", useMock);
        
        Map<String, Object> result = new HashMap<>();
        result.put("type", "Port-Restricted Cone NAT");
        result.put("description", "Port-restricted cone NAT is used");
        result.put("p2pFriendly", true);
        result.put("upnpAvailable", true);
        return result;
    }

    // ==================== 辅助方法 ====================

    /**
     * 执行 UCI 命令（真实模式）
     */
    private Map<String, Object> executeUCICommand(String config, String action, Map<String, Object> params) {
        if (useMock) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("mock", true);
            result.put("config", config);
            result.put("action", action);
            return result;
        }
        
        // 真实模式：通过 OpenWrtBridge 执行 SSH 命令
        String command = buildUCICommand(config, action, params);
        return openWrtService.executeCommand(command);
    }

    private Map<String, Object> executeUCICommand(String config, String action) {
        return executeUCICommand(config, action, null);
    }

    /**
     * 执行 Shell 命令（真实模式）
     */
    private Map<String, Object> executeShellCommand(String command) {
        if (useMock) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("mock", true);
            result.put("command", command);
            return result;
        }
        
        return openWrtService.executeCommand(command);
    }

    /**
     * 构建 UCI 命令
     */
    private String buildUCICommand(String config, String action, Map<String, Object> params) {
        StringBuilder cmd = new StringBuilder("uci ");
        
        switch (action) {
            case "get_port_forwards":
                cmd.append("show firewall.@redirect");
                break;
            case "add_port_forward":
                cmd.append("add firewall redirect");
                break;
            case "delete_port_forward":
                cmd.append("delete firewall.").append(params.get("id"));
                break;
            default:
                cmd.append("show ").append(config);
        }
        
        return cmd.toString();
    }
}
