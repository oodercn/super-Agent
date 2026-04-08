package net.ooder.skill.install.controller;

import net.ooder.skill.install.model.ResultModel;
import net.ooder.skill.install.dto.InstallResultDTO;
import net.ooder.skill.install.dto.InstallSkillRequestDTO;
import net.ooder.skills.api.InstallRequest;
import net.ooder.skills.api.InstallResultWithDependencies;
import net.ooder.skills.api.SkillPackageManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/v1/install")
@CrossOrigin(originPatterns = "*", allowCredentials = "true", allowedHeaders = "*")
public class InstallController {

    private static final Logger log = LoggerFactory.getLogger(InstallController.class);

    @Autowired(required = false)
    private SkillPackageManager skillPackageManager;

    @Autowired
    private ApplicationContext applicationContext;

    @Value("${ooder.discovery.use-se-sdk:true}")
    private boolean useSeSdk;

    @PostMapping("/install")
    public ResultModel<InstallResultDTO> installSkill(@RequestBody InstallSkillRequestDTO request) {
        String skillId = request.getSkillId();
        String source = request.getSource() != null ? request.getSource() : "local";

        log.info("[installSkill] Installing skill: {} from {}, type: {}", skillId, source, request.getType());
        
        InstallResultDTO result = new InstallResultDTO();
        result.setSkillId(skillId);
        result.setInstallSource(source);

        if (skillPackageManager == null) {
            log.error("[installSkill] SkillPackageManager not available");
            result.setStatus("failed");
            result.setMessage("SkillPackageManager 不可用，请检查 skill-hotplug-starter 配置");
            return ResultModel.error("SkillPackageManager 不可用，无法安装 skill");
        }

        try {
            CompletableFuture<Boolean> isInstalledFuture = skillPackageManager.isInstalled(skillId);
            Boolean isInstalled = isInstalledFuture.get(10, TimeUnit.SECONDS);

            if (Boolean.TRUE.equals(isInstalled)) {
                log.info("[installSkill] Skill already installed: {}", skillId);
                result.setStatus("installed");
                result.setMessage("已安装，跳过");
                result.setInstallTime(System.currentTimeMillis());
                
                handlePostInstallConfig(request);
                return ResultModel.success(result);
            }

            CompletableFuture<InstallResultWithDependencies> installFuture =
                skillPackageManager.installWithDependencies(skillId, InstallRequest.InstallMode.FULL_INSTALL);

            InstallResultWithDependencies installResult = installFuture.get(120, TimeUnit.SECONDS);

            if (installResult != null && installResult.isSuccess()) {
                log.info("[installSkill] Successfully installed: {}", skillId);
                result.setStatus("installed");
                result.setMessage("安装成功");
                result.setInstallTime(System.currentTimeMillis());
                if (installResult.getInstalledDependencies() != null) {
                    result.setInstalledDependencies(installResult.getInstalledDependencies());
                }

                registerSkillInRegistry(skillId);
                handlePostInstallConfig(request);
            } else {
                String error = installResult != null ? installResult.getError() : "未知错误";
                log.error("[installSkill] Failed to install {}: {}", skillId, error);
                result.setStatus("failed");
                result.setMessage("安装失败: " + error);
            }
        } catch (Exception e) {
            log.error("[installSkill] Error installing {}: {}", skillId, e.getMessage());
            result.setStatus("failed");
            result.setMessage("安装异常: " + e.getMessage());
        }

        return ResultModel.success(result);
    }
    
    private void handlePostInstallConfig(InstallSkillRequestDTO request) {
        String skillId = request.getSkillId();
        String type = request.getType();
        
        log.info("[handlePostInstallConfig] Processing post-install config for: {}, type: {}", skillId, type);
    }

    private void registerSkillInRegistry(String skillId) {
        try {
            File registryDir = new File("data/installed-skills");
            if (!registryDir.exists()) {
                registryDir.mkdirs();
            }

            File registryFile = new File("data/installed-skills/registry.properties");
            Properties props = new Properties();

            if (registryFile.exists()) {
                try (FileInputStream fis = new FileInputStream(registryFile)) {
                    props.load(fis);
                }
            }

            String skillPath = findSkillInstallPath(skillId);
            if (skillPath == null) {
                log.warn("[registerSkillInRegistry] Could not find install path for: {}", skillId);
                skillPath = "./.ooder/installed/" + skillId;
            }

            props.setProperty(skillId + ".id", skillId);
            props.setProperty(skillId + ".path", skillPath);
            props.setProperty(skillId + ".installedAt", String.valueOf(System.currentTimeMillis()));

            try (FileOutputStream fos = new FileOutputStream(registryFile)) {
                props.store(fos, "Installed Skills Registry");
            }

            log.info("[registerSkillInRegistry] Registered skill in registry: {} -> {}", skillId, skillPath);

        } catch (Exception e) {
            log.error("[registerSkillInRegistry] Failed to register skill: {}", e.getMessage());
        }
    }

    private String findSkillInstallPath(String skillId) {
        String[] possiblePaths = {
            "./.ooder/downloads/" + skillId,
            "./.ooder/installed/" + skillId,
            "./.ooder/activated/" + skillId,
            "../skills/_system/" + skillId,
            "../skills/" + skillId
        };

        for (String path : possiblePaths) {
            File dir = new File(path);
            if (dir.exists() && dir.isDirectory()) {
                log.info("[findSkillInstallPath] Found skill {} at: {}", skillId, dir.getAbsolutePath());
                return dir.getAbsolutePath();
            }
        }

        log.warn("[findSkillInstallPath] Skill {} not found in any known directory", skillId);
        return null;
    }

    private <T> T getBean(Class<T> clazz) {
        try {
            return applicationContext != null ? applicationContext.getBean(clazz) : null;
        } catch (Exception e) {
            log.warn("[getBean] Failed to get bean for {}: {}", clazz.getSimpleName(), e.getMessage());
            return null;
        }
    }
}
