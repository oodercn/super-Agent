package net.ooder.nexus.skillcenter.controller;

import net.ooder.nexus.skillcenter.model.ResultModel;
import net.ooder.nexus.skillcenter.dto.domain.*;
import net.ooder.skillcenter.dto.PageResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/observer")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS})
public class ObserverController extends BaseController {

    private final Map<String, ObservationDTO> observationStore;
    private final Map<String, StaticTopologyDTO> staticTopologyStore;
    private final Map<String, DynamicRoutingDTO> dynamicRoutingStore;
    private final Map<String, HistoryLogDTO> historyLogStore;

    public ObserverController() {
        this.observationStore = new ConcurrentHashMap<>();
        this.staticTopologyStore = new ConcurrentHashMap<>();
        this.dynamicRoutingStore = new ConcurrentHashMap<>();
        this.historyLogStore = new ConcurrentHashMap<>();
        initMockData();
    }

    private void initMockData() {
        StaticTopologyDTO topology = createMockStaticTopology("domain-001");
        staticTopologyStore.put(topology.getTopologyId(), topology);

        DynamicRoutingDTO routing = createMockDynamicRouting("domain-001");
        dynamicRoutingStore.put(routing.getRoutingId(), routing);

        HistoryLogDTO history = createMockHistoryLog("domain-001");
        historyLogStore.put(history.getLogId(), history);
    }

    @PostMapping("/observe")
    public ResultModel<ObservationDTO> startObservation(@RequestBody ObservationRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("startObservation", request);

        try {
            String observationId = "obs-" + UUID.randomUUID().toString().substring(0, 8);
            
            ObservationDTO observation = new ObservationDTO();
            observation.setObservationId(observationId);
            observation.setDomainId(request.getDomainId());
            observation.setObserverId(request.getObserverId());
            observation.setType(request.getType());
            observation.setScope(request.getScope());
            observation.setTimestamp(System.currentTimeMillis());
            observation.setStartTime(System.currentTimeMillis());
            
            List<ObservationDTO.ObservationMetricDTO> metrics = generateMockMetrics(request.getType());
            observation.setMetrics(metrics);
            
            List<ObservationDTO.ObservationEventDTO> events = generateMockEvents();
            observation.setEvents(events);
            
            observationStore.put(observationId, observation);

            logRequestEnd("startObservation", observation, System.currentTimeMillis() - startTime);
            return ResultModel.success("观测启动成功", observation);
        } catch (Exception e) {
            logRequestError("startObservation", e);
            return ResultModel.error(500, "启动观测失败: " + e.getMessage());
        }
    }

    @PostMapping("/stop")
    public ResultModel<Boolean> stopObservation(@RequestBody ObservationIdRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("stopObservation", request);

        try {
            ObservationDTO observation = observationStore.get(request.getObservationId());
            if (observation == null) {
                return ResultModel.notFound("观测任务不存在");
            }
            observation.setEndTime(System.currentTimeMillis());

            logRequestEnd("stopObservation", true, System.currentTimeMillis() - startTime);
            return ResultModel.success("观测已停止", true);
        } catch (Exception e) {
            logRequestError("stopObservation", e);
            return ResultModel.error(500, "停止观测失败: " + e.getMessage());
        }
    }

    @PostMapping("/get")
    public ResultModel<ObservationDTO> getObservation(@RequestBody ObservationIdRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("getObservation", request);

        try {
            ObservationDTO observation = observationStore.get(request.getObservationId());
            if (observation == null) {
                return ResultModel.notFound("观测任务不存在");
            }
            logRequestEnd("getObservation", observation, System.currentTimeMillis() - startTime);
            return ResultModel.success(observation);
        } catch (Exception e) {
            logRequestError("getObservation", e);
            return ResultModel.error(500, "获取观测失败: " + e.getMessage());
        }
    }

