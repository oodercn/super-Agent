package net.ooder.nexus.skillcenter.controller;

import net.ooder.nexus.skillcenter.dto.common.PaginationDTO;
import net.ooder.nexus.skillcenter.model.ResultModel;
import net.ooder.skillcenter.dto.HostingInstanceDTO;
import net.ooder.skillcenter.dto.PageResult;
import net.ooder.skillcenter.service.HostingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 托管服务Controller - 符合v0.7.0协议规范
 */
@RestController
@RequestMapping("/api/hosting")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS})
public class HostingController extends BaseController {

    private final HostingService hostingService;

    @Autowired
    public HostingController(HostingService hostingService) {
        this.hostingService = hostingService;
    }

    @PostMapping("/instances")
    public ResultModel<List<HostingInstanceDTO>> getAllInstances() {
        long startTime = System.currentTimeMillis();
        logRequestStart("getAllInstances", null);

        try {
            List<HostingInstanceDTO> instances = hostingService.getAllInstances();
            logRequestEnd("getAllInstances", instances.size() + " instances", System.currentTimeMillis() - startTime);
            return ResultModel.success(instances);
        } catch (Exception e) {
            logRequestError("getAllInstances", e);
            return ResultModel.error(500, e.getMessage());
        }
    }

    @PostMapping("/instances/page")
    public ResultModel<PageResult<HostingInstanceDTO>> getInstancesPage(@RequestBody PaginationDTO pagination) {
        long startTime = System.currentTimeMillis();
        logRequestStart("getInstancesPage", pagination);

        try {
            int page = pagination.getPageNum() > 0 ? pagination.getPageNum() : 1;
            int size = pagination.getPageSize() > 0 ? pagination.getPageSize() : 10;
            PageResult<HostingInstanceDTO> result = hostingService.getInstances(page, size);
            logRequestEnd("getInstancesPage", result.getTotal() + " total", System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("getInstancesPage", e);
            return ResultModel.error(500, e.getMessage());
        }
    }

    @PostMapping("/instances/get")
    public ResultModel<HostingInstanceDTO> getInstance(@RequestBody InstanceIdRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("getInstance", request);

        try {
            HostingInstanceDTO instance = hostingService.getInstance(request.getInstanceId());
            if (instance == null) {
                logRequestEnd("getInstance", "Not found", System.currentTimeMillis() - startTime);
                return ResultModel.notFound("托管实例不存在");
            }
            logRequestEnd("getInstance", instance.getName(), System.currentTimeMillis() - startTime);
            return ResultModel.success(instance);
        } catch (Exception e) {
            logRequestError("getInstance", e);
            return ResultModel.error(500, e.getMessage());
        }
    }

    @PostMapping("/instances/create")
    public ResultModel<HostingInstanceDTO> createInstance(@RequestBody HostingInstanceDTO instance) {
        long startTime = System.currentTimeMillis();
        logRequestStart("createInstance", instance);

        try {
            HostingInstanceDTO created = hostingService.createInstance(instance);
            logRequestEnd("createInstance", created.getId(), System.currentTimeMillis() - startTime);
            return ResultModel.success(created);
        } catch (Exception e) {
            logRequestError("createInstance", e);
            return ResultModel.error(500, e.getMessage());
        }
    }

    @PostMapping("/instances/update")
    public ResultModel<HostingInstanceDTO> updateInstance(@RequestBody UpdateInstanceRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("updateInstance", request);

        try {
            HostingInstanceDTO updated = hostingService.updateInstance(request.getInstanceId(), request.getInstance());
            if (updated == null) {
                logRequestEnd("updateInstance", "Not found", System.currentTimeMillis() - startTime);
                return ResultModel.notFound("托管实例不存在");
            }
            logRequestEnd("updateInstance", updated.getId(), System.currentTimeMillis() - startTime);
            return ResultModel.success(updated);
        } catch (Exception e) {
            logRequestError("updateInstance", e);
            return ResultModel.error(500, e.getMessage());
        }
    }

    @PostMapping("/instances/delete")
    public ResultModel<Boolean> deleteInstance(@RequestBody InstanceIdRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("deleteInstance", request);

        try {
            boolean deleted = hostingService.deleteInstance(request.getInstanceId());
            logRequestEnd("deleteInstance", deleted, System.currentTimeMillis() - startTime);
            return ResultModel.success(deleted);
        } catch (Exception e) {
            logRequestError("deleteInstance", e);
            return ResultModel.error(500, e.getMessage());
        }
    }

    @PostMapping("/instances/start")
    public ResultModel<Boolean> startInstance(@RequestBody InstanceIdRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("startInstance", request);

        try {
            boolean started = hostingService.startInstance(request.getInstanceId());
            logRequestEnd("startInstance", started, System.currentTimeMillis() - startTime);
            return ResultModel.success(started);
        } catch (Exception e) {
            logRequestError("startInstance", e);
            return ResultModel.error(500, e.getMessage());
        }
    }

    @PostMapping("/instances/stop")
    public ResultModel<Boolean> stopInstance(@RequestBody InstanceIdRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("stopInstance", request);

        try {
            boolean stopped = hostingService.stopInstance(request.getInstanceId());
            logRequestEnd("stopInstance", stopped, System.currentTimeMillis() - startTime);
            return ResultModel.success(stopped);
        } catch (Exception e) {
            logRequestError("stopInstance", e);
            return ResultModel.error(500, e.getMessage());
        }
    }

    @PostMapping("/instances/restart")
    public ResultModel<Boolean> restartInstance(@RequestBody InstanceIdRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("restartInstance", request);

        try {
            boolean restarted = hostingService.restartInstance(request.getInstanceId());
            logRequestEnd("restartInstance", restarted, System.currentTimeMillis() - startTime);
            return ResultModel.success(restarted);
        } catch (Exception e) {
            logRequestError("restartInstance", e);
            return ResultModel.error(500, e.getMessage());
        }
    }

    @PostMapping("/instances/status")
    public ResultModel<String> getInstanceStatus(@RequestBody InstanceIdRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("getInstanceStatus", request);

        try {
            String status = hostingService.getInstanceStatus(request.getInstanceId());
            if (status == null) {
                logRequestEnd("getInstanceStatus", "Not found", System.currentTimeMillis() - startTime);
                return ResultModel.notFound("托管实例不存在");
            }
            logRequestEnd("getInstanceStatus", status, System.currentTimeMillis() - startTime);
            return ResultModel.success(status);
        } catch (Exception e) {
            logRequestError("getInstanceStatus", e);
            return ResultModel.error(500, e.getMessage());
        }
    }

    @PostMapping("/instances/health")
    public ResultModel<String> getInstanceHealth(@RequestBody InstanceIdRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("getInstanceHealth", request);

        try {
            String health = hostingService.getInstanceHealth(request.getInstanceId());
            if (health == null) {
                logRequestEnd("getInstanceHealth", "Not found", System.currentTimeMillis() - startTime);
                return ResultModel.notFound("托管实例不存在");
            }
            logRequestEnd("getInstanceHealth", health, System.currentTimeMillis() - startTime);
            return ResultModel.success(health);
        } catch (Exception e) {
            logRequestError("getInstanceHealth", e);
            return ResultModel.error(500, e.getMessage());
        }
    }

    @PostMapping("/instances/scale")
    public ResultModel<HostingInstanceDTO> scaleInstance(@RequestBody ScaleInstanceRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("scaleInstance", request);

        try {
            HostingInstanceDTO scaled = hostingService.scaleInstance(request.getInstanceId(), request.getReplicas());
            if (scaled == null) {
                logRequestEnd("scaleInstance", "Not found", System.currentTimeMillis() - startTime);
                return ResultModel.notFound("托管实例不存在");
            }
            logRequestEnd("scaleInstance", scaled.getMaxInstances() + " replicas", System.currentTimeMillis() - startTime);
            return ResultModel.success(scaled);
        } catch (Exception e) {
            logRequestError("scaleInstance", e);
            return ResultModel.error(500, e.getMessage());
        }
    }

    @PostMapping("/instances/resources")
    public ResultModel<HostingInstanceDTO> updateResources(@RequestBody UpdateResourcesRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("updateResources", request);

        try {
            HostingInstanceDTO updated = hostingService.updateResources(
                    request.getInstanceId(), request.getCpuLimit(), request.getMemoryLimit());
            if (updated == null) {
                logRequestEnd("updateResources", "Not found", System.currentTimeMillis() - startTime);
                return ResultModel.notFound("托管实例不存在");
            }
            logRequestEnd("updateResources", "cpu=" + request.getCpuLimit(), System.currentTimeMillis() - startTime);
            return ResultModel.success(updated);
        } catch (Exception e) {
            logRequestError("updateResources", e);
            return ResultModel.error(500, e.getMessage());
        }
    }

    @PostMapping("/instances/by-skill")
    public ResultModel<List<HostingInstanceDTO>> getInstancesBySkill(@RequestBody SkillIdRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("getInstancesBySkill", request);

        try {
            List<HostingInstanceDTO> instances = hostingService.getInstancesBySkill(request.getSkillId());
            logRequestEnd("getInstancesBySkill", instances.size() + " instances", System.currentTimeMillis() - startTime);
            return ResultModel.success(instances);
        } catch (Exception e) {
            logRequestError("getInstancesBySkill", e);
            return ResultModel.error(500, e.getMessage());
        }
    }

    @PostMapping("/stats")
    public ResultModel<HostingStatsDTO> getStats() {
        long startTime = System.currentTimeMillis();
        logRequestStart("getStats", null);

        try {
            HostingStatsDTO stats = new HostingStatsDTO();
            stats.setTotalInstances(hostingService.getTotalInstances());
            stats.setRunningInstances(hostingService.getRunningInstances());
            stats.setStoppedInstances(stats.getTotalInstances() - stats.getRunningInstances());
            logRequestEnd("getStats", stats.getTotalInstances() + " total", System.currentTimeMillis() - startTime);
            return ResultModel.success(stats);
        } catch (Exception e) {
            logRequestError("getStats", e);
            return ResultModel.error(500, e.getMessage());
        }
    }

    public static class InstanceIdRequest {
        private String instanceId;
        public String getInstanceId() { return instanceId; }
        public void setInstanceId(String instanceId) { this.instanceId = instanceId; }
    }

    public static class SkillIdRequest {
        private String skillId;
        public String getSkillId() { return skillId; }
        public void setSkillId(String skillId) { this.skillId = skillId; }
    }

    public static class UpdateInstanceRequest {
        private String instanceId;
        private HostingInstanceDTO instance;
        public String getInstanceId() { return instanceId; }
        public void setInstanceId(String instanceId) { this.instanceId = instanceId; }
        public HostingInstanceDTO getInstance() { return instance; }
        public void setInstance(HostingInstanceDTO instance) { this.instance = instance; }
    }

    public static class ScaleInstanceRequest {
        private String instanceId;
        private int replicas;
        public String getInstanceId() { return instanceId; }
        public void setInstanceId(String instanceId) { this.instanceId = instanceId; }
        public int getReplicas() { return replicas; }
        public void setReplicas(int replicas) { this.replicas = replicas; }
    }

    public static class UpdateResourcesRequest {
        private String instanceId;
        private double cpuLimit;
        private long memoryLimit;
        public String getInstanceId() { return instanceId; }
        public void setInstanceId(String instanceId) { this.instanceId = instanceId; }
        public double getCpuLimit() { return cpuLimit; }
        public void setCpuLimit(double cpuLimit) { this.cpuLimit = cpuLimit; }
        public long getMemoryLimit() { return memoryLimit; }
        public void setMemoryLimit(long memoryLimit) { this.memoryLimit = memoryLimit; }
    }

    public static class HostingStatsDTO {
        private long totalInstances;
        private long runningInstances;
        private long stoppedInstances;
        public long getTotalInstances() { return totalInstances; }
        public void setTotalInstances(long totalInstances) { this.totalInstances = totalInstances; }
        public long getRunningInstances() { return runningInstances; }
        public void setRunningInstances(long runningInstances) { this.runningInstances = runningInstances; }
        public long getStoppedInstances() { return stoppedInstances; }
        public void setStoppedInstances(long stoppedInstances) { this.stoppedInstances = stoppedInstances; }
    }
}
