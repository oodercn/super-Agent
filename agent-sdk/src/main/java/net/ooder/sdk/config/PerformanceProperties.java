package net.ooder.sdk.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "ooder.sdk.performance")
public class PerformanceProperties {
    
    private boolean optimizerEnabled = true;
    
    private boolean compressionEnabled = true;
    
    private int compressionThreshold = 1024;
    
    private boolean adaptiveBuffer = true;
    
    private boolean adaptiveTimeout = true;
    
    private boolean connectionPoolEnabled = true;
    
    private int connectionPoolSize = 10;
    
    private int threadPoolSize = Runtime.getRuntime().availableProcessors();
    
    private boolean useNio = true;
    
    public boolean isOptimizerEnabled() {
        return optimizerEnabled;
    }
    
    public void setOptimizerEnabled(boolean optimizerEnabled) {
        this.optimizerEnabled = optimizerEnabled;
    }
    
    public boolean isCompressionEnabled() {
        return compressionEnabled;
    }
    
    public void setCompressionEnabled(boolean compressionEnabled) {
        this.compressionEnabled = compressionEnabled;
    }
    
    public int getCompressionThreshold() {
        return compressionThreshold;
    }
    
    public void setCompressionThreshold(int compressionThreshold) {
        this.compressionThreshold = compressionThreshold;
    }
    
    public boolean isAdaptiveBuffer() {
        return adaptiveBuffer;
    }
    
    public void setAdaptiveBuffer(boolean adaptiveBuffer) {
        this.adaptiveBuffer = adaptiveBuffer;
    }
    
    public boolean isAdaptiveTimeout() {
        return adaptiveTimeout;
    }
    
    public void setAdaptiveTimeout(boolean adaptiveTimeout) {
        this.adaptiveTimeout = adaptiveTimeout;
    }
    
    public boolean isConnectionPoolEnabled() {
        return connectionPoolEnabled;
    }
    
    public void setConnectionPoolEnabled(boolean connectionPoolEnabled) {
        this.connectionPoolEnabled = connectionPoolEnabled;
    }
    
    public int getConnectionPoolSize() {
        return connectionPoolSize;
    }
    
    public void setConnectionPoolSize(int connectionPoolSize) {
        this.connectionPoolSize = connectionPoolSize;
    }
    
    public int getThreadPoolSize() {
        return threadPoolSize;
    }
    
    public void setThreadPoolSize(int threadPoolSize) {
        this.threadPoolSize = threadPoolSize;
    }
    
    public boolean isUseNio() {
        return useNio;
    }
    
    public void setUseNio(boolean useNio) {
        this.useNio = useNio;
    }
}
