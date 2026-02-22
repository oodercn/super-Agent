package net.ooder.nexus.adapter.inbound.controller.scenario;

import net.ooder.nexus.common.ResultModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 场景管理控制器
 * 处理场景的增删改查、导入导出、统计等操作
 *
 * @author ooder Team
 * @version 0.7.0
 * @since 0.7.0
 */
@RestController
@RequestMapping("/api/scenario")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS})
public class ScenarioController {

    private static final Logger logger = LoggerFactory.getLogger(ScenarioController.class);

    private final Map<String, Scenario> scenarioMap = new ConcurrentHashMap<>();

    public ScenarioController() {
        initMockData();
    }

    private void initMockData() {
        Map<String, Object> config1 = new ConcurrentHashMap<>();
        List<String> nodes1 = new ArrayList<>();
        nodes1.add("mcp-agent-001");
        nodes1.add("route-agent-001");
        nodes1.add("end-agent-001");
        config1.put("nodes", nodes1);

        List<Map<String, Object>> links1 = new ArrayList<>();
        Map<String, Object> link1_1 = new ConcurrentHashMap<>();
        link1_1.put("source", "mcp-agent-001");
        link1_1.put("target", "route-agent-001");
        links1.add(link1_1);

        Map<String, Object> link1_2 = new ConcurrentHashMap<>();
        link1_2.put("source", "route-agent-001");
        link1_2.put("target", "end-agent-001");
        links1.add(link1_2);
        config1.put("links", links1);

        Map<String, Object> configMap1 = new ConcurrentHashMap<>();
        configMap1.put("timeout", 30000);
        configMap1.put("retry", 3);
        config1.put("config", configMap1);

        Scenario scenario1 = new Scenario(
                "scenario-001",
                "企业网络场景",
                "enterprise",
                "active",
                new Date(),
                new Date(),
                "这是一个企业网络场景，用于测试场景管理功能。",
                config1
        );

        Map<String, Object> config2 = new ConcurrentHashMap<>();
        List<String> nodes2 = new ArrayList<>();
        nodes2.add("mcp-agent-002");
        nodes2.add("end-agent-002");
        config2.put("nodes", nodes2);

        List<Map<String, Object>> links2 = new ArrayList<>();
        Map<String, Object> link2_1 = new ConcurrentHashMap<>();
        link2_1.put("source", "mcp-agent-002");
        link2_1.put("target", "end-agent-002");
        links2.add(link2_1);
        config2.put("links", links2);

        Map<String, Object> configMap2 = new ConcurrentHashMap<>();
        configMap2.put("timeout", 30000);
        configMap2.put("retry", 3);
        config2.put("config", configMap2);

        Scenario scenario2 = new Scenario(
                "scenario-002",
                "个人网络场景",
                "personal",
                "active",
                new Date(),
                new Date(),
                "这是一个个人网络场景，用于测试场景管理功能。",
                config2
        );

        Map<String, Object> config3 = new ConcurrentHashMap<>();
        List<String> nodes3 = new ArrayList<>();
        nodes3.add("mcp-agent-001");
        nodes3.add("route-agent-001");
        nodes3.add("end-agent-001");
        nodes3.add("end-agent-002");
        config3.put("nodes", nodes3);

        List<Map<String, Object>> links3 = new ArrayList<>();
        Map<String, Object> link3_1 = new ConcurrentHashMap<>();
        link3_1.put("source", "mcp-agent-001");
        link3_1.put("target", "route-agent-001");
        links3.add(link3_1);

        Map<String, Object> link3_2 = new ConcurrentHashMap<>();
        link3_2.put("source", "route-agent-001");
        link3_2.put("target", "end-agent-001");
        links3.add(link3_2);

        Map<String, Object> link3_3 = new ConcurrentHashMap<>();
        link3_3.put("source", "route-agent-001");
        link3_3.put("target", "end-agent-002");
        links3.add(link3_3);
        config3.put("links", links3);

        Map<String, Object> configMap3 = new ConcurrentHashMap<>();
        configMap3.put("timeout", 30000);
        configMap3.put("retry", 3);
        config3.put("config", configMap3);

        Scenario scenario3 = new Scenario(
                "scenario-003",
                "多网络场景",
                "multi-network",
                "active",
                new Date(),
                new Date(),
                "这是一个多网络场景，用于测试场景管理功能。",
                config3
        );

        Map<String, Object> config4 = new ConcurrentHashMap<>();
        List<String> nodes4 = new ArrayList<>();
        nodes4.add("mcp-agent-test");
        nodes4.add("end-agent-test");
        config4.put("nodes", nodes4);

        List<Map<String, Object>> links4 = new ArrayList<>();
        Map<String, Object> link4_1 = new ConcurrentHashMap<>();
        link4_1.put("source", "mcp-agent-test");
        link4_1.put("target", "end-agent-test");
        links4.add(link4_1);
        config4.put("links", links4);

        Map<String, Object> configMap4 = new ConcurrentHashMap<>();
        configMap4.put("timeout", 30000);
        configMap4.put("retry", 3);
        config4.put("config", configMap4);

        Scenario scenario4 = new Scenario(
                "scenario-004",
                "测试网络场景",
                "test",
                "inactive",
                new Date(),
                new Date(),
                "这是一个测试网络场景，用于测试场景管理功能。",
                config4
        );

        Map<String, Object> config5 = new ConcurrentHashMap<>();
        List<String> nodes5 = new ArrayList<>();
        nodes5.add("mcp-agent-dev");
        nodes5.add("route-agent-dev");
        nodes5.add("end-agent-dev");
        config5.put("nodes", nodes5);

        List<Map<String, Object>> links5 = new ArrayList<>();
        Map<String, Object> link5_1 = new ConcurrentHashMap<>();
        link5_1.put("source", "mcp-agent-dev");
        link5_1.put("target", "route-agent-dev");
        links5.add(link5_1);

        Map<String, Object> link5_2 = new ConcurrentHashMap<>();
        link5_2.put("source", "route-agent-dev");
        link5_2.put("target", "end-agent-dev");
        links5.add(link5_2);
        config5.put("links", links5);

        Map<String, Object> configMap5 = new ConcurrentHashMap<>();
        configMap5.put("timeout", 30000);
        configMap5.put("retry", 3);
        config5.put("config", configMap5);

        Scenario scenario5 = new Scenario(
                "scenario-005",
                "开发环境场景",
                "development",
                "active",
                new Date(),
                new Date(),
                "这是一个开发环境场景，用于测试场景管理功能。",
                config5
        );

        scenarioMap.put(scenario1.getId(), scenario1);
        scenarioMap.put(scenario2.getId(), scenario2);
        scenarioMap.put(scenario3.getId(), scenario3);
        scenarioMap.put(scenario4.getId(), scenario4);
        scenarioMap.put(scenario5.getId(), scenario5);
    }

