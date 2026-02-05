package net.ooder.sdk.monitoring;

import java.util.Map;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public interface MonitoringManager {
    // 监控启动和停止
    CompletableFuture<Boolean> startMonitoring();
    CompletableFuture<Boolean> stopMonitoring();
    boolean isMonitoringRunning();
    
    // 系统指标采集
    CompletableFuture<Map<String, Object>> collectSystemMetrics();
    CompletableFuture<Map<String, Object>> collectJvmMetrics();
    CompletableFuture<Map<String, Object>> collectProcessMetrics();
    CompletableFuture<Map<String, Object>> collectAllSystemMetrics();
    
    // 网络状态监控
    CompletableFuture<Map<String, Object>> collectNetworkMetrics();
    CompletableFuture<Map<String, Object>> collectLinkMetrics(String linkId);
    CompletableFuture<Map<String, Object>> collectAllLinkMetrics();
    CompletableFuture<Map<String, Object>> collectTerminalMetrics(String terminalId);
    CompletableFuture<Map<String, Object>> collectAllTerminalMetrics();
    
    // 性能分析
    CompletableFuture<Map<String, Object>> analyzeSystemPerformance();
    CompletableFuture<Map<String, Object>> analyzeNetworkPerformance();
    CompletableFuture<Map<String, Object>> analyzeTerminalPerformance(String terminalId);
    CompletableFuture<Map<String, Object>> analyzeAllTerminalPerformance();
    CompletableFuture<List<String>> findPerformanceBottlenecks();
    
    // 告警管理
    CompletableFuture<Boolean> addAlertRule(AlertRule rule);
    CompletableFuture<Boolean> removeAlertRule(String ruleId);
    CompletableFuture<List<AlertRule>> getAlertRules();
    CompletableFuture<AlertRule> getAlertRule(String ruleId);
    CompletableFuture<Boolean> updateAlertRule(String ruleId, AlertRule rule);
    
    // 告警通知
    CompletableFuture<Boolean> sendAlert(Alert alert);
    void subscribeToAlerts(Consumer<Alert> alertHandler);
    void unsubscribeFromAlerts(Consumer<Alert> alertHandler);
    void subscribeToAlertsByLevel(AlertLevel level, Consumer<Alert> alertHandler);
    void unsubscribeFromAlertsByLevel(AlertLevel level, Consumer<Alert> alertHandler);
    
    // 告警历史
    CompletableFuture<List<Alert>> getAlertHistory(int limit);
    CompletableFuture<List<Alert>> getAlertsByLevel(AlertLevel level, int limit);
    CompletableFuture<List<Alert>> getAlertsByType(String alertType, int limit);
    CompletableFuture<Map<String, Long>> getAlertCountsByLevel();
    CompletableFuture<Map<String, Long>> getAlertCountsByType();
    
    // 监控配置
    CompletableFuture<Boolean> setMonitoringInterval(long interval, java.util.concurrent.TimeUnit unit);
    CompletableFuture<Long> getMonitoringInterval();
    CompletableFuture<Boolean> setMetricsRetention(int days);
    CompletableFuture<Integer> getMetricsRetention();
    
    // 监控数据存储和查询
    CompletableFuture<Boolean> storeMetrics(Map<String, Object> metrics);
    CompletableFuture<List<Map<String, Object>>> queryMetrics(String metricName, long startTime, long endTime);
    CompletableFuture<Map<String, Object>> getMetricStatistics(String metricName, long startTime, long endTime);
    
    // 健康检查
    CompletableFuture<HealthStatus> checkSystemHealth();
    CompletableFuture<HealthStatus> checkNetworkHealth();
    CompletableFuture<HealthStatus> checkTerminalHealth(String terminalId);
    CompletableFuture<Map<String, HealthStatus>> checkAllTerminalHealth();
}
