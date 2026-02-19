package net.ooder.sdk.capability;

import net.ooder.sdk.capability.model.MonitorConfig;
import net.ooder.sdk.capability.model.MonitorStatus;
import net.ooder.sdk.capability.model.ExecutionLog;
import net.ooder.sdk.capability.model.MetricRecord;
import net.ooder.sdk.capability.model.ExecutionTrace;
import net.ooder.sdk.capability.model.AlertInfo;
import net.ooder.sdk.capability.model.LogQuery;
import net.ooder.sdk.capability.model.MetricQuery;
import net.ooder.sdk.capability.model.MonitorListener;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface CapabilityMonService {
    
    CompletableFuture<Void> startMonitoring(String capabilityId, MonitorConfig config);
    
    CompletableFuture<Void> stopMonitoring(String capabilityId);
    
    CompletableFuture<MonitorStatus> getMonitorStatus(String capabilityId);
    
    CompletableFuture<List<ExecutionLog>> getExecutionLogs(String capabilityId, LogQuery query);
    
    CompletableFuture<List<MetricRecord>> getMetrics(String capabilityId, MetricQuery query);
    
    CompletableFuture<ExecutionTrace> getTrace(String traceId);
    
    CompletableFuture<List<AlertInfo>> getAlerts(String capabilityId);
    
    CompletableFuture<Void> acknowledgeAlert(String alertId);
    
    void addMonitorListener(MonitorListener listener);
    
    void removeMonitorListener(MonitorListener listener);
}
