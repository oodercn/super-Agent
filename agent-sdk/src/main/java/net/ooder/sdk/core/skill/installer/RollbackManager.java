
package net.ooder.sdk.core.skill.installer;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RollbackManager {
    
    private static final Logger log = LoggerFactory.getLogger(RollbackManager.class);
    
    private final Map<String, Stack<RollbackAction>> rollbackStacks;
    
    public RollbackManager() {
        this.rollbackStacks = new HashMap<>();
    }
    
    public void registerAction(String contextId, String description, Runnable action) {
        Stack<RollbackAction> stack = rollbackStacks.computeIfAbsent(contextId, k -> new Stack<>());
        stack.push(new RollbackAction(description, action));
        log.debug("Registered rollback action: {} for context: {}", description, contextId);
    }
    
    public void rollback(InstallContext context) {
        String contextId = context.getContextId();
        Stack<RollbackAction> stack = rollbackStacks.get(contextId);
        
        if (stack == null || stack.isEmpty()) {
            log.debug("No rollback actions for context: {}", contextId);
            return;
        }
        
        log.info("Starting rollback for context: {}", contextId);
        
        while (!stack.isEmpty()) {
            RollbackAction action = stack.pop();
            try {
                log.debug("Executing rollback: {}", action.getDescription());
                action.execute();
            } catch (Exception e) {
                log.error("Rollback action failed: {}", action.getDescription(), e);
            }
        }
        
        rollbackStacks.remove(contextId);
        log.info("Rollback completed for context: {}", contextId);
    }
    
    public void cleanup(String skillId) {
        rollbackStacks.entrySet().removeIf(entry -> {
            return true;
        });
        
        log.debug("Cleaned up rollback data for skill: {}", skillId);
    }
    
    public void clearContext(String contextId) {
        rollbackStacks.remove(contextId);
    }
    
    private static class RollbackAction {
        private final String description;
        private final Runnable action;
        
        public RollbackAction(String description, Runnable action) {
            this.description = description;
            this.action = action;
        }
        
        public String getDescription() { return description; }
        
        public void execute() {
            action.run();
        }
    }
}
