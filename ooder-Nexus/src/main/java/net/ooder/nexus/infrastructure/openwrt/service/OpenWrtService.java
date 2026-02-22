package net.ooder.nexus.infrastructure.openwrt.service;

import net.ooder.nexus.infrastructure.openwrt.bridge.OpenWrtBridge;
import net.ooder.nexus.infrastructure.openwrt.bridge.OpenWrtBridgeFactory;
import net.ooder.nexus.infrastructure.openwrt.config.OpenWrtProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Map;

/**
 * OpenWrt 路由器管理服务
 *
 * <p>提供 OpenWrt 路由器的全面管理功能，包括：</p>
 * <ul>
 *   <li>SSH 连接管理</li>
 *   <li>网络配置管理</li>
 *   <li>IP 地址管理</li>
 *   <li>访问控制（黑名单）</li>
 *   <li>系统状态监控</li>
 *   <li>命令执行</li>
 * </ul>
 *
 * <p><strong>Mock 模式：</strong></p>
 * <p>支持 Mock 模式和真实模式切换，便于开发和测试：</p>
 * <ul>
 *   <li>Mock 模式：使用模拟数据，无需真实路由器</li>
 *   <li>真实模式：连接真实 OpenWrt 路由器</li>
 * </ul>
 *
 * @author ooder Team
 * @version 2.0.0-openwrt-preview
 * @since 2.0.0
 * @see OpenWrtBridge
 * @see OpenWrtMockService
 */
@Service
public class OpenWrtService {

    private static final Logger log = LoggerFactory.getLogger(OpenWrtService.class);

    /** OpenWrt 配置属性 */
    private final OpenWrtProperties properties;

    /** Mock 服务实例 */
    private final OpenWrtMockService mockService;

    /** OpenWrt 桥接器（可能是真实桥接器或 Mock 桥接器） */
    private OpenWrtBridge bridge;

    /** 是否使用 Mock 模式 */
    private boolean useMock = false;

    /**
     * 构造函数
     *
     * @param properties  OpenWrt 配置属性
     * @param mockService Mock 服务实例
     */
    @Autowired
    public OpenWrtService(OpenWrtProperties properties, OpenWrtMockService mockService) {
        this.properties = properties;
        this.mockService = mockService;
    }

    /**
     * 初始化服务
     *
     * <p>根据配置决定使用 Mock 模式还是真实模式：</p>
     * <ul>
     *   <li>如果 mock.enabled=true，使用 Mock 模式</li>
     *   <li>如果 openwrt.enabled=true，使用真实模式</li>
     *   <li>否则禁用 OpenWrt 服务</li>
     * </ul>
     */
    @PostConstruct
    public void init() {
        log.info("Initializing OpenWrt Service");
        this.useMock = properties.isMockEnabled();

        if (useMock) {
            log.info("OpenWrt Service running in MOCK mode");
            this.bridge = mockService;
        } else if (properties.isEnabled()) {
            this.bridge = OpenWrtBridgeFactory.getInstance();
            log.info("OpenWrt Service initialized with real bridge");
        } else {
            log.info("OpenWrt Service is disabled");
        }
    }

    /**
     * 销毁服务
     *
     * <p>断开与路由器的连接，释放资源。</p>
     */
    @PreDestroy
    public void destroy() {
        log.info("Destroying OpenWrt Service");
        if (bridge != null && bridge.isConnected() && !useMock) {
            bridge.disconnect();
        }
        log.info("OpenWrt Service destroyed");
    }

    /**
     * 切换 Mock 模式
     *
     * <p>在 Mock 模式和真实模式之间切换。</p>
     *
     * <p><strong>注意：</strong>切换时会断开当前连接。</p>
     *
     * @param enabled true 启用 Mock 模式，false 使用真实模式
     */
    public void setMockMode(boolean enabled) {
        this.useMock = enabled;
        if (enabled) {
            log.info("Switching to MOCK mode");
            if (bridge != null && bridge.isConnected() && !useMock) {
                bridge.disconnect();
            }
            this.bridge = mockService;
        } else {
            log.info("Switching to REAL mode");
            this.bridge = OpenWrtBridgeFactory.getInstance();
        }
    }

    /**
     * 检查是否使用 Mock 模式
     *
     * @return true 如果使用 Mock 模式，false 如果使用真实模式
     */
    public boolean isMockMode() {
        return useMock;
    }

    // ==================== 连接管理 ====================

    /**
     * 连接到 OpenWrt 路由器
     *
     * @param host     路由器 IP 地址或主机名
     * @param username SSH 用户名
     * @param password SSH 密码
     * @return true 如果连接成功，false 如果连接失败
     */
    public boolean connect(String host, String username, String password) {
        log.info("Connecting to OpenWrt router: {}@{} (mock={})", username, host, useMock);
        return bridge.connect(host, username, password);
    }

    /**
     * 断开与 OpenWrt 路由器的连接
     */
    public void disconnect() {
        log.info("Disconnecting from OpenWrt router (mock={})", useMock);
        bridge.disconnect();
    }

    /**
     * 检查是否已连接到路由器
     *
     * @return true 如果已连接，false 如果未连接
     */
    public boolean isConnected() {
        return bridge != null && bridge.isConnected();
    }

    // ==================== 网络设置管理 ====================

    /**
     * 获取特定类型的网络设置
     *
     * @param settingType 设置类型，如 "interface", "dhcp", "firewall"
     * @return 网络设置数据
     */
    public Map<String, Object> getNetworkSetting(String settingType) {
        return bridge.getNetworkSetting(settingType);
    }

