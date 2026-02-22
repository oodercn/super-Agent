package net.ooder.skillcenter.resources;

import net.ooder.skillcenter.resources.model.ResourceLimit;
import net.ooder.skillcenter.resources.model.ResourceRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 资源分配器 - 符合v0.7.0协议规范
 */
@Component
public class ResourceAllocator {

    private static final Logger log = LoggerFactory.getLogger(ResourceAllocator.class);

    private final Map<String, ResourceRequest> allocations = new ConcurrentHashMap<>();
    private final Map<String, ResourceLimit> limits = new ConcurrentHashMap<>();
    
    private long totalCpuMillis = 4000;
    private long totalMemoryBytes = 8L * 1024 * 1024 * 1024;
    private long totalStorageBytes = 100L * 1024 * 1024 * 1024;
    
    private long allocatedCpuMillis = 0;
    private long allocatedMemoryBytes = 0;
    private long allocatedStorageBytes = 0;

    public ResourceAllocator() {
        limits.put("default", ResourceLimit.defaultLimit());
    }

    public boolean allocate(String skillId, ResourceRequest request) {
        if (request == null) {
            log.error("Null resource request for skill: {}", skillId);
            return false;
        }

        ResourceLimit limit = limits.getOrDefault("default", ResourceLimit.defaultLimit());
        if (!limit.isWithinLimits(request)) {
            log.error("Resource request exceeds limits for skill: {}", skillId);
            return false;
        }

        long cpuNeeded = request.getCpuMillis();
        long memoryNeeded = request.getMemoryBytes();
        long storageNeeded = request.getStorageBytes();

        if (allocatedCpuMillis + cpuNeeded > totalCpuMillis) {
            log.error("Insufficient CPU for skill: {}. Needed: {}, Available: {}", 
                    skillId, cpuNeeded, totalCpuMillis - allocatedCpuMillis);
            return false;
        }

        if (allocatedMemoryBytes + memoryNeeded > totalMemoryBytes) {
            log.error("Insufficient memory for skill: {}. Needed: {}, Available: {}", 
                    skillId, memoryNeeded, totalMemoryBytes - allocatedMemoryBytes);
            return false;
        }

        if (allocatedStorageBytes + storageNeeded > totalStorageBytes) {
            log.error("Insufficient storage for skill: {}. Needed: {}, Available: {}", 
                    skillId, storageNeeded, totalStorageBytes - allocatedStorageBytes);
            return false;
        }

        allocatedCpuMillis += cpuNeeded;
        allocatedMemoryBytes += memoryNeeded;
        allocatedStorageBytes += storageNeeded;
        allocations.put(skillId, request);

        log.info("Allocated resources for skill {}: CPU={}, Memory={}, Storage={}", 
                skillId, request.getCpu(), request.getMemory(), request.getStorage());
        return true;
    }

    public boolean release(String skillId) {
        ResourceRequest request = allocations.remove(skillId);
        if (request == null) {
            return false;
        }

        allocatedCpuMillis -= request.getCpuMillis();
        allocatedMemoryBytes -= request.getMemoryBytes();
        allocatedStorageBytes -= request.getStorageBytes();

        log.info("Released resources for skill: {}", skillId);
        return true;
    }

    public Map<String, Object> getAvailableResources() {
        Map<String, Object> available = new ConcurrentHashMap<>();
        available.put("cpu", totalCpuMillis - allocatedCpuMillis);
        available.put("memory", totalMemoryBytes - allocatedMemoryBytes);
        available.put("storage", totalStorageBytes - allocatedStorageBytes);
        return available;
    }

    public Map<String, Object> getAllocatedResources() {
        Map<String, Object> allocated = new ConcurrentHashMap<>();
        allocated.put("cpu", allocatedCpuMillis);
        allocated.put("memory", allocatedMemoryBytes);
        allocated.put("storage", allocatedStorageBytes);
        allocated.put("count", allocations.size());
        return allocated;
    }

    public ResourceRequest getAllocation(String skillId) {
        return allocations.get(skillId);
    }

    public void setLimit(String name, ResourceLimit limit) {
        limits.put(name, limit);
    }

    public ResourceLimit getLimit(String name) {
        return limits.get(name);
    }
}
