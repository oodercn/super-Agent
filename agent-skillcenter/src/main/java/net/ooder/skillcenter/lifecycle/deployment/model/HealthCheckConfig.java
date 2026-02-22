package net.ooder.skillcenter.lifecycle.deployment.model;

/**
 * 健康检查配置 - 符合v0.7.0协议规范
 */
public class HealthCheckConfig {
    
    private String path;
    private int interval;
    private int timeout;
    private int failureThreshold;
    private int successThreshold;
    
    public HealthCheckConfig() {
        this.path = "/health";
        this.interval = 30;
        this.timeout = 10;
        this.failureThreshold = 3;
        this.successThreshold = 1;
    }
    
    public static HealthCheckConfig defaultConfig() {
        return new HealthCheckConfig();
    }
    
    public static HealthCheckConfig of(String path, int interval, int timeout) {
        HealthCheckConfig config = new HealthCheckConfig();
        config.setPath(path);
        config.setInterval(interval);
        config.setTimeout(timeout);
        return config;
    }
    
    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }
    
    public int getInterval() { return interval; }
    public void setInterval(int interval) { this.interval = interval; }
    
    public int getTimeout() { return timeout; }
    public void setTimeout(int timeout) { this.timeout = timeout; }
    
    public int getFailureThreshold() { return failureThreshold; }
    public void setFailureThreshold(int failureThreshold) { this.failureThreshold = failureThreshold; }
    
    public int getSuccessThreshold() { return successThreshold; }
    public void setSuccessThreshold(int successThreshold) { this.successThreshold = successThreshold; }
}
