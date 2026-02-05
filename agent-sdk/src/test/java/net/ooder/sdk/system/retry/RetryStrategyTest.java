package net.ooder.sdk.system.retry;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RetryStrategyTest {

    @Test
    void testExponentialBackoffStrategy() {
        RetryStrategy strategy = RetryStrategy.builder()
                .type(RetryStrategy.RetryType.EXPONENTIAL_BACKOFF)
                .initialInterval(1000)
                .maxInterval(30000)
                .backoffFactor(2)
                .build();

        // 测试计算重试间隔
        long interval1 = strategy.calculateNextRetryDelay(1);
        long interval2 = strategy.calculateNextRetryDelay(2);
        long interval3 = strategy.calculateNextRetryDelay(3);

        // 验证间隔是否按指数增长
        assertTrue(interval1 > 0);
        assertTrue(interval2 > interval1);
        assertTrue(interval3 > interval2);

        // 测试shouldRetry方法
        assertTrue(strategy.shouldRetry(1, 5));
        assertTrue(strategy.shouldRetry(4, 5));
        assertFalse(strategy.shouldRetry(5, 5));
    }

    @Test
    void testFixedIntervalStrategy() {
        RetryStrategy strategy = RetryStrategy.builder()
                .type(RetryStrategy.RetryType.FIXED_INTERVAL)
                .initialInterval(2000)
                .build();

        // 测试计算重试间隔
        long interval1 = strategy.calculateNextRetryDelay(1);
        long interval2 = strategy.calculateNextRetryDelay(2);

        // 验证间隔是否固定
        assertEquals(2000, interval1);
        assertEquals(2000, interval2);

        // 测试shouldRetry方法
        assertTrue(strategy.shouldRetry(1, 3));
        assertTrue(strategy.shouldRetry(2, 3));
        assertFalse(strategy.shouldRetry(3, 3));
    }

    @Test
    void testRandomStrategy() {
        RetryStrategy strategy = RetryStrategy.builder()
                .type(RetryStrategy.RetryType.RANDOM)
                .initialInterval(1000)
                .maxInterval(5000)
                .build();

        // 测试计算重试间隔
        long interval1 = strategy.calculateNextRetryDelay(1);
        long interval2 = strategy.calculateNextRetryDelay(2);
        long interval3 = strategy.calculateNextRetryDelay(3);

        // 验证间隔是否在范围内
        assertTrue(interval1 >= 1000 && interval1 <= 5000);
        assertTrue(interval2 >= 1000 && interval2 <= 5000);
        assertTrue(interval3 >= 1000 && interval3 <= 5000);

        // 测试shouldRetry方法
        assertTrue(strategy.shouldRetry(1, 4));
        assertTrue(strategy.shouldRetry(3, 4));
        assertFalse(strategy.shouldRetry(4, 4));
    }

    @Test
    void testCalculateRetryIntervalCompatibility() {
        RetryStrategy strategy = RetryStrategy.builder()
                .type(RetryStrategy.RetryType.EXPONENTIAL_BACKOFF)
                .initialInterval(1000)
                .maxInterval(30000)
                .backoffFactor(2)
                .build();

        // 测试兼容方法calculateRetryInterval
        long interval1 = strategy.calculateRetryInterval(1);
        long interval2 = strategy.calculateRetryInterval(2);

        // 验证方法是否正常工作
        assertTrue(interval1 > 0);
        assertTrue(interval2 > interval1);
    }

    @Test
    void testShouldRetryLogic() {
        RetryStrategy strategy = RetryStrategy.builder()
                .type(RetryStrategy.RetryType.FIXED_INTERVAL)
                .initialInterval(1000)
                .build();

        // 测试不同重试次数的情况
        assertTrue(strategy.shouldRetry(0, 3)); // 第一次尝试
        assertTrue(strategy.shouldRetry(1, 3)); // 第一次重试
        assertTrue(strategy.shouldRetry(2, 3)); // 第二次重试
        assertFalse(strategy.shouldRetry(3, 3)); // 达到最大重试次数
        assertFalse(strategy.shouldRetry(4, 3)); // 超过最大重试次数
    }
}
