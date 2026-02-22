package net.ooder.nexus.service;

import net.ooder.nexus.model.Result;
import net.ooder.nexus.domain.system.model.SystemInfo;
import net.ooder.nexus.domain.system.model.ServiceStatus;
import net.ooder.nexus.domain.system.model.ResourceUsage;
import net.ooder.nexus.domain.system.model.SystemHealthData;
import net.ooder.nexus.domain.system.model.SystemLoadData;

import java.util.List;

/**
 * 系统状态服务接口
 * 提供系统信息、健康状态、服务状态、资源使用等功能
 */
public interface SystemStatusService {

    /**
     * 获取系统信息
     */
    Result<SystemInfo> getSystemInfo();

    /**
     * 获取系统健康状态
     */
    Result<SystemHealthData> getSystemHealth();

    /**
     * 获取所有服务状态
     */
    Result<List<ServiceStatus>> getServiceStatuses();

    /**
     * 获取指定服务状态
     */
    Result<ServiceStatus> getServiceStatus(String serviceId);

    /**
     * 获取资源使用情况
     */
    Result<ResourceUsage> getResourceUsage();

    /**
     * 获取系统负载
     */
    Result<SystemLoadData> getSystemLoad();

    /**
     * 重启服务
     */
    Result<ServiceStatus> restartService(String serviceId);
}
