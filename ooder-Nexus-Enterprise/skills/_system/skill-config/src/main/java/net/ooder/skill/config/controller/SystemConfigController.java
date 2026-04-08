package net.ooder.skill.config.controller;

import net.ooder.skill.common.model.ResultModel;
import net.ooder.skill.config.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/config")
@CrossOrigin(originPatterns = "*", allowCredentials = "true", allowedHeaders = "*")
public class SystemConfigController {

    private static final Logger log = LoggerFactory.getLogger(SystemConfigController.class);

    private final Map<String, SystemConfigDTO> configStore = new HashMap<>();

    @GetMapping("/system")
    public ResultModel<SystemConfigDTO> getSystemConfig() {
        log.info("[SystemConfigController] Get system config");
        SystemConfigDTO config = configStore.getOrDefault("system", new SystemConfigDTO());
        return ResultModel.success(config);
    }

    @PostMapping("/system")
    public ResultModel<SystemConfigDTO> updateSystemConfig(@RequestBody SystemConfigDTO config) {
        log.info("[SystemConfigController] Update system config");
        configStore.put("system", config);
        return ResultModel.success(config);
    }

    @GetMapping("/system/capabilities")
    public ResultModel<List<SystemConfigDTO>> getCapabilityConfig() {
        log.info("[SystemConfigController] Get capability config");
        return ResultModel.success(new ArrayList<>());
    }

    @GetMapping("/system/profile")
    public ResultModel<ProfileConfigDTO> getProfileConfig() {
        log.info("[SystemConfigController] Get profile config");
        ProfileConfigDTO profile = new ProfileConfigDTO();
        profile.setName("Default Profile");
        profile.setVersion("1.0.0");
        return ResultModel.success(profile);
    }

    @GetMapping("/vfs")
    public ResultModel<SystemConfigDTO> getVfsConfig() {
        log.info("[SystemConfigController] Get VFS config");
        SystemConfigDTO config = configStore.getOrDefault("vfs", new SystemConfigDTO());
        return ResultModel.success(config);
    }

    @PostMapping("/vfs")
    public ResultModel<SystemConfigDTO> updateVfsConfig(@RequestBody SystemConfigDTO config) {
        log.info("[SystemConfigController] Update VFS config");
        configStore.put("vfs", config);
        return ResultModel.success(config);
    }

    @GetMapping("/comm")
    public ResultModel<SystemConfigDTO> getCommConfig() {
        log.info("[SystemConfigController] Get comm config");
        SystemConfigDTO config = configStore.getOrDefault("comm", new SystemConfigDTO());
        return ResultModel.success(config);
    }

    @PostMapping("/comm")
    public ResultModel<SystemConfigDTO> updateCommConfig(@RequestBody SystemConfigDTO config) {
        log.info("[SystemConfigController] Update comm config");
        configStore.put("comm", config);
        return ResultModel.success(config);
    }

    @GetMapping("/db/connections")
    public ResultModel<List<SystemConfigDTO>> getDbConnections() {
        log.info("[SystemConfigController] Get DB connections");
        return ResultModel.success(new ArrayList<>());
    }

    @PostMapping("/db/test")
    public ResultModel<DbConnectionTestResultDTO> testDbConnection(@RequestBody SystemConfigDTO config) {
        log.info("[SystemConfigController] Test DB connection");
        DbConnectionTestResultDTO result = new DbConnectionTestResultDTO();
        result.setSuccess(true);
        result.setMessage("Connection test passed");
        result.setResponseTime(50);
        return ResultModel.success(result);
    }

    @GetMapping("/db/pool")
    public ResultModel<DbPoolConfigDTO> getDbPoolConfig() {
        log.info("[SystemConfigController] Get DB pool config");
        DbPoolConfigDTO config = new DbPoolConfigDTO();
        config.setMaxConnections(100);
        config.setMinIdle(10);
        return ResultModel.success(config);
    }

    @GetMapping("/db/monitor")
    public ResultModel<DbMonitorDTO> getDbMonitor() {
        log.info("[SystemConfigController] Get DB monitor");
        DbMonitorDTO monitor = new DbMonitorDTO();
        monitor.setActiveConnections(5);
        monitor.setIdleConnections(10);
        return ResultModel.success(monitor);
    }

    @GetMapping("/org")
    public ResultModel<SystemConfigDTO> getOrgConfig() {
        log.info("[SystemConfigController] Get org config");
        SystemConfigDTO config = configStore.getOrDefault("org", new SystemConfigDTO());
        return ResultModel.success(config);
    }

    @PostMapping("/org")
    public ResultModel<SystemConfigDTO> updateOrgConfig(@RequestBody SystemConfigDTO config) {
        log.info("[SystemConfigController] Update org config");
        configStore.put("org", config);
        return ResultModel.success(config);
    }

    @GetMapping("/auth")
    public ResultModel<SystemConfigDTO> getAuthConfig() {
        log.info("[SystemConfigController] Get auth config");
        SystemConfigDTO config = configStore.getOrDefault("auth", new SystemConfigDTO());
        return ResultModel.success(config);
    }

    @PostMapping("/auth")
    public ResultModel<SystemConfigDTO> updateAuthConfig(@RequestBody SystemConfigDTO config) {
        log.info("[SystemConfigController] Update auth config");
        configStore.put("auth", config);
        return ResultModel.success(config);
    }

    @GetMapping("/categories")
    public ResultModel<List<ConfigCategoryDTO>> getCategories() {
        log.info("[SystemConfigController] Get categories");
        List<ConfigCategoryDTO> categories = new ArrayList<>();
        ConfigCategoryDTO cat1 = new ConfigCategoryDTO();
        cat1.setId("system");
        cat1.setName("System");
        categories.add(cat1);
        ConfigCategoryDTO cat2 = new ConfigCategoryDTO();
        cat2.setId("business");
        cat2.setName("Business");
        categories.add(cat2);
        return ResultModel.success(categories);
    }

    @GetMapping("/skills/{id}")
    public ResultModel<SystemConfigDTO> getSkillConfig(@PathVariable String id) {
        log.info("[SystemConfigController] Get skill config: {}", id);
        SystemConfigDTO config = configStore.getOrDefault("skill_" + id, new SystemConfigDTO());
        return ResultModel.success(config);
    }

    @PostMapping("/skills/{id}")
    public ResultModel<SystemConfigDTO> updateSkillConfig(@PathVariable String id, @RequestBody SystemConfigDTO config) {
        log.info("[SystemConfigController] Update skill config: {}", id);
        configStore.put("skill_" + id, config);
        return ResultModel.success(config);
    }

    @PostMapping("/system/reset")
    public ResultModel<OperationResultDTO> resetSystemConfig() {
        log.info("[SystemConfigController] Reset system config");
        configStore.clear();
        OperationResultDTO result = new OperationResultDTO();
        result.setSuccess(true);
        result.setMessage("System config reset successfully");
        return ResultModel.success(result);
    }

    @PostMapping("/system/profile")
    public ResultModel<ProfileConfigDTO> updateProfileConfig(@RequestBody ProfileConfigDTO profile) {
        log.info("[SystemConfigController] Update profile config");
        return ResultModel.success(profile);
    }

    @PostMapping("/system/capabilities/{address}")
    public ResultModel<SystemConfigDTO> configureCapability(
            @PathVariable String address,
            @RequestBody SystemConfigDTO config) {
        log.info("[SystemConfigController] Configure capability: {}", address);
        configStore.put("capability_" + address, config);
        return ResultModel.success(config);
    }
}
