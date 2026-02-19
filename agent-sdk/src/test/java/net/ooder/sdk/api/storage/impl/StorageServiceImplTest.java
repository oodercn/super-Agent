package net.ooder.sdk.api.storage.impl;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class StorageServiceImplTest {
    
    private StorageServiceImpl storage;
    private String testPath = "./target/test-storage-api";
    
    @Before
    public void setUp() {
        storage = new StorageServiceImpl(testPath);
    }
    
    @After
    public void tearDown() {
        if (storage != null) {
            storage.shutdown();
        }
        deleteDirectory(new File(testPath));
    }
    
    private void deleteDirectory(File dir) {
        if (dir.exists()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        file.delete();
                    }
                }
            }
            dir.delete();
        }
    }
    
    @Test
    public void testInitialization() {
        assertNotNull(storage);
    }
    
    @Test
    public void testSave() {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("name", "test");
        data.put("value", 123);
        
        String result = storage.save("test-key", data);
        
        assertNotNull(result);
        assertEquals("test-key", result);
    }
    
    @Test
    public void testLoad() {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("name", "test");
        data.put("value", 123);
        
        storage.save("test-key", data);
        
        Optional<Map> loaded = storage.load("test-key", Map.class);
        
        assertTrue(loaded.isPresent());
        assertEquals("test", loaded.get().get("name"));
        assertEquals(123, loaded.get().get("value"));
    }
    
    @Test
    public void testLoadNotExists() {
        Optional<Map> loaded = storage.load("non-existent", Map.class);
        
        assertFalse(loaded.isPresent());
    }
    
    @Test
    public void testDelete() {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("name", "test");
        
        storage.save("delete-key", data);
        
        boolean deleted = storage.delete("delete-key");
        
        assertTrue(deleted);
        assertFalse(storage.load("delete-key", Map.class).isPresent());
    }
    
    @Test
    public void testDeleteNotExists() {
        boolean deleted = storage.delete("non-existent");
        
        assertFalse(deleted);
    }
    
    @Test
    public void testExists() {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("name", "test");
        
        storage.save("exists-key", data);
        
        assertTrue(storage.exists("exists-key"));
        assertFalse(storage.exists("non-existent"));
    }
    
    @Test
    public void testSaveString() {
        storage.save("string-key", "test-value");
        
        Optional<String> loaded = storage.load("string-key", String.class);
        
        assertTrue(loaded.isPresent());
        assertEquals("test-value", loaded.get());
    }
    
    @Test
    public void testSaveInteger() {
        storage.save("int-key", 12345);
        
        Optional<Integer> loaded = storage.load("int-key", Integer.class);
        
        assertTrue(loaded.isPresent());
        assertEquals(Integer.valueOf(12345), loaded.get());
    }
    
    @Test
    public void testUpdate() {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("name", "original");
        
        storage.save("update-key", data);
        
        data.put("name", "updated");
        storage.save("update-key", data);
        
        Optional<Map> loaded = storage.load("update-key", Map.class);
        
        assertTrue(loaded.isPresent());
        assertEquals("updated", loaded.get().get("name"));
    }
    
    @Test
    public void testClear() {
        storage.save("clear-1", "value1");
        storage.save("clear-2", "value2");
        
        storage.clear();
        
        assertFalse(storage.exists("clear-1"));
        assertFalse(storage.exists("clear-2"));
    }
    
    @Test
    public void testShutdown() {
        storage.save("shutdown-key", "value");
        
        storage.shutdown();
    }
}
