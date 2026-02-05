package net.ooder.nexus.service.config;

import net.ooder.nexus.model.Result;
import net.ooder.nexus.model.config.BasicConfig;
import net.ooder.nexus.model.config.AdvancedConfig;
import net.ooder.nexus.model.config.SecurityConfig;
import net.ooder.nexus.model.config.TerminalConfig;
import net.ooder.nexus.model.config.ServiceConfig;
import net.ooder.nexus.model.config.SystemConfig;
import net.ooder.nexus.model.config.NetworkConfig;
import net.ooder.nexus.model.config.ConfigsResult;
import net.ooder.nexus.model.config.ConfigHistoryItemsResult;
import net.ooder.nexus.model.ConfigResult;
import net.ooder.nexus.model.ConfigDataResult;
import net.ooder.nexus.model.ConfigExportResult;
import net.ooder.nexus.model.ConfigImportResult;
import net.ooder.nexus.model.ConfigResetResult;
import net.ooder.nexus.model.ConfigHistoryResult;

import java.util.Map;

/**
 * 配置管理服务接口
 * 负责系统配置、网络配置、安全配置等功能
 */
public interface IConfigService {
    
    /**
     * 获取配置列表
     * @return 配置列表
     */
    Result<ConfigsResult> getConfigs();

    /**
     * 获取系统配置
     * @return 系统配置
     */
    Result<SystemConfig> getSystemConfig();

    /**
     * 获取网络配置
     * @return 网络配置
     */
    Result<NetworkConfig> getNetworkConfig();

    /**
     * 获取终端配置
     * @return 终端配置
     */
    Result<TerminalConfig> getTerminalConfig();

    /**
     * 获取服务配置
     * @return 服务配置
     */
    Result<ServiceConfig> getServiceConfig();

    /**
     * 获取基本配置
     * @return 基本配置
     */
    Result<BasicConfig> getBasicConfig();

    /**
     * 获取高级配置
     * @return 高级配置
     */
    Result<AdvancedConfig> getAdvancedConfig();

    /**
     * 获取安全配置
     * @return 安全配置
     */
    Result<SecurityConfig> getSecurityConfig();

    /**
     * 保存配置
     * @param configData 配置数据
     * @return 保存结果
     */
    Result<ConfigResult> saveConfig(Map<String, Object> configData);

    /**
     * 重置配置
     * @return 重置结果
     */
    Result<ConfigResult> resetConfig();

    /**
     * 保存配置数据
     * @param configData 配置数据
     * @return 保存结果
     */
    Result<ConfigDataResult> saveConfigData(Map<String, Object> configData);

    /**
     * 导出配置
     * @param params 导出参数
     * @return 导出结果
     */
    Result<ConfigExportResult> exportConfig(Map<String, Object> params);

    /**
     * 导入配置
     * @param params 导入参数
     * @return 导入结果
     */
    Result<ConfigImportResult> importConfig(Map<String, Object> params);

    /**
     * 重置配置数据
     * @return 重置结果
     */
    Result<ConfigResetResult> resetConfigData();

    /**
     * 获取配置历史
     * @param params 查询参数
     * @return 配置历史
     */
    Result<ConfigHistoryItemsResult> getConfigHistory(Map<String, Object> params);

    /**
     * 应用配置历史
     * @param historyId 历史 ID
     * @return 应用结果
     */
    Result<ConfigHistoryResult> applyConfigHistory(String historyId);
}
