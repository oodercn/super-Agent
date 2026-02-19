
package net.ooder.sdk.service.monitoring.metrics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MetricsReporter {
    
    private static final Logger log = LoggerFactory.getLogger(MetricsReporter.class);
    
    private final MetricsCollector collector;
    private final ScheduledExecutorService scheduler;
    private final List<MetricsExportListener> listeners;
    
    private String endpoint;
    private long reportInterval = 60000;
    private volatile boolean running;
    private boolean compressData = true;
    
    public MetricsReporter(MetricsCollector collector) {
        this.collector = collector;
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        this.listeners = new ArrayList<>();
    }
    
    public void start() {
        if (endpoint == null || endpoint.isEmpty()) {
            log.warn("No endpoint configured, metrics will not be reported");
            return;
        }
        
        running = true;
        scheduler.scheduleAtFixedRate(this::report, 0, reportInterval, TimeUnit.MILLISECONDS);
        log.info("Metrics reporter started, endpoint: {}", endpoint);
    }
    
    public void stop() {
        running = false;
        scheduler.shutdown();
        log.info("Metrics reporter stopped");
    }
    
    public void report() {
        if (!running) return;
        
        try {
            Map<String, Long> counters = collector.getCounters();
            Map<String, Double> gauges = collector.getGauges();
            Map<String, MetricsCollector.HistogramSnapshot> histograms = collector.getHistograms();
            
            String payload = buildPayload(counters, gauges, histograms);
            
            if (endpoint != null && !endpoint.isEmpty()) {
                sendToEndpoint(payload);
            }
            
            notifyListeners(counters, gauges, histograms);
            
            log.debug("Reported {} counters, {} gauges, {} histograms", 
                counters.size(), gauges.size(), histograms.size());
        } catch (Exception e) {
            log.error("Failed to report metrics", e);
        }
    }
    
    private String buildPayload(Map<String, Long> counters, 
                                  Map<String, Double> gauges,
                                  Map<String, MetricsCollector.HistogramSnapshot> histograms) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"timestamp\":").append(System.currentTimeMillis());
        sb.append(",\"counters\":{");
        
        boolean first = true;
        for (Map.Entry<String, Long> entry : counters.entrySet()) {
            if (!first) sb.append(",");
            first = false;
            sb.append("\"").append(entry.getKey()).append("\":").append(entry.getValue());
        }
        
        sb.append("},\"gauges\":{");
        first = true;
        for (Map.Entry<String, Double> entry : gauges.entrySet()) {
            if (!first) sb.append(",");
            first = false;
            sb.append("\"").append(entry.getKey()).append("\":").append(entry.getValue());
        }
        
        sb.append("},\"histograms\":{");
        first = true;
        for (Map.Entry<String, MetricsCollector.HistogramSnapshot> entry : histograms.entrySet()) {
            if (!first) sb.append(",");
            first = false;
            MetricsCollector.HistogramSnapshot h = entry.getValue();
            sb.append("\"").append(entry.getKey()).append("\":{");
            sb.append("\"count\":").append(h.getCount());
            sb.append(",\"mean\":").append(h.getMean());
            sb.append(",\"min\":").append(h.getMin());
            sb.append(",\"max\":").append(h.getMax());
            sb.append("}");
        }
        
        sb.append("}}");
        return sb.toString();
    }
    
    private void sendToEndpoint(String payload) throws IOException {
        URL url = new URL(endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);
        
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = payload.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        
        int responseCode = conn.getResponseCode();
        if (responseCode >= 400) {
            log.warn("Metrics report failed with status: {}", responseCode);
        }
        
        conn.disconnect();
    }
    
    public void addListener(MetricsExportListener listener) {
        listeners.add(listener);
    }
    
    private void notifyListeners(Map<String, Long> counters, 
                                  Map<String, Double> gauges,
                                  Map<String, MetricsCollector.HistogramSnapshot> histograms) {
        for (MetricsExportListener listener : listeners) {
            try {
                listener.onMetricsExport(counters, gauges, histograms);
            } catch (Exception e) {
                log.warn("Metrics listener error", e);
            }
        }
    }
    
    public void setEndpoint(String endpoint) { this.endpoint = endpoint; }
    public void setReportInterval(long interval) { this.reportInterval = interval; }
    public void setCompressData(boolean compress) { this.compressData = compress; }
    
    public interface MetricsExportListener {
        void onMetricsExport(Map<String, Long> counters, 
                            Map<String, Double> gauges,
                            Map<String, MetricsCollector.HistogramSnapshot> histograms);
    }
}