    @PostMapping("/list")
    public ResultModel<PageResult<ObservationDTO>> listObservations(@RequestBody DomainPageRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("listObservations", request);

        try {
            List<ObservationDTO> filtered = new ArrayList<>();
            for (ObservationDTO obs : observationStore.values()) {
                if (request.getDomainId() == null || request.getDomainId().equals(obs.getDomainId())) {
                    filtered.add(obs);
                }
            }
            
            int total = filtered.size();
            int fromIndex = (request.getPageNum() - 1) * request.getPageSize();
            int toIndex = Math.min(fromIndex + request.getPageSize(), total);
            List<ObservationDTO> paged = fromIndex < total ? 
                filtered.subList(fromIndex, toIndex) : new ArrayList<>();
            
            PageResult<ObservationDTO> result = new PageResult<>(paged, total, request.getPageNum(), request.getPageSize());

            logRequestEnd("listObservations", total + " observations", System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("listObservations", e);
            return ResultModel.error(500, "获取观测列表失败: " + e.getMessage());
        }
    }

    @PostMapping("/topology/static/get")
    public ResultModel<StaticTopologyDTO> getStaticTopology(@RequestBody TopologyIdRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("getStaticTopology", request);

        try {
            StaticTopologyDTO topology = staticTopologyStore.get(request.getTopologyId());
            if (topology == null) {
                topology = createMockStaticTopology(request.getDomainId());
                staticTopologyStore.put(topology.getTopologyId(), topology);
            }
            logRequestEnd("getStaticTopology", topology, System.currentTimeMillis() - startTime);
            return ResultModel.success(topology);
        } catch (Exception e) {
            logRequestError("getStaticTopology", e);
            return ResultModel.error(500, "获取静态拓扑失败: " + e.getMessage());
        }
    }

    @PostMapping("/topology/static/define")
    public ResultModel<StaticTopologyDTO> defineStaticTopology(@RequestBody StaticTopologyDefineRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("defineStaticTopology", request);

        try {
            String topologyId = "topo-" + UUID.randomUUID().toString().substring(0, 8);
            
            StaticTopologyDTO topology = new StaticTopologyDTO();
            topology.setTopologyId(topologyId);
            topology.setDomainId(request.getDomainId());
            topology.setSpec(request.getSpec());
            
            StaticTopologyDTO.TopologyStatus status = new StaticTopologyDTO.TopologyStatus();
            status.setPhase("defined");
            status.setMessage("拓扑已定义，等待部署");
            status.setTotalNodes(request.getSpec().getNodes() != null ? request.getSpec().getNodes().size() : 0);
            status.setTotalEdges(request.getSpec().getEdges() != null ? request.getSpec().getEdges().size() : 0);
            status.setDeployedNodes(0);
            status.setActiveEdges(0);
            topology.setStatus(status);
            topology.setCreatedAt(System.currentTimeMillis());
            topology.setUpdatedAt(System.currentTimeMillis());
            
            staticTopologyStore.put(topologyId, topology);

            logRequestEnd("defineStaticTopology", topology, System.currentTimeMillis() - startTime);
            return ResultModel.success("静态拓扑定义成功", topology);
        } catch (Exception e) {
            logRequestError("defineStaticTopology", e);
            return ResultModel.error(500, "定义静态拓扑失败: " + e.getMessage());
        }
    }

    @PostMapping("/topology/static/deploy")
    public ResultModel<StaticTopologyDTO> deployStaticTopology(@RequestBody TopologyIdRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("deployStaticTopology", request);

        try {
            StaticTopologyDTO topology = staticTopologyStore.get(request.getTopologyId());
            if (topology == null) {
                return ResultModel.notFound("拓扑不存在");
            }
            
            StaticTopologyDTO.TopologyStatus status = topology.getStatus();
            status.setPhase("deploying");
            status.setMessage("正在部署拓扑...");
            topology.setUpdatedAt(System.currentTimeMillis());

            logRequestEnd("deployStaticTopology", topology, System.currentTimeMillis() - startTime);
            return ResultModel.success("拓扑部署已启动", topology);
        } catch (Exception e) {
            logRequestError("deployStaticTopology", e);
            return ResultModel.error(500, "部署静态拓扑失败: " + e.getMessage());
        }
    }

    @PostMapping("/routing/dynamic/get")
    public ResultModel<DynamicRoutingDTO> getDynamicRouting(@RequestBody RoutingIdRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("getDynamicRouting", request);

        try {
            DynamicRoutingDTO routing = dynamicRoutingStore.get(request.getRoutingId());
            if (routing == null) {
                routing = createMockDynamicRouting(request.getDomainId());
                dynamicRoutingStore.put(routing.getRoutingId(), routing);
            }
            logRequestEnd("getDynamicRouting", routing, System.currentTimeMillis() - startTime);
            return ResultModel.success(routing);
        } catch (Exception e) {
            logRequestError("getDynamicRouting", e);
            return ResultModel.error(500, "获取动态路由失败: " + e.getMessage());
        }
    }

    @PostMapping("/routing/dynamic/observe")
    public ResultModel<DynamicRoutingDTO> observeDynamicRouting(@RequestBody DomainIdRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("observeDynamicRouting", request);

        try {
            DynamicRoutingDTO routing = createMockDynamicRouting(request.getDomainId());
            routing.setRoutingId("route-obs-" + UUID.randomUUID().toString().substring(0, 8));
            dynamicRoutingStore.put(routing.getRoutingId(), routing);

            logRequestEnd("observeDynamicRouting", routing, System.currentTimeMillis() - startTime);
            return ResultModel.success("动态路由观测已启动", routing);
        } catch (Exception e) {
            logRequestError("observeDynamicRouting", e);
            return ResultModel.error(500, "启动动态路由观测失败: " + e.getMessage());
        }
    }

    @PostMapping("/history/get")
    public ResultModel<HistoryLogDTO> getHistoryLog(@RequestBody LogIdRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("getHistoryLog", request);

        try {
            HistoryLogDTO history = historyLogStore.get(request.getLogId());
            if (history == null) {
                history = createMockHistoryLog(request.getDomainId());
                historyLogStore.put(history.getLogId(), history);
            }
            logRequestEnd("getHistoryLog", history, System.currentTimeMillis() - startTime);
            return ResultModel.success(history);
        } catch (Exception e) {
            logRequestError("getHistoryLog", e);
            return ResultModel.error(500, "获取历史日志失败: " + e.getMessage());
        }
    }

    @PostMapping("/history/query")
    public ResultModel<HistoryLogDTO> queryHistoryLog(@RequestBody HistoryQueryRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("queryHistoryLog", request);

        try {
            HistoryLogDTO history = createMockHistoryLog(request.getDomainId());
            history.setStartTime(request.getStartTime());
            history.setEndTime(request.getEndTime());
            history.setScope(request.getScope());

            logRequestEnd("queryHistoryLog", history, System.currentTimeMillis() - startTime);
            return ResultModel.success(history);
        } catch (Exception e) {
            logRequestError("queryHistoryLog", e);
            return ResultModel.error(500, "查询历史日志失败: " + e.getMessage());
        }
    }

    @PostMapping("/history/correct")
    public ResultModel<HistoryLogDTO.CorrectionDTO> correctHistoryEntry(@RequestBody CorrectionRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("correctHistoryEntry", request);

        try {
            HistoryLogDTO.CorrectionDTO correction = new HistoryLogDTO.CorrectionDTO();
            correction.setCorrectionId("corr-" + UUID.randomUUID().toString().substring(0, 8));
            correction.setCorrectionType(request.getCorrectionType());
            correction.setDescription(request.getDescription());
            correction.setCorrectedBy(request.getCorrectedBy());
            correction.setCorrectedAt(System.currentTimeMillis());
            correction.setBeforeState(request.getBeforeState());
            correction.setAfterState(request.getAfterState());

            logRequestEnd("correctHistoryEntry", correction, System.currentTimeMillis() - startTime);
            return ResultModel.success("历史纠正已记录", correction);
        } catch (Exception e) {
            logRequestError("correctHistoryEntry", e);
            return ResultModel.error(500, "历史纠正失败: " + e.getMessage());
        }
    }

    private List<ObservationDTO.ObservationMetricDTO> generateMockMetrics(ObservationDTO.ObservationType type) {
        List<ObservationDTO.ObservationMetricDTO> metrics = new ArrayList<>();
        
        ObservationDTO.ObservationMetricDTO metric1 = new ObservationDTO.ObservationMetricDTO();
        metric1.setMetricId("metric-1");
        metric1.setName("cpu_usage");
        metric1.setUnit("percent");
        metric1.setValue(45.5);
        metric1.setThreshold(80.0);
        metric1.setStatus("normal");
        metrics.add(metric1);

        ObservationDTO.ObservationMetricDTO metric2 = new ObservationDTO.ObservationMetricDTO();
        metric2.setMetricId("metric-2");
        metric2.setName("memory_usage");
        metric2.setUnit("percent");
        metric2.setValue(62.3);
        metric2.setThreshold(85.0);
        metric2.setStatus("normal");
        metrics.add(metric2);

        ObservationDTO.ObservationMetricDTO metric3 = new ObservationDTO.ObservationMetricDTO();
        metric3.setMetricId("metric-3");
        metric3.setName("network_latency");
        metric3.setUnit("ms");
        metric3.setValue(35.0);
        metric3.setThreshold(100.0);
        metric3.setStatus("normal");
        metrics.add(metric3);

        return metrics;
    }

    private List<ObservationDTO.ObservationEventDTO> generateMockEvents() {
        List<ObservationDTO.ObservationEventDTO> events = new ArrayList<>();
        
        ObservationDTO.ObservationEventDTO event1 = new ObservationDTO.ObservationEventDTO();
        event1.setEventId("evt-1");
        event1.setEventType("NODE_JOINED");
        event1.setSeverity("INFO");
        event1.setMessage("节点 node-002 加入域");
        event1.setSourceNodeId("node-002");
        event1.setEventTime(System.currentTimeMillis() - 3600000L);
        events.add(event1);

        ObservationDTO.ObservationEventDTO event2 = new ObservationDTO.ObservationEventDTO();
        event2.setEventId("evt-2");
        event2.setEventType("SKILL_EXECUTED");
        event2.setSeverity("INFO");
        event2.setMessage("技能 text-analyzer 执行完成");
        event2.setSourceNodeId("node-001");
        event2.setEventTime(System.currentTimeMillis() - 1800000L);
        events.add(event2);

        return events;
    }

    private StaticTopologyDTO createMockStaticTopology(String domainId) {
        StaticTopologyDTO topology = new StaticTopologyDTO();
        topology.setTopologyId("topo-static-" + UUID.randomUUID().toString().substring(0, 8));
        topology.setDomainId(domainId);
        topology.setCreatedAt(System.currentTimeMillis());
        topology.setUpdatedAt(System.currentTimeMillis());

        StaticTopologyDTO.TopologySpec spec = new StaticTopologyDTO.TopologySpec();
        
        List<StaticTopologyDTO.NodeSpec> nodes = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            StaticTopologyDTO.NodeSpec node = new StaticTopologyDTO.NodeSpec();
            node.setNodeId("node-" + i);
            node.setNodeType(i == 1 ? "primary" : "worker");
            node.setRole(i == 1 ? "manager" : "executor");
            node.setPriority(i);
            nodes.add(node);
        }
        spec.setNodes(nodes);

        List<StaticTopologyDTO.EdgeSpec> edges = new ArrayList<>();
        for (int i = 1; i <= 2; i++) {
            StaticTopologyDTO.EdgeSpec edge = new StaticTopologyDTO.EdgeSpec();
            edge.setSourceNodeId("node-" + i);
            edge.setTargetNodeId("node-" + (i + 1));
            edge.setEdgeType("data");
            edge.setBandwidth(1000);
            edge.setLatency(10);
            edge.setBidirectional(true);
            edges.add(edge);
        }
        spec.setEdges(edges);
        spec.setDeploymentStrategy("rolling");

        topology.setSpec(spec);

        StaticTopologyDTO.TopologyStatus status = new StaticTopologyDTO.TopologyStatus();
        status.setPhase("deployed");
        status.setMessage("拓扑已部署");
        status.setTotalNodes(3);
        status.setDeployedNodes(3);
        status.setTotalEdges(2);
        status.setActiveEdges(2);
        topology.setStatus(status);

        return topology;
    }

