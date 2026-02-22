package net.ooder.nexus.infrastructure.openwrt.controller;

import net.ooder.nexus.infrastructure.openwrt.service.OpenWrtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OpenWrt 路由器管理 REST API 控制器
 *
 * <p>提供 OpenWrt 路由器的全面管理接口，包括：</p>
 * <ul>
 *   <li>SSH 连接管理（连接、断开、状态查询）</li>
 *   <li>网络配置管理（接口、DHCP、防火墙）</li>
 *   <li>IP 地址管理（静态 IP、DHCP 租约）</li>
 *   <li>访问控制（IP 黑名单）</li>
 *   <li>配置文件管理</li>
 *   <li>系统操作（命令执行、重启、状态监控）</li>
 *   <li>Mock 模式管理</li>
 * </ul>
 *
 * <p><strong>基础路径：</strong> {@code /api/openwrt}</p>
 *
 * @author ooder Team
 * @version 2.0.0-openwrt-preview
 * @since 2.0.0
 * @see OpenWrtService
 */
@RestController
@RequestMapping("/api/openwrt")
public class OpenWrtController {

    private static final Logger log = LoggerFactory.getLogger(OpenWrtController.class);

    /** OpenWrt 服务 */
    private final OpenWrtService openWrtService;

    /**
     * 构造函数
     *
     * @param openWrtService OpenWrt 服务实例
     */
    @Autowired
    public OpenWrtController(OpenWrtService openWrtService) {
        this.openWrtService = openWrtService;
    }

    // ==================== 连接管理 ====================

    /**
     * 连接到 OpenWrt 路由器
     *
     * <p>通过 SSH 连接到指定的 OpenWrt 路由器。</p>
     *
     * @param credentials 连接凭证，包含 host、username、password
     * @return 连接结果，包含 status、connected、message
     */
    @PostMapping("/connect")
    public ResponseEntity<Map<String, Object>> connect(@RequestBody Map<String, String> credentials) {
        log.info("Connecting to OpenWrt router");
        String host = credentials.getOrDefault("host", "192.168.1.1");
        String username = credentials.getOrDefault("username", "root");
        String password = credentials.getOrDefault("password", "");

        boolean connected = openWrtService.connect(host, username, password);

        Map<String, Object> response = new HashMap<>();
        response.put("status", connected ? "success" : "error");
        response.put("connected", connected);
        response.put("message", connected ? "Connected successfully" : "Failed to connect");

        return ResponseEntity.ok(response);
    }

    /**
     * 断开与 OpenWrt 路由器的连接
     *
     * @return 断开结果
     */
    @PostMapping("/disconnect")
    public ResponseEntity<Map<String, Object>> disconnect() {
        log.info("Disconnecting from OpenWrt router");
        openWrtService.disconnect();

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Disconnected successfully");

        return ResponseEntity.ok(response);
    }

    /**
     * 获取连接状态
     *
     * @return 连接状态，包含 connected 字段
     */
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getConnectionStatus() {
        boolean connected = openWrtService.isConnected();

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("connected", connected);

        return ResponseEntity.ok(response);
    }

    // ==================== 网络设置管理 ====================

    /**
     * 获取特定类型的网络设置
     *
     * @param settingType 设置类型，如 "interface", "dhcp", "firewall"
     * @return 网络设置数据
     */
    @GetMapping("/settings/{settingType}")
    public ResponseEntity<Map<String, Object>> getNetworkSetting(@PathVariable String settingType) {
        log.info("Getting network setting: {}", settingType);
        Map<String, Object> result = openWrtService.getNetworkSetting(settingType);
        return ResponseEntity.ok(result);
    }

    /**
     * 获取所有网络设置
     *
     * @return 所有网络设置数据
     */
    @GetMapping("/settings")
    public ResponseEntity<Map<String, Object>> getAllNetworkSettings() {
        log.info("Getting all network settings");
        Map<String, Object> result = openWrtService.getAllNetworkSettings();
        return ResponseEntity.ok(result);
    }

    /**
     * 更新网络设置
     *
     * @param settingType 设置类型
     * @param settingData 设置数据
     * @return 更新结果
     */
    @PutMapping("/settings/{settingType}")
    public ResponseEntity<Map<String, Object>> updateNetworkSetting(
            @PathVariable String settingType,
            @RequestBody Map<String, Object> settingData) {
        log.info("Updating network setting: {}", settingType);
        Map<String, Object> result = openWrtService.updateNetworkSetting(settingType, settingData);
        return ResponseEntity.ok(result);
    }

    /**
     * 批量更新网络设置
     *
     * @param settingsData 设置数据映射
     * @return 更新结果
     */
    @PutMapping("/settings/batch")
    public ResponseEntity<Map<String, Object>> batchUpdateNetworkSettings(
            @RequestBody Map<String, Map<String, Object>> settingsData) {
        log.info("Batch updating network settings");
        Map<String, Object> result = openWrtService.batchUpdateNetworkSettings(settingsData);
        return ResponseEntity.ok(result);
    }

