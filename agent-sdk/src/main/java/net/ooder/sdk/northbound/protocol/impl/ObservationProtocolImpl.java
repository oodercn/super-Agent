package net.ooder.sdk.northbound.protocol.impl;

import net.ooder.sdk.northbound.protocol.ObservationProtocol;
import net.ooder.sdk.northbound.protocol.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;

public class ObservationProtocolImpl implements ObservationProtocol {
    
    private static final Logger log = LoggerFactory.getLogger(ObservationProtocolImpl.class);
    
    private final Map<String, ObservationStatus> observationStatuses;
    private final Map<String, List<ObservationMetric>> metricsStore;
    private final Map<String, List<ObservationLog>> logsStore;
    private final Map<String, List<ObservationTrace>> tracesStore;
    private final Map<String, List<AlertRuleConfig>> alertRules;
    private final Map<String, List<AlertInfo>> activeAlerts;
    private final List<ObservationListener> listeners;
    private final ExecutorService executor;
    private final ScheduledExecutorService scheduler;
    
    public ObservationProtocolImpl() {
        this.observationStatuses = new ConcurrentHashMap<String, ObservationStatus>();
        this.metricsStore = new ConcurrentHashMap<String, List<ObservationMetric>>();
        this.logsStore = new ConcurrentHashMap<String, List<ObservationLog>>();
        this.tracesStore = new ConcurrentHashMap<String, List<ObservationTrace>>();
        this.alertRules = new ConcurrentHashMap<String, List<AlertRuleConfig>>();
        this.activeAlerts = new ConcurrentHashMap<String, List<AlertInfo>>();
        this.listeners = new CopyOnWriteArrayList<ObservationListener>();
        this.executor = Executors.newCachedThreadPool();
        this.scheduler = Executors.newScheduledThreadPool(2);
        log.info("ObservationProtocolImpl initialized");
    }
    
    @Override
    public CompletableFuture<Void> startObservation(String targetId, ObservationConfig config) {
        return CompletableFuture.runAsync(() -> {
            log.info("Starting observation: targetId={}", targetId);
            
            ObservationStatus status = new ObservationStatus();
            status.setTargetId(targetId);
            status.setObserving(true);
            status.setStartTime(System.currentTimeMillis());
            status.setLastUpdate(System.currentTimeMillis());
            
            observationStatuses.put(targetId, status);
            metricsStore.put(targetId, new CopyOnWriteArrayList<ObservationMetric>());
            logsStore.put(targetId, new CopyOnWriteArrayList<ObservationLog>());
            tracesStore.put(targetId, new CopyOnWriteArrayList<ObservationTrace>());
            activeAlerts.put(targetId, new CopyOnWriteArrayList<AlertInfo>());
            
            if (config.isEnableMetrics() && config.getMetricsInterval() > 0) {
                scheduler.scheduleAtFixedRate(new Runnable() {
                    @Override
                    public void run() {
                        collectMetrics(targetId);
                    }
                }, 0, config.getMetricsInterval(), TimeUnit.MILLISECONDS);
            }
            
            log.info("Observation started: targetId={}", targetId);
        }, executor);
    }
    
    @Override
    public CompletableFuture<Void> stopObservation(String targetId) {
        return CompletableFuture.runAsync(() -> {
            log.info("Stopping observation: targetId={}", targetId);
            
            ObservationStatus status = observationStatuses.get(targetId);
            if (status != null) {
                status.setObserving(false);
                log.info("Observation stopped: targetId={}", targetId);
            }
        }, executor);
    }
    
    @Override
    public CompletableFuture<ObservationStatus> getObservationStatus(String targetId) {
        return CompletableFuture.supplyAsync(() -> observationStatuses.get(targetId), executor);
    }
    
    @Override
    public CompletableFuture<List<ObservationMetric>> getMetrics(String targetId, MetricQuery query) {
        return CompletableFuture.supplyAsync(() -> {
            List<ObservationMetric> allMetrics = metricsStore.get(targetId);
            if (allMetrics == null) {
                return new ArrayList<ObservationMetric>();
            }
            
            List<ObservationMetric> result = new ArrayList<ObservationMetric>();
            for (ObservationMetric metric : allMetrics) {
                if (query.getMetricType() != null && !query.getMetricType().equals(metric.getMetricType())) {
                    continue;
                }
                if (query.getStartTime() > 0 && metric.getTimestamp() < query.getStartTime()) {
                    continue;
                }
                if (query.getEndTime() > 0 && metric.getTimestamp() > query.getEndTime()) {
                    continue;
                }
                result.add(metric);
                if (query.getLimit() > 0 && result.size() >= query.getLimit()) {
                    break;
                }
            }
            return result;
        }, executor);
    }
    
