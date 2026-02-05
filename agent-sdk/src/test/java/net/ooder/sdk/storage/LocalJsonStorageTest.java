package net.ooder.sdk.storage;

import com.alibaba.fastjson.JSONObject;
import net.ooder.sdk.agent.model.AgentConfig;
import net.ooder.sdk.storage.impl.LocalJsonStorage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * LocalJsonStorage测试类
 */
public class LocalJsonStorageTest {
    private static final String TEST_ROOT_DIR = "test-storage";
    private LocalJsonStorage storage;
    
    @BeforeEach
    public void setUp() {
        // 创建测试存储实例
        StorageConfig config = new StorageConfig(TEST_ROOT_DIR);
        config.setEnableFileLock(false); // 测试时禁用文件锁
        storage = new LocalJsonStorage(config);
    }
    
    @AfterEach
    public void tearDown() {
        // 清理测试目录
        try {
            Files.walk(Paths.get(TEST_ROOT_DIR))
                 .sorted((a, b) -> b.compareTo(a))
                 .map(Path::toFile)
                 .forEach(File::delete);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // 关闭存储服务
        try {
            storage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testSaveAndLoad() {
        // 创建测试对象
        AgentConfig config = new AgentConfig();
        config.setAgentId("test-agent-123");
        config.setAgentName("Test Agent");
        config.setUdpPort(9000);
        
        // 保存数据
        boolean saved = storage.save("agent/test-agent-123/config", config);
        assertTrue(saved);
        
        // 加载数据
        AgentConfig loadedConfig = storage.load("agent/test-agent-123/config", AgentConfig.class);
        assertNotNull(loadedConfig);
        assertEquals("test-agent-123", loadedConfig.getAgentId());
        assertEquals("Test Agent", loadedConfig.getAgentName());
        assertEquals(9000, loadedConfig.getUdpPort());
        
        // 验证文件存在
        Path filePath = Paths.get(TEST_ROOT_DIR, "data", "agent", "test-agent-123", "config.json");
        assertTrue(Files.exists(filePath));
    }
    
    @Test
    public void testSaveJsonAndLoadJson() {
        // 创建JSON对象
        JSONObject json = new JSONObject();
        json.put("name", "Test Object");
        json.put("value", 123);
        json.put("active", true);
        
        // 保存JSON
        boolean saved = storage.saveJson("test/data", json);
        assertTrue(saved);
        
        // 加载JSON
        JSONObject loadedJson = storage.loadJson("test/data");
        assertNotNull(loadedJson);
        assertEquals("Test Object", loadedJson.getString("name"));
        assertEquals(123, loadedJson.getIntValue("value"));
        assertTrue(loadedJson.getBooleanValue("active"));
    }
    
    @Test
    public void testUpdateJson() {
        // 创建初始JSON
        JSONObject json = new JSONObject();
        json.put("name", "Test Object");
        json.put("value", 123);
        
        storage.saveJson("test/data", json);
        
        // 更新部分字段
        Map<String, Object> updates = new HashMap<>();
        updates.put("value", 456);
        updates.put("newField", "new value");
        
        boolean updated = storage.updateJson("test/data", updates);
        assertTrue(updated);
        
        // 验证更新
        JSONObject loadedJson = storage.loadJson("test/data");
        assertEquals(456, loadedJson.getIntValue("value"));
        assertEquals("new value", loadedJson.getString("newField"));
        assertEquals("Test Object", loadedJson.getString("name")); // 确保原有字段不变
    }
    
    @Test
    public void testGetJsonField() {
        // 创建JSON对象
        JSONObject json = new JSONObject();
        json.put("name", "Test Object");
        json.put("value", 123);
        json.put("active", true);
        
        storage.saveJson("test/data", json);
        
        // 获取单个字段
        String name = storage.getJsonField("test/data", "name", String.class);
        Integer value = storage.getJsonField("test/data", "value", Integer.class);
        Boolean active = storage.getJsonField("test/data", "active", Boolean.class);
        
        assertEquals("Test Object", name);
        assertEquals(Integer.valueOf(123), value);
        assertEquals(Boolean.TRUE, active);
        
        // 获取不存在的字段
        String nonExistent = storage.getJsonField("test/data", "nonExistent", String.class);
        assertNull(nonExistent);
    }
    
    @Test
    public void testDelete() {
        // 创建测试数据
        AgentConfig config = new AgentConfig();
        config.setAgentId("test-agent-456");
        
        storage.save("agent/test-agent-456/config", config);
        
        // 验证存在
        assertTrue(storage.exists("agent/test-agent-456/config"));
        
        // 删除
        boolean deleted = storage.delete("agent/test-agent-456/config");
        assertTrue(deleted);
        
        // 验证不存在
        assertFalse(storage.exists("agent/test-agent-456/config"));
        
        // 验证文件被删除
        Path filePath = Paths.get(TEST_ROOT_DIR, "data", "agent", "test-agent-456", "config.json");
        assertFalse(Files.exists(filePath));
    }
    
    @Test
    public void testBatchOperations() {
        // 批量保存
        Map<String, AgentConfig> configMap = new HashMap<>();
        
        for (int i = 1; i <= 3; i++) {
            AgentConfig config = new AgentConfig();
            config.setAgentId("batch-agent-" + i);
            config.setAgentName("Batch Agent " + i);
            configMap.put("agent/batch-agent-" + i + "/config", config);
        }
        
        boolean saved = storage.saveBatch(configMap);
        assertTrue(saved);
        
        // 批量加载
        List<String> keys = Arrays.asList(
            "agent/batch-agent-1/config",
            "agent/batch-agent-2/config",
            "agent/batch-agent-3/config"
        );
        
        Map<String, AgentConfig> loadedMap = storage.loadBatch(keys, AgentConfig.class);
        assertEquals(3, loadedMap.size());
        
        for (int i = 1; i <= 3; i++) {
            AgentConfig config = loadedMap.get("agent/batch-agent-" + i + "/config");
            assertNotNull(config);
            assertEquals("batch-agent-" + i, config.getAgentId());
        }
        
        // 批量删除
        boolean deleted = storage.deleteBatch(keys);
        assertTrue(deleted);
        
        for (String key : keys) {
            assertFalse(storage.exists(key));
        }
    }
    
    @Test
    public void testAsyncOperations() throws Exception {
        // 创建测试对象
        AgentConfig config = new AgentConfig();
        config.setAgentId("async-agent-789");
        config.setAgentName("Async Agent");
        
        // 异步保存
        CompletableFuture<Boolean> saveFuture = storage.saveAsync("agent/async-agent-789/config", config);
        Boolean saved = saveFuture.get(5, TimeUnit.SECONDS);
        assertTrue(saved);
        
        // 异步加载
        CompletableFuture<AgentConfig> loadFuture = storage.loadAsync("agent/async-agent-789/config", AgentConfig.class);
        AgentConfig loadedConfig = loadFuture.get(5, TimeUnit.SECONDS);
        assertNotNull(loadedConfig);
        assertEquals("async-agent-789", loadedConfig.getAgentId());
    }
    
    @Test
    public void testTransaction() {
        // 开始事务
        StorageService.Transaction tx = storage.beginTransaction();
        
        // 在事务中保存数据
        AgentConfig config1 = new AgentConfig();
        config1.setAgentId("tx-agent-1");
        config1.setAgentName("Transaction Agent 1");
        
        AgentConfig config2 = new AgentConfig();
        config2.setAgentId("tx-agent-2");
        config2.setAgentName("Transaction Agent 2");
        
        tx.save("agent/tx-agent-1/config", config1);
        tx.save("agent/tx-agent-2/config", config2);
        
        // 在事务中加载数据
        AgentConfig loadedConfig1 = tx.load("agent/tx-agent-1/config", AgentConfig.class);
        AgentConfig loadedConfig2 = tx.load("agent/tx-agent-2/config", AgentConfig.class);
        
        assertNotNull(loadedConfig1);
        assertNotNull(loadedConfig2);
        assertEquals("tx-agent-1", loadedConfig1.getAgentId());
        assertEquals("tx-agent-2", loadedConfig2.getAgentId());
        
        // 验证数据还未持久化到文件系统
        assertFalse(storage.exists("agent/tx-agent-1/config"));
        assertFalse(storage.exists("agent/tx-agent-2/config"));
        
        // 提交事务
        boolean committed = tx.commit();
        assertTrue(committed);
        
        // 验证数据已持久化
        assertTrue(storage.exists("agent/tx-agent-1/config"));
        assertTrue(storage.exists("agent/tx-agent-2/config"));
        
        // 重新加载验证
        AgentConfig finalConfig1 = storage.load("agent/tx-agent-1/config", AgentConfig.class);
        AgentConfig finalConfig2 = storage.load("agent/tx-agent-2/config", AgentConfig.class);
        
        assertNotNull(finalConfig1);
        assertNotNull(finalConfig2);
        assertEquals("tx-agent-1", finalConfig1.getAgentId());
        assertEquals("tx-agent-2", finalConfig2.getAgentId());
    }
    
    @Test
    public void testTransactionRollback() {
        // 开始事务
        StorageService.Transaction tx = storage.beginTransaction();
        
        // 在事务中保存数据
        AgentConfig config = new AgentConfig();
        config.setAgentId("rollback-agent-123");
        config.setAgentName("Rollback Agent");
        
        tx.save("agent/rollback-agent-123/config", config);
        
        // 在事务中验证数据存在
        assertNotNull(tx.load("agent/rollback-agent-123/config", AgentConfig.class));
        
        // 回滚事务
        boolean rolledBack = tx.rollback();
        assertTrue(rolledBack);
        
        // 验证数据未持久化
        assertFalse(storage.exists("agent/rollback-agent-123/config"));
    }
}
