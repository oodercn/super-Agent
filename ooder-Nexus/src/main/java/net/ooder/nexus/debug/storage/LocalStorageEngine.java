package net.ooder.nexus.debug.storage;

import net.ooder.nexus.common.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 本地存储引擎 - 基于JSON文件的本地存储
 */
public class LocalStorageEngine {

    private static final Logger logger = LoggerFactory.getLogger(LocalStorageEngine.class);

    private final String basePath;
    private final Map<String, Object> memoryCache = new ConcurrentHashMap<>();

    public LocalStorageEngine() {
        this.basePath = "./storage";
        initializeStorage();
    }

    public LocalStorageEngine(String basePath) {
        this.basePath = basePath;
        initializeStorage();
    }

    private void initializeStorage() {
        try {
            // 创建存储目录结构
            Path storagePath = Paths.get(basePath);
            if (!Files.exists(storagePath)) {
                Files.createDirectories(storagePath);
            }

            // 创建子目录
            createSubDirectory("protocols");
            createSubDirectory("simulators");
            createSubDirectory("scenarios");
            createSubDirectory("results");

            System.out.println("Storage initialized at: " + basePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize storage", e);
        }
    }

    private void createSubDirectory(String name) throws IOException {
        Path subPath = Paths.get(basePath, name);
        if (!Files.exists(subPath)) {
            Files.createDirectories(subPath);
        }
    }

    /**
     * 保存对象到JSON文件
     */
    public <T> void save(String category, String fileName, T data) {
        try {
            String json = JsonUtils.toJsonPretty(data);
            Path filePath = Paths.get(basePath, category, fileName + ".json");
            Files.write(filePath, json.getBytes(StandardCharsets.UTF_8));

            // 更新内存缓存
            memoryCache.put(category + ":" + fileName, data);

            System.out.println("Saved " + category + "/" + fileName + ".json");
        } catch (IOException e) {
            throw new RuntimeException("Failed to save " + category + "/" + fileName, e);
        }
    }

    /**
     * 从JSON文件加载对象
     */
    @SuppressWarnings("unchecked")
    public <T> T load(String category, String fileName, Class<T> clazz) {
        try {
            // 先检查缓存
            String cacheKey = category + ":" + fileName;
            if (memoryCache.containsKey(cacheKey)) {
                return (T) memoryCache.get(cacheKey);
            }

            Path filePath = Paths.get(basePath, category, fileName + ".json");
            if (!Files.exists(filePath)) {
                return null;
            }

            String json = new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8);
            T data = JsonUtils.fromJson(json, clazz);

            // 放入缓存
            if (data != null) {
                memoryCache.put(cacheKey, data);
            }

            return data;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load " + category + "/" + fileName, e);
        }
    }

    /**
     * 获取目录下所有文件
     */
    public List<String> listFiles(String category) {
        try {
            Path categoryPath = Paths.get(basePath, category);
            if (!Files.exists(categoryPath)) {
                return new ArrayList<>();
            }

            List<String> files = new ArrayList<>();
            Files.walk(categoryPath)
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".json"))
                    .forEach(p -> {
                        String fileName = p.getFileName().toString();
                        files.add(fileName.replace(".json", ""));
                    });

            return files;
        } catch (IOException e) {
            throw new RuntimeException("Failed to list files in " + category, e);
        }
    }

    /**
     * 删除文件
     */
    public boolean delete(String category, String fileName) {
        try {
            Path filePath = Paths.get(basePath, category, fileName + ".json");
            boolean deleted = Files.deleteIfExists(filePath);

            // 从缓存中移除
            memoryCache.remove(category + ":" + fileName);

            return deleted;
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete " + category + "/" + fileName, e);
        }
    }

    /**
     * 清空缓存
     */
    public void clearCache() {
        memoryCache.clear();
    }

    /**
     * 获取存储路径
     */
    public String getBasePath() {
        return basePath;
    }

    /**
     * 存储索引文件
     */
    public <T> void saveIndex(String category, List<StorageIndex> indexes) {
        IndexFile indexFile = new IndexFile();
        indexFile.lastUpdated = System.currentTimeMillis();
        indexFile.items = indexes;

        save(category, "index", indexFile);
    }

    /**
     * 加载索引文件
     */
    @SuppressWarnings("unchecked")
    public List<StorageIndex> loadIndex(String category) {
        Path indexPath = Paths.get(basePath, category, "index.json");
        if (!Files.exists(indexPath)) {
            return new ArrayList<>();
        }

        try {
            String json = new String(Files.readAllBytes(indexPath), StandardCharsets.UTF_8);
            IndexFile indexFile = JsonUtils.fromJson(json, IndexFile.class);
            return indexFile != null ? indexFile.items : new ArrayList<>();
        } catch (IOException e) {
            logger.error("Failed to load index: {}", indexPath, e);
            return new ArrayList<>();
        }
    }

    /**
     * 索引文件结构
     */
    public static class IndexFile {
        public long lastUpdated;
        public List<StorageIndex> items;
    }

    /**
     * 存储索引
     */
    public static class StorageIndex {
        public String id;
        public String name;
        public String type;
        public long createdAt;
        public long updatedAt;
    }
}
