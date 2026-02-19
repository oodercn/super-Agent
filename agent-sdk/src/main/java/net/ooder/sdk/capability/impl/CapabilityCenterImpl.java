package net.ooder.sdk.capability.impl;

import net.ooder.sdk.capability.CapabilityCenter;
import net.ooder.sdk.capability.CapabilitySpecService;
import net.ooder.sdk.capability.CapabilityDistService;
import net.ooder.sdk.capability.CapabilityMgtService;
import net.ooder.sdk.capability.CapabilityMonService;
import net.ooder.sdk.capability.CapabilityCoopService;
import net.ooder.sdk.capability.model.CapabilitySpec;
import net.ooder.sdk.capability.model.SpecDefinition;
import net.ooder.sdk.capability.model.ValidationResult;
import net.ooder.sdk.capability.model.SpecQuery;
import net.ooder.sdk.capability.model.SpecStatus;
import net.ooder.sdk.capability.model.DistRequest;
import net.ooder.sdk.capability.model.DistResult;
import net.ooder.sdk.capability.model.DistStatus;
import net.ooder.sdk.capability.model.DistState;
import net.ooder.sdk.capability.model.NodeDistStatus;
import net.ooder.sdk.capability.model.NodeDistState;
import net.ooder.sdk.capability.model.CapabilityRegistration;
import net.ooder.sdk.capability.model.CapabilityInfo;
import net.ooder.sdk.capability.model.CapabilityUpdate;
import net.ooder.sdk.capability.model.CapabilityQuery;
import net.ooder.sdk.capability.model.CapabilityState;
import net.ooder.sdk.capability.model.VersionInfo;
import net.ooder.sdk.capability.model.MonitorConfig;
import net.ooder.sdk.capability.model.MonitorStatus;
import net.ooder.sdk.capability.model.ExecutionLog;
import net.ooder.sdk.capability.model.MetricRecord;
import net.ooder.sdk.capability.model.ExecutionTrace;
import net.ooder.sdk.capability.model.AlertInfo;
import net.ooder.sdk.capability.model.LogQuery;
import net.ooder.sdk.capability.model.MetricQuery;
import net.ooder.sdk.capability.model.HealthStatus;
import net.ooder.sdk.capability.model.MonitorListener;
import net.ooder.sdk.capability.model.OrchestrationConfig;
import net.ooder.sdk.capability.model.OrchestrationDef;
import net.ooder.sdk.capability.model.OrchestrationResult;
import net.ooder.sdk.capability.model.OrchestrationStatus;
import net.ooder.sdk.capability.model.OrchestrationStep;
import net.ooder.sdk.capability.model.StepResult;
import net.ooder.sdk.capability.model.SceneGroupConfig;
import net.ooder.sdk.capability.model.SceneGroupDef;
import net.ooder.sdk.capability.model.SceneGroupStatus;
import net.ooder.sdk.capability.model.ChainConfig;
import net.ooder.sdk.capability.model.ChainDef;
import net.ooder.sdk.capability.model.ChainLink;
import net.ooder.sdk.capability.model.ChainResult;
import net.ooder.sdk.capability.model.ChainStatus;
import net.ooder.sdk.capability.model.LinkResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;

public class CapabilityCenterImpl implements CapabilityCenter {
    
    private static final Logger log = LoggerFactory.getLogger(CapabilityCenterImpl.class);
    
    private final CapabilitySpecServiceImpl specService;
    private final CapabilityDistServiceImpl distService;
    private final CapabilityMgtServiceImpl mgtService;
    private final CapabilityMonServiceImpl monService;
    private final CapabilityCoopServiceImpl coopService;
    
    private volatile boolean initialized = false;
    
    public CapabilityCenterImpl() {
        this.specService = new CapabilitySpecServiceImpl();
        this.distService = new CapabilityDistServiceImpl();
        this.mgtService = new CapabilityMgtServiceImpl();
        this.monService = new CapabilityMonServiceImpl();
        this.coopService = new CapabilityCoopServiceImpl();
        log.info("CapabilityCenterImpl created");
    }
    
    @Override
    public CapabilitySpecService getSpecService() {
        return specService;
    }
    
    @Override
    public CapabilityDistService getDistService() {
        return distService;
    }
    
    @Override
    public CapabilityMgtService getMgtService() {
        return mgtService;
    }
    
    @Override
    public CapabilityMonService getMonService() {
        return monService;
    }
    
    @Override
    public CapabilityCoopService getCoopService() {
        return coopService;
    }
    
