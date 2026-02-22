package net.ooder.skillcenter.service.impl;

import net.ooder.skillcenter.dto.HostingInstanceDTO;
import net.ooder.skillcenter.dto.PageResult;
import net.ooder.skillcenter.sdk.HostingSdkAdapter;
import net.ooder.skillcenter.service.HostingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@ConditionalOnProperty(name = "skillcenter.sdk.mode", havingValue = "sdk")
public class HostingServiceSdkImpl implements HostingService {

    @Autowired
    private HostingSdkAdapter hostingSdkAdapter;

    @Override
    public List<HostingInstanceDTO> getAllInstances() {
        return hostingSdkAdapter.getAllInstances();
    }

    @Override
    public PageResult<HostingInstanceDTO> getInstances(int page, int size) {
        return hostingSdkAdapter.getInstances(page, size);
    }

    @Override
    public HostingInstanceDTO getInstance(String instanceId) {
        return hostingSdkAdapter.getInstance(instanceId);
    }

    @Override
    public HostingInstanceDTO createInstance(HostingInstanceDTO instance) {
        return hostingSdkAdapter.createInstance(instance);
    }

    @Override
    public HostingInstanceDTO updateInstance(String instanceId, HostingInstanceDTO instance) {
        return hostingSdkAdapter.updateInstance(instanceId, instance);
    }

    @Override
    public boolean deleteInstance(String instanceId) {
        return hostingSdkAdapter.deleteInstance(instanceId);
    }

    @Override
    public boolean startInstance(String instanceId) {
        return hostingSdkAdapter.startInstance(instanceId);
    }

    @Override
    public boolean stopInstance(String instanceId) {
        return hostingSdkAdapter.stopInstance(instanceId);
    }

    @Override
    public boolean restartInstance(String instanceId) {
        return hostingSdkAdapter.restartInstance(instanceId);
    }

    @Override
    public String getInstanceStatus(String instanceId) {
        return hostingSdkAdapter.getInstanceStatus(instanceId);
    }

    @Override
    public String getInstanceHealth(String instanceId) {
        return hostingSdkAdapter.getInstanceHealth(instanceId);
    }

    @Override
    public HostingInstanceDTO scaleInstance(String instanceId, int replicas) {
        return hostingSdkAdapter.scaleInstance(instanceId, replicas);
    }

    @Override
    public HostingInstanceDTO updateResources(String instanceId, double cpuLimit, long memoryLimit) {
        return hostingSdkAdapter.updateResources(instanceId, cpuLimit, memoryLimit);
    }

    @Override
    public List<HostingInstanceDTO> getInstancesBySkill(String skillId) {
        return hostingSdkAdapter.getInstancesBySkill(skillId);
    }

    @Override
    public List<HostingInstanceDTO> getInstancesByOwner(String owner) {
        return hostingSdkAdapter.getInstancesByOwner(owner);
    }

    @Override
    public long getTotalInstances() {
        return hostingSdkAdapter.getTotalInstances();
    }

    @Override
    public long getRunningInstances() {
        return hostingSdkAdapter.getRunningInstances();
    }
}
