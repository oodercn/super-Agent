package net.ooder.nexus.skillcenter.controller;

import net.ooder.nexus.skillcenter.dto.cloud.*;
import net.ooder.nexus.skillcenter.model.ResultModel;
import net.ooder.skillcenter.sdk.cloud.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/cloud-hosting")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS})
public class CloudHostingController extends BaseController {

    @Autowired
    private List<CloudHostingProvider> providers;

    private CloudHostingProvider getProvider(String providerName) {
        return providers.stream()
            .filter(p -> p.getProviderName().equals(providerName))
            .findFirst()
            .orElse(null);
    }

    @PostMapping("/providers/list")
    public ResultModel<List<CloudProviderInfoDTO>> listProviders() {
        long startTime = System.currentTimeMillis();
        logRequestStart("listProviders", null);

        try {
            List<CloudProviderInfoDTO> providerList = new ArrayList<>();
            for (CloudHostingProvider provider : providers) {
                CloudProviderInfoDTO info = new CloudProviderInfoDTO();
                info.setName(provider.getProviderName());
                info.setRegions(provider.getSupportedRegions());
                info.setInstanceTypes(provider.getSupportedInstanceTypes());
                providerList.add(info);
            }

            logRequestEnd("listProviders", providerList.size() + " providers", System.currentTimeMillis() - startTime);
            return ResultModel.success(providerList);
        } catch (Exception e) {
            logRequestError("listProviders", e);
            return ResultModel.error(500, "获取云厂商列表失败: " + e.getMessage());
        }
    }

    @PostMapping("/providers/regions")
    public ResultModel<List<String>> getRegions(@RequestBody CloudProviderRequestDTO request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("getRegions", request);

        try {
            CloudHostingProvider provider = getProvider(request.getProvider());
            if (provider == null) {
                logRequestEnd("getRegions", "Provider not found", System.currentTimeMillis() - startTime);
                return ResultModel.notFound("云厂商不存在");
            }

            List<String> regions = provider.getSupportedRegions();
            logRequestEnd("getRegions", regions.size() + " regions", System.currentTimeMillis() - startTime);
            return ResultModel.success(regions);
        } catch (Exception e) {
            logRequestError("getRegions", e);
            return ResultModel.error(500, "获取区域列表失败: " + e.getMessage());
        }
    }

    @PostMapping("/instances/create")
    public ResultModel<CloudInstance> createInstance(@RequestBody CreateCloudInstanceRequestDTO request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("createInstance", request);

        try {
            CloudHostingProvider provider = getProvider(request.getProvider());
            if (provider == null) {
                logRequestEnd("createInstance", "Provider not found", System.currentTimeMillis() - startTime);
                return ResultModel.notFound("云厂商不存在");
            }

            CloudHostingConfig config = new CloudHostingConfig();
            config.setProvider(request.getProvider());
            config.setProviderType(request.getProviderType());
            config.setRegion(request.getRegion());
            config.setInstanceName(request.getInstanceName());
            
            ResourceConfig resources = new ResourceConfig();
            resources.setCpuLimit(request.getCpuLimit());
            resources.setMemoryLimit(request.getMemoryLimit());
            config.setResources(resources);

            ContainerConfig container = new ContainerConfig();
            container.setImage(request.getImage());
            config.setContainer(container);

            ScaleConfig scaling = new ScaleConfig();
            scaling.setMinReplicas(request.getMinReplicas() > 0 ? request.getMinReplicas() : 1);
            scaling.setMaxReplicas(request.getMaxReplicas() > 0 ? request.getMaxReplicas() : 5);
            config.setScaling(scaling);

            CloudInstance instance = provider.createInstance(config);

            logRequestEnd("createInstance", instance.getId(), System.currentTimeMillis() - startTime);
            return ResultModel.success("创建云托管实例成功", instance);
        } catch (Exception e) {
            logRequestError("createInstance", e);
            return ResultModel.error(500, "创建实例失败: " + e.getMessage());
        }
    }

    @PostMapping("/instances/list")
    public ResultModel<List<CloudInstance>> listInstances(@RequestBody CloudProviderRequestDTO request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("listInstances", request);

        try {
            CloudHostingProvider provider = getProvider(request.getProvider());
            if (provider == null) {
                logRequestEnd("listInstances", "Provider not found", System.currentTimeMillis() - startTime);
                return ResultModel.notFound("云厂商不存在");
            }

            List<CloudInstance> instances = provider.listInstances();
            logRequestEnd("listInstances", instances.size() + " instances", System.currentTimeMillis() - startTime);
            return ResultModel.success(instances);
        } catch (Exception e) {
            logRequestError("listInstances", e);
            return ResultModel.error(500, "获取实例列表失败: " + e.getMessage());
        }
    }