    @PostMapping("/list")
    @ResponseBody
    public ResultModel<List<Scenario>> getScenarioList() {
        try {
            logger.info("获取场景列表请求");
            List<Scenario> scenarios = new ArrayList<>(scenarioMap.values());
            logger.info("获取场景列表成功，共 {} 个场景", scenarios.size());
            return ResultModel.success(scenarios);
        } catch (Exception e) {
            logger.error("获取场景列表失败", e);
            return ResultModel.error("获取场景列表失败: " + e.getMessage(), 500);
        }
    }

    @PostMapping("/detail")
    @ResponseBody
    public ResultModel<Scenario> getScenarioDetail(@RequestBody Map<String, String> request) {
        String scenarioId = request.get("scenarioId");
        try {
            logger.info("获取场景详情请求，场景ID: {}", scenarioId);
            Scenario scenario = scenarioMap.get(scenarioId);
            if (scenario == null) {
                logger.warn("场景不存在，场景ID: {}", scenarioId);
                return ResultModel.error("场景不存在", 404);
            }
            logger.info("获取场景详情成功，场景ID: {}", scenarioId);
            return ResultModel.success(scenario);
        } catch (Exception e) {
            logger.error("获取场景详情失败，场景ID: {}", scenarioId, e);
            return ResultModel.error("获取场景详情失败: " + e.getMessage(), 500);
        }
    }

    @PostMapping("/create")
    @ResponseBody
    public ResultModel<Scenario> createScenario(@RequestBody Scenario scenario) {
        try {
            logger.info("创建场景请求");
            String scenarioId = "scenario-" + String.format("%03d", scenarioMap.size() + 1);
            scenario.setId(scenarioId);
            scenario.setCreateTime(new Date());
            scenario.setUpdateTime(new Date());

            scenarioMap.put(scenarioId, scenario);
            logger.info("创建场景成功，场景ID: {}", scenarioId);
            return ResultModel.success(scenario);
        } catch (Exception e) {
            logger.error("创建场景失败", e);
            return ResultModel.error("创建场景失败: " + e.getMessage(), 500);
        }
    }

