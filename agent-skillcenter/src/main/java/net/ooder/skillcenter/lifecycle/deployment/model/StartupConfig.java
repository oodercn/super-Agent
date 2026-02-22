package net.ooder.skillcenter.lifecycle.deployment.model;

/**
 * 启动配置 - 符合v0.7.0协议规范
 */
public class StartupConfig {
    
    private int timeout;
    private int order;
    private boolean waitForDependencies;
    private int retryCount;
    private long retryDelay;
    
    public StartupConfig() {
        this.timeout = 60;
        this.order = 100;
        this.waitForDependencies = false;
        this.retryCount = 3;
        this.retryDelay = 1000;
    }
    
    public static StartupConfig defaultConfig() {
        return new StartupConfig();
    }
    
    public static StartupConfig of(int timeout, int order) {
        StartupConfig config = new StartupConfig();
        config.setTimeout(timeout);
        config.setOrder(order);
        return config;
    }
    
    public int getTimeout() { return timeout; }
    public void setTimeout(int timeout) { this.timeout = timeout; }
    
    public int getOrder() { return order; }
    public void setOrder(int order) { this.order = order; }
    
    public boolean isWaitForDependencies() { return waitForDependencies; }
    public void setWaitForDependencies(boolean waitForDependencies) { this.waitForDependencies = waitForDependencies; }
    
    public int getRetryCount() { return retryCount; }
    public void setRetryCount(int retryCount) { this.retryCount = retryCount; }
    
    public long getRetryDelay() { return retryDelay; }
    public void setRetryDelay(long retryDelay) { this.retryDelay = retryDelay; }
}
