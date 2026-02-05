package net.ooder.sdk.system.retry;

/**
 * 固定间隔重试策略，每次重试使用相同的时间间隔
 */
public class FixedIntervalRetryStrategy implements RetryStrategy {

    /**
     * 固定重试间隔（毫秒）
     */
    private final long interval;

    /**
     * 最大重试次数
     */
    private final int maxRetries;

    /**
     * 构造方法
     * @param interval 固定重试间隔（毫秒）
     */
    public FixedIntervalRetryStrategy(long interval) {
        this.interval = interval;
        this.maxRetries = 3; // 默认最大重试次数
    }

    /**
     * 构造方法
     * @param interval 固定重试间隔（毫秒）
     * @param maxRetries 最大重试次数
     */
    public FixedIntervalRetryStrategy(long interval, int maxRetries) {
        this.interval = interval;
        this.maxRetries = maxRetries;
    }

    @Override
    public long calculateNextRetryDelay(int retryCount) {
        return interval;
    }

    @Override
    public long calculateRetryInterval(int retryCount) {
        return calculateNextRetryDelay(retryCount);
    }

    @Override
    public boolean shouldRetry(int retryCount, int maxRetries) {
        return retryCount < maxRetries;
    }

    @Override
    public int getMaxRetries() {
        return maxRetries;
    }
}