    @PostMapping("/update")
    @ResponseBody
    public ResultModel<Scenario> updateScenario(@RequestBody Map<String, Object> request) {
        String scenarioId = (String) request.get("scenarioId");
        Map<String, Object> scenarioData = (Map<String, Object>) request.get("scenario");

        try {
            logger.info("更新场景请求，场景ID: {}", scenarioId);
            if (!scenarioMap.containsKey(scenarioId)) {
                logger.warn("场景不存在，场景ID: {}", scenarioId);
                return ResultModel.error("场景不存在", 404);
            }

            Scenario existingScenario = scenarioMap.get(scenarioId);
            if (scenarioData.containsKey("name")) {
                existingScenario.setName((String) scenarioData.get("name"));
            }
            if (scenarioData.containsKey("type")) {
                existingScenario.setType((String) scenarioData.get("type"));
            }
            if (scenarioData.containsKey("status")) {
                existingScenario.setStatus((String) scenarioData.get("status"));
            }
            if (scenarioData.containsKey("description")) {
                existingScenario.setDescription((String) scenarioData.get("description"));
            }
            if (scenarioData.containsKey("config")) {
                existingScenario.setConfig((Map<String, Object>) scenarioData.get("config"));
            }
            existingScenario.setUpdateTime(new Date());

            logger.info("更新场景成功，场景ID: {}", scenarioId);
            return ResultModel.success(existingScenario);
        } catch (Exception e) {
            logger.error("更新场景失败，场景ID: {}", scenarioId, e);
            return ResultModel.error("更新场景失败: " + e.getMessage(), 500);
        }
    }

    @PostMapping("/delete")
    @ResponseBody
    public ResultModel<Boolean> deleteScenario(@RequestBody Map<String, String> request) {
        String scenarioId = request.get("scenarioId");
        try {
            logger.info("删除场景请求，场景ID: {}", scenarioId);
            if (!scenarioMap.containsKey(scenarioId)) {
                logger.warn("场景不存在，场景ID: {}", scenarioId);
                return ResultModel.error("场景不存在", 404);
            }

            scenarioMap.remove(scenarioId);
            logger.info("删除场景成功，场景ID: {}", scenarioId);
            return ResultModel.success(true);
        } catch (Exception e) {
            logger.error("删除场景失败，场景ID: {}", scenarioId, e);
            return ResultModel.error("删除场景失败: " + e.getMessage(), 500);
        }
    }

    @PostMapping("/import")
    @ResponseBody
    public ResultModel<Map<String, Object>> importScenario(@RequestBody Map<String, Object> importData) {
        try {
            logger.info("导入场景请求，数据: {}", importData);

            if (importData == null || importData.isEmpty()) {
                logger.warn("导入数据为空");
                return ResultModel.error("导入数据为空", 400);
            }

            List<Scenario> importedScenarios = new ArrayList<>();
            int successCount = 0;
            int failCount = 0;
            List<String> errorMessages = new ArrayList<>();

            List<Map<String, Object>> scenariosToImport = null;

            if (importData.containsKey("scenarios")) {
                Object scenariosObj = importData.get("scenarios");
                if (scenariosObj instanceof List) {
                    scenariosToImport = (List<Map<String, Object>>) scenariosObj;
                }
            }

            if (scenariosToImport == null || scenariosToImport.isEmpty()) {
                logger.warn("未找到有效的场景数据");
                return ResultModel.error("未找到有效的场景数据", 400);
            }

            for (Map<String, Object> scenarioData : scenariosToImport) {
                try {
                    Scenario scenario = new Scenario();

                    if (scenarioData.containsKey("id")) {
                        scenario.setId((String) scenarioData.get("id"));
                    } else {
                        String newId = "scenario-" + String.format("%03d", scenarioMap.size() + 1);
                        scenario.setId(newId);
                    }

                    if (!scenarioData.containsKey("name") || scenarioData.get("name") == null) {
                        errorMessages.add("场景名称不能为空");
                        failCount++;
                        continue;
                    }
                    scenario.setName((String) scenarioData.get("name"));

                    if (!scenarioData.containsKey("type") || scenarioData.get("type") == null) {
                        errorMessages.add("场景类型不能为空");
                        failCount++;
                        continue;
                    }
                    scenario.setType((String) scenarioData.get("type"));

                    scenario.setStatus((String) scenarioData.getOrDefault("status", "active"));
                    scenario.setDescription((String) scenarioData.get("description"));
                    scenario.setCreateTime(new Date());
                    scenario.setUpdateTime(new Date());

                    if (scenarioData.containsKey("config")) {
                        scenario.setConfig((Map<String, Object>) scenarioData.get("config"));
                    } else {
                        scenario.setConfig(new ConcurrentHashMap<>());
                    }

                    scenarioMap.put(scenario.getId(), scenario);
                    importedScenarios.add(scenario);
                    successCount++;

                } catch (Exception e) {
                    String errorMsg = "导入场景失败 - " + e.getMessage();
                    logger.error(errorMsg, e);
                    errorMessages.add(errorMsg);
                    failCount++;
                }
            }

            Map<String, Object> result = new HashMap<>();
            result.put("imported", importedScenarios);
            result.put("successCount", successCount);
            result.put("failCount", failCount);
            result.put("errorMessages", errorMessages);

            logger.info("场景导入完成，成功: {}, 失败: {}", successCount, failCount);
            return ResultModel.success(result);
        } catch (Exception e) {
            logger.error("导入场景失败", e);
            return ResultModel.error("导入场景失败: " + e.getMessage(), 500);
        }
    }

