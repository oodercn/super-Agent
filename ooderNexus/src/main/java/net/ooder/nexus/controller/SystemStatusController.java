package net.ooder.nexus.controller;

import net.ooder.nexus.model.Result;
import net.ooder.nexus.model.system.SystemInfo;
import net.ooder.nexus.model.system.ServiceStatus;
import net.ooder.nexus.model.system.ResourceUsage;
import net.ooder.nexus.model.system.SystemHealthData;
import net.ooder.nexus.model.system.SystemLoadData;
import net.ooder.nexus.service.INexusService;
import net.ooder.nexus.service.NexusServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/system")
public class SystemStatusController {

    private static final Logger log = LoggerFactory.getLogger(SystemStatusController.class);

    @Autowired
    private NexusServiceFactory serviceFactory;

    public SystemStatusController() {
        log.info("SystemStatusController initialized");
    }

    private INexusService getService() {
        return serviceFactory.getService();
    }

    @GetMapping("/info")
    public Result<SystemInfo> getSystemInfo() {
        log.info("Get system info requested");
        return getService().getSystemInfo();
    }

    @GetMapping("/health")
    public Result<SystemHealthData> getSystemHealth() {
        log.info("Get system health requested");
        return getService().getSystemHealth();
    }

    @GetMapping("/services")
    public Result<List<ServiceStatus>> getServiceStatuses() {
        log.info("Get service statuses requested");
        return getService().getServiceStatuses();
    }

    @GetMapping("/services/{serviceId}")
    public Result<ServiceStatus> getServiceStatus(@PathVariable String serviceId) {
        log.info("Get service status requested: serviceId={}", serviceId);
        return getService().getServiceStatus(serviceId);
    }

    @GetMapping("/resources")
    public Result<ResourceUsage> getResourceUsage() {
        log.info("Get resource usage requested");
        return getService().getResourceUsage();
    }

    @GetMapping("/load")
    public Result<SystemLoadData> getSystemLoad() {
        log.info("Get system load requested");
        return getService().getSystemLoad();
    }

    @PostMapping("/services/{serviceId}/restart")
    public Result<ServiceStatus> restartService(@PathVariable String serviceId) {
        log.info("Restart service requested: serviceId={}", serviceId);
        return getService().restartService(serviceId);
    }
}