package net.ooder.skillcenter.sdk.protocol;

import net.ooder.nexus.skillcenter.dto.protocol.ObservationDTO;
import net.ooder.nexus.skillcenter.dto.protocol.ObservationDTO.AlertInfoDTO;
import net.ooder.nexus.skillcenter.dto.protocol.ObservationDTO.AlertRuleConfigDTO;
import net.ooder.nexus.skillcenter.dto.protocol.ObservationDTO.LogQueryDTO;
import net.ooder.nexus.skillcenter.dto.protocol.ObservationDTO.MetricQueryDTO;
import net.ooder.nexus.skillcenter.dto.protocol.ObservationDTO.ObservationConfigDTO;
import net.ooder.nexus.skillcenter.dto.protocol.ObservationDTO.ObservationLogDTO;
import net.ooder.nexus.skillcenter.dto.protocol.ObservationDTO.ObservationMetricDTO;
import net.ooder.nexus.skillcenter.dto.protocol.ObservationDTO.ObservationReportDTO;
import net.ooder.nexus.skillcenter.dto.protocol.ObservationDTO.ObservationSnapshotDTO;
import net.ooder.nexus.skillcenter.dto.protocol.ObservationDTO.ObservationStatusDTO;
import net.ooder.nexus.skillcenter.dto.protocol.ObservationDTO.ObservationTraceDTO;
import net.ooder.nexus.skillcenter.dto.protocol.ObservationDTO.ReportConfigDTO;
import net.ooder.nexus.skillcenter.dto.protocol.ObservationDTO.TraceQueryDTO;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ObservationProtocolAdapter {

    CompletableFuture<Void> startObservation(String targetId, ObservationConfigDTO config);

    CompletableFuture<Void> stopObservation(String targetId);

    CompletableFuture<ObservationStatusDTO> getObservationStatus(String targetId);

    CompletableFuture<List<ObservationMetricDTO>> getMetrics(String targetId, MetricQueryDTO query);

    CompletableFuture<List<ObservationLogDTO>> getLogs(String targetId, LogQueryDTO query);

    CompletableFuture<List<ObservationTraceDTO>> getTraces(String targetId, TraceQueryDTO query);

    CompletableFuture<ObservationSnapshotDTO> getSnapshot(String targetId);

    CompletableFuture<Void> setAlertRule(String targetId, AlertRuleConfigDTO rule);

    CompletableFuture<Void> removeAlertRule(String ruleId);

    CompletableFuture<List<AlertRuleConfigDTO>> getAlertRules(String targetId);

    CompletableFuture<List<AlertInfoDTO>> getActiveAlerts(String targetId);

    CompletableFuture<Void> acknowledgeAlert(String alertId);

    CompletableFuture<ObservationReportDTO> generateReport(String targetId, ReportConfigDTO config);

    void addObservationListener(ObservationEventListener listener);

    void removeObservationListener(ObservationEventListener listener);

    boolean isAvailable();

    interface ObservationEventListener {
        void onMetricCollected(String targetId, ObservationMetricDTO metric);
        void onLogCollected(String targetId, ObservationLogDTO log);
        void onTraceCollected(String targetId, ObservationTraceDTO trace);
        void onAlertTriggered(String targetId, AlertInfoDTO alert);
        void onAlertAcknowledged(String alertId);
        void onHealthChanged(String targetId, String oldStatus, String newStatus);
    }
}
