package net.ooder.sdk.storage;

import net.ooder.sdk.storage.impl.SwitchableJsonStorage;

/**
 * 存储管理器类，采用单例模式管理存储服务的实例和配置
 */
public class StorageManager {
    // 单例实例
    private static final StorageManager instance = new StorageManager();
    
    private JsonStorage jsonStorage;
    private StorageConfig config;
    
    // 私有构造函数，防止外部实例化
    private StorageManager() {
        this.config = new StorageConfig();
        this.jsonStorage = new SwitchableJsonStorage(config);
    }
    
    /**
     * 获取存储管理器实例
     * @return 存储管理器单例实例
     */
    public static StorageManager getInstance() {
        return instance;
    }
    
    /**
     * 获取JSON存储服务
     * @return JSON存储服务实例
     */
    public JsonStorage getJsonStorage() {
        return jsonStorage;
    }
    
    /**
     * 设置JSON存储服务实现
     * @param jsonStorage JSON存储服务实例
     */
    public void setJsonStorage(JsonStorage jsonStorage) {
        this.jsonStorage = jsonStorage;
    }
    
    /**
     * 获取存储配置
     * @return 存储配置实例
     */
    public StorageConfig getConfig() {
        return config;
    }
    
    /**
     * 配置存储服务
     * @param config 存储配置实例
     */
    public void configure(StorageConfig config) {
        this.config = config;
        this.jsonStorage = new SwitchableJsonStorage(config);
    }
    
    /**
     * 配置存储服务
     * @param rootDirectory 根目录路径
     */
    public void configure(String rootDirectory) {
        this.config = new StorageConfig(rootDirectory);
        this.jsonStorage = new SwitchableJsonStorage(config);
    }
    
    /**
     * 关闭存储服务，释放资源
     */
    public void shutdown() {
        // 如果存储服务实现了Closeable接口，调用close方法
        if (jsonStorage instanceof AutoCloseable) {
            try {
                ((AutoCloseable) jsonStorage).close();
            } catch (Exception e) {
                // 日志记录
                System.err.println("Failed to close storage service: " + e.getMessage());
            }
        }
    }
    
    /**
     * 检查VFS是否可用
     * @return VFS是否可用
     */
    public boolean isVfsAvailable() {
        if (jsonStorage instanceof SwitchableJsonStorage) {
            return ((SwitchableJsonStorage) jsonStorage).isVfsAvailable();
        }
        return false;
    }
    
    /**
     * 手动检查VFS可用性
     * @return VFS是否可用
     */
    public boolean checkVfsNow() {
        if (jsonStorage instanceof SwitchableJsonStorage) {
            return ((SwitchableJsonStorage) jsonStorage).checkVfsNow();
        }
        return false;
    }
}