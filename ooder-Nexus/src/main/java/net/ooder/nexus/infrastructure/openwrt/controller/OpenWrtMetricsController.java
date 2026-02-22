package net.ooder.nexus.infrastructure.openwrt.controller;

import net.ooder.nexus.infrastructure.openwrt.bridge.OpenWrtBridgeMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * OpenWrt Bridge 监控指标控制器
 * 暴露监控指标，便于接入 Prometheus/Grafana
 */
@RestController
@RequestMapping("/api/metrics/openwrt")
public class OpenWrtMetricsController {

    private static final Logger log = LoggerFactory.getLogger(OpenWrtMetricsController.class);

    private final OpenWrtBridgeMonitor monitor = OpenWrtBridgeMonitor.getInstance();

    /**
     * 获取所有监控指标（JSON格式）
     * @return 监控指标
     */
    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllMetrics() {
        log.debug("Getting all OpenWrt metrics");
        return ResponseEntity.ok(monitor.getAllStats());
    }

    /**
     * 获取操作监控指标
     * @return 操作监控指标
     */
    @GetMapping("/operations")
    public ResponseEntity<Map<String, OpenWrtBridgeMonitor.OperationStats>> getOperationMetrics() {
        log.debug("Getting operation metrics");
        return ResponseEntity.ok(monitor.getAllOperationStats());
    }

    /**
     * 获取连接监控指标
     * @return 连接监控指标
     */
    @GetMapping("/connections")
    public ResponseEntity<Map<String, Number>> getConnectionMetrics() {
        log.debug("Getting connection metrics");
        return ResponseEntity.ok(monitor.getConnectionStats());
    }

    /**
     * 获取缓存监控指标
     * @return 缓存监控指标
     */
    @GetMapping("/cache")
    public ResponseEntity<Map<String, Number>> getCacheMetrics() {
        log.debug("Getting cache metrics");
        return ResponseEntity.ok(monitor.getCacheStats());
    }

    /**
     * 获取Prometheus格式的监控指标
     * @return Prometheus格式的监控指标
     */
    @GetMapping(value = "/prometheus", produces = "text/plain; charset=UTF-8")
    public ResponseEntity<String> getPrometheusMetrics() {
        log.debug("Getting Prometheus metrics");
        StringBuilder metrics = new StringBuilder();

        // 添加连接指标
        addConnectionMetrics(metrics);

        // 添加缓存指标
        addCacheMetrics(metrics);

        // 添加操作指标
        addOperationMetrics(metrics);

        return ResponseEntity.ok(metrics.toString());
    }

    /**
     * 添加连接指标到Prometheus格式
     * @param metrics 指标字符串构建器
     */
    private void addConnectionMetrics(StringBuilder metrics) {
        Map<String, Number> connectionStats = monitor.getConnectionStats();

        metrics.append("# HELP openwrt_bridge_active_connections Number of active connections\n");
        metrics.append("# TYPE openwrt_bridge_active_connections gauge\n");
        metrics.append("openwrt_bridge_active_connections ").append(connectionStats.getOrDefault("activeConnections", 0)).append("\n\n");

        metrics.append("# HELP openwrt_bridge_total_connections Total number of connections\n");
        metrics.append("# TYPE openwrt_bridge_total_connections counter\n");
        metrics.append("openwrt_bridge_total_connections ").append(connectionStats.getOrDefault("totalConnections", 0)).append("\n\n");

        metrics.append("# HELP openwrt_bridge_connection_failures Number of connection failures\n");
        metrics.append("# TYPE openwrt_bridge_connection_failures counter\n");
        metrics.append("openwrt_bridge_connection_failures ").append(connectionStats.getOrDefault("connectionFailures", 0)).append("\n\n");

        metrics.append("# HELP openwrt_bridge_connection_success_rate Connection success rate\n");
        metrics.append("# TYPE openwrt_bridge_connection_success_rate gauge\n");
        metrics.append("openwrt_bridge_connection_success_rate ").append(connectionStats.getOrDefault("connectionSuccessRate", 0)).append("\n\n");
    }

