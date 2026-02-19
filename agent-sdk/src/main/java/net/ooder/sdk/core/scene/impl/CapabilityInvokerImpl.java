package net.ooder.sdk.core.scene.impl;

import net.ooder.sdk.api.scene.CapabilityInvoker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ScheduledExecutorService;

public class CapabilityInvokerImpl implements CapabilityInvoker {
    
    private static final Logger log = LoggerFactory.getLogger(CapabilityInvokerImpl.class);
    
    private static final int DEFAULT_TIMEOUT_SECONDS = 30;
    private static final int MAX_POOL_SIZE = 50;
    
    private final ExecutorService executor;
    private final ScheduledExecutorService scheduler;
    private final Map<String, CapabilityMetadata> metadataStore;
    private final Map<String, Object> capabilityHandlers;
    private int defaultTimeoutSeconds = DEFAULT_TIMEOUT_SECONDS;
    
    public CapabilityInvokerImpl() {
        this.executor = new ThreadPoolExecutor(
            4, MAX_POOL_SIZE, 60L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(100),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.CallerRunsPolicy()
        );
        this.scheduler = Executors.newScheduledThreadPool(2);
        this.metadataStore = new ConcurrentHashMap<>();
        this.capabilityHandlers = new ConcurrentHashMap<>();
    }
    
    @Override
    public CompletableFuture<Object> invoke(String sceneId, String capId, Map<String, Object> params) {
        log.debug("Synchronously invoking capability: sceneId={}, capId={}", sceneId, capId);
        
        String key = buildKey(sceneId, capId);
        long startTime = System.currentTimeMillis();
        
        try {
            Object handler = capabilityHandlers.get(key);
            if (handler == null) {
                log.warn("No handler found for capability: {}", key);
                return CompletableFuture.completedFuture(createDefaultResult(capId, params));
            }
            
            Object result = executeHandler(handler, params);
            
            updateMetadata(sceneId, capId, System.currentTimeMillis() - startTime);
            
            return CompletableFuture.completedFuture(result);
            
        } catch (Exception e) {
            log.error("Capability invocation failed: {}", key, e);
            CompletableFuture<Object> failed = new CompletableFuture<>();
            failed.completeExceptionally(new RuntimeException("Capability invocation failed: " + e.getMessage(), e));
            return failed;
        }
    }
    
    @Override
    public CompletableFuture<Object> invoke(String capId, Map<String, Object> params) {
        return invoke("default", capId, params);
    }
    
    @Override
    public CompletableFuture<Object> invokeAsync(String sceneId, String capId, Map<String, Object> params) {
        log.debug("Asynchronously invoking capability: sceneId={}, capId={}", sceneId, capId);
        
        CompletableFuture<Object> future = CompletableFuture.supplyAsync(() -> {
            String key = buildKey(sceneId, capId);
            long startTime = System.currentTimeMillis();
            
            try {
                Object handler = capabilityHandlers.get(key);
                if (handler == null) {
                    log.warn("No handler found for async capability: {}", key);
                    return createDefaultResult(capId, params);
                }
                
                Object result = executeHandler(handler, params);
                
                updateMetadata(sceneId, capId, System.currentTimeMillis() - startTime);
                
                return result;
                
            } catch (Exception e) {
                log.error("Async capability invocation failed: {}", key, e);
                throw new RuntimeException("Async capability invocation failed: " + e.getMessage(), e);
            }
        }, executor);
        
        return withTimeout(future, defaultTimeoutSeconds, TimeUnit.SECONDS)
            .exceptionally(ex -> {
                log.error("Async capability invocation error: sceneId={}, capId={}, error={}", 
                    sceneId, capId, ex.getMessage());
                Map<String, Object> errorResult = new ConcurrentHashMap<>();
                errorResult.put("status", "error");
                errorResult.put("errorMessage", ex.getMessage());
                errorResult.put("sceneId", sceneId);
                errorResult.put("capId", capId);
                return errorResult;
            });
    }
    
    private <T> CompletableFuture<T> withTimeout(CompletableFuture<T> future, long timeout, TimeUnit unit) {
        final CompletableFuture<T> result = new CompletableFuture<>();
        
        final java.util.concurrent.ScheduledFuture<?> timeoutFuture = scheduler.schedule(() -> {
            if (!future.isDone()) {
                result.completeExceptionally(new java.util.concurrent.TimeoutException(
                    "Operation timed out after " + timeout + " " + unit.toString().toLowerCase()));
            }
        }, timeout, unit);
        
        future.whenComplete((res, ex) -> {
            timeoutFuture.cancel(false);
            if (ex != null) {
                result.completeExceptionally(ex);
            } else {
                result.complete(res);
            }
        });
        
        return result;
    }
    
    @Override
    public CompletableFuture<Boolean> isAvailable(String sceneId, String capId) {
        return CompletableFuture.supplyAsync(() -> {
            String key = buildKey(sceneId, capId);
            return capabilityHandlers.containsKey(key) || metadataStore.containsKey(key);
        }, executor);
    }
    
