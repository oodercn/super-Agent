
package net.ooder.sdk.service.monitoring.alert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlertManager {
    
    private static final Logger log = LoggerFactory.getLogger(AlertManager.class);
    
    private final Map<String, AlertRule> rules;
    private final Map<String, Alert> activeAlerts;
    private final List<AlertListener> listeners;
    private final int maxActiveAlerts;
    
    public AlertManager() {
        this(1000);
    }
    
    public AlertManager(int maxActiveAlerts) {
        this.rules = new ConcurrentHashMap<>();
        this.activeAlerts = new ConcurrentHashMap<>();
        this.listeners = new ArrayList<>();
        this.maxActiveAlerts = maxActiveAlerts;
    }
    
    public void addRule(AlertRule rule) {
        rules.put(rule.getId(), rule);
        log.info("Added alert rule: {} ({})", rule.getName(), rule.getLevel());
    }
    
    public void removeRule(String ruleId) {
        rules.remove(ruleId);
        log.info("Removed alert rule: {}", ruleId);
    }
    
    public void addListener(AlertListener listener) {
        listeners.add(listener);
    }
    
    public void removeListener(AlertListener listener) {
        listeners.remove(listener);
    }
    
    public Alert checkAndAlert(String ruleId, Object value) {
        AlertRule rule = rules.get(ruleId);
        if (rule == null) {
            return null;
        }
        
        if (rule.shouldTrigger(value)) {
            Alert alert = createAlert(rule, value);
            activeAlerts.put(alert.getId(), alert);
            
            if (activeAlerts.size() > maxActiveAlerts) {
                cleanupOldAlerts();
            }
            
            notifyListeners(alert);
            return alert;
        }
        
        return null;
    }
    
    private Alert createAlert(AlertRule rule, Object value) {
        Alert alert = new Alert();
        alert.setId(UUID.randomUUID().toString());
        alert.setRuleId(rule.getId());
        alert.setLevel(rule.getLevel());
        alert.setTitle(rule.getName());
        alert.setMessage(rule.getDescription());
        alert.setTimestamp(System.currentTimeMillis());
        
        log.warn("Alert triggered: {} - {}", alert.getTitle(), alert.getMessage());
        
        return alert;
    }
    
    private void notifyListeners(Alert alert) {
        for (AlertListener listener : listeners) {
            try {
                listener.onAlert(alert);
            } catch (Exception e) {
                log.error("Alert listener error", e);
            }
        }
    }
    
    public void acknowledgeAlert(String alertId, String acknowledgedBy) {
        Alert alert = activeAlerts.get(alertId);
        if (alert != null && !alert.isAcknowledged()) {
            alert.acknowledge(acknowledgedBy);
            log.info("Alert acknowledged: {} by {}", alertId, acknowledgedBy);
        }
    }
    
    public void resolveAlert(String alertId) {
        Alert alert = activeAlerts.remove(alertId);
        if (alert != null) {
            log.info("Alert resolved: {}", alertId);
        }
    }
    
    public Map<String, Alert> getActiveAlerts() {
        return new ConcurrentHashMap<>(activeAlerts);
    }
    
    public List<Alert> getAlertsByLevel(AlertLevel level) {
        List<Alert> result = new ArrayList<>();
        for (Alert alert : activeAlerts.values()) {
            if (alert.getLevel() == level) {
                result.add(alert);
            }
        }
        return result;
    }
    
    private void cleanupOldAlerts() {
        long cutoff = System.currentTimeMillis() - 3600000;
        activeAlerts.entrySet().removeIf(entry -> 
            entry.getValue().isAcknowledged() && entry.getValue().getTimestamp() < cutoff);
    }
    
    public interface AlertListener {
        void onAlert(Alert alert);
    }
}
