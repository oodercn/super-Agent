package net.ooder.nexus.adapter.inbound.controller.skill;

import net.ooder.nexus.domain.skill.model.DatabaseConnection;
import net.ooder.nexus.dto.skill.*;
import net.ooder.nexus.model.ApiResponse;
import net.ooder.nexus.service.skill.SkillConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/skills/database/connections")
public class DatabaseConnectionController {

    private static final Logger log = LoggerFactory.getLogger(DatabaseConnectionController.class);

    @Autowired
    private SkillConfigService skillConfigService;

    @GetMapping
    public ApiResponse<DatabaseConnectionListDTO> getConnections() {
        try {
            List<DatabaseConnection> connections = skillConfigService.getDatabaseConnections();
            DatabaseConnectionListDTO data = new DatabaseConnectionListDTO();
            data.setConnections(connections);
            return ApiResponse.success(data);
        } catch (Exception e) {
            log.error("Failed to get database connections", e);
            return ApiResponse.error("获取连接列表失败: " + e.getMessage());
        }
    }

    @GetMapping("/{connectionId}")
    public ApiResponse<DatabaseConnection> getConnection(@PathVariable String connectionId) {
        try {
            DatabaseConnection connection = skillConfigService.getDatabaseConnection(connectionId);
            if (connection != null) {
                return ApiResponse.success(connection);
            } else {
                return ApiResponse.notFound("连接不存在");
            }
        } catch (Exception e) {
            log.error("Failed to get database connection", e);
            return ApiResponse.error("获取连接失败: " + e.getMessage());
        }
    }

    @PostMapping("/create")
    public ApiResponse<DatabaseConnection> createConnection(@RequestBody DatabaseConnection connection) {
        try {
            DatabaseConnection created = skillConfigService.createDatabaseConnection(connection);
            return ApiResponse.success("连接创建成功", created);
        } catch (Exception e) {
            log.error("Failed to create database connection", e);
            return ApiResponse.error("创建连接失败: " + e.getMessage());
        }
    }

    @PostMapping("/update")
    public ApiResponse<DatabaseConnection> updateConnection(@RequestBody DatabaseConnection connection) {
        try {
            DatabaseConnection updated = skillConfigService.updateDatabaseConnection(connection);
            if (updated != null) {
                return ApiResponse.success("连接更新成功", updated);
            } else {
                return ApiResponse.notFound("连接不存在");
            }
        } catch (Exception e) {
            log.error("Failed to update database connection", e);
            return ApiResponse.error("更新连接失败: " + e.getMessage());
        }
    }

    @PostMapping("/delete")
    public ApiResponse<Void> deleteConnection(@RequestBody DatabaseConnectionDeleteDTO request) {
        try {
            String connectionId = request.getConnectionId();
            boolean deleted = skillConfigService.deleteDatabaseConnection(connectionId);
            if (deleted) {
                return ApiResponse.success("连接删除成功");
            } else {
                return ApiResponse.notFound("连接不存在");
            }
        } catch (Exception e) {
            log.error("Failed to delete database connection", e);
            return ApiResponse.error("删除连接失败: " + e.getMessage());
        }
    }

    @PostMapping("/test")
    public ApiResponse<DatabaseTestResultDTO> testConnection(@RequestBody DatabaseConnection connection) {
        try {
            Map<String, Object> testResult = skillConfigService.testDatabaseConnection(connection);
            DatabaseTestResultDTO dto = convertToTestResultDTO(testResult);
            return ApiResponse.success(dto);
        } catch (Exception e) {
            log.error("Failed to test database connection", e);
            return ApiResponse.error("连接测试失败: " + e.getMessage());
        }
    }

    private DatabaseTestResultDTO convertToTestResultDTO(Map<String, Object> map) {
        DatabaseTestResultDTO dto = new DatabaseTestResultDTO();
        
        Object success = map.get("success");
        if (success instanceof Boolean) {
            dto.setSuccess((Boolean) success);
        }
        
        dto.setMessage((String) map.get("message"));
        
        Object responseTime = map.get("responseTime");
        if (responseTime instanceof Number) {
            dto.setResponseTime(((Number) responseTime).longValue());
        }
        
        dto.setDatabaseVersion((String) map.get("databaseVersion"));
        dto.setError((String) map.get("error"));
        
        return dto;
    }
}