    @Override
    public void initialize() {
        log.info("Initializing CapabilityCenter");
        specService.initialize();
        distService.initialize();
        mgtService.initialize();
        monService.initialize();
        coopService.initialize();
        initialized = true;
        log.info("CapabilityCenter initialized");
    }
    
    @Override
    public void shutdown() {
        log.info("Shutting down CapabilityCenter");
        specService.shutdown();
        distService.shutdown();
        mgtService.shutdown();
        monService.shutdown();
        coopService.shutdown();
        initialized = false;
        log.info("CapabilityCenter shutdown complete");
    }
    
    @Override
    public boolean isInitialized() {
        return initialized;
    }
}

class CapabilitySpecServiceImpl implements CapabilitySpecService {
    
    private static final Logger log = LoggerFactory.getLogger(CapabilitySpecServiceImpl.class);
    
    private final Map<String, CapabilitySpec> specs;
    private final ExecutorService executor;
    
    CapabilitySpecServiceImpl() {
        this.specs = new ConcurrentHashMap<String, CapabilitySpec>();
        this.executor = Executors.newCachedThreadPool();
    }
    
    void initialize() {
        log.info("CapabilitySpecService initialized");
    }
    
    void shutdown() {
        log.info("CapabilitySpecService shutdown");
        executor.shutdown();
        specs.clear();
    }
    
    @Override
    public CompletableFuture<CapabilitySpec> registerSpec(SpecDefinition definition) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("Registering spec: {}", definition.getName());
            
            CapabilitySpec spec = new CapabilitySpec();
            spec.setSpecId("spec-" + UUID.randomUUID().toString().substring(0, 8));
            spec.setSpecName(definition.getName());
            spec.setType(definition.getType());
            spec.setVersion(definition.getVersion());
            spec.setDescription(definition.getDescription());
            spec.setAuthor(definition.getAuthor());
            spec.setTags(definition.getTags());
            spec.setMetadata(definition.getMetadata());
            spec.setDependencies(definition.getDependencies());
            spec.setParameters(definition.getParameters());
            spec.setOutputs(definition.getOutputs());
            spec.setExecutionConfig(definition.getExecutionConfig());
            spec.setSecurityConfig(definition.getSecurityConfig());
            spec.setCreatedTime(System.currentTimeMillis());
            spec.setUpdatedTime(System.currentTimeMillis());
            spec.setStatus(SpecStatus.VALIDATED);
            
            specs.put(spec.getSpecId(), spec);
            log.info("Spec registered: {}", spec.getSpecId());
            return spec;
        }, executor);
    }
    
    @Override
    public CompletableFuture<CapabilitySpec> getSpec(String specId) {
        return CompletableFuture.supplyAsync(() -> specs.get(specId), executor);
    }
    
    @Override
    public CompletableFuture<CapabilitySpec> getSpecByName(String name, String version) {
        return CompletableFuture.supplyAsync(() -> {
            for (CapabilitySpec spec : specs.values()) {
                if (spec.getSpecName().equals(name) && spec.getVersion().equals(version)) {
                    return spec;
                }
            }
            return null;
        }, executor);
    }
    
    @Override
    public CompletableFuture<List<CapabilitySpec>> listSpecs(SpecQuery query) {
        return CompletableFuture.supplyAsync(() -> {
            List<CapabilitySpec> result = new ArrayList<CapabilitySpec>();
            for (CapabilitySpec spec : specs.values()) {
                if (query.getType() != null && spec.getType() != query.getType()) {
                    continue;
                }
                if (query.getStatus() != null && !spec.getStatus().name().equals(query.getStatus())) {
                    continue;
                }
                if (query.getKeyword() != null && !spec.getSpecName().contains(query.getKeyword())) {
                    continue;
                }
                result.add(spec);
            }
            return result;
        }, executor);
    }
    
    @Override
    public CompletableFuture<CapabilitySpec> updateSpec(String specId, SpecDefinition definition) {
        return CompletableFuture.supplyAsync(() -> {
            CapabilitySpec spec = specs.get(specId);
            if (spec != null) {
                spec.setSpecName(definition.getName());
                spec.setVersion(definition.getVersion());
                spec.setDescription(definition.getDescription());
                spec.setUpdatedTime(System.currentTimeMillis());
                log.info("Spec updated: {}", specId);
            }
            return spec;
        }, executor);
    }
    
    @Override
    public CompletableFuture<Void> deleteSpec(String specId) {
        return CompletableFuture.runAsync(() -> {
            specs.remove(specId);
            log.info("Spec deleted: {}", specId);
        }, executor);
    }
    
    @Override
    public CompletableFuture<ValidationResult> validateSpec(SpecDefinition definition) {
        return CompletableFuture.supplyAsync(() -> {
            ValidationResult result = new ValidationResult();
            result.setValid(true);
            result.setErrors(new ArrayList<String>());
            result.setWarnings(new ArrayList<String>());
            
            if (definition.getName() == null || definition.getName().isEmpty()) {
                result.setValid(false);
                result.getErrors().add("Name is required");
            }
            if (definition.getVersion() == null || definition.getVersion().isEmpty()) {
                result.setValid(false);
                result.getErrors().add("Version is required");
            }
            
            return result;
        }, executor);
    }
    
    @Override
    public CompletableFuture<List<CapabilitySpec>> searchSpecs(String keyword) {
        return CompletableFuture.supplyAsync(() -> {
            List<CapabilitySpec> result = new ArrayList<CapabilitySpec>();
            for (CapabilitySpec spec : specs.values()) {
                if (spec.getSpecName().contains(keyword) || 
                    (spec.getDescription() != null && spec.getDescription().contains(keyword))) {
                    result.add(spec);
                }
            }
            return result;
        }, executor);
    }
}

