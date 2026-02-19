
package net.ooder.sdk.api.scene;

import java.util.List;

public class SceneGroupKey {
    
    private String sceneGroupId;
    private String keyValue;
    private List<KeyShare> shares;
    private int threshold;
    private int totalShares;
    private long createTime;
    private long expireTime;
    
    public String getSceneGroupId() {
        return sceneGroupId;
    }
    
    public void setSceneGroupId(String sceneGroupId) {
        this.sceneGroupId = sceneGroupId;
    }
    
    public String getKeyValue() {
        return keyValue;
    }
    
    public void setKeyValue(String keyValue) {
        this.keyValue = keyValue;
    }
    
    public List<KeyShare> getShares() {
        return shares;
    }
    
    public void setShares(List<KeyShare> shares) {
        this.shares = shares;
    }
    
    public int getThreshold() {
        return threshold;
    }
    
    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }
    
    public int getTotalShares() {
        return totalShares;
    }
    
    public void setTotalShares(int totalShares) {
        this.totalShares = totalShares;
    }
    
    public long getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
    
    public long getExpireTime() {
        return expireTime;
    }
    
    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }
    
    public boolean isExpired() {
        return expireTime > 0 && System.currentTimeMillis() > expireTime;
    }
    
    public boolean isValid() {
        return keyValue != null && !keyValue.isEmpty() && !isExpired();
    }
    
    public KeyShare getShareForAgent(String agentId) {
        if (shares == null) return null;
        return shares.stream()
            .filter(s -> agentId.equals(s.getAgentId()))
            .findFirst()
            .orElse(null);
    }
    
    public static class KeyShare {
        private String agentId;
        private int shareIndex;
        private String shareData;
        
        public KeyShare() {}
        
        public KeyShare(String agentId, int shareIndex, String shareData) {
            this.agentId = agentId;
            this.shareIndex = shareIndex;
            this.shareData = shareData;
        }
        
        public String getAgentId() { return agentId; }
        public void setAgentId(String agentId) { this.agentId = agentId; }
        public int getShareIndex() { return shareIndex; }
        public void setShareIndex(int shareIndex) { this.shareIndex = shareIndex; }
        public String getShareData() { return shareData; }
        public void setShareData(String shareData) { this.shareData = shareData; }
    }
}
