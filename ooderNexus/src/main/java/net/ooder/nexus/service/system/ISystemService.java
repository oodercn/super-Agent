package net.ooder.nexus.service.system;

import net.ooder.nexus.model.Result;
import net.ooder.nexus.model.system.SystemInfo;
import net.ooder.nexus.model.system.ServiceStatus;
import net.ooder.nexus.model.system.ResourceUsage;
import net.ooder.nexus.model.system.SystemLoadData;
import net.ooder.nexus.model.system.ServiceCheckResult;

import java.util.List;
import java.util.Map;

/**
 * 系统管理服务接口
 * 负责系统状态、系统配置、系统监控等功能
 */
public interface ISystemService {
    
    /**
     * 获取系统基本信息
     * @return 系统基本信息
     */
    Result<SystemInfo> getSystemInfo();

    /**
     * 获取服务状态列表
     * @return 服务状态列表
     */
    Result<List<ServiceStatus>> getServiceStatuses();

    /**
     * 获取服务状态详情
     * @param serviceId 服务 ID
     * @return 服务状态详情
     */
    Result<ServiceStatus> getServiceStatus(String serviceId);

    /**
     * 获取系统资源使用情况
     * @return 系统资源使用情况
     */
    Result<ResourceUsage> getResourceUsage();

    /**
     * 获取系统负载
     * @return 系统负载
     */
    Result<SystemLoadData> getSystemLoad();

    /**
     * 重启服务
     * @param serviceId 服务 ID
     * @return 重启结果
     */
    Result<ServiceStatus> restartService(String serviceId);

    /**
     * 检查服务状态
     * @param serviceName 服务名称
     * @return 服务状态
     */
    Result<ServiceCheckResult> checkService(String serviceName);
}
