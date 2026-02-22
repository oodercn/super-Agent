package net.ooder.nexus.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.ooder.nexus.domain.task.model.ListDataExtractTask;
import net.ooder.nexus.service.ListDataExtractTaskService;
import net.ooder.nexus.service.SdkDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 列表数据抽取任务服务实现类
 */
@Service
public class ListDataExtractTaskServiceImpl implements ListDataExtractTaskService {

    private static final Logger log = LoggerFactory.getLogger(ListDataExtractTaskServiceImpl.class);
    private static final String DATA_DIR = "./storage/tasks";
    private static final String TASKS_FILE = "list-extract-tasks.json";

    private final Map<String, ListDataExtractTask> taskCache = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;
    private final Path storagePath;
    private final RestTemplate restTemplate;

    @Autowired
    private SdkDataService sdkDataService;

    public ListDataExtractTaskServiceImpl() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        this.storagePath = Paths.get(DATA_DIR, TASKS_FILE);
        this.restTemplate = new RestTemplate();
    }

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(Paths.get(DATA_DIR));
            loadTasks();
            log.info("列表数据抽取任务服务初始化完成，共加载 {} 个任务", taskCache.size());
        } catch (IOException e) {
            log.error("初始化列表数据抽取任务服务失败", e);
        }
    }

    /**
     * 从文件加载任务
     */
    private void loadTasks() {
        if (!Files.exists(storagePath)) {
            return;
        }

        try {
            String json = new String(Files.readAllBytes(storagePath), StandardCharsets.UTF_8);
            List<ListDataExtractTask> tasks = objectMapper.readValue(json, new TypeReference<List<ListDataExtractTask>>() {});
            tasks.forEach(task -> taskCache.put(task.getId(), task));
        } catch (IOException e) {
            log.error("加载任务数据失败", e);
        }
    }

    /**
     * 保存任务到文件
     */
    private void saveTasks() {
        try {
            List<ListDataExtractTask> tasks = new ArrayList<>(taskCache.values());
            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(tasks);
            Files.write(storagePath, json.getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            log.error("保存任务数据失败", e);
        }
    }

    @Override
    public ListDataExtractTask createTask(ListDataExtractTask task) {
        if (task.getId() == null || task.getId().isEmpty()) {
            task.setId(UUID.randomUUID().toString());
        }

        task.setCreateTime(LocalDateTime.now());
        task.setUpdateTime(LocalDateTime.now());
        task.setStatus(ListDataExtractTask.TaskStatus.PENDING);

        taskCache.put(task.getId(), task);
        saveTasks();

        log.info("创建列表数据抽取任务: {}", task.getName());
        return task;
    }

    @Override
    public ListDataExtractTask updateTask(String id, ListDataExtractTask task) {
        ListDataExtractTask existingTask = taskCache.get(id);
        if (existingTask == null) {
            log.warn("任务不存在: {}", id);
            return null;
        }

        task.setId(id);
        task.setCreateTime(existingTask.getCreateTime());
        task.setUpdateTime(LocalDateTime.now());
        task.setExecuteCount(existingTask.getExecuteCount());
        task.setSuccessCount(existingTask.getSuccessCount());
        task.setFailCount(existingTask.getFailCount());
        task.setLastExecuteTime(existingTask.getLastExecuteTime());
        task.setTotalExtractedRecords(existingTask.getTotalExtractedRecords());

        taskCache.put(id, task);
        saveTasks();

        log.info("更新列表数据抽取任务: {}", task.getName());
        return task;
    }

    @Override
    public boolean deleteTask(String id) {
        ListDataExtractTask removed = taskCache.remove(id);
        if (removed != null) {
            saveTasks();
            log.info("删除列表数据抽取任务: {}", removed.getName());
            return true;
        }
        return false;
    }

    @Override
    public ListDataExtractTask getTaskById(String id) {
        return taskCache.get(id);
    }

    @Override
    public List<ListDataExtractTask> getAllTasks() {
        return new ArrayList<>(taskCache.values());
    }

    @Override
    public List<ListDataExtractTask> getEnabledTasks() {
        return taskCache.values().stream()
                .filter(ListDataExtractTask::isEnabled)
                .collect(Collectors.toList());
    }

    @Override
    public boolean executeTask(String id) {
        ListDataExtractTask task = taskCache.get(id);
        if (task == null) {
            log.warn("任务不存在: {}", id);
            return false;
        }

        // 模拟执行任务
        task.setStatus(ListDataExtractTask.TaskStatus.RUNNING);
        task.setLastExecuteTime(LocalDateTime.now());
        task.incrementExecuteCount();
        saveTasks();

        log.info("开始执行列表数据抽取任务: {}", task.getName());

        // 异步执行实际任务逻辑
        new Thread(() -> {
            try {
                List<Map<String, Object>> extractedData = doExtract(task);

                // 保存到目标集合
                if (task.getTargetCollection() != null && !task.getTargetCollection().isEmpty()) {
                    for (Map<String, Object> record : extractedData) {
                        String recordId = task.getPrimaryKey() != null ?
                                String.valueOf(record.get(task.getPrimaryKey())) :
                                UUID.randomUUID().toString();
                        sdkDataService.save(task.getTargetCollection(), recordId, record);
                    }
                }

                // 更新任务状态
                task.setStatus(ListDataExtractTask.TaskStatus.COMPLETED);
                task.incrementSuccessCount();
                task.setLastErrorMessage(null);
                task.addExtractedRecords(extractedData.size());

                log.info("列表数据抽取任务执行成功: {}, 抽取 {} 条记录", task.getName(), extractedData.size());
            } catch (Exception e) {
                task.setStatus(ListDataExtractTask.TaskStatus.FAILED);
                task.incrementFailCount();
                task.setLastErrorMessage(e.getMessage());

                log.error("列表数据抽取任务执行失败: {}", task.getName(), e);
            }

            task.setUpdateTime(LocalDateTime.now());
            saveTasks();
        }).start();

        return true;
    }

    /**
     * 执行数据抽取
     */
    private List<Map<String, Object>> doExtract(ListDataExtractTask task) throws Exception {
        List<Map<String, Object>> result = new ArrayList<>();

        switch (task.getSourceType()) {
            case API:
                result = extractFromApi(task);
                break;
            case FILE:
                result = extractFromFile(task);
                break;
            case DATABASE:
            case MESSAGE_QUEUE:
            case CUSTOM:
            default:
                // 模拟数据抽取
                result = generateMockData(task);
                break;
        }

        // 应用过滤条件
        if (task.getFilterCondition() != null && !task.getFilterCondition().isEmpty()) {
            result = applyFilter(result, task.getFilterCondition());
        }

        // 限制最大记录数
        if (task.getMaxRecords() > 0 && result.size() > task.getMaxRecords()) {
            result = result.subList(0, task.getMaxRecords());
        }

        return result;
    }

    /**
     * 从API抽取数据
     */
    private List<Map<String, Object>> extractFromApi(ListDataExtractTask task) throws Exception {
        List<Map<String, Object>> result = new ArrayList<>();

        String url = task.getApiUrl();
        String method = task.getApiMethod() != null ? task.getApiMethod().toUpperCase() : "GET";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 添加请求头
        if (task.getApiHeaders() != null) {
            task.getApiHeaders().forEach(headers::set);
        }

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response;
        if ("POST".equals(method)) {
            response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        } else {
            response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        }

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            // 解析JSON响应
            String body = response.getBody();
            Object parsed = objectMapper.readValue(body, Object.class);

            if (parsed instanceof List) {
                result = (List<Map<String, Object>>) parsed;
            } else if (parsed instanceof Map) {
                Map<String, Object> map = (Map<String, Object>) parsed;
                // 尝试找到列表数据
                for (Object value : map.values()) {
                    if (value instanceof List) {
                        result = (List<Map<String, Object>>) value;
                        break;
                    }
                }
                if (result.isEmpty()) {
                    result.add(map);
                }
            }
        } else {
            throw new RuntimeException("API请求失败: HTTP " + response.getStatusCode());
        }

        return result;
    }

    /**
     * 从文件抽取数据
     */
    private List<Map<String, Object>> extractFromFile(ListDataExtractTask task) throws Exception {
        List<Map<String, Object>> result = new ArrayList<>();
        String filePath = task.getFilePath();
        String format = task.getFileFormat();

        if (filePath == null || !Files.exists(Paths.get(filePath))) {
            throw new RuntimeException("文件不存在: " + filePath);
        }

        if ("JSON".equalsIgnoreCase(format)) {
            String content = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
            Object parsed = objectMapper.readValue(content, Object.class);

            if (parsed instanceof List) {
                result = (List<Map<String, Object>>) parsed;
            } else if (parsed instanceof Map) {
                result.add((Map<String, Object>) parsed);
            }
        } else if ("CSV".equalsIgnoreCase(format)) {
            result = parseCsvFile(filePath);
        } else {
            // 默认按JSON处理
            String content = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
            Object parsed = objectMapper.readValue(content, Object.class);

            if (parsed instanceof List) {
                result = (List<Map<String, Object>>) parsed;
            } else if (parsed instanceof Map) {
                result.add((Map<String, Object>) parsed);
            }
        }

        return result;
    }

    /**
     * 解析CSV文件
     */
    private List<Map<String, Object>> parseCsvFile(String filePath) throws Exception {
        List<Map<String, Object>> result = new ArrayList<>();
        List<String> headers = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");

                if (isFirstLine) {
                    headers = Arrays.asList(values);
                    isFirstLine = false;
                } else {
                    Map<String, Object> record = new HashMap<>();
                    for (int i = 0; i < headers.size() && i < values.length; i++) {
                        record.put(headers.get(i), values[i]);
                    }
                    result.add(record);
                }
            }
        }

        return result;
    }

    /**
     * 生成模拟数据
     */
    private List<Map<String, Object>> generateMockData(ListDataExtractTask task) {
        List<Map<String, Object>> result = new ArrayList<>();
        int count = Math.min(task.getBatchSize(), 100);

        for (int i = 0; i < count; i++) {
            Map<String, Object> record = new HashMap<>();
            record.put("id", UUID.randomUUID().toString());
            record.put("name", "Record " + (i + 1));
            record.put("value", Math.random() * 100);
            record.put("timestamp", System.currentTimeMillis());
            record.put("status", i % 2 == 0 ? "active" : "inactive");
            result.add(record);
        }

        return result;
    }

    /**
     * 应用过滤条件
     */
    private List<Map<String, Object>> applyFilter(List<Map<String, Object>> data, String filterCondition) {
        // 简单过滤实现，支持 key=value 格式
        if (filterCondition.contains("=")) {
            String[] parts = filterCondition.split("=", 2);
            String key = parts[0].trim();
            String value = parts[1].trim();

            return data.stream()
                    .filter(record -> value.equals(String.valueOf(record.get(key))))
                    .collect(Collectors.toList());
        }

        return data;
    }

    @Override
    public boolean enableTask(String id) {
        ListDataExtractTask task = taskCache.get(id);
        if (task != null) {
            task.setEnabled(true);
            task.setUpdateTime(LocalDateTime.now());
            saveTasks();
            log.info("启用列表数据抽取任务: {}", task.getName());
            return true;
        }
        return false;
    }

    @Override
    public boolean disableTask(String id) {
        ListDataExtractTask task = taskCache.get(id);
        if (task != null) {
            task.setEnabled(false);
            task.setUpdateTime(LocalDateTime.now());
            saveTasks();
            log.info("禁用列表数据抽取任务: {}", task.getName());
            return true;
        }
        return false;
    }

    @Override
    public TaskStats getTaskStats() {
        TaskStats stats = new TaskStats();
        stats.setTotalCount(taskCache.size());
        stats.setEnabledCount((int) taskCache.values().stream().filter(ListDataExtractTask::isEnabled).count());
        stats.setRunningCount((int) taskCache.values().stream()
                .filter(t -> t.getStatus() == ListDataExtractTask.TaskStatus.RUNNING).count());
        stats.setCompletedCount((int) taskCache.values().stream()
                .filter(t -> t.getStatus() == ListDataExtractTask.TaskStatus.COMPLETED).count());
        stats.setFailedCount((int) taskCache.values().stream()
                .filter(t -> t.getStatus() == ListDataExtractTask.TaskStatus.FAILED).count());

        long totalRecords = taskCache.values().stream()
                .mapToLong(ListDataExtractTask::getTotalExtractedRecords)
                .sum();
        stats.setTotalExtractedRecords(totalRecords);

        return stats;
    }

    @Override
    public boolean testConnection(String id) {
        ListDataExtractTask task = taskCache.get(id);
        if (task == null) {
            return false;
        }

        try {
            switch (task.getSourceType()) {
                case API:
                    return testApiConnection(task);
                case FILE:
                    return testFileConnection(task);
                case DATABASE:
                default:
                    return true;
            }
        } catch (Exception e) {
            log.error("测试连接失败: {}", task.getName(), e);
            return false;
        }
    }

    /**
     * 测试API连接
     */
    private boolean testApiConnection(ListDataExtractTask task) {
        try {
            String url = task.getApiUrl();
            HttpHeaders headers = new HttpHeaders();
            if (task.getApiHeaders() != null) {
                task.getApiHeaders().forEach(headers::set);
            }
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            return response.getStatusCode() == HttpStatus.OK;
        } catch (Exception e) {
            log.error("API连接测试失败", e);
            return false;
        }
    }

    /**
     * 测试文件连接
     */
    private boolean testFileConnection(ListDataExtractTask task) {
        String filePath = task.getFilePath();
        return filePath != null && Files.exists(Paths.get(filePath));
    }

    @Override
    public List<Map<String, Object>> previewData(String id, int limit) {
        ListDataExtractTask task = taskCache.get(id);
        if (task == null) {
            return new ArrayList<>();
        }

        try {
            List<Map<String, Object>> data = doExtract(task);
            if (data.size() > limit) {
                return data.subList(0, limit);
            }
            return data;
        } catch (Exception e) {
            log.error("预览数据失败: {}", task.getName(), e);
            return new ArrayList<>();
        }
    }
}
