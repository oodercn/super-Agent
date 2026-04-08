package net.ooder.mvp.skill.scene.capability.service.impl;

import net.ooder.mvp.skill.scene.capability.model.Capability;
import net.ooder.mvp.skill.scene.capability.model.CapabilityStatus;
import net.ooder.mvp.skill.scene.capability.model.CapabilityType;
import net.ooder.mvp.skill.scene.capability.model.SceneType;
import net.ooder.mvp.skill.scene.capability.model.SkillForm;
import net.ooder.mvp.skill.scene.capability.model.Visibility;
import net.ooder.mvp.skill.scene.capability.service.CapabilityService;
import net.ooder.mvp.skill.scene.capability.service.CapabilityStateService;
import net.ooder.mvp.skill.scene.capability.service.SceneSkillLifecycleService;
import net.ooder.mvp.skill.scene.dto.scene.SceneGroupConfigDTO;
import net.ooder.mvp.skill.scene.dto.scene.SceneGroupDTO;
import net.ooder.mvp.skill.scene.service.SceneGroupService;
import net.ooder.mvp.skill.scene.service.MenuAutoRegisterService;
import net.ooder.mvp.skill.scene.template.SceneTemplate;
import net.ooder.mvp.skill.scene.template.SceneTemplateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SceneSkillLifecycleServiceImpl implements SceneSkillLifecycleService {

    private static final Logger log = LoggerFactory.getLogger(SceneSkillLifecycleServiceImpl.class);

    @Autowired
    private CapabilityService capabilityService;
    
    @Autowired
    private CapabilityStateService capabilityStateService;
    
    @Autowired(required = false)
    private SceneGroupService sceneGroupService;
    
    @Autowired(required = false)
    private MenuAutoRegisterService menuAutoRegisterService;
    
    @Autowired(required = false)
    private SceneTemplateService sceneTemplateService;

    @Override
    public LifecycleResult activate(String capabilityId, String userId) {
        log.info("[activate] Activating capability: {} by user: {}", capabilityId, userId);
        
        Capability capability = capabilityService.findById(capabilityId);
        if (capability == null) {
            return LifecycleResult.failure(capabilityId, "Capability not found: " + capabilityId);
        }
        
        if (capability.getCapabilityType() != CapabilityType.SCENE) {
            return LifecycleResult.failure(capabilityId, "Only scene capabilities can be activated");
        }
        
        SkillForm skillForm = capability.getSkillForm() != null ? capability.getSkillForm() : SkillForm.PROVIDER;
        if (skillForm != SkillForm.SCENE) {
            return LifecycleResult.failure(capabilityId, "Invalid skill form: " + skillForm);
        }
        
        SceneType sceneType = capability.getSceneTypeEnum();
        boolean isInternal = capability.getVisibilityEnum() == Visibility.INTERNAL;
        
        CapabilityStatus currentStatus = getStatus(capabilityId);
        CapabilityStatus targetStatus = determineActivateStatus(sceneType, isInternal);
        
        if (currentStatus == targetStatus) {
            log.info("[activate] Capability {} already in target status: {}", capabilityId, targetStatus);
            LifecycleResult result = LifecycleResult.success(capabilityId, currentStatus, targetStatus);
            result.setSceneType(sceneType);
            result.setSkillForm(skillForm);
            result.setMessage("Already activated");
            result.setNextSteps(getNextSteps(targetStatus, sceneType));
            return result;
        }
        
        if (!canTransition(currentStatus, targetStatus, sceneType)) {
            return LifecycleResult.failure(capabilityId, 
                "Cannot activate from status: " + currentStatus + " to: " + targetStatus);
        }
        
        capabilityStateService.setStatus(capabilityId, targetStatus);
        capabilityStateService.setInstalled(capabilityId, true, userId, "activate");
        
        String sceneGroupId = autoCreateSceneGroup(capability, userId);
        
        LifecycleResult result = LifecycleResult.success(capabilityId, currentStatus, targetStatus);
        result.setSceneType(sceneType);
        result.setSkillForm(skillForm);
        result.setSceneGroupId(sceneGroupId);
        result.setMessage("Activated successfully" + (sceneGroupId != null ? ", SceneGroup: " + sceneGroupId : ""));
        result.setNextSteps(getNextSteps(targetStatus, sceneType));
        
        return result;
    }
    
    private String autoCreateSceneGroup(Capability capability, String userId) {
        if (sceneGroupService == null) {
            log.warn("[autoCreateSceneGroup] SceneGroupService not available");
            return null;
        }
        
        String existingSceneGroupId = capabilityStateService.getSceneGroupId(capability.getCapabilityId());
        if (existingSceneGroupId != null) {
            SceneGroupDTO existingGroup = sceneGroupService.get(existingSceneGroupId);
            if (existingGroup != null) {
                log.info("[autoCreateSceneGroup] Reusing existing SceneGroup: {} for capability: {}", 
                    existingSceneGroupId, capability.getCapabilityId());
                
                if (menuAutoRegisterService != null) {
                    try {
                        String templateId = findTemplateForSkill(capability.getCapabilityId());
                        if (templateId != null) {
                            menuAutoRegisterService.registerMenusOnActivation(
                                existingSceneGroupId,
                                templateId,
                                userId != null ? userId : "system",
                                "MANAGER"
                            );
                            log.info("[autoCreateSceneGroup] Menus re-registered for SceneGroup: {}", 
                                existingSceneGroupId);
                        }
                    } catch (Exception e) {
                        log.warn("[autoCreateSceneGroup] Failed to re-register menus: {}", e.getMessage());
                    }
                }
                
                return existingSceneGroupId;
            }
        }
        
        String templateId = findTemplateForSkill(capability.getCapabilityId());
        if (templateId == null) {
            log.info("[autoCreateSceneGroup] No template found for skill: {}", capability.getCapabilityId());
            return null;
        }
        
        try {
            SceneGroupConfigDTO config = new SceneGroupConfigDTO();
            config.setName(capability.getName() + " 场景组");
            config.setCreatorId(userId != null ? userId : "system");
            config.setCreatorType(SceneGroupConfigDTO.CreatorType.USER);
            config.setMinMembers(1);
            config.setMaxMembers(100);
            
            SceneGroupDTO sceneGroup = sceneGroupService.create(templateId, config);
            if (sceneGroup != null) {
                log.info("[autoCreateSceneGroup] Created SceneGroup: {} for skill: {}", 
                    sceneGroup.getSceneGroupId(), capability.getCapabilityId());
                
                capabilityStateService.setSceneGroupId(capability.getCapabilityId(), sceneGroup.getSceneGroupId());
                
                if (menuAutoRegisterService != null) {
                    try {
                        menuAutoRegisterService.registerMenusOnActivation(
                            sceneGroup.getSceneGroupId(),
                            templateId,
                            userId != null ? userId : "system",
                            "MANAGER"
                        );
                        log.info("[autoCreateSceneGroup] Menus registered for SceneGroup: {}", 
                            sceneGroup.getSceneGroupId());
                    } catch (Exception e) {
                        log.warn("[autoCreateSceneGroup] Failed to register menus: {}", e.getMessage());
                    }
                }
                
                return sceneGroup.getSceneGroupId();
            }
        } catch (Exception e) {
            log.error("[autoCreateSceneGroup] Failed to create SceneGroup: {}", e.getMessage());
        }
        
        return null;
    }
    
    private String findTemplateForSkill(String skillId) {
        if (sceneTemplateService == null) {
            return null;
        }
        
        try {
            List<SceneTemplate> templates = sceneTemplateService.listTemplates();
            if (templates != null) {
                for (SceneTemplate template : templates) {
                    if (template.getMetadata() != null) {
                        String templateId = template.getMetadata().getId();
                        if (template.getSpec() != null && template.getSpec().getSkills() != null) {
                            for (SceneTemplate.SkillRef skillRef : template.getSpec().getSkills()) {
                                if (skillId.equals(skillRef.getId())) {
                                    log.info("[findTemplateForSkill] Found template {} for skill {}", 
                                        templateId, skillId);
                                    return templateId;
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.warn("[findTemplateForSkill] Failed to find template: {}", e.getMessage());
        }
        
        return null;
    }

    @Override
    public LifecycleResult pause(String capabilityId, String userId) {
        log.info("[pause] Pausing capability: {} by user: {}", capabilityId, userId);
        
        Capability capability = capabilityService.findById(capabilityId);
        if (capability == null) {
            return LifecycleResult.failure(capabilityId, "Capability not found: " + capabilityId);
        }
        
        SceneType sceneType = capability.getSceneTypeEnum();
        if (sceneType == SceneType.AUTO && capability.getVisibilityEnum() == Visibility.INTERNAL) {
            return LifecycleResult.failure(capabilityId, "Internal AUTO scenes do not support pause");
        }
        
        CapabilityStatus currentStatus = getStatus(capabilityId);
        if (currentStatus != CapabilityStatus.RUNNING && currentStatus != CapabilityStatus.SCHEDULED) {
            return LifecycleResult.failure(capabilityId, "Can only pause from RUNNING or SCHEDULED status");
        }
        
        capabilityStateService.setStatus(capabilityId, CapabilityStatus.PAUSED);
        
        LifecycleResult result = LifecycleResult.success(capabilityId, currentStatus, CapabilityStatus.PAUSED);
        result.setSceneType(sceneType);
        result.setMessage("Paused successfully");
        
        return result;
    }

    @Override
    public LifecycleResult deactivate(String capabilityId, String userId) {
        log.info("[deactivate] Deactivating capability: {} by user: {}", capabilityId, userId);
        
        Capability capability = capabilityService.findById(capabilityId);
        if (capability == null) {
            return LifecycleResult.failure(capabilityId, "Capability not found: " + capabilityId);
        }
        
        CapabilityStatus currentStatus = getStatus(capabilityId);
        
        if (currentStatus == CapabilityStatus.REGISTERED || currentStatus == CapabilityStatus.DRAFT) {
            return LifecycleResult.failure(capabilityId, "Capability is not activated: " + currentStatus);
        }
        
        String sceneGroupId = capabilityStateService.getSceneGroupId(capabilityId);
        
        if (sceneGroupId != null && menuAutoRegisterService != null) {
            try {
                menuAutoRegisterService.removeMenusOnSceneDestroy(sceneGroupId, userId);
                log.info("[deactivate] Removed menus for SceneGroup: {}", sceneGroupId);
            } catch (Exception e) {
                log.warn("[deactivate] Failed to remove menus: {}", e.getMessage());
            }
        }
        
        capabilityStateService.setStatus(capabilityId, CapabilityStatus.REGISTERED);
        
        LifecycleResult result = LifecycleResult.success(capabilityId, currentStatus, CapabilityStatus.REGISTERED);
        result.setSceneGroupId(sceneGroupId);
        result.setMessage("Deactivated successfully");
        
        return result;
    }

    @Override
    public LifecycleResult resume(String capabilityId, String userId) {
        log.info("[resume] Resuming capability: {} by user: {}", capabilityId, userId);
        
        Capability capability = capabilityService.findById(capabilityId);
        if (capability == null) {
            return LifecycleResult.failure(capabilityId, "Capability not found: " + capabilityId);
        }
        
        SceneType sceneType = capability.getSceneTypeEnum();
        boolean isInternal = capability.getVisibilityEnum() == Visibility.INTERNAL;
        
        CapabilityStatus currentStatus = getStatus(capabilityId);
        if (currentStatus != CapabilityStatus.PAUSED) {
            return LifecycleResult.failure(capabilityId, "Can only resume from PAUSED status");
        }
        
        CapabilityStatus targetStatus = (sceneType == SceneType.AUTO && isInternal) 
            ? CapabilityStatus.SCHEDULED : CapabilityStatus.RUNNING;
        
        capabilityStateService.setStatus(capabilityId, targetStatus);
        
        LifecycleResult result = LifecycleResult.success(capabilityId, currentStatus, targetStatus);
        result.setSceneType(sceneType);
        result.setMessage("Resumed successfully");
        
        return result;
    }

    @Override
    public LifecycleResult trigger(String capabilityId, TriggerRequest request) {
        log.info("[trigger] Triggering capability: {} with action: {}", capabilityId, request.getAction());
        
        Capability capability = capabilityService.findById(capabilityId);
        if (capability == null) {
            return LifecycleResult.failure(capabilityId, "Capability not found: " + capabilityId);
        }
        
        SceneType sceneType = capability.getSceneTypeEnum();
        if (sceneType != SceneType.TRIGGER) {
            return LifecycleResult.failure(capabilityId, "Only TRIGGER scenes can be manually triggered");
        }
        
        CapabilityStatus currentStatus = getStatus(capabilityId);
        if (currentStatus != CapabilityStatus.PENDING && currentStatus != CapabilityStatus.WAITING) {
            return LifecycleResult.failure(capabilityId, 
                "Can only trigger from PENDING or WAITING status, current: " + currentStatus);
        }
        
        capabilityStateService.setStatus(capabilityId, CapabilityStatus.RUNNING);
        
        LifecycleResult result = LifecycleResult.success(capabilityId, currentStatus, CapabilityStatus.RUNNING);
        result.setSceneType(sceneType);
        result.setMessage("Triggered successfully with action: " + request.getAction());
        
        return result;
    }

    @Override
    public LifecycleResult archive(String capabilityId, String userId) {
        log.info("[archive] Archiving capability: {} by user: {}", capabilityId, userId);
        
        Capability capability = capabilityService.findById(capabilityId);
        if (capability == null) {
            return LifecycleResult.failure(capabilityId, "Capability not found: " + capabilityId);
        }
        
        SceneType sceneType = capability.getSceneTypeEnum();
        
        CapabilityStatus currentStatus = getStatus(capabilityId);
        if (currentStatus != CapabilityStatus.COMPLETED && currentStatus != CapabilityStatus.PAUSED) {
            return LifecycleResult.failure(capabilityId, 
                "Can only archive from COMPLETED or PAUSED status");
        }
        
        capabilityStateService.setStatus(capabilityId, CapabilityStatus.ARCHIVED);
        
        LifecycleResult result = LifecycleResult.success(capabilityId, currentStatus, CapabilityStatus.ARCHIVED);
        result.setSceneType(sceneType);
        result.setMessage("Archived successfully");
        
        return result;
    }

    @Override
    public LifecycleState getState(String capabilityId) {
        Capability capability = capabilityService.findById(capabilityId);
        if (capability == null) {
            return null;
        }
        
        SceneType sceneType = capability.getSceneType() != null ? SceneType.valueOf(capability.getSceneType()) : null;
        CapabilityStatus status = getStatus(capabilityId);
        
        LifecycleState state = new LifecycleState();
        state.setCapabilityId(capabilityId);
        state.setStatus(status);
        state.setSceneType(sceneType);
        state.setCanPause(canPause(status, sceneType));
        state.setCanResume(canResume(status, sceneType));
        state.setCanTrigger(canTrigger(status, sceneType));
        state.setCanArchive(canArchive(status, sceneType));
        state.setAvailableTransitions(getAvailableTransitions(capabilityId));
        state.setContext(new HashMap<String, Object>());
        
        return state;
    }

    @Override
    public List<LifecycleTransition> getAvailableTransitions(String capabilityId) {
        List<LifecycleTransition> transitions = new ArrayList<LifecycleTransition>();
        
        Capability capability = capabilityService.findById(capabilityId);
        if (capability == null) {
            return transitions;
        }
        
        SceneType sceneType = capability.getSceneType() != null ? SceneType.valueOf(capability.getSceneType()) : null;
        CapabilityStatus currentStatus = getStatus(capabilityId);
        
        if (canTransition(currentStatus, CapabilityStatus.SCHEDULED, sceneType)) {
            transitions.add(createTransition("activate", "激活", "激活场景", CapabilityStatus.SCHEDULED));
        }
        
        if (canPause(currentStatus, sceneType)) {
            transitions.add(createTransition("pause", "暂停", "暂停场景运行", CapabilityStatus.PAUSED));
        }
        
        if (canResume(currentStatus, sceneType)) {
            transitions.add(createTransition("resume", "恢复", "恢复场景运行", CapabilityStatus.RUNNING));
        }
        
        if (canTrigger(currentStatus, sceneType)) {
            transitions.add(createTransition("trigger", "触发", "手动触发场景", CapabilityStatus.RUNNING));
        }
        
        if (canArchive(currentStatus, sceneType)) {
            transitions.add(createTransition("archive", "归档", "归档场景记录", CapabilityStatus.ARCHIVED));
        }
        
        return transitions;
    }

    private CapabilityStatus getStatus(String capabilityId) {
        return capabilityStateService.getStatus(capabilityId);
    }

    private CapabilityStatus determineActivateStatus(SceneType sceneType, boolean isInternal) {
        if (sceneType == SceneType.AUTO) {
            return isInternal ? CapabilityStatus.INITIALIZING : CapabilityStatus.SCHEDULED;
        } else if (sceneType == SceneType.TRIGGER) {
            return CapabilityStatus.PENDING;
        }
        return CapabilityStatus.DRAFT;
    }

    private boolean canTransition(CapabilityStatus from, CapabilityStatus to, SceneType sceneType) {
        if (from == to) {
            return false;
        }
        
        switch (from) {
            case REGISTERED:
                return to == CapabilityStatus.DRAFT 
                    || to == CapabilityStatus.PENDING 
                    || to == CapabilityStatus.SCHEDULED 
                    || to == CapabilityStatus.INITIALIZING;
            case DRAFT:
                return to == CapabilityStatus.PENDING 
                    || to == CapabilityStatus.SCHEDULED 
                    || to == CapabilityStatus.INITIALIZING;
            case PENDING:
                return to == CapabilityStatus.WAITING 
                    || to == CapabilityStatus.RUNNING;
            case SCHEDULED:
                return to == CapabilityStatus.RUNNING 
                    || to == CapabilityStatus.PAUSED 
                    || to == CapabilityStatus.ERROR;
            case RUNNING:
                return to == CapabilityStatus.COMPLETED 
                    || to == CapabilityStatus.PAUSED 
                    || to == CapabilityStatus.ERROR 
                    || to == CapabilityStatus.SCHEDULED;
            case PAUSED:
                return to == CapabilityStatus.RUNNING 
                    || to == CapabilityStatus.SCHEDULED 
                    || to == CapabilityStatus.ARCHIVED;
            case ERROR:
                return to == CapabilityStatus.RUNNING 
                    || to == CapabilityStatus.DRAFT 
                    || to == CapabilityStatus.ARCHIVED;
            case WAITING:
                return to == CapabilityStatus.RUNNING 
                    || to == CapabilityStatus.PAUSED 
                    || to == CapabilityStatus.COMPLETED;
            case INITIALIZING:
                return to == CapabilityStatus.SCHEDULED 
                    || to == CapabilityStatus.ERROR;
            default:
                return false;
        }
    }

    private boolean canPause(CapabilityStatus status, SceneType sceneType) {
        return status == CapabilityStatus.RUNNING || status == CapabilityStatus.SCHEDULED;
    }

    private boolean canResume(CapabilityStatus status, SceneType sceneType) {
        return status == CapabilityStatus.PAUSED;
    }

    private boolean canTrigger(CapabilityStatus status, SceneType sceneType) {
        return sceneType == SceneType.TRIGGER 
            && (status == CapabilityStatus.PENDING || status == CapabilityStatus.WAITING);
    }

    private boolean canArchive(CapabilityStatus status, SceneType sceneType) {
        return status == CapabilityStatus.COMPLETED || status == CapabilityStatus.PAUSED;
    }
    
    private LifecycleTransition createTransition(String action, String name, String description, 
            CapabilityStatus targetStatus) {
        LifecycleTransition transition = new LifecycleTransition();
        transition.setAction(action);
        transition.setName(name);
        transition.setDescription(description);
        transition.setTargetStatus(targetStatus);
        transition.setRequiresPermission(true);
        
        List<String> permissions = new ArrayList<String>();
        if ("activate".equals(action) || "archive".equals(action)) {
            permissions.add("activate");
        } else if ("pause".equals(action) || "resume".equals(action)) {
            permissions.add("configure");
        } else if ("trigger".equals(action)) {
            permissions.add("participate");
        }
        transition.setRequiredPermissions(permissions);
        
        return transition;
    }

    private List<String> getNextSteps(CapabilityStatus status, SceneType sceneType) {
        List<String> steps = new ArrayList<String>();
        
        switch (status) {
            case SCHEDULED:
                steps.add("等待定时触发");
                steps.add("执行能力调用链");
                break;
            case INITIALIZING:
                steps.add("初始化驱动能力");
                steps.add("初始化子能力");
                break;
            case PENDING:
                steps.add("等待人工触发");
                steps.add("或通过API触发");
                break;
            default:
                break;
        }
        
        return steps;
    }
}
