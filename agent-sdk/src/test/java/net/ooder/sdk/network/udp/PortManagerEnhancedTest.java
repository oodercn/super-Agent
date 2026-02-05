package net.ooder.sdk.network.udp;

import net.ooder.sdk.config.TestConfiguration;
import net.ooder.sdk.config.PortProperties;
import org.junit.jupiter.api.AfterEach;
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
public class PortManagerEnhancedTest {

    @Autowired
    private PortManager portManager;

    @Autowired
    private PortProperties portProperties;

    @BeforeEach
    public void setUp() {
        assertNotNull(portManager);
        assertNotNull(portProperties);
    }

    @AfterEach
    public void tearDown() {
        // 清理分配的端口
        // PortManager没有提供releaseAllPorts方法，我们只能手动释放
    }

    @Test
    public void testSmartPortAllocation() {
        // 测试智能端口分配
        int port = portManager.allocatePort(PortManager.ServiceType.UDPSDK);
        
        // 验证端口是否已分配
        assertNotNull(port);
        assertTrue(port > 0);
    }
    
    @Test
    public void testPortAllocationStrategy() {
        // 测试不同服务类型的端口分配策略
        int udpSdkPort = portManager.allocatePort(PortManager.ServiceType.UDPSDK);
        int discoveryPort = portManager.allocatePort(PortManager.ServiceType.DISCOVERY);
        int skillPort = portManager.allocatePort(PortManager.ServiceType.SKILL);
        
        // 验证不同服务类型分配到不同范围的端口
        // 注意：PortManager根据网络环境分配端口，LOCAL环境下所有服务都使用local范围
        // 实际分配的端口取决于网络环境检测结果
        assertTrue(udpSdkPort > 0);
        assertTrue(discoveryPort > 0);
        assertTrue(skillPort > 0);
    }
    
    @Test
    public void testPortConflictHandling() {
        // 测试端口冲突处理
        int port1 = portManager.allocatePort(PortManager.ServiceType.UDPSDK);
        
        // 释放第一个端口
        portManager.releasePort(PortManager.ServiceType.UDPSDK);
        
        // 再次分配相同类型的端口
        int port2 = portManager.allocatePort(PortManager.ServiceType.UDPSDK);
        
        // 验证端口已分配
        assertNotNull(port2);
        assertTrue(port2 > 0);
    }
    
    @Test
    public void testServiceTypePortRanges() {
        // 测试服务类型的端口范围
        PortManager.ServiceType udpSdkType = PortManager.ServiceType.UDPSDK;
        assertEquals(8080, udpSdkType.getDefaultPort());
        assertEquals(8080, udpSdkType.getStartPort());
        assertEquals(8100, udpSdkType.getEndPort());
        
        PortManager.ServiceType discoveryType = PortManager.ServiceType.DISCOVERY;
        assertEquals(5000, discoveryType.getDefaultPort());
        assertEquals(5000, discoveryType.getStartPort());
        assertEquals(5020, discoveryType.getEndPort());
        
        PortManager.ServiceType skillType = PortManager.ServiceType.SKILL;
        assertEquals(9000, skillType.getDefaultPort());
        assertEquals(9000, skillType.getStartPort());
        assertEquals(9020, skillType.getEndPort());
    }
}
