package net.ooder.mvp.skill.scene.capability.service.impl;

import net.ooder.mvp.skill.scene.capability.model.*;
import net.ooder.mvp.skill.scene.capability.registry.CapabilityRegistry;
import net.ooder.mvp.skill.scene.capability.service.CapabilityService;
import net.ooder.mvp.skill.scene.capability.service.CapabilityStateService;
import net.ooder.skills.api.SkillDiscoverer;
import net.ooder.skills.api.SkillRegistry;
import net.ooder.skills.api.SkillPackageManager;
import net.ooder.skills.api.SkillPackage;
import net.ooder.skills.api.InstalledSkill;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;

import jakarta.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class SeCapabilityServiceImpl implements CapabilityService {

    private static final Logger log = LoggerFactory.getLogger(SeCapabilityServiceImpl.class);

    private final SkillRegistry skillRegistry;
    private final SkillDiscoverer skillDiscoverer;
    private final SkillPackageManager packageManager;
    private final CapabilityStateService stateService;
    private final CapabilityRegistry localRegistry;
    private ApplicationEventPublisher eventPublisher;
    private boolean initialized = false;

    public SeCapabilityServiceImpl(SkillRegistry skillRegistry, 
                                    SkillDiscoverer skillDiscoverer,
                                    SkillPackageManager packageManager,
                                    CapabilityStateService stateService) {
        this.skillRegistry = skillRegistry;
        this.skillDiscoverer = skillDiscoverer;
        this.packageManager = packageManager;
        this.stateService = stateService;
        this.localRegistry = new CapabilityRegistry();
        log.info("[SeCapabilityServiceImpl] Created with SE SDK components");
    }
    
    public void setEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @PostConstruct
    public void init() {
        if (!initialized) {
            syncFromSeSdk();
            initialized = true;
            log.info("[init] SeCapabilityServiceImpl initialized with {} capabilities", localRegistry.size());
        }
    }

    private void syncFromSeSdk() {
        log.info("[syncFromSeSdk] Starting sync from SE SDK");
        
        try {
            if (skillDiscoverer != null) {
                CompletableFuture<List<SkillPackage>> future = skillDiscoverer.discover();
                List<SkillPackage> skills = future.get(30, java.util.concurrent.TimeUnit.SECONDS);
                log.info("[syncFromSeSdk] Discovered {} skills from SE SDK", skills.size());
                
                for (SkillPackage skill : skills) {
                    Capability cap = convertSkillPackageToCapability(skill);
                    if (cap != null) {
                        localRegistry.register(cap);
                    }
                }
            }
            
            if (skillRegistry != null) {
                List<InstalledSkill> registeredSkills = skillRegistry.getInstalledSkills();
                log.info("[syncFromSeSdk] Found {} registered skills", registeredSkills.size());
                
                for (InstalledSkill skill : registeredSkills) {
                    Capability cap = convertInstalledSkillToCapability(skill);
                    if (cap != null) {
                        localRegistry.register(cap);
                    }
                }
            }
            
            log.info("[syncFromSeSdk] Sync completed, total {} capabilities", localRegistry.size());
            
        } catch (Exception e) {
            log.error("[syncFromSeSdk] Failed to sync from SE SDK: {}", e.getMessage(), e);
        }
    }

    private Capability convertSkillPackageToCapability(SkillPackage skill) {
        if (skill == null || skill.getSkillId() == null) {
            return null;
        }
        
        Capability cap = new Capability();
        cap.setCapabilityId(skill.getSkillId());
        cap.setName(skill.getName());
        cap.setDescription(skill.getDescription());
        cap.setVersion(skill.getVersion());
        cap.setSkillId(skill.getSkillId());
        
        String category = skill.getCategory();
        if (category != null) {
            cap.setCapabilityCategory(CapabilityCategory.fromSeSdkCategory(category));
        }
        
        List<String> tags = skill.getTags();
        if (tags != null) {
            cap.setTags(tags);
        }
        
        List<String> dependencies = skill.getDependencies();
        if (dependencies != null) {
            cap.setDependencies(dependencies);
        }
        
        cap.setCreateTime(System.currentTimeMillis());
        cap.setUpdateTime(System.currentTimeMillis());
        cap.setStatus(CapabilityStatus.REGISTERED);
        cap.setInstalled(false);
        
        return cap;
    }

    private Capability convertInstalledSkillToCapability(InstalledSkill skill) {
        if (skill == null || skill.getSkillId() == null) {
            return null;
        }
        
        Capability cap = new Capability();
        cap.setCapabilityId(skill.getSkillId());
        cap.setName(skill.getName());
        cap.setVersion(skill.getVersion());
        cap.setSkillId(skill.getSkillId());
        cap.setSceneType(skill.getSceneId());
        
        cap.setCreateTime(skill.getInstallTime() > 0 ? skill.getInstallTime() : System.currentTimeMillis());
        cap.setUpdateTime(System.currentTimeMillis());
        cap.setStatus(CapabilityStatus.ENABLED);
        cap.setInstalled(skill.isActive());
        
        return cap;
    }

    private void checkInstallStatus(Capability cap) {
        if (packageManager != null && cap.getSkillId() != null) {
            try {
                CompletableFuture<Boolean> future = packageManager.isInstalled(cap.getSkillId());
                Boolean installed = future.get(5, java.util.concurrent.TimeUnit.SECONDS);
                cap.setInstalled(Boolean.TRUE.equals(installed));
            } catch (Exception e) {
                log.debug("[checkInstallStatus] Could not check install status for {}: {}", 
                    cap.getSkillId(), e.getMessage());
                cap.setInstalled(false);
            }
        } else {
            cap.setInstalled(false);
        }
    }

    @Override
    public Capability register(Capability capability) {
        if (capability == null || capability.getCapabilityId() == null) {
            throw new IllegalArgumentException("Capability and capabilityId must not be null");
        }

        capability.setUpdateTime(System.currentTimeMillis());
        if (capability.getCreateTime() == 0) {
            capability.setCreateTime(capability.getUpdateTime());
        }
        if (capability.getStatus() == null) {
            capability.setStatus(CapabilityStatus.REGISTERED);
        }

        localRegistry.register(capability);
        
        log.info("[register] Registered capability via SE SDK: {}", capability.getCapabilityId());
        return capability;
    }

    @Override
    public void unregister(String capabilityId) {
        localRegistry.unregister(capabilityId);
        log.info("[unregister] Unregistered capability: {}", capabilityId);
    }

    @Override
    public Capability findById(String capabilityId) {
        Capability cap = localRegistry.findById(capabilityId);
        if (cap == null) {
            syncFromSeSdk();
            cap = localRegistry.findById(capabilityId);
        }
        return cap;
    }

    @Override
    public List<Capability> findAll() {
        if (localRegistry.size() == 0) {
            syncFromSeSdk();
        }
        return localRegistry.findAll();
    }

    @Override
    public List<Capability> findByType(CapabilityType type) {
        return findAll().stream()
            .filter(cap -> cap.getCapabilityType() == type)
            .collect(Collectors.toList());
    }

    @Override
    public List<Capability> findBySceneType(String sceneType) {
        return findAll().stream()
            .filter(cap -> cap.supportsSceneType(sceneType))
            .collect(Collectors.toList());
    }

    @Override
    public List<Capability> search(String query) {
        return findAll().stream()
            .filter(cap -> 
                (cap.getName() != null && cap.getName().toLowerCase().contains(query.toLowerCase())) ||
                (cap.getCapabilityId() != null && cap.getCapabilityId().toLowerCase().contains(query.toLowerCase())) ||
                (cap.getDescription() != null && cap.getDescription().toLowerCase().contains(query.toLowerCase())))
            .collect(Collectors.toList());
    }

    @Override
    public Capability update(Capability capability) {
        Capability existing = localRegistry.findById(capability.getCapabilityId());
        if (existing == null) {
            throw new IllegalArgumentException("Capability not found: " + capability.getCapabilityId());
        }

        capability.setUpdateTime(System.currentTimeMillis());
        capability.setCreateTime(existing.getCreateTime());

        localRegistry.register(capability);
        log.info("[update] Updated capability: {}", capability.getCapabilityId());
        return capability;
    }

    @Override
    public void updateStatus(String capabilityId, String status) {
        Capability capability = localRegistry.findById(capabilityId);
        if (capability != null) {
            capability.setStatus(CapabilityStatus.valueOf(status));
            capability.setUpdateTime(System.currentTimeMillis());
            log.info("[updateStatus] Updated capability status: {} -> {}", capabilityId, status);
        }
    }

    @Override
    public Capability addSceneType(String capabilityId, String sceneType, String approvedBy) {
        Capability capability = localRegistry.findById(capabilityId);
        if (capability == null) {
            throw new IllegalArgumentException("Capability not found: " + capabilityId);
        }
        
        if (!capability.isDynamicSceneTypes()) {
            throw new IllegalStateException("Capability does not support dynamic scene types: " + capabilityId);
        }
        
        capability.addSceneType(sceneType);
        log.info("[addSceneType] Added scene type {} to capability {} by {}", sceneType, capabilityId, approvedBy);
        
        return capability;
    }

    @Override
    public Capability removeSceneType(String capabilityId, String sceneType, String approvedBy) {
        Capability capability = localRegistry.findById(capabilityId);
        if (capability == null) {
            throw new IllegalArgumentException("Capability not found: " + capabilityId);
        }
        
        if (!capability.isDynamicSceneTypes()) {
            throw new IllegalStateException("Capability does not support dynamic scene types: " + capabilityId);
        }
        
        capability.removeSceneType(sceneType);
        log.info("[removeSceneType] Removed scene type {} from capability {} by {}", sceneType, capabilityId, approvedBy);
        
        return capability;
    }

    @Override
    public List<Capability> findByOwnership(CapabilityOwnership ownership) {
        return findAll().stream()
            .filter(cap -> cap.getOwnership() == ownership)
            .collect(Collectors.toList());
    }

    @Override
    public List<Capability> findByOwnershipAndSceneType(CapabilityOwnership ownership, String sceneType) {
        return findAll().stream()
            .filter(cap -> cap.getOwnership() == ownership)
            .filter(cap -> cap.supportsSceneType(sceneType))
            .collect(Collectors.toList());
    }

    @Override
    public List<Capability> findBySkillForm(SkillForm form) {
        return findAll().stream()
            .filter(cap -> cap.getSkillForm() == form)
            .collect(Collectors.toList());
    }

    @Override
    public List<Capability> findBySceneTypeNew(SceneType sceneType) {
        return findAll().stream()
            .filter(cap -> sceneType.getCode().equals(cap.getSceneType()))
            .collect(Collectors.toList());
    }

    @Override
    public List<Capability> findByCapabilityCategory(CapabilityCategory category) {
        return findAll().stream()
            .filter(cap -> cap.getCapabilityCategory() == category)
            .collect(Collectors.toList());
    }

    @Override
    public List<Capability> findByFilters(SkillForm form, SceneType sceneType, CapabilityCategory category,
                                           CapabilityOwnership ownership, String keyword) {
        return findAll().stream()
            .filter(cap -> form == null || cap.getSkillForm() == form)
            .filter(cap -> sceneType == null || (cap.getSceneTypeEnum() != null && cap.getSceneTypeEnum() == sceneType))
            .filter(cap -> category == null || cap.getCapabilityCategory() == category)
            .filter(cap -> ownership == null || cap.getOwnership() == ownership)
            .filter(cap -> keyword == null || keyword.isEmpty() || 
                (cap.getName() != null && cap.getName().toLowerCase().contains(keyword.toLowerCase())) ||
                (cap.getCapabilityId() != null && cap.getCapabilityId().toLowerCase().contains(keyword.toLowerCase())) ||
                (cap.getDescription() != null && cap.getDescription().toLowerCase().contains(keyword.toLowerCase())))
            .collect(Collectors.toList());
    }

    @Override
    public void updateInstallStatus(String capabilityId, boolean installed) {
        Capability cap = localRegistry.findById(capabilityId);
        if (cap != null) {
            stateService.setInstalled(capabilityId, installed);
            log.info("[updateInstallStatus] Updated install status for {}: {}", capabilityId, installed);
        } else {
            log.warn("[updateInstallStatus] Capability not found: {}", capabilityId);
        }
    }
    
    @Override
    public boolean isInstalled(String capabilityId) {
        return stateService.isInstalled(capabilityId);
    }
    
    @Override
    public CapabilityStatus getCapabilityStatus(String capabilityId) {
        return stateService.getStatus(capabilityId);
    }
    
    public void refresh() {
        log.info("[refresh] Refreshing capabilities from SE SDK");
        localRegistry.clear();
        syncFromSeSdk();
    }
}
