package net.ooder.skillcenter.storage;

import java.util.Map;
import java.util.List;

/**
 * 统一存储服务接口，定义存储操作的标准方法
 * 支持不同的存储实现，如JSON文件存储和VFS存储
 */
public interface StorageService {
    
    /**
     * 初始化存储服务
     */
    void initialize();
    
    /**
     * 关闭存储服务
     */
    void close();
    
    /**
     * 保存数据
     * @param key 存储键
     * @param data 要存储的数据
     */
    void save(String key, Object data);
    
    /**
     * 批量保存数据
     * @param dataMap 键值对映射
     */
    void saveAll(Map<String, Object> dataMap);
    
    /**
     * 加载数据
     * @param key 存储键
     * @param clazz 数据类型
     * @param <T> 泛型类型
     * @return 加载的数据
     */
    <T> T load(String key, Class<T> clazz);
    
    /**
     * 批量加载数据
     * @param keys 存储键列表
     * @param clazz 数据类型
     * @param <T> 泛型类型
     * @return 键值对映射
     */
    <T> Map<String, T> loadAll(List<String> keys, Class<T> clazz);
    
    /**
     * 删除数据
     * @param key 存储键
     */
    void delete(String key);
    
    /**
     * 批量删除数据
     * @param keys 存储键列表
     */
    void deleteAll(List<String> keys);
    
    /**
     * 检查数据是否存在
     * @param key 存储键
     * @return 是否存在
     */
    boolean exists(String key);
    
    /**
     * 获取所有存储键
     * @return 存储键列表
     */
    List<String> getAllKeys();
    
    /**
     * 获取存储服务名称
     * @return 存储服务名称
     */
    String getName();
    
    /**
     * 获取存储服务状态
     * @return 存储服务状态
     */
    StorageStatus getStatus();
}
