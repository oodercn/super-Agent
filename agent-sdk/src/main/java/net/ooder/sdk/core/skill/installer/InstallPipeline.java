
package net.ooder.sdk.core.skill.installer;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InstallPipeline {
    
    private static final Logger log = LoggerFactory.getLogger(InstallPipeline.class);
    
    private final List<InstallStage> stages;
    
    public InstallPipeline() {
        this.stages = new ArrayList<>();
    }
    
    public void addStage(InstallStage stage) {
        stages.add(stage);
        log.debug("Added install stage: {}", stage.getName());
    }
    
    public void removeStage(String name) {
        stages.removeIf(s -> s.getName().equals(name));
    }
    
    public void execute(InstallContext context) throws Exception {
        log.info("Starting install pipeline for: {}", context.getSkillId());
        
        for (InstallStage stage : stages) {
            if (context.getStatus() == InstallStatus.FAILED) {
                break;
            }
            
            log.debug("Executing stage: {}", stage.getName());
            
            try {
                stage.execute(context);
                log.debug("Stage completed: {}", stage.getName());
            } catch (Exception e) {
                context.setStatus(InstallStatus.FAILED);
                context.setErrorMessage("Stage '" + stage.getName() + "' failed: " + e.getMessage());
                throw e;
            }
        }
        
        if (context.getStatus() != InstallStatus.FAILED) {
            context.setStatus(InstallStatus.COMPLETED);
        }
        
        log.info("Install pipeline completed for: {}", context.getSkillId());
    }
    
    public List<String> getStageNames() {
        List<String> names = new ArrayList<>();
        for (InstallStage stage : stages) {
            names.add(stage.getName());
        }
        return names;
    }
    
    public int getStageCount() {
        return stages.size();
    }
}
