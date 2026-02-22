package net.ooder.nexus.adapter.inbound.controller.skill;

import net.ooder.nexus.service.SkillPackageService;
import net.ooder.sdk.api.skill.SkillPackage;
import net.ooder.sdk.api.skill.InstalledSkill;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/skills/package")
public class SkillPackageController {

    private static final Logger log = LoggerFactory.getLogger(SkillPackageController.class);

    @Autowired
    private SkillPackageService skillPackageService;

    @PostMapping("/install")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> installSkill(
            @RequestBody Map<String, Object> request) {
        
        String skillId = (String) request.get("skillId");
        String version = (String) request.get("version");
        @SuppressWarnings("unchecked")
        Map<String, String> config = (Map<String, String>) request.get("config");

        return skillPackageService.installSkill(skillId, version, config)
                .thenApply(result -> {
                    Map<String, Object> response = new HashMap<String, Object>();
                    response.put("success", result.get("success"));
                    response.put("skillId", result.get("skillId"));
                    response.put("version", result.get("version"));
                    response.put("installPath", result.get("installPath"));
                    if (!Boolean.TRUE.equals(result.get("success"))) {
                        response.put("error", result.get("error"));
                    }
                    return ResponseEntity.ok(response);
                });
    }

    @DeleteMapping("/{skillId}")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> uninstallSkill(
            @PathVariable String skillId) {
        
        return skillPackageService.uninstallSkill(skillId)
                .thenApply(result -> {
                    if (Boolean.TRUE.equals(result.get("success"))) {
                        return ResponseEntity.ok(result);
                    } else {
                        return ResponseEntity.badRequest().body(result);
                    }
                });
    }

    @PutMapping("/{skillId}/update")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> updateSkill(
            @PathVariable String skillId,
            @RequestParam(required = false) String version) {
        
        return skillPackageService.updateSkill(skillId, version)
                .thenApply(result -> {
                    if (Boolean.TRUE.equals(result.get("success"))) {
                        return ResponseEntity.ok(result);
                    } else {
                        return ResponseEntity.badRequest().body(result);
                    }
                });
    }

    @GetMapping("/discover")
    public CompletableFuture<ResponseEntity<List<Map<String, Object>>>> discoverSkills(
            @RequestParam(required = false) String scene,
            @RequestParam(required = false) String capability,
            @RequestParam(required = false) String keyword) {
        
        return skillPackageService.discoverSkills(scene, capability, keyword)
                .thenApply(packages -> {
                    List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
                    for (SkillPackage pkg : packages) {
                        Map<String, Object> item = new HashMap<String, Object>();
                        item.put("skillId", pkg.getSkillId());
                        item.put("name", pkg.getName());
                        item.put("version", pkg.getVersion());
                        item.put("description", pkg.getDescription());
                        item.put("sceneId", pkg.getSceneId());
                        result.add(item);
                    }
                    return ResponseEntity.ok(result);
                });
    }

    @GetMapping("/{skillId}/info")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> getSkillInfo(
            @PathVariable String skillId) {
        
        return skillPackageService.getSkillInfo(skillId)
                .thenApply(pkg -> {
                    if (pkg == null) {
                        Map<String, Object> error = new HashMap<String, Object>();
                        error.put("error", "Skill not found: " + skillId);
                        return ResponseEntity.<Map<String, Object>>notFound().build();
                    }
                    Map<String, Object> result = new HashMap<String, Object>();
                    result.put("skillId", pkg.getSkillId());
                    result.put("name", pkg.getName());
                    result.put("version", pkg.getVersion());
                    result.put("description", pkg.getDescription());
                    result.put("sceneId", pkg.getSceneId());
                    result.put("source", pkg.getSource());
                    return ResponseEntity.ok(result);
                });
    }

    @GetMapping("/{skillId}/connection")
    public ResponseEntity<Map<String, Object>> getSkillConnection(
            @PathVariable String skillId) {
        
        Map<String, Object> conn = skillPackageService.getSkillConnection(skillId);
        if (conn == null || conn.containsKey("error")) {
            Map<String, Object> error = new HashMap<String, Object>();
            error.put("error", "Connection info not found for skill: " + skillId);
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(conn);
    }

    @GetMapping("/{skillId}/status")
    public ResponseEntity<Map<String, Object>> getSkillStatus(
            @PathVariable String skillId) {
        
        String status = skillPackageService.getSkillStatus(skillId);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("skillId", skillId);
        result.put("status", status);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{skillId}/test-connection")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> testConnection(
            @PathVariable String skillId) {
        
        return skillPackageService.testConnection(skillId)
                .thenApply(connected -> {
                    Map<String, Object> result = new HashMap<String, Object>();
                    result.put("skillId", skillId);
                    result.put("connected", connected);
                    return ResponseEntity.ok(result);
                });
    }

    @PostMapping("/{skillId}/start")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> startSkill(
            @PathVariable String skillId) {
        
        return skillPackageService.startSkill(skillId)
                .thenApply(v -> {
                    Map<String, Object> result = new HashMap<String, Object>();
                    result.put("success", true);
                    result.put("message", "Skill started: " + skillId);
                    return ResponseEntity.ok(result);
                })
                .exceptionally(e -> {
                    Map<String, Object> result = new HashMap<String, Object>();
                    result.put("success", false);
                    result.put("error", e.getMessage());
                    return ResponseEntity.badRequest().body(result);
                });
    }

    @PostMapping("/{skillId}/stop")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> stopSkill(
            @PathVariable String skillId) {
        
        return skillPackageService.stopSkill(skillId)
                .thenApply(v -> {
                    Map<String, Object> result = new HashMap<String, Object>();
                    result.put("success", true);
                    result.put("message", "Skill stopped: " + skillId);
                    return ResponseEntity.ok(result);
                })
                .exceptionally(e -> {
                    Map<String, Object> result = new HashMap<String, Object>();
                    result.put("success", false);
                    result.put("error", e.getMessage());
                    return ResponseEntity.badRequest().body(result);
                });
    }

    @GetMapping("/installed")
    public ResponseEntity<List<Map<String, Object>>> getInstalledSkills() {
        List<InstalledSkill> skills = skillPackageService.getInstalledSkills();
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        for (InstalledSkill skill : skills) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("skillId", skill.getSkillId());
            item.put("name", skill.getName());
            item.put("version", skill.getVersion());
            item.put("sceneId", skill.getSceneId());
            item.put("status", skill.getStatus());
            item.put("installPath", skill.getInstallPath());
            item.put("installTime", skill.getInstallTime());
            result.add(item);
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/installed/{skillId}")
    public ResponseEntity<Map<String, Object>> getInstalledSkill(
            @PathVariable String skillId) {
        
        InstalledSkill skill = skillPackageService.getInstalledSkill(skillId);
        if (skill == null) {
            return ResponseEntity.notFound().build();
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("skillId", skill.getSkillId());
        result.put("name", skill.getName());
        result.put("version", skill.getVersion());
        result.put("sceneId", skill.getSceneId());
        result.put("status", skill.getStatus());
        result.put("installPath", skill.getInstallPath());
        result.put("installTime", skill.getInstallTime());
        return ResponseEntity.ok(result);
    }
}
