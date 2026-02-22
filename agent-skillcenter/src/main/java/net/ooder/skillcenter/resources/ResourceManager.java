package net.ooder.skillcenter.resources;

import net.ooder.skillcenter.resources.model.NetworkPolicy;
import net.ooder.skillcenter.resources.model.ResourceLimit;
import net.ooder.skillcenter.resources.model.ResourceRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 资源管理器 - 符合v0.7.0协议规范
 */
@Component
public class ResourceManager {

    private static final Logger log = LoggerFactory.getLogger(ResourceManager.class);

    @Autowired
    private ResourceAllocator allocator;

    public boolean requestResources(String skillId, ResourceRequest request) {
        if (request == null) {
            log.warn("Null resource request for skill: {}", skillId);
            return false;
        }

        log.info("Requesting resources for skill {}: CPU={}, Memory={}, Storage={}",
                skillId, request.getCpu(), request.getMemory(), request.getStorage());

        return allocator.allocate(skillId, request);
    }

    public boolean releaseResources(String skillId) {
        log.info("Releasing resources for skill: {}", skillId);
        return allocator.release(skillId);
    }

    public ResourceRequest getResourceAllocation(String skillId) {
        return allocator.getAllocation(skillId);
    }

    public Map<String, Object> getResourceStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("available", allocator.getAvailableResources());
        status.put("allocated", allocator.getAllocatedResources());
        return status;
    }

    public boolean validateResourceRequest(ResourceRequest request) {
        if (request == null) {
            return false;
        }

        if (request.getCpu() == null || request.getMemory() == null) {
            return false;
        }

        if (request.getCpuMillis() <= 0) {
            return false;
        }

        if (request.getMemoryBytes() <= 0) {
            return false;
        }

        return true;
    }

    public Map<String, Object> calculateResourceUsage(List<String> skillIds) {
        Map<String, Object> usage = new HashMap<>();
        long totalCpu = 0;
        long totalMemory = 0;
        long totalStorage = 0;

        for (String skillId : skillIds) {
            ResourceRequest request = allocator.getAllocation(skillId);
            if (request != null) {
                totalCpu += request.getCpuMillis();
                totalMemory += request.getMemoryBytes();
                totalStorage += request.getStorageBytes();
            }
        }

        usage.put("totalCpuMillis", totalCpu);
        usage.put("totalMemoryBytes", totalMemory);
        usage.put("totalStorageBytes", totalStorage);
        usage.put("skillCount", skillIds.size());

        return usage;
    }

    public void setResourceLimit(String name, ResourceLimit limit) {
        allocator.setLimit(name, limit);
        log.info("Set resource limit '{}': CPU={}, Memory={}, Storage={}",
                name, limit.getCpuLimit(), limit.getMemoryLimit(), limit.getStorageLimit());
    }

    public NetworkPolicy createDefaultNetworkPolicy() {
        NetworkPolicy policy = new NetworkPolicy();
        policy.setIngress(true);
        policy.setEgress(true);
        policy.addPort(8080, "TCP");
        return policy;
    }

    public ResourceRequest createDefaultResourceRequest() {
        return ResourceRequest.standard();
    }
}
