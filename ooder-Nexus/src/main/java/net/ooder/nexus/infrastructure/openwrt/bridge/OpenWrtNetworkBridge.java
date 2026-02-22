package net.ooder.nexus.infrastructure.openwrt.bridge;

import java.util.List;
import java.util.Map;

/**
 * OpenWrt 网络高级管理桥接接口
 * 
 * <p>提供 OpenWrt 路由器的深度网络管理功能：</p>
 * <ul>
 *   <li>端口映射管理（Port Forwarding）</li>
 *   <li>UPnP/NAT-PMP 管理</li>
 *   <li>防火墙规则管理</li>
 *   <li>内网穿透配置</li>
 *   <li>DDNS 动态域名管理</li>
 *   <li>VPN 隧道管理</li>
 *   <li>流量控制和 QoS</li>
 * </ul>
 * 
 * <p><strong>软硬一体化设计：</strong></p>
 * <p>该接口不仅提供传统的打洞方案，更重要的是实现与 OpenWrt 硬件的深度集成，
 * 通过直接操作路由器底层配置，实现更高效、更稳定的网络连接管理。</p>
 * 
 * @author ooder Team
 * @version 2.0.0
 * @since 2.0.0
 */
public interface OpenWrtNetworkBridge {

    // ==================== 端口映射管理 ====================

    /**
     * 获取所有端口映射规则
     * 
     * @return 端口映射规则列表
     */
    Map<String, Object> getPortForwardingRules();

    /**
     * 添加端口映射规则
     * 
     * <p>支持多种映射类型：</p>
     * <ul>
     *   <li>TCP 端口映射</li>
     *   <li>UDP 端口映射</li>
     *   <li>TCP/UDP 同时映射</li>
     *   <li>端口范围映射</li>
     * </ul>
     *
     * @param ruleData 规则数据，包含：
     *                 - name: 规则名称
     *                 - proto: 协议类型 (tcp/udp/tcpudp)
     *                 - src_dport: 外部端口
     *                 - dest_ip: 内部IP地址
     *                 - dest_port: 内部端口
     *                 - src: 源区域 (默认 wan)
     *                 - dest: 目标区域 (默认 lan)
     * @return 操作结果
     */
    Map<String, Object> addPortForwardingRule(Map<String, Object> ruleData);

    /**
     * 更新端口映射规则
     *
     * @param ruleId   规则ID
     * @param ruleData 规则数据
     * @return 操作结果
     */
    Map<String, Object> updatePortForwardingRule(String ruleId, Map<String, Object> ruleData);

    /**
     * 删除端口映射规则
     *
     * @param ruleId 规则ID
     * @return 操作结果
     */
    Map<String, Object> deletePortForwardingRule(String ruleId);

    /**
     * 批量添加端口映射规则
     *
     * @param rulesData 规则数据列表
     * @return 操作结果
     */
    Map<String, Object> batchAddPortForwardingRules(List<Map<String, Object>> rulesData);

    /**
     * 启用/禁用端口映射规则
     *
     * @param ruleId  规则ID
     * @param enabled true 启用，false 禁用
     * @return 操作结果
     */
    Map<String, Object> togglePortForwardingRule(String ruleId, boolean enabled);

    /**
     * 验证端口映射规则
     * 
     * <p>检查规则配置是否正确，端口是否冲突</p>
     *
     * @param ruleData 规则数据
     * @return 验证结果
     */
    Map<String, Object> validatePortForwardingRule(Map<String, Object> ruleData);

    // ==================== UPnP/NAT-PMP 管理 ====================

    /**
     * 获取 UPnP 状态
     * 
     * @return UPnP 服务状态和配置
     */
    Map<String, Object> getUPnPStatus();

    /**
     * 启用/禁用 UPnP 服务
     *
     * @param enabled true 启用，false 禁用
     * @return 操作结果
     */
    Map<String, Object> setUPnPEnabled(boolean enabled);

    /**
     * 获取 UPnP 端口映射列表
     * 
     * <p>返回通过 UPnP 协议动态创建的端口映射</p>
     *
     * @return UPnP 端口映射列表
     */
    Map<String, Object> getUPnPPortMappings();

    /**
     * 删除 UPnP 端口映射
     *
     * @param mappingId 映射ID
     * @return 操作结果
     */
    Map<String, Object> deleteUPnPPortMapping(String mappingId);

