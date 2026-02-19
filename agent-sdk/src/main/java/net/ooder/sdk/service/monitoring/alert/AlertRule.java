
package net.ooder.sdk.service.monitoring.alert;

import java.util.function.Predicate;

public class AlertRule {
    
    private String id;
    private String name;
    private AlertLevel level;
    private String description;
    private Predicate<Object> condition;
    private long cooldownMs;
    private long lastTriggered;
    private boolean enabled;
    
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public AlertLevel getLevel() { return level; }
    public void setLevel(AlertLevel level) { this.level = level; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Predicate<Object> getCondition() { return condition; }
    public void setCondition(Predicate<Object> condition) { this.condition = condition; }
    
    public long getCooldownMs() { return cooldownMs; }
    public void setCooldownMs(long cooldownMs) { this.cooldownMs = cooldownMs; }
    
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    
    public boolean shouldTrigger(Object value) {
        if (!enabled) {
            return false;
        }
        
        if (cooldownMs > 0 && System.currentTimeMillis() - lastTriggered < cooldownMs) {
            return false;
        }
        
        if (condition != null && condition.test(value)) {
            lastTriggered = System.currentTimeMillis();
            return true;
        }
        
        return false;
    }
    
    public static AlertRule threshold(String id, String name, AlertLevel level, 
                                       double threshold, java.util.function.ToDoubleFunction<Object> extractor) {
        AlertRule rule = new AlertRule();
        rule.setId(id);
        rule.setName(name);
        rule.setLevel(level);
        rule.setCondition(value -> {
            if (value == null) return false;
            double extracted = extractor.applyAsDouble(value);
            return extracted >= threshold;
        });
        rule.setEnabled(true);
        return rule;
    }
}
