
package net.ooder.sdk.core.skill.installer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ooder.sdk.api.skill.InstallRequest;
import net.ooder.sdk.api.skill.InstallResult;
import net.ooder.sdk.api.skill.SkillPackage;

public class SkillInstaller {
    
    private static final Logger log = LoggerFactory.getLogger(SkillInstaller.class);
    
    private final InstallPipeline pipeline;
    private final RollbackManager rollbackManager;
    private final List<InstallListener> listeners;
    
    public SkillInstaller() {
        this.pipeline = new InstallPipeline();
        this.rollbackManager = new RollbackManager();
        this.listeners = new ArrayList<>();
        
        setupDefaultPipeline();
    }
    
    private void setupDefaultPipeline() {
        pipeline.addStage(new ValidationStage());
        pipeline.addStage(new DependencyCheckStage());
        pipeline.addStage(new DownloadStage());
        pipeline.addStage(new ExtractionStage());
        pipeline.addStage(new ConfigurationStage());
        pipeline.addStage(new VerificationStage());
    }
    
    public InstallResult install(InstallRequest request) {
        InstallContext context = new InstallContext();
        context.setRequest(request);
        context.setStartTime(System.currentTimeMillis());
        
        try {
            notifyListeners("start", context);
            
            pipeline.execute(context);
            
            context.setStatus(InstallStatus.COMPLETED);
            context.setEndTime(System.currentTimeMillis());
            
            notifyListeners("complete", context);
            
            log.info("Skill installed successfully: {}", request.getSkillId());
            
            return createSuccessResult(context);
            
        } catch (Exception e) {
            context.setStatus(InstallStatus.FAILED);
            context.setErrorMessage(e.getMessage());
            context.setEndTime(System.currentTimeMillis());
            
            log.error("Skill installation failed: {}", request.getSkillId(), e);
            
            rollbackManager.rollback(context);
            
            notifyListeners("failed", context);
            
            return createFailureResult(context, e);
        }
    }
    
    public InstallResult uninstall(String skillId) {
        InstallContext context = new InstallContext();
        context.setSkillId(skillId);
        context.setStatus(InstallStatus.UNINSTALLING);
        context.setStartTime(System.currentTimeMillis());
        
        try {
            notifyListeners("uninstall_start", context);
            
            rollbackManager.cleanup(skillId);
            
            context.setStatus(InstallStatus.UNINSTALLED);
            context.setEndTime(System.currentTimeMillis());
            
            notifyListeners("uninstall_complete", context);
            
            log.info("Skill uninstalled: {}", skillId);
            
            InstallResult result = new InstallResult();
            result.setSuccess(true);
            result.setSkillId(skillId);
            return result;
            
        } catch (Exception e) {
            context.setErrorMessage(e.getMessage());
            notifyListeners("uninstall_failed", context);
            
            InstallResult result = new InstallResult();
            result.setSuccess(false);
            result.setSkillId(skillId);
            result.setError(e.getMessage());
            return result;
        }
    }
    
    public void addInstallStage(InstallStage stage) {
        pipeline.addStage(stage);
    }
    
    public void addListener(InstallListener listener) {
        listeners.add(listener);
    }
    
    private void notifyListeners(String event, InstallContext context) {
        for (InstallListener listener : listeners) {
            try {
                listener.onInstallEvent(event, context);
            } catch (Exception e) {
                log.warn("Install listener error", e);
            }
        }
    }
    
    private InstallResult createSuccessResult(InstallContext context) {
        InstallResult result = new InstallResult();
        result.setSuccess(true);
        result.setSkillId(context.getRequest().getSkillId());
        result.setInstallPath(context.getInstallPath());
        return result;
    }
    
    private InstallResult createFailureResult(InstallContext context, Exception e) {
        InstallResult result = new InstallResult();
        result.setSuccess(false);
        result.setSkillId(context.getRequest().getSkillId());
        result.setError(e.getMessage());
        return result;
    }
    
    public interface InstallListener {
        void onInstallEvent(String event, InstallContext context);
    }
}