class CapabilityDistServiceImpl implements CapabilityDistService {
    
    private static final Logger log = LoggerFactory.getLogger(CapabilityDistServiceImpl.class);
    
    private final Map<String, DistStatus> distStatuses;
    private final Map<String, CapabilityInfo> capabilities;
    private final ExecutorService executor;
    
    CapabilityDistServiceImpl() {
        this.distStatuses = new ConcurrentHashMap<String, DistStatus>();
        this.capabilities = new ConcurrentHashMap<String, CapabilityInfo>();
        this.executor = Executors.newCachedThreadPool();
    }
    
    void initialize() {
        log.info("CapabilityDistService initialized");
    }
    
    void shutdown() {
        log.info("CapabilityDistService shutdown");
        executor.shutdown();
        distStatuses.clear();
        capabilities.clear();
    }
    
    @Override
    public CompletableFuture<DistResult> distribute(DistRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("Distributing spec: {} to {} nodes", request.getSpecId(), request.getTargetNodes().size());
            
            String distId = "dist-" + UUID.randomUUID().toString().substring(0, 8);
            
            DistStatus status = new DistStatus();
            status.setDistId(distId);
            status.setSpecId(request.getSpecId());
            status.setState(DistState.RUNNING);
            status.setTotalNodes(request.getTargetNodes().size());
            status.setCompletedNodes(0);
            status.setFailedNodes(0);
            status.setStartTime(System.currentTimeMillis());
            status.setNodeStatuses(new ArrayList<NodeDistStatus>());
            
            distStatuses.put(distId, status);
            
            DistResult result = new DistResult();
            result.setDistId(distId);
            result.setSuccess(true);
            result.setTotalNodes(request.getTargetNodes().size());
            result.setSuccessNodes(request.getTargetNodes().size());
            result.setFailedNodes(0);
            result.setNodeResults(new HashMap<String, String>());
            
            for (String nodeId : request.getTargetNodes()) {
                result.getNodeResults().put(nodeId, "SUCCESS");
                
                NodeDistStatus nodeStatus = new NodeDistStatus();
                nodeStatus.setNodeId(nodeId);
                nodeStatus.setState(NodeDistState.COMPLETED);
                nodeStatus.setStartTime(System.currentTimeMillis());
                nodeStatus.setEndTime(System.currentTimeMillis());
                status.getNodeStatuses().add(nodeStatus);
            }
            
            status.setState(DistState.COMPLETED);
            status.setCompletedNodes(request.getTargetNodes().size());
            status.setEndTime(System.currentTimeMillis());
            
            log.info("Distribution completed: {}", distId);
            return result;
        }, executor);
    }
    
    @Override
    public CompletableFuture<DistStatus> getDistStatus(String distId) {
        return CompletableFuture.supplyAsync(() -> distStatuses.get(distId), executor);
    }
    
    @Override
    public CompletableFuture<Void> cancelDist(String distId) {
        return CompletableFuture.runAsync(() -> {
            DistStatus status = distStatuses.get(distId);
            if (status != null && status.getState() == DistState.RUNNING) {
                status.setState(DistState.CANCELLED);
                log.info("Distribution cancelled: {}", distId);
            }
        }, executor);
    }
    
    @Override
    public CompletableFuture<List<DistStatus>> listPendingDists() {
        return CompletableFuture.supplyAsync(() -> {
            List<DistStatus> result = new ArrayList<DistStatus>();
            for (DistStatus status : distStatuses.values()) {
                if (status.getState() == DistState.PENDING || status.getState() == DistState.RUNNING) {
                    result.add(status);
                }
            }
            return result;
        }, executor);
    }
    
    @Override
    public CompletableFuture<Void> confirmReceipt(String distId, String nodeId) {
        return CompletableFuture.runAsync(() -> {
            log.info("Confirming receipt: distId={}, nodeId={}", distId, nodeId);
            
            DistStatus status = distStatuses.get(distId);
            if (status == null) {
                log.warn("Distribution status not found: {}", distId);
                return;
            }
            
            List<NodeDistStatus> nodeStatuses = status.getNodeStatuses();
            if (nodeStatuses == null) {
                log.warn("No node statuses for distribution: {}", distId);
                return;
            }
            
            for (NodeDistStatus nodeStatus : nodeStatuses) {
                if (nodeId.equals(nodeStatus.getNodeId())) {
                    nodeStatus.setState(NodeDistState.COMPLETED);
                    break;
                }
            }
            
            log.info("Receipt confirmed successfully: distId={}, nodeId={}", distId, nodeId);
        }, executor);
    }
    
    @Override
    public CompletableFuture<List<String>> getDistTargets(String specId) {
        return CompletableFuture.supplyAsync(() -> {
            List<String> targets = new ArrayList<String>();
            
            for (Map.Entry<String, CapabilityInfo> entry : capabilities.entrySet()) {
                CapabilityInfo info = entry.getValue();
                if (info.getSpecId().equals(specId)) {
                    targets.add(info.getNodeId());
                }
            }
            
            log.debug("Found {} targets for spec: {}", targets.size(), specId);
            return targets;
        }, executor);
    }
}

