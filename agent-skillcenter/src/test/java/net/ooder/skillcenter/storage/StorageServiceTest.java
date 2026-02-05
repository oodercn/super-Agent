package net.ooder.skillcenter.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 存储服务测试类
 * 测试JSON和VFS存储服务的功能
 */
public class StorageServiceTest {
    
    private StorageManager storageManager;
    private StorageService vfsStorage;
    private StorageService jsonStorage;
    
    @BeforeEach
    public void setUp() {
        // 获取存储管理器实例
        storageManager = StorageManager.getInstance();
        // 初始化所有存储服务
        storageManager.initializeAll();
        // 获取VFS存储服务
        vfsStorage = storageManager.getStorageService("vfs");
        // 获取JSON存储服务
        jsonStorage = storageManager.getStorageService("json");
    }
    
    @Test
    public void testVFSStorageService() {
        // 测试VFS存储服务状态
        assertEquals(StorageStatus.RUNNING, vfsStorage.getStatus());
        
        // 测试保存和加载数据
        String key = "test:vfs:data";
        TestData testData = new TestData("VFS Test", 123, true);
        
        // 保存数据
        vfsStorage.save(key, testData);
        
        // 加载数据
        TestData loadedData = vfsStorage.load(key, TestData.class);
        assertNotNull(loadedData);
        assertEquals(testData.getName(), loadedData.getName());
        assertEquals(testData.getValue(), loadedData.getValue());
        assertEquals(testData.isActive(), loadedData.isActive());
        
        // 测试数据存在性
        assertTrue(vfsStorage.exists(key));
        
        // 测试删除数据
        vfsStorage.delete(key);
        assertFalse(vfsStorage.exists(key));
    }
    
    @Test
    public void testJsonStorageService() {
        // 测试JSON存储服务状态
        assertEquals(StorageStatus.RUNNING, jsonStorage.getStatus());
        
        // 测试保存和加载数据
        String key = "test:json:data";
        TestData testData = new TestData("JSON Test", 456, false);
        
        // 保存数据
        jsonStorage.save(key, testData);
        
        // 加载数据
        TestData loadedData = jsonStorage.load(key, TestData.class);
        assertNotNull(loadedData);
        assertEquals(testData.getName(), loadedData.getName());
        assertEquals(testData.getValue(), loadedData.getValue());
        assertEquals(testData.isActive(), loadedData.isActive());
        
        // 测试数据存在性
        assertTrue(jsonStorage.exists(key));
        
        // 测试删除数据
        jsonStorage.delete(key);
        assertFalse(jsonStorage.exists(key));
    }
    
    @Test
    public void testBatchOperations() {
        // 测试批量保存和加载
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("batch:test:1", new TestData("Batch Test 1", 100, true));
        dataMap.put("batch:test:2", new TestData("Batch Test 2", 200, false));
        dataMap.put("batch:test:3", new TestData("Batch Test 3", 300, true));
        
        // 批量保存
        vfsStorage.saveAll(dataMap);
        
        // 测试批量加载
        List<String> keys = Arrays.asList("batch:test:1", "batch:test:2", "batch:test:3");
        Map<String, TestData> loadedMap = vfsStorage.loadAll(keys, TestData.class);
        assertEquals(3, loadedMap.size());
        
        // 测试批量删除
        vfsStorage.deleteAll(keys);
        for (String key : keys) {
            assertFalse(vfsStorage.exists(key));
        }
    }
    
    @Test
    public void testDefaultStorage() {
        // 测试默认存储服务
        StorageService defaultStorage = storageManager.getDefaultStorage();
        assertNotNull(defaultStorage);
        assertEquals(StorageStatus.RUNNING, defaultStorage.getStatus());
        
        // 测试默认存储类型
        assertEquals(StorageManager.StorageType.VFS, storageManager.getDefaultStorageType());
    }
    
    @Test
    public void testStorageTypeSwitching() {
        // 测试切换默认存储类型
        storageManager.setDefaultStorage(StorageManager.StorageType.JSON);
        assertEquals(StorageManager.StorageType.JSON, storageManager.getDefaultStorageType());
        
        // 切换回VFS
        storageManager.setDefaultStorage(StorageManager.StorageType.VFS);
        assertEquals(StorageManager.StorageType.VFS, storageManager.getDefaultStorageType());
    }
    
    /**
     * 测试数据类
     */
    private static class TestData {
        private String name;
        private int value;
        private boolean active;
        
        public TestData() {
        }
        
        public TestData(String name, int value, boolean active) {
            this.name = name;
            this.value = value;
            this.active = active;
        }
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public int getValue() {
            return value;
        }
        
        public void setValue(int value) {
            this.value = value;
        }
        
        public boolean isActive() {
            return active;
        }
        
        public void setActive(boolean active) {
            this.active = active;
        }
    }
}
