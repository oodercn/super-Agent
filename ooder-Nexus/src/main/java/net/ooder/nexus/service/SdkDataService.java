package net.ooder.nexus.service;

import net.ooder.nexus.model.Result;
import java.util.List;
import java.util.Map;

/**
 * SDK数据服务接口
 * 提供JSON数据的存储、读取、更新、删除操作
 * 数据存储在 ./storage/sdk/ 目录下
 */
public interface SdkDataService {

    /**
     * 保存数据到指定集合
     * @param collection 集合名称（对应子目录）
     * @param id 数据ID
     * @param data 数据对象
     * @return 操作结果
     */
    Result<Map<String, Object>> save(String collection, String id, Map<String, Object> data);

    /**
     * 批量保存数据
     * @param collection 集合名称
     * @param dataList 数据列表
     * @return 操作结果
     */
    Result<Map<String, Object>> saveBatch(String collection, List<Map<String, Object>> dataList);

    /**
     * 根据ID获取数据
     * @param collection 集合名称
     * @param id 数据ID
     * @return 数据对象
     */
    Result<Map<String, Object>> getById(String collection, String id);

    /**
     * 获取集合中的所有数据
     * @param collection 集合名称
     * @return 数据列表
     */
    Result<List<Map<String, Object>>> getAll(String collection);

    /**
     * 根据条件查询数据
     * @param collection 集合名称
     * @param conditions 查询条件
     * @return 符合条件的数据列表
     */
    Result<List<Map<String, Object>>> query(String collection, Map<String, Object> conditions);

    /**
     * 更新数据
     * @param collection 集合名称
     * @param id 数据ID
     * @param data 更新后的数据
     * @return 操作结果
     */
    Result<Map<String, Object>> update(String collection, String id, Map<String, Object> data);

    /**
     * 删除数据
     * @param collection 集合名称
     * @param id 数据ID
     * @return 操作结果
     */
    Result<Void> delete(String collection, String id);

    /**
     * 删除集合中的所有数据
     * @param collection 集合名称
     * @return 操作结果
     */
    Result<Void> deleteAll(String collection);

    /**
     * 获取集合列表
     * @return 集合名称列表
     */
    Result<List<String>> getCollections();

    /**
     * 检查数据是否存在
     * @param collection 集合名称
     * @param id 数据ID
     * @return 是否存在
     */
    Result<Boolean> exists(String collection, String id);

    /**
     * 获取集合中的数据数量
     * @param collection 集合名称
     * @return 数据数量
     */
    Result<Long> count(String collection);
}
