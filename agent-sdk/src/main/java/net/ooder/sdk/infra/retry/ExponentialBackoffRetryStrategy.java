
package net.ooder.sdk.infra.retry;

import java.util.concurrent.Callable;

public class ExponentialBackoffRetryStrategy implements RetryStrategy {
    
    private final int maxRetries;
    private final long initialInterval;
    private final long maxInterval;
    private final double multiplier;
    
    public ExponentialBackoffRetryStrategy() {
        this(3, 1000, 30000, 2.0);
    }
    
    public ExponentialBackoffRetryStrategy(int maxRetries, long initialInterval, long maxInterval, double multiplier) {
        this.maxRetries = maxRetries;
        this.initialInterval = initialInterval;
        this.maxInterval = maxInterval;
        this.multiplier = multiplier;
    }
    
    @Override
    public <T> T execute(Callable<T> task) throws Exception {
        return execute(task, maxRetries);
    }
    
    @Override
    public <T> T execute(Callable<T> task, int maxRetries) throws Exception {
        Exception lastException = null;
        int attempts = 0;
        long currentInterval = initialInterval;
        
        while (attempts <= maxRetries) {
            try {
                return task.call();
            } catch (Exception e) {
                lastException = e;
                attempts++;
                
                if (attempts <= maxRetries) {
                    try {
                        Thread.sleep(currentInterval);
                        currentInterval = Math.min((long) (currentInterval * multiplier), maxInterval);
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
        return "ExponentialBackoff";
    }
    
    @Override
    public int getMaxRetries() {
        return maxRetries;
    }
    
    @Override
    public long getInitialInterval() {
        return initialInterval;
    }
    
    @Override
    public long getMaxInterval() {
        return maxInterval;
    }
    
    public double getMultiplier() {
        return multiplier;
    }
}
