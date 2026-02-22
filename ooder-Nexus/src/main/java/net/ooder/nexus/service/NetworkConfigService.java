package net.ooder.nexus.service;

import net.ooder.nexus.model.Result;
import net.ooder.nexus.domain.network.model.NetworkSetting;
import net.ooder.nexus.domain.network.model.IPAddress;
import net.ooder.nexus.domain.network.model.IPBlacklist;

import java.util.List;
import java.util.Map;

/**
 * 网络配置服务接口
 * 提供网络设置、IP地址管理、黑名单管理等功能
 */
public interface NetworkConfigService {

    /**
     * 获取网络设置
     */
    Result<NetworkSetting> getNetworkSetting(String settingType);

    /**
     * 获取所有网络设置
     */
    Result<List<NetworkSetting>> getAllNetworkSettings();

    /**
     * 更新网络设置
     */
    Result<NetworkSetting> updateNetworkSetting(String settingType, Map<String, Object> settingData);

    /**
     * 获取IP地址列表
     */
    Result<List<IPAddress>> getIPAddresses(String type, String status);

    /**
     * 添加静态IP地址
     */
    Result<IPAddress> addStaticIPAddress(Map<String, Object> ipData);

    /**
     * 删除IP地址
     */
    Result<IPAddress> deleteIPAddress(String ipId);

    /**
     * 获取IP黑名单
     */
    Result<List<IPBlacklist>> getIPBlacklist();

    /**
     * 添加IP到黑名单
     */
    Result<IPBlacklist> addIPToBlacklist(Map<String, Object> blacklistData);

    /**
     * 从黑名单移除IP
     */
    Result<IPBlacklist> removeIPFromBlacklist(String blacklistId);
}
