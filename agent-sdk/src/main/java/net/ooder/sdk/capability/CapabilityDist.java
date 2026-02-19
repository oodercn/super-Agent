package net.ooder.sdk.capability;

import net.ooder.sdk.capability.model.DistRequest;
import net.ooder.sdk.capability.model.DistResult;
import net.ooder.sdk.capability.model.DistStatus;
import net.ooder.sdk.capability.model.NodeDistStatus;
import net.ooder.sdk.capability.model.DistStrategy;
import net.ooder.sdk.capability.model.DistState;
import net.ooder.sdk.capability.model.NodeDistState;
import net.ooder.sdk.capability.model.CapabilityRegistration;
import net.ooder.sdk.capability.model.CapabilityInfo;
import net.ooder.sdk.capability.model.CapabilityUpdate;
import net.ooder.sdk.capability.model.CapabilityQuery;
import net.ooder.sdk.capability.model.VersionInfo;
import net.ooder.sdk.capability.model.CapabilityState;
import net.ooder.sdk.capability.model.MonitorConfig;
import net.ooder.sdk.capability.model.MonitorStatus;
import net.ooder.sdk.capability.model.ExecutionLog;
import net.ooder.sdk.capability.model.MetricRecord;
import net.ooder.sdk.capability.model.ExecutionTrace;
import net.ooder.sdk.capability.model.TraceSpan;
import net.ooder.sdk.capability.model.AlertInfo;
import net.ooder.sdk.capability.model.LogQuery;
import net.ooder.sdk.capability.model.MetricQuery;
import net.ooder.sdk.capability.model.HealthStatus;
import net.ooder.sdk.capability.model.AlertSeverity;
import net.ooder.sdk.capability.model.MonitorListener;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface CapabilityDist {
    
    CompletableFuture<DistResult> distribute(DistRequest request);
    
    CompletableFuture<DistStatus> getDistStatus(String distId);
    
    CompletableFuture<Void> cancelDist(String distId);
    
    CompletableFuture<List<DistStatus>> listPendingDists();
    
    CompletableFuture<Void> confirmReceipt(String distId, String nodeId);
    
    CompletableFuture<List<String>> getDistTargets(String specId);
}
