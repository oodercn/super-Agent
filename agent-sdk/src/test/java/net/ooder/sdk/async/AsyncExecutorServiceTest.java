package net.ooder.sdk.async;

import net.ooder.sdk.config.TestConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ContextConfiguration(classes = TestConfiguration.class)
@ActiveProfiles("test")
public class AsyncExecutorServiceTest {
    
    @Autowired
    private TestConfiguration testConfiguration;
    
    private static final long TEST_TIMEOUT = 5000;
    
    @BeforeEach
    public void setUp() {
        // 每个测试前确保配置可用
        assertNotNull(testConfiguration);
    }
    
    @Test
    public void testExecuteAsyncWithResult() throws Exception {
        AsyncExecutorService asyncExecutorService = new AsyncExecutorService();
        // 测试带返回值的异步执行
        String expectedResult = "Test Result";
        
        CompletableFuture<String> future = asyncExecutorService.executeAsync(() -> {
            Thread.sleep(100); // 模拟耗时操作
            return expectedResult;
        });
        
        // 验证任务是否在执行
        assertFalse(future.isDone());
        
        // 等待任务完成并获取结果
        String result = future.get(TEST_TIMEOUT, TimeUnit.MILLISECONDS);
        assertEquals(expectedResult, result);
        assertTrue(future.isDone());
        
        asyncExecutorService.shutdown();
    }
    
    @Test
    public void testExecuteAsyncWithoutResult() throws Exception {
        AsyncExecutorService asyncExecutorService = new AsyncExecutorService();
        // 测试无返回值的异步执行
        final boolean[] executed = {false};
        
        CompletableFuture<Void> future = asyncExecutorService.executeAsync(() -> {
            try {
                Thread.sleep(100); // 模拟耗时操作
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            executed[0] = true;
        });
        
        // 等待任务完成
        future.get(TEST_TIMEOUT, TimeUnit.MILLISECONDS);
        assertTrue(future.isDone());
        assertTrue(executed[0]);
        
        asyncExecutorService.shutdown();
    }
    
    @Test
    public void testExecuteAsyncWithTimeout() throws Exception {
        AsyncExecutorService asyncExecutorService = new AsyncExecutorService();
        // 测试带超时的异步执行
        String expectedResult = "Timeout Test";
        
        CompletableFuture<String> future = asyncExecutorService.executeAsyncWithTimeout(() -> {
            Thread.sleep(100); // 模拟耗时操作
            return expectedResult;
        }, 2000);
        
        // 验证结果
        String result = future.get(TEST_TIMEOUT, TimeUnit.MILLISECONDS);
        assertEquals(expectedResult, result);
        
        asyncExecutorService.shutdown();
    }
    
    @Test
    public void testExecuteAsyncWithTimeoutExceeded() {
        AsyncExecutorService asyncExecutorService = new AsyncExecutorService();
        // 测试超时异常
        assertThrows(RuntimeException.class, () -> {
            asyncExecutorService.executeAsyncWithTimeout(() -> {
                try {
                    Thread.sleep(3000); // 模拟超时操作
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                return "Should not return";
            }, 500); // 500ms超时
        });
        
        asyncExecutorService.shutdown();
    }
    
    @Test
    public void testExecuteAsyncWithException() throws Exception {
        AsyncExecutorService asyncExecutorService = new AsyncExecutorService();
        // 测试异常情况
        assertThrows(ExecutionException.class, () -> {
            asyncExecutorService.executeAsync(() -> {
                throw new Exception("Test exception");
            }).get(TEST_TIMEOUT, TimeUnit.MILLISECONDS);
        });
        
        asyncExecutorService.shutdown();
    }
    
    @Test
    public void testShutdown() throws Exception {
        AsyncExecutorService asyncExecutorService = new AsyncExecutorService();
        // 测试关闭服务
        asyncExecutorService.shutdown();
        
        // 验证服务是否已关闭
        Thread.sleep(100);
        // 注意：shutdown()后服务不会立即关闭，而是等待任务完成
        // 这里我们只是测试方法调用没有异常
    }
}
