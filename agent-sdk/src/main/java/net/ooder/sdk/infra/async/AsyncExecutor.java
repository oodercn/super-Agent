
package net.ooder.sdk.infra.async;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public final class AsyncExecutor {
    
    private final ExecutorService executorService;
    private final ScheduledExecutorService scheduledExecutor;
    private final AtomicInteger activeTasks = new AtomicInteger(0);
    private volatile boolean shutdown = false;
    
    public AsyncExecutor() {
        this(Runtime.getRuntime().availableProcessors());
    }
    
    public AsyncExecutor(int threadPoolSize) {
        this.executorService = Executors.newFixedThreadPool(threadPoolSize, r -> {
            Thread t = new Thread(r);
            t.setName("async-executor-" + t.getId());
            t.setDaemon(true);
            return t;
        });
        this.scheduledExecutor = Executors.newScheduledThreadPool(1, r -> {
            Thread t = new Thread(r);
            t.setName("async-scheduler-" + t.getId());
            t.setDaemon(true);
            return t;
        });
    }
    
    public <T> CompletableFuture<T> submit(Callable<T> task) {
        if (shutdown) {
            CompletableFuture<T> future = new CompletableFuture<>();
            future.completeExceptionally(new IllegalStateException("Executor is shutdown"));
            return future;
        }
        
        activeTasks.incrementAndGet();
        CompletableFuture<T> future = new CompletableFuture<>();
        
        executorService.submit(() -> {
            try {
                T result = task.call();
                future.complete(result);
            } catch (Exception e) {
                future.completeExceptionally(e);
            } finally {
                activeTasks.decrementAndGet();
            }
        });
        
        return future;
    }
    
    public CompletableFuture<Void> submit(Runnable task) {
        return submit(() -> {
            task.run();
            return null;
        });
    }
    
    public <T> CompletableFuture<T> submitAsync(java.util.function.Supplier<T> supplier) {
        return submit(supplier::get);
    }
    
    public ScheduledFuture<?> schedule(Runnable task, long delay, TimeUnit unit) {
        if (shutdown) {
            throw new IllegalStateException("Executor is shutdown");
        }
        return scheduledExecutor.schedule(task, delay, unit);
    }
    
    public <T> ScheduledFuture<T> schedule(Callable<T> task, long delay, TimeUnit unit) {
        if (shutdown) {
            throw new IllegalStateException("Executor is shutdown");
        }
        return scheduledExecutor.schedule(task, delay, unit);
    }
    
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, long initialDelay, long period, TimeUnit unit) {
        if (shutdown) {
            throw new IllegalStateException("Executor is shutdown");
        }
        return scheduledExecutor.scheduleAtFixedRate(task, initialDelay, period, unit);
    }
    
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable task, long initialDelay, long delay, TimeUnit unit) {
        if (shutdown) {
            throw new IllegalStateException("Executor is shutdown");
        }
        return scheduledExecutor.scheduleWithFixedDelay(task, initialDelay, delay, unit);
    }
    
    public <T> CompletableFuture<T> withTimeout(CompletableFuture<T> future, long timeout, TimeUnit unit) {
        CompletableFuture<T> timeoutFuture = new CompletableFuture<>();
        
        scheduledExecutor.schedule(() -> {
            if (!future.isDone()) {
                timeoutFuture.completeExceptionally(new java.util.concurrent.TimeoutException("Task timed out"));
            }
        }, timeout, unit);
        
        future.whenComplete((result, ex) -> {
            if (ex != null) {
                timeoutFuture.completeExceptionally(ex);
            } else {
                timeoutFuture.complete(result);
            }
        });
        
        return timeoutFuture;
    }
    
    public <T> CompletableFuture<T> withFallback(CompletableFuture<T> future, java.util.function.Supplier<T> fallback) {
        CompletableFuture<T> resultFuture = new CompletableFuture<>();
        
        future.whenComplete((result, ex) -> {
            if (ex != null) {
                try {
                    T fallbackResult = fallback.get();
                    resultFuture.complete(fallbackResult);
                } catch (Exception e) {
                    resultFuture.completeExceptionally(e);
                }
            } else {
                resultFuture.complete(result);
            }
        });
        
        return resultFuture;
    }
    
    public int getActiveTaskCount() {
        return activeTasks.get();
    }
    
    public boolean isShutdown() {
        return shutdown;
    }
    
    public void shutdown() {
        shutdown = true;
        executorService.shutdown();
        scheduledExecutor.shutdown();
    }
    
    public void shutdownNow() {
        shutdown = true;
        executorService.shutdownNow();
        scheduledExecutor.shutdownNow();
    }
    
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        boolean executorTerminated = executorService.awaitTermination(timeout, unit);
        boolean scheduledTerminated = scheduledExecutor.awaitTermination(timeout, unit);
        return executorTerminated && scheduledTerminated;
    }
}
