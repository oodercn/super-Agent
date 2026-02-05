package net.ooder.skillcenter.shell;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * 控制台主类单元测试
 */
public class ShellConsoleTest {
    private ShellConsole console;

    @Before
    public void setUp() {
        // 创建控制台实例
        console = new ShellConsole();
    }

    @After
    public void tearDown() {
        // 清理资源
        console.stop();
        console = null;
    }

    @Test
    public void testConstructor() {
        // 验证：控制台是否初始化成功
        assertNotNull(console);
        assertNotNull(console.getCommandRegistry());
        assertNotNull(console.getOutput());
    }

    @Test
    public void testBuiltinCommandsRegistered() {
        // 验证：内置命令是否注册成功
        CommandRegistry registry = console.getCommandRegistry();
        
        // 验证基本命令
        assertTrue(registry.hasCommand("help"));
        assertTrue(registry.hasCommand("exit"));
        
        // 验证技能管理命令
        assertTrue(registry.hasCommand("skill list"));
        assertTrue(registry.hasCommand("skill info"));
        assertTrue(registry.hasCommand("skill publish"));
        
        // 验证市场管理命令
        assertTrue(registry.hasCommand("market search"));
        assertTrue(registry.hasCommand("market rate"));
        
        // 验证执行管理命令
        assertTrue(registry.hasCommand("execute run"));
        
        // 验证存储管理命令
        assertTrue(registry.hasCommand("storage status"));
        
        // 验证系统管理命令
        assertTrue(registry.hasCommand("system status"));
    }

    @Test
    public void testExecuteCommand_ValidCommand() {
        // 执行：执行有效的内置命令（help命令）
        // 注意：这里只测试命令执行不抛异常，因为help命令会输出信息到控制台
        try {
            console.executeCommand("help");
            // 如果执行到这里，说明命令执行成功
            assertTrue(true);
        } catch (Exception e) {
            fail("Valid command should not throw exception: " + e.getMessage());
        }
    }

    @Test
    public void testExecuteCommand_InvalidCommand() {
        // 执行：执行无效命令
        // 注意：这里只测试命令执行不抛异常，因为无效命令会输出错误信息到控制台
        try {
            console.executeCommand("invalid-command");
            // 如果执行到这里，说明命令执行成功（虽然命令无效）
            assertTrue(true);
        } catch (Exception e) {
            fail("Invalid command should not throw exception: " + e.getMessage());
        }
    }

    @Test
    public void testExecuteCommand_EmptyCommand() {
        // 执行：执行空命令
        // 注意：这里只测试命令执行不抛异常
        try {
            console.executeCommand("");
            // 如果执行到这里，说明命令执行成功
            assertTrue(true);
        } catch (Exception e) {
            fail("Empty command should not throw exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetCommandRegistry() {
        // 执行：获取命令注册中心
        CommandRegistry registry = console.getCommandRegistry();
        
        // 验证：命令注册中心是否获取成功
        assertNotNull(registry);
    }

    @Test
    public void testGetOutput() {
        // 执行：获取命令输出
        CommandOutput output = console.getOutput();
        
        // 验证：命令输出是否获取成功
        assertNotNull(output);
    }

    @Test
    public void testPrintMethods() {
        // 执行：测试输出方法
        try {
            console.println("Test message");
            // 如果执行到这里，说明方法调用成功
            assertTrue(true);
        } catch (Exception e) {
            fail("Print method should not throw exception: " + e.getMessage());
        }
    }

    @Test
    public void testStop() {
        // 执行：停止控制台
        console.stop();
        // 注意：这里无法直接验证running状态，因为它是私有变量
        // 但可以验证方法调用不抛异常
        assertTrue(true);
    }
}