    private DynamicRoutingDTO createMockDynamicRouting(String domainId) {
        DynamicRoutingDTO routing = new DynamicRoutingDTO();
        routing.setRoutingId("route-dynamic-" + UUID.randomUUID().toString().substring(0, 8));
        routing.setDomainId(domainId);
        routing.setTimestamp(System.currentTimeMillis());

        DynamicRoutingDTO.RoutingState state = new DynamicRoutingDTO.RoutingState();
        state.setTotalRoutes(10);
        state.setActiveRoutes(8);
        state.setFailedRoutes(2);
        state.setAvgLatency(45.5);
        state.setAvgBandwidth(850.0);
        state.setHealthStatus("healthy");
        routing.setState(state);

        List<DynamicRoutingDTO.RouteDTO> routes = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            DynamicRoutingDTO.RouteDTO route = new DynamicRoutingDTO.RouteDTO();
            route.setRouteId("route-" + i);
            route.setSourceNodeId("node-1");
            route.setTargetNodeId("node-" + (i + 1));
            route.setHops(Arrays.asList("node-1", "node-" + (i + 1)));
            route.setStatus(i <= 4 ? "active" : "failed");
            route.setCurrentLatency(30 + i * 10);
            route.setCurrentBandwidth(800 + i * 50);
            route.setLastUpdated(System.currentTimeMillis() - i * 60000L);
            route.setHopCount(2);
            routes.add(route);
        }
        routing.setRoutes(routes);