    /**
     * 配置 UPnP 安全规则
     * 
     * <p>设置 UPnP 访问控制，限制哪些设备可以使用 UPnP</p>
     *
     * @param securityConfig 安全配置
     * @return 操作结果
     */
    Map<String, Object> configureUPnPSecurity(Map<String, Object> securityConfig);

    /**
     * 获取 NAT-PMP 状态
     * 
     * @return NAT-PMP 服务状态
     */
    Map<String, Object> getNATPMPStatus();

    /**
     * 启用/禁用 NAT-PMP 服务
     *
     * @param enabled true 启用，false 禁用
     * @return 操作结果
     */
    Map<String, Object> setNATPMPEnabled(boolean enabled);

    // ==================== 防火墙规则管理 ====================

    /**
     * 获取防火墙规则列表
     * 
     * @return 防火墙规则列表
     */
    Map<String, Object> getFirewallRules();

    /**
     * 添加防火墙规则
     * 
     * <p>支持自定义防火墙规则，用于精细的访问控制</p>
     *
     * @param ruleData 规则数据
     * @return 操作结果
     */
    Map<String, Object> addFirewallRule(Map<String, Object> ruleData);

    /**
     * 更新防火墙规则
     *
     * @param ruleId   规则ID
     * @param ruleData 规则数据
     * @return 操作结果
     */
    Map<String, Object> updateFirewallRule(String ruleId, Map<String, Object> ruleData);

    /**
     * 删除防火墙规则
     *
     * @param ruleId 规则ID
     * @return 操作结果
     */
    Map<String, Object> deleteFirewallRule(String ruleId);

    /**
     * 启用/禁用防火墙规则
     *
     * @param ruleId  规则ID
     * @param enabled true 启用，false 禁用
     * @return 操作结果
     */
    Map<String, Object> toggleFirewallRule(String ruleId, boolean enabled);

    // ==================== 内网穿透管理 ====================

    /**
     * 获取内网穿透配置
     * 
     * <p>支持多种内网穿透方案：</p>
     * <ul>
     *   <li>FRP (Fast Reverse Proxy)</li>
     *   <li>Ngrok</li>
     *   <li>ZeroTier</li>
     *   <li>Tailscale</li>
     *   <li>WireGuard</li>
     * </ul>
     *
     * @return 内网穿透配置
     */
    Map<String, Object> getTunnelConfigs();

    /**
     * 配置 FRP 客户端
     *
     * @param config FRP 配置
     * @return 操作结果
     */
    Map<String, Object> configureFRP(Map<String, Object> config);

    /**
     * 配置 ZeroTier 网络
     *
     * @param config ZeroTier 配置
     * @return 操作结果
     */
    Map<String, Object> configureZeroTier(Map<String, Object> config);

    /**
     * 配置 Tailscale 网络
     *
     * @param config Tailscale 配置
     * @return 操作结果
     */
    Map<String, Object> configureTailscale(Map<String, Object> config);

    /**
     * 配置 WireGuard VPN
     *
     * @param config WireGuard 配置
     * @return 操作结果
     */
    Map<String, Object> configureWireGuard(Map<String, Object> config);

    /**
     * 获取隧道连接状态
     *
     * @return 隧道状态列表
     */
    Map<String, Object> getTunnelStatus();

    /**
     * 启动/停止隧道
     *
     * @param tunnelId 隧道ID
     * @param action   "start" 或 "stop"
     * @return 操作结果
     */
    Map<String, Object> controlTunnel(String tunnelId, String action);

    // ==================== DDNS 动态域名管理 ====================

    /**
     * 获取 DDNS 配置
     * 
     * @return DDNS 配置列表
     */
    Map<String, Object> getDDNSConfigs();

    /**
     * 添加 DDNS 配置
     * 
     * <p>支持多种 DDNS 服务商：</p>
     * <ul>
     *   <li>阿里云 DNS</li>
     *   <li>腾讯云 DNSPod</li>
     *   <li>Cloudflare</li>
     *   <li>No-IP</li>
     *   <li>DynDNS</li>
     * </ul>
     *
     * @param config DDNS 配置
     * @return 操作结果
     */
    Map<String, Object> addDDNSConfig(Map<String, Object> config);

    /**
     * 更新 DDNS 配置
     *
     * @param configId 配置ID
     * @param config   配置数据
     * @return 操作结果
     */
    Map<String, Object> updateDDNSConfig(String configId, Map<String, Object> config);

