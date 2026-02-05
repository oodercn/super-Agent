package net.ooder.sdk.system.retry;



import java.util.Random;

/**
 * 随机间隔重试策略，每次重试使用随机的时间间隔
 */
public class RandomRetryStrategy implements RetryStrategy {

    /**
     * 最小重试间隔（毫秒）
     */
    private final long minInterval;

    /**
     * 最大重试间隔（毫秒）
     */
    private final long maxInterval;

    /**
     * 随机数生成器
     */
    private final Random random;

    /**
     * 最大重试次数
     */
    private final int maxRetries;

    /**
     * 构造方法
     * @param minInterval 最小重试间隔（毫秒）
     * @param maxInterval 最大重试间隔（毫秒）
     */
    public RandomRetryStrategy(long minInterval, long maxInterval) {
        this.minInterval = minInterval;
        this.maxInterval = maxInterval;
        this.random = new Random();
        this.maxRetries = 3; // 默认最大重试次数
    }

    /**
     * 构造方法
     * @param minInterval 最小重试间隔（毫秒）
     * @param maxInterval 最大重试间隔（毫秒）
     * @param maxRetries 最大重试次数
     */
    public RandomRetryStrategy(long minInterval, long maxInterval, int maxRetries) {
        this.minInterval = minInterval;
        this.maxInterval = maxInterval;
        this.random = new Random();
        this.maxRetries = maxRetries;
    }

    @Override
    public long calculateNextRetryDelay(int retryCount) {
        // 生成[minInterval, maxInterval]范围内的随机间隔
        long range = maxInterval - minInterval + 1;
        long randomValue = Math.abs(random.nextLong() % range);
        return minInterval + randomValue;
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
