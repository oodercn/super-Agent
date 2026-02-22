package net.ooder.nexus.infrastructure.openwrt.bridge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicInteger;

public class OpenWrtBridgeMonitor {

    private static final Logger log = LoggerFactory.getLogger(OpenWrtBridgeMonitor.class);
    private static final OpenWrtBridgeMonitor instance = new OpenWrtBridgeMonitor();

    private final Map<String, OperationStats> operationStatsMap = new ConcurrentHashMap<>();

    private final AtomicInteger activeConnections = new AtomicInteger(0);
    private final AtomicLong totalConnections = new AtomicLong(0);
    private final AtomicLong connectionFailures = new AtomicLong(0);

    private final AtomicLong cacheHits = new AtomicLong(0);
    private final AtomicLong cacheMisses = new AtomicLong(0);
    private final AtomicLong cacheUpdates = new AtomicLong(0);

    private OpenWrtBridgeMonitor() {
    }

    public static OpenWrtBridgeMonitor getInstance() {
        return instance;
    }

    public long startOperation(String operationName) {
        operationStatsMap.computeIfAbsent(operationName, k -> new OperationStats());
        return System.currentTimeMillis();
    }

    public void endOperation(String operationName, long startTime, boolean success) {
        OperationStats stats = operationStatsMap.get(operationName);
        if (stats != null) {
            long duration = System.currentTimeMillis() - startTime;
            stats.recordOperation(duration, success);

            if (success) {
                log.debug("Operation {} completed successfully in {}ms", operationName, duration);
            } else {
                log.warn("Operation {} failed in {}ms", operationName, duration);
            }
        }
    }

    public void recordConnectionSuccess() {
        activeConnections.incrementAndGet();
        totalConnections.incrementAndGet();
        log.info("Connection established. Active connections: {}, Total connections: {}",
                activeConnections.get(), totalConnections.get());
    }

    public void recordConnectionFailure() {
        connectionFailures.incrementAndGet();
        log.warn("Connection failed. Total failures: {}", connectionFailures.get());
    }

    public void recordConnectionClose() {
        int current = activeConnections.decrementAndGet();
        if (current < 0) {
            activeConnections.set(0);
            current = 0;
        }
        log.info("Connection closed. Active connections: {}", current);
    }

    public void recordCacheHit() {
        cacheHits.incrementAndGet();
    }

    public void recordCacheMiss() {
        cacheMisses.incrementAndGet();
    }

    public void recordCacheUpdate() {
        cacheUpdates.incrementAndGet();
    }

    public OperationStats getOperationStats(String operationName) {
        return operationStatsMap.get(operationName);
    }

    public Map<String, OperationStats> getAllOperationStats() {
        return new ConcurrentHashMap<>(operationStatsMap);
    }

    public Map<String, Number> getConnectionStats() {
        Map<String, Number> stats = new ConcurrentHashMap<>();
        stats.put("activeConnections", activeConnections.get());
        stats.put("totalConnections", totalConnections.get());
        stats.put("connectionFailures", connectionFailures.get());
        if (totalConnections.get() > 0) {
            double successRate = (double) (totalConnections.get() - connectionFailures.get()) / totalConnections.get() * 100;
            stats.put("connectionSuccessRate", successRate);
        } else {
            stats.put("connectionSuccessRate", 0.0);
        }
        return stats;
    }

    public Map<String, Number> getCacheStats() {
        Map<String, Number> stats = new ConcurrentHashMap<>();
        stats.put("cacheHits", cacheHits.get());
        stats.put("cacheMisses", cacheMisses.get());
        stats.put("cacheUpdates", cacheUpdates.get());
        long totalAccesses = cacheHits.get() + cacheMisses.get();
        if (totalAccesses > 0) {
            double hitRate = (double) cacheHits.get() / totalAccesses * 100;
            stats.put("cacheHitRate", hitRate);
        } else {
            stats.put("cacheHitRate", 0.0);
        }
        return stats;
    }

    public Map<String, Object> getAllStats() {
        Map<String, Object> stats = new ConcurrentHashMap<>();
        stats.put("operations", getAllOperationStats());
        stats.put("connections", getConnectionStats());
        stats.put("cache", getCacheStats());
        stats.put("timestamp", System.currentTimeMillis());
        return stats;
    }

    public void resetStats() {
        operationStatsMap.clear();
        activeConnections.set(0);
        totalConnections.set(0);
        connectionFailures.set(0);
        cacheHits.set(0);
        cacheMisses.set(0);
        cacheUpdates.set(0);
        log.info("Monitoring stats reset");
    }

    public static class OperationStats {
        private final AtomicLong totalOperations = new AtomicLong(0);
        private final AtomicLong successfulOperations = new AtomicLong(0);
        private final AtomicLong failedOperations = new AtomicLong(0);
        private final AtomicLong totalDuration = new AtomicLong(0);
        private final AtomicLong minDuration = new AtomicLong(Long.MAX_VALUE);
        private final AtomicLong maxDuration = new AtomicLong(0);

        public void recordOperation(long duration, boolean success) {
            totalOperations.incrementAndGet();
            totalDuration.addAndGet(duration);

            if (duration < minDuration.get()) {
                minDuration.set(duration);
            }
            if (duration > maxDuration.get()) {
                maxDuration.set(duration);
            }

            if (success) {
                successfulOperations.incrementAndGet();
            } else {
                failedOperations.incrementAndGet();
            }
        }

        public long getTotalOperations() {
            return totalOperations.get();
        }

        public long getSuccessfulOperations() {
            return successfulOperations.get();
        }

        public long getFailedOperations() {
            return failedOperations.get();
        }

        public double getAverageDuration() {
            long total = totalOperations.get();
            return total > 0 ? (double) totalDuration.get() / total : 0;
        }

        public long getMinDuration() {
            long min = minDuration.get();
            return min == Long.MAX_VALUE ? 0 : min;
        }

        public long getMaxDuration() {
            return maxDuration.get();
        }

        public double getSuccessRate() {
            long total = totalOperations.get();
            return total > 0 ? (double) successfulOperations.get() / total * 100 : 0;
        }

        public double getFailureRate() {
            long total = totalOperations.get();
            return total > 0 ? (double) failedOperations.get() / total * 100 : 0;
        }

        public Map<String, Object> getStats() {
            Map<String, Object> stats = new ConcurrentHashMap<>();
            stats.put("totalOperations", getTotalOperations());
            stats.put("successfulOperations", getSuccessfulOperations());
            stats.put("failedOperations", getFailedOperations());
            stats.put("averageDuration", getAverageDuration());
            stats.put("minDuration", getMinDuration());
            stats.put("maxDuration", getMaxDuration());
            stats.put("successRate", getSuccessRate());
            stats.put("failureRate", getFailureRate());
            return stats;
        }
    }
}