class CapabilityMgtServiceImpl implements CapabilityMgtService {
    
    private static final Logger log = LoggerFactory.getLogger(CapabilityMgtServiceImpl.class);
    
    private final Map<String, CapabilityInfo> capabilities;
    private final ExecutorService executor;
    
    CapabilityMgtServiceImpl() {
        this.capabilities = new ConcurrentHashMap<String, CapabilityInfo>();
        this.executor = Executors.newCachedThreadPool();
    }
    
    void initialize() {
        log.info("CapabilityMgtService initialized");
    }
    
    void shutdown() {
        log.info("CapabilityMgtService shutdown");
        executor.shutdown();
        capabilities.clear();
    }
    
    @Override
    public CompletableFuture<CapabilityInfo> register(CapabilityRegistration registration) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("Registering capability: specId={}", registration.getSpecId());
            
            CapabilityInfo info = new CapabilityInfo();
            info.setCapabilityId("cap-" + UUID.randomUUID().toString().substring(0, 8));
            info.setSpecId(registration.getSpecId());
            info.setSpecName("Spec-" + registration.getSpecId());
            info.setVersion("1.0.0");
            info.setNodeId(registration.getNodeId());
            info.setState(CapabilityState.REGISTERED);
            info.setRegisteredTime(System.currentTimeMillis());
            info.setLastActiveTime(System.currentTimeMillis());
            info.setExecutionCount(0);
            info.setConfig(registration.getConfig());
            
