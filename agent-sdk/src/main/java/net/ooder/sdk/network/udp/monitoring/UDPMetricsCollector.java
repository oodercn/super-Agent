package net.ooder.sdk.network.udp.monitoring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class UDPMetricsCollector {
    private static final Logger log = LoggerFactory.getLogger(UDPMetricsCollector.class);
    
    private final ConcurrentMap<String, MetricEntry> metrics;
    
    private final AtomicLong totalPacketsSent = new AtomicLong(0);
    private final AtomicLong totalPacketsReceived = new AtomicLong(0);
    private final AtomicLong totalBytesSent = new AtomicLong(0);
    private final AtomicLong totalBytesReceived = new AtomicLong(0);
    private final AtomicLong totalErrors = new AtomicLong(0);
    private final AtomicLong totalRetries = new AtomicLong(0);
    private final AtomicLong totalTimeouts = new AtomicLong(0);
    
    private final ConcurrentMap<Integer, PortMetrics> portMetricsMap;
    
    private final LatencyEstimator latencyEstimator;
    private final ThroughputCalculator throughputCalculator;
    private final ErrorRateCalculator errorRateCalculator;
    
    @Autowired
    public UDPMetricsCollector() {
        this.metrics = new ConcurrentHashMap<>();
        this.portMetricsMap = new ConcurrentHashMap<>();
        this.latencyEstimator = new EWMA(0.1);
        this.throughputCalculator = new ThroughputCalculator();
        this.errorRateCalculator = new ErrorRateCalculator();
    }
    
    public void recordPacketSent(int bytes, String operation) {
        totalPacketsSent.incrementAndGet();
        totalBytesSent.addAndGet(bytes);
        latencyEstimator.recordSend();
        throughputCalculator.recordPacketSent(bytes);
        
        MetricEntry entry = metrics.computeIfAbsent(operation, k -> new MetricEntry(k));
        entry.recordPacketSent(bytes);
        
        log.debug("记录数据包发送: 操作={}, 字节数={}", operation, bytes);
    }
    
    public void recordPacketReceived(int bytes, String operation) {
        totalPacketsReceived.incrementAndGet();
        totalBytesReceived.addAndGet(bytes);
        latencyEstimator.recordReceive();
        throughputCalculator.recordPacketReceived(bytes);
        
        MetricEntry entry = metrics.computeIfAbsent(operation, k -> new MetricEntry(k));
        entry.recordPacketReceived(bytes);
        
        log.debug("记录数据包接收: 操作={}, 字节数={}", operation, bytes);
    }
    
    public void recordError(String errorType, String operation, String details) {
        totalErrors.incrementAndGet();
        errorRateCalculator.recordError(errorType);
        
        MetricEntry entry = metrics.computeIfAbsent(operation, k -> new MetricEntry(k));
        entry.recordError(errorType, details);
        
        log.warn("记录错误: 类型={}, 操作={}, 详情={}", errorType, operation, details);
    }
    
    public void recordRetry(String operation, int retryCount) {
        totalRetries.incrementAndGet();
        
        MetricEntry entry = metrics.computeIfAbsent(operation, k -> new MetricEntry(k));
        entry.recordRetry(retryCount);
        
        log.debug("记录重试: 操作={}, 重试次数={}", operation, retryCount);
    }
    
    public void recordTimeout(String operation, long timeoutMs) {
        totalTimeouts.incrementAndGet();
        
        MetricEntry entry = metrics.computeIfAbsent(operation, k -> new MetricEntry(k));
        entry.recordTimeout(timeoutMs);
        
        log.warn("记录超时: 操作={}, 超时={}ms", operation, timeoutMs);
    }
    
    public void recordLatency(String operation, long latencyMs) {
        latencyEstimator.update(latencyMs);
        
        MetricEntry entry = metrics.computeIfAbsent(operation, k -> new MetricEntry(k));
        entry.recordLatency(latencyMs);
        
        log.debug("记录延迟: 操作={}, 延迟={}ms", operation, latencyMs);
    }
    
    public void recordPortAllocation(int port, String serviceType) {
        PortMetrics metrics = portMetricsMap.computeIfAbsent(port, k -> new PortMetrics(k));
        metrics.recordAllocation();
        
        log.debug("记录端口分配: 端口={}, 服务={}", port, serviceType);
    }
    
    public void recordPortRelease(int port) {
        PortMetrics metrics = portMetricsMap.get(port);
        if (metrics != null) {
            metrics.recordRelease();
            log.debug("记录端口释放: 端口={}", port);
        }
    }
    
    public UDPMetricsSnapshot getMetricsSnapshot() {
        UDPMetricsSnapshot snapshot = new UDPMetricsSnapshot();
        
        snapshot.setTotalPacketsSent(totalPacketsSent.get());
        snapshot.setTotalPacketsReceived(totalPacketsReceived.get());
        snapshot.setTotalBytesSent(totalBytesSent.get());
        snapshot.setTotalBytesReceived(totalBytesReceived.get());
        snapshot.setTotalErrors(totalErrors.get());
        snapshot.setTotalRetries(totalRetries.get());
        snapshot.setTotalTimeouts(totalTimeouts.get());
        
        snapshot.setAverageLatency(latencyEstimator.getCurrentValue());
        snapshot.setAverageThroughput(throughputCalculator.getCurrentThroughput());
        snapshot.setErrorRate(errorRateCalculator.getCurrentErrorRate());
        
        snapshot.setPortMetrics(new ConcurrentHashMap<>(portMetricsMap));
        snapshot.setOperationMetrics(new ConcurrentHashMap<>(metrics));
        
        snapshot.setTimestamp(System.currentTimeMillis());
        
        return snapshot;
    }
    
    public void reset() {
        totalPacketsSent.set(0);
        totalPacketsReceived.set(0);
        totalBytesSent.set(0);
        totalBytesReceived.set(0);
        totalErrors.set(0);
        totalRetries.set(0);
        totalTimeouts.set(0);
        
        latencyEstimator.reset();
        throughputCalculator.reset();
        errorRateCalculator.reset();
        
        metrics.clear();
        portMetricsMap.clear();
        
        log.info("UDP指标收集器已重置");
    }
    
    @Scheduled(fixedRate = 60000)
    public void cleanupOldMetrics() {
        long currentTime = System.currentTimeMillis();
        long cutoffTime = currentTime - 3600000; // 1小时前
        
        metrics.entrySet().removeIf(entry -> {
            MetricEntry metric = entry.getValue();
            return metric.getLastUpdateTime() < cutoffTime;
        });
        
        log.debug("清理旧指标数据");
    }
    
    public interface LatencyEstimator {
        void recordSend();
        void recordReceive();
        void update(double value);
        double getCurrentValue();
        void reset();
    }
    
    public static class EWMA implements LatencyEstimator {
        private final double alpha;
        private double average;
        private long lastUpdateTime;
        
        public EWMA(double alpha) {
            this.alpha = alpha;
            this.average = 0.0;
            this.lastUpdateTime = System.currentTimeMillis();
        }
        
        @Override
        public void recordSend() {
            lastUpdateTime = System.currentTimeMillis();
        }
        
        @Override
        public void recordReceive() {
            long now = System.currentTimeMillis();
            double latency = now - lastUpdateTime;
            update(latency);
        }
        
        @Override
        public void update(double value) {
            average = alpha * value + (1 - alpha) * average;
        }
        
        @Override
        public double getCurrentValue() {
            return average;
        }
        
        @Override
        public void reset() {
            average = 0.0;
            lastUpdateTime = System.currentTimeMillis();
        }
    }
    
    public static class ThroughputCalculator {
        private final CircularBuffer<Long> timestamps;
        private final CircularBuffer<Long> bytesList;
        private static final long WINDOW_SIZE = 10000; // 10秒窗口
        
        public ThroughputCalculator() {
            this.timestamps = new CircularBuffer<>(1000);
            this.bytesList = new CircularBuffer<>(1000);
        }
        
        public void recordPacketSent(int bytes) {
            long now = System.currentTimeMillis();
            timestamps.add(now);
            bytesList.add((long) bytes);
        }
        
        public void recordPacketReceived(int bytes) {
            long now = System.currentTimeMillis();
            timestamps.add(now);
            bytesList.add((long) bytes);
        }
        
        public double getCurrentThroughput() {
            if (timestamps.size() < 2) {
                return 0.0;
            }
            
            long timeDiff = timestamps.get(timestamps.size() - 1) - timestamps.get(0);
            if (timeDiff <= 0) {
                return 0.0;
            }
            
            long bytesDiff = bytesList.get(bytesList.size() - 1) - bytesList.get(0);
            return (bytesDiff * 1000.0) / timeDiff; // bytes per second
        }
        
        public void reset() {
            timestamps.clear();
            bytesList.clear();
        }
    }
    
    public static class ErrorRateCalculator {
        private final CircularBuffer<Long> errorTimestamps;
        private static final long ERROR_WINDOW_SIZE = 60000; // 1分钟窗口
        
        public ErrorRateCalculator() {
            this.errorTimestamps = new CircularBuffer<>(1000);
        }
        
        public void recordError(String errorType) {
            errorTimestamps.add(System.currentTimeMillis());
        }
        
        public double getCurrentErrorRate() {
            if (errorTimestamps.isEmpty()) {
                return 0.0;
            }
            
            long timeWindow = System.currentTimeMillis() - errorTimestamps.get(0);
            return errorTimestamps.size() * 1000.0 / timeWindow; // errors per second
        }
        
        public void reset() {
            errorTimestamps.clear();
        }
    }
    
    public static class CircularBuffer<T> {
        private final T[] buffer;
        private int head = 0;
        private int tail = 0;
        private int size = 0;
        
        @SuppressWarnings("unchecked")
        public CircularBuffer(int capacity) {
            this.buffer = (T[]) new Object[capacity];
        }
        
        public void add(T item) {
            buffer[head] = item;
            head = (head + 1) % buffer.length;
            if (size < buffer.length) {
                size++;
            } else {
                tail = (tail + 1) % buffer.length;
            }
        }
        
        public T get(int index) {
            if (index < 0 || index >= size) {
                throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
            }
            return buffer[(tail + index) % buffer.length];
        }
        
        public int size() {
            return size;
        }
        
        public boolean isEmpty() {
            return size == 0;
        }
        
        public void clear() {
            head = 0;
            tail = 0;
            size = 0;
        }
    }
}
