package net.ooder.nexus.skillcenter.controller;

import net.ooder.nexus.skillcenter.dto.hosting.*;
import net.ooder.nexus.skillcenter.dto.PageResult;
import net.ooder.nexus.skillcenter.model.ResultModel;
import net.ooder.skillcenter.sdk.HostingExtensionSdkAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hosting")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS})
public class HostingExtensionController extends BaseController {

    @Autowired
    private HostingExtensionSdkAdapter extensionAdapter;

    @PostMapping("/compatibility/check")
    public ResultModel<HostingCompatibilityDTO> checkCompatibility(@RequestBody SkillIdRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("checkCompatibility", request);

        try {
            HostingCompatibilityDTO result = extensionAdapter.checkCompatibility(request.getSkillId());
            logRequestEnd("checkCompatibility", result.getCompatibilityScore() + "%", System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("checkCompatibility", e);
            return ResultModel.error(500, "检查托管适配性失败: " + e.getMessage());
        }
    }

    @PostMapping("/autoscale/get")
    public ResultModel<AutoScalePolicyDTO> getAutoScalePolicy(@RequestBody InstanceIdRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("getAutoScalePolicy", request);

        try {
            AutoScalePolicyDTO policy = extensionAdapter.getAutoScalePolicy(request.getInstanceId());
            if (policy == null) {
                logRequestEnd("getAutoScalePolicy", "Not found", System.currentTimeMillis() - startTime);
                return ResultModel.notFound("未找到自动伸缩策略");
            }
            logRequestEnd("getAutoScalePolicy", policy.getName(), System.currentTimeMillis() - startTime);
            return ResultModel.success(policy);
        } catch (Exception e) {
            logRequestError("getAutoScalePolicy", e);
            return ResultModel.error(500, "获取自动伸缩策略失败: " + e.getMessage());
        }
    }

    @PostMapping("/autoscale/create")
    public ResultModel<AutoScalePolicyDTO> createAutoScalePolicy(@RequestBody AutoScalePolicyDTO policy) {
        long startTime = System.currentTimeMillis();
        logRequestStart("createAutoScalePolicy", policy);

        try {
            AutoScalePolicyDTO created = extensionAdapter.createAutoScalePolicy(policy);
            logRequestEnd("createAutoScalePolicy", created.getPolicyId(), System.currentTimeMillis() - startTime);
            return ResultModel.success("创建自动伸缩策略成功", created);
        } catch (Exception e) {
            logRequestError("createAutoScalePolicy", e);
            return ResultModel.error(500, "创建自动伸缩策略失败: " + e.getMessage());
        }
    }

    @PostMapping("/autoscale/update")
    public ResultModel<AutoScalePolicyDTO> updateAutoScalePolicy(@RequestBody UpdatePolicyRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("updateAutoScalePolicy", request);

        try {
            AutoScalePolicyDTO updated = extensionAdapter.updateAutoScalePolicy(request.getPolicyId(), request.getPolicy());
            if (updated == null) {
                logRequestEnd("updateAutoScalePolicy", "Not found", System.currentTimeMillis() - startTime);
                return ResultModel.notFound("策略不存在");
            }
            logRequestEnd("updateAutoScalePolicy", updated.getName(), System.currentTimeMillis() - startTime);
            return ResultModel.success("更新自动伸缩策略成功", updated);
        } catch (Exception e) {
            logRequestError("updateAutoScalePolicy", e);
            return ResultModel.error(500, "更新自动伸缩策略失败: " + e.getMessage());
        }
    }

    @PostMapping("/autoscale/delete")
    public ResultModel<Boolean> deleteAutoScalePolicy(@RequestBody PolicyIdRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("deleteAutoScalePolicy", request);

        try {
            boolean deleted = extensionAdapter.deleteAutoScalePolicy(request.getPolicyId());
            logRequestEnd("deleteAutoScalePolicy", deleted, System.currentTimeMillis() - startTime);
            return ResultModel.success("删除自动伸缩策略成功", deleted);
        } catch (Exception e) {
            logRequestError("deleteAutoScalePolicy", e);
            return ResultModel.error(500, "删除自动伸缩策略失败: " + e.getMessage());
        }
    }

    @PostMapping("/autoscale/enable")
    public ResultModel<Boolean> enableAutoScalePolicy(@RequestBody PolicyIdRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("enableAutoScalePolicy", request);

        try {
            boolean enabled = extensionAdapter.enableAutoScalePolicy(request.getPolicyId());
            logRequestEnd("enableAutoScalePolicy", enabled, System.currentTimeMillis() - startTime);
            return ResultModel.success("启用自动伸缩策略成功", enabled);
        } catch (Exception e) {
            logRequestError("enableAutoScalePolicy", e);
            return ResultModel.error(500, "启用自动伸缩策略失败: " + e.getMessage());
        }
    }

    @PostMapping("/autoscale/disable")
    public ResultModel<Boolean> disableAutoScalePolicy(@RequestBody PolicyIdRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("disableAutoScalePolicy", request);

        try {
            boolean disabled = extensionAdapter.disableAutoScalePolicy(request.getPolicyId());
            logRequestEnd("disableAutoScalePolicy", disabled, System.currentTimeMillis() - startTime);
            return ResultModel.success("禁用自动伸缩策略成功", disabled);
        } catch (Exception e) {
            logRequestError("disableAutoScalePolicy", e);
            return ResultModel.error(500, "禁用自动伸缩策略失败: " + e.getMessage());
        }
    }

    @PostMapping("/service/register")
    public ResultModel<ServiceEndpointDTO> registerService(@RequestBody RegisterServiceRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("registerService", request);

        try {
            ServiceEndpointDTO registered = extensionAdapter.registerService(request.getInstanceId(), request.getService());
            logRequestEnd("registerService", registered.getServiceId(), System.currentTimeMillis() - startTime);
            return ResultModel.success("注册服务成功", registered);
        } catch (Exception e) {
            logRequestError("registerService", e);
            return ResultModel.error(500, "注册服务失败: " + e.getMessage());
        }
    }

    @PostMapping("/service/unregister")
    public ResultModel<Boolean> unregisterService(@RequestBody ServiceIdRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("unregisterService", request);

        try {
            boolean unregistered = extensionAdapter.unregisterService(request.getServiceId());
            logRequestEnd("unregisterService", unregistered, System.currentTimeMillis() - startTime);
            return ResultModel.success("注销服务成功", unregistered);
        } catch (Exception e) {
            logRequestError("unregisterService", e);
            return ResultModel.error(500, "注销服务失败: " + e.getMessage());
        }
    }

    @PostMapping("/service/get")
    public ResultModel<ServiceEndpointDTO> getService(@RequestBody ServiceIdRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("getService", request);

        try {
            ServiceEndpointDTO service = extensionAdapter.getService(request.getServiceId());
            if (service == null) {
                logRequestEnd("getService", "Not found", System.currentTimeMillis() - startTime);
                return ResultModel.notFound("服务不存在");
            }
            logRequestEnd("getService", service.getServiceName(), System.currentTimeMillis() - startTime);
            return ResultModel.success(service);
        } catch (Exception e) {
            logRequestError("getService", e);
            return ResultModel.error(500, "获取服务失败: " + e.getMessage());
        }
    }

    @PostMapping("/service/discover")
    public ResultModel<List<ServiceEndpointDTO>> discoverService(@RequestBody ServiceNameRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("discoverService", request);

        try {
            List<ServiceEndpointDTO> services = extensionAdapter.discoverService(request.getServiceName());
            logRequestEnd("discoverService", services.size() + " services", System.currentTimeMillis() - startTime);
            return ResultModel.success(services);
        } catch (Exception e) {
            logRequestError("discoverService", e);
            return ResultModel.error(500, "发现服务失败: " + e.getMessage());
        }
    }

    @PostMapping("/volume/create")
    public ResultModel<VolumeDTO> createVolume(@RequestBody VolumeDTO volume) {
        long startTime = System.currentTimeMillis();
        logRequestStart("createVolume", volume);

        try {
            VolumeDTO created = extensionAdapter.createVolume(volume);
            logRequestEnd("createVolume", created.getVolumeId(), System.currentTimeMillis() - startTime);
            return ResultModel.success("创建存储卷成功", created);
        } catch (Exception e) {
            logRequestError("createVolume", e);
            return ResultModel.error(500, "创建存储卷失败: " + e.getMessage());
        }
    }

    @PostMapping("/volume/get")
    public ResultModel<VolumeDTO> getVolume(@RequestBody VolumeIdRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("getVolume", request);

        try {
            VolumeDTO volume = extensionAdapter.getVolume(request.getVolumeId());
            if (volume == null) {
                logRequestEnd("getVolume", "Not found", System.currentTimeMillis() - startTime);
                return ResultModel.notFound("存储卷不存在");
            }
            logRequestEnd("getVolume", volume.getName(), System.currentTimeMillis() - startTime);
            return ResultModel.success(volume);
        } catch (Exception e) {
            logRequestError("getVolume", e);
            return ResultModel.error(500, "获取存储卷失败: " + e.getMessage());
        }
    }

    @PostMapping("/volume/list")
    public ResultModel<List<VolumeDTO>> listVolumes() {
        long startTime = System.currentTimeMillis();
        logRequestStart("listVolumes", null);

        try {
            List<VolumeDTO> volumes = extensionAdapter.listVolumes();
            logRequestEnd("listVolumes", volumes.size() + " volumes", System.currentTimeMillis() - startTime);
            return ResultModel.success(volumes);
        } catch (Exception e) {
            logRequestError("listVolumes", e);
            return ResultModel.error(500, "获取存储卷列表失败: " + e.getMessage());
        }
    }

    @PostMapping("/volume/delete")
    public ResultModel<Boolean> deleteVolume(@RequestBody VolumeIdRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("deleteVolume", request);

        try {
            boolean deleted = extensionAdapter.deleteVolume(request.getVolumeId());
            logRequestEnd("deleteVolume", deleted, System.currentTimeMillis() - startTime);
            return ResultModel.success("删除存储卷成功", deleted);
        } catch (Exception e) {
            logRequestError("deleteVolume", e);
            return ResultModel.error(500, "删除存储卷失败: " + e.getMessage());
        }
    }

    @PostMapping("/volume/mount")
    public ResultModel<Boolean> mountVolume(@RequestBody MountVolumeRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("mountVolume", request);

        try {
            boolean mounted = extensionAdapter.mountVolume(
                request.getVolumeId(),
                request.getInstanceId(),
                request.getMountPath(),
                request.isReadOnly()
            );
            logRequestEnd("mountVolume", mounted, System.currentTimeMillis() - startTime);
            return ResultModel.success("挂载存储卷成功", mounted);
        } catch (Exception e) {
            logRequestError("mountVolume", e);
            return ResultModel.error(500, "挂载存储卷失败: " + e.getMessage());
        }
    }

    @PostMapping("/volume/unmount")
    public ResultModel<Boolean> unmountVolume(@RequestBody UnmountVolumeRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("unmountVolume", request);

        try {
            boolean unmounted = extensionAdapter.unmountVolume(request.getVolumeId(), request.getInstanceId());
            logRequestEnd("unmountVolume", unmounted, System.currentTimeMillis() - startTime);
            return ResultModel.success("卸载存储卷成功", unmounted);
        } catch (Exception e) {
            logRequestError("unmountVolume", e);
            return ResultModel.error(500, "卸载存储卷失败: " + e.getMessage());
        }
    }

    public static class SkillIdRequest {
        private String skillId;
        public String getSkillId() { return skillId; }
        public void setSkillId(String skillId) { this.skillId = skillId; }
    }

    public static class InstanceIdRequest {
        private String instanceId;
        public String getInstanceId() { return instanceId; }
        public void setInstanceId(String instanceId) { this.instanceId = instanceId; }
    }

    public static class PolicyIdRequest {
        private String policyId;
        public String getPolicyId() { return policyId; }
        public void setPolicyId(String policyId) { this.policyId = policyId; }
    }

    public static class ServiceIdRequest {
        private String serviceId;
        public String getServiceId() { return serviceId; }
        public void setServiceId(String serviceId) { this.serviceId = serviceId; }
    }

    public static class ServiceNameRequest {
        private String serviceName;
        public String getServiceName() { return serviceName; }
        public void setServiceName(String serviceName) { this.serviceName = serviceName; }
    }

    public static class VolumeIdRequest {
        private String volumeId;
        public String getVolumeId() { return volumeId; }
        public void setVolumeId(String volumeId) { this.volumeId = volumeId; }
    }

    public static class UpdatePolicyRequest {
        private String policyId;
        private AutoScalePolicyDTO policy;
        public String getPolicyId() { return policyId; }
        public void setPolicyId(String policyId) { this.policyId = policyId; }
        public AutoScalePolicyDTO getPolicy() { return policy; }
        public void setPolicy(AutoScalePolicyDTO policy) { this.policy = policy; }
    }

    public static class RegisterServiceRequest {
        private String instanceId;
        private ServiceEndpointDTO service;
        public String getInstanceId() { return instanceId; }
        public void setInstanceId(String instanceId) { this.instanceId = instanceId; }
        public ServiceEndpointDTO getService() { return service; }
        public void setService(ServiceEndpointDTO service) { this.service = service; }
    }

    public static class MountVolumeRequest {
        private String volumeId;
        private String instanceId;
        private String mountPath;
        private boolean readOnly;
        public String getVolumeId() { return volumeId; }
        public void setVolumeId(String volumeId) { this.volumeId = volumeId; }
        public String getInstanceId() { return instanceId; }
        public void setInstanceId(String instanceId) { this.instanceId = instanceId; }
        public String getMountPath() { return mountPath; }
        public void setMountPath(String mountPath) { this.mountPath = mountPath; }
        public boolean isReadOnly() { return readOnly; }
        public void setReadOnly(boolean readOnly) { this.readOnly = readOnly; }
    }

    public static class UnmountVolumeRequest {
        private String volumeId;
        private String instanceId;
        public String getVolumeId() { return volumeId; }
        public void setVolumeId(String volumeId) { this.volumeId = volumeId; }
        public String getInstanceId() { return instanceId; }
        public void setInstanceId(String instanceId) { this.instanceId = instanceId; }
    }
}
