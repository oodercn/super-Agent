package net.ooder.nexus.service;

import net.ooder.nexus.infrastructure.openwrt.service.OpenWrtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * OpenWrtService 单元测试
 *
 * @author ooder Team
 * @version 2.0.0-openwrt-preview
 * @since 2.0.0
 */
@SpringBootTest
public class OpenWrtServiceTest {

    @Autowired
    private OpenWrtService openWrtService;

    @BeforeEach
    public void setUp() {
        // 确保使用 Mock 模式进行测试
        openWrtService.setMockMode(true);
    }

    @Test
    public void testMockMode() {
        // 测试 Mock 模式切换
        assertTrue(openWrtService.isMockMode(), "应该处于 Mock 模式");

        openWrtService.setMockMode(false);
        assertFalse(openWrtService.isMockMode(), "应该处于真实模式");

        openWrtService.setMockMode(true);
        assertTrue(openWrtService.isMockMode(), "应该回到 Mock 模式");
    }

    @Test
    public void testConnect() {
        // 测试连接功能
        boolean connected = openWrtService.connect("192.168.1.1", "root", "password");
        assertTrue(connected, "Mock 模式下连接应该成功");
        assertTrue(openWrtService.isConnected(), "连接状态应该为 true");
    }

    @Test
    public void testDisconnect() {
        // 先连接
        openWrtService.connect("192.168.1.1", "root", "password");
        assertTrue(openWrtService.isConnected());

        // 断开连接
        openWrtService.disconnect();
        assertFalse(openWrtService.isConnected(), "断开连接后状态应该为 false");
    }

    @Test
    public void testGetSystemStatus() {
        // 测试获取系统状态
        Map<String, Object> status = openWrtService.getSystemStatus();

        assertNotNull(status, "系统状态不应为空");
        assertTrue(status.containsKey("cpu"), "应包含 CPU 信息");
        assertTrue(status.containsKey("memory"), "应包含内存信息");
        assertTrue(status.containsKey("temperature"), "应包含温度信息");
    }

    @Test
    public void testExecuteCommand() {
        // 测试命令执行
        Map<String, Object> result = openWrtService.executeCommand("uname -a");

        assertNotNull(result, "执行结果不应为空");
        assertTrue(result.containsKey("output"), "应包含输出字段");
        assertTrue(result.containsKey("exitCode"), "应包含退出码字段");
    }

    @Test
    public void testGetNetworkSettings() {
        // 测试获取网络设置
        Map<String, Object> settings = openWrtService.getAllNetworkSettings();

        assertNotNull(settings, "网络设置不应为空");
    }

    @Test
    public void testGetIPAddresses() {
        // 测试获取 IP 地址
        Map<String, Object> result = openWrtService.getIPAddresses("static", null);

        assertNotNull(result, "IP 地址列表不应为空");
    }

    @Test
    public void testGetIPBlacklist() {
        // 测试获取黑名单
        Map<String, Object> blacklist = openWrtService.getIPBlacklist();

        assertNotNull(blacklist, "黑名单不应为空");
    }

    @Test
    public void testGetVersionInfo() {
        // 测试获取版本信息
        Map<String, Object> version = openWrtService.getVersionInfo();

        assertNotNull(version, "版本信息不应为空");
        assertTrue(version.containsKey("version"), "应包含版本号");
    }

    @Test
    public void testIsVersionSupported() {
        // 测试版本支持检查
        boolean supported = openWrtService.isVersionSupported("21.02");
        assertTrue(supported, "21.02 版本应该受支持");
    }

    @Test
    public void testGetConfigFile() {
        // 测试获取配置文件
        Map<String, Object> config = openWrtService.getConfigFile("/etc/config/network");

        assertNotNull(config, "配置文件内容不应为空");
    }

    @Test
    public void testReboot() {
        // 测试重启（Mock 模式下不会真正重启）
        Map<String, Object> result = openWrtService.reboot();

        assertNotNull(result, "重启结果不应为空");
    }
}
