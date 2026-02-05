package net.ooder.skillcenter.p2p.refresh;

import java.util.List;

public interface SkillsAgentRefreshEngine {
    
    void startRefreshScheduler();
    
    void stopRefreshScheduler();
    
    void scheduleRefresh(String agentId, long intervalMs);
    
    void scheduleFullRefresh(long intervalMs);
    
    void cancelRefresh(String agentId);
    
    RefreshResult refreshAgent(String agentId);
    
    List<RefreshResult> refreshAllAgents();
    
    void setRefreshPolicy(RefreshPolicy policy);
    
    class RefreshResult {
        private String agentId;
        private boolean success;
        private long timestamp;
        private String message;
        private List<String> updatedCapabilities;
        
        public String getAgentId() {
            return agentId;
        }
        
        public void setAgentId(String agentId) {
            this.agentId = agentId;
        }
        
        public boolean isSuccess() {
            return success;
        }
        
        public void setSuccess(boolean success) {
            this.success = success;
        }
        
        public long getTimestamp() {
            return timestamp;
        }
        
        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }
        
        public String getMessage() {
            return message;
        }
        
        public void setMessage(String message) {
            this.message = message;
        }
        
        public List<String> getUpdatedCapabilities() {
            return updatedCapabilities;
        }
        
        public void setUpdatedCapabilities(List<String> updatedCapabilities) {
            this.updatedCapabilities = updatedCapabilities;
        }
    }
    
    class RefreshPolicy {
        private long defaultRefreshInterval;
        private int maxRetryAttempts;
        private long retryDelay;
        private boolean autoRetryOnFailure;
        
        public long getDefaultRefreshInterval() {
            return defaultRefreshInterval;
        }
        
        public void setDefaultRefreshInterval(long defaultRefreshInterval) {
            this.defaultRefreshInterval = defaultRefreshInterval;
        }
        
        public int getMaxRetryAttempts() {
            return maxRetryAttempts;
        }
        
        public void setMaxRetryAttempts(int maxRetryAttempts) {
            this.maxRetryAttempts = maxRetryAttempts;
        }
        
        public long getRetryDelay() {
            return retryDelay;
        }
        
        public void setRetryDelay(long retryDelay) {
            this.retryDelay = retryDelay;
        }
        
        public boolean isAutoRetryOnFailure() {
            return autoRetryOnFailure;
        }
        
        public void setAutoRetryOnFailure(boolean autoRetryOnFailure) {
            this.autoRetryOnFailure = autoRetryOnFailure;
        }
    }
}
