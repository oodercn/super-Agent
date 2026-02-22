package net.ooder.skillcenter.sdk;

import net.ooder.scene.provider.*;
import net.ooder.skillcenter.config.SdkConfig;
import net.ooder.skillcenter.dto.HostingInstanceDTO;
import net.ooder.skillcenter.dto.PageResult;
import net.ooder.skillcenter.manager.HostingManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Primary
public class HostingSdkAdapterImpl implements HostingSdkAdapter {

    private static final Logger log = LoggerFactory.getLogger(HostingSdkAdapterImpl.class);

    @Autowired
    private SdkConfig sdkConfig;

    @Autowired
    private SceneEngineAdapter sceneEngineAdapter;

    private HostingProvider hostingProvider;
    private HostingManager hostingManager;
    private boolean sdkAvailable = false;

    @PostConstruct
    public void init() {
        log.info("[HostingSdkAdapter] Initializing...");
        hostingManager = HostingManager.getInstance();
        
        if (sceneEngineAdapter != null && sceneEngineAdapter.isAvailable()) {
            hostingProvider = sceneEngineAdapter.getHostingProvider();
            if (hostingProvider != null) {
                sdkAvailable = true;
                log.info("[HostingSdkAdapter] HostingProvider available from SceneEngine");
            }
        }
        
        if (!sdkAvailable) {
            log.info("[HostingSdkAdapter] Using HostingManager as fallback");
        }
    }

    private HostingInstanceDTO convertToDTO(HostingManager.HostingInstance instance) {
        if (instance == null) return null;
        HostingInstanceDTO dto = new HostingInstanceDTO();
        dto.setId(instance.getId());
        dto.setName(instance.getName());
        dto.setSkillId(instance.getSkillId());
        dto.setDescription(instance.getDescription());
        dto.setStatus(instance.getStatus());
        dto.setHealthStatus("running".equals(instance.getStatus()) ? "healthy" : "unhealthy");
        return dto;
    }

