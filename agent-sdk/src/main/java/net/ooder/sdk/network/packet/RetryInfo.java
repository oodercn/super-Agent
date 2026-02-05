package net.ooder.sdk.network.packet;

public class RetryInfo {
    private boolean enabled;
    private String strategy;
    private int currentRetryCount;
    private int maxRetries;
    private long nextRetryTime;
    private long lastRetryTime;
    private int totalRetryCount;

    // Getter and Setter methods
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }

    public int getCurrentRetryCount() {
        return currentRetryCount;
    }

    public void setCurrentRetryCount(int currentRetryCount) {
        this.currentRetryCount = currentRetryCount;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    public long getNextRetryTime() {
        return nextRetryTime;
    }

    public void setNextRetryTime(long nextRetryTime) {
        this.nextRetryTime = nextRetryTime;
    }

    public long getLastRetryTime() {
        return lastRetryTime;
    }

    public void setLastRetryTime(long lastRetryTime) {
        this.lastRetryTime = lastRetryTime;
    }

    public int getTotalRetryCount() {
        return totalRetryCount;
    }

    public void setTotalRetryCount(int totalRetryCount) {
        this.totalRetryCount = totalRetryCount;
    }
}