    @Override
    public CompletableFuture<List<ObservationLog>> getLogs(String targetId, LogQuery query) {
        return CompletableFuture.supplyAsync(() -> {
            List<ObservationLog> allLogs = logsStore.get(targetId);
            if (allLogs == null) {
                return new ArrayList<ObservationLog>();
            }
            
            List<ObservationLog> result = new ArrayList<ObservationLog>();
            for (ObservationLog logEntry : allLogs) {
                if (query.getLevel() > 0 && logEntry.getLevel() < query.getLevel()) {
                    continue;
                }
                if (query.getKeyword() != null && !logEntry.getMessage().contains(query.getKeyword())) {
                    continue;
                }
                if (query.getStartTime() > 0 && logEntry.getTimestamp() < query.getStartTime()) {
                    continue;
                }
                if (query.getEndTime() > 0 && logEntry.getTimestamp() > query.getEndTime()) {
                    continue;
                }
                result.add(logEntry);
                if (query.getLimit() > 0 && result.size() >= query.getLimit()) {
                    break;
                }
            }
            return result;
        }, executor);
    }
    
    @Override
    public CompletableFuture<List<ObservationTrace>> getTraces(String targetId, TraceQuery query) {
        return CompletableFuture.supplyAsync(() -> {
            List<ObservationTrace> allTraces = tracesStore.get(targetId);
            if (allTraces == null) {
                return new ArrayList<ObservationTrace>();
            }
            
            List<ObservationTrace> result = new ArrayList<ObservationTrace>();
            for (ObservationTrace trace : allTraces) {
                if (query.getOperationType() != null && !query.getOperationType().equals(trace.getOperationType())) {
                    continue;
                }
                if (query.getStartTime() > 0 && trace.getTimestamp() < query.getStartTime()) {
                    continue;
                }
                if (query.getEndTime() > 0 && trace.getTimestamp() > query.getEndTime()) {
                    continue;
                }
                result.add(trace);
                if (query.getLimit() > 0 && result.size() >= query.getLimit()) {
                    break;
                }
            }
            return result;
        }, executor);
    }
    
    @Override
    public CompletableFuture<ObservationSnapshot> getSnapshot(String targetId) {
        return CompletableFuture.supplyAsync(() -> {
            ObservationSnapshot snapshot = new ObservationSnapshot();
            snapshot.setTargetId(targetId);
            snapshot.setTimestamp(System.currentTimeMillis());
            
            List<ObservationMetric> metrics = metricsStore.get(targetId);
            if (metrics != null && !metrics.isEmpty()) {
                Map<String, ObservationMetric> latestMetrics = new HashMap<String, ObservationMetric>();
                for (ObservationMetric metric : metrics) {
                    latestMetrics.put(metric.getMetricType(), metric);
                }
                snapshot.setLatestMetrics(latestMetrics);
            }
            
            List<ObservationLog> logs = logsStore.get(targetId);
            if (logs != null) {
                int start = Math.max(0, logs.size() - 100);
                snapshot.setRecentLogs(new ArrayList<ObservationLog>(logs.subList(start, logs.size())));
            }
            
            List<AlertInfo> alerts = activeAlerts.get(targetId);
            if (alerts != null) {
                List<AlertInfo> active = new ArrayList<AlertInfo>();
                for (AlertInfo alert : alerts) {
                    if (!alert.isAcknowledged()) {
                        active.add(alert);
                    }
                }
                snapshot.setActiveAlerts(active);
            }
            
            snapshot.setHealthStatus(calculateHealthStatus(targetId));
            return snapshot;
        }, executor);
    }
    
    @Override
    public CompletableFuture<Void> setAlertRule(String targetId, AlertRuleConfig rule) {
        return CompletableFuture.runAsync(() -> {
            log.info("Setting alert rule: targetId={}, ruleId={}", targetId, rule.getRuleId());
            
            List<AlertRuleConfig> rules = alertRules.computeIfAbsent(targetId, k -> new CopyOnWriteArrayList<AlertRuleConfig>());
            rules.add(rule);
            log.info("Alert rule set: targetId={}, ruleId={}", targetId, rule.getRuleId());
        }, executor);
    }
    
    @Override
    public CompletableFuture<Void> removeAlertRule(String ruleId) {
        return CompletableFuture.runAsync(() -> {
            for (List<AlertRuleConfig> rules : alertRules.values()) {
                rules.removeIf(r -> r.getRuleId().equals(ruleId));
            }
            log.info("Alert rule removed: ruleId={}", ruleId);
        }, executor);
    }
    
