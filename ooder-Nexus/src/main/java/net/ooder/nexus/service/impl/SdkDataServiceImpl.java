package net.ooder.nexus.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import net.ooder.nexus.common.utils.JsonUtils;
import net.ooder.nexus.model.Result;
import net.ooder.nexus.service.SdkDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * SDK数据服务实现类
 * 数据存储在 ./storage/sdk/ 目录下的JSON文件中
 * 每个集合对应一个子目录，每个数据项对应一个JSON文件
 */
@Service
public class SdkDataServiceImpl implements SdkDataService {

    private static final Logger log = LoggerFactory.getLogger(SdkDataServiceImpl.class);
    private static final String BASE_PATH = "./storage/sdk";
    private static final String FILE_EXTENSION = ".json";

    @PostConstruct
    public void init() {
        try {
            Path basePath = Paths.get(BASE_PATH);
            if (!Files.exists(basePath)) {
                Files.createDirectories(basePath);
                log.info("SDK数据存储目录已创建: {}", basePath.toAbsolutePath());
            }
        } catch (IOException e) {
            log.error("初始化SDK数据存储目录失败", e);
        }
    }

    @Override
    public Result<Map<String, Object>> save(String collection, String id, Map<String, Object> data) {
        try {
            Path collectionPath = getCollectionPath(collection);
            if (!Files.exists(collectionPath)) {
                Files.createDirectories(collectionPath);
            }

            // 确保数据包含ID
            data.put("id", id);
            data.put("_updatedAt", System.currentTimeMillis());
            if (!data.containsKey("_createdAt")) {
                data.put("_createdAt", System.currentTimeMillis());
            }

            Path filePath = collectionPath.resolve(id + FILE_EXTENSION);
            String json = JsonUtils.toJsonPretty(data);
            Files.write(filePath, json.getBytes(StandardCharsets.UTF_8));

            log.info("数据已保存: {}/{}", collection, id);
            return Result.success("数据保存成功", data);
        } catch (Exception e) {
            log.error("保存数据失败: {}/{}", collection, id, e);
            return Result.error("保存数据失败: " + e.getMessage());
        }
    }

    @Override
    public Result<Map<String, Object>> saveBatch(String collection, List<Map<String, Object>> dataList) {
        try {
            List<Map<String, Object>> savedList = new ArrayList<>();
            for (Map<String, Object> data : dataList) {
                String id = (String) data.get("id");
                if (id == null || id.isEmpty()) {
                    id = UUID.randomUUID().toString();
                    data.put("id", id);
                }
                Result<Map<String, Object>> result = save(collection, id, data);
                if (result.getCode() == 200) {
                    savedList.add(result.getData());
                }
            }

            Map<String, Object> result = new HashMap<>();
            result.put("savedCount", savedList.size());
            result.put("totalCount", dataList.size());
            result.put("data", savedList);

            return Result.success("批量保存成功", result);
        } catch (Exception e) {
            log.error("批量保存数据失败: {}", collection, e);
            return Result.error("批量保存数据失败: " + e.getMessage());
        }
    }

