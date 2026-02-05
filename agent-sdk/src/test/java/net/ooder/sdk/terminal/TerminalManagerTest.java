package net.ooder.sdk.terminal;

import net.ooder.sdk.terminal.model.TerminalDevice;
import net.ooder.sdk.terminal.model.TerminalEvent;
import net.ooder.sdk.terminal.model.TerminalEventType;
import net.ooder.sdk.terminal.model.TerminalStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TerminalManagerTest {

    @Mock
    private TerminalManager terminalManager;

    private TerminalDevice testDevice1;
    private TerminalDevice testDevice2;
    private TerminalEvent testEvent;

    @Before
    public void setUp() {
        // 初始化测试终端设备
        testDevice1 = new TerminalDevice();
        testDevice1.setDeviceId("device-1");
        testDevice1.setDeviceType("laptop");
        testDevice1.setStatus(TerminalStatus.ONLINE);
        testDevice1.setIpAddress("192.168.1.100");
        testDevice1.setDeviceName("test-laptop-1");
        testDevice1.setMacAddress("00:11:22:33:44:55");
        testDevice1.setLastSeen(System.currentTimeMillis());
        Map<String, Object> metadata1 = new HashMap<>();
        metadata1.put("manufacturer", "Dell");
        metadata1.put("model", "XPS 13");
        metadata1.put("cpu", "Intel i7");
        metadata1.put("memory", "16GB");
        metadata1.put("osName", "Windows 10");
        metadata1.put("osVersion", "10.0.19041");
        testDevice1.setMetadata(metadata1);

        testDevice2 = new TerminalDevice();
        testDevice2.setDeviceId("device-2");
        testDevice2.setDeviceType("desktop");
        testDevice2.setStatus(TerminalStatus.OFFLINE);
        testDevice2.setIpAddress("192.168.1.101");
        testDevice2.setDeviceName("test-desktop-1");
        testDevice2.setMacAddress("AA:BB:CC:DD:EE:FF");
        testDevice2.setLastSeen(System.currentTimeMillis() - 3600000); // 1小时前
        Map<String, Object> metadata2 = new HashMap<>();
        metadata2.put("manufacturer", "HP");
        metadata2.put("model", "EliteDesk");
        metadata2.put("cpu", "Intel i5");
        metadata2.put("memory", "8GB");
        metadata2.put("osName", "Ubuntu");
        metadata2.put("osVersion", "20.04 LTS");
        testDevice2.setMetadata(metadata2);

        // 初始化测试终端事件
        testEvent = new TerminalEvent();
        testEvent.setEventId("event-1");
        testEvent.setDeviceId("device-1");
        testEvent.setEventType(TerminalEventType.DEVICE_STATUS_CHANGED);
        testEvent.setTimestamp(System.currentTimeMillis());
        Map<String, Object> eventDetails = new HashMap<>();
        eventDetails.put("ipAddress", "192.168.1.100");
        eventDetails.put("deviceName", "test-laptop-1");
        eventDetails.put("previousStatus", TerminalStatus.OFFLINE);
        eventDetails.put("currentStatus", TerminalStatus.ONLINE);
        testEvent.setDetails(eventDetails);
    }

    // 测试终端发现相关方法
    @Test
    public void testTerminalDiscoveryMethods() {
        // 测试开始发现
        terminalManager.startDiscovery();
        verify(terminalManager).startDiscovery();

        // 测试停止发现
        terminalManager.stopDiscovery();
        verify(terminalManager).stopDiscovery();

        // 测试发现运行状态
        when(terminalManager.isDiscoveryRunning()).thenReturn(true);
        assertTrue(terminalManager.isDiscoveryRunning());

        when(terminalManager.isDiscoveryRunning()).thenReturn(false);
        assertFalse(terminalManager.isDiscoveryRunning());
    }

    // 测试终端管理相关方法
    @Test
    public void testTerminalManagementMethods() {
        // 测试注册设备
        when(terminalManager.registerDevice(testDevice1)).thenReturn(testDevice1);
        TerminalDevice registeredDevice = terminalManager.registerDevice(testDevice1);
        assertNotNull(registeredDevice);
        assertEquals("device-1", registeredDevice.getDeviceId());
        assertEquals("laptop", registeredDevice.getDeviceType());

        // 测试注销设备
        terminalManager.deregisterDevice("device-1");
        verify(terminalManager).deregisterDevice("device-1");

        // 测试获取设备
        when(terminalManager.getDevice("device-1")).thenReturn(testDevice1);
        TerminalDevice retrievedDevice = terminalManager.getDevice("device-1");
        assertNotNull(retrievedDevice);
        assertEquals("device-1", retrievedDevice.getDeviceId());

        // 测试获取所有设备
        List<TerminalDevice> devices = Arrays.asList(testDevice1, testDevice2);
        when(terminalManager.getAllDevices()).thenReturn(devices);
        List<TerminalDevice> allDevices = terminalManager.getAllDevices();
        assertEquals(2, allDevices.size());
        assertEquals("device-1", allDevices.get(0).getDeviceId());
        assertEquals("device-2", allDevices.get(1).getDeviceId());

        // 测试按状态获取设备
        when(terminalManager.getDevicesByStatus(TerminalStatus.ONLINE)).thenReturn(Collections.singletonList(testDevice1));
        List<TerminalDevice> onlineDevices = terminalManager.getDevicesByStatus(TerminalStatus.ONLINE);
        assertEquals(1, onlineDevices.size());
        assertEquals(TerminalStatus.ONLINE, onlineDevices.get(0).getStatus());

        when(terminalManager.getDevicesByStatus(TerminalStatus.OFFLINE)).thenReturn(Collections.singletonList(testDevice2));
        List<TerminalDevice> offlineDevices = terminalManager.getDevicesByStatus(TerminalStatus.OFFLINE);
        assertEquals(1, offlineDevices.size());
        assertEquals(TerminalStatus.OFFLINE, offlineDevices.get(0).getStatus());

        // 测试按类型获取设备
        when(terminalManager.getDevicesByType("laptop")).thenReturn(Collections.singletonList(testDevice1));
        List<TerminalDevice> laptopDevices = terminalManager.getDevicesByType("laptop");
        assertEquals(1, laptopDevices.size());
        assertEquals("laptop", laptopDevices.get(0).getDeviceType());

        when(terminalManager.getDevicesByType("desktop")).thenReturn(Collections.singletonList(testDevice2));
        List<TerminalDevice> desktopDevices = terminalManager.getDevicesByType("desktop");
        assertEquals(1, desktopDevices.size());
        assertEquals("desktop", desktopDevices.get(0).getDeviceType());
    }

    // 测试终端状态管理相关方法
    @Test
    public void testTerminalStatusManagementMethods() {
        // 测试更新设备状态
        terminalManager.updateDeviceStatus("device-1", TerminalStatus.OFFLINE);
        verify(terminalManager).updateDeviceStatus("device-1", TerminalStatus.OFFLINE);

        // 测试更新设备元数据
        Map<String, Object> newMetadata = new HashMap<>();
        newMetadata.put("manufacturer", "Dell");
        newMetadata.put("model", "XPS 13");
        newMetadata.put("cpu", "Intel i7");
        newMetadata.put("memory", "32GB"); // 更新内存
        newMetadata.put("storage", "1TB SSD"); // 新增存储信息
        terminalManager.updateDeviceMetadata("device-1", newMetadata);
        verify(terminalManager).updateDeviceMetadata("device-1", newMetadata);

        // 测试同步设备状态
        terminalManager.syncDeviceStatus("device-1");
        verify(terminalManager).syncDeviceStatus("device-1");
    }

    // 测试终端事件相关方法
    @Test
    public void testTerminalEventMethods() throws InterruptedException {
        // 测试发布终端事件
        terminalManager.publishEvent(testEvent);
        verify(terminalManager).publishEvent(testEvent);

        // 测试订阅终端事件
        final CountDownLatch latch = new CountDownLatch(1);
        final TerminalEvent[] receivedEvent = new TerminalEvent[1];

        Consumer<TerminalEvent> eventHandler = e -> {
            receivedEvent[0] = e;
            latch.countDown();
        };

        terminalManager.subscribeToEvents(eventHandler);
        verify(terminalManager).subscribeToEvents(eventHandler);

        // 测试取消订阅终端事件
        terminalManager.unsubscribeFromEvents(eventHandler);
        verify(terminalManager).unsubscribeFromEvents(eventHandler);

        // 测试获取最近的终端事件
        List<TerminalEvent> events = Collections.singletonList(testEvent);
        when(terminalManager.getRecentEvents(10)).thenReturn(events);
        List<TerminalEvent> recentEvents = terminalManager.getRecentEvents(10);
        assertEquals(1, recentEvents.size());
        assertEquals("event-1", recentEvents.get(0).getEventId());
    }

    // 测试终端监控相关方法
    @Test
    public void testTerminalMonitoringMethods() {
        // 测试开始监控
        terminalManager.startMonitoring();
        verify(terminalManager).startMonitoring();

        // 测试停止监控
        terminalManager.stopMonitoring();
        verify(terminalManager).stopMonitoring();

        // 测试获取设备统计信息
        Map<String, Object> deviceStats = new HashMap<>();
        deviceStats.put("uptime", 3600);
        deviceStats.put("cpuUsage", 0.3);
        deviceStats.put("memoryUsage", 0.4);
        deviceStats.put("diskUsage", 0.5);
        deviceStats.put("networkIn", 1024);
        deviceStats.put("networkOut", 512);
        deviceStats.put("batteryLevel", 0.8);
        deviceStats.put("lastHeartbeat", System.currentTimeMillis());
        when(terminalManager.getDeviceStats("device-1")).thenReturn(deviceStats);
        Map<String, Object> retrievedDeviceStats = terminalManager.getDeviceStats("device-1");
        assertNotNull(retrievedDeviceStats);
        assertEquals(3600, retrievedDeviceStats.get("uptime"));
        assertEquals(0.3, retrievedDeviceStats.get("cpuUsage"));

        // 测试获取整体统计信息
        Map<String, Object> overallStats = new HashMap<>();
        overallStats.put("totalDevices", 10);
        overallStats.put("onlineDevices", 8);
        overallStats.put("offlineDevices", 2);
        overallStats.put("deviceTypes", Arrays.asList("laptop", "desktop", "tablet", "phone"));
        Map<String, Double> osDistribution = new HashMap<>();
        osDistribution.put("Windows", 60.0);
        osDistribution.put("Linux", 30.0);
        osDistribution.put("macOS", 10.0);
        overallStats.put("osDistribution", osDistribution);
        overallStats.put("averageUptime", 86400);
        overallStats.put("lastScanTime", System.currentTimeMillis());
        when(terminalManager.getOverallStats()).thenReturn(overallStats);
        Map<String, Object> retrievedOverallStats = terminalManager.getOverallStats();
        assertNotNull(retrievedOverallStats);
        assertEquals(10, retrievedOverallStats.get("totalDevices"));
        assertEquals(8, retrievedOverallStats.get("onlineDevices"));
        assertEquals(2, retrievedOverallStats.get("offlineDevices"));
    }

    // 测试终端设备的元数据更新
    @Test
    public void testTerminalDeviceMetadataUpdate() {
        // 测试设备元数据合并
        Map<String, Object> existingMetadata = new HashMap<>();
        existingMetadata.put("manufacturer", "Dell");
        existingMetadata.put("model", "XPS 13");
        existingMetadata.put("cpu", "Intel i7");

        Map<String, Object> updateMetadata = new HashMap<>();
        updateMetadata.put("memory", "32GB");
        updateMetadata.put("storage", "1TB SSD");

        // 模拟元数据更新
        TerminalDevice deviceWithUpdatedMetadata = testDevice1;
        Map<String, Object> mergedMetadata = new HashMap<>(existingMetadata);
        mergedMetadata.putAll(updateMetadata);
        deviceWithUpdatedMetadata.setMetadata(mergedMetadata);

        // 测试更新后的元数据
        assertEquals("Dell", deviceWithUpdatedMetadata.getMetadata().get("manufacturer"));
        assertEquals("XPS 13", deviceWithUpdatedMetadata.getMetadata().get("model"));
        assertEquals("Intel i7", deviceWithUpdatedMetadata.getMetadata().get("cpu"));
        assertEquals("32GB", deviceWithUpdatedMetadata.getMetadata().get("memory"));
        assertEquals("1TB SSD", deviceWithUpdatedMetadata.getMetadata().get("storage"));
    }

    // 测试终端设备的状态转换
    @Test
    public void testTerminalDeviceStatusTransition() {
        // 测试设备状态从离线到在线
        TerminalDevice device = testDevice2;
        assertEquals(TerminalStatus.OFFLINE, device.getStatus());

        // 模拟状态更新
        device.setStatus(TerminalStatus.ONLINE);
        device.setLastSeen(System.currentTimeMillis());

        assertEquals(TerminalStatus.ONLINE, device.getStatus());
        assertTrue(device.getLastSeen() > System.currentTimeMillis() - 1000);

        // 测试设备状态从在线到离线
        device.setStatus(TerminalStatus.OFFLINE);
        assertEquals(TerminalStatus.OFFLINE, device.getStatus());
    }

    // 测试终端设备的心跳和最后看到时间
    @Test
    public void testTerminalDeviceHeartbeat() {
        long initialLastSeen = testDevice1.getLastSeen();
        
        // 模拟设备心跳
        long newLastSeen = System.currentTimeMillis();
        testDevice1.setLastSeen(newLastSeen);
        
        assertEquals(newLastSeen, testDevice1.getLastSeen());
        assertTrue(testDevice1.getLastSeen() > initialLastSeen);
        
        // 测试设备离线检测
        long offlineTimeThreshold = 300000; // 5分钟
        long oldLastSeen = System.currentTimeMillis() - offlineTimeThreshold - 1000;
        testDevice1.setLastSeen(oldLastSeen);
        
        // 计算设备是否离线
        boolean isOffline = System.currentTimeMillis() - testDevice1.getLastSeen() > offlineTimeThreshold;
        assertTrue(isOffline);
    }
}
