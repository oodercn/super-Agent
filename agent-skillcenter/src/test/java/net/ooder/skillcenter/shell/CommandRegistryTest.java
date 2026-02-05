package net.ooder.skillcenter.shell;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * 命令注册中心单元测试
 */
public class CommandRegistryTest {
    private CommandRegistry registry;
    private TestCommand testCommand;

    @Before
    public void setUp() {
        // 创建命令注册中心实例
        registry = new CommandRegistry();
        // 创建测试命令
        testCommand = new TestCommand();
    }

    @After
    public void tearDown() {
        // 清理资源
        registry.clear();
        registry = null;
        testCommand = null;
    }

    @Test
    public void testRegisterCommand() {
        // 执行：注册命令
        registry.registerCommand("test", testCommand);
        
        // 验证：命令是否注册成功
        assertTrue(registry.hasCommand("test"));
        assertNotNull(registry.getCommand("test"));
    }

    @Test
    public void testRegisterCommandWithGroup() {
        // 执行：注册命令到指定分组
        registry.registerCommand("test", testCommand, "custom-group");
        
        // 验证：命令是否注册成功，是否在指定分组中
        assertTrue(registry.hasCommand("test"));
        Set<String> groupCommands = registry.getCommandsByGroup("custom-group");
        assertTrue(groupCommands.contains("test"));
    }

    @Test
    public void testGetCommand() {
        // 准备：注册命令
        registry.registerCommand("test", testCommand);
        
        // 执行：获取命令
        Command retrievedCommand = registry.getCommand("test");
        
        // 验证：命令是否获取成功
        assertNotNull(retrievedCommand);
        assertEquals("test", retrievedCommand.getName());
    }

    @Test
    public void testHasCommand() {
        // 执行：检查不存在的命令
        assertFalse(registry.hasCommand("non-existent"));
        
        // 准备：注册命令
        registry.registerCommand("test", testCommand);
        
        // 执行：检查存在的命令
        assertTrue(registry.hasCommand("test"));
    }

    @Test
    public void testGetAllCommands() {
        // 准备：注册多个命令
        registry.registerCommand("test1", new TestCommand());
        registry.registerCommand("test2", new TestCommand());
        
        // 执行：获取所有命令
        Map<String, Command> allCommands = registry.getAllCommands();
        
        // 验证：命令数量是否正确
        assertEquals(2, allCommands.size());
        assertTrue(allCommands.containsKey("test1"));
        assertTrue(allCommands.containsKey("test2"));
    }

    @Test
    public void testGetCommandGroups() {
        // 准备：注册带分组的命令
        registry.registerCommand("group1 command1", new TestCommand());
        registry.registerCommand("group1 command2", new TestCommand());
        registry.registerCommand("group2 command1", new TestCommand());
        
        // 执行：获取命令分组
        Map<String, Set<String>> groups = registry.getCommandGroups();
        
        // 验证：分组是否正确
        assertTrue(groups.containsKey("group1"));
        assertTrue(groups.containsKey("group2"));
        assertEquals(2, groups.get("group1").size());
        assertEquals(1, groups.get("group2").size());
    }

    @Test
    public void testGetCommandsByGroup() {
        // 准备：注册带分组的命令
        registry.registerCommand("group1 command1", new TestCommand());
        registry.registerCommand("group1 command2", new TestCommand());
        
        // 执行：获取指定分组的命令
        Set<String> group1Commands = registry.getCommandsByGroup("group1");
        Set<String> nonExistentGroupCommands = registry.getCommandsByGroup("non-existent");
        
        // 验证：分组命令是否正确
        assertEquals(2, group1Commands.size());
        assertTrue(group1Commands.contains("group1 command1"));
        assertTrue(group1Commands.contains("group1 command2"));
        assertTrue(nonExistentGroupCommands.isEmpty());
    }

    @Test
    public void testRemoveCommand() {
        // 准备：注册命令
        registry.registerCommand("test", testCommand);
        assertTrue(registry.hasCommand("test"));
        
        // 执行：移除命令
        Command removedCommand = registry.removeCommand("test");
        
        // 验证：命令是否移除成功
        assertNotNull(removedCommand);
        assertFalse(registry.hasCommand("test"));
    }

    @Test
    public void testClear() {
        // 准备：注册多个命令
        registry.registerCommand("test1", new TestCommand());
        registry.registerCommand("test2", new TestCommand());
        assertEquals(2, registry.size());
        
        // 执行：清空所有命令
        registry.clear();
        
        // 验证：命令是否清空成功
        assertEquals(0, registry.size());
        assertFalse(registry.hasCommand("test1"));
        assertFalse(registry.hasCommand("test2"));
    }

    @Test
    public void testSize() {
        // 执行：获取初始大小
        assertEquals(0, registry.size());
        
        // 准备：注册命令
        registry.registerCommand("test1", new TestCommand());
        registry.registerCommand("test2", new TestCommand());
        
        // 执行：获取大小
        assertEquals(2, registry.size());
    }

    /**
     * 测试命令类
     */
    private static class TestCommand implements Command {
        @Override
        public String getName() {
            return "test";
        }

        @Override
        public String getDescription() {
            return "Test command";
        }

        @Override
        public void execute(String[] args) throws Exception {
            // 空实现
        }
    }
}
