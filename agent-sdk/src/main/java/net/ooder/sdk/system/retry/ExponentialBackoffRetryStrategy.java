package net.ooder.sdk.system.retry;


/**
 * 指数退避重试策略，每次重试的时间间隔呈指数增长
 */
public class ExponentialBackoffRetryStrategy implements RetryStrategy {

    /**
     * 初始重试间隔（毫秒）
     */
    private final long initialInterval;

    /**
     * 最大重试间隔（毫秒）
     */
    private final long maxInterval;

    /**
     * 退避因子
     */
    private final double backoffFactor;

    /**
     * 最大重试次数
     */
    private final int maxRetries;

    /**
     * 构造方法
     * @param initialInterval 初始重试间隔（毫秒）
     * @param maxInterval 最大重试间隔（毫秒）
     * @param backoffFactor 退避因子
     */
    public ExponentialBackoffRetryStrategy(long initialInterval, long maxInterval, double backoffFactor) {
        this.initialInterval = initialInterval;
        this.maxInterval = maxInterval;
        this.backoffFactor = backoffFactor;
        this.maxRetries = 3; // 默认最大重试次数
    }

    /**
     * 构造方法（使用默认退避因子2.0）
     * @param initialInterval 初始重试间隔（毫秒）
     * @param maxInterval 最大重试间隔（毫秒）
     */
    public ExponentialBackoffRetryStrategy(long initialInterval, long maxInterval) {
        this(initialInterval, maxInterval, 2.0);
    }

    /**
     * 构造方法
     * @param initialInterval 初始重试间隔（毫秒）
     * @param maxInterval 最大重试间隔（毫秒）
     * @param backoffFactor 退避因子
     * @param maxRetries 最大重试次数
     */
    public ExponentialBackoffRetryStrategy(long initialInterval, long maxInterval, double backoffFactor, int maxRetries) {
        this.initialInterval = initialInterval;
        this.maxInterval = maxInterval;
        this.backoffFactor = backoffFactor;
        this.maxRetries = maxRetries;
    }

    @Override
    public long calculateNextRetryDelay(int retryCount) {
        // 计算指数退避间隔：initialInterval * (backoffFactor ^ retryCount)
        long delay = (long) (initialInterval * Math.pow(backoffFactor, retryCount));
        // 确保不超过最大间隔
        return Math.min(delay, maxInterval);
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
