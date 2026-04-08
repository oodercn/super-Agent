package net.ooder.mvp.skill.scene.config.controller;

import net.ooder.mvp.skill.scene.config.service.DbConfigService;
import net.ooder.mvp.skill.scene.dto.PageResult;
import net.ooder.mvp.skill.scene.dto.db.DbConnectionDTO;
import net.ooder.mvp.skill.scene.dto.db.DbPoolConfigDTO;
import net.ooder.mvp.skill.scene.dto.db.DbTestRequestDTO;
import net.ooder.mvp.skill.scene.dto.db.DbMonitorDTO;
import net.ooder.mvp.skill.scene.dto.config.DbDriverDTO;
import net.ooder.mvp.skill.scene.dto.config.TestResultDTO;
import net.ooder.mvp.skill.scene.model.ResultModel;
import net.ooder.mvp.skill.scene.service.AuditService;
import net.ooder.mvp.skill.scene.dto.audit.AuditLogDTO;
import net.ooder.mvp.skill.scene.dto.audit.AuditEventType;
import net.ooder.mvp.skill.scene.dto.audit.AuditResultType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/config/db")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class DbConfigController {

    private static final Logger log = LoggerFactory.getLogger(DbConfigController.class);

    @Autowired(required = false)
    private DbConfigService dbConfigService;

    @Autowired(required = false)
    private AuditService auditService;

    @GetMapping("/connections")
    public ResultModel<List<DbConnectionDTO>> listConnections() {
        try {
            if (dbConfigService != null) {
                List<DbConnectionDTO> connections = dbConfigService.listConnections();
                return ResultModel.success(connections);
            }
            return ResultModel.success(Collections.emptyList());
        } catch (Exception e) {
            log.error("Failed to list db connections", e);
            return ResultModel.error(500, "获取数据库连接列表失败: " + e.getMessage());
        }
    }

    @GetMapping("/connections/{id}")
    public ResultModel<DbConnectionDTO> getConnection(@PathVariable String id) {
        try {
            if (dbConfigService != null) {
                DbConnectionDTO connection = dbConfigService.getConnection(id);
                if (connection == null) {
                    return ResultModel.notFound("数据库连接不存在");
                }
                return ResultModel.success(connection);
            }
            return ResultModel.notFound("数据库连接不存在");
        } catch (Exception e) {
            log.error("Failed to get db connection", e);
            return ResultModel.error(500, "获取数据库连接失败: " + e.getMessage());
        }
    }

    @PutMapping("/connections/{id}")
    public ResultModel<DbConnectionDTO> saveConnection(
            @PathVariable String id,
            @RequestBody DbConnectionDTO connection) {
        try {
            connection.setId(id);
            if (dbConfigService != null) {
                DbConnectionDTO saved = dbConfigService.saveConnection(connection);
                logConfigChange("db_connection", id, "update", "更新数据库连接配置: " + connection.getName());
                return ResultModel.success(saved);
            }
            return ResultModel.success(connection);
        } catch (Exception e) {
            log.error("Failed to save db connection", e);
            return ResultModel.error(500, "保存数据库连接失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/connections/{id}")
    public ResultModel<Void> deleteConnection(@PathVariable String id) {
        try {
            if (dbConfigService != null) {
                dbConfigService.deleteConnection(id);
                logConfigChange("db_connection", id, "delete", "删除数据库连接配置");
            }
            return ResultModel.success("删除成功", null);
        } catch (Exception e) {
            log.error("Failed to delete db connection", e);
            return ResultModel.error(500, "删除数据库连接失败: " + e.getMessage());
        }
    }

    @PostMapping("/test")
    public ResultModel<TestResultDTO> testConnection(@RequestBody DbTestRequestDTO request) {
        try {
            if (dbConfigService != null) {
                long start = System.currentTimeMillis();
                boolean success = dbConfigService.testConnection(request);
                long responseTime = System.currentTimeMillis() - start;
                
                if (success) {
                    TestResultDTO result = new TestResultDTO();
                    result.setSuccess(true);
                    result.setResponseTime(responseTime + "ms");
                    return ResultModel.success(result);
                } else {
                    return ResultModel.error(500, "连接测试失败");
                }
            }
            TestResultDTO mockResult = new TestResultDTO();
            mockResult.setSuccess(true);
            mockResult.setResponseTime("0ms");
            mockResult.setMessage("模拟测试成功（服务未启动）");
            return ResultModel.success(mockResult);
        } catch (Exception e) {
            log.error("Failed to test db connection", e);
            return ResultModel.error(500, "连接测试失败: " + e.getMessage());
        }
    }

    @GetMapping("/pool")
    public ResultModel<DbPoolConfigDTO> getPoolConfig() {
        try {
            if (dbConfigService != null) {
                DbPoolConfigDTO config = dbConfigService.getPoolConfig();
                return ResultModel.success(config);
            }
            DbPoolConfigDTO defaultConfig = new DbPoolConfigDTO();
            return ResultModel.success(defaultConfig);
        } catch (Exception e) {
            log.error("Failed to get pool config", e);
            return ResultModel.error(500, "获取连接池配置失败: " + e.getMessage());
        }
    }

    @PutMapping("/pool")
    public ResultModel<Void> savePoolConfig(@RequestBody DbPoolConfigDTO config) {
        try {
            if (dbConfigService != null) {
                dbConfigService.savePoolConfig(config);
                logConfigChange("db_pool", "global", "update", "更新数据库连接池配置");
            }
            return ResultModel.success("保存成功", null);
        } catch (Exception e) {
            log.error("Failed to save pool config", e);
            return ResultModel.error(500, "保存连接池配置失败: " + e.getMessage());
        }
    }

    @GetMapping("/monitor")
    public ResultModel<DbMonitorDTO> getMonitor() {
        try {
            if (dbConfigService != null) {
                DbMonitorDTO monitor = dbConfigService.getMonitor();
                return ResultModel.success(monitor);
            }
            DbMonitorDTO mockMonitor = new DbMonitorDTO();
            mockMonitor.setActiveConnections(8);
            mockMonitor.setIdleConnections(12);
            mockMonitor.setPendingConnections(0);
            mockMonitor.setTotalConnections(20);
            mockMonitor.setAvgResponseTime(15L);
            mockMonitor.setPoolUsagePercent(40.0);
            return ResultModel.success(mockMonitor);
        } catch (Exception e) {
            log.error("Failed to get monitor data", e);
            return ResultModel.error(500, "获取监控数据失败: " + e.getMessage());
        }
    }

    @GetMapping("/drivers")
    public ResultModel<List<DbDriverDTO>> getDrivers() {
        List<DbDriverDTO> drivers = Arrays.asList(
            createDriverInfo("mysql", "MySQL", "com.mysql.cj.jdbc.Driver", 3306, "MySQL数据库"),
            createDriverInfo("postgresql", "PostgreSQL", "org.postgresql.Driver", 5432, "PostgreSQL数据库"),
            createDriverInfo("sqlite", "SQLite", "org.sqlite.JDBC", null, "嵌入式SQLite数据库"),
            createDriverInfo("oracle", "Oracle", "oracle.jdbc.OracleDriver", 1521, "Oracle数据库"),
            createDriverInfo("sqlserver", "SQL Server", "com.microsoft.sqlserver.jdbc.SQLServerDriver", 1433, "Microsoft SQL Server"),
            createDriverInfo("dm", "达梦", "dm.jdbc.driver.DmDriver", 5236, "达梦数据库"),
            createDriverInfo("kingbase", "人大金仓", "com.kingbase8.Driver", 54321, "人大金仓数据库"),
            createDriverInfo("gaussdb", "GaussDB", "org.opengauss.Driver", 8000, "华为GaussDB")
        );
        return ResultModel.success(drivers);
    }

    private DbDriverDTO createDriverInfo(String type, String name, String driverClass, Integer defaultPort, String description) {
        DbDriverDTO info = new DbDriverDTO();
        info.setType(type);
        info.setName(name);
        info.setDriverClass(driverClass);
        info.setDefaultPort(defaultPort);
        info.setDescription(description);
        return info;
    }

    private void logConfigChange(String resourceType, String resourceId, String action, String detail) {
        if (auditService != null) {
            try {
                AuditLogDTO log = new AuditLogDTO();
                log.setEventType(AuditEventType.CONFIG_CHANGE);
                log.setResult(AuditResultType.SUCCESS);
                log.setResourceType(resourceType);
                log.setResourceId(resourceId);
                log.setAction(action);
                log.setDetail(detail);
                auditService.logEvent(log);
            } catch (Exception e) {
                DbConfigController.log.warn("Failed to log audit event", e);
            }
        }
    }
}
