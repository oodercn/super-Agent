package net.ooder.nexus.infrastructure.openwrt.p2p;

import java.util.List;
import java.util.Map;

/**
 * ooderAgent 0.7.0 OpenWrt P2P 通讯桥接接口
 *
 * <p>该接口定义了ooderAgent协议0.7.0中OpenWrt硬件与P2P通讯的集成规范，
 * 实现软硬一体化设计，支持UDP贯穿、硬防火墙直通、AI智能路由等核心能力。</p>
 *
 * <p><strong>核心功能：</strong></p>
 * <ul>
 *   <li>conntrack预建立 - 实现硬防火墙直通</li>
 *   <li>UDP端口池管理 - 支持AI预测打洞</li>
 *   <li>三层QoS控制 - 应用层/系统层/硬件层</li>
 *   <li>NAT类型检测 - 自动适配连接策略</li>
 *   <li>Agent注册管理 - 双中心协同认证</li>
 * </ul>
 *
 * <p><strong>协议版本：</strong>ooderAgent Protocol 0.7.0</p>
 *
 * @author ooder Team
 * @version 0.7.0
 * @since 0.7.0
 */
public interface OpenWrtP2PBridge {

    // ==================== ooderAgent 0.7.0 核心P2P接口 ====================

    /**
     * 预建立conntrack连接跟踪条目
     *
     * <p>核心功能：通过预注入连接状态实现硬防火墙"直通"，
     * 避免传统打洞的延迟和不确定性。</p>
     *
     * <p>实现原理：</p>
     * <ul>
     *   <li>使用conntrack -I命令预建立ESTABLISHED状态</li>
     *   <li>配置iptables规则允许双向UDP流量</li>
     *   <li>设置合理的超时时间(默认3600秒)</li>
     * </ul>
     *
     * @param agentIp Agent内网IP地址
     * @param agentPort Agent监听端口
     * @param peerIp 对端公网IP地址
     * @param peerPort 对端端口
     * @param sessionKey 会话密钥(用于iptables规则注释)
     * @return 操作结果，包含success状态和详细信息
     */
    Map<String, Object> preEstablishConntrack(String agentIp, int agentPort,
                                               String peerIp, int peerPort,
                                               String sessionKey);

    /**
     * 批量预建立conntrack条目
     *
     * <p>为端口池中的所有端口预建立连接状态，
     * 支持AI预测打洞时的多端口尝试。</p>
     *
     * @param agentIp Agent内网IP地址
     * @param agentPorts Agent端口列表
     * @param peerIp 对端公网IP地址
     * @param peerPorts 对端端口列表(与agentPorts一一对应)
     * @param sessionKey 会话密钥
     * @return 批量操作结果
     */
    Map<String, Object> batchPreEstablishConntrack(String agentIp, List<Integer> agentPorts,
                                                    String peerIp, List<Integer> peerPorts,
                                                    String sessionKey);

    /**
     * 释放conntrack条目
     *
     * <p>会话结束时清理预建立的连接状态，释放系统资源。</p>
     *
     * @param agentIp Agent内网IP地址
     * @param agentPort Agent监听端口
     * @param peerIp 对端公网IP地址
     * @param peerPort 对端端口
     * @return 操作结果
     */
    Map<String, Object> releaseConntrack(String agentIp, int agentPort,
                                          String peerIp, int peerPort);

    /**
     * 分配UDP端口池
     *
     * <p>为ooderAgent分配一组UDP端口用于P2P通讯，
     * 支持AI预测打洞时的端口轮换。</p>
     *
     * @param agentId Agent唯一标识(WID)
     * @param poolSize 端口池大小(推荐8)
     * @param basePort 起始端口(可选，0表示自动分配)
     * @return 端口池配置，包含分配的端口列表
     */
    Map<String, Object> allocatePortPool(String agentId, int poolSize, int basePort);

    /**
     * 释放UDP端口池
     *
     * @param agentId Agent唯一标识
     * @return 操作结果
     */
    Map<String, Object> releasePortPool(String agentId);

    /**
     * 获取端口池状态
     *
     * @param agentId Agent唯一标识
     * @return 端口池状态，包含已分配端口、使用状态等
     */
    Map<String, Object> getPortPoolStatus(String agentId);

    // ==================== 三层QoS控制接口 ====================

    /**
     * 配置L3硬件层QoS(HTB队列)
     *
     * <p>在OpenWrt路由器上配置硬件级流量控制，
     * 使用HTB(Hierarchical Token Bucket)实现分层带宽管理。</p>
     *
     * @param agentIp Agent内网IP地址
     * @param agentPort Agent端口
     * @param qosProfile QoS配置配置文件
     * @return 配置结果
     */
    Map<String, Object> configureHardwareQoS(String agentIp, int agentPort,
                                              Map<String, Object> qosProfile);

    /**
     * 配置iptables标记规则(L2系统层)
     *
     * <p>使用iptables mangle表为P2P流量打标记，
     * 配合tc实现精细化流量控制。</p>
     *
     * @param agentId Agent唯一标识
     * @param ports 端口列表
     * @param dscpValue DSCP值(0-63)
     * @return 配置结果
     */
    Map<String, Object> configureTrafficMarking(String agentId, List<Integer> ports, int dscpValue);

    /**
     * 获取实时流量统计
     *
     * @param agentId Agent唯一标识
     * @return 流量统计数据，包含收发字节数、包数、丢包率等
     */
    Map<String, Object> getRealtimeTrafficStats(String agentId);

