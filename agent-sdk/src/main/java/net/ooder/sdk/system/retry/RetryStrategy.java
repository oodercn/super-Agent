package net.ooder.sdk.system.retry;


/**
 * 重试策略接口，定义重试间隔的计算方法
 */
public interface RetryStrategy {

    /**
     * 重试类型枚举
     */
    enum RetryType {
        FIXED_INTERVAL,
        EXPONENTIAL_BACKOFF,
        RANDOM
    }

    /**
     * 计算下一次重试的时间间隔
     * @param retryCount 当前重试次数
     * @return 下一次重试的时间间隔（毫秒）
     */
    long calculateNextRetryDelay(int retryCount);

    /**
     * 计算重试间隔（兼容旧方法）
     * @param retryCount 当前重试次数
     * @return 重试间隔（毫秒）
     */
    long calculateRetryInterval(int retryCount);

    /**
     * 检查是否应该继续重试
     * @param retryCount 当前重试次数
     * @param maxRetries 最大重试次数
     * @return 是否应该继续重试
     */
    boolean shouldRetry(int retryCount, int maxRetries);

    /**
     * 获取最大重试次数
     * @return 最大重试次数
     */
    int getMaxRetries();

    /**
     * 创建重试策略构建器
     * @return 重试策略构建器
     */
    static Builder builder() {
        return new Builder();
    }

    /**
     * 重试策略构建器
     */
    class Builder {
        private RetryType type;
        private long initialInterval;
        private long maxInterval;
        private double backoffFactor;
        private int maxRetries;

        public Builder type(RetryType type) {
            this.type = type;
            return this;
        }

        public Builder initialInterval(long initialInterval) {
            this.initialInterval = initialInterval;
            return this;
        }

        public Builder maxInterval(long maxInterval) {
            this.maxInterval = maxInterval;
            return this;
        }

        public Builder backoffFactor(double backoffFactor) {
            this.backoffFactor = backoffFactor;
            return this;
        }

        public Builder maxRetries(int maxRetries) {
            this.maxRetries = maxRetries;
            return this;
        }

        public RetryStrategy build() {
            switch (type) {
                case FIXED_INTERVAL:
                    return new FixedIntervalRetryStrategy(initialInterval);
                case EXPONENTIAL_BACKOFF:
                    return new ExponentialBackoffRetryStrategy(initialInterval, maxInterval, backoffFactor);
                case RANDOM:
                    return new RandomRetryStrategy(initialInterval, maxInterval);
                default:
                    throw new IllegalArgumentException("Unknown retry type: " + type);
            }
        }
    }
}
