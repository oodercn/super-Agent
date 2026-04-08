package net.ooder.mvp.skill.scene.capability.install;

import net.ooder.scene.skill.capability.Capability;
import net.ooder.scene.skill.install.CapabilityInstallLifecycle;
import net.ooder.scene.skill.install.InstallContext;
import net.ooder.scene.skill.install.InstallResult;
import net.ooder.scene.core.persistence.InstallationPersistence;
import net.ooder.scene.skill.notification.NotificationService;
import net.ooder.mvp.skill.scene.llm.prompt.PromptIndexingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SceneCapabilityInstallLifecycle implements CapabilityInstallLifecycle {

    private static final Logger log = LoggerFactory.getLogger(SceneCapabilityInstallLifecycle.class);

    @Autowired(required = false)
    private InstallationPersistence installationPersistence;

    @Autowired(required = false)
    private NotificationService notificationService;

    @Autowired(required = false)
    private net.ooder.mvp.skill.scene.capability.service.CapabilityService capabilityService;

    @Autowired(required = false)
    private PromptIndexingService promptIndexingService;

    @Override
    public void onPreInstall(Capability capability, InstallContext context) {
        log.info("[onPreInstall] Capability: {}, Context: {}", 
            capability != null ? capability.getId() : "null", 
            context != null ? context.getInstallId() : "null");
        
        if (installationPersistence != null && context != null) {
            InstallationPersistence.InstallationState state = new InstallationPersistence.InstallationState();
            state.setInstallId(context.getInstallId());
            state.setUserId(context.getOperatorId());
            state.setPhase(InstallationPersistence.InstallationPhase.INITIALIZING);
            state.setTotalSteps(5);
            state.setCompletedSteps(0);
            state.setProgress(0.0);
            state.setCreateTime(System.currentTimeMillis());
            state.setUpdateTime(System.currentTimeMillis());
            
            Map<String, Object> contextMap = new HashMap<>();
            contextMap.put("capabilityId", capability != null ? capability.getId() : null);
            contextMap.put("capabilityName", capability != null ? capability.getName() : null);
            state.setContext(contextMap);
            
            installationPersistence.saveState(state);
            log.info("[onPreInstall] Saved initial state for install: {}", context.getInstallId());
        }
    }

    @Override
    public void onPostInstall(Capability capability, InstallResult result) {
        log.info("[onPostInstall] Capability: {}, Result: {}", 
            capability != null ? capability.getId() : "null",
            result != null ? result.isSuccess() : "null");
        
        if (installationPersistence != null && result != null) {
            Map<String, Object> updates = new HashMap<>();
            updates.put("phase", InstallationPersistence.InstallationPhase.COMPLETED);
            updates.put("progress", 1.0);
            updates.put("completedSteps", 5);
            updates.put("updateTime", System.currentTimeMillis());
            
            String installId = capability != null ? capability.getId() : "unknown";
            installationPersistence.updateState(installId, updates);
            log.info("[onPostInstall] Updated state to COMPLETED for install: {}", installId);
        }
        
        if (capabilityService != null && capability != null) {
            try {
                net.ooder.mvp.skill.scene.capability.model.Capability cap = 
                    capabilityService.findById(capability.getId());
                if (cap != null) {
                    capabilityService.updateInstallStatus(cap.getCapabilityId(), true);
                    log.info("[onPostInstall] Marked capability as installed: {}", capability.getId());
                }
            } catch (Exception e) {
                log.error("[onPostInstall] Failed to update capability status: {}", e.getMessage());
            }
        }
        
        if (promptIndexingService != null && capability != null && result != null && result.isSuccess()) {
            try {
                String skillId = capability.getId();
                PromptIndexingService.IndexingResult indexingResult = promptIndexingService.indexPromptsForSkill(skillId);
                log.info("[onPostInstall] Prompt indexing result for {}: indexed={}, failed={}", 
                    skillId, indexingResult.getIndexedCount(), indexingResult.getFailedCount());
            } catch (Exception e) {
                log.error("[onPostInstall] Failed to index prompts: {}", e.getMessage());
            }
        }
    }

    @Override
    public void onUninstall(Capability capability) {
        log.info("[onUninstall] Capability: {}", 
            capability != null ? capability.getId() : "null");
        
        if (capabilityService != null && capability != null) {
            try {
                net.ooder.mvp.skill.scene.capability.model.Capability cap = 
                    capabilityService.findById(capability.getId());
                if (cap != null) {
                    capabilityService.updateInstallStatus(cap.getCapabilityId(), false);
                    log.info("[onUninstall] Marked capability as uninstalled: {}", capability.getId());
                }
            } catch (Exception e) {
                log.error("[onUninstall] Failed to update capability status: {}", e.getMessage());
            }
        }
    }

    @Override
    public void onInstallFailed(Capability capability, Exception error) {
        log.error("[onInstallFailed] Capability: {}, Error: {}", 
            capability != null ? capability.getId() : "null",
            error != null ? error.getMessage() : "null");
        
        if (installationPersistence != null) {
            Map<String, Object> updates = new HashMap<>();
            updates.put("phase", InstallationPersistence.InstallationPhase.FAILED);
            updates.put("errorMessage", error != null ? error.getMessage() : "Unknown error");
            updates.put("updateTime", System.currentTimeMillis());
            
            String installId = capability != null ? capability.getId() + "-" + System.currentTimeMillis() : "unknown";
            installationPersistence.updateState(installId, updates);
            log.info("[onInstallFailed] Updated state to FAILED for install: {}", installId);
        }
        
        if (notificationService != null && capability != null) {
            try {
                String operatorId = "system";
                notificationService.push(
                    operatorId,
                    "安装失败",
                    "能力 " + capability.getName() + " 安装失败: " + (error != null ? error.getMessage() : "Unknown error"),
                    NotificationService.PushChannel.IN_APP
                );
                log.info("[onInstallFailed] Sent failure notification for: {}", capability.getId());
            } catch (Exception e) {
                log.error("[onInstallFailed] Failed to send notification: {}", e.getMessage());
            }
        }
    }
}