    @PostMapping("/instances/get")
    public ResultModel<CloudInstance> getInstance(@RequestBody CloudInstanceRequestDTO request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("getInstance", request);

        try {
            CloudHostingProvider provider = getProvider(request.getProvider());
            if (provider == null) {
                logRequestEnd("getInstance", "Provider not found", System.currentTimeMillis() - startTime);
                return ResultModel.notFound("云厂商不存在");
            }

            CloudInstance instance = provider.getInstance(request.getInstanceId());
            logRequestEnd("getInstance", instance.getName(), System.currentTimeMillis() - startTime);
            return ResultModel.success(instance);
        } catch (Exception e) {
            logRequestError("getInstance", e);
            return ResultModel.error(500, "获取实例失败: " + e.getMessage());
        }
    }

    @PostMapping("/instances/delete")
    public ResultModel<Boolean> deleteInstance(@RequestBody CloudInstanceRequestDTO request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("deleteInstance", request);

        try {
            CloudHostingProvider provider = getProvider(request.getProvider());
            if (provider == null) {
                logRequestEnd("deleteInstance", "Provider not found", System.currentTimeMillis() - startTime);
                return ResultModel.notFound("云厂商不存在");
            }

            boolean deleted = provider.deleteInstance(request.getInstanceId());
            logRequestEnd("deleteInstance", deleted, System.currentTimeMillis() - startTime);
            return ResultModel.success("删除实例成功", deleted);
        } catch (Exception e) {
            logRequestError("deleteInstance", e);
            return ResultModel.error(500, "删除实例失败: " + e.getMessage());
        }
    }

    @PostMapping("/instances/start")
    public ResultModel<Boolean> startInstance(@RequestBody CloudInstanceRequestDTO request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("startInstance", request);

        try {
            CloudHostingProvider provider = getProvider(request.getProvider());
            if (provider == null) {
                logRequestEnd("startInstance", "Provider not found", System.currentTimeMillis() - startTime);
                return ResultModel.notFound("云厂商不存在");
            }

            boolean started = provider.startInstance(request.getInstanceId());
            logRequestEnd("startInstance", started, System.currentTimeMillis() - startTime);
            return ResultModel.success("启动实例成功", started);
        } catch (Exception e) {
            logRequestError("startInstance", e);
            return ResultModel.error(500, "启动实例失败: " + e.getMessage());
        }
    }

    @PostMapping("/instances/stop")
    public ResultModel<Boolean> stopInstance(@RequestBody CloudInstanceRequestDTO request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("stopInstance", request);

        try {
            CloudHostingProvider provider = getProvider(request.getProvider());
            if (provider == null) {
                logRequestEnd("stopInstance", "Provider not found", System.currentTimeMillis() - startTime);
                return ResultModel.notFound("云厂商不存在");
            }

            boolean stopped = provider.stopInstance(request.getInstanceId());
            logRequestEnd("stopInstance", stopped, System.currentTimeMillis() - startTime);
            return ResultModel.success("停止实例成功", stopped);
        } catch (Exception e) {
            logRequestError("stopInstance", e);
            return ResultModel.error(500, "停止实例失败: " + e.getMessage());
        }
    }

    @PostMapping("/instances/restart")
    public ResultModel<Boolean> restartInstance(@RequestBody CloudInstanceRequestDTO request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("restartInstance", request);

        try {
            CloudHostingProvider provider = getProvider(request.getProvider());
            if (provider == null) {
                logRequestEnd("restartInstance", "Provider not found", System.currentTimeMillis() - startTime);
                return ResultModel.notFound("云厂商不存在");
            }

            boolean restarted = provider.restartInstance(request.getInstanceId());
            logRequestEnd("restartInstance", restarted, System.currentTimeMillis() - startTime);
            return ResultModel.success("重启实例成功", restarted);
        } catch (Exception e) {
            logRequestError("restartInstance", e);
            return ResultModel.error(500, "重启实例失败: " + e.getMessage());
        }
    }

    @PostMapping("/instances/scale")
    public ResultModel<Boolean> scaleInstance(@RequestBody ScaleCloudInstanceRequestDTO request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("scaleInstance", request);

        try {
            CloudHostingProvider provider = getProvider(request.getProvider());
            if (provider == null) {
                logRequestEnd("scaleInstance", "Provider not found", System.currentTimeMillis() - startTime);
                return ResultModel.notFound("云厂商不存在");
            }

            boolean scaled = provider.scaleInstance(request.getInstanceId(), request.getReplicas());
            logRequestEnd("scaleInstance", scaled, System.currentTimeMillis() - startTime);
            return ResultModel.success("扩缩容成功", scaled);
        } catch (Exception e) {
            logRequestError("scaleInstance", e);
            return ResultModel.error(500, "扩缩容失败: " + e.getMessage());
        }
    }