    @Override
    public CompletableFuture<CapabilityMetadata> getMetadata(String sceneId, String capId) {
        return CompletableFuture.supplyAsync(() -> {
            String key = buildKey(sceneId, capId);
            CapabilityMetadata metadata = metadataStore.get(key);
            if (metadata == null) {
                metadata = createDefaultMetadata(sceneId, capId);
            }
            return metadata;
        }, executor);
    }
    
    @Override
    public CompletableFuture<Object> invokeWithFallback(String sceneId, String capId, 
            Map<String, Object> params, String fallbackCapId) {
        
        return isAvailable(sceneId, capId)
            .thenCompose(available -> {
                if (available) {
                    return invoke(sceneId, capId, params);
                } else {
                    log.info("Capability {} not available, using fallback: {}", capId, fallbackCapId);
                    return invoke(sceneId, fallbackCapId, params);
                }
            });
    }
    
    public void registerHandler(String sceneId, String capId, Object handler) {
        String key = buildKey(sceneId, capId);
        capabilityHandlers.put(key, handler);
        
        CapabilityMetadata metadata = createDefaultMetadata(sceneId, capId);
        metadataStore.put(key, metadata);
        
        log.info("Registered capability handler: {}", key);
    }
    
    public void unregisterHandler(String sceneId, String capId) {
        String key = buildKey(sceneId, capId);
        capabilityHandlers.remove(key);
        metadataStore.remove(key);
        
        log.info("Unregistered capability handler: {}", key);
    }
    
    public void shutdown() {
        log.info("Shutting down CapabilityInvoker");
        executor.shutdown();
        scheduler.shutdown();
        try {
            if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                log.warn("Executor did not terminate in time, forcing shutdown");
                executor.shutdownNow();
            }
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            log.warn("Shutdown interrupted, forcing immediate shutdown");
            executor.shutdownNow();
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
        capabilityHandlers.clear();
        metadataStore.clear();
        log.info("CapabilityInvoker shutdown complete");
    }
    
    public void setDefaultTimeoutSeconds(int timeoutSeconds) {
        if (timeoutSeconds > 0) {
            this.defaultTimeoutSeconds = timeoutSeconds;
        }
    }
    
    public int getDefaultTimeoutSeconds() {
        return defaultTimeoutSeconds;
    }
    
    private String buildKey(String sceneId, String capId) {
        return sceneId + ":" + capId;
    }
    
    private Object executeHandler(Object handler, Map<String, Object> params) {
        try {
            if (handler instanceof java.util.function.Function) {
                @SuppressWarnings("unchecked")
                java.util.function.Function<Map<String, Object>, Object> functionHandler = 
                    (java.util.function.Function<Map<String, Object>, Object>) handler;
                return functionHandler.apply(params);
            } else if (handler instanceof java.util.concurrent.Callable) {
                @SuppressWarnings("unchecked")
                java.util.concurrent.Callable<Object> callableHandler = 
                    (java.util.concurrent.Callable<Object>) handler;
                return callableHandler.call();
            } else if (handler instanceof Runnable) {
                ((Runnable) handler).run();
                return createDefaultResult("handler", params);
            } else {
                java.lang.reflect.Method[] methods = handler.getClass().getMethods();
                for (java.lang.reflect.Method method : methods) {
                    if (method.getName().equals("execute") || 
                        method.getName().equals("invoke") || 
                        method.getName().equals("handle") ||
                        method.getName().equals("run")) {
                        if (method.getParameterCount() == 0) {
                            return method.invoke(handler);
                        } else if (method.getParameterCount() == 1 && 
                                   method.getParameterTypes()[0] == Map.class) {
                            return method.invoke(handler, params);
                        }
                    }
                }
                log.warn("Handler type not supported, using default result");
                return createDefaultResult("handler", params);
            }
        } catch (Exception e) {
            log.error("Failed to execute handler: {}", e.getMessage(), e);
            Map<String, Object> errorResult = new ConcurrentHashMap<>();
            errorResult.put("status", "error");
            errorResult.put("errorMessage", e.getMessage());
            errorResult.put("timestamp", System.currentTimeMillis());
            return errorResult;
        }
    }
    
    private Object createDefaultResult(String capId, Map<String, Object> params) {
        Map<String, Object> result = new ConcurrentHashMap<>();
        result.put("capId", capId);
        result.put("status", "success");
        result.put("params", params);
        result.put("timestamp", System.currentTimeMillis());
        return result;
    }
    
    private CapabilityMetadata createDefaultMetadata(String sceneId, String capId) {
        CapabilityMetadata metadata = new CapabilityMetadata();
        metadata.setCapId(capId);
        metadata.setName(capId);
        metadata.setDescription("Capability: " + capId);
        metadata.setSceneId(sceneId);
        metadata.setAsync(false);
        metadata.setAverageInvokeTime(0);
        metadata.setInvokeCount(0);
        return metadata;
    }
    
    private void updateMetadata(String sceneId, String capId, long invokeTime) {
        String key = buildKey(sceneId, capId);
        CapabilityMetadata metadata = metadataStore.get(key);
        if (metadata != null) {
            int count = metadata.getInvokeCount();
            long avgTime = metadata.getAverageInvokeTime();
            metadata.setInvokeCount(count + 1);
            metadata.setAverageInvokeTime((avgTime * count + invokeTime) / (count + 1));
        }
    }
}
