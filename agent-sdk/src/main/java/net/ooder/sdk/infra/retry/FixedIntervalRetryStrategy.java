
package net.ooder.sdk.infra.retry;

import java.util.concurrent.Callable;

public class FixedIntervalRetryStrategy implements RetryStrategy {
    
    private final int maxRetries;
    private final long interval;
    
    public FixedIntervalRetryStrategy() {
        this(3, 1000);
    }
    
    public FixedIntervalRetryStrategy(int maxRetries, long interval) {
        this.maxRetries = maxRetries;
        this.interval = interval;
    }
    
    @Override
    public <T> T execute(Callable<T> task) throws Exception {
        return execute(task, maxRetries);
    }
    
    @Override
    public <T> T execute(Callable<T> task, int maxRetries) throws Exception {
        Exception lastException = null;
        int attempts = 0;
        
        while (attempts <= maxRetries) {
            try {
                return task.call();
            } catch (Exception e) {
                lastException = e;
                attempts++;
                
                if (attempts <= maxRetries) {
                    try {
                        Thread.sleep(interval);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException("Retry interrupted", ie);
                    }
                }
            }
        }
        
        throw lastException != null ? lastException : 
            new RuntimeException("Retry failed after " + maxRetries + " attempts");
    }
    
    @Override
    public String getName() {
        return "FixedInterval";
    }
    
    @Override
    public int getMaxRetries() {
        return maxRetries;
    }
    
    @Override
    public long getInitialInterval() {
        return interval;
    }
    
    @Override
    public long getMaxInterval() {
        return interval;
    }
}
