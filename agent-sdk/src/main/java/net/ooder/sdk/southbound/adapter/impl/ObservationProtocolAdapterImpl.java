
package net.ooder.sdk.southbound.adapter.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import net.ooder.sdk.southbound.adapter.ObservationProtocolAdapter;
import net.ooder.sdk.southbound.adapter.model.ObservationConfig;
import net.ooder.sdk.southbound.adapter.model.ObservationData;
import net.ooder.sdk.southbound.adapter.model.ObservationListener;
import net.ooder.sdk.southbound.adapter.model.ObservationMetric;

public class ObservationProtocolAdapterImpl implements ObservationProtocolAdapter {
    
    private final Map<String, ObservationConfig> activeObservations = new ConcurrentHashMap<>();
    private final Map<String, ObservationData> observationData = new ConcurrentHashMap<>();
    private final List<ObservationListener> listeners = new CopyOnWriteArrayList<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
    
    public ObservationProtocolAdapterImpl() {
    }
    
    @Override
    public void startObservation(String targetId, ObservationConfig config) {
        activeObservations.put(targetId, config);
        notifyObservationStarted(targetId);
        
        if (config.isContinuous() && config.getInterval() > 0) {
            scheduler.scheduleAtFixedRate(() -> {
                if (activeObservations.containsKey(targetId)) {
                    collectAndNotify(targetId);
                }
            }, 0, config.getInterval(), TimeUnit.MILLISECONDS);
        } else {
            collectAndNotify(targetId);
        }
    }
    
    @Override
    public void stopObservation(String targetId) {
        activeObservations.remove(targetId);
        notifyObservationStopped(targetId);
    }
    
    @Override
    public boolean isObserving(String targetId) {
        return activeObservations.containsKey(targetId);
    }
    
    @Override
    public ObservationData getObservationData(String targetId) {
        return observationData.get(targetId);
    }
    
    @Override
    public CompletableFuture<ObservationData> collectData(String targetId) {
        return CompletableFuture.supplyAsync(() -> {
            ObservationData data = doCollectData(targetId);
            observationData.put(targetId, data);
            return data;
        });
    }
    
    @Override
    public void addObservationListener(ObservationListener listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }
    
    @Override
    public void removeObservationListener(ObservationListener listener) {
        listeners.remove(listener);
    }
    
    @Override
    public List<ObservationMetric> getAvailableMetrics(String targetId) {
        List<ObservationMetric> metrics = new ArrayList<>();
        
        ObservationMetric cpuMetric = new ObservationMetric();
        cpuMetric.setMetricId("cpu_usage");
        cpuMetric.setName("cpu_usage");
        cpuMetric.setDisplayName("CPU Usage");
        cpuMetric.setUnit("%");
        cpuMetric.setDataType("double");
        cpuMetric.setCategory("system");
        cpuMetric.setMinValue(0);
        cpuMetric.setMaxValue(100);
        metrics.add(cpuMetric);
        
        ObservationMetric memoryMetric = new ObservationMetric();
        memoryMetric.setMetricId("memory_usage");
        memoryMetric.setName("memory_usage");
        memoryMetric.setDisplayName("Memory Usage");
        memoryMetric.setUnit("%");
        memoryMetric.setDataType("double");
        memoryMetric.setCategory("system");
        memoryMetric.setMinValue(0);
        memoryMetric.setMaxValue(100);
        metrics.add(memoryMetric);
        
        ObservationMetric latencyMetric = new ObservationMetric();
        latencyMetric.setMetricId("latency");
        latencyMetric.setName("latency");
        latencyMetric.setDisplayName("Latency");
        latencyMetric.setUnit("ms");
        latencyMetric.setDataType("long");
        latencyMetric.setCategory("network");
        latencyMetric.setMinValue(0);
        metrics.add(latencyMetric);
        
        return metrics;
    }
    
    @Override
    public Map<String, Object> getObservationSummary(String targetId) {
        Map<String, Object> summary = new HashMap<>();
        ObservationData data = observationData.get(targetId);
        if (data != null) {
            summary.put("targetId", targetId);
            summary.put("lastUpdate", data.getTimestamp());
            summary.put("status", data.getStatus());
            summary.put("metricCount", data.getMetrics() != null ? data.getMetrics().size() : 0);
        }
        return summary;
    }
    
    private void collectAndNotify(String targetId) {
        try {
            ObservationData data = doCollectData(targetId);
            observationData.put(targetId, data);
            notifyDataCollected(targetId, data);
        } catch (Exception e) {
            notifyObservationError(targetId, "COLLECT_ERROR", e.getMessage());
        }
    }
    
    protected ObservationData doCollectData(String targetId) {
        ObservationData data = new ObservationData();
        data.setTargetId(targetId);
        data.setObservationId("obs_" + System.currentTimeMillis());
        data.setTimestamp(System.currentTimeMillis());
        data.setStatus("success");
        
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("cpu_usage", Math.random() * 100);
        metrics.put("memory_usage", Math.random() * 100);
        metrics.put("latency", (long)(Math.random() * 100));
        metrics.put("throughput", (long)(Math.random() * 10000));
        metrics.put("error_rate", Math.random() * 5);
        data.setMetrics(metrics);
        
        return data;
    }
    
    private void notifyDataCollected(String targetId, ObservationData data) {
        for (ObservationListener listener : listeners) {
            try {
                listener.onDataCollected(targetId, data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    private void notifyObservationStarted(String targetId) {
        for (ObservationListener listener : listeners) {
            try {
                listener.onObservationStarted(targetId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    private void notifyObservationStopped(String targetId) {
        for (ObservationListener listener : listeners) {
            try {
                listener.onObservationStopped(targetId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    private void notifyObservationError(String targetId, String errorCode, String errorMessage) {
        for (ObservationListener listener : listeners) {
            try {
                listener.onObservationError(targetId, errorCode, errorMessage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public void shutdown() {
        scheduler.shutdown();
        activeObservations.clear();
    }
}
