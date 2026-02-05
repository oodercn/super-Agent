package net.ooder.skillcenter.manager;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 托管管理器，负责托管实例的管理
 */
public class HostingManager {
    // 单例实例
    private static HostingManager instance;
    
    // 托管实例映射，key为实例ID，value为托管实例
    private Map<String, HostingInstance> hostingInstances;

    /**
     * 私有构造方法
     */
    private HostingManager() {
        this.hostingInstances = new HashMap<>();
        this.loadSampleData();
    }

    /**
     * 获取实例
     * @return 托管管理器实例
     */
    public static synchronized HostingManager getInstance() {
        if (instance == null) {
            instance = new HostingManager();
        }
        return instance;
    }

    /**
     * 创建新的托管实例
     * @param instance 托管实例
     * @return 创建的托管实例
     */
    public synchronized HostingInstance createHostingInstance(HostingInstance instance) {
        if (instance == null) {
            throw new IllegalArgumentException("Hosting instance cannot be null");
        }

        // 生成实例ID
        if (instance.getId() == null || instance.getId().isEmpty()) {
            instance.setId("hosting-" + UUID.randomUUID().toString().substring(0, 8));
        }

        // 设置部署时间
        if (instance.getDeployedAt() == null) {
            instance.setDeployedAt(LocalDateTime.now());
        }

        // 设置初始状态
        if (instance.getStatus() == null) {
            instance.setStatus("stopped");
        }

        // 设置运行时间
        updateUptime(instance);

        // 添加到托管实例映射
        hostingInstances.put(instance.getId(), instance);
        return instance;
    }