            capabilities.put(info.getCapabilityId(), info);
            log.info("Capability registered: {}", info.getCapabilityId());
            return info;
        }, executor);
    }
    
    @Override
    public CompletableFuture<Void> unregister(String capabilityId) {
        return CompletableFuture.runAsync(() -> {
            capabilities.remove(capabilityId);
            log.info("Capability unregistered: {}", capabilityId);
        }, executor);
    }
    
    @Override
    public CompletableFuture<CapabilityInfo> getCapability(String capabilityId) {
        return CompletableFuture.supplyAsync(() -> capabilities.get(capabilityId), executor);
    }
    
    @Override
    public CompletableFuture<List<CapabilityInfo>> listCapabilities(CapabilityQuery query) {
        return CompletableFuture.supplyAsync(() -> {
            List<CapabilityInfo> result = new ArrayList<CapabilityInfo>();
            for (CapabilityInfo info : capabilities.values()) {
                if (query.getSpecId() != null && !info.getSpecId().equals(query.getSpecId())) {
                    continue;
                }
                if (query.getNodeId() != null && !info.getNodeId().equals(query.getNodeId())) {
                    continue;
                }
                if (query.getState() != null && info.getState() != query.getState()) {
                    continue;
                }
                result.add(info);
            }
            return result;
        }, executor);
    }
    
    @Override
    public CompletableFuture<Void> enableCapability(String capabilityId) {
        return CompletableFuture.runAsync(() -> {
            CapabilityInfo info = capabilities.get(capabilityId);
            if (info != null) {
                info.setState(CapabilityState.ACTIVE);
                log.info("Capability enabled: {}", capabilityId);
            }
        }, executor);
    }
    
    @Override
    public CompletableFuture<Void> disableCapability(String capabilityId) {
        return CompletableFuture.runAsync(() -> {
            CapabilityInfo info = capabilities.get(capabilityId);
            if (info != null) {
                info.setState(CapabilityState.DISABLED);
                log.info("Capability disabled: {}", capabilityId);
            }
        }, executor);
    }
    
    @Override
    public CompletableFuture<CapabilityInfo> updateCapability(String capabilityId, CapabilityUpdate update) {
        return CompletableFuture.supplyAsync(() -> {
            CapabilityInfo info = capabilities.get(capabilityId);
            if (info != null) {
                if (update.getVersion() != null) {
                    info.setVersion(update.getVersion());
                }
                if (update.getConfig() != null) {
                    info.setConfig(update.getConfig());
                }
                log.info("Capability updated: {}", capabilityId);
            }
            return info;
        }, executor);
    }
    
    @Override
    public CompletableFuture<VersionInfo> getVersion(String capabilityId, String version) {
        return CompletableFuture.supplyAsync(() -> {
            VersionInfo info = new VersionInfo();
            info.setVersion(version);
            info.setDescription("Version " + version);
            info.setReleaseTime(System.currentTimeMillis());
            info.setLatest(true);
            info.setStable(true);
            return info;
        }, executor);
    }
    
    @Override
    public CompletableFuture<List<VersionInfo>> listVersions(String capabilityId) {
        return CompletableFuture.supplyAsync(() -> {
            List<VersionInfo> versions = new ArrayList<VersionInfo>();
            VersionInfo v1 = new VersionInfo();
            v1.setVersion("1.0.0");
            v1.setLatest(true);
            v1.setStable(true);
            versions.add(v1);
            return versions;
        }, executor);
    }
    
    @Override
    public CompletableFuture<Void> rollbackVersion(String capabilityId, String version) {
        return CompletableFuture.runAsync(() -> {
            CapabilityInfo info = capabilities.get(capabilityId);
            if (info == null) {
                log.warn("Capability not found for rollback: {}", capabilityId);
                return;
            }
            
            String currentVersion = info.getVersion();
            info.setVersion(version);
            info.setLastActiveTime(System.currentTimeMillis());
            
            log.info("Capability rolled back: {} from {} to version {}", capabilityId, currentVersion, version);
        }, executor);
    }
}

class CapabilityMonServiceImpl implements CapabilityMonService {
    
    private static final Logger log = LoggerFactory.getLogger(CapabilityMonServiceImpl.class);
    
    private final Map<String, MonitorStatus> monitorStatuses;
    private final Map<String, List<ExecutionLog>> logsStore;
    private final Map<String, List<MetricRecord>> metricsStore;
    private final Map<String, List<ExecutionTrace>> tracesStore;
    private final Map<String, List<AlertInfo>> alertsStore;
    private final List<MonitorListener> listeners;
    private final ExecutorService executor;
    
    CapabilityMonServiceImpl() {
        this.monitorStatuses = new ConcurrentHashMap<String, MonitorStatus>();
        this.logsStore = new ConcurrentHashMap<String, List<ExecutionLog>>();
        this.metricsStore = new ConcurrentHashMap<String, List<MetricRecord>>();
        this.tracesStore = new ConcurrentHashMap<String, List<ExecutionTrace>>();
        this.alertsStore = new ConcurrentHashMap<String, List<AlertInfo>>();
        this.listeners = new CopyOnWriteArrayList<MonitorListener>();
        this.executor = Executors.newCachedThreadPool();
    }
    
    void initialize() {
        log.info("CapabilityMonService initialized");
    }
    
    void shutdown() {
        log.info("CapabilityMonService shutdown");
        executor.shutdown();
        monitorStatuses.clear();
        logsStore.clear();
        metricsStore.clear();
        tracesStore.clear();
        alertsStore.clear();
    }
    
    @Override
    public CompletableFuture<Void> startMonitoring(String capabilityId, MonitorConfig config) {
        return CompletableFuture.runAsync(() -> {
            log.info("Starting monitoring: {}", capabilityId);
            
            MonitorStatus status = new MonitorStatus();
            status.setCapabilityId(capabilityId);
            status.setMonitoring(true);
            status.setStartTime(System.currentTimeMillis());
            status.setHealthStatus(HealthStatus.HEALTHY);
            
            monitorStatuses.put(capabilityId, status);
            logsStore.put(capabilityId, new CopyOnWriteArrayList<ExecutionLog>());
            metricsStore.put(capabilityId, new CopyOnWriteArrayList<MetricRecord>());
            tracesStore.put(capabilityId, new CopyOnWriteArrayList<ExecutionTrace>());
            alertsStore.put(capabilityId, new CopyOnWriteArrayList<AlertInfo>());
            
            log.info("Monitoring started: {}", capabilityId);
        }, executor);
    }
    