    @Override
    public CompletableFuture<List<AlertRuleConfig>> getAlertRules(String targetId) {
        return CompletableFuture.supplyAsync(() -> {
            List<AlertRuleConfig> rules = alertRules.get(targetId);
            return rules != null ? new ArrayList<AlertRuleConfig>(rules) : new ArrayList<AlertRuleConfig>();
        }, executor);
    }
    
    @Override
    public CompletableFuture<List<AlertInfo>> getActiveAlerts(String targetId) {
        return CompletableFuture.supplyAsync(() -> {
            List<AlertInfo> alerts = activeAlerts.get(targetId);
            if (alerts == null) {
                return new ArrayList<AlertInfo>();
            }
            
            List<AlertInfo> active = new ArrayList<AlertInfo>();
            for (AlertInfo alert : alerts) {
                if (!alert.isAcknowledged()) {
                    active.add(alert);
                }
            }
            return active;
        }, executor);
    }
    
    @Override
    public CompletableFuture<Void> acknowledgeAlert(String alertId) {
        return CompletableFuture.runAsync(() -> {
            for (List<AlertInfo> alerts : activeAlerts.values()) {
                for (AlertInfo alert : alerts) {
                    if (alert.getAlertId().equals(alertId)) {
                        alert.setAcknowledged(true);
                        alert.setAcknowledgedAt(System.currentTimeMillis());
                        notifyAlertAcknowledged(alertId);
                        log.info("Alert acknowledged: alertId={}", alertId);
                        return;
                    }
                }
            }
        }, executor);
    }
    
    @Override
    public CompletableFuture<ObservationReport> generateReport(String targetId, ReportConfig config) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("Generating report: targetId={}", targetId);
            
            ObservationReport report = new ObservationReport();
            report.setReportId("report-" + UUID.randomUUID().toString().substring(0, 8));
            report.setTargetId(targetId);
            report.setReportType(config.getReportType());
            report.setStartTime(config.getStartTime());
            report.setEndTime(config.getEndTime());
            report.setGeneratedAt(System.currentTimeMillis());
            
            Map<String, Object> summary = new HashMap<String, Object>();
            ObservationStatus status = observationStatuses.get(targetId);
            if (status != null) {
                summary.put("metricsCount", status.getMetricsCount());
                summary.put("logsCount", status.getLogsCount());
                summary.put("tracesCount", status.getTracesCount());
                summary.put("alertsCount", status.getAlertsCount());
            }
            report.setSummary(summary);
            
            report.setMetrics(getMetrics(targetId, new MetricQuery()).join());
            report.setAlerts(getActiveAlerts(targetId).join());
            