    /**
     * 删除 DDNS 配置
     *
     * @param configId 配置ID
     * @return 操作结果
     */
    Map<String, Object> deleteDDNSConfig(String configId);

    /**
     * 强制更新 DDNS
     *
     * @param configId 配置ID
     * @return 操作结果
     */
    Map<String, Object> forceUpdateDDNS(String configId);

    // ==================== 流量控制和 QoS ====================

    /**
     * 获取 QoS 配置
     * 
     * @return QoS 配置
     */
    Map<String, Object> getQoSConfig();

    /**
     * 配置 QoS 规则
     * 
     * <p>支持基于设备、应用、端口的流量控制</p>
     *
     * @param config QoS 配置
     * @return 操作结果
     */
    Map<String, Object> configureQoS(Map<String, Object> config);

    /**
     * 获取实时流量统计
     * 
     * @return 流量统计数据
     */
    Map<String, Object> getRealtimeTrafficStats();

    /**
     * 获取设备流量统计
     * 
     * @return 各设备的流量使用情况
     */
    Map<String, Object> getDeviceTrafficStats();

    /**
     * 设置带宽限制
     *
     * @param deviceId 设备ID (MAC地址)
     * @param limit    带宽限制配置
     * @return 操作结果
     */
    Map<String, Object> setBandwidthLimit(String deviceId, Map<String, Object> limit);

    // ==================== OoderAgent 软硬一体化协议 ====================

    /**
     * 注册 OoderAgent 设备
     * 
     * <p>将 Nexus Agent 注册到路由器，实现软硬一体化管理</p>
     *
     * @param agentInfo Agent 信息
     * @return 注册结果
     */
    Map<String, Object> registerOoderAgent(Map<String, Object> agentInfo);

    /**
     * 获取已注册的 OoderAgent 列表
     * 
     * @return Agent 列表
     */
    Map<String, Object> getRegisteredOoderAgents();

    /**
     * 为 OoderAgent 自动配置端口映射
     * 
     * <p>根据 Agent 的服务需求，自动配置最优的端口映射方案：</p>
     * <ul>
     *   <li>优先尝试 UPnP 自动映射</li>
     *   <li>UPnP 失败时创建静态端口映射</li>
     *   <li>配置防火墙规则允许访问</li>
     * </ul>
     *
     * @param agentId   Agent ID
     * @param services  服务列表及端口需求
     * @return 配置结果
     */
    Map<String, Object> autoConfigureAgentPorts(String agentId, List<Map<String, Object>> services);

    /**
     * 获取 Agent 的网络诊断信息
     * 
     * <p>提供详细的网络连接诊断，帮助排查连接问题</p>
     *
     * @param agentId Agent ID
     * @return 诊断信息
     */
    Map<String, Object> getAgentNetworkDiagnostics(String agentId);

    /**
     * 配置 Agent 的专用网络通道
     * 
     * <p>为特定 Agent 创建专用的网络通道，确保服务质量</p>
     *
     * @param agentId Agent ID
     * @param config  通道配置
     * @return 配置结果
     */
    Map<String, Object> configureAgentDedicatedChannel(String agentId, Map<String, Object> config);

    /**
     * 同步 Agent 网络配置
     * 
     * <p>将路由器的网络配置同步到 Agent，确保网络策略一致性</p>
     *
     * @param agentId Agent ID
     * @return 同步结果
     */
    Map<String, Object> syncAgentNetworkConfig(String agentId);

    // ==================== 网络诊断工具 ====================

    /**
     * 执行端口连通性测试
     * 
     * <p>测试外部端口是否正确映射到内部服务</p>
     *
     * @param externalPort 外部端口
     * @param protocol     协议 (tcp/udp)
     * @return 测试结果
     */
    Map<String, Object> testPortConnectivity(int externalPort, String protocol);

    /**
     * 获取公网 IP 地址
     * 
     * @return 公网 IP 信息
     */
    Map<String, Object> getPublicIPInfo();

    /**
     * 执行 traceroute
     * 
     * @param target 目标地址
     * @return traceroute 结果
     */
    Map<String, Object> traceroute(String target);

    /**
     * 获取 NAT 类型
     * 
     * <p>检测当前网络的 NAT 类型（Full Cone、Restricted Cone 等）</p>
     *
     * @return NAT 类型信息
     */
    Map<String, Object> getNATType();
}