    @Override
    public CompletableFuture<Void> stopMonitoring(String capabilityId) {
        return CompletableFuture.runAsync(() -> {
            MonitorStatus status = monitorStatuses.get(capabilityId);
            if (status != null) {
                status.setMonitoring(false);
                log.info("Monitoring stopped: {}", capabilityId);
            }
        }, executor);
    }
    
    @Override
    public CompletableFuture<MonitorStatus> getMonitorStatus(String capabilityId) {
        return CompletableFuture.supplyAsync(() -> monitorStatuses.get(capabilityId), executor);
    }
    
    @Override
    public CompletableFuture<List<ExecutionLog>> getExecutionLogs(String capabilityId, LogQuery query) {
        return CompletableFuture.supplyAsync(() -> {
            List<ExecutionLog> logs = logsStore.get(capabilityId);
            return logs != null ? new ArrayList<ExecutionLog>(logs) : new ArrayList<ExecutionLog>();
        }, executor);
    }
    
    @Override
    public CompletableFuture<List<MetricRecord>> getMetrics(String capabilityId, MetricQuery query) {
        return CompletableFuture.supplyAsync(() -> {
            List<MetricRecord> metrics = metricsStore.get(capabilityId);
            return metrics != null ? new ArrayList<MetricRecord>(metrics) : new ArrayList<MetricRecord>();
        }, executor);
    }
    
    @Override
    public CompletableFuture<ExecutionTrace> getTrace(String traceId) {
        return CompletableFuture.supplyAsync(() -> {
            for (List<ExecutionTrace> traces : tracesStore.values()) {
                for (ExecutionTrace trace : traces) {
                    if (trace.getTraceId().equals(traceId)) {
                        return trace;
                    }
                }
            }
            return null;
        }, executor);
    }
    
    @Override
    public CompletableFuture<List<AlertInfo>> getAlerts(String capabilityId) {
        return CompletableFuture.supplyAsync(() -> {
            List<AlertInfo> alerts = alertsStore.get(capabilityId);
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
            for (List<AlertInfo> alerts : alertsStore.values()) {
                for (AlertInfo alert : alerts) {
                    if (alert.getAlertId().equals(alertId)) {
                        alert.setAcknowledged(true);
                        alert.setAcknowledgedAt(System.currentTimeMillis());
                        log.info("Alert acknowledged: {}", alertId);
                        return;
                    }
                }
            }
        }, executor);
    }
    
    @Override
    public void addMonitorListener(MonitorListener listener) {
        listeners.add(listener);
    }
    
    @Override
    public void removeMonitorListener(MonitorListener listener) {
        listeners.remove(listener);
    }
}

class CapabilityCoopServiceImpl implements CapabilityCoopService {
    
    private static final Logger log = LoggerFactory.getLogger(CapabilityCoopServiceImpl.class);
    
    private final Map<String, OrchestrationDef> orchestrations;
    private final Map<String, SceneGroupDef> sceneGroups;
    private final Map<String, ChainDef> chains;
    private final ExecutorService executor;
    
    CapabilityCoopServiceImpl() {
        this.orchestrations = new ConcurrentHashMap<String, OrchestrationDef>();
        this.sceneGroups = new ConcurrentHashMap<String, SceneGroupDef>();
        this.chains = new ConcurrentHashMap<String, ChainDef>();
        this.executor = Executors.newCachedThreadPool();
    }
    
    void initialize() {
        log.info("CapabilityCoopService initialized");
    }
    
    void shutdown() {
        log.info("CapabilityCoopService shutdown");
        executor.shutdown();
        orchestrations.clear();
        sceneGroups.clear();
        chains.clear();
    }
    
    @Override
    public CompletableFuture<OrchestrationDef> createOrchestration(OrchestrationConfig config) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("Creating orchestration: {}", config.getName());
            
            OrchestrationDef def = new OrchestrationDef();
            def.setOrchestrationId("orch-" + UUID.randomUUID().toString().substring(0, 8));
            def.setName(config.getName());
            def.setDescription(config.getDescription());
            def.setType(config.getType());
            def.setSteps(config.getSteps());
            def.setVariables(config.getVariables());
            def.setErrorHandling(config.getErrorHandling());
            def.setCreatedTime(System.currentTimeMillis());
            def.setUpdatedTime(System.currentTimeMillis());
            def.setStatus(OrchestrationStatus.ACTIVE);
            
