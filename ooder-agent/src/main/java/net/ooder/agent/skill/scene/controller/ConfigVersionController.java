package net.ooder.mvp.skill.scene.controller;

import net.ooder.mvp.skill.scene.config.ConfigVersion;
import net.ooder.mvp.skill.scene.config.ConfigVersionService;
import net.ooder.mvp.skill.scene.config.ConfigVersionService.ConfigDiff;
import net.ooder.mvp.skill.scene.model.ResultModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/config/versions")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ConfigVersionController {

    private static final Logger log = LoggerFactory.getLogger(ConfigVersionController.class);

    @Autowired
    private ConfigVersionService configVersionService;

    @GetMapping("/{sceneId}")
    public ResultModel<List<ConfigVersion>> getVersionHistory(@PathVariable String sceneId) {
        log.info("[getVersionHistory] Getting version history for scene: {}", sceneId);
        List<ConfigVersion> versions = configVersionService.getVersionHistory(sceneId);
        return ResultModel.success(versions);
    }

    @GetMapping("/{sceneId}/active")
    public ResultModel<ConfigVersion> getActiveVersion(@PathVariable String sceneId) {
        log.info("[getActiveVersion] Getting active version for scene: {}", sceneId);
        ConfigVersion version = configVersionService.getActiveVersion(sceneId);
        if (version == null) {
            return ResultModel.notFound("No active version for scene: " + sceneId);
        }
        return ResultModel.success(version);
    }

    @GetMapping("/{sceneId}/{versionId}")
    public ResultModel<ConfigVersion> getVersion(
            @PathVariable String sceneId,
            @PathVariable String versionId) {
        log.info("[getVersion] Getting version {} for scene: {}", versionId, sceneId);
        ConfigVersion version = configVersionService.getVersion(sceneId, versionId);
        if (version == null) {
            return ResultModel.notFound("Version not found: " + versionId);
        }
        return ResultModel.success(version);
    }

    @PostMapping("/{sceneId}/save")
    public ResultModel<ConfigVersion> saveVersion(
            @PathVariable String sceneId,
            @RequestBody SaveVersionRequest request) {
        log.info("[saveVersion] Saving version for scene: {}", sceneId);
        try {
            ConfigVersion version = configVersionService.saveVersion(
                sceneId,
                request.getConfig(),
                request.getOperator(),
                request.getDescription()
            );
            return ResultModel.success(version);
        } catch (Exception e) {
            log.error("[saveVersion] Failed to save version: {}", e.getMessage());
            return ResultModel.error(500, "Failed to save version: " + e.getMessage());
        }
    }

    @PostMapping("/{sceneId}/rollback/{versionId}")
    public ResultModel<ConfigVersion> rollback(
            @PathVariable String sceneId,
            @PathVariable String versionId) {
        log.info("[rollback] Rolling back scene {} to version {}", sceneId, versionId);
        try {
            ConfigVersion version = configVersionService.rollback(sceneId, versionId);
            return ResultModel.success(version);
        } catch (Exception e) {
            log.error("[rollback] Rollback failed: {}", e.getMessage());
            return ResultModel.error(500, "Rollback failed: " + e.getMessage());
        }
    }

    @GetMapping("/{sceneId}/compare/{versionId1}/{versionId2}")
    public ResultModel<ConfigDiff> compareVersions(
            @PathVariable String sceneId,
            @PathVariable String versionId1,
            @PathVariable String versionId2) {
        log.info("[compareVersions] Comparing versions {} and {} for scene {}", 
            versionId1, versionId2, sceneId);
        try {
            ConfigDiff diff = configVersionService.compareVersions(sceneId, versionId1, versionId2);
            return ResultModel.success(diff);
        } catch (Exception e) {
            log.error("[compareVersions] Compare failed: {}", e.getMessage());
            return ResultModel.error(500, "Compare failed: " + e.getMessage());
        }
    }

    @DeleteMapping("/{sceneId}/{versionId}")
    public ResultModel<Boolean> deleteVersion(
            @PathVariable String sceneId,
            @PathVariable String versionId) {
        log.info("[deleteVersion] Deleting version {} for scene {}", versionId, sceneId);
        try {
            configVersionService.deleteVersion(sceneId, versionId);
            return ResultModel.success(true);
        } catch (Exception e) {
            log.error("[deleteVersion] Delete failed: {}", e.getMessage());
            return ResultModel.error(500, "Delete failed: " + e.getMessage());
        }
    }

    public static class SaveVersionRequest {
        private Map<String, Object> config;
        private String operator;
        private String description;

        public Map<String, Object> getConfig() { return config; }
        public void setConfig(Map<String, Object> config) { this.config = config; }
        public String getOperator() { return operator; }
        public void setOperator(String operator) { this.operator = operator; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }
}
