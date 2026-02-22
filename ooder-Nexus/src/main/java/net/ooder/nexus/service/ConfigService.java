package net.ooder.nexus.service;

import net.ooder.nexus.model.Result;
import net.ooder.nexus.domain.config.model.ConfigsResult;
import net.ooder.nexus.domain.config.model.SystemConfig;
import net.ooder.nexus.domain.config.model.NetworkConfig;
import net.ooder.nexus.domain.config.model.TerminalConfig;
import net.ooder.nexus.domain.config.model.ServiceConfig;
import net.ooder.nexus.model.ConfigDataResult;
import net.ooder.nexus.model.ConfigExportResult;
import net.ooder.nexus.model.ConfigImportResult;
import net.ooder.nexus.model.ConfigResetResult;
import net.ooder.nexus.domain.config.model.ConfigHistoryItemsResult;
import net.ooder.nexus.model.ConfigHistoryResult;

import java.util.Map;

/**
 * 配置管理服务接口
 * 提供系统配置、网络配置、终端配置、服务配置等管理功能
 */
public interface ConfigService {

    /**
     * 获取所有配置列表
     */
    Result<ConfigsResult> getConfigs();

    /**
     * 获取系统配置
     */
    Result<SystemConfig> getSystemConfig();

    /**
     * 获取网络配置
     */
    Result<NetworkConfig> getNetworkConfig();

    /**
     * 获取终端配置
     */
    Result<TerminalConfig> getTerminalConfig();

    /**
     * 获取服务配置
     */
    Result<ServiceConfig> getServiceConfig();

    /**
     * 保存配置数据
     */
    Result<ConfigDataResult> saveConfigData(Map<String, Object> configData);

    /**
     * 导出配置
     */
    Result<ConfigExportResult> exportConfig(Map<String, Object> params);

    /**
     * 导入配置
     */
    Result<ConfigImportResult> importConfig(Map<String, Object> params);

    /**
     * 重置配置数据
     */
    Result<ConfigResetResult> resetConfigData();

    /**
     * 获取配置历史记录
     */
    Result<ConfigHistoryItemsResult> getConfigHistory(Map<String, Object> params);

    /**
     * 应用指定历史版本的配置
     */
    Result<ConfigHistoryResult> applyConfigHistory(String historyId);
}