    @PostMapping("/metrics/get")
    public ResultModel<InstanceMetrics> getMetrics(@RequestBody CloudInstanceRequestDTO request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("getMetrics", request);

        try {
            CloudHostingProvider provider = getProvider(request.getProvider());
            if (provider == null) {
                logRequestEnd("getMetrics", "Provider not found", System.currentTimeMillis() - startTime);
                return ResultModel.notFound("云厂商不存在");
            }

            InstanceMetrics metrics = provider.getMetrics(request.getInstanceId());
            logRequestEnd("getMetrics", metrics, System.currentTimeMillis() - startTime);
            return ResultModel.success(metrics);
        } catch (Exception e) {
            logRequestError("getMetrics", e);
            return ResultModel.error(500, "获取指标失败: " + e.getMessage());
        }
    }

    @PostMapping("/logs/get")
    public ResultModel<List<LogEntry>> getLogs(@RequestBody GetCloudLogsRequestDTO request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("getLogs", request);

        try {
            CloudHostingProvider provider = getProvider(request.getProvider());
            if (provider == null) {
                logRequestEnd("getLogs", "Provider not found", System.currentTimeMillis() - startTime);
                return ResultModel.notFound("云厂商不存在");
            }

            LogQueryOptions options = new LogQueryOptions();
            options.setLimit(request.getLimit() > 0 ? request.getLimit() : 50);
            options.setLevel(request.getLevel());

            List<LogEntry> logs = provider.getLogs(request.getInstanceId(), options);
            logRequestEnd("getLogs", logs.size() + " logs", System.currentTimeMillis() - startTime);
            return ResultModel.success(logs);
        } catch (Exception e) {
            logRequestError("getLogs", e);
            return ResultModel.error(500, "获取日志失败: " + e.getMessage());
        }
    }

    @PostMapping("/events/list")
    public ResultModel<List<InstanceEvent>> getEvents(@RequestBody CloudInstanceRequestDTO request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("getEvents", request);

        try {
            CloudHostingProvider provider = getProvider(request.getProvider());
            if (provider == null) {
                logRequestEnd("getEvents", "Provider not found", System.currentTimeMillis() - startTime);
                return ResultModel.notFound("云厂商不存在");
            }

            List<InstanceEvent> events = provider.getEvents(request.getInstanceId());
            logRequestEnd("getEvents", events.size() + " events", System.currentTimeMillis() - startTime);
            return ResultModel.success(events);
        } catch (Exception e) {
            logRequestError("getEvents", e);
            return ResultModel.error(500, "获取事件失败: " + e.getMessage());
        }
    }

    @PostMapping("/cost/estimate")
    public ResultModel<CostEstimate> estimateCost(@RequestBody EstimateCloudCostRequestDTO request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("estimateCost", request);

        try {
            CloudHostingProvider provider = getProvider(request.getProvider());
            if (provider == null) {
                logRequestEnd("estimateCost", "Provider not found", System.currentTimeMillis() - startTime);
                return ResultModel.notFound("云厂商不存在");
            }

            CloudHostingConfig config = new CloudHostingConfig();
            ResourceConfig resources = new ResourceConfig();
            resources.setCpuLimit(request.getCpuLimit());
            resources.setMemoryLimit(request.getMemoryLimit());
            config.setResources(resources);

            CostEstimate estimate = provider.estimateCost(config);
            logRequestEnd("estimateCost", estimate, System.currentTimeMillis() - startTime);
            return ResultModel.success(estimate);
        } catch (Exception e) {
            logRequestError("estimateCost", e);
            return ResultModel.error(500, "估算成本失败: " + e.getMessage());
        }
    }

    @PostMapping("/cost/actual")
    public ResultModel<CostActual> getActualCost(@RequestBody GetActualCostRequestDTO request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("getActualCost", request);

        try {
            CloudHostingProvider provider = getProvider(request.getProvider());
            if (provider == null) {
                logRequestEnd("getActualCost", "Provider not found", System.currentTimeMillis() - startTime);
                return ResultModel.notFound("云厂商不存在");
            }

            CostActual cost = provider.getActualCost(request.getInstanceId(), request.getStartTime(), request.getEndTime());
            logRequestEnd("getActualCost", cost, System.currentTimeMillis() - startTime);
            return ResultModel.success(cost);
        } catch (Exception e) {
            logRequestError("getActualCost", e);
            return ResultModel.error(500, "获取实际成本失败: " + e.getMessage());
        }
    }
}
