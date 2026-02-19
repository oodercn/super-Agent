package net.ooder.sdk.infra.observer;

import java.util.Map;

public interface ConfigChangeListener {
    
    void onConfigWritten(String configType, String configId, Map<String, Object> config);
    
    void onConfigRead(String configType, String configId, Map<String, Object> config);
    
    default void onConfigDeleted(String configType, String configId) {
    }
    
    default void onConfigError(String configType, String configId, Throwable error) {
    }
}
