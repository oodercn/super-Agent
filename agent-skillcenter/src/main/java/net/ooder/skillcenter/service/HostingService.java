package net.ooder.skillcenter.service;

import net.ooder.skillcenter.dto.HostingInstanceDTO;
import net.ooder.skillcenter.dto.PageResult;

import java.util.List;

/**
 * 托管服务接口 - 符合v0.7.0协议规范
 */
public interface HostingService {

    List<HostingInstanceDTO> getAllInstances();

    PageResult<HostingInstanceDTO> getInstances(int page, int size);

    HostingInstanceDTO getInstance(String instanceId);

    HostingInstanceDTO createInstance(HostingInstanceDTO instance);

    HostingInstanceDTO updateInstance(String instanceId, HostingInstanceDTO instance);

    boolean deleteInstance(String instanceId);

    boolean startInstance(String instanceId);

    boolean stopInstance(String instanceId);

    boolean restartInstance(String instanceId);

    String getInstanceStatus(String instanceId);

    String getInstanceHealth(String instanceId);

    HostingInstanceDTO scaleInstance(String instanceId, int replicas);

    HostingInstanceDTO updateResources(String instanceId, double cpuLimit, long memoryLimit);

    List<HostingInstanceDTO> getInstancesBySkill(String skillId);

    List<HostingInstanceDTO> getInstancesByOwner(String owner);

    long getTotalInstances();

    long getRunningInstances();
}
