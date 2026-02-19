package net.ooder.sdk.core.collaboration;

import java.util.Map;

public interface StateListener {
    
    void onStateChanged(String stateId, Map<String, Object> oldData, Map<String, Object> newData);
    
    void onSynced(String stateId);
    
    void onError(String stateId, Throwable error);
}