    /**
     * 添加缓存指标到Prometheus格式
     * @param metrics 指标字符串构建器
     */
    private void addCacheMetrics(StringBuilder metrics) {
        Map<String, Number> cacheStats = monitor.getCacheStats();

        metrics.append("# HELP openwrt_bridge_cache_hits Number of cache hits\n");
        metrics.append("# TYPE openwrt_bridge_cache_hits counter\n");
        metrics.append("openwrt_bridge_cache_hits ").append(cacheStats.getOrDefault("cacheHits", 0)).append("\n\n");

        metrics.append("# HELP openwrt_bridge_cache_misses Number of cache misses\n");
        metrics.append("# TYPE openwrt_bridge_cache_misses counter\n");
        metrics.append("openwrt_bridge_cache_misses ").append(cacheStats.getOrDefault("cacheMisses", 0)).append("\n\n");

        metrics.append("# HELP openwrt_bridge_cache_updates Number of cache updates\n");
        metrics.append("# TYPE openwrt_bridge_cache_updates counter\n");
        metrics.append("openwrt_bridge_cache_updates ").append(cacheStats.getOrDefault("cacheUpdates", 0)).append("\n\n");

        metrics.append("# HELP openwrt_bridge_cache_hit_rate Cache hit rate\n");
        metrics.append("# TYPE openwrt_bridge_cache_hit_rate gauge\n");
        metrics.append("openwrt_bridge_cache_hit_rate ").append(cacheStats.getOrDefault("cacheHitRate", 0)).append("\n\n");
    }

    /**
     * 添加操作指标到Prometheus格式
     * @param metrics 指标字符串构建器
     */
    private void addOperationMetrics(StringBuilder metrics) {
        Map<String, OpenWrtBridgeMonitor.OperationStats> operationStats = monitor.getAllOperationStats();

        for (Map.Entry<String, OpenWrtBridgeMonitor.OperationStats> entry : operationStats.entrySet()) {
            String operation = entry.getKey();
            OpenWrtBridgeMonitor.OperationStats stats = entry.getValue();

            // 替换操作名称中的特殊字符，使其符合Prometheus格式
            String sanitizedOperation = operation.replaceAll("[^a-zA-Z0-9_]", "_");

            metrics.append("# HELP openwrt_bridge_operation_total_").append(sanitizedOperation).append(" Total number of operations\n");
            metrics.append("# TYPE openwrt_bridge_operation_total_").append(sanitizedOperation).append(" counter\n");
            metrics.append("openwrt_bridge_operation_total{operation=\"").append(operation).append("\"} ").append(stats.getTotalOperations()).append("\n");

            metrics.append("# HELP openwrt_bridge_operation_successful_").append(sanitizedOperation).append(" Number of successful operations\n");
            metrics.append("# TYPE openwrt_bridge_operation_successful_").append(sanitizedOperation).append(" counter\n");
            metrics.append("openwrt_bridge_operation_successful{operation=\"").append(operation).append("\"} ").append(stats.getSuccessfulOperations()).append("\n");

            metrics.append("# HELP openwrt_bridge_operation_failed_").append(sanitizedOperation).append(" Number of failed operations\n");
            metrics.append("# TYPE openwrt_bridge_operation_failed_").append(sanitizedOperation).append(" counter\n");
            metrics.append("openwrt_bridge_operation_failed{operation=\"").append(operation).append("\"} ").append(stats.getFailedOperations()).append("\n");

            metrics.append("# HELP openwrt_bridge_operation_duration_").append(sanitizedOperation).append(" Average operation duration in milliseconds\n");
            metrics.append("# TYPE openwrt_bridge_operation_duration_").append(sanitizedOperation).append(" gauge\n");
            metrics.append("openwrt_bridge_operation_duration{operation=\"").append(operation).append("\"} ").append(stats.getAverageDuration()).append("\n");

            metrics.append("# HELP openwrt_bridge_operation_success_rate_").append(sanitizedOperation).append(" Operation success rate\n");
            metrics.append("# TYPE openwrt_bridge_operation_success_rate_").append(sanitizedOperation).append(" gauge\n");
            metrics.append("openwrt_bridge_operation_success_rate{operation=\"").append(operation).append("\"} ").append(stats.getSuccessRate()).append("\n\n");
        }
    }

    /**
     * 重置监控指标
     * @return 重置结果
     */
    @GetMapping("/reset")
    public ResponseEntity<Map<String, Object>> resetMetrics() {
        log.info("Resetting OpenWrt metrics");
        monitor.resetStats();
        Map<String, Object> result = new HashMap<>();
        result.put("status", "success");
        result.put("message", "Monitoring metrics reset");
        result.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(result);
    }
}
