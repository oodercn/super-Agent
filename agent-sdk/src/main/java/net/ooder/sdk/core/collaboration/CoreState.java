package net.ooder.sdk.core.collaboration;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface CoreState {
    
    String getStateId();
    
    StateType getType();
    
    Map<String, Object> getData();
    
    CompletableFuture<Void> update(Map<String, Object> data);
    
    CompletableFuture<Void> sync();
    
    CompletableFuture<Void> reset();
    
    long getVersion();
    
    long getLastModified();
    
    void setStateListener(StateListener listener);
}