        List<DynamicRoutingDTO.RoutingEventDTO> events = new ArrayList<>();
        DynamicRoutingDTO.RoutingEventDTO event = new DynamicRoutingDTO.RoutingEventDTO();
        event.setEventId("route-evt-1");
        event.setEventType("ROUTE_CHANGED");
        event.setRouteId("route-3");
        event.setMessage("路由路径已更新");
        event.setSeverity("INFO");
        event.setEventTime(System.currentTimeMillis() - 300000L);
        events.add(event);
        routing.setEvents(events);

        return routing;
    }

    private HistoryLogDTO createMockHistoryLog(String domainId) {
        HistoryLogDTO history = new HistoryLogDTO();
        history.setLogId("log-" + UUID.randomUUID().toString().substring(0, 8));
        history.setDomainId(domainId);
        history.setScope(HistoryLogDTO.LogScope.DOMAIN);
        history.setStartTime(System.currentTimeMillis() - 86400000L);
        history.setEndTime(System.currentTimeMillis());

        List<HistoryLogDTO.LogEntryDTO> entries = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            HistoryLogDTO.LogEntryDTO entry = new HistoryLogDTO.LogEntryDTO();
            entry.setEntryId("entry-" + i);
            entry.setEventType(i % 2 == 0 ? "EXECUTION_COMPLETED" : "MEMBER_JOINED");
            entry.setSeverity(i % 3 == 0 ? "WARN" : "INFO");
            entry.setSourceNodeId("node-" + ((i % 3) + 1));
            entry.setSourceType("agent");
            entry.setMessage("Event " + i + " occurred");
            entry.setTimestamp(System.currentTimeMillis() - i * 3600000L);
            entries.add(entry);
        }
        history.setEntries(entries);

        HistoryLogDTO.LogStatistics stats = new HistoryLogDTO.LogStatistics();
        stats.setTotalEntries(5);
        stats.setErrorCount(0);
        stats.setWarningCount(1);
        stats.setInfoCount(4);
        stats.setCorrectionCount(0);
        history.setStatistics(stats);

        return history;
    }

    public static class ObservationRequest {
        private String domainId;
        private String observerId;
        private ObservationDTO.ObservationType type;
        private ObservationDTO.ObservationScope scope;

        public String getDomainId() { return domainId; }
        public void setDomainId(String domainId) { this.domainId = domainId; }
        public String getObserverId() { return observerId; }
        public void setObserverId(String observerId) { this.observerId = observerId; }
        public ObservationDTO.ObservationType getType() { return type; }
        public void setType(ObservationDTO.ObservationType type) { this.type = type; }
        public ObservationDTO.ObservationScope getScope() { return scope; }
        public void setScope(ObservationDTO.ObservationScope scope) { this.scope = scope; }
    }

    public static class ObservationIdRequest {
        private String observationId;

        public String getObservationId() { return observationId; }
        public void setObservationId(String observationId) { this.observationId = observationId; }
    }

    public static class DomainPageRequest {
        private String domainId;
        private int pageNum = 1;
        private int pageSize = 10;

        public String getDomainId() { return domainId; }
        public void setDomainId(String domainId) { this.domainId = domainId; }
        public int getPageNum() { return pageNum; }
        public void setPageNum(int pageNum) { this.pageNum = pageNum; }
        public int getPageSize() { return pageSize; }
        public void setPageSize(int pageSize) { this.pageSize = pageSize; }
    }

    public static class TopologyIdRequest {
        private String topologyId;
        private String domainId;

        public String getTopologyId() { return topologyId; }
        public void setTopologyId(String topologyId) { this.topologyId = topologyId; }
        public String getDomainId() { return domainId; }
        public void setDomainId(String domainId) { this.domainId = domainId; }
    }

    public static class DomainIdRequest {
        private String domainId;

        public String getDomainId() { return domainId; }
        public void setDomainId(String domainId) { this.domainId = domainId; }
    }

    public static class RoutingIdRequest {
        private String routingId;
        private String domainId;

        public String getRoutingId() { return routingId; }
        public void setRoutingId(String routingId) { this.routingId = routingId; }
        public String getDomainId() { return domainId; }
        public void setDomainId(String domainId) { this.domainId = domainId; }
    }

    public static class LogIdRequest {
        private String logId;
        private String domainId;

        public String getLogId() { return logId; }
        public void setLogId(String logId) { this.logId = logId; }
        public String getDomainId() { return domainId; }
        public void setDomainId(String domainId) { this.domainId = domainId; }
    }

    public static class StaticTopologyDefineRequest {
        private String domainId;
        private StaticTopologyDTO.TopologySpec spec;

        public String getDomainId() { return domainId; }
        public void setDomainId(String domainId) { this.domainId = domainId; }
        public StaticTopologyDTO.TopologySpec getSpec() { return spec; }
        public void setSpec(StaticTopologyDTO.TopologySpec spec) { this.spec = spec; }
    }

    public static class HistoryQueryRequest {
        private String domainId;
        private HistoryLogDTO.LogScope scope;
        private long startTime;
        private long endTime;

        public String getDomainId() { return domainId; }
        public void setDomainId(String domainId) { this.domainId = domainId; }
        public HistoryLogDTO.LogScope getScope() { return scope; }
        public void setScope(HistoryLogDTO.LogScope scope) { this.scope = scope; }
        public long getStartTime() { return startTime; }
        public void setStartTime(long startTime) { this.startTime = startTime; }
        public long getEndTime() { return endTime; }
        public void setEndTime(long endTime) { this.endTime = endTime; }
    }

    public static class CorrectionRequest {
        private String logEntryId;
        private String correctionType;
        private String description;
        private String correctedBy;
        private Map<String, Object> beforeState;
        private Map<String, Object> afterState;

        public String getLogEntryId() { return logEntryId; }
        public void setLogEntryId(String logEntryId) { this.logEntryId = logEntryId; }
        public String getCorrectionType() { return correctionType; }
        public void setCorrectionType(String correctionType) { this.correctionType = correctionType; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getCorrectedBy() { return correctedBy; }
        public void setCorrectedBy(String correctedBy) { this.correctedBy = correctedBy; }
        public Map<String, Object> getBeforeState() { return beforeState; }
        public void setBeforeState(Map<String, Object> beforeState) { this.beforeState = beforeState; }
        public Map<String, Object> getAfterState() { return afterState; }
        public void setAfterState(Map<String, Object> afterState) { this.afterState = afterState; }
    }
}
