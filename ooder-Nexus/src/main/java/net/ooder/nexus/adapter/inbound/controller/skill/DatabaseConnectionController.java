package net.ooder.nexus.adapter.inbound.controller.skill;

import net.ooder.nexus.domain.skill.model.DatabaseConnection;
import net.ooder.nexus.service.skill.SkillConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/skills/database/connections")
public class DatabaseConnectionController {

    private static final Logger log = LoggerFactory.getLogger(DatabaseConnectionController.class);

    @Autowired
    private SkillConfigService skillConfigService;

    @GetMapping
    public Map<String, Object> getConnections() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<DatabaseConnection> connections = skillConfigService.getDatabaseConnections();
            result.put("requestStatus", 200);
            result.put("data", new HashMap<String, Object>() {{
                put("connections", connections);
            }});
        } catch (Exception e) {
            log.error("Failed to get database connections", e);
            result.put("requestStatus", 500);
            result.put("message", "获取连接列表失败: " + e.getMessage());
        }
        return result;
    }

    @GetMapping("/{connectionId}")
    public Map<String, Object> getConnection(@PathVariable String connectionId) {
        Map<String, Object> result = new HashMap<>();
        try {
            DatabaseConnection connection = skillConfigService.getDatabaseConnection(connectionId);
            if (connection != null) {
                result.put("requestStatus", 200);
                result.put("data", connection);
            } else {
                result.put("requestStatus", 404);
                result.put("message", "连接不存在");
            }
        } catch (Exception e) {
            log.error("Failed to get database connection", e);
            result.put("requestStatus", 500);
            result.put("message", "获取连接失败: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/create")
    public Map<String, Object> createConnection(@RequestBody DatabaseConnection connection) {
        Map<String, Object> result = new HashMap<>();
        try {
            DatabaseConnection created = skillConfigService.createDatabaseConnection(connection);
            result.put("requestStatus", 200);
            result.put("message", "连接创建成功");
            result.put("data", created);
        } catch (Exception e) {
            log.error("Failed to create database connection", e);
            result.put("requestStatus", 500);
            result.put("message", "创建连接失败: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/update")
    public Map<String, Object> updateConnection(@RequestBody DatabaseConnection connection) {
        Map<String, Object> result = new HashMap<>();
        try {
            DatabaseConnection updated = skillConfigService.updateDatabaseConnection(connection);
            if (updated != null) {
                result.put("requestStatus", 200);
                result.put("message", "连接更新成功");
                result.put("data", updated);
            } else {
                result.put("requestStatus", 404);
                result.put("message", "连接不存在");
            }
        } catch (Exception e) {
            log.error("Failed to update database connection", e);
            result.put("requestStatus", 500);
            result.put("message", "更新连接失败: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/delete")
    public Map<String, Object> deleteConnection(@RequestBody Map<String, String> request) {
        Map<String, Object> result = new HashMap<>();
        try {
            String connectionId = request.get("connectionId");
            boolean deleted = skillConfigService.deleteDatabaseConnection(connectionId);
            if (deleted) {
                result.put("requestStatus", 200);
                result.put("message", "连接删除成功");
            } else {
                result.put("requestStatus", 404);
                result.put("message", "连接不存在");
            }
        } catch (Exception e) {
            log.error("Failed to delete database connection", e);
            result.put("requestStatus", 500);
            result.put("message", "删除连接失败: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/test")
    public Map<String, Object> testConnection(@RequestBody DatabaseConnection connection) {
        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, Object> testResult = skillConfigService.testDatabaseConnection(connection);
            result.put("requestStatus", 200);
            result.put("data", testResult);
        } catch (Exception e) {
            log.error("Failed to test database connection", e);
            result.put("requestStatus", 500);
            result.put("message", "连接测试失败: " + e.getMessage());
        }
        return result;
    }
}
