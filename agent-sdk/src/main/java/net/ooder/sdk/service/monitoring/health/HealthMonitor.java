
package net.ooder.sdk.service.monitoring.health;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HealthMonitor {
    
    private static final Logger log = LoggerFactory.getLogger(HealthMonitor.class);
    
    private final Map<String, HealthCheck> checks;
    private final ScheduledExecutorService scheduler;
    private final List<HealthStatusListener> listeners;
    private volatile boolean running;
    
    public HealthMonitor() {
        this.checks = new ConcurrentHashMap<>();
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        this.listeners = new ArrayList<>();
        this.running = false;
    }
    
    public void registerCheck(String name, HealthCheck check) {
        checks.put(name, check);
        log.info("Registered health check: {}", name);
    }
    
    public void unregisterCheck(String name) {
        checks.remove(name);
        log.info("Unregistered health check: {}", name);
    }
    
    public void addListener(HealthStatusListener listener) {
        listeners.add(listener);
    }
    
    public void start(long intervalMs) {
        if (!running) {
            running = true;
            scheduler.scheduleAtFixedRate(this::runChecks, 0, intervalMs, TimeUnit.MILLISECONDS);
            log.info("Health monitor started with interval {}ms", intervalMs);
        }
    }
    
    public void stop() {
        if (running) {
            running = false;
            scheduler.shutdown();
            log.info("Health monitor stopped");
        }
    }
    
    public HealthReport check() {
        return runChecks();
    }
    
    private HealthReport runChecks() {
        HealthReport report = new HealthReport();
        report.setTimestamp(System.currentTimeMillis());
        
        HealthStatus overallStatus = HealthStatus.HEALTHY;
        
        for (Map.Entry<String, HealthCheck> entry : checks.entrySet()) {
            try {
                HealthCheckResult result = entry.getValue().check();
                report.addResult(entry.getKey(), result);
                
                if (result.getStatus().needsAttention()) {
                    if (result.getStatus() == HealthStatus.UNHEALTHY) {
                        overallStatus = HealthStatus.UNHEALTHY;
                    } else if (overallStatus == HealthStatus.HEALTHY) {
                        overallStatus = HealthStatus.DEGRADED;
                    }
                }
            } catch (Exception e) {
                log.error("Health check failed: {}", entry.getKey(), e);
                HealthCheckResult errorResult = new HealthCheckResult();
                errorResult.setStatus(HealthStatus.UNKNOWN);
                errorResult.setMessage("Check failed: " + e.getMessage());
                report.addResult(entry.getKey(), errorResult);
                overallStatus = HealthStatus.UNKNOWN;
            }
        }
        
        report.setOverallStatus(overallStatus);
        
        for (HealthStatusListener listener : listeners) {
            try {
                listener.onStatusChange(report);
            } catch (Exception e) {
                log.warn("Health status listener error", e);
            }
        }
        
        return report;
    }
    
    public interface HealthCheck {
        HealthCheckResult check();
    }
    
    public interface HealthStatusListener {
        void onStatusChange(HealthReport report);
    }
    
    public static class HealthCheckResult {
        private HealthStatus status;
        private String message;
        private Map<String, Object> details;
        
        public HealthStatus getStatus() { return status; }
        public void setStatus(HealthStatus status) { this.status = status; }
        
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        
        public Map<String, Object> getDetails() { return details; }
        public void setDetails(Map<String, Object> details) { this.details = details; }
    }
    
    public static class HealthReport {
        private long timestamp;
        private HealthStatus overallStatus;
        private final Map<String, HealthCheckResult> results = new ConcurrentHashMap<>();
        
        public long getTimestamp() { return timestamp; }
        public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
        
        public HealthStatus getOverallStatus() { return overallStatus; }
        public void setOverallStatus(HealthStatus status) { this.overallStatus = status; }
        
        public void addResult(String name, HealthCheckResult result) {
            results.put(name, result);
        }
        
        public Map<String, HealthCheckResult> getResults() {
            return results;
        }
    }
}
