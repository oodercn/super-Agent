package net.ooder.sdk.network.link;

import net.ooder.sdk.network.link.model.LinkEvent;
import net.ooder.sdk.network.link.model.LinkEventType;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public interface EnhancedLinkManager extends LinkTableManager {
    // 链路性能监控相关方法
    void startLinkMonitoring();
    void stopLinkMonitoring();
    boolean isLinkMonitoringRunning();
    
    // 链路性能分析相关方法
    CompletableFuture<Map<String, Object>> analyzeLinkPerformance(String linkId);
    CompletableFuture<Map<String, Object>> analyzeAllLinksPerformance();
    CompletableFuture<List<String>> findPerformanceIssues();
    CompletableFuture<List<String>> findBottlenecks();
    
    // 链路事件相关方法
    void publishLinkEvent(LinkEvent event);
    void subscribeToLinkEvents(Consumer<LinkEvent> eventHandler);
    void unsubscribeFromLinkEvents(Consumer<LinkEvent> eventHandler);
    void subscribeToLinkEventsByType(LinkEventType eventType, Consumer<LinkEvent> eventHandler);
    void unsubscribeFromLinkEventsByType(LinkEventType eventType, Consumer<LinkEvent> eventHandler);
    List<LinkEvent> getRecentLinkEvents(int limit);
    List<LinkEvent> getLinkEventsByType(LinkEventType eventType, int limit);
    
    // 链路统计相关方法
    CompletableFuture<Map<String, Object>> getLinkStatistics(String linkId);
    CompletableFuture<Map<String, Object>> getOverallLinkStatistics();
    CompletableFuture<Map<String, Object>> getLinkHealth(String linkId);
    CompletableFuture<Map<String, Object>> getOverallLinkHealth();
    
    // 链路操作相关方法
    CompletableFuture<Boolean> updateLinkMetrics(String linkId, Map<String, Object> metrics);
    CompletableFuture<Boolean> updateLinkQuality(String linkId, double quality);
    CompletableFuture<Boolean> testLink(String linkId);
    CompletableFuture<Boolean> resetLinkStatistics(String linkId);
    
    // 链路配置相关方法
    CompletableFuture<Boolean> setLinkThresholds(String linkId, Map<String, Object> thresholds);
    CompletableFuture<Map<String, Object>> getLinkThresholds(String linkId);
    
    // 链路拓扑相关方法
    CompletableFuture<Map<String, Object>> getLinkTopology();
    CompletableFuture<List<String>> getShortestPath(String sourceNodeId, String targetNodeId);
    CompletableFuture<List<List<String>>> getMultiplePaths(String sourceNodeId, String targetNodeId, int maxPaths);
}