    // ==================== IP 地址管理 ====================

    /**
     * 获取 IP 地址列表
     *
     * @param type   地址类型，如 "static", "dhcp"
     * @param status 状态过滤
     * @return IP 地址列表
     */
    @GetMapping("/ip-addresses")
    public ResponseEntity<Map<String, Object>> getIPAddresses(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status) {
        log.info("Getting IP addresses - type: {}, status: {}", type, status);
        Map<String, Object> result = openWrtService.getIPAddresses(type, status);
        return ResponseEntity.ok(result);
    }

    /**
     * 添加静态 IP 地址
     *
     * @param ipData IP 地址数据
     * @return 操作结果
     */
    @PostMapping("/ip-addresses")
    public ResponseEntity<Map<String, Object>> addStaticIPAddress(@RequestBody Map<String, Object> ipData) {
        log.info("Adding static IP address");
        Map<String, Object> result = openWrtService.addStaticIPAddress(ipData);
        return ResponseEntity.ok(result);
    }

    /**
     * 删除 IP 地址
     *
     * @param ipId IP 地址 ID
     * @return 操作结果
     */
    @DeleteMapping("/ip-addresses/{ipId}")
    public ResponseEntity<Map<String, Object>> deleteIPAddress(@PathVariable String ipId) {
        log.info("Deleting IP address: {}", ipId);
        Map<String, Object> result = openWrtService.deleteIPAddress(ipId);
        return ResponseEntity.ok(result);
    }

    /**
     * 批量添加静态 IP 地址
     *
     * @param ipDataList IP 地址数据列表
     * @return 操作结果
     */
    @PostMapping("/ip-addresses/batch")
    public ResponseEntity<Map<String, Object>> batchAddStaticIPAddresses(
            @RequestBody List<Map<String, Object>> ipDataList) {
        log.info("Batch adding static IP addresses");
        Map<String, Object> result = openWrtService.batchAddStaticIPAddresses(ipDataList);
        return ResponseEntity.ok(result);
    }

    /**
     * 批量删除 IP 地址
     *
     * @param ipIds IP 地址 ID 列表
     * @return 操作结果
     */
    @DeleteMapping("/ip-addresses/batch")
    public ResponseEntity<Map<String, Object>> batchDeleteIPAddresses(@RequestBody List<String> ipIds) {
        log.info("Batch deleting IP addresses");
        Map<String, Object> result = openWrtService.batchDeleteIPAddresses(ipIds);
        return ResponseEntity.ok(result);
    }

    // ==================== IP 黑名单管理 ====================

    /**
     * 获取 IP 黑名单
     *
     * @return 黑名单列表
     */
    @GetMapping("/blacklist")
    public ResponseEntity<Map<String, Object>> getIPBlacklist() {
        log.info("Getting IP blacklist");
        Map<String, Object> result = openWrtService.getIPBlacklist();
        return ResponseEntity.ok(result);
    }

    /**
     * 添加 IP 到黑名单
     *
     * @param blacklistData 黑名单数据
     * @return 操作结果
     */
    @PostMapping("/blacklist")
    public ResponseEntity<Map<String, Object>> addIPToBlacklist(@RequestBody Map<String, Object> blacklistData) {
        log.info("Adding IP to blacklist");
        Map<String, Object> result = openWrtService.addIPToBlacklist(blacklistData);
        return ResponseEntity.ok(result);
    }

    /**
     * 从黑名单移除 IP
     *
     * @param blacklistId 黑名单条目 ID
     * @return 操作结果
     */
    @DeleteMapping("/blacklist/{blacklistId}")
    public ResponseEntity<Map<String, Object>> removeIPFromBlacklist(@PathVariable String blacklistId) {
        log.info("Removing IP from blacklist: {}", blacklistId);
        Map<String, Object> result = openWrtService.removeIPFromBlacklist(blacklistId);
        return ResponseEntity.ok(result);
    }

    /**
     * 批量添加 IP 到黑名单
     *
     * @param blacklistDataList 黑名单数据列表
     * @return 操作结果
     */
    @PostMapping("/blacklist/batch")
    public ResponseEntity<Map<String, Object>> batchAddIPToBlacklist(
            @RequestBody List<Map<String, Object>> blacklistDataList) {
        log.info("Batch adding IPs to blacklist");
        Map<String, Object> result = openWrtService.batchAddIPToBlacklist(blacklistDataList);
        return ResponseEntity.ok(result);
    }