    @Override
    public Result<Map<String, Object>> getById(String collection, String id) {
        try {
            Path filePath = getCollectionPath(collection).resolve(id + FILE_EXTENSION);
            if (!Files.exists(filePath)) {
                return Result.error("数据不存在: " + id);
            }

            String json = new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8);
            Map<String, Object> data = JsonUtils.fromJson(json, new TypeReference<Map<String, Object>>() {});

            return Result.success("数据获取成功", data);
        } catch (Exception e) {
            log.error("获取数据失败: {}/{}", collection, id, e);
            return Result.error("获取数据失败: " + e.getMessage());
        }
    }

    @Override
    public Result<List<Map<String, Object>>> getAll(String collection) {
        try {
            Path collectionPath = getCollectionPath(collection);
            if (!Files.exists(collectionPath)) {
                return Result.success("集合为空", new ArrayList<>());
            }

            List<Map<String, Object>> dataList = new ArrayList<>();
            try (Stream<Path> paths = Files.list(collectionPath)) {
                paths.filter(Files::isRegularFile)
                     .filter(p -> p.toString().endsWith(FILE_EXTENSION))
                     .forEach(p -> {
                         try {
                             String json = new String(Files.readAllBytes(p), StandardCharsets.UTF_8);
                             Map<String, Object> data = JsonUtils.fromJson(json, new TypeReference<Map<String, Object>>() {});
                             dataList.add(data);
                         } catch (Exception e) {
                             log.warn("读取文件失败: {}", p, e);
                         }
                     });
            }

            // 按更新时间排序
            dataList.sort((a, b) -> {
                Long timeA = (Long) a.getOrDefault("_updatedAt", 0L);
                Long timeB = (Long) b.getOrDefault("_updatedAt", 0L);
                return timeB.compareTo(timeA);
            });

            return Result.success("数据列表获取成功", dataList);
        } catch (Exception e) {
            log.error("获取数据列表失败: {}", collection, e);
            return Result.error("获取数据列表失败: " + e.getMessage());
        }
    }

    @Override
    public Result<List<Map<String, Object>>> query(String collection, Map<String, Object> conditions) {
        Result<List<Map<String, Object>>> allResult = getAll(collection);
        if (allResult.getCode() != 200) {
            return allResult;
        }

        List<Map<String, Object>> dataList = allResult.getData();
        if (conditions == null || conditions.isEmpty()) {
            return Result.success("查询成功", dataList);
        }

        // 过滤数据
        List<Map<String, Object>> filteredList = dataList.stream()
            .filter(data -> matchesConditions(data, conditions))
            .collect(Collectors.toList());

        return Result.success("查询成功", filteredList);
    }

    @Override
    public Result<Map<String, Object>> update(String collection, String id, Map<String, Object> data) {
        Result<Map<String, Object>> existingResult = getById(collection, id);
        if (existingResult.getCode() != 200) {
            return Result.error("数据不存在: " + id);
        }

        Map<String, Object> existingData = existingResult.getData();
        existingData.putAll(data);
        existingData.put("id", id); // 确保ID不变

        return save(collection, id, existingData);
    }

    @Override
    public Result<Void> delete(String collection, String id) {
        try {
            Path filePath = getCollectionPath(collection).resolve(id + FILE_EXTENSION);
            if (!Files.exists(filePath)) {
                return Result.error("数据不存在: " + id);
            }

            Files.delete(filePath);
            log.info("数据已删除: {}/{}", collection, id);
            return Result.success("数据删除成功", null);
        } catch (Exception e) {
            log.error("删除数据失败: {}/{}", collection, id, e);
            return Result.error("删除数据失败: " + e.getMessage());
        }
    }

    @Override
    public Result<Void> deleteAll(String collection) {
        try {
            Path collectionPath = getCollectionPath(collection);
            if (!Files.exists(collectionPath)) {
                return Result.success("集合为空", null);
            }

            try (Stream<Path> paths = Files.list(collectionPath)) {
                paths.filter(Files::isRegularFile)
                     .filter(p -> p.toString().endsWith(FILE_EXTENSION))
                     .forEach(p -> {
                         try {
                             Files.delete(p);
                         } catch (Exception e) {
                             log.warn("删除文件失败: {}", p, e);
                         }
                     });
            }

            log.info("集合已清空: {}", collection);
            return Result.success("集合已清空", null);
        } catch (Exception e) {
            log.error("清空集合失败: {}", collection, e);
            return Result.error("清空集合失败: " + e.getMessage());
        }
    }

    @Override
    public Result<List<String>> getCollections() {
        try {
            Path basePath = Paths.get(BASE_PATH);
            if (!Files.exists(basePath)) {
                return Result.success("暂无集合", new ArrayList<>());
            }

            List<String> collections = new ArrayList<>();
            try (Stream<Path> paths = Files.list(basePath)) {
                paths.filter(Files::isDirectory)
                     .forEach(p -> collections.add(p.getFileName().toString()));
            }

            return Result.success("集合列表获取成功", collections);
        } catch (Exception e) {
            log.error("获取集合列表失败", e);
            return Result.error("获取集合列表失败: " + e.getMessage());
        }
    }

    @Override
    public Result<Boolean> exists(String collection, String id) {
        Path filePath = getCollectionPath(collection).resolve(id + FILE_EXTENSION);
        return Result.success("检查完成", Files.exists(filePath));
    }

    @Override
    public Result<Long> count(String collection) {
        try {
            Path collectionPath = getCollectionPath(collection);
            if (!Files.exists(collectionPath)) {
                return Result.success("集合为空", 0L);
            }

            long count;
            try (Stream<Path> paths = Files.list(collectionPath)) {
                count = paths.filter(Files::isRegularFile)
                            .filter(p -> p.toString().endsWith(FILE_EXTENSION))
                            .count();
            }

            return Result.success("统计成功", count);
        } catch (Exception e) {
            log.error("统计数据数量失败: {}", collection, e);
            return Result.error("统计数据数量失败: " + e.getMessage());
        }
    }

    /**
     * 获取集合路径
     */
    private Path getCollectionPath(String collection) {
        return Paths.get(BASE_PATH, collection);
    }

    /**
     * 检查数据是否匹配查询条件
     */
    private boolean matchesConditions(Map<String, Object> data, Map<String, Object> conditions) {
        for (Map.Entry<String, Object> entry : conditions.entrySet()) {
            String key = entry.getKey();
            Object expectedValue = entry.getValue();
            Object actualValue = data.get(key);

            if (actualValue == null) {
                return false;
            }

            if (!String.valueOf(actualValue).equalsIgnoreCase(String.valueOf(expectedValue))) {
                return false;
            }
        }
        return true;
    }
}
