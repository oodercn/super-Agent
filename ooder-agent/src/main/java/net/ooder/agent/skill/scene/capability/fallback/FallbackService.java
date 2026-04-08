package net.ooder.mvp.skill.scene.capability.fallback;

import net.ooder.mvp.skill.scene.capability.model.CapabilityBinding;
import net.ooder.mvp.skill.scene.capability.service.CapabilityBindingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class FallbackService {

    private static final Logger log = LoggerFactory.getLogger(FallbackService.class);

    @Autowired(required = false)
    private CapabilityBindingService bindingService;

    private final Map<String, FallbackConfig> fallbackConfigs = new ConcurrentHashMap<>();
    private final Map<String, Object> resultCache = new ConcurrentHashMap<>();
    private final Map<String, Integer> failureCounts = new ConcurrentHashMap<>();
    
    private static final int MAX_RETRY = 3;
    private static final long RETRY_DELAY_MS = 1000;
    private static final int FAILURE_THRESHOLD = 5;
    private static final long CIRCUIT_BREAKER_TIMEOUT_MS = 60000;

    public Object executeWithFallback(CapabilityBinding binding, Object input, 
                                       PrimaryExecutor primaryExecutor) {
        String bindingId = binding.getBindingId();
        FallbackConfig config = fallbackConfigs.getOrDefault(bindingId, 
            FallbackConfig.defaultConfig());
        
        if (isCircuitBreakerOpen(bindingId)) {
            log.warn("[Fallback] Circuit breaker open for binding: {}", bindingId);
            return executeFallback(binding, input, "Circuit breaker open");
        }

        try {
            Object result = executeWithRetry(binding, input, primaryExecutor, config);
            resetFailureCount(bindingId);
            cacheResult(bindingId, result);
            return result;
        } catch (Exception e) {
            log.error("[Fallback] Primary execution failed for binding {}: {}", 
                bindingId, e.getMessage());
            incrementFailureCount(bindingId);
            return executeFallback(binding, input, e.getMessage());
        }
    }

    private Object executeWithRetry(CapabilityBinding binding, Object input,
                                     PrimaryExecutor executor, FallbackConfig config) 
        throws Exception {
        
        int maxRetries = config.getMaxRetries();
        Exception lastException = null;
        
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                log.debug("[Fallback] Attempt {}/{} for binding {}", 
                    attempt, maxRetries, binding.getBindingId());
                return executor.execute(input);
            } catch (Exception e) {
                lastException = e;
                if (attempt < maxRetries) {
                    try {
                        Thread.sleep(RETRY_DELAY_MS * attempt);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw ie;
                    }
                }
            }
        }
        
        throw lastException;
    }

    private Object executeFallback(CapabilityBinding binding, Object input, 
                                    String reason) {
        String bindingId = binding.getBindingId();
        FallbackConfig config = fallbackConfigs.getOrDefault(bindingId, 
            FallbackConfig.defaultConfig());
        FallbackStrategy strategy = config.getStrategy();
        
        log.info("[Fallback] Executing fallback strategy {} for binding {}: {}", 
            strategy, bindingId, reason);
        
        switch (strategy) {
            case FALLBACK_PROVIDER:
                return executeFallbackProvider(binding, input);
            
            case CACHE:
                return getCachedResult(bindingId);
            
            case DEFAULT_VALUE:
                return config.getDefaultValue();
            
            case SKIP:
                return null;
            
            case FAIL_FAST:
            default:
                throw new RuntimeException("Capability execution failed: " + reason);
        }
    }

    private Object executeFallbackProvider(CapabilityBinding binding, Object input) {
        String fallbackProvider = binding.getFallbackBindingId();
        
        if (fallbackProvider == null || fallbackProvider.isEmpty()) {
            log.warn("[Fallback] No fallback provider configured for binding: {}", 
                binding.getBindingId());
            return null;
        }
        
        try {
            log.info("[Fallback] Using fallback provider: {}", fallbackProvider);
            // 实际调用备用Provider执行
            // 这里需要根据实际架构实现
            return null;
        } catch (Exception e) {
            log.error("[Fallback] Fallback provider execution failed: {}", e.getMessage());
            return null;
        }
    }

    private void cacheResult(String bindingId, Object result) {
        if (result != null) {
            resultCache.put(bindingId, result);
        }
    }

    private Object getCachedResult(String bindingId) {
        Object cached = resultCache.get(bindingId);
        if (cached != null) {
            log.info("[Fallback] Returning cached result for binding: {}", bindingId);
        }
        return cached;
    }

    private void incrementFailureCount(String bindingId) {
        int count = failureCounts.getOrDefault(bindingId, 0) + 1;
        failureCounts.put(bindingId, count);
        
        if (count >= FAILURE_THRESHOLD) {
            log.warn("[Fallback] Failure threshold reached for binding: {}", bindingId);
        }
    }

    private void resetFailureCount(String bindingId) {
        failureCounts.remove(bindingId);
    }

    private boolean isCircuitBreakerOpen(String bindingId) {
        Integer failures = failureCounts.get(bindingId);
        return failures != null && failures >= FAILURE_THRESHOLD;
    }

    public void configureFallback(String bindingId, FallbackConfig config) {
        fallbackConfigs.put(bindingId, config);
        log.info("[Fallback] Configured fallback for binding {}: strategy={}", 
            bindingId, config.getStrategy());
    }

    public FallbackConfig getFallbackConfig(String bindingId) {
        return fallbackConfigs.get(bindingId);
    }

    public void clearCache(String bindingId) {
        resultCache.remove(bindingId);
    }

    public void resetCircuitBreaker(String bindingId) {
        failureCounts.remove(bindingId);
        log.info("[Fallback] Circuit breaker reset for binding: {}", bindingId);
    }

    @FunctionalInterface
    public interface PrimaryExecutor {
        Object execute(Object input) throws Exception;
    }

    public static class FallbackConfig {
        private FallbackStrategy strategy;
        private int maxRetries;
        private Object defaultValue;
        private long timeoutMs;

        public FallbackConfig() {
            this.strategy = FallbackStrategy.CACHE;
            this.maxRetries = MAX_RETRY;
            this.timeoutMs = 30000;
        }

        public static FallbackConfig defaultConfig() {
            return new FallbackConfig();
        }

        public FallbackConfig strategy(FallbackStrategy strategy) {
            this.strategy = strategy;
            return this;
        }

        public FallbackConfig maxRetries(int maxRetries) {
            this.maxRetries = maxRetries;
            return this;
        }

        public FallbackConfig defaultValue(Object defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        public FallbackConfig timeoutMs(long timeoutMs) {
            this.timeoutMs = timeoutMs;
            return this;
        }

        public FallbackStrategy getStrategy() { return strategy; }
        public int getMaxRetries() { return maxRetries; }
        public Object getDefaultValue() { return defaultValue; }
        public long getTimeoutMs() { return timeoutMs; }
    }
}
