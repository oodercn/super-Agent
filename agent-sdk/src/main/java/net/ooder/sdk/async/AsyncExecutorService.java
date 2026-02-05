package net.ooder.sdk.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.TimeUnit;

@Service
public class AsyncExecutorService {
    private static final Logger log = LoggerFactory.getLogger(AsyncExecutorService.class);
    
    private final ExecutorService executorService;
    private final AtomicInteger threadCounter = new AtomicInteger(0);
    
    @Autowired
    public AsyncExecutorService() {
        int threadPoolSize = Runtime.getRuntime().availableProcessors() * 2;
        this.executorService = Executors.newFixedThreadPool(threadPoolSize, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r, "async-executor-" + threadCounter.incrementAndGet());
                thread.setDaemon(true);
                thread.setPriority(Thread.NORM_PRIORITY);
                return thread;
            }
        });
        
        log.info("异步执行服务初始化完成，线程池大小: {}", threadPoolSize);
    }
    
    @Async("asyncExecutor")
    public <T> CompletableFuture<T> executeAsync(RunnableWithResult<T> task) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return task.run();
            } catch (Exception e) {
                log.error("异步任务执行失败", e);
                throw new RuntimeException("异步任务执行失败", e);
            }
        }, executorService);
    }
    
    @Async("asyncExecutor")
    public CompletableFuture<Void> executeAsync(Runnable task) {
        return CompletableFuture.runAsync(() -> {
            try {
                task.run();
            } catch (Exception e) {
                log.error("异步任务执行失败", e);
                throw new RuntimeException("异步任务执行失败", e);
            }
        }, executorService);
    }
    
    public <T> CompletableFuture<T> executeAsyncWithTimeout(RunnableWithResult<T> task, long timeoutMs) {
        CompletableFuture<T> future = executeAsync(task);
        
        try {
            T result = future.get(timeoutMs, TimeUnit.MILLISECONDS);
            return CompletableFuture.completedFuture(result);
        } catch (java.util.concurrent.TimeoutException e) {
            log.warn("异步任务执行超时: {}ms", timeoutMs);
            throw new RuntimeException("异步任务执行超时");
        } catch (Exception e) {
            log.error("异步任务执行异常", e);
            throw new RuntimeException("异步任务执行异常", e);
        }
    }
    
    public void shutdown() {
        log.info("异步执行服务正在关闭...");
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(30, TimeUnit.SECONDS)) {
                log.warn("异步执行服务关闭超时，强制关闭");
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            log.error("异步执行服务关闭被中断", e);
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
        log.info("异步执行服务已关闭");
    }
    
    @PreDestroy
    public void destroy() {
        shutdown();
    }
    
    @FunctionalInterface
    public interface RunnableWithResult<T> {
        T run() throws Exception;
    }
}
