package net.ooder.sdk.network.udp;

import net.ooder.sdk.config.TestConfiguration;
import net.ooder.sdk.config.PortProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ContextConfiguration(classes = TestConfiguration.class)
@ActiveProfiles("test")
public class PortManagerTest {
    @Autowired
    private PortManager portManager;
    
    @Autowired
    private PortProperties portProperties;
    
    @BeforeEach
    public void setUp() {
        // portProperties is now a real Spring bean from TestConfiguration
        // No need to mock it
    }
    
    @Test
    public void testIsPortAvailable() {
        // 测试一个不太可能被使用的端口
        boolean available = portManager.isPortAvailable(12345);
        assertTrue(available);
    }
    
    @Test
    public void testAllocatePort() {
        int port = portManager.allocatePort(PortManager.ServiceType.UDPSDK);
        assertTrue(port > 0);
        assertTrue(port < 65536);
    }
    
    @Test
    public void testReleasePort() {
        int port = portManager.allocatePort(PortManager.ServiceType.UDPSDK);
        assertTrue(port > 0);
        
        portManager.releasePort(PortManager.ServiceType.UDPSDK);
        // 验证端口已被释放（应该可以重新分配）
        int newPort = portManager.allocatePort(PortManager.ServiceType.UDPSDK);
        assertTrue(newPort > 0);
    }
    
    @Test
    public void testReleasePortByNumber() {
        int port = portManager.allocatePort(PortManager.ServiceType.UDPSDK);
        assertTrue(port > 0);
        
        portManager.releasePort(port);
        // 验证端口已被释放（应该可以重新分配）
        int newPort = portManager.allocatePort(PortManager.ServiceType.UDPSDK);
        assertTrue(newPort > 0);
    }
    
    @Test
    public void testGetAllocatedPort() {
        int port = portManager.allocatePort(PortManager.ServiceType.UDPSDK);
        Integer allocatedPort = portManager.getAllocatedPort(PortManager.ServiceType.UDPSDK);
        assertNotNull(allocatedPort);
        assertEquals(port, allocatedPort.intValue());
    }
    
    @Test
    public void testGetServiceTypeForPort() {
        int port = portManager.allocatePort(PortManager.ServiceType.UDPSDK);
        PortManager.ServiceType serviceType = portManager.getServiceTypeForPort(port);
        assertEquals(PortManager.ServiceType.UDPSDK, serviceType);
    }
    
    @Test
    public void testGetNetworkEnvironment() {
        PortManager.NetworkEnvironment env = portManager.getNetworkEnvironment();
        assertNotNull(env);
        // 环境应该是LOCAL、LAN或INTRANET之一
        assertTrue(env == PortManager.NetworkEnvironment.LOCAL ||
                   env == PortManager.NetworkEnvironment.LAN ||
                   env == PortManager.NetworkEnvironment.INTRANET);
    }
    
    @Test
    public void testGetPortStatistics() {
        Map<String, Object> stats = portManager.getPortStatistics();
        assertNotNull(stats);
        assertTrue(stats.containsKey("allocationAttempts"));
        assertTrue(stats.containsKey("portConflicts"));
        assertTrue(stats.containsKey("allocatedPorts"));
        assertTrue(stats.containsKey("networkEnvironment"));
        assertTrue(stats.containsKey("portMap"));
    }
    
    @Test
    public void testPrintPortStatistics() {
        // 测试printPortStatistics方法不会抛出异常
        assertDoesNotThrow(() -> {
            portManager.printPortStatistics();
        });
    }
    
    @Test
    public void testMultipleServiceTypes() {
        // 为不同服务类型分配端口
        int udpPort = portManager.allocatePort(PortManager.ServiceType.UDPSDK);
        int discoveryPort = portManager.allocatePort(PortManager.ServiceType.DISCOVERY);
        int skillPort = portManager.allocatePort(PortManager.ServiceType.SKILL);
        
        assertTrue(udpPort > 0);
        assertTrue(discoveryPort > 0);
        assertTrue(skillPort > 0);
        
        // 验证端口不同
        assertNotEquals(udpPort, discoveryPort);
        assertNotEquals(udpPort, skillPort);
        assertNotEquals(discoveryPort, skillPort);
        
        // 释放所有端口
        portManager.releasePort(PortManager.ServiceType.UDPSDK);
        portManager.releasePort(PortManager.ServiceType.DISCOVERY);
        portManager.releasePort(PortManager.ServiceType.SKILL);
    }
}
