package net.ooder.nexus.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.ooder.nexus.domain.sync.model.SyncTask;
import net.ooder.nexus.service.SkillSyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 技能同步服务实现类
 * 参考agent-skillcenter设计，支持个人使用和小规模办公分享
 */
@Service
public class SkillSyncServiceImpl implements SkillSyncService {

    private static final Logger log = LoggerFactory.getLogger(SkillSyncServiceImpl.class);
    private static final String DATA_DIR = "./storage/sync";
    private static final String TASKS_FILE = "sync-tasks.json";
    private static final String SYNCED_SKILLS_FILE = "synced-skills.json";

    private final Map<String, SyncTask> taskCache = new ConcurrentHashMap<>();
    private final Map<String, SyncedSkill> syncedSkillCache = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;
    private final Path tasksPath;
    private final Path syncedSkillsPath;

    public SkillSyncServiceImpl() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        this.tasksPath = Paths.get(DATA_DIR, TASKS_FILE);
        this.syncedSkillsPath = Paths.get(DATA_DIR, SYNCED_SKILLS_FILE);
    }

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(Paths.get(DATA_DIR));
            loadData();
            
            if (taskCache.isEmpty()) {
                initDefaultData();
            }
            
            log.info("技能同步服务初始化完成，共加载 {} 个任务", taskCache.size());
        } catch (IOException e) {
            log.error("初始化技能同步服务失败", e);
        }
    }

    private void loadData() {
        if (Files.exists(tasksPath)) {
            try {
                String json = new String(Files.readAllBytes(tasksPath), StandardCharsets.UTF_8);
                List<SyncTask> tasks = objectMapper.readValue(json, new TypeReference<List<SyncTask>>() {});
                tasks.forEach(t -> taskCache.put(t.getId(), t));
            } catch (IOException e) {
                log.error("加载同步任务失败", e);
            }
        }

        if (Files.exists(syncedSkillsPath)) {
            try {
                String json = new String(Files.readAllBytes(syncedSkillsPath), StandardCharsets.UTF_8);
                List<SyncedSkill> skills = objectMapper.readValue(json, new TypeReference<List<SyncedSkill>>() {});
                skills.forEach(s -> syncedSkillCache.put(s.getId(), s));
            } catch (IOException e) {
                log.error("加载已同步技能失败", e);
            }
        }
    }

    private void saveTasks() {
        try {
            List<SyncTask> tasks = new ArrayList<>(taskCache.values());
            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(tasks);
            Files.write(tasksPath, json.getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            log.error("保存同步任务失败", e);
        }
    }

    private void saveSyncedSkills() {
        try {
            List<SyncedSkill> skills = new ArrayList<>(syncedSkillCache.values());
            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(skills);
            Files.write(syncedSkillsPath, json.getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            log.error("保存已同步技能失败", e);
        }
    }

    @Override
    public void initDefaultData() {
        // 创建示例同步任务
        SyncTask task1 = new SyncTask();
        task1.setId("sync-001");
        task1.setName("初始同步");
        task1.setType("bidirectional");
        task1.setStatus("completed");
        task1.setSource("本地");
        task1.setTarget("云端");
        task1.setTotalItems(5);
        task1.setProcessedItems(5);
        task1.setFailedItems(0);
        task1.setCreateTime(LocalDateTime.now().minusDays(1));
        task1.setStartTime(LocalDateTime.now().minusDays(1));
        task1.setEndTime(LocalDateTime.now().minusDays(1).plusMinutes(5));
        
        taskCache.put(task1.getId(), task1);
        saveTasks();
        
        log.info("初始化默认同步数据完成");
    }

    @Override
    public List<SyncTask> getAllTasks() {
        return new ArrayList<>(taskCache.values()).stream()
                .sorted((a, b) -> b.getCreateTime().compareTo(a.getCreateTime()))
                .collect(Collectors.toList());
    }

    @Override
    public List<SyncTask> getTasksByStatus(String status) {
        return taskCache.values().stream()
                .filter(t -> status.equals(t.getStatus()))
                .sorted((a, b) -> b.getCreateTime().compareTo(a.getCreateTime()))
                .collect(Collectors.toList());
    }

    @Override
    public SyncTask createTask(SyncTask task) {
        if (task.getId() == null || task.getId().isEmpty()) {
            task.setId("sync-" + UUID.randomUUID().toString().substring(0, 8));
        }
        task.setCreateTime(LocalDateTime.now());
        task.setStatus("pending");
        taskCache.put(task.getId(), task);
        saveTasks();
        
        log.info("创建同步任务: {}", task.getName());
        return task;
    }

    @Override
    public SyncTask executeTask(String taskId) {
        SyncTask task = taskCache.get(taskId);
        if (task == null) {
            return null;
        }
        
        task.setStatus("running");
        task.setStartTime(LocalDateTime.now());
        saveTasks();
        
        // 模拟同步过程
        simulateSyncProcess(task);
        
        log.info("执行同步任务: {}", taskId);
        return task;
    }

    private void simulateSyncProcess(SyncTask task) {
        // 异步模拟同步过程
        new Thread(() -> {
            try {
                Thread.sleep(2000); // 模拟处理时间
                
                task.setProcessedItems(task.getTotalItems());
                task.setFailedItems(0);
                task.setStatus("completed");
                task.setEndTime(LocalDateTime.now());
                
                // 更新已同步技能列表
                if (task.getItems() != null) {
                    for (SyncTask.SyncItem item : task.getItems()) {
                        SyncedSkill skill = new SyncedSkill();
                        skill.setId(item.getId());
                        skill.setName(item.getName());
                        skill.setVersion("1.0.0");
                        skill.setCategory(item.getType());
                        skill.setSyncDirection(task.getType().contains("upload") ? "upload" : "download");
                        skill.setSyncTime(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                        skill.setSyncStatus("success");
                        syncedSkillCache.put(skill.getId(), skill);
                    }
                    saveSyncedSkills();
                }
                
                saveTasks();
                log.info("同步任务完成: {}", task.getId());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                task.setStatus("failed");
                task.setErrorMessage("同步被中断");
                saveTasks();
            }
        }).start();
    }

    @Override
    public SyncTask cancelTask(String taskId) {
        SyncTask task = taskCache.get(taskId);
        if (task == null || !"running".equals(task.getStatus())) {
            return null;
        }
        
        task.setStatus("cancelled");
        task.setEndTime(LocalDateTime.now());
        saveTasks();
        
        log.info("取消同步任务: {}", taskId);
        return task;
    }

    @Override
    public boolean deleteTask(String taskId) {
        if (taskCache.remove(taskId) != null) {
            saveTasks();
            log.info("删除同步任务: {}", taskId);
            return true;
        }
        return false;
    }

    @Override
    public SyncStatistics getStatistics() {
        SyncStatistics stats = new SyncStatistics();
        List<SyncTask> tasks = getAllTasks();
        
        stats.setTotalTasks(tasks.size());
        stats.setCompletedTasks((int) tasks.stream().filter(t -> "completed".equals(t.getStatus())).count());
        stats.setFailedTasks((int) tasks.stream().filter(t -> "failed".equals(t.getStatus())).count());
        stats.setRunningTasks((int) tasks.stream().filter(t -> "running".equals(t.getStatus())).count());
        stats.setPendingTasks((int) tasks.stream().filter(t -> "pending".equals(t.getStatus())).count());
        stats.setTotalSyncedSkills(syncedSkillCache.size());
        
        // 获取最后同步时间
        tasks.stream()
                .filter(t -> "completed".equals(t.getStatus()))
                .max(Comparator.comparing(SyncTask::getEndTime))
                .ifPresent(t -> stats.setLastSyncTime(t.getEndTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
        
        // 计算成功率
        int completed = stats.getCompletedTasks();
        int total = completed + stats.getFailedTasks();
        stats.setSuccessRate(total > 0 ? (double) completed / total * 100 : 0);
        
        return stats;
    }

    @Override
    public List<SyncableSkill> getSyncableSkills() {
        // 模拟可同步技能列表
        List<SyncableSkill> skills = new ArrayList<>();
        
        SyncableSkill skill1 = new SyncableSkill();
        skill1.setId("skill-001");
        skill1.setName("文本处理工具");
        skill1.setDescription("文本转换和处理工具");
        skill1.setVersion("1.0.0");
        skill1.setCategory("productivity");
        skill1.setSize(1024);
        skill1.setLastModified(LocalDateTime.now().minusDays(2).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        skill1.setSynced(syncedSkillCache.containsKey("skill-001"));
        skills.add(skill1);
        
        SyncableSkill skill2 = new SyncableSkill();
        skill2.setId("skill-002");
        skill2.setName("代码生成器");
        skill2.setDescription("自动生成代码片段");
        skill2.setVersion("1.2.0");
        skill2.setCategory("development");
        skill2.setSize(2048);
        skill2.setLastModified(LocalDateTime.now().minusDays(5).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        skill2.setSynced(syncedSkillCache.containsKey("skill-002"));
        skills.add(skill2);
        
        SyncableSkill skill3 = new SyncableSkill();
        skill3.setId("skill-003");
        skill3.setName("数据分析助手");
        skill3.setDescription("数据分析和可视化");
        skill3.setVersion("2.0.0");
        skill3.setCategory("data");
        skill3.setSize(5120);
        skill3.setLastModified(LocalDateTime.now().minusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        skill3.setSynced(syncedSkillCache.containsKey("skill-003"));
        skills.add(skill3);
        
        return skills;
    }

    @Override
    public List<SyncedSkill> getSyncedSkills() {
        return new ArrayList<>(syncedSkillCache.values());
    }

    @Override
    public SyncTask uploadSkill(String skillId, String target) {
        SyncTask task = new SyncTask();
        task.setName("上传技能: " + skillId);
        task.setType("upload");
        task.setSource("本地");
        task.setTarget(target != null ? target : "云端");
        task.setTotalItems(1);
        
        SyncTask.SyncItem item = new SyncTask.SyncItem();
        item.setId(skillId);
        item.setName(skillId);
        item.setType("skill");
        item.setStatus("pending");
        task.setItems(Collections.singletonList(item));
        
        createTask(task);
        return executeTask(task.getId());
    }

    @Override
    public SyncTask downloadSkill(String skillId, String source) {
        SyncTask task = new SyncTask();
        task.setName("下载技能: " + skillId);
        task.setType("download");
        task.setSource(source != null ? source : "云端");
        task.setTarget("本地");
        task.setTotalItems(1);
        
        SyncTask.SyncItem item = new SyncTask.SyncItem();
        item.setId(skillId);
        item.setName(skillId);
        item.setType("skill");
        item.setStatus("pending");
        task.setItems(Collections.singletonList(item));
        
        createTask(task);
        return executeTask(task.getId());
    }

    @Override
    public SyncTask batchSync(List<String> skillIds, String type) {
        SyncTask task = new SyncTask();
        task.setName("批量同步: " + skillIds.size() + " 个技能");
        task.setType(type);
        task.setSource("本地");
        task.setTarget("云端");
        task.setTotalItems(skillIds.size());
        
        List<SyncTask.SyncItem> items = skillIds.stream().map(id -> {
            SyncTask.SyncItem item = new SyncTask.SyncItem();
            item.setId(id);
            item.setName(id);
            item.setType("skill");
            item.setStatus("pending");
            return item;
        }).collect(Collectors.toList());
        task.setItems(items);
        
        createTask(task);
        return executeTask(task.getId());
    }
}