    /**
     * 批量从黑名单移除 IP
     *
     * @param blacklistIds 黑名单条目 ID 列表
     * @return 操作结果
     */
    @DeleteMapping("/blacklist/batch")
    public ResponseEntity<Map<String, Object>> batchRemoveIPFromBlacklist(@RequestBody List<String> blacklistIds) {
        log.info("Batch removing IPs from blacklist");
        Map<String, Object> result = openWrtService.batchRemoveIPFromBlacklist(blacklistIds);
        return ResponseEntity.ok(result);
    }

    // ==================== 配置文件管理 ====================

    /**
     * 获取配置文件内容
     *
     * @param configFile 配置文件路径
     * @return 配置文件内容
     */
    @GetMapping("/config/{configFile}")
    public ResponseEntity<Map<String, Object>> getConfigFile(@PathVariable String configFile) {
        log.info("Getting config file: {}", configFile);
        Map<String, Object> result = openWrtService.getConfigFile(configFile);
        return ResponseEntity.ok(result);
    }

    /**
     * 更新配置文件
     *
     * @param configFile 配置文件路径
     * @param configData 配置数据
     * @return 操作结果
     */
    @PutMapping("/config/{configFile}")
    public ResponseEntity<Map<String, Object>> updateConfigFile(
            @PathVariable String configFile,
            @RequestBody Map<String, Object> configData) {
        log.info("Updating config file: {}", configFile);
        Map<String, Object> result = openWrtService.updateConfigFile(configFile, configData);
        return ResponseEntity.ok(result);
    }

    // ==================== 系统操作 ====================

    /**
     * 执行 Shell 命令
     *
     * @param request 包含 command 字段的请求体
     * @return 命令执行结果
     */
    @PostMapping("/execute")
    public ResponseEntity<Map<String, Object>> executeCommand(@RequestBody Map<String, String> request) {
        String command = request.get("command");
        log.info("Executing command: {}", command);
        Map<String, Object> result = openWrtService.executeCommand(command);
        return ResponseEntity.ok(result);
    }

    /**
     * 重启路由器
     *
     * @return 操作结果
     */
    @PostMapping("/reboot")
    public ResponseEntity<Map<String, Object>> reboot() {
        log.info("Rebooting OpenWrt router");
        Map<String, Object> result = openWrtService.reboot();
        return ResponseEntity.ok(result);
    }

    /**
     * 获取系统状态
     *
     * @return 系统状态信息（CPU、内存、温度等）
     */
    @GetMapping("/system-status")
    public ResponseEntity<Map<String, Object>> getSystemStatus() {
        log.info("Getting system status");
        Map<String, Object> result = openWrtService.getSystemStatus();
        return ResponseEntity.ok(result);
    }

    // ==================== 版本信息 ====================

    /**
     * 获取版本信息
     *
     * @return 版本信息
     */
    @GetMapping("/version")
    public ResponseEntity<Map<String, Object>> getVersionInfo() {
        log.info("Getting version info");
        Map<String, Object> result = openWrtService.getVersionInfo();
        return ResponseEntity.ok(result);
    }

    /**
     * 检查版本是否受支持
     *
     * @param version 版本号
     * @return 检查结果
     */
    @GetMapping("/version/supported")
    public ResponseEntity<Map<String, Object>> isVersionSupported(@RequestParam String version) {
        boolean supported = openWrtService.isVersionSupported(version);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("version", version);
        response.put("supported", supported);

        return ResponseEntity.ok(response);
    }

    /**
     * 获取支持的版本列表
     *
     * @return 支持的版本列表
     */
    @GetMapping("/version/supported-list")
    public ResponseEntity<Map<String, Object>> getSupportedVersions() {
        Map<String, Object> result = openWrtService.getSupportedVersions();
        return ResponseEntity.ok(result);
    }

    // ==================== Mock 模式管理 ====================

    /**
     * 获取 Mock 模式状态
     *
     * @return Mock 模式状态
     */
    @GetMapping("/mock/status")
    public ResponseEntity<Map<String, Object>> getMockStatus() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("mockEnabled", openWrtService.isMockMode());
        response.put("message", openWrtService.isMockMode() ? "Running in MOCK mode" : "Running in REAL mode");
        return ResponseEntity.ok(response);
    }

    /**
     * 启用 Mock 模式
     *
     * @return 操作结果
     */
    @PostMapping("/mock/enable")
    public ResponseEntity<Map<String, Object>> enableMockMode() {
        log.info("Enabling mock mode");
        openWrtService.setMockMode(true);
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("mockEnabled", true);
        response.put("message", "Mock mode enabled");
        return ResponseEntity.ok(response);
    }

    /**
     * 禁用 Mock 模式（切换到真实模式）
     *
     * @return 操作结果
     */
    @PostMapping("/mock/disable")
    public ResponseEntity<Map<String, Object>> disableMockMode() {
        log.info("Disabling mock mode");
        openWrtService.setMockMode(false);
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("mockEnabled", false);
        response.put("message", "Mock mode disabled, switched to real mode");
        return ResponseEntity.ok(response);
    }
}