    @Override
    public List<HostingInstanceDTO> getAllInstances() {
        if (sdkAvailable && hostingProvider != null) {
            try {
                List<?> instances = hostingProvider.getAllInstances();
                return instances.stream()
                    .map(i -> new HostingInstanceDTO())
                    .collect(Collectors.toList());
            } catch (Exception e) {
                log.error("[HostingSdkAdapter] Failed to get instances from provider: {}", e.getMessage());
            }
        }

        log.debug("[HostingSdkAdapter] Getting all instances from HostingManager");
        return hostingManager.getAllHostingInstances().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Override
    public PageResult<HostingInstanceDTO> getInstances(int page, int size) {
        if (sdkAvailable && hostingProvider != null) {
            try {
                net.ooder.scene.core.PageResult<?> result = hostingProvider.getInstances(page, size);
                List<HostingInstanceDTO> dtoList = result.getList().stream()
                    .map(i -> new HostingInstanceDTO())
                    .collect(Collectors.toList());
                return new PageResult<>(dtoList, result.getTotal(), result.getPageNum(), result.getPageSize());
            } catch (Exception e) {
                log.error("[HostingSdkAdapter] Failed to get instances from provider: {}", e.getMessage());
            }
        }

        log.debug("[HostingSdkAdapter] Getting instances: page={}, size={}", page, size);
        List<HostingInstanceDTO> all = hostingManager.getAllHostingInstances().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        return paginate(all, page, size);
    }

    @Override
    public HostingInstanceDTO getInstance(String instanceId) {
        if (sdkAvailable && hostingProvider != null) {
            try {
                Object instance = hostingProvider.getInstance(instanceId);
                return instance != null ? new HostingInstanceDTO() : null;
            } catch (Exception e) {
                log.error("[HostingSdkAdapter] Failed to get instance from provider: {}", e.getMessage());
            }
        }

        log.debug("[HostingSdkAdapter] Getting instance: {}", instanceId);
        HostingManager.HostingInstance instance = hostingManager.getHostingInstance(instanceId);
        return instance != null ? convertToDTO(instance) : null;
    }

    @Override
    public HostingInstanceDTO createInstance(HostingInstanceDTO instance) {
        if (sdkAvailable && hostingProvider != null) {
            try {
                Object created = hostingProvider.createInstance(new HashMap<String, Object>());
                return created != null ? new HostingInstanceDTO() : null;
            } catch (Exception e) {
                log.error("[HostingSdkAdapter] Failed to create instance via provider: {}", e.getMessage());
            }
        }

        log.debug("[HostingSdkAdapter] Creating instance: {}", instance.getName());
        HostingManager.HostingInstance newInstance = new HostingManager.HostingInstance();
        newInstance.setName(instance.getName());
        newInstance.setSkillId(instance.getSkillId());
        newInstance.setDescription(instance.getDescription());
        newInstance.setStatus("stopped");
        
        HostingManager.HostingInstance created = hostingManager.createHostingInstance(newInstance);
        return convertToDTO(created);
    }

    @Override
    public HostingInstanceDTO updateInstance(String instanceId, HostingInstanceDTO instance) {
        if (sdkAvailable && hostingProvider != null) {
            try {
                Object existing = hostingProvider.getInstance(instanceId);
                if (existing == null) {
                    return null;
                }
                boolean updated = hostingProvider.updateInstance(existing);
                return updated ? new HostingInstanceDTO() : null;
            } catch (Exception e) {
                log.error("[HostingSdkAdapter] Failed to update instance via provider: {}", e.getMessage());
            }
        }

        log.debug("[HostingSdkAdapter] Updating instance: {}", instanceId);
        HostingManager.HostingInstance existing = hostingManager.getHostingInstance(instanceId);
        if (existing == null) {
            return null;
        }
        
        existing.setName(instance.getName());
        existing.setSkillId(instance.getSkillId());
        existing.setDescription(instance.getDescription());
        
        HostingManager.HostingInstance updated = hostingManager.updateHostingInstance(existing);
        return convertToDTO(updated);
    }

    @Override
    public boolean deleteInstance(String instanceId) {
        if (sdkAvailable && hostingProvider != null) {
            try {
                return hostingProvider.deleteInstance(instanceId);
            } catch (Exception e) {
                log.error("[HostingSdkAdapter] Failed to delete instance via provider: {}", e.getMessage());
            }
        }

        log.debug("[HostingSdkAdapter] Deleting instance: {}", instanceId);
        return hostingManager.deleteHostingInstance(instanceId);
    }

    @Override
    public boolean startInstance(String instanceId) {
        if (sdkAvailable && hostingProvider != null) {
            try {
                return hostingProvider.startInstance(instanceId);
            } catch (Exception e) {
                log.error("[HostingSdkAdapter] Failed to start instance via provider: {}", e.getMessage());
            }
        }

        log.debug("[HostingSdkAdapter] Starting instance: {}", instanceId);
        try {
            hostingManager.startHostingInstance(instanceId);
            return true;
        } catch (Exception e) {
            log.error("[HostingSdkAdapter] Failed to start instance: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public boolean stopInstance(String instanceId) {
        if (sdkAvailable && hostingProvider != null) {
            try {
                return hostingProvider.stopInstance(instanceId);
            } catch (Exception e) {
                log.error("[HostingSdkAdapter] Failed to stop instance via provider: {}", e.getMessage());
            }
        }

        log.debug("[HostingSdkAdapter] Stopping instance: {}", instanceId);
        try {
            hostingManager.stopHostingInstance(instanceId);
            return true;
        } catch (Exception e) {
            log.error("[HostingSdkAdapter] Failed to stop instance: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public boolean restartInstance(String instanceId) {
        if (sdkAvailable && hostingProvider != null) {
            try {
                return hostingProvider.restartInstance(instanceId);
            } catch (Exception e) {
                log.error("[HostingSdkAdapter] Failed to restart instance via provider: {}", e.getMessage());
            }
        }

        log.debug("[HostingSdkAdapter] Restarting instance: {}", instanceId);
        try {
            hostingManager.stopHostingInstance(instanceId);
            hostingManager.startHostingInstance(instanceId);
            return true;
        } catch (Exception e) {
            log.error("[HostingSdkAdapter] Failed to restart instance: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public String getInstanceStatus(String instanceId) {
        if (sdkAvailable && hostingProvider != null) {
            try {
                Object instance = hostingProvider.getInstance(instanceId);
                return instance != null ? "unknown" : null;
            } catch (Exception e) {
                log.error("[HostingSdkAdapter] Failed to get status via provider: {}", e.getMessage());
            }
        }

        log.debug("[HostingSdkAdapter] Getting instance status: {}", instanceId);
        HostingManager.HostingInstance instance = hostingManager.getHostingInstance(instanceId);
        return instance != null ? instance.getStatus() : null;
    }

    @Override
    public String getInstanceHealth(String instanceId) {
        if (sdkAvailable && hostingProvider != null) {
            try {
                InstanceHealth health = hostingProvider.getHealth(instanceId);
                return health != null ? health.getStatus() : "unknown";
            } catch (Exception e) {
                log.error("[HostingSdkAdapter] Failed to get health via provider: {}", e.getMessage());
            }
        }

        log.debug("[HostingSdkAdapter] Getting instance health: {}", instanceId);
        HostingManager.HostingInstance instance = hostingManager.getHostingInstance(instanceId);
        if (instance == null) {
            return "unknown";
        }
        return "running".equals(instance.getStatus()) ? "healthy" : "unhealthy";
    }

    @Override
    public HostingInstanceDTO scaleInstance(String instanceId, int replicas) {
        if (sdkAvailable && hostingProvider != null) {
            try {
                boolean success = hostingProvider.scaleInstance(instanceId, replicas);
                if (success) {
                    Object instance = hostingProvider.getInstance(instanceId);
                    return instance != null ? new HostingInstanceDTO() : null;
                }
                return null;
            } catch (Exception e) {
                log.error("[HostingSdkAdapter] Failed to scale instance via provider: {}", e.getMessage());
            }
        }

        log.debug("[HostingSdkAdapter] Scaling instance: {} to {} replicas", instanceId, replicas);
        HostingManager.HostingInstance instance = hostingManager.getHostingInstance(instanceId);
        return instance != null ? convertToDTO(instance) : null;
    }

    @Override
    public HostingInstanceDTO updateResources(String instanceId, double cpuLimit, long memoryLimit) {
        if (sdkAvailable && hostingProvider != null) {
            try {
                Object instance = hostingProvider.getInstance(instanceId);
                if (instance != null) {
                    boolean updated = hostingProvider.updateInstance(instance);
                    return updated ? new HostingInstanceDTO() : null;
                }
                return null;
            } catch (Exception e) {
                log.error("[HostingSdkAdapter] Failed to update resources via provider: {}", e.getMessage());
            }
        }

        log.debug("[HostingSdkAdapter] Updating resources for instance: {}", instanceId);
        HostingManager.HostingInstance instance = hostingManager.getHostingInstance(instanceId);
        return instance != null ? convertToDTO(instance) : null;
    }

    @Override
    public List<HostingInstanceDTO> getInstancesBySkill(String skillId) {
        log.debug("[HostingSdkAdapter] Getting instances by skill: {}", skillId);
        return getAllInstances().stream()
            .filter(i -> skillId.equals(i.getSkillId()))
            .collect(Collectors.toList());
    }

    @Override
    public List<HostingInstanceDTO> getInstancesByOwner(String owner) {
        log.debug("[HostingSdkAdapter] Getting instances by owner: {}", owner);
        return getAllInstances();
    }

    @Override
    public long getTotalInstances() {
        if (sdkAvailable && hostingProvider != null) {
            try {
                return hostingProvider.getAllInstances().size();
            } catch (Exception e) {
                log.error("[HostingSdkAdapter] Failed to get total from provider: {}", e.getMessage());
            }
        }
        return hostingManager.getAllHostingInstances().size();
    }

    @Override
    public long getRunningInstances() {
        if (sdkAvailable && hostingProvider != null) {
            try {
                return hostingProvider.getAllInstances().size();
            } catch (Exception e) {
                log.error("[HostingSdkAdapter] Failed to get running count from provider: {}", e.getMessage());
            }
        }
        return hostingManager.getAllHostingInstances().stream()
            .filter(i -> "running".equals(i.getStatus()))
            .count();
    }

    @Override
    public boolean isAvailable() {
        return sdkAvailable || hostingManager != null;
    }

    private <T> PageResult<T> paginate(List<T> list, int pageNum, int pageSize) {
        int total = list.size();
        int start = (pageNum - 1) * pageSize;
        int end = Math.min(start + pageSize, total);

        if (start >= total) {
            return PageResult.empty();
        }

        List<T> pageList = list.subList(start, end);
        return PageResult.of(pageList, total, pageNum, pageSize);
    }
}
