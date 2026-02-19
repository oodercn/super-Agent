package net.ooder.sdk.northbound.protocol;

import net.ooder.sdk.northbound.protocol.model.ObservationConfig;
import net.ooder.sdk.northbound.protocol.model.ObservationStatus;
import net.ooder.sdk.northbound.protocol.model.MetricQuery;
import net.ooder.sdk.northbound.protocol.model.LogQuery;
import net.ooder.sdk.northbound.protocol.model.TraceQuery;
import net.ooder.sdk.northbound.protocol.model.ObservationMetric;
import net.ooder.sdk.northbound.protocol.model.ObservationLog;
import net.ooder.sdk.northbound.protocol.model.ObservationTrace;
import net.ooder.sdk.northbound.protocol.model.ObservationSnapshot;
import net.ooder.sdk.northbound.protocol.model.AlertRuleConfig;
import net.ooder.sdk.northbound.protocol.model.AlertInfo;
import net.ooder.sdk.northbound.protocol.model.ReportConfig;
import net.ooder.sdk.northbound.protocol.model.ObservationReport;
import net.ooder.sdk.northbound.protocol.model.ObservationListener;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ObservationProtocol {
    
    CompletableFuture<Void> startObservation(String targetId, ObservationConfig config);
    
    CompletableFuture<Void> stopObservation(String targetId);
    
    CompletableFuture<ObservationStatus> getObservationStatus(String targetId);
    
    CompletableFuture<List<ObservationMetric>> getMetrics(String targetId, MetricQuery query);
    
    CompletableFuture<List<ObservationLog>> getLogs(String targetId, LogQuery query);
    
    CompletableFuture<List<ObservationTrace>> getTraces(String targetId, TraceQuery query);
    
    CompletableFuture<ObservationSnapshot> getSnapshot(String targetId);
    
    CompletableFuture<Void> setAlertRule(String targetId, AlertRuleConfig rule);
    
    CompletableFuture<Void> removeAlertRule(String ruleId);
    
    CompletableFuture<List<AlertRuleConfig>> getAlertRules(String targetId);
    
    CompletableFuture<List<AlertInfo>> getActiveAlerts(String targetId);
    
    CompletableFuture<Void> acknowledgeAlert(String alertId);
    
    CompletableFuture<ObservationReport> generateReport(String targetId, ReportConfig config);
    
    void addObservationListener(ObservationListener listener);
    
    void removeObservationListener(ObservationListener listener);
}