    /**
     * 获取所有网络设置
     *
     * @return 所有网络设置数据
     */
    public Map<String, Object> getAllNetworkSettings() {
        return bridge.getAllNetworkSettings();
    }

    /**
     * 更新网络设置
     *
     * @param settingType 设置类型
     * @param settingData 设置数据
     * @return 更新结果
     */
    public Map<String, Object> updateNetworkSetting(String settingType, Map<String, Object> settingData) {
        return bridge.updateNetworkSetting(settingType, settingData);
    }

    /**
     * 批量更新网络设置
     *
     * @param settingsData 设置数据映射
     * @return 更新结果
     */
    public Map<String, Object> batchUpdateNetworkSettings(Map<String, Map<String, Object>> settingsData) {
        return bridge.batchUpdateNetworkSettings(settingsData);
    }

    // ==================== IP 地址管理 ====================

    /**
     * 获取 IP 地址列表
     *
     * @param type   地址类型，如 "static", "dhcp"
     * @param status 状态过滤
     * @return IP 地址列表
     */
    public Map<String, Object> getIPAddresses(String type, String status) {
        return bridge.getIPAddresses(type, status);
    }

    /**
     * 添加静态 IP 地址
     *
     * @param ipData IP 地址数据
     * @return 操作结果
     */
    public Map<String, Object> addStaticIPAddress(Map<String, Object> ipData) {
        return bridge.addStaticIPAddress(ipData);
    }

    /**
     * 删除 IP 地址
     *
     * @param ipId IP 地址 ID
     * @return 操作结果
     */
    public Map<String, Object> deleteIPAddress(String ipId) {
        return bridge.deleteIPAddress(ipId);
    }

    /**
     * 批量添加静态 IP 地址
     *
     * @param ipDataList IP 地址数据列表
     * @return 操作结果
     */
    public Map<String, Object> batchAddStaticIPAddresses(List<Map<String, Object>> ipDataList) {
        return bridge.batchAddStaticIPAddresses(ipDataList);
    }

    /**
     * 批量删除 IP 地址
     *
     * @param ipIds IP 地址 ID 列表
     * @return 操作结果
     */
    public Map<String, Object> batchDeleteIPAddresses(List<String> ipIds) {
        return bridge.batchDeleteIPAddresses(ipIds);
    }

    // ==================== IP 黑名单管理 ====================

    /**
     * 获取 IP 黑名单
     *
     * @return 黑名单列表
     */
    public Map<String, Object> getIPBlacklist() {
        return bridge.getIPBlacklist();
    }

    /**
     * 添加 IP 到黑名单
     *
     * @param blacklistData 黑名单数据
     * @return 操作结果
     */
    public Map<String, Object> addIPToBlacklist(Map<String, Object> blacklistData) {
        return bridge.addIPToBlacklist(blacklistData);
    }

    /**
     * 从黑名单移除 IP
     *
     * @param blacklistId 黑名单条目 ID
     * @return 操作结果
     */
    public Map<String, Object> removeIPFromBlacklist(String blacklistId) {
        return bridge.removeIPFromBlacklist(blacklistId);
    }

    /**
     * 批量添加 IP 到黑名单
     *
     * @param blacklistDataList 黑名单数据列表
     * @return 操作结果
     */
    public Map<String, Object> batchAddIPToBlacklist(List<Map<String, Object>> blacklistDataList) {
        return bridge.batchAddIPToBlacklist(blacklistDataList);
    }

    /**
     * 批量从黑名单移除 IP
     *
     * @param blacklistIds 黑名单条目 ID 列表
     * @return 操作结果
     */
    public Map<String, Object> batchRemoveIPFromBlacklist(List<String> blacklistIds) {
        return bridge.batchRemoveIPFromBlacklist(blacklistIds);
    }

    // ==================== 配置文件管理 ====================

    /**
     * 获取配置文件内容
     *
     * @param configFile 配置文件路径
     * @return 配置文件内容
     */
    public Map<String, Object> getConfigFile(String configFile) {
        return bridge.getConfigFile(configFile);
    }

    /**
     * 更新配置文件
     *
     * @param configFile 配置文件路径
     * @param configData 配置数据
     * @return 操作结果
     */
    public Map<String, Object> updateConfigFile(String configFile, Map<String, Object> configData) {
        return bridge.updateConfigFile(configFile, configData);
    }

    // ==================== 系统操作 ====================

    /**
     * 执行 Shell 命令
     *
     * @param command 要执行的命令
     * @return 命令执行结果
     */
    public Map<String, Object> executeCommand(String command) {
        return bridge.executeCommand(command);
    }

    /**
     * 重启路由器
     *
     * @return 操作结果
     */
    public Map<String, Object> reboot() {
        return bridge.reboot();
    }

    /**
     * 获取系统状态
     *
     * @return 系统状态信息（CPU、内存、温度等）
     */
    public Map<String, Object> getSystemStatus() {
        return bridge.getSystemStatus();
    }

    // ==================== 版本信息 ====================

    /**
     * 获取版本信息
     *
     * @return 版本信息
     */
    public Map<String, Object> getVersionInfo() {
        return bridge.getVersionInfo();
    }

    /**
     * 检查版本是否受支持
     *
     * @param version 版本号
     * @return true 如果受支持
     */
    public boolean isVersionSupported(String version) {
        return bridge.isVersionSupported(version);
    }

    /**
     * 获取支持的版本列表
     *
     * @return 支持的版本列表
     */
    public Map<String, Object> getSupportedVersions() {
        return bridge.getSupportedVersions();
    }
}
