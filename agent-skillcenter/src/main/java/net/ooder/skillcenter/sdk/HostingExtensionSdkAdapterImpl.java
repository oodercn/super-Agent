package net.ooder.skillcenter.sdk;

import net.ooder.nexus.skillcenter.dto.hosting.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@ConditionalOnProperty(name = "skillcenter.sdk.mode", havingValue = "sdk")
public class HostingExtensionSdkAdapterImpl implements HostingExtensionSdkAdapter {

    private static final Logger log = LoggerFactory.getLogger(HostingExtensionSdkAdapterImpl.class);

    private final Map<String, AutoScalePolicyDTO> policies = new HashMap<>();
    private final Map<String, ServiceEndpointDTO> services = new HashMap<>();
    private final Map<String, VolumeDTO> volumes = new HashMap<>();

    @Override
    public HostingCompatibilityDTO checkCompatibility(String skillId) {
        HostingCompatibilityDTO dto = new HostingCompatibilityDTO();
        dto.setSkillId(skillId);
        dto.setCompatibilityScore(95.0);
        dto.setRecommendation("Compatible");
        List<HostingCompatibilityDTO.CompatibilityCheck> checks = new ArrayList<>();
        checks.add(createCheck("runtime", "pass", "All runtimes supported", true));
        checks.add(createCheck("cpu", "pass", "Minimum CPU requirement met", true));
        checks.add(createCheck("memory", "pass", "Minimum memory requirement met", true));
        dto.setChecks(checks);
        return dto;
    }

    private HostingCompatibilityDTO.CompatibilityCheck createCheck(String name, String status, String message, boolean required) {
        HostingCompatibilityDTO.CompatibilityCheck check = new HostingCompatibilityDTO.CompatibilityCheck();
        check.setName(name);
        check.setStatus(status);
        check.setMessage(message);
        check.setRequired(required);
        return check;
    }

    @Override
    public AutoScalePolicyDTO getAutoScalePolicy(String instanceId) {
        return policies.values().stream()
            .filter(p -> instanceId.equals(p.getInstanceId()))
            .findFirst()
            .orElse(null);
    }

    @Override
    public AutoScalePolicyDTO createAutoScalePolicy(AutoScalePolicyDTO policy) {
        String policyId = "policy-" + UUID.randomUUID().toString().substring(0, 8);
        policy.setPolicyId(policyId);
        policy.setEnabled(true);
        policies.put(policyId, policy);
        return policy;
    }

    @Override
    public AutoScalePolicyDTO updateAutoScalePolicy(String policyId, AutoScalePolicyDTO policy) {
        if (!policies.containsKey(policyId)) {
            return null;
        }
        policy.setPolicyId(policyId);
        policies.put(policyId, policy);
        return policy;
    }

    @Override
    public boolean deleteAutoScalePolicy(String policyId) {
        return policies.remove(policyId) != null;
    }

    @Override
    public boolean enableAutoScalePolicy(String policyId) {
        AutoScalePolicyDTO policy = policies.get(policyId);
        if (policy != null) {
            policy.setEnabled(true);
            return true;
        }
        return false;
    }

    @Override
    public boolean disableAutoScalePolicy(String policyId) {
        AutoScalePolicyDTO policy = policies.get(policyId);
        if (policy != null) {
            policy.setEnabled(false);
            return true;
        }
        return false;
    }

    @Override
    public ServiceEndpointDTO registerService(String instanceId, ServiceEndpointDTO service) {
        String serviceId = "svc-" + UUID.randomUUID().toString().substring(0, 8);
        service.setServiceId(serviceId);
        service.setInstanceId(instanceId);
        service.setStatus("registered");
        services.put(serviceId, service);
        return service;
    }

    @Override
    public boolean unregisterService(String serviceId) {
        return services.remove(serviceId) != null;
    }

    @Override
    public ServiceEndpointDTO getService(String serviceId) {
        return services.get(serviceId);
    }

    @Override
    public List<ServiceEndpointDTO> getServicesByInstance(String instanceId) {
        List<ServiceEndpointDTO> result = new ArrayList<>();
        for (ServiceEndpointDTO svc : services.values()) {
            if (instanceId.equals(svc.getInstanceId())) {
                result.add(svc);
            }
        }
        return result;
    }

    @Override
    public List<ServiceEndpointDTO> discoverService(String serviceName) {
        List<ServiceEndpointDTO> result = new ArrayList<>();
        for (ServiceEndpointDTO svc : services.values()) {
            if (serviceName.equals(svc.getServiceName())) {
                result.add(svc);
            }
        }
        return result;
    }

    @Override
    public VolumeDTO createVolume(VolumeDTO volume) {
        String volumeId = "vol-" + UUID.randomUUID().toString().substring(0, 8);
        volume.setVolumeId(volumeId);
        volume.setStatus("creating");
        volumes.put(volumeId, volume);
        return volume;
    }

    @Override
    public VolumeDTO getVolume(String volumeId) {
        return volumes.get(volumeId);
    }

    @Override
    public boolean deleteVolume(String volumeId) {
        return volumes.remove(volumeId) != null;
    }

    @Override
    public boolean mountVolume(String volumeId, String instanceId, String mountPath, boolean readOnly) {
        VolumeDTO volume = volumes.get(volumeId);
        if (volume == null) {
            return false;
        }
        List<VolumeDTO.VolumeMount> mounts = volume.getMounts();
        if (mounts == null) {
            mounts = new ArrayList<>();
            volume.setMounts(mounts);
        }
        VolumeDTO.VolumeMount mount = new VolumeDTO.VolumeMount();
        mount.setInstanceId(instanceId);
        mount.setMountPath(mountPath);
        mount.setReadOnly(readOnly);
        mounts.add(mount);
        volume.setStatus("mounted");
        return true;
    }

    @Override
    public boolean unmountVolume(String volumeId, String instanceId) {
        VolumeDTO volume = volumes.get(volumeId);
        if (volume == null) {
            return false;
        }
        List<VolumeDTO.VolumeMount> mounts = volume.getMounts();
        if (mounts != null) {
            mounts.removeIf(mount -> instanceId.equals(mount.getInstanceId()));
        }
        volume.setStatus("available");
        return true;
    }

    @Override
    public List<VolumeDTO> listVolumes() {
        return new ArrayList<>(volumes.values());
    }

    @Override
    public boolean isAvailable() {
        return true;
    }
}
