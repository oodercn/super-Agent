package net.ooder.mvp.skill.scene.dto.scene;

public class FallbackConfigDTO {
    private String strategy;
    private int retryCount;
    private long retryInterval;

    public String getStrategy() { return strategy; }
    public void setStrategy(String strategy) { this.strategy = strategy; }
    public int getRetryCount() { return retryCount; }
    public void setRetryCount(int retryCount) { this.retryCount = retryCount; }
    public long getRetryInterval() { return retryInterval; }
    public void setRetryInterval(long retryInterval) { this.retryInterval = retryInterval; }
}
