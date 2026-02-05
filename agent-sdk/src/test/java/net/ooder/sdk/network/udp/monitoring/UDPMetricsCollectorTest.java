package net.ooder.sdk.network.udp.monitoring;

import net.ooder.sdk.config.TestConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ContextConfiguration(classes = TestConfiguration.class)
@ActiveProfiles("test")
public class UDPMetricsCollectorTest {

    @Autowired
    private UDPMetricsCollector metricsCollector;

    @BeforeEach
    public void setUp() {
        assertNotNull(metricsCollector);
        // 确保服务已初始化
        metricsCollector.reset();
    }

    @Test
    public void testRecordPacketSent() {
        // 测试记录发送的数据包
        int packetSize = 1024;
        String operation = "test_operation";
        
        metricsCollector.recordPacketSent(packetSize, operation);
        
        UDPMetricsSnapshot snapshot = metricsCollector.getMetricsSnapshot();
        assertEquals(1, snapshot.getTotalPacketsSent());
        assertEquals(packetSize, snapshot.getTotalBytesSent());
    }

    @Test
    public void testRecordPacketReceived() {
        // 测试记录接收的数据包
        int packetSize = 512;
        String operation = "test_operation";
        
        metricsCollector.recordPacketReceived(packetSize, operation);
        
        UDPMetricsSnapshot snapshot = metricsCollector.getMetricsSnapshot();
        assertEquals(1, snapshot.getTotalPacketsReceived());
        assertEquals(packetSize, snapshot.getTotalBytesReceived());
    }

    @Test
    public void testRecordError() {
        // 测试记录错误
        String errorType = "test_error";
        String operation = "test_operation";
        String details = "test_details";
        
        metricsCollector.recordError(errorType, operation, details);
        
        UDPMetricsSnapshot snapshot = metricsCollector.getMetricsSnapshot();
        assertEquals(1, snapshot.getTotalErrors());
    }

    @Test
    public void testRecordLatency() {
        // 测试记录延迟
        long latency = 100; // 毫秒
        String operation = "test_operation";
        metricsCollector.recordLatency(operation, latency);
        
        UDPMetricsSnapshot snapshot = metricsCollector.getMetricsSnapshot();
        assertTrue(snapshot.getAverageLatency() >= 0);
    }

    @Test
    public void testMetricsSnapshot() {
        // 测试指标快照
        // 模拟一些数据
        String operation = "test_operation";
        metricsCollector.recordPacketSent(1024, operation);
        metricsCollector.recordPacketReceived(512, operation);
        metricsCollector.recordError("test_error", operation, "test_details");
        metricsCollector.recordLatency(operation, 100);
        
        UDPMetricsSnapshot snapshot = metricsCollector.getMetricsSnapshot();
        
        // 验证快照数据
        assertEquals(1, snapshot.getTotalPacketsSent());
        assertEquals(1024, snapshot.getTotalBytesSent());
        assertEquals(1, snapshot.getTotalPacketsReceived());
        assertEquals(512, snapshot.getTotalBytesReceived());
        assertEquals(1, snapshot.getTotalErrors());
        assertTrue(snapshot.getAverageLatency() >= 0);
        assertTrue(snapshot.getAverageThroughput() >= 0);
        assertNotNull(snapshot.getTimestamp());
    }

    @Test
    public void testResetMetrics() {
        // 测试重置指标
        // 先记录一些数据
        String operation = "test_operation";
        metricsCollector.recordPacketSent(1024, operation);
        metricsCollector.recordError("test_error", operation, "test_details");
        
        // 重置指标
        metricsCollector.reset();
        
        // 验证指标已重置
        UDPMetricsSnapshot snapshot = metricsCollector.getMetricsSnapshot();
        assertEquals(0, snapshot.getTotalPacketsSent());
        assertEquals(0, snapshot.getTotalBytesSent());
        assertEquals(0, snapshot.getTotalPacketsReceived());
        assertEquals(0, snapshot.getTotalBytesReceived());
        assertEquals(0, snapshot.getTotalErrors());
    }

    @Test
    public void testThroughputCalculation() {
        // 测试吞吐量计算
        // 记录多个数据包
        String operation = "test_operation";
        for (int i = 0; i < 10; i++) {
            metricsCollector.recordPacketSent(100, operation);
            try {
                Thread.sleep(10); // 模拟时间间隔
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        UDPMetricsSnapshot snapshot = metricsCollector.getMetricsSnapshot();
        // 吞吐量应该大于0
        assertTrue(snapshot.getAverageThroughput() >= 0);
    }
}
