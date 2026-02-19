
package net.ooder.sdk.infra.retry;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public final class RetryManager {
    
    private static final RetryManager INSTANCE = new RetryManager();
    
    private final Map<String, RetryStrategy> strategies = new HashMap<>();
    private RetryStrategy defaultStrategy;
    
    private RetryManager() {
        defaultStrategy = new ExponentialBackoffRetryStrategy();
        registerStrategy("fixed", new FixedIntervalRetryStrategy());
        registerStrategy("exponential", defaultStrategy);
    }
    
    public static RetryManager getInstance() {
        return INSTANCE;
    }
    
    public void registerStrategy(String name, RetryStrategy strategy) {
        strategies.put(name, strategy);
    }
    
    public RetryStrategy getStrategy(String name) {
        return strategies.get(name);
    }
    
    public RetryStrategy getDefaultStrategy() {
        return defaultStrategy;
    }
    
    public void setDefaultStrategy(RetryStrategy strategy) {
        this.defaultStrategy = strategy;
    }
    
    public <T> T execute(Callable<T> task) throws Exception {
        return defaultStrategy.execute(task);
    }
    
    public <T> T execute(Callable<T> task, int maxRetries) throws Exception {
        return defaultStrategy.execute(task, maxRetries);
    }
    
    public <T> T execute(String strategyName, Callable<T> task) throws Exception {
        RetryStrategy strategy = strategies.get(strategyName);
        if (strategy == null) {
            throw new IllegalArgumentException("Unknown retry strategy: " + strategyName);
        }
        return strategy.execute(task);
    }
    
    public <T> T executeWithResult(Callable<T> task, T defaultValue) {
        try {
            return execute(task);
        } catch (Exception e) {
            return defaultValue;
        }
    }
    
    public boolean executeWithBooleanResult(Callable<Boolean> task) {
        try {
            return execute(task);
        } catch (Exception e) {
            return false;
        }
    }
}
