package net.ooder.mvp.skill.scene.capability.service.impl;

import net.ooder.mvp.skill.scene.capability.event.SceneTypeUpdateEvent;
import net.ooder.mvp.skill.scene.capability.model.Capability;
import net.ooder.mvp.skill.scene.capability.model.CapabilityOwnership;
import net.ooder.mvp.skill.scene.capability.model.CapabilityStatus;
import net.ooder.mvp.skill.scene.capability.model.CapabilityType;
import net.ooder.mvp.skill.scene.capability.model.SceneType;
import net.ooder.mvp.skill.scene.capability.model.SkillForm;
import net.ooder.mvp.skill.scene.capability.model.CapabilityCategory;
import net.ooder.mvp.skill.scene.capability.registry.CapabilityRegistry;
import net.ooder.mvp.skill.scene.capability.service.CapabilityService;
import net.ooder.mvp.skill.scene.capability.service.CapabilityStateService;
import net.ooder.skill.common.storage.JsonStorageService;
import net.ooder.skills.api.SkillRegistry;
import net.ooder.skills.api.InstalledSkill;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CapabilityServiceImpl implements CapabilityService {

    private static final Logger log = LoggerFactory.getLogger(CapabilityServiceImpl.class);
    private static final String STORAGE_KEY = "capabilities";

    private final CapabilityRegistry registry;
    private final JsonStorageService storageService;
    private final CapabilityStateService stateService;
    private ApplicationEventPublisher eventPublisher;
    private boolean initialized = false;
    private boolean batchMode = false;
    
    @Autowired(required = false)
    private SkillRegistry skillRegistry;
    
    @Value("${ooder.capability.use-se-sdk:true}")
    private boolean useSeSdk;

    public CapabilityServiceImpl(JsonStorageService storageService, CapabilityStateService stateService) {
        this.storageService = storageService;
        this.stateService = stateService;
        this.registry = new CapabilityRegistry();
    }
    
    @PostConstruct
    public void init() {
        if (!initialized) {
            loadFromStorage();
            initialized = true;
            log.info("[init] CapabilityServiceImpl initialized with {} capabilities", registry.size());
        }
    }
    
    public void setEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    private void loadFromStorage() {
        if (useSeSdk && skillRegistry != null) {
            try {
                List<InstalledSkill> skills = skillRegistry.getInstalledSkills();
                if (skills != null && !skills.isEmpty()) {
                    for (InstalledSkill skill : skills) {
                        Capability cap = convertInstalledSkillToCapability(skill);
                        registry.register(cap);
                    }
                    log.info("[loadFromStorage] Loaded {} capabilities from SE SDK SkillRegistry", skills.size());
                    return;
                }
            } catch (Exception e) {
                log.warn("[loadFromStorage] Failed to load from SE SDK: {}, falling back to JSON", e.getMessage());
            }
        }
        
        try {
            List<Capability> stored = storageService.loadList(STORAGE_KEY, Capability.class);
            if (stored != null) {
                for (Capability cap : stored) {
                    registry.register(cap);
                }
                log.info("[loadFromStorage] Loaded {} capabilities from JSON storage", stored.size());
            }
        } catch (Exception e) {
            log.warn("[loadFromStorage] Failed to load capabilities from storage: {}", e.getMessage());
        }
    }
    
    private Capability convertInstalledSkillToCapability(InstalledSkill skill) {
        Capability cap = new Capability();
        cap.setCapabilityId(skill.getSkillId());
        cap.setName(skill.getName());
        cap.setVersion(skill.getVersion());
        cap.setSkillId(skill.getSkillId());
        cap.setSceneType(skill.getSceneId());
        cap.setStatus(CapabilityStatus.REGISTERED);
        cap.setDependencies(skill.getDependencies());
        cap.setInstalled(true);
        cap.setCreateTime(skill.getInstallTime() > 0 ? skill.getInstallTime() : System.currentTimeMillis());
        cap.setUpdateTime(System.currentTimeMillis());
        return cap;
    }

    private void saveToStorage() {
        if (batchMode) {
            log.debug("[saveToStorage] Batch mode enabled, skipping save");
            return;
        }
        try {
            storageService.saveList(STORAGE_KEY, registry.findAll());
        } catch (Exception e) {
            log.error("Failed to save capabilities to storage: {}", e.getMessage());
        }
    }
    
    public void startBatchMode() {
        this.batchMode = true;
        log.debug("[startBatchMode] Batch mode started");
    }
    
    public void endBatchMode() {
        this.batchMode = false;
        saveToStorage();
        log.debug("[endBatchMode] Batch mode ended, saved {} capabilities", registry.size());
    }

    private void initDefaultCapabilities() {
        if (registry.size() == 0) {
            log.info("No capabilities found in storage, will be synced from skill.yaml files");
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

        registry.register(capability);
        saveToStorage();
        log.info("Registered capability: {}", capability.getCapabilityId());
        return capability;
    }

    @Override
    public void unregister(String capabilityId) {
        registry.unregister(capabilityId);
        saveToStorage();
        log.info("Unregistered capability: {}", capabilityId);
    }

    @Override
    public Capability findById(String capabilityId) {
        return registry.findById(capabilityId);
    }

    @Override
    public List<Capability> findAll() {
        return registry.findAll();
    }

    @Override
    public List<Capability> findByType(CapabilityType type) {
        return registry.findByType(type);
    }

    @Override
    public List<Capability> findBySceneType(String sceneType) {
        return registry.findBySceneType(sceneType);
    }

    @Override
    public List<Capability> search(String query) {
        return registry.search(query);
    }

    @Override
    public Capability update(Capability capability) {
        Capability existing = registry.findById(capability.getCapabilityId());
        if (existing == null) {
            throw new IllegalArgumentException("Capability not found: " + capability.getCapabilityId());
        }

        capability.setUpdateTime(System.currentTimeMillis());
        capability.setCreateTime(existing.getCreateTime());

        registry.register(capability);
        saveToStorage();
        log.info("Updated capability: {}", capability.getCapabilityId());
        return capability;
    }

    @Override
    public void updateStatus(String capabilityId, String status) {
        Capability capability = registry.findById(capabilityId);
        if (capability != null) {
            capability.setStatus(CapabilityStatus.valueOf(status));
            capability.setUpdateTime(System.currentTimeMillis());
            saveToStorage();
            log.info("Updated capability status: {} -> {}", capabilityId, status);
        }
    }

    @Override
    public Capability addSceneType(String capabilityId, String sceneType, String approvedBy) {
        Capability capability = registry.findById(capabilityId);
        if (capability == null) {
            throw new IllegalArgumentException("Capability not found: " + capabilityId);
        }
        
        if (!capability.isDynamicSceneTypes()) {
            throw new IllegalStateException("Capability does not support dynamic scene types: " + capabilityId);
        }
        
        capability.addSceneType(sceneType);
        saveToStorage();
        log.info("Added scene type {} to capability {} by {}", sceneType, capabilityId, approvedBy);
        
        publishSceneTypeUpdateEvent(capability, SceneTypeUpdateEvent.UpdateAction.ADD, sceneType, approvedBy);
        
        return capability;
    }

    @Override
    public Capability removeSceneType(String capabilityId, String sceneType, String approvedBy) {
        Capability capability = registry.findById(capabilityId);
        if (capability == null) {
            throw new IllegalArgumentException("Capability not found: " + capabilityId);
        }
        
        if (!capability.isDynamicSceneTypes()) {
            throw new IllegalStateException("Capability does not support dynamic scene types: " + capabilityId);
        }
        
        capability.removeSceneType(sceneType);
        saveToStorage();
        log.info("Removed scene type {} from capability {} by {}", sceneType, capabilityId, approvedBy);
        
        publishSceneTypeUpdateEvent(capability, SceneTypeUpdateEvent.UpdateAction.REMOVE, sceneType, approvedBy);
        
        return capability;
    }
    
    private void publishSceneTypeUpdateEvent(Capability capability, SceneTypeUpdateEvent.UpdateAction action, 
                                             String sceneType, String approvedBy) {
        if (eventPublisher != null) {
            List<String> updatedSceneTypes = new ArrayList<>(capability.getSupportedSceneTypes());
            SceneTypeUpdateEvent event = new SceneTypeUpdateEvent(
                capability.getCapabilityId(),
                capability.getName(),
                action,
                sceneType,
                updatedSceneTypes,
                approvedBy
            );
            eventPublisher.publishEvent(event);
            log.debug("Published scene type update event: {}", event);
        }
    }

    @Override
    public List<Capability> findByOwnership(CapabilityOwnership ownership) {
        return registry.findAll().stream()
            .filter(cap -> cap.getOwnership() == ownership)
            .collect(Collectors.toList());
    }

    @Override
    public List<Capability> findByOwnershipAndSceneType(CapabilityOwnership ownership, String sceneType) {
        return registry.findAll().stream()
            .filter(cap -> cap.getOwnership() == ownership)
            .filter(cap -> cap.supportsSceneType(sceneType))
            .collect(Collectors.toList());
    }

    @Override
    public List<Capability> findBySkillForm(SkillForm form) {
        return registry.findAll().stream()
            .filter(cap -> {
                SkillForm capForm = cap.getSkillForm();
                return capForm != null && capForm == form;
            })
            .collect(Collectors.toList());
    }

    @Override
    public List<Capability> findBySceneTypeNew(SceneType sceneType) {
        return registry.findAll().stream()
            .filter(cap -> {
                String capSceneType = cap.getSceneType();
                return capSceneType != null && capSceneType.equals(sceneType.getCode());
            })
            .collect(Collectors.toList());
    }

    @Override
    public List<Capability> findByCapabilityCategory(CapabilityCategory category) {
        return registry.findAll().stream()
            .filter(cap -> {
                if (cap.getCapabilityCategory() == null) return false;
                return cap.getCapabilityCategory() == category;
            })
            .collect(Collectors.toList());
    }

    @Override
    public List<Capability> findByFilters(SkillForm form, SceneType sceneType, CapabilityCategory category,
                                           CapabilityOwnership ownership, String keyword) {
        return registry.findAll().stream()
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
        Capability cap = registry.findById(capabilityId);
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
}
