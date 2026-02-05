package net.ooder.skillcenter.manager;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 存储管理器，负责存储相关的管理功能
 */
public class StorageManager {
    // 单例实例
    private static StorageManager instance;
    
    // 存储项目映射，key为存储ID，value为存储项目
    private Map<String, StorageItem> storageItems;

    /**
     * 私有构造方法
     */
    private StorageManager() {
        this.storageItems = new HashMap<>();
        this.initializeStorageItems();
    }

    /**
     * 获取实例
     * @return 存储管理器实例
     */
    public static synchronized StorageManager getInstance() {
        if (instance == null) {
            instance = new StorageManager();
        }
        return instance;
    }

    /**
     * 初始化存储项目
     */
    private void initializeStorageItems() {
        // 创建示例存储项目数据
        addStorageItem(createStorageItem("json-001", "技能元数据存储", "json", "./skill_metadata.json", "2.5 MB", LocalDateTime.now().minusDays(1)));
        addStorageItem(createStorageItem("json-002", "用户配置存储", "json", "./user_config.json", "1.2 MB", LocalDateTime.now().minusDays(2)));
        addStorageItem(createStorageItem("json-003", "执行历史存储", "json", "./execution_history.json", "5.8 MB", LocalDateTime.now().minusDays(3)));
        addStorageItem(createStorageItem("vfs-001", "技能代码存储", "vfs", "./vfs/skills", "1.5 GB", LocalDateTime.now().minusDays(4)));
        addStorageItem(createStorageItem("vfs-002", "资源文件存储", "vfs", "./vfs/resources", "3.2 GB", LocalDateTime.now().minusDays(5)));

        System.out.println("Loaded " + storageItems.size() + " sample storage items");
    }

    /**
     * 创建存储项目
     * @param id 存储ID
     * @param name 存储名称
     * @param type 存储类型
     * @param path 存储路径
     * @param size 存储大小
     * @param lastModified 最后修改时间
     * @return 存储项目
     */
    private StorageItem createStorageItem(String id, String name, String type, String path, String size, LocalDateTime lastModified) {
        StorageItem item = new StorageItem();
        item.setId(id);
        item.setName(name);
        item.setType(type);
        item.setPath(path);
        item.setSize(size);
        item.setLastModified(lastModified);
        return item;
    }

    /**
     * 添加存储项目
     * @param item 存储项目
     */
    private void addStorageItem(StorageItem item) {
        storageItems.put(item.getId(), item);
    }

    /**
     * 创建新的存储项目
     * @param item 存储项目
     * @return 创建的存储项目
     */
    public synchronized StorageItem createStorageItem(StorageItem item) {
        if (item == null) {
            throw new IllegalArgumentException("Storage item cannot be null");
        }

        // 生成存储ID
        if (item.getId() == null || item.getId().isEmpty()) {
            item.setId(item.getType() + "-" + UUID.randomUUID().toString().substring(0, 8));
        }

        // 设置最后修改时间
        if (item.getLastModified() == null) {
            item.setLastModified(LocalDateTime.now());
        }

        // 设置初始大小
        if (item.getSize() == null) {
            item.setSize("0 MB");
        }

        // 添加到存储项目映射
        storageItems.put(item.getId(), item);
        return item;
    }

    /**
     * 获取指定ID的存储项目
     * @param id 存储ID
     * @return 存储项目，不存在则返回null
     */
    public StorageItem getStorageItem(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Storage item ID cannot be null or empty");
        }
        return storageItems.get(id);
    }

    /**
     * 获取所有存储项目
     * @return 存储项目列表
     */
    public List<StorageItem> getAllStorageItems() {
        return new ArrayList<>(storageItems.values());
    }

    /**
     * 根据类型获取存储项目
     * @param type 存储类型
     * @return 存储项目列表
     */
    public List<StorageItem> getStorageItemsByType(String type) {
        if (type == null || type.isEmpty()) {
            return getAllStorageItems();
        }

        List<StorageItem> result = new ArrayList<>();
        for (StorageItem item : storageItems.values()) {
            if (type.equals(item.getType())) {
                result.add(item);
            }
        }
        return result;
    }

    /**
     * 删除存储项目
     * @param id 存储ID
     * @return 删除是否成功
     */
    public synchronized boolean deleteStorageItem(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Storage item ID cannot be null or empty");
        }

        return storageItems.remove(id) != null;
    }

    /**
     * 获取存储统计信息
     * @return 统计信息
     */
    public Map<String, Object> getStorageStats() {
        Map<String, Object> stats = new HashMap<>();

        // 模拟存储统计数据
        stats.put("totalStorage", "12.5 GB");
        stats.put("usedStorage", "8.2 GB");
        stats.put("availableStorage", "4.3 GB");
        stats.put("usagePercentage", 65.6);

        return stats;
    }

    /**
     * 存储项目类
     */
    public static class StorageItem {
        private String id;
        private String name;
        private String type;
        private String path;
        private String size;
        private LocalDateTime lastModified;

        // getters and setters
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public LocalDateTime getLastModified() {
            return lastModified;
        }

        public void setLastModified(LocalDateTime lastModified) {
            this.lastModified = lastModified;
        }
    }
}
