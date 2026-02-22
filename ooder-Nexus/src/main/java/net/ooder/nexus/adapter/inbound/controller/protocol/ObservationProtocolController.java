package net.ooder.nexus.adapter.inbound.controller.protocol;

import net.ooder.config.ResultModel;
import net.ooder.sdk.northbound.protocol.ObservationProtocol;
import net.ooder.sdk.northbound.protocol.model.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/protocol/observation")
public class ObservationProtocolController {

    private static final Logger log = LoggerFactory.getLogger(ObservationProtocolController.class);

    private final ObservationProtocol observationProtocol;

    @Autowired
    public ObservationProtocolController(@Autowired(required = false) ObservationProtocol observationProtocol) {
        this.observationProtocol = observationProtocol;
        log.info("ObservationProtocolController initialized: {}", 
            observationProtocol != null ? "SDK protocol available" : "using mock");
    }

    @PostMapping("/start")
    @ResponseBody
    public ResultModel<Void> startObservation(@RequestBody Map<String, Object> params) {
        log.info("Start observation requested: {}", params);
        ResultModel<Void> result = new ResultModel<>();

        try {
            String targetId = (String) params.get("targetId");
            
            if (observationProtocol == null) {
                result.setRequestStatus(503);
                result.setMessage("SDK not available");
            } else {
                ObservationConfig config = new ObservationConfig();
                config.setEnableMetrics(true);
                config.setEnableLogs(true);
                config.setEnableTraces(true);
                config.setMetricsInterval(60);
                config.setRetentionDays(7);
                
                CompletableFuture<Void> future = observationProtocol.startObservation(targetId, config);
                future.get();
                
                result.setRequestStatus(200);
                result.setMessage("Observation started");
            }
        } catch (Exception e) {
            log.error("Start observation failed", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to start observation: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/stop")
    @ResponseBody
    public ResultModel<Void> stopObservation(@RequestBody Map<String, Object> params) {
        log.info("Stop observation requested: {}", params);
        ResultModel<Void> result = new ResultModel<>();

        try {
            String targetId = (String) params.get("targetId");
            
            if (observationProtocol != null) {
                observationProtocol.stopObservation(targetId);
            }
            result.setRequestStatus(200);
            result.setMessage("Observation stopped");
        } catch (Exception e) {
            log.error("Stop observation failed", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to stop observation: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/status")
    @ResponseBody
    public ResultModel<ObservationStatus> getObservationStatus(@RequestBody Map<String, Object> params) {
        log.info("Get observation status requested: {}", params);
        ResultModel<ObservationStatus> result = new ResultModel<>();

        try {
            String targetId = (String) params.get("targetId");
            
            if (observationProtocol == null) {
                result.setRequestStatus(503);
                result.setMessage("SDK not available");
            } else {
                CompletableFuture<ObservationStatus> future = observationProtocol.getObservationStatus(targetId);
                ObservationStatus status = future.get();
                result.setData(status);
                result.setRequestStatus(200);
            }
        } catch (Exception e) {
            log.error("Get observation status failed", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to get observation status: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/metrics")
    @ResponseBody
    public ResultModel<List<ObservationMetric>> getMetrics(@RequestBody Map<String, Object> params) {
        log.info("Get metrics requested: {}", params);
        ResultModel<List<ObservationMetric>> result = new ResultModel<>();

        try {
            String targetId = (String) params.get("targetId");
            
            if (observationProtocol == null) {
                result.setData(new ArrayList<>());
                result.setRequestStatus(200);
            } else {
                MetricQuery query = new MetricQuery();
                query.setMetricType((String) params.get("metricType"));
                query.setStartTime(((Number) params.getOrDefault("startTime", 0)).longValue());
                query.setEndTime(((Number) params.getOrDefault("endTime", System.currentTimeMillis())).longValue());
                query.setLimit(((Number) params.getOrDefault("limit", 100)).intValue());
                
                CompletableFuture<List<ObservationMetric>> future = observationProtocol.getMetrics(targetId, query);
                List<ObservationMetric> metrics = future.get();
                result.setData(metrics);
                result.setRequestStatus(200);
            }
        } catch (Exception e) {
            log.error("Get metrics failed", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to get metrics: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/logs")
    @ResponseBody
    public ResultModel<List<ObservationLog>> getLogs(@RequestBody Map<String, Object> params) {
        log.info("Get logs requested: {}", params);
        ResultModel<List<ObservationLog>> result = new ResultModel<>();

        try {
            String targetId = (String) params.get("targetId");
            
            if (observationProtocol == null) {
                result.setData(new ArrayList<>());
                result.setRequestStatus(200);
            } else {
                LogQuery query = new LogQuery();
                Object levelParam = params.get("level");
                if (levelParam instanceof Number) {
                    query.setLevel(((Number) levelParam).intValue());
                } else if (levelParam instanceof String) {
                    query.setLevel(Integer.parseInt((String) levelParam));
                }
                query.setKeyword((String) params.get("keyword"));
                query.setLimit(((Number) params.getOrDefault("limit", 100)).intValue());
                
                CompletableFuture<List<ObservationLog>> future = observationProtocol.getLogs(targetId, query);
                List<ObservationLog> logs = future.get();
                result.setData(logs);
                result.setRequestStatus(200);
            }
        } catch (Exception e) {
            log.error("Get logs failed", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to get logs: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/traces")
    @ResponseBody
    public ResultModel<List<ObservationTrace>> getTraces(@RequestBody Map<String, Object> params) {
        log.info("Get traces requested: {}", params);
        ResultModel<List<ObservationTrace>> result = new ResultModel<>();

        try {
            String targetId = (String) params.get("targetId");
            
            if (observationProtocol == null) {
                result.setData(new ArrayList<>());
                result.setRequestStatus(200);
            } else {
                TraceQuery query = new TraceQuery();
                query.setOperationType((String) params.get("operationType"));
                query.setLimit(((Number) params.getOrDefault("limit", 100)).intValue());
                
                CompletableFuture<List<ObservationTrace>> future = observationProtocol.getTraces(targetId, query);
                List<ObservationTrace> traces = future.get();
                result.setData(traces);
                result.setRequestStatus(200);
            }
        } catch (Exception e) {
            log.error("Get traces failed", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to get traces: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/snapshot")
    @ResponseBody
    public ResultModel<ObservationSnapshot> getSnapshot(@RequestBody Map<String, Object> params) {
        log.info("Get snapshot requested: {}", params);
        ResultModel<ObservationSnapshot> result = new ResultModel<>();

        try {
            String targetId = (String) params.get("targetId");
            
            if (observationProtocol == null) {
                result.setRequestStatus(503);
                result.setMessage("SDK not available");
            } else {
                CompletableFuture<ObservationSnapshot> future = observationProtocol.getSnapshot(targetId);
                ObservationSnapshot snapshot = future.get();
                result.setData(snapshot);
                result.setRequestStatus(200);
            }
        } catch (Exception e) {
            log.error("Get snapshot failed", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to get snapshot: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/alert-rules")
    @ResponseBody
    public ResultModel<List<AlertRuleConfig>> getAlertRules(@RequestBody Map<String, Object> params) {
        log.info("Get alert rules requested: {}", params);
        ResultModel<List<AlertRuleConfig>> result = new ResultModel<>();

        try {
            String targetId = (String) params.get("targetId");
            
            if (observationProtocol == null) {
                result.setData(new ArrayList<>());
                result.setRequestStatus(200);
            } else {
                CompletableFuture<List<AlertRuleConfig>> future = observationProtocol.getAlertRules(targetId);
                List<AlertRuleConfig> rules = future.get();
                result.setData(rules);
                result.setRequestStatus(200);
            }
        } catch (Exception e) {
            log.error("Get alert rules failed", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to get alert rules: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/alert-rule/set")
    @ResponseBody
    public ResultModel<Void> setAlertRule(@RequestBody Map<String, Object> params) {
        log.info("Set alert rule requested: {}", params);
        ResultModel<Void> result = new ResultModel<>();

        try {
            String targetId = (String) params.get("targetId");
            
            if (observationProtocol == null) {
                result.setRequestStatus(503);
                result.setMessage("SDK not available");
            } else {
                AlertRuleConfig rule = new AlertRuleConfig();
                rule.setRuleId((String) params.get("ruleId"));
                rule.setTargetId(targetId);
                rule.setMetricType((String) params.get("metricType"));
                rule.setThreshold(((Number) params.get("threshold")).doubleValue());
                rule.setCondition((String) params.get("condition"));
                rule.setMessage((String) params.get("message"));
                
                observationProtocol.setAlertRule(targetId, rule);
                result.setRequestStatus(200);
                result.setMessage("Alert rule set");
            }
        } catch (Exception e) {
            log.error("Set alert rule failed", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to set alert rule: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/alert-rule/remove")
    @ResponseBody
    public ResultModel<Void> removeAlertRule(@RequestBody Map<String, Object> params) {
        log.info("Remove alert rule requested: {}", params);
        ResultModel<Void> result = new ResultModel<>();

        try {
            String ruleId = (String) params.get("ruleId");
            
            if (observationProtocol != null) {
                observationProtocol.removeAlertRule(ruleId);
            }
            result.setRequestStatus(200);
            result.setMessage("Alert rule removed");
        } catch (Exception e) {
            log.error("Remove alert rule failed", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to remove alert rule: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/alerts")
    @ResponseBody
    public ResultModel<List<AlertInfo>> getActiveAlerts(@RequestBody Map<String, Object> params) {
        log.info("Get active alerts requested: {}", params);
        ResultModel<List<AlertInfo>> result = new ResultModel<>();

        try {
            String targetId = (String) params.get("targetId");
            
            if (observationProtocol == null) {
                result.setData(new ArrayList<>());
                result.setRequestStatus(200);
            } else {
                CompletableFuture<List<AlertInfo>> future = observationProtocol.getActiveAlerts(targetId);
                List<AlertInfo> alerts = future.get();
                result.setData(alerts);
                result.setRequestStatus(200);
            }
        } catch (Exception e) {
            log.error("Get active alerts failed", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to get active alerts: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/alert/acknowledge")
    @ResponseBody
    public ResultModel<Void> acknowledgeAlert(@RequestBody Map<String, Object> params) {
        log.info("Acknowledge alert requested: {}", params);
        ResultModel<Void> result = new ResultModel<>();

        try {
            String alertId = (String) params.get("alertId");
            
            if (observationProtocol != null) {
                observationProtocol.acknowledgeAlert(alertId);
            }
            result.setRequestStatus(200);
            result.setMessage("Alert acknowledged");
        } catch (Exception e) {
            log.error("Acknowledge alert failed", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to acknowledge alert: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/report")
    @ResponseBody
    public ResultModel<ObservationReport> generateReport(@RequestBody Map<String, Object> params) {
        log.info("Generate report requested: {}", params);
        ResultModel<ObservationReport> result = new ResultModel<>();

        try {
            String targetId = (String) params.get("targetId");
            
            if (observationProtocol == null) {
                result.setRequestStatus(503);
                result.setMessage("SDK not available");
            } else {
                ReportConfig config = new ReportConfig();
                config.setReportType((String) params.getOrDefault("reportType", "SUMMARY"));
                config.setStartTime(((Number) params.getOrDefault("startTime", 0)).longValue());
                config.setEndTime(((Number) params.getOrDefault("endTime", System.currentTimeMillis())).longValue());
                
                CompletableFuture<ObservationReport> future = observationProtocol.generateReport(targetId, config);
                ObservationReport report = future.get();
                result.setData(report);
                result.setRequestStatus(200);
            }
        } catch (Exception e) {
            log.error("Generate report failed", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to generate report: " + e.getMessage());
        }

        return result;
    }

    @GetMapping("/status")
    @ResponseBody
    public ResultModel<Map<String, Object>> getStatus() {
        ResultModel<Map<String, Object>> result = new ResultModel<>();
        
        Map<String, Object> status = new HashMap<>();
        status.put("sdkAvailable", observationProtocol != null);
        status.put("protocolType", "ObservationProtocol");
        
        result.setData(status);
        result.setRequestStatus(200);
        return result;
    }
}
