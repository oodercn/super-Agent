package net.ooder.skillcenter.sdk;

import net.ooder.nexus.skillcenter.dto.hosting.*;

import java.util.List;

public interface HostingExtensionSdkAdapter {

    HostingCompatibilityDTO checkCompatibility(String skillId);

    AutoScalePolicyDTO getAutoScalePolicy(String instanceId);

    AutoScalePolicyDTO createAutoScalePolicy(AutoScalePolicyDTO policy);

    AutoScalePolicyDTO updateAutoScalePolicy(String policyId, AutoScalePolicyDTO policy);

    boolean deleteAutoScalePolicy(String policyId);

    boolean enableAutoScalePolicy(String policyId);

    boolean disableAutoScalePolicy(String policyId);

    ServiceEndpointDTO registerService(String instanceId, ServiceEndpointDTO service);

    boolean unregisterService(String serviceId);

    ServiceEndpointDTO getService(String serviceId);

    List<ServiceEndpointDTO> getServicesByInstance(String instanceId);

    List<ServiceEndpointDTO> discoverService(String serviceName);

    VolumeDTO createVolume(VolumeDTO volume);

    VolumeDTO getVolume(String volumeId);

    boolean deleteVolume(String volumeId);

    boolean mountVolume(String volumeId, String instanceId, String mountPath, boolean readOnly);

    boolean unmountVolume(String volumeId, String instanceId);

    List<VolumeDTO> listVolumes();

    boolean isAvailable();
}
