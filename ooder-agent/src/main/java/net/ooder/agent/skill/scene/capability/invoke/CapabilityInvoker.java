package net.ooder.mvp.skill.scene.capability.invoke;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface CapabilityInvoker {
    
    Object invoke(String capId, Map<String, Object> params);
    
    Object invokeWithFallback(String capId, Map<String, Object> params);
    
    CompletableFuture<Object> invokeAsync(String capId, Map<String, Object> params);
    
    InvokeResult invokeWithResult(String capId, Map<String, Object> params);
    
    public static class InvokeResult {
        private boolean success;
        private Object result;
        private String error;
        private long duration;
        private String providerId;
        private String connectorType;
        
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public Object getResult() { return result; }
        public void setResult(Object result) { this.result = result; }
        public String getError() { return error; }
        public void setError(String error) { this.error = error; }
        public long getDuration() { return duration; }
        public void setDuration(long duration) { this.duration = duration; }
        public String getProviderId() { return providerId; }
        public void setProviderId(String providerId) { this.providerId = providerId; }
        public String getConnectorType() { return connectorType; }
        public void setConnectorType(String connectorType) { this.connectorType = connectorType; }
        
        public static InvokeResult success(Object result, long duration) {
            InvokeResult r = new InvokeResult();
            r.setSuccess(true);
            r.setResult(result);
            r.setDuration(duration);
            return r;
        }
        
        public static InvokeResult failure(String error, long duration) {
            InvokeResult r = new InvokeResult();
            r.setSuccess(false);
            r.setError(error);
            r.setDuration(duration);
            return r;
        }
    }
}
