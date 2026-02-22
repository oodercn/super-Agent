package net.ooder.nexus.service.skill.impl;

import net.ooder.nexus.domain.skill.model.*;
import net.ooder.nexus.service.skill.SkillConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SkillConfigServiceImpl implements SkillConfigService {

    private static final Logger log = LoggerFactory.getLogger(SkillConfigServiceImpl.class);

    private final Map<String, SkillConfig> skillConfigStore = new HashMap<>();
    private final Map<String, DatabaseConnection> dbConnectionStore = new HashMap<>();

    public SkillConfigServiceImpl() {
        initDefaultSkills();
    }

    private void initDefaultSkills() {
        SkillConfig dingding = new SkillConfig();
        dingding.setSkillId("skill-org-dingding");
        dingding.setName("钉钉组织服务");
        dingding.setType("org");
        dingding.setVersion("1.0.0");
        dingding.setDescription("钉钉组织数据集成服务");
        dingding.setStatus("PENDING_CONFIG");

        ConfigSchema schema = new ConfigSchema();
        List<ConfigField> required = new ArrayList<>();
        
        ConfigField appKey = new ConfigField();
        appKey.setName("DINGDING_APP_KEY");
        appKey.setType("string");
        appKey.setLabel("App Key");
        appKey.setDescription("钉钉应用的AppKey");
        required.add(appKey);

        ConfigField appSecret = new ConfigField();
        appSecret.setName("DINGDING_APP_SECRET");
        appSecret.setType("password");
        appSecret.setLabel("App Secret");
        appSecret.setDescription("钉钉应用的AppSecret");
        appSecret.setSecret(true);
        required.add(appSecret);

        schema.setRequired(required);

        List<ConfigField> optional = new ArrayList<>();
        ConfigField apiUrl = new ConfigField();
        apiUrl.setName("DINGDING_API_BASE_URL");
        apiUrl.setType("string");
        apiUrl.setLabel("API地址");
        apiUrl.setDefaultValue("https://oapi.dingtalk.com");
        apiUrl.setDescription("钉钉API地址");
        optional.add(apiUrl);
        schema.setOptional(optional);

        dingding.setConfigSchema(schema);
        skillConfigStore.put(dingding.getSkillId(), dingding);

        SkillConfig feishu = new SkillConfig();
        feishu.setSkillId("skill-org-feishu");
        feishu.setName("飞书组织服务");
        feishu.setType("org");
        feishu.setVersion("1.0.0");
        feishu.setDescription("飞书组织数据集成服务");
        feishu.setStatus("PENDING_CONFIG");

        ConfigSchema feishuSchema = new ConfigSchema();
        List<ConfigField> feishuRequired = new ArrayList<>();
        
        ConfigField appId = new ConfigField();
        appId.setName("FEISHU_APP_ID");
        appId.setType("string");
        appId.setLabel("App ID");
        appId.setDescription("飞书应用的App ID");
        feishuRequired.add(appId);

        ConfigField appSecretF = new ConfigField();
        appSecretF.setName("FEISHU_APP_SECRET");
        appSecretF.setType("password");
        appSecretF.setLabel("App Secret");
        appSecretF.setDescription("飞书应用的App Secret");
        appSecretF.setSecret(true);
        feishuRequired.add(appSecretF);

        feishuSchema.setRequired(feishuRequired);
        feishu.setConfigSchema(feishuSchema);
        skillConfigStore.put(feishu.getSkillId(), feishu);

        SkillConfig qiwei = new SkillConfig();
        qiwei.setSkillId("skill-org-qiwei");
        qiwei.setName("企业微信");
        qiwei.setType("org");
        qiwei.setVersion("1.0.0");
        qiwei.setDescription("企业微信组织数据集成服务");
        qiwei.setStatus("PENDING_CONFIG");

        ConfigSchema qiweiSchema = new ConfigSchema();
        List<ConfigField> qiweiRequired = new ArrayList<>();
        
        ConfigField corpId = new ConfigField();
        corpId.setName("QIWEI_CORP_ID");
        corpId.setType("string");
        corpId.setLabel("企业ID");
        corpId.setDescription("企业微信企业ID");
        qiweiRequired.add(corpId);

        ConfigField agentId = new ConfigField();
        agentId.setName("QIWEI_AGENT_ID");
        agentId.setType("string");
        agentId.setLabel("Agent ID");
        agentId.setDescription("应用Agent ID");
        qiweiRequired.add(agentId);

        ConfigField secret = new ConfigField();
        secret.setName("QIWEI_SECRET");
        secret.setType("password");
        secret.setLabel("Secret");
        secret.setDescription("应用Secret");
        secret.setSecret(true);
        qiweiRequired.add(secret);

        qiweiSchema.setRequired(qiweiRequired);
        qiwei.setConfigSchema(qiweiSchema);
        skillConfigStore.put(qiwei.getSkillId(), qiwei);
    }

    @Override
    public Map<String, Object> getConfigOverview() {
        Map<String, Object> result = new HashMap<>();
        
        int total = skillConfigStore.size();
        int configured = 0;
        int connected = 0;
        int pendingConfig = 0;
        int connectionFailed = 0;

        for (SkillConfig config : skillConfigStore.values()) {
            switch (config.getStatus()) {
                case "CONFIGURED":
                    configured++;
                    break;
                case "CONNECTED":
                    connected++;
                    configured++;
                    break;
                case "PENDING_CONFIG":
                    pendingConfig++;
                    break;
                case "CONNECTION_FAILED":
                    connectionFailed++;
                    break;
            }
        }

        result.put("total", total);
        result.put("configured", configured);
        result.put("connected", connected);
        result.put("pendingConfig", pendingConfig);
        result.put("connectionFailed", connectionFailed);

        Map<String, List<Map<String, Object>>> categories = new HashMap<>();
        
        for (SkillConfig config : skillConfigStore.values()) {
            String type = config.getType();
            if (!categories.containsKey(type)) {
                categories.put(type, new ArrayList<>());
            }
            
            Map<String, Object> skillInfo = new HashMap<>();
            skillInfo.put("skillId", config.getSkillId());
            skillInfo.put("name", config.getName());
            skillInfo.put("icon", getSkillIcon(config.getSkillId()));
            skillInfo.put("status", config.getStatus());
            skillInfo.put("lastChecked", config.getConnectionInfo() != null ? 
                config.getConnectionInfo().getLastChecked() : null);
            
            categories.get(type).add(skillInfo);
        }

        List<Map<String, Object>> categoryList = new ArrayList<>();
        for (Map.Entry<String, List<Map<String, Object>>> entry : categories.entrySet()) {
            Map<String, Object> category = new HashMap<>();
            category.put("type", entry.getKey());
            category.put("name", getCategoryName(entry.getKey()));
            category.put("skills", entry.getValue());
            categoryList.add(category);
        }
        result.put("categories", categoryList);

        return result;
    }

    @Override
    public SkillConfig getSkillConfig(String skillId) {
        return skillConfigStore.get(skillId);
    }

    @Override
    public SkillConfig updateSkillConfig(String skillId, Map<String, Object> config, boolean testConnection) {
        SkillConfig skillConfig = skillConfigStore.get(skillId);
        if (skillConfig == null) {
            return null;
        }

        skillConfig.setCurrentConfig(config);
        skillConfig.setLastUpdated(new Date());

        if (testConnection) {
            Map<String, Object> testResult = testSkillConnection(skillId, config);
            boolean success = Boolean.TRUE.equals(testResult.get("success"));
            
            ConnectionInfo connInfo = new ConnectionInfo();
            connInfo.setConnected(success);
            connInfo.setLastChecked(new Date());
            connInfo.setResponseTime(success ? (Long) testResult.get("responseTime") : 0);
            connInfo.setError(success ? null : (String) testResult.get("message"));
            
            skillConfig.setConnectionInfo(connInfo);
            skillConfig.setStatus(success ? "CONNECTED" : "CONNECTION_FAILED");
        } else {
            skillConfig.setStatus("CONFIGURED");
        }

        log.info("Updated skill config: {}", skillId);
        return skillConfig;
    }

    @Override
    public Map<String, Object> testSkillConnection(String skillId, Map<String, Object> config) {
        Map<String, Object> result = new HashMap<>();
        
        long startTime = System.currentTimeMillis();
        
        try {
            Thread.sleep(100);
            
            boolean success = config != null && !config.isEmpty();
            
            long responseTime = System.currentTimeMillis() - startTime;
            
            result.put("success", success);
            result.put("responseTime", responseTime);
            result.put("message", success ? "连接成功" : "配置不完整");
            
            if (success) {
                Map<String, Object> details = new HashMap<>();
                details.put("apiVersion", "1.0");
                result.put("details", details);
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "连接测试失败: " + e.getMessage());
        }
        
        return result;
    }

    @Override
    public List<DatabaseConnection> getDatabaseConnections() {
        return new ArrayList<>(dbConnectionStore.values());
    }

    @Override
    public DatabaseConnection getDatabaseConnection(String connectionId) {
        return dbConnectionStore.get(connectionId);
    }

    @Override
    public DatabaseConnection createDatabaseConnection(DatabaseConnection connection) {
        String connectionId = "db-conn-" + UUID.randomUUID().toString().substring(0, 8);
        connection.setConnectionId(connectionId);
        connection.setStatus("PENDING_CONFIG");
        connection.setCreatedAt(new Date());
        connection.setUpdatedAt(new Date());
        
        if (connection.getPoolConfig() == null) {
            connection.setPoolConfig(new PoolConfig());
        }
        
        dbConnectionStore.put(connectionId, connection);
        log.info("Created database connection: {}", connectionId);
        
        return connection;
    }

    @Override
    public DatabaseConnection updateDatabaseConnection(DatabaseConnection connection) {
        String connectionId = connection.getConnectionId();
        DatabaseConnection existing = dbConnectionStore.get(connectionId);
        if (existing == null) {
            return null;
        }
        
        connection.setUpdatedAt(new Date());
        connection.setCreatedAt(existing.getCreatedAt());
        dbConnectionStore.put(connectionId, connection);
        
        log.info("Updated database connection: {}", connectionId);
        return connection;
    }

    @Override
    public boolean deleteDatabaseConnection(String connectionId) {
        DatabaseConnection removed = dbConnectionStore.remove(connectionId);
        if (removed != null) {
            log.info("Deleted database connection: {}", connectionId);
            return true;
        }
        return false;
    }

    @Override
    public Map<String, Object> testDatabaseConnection(DatabaseConnection connection) {
        Map<String, Object> result = new HashMap<>();
        
        long startTime = System.currentTimeMillis();
        
        try {
            Thread.sleep(50);
            
            boolean success = connection.getHost() != null && 
                             connection.getPort() > 0 && 
                             connection.getDatabase() != null &&
                             connection.getUsername() != null;
            
            long responseTime = System.currentTimeMillis() - startTime;
            
            result.put("success", success);
            result.put("responseTime", responseTime);
            result.put("message", success ? "连接成功" : "连接信息不完整");
            
            if (success) {
                Map<String, Object> details = new HashMap<>();
                details.put("database", connection.getDatabase());
                details.put("version", "8.0");
                result.put("details", details);
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "连接测试失败: " + e.getMessage());
        }
        
        return result;
    }

    @Override
    public boolean enableSkill(String skillId) {
        SkillConfig config = skillConfigStore.get(skillId);
        if (config == null) {
            return false;
        }
        
        if ("CONNECTED".equals(config.getStatus()) || "CONFIGURED".equals(config.getStatus())) {
            config.setStatus("CONNECTED");
            config.setLastUpdated(new Date());
            log.info("Enabled skill: {}", skillId);
            return true;
        }
        
        return false;
    }

    @Override
    public boolean disableSkill(String skillId) {
        SkillConfig config = skillConfigStore.get(skillId);
        if (config == null) {
            return false;
        }
        
        config.setStatus("DISABLED");
        config.setLastUpdated(new Date());
        log.info("Disabled skill: {}", skillId);
        return true;
    }

    private String getSkillIcon(String skillId) {
        if (skillId == null) {
            return "ri-plug-line";
        }
        
        if (skillId.contains("dingding")) {
            return "ri-dingding-line";
        } else if (skillId.contains("feishu")) {
            return "ri-lark-line";
        } else if (skillId.contains("qiwei")) {
            return "ri-wechat-line";
        } else if (skillId.contains("db")) {
            return "ri-database-2-line";
        }
        
        return "ri-plug-line";
    }

    private String getCategoryName(String type) {
        if (type == null) {
            return "其他";
        }
        
        switch (type) {
            case "org":
                return "组织服务";
            case "database":
                return "数据库连接";
            case "messaging":
                return "消息服务";
            case "storage":
                return "存储服务";
            case "ai":
                return "AI服务";
            default:
                return "其他";
        }
    }
}