    /**
     * 设置带宽限制
     *
     * @param agentId Agent唯一标识
     * @param downloadLimit 下载限速(字节/秒，0表示不限速)
     * @param uploadLimit 上传限速(字节/秒，0表示不限速)
     * @return 配置结果
     */
    Map<String, Object> setBandwidthLimit(String agentId, long downloadLimit, long uploadLimit);

    // ==================== NAT检测与适配接口 ====================

    /**
     * 检测NAT类型
     *
     * <p>使用STUN协议检测当前网络的NAT类型，
     * 为AI路由决策提供依据。</p>
     *
     * @return NAT类型信息
     *         - type: FullCone/RestrictedCone/PortRestricted/Symmetric
     *         - p2pFriendly: 是否适合P2P
     *         - recommendation: 连接策略建议
     */
    Map<String, Object> detectNATType();

    /**
     * 获取NAT映射信息
     *
     * <p>获取当前NAT设备的端口映射表，
     * 用于分析NAT分配规律。</p>
     *
     * @return NAT映射信息列表
     */
    Map<String, Object> getNATMappings();

    /**
     * 预测NAT端口分配
     *
     * <p>基于历史数据分析NAT端口分配规律，
     * 为AI预测打洞提供端口候选列表。</p>
     *
     * @param targetCount 预测端口数量
     * @return 预测的端口列表
     */
    Map<String, Object> predictNATPorts(int targetCount);

    // ==================== ooderAgent注册与认证接口 ====================

    /**
     * 注册ooderAgent设备
     *
     * <p>将Agent注册到OpenWrt路由器，建立软硬一体化管理关系。
     * 注册后路由器会为该Agent维护独立的防火墙链和QoS配置。</p>
     *
     * @param agentInfo Agent信息
     *                  - wid: 设备WID
     *                  - internalIp: 内网IP
     *                  - protocolVersion: 协议版本(应为0.7.0)
     *                  - capabilities: 能力列表
     * @return 注册结果，包含分配的agentId
     */
    Map<String, Object> registerAgent(Map<String, Object> agentInfo);

    /**
     * 注销ooderAgent设备
     *
     * @param wid 设备WID
     * @return 操作结果
     */
    Map<String, Object> unregisterAgent(String wid);

    /**
     * 获取已注册的Agent列表
     *
     * @return Agent列表
     */
    Map<String, Object> getRegisteredAgents();

    /**
     * 验证Agent身份
     *
     * <p>通过WID和AgentCap令牌验证Agent身份，
     * 支持与ooderAgent安全认证中心协同验证。</p>
     *
     * @param wid 设备WID
     * @param agentCap AgentCap令牌(可选，用于增强验证)
     * @return 验证结果
     */
    Map<String, Object> verifyAgentIdentity(String wid, String agentCap);

    // ==================== P2P连接管理接口 ====================

    /**
     * 建立P2P连接
     *
     * <p>完整的P2P连接建立流程：</p>
     * <ol>
     *   <li>验证双方Agent身份</li>
     *   <li>检测NAT类型，选择连接策略(L1/L2/L3)</li>
     *   <li>如果是L1策略，预建立conntrack</li>
     *   <li>配置QoS规则</li>
     *   <li>返回连接配置</li>
     * </ol>
     *
     * @param localWid 本地Agent WID
     * @param remoteWid 远端Agent WID
     * @param connectionParams 连接参数
     * @return 连接结果，包含连接ID、策略类型、配置信息等
     */
    Map<String, Object> establishP2PConnection(String localWid, String remoteWid,
                                                Map<String, Object> connectionParams);

    /**
     * 关闭P2P连接
     *
     * @param connectionId 连接ID
     * @return 操作结果
     */
    Map<String, Object> closeP2PConnection(String connectionId);

    /**
     * 获取P2P连接状态
     *
     * @param connectionId 连接ID
     * @return 连接状态信息
     */
    Map<String, Object> getP2PConnectionStatus(String connectionId);

    /**
     * 获取Agent的所有P2P连接
     *
     * @param wid Agent WID
     * @return 连接列表
     */
    Map<String, Object> getAgentP2PConnections(String wid);

    // ==================== 诊断与监控接口 ====================

    /**
     * 执行P2P连通性测试
     *
     * @param targetIp 目标IP
     * @param targetPort 目标端口
     * @param protocol 协议(udp/tcp)
     * @return 测试结果
     */
    Map<String, Object> testP2PConnectivity(String targetIp, int targetPort, String protocol);

    /**
     * 获取P2P诊断信息
     *
     * <p>提供详细的P2P连接诊断信息，包括：</p>
     * <ul>
     *   <li>NAT类型和配置</li>
     *   <li>端口映射状态</li>
     *   <li>防火墙规则状态</li>
     *   <li>QoS配置状态</li>
     *   <li>连接质量统计</li>
     * </ul>
     *
     * @param wid Agent WID
     * @return 诊断信息
     */
    Map<String, Object> getP2PDiagnostics(String wid);

    /**
     * 获取系统资源使用情况
     *
     * @return 资源使用统计，包含conntrack表使用率、内存、CPU等
     */
    Map<String, Object> getSystemResources();

    // ==================== 北向协议接口 ====================

    /**
     * 执行北向协议命令
     *
     * <p>执行ooderAgent 0.7.0北向协议定义的标准命令，
     * 支持与上层控制器的协议交互。</p>
     *
     * @param command 命令名称
     * @param params 命令参数
     * @return 命令执行结果
     */
    Map<String, Object> executeNorthboundCommand(String command, Map<String, Object> params);

    /**
     * 获取北向协议版本信息
     *
     * @return 协议版本信息
     */
    Map<String, Object> getProtocolVersion();
}
