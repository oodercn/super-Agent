
package net.ooder.sdk.api.scene;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface CapabilityInvoker {
    
    CompletableFuture<Object> invoke(String sceneId, String capId, Map<String, Object> params);
    
    CompletableFuture<Object> invoke(String capId, Map<String, Object> params);
    
    CompletableFuture<Object> invokeAsync(String sceneId, String capId, Map<String, Object> params);
    
    CompletableFuture<Boolean> isAvailable(String sceneId, String capId);
    
    CompletableFuture<CapabilityMetadata> getMetadata(String sceneId, String capId);
    
    CompletableFuture<Object> invokeWithFallback(String sceneId, String capId, Map<String, Object> params, String fallbackCapId);
    
    class CapabilityMetadata {
        private String capId;
        private String name;
        private String description;
        private String sceneId;
        private String providerAgentId;
        private boolean async;
        private long averageInvokeTime;
        private int invokeCount;
        
        public String getCapId() { return capId; }
        public void setCapId(String capId) { this.capId = capId; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getSceneId() { return sceneId; }
        public void setSceneId(String sceneId) { this.sceneId = sceneId; }
        public String getProviderAgentId() { return providerAgentId; }
        public void setProviderAgentId(String providerAgentId) { this.providerAgentId = providerAgentId; }
        public boolean isAsync() { return async; }
        public void setAsync(boolean async) { this.async = async; }
        public long getAverageInvokeTime() { return averageInvokeTime; }
        public void setAverageInvokeTime(long averageInvokeTime) { this.averageInvokeTime = averageInvokeTime; }
        public int getInvokeCount() { return invokeCount; }
        public void setInvokeCount(int invokeCount) { this.invokeCount = invokeCount; }
    }
}
