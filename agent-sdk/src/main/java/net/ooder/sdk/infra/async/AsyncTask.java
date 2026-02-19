
package net.ooder.sdk.infra.async;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

public class AsyncTask<T> {
    
    private final String taskId;
    private final CompletableFuture<T> future;
    private final long createTime;
    private volatile long startTime;
    private volatile long endTime;
    
    public AsyncTask(String taskId, CompletableFuture<T> future) {
        this.taskId = taskId;
        this.future = future;
        this.createTime = System.currentTimeMillis();
        this.future.whenComplete((result, ex) -> {
            this.endTime = System.currentTimeMillis();
        });
    }
    
    public String getTaskId() {
        return taskId;
    }
    
    public CompletableFuture<T> getFuture() {
        return future;
    }
    
    public long getCreateTime() {
        return createTime;
    }
    
    public long getStartTime() {
        return startTime;
    }
    
    public long getEndTime() {
        return endTime;
    }
    
    public boolean isDone() {
        return future.isDone();
    }
    
    public boolean isCompletedExceptionally() {
        return future.isCompletedExceptionally();
    }
    
    public boolean isCancelled() {
        return future.isCancelled();
    }
    
    public T get() throws Exception {
        startTime = System.currentTimeMillis();
        return future.get();
    }
    
    public T get(long timeout, TimeUnit unit) throws Exception {
        startTime = System.currentTimeMillis();
        return future.get(timeout, unit);
    }
    
    public T getNow(T defaultValue) {
        return future.getNow(defaultValue);
    }
    
    public T join() {
        startTime = System.currentTimeMillis();
        return future.join();
    }
    
    public boolean cancel(boolean mayInterruptIfRunning) {
        return future.cancel(mayInterruptIfRunning);
    }
    
    public AsyncTask<T> thenAccept(Consumer<T> action) {
        CompletableFuture<Void> nextFuture = future.thenAccept(action);
        return new AsyncTask<>(taskId + "-thenAccept", 
            nextFuture.thenApply(v -> null));
    }
    
    public <U> AsyncTask<U> thenApply(Function<T, U> fn) {
        CompletableFuture<U> nextFuture = future.thenApply(fn);
        return new AsyncTask<>(taskId + "-thenApply", nextFuture);
    }
    
    public <U> AsyncTask<U> thenCompose(Function<T, CompletableFuture<U>> fn) {
        CompletableFuture<U> nextFuture = future.thenCompose(fn);
        return new AsyncTask<>(taskId + "-thenCompose", nextFuture);
    }
    
    public AsyncTask<T> exceptionally(Function<Throwable, T> fn) {
        CompletableFuture<T> nextFuture = future.exceptionally(fn);
        return new AsyncTask<>(taskId + "-exceptionally", nextFuture);
    }
    
    public AsyncTask<T> whenComplete(Consumer<T> successHandler, Consumer<Throwable> errorHandler) {
        CompletableFuture<T> nextFuture = future.whenComplete((result, ex) -> {
            if (ex != null) {
                errorHandler.accept(ex);
            } else {
                successHandler.accept(result);
            }
        });
        return new AsyncTask<>(taskId + "-whenComplete", nextFuture);
    }
    
    public long getDuration() {
        if (endTime > 0 && startTime > 0) {
            return endTime - startTime;
        }
        if (startTime > 0 && isDone()) {
            return System.currentTimeMillis() - startTime;
        }
        return -1;
    }
    
    public AsyncTaskStatus getStatus() {
        if (isCancelled()) {
            return AsyncTaskStatus.CANCELLED;
        }
        if (isCompletedExceptionally()) {
            return AsyncTaskStatus.FAILED;
        }
        if (isDone()) {
            return AsyncTaskStatus.COMPLETED;
        }
        if (startTime > 0) {
            return AsyncTaskStatus.RUNNING;
        }
        return AsyncTaskStatus.PENDING;
    }
    
    public enum AsyncTaskStatus {
        PENDING,
        RUNNING,
        COMPLETED,
        FAILED,
        CANCELLED
    }
}
