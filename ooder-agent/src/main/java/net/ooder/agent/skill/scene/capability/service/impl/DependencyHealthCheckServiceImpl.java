package net.ooder.mvp.skill.scene.capability.service.impl;

import net.ooder.mvp.skill.scene.capability.model.Capability;
import net.ooder.mvp.skill.scene.capability.service.CapabilityService;
import net.ooder.mvp.skill.scene.capability.service.DependencyHealthCheckService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DependencyHealthCheckServiceImpl implements DependencyHealthCheckService {

    private static final Logger log = LoggerFactory.getLogger(DependencyHealthCheckServiceImpl.class);

    private Map<String, HealthChecker> healthCheckers = new ConcurrentHashMap<>();
    
    private Map<String, HealthCheckResult> cachedResults = new ConcurrentHashMap<>();
    private long cacheExpireMs = 60000;
    
    @Autowired(required = false)
    private CapabilityService capabilityService;

    @Override
    public HealthCheckResult checkDependency(String capabilityId) {
        log.debug("[checkDependency] Checking health for: {}", capabilityId);
        
        HealthCheckResult cached = cachedResults.get(capabilityId);
        if (cached != null && System.currentTimeMillis() - cached.getCheckTime() < cacheExpireMs) {
            log.debug("[checkDependency] Returning cached result for: {}", capabilityId);
            return cached;
        }
        
        if (capabilityService == null) {
            return HealthCheckResult.unknown(capabilityId, capabilityId);
        }
        
        try {
            Capability capability = capabilityService.findById(capabilityId);
            if (capability == null) {
                return HealthCheckResult.unknown(capabilityId, capabilityId);
            }
            
            HealthChecker checker = healthCheckers.get(capabilityId);
            if (checker != null) {
                log.debug("[checkDependency] Using custom checker for: {}", capabilityId);
                HealthCheckResult result = checker.check(capability);
                cachedResults.put(capabilityId, result);
                return result;
            }
            
            HealthCheckResult result = performDefaultCheck(capability);
            cachedResults.put(capabilityId, result);
            return result;
            
        } catch (Exception e) {
            log.error("[checkDependency] Health check failed for {}: {}", capabilityId, e.getMessage());
            return HealthCheckResult.unhealthy(capabilityId, capabilityId, "检查失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, HealthCheckResult> checkAllDependencies(List<String> capabilityIds) {
        log.info("[checkAllDependencies] Checking {} dependencies", capabilityIds.size());
        
        Map<String, HealthCheckResult> results = new LinkedHashMap<>();
        
        for (String capabilityId : capabilityIds) {
            HealthCheckResult result = checkDependency(capabilityId);
            results.put(capabilityId, result);
        }
        
        long healthyCount = results.values().stream()
            .filter(HealthCheckResult::isHealthy)
            .count();
        
        log.info("[checkAllDependencies] Results: {}/{} healthy", healthyCount, capabilityIds.size());
        
        return results;
    }

    @Override
    public boolean isHealthy(String capabilityId) {
        HealthCheckResult result = checkDependency(capabilityId);
        return result.isHealthy();
    }

    @Override
    public boolean isAllHealthy(List<String> capabilityIds) {
        if (capabilityIds == null || capabilityIds.isEmpty()) {
            return true;
        }
        
        Map<String, HealthCheckResult> results = checkAllDependencies(capabilityIds);
        return results.values().stream().allMatch(HealthCheckResult::isHealthy);
    }

    @Override
    public void registerHealthCheck(String capabilityId, HealthChecker checker) {
        log.info("[registerHealthCheck] Registering health checker for: {} (type={})", 
            capabilityId, checker.getType());
        healthCheckers.put(capabilityId, checker);
    }

    @Override
    public void unregisterHealthCheck(String capabilityId) {
        log.info("[unregisterHealthCheck] Unregistering health checker for: {}", capabilityId);
        healthCheckers.remove(capabilityId);
    }
    
    private HealthCheckResult performDefaultCheck(Capability capability) {
        long startTime = System.currentTimeMillis();
        String capabilityId = capability.getCapabilityId();
        String name = capability.getName();
        
        try {
            if (!capability.isInstalled()) {
                return HealthCheckResult.unhealthy(capabilityId, name, "能力未安装");
            }
            
            net.ooder.mvp.skill.scene.capability.model.CapabilityStatus status = capability.getStatus();
            if (status == null) {
                return HealthCheckResult.unknown(capabilityId, name);
            }
            
            if (status.isActive()) {
                long responseTime = System.currentTimeMillis() - startTime;
                HealthCheckResult result = HealthCheckResult.healthy(capabilityId, name);
                result.setResponseTimeMs(responseTime);
                return result;
            }
            
            switch (status) {
                case REGISTERED:
                case PUBLISHED:
                    long responseTime = System.currentTimeMillis() - startTime;
                    HealthCheckResult result = HealthCheckResult.healthy(capabilityId, name);
                    result.setResponseTimeMs(responseTime);
                    return result;
                    
                case DISABLED:
                case PAUSED:
                    return HealthCheckResult.degraded(capabilityId, name, "能力已禁用/暂停");
                    
                case ERROR:
                    return HealthCheckResult.unhealthy(capabilityId, name, "能力处于错误状态");
                    
                case DEPRECATED:
                case ARCHIVED:
                    return HealthCheckResult.degraded(capabilityId, name, "能力已废弃/归档");
                    
                default:
                    return HealthCheckResult.unknown(capabilityId, name);
            }
            
        } catch (Exception e) {
            log.error("[performDefaultCheck] Check failed for {}: {}", capabilityId, e.getMessage());
            return HealthCheckResult.unhealthy(capabilityId, name, "检查异常: " + e.getMessage());
        }
    }
    
    public void clearCache() {
        cachedResults.clear();
        log.info("[clearCache] Health check cache cleared");
    }
    
    public void setCacheExpireMs(long cacheExpireMs) {
        this.cacheExpireMs = cacheExpireMs;
    }
}
