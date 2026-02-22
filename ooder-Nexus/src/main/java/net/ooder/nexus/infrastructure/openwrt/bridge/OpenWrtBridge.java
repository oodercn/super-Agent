package net.ooder.nexus.infrastructure.openwrt.bridge;

import java.util.Map;
import java.util.List;

/**
 * OpenWrt 桥接接口 - 提供与 OpenWrt 路由器的交互能力
 */
public interface OpenWrtBridge {

    // ==================== 网络设置相关方法 ====================
    Map<String, Object> getNetworkSetting(String settingType);
    Map<String, Object> getAllNetworkSettings();
    Map<String, Object> updateNetworkSetting(String settingType, Map<String, Object> settingData);

    // 批量网络设置方法
    Map<String, Object> batchUpdateNetworkSettings(Map<String, Map<String, Object>> settingsData);

    // ==================== IP地址管理相关方法 ====================
    Map<String, Object> getIPAddresses(String type, String status);
    Map<String, Object> addStaticIPAddress(Map<String, Object> ipData);
    Map<String, Object> deleteIPAddress(String ipId);

    // 批量 IP 地址管理方法
    Map<String, Object> batchAddStaticIPAddresses(List<Map<String, Object>> ipDataList);
    Map<String, Object> batchDeleteIPAddresses(List<String> ipIds);

    // ==================== IP黑名单相关方法 ====================
    Map<String, Object> getIPBlacklist();
    Map<String, Object> addIPToBlacklist(Map<String, Object> blacklistData);
    Map<String, Object> removeIPFromBlacklist(String blacklistId);

    // 批量 IP 黑名单管理方法
    Map<String, Object> batchAddIPToBlacklist(List<Map<String, Object>> blacklistDataList);
    Map<String, Object> batchRemoveIPFromBlacklist(List<String> blacklistIds);

    // ==================== 路由器连接管理 ====================
    boolean connect(String host, String username, String password);
    void disconnect();
    boolean isConnected();

    // ==================== 配置文件管理 ====================
    Map<String, Object> getConfigFile(String configFile);
    Map<String, Object> updateConfigFile(String configFile, Map<String, Object> configData);

    // ==================== 系统操作 ====================
    Map<String, Object> executeCommand(String command);
    Map<String, Object> reboot();
    Map<String, Object> getSystemStatus();

    // ==================== 版本检测与适配 ====================
    Map<String, Object> getVersionInfo();
    boolean isVersionSupported(String version);
    Map<String, Object> getSupportedVersions();
}