            log.info("Report generated: reportId={}", report.getReportId());
            return report;
        }, executor);
    }
    
    @Override
    public void addObservationListener(ObservationListener listener) {
        listeners.add(listener);
    }
    
    @Override
    public void removeObservationListener(ObservationListener listener) {
        listeners.remove(listener);
    }
    
    private void collectMetrics(String targetId) {
        try {
            java.lang.management.OperatingSystemMXBean osBean = 
                java.lang.management.ManagementFactory.getOperatingSystemMXBean();
            java.lang.management.MemoryMXBean memoryBean = 
                java.lang.management.ManagementFactory.getMemoryMXBean();
            
            double cpuUsage = osBean.getSystemLoadAverage() / osBean.getAvailableProcessors() * 100;
            if (cpuUsage < 0) cpuUsage = 0;
            
            java.lang.management.MemoryUsage heapUsage = memoryBean.getHeapMemoryUsage();
            long usedMemory = heapUsage.getUsed();
            long maxMemory = heapUsage.getMax();
            double memoryUsage = maxMemory > 0 ? (double) usedMemory / maxMemory * 100 : 0;
            
            ObservationMetric cpuMetric = new ObservationMetric();
            cpuMetric.setMetricId("metric-" + UUID.randomUUID().toString().substring(0, 8));
            cpuMetric.setTargetId(targetId);
            cpuMetric.setMetricType("cpu_usage");
            cpuMetric.setValue(cpuUsage);
            cpuMetric.setUnit("%");
            cpuMetric.setTimestamp(System.currentTimeMillis());
            
            ObservationMetric memoryMetric = new ObservationMetric();
            memoryMetric.setMetricId("metric-" + UUID.randomUUID().toString().substring(0, 8));
            memoryMetric.setTargetId(targetId);
            memoryMetric.setMetricType("memory_usage");
            memoryMetric.setValue(memoryUsage);
            memoryMetric.setUnit("%");
            memoryMetric.setTimestamp(System.currentTimeMillis());
            
            ObservationMetric threadMetric = new ObservationMetric();
            threadMetric.setMetricId("metric-" + UUID.randomUUID().toString().substring(0, 8));
            threadMetric.setTargetId(targetId);
            threadMetric.setMetricType("thread_count");
            threadMetric.setValue(java.lang.management.ManagementFactory.getThreadMXBean().getThreadCount());
            threadMetric.setUnit("threads");
            threadMetric.setTimestamp(System.currentTimeMillis());
            
            List<ObservationMetric> metrics = metricsStore.get(targetId);
            if (metrics != null) {
                metrics.add(cpuMetric);
                metrics.add(memoryMetric);
                metrics.add(threadMetric);
                
                ObservationStatus status = observationStatuses.get(targetId);
                if (status != null) {
                    status.setMetricsCount(metrics.size());
                    status.setLastUpdate(System.currentTimeMillis());
                }
                
                notifyMetricCollected(targetId, cpuMetric);
                notifyMetricCollected(targetId, memoryMetric);
                notifyMetricCollected(targetId, threadMetric);
            }
        } catch (Exception e) {
            log.warn("Failed to collect metrics for target: {}", targetId, e);
        }
    }
    
    private HealthStatus calculateHealthStatus(String targetId) {
        List<AlertInfo> alerts = activeAlerts.get(targetId);
        if (alerts != null) {
            for (AlertInfo alert : alerts) {
                if (!alert.isAcknowledged()) {
                    if (alert.getSeverity() == AlertSeverity.CRITICAL) {
                        return HealthStatus.UNHEALTHY;
                    }
                    if (alert.getSeverity() == AlertSeverity.ERROR || alert.getSeverity() == AlertSeverity.WARNING) {
                        return HealthStatus.DEGRADED;
                    }
                }
            }
        }
        return HealthStatus.HEALTHY;
    }
    
    private void notifyMetricCollected(String targetId, ObservationMetric metric) {
        for (ObservationListener listener : listeners) {
            try {
                listener.onMetricCollected(targetId, metric);
            } catch (Exception e) {
                log.warn("Listener notification failed", e);
            }
        }
    }
    
    private void notifyAlertAcknowledged(String alertId) {
        for (ObservationListener listener : listeners) {
            try {
                listener.onAlertAcknowledged(alertId);
            } catch (Exception e) {
                log.warn("Listener notification failed", e);
            }
        }
    }
    
    public void recordLog(String targetId, int level, String message, Map<String, Object> context) {
        List<ObservationLog> logs = logsStore.get(targetId);
        if (logs != null) {
            ObservationLog logEntry = new ObservationLog();
            logEntry.setLogId("log-" + UUID.randomUUID().toString().substring(0, 8));
            logEntry.setTargetId(targetId);
            logEntry.setLevel(level);
            logEntry.setMessage(message);
            logEntry.setContext(context);
            logEntry.setTimestamp(System.currentTimeMillis());
            
            logs.add(logEntry);
            
            ObservationStatus status = observationStatuses.get(targetId);
            if (status != null) {
                status.setLogsCount(logs.size());
            }
            
            notifyLogCollected(targetId, logEntry);
        }
    }
    
    public void recordTrace(String targetId, ObservationTrace trace) {
        List<ObservationTrace> traces = tracesStore.get(targetId);
        if (traces != null) {
            traces.add(trace);
            
            ObservationStatus status = observationStatuses.get(targetId);
            if (status != null) {
                status.setTracesCount(traces.size());
            }
            
            notifyTraceCollected(targetId, trace);
        }
    }
    
    private void notifyLogCollected(String targetId, ObservationLog logEntry) {
        for (ObservationListener listener : listeners) {
            try {
                listener.onLogCollected(targetId, logEntry);
            } catch (Exception e) {
                log.warn("Listener notification failed", e);
            }
        }
    }
    
    private void notifyTraceCollected(String targetId, ObservationTrace trace) {
        for (ObservationListener listener : listeners) {
            try {
                listener.onTraceCollected(targetId, trace);
            } catch (Exception e) {
                log.warn("Listener notification failed", e);
            }
        }
    }
    
    public void shutdown() {
        log.info("Shutting down ObservationProtocol");
        scheduler.shutdown();
        executor.shutdown();
        observationStatuses.clear();
        metricsStore.clear();
        logsStore.clear();
        tracesStore.clear();
        alertRules.clear();
        activeAlerts.clear();
        log.info("ObservationProtocol shutdown complete");
    }
}
