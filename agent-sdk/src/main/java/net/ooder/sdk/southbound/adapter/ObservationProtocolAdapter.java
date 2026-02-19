
package net.ooder.sdk.southbound.adapter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import net.ooder.sdk.southbound.adapter.model.ObservationConfig;
import net.ooder.sdk.southbound.adapter.model.ObservationData;
import net.ooder.sdk.southbound.adapter.model.ObservationMetric;
import net.ooder.sdk.southbound.adapter.model.ObservationListener;

public interface ObservationProtocolAdapter {
    
    void startObservation(String targetId, ObservationConfig config);
    
    void stopObservation(String targetId);
    
    boolean isObserving(String targetId);
    
    ObservationData getObservationData(String targetId);
    
    CompletableFuture<ObservationData> collectData(String targetId);
    
    void addObservationListener(ObservationListener listener);
    
    void removeObservationListener(ObservationListener listener);
    
    List<ObservationMetric> getAvailableMetrics(String targetId);
    
    Map<String, Object> getObservationSummary(String targetId);
}
