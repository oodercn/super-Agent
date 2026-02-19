
package net.ooder.sdk.core.skill.installer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ValidationStage implements InstallStage {
    
    private static final Logger log = LoggerFactory.getLogger(ValidationStage.class);
    
    @Override
    public String getName() {
        return "validation";
    }
    
    @Override
    public void execute(InstallContext context) throws Exception {
        log.debug("Validating install request for: {}", context.getSkillId());
        
        if (context.getRequest() == null) {
            throw new InstallException("Install request is null");
        }
        
        if (context.getSkillId() == null || context.getSkillId().isEmpty()) {
            throw new InstallException("Skill ID is required");
        }
        
        context.setStatus(InstallStatus.VALIDATING);
        
        log.debug("Validation passed for: {}", context.getSkillId());
    }
}
