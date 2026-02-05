package net.ooder.sdk.system.retry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

public class RetryExecutor {
    private static final Logger log = LoggerFactory.getLogger(RetryExecutor.class);
    private final RetryStrategy strategy;
    private final ExecutorService executorService;

    public RetryExecutor(RetryStrategy strategy) {
        this.strategy = strategy;
        this.executorService = Executors.newSingleThreadExecutor(r -> {
            Thread thread = new Thread(r, "retry-executor");
            thread.setDaemon(true);
            return thread;
        });
    }

    public <T> CompletableFuture<T> executeWithRetry(Supplier<T> task, int errorCode) {
        CompletableFuture<T> future = new CompletableFuture<>();
        executeWithRetry(task, errorCode, 0, future);
        return future;
    }

    private <T> void executeWithRetry(Supplier<T> task, int errorCode, int retryCount, CompletableFuture<T> future) {
        executorService.submit(() -> {
            try {
                T result = task.get();
                future.complete(result);
            } catch (Exception e) {
                if (strategy.shouldRetry(errorCode, retryCount)) {
                    long interval = strategy.calculateRetryInterval(retryCount);
                    log.info("Retry attempt {} for error code {}, waiting {}ms", retryCount + 1, errorCode, interval);

                    try {
                        Thread.sleep(interval);
                        executeWithRetry(task, errorCode, retryCount + 1, future);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        future.completeExceptionally(ie);
                    }
                } else {
                    log.error("Max retries reached or error not retryable for error code {}", errorCode);
                    future.completeExceptionally(e);
                }
            }
        });
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