    @PostMapping("/export")
    @ResponseBody
    public ResultModel<Map<String, Object>> exportScenario(@RequestBody Map<String, String> request) {
        String scenarioId = request.get("scenarioId");
        try {
            logger.info("导出场景请求，场景ID: {}", scenarioId);
            Map<String, Object> exportData = new HashMap<>();

            if (scenarioId != null && !scenarioId.isEmpty()) {
                Scenario scenario = scenarioMap.get(scenarioId);
                if (scenario == null) {
                    logger.warn("场景不存在，场景ID: {}", scenarioId);
                    return ResultModel.error("场景不存在", 404);
                }
                exportData.put("scenario", scenario);
            } else {
                exportData.put("scenarios", new ArrayList<>(scenarioMap.values()));
            }

            logger.info("导出场景成功");
            return ResultModel.success(exportData);
        } catch (Exception e) {
            logger.error("导出场景失败", e);
            return ResultModel.error("导出场景失败: " + e.getMessage(), 500);
        }
    }

    @PostMapping("/stats")
    @ResponseBody
    public ResultModel<Map<String, Object>> getScenarioStats() {
        try {
            logger.info("获取场景统计信息请求");

            int totalScenarios = scenarioMap.size();
            int activeScenarios = (int) scenarioMap.values().stream().filter(s -> "active".equals(s.getStatus())).count();
            long scenarioTypes = scenarioMap.values().stream().map(Scenario::getType).distinct().count();
            Date lastUpdate = scenarioMap.values().stream()
                    .map(Scenario::getUpdateTime)
                    .max(Date::compareTo)
                    .orElse(new Date());

            Map<String, Object> stats = new HashMap<>();
            stats.put("totalScenarios", totalScenarios);
            stats.put("activeScenarios", activeScenarios);
            stats.put("scenarioTypes", scenarioTypes);
            stats.put("lastUpdate", lastUpdate);

            logger.info("获取场景统计信息成功");
            return ResultModel.success(stats);
        } catch (Exception e) {
            logger.error("获取场景统计信息失败", e);
            return ResultModel.error("获取场景统计信息失败: " + e.getMessage(), 500);
        }
    }

    /**
     * 场景模型类
     */
    public static class Scenario {
        private String id;
        private String name;
        private String type;
        private String status;
        private Date createTime;
        private Date updateTime;
        private String description;
        private Map<String, Object> config;

        public Scenario(String id, String name, String type, String status, Date createTime, Date updateTime, String description, Map<String, Object> config) {
            this.id = id;
            this.name = name;
            this.type = type;
            this.status = status;
            this.createTime = createTime;
            this.updateTime = updateTime;
            this.description = description;
            this.config = config;
        }

        public Scenario() {
        }

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

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Date getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Date createTime) {
            this.createTime = createTime;
        }

        public Date getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(Date updateTime) {
            this.updateTime = updateTime;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Map<String, Object> getConfig() {
            return config;
        }

        public void setConfig(Map<String, Object> config) {
            this.config = config;
        }
    }
}
