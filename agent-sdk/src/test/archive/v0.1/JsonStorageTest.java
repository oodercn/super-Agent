package net.ooder.sdk.service.storage.persistence;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.*;

public class JsonStorageTest {

    private JsonStorage storage;
    private String testBasePath;

    @Before
    public void setUp() throws IOException {
        testBasePath = System.getProperty("java.io.tmpdir") + File.separator + "jsonStorageTest_" + System.currentTimeMillis();
        storage = new JsonStorage(testBasePath);
    }

    @After
    public void tearDown() {
        deleteDirectory(new File(testBasePath));
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
    public void testSaveAndLoad() throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("name", "test");
        data.put("value", 123);

        storage.save("testKey", data);

        Map<String, Object> loaded = storage.load("testKey");
        assertNotNull(loaded);
        assertEquals("test", loaded.get("name"));
        assertEquals(123L, loaded.get("value"));
    }

    @Test
    public void testLoadNonExistent() throws IOException {
        Map<String, Object> loaded = storage.load("nonExistent");
        assertNull(loaded);
    }

    @Test
    public void testExists() throws IOException {
        assertFalse(storage.exists("testKey"));

        Map<String, Object> data = new HashMap<>();
        data.put("name", "test");
        storage.save("testKey", data);

        assertTrue(storage.exists("testKey"));
    }

    @Test
    public void testDelete() throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("name", "test");
        storage.save("testKey", data);

        assertTrue(storage.exists("testKey"));

        storage.delete("testKey");

        assertFalse(storage.exists("testKey"));
    }

    @Test
    public void testListKeys() throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("name", "test");

        storage.save("key1", data);
        storage.save("key2", data);
        storage.save("key3", data);

        List<String> keys = storage.listKeys();
        assertEquals(3, keys.size());
        assertTrue(keys.contains("key1"));
        assertTrue(keys.contains("key2"));
        assertTrue(keys.contains("key3"));
    }

    @Test
    public void testSaveList() throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();

        Map<String, Object> item1 = new HashMap<>();
        item1.put("id", 1);
        item1.put("name", "item1");
        list.add(item1);

        Map<String, Object> item2 = new HashMap<>();
        item2.put("id", 2);
        item2.put("name", "item2");
        list.add(item2);

        storage.saveList("myList", list);

        List<Map<String, Object>> loaded = storage.loadList("myList");
        assertEquals(2, loaded.size());
        assertEquals(1L, loaded.get(0).get("id"));
        assertEquals("item1", loaded.get(0).get("name"));
    }

    @Test
    public void testLoadEmptyList() throws IOException {
        List<Map<String, Object>> loaded = storage.loadList("nonExistentList");
        assertNotNull(loaded);
        assertTrue(loaded.isEmpty());
    }

    @Test
    public void testSaveWithSpecialCharacters() throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("message", "Hello\nWorld\t!");

        storage.save("special", data);

        Map<String, Object> loaded = storage.load("special");
        assertEquals("Hello\nWorld\t!", loaded.get("message"));
    }

    @Test
    public void testSaveNestedObject() throws IOException {
        Map<String, Object> nested = new HashMap<>();
        nested.put("inner", "value");

        Map<String, Object> data = new HashMap<>();
        data.put("outer", nested);

        storage.save("nested", data);

        Map<String, Object> loaded = storage.load("nested");
        assertNotNull(loaded.get("outer"));
        assertTrue(loaded.get("outer") instanceof Map);
        @SuppressWarnings("unchecked")
        Map<String, Object> inner = (Map<String, Object>) loaded.get("outer");
        assertEquals("value", inner.get("inner"));
    }

    @Test
    public void testSanitizeKey() throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("value", "test");

        storage.save("key with spaces", data);
        storage.save("key/with/slashes", data);
        storage.save("key.with.dots", data);

        assertTrue(storage.exists("key_with_spaces"));
        assertTrue(storage.exists("key_with_slashes"));
        assertTrue(storage.exists("key_with_dots"));
    }

    @Test
    public void testSaveNullValue() throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("nullValue", null);

        storage.save("nullTest", data);

        Map<String, Object> loaded = storage.load("nullTest");
        assertNull(loaded.get("nullValue"));
    }

    @Test
    public void testSaveBooleanValues() throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("trueValue", true);
        data.put("falseValue", false);

        storage.save("boolTest", data);

        Map<String, Object> loaded = storage.load("boolTest");
        assertEquals(true, loaded.get("trueValue"));
        assertEquals(false, loaded.get("falseValue"));
    }

    @Test
    public void testSaveNumericValues() throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("integer", 42);
        data.put("negative", -10);
        data.put("decimal", 3.14);

        storage.save("numberTest", data);

        Map<String, Object> loaded = storage.load("numberTest");
        assertEquals(42L, loaded.get("integer"));
        assertEquals(-10L, loaded.get("negative"));
        assertEquals(3.14, loaded.get("decimal"));
    }
}
