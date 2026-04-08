package net.ooder.mvp.skill.scene.llm;

import net.ooder.scene.llm.context.*;
import net.ooder.scene.skill.tool.ToolRegistry;
import net.ooder.scene.skill.tool.ToolOrchestrator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SkillActivationService {

    private static final Logger log = LoggerFactory.getLogger(SkillActivationService.class);

    private ToolRegistry toolRegistry;
    private ToolOrchestrator toolOrchestrator;
    
    private final Map<String, SkillActivationContext> activeContexts = new ConcurrentHashMap<String, SkillActivationContext>();

    @Autowired
    public void setToolRegistry(ToolRegistry toolRegistry) {
        this.toolRegistry = toolRegistry;
    }

    @Autowired
    public void setToolOrchestrator(ToolOrchestrator toolOrchestrator) {
        this.toolOrchestrator = toolOrchestrator;
    }

    public SkillActivationContext activateSkill(ActivationRequest request) {
        log.info("[SkillActivationService] Activating skill: {}", request.getSkillId());
        
        SkillActivationContext context = SkillActivationContext.activate(request);
        
        activeContexts.put(context.getActivationId(), context);
        
        log.info("[SkillActivationService] Skill activated: {} -> {}", request.getSkillId(), context.getActivationId());
        
        return context;
    }

    public SkillActivationContext getContext(String activationId) {
        return activeContexts.get(activationId);
    }

    public void deactivateContext(String activationId) {
        SkillActivationContext context = activeContexts.remove(activationId);
        if (context != null) {
            context.destroy();
            log.info("[SkillActivationService] Context deactivated: {}", activationId);
        }
    }

    public List<Map<String, Object>> getToolsForLLM(String activationId) {
        SkillActivationContext context = activeContexts.get(activationId);
        if (context != null) {
            return context.getTools();
        }
        
        if (toolRegistry != null) {
            return toolRegistry.getToolDefinitions();
        }
        
        return Collections.emptyList();
    }

    public Object executeFunction(String activationId, String functionName, Map<String, Object> args) {
        SkillActivationContext context = activeContexts.get(activationId);
        if (context == null) {
            Map<String, Object> errorResult = new HashMap<String, Object>();
            errorResult.put("error", true);
            errorResult.put("message", "Activation context not found: " + activationId);
            return errorResult;
        }
        
        return context.executeFunction(functionName, args);
    }
    
    public ToolOrchestrator getToolOrchestrator() {
        return toolOrchestrator;
    }
    
    public ToolRegistry getToolRegistry() {
        return toolRegistry;
    }
}
