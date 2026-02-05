package net.ooder.nexus.service.network;

import net.ooder.nexus.model.Result;
import net.ooder.nexus.model.network.NetworkSetting;
import net.ooder.nexus.model.network.IPAddress;
import net.ooder.nexus.model.network.IPBlacklist;
import net.ooder.nexus.model.network.EndAgent;
import net.ooder.nexus.model.network.NetworkStatusData;
import net.ooder.nexus.model.network.NetworkDevice;

import java.util.List;
import java.util.Map;

/**
 * 网络管理服务接口
 * 负责网络配置、网络状态、网络设备等功能
 */
public interface INetworkService {
    
    /**
     * 获取网络设置
     * @param settingType 设置类型
     * @return 网络设置
     */
    Result<NetworkSetting> getNetworkSetting(String settingType);

    /**
     * 获取所有网络设置
     * @return 所有网络设置
     */
    Result<List<NetworkSetting>> getAllNetworkSettings();

    /**
     * 更新网络设置
     * @param settingType 设置类型
     * @param settingData 设置数据
     * @return 更新结果
     */
    Result<NetworkSetting> updateNetworkSetting(String settingType, Map<String, Object> settingData);

    /**
     * 获取 IP 地址列表
     * @param type IP 类型
     * @param status IP 状态
     * @return IP 地址列表
     */
    Result<List<IPAddress>> getIPAddresses(String type, String status);

    /**
     * 添加静态 IP 地址
     * @param ipData IP 数据
     * @return 添加结果
     */
    Result<IPAddress> addStaticIPAddress(Map<String, Object> ipData);

    /**
     * 删除 IP 地址
     * @param ipId IP ID
     * @return 删除结果
     */
    Result<IPAddress> deleteIPAddress(String ipId);

    /**
     * 获取 IP 黑名单
     * @return IP 黑名单
     */
    Result<List<IPBlacklist>> getIPBlacklist();

    /**
     * 添加 IP 到黑名单
     * @param blacklistData 黑名单数据
     * @return 添加结果
     */
    Result<IPBlacklist> addIPToBlacklist(Map<String, Object> blacklistData);

    /**
     * 从黑名单移除 IP
     * @param blacklistId 黑名单 ID
     * @return 移除结果
     */
    Result<IPBlacklist> removeIPFromBlacklist(String blacklistId);

    /**
     * 获取网络状态
     * @return 网络状态
     */
    Result<NetworkStatusData> getNetworkStatus();

    /**
     * 获取终端代理列表
     * @return 终端代理列表
     */
    Result<List<EndAgent>> getEndAgents();

    /**
     * 添加终端代理
     * @param agentData 终端代理数据
     * @return 添加结果
     */
    Result<EndAgent> addEndAgent(Map<String, Object> agentData);

    /**
     * 编辑终端代理
     * @param agentId 终端代理 ID
     * @param agentData 终端代理数据
     * @return 编辑结果
     */
    Result<EndAgent> editEndAgent(String agentId, Map<String, Object> agentData);

    /**
     * 删除终端代理
     * @param agentId 终端代理 ID
     * @return 删除结果
     */
    Result<EndAgent> deleteEndAgent(String agentId);

    /**
     * 获取终端代理详情
     * @param agentId 终端代理 ID
     * @return 终端代理详情
     */
    Result<EndAgent> getEndAgentDetails(String agentId);

    /**
     * 获取网络设备列表
     * @return 网络设备列表
     */
    Result<List<NetworkDevice>> getNetworkDevices();
}
