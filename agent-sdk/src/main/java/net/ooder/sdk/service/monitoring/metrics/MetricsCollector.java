
package net.ooder.sdk.service.monitoring.metrics;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MetricsCollector {
    
    private static final Logger log = LoggerFactory.getLogger(MetricsCollector.class);
    
    private final Map<String, Counter> counters;
    private final Map<String, Gauge> gauges;
    private final Map<String, Histogram> histograms;
    
    public MetricsCollector() {
        this.counters = new ConcurrentHashMap<>();
        this.gauges = new ConcurrentHashMap<>();
        this.histograms = new ConcurrentHashMap<>();
    }
    
    public Counter counter(String name) {
        return counters.computeIfAbsent(name, k -> new Counter());
    }
    
    public Gauge gauge(String name) {
        return gauges.computeIfAbsent(name, k -> new Gauge());
    }
    
    public Histogram histogram(String name) {
        return histograms.computeIfAbsent(name, k -> new Histogram());
    }
    
    public void increment(String name) {
        counter(name).increment();
    }
    
    public void increment(String name, long delta) {
        counter(name).add(delta);
    }
    
    public void recordValue(String name, long value) {
        histogram(name).record(value);
    }
    
    public void setGauge(String name, double value) {
        gauge(name).set(value);
    }
    
    public Map<String, Long> getCounters() {
        Map<String, Long> result = new ConcurrentHashMap<>();
        counters.forEach((k, v) -> result.put(k, v.get()));
        return result;
    }
    
    public Map<String, Double> getGauges() {
        Map<String, Double> result = new ConcurrentHashMap<>();
        gauges.forEach((k, v) -> result.put(k, v.get()));
        return result;
    }
    
    public Map<String, HistogramSnapshot> getHistograms() {
        Map<String, HistogramSnapshot> result = new ConcurrentHashMap<>();
        histograms.forEach((k, v) -> result.put(k, v.getSnapshot()));
        return result;
    }
    
    public void reset() {
        counters.clear();
        gauges.clear();
        histograms.clear();
        log.info("Metrics reset");
    }
    
    public static class Counter {
        private final AtomicLong value = new AtomicLong(0);
        
        public void increment() {
            value.incrementAndGet();
        }
        
        public void add(long delta) {
            value.addAndGet(delta);
        }
        
        public long get() {
            return value.get();
        }
    }
    
    public static class Gauge {
        private volatile double value;
        
        public void set(double value) {
            this.value = value;
        }
        
        public double get() {
            return value;
        }
    }
    
    public static class Histogram {
        private final AtomicLong count = new AtomicLong(0);
        private final AtomicLong sum = new AtomicLong(0);
        private volatile long min = Long.MAX_VALUE;
        private volatile long max = Long.MIN_VALUE;
        
        public void record(long value) {
            count.incrementAndGet();
            sum.addAndGet(value);
            
            synchronized (this) {
                if (value < min) min = value;
                if (value > max) max = value;
            }
        }
        
        public HistogramSnapshot getSnapshot() {
            long c = count.get();
            long s = sum.get();
            return new HistogramSnapshot(c, s, c > 0 ? (double) s / c : 0, min, max);
        }
    }
    
    public static class HistogramSnapshot {
        private final long count;
        private final long sum;
        private final double mean;
        private final long min;
        private final long max;
        
        public HistogramSnapshot(long count, long sum, double mean, long min, long max) {
            this.count = count;
            this.sum = sum;
            this.mean = mean;
            this.min = min;
            this.max = max;
        }
        
        public long getCount() { return count; }
        public long getSum() { return sum; }
        public double getMean() { return mean; }
        public long getMin() { return min; }
        public long getMax() { return max; }
    }
}