            orchestrations.put(def.getOrchestrationId(), def);
            log.info("Orchestration created: {}", def.getOrchestrationId());
            return def;
        }, executor);
    }
    
    @Override
    public CompletableFuture<Void> deleteOrchestration(String orchestrationId) {
        return CompletableFuture.runAsync(() -> {
            orchestrations.remove(orchestrationId);
            log.info("Orchestration deleted: {}", orchestrationId);
        }, executor);
    }
    
    @Override
    public CompletableFuture<OrchestrationDef> getOrchestration(String orchestrationId) {
        return CompletableFuture.supplyAsync(() -> orchestrations.get(orchestrationId), executor);
    }
    
    @Override
    public CompletableFuture<List<OrchestrationDef>> listOrchestrations() {
        return CompletableFuture.supplyAsync(() -> new ArrayList<OrchestrationDef>(orchestrations.values()), executor);
    }
    
    @Override
    public CompletableFuture<OrchestrationResult> executeOrchestration(String orchestrationId, Map<String, Object> input) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("Executing orchestration: {}", orchestrationId);
            
            OrchestrationDef def = orchestrations.get(orchestrationId);
            if (def == null) {
                log.warn("Orchestration not found: {}", orchestrationId);
                OrchestrationResult notFoundResult = new OrchestrationResult();
                notFoundResult.setExecutionId("exec-" + UUID.randomUUID().toString().substring(0, 8));
                notFoundResult.setOrchestrationId(orchestrationId);
                notFoundResult.setSuccess(false);
                notFoundResult.setErrorMessage("Orchestration not found: " + orchestrationId);
                return notFoundResult;
            }
            
            long startTime = System.currentTimeMillis();
            List<StepResult> stepResults = new ArrayList<StepResult>();
            Map<String, Object> output = new HashMap<String, Object>(input);
            boolean success = true;
            String errorMessage = null;
            
            List<OrchestrationStep> steps = def.getSteps();
            if (steps != null) {
                for (int i = 0; i < steps.size(); i++) {
                    OrchestrationStep step = steps.get(i);
                    StepResult stepResult = new StepResult();
                    stepResult.setStepId(step.getStepId());
                    stepResult.setStartTime(System.currentTimeMillis());
                    
                    try {
                        Map<String, Object> stepOutput = new HashMap<String, Object>();
                        stepOutput.put("input", output);
                        stepOutput.put("action", step.getAction());
                        stepOutput.put("processed", true);
                        
                        stepResult.setOutput(stepOutput);
                        stepResult.setSuccess(true);
                        output.putAll(stepOutput);
                    } catch (Exception e) {
                        stepResult.setSuccess(false);
                        stepResult.setErrorMessage(e.getMessage());
                        success = false;
                        errorMessage = "Step " + step.getName() + " failed: " + e.getMessage();
                        log.warn("Step {} failed: {}", step.getName(), e.getMessage());
                        
                        if ("STOP".equals(step.getOnError())) {
                            break;
                        }
                    }
                    
                    stepResult.setEndTime(System.currentTimeMillis());
                    stepResults.add(stepResult);
                }
            }
            
            OrchestrationResult result = new OrchestrationResult();
            result.setExecutionId("exec-" + UUID.randomUUID().toString().substring(0, 8));
            result.setOrchestrationId(orchestrationId);
            result.setSuccess(success);
            result.setOutput(output);
            result.setStepResults(stepResults);
            result.setStartTime(startTime);
            result.setEndTime(System.currentTimeMillis());
            if (errorMessage != null) {
                result.setErrorMessage(errorMessage);
            }
            
            log.info("Orchestration executed: {} with {} steps, success={}", orchestrationId, stepResults.size(), success);
            return result;
        }, executor);
    }
    
    @Override
    public CompletableFuture<SceneGroupDef> createSceneGroup(SceneGroupConfig config) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("Creating scene group: {}", config.getName());
            
            SceneGroupDef def = new SceneGroupDef();
            def.setSceneGroupId("sg-" + UUID.randomUUID().toString().substring(0, 8));
            def.setName(config.getName());
            def.setSceneId(config.getSceneId());
            def.setMemberIds(new ArrayList<String>());
            def.setMemberCount(0);
            def.setStatus(SceneGroupStatus.ACTIVE);
            def.setCreatedTime(System.currentTimeMillis());
            def.setProperties(config.getProperties());
            
            sceneGroups.put(def.getSceneGroupId(), def);
            log.info("Scene group created: {}", def.getSceneGroupId());
            return def;
        }, executor);
    }
    
    @Override
    public CompletableFuture<Void> deleteSceneGroup(String sceneGroupId) {
        return CompletableFuture.runAsync(() -> {
            sceneGroups.remove(sceneGroupId);
            log.info("Scene group deleted: {}", sceneGroupId);
        }, executor);
    }
    
    @Override
    public CompletableFuture<SceneGroupDef> getSceneGroup(String sceneGroupId) {
        return CompletableFuture.supplyAsync(() -> sceneGroups.get(sceneGroupId), executor);
    }
    
    @Override
    public CompletableFuture<List<SceneGroupDef>> listSceneGroups() {
        return CompletableFuture.supplyAsync(() -> new ArrayList<SceneGroupDef>(sceneGroups.values()), executor);
    }
    
    @Override
    public CompletableFuture<ChainDef> createChain(ChainConfig config) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("Creating chain: {}", config.getName());
            
            ChainDef def = new ChainDef();
            def.setChainId("chain-" + UUID.randomUUID().toString().substring(0, 8));
            def.setName(config.getName());
            def.setDescription(config.getDescription());
            def.setLinks(config.getLinks());
            def.setErrorHandling(config.getErrorHandling());
            def.setCreatedTime(System.currentTimeMillis());
            def.setStatus(ChainStatus.ACTIVE);
            
            chains.put(def.getChainId(), def);
            log.info("Chain created: {}", def.getChainId());
            return def;
        }, executor);
    }
    
    @Override
    public CompletableFuture<Void> deleteChain(String chainId) {
        return CompletableFuture.runAsync(() -> {
            chains.remove(chainId);
            log.info("Chain deleted: {}", chainId);
        }, executor);
    }
    
    @Override
    public CompletableFuture<ChainResult> executeChain(String chainId, Map<String, Object> input) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("Executing chain: {}", chainId);
            
            ChainDef def = chains.get(chainId);
            if (def == null) {
                log.warn("Chain not found: {}", chainId);
                ChainResult notFoundResult = new ChainResult();
                notFoundResult.setExecutionId("exec-" + UUID.randomUUID().toString().substring(0, 8));
                notFoundResult.setChainId(chainId);
                notFoundResult.setSuccess(false);
                notFoundResult.setErrorMessage("Chain not found: " + chainId);
                return notFoundResult;
            }
            
            long startTime = System.currentTimeMillis();
            List<LinkResult> linkResults = new ArrayList<LinkResult>();
            Map<String, Object> output = new HashMap<String, Object>(input);
            boolean success = true;
            String errorMessage = null;
            
            List<ChainLink> links = def.getLinks();
            if (links != null) {
                for (int i = 0; i < links.size(); i++) {
                    ChainLink link = links.get(i);
                    LinkResult linkResult = new LinkResult();
                    linkResult.setLinkId(link.getLinkId());
                    linkResult.setStartTime(System.currentTimeMillis());
                    
                    try {
                        Map<String, Object> linkOutput = new HashMap<String, Object>();
                        linkOutput.put("input", output);
                        linkOutput.put("capabilityId", link.getCapabilityId());
                        linkOutput.put("processed", true);
                        
                        linkResult.setOutput(linkOutput);
                        linkResult.setSuccess(true);
                        output.putAll(linkOutput);
                    } catch (Exception e) {
                        linkResult.setSuccess(false);
                        linkResult.setErrorMessage(e.getMessage());
                        success = false;
                        errorMessage = "Link " + link.getName() + " failed: " + e.getMessage();
                        log.warn("Link {} failed: {}", link.getName(), e.getMessage());
                        
                        if (def.getErrorHandling() != null) {
                            break;
                        }
                    }
                    
                    linkResult.setEndTime(System.currentTimeMillis());
                    linkResults.add(linkResult);
                }
            }
            
            ChainResult result = new ChainResult();
            result.setExecutionId("exec-" + UUID.randomUUID().toString().substring(0, 8));
            result.setChainId(chainId);
            result.setSuccess(success);
            result.setOutput(output);
            result.setLinkResults(linkResults);
            result.setStartTime(startTime);
            result.setEndTime(System.currentTimeMillis());
            if (errorMessage != null) {
                result.setErrorMessage(errorMessage);
            }
            
            log.info("Chain executed: {} with {} links, success={}", chainId, linkResults.size(), success);
            return result;
        }, executor);
    }
}