    /**
     * 获取指定ID的托管实例
     * @param id 实例ID
     * @return 托管实例，不存在则返回null
     */
    public HostingInstance getHostingInstance(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Hosting instance ID cannot be null or empty");
        }
        return hostingInstances.get(id);
    }

    /**
     * 获取所有托管实例
     * @return 托管实例列表
     */
    public List<HostingInstance> getAllHostingInstances() {
        return new ArrayList<>(hostingInstances.values());
    }

    /**
     * 更新托管实例信息
     * @param instance 托管实例
     * @return 更新后的托管实例
     */
    public synchronized HostingInstance updateHostingInstance(HostingInstance instance) {
        if (instance == null) {
            throw new IllegalArgumentException("Hosting instance cannot be null");
        }

        if (instance.getId() == null || instance.getId().isEmpty()) {
            throw new IllegalArgumentException("Hosting instance ID cannot be null or empty");
        }

        if (!hostingInstances.containsKey(instance.getId())) {
            throw new IllegalArgumentException("Hosting instance not found: " + instance.getId());
        }

        // 更新运行时间
        updateUptime(instance);

        // 更新托管实例
        hostingInstances.put(instance.getId(), instance);
        return instance;
    }

    /**
     * 删除托管实例
     * @param id 实例ID
     * @return 删除是否成功
     */
    public synchronized boolean deleteHostingInstance(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Hosting instance ID cannot be null or empty");
        }

        return hostingInstances.remove(id) != null;
    }

    /**
     * 启动托管实例
     * @param id 实例ID
     * @return 启动后的托管实例
     */
    public synchronized HostingInstance startHostingInstance(String id) {
        HostingInstance instance = getHostingInstance(id);
        if (instance == null) {
            throw new IllegalArgumentException("Hosting instance not found: " + id);
        }

        instance.setStatus("running");
        instance.setStartedAt(LocalDateTime.now());
        updateUptime(instance);

        hostingInstances.put(id, instance);
        return instance;
    }

    /**
     * 停止托管实例
     * @param id 实例ID
     * @return 停止后的托管实例
     */
    public synchronized HostingInstance stopHostingInstance(String id) {
        HostingInstance instance = getHostingInstance(id);
        if (instance == null) {
            throw new IllegalArgumentException("Hosting instance not found: " + id);
        }

        instance.setStatus("stopped");
        instance.setUptime("0h");

        hostingInstances.put(id, instance);
        return instance;
    }

    /**
     * 搜索托管实例
     * @param keyword 关键词
     * @return 匹配的托管实例列表
     */
    public List<HostingInstance> searchHostingInstances(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return getAllHostingInstances();
        }

        List<HostingInstance> result = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();

        for (HostingInstance instance : hostingInstances.values()) {
            if (instance.getName().toLowerCase().contains(lowerKeyword) || 
                instance.getId().toLowerCase().contains(lowerKeyword) ||
                (instance.getDescription() != null && instance.getDescription().toLowerCase().contains(lowerKeyword))) {
                result.add(instance);
            }
        }

        return result;
    }

    /**
     * 获取托管实例统计信息
     * @return 统计信息
     */
    public Map<String, Object> getHostingStats() {
        Map<String, Object> stats = new HashMap<>();

        int totalInstances = hostingInstances.size();
        int runningInstances = 0;
        int stoppedInstances = 0;

        for (HostingInstance instance : hostingInstances.values()) {
            if ("running".equals(instance.getStatus())) {
                runningInstances++;
            } else if ("stopped".equals(instance.getStatus())) {
                stoppedInstances++;
            }
        }

        stats.put("totalInstances", totalInstances);
        stats.put("runningInstances", runningInstances);
        stats.put("stoppedInstances", stoppedInstances);
        stats.put("availability", 98.0); // 模拟值

        return stats;
    }

    /**
     * 更新运行时间
     * @param instance 托管实例
     */
    private void updateUptime(HostingInstance instance) {
        if ("running".equals(instance.getStatus()) && instance.getStartedAt() != null) {
            LocalDateTime now = LocalDateTime.now();
            long hours = ChronoUnit.HOURS.between(instance.getStartedAt(), now);
            long minutes = ChronoUnit.MINUTES.between(instance.getStartedAt(), now) % 60;

            if (hours > 0) {
                instance.setUptime(hours + "h " + minutes + "m");
            } else {
                instance.setUptime(minutes + "m");
            }
        } else {
            instance.setUptime("0h");
        }
    }

    /**
     * 加载示例数据
     */
    private void loadSampleData() {
        // 创建示例托管实例数据
        HostingInstance instance1 = new HostingInstance();
        instance1.setId("hosting-001");
        instance1.setName("Text to Uppercase Service");
        instance1.setSkillId("text-to-uppercase-skill");
        instance1.setStatus("running");
        instance1.setDeployedAt(LocalDateTime.now().minusHours(10).minusMinutes(30));
        instance1.setStartedAt(LocalDateTime.now().minusHours(10).minusMinutes(30));
        instance1.setUptime("10h 30m");
        hostingInstances.put(instance1.getId(), instance1);

        HostingInstance instance2 = new HostingInstance();
        instance2.setId("hosting-002");
        instance2.setName("Code Generator Service");
        instance2.setSkillId("code-generation-skill");
        instance2.setStatus("running");
        instance2.setDeployedAt(LocalDateTime.now().minusHours(25));
        instance2.setStartedAt(LocalDateTime.now().minusHours(25));
        instance2.setUptime("25h");
        hostingInstances.put(instance2.getId(), instance2);

        HostingInstance instance3 = new HostingInstance();
        instance3.setId("hosting-003");
        instance3.setName("Local Deployment Service");
        instance3.setSkillId("local-deployment-skill");
        instance3.setStatus("stopped");
        instance3.setDeployedAt(LocalDateTime.now().minusDays(2).minusHours(9).minusMinutes(15));
        instance3.setUptime("0h");
        hostingInstances.put(instance3.getId(), instance3);

        HostingInstance instance4 = new HostingInstance();
        instance4.setId("hosting-004");
        instance4.setName("Weather API Service");
        instance4.setSkillId("weather-api-skill");
        instance4.setStatus("running");
        instance4.setDeployedAt(LocalDateTime.now().minusDays(3));
        instance4.setStartedAt(LocalDateTime.now().minusDays(3));
        instance4.setUptime("72h");
        hostingInstances.put(instance4.getId(), instance4);

        HostingInstance instance5 = new HostingInstance();
        instance5.setId("hosting-005");
        instance5.setName("Stock API Service");
        instance5.setSkillId("stock-api-skill");
        instance5.setStatus("running");
        instance5.setDeployedAt(LocalDateTime.now().minusDays(4));
        instance5.setStartedAt(LocalDateTime.now().minusDays(4));
        instance5.setUptime("96h");
        hostingInstances.put(instance5.getId(), instance5);

        HostingInstance instance6 = new HostingInstance();
        instance6.setId("hosting-006");
        instance6.setName("Image Resizer Service");
        instance6.setSkillId("image-resizer-skill");
        instance6.setStatus("stopped");
        instance6.setDeployedAt(LocalDateTime.now().minusDays(5).minusHours(16).minusMinutes(50));
        instance6.setUptime("0h");
        hostingInstances.put(instance6.getId(), instance6);

        System.out.println("Loaded " + hostingInstances.size() + " sample hosting instances");
    }

    /**
     * 托管实例类
     */
    public static class HostingInstance {
        private String id;
        private String name;
        private String skillId;
        private String description;
        private String status;
        private LocalDateTime deployedAt;
        private LocalDateTime startedAt;
        private String uptime;

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

        public String getSkillId() {
            return skillId;
        }

        public void setSkillId(String skillId) {
            this.skillId = skillId;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public LocalDateTime getDeployedAt() {
            return deployedAt;
        }

        public void setDeployedAt(LocalDateTime deployedAt) {
            this.deployedAt = deployedAt;
        }

        public LocalDateTime getStartedAt() {
            return startedAt;
        }

        public void setStartedAt(LocalDateTime startedAt) {
            this.startedAt = startedAt;
        }

        public String getUptime() {
            return uptime;
        }

        public void setUptime(String uptime) {
            this.uptime = uptime;
        }
    }
}
