package net.ooder.skillcenter.shell;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * 控制台核心组件单元测试
 */
public class ConsoleCoreTest {
    private TestCommand testCommand;

    @Before
    public void setUp() {
        // 创建测试命令实例
        testCommand = new TestCommand();
    }

    @After
    public void tearDown() {
        // 清理资源
        testCommand = null;
    }

    @Test
    public void testCommandInterface() {
        // 验证命令接口实现
        assertNotNull(testCommand);
        assertEquals("test", testCommand.getName());
        assertEquals("Test command", testCommand.getDescription());
    }

    @Test
    public void testAbstractCommandDependencies() {
        // 验证AbstractCommand依赖注入
        assertNotNull(testCommand.skillManager);
        assertNotNull(testCommand.marketManager);
        assertNotNull(testCommand.executorEngine);
        assertNotNull(testCommand.skillStorage);
        assertNotNull(testCommand.output);
    }

    @Test
    public void testCommandExecution() throws Exception {
        // 执行命令
        String[] args = {"arg1", "arg2"};
        testCommand.execute(args);
        
        // 验证命令执行结果
        assertTrue(testCommand.executed);
        assertEquals(2, testCommand.args.length);
        assertEquals("arg1", testCommand.args[0]);
        assertEquals("arg2", testCommand.args[1]);
    }

    @Test
    public void testCommandOutputMethods() {
        // 测试输出方法（这些方法主要是输出到控制台，这里只测试方法调用不抛异常）
        try {
            testCommand.println("Test message");
            testCommand.error("Error message");
            testCommand.success("Success message");
            testCommand.warn("Warning message");
            // 如果执行到这里，说明方法调用成功
            assertTrue(true);
        } catch (Exception e) {
            fail("Output methods should not throw exceptions: " + e.getMessage());
        }
    }

    /**
     * 测试命令类，实现Command接口和继承AbstractCommand
     */
    private static class TestCommand extends AbstractCommand {
        public boolean executed = false;
        public String[] args;

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
            this.executed = true;
            this.args = args;
        }
    }
}
