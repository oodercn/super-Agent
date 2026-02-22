package net.ooder.nexus.provider;

import net.ooder.scene.core.Result;
import net.ooder.scene.core.PageResult;
import net.ooder.scene.core.SceneEngine;
import net.ooder.scene.provider.BaseProvider;
import net.ooder.scene.provider.SystemProvider;
import net.ooder.scene.provider.SystemInfo;
import net.ooder.scene.provider.SystemStatus;
import net.ooder.scene.provider.SystemLoad;
import net.ooder.scene.provider.ServiceInfo;
import net.ooder.scene.provider.ResourceUsage;
import net.ooder.scene.provider.SystemCommandResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.MemoryMXBean;
import java.lang.management.RuntimeMXBean;
import java.util.*;

@Component
public class NexusSystemProvider implements SystemProvider {

    private static final Logger log = LoggerFactory.getLogger(NexusSystemProvider.class);

    private SceneEngine sceneEngine;
    private boolean initialized = false;
    private boolean running = false;
    private final long startTime = System.currentTimeMillis();

    @Override
    public String getProviderName() {
        return "NexusSystemProvider";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public void initialize(SceneEngine engine) {
        this.sceneEngine = engine;
        this.initialized = true;
        log.info("NexusSystemProvider initialized");
    }

    @Override
    public void start() {
        this.running = true;
        log.info("NexusSystemProvider started");
    }

    @Override
    public void stop() {
        this.running = false;
        log.info("NexusSystemProvider stopped");
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public Result<SystemInfo> getSystemInfo() {
        log.debug("Getting system info");
        
        try {
            RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
            OperatingSystemMXBean osMXBean = ManagementFactory.getOperatingSystemMXBean();
            MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
            
            SystemInfo info = new SystemInfo();
            info.setHostname(getHostname());
            info.setOsName(osMXBean.getName());
            info.setOsVersion(osMXBean.getVersion());
            info.setOsArch(osMXBean.getArch());
            info.setJavaVersion(System.getProperty("java.version"));
            info.setJavaVendor(System.getProperty("java.vendor"));
            info.setAvailableProcessors(osMXBean.getAvailableProcessors());
            info.setStartTime(runtimeMXBean.getStartTime());
            info.setUptime(runtimeMXBean.getUptime());
            
            return Result.success(info);
        } catch (Exception e) {
            log.error("Failed to get system info: {}", e.getMessage(), e);
            return Result.error("Failed to get system info: " + e.getMessage());
        }
    }

    @Override
    public Result<SystemStatus> getSystemStatus() {
        log.debug("Getting system status");
        
        try {
            OperatingSystemMXBean osMXBean = ManagementFactory.getOperatingSystemMXBean();
            MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
            
            SystemStatus status = new SystemStatus();
            status.setCpuUsage(osMXBean.getSystemLoadAverage());
            status.setMemoryUsage((double) memoryMXBean.getHeapMemoryUsage().getUsed() / memoryMXBean.getHeapMemoryUsage().getMax() * 100);
            status.setStatus("running");
            status.setMessage("System is running normally");
            status.setTimestamp(System.currentTimeMillis());
            status.setMaxMemory(memoryMXBean.getHeapMemoryUsage().getMax());
            status.setFreeMemory(memoryMXBean.getHeapMemoryUsage().getMax() - memoryMXBean.getHeapMemoryUsage().getUsed());
            status.setTotalMemory(memoryMXBean.getHeapMemoryUsage().getCommitted());
            
            return Result.success(status);
        } catch (Exception e) {
            log.error("Failed to get system status: {}", e.getMessage(), e);
            return Result.error("Failed to get system status: " + e.getMessage());
        }
    }

    @Override
    public Result<SystemLoad> getSystemLoad() {
        log.debug("Getting system load");
        
        try {
            OperatingSystemMXBean osMXBean = ManagementFactory.getOperatingSystemMXBean();
            MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
            
            SystemLoad load = new SystemLoad();
            load.setCpuLoad(osMXBean.getSystemLoadAverage());
            load.setMemoryUsage((double) memoryMXBean.getHeapMemoryUsage().getUsed() / memoryMXBean.getHeapMemoryUsage().getMax() * 100);
            load.setProcessCount(getProcessCount());
            load.setThreadCount(Thread.activeCount());
            load.setTimestamp(System.currentTimeMillis());
            
            return Result.success(load);
        } catch (Exception e) {
            log.error("Failed to get system load: {}", e.getMessage(), e);
            return Result.error("Failed to get system load: " + e.getMessage());
        }
    }

    @Override
    public Result<PageResult<ServiceInfo>> listServices(int page, int size) {
        log.debug("Listing services: page={}, size={}", page, size);
        
        List<ServiceInfo> services = new ArrayList<ServiceInfo>();
        
        ServiceInfo apiService = new ServiceInfo();
        apiService.setServiceName("api");
        apiService.setDisplayName("API Service");
        apiService.setStatus("running");
        apiService.setMessage("API service is running");
        apiService.setLastUpdated(startTime);
        services.add(apiService);
        
        ServiceInfo networkService = new ServiceInfo();
        networkService.setServiceName("network");
        networkService.setDisplayName("Network Service");
        networkService.setStatus("running");
        networkService.setMessage("Network service is running");
        networkService.setLastUpdated(startTime);
        services.add(networkService);
        
        ServiceInfo sceneService = new ServiceInfo();
        sceneService.setServiceName("scene");
        sceneService.setDisplayName("Scene Engine Service");
        sceneService.setStatus(running ? "running" : "stopped");
        sceneService.setMessage(running ? "Scene engine is running" : "Scene engine is stopped");
        sceneService.setLastUpdated(startTime);
        services.add(sceneService);
        
        PageResult<ServiceInfo> pageResult = new PageResult<ServiceInfo>();
        pageResult.setItems(services);
        pageResult.setTotal(services.size());
        pageResult.setPageNum(page);
        pageResult.setPageSize(size);
        
        return Result.success(pageResult);
    }

    @Override
    public Result<ServiceInfo> getService(String serviceId) {
        log.debug("Getting service: {}", serviceId);
        
        ServiceInfo service = new ServiceInfo();
        
        if ("api".equals(serviceId)) {
            service.setServiceName("api");
            service.setDisplayName("API Service");
            service.setStatus("running");
            service.setMessage("API service is running");
            service.setLastUpdated(startTime);
        } else if ("network".equals(serviceId)) {
            service.setServiceName("network");
            service.setDisplayName("Network Service");
            service.setStatus("running");
            service.setMessage("Network service is running");
            service.setLastUpdated(startTime);
        } else if ("scene".equals(serviceId)) {
            service.setServiceName("scene");
            service.setDisplayName("Scene Engine Service");
            service.setStatus(running ? "running" : "stopped");
            service.setMessage(running ? "Scene engine is running" : "Scene engine is stopped");
            service.setLastUpdated(startTime);
        } else {
            return Result.error("Service not found: " + serviceId);
        }
        
        return Result.success(service);
    }

    @Override
    public Result<Boolean> startService(String serviceId) {
        log.info("Starting service: {}", serviceId);
        return Result.success(true);
    }

    @Override
    public Result<Boolean> stopService(String serviceId) {
        log.info("Stopping service: {}", serviceId);
        return Result.success(true);
    }

    @Override
    public Result<Boolean> restartService(String serviceId) {
        log.info("Restarting service: {}", serviceId);
        return Result.success(true);
    }

    @Override
    public Result<List<ResourceUsage>> getResourceUsage() {
        log.debug("Getting resource usage");
        
        List<ResourceUsage> usages = new ArrayList<ResourceUsage>();
        
        try {
            OperatingSystemMXBean osMXBean = ManagementFactory.getOperatingSystemMXBean();
            MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
            
            ResourceUsage cpuUsage = new ResourceUsage();
            cpuUsage.setType("cpu");
            cpuUsage.setName("CPU Usage");
            cpuUsage.setPercentage(osMXBean.getSystemLoadAverage());
            cpuUsage.setUnit("percentage");
            cpuUsage.setTimestamp(System.currentTimeMillis());
            usages.add(cpuUsage);
            
            ResourceUsage memUsage = new ResourceUsage();
            memUsage.setType("memory");
            memUsage.setName("Memory Usage");
            memUsage.setPercentage((double) memoryMXBean.getHeapMemoryUsage().getUsed() / memoryMXBean.getHeapMemoryUsage().getMax() * 100);
            memUsage.setUsed(memoryMXBean.getHeapMemoryUsage().getUsed());
            memUsage.setTotal(memoryMXBean.getHeapMemoryUsage().getMax());
            memUsage.setUnit("bytes");
            memUsage.setTimestamp(System.currentTimeMillis());
            usages.add(memUsage);
            
            return Result.success(usages);
        } catch (Exception e) {
            log.error("Failed to get resource usage: {}", e.getMessage(), e);
            return Result.error("Failed to get resource usage: " + e.getMessage());
        }
    }

    @Override
    public Result<Map<String, String>> getEnvironmentVariables() {
        log.debug("Getting environment variables");
        return Result.success(System.getenv());
    }

    @Override
    public Result<Map<String, String>> getSystemProperties() {
        log.debug("Getting system properties");
        
        Map<String, String> result = new HashMap<String, String>();
        Properties props = System.getProperties();
        for (String key : props.stringPropertyNames()) {
            result.put(key, props.getProperty(key));
        }
        return Result.success(result);
    }

    @Override
    public Result<SystemCommandResult> executeCommand(String command) {
        log.info("Executing command: {}", command);
        
        SystemCommandResult result = new SystemCommandResult();
        result.setCommand(command);
        result.setExitCode(-1);
        result.setOutput("Command execution not supported in current mode");
        result.setError("Command execution is disabled for security reasons");
        result.setTimestamp(System.currentTimeMillis());
        
        return Result.success(result);
    }

    private String getHostname() {
        try {
            return java.net.InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            return "unknown";
        }
    }

    private int getProcessCount() {
        try {
            return ManagementFactory.getRuntimeMXBean().getName().split("@")[0].length() > 0 ? 1 : 0;
        } catch (Exception e) {
            return 1;
        }
    }
}
