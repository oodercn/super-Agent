/*
 * Copyright (c) 2024 Ooder Team
 *
 * This software is released under the MIT License.
 * https://opensource.org/licenses/MIT
 */
package net.ooder.skillcenter.storage;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import net.ooder.skillcenter.model.ExecutionRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Execution Record Data Storage Service
 * Uses JSON files to store execution history
 */
@Service
public class ExecutionStorageService {
    private static final Logger logger = LoggerFactory.getLogger(ExecutionStorageService.class);

    private static final String STORAGE_DIR = "skillcenter/storage";
    private static final String EXECUTIONS_FILE = "executions.json";

    private Path storagePath;
    private List<ExecutionRecord> executionsCache;

    @PostConstruct
    public void init() {
        this.storagePath = Paths.get(System.getProperty("user.dir"), STORAGE_DIR);
        try {
            Files.createDirectories(storagePath);
            loadData();
            logger.info("[ExecutionStorageService] Initialized, loaded {} execution records", executionsCache.size());
        } catch (IOException e) {
            logger.error("[ExecutionStorageService] Initialization failed: {}", e.getMessage(), e);
        }
    }

    /**
     * Load data from file
     */
    private void loadData() throws IOException {
        Path executionsFile = storagePath.resolve(EXECUTIONS_FILE);
        if (Files.exists(executionsFile)) {
            String content = new String(Files.readAllBytes(executionsFile));
            executionsCache = JSON.parseObject(content, new TypeReference<List<ExecutionRecord>>() {});
        } else {
            executionsCache = new ArrayList<>();
            initDefaultExecutions();
        }
    }

    /**
     * Save data to file
     */
    private void saveData() throws IOException {
        Path executionsFile = storagePath.resolve(EXECUTIONS_FILE);
        String executionsJson = JSON.toJSONString(executionsCache, true);
        Files.write(executionsFile, executionsJson.getBytes());
    }

    /**
     * Initialize default execution records
     */
    private void initDefaultExecutions() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        executionsCache.add(new ExecutionRecord(
                "exec-12345", "text-uppercase", "Text to Uppercase",
                LocalDateTime.now().format(formatter), "SUCCESS", "0.5s"));
        executionsCache.add(new ExecutionRecord(
                "exec-12344", "code-generation", "Code Generation",
                LocalDateTime.now().minusHours(1).format(formatter), "SUCCESS", "3.2s"));
        executionsCache.add(new ExecutionRecord(
                "exec-12343", "local-deployment", "Local Deployment",
                LocalDateTime.now().minusHours(2).format(formatter), "FAILED", "1.8s"));

        try {
            saveData();
        } catch (IOException e) {
            logger.error("[ExecutionStorageService] Failed to save default execution records: {}", e.getMessage());
        }
    }

    /**
     * Get all execution records
     */
    public List<ExecutionRecord> getAllExecutions() {
        return new ArrayList<>(executionsCache);
    }

    /**
     * Get execution record by ID
     */
    public ExecutionRecord getExecutionById(String executionId) {
        return executionsCache.stream()
                .filter(e -> e.getExecutionId().equals(executionId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Get execution records by skill ID
     */
    public List<ExecutionRecord> getExecutionsBySkillId(String skillId) {
        return executionsCache.stream()
                .filter(e -> e.getSkillId().equals(skillId))
                .collect(Collectors.toList());
    }

    /**
     * Add execution record
     */
    public ExecutionRecord addExecution(ExecutionRecord execution) {
        if (execution.getExecutionId() == null) {
            execution.setExecutionId("exec-" + UUID.randomUUID().toString().substring(0, 8));
        }
        executionsCache.add(execution);
        try {
            saveData();
        } catch (IOException e) {
            logger.error("[ExecutionStorageService] Failed to save execution record: {}", e.getMessage());
        }
        return execution;
    }

    /**
     * Update execution record
     */
    public ExecutionRecord updateExecution(ExecutionRecord execution) {
        for (int i = 0; i < executionsCache.size(); i++) {
            if (executionsCache.get(i).getExecutionId().equals(execution.getExecutionId())) {
                executionsCache.set(i, execution);
                try {
                    saveData();
                } catch (IOException e) {
                    logger.error("[ExecutionStorageService] Failed to save execution record: {}", e.getMessage());
                }
                return execution;
            }
        }
        return null;
    }

    /**
     * Delete execution record
     */
    public boolean deleteExecution(String executionId) {
        boolean removed = executionsCache.removeIf(e -> e.getExecutionId().equals(executionId));
        if (removed) {
            try {
                saveData();
            } catch (IOException e) {
                logger.error("[ExecutionStorageService] Failed to save execution record: {}", e.getMessage());
            }
        }
        return removed;
    }

    /**
     * Clear all execution records
     */
    public void clearAllExecutions() {
        executionsCache.clear();
        try {
            saveData();
        } catch (IOException e) {
            logger.error("[ExecutionStorageService] Failed to save execution record: {}", e.getMessage());
        }
    }
}
