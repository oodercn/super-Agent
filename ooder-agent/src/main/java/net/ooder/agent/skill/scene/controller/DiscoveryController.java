package net.ooder.mvp.skill.scene.controller;

import net.ooder.mvp.skill.scene.model.ResultModel;
import net.ooder.mvp.skill.scene.capability.service.LocalDiscoveryService;
import net.ooder.mvp.skill.scene.capability.service.LocalDiscoveryService.SyncResult;
import net.ooder.mvp.skill.scene.capability.install.SkillDownloadService;
import net.ooder.mvp.skill.scene.dto.discovery.DiscoveryResultDTO;
import net.ooder.mvp.skill.scene.discovery.MvpSkillIndexLoader;
import net.ooder.mvp.skill.scene.dto.discovery.CapabilityDTO;
import net.ooder.mvp.skill.scene.dto.discovery.CapabilityDetailDTO;
import net.ooder.mvp.skill.scene.dto.discovery.CategoryStatsDTO;
import net.ooder.mvp.skill.scene.dto.discovery.ConfigFieldDTO;
import net.ooder.mvp.skill.scene.dto.discovery.DiscoveryMethodDTO;
import net.ooder.mvp.skill.scene.dto.discovery.GitDiscoveryRequestDTO;
import net.ooder.mvp.skill.scene.dto.discovery.GitDiscoveryResultDTO;
import net.ooder.mvp.skill.scene.dto.discovery.InstallResultDTO;
import net.ooder.mvp.skill.scene.dto.discovery.InstallSkillRequestDTO;
import net.ooder.mvp.skill.scene.dto.discovery.LocalDiscoveryResultDTO;
import net.ooder.mvp.skill.scene.dto.discovery.RefreshResultDTO;
import net.ooder.mvp.skill.scene.dto.discovery.SyncResultDTO;
import net.ooder.skills.api.SkillPackageManager;
import net.ooder.skills.api.SkillRegistry;
import net.ooder.skills.api.SkillDiscoverer;
import net.ooder.skills.api.SkillPackage;
import net.ooder.skills.api.InstalledSkill;
import net.ooder.skills.api.InstallResultWithDependencies;
import net.ooder.skills.api.InstallRequest;
import net.ooder.skills.common.enums.DiscoveryMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

@RestController
@RequestMapping("/api/v1/discovery")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class DiscoveryController {

    private static final Logger log = LoggerFactory.getLogger(DiscoveryController.class);

    @Autowired(required = false)
    private LocalDiscoveryService localDiscoveryService;

    @Autowired(required = false)
    private MvpSkillIndexLoader mvpSkillIndexLoader;

    @Autowired(required = false)
    private SkillPackageManager skillPackageManager;
    
    @Autowired(required = false)
    private SkillRegistry skillRegistry;
    
    @Autowired(required = false)
    private SkillDiscoverer skillDiscoverer;
    
    @Autowired(required = false)
    private net.ooder.scene.discovery.UnifiedDiscoveryService unifiedDiscoveryService;
    
    @Autowired
    private ApplicationContext applicationContext;

    @Value("${ooder.mock.enabled:false}")
    private boolean mockEnabled;

    @Value("${ooder.discovery.use-se-sdk:true}")
    private boolean useSeSdk;

    @Value("${ooder.discovery.use-index-first:false}")
    private boolean useIndexFirst;

    @PostMapping("/local")
    public ResultModel<LocalDiscoveryResultDTO> discoverLocal(@RequestBody(required = false) Map<String, Object> request) {
        log.info("[discoverLocal] Starting local discovery");
        
        LocalDiscoveryResultDTO result = new LocalDiscoveryResultDTO();
        List<CapabilityDTO> capabilities = new ArrayList<>();
        
        if (mvpSkillIndexLoader != null) {
            log.info("[discoverLocal] Scanning workspace directories for skills");
            List<CapabilityDTO> workspaceCaps = mvpSkillIndexLoader.getWorkspaceCapabilities("WORKSPACE");
            if (workspaceCaps != null && !workspaceCaps.isEmpty()) {
                capabilities.addAll(workspaceCaps);
                log.info("[discoverLocal] Found {} skills in workspace directories", workspaceCaps.size());
                
                result.setCapabilities(capabilities);
                result.setTotal(capabilities.size());
                result.setSource("workspace");
                result.setTimestamp(System.currentTimeMillis());
                return ResultModel.success(result);
            }
            
            log.info("[discoverLocal] No skills found in workspace directories, returning empty list");
            result.setCapabilities(capabilities);
            result.setTotal(0);
            result.setSource("workspace-empty");
            result.setTimestamp(System.currentTimeMillis());
            return ResultModel.success(result);
        }
        
        if (useSeSdk && skillDiscoverer != null) {
            log.info("[discoverLocal] Using SE SDK SkillDiscoverer");
            try {
                CompletableFuture<List<SkillPackage>> future = skillDiscoverer.discover();
                List<SkillPackage> packages = future.get(30, TimeUnit.SECONDS);
                log.info("[discoverLocal] Discovered {} skill packages from SE SDK", packages.size());
                
                if (packages != null && !packages.isEmpty()) {
                    for (SkillPackage pkg : packages) {
                        capabilities.add(convertSkillPackageToCapabilityDTO(pkg));
                    }
                    
                    result.setCapabilities(capabilities);
                    result.setTotal(capabilities.size());
                    result.setSource("se-sdk");
                    result.setTimestamp(System.currentTimeMillis());
                    
                    return ResultModel.success(result);
                }
                log.warn("[discoverLocal] SE SDK returned empty result, trying next source");
            } catch (Throwable e) {
                log.error("[discoverLocal] SE SDK discovery failed: {}", e.getMessage());
                result.setErrorMessage(e.getMessage());
            }
        }
        
        if (useSeSdk && skillPackageManager != null) {
            log.info("[discoverLocal] Using SE SDK SkillPackageManager");
            try {
                CompletableFuture<List<SkillPackage>> future = skillPackageManager.discoverAll(DiscoveryMethod.LOCAL_FS);
                List<SkillPackage> packages = future.get(30, TimeUnit.SECONDS);
                log.info("[discoverLocal] Discovered {} skill packages from SkillPackageManager", packages.size());
                
                if (packages != null && !packages.isEmpty()) {
                    for (SkillPackage pkg : packages) {
                        capabilities.add(convertSkillPackageToCapabilityDTO(pkg));
                    }
                    
                    result.setCapabilities(capabilities);
                    result.setTotal(capabilities.size());
                    result.setSource("se-package-manager");
                    result.setTimestamp(System.currentTimeMillis());
                    
                    return ResultModel.success(result);
                }
                log.warn("[discoverLocal] SkillPackageManager returned empty result, trying next source");
            } catch (Throwable e) {
                log.error("[discoverLocal] SE SDK package manager discovery failed: {}", e.getMessage());
                result.setErrorMessage(e.getMessage());
            }
        }
        
        if (skillRegistry != null) {
            log.info("[discoverLocal] Using SE SDK SkillRegistry");
            try {
                List<InstalledSkill> installedSkills = skillRegistry.getInstalledSkills();
                log.info("[discoverLocal] Found {} installed skills from SE SDK", installedSkills.size());
                
                if (installedSkills != null && !installedSkills.isEmpty()) {
                    for (InstalledSkill skill : installedSkills) {
                        capabilities.add(convertInstalledSkillToCapabilityDTO(skill));
                    }
                    
                    result.setCapabilities(capabilities);
                    result.setTotal(capabilities.size());
                    result.setSource("se-registry");
                    result.setTimestamp(System.currentTimeMillis());
                    
                    return ResultModel.success(result);
                }
                log.warn("[discoverLocal] SkillRegistry returned empty result, trying next source");
            } catch (Throwable e) {
                log.error("[discoverLocal] SE SDK registry query failed: {}", e.getMessage());
                result.setErrorMessage(e.getMessage());
            }
        }
        
        if (mvpSkillIndexLoader != null) {
            log.info("[discoverLocal] Using MvpSkillIndexLoader as fallback");
            List<CapabilityDTO> caps = mvpSkillIndexLoader.getAllCapabilities("LOCAL");
            if (caps != null && !caps.isEmpty()) {
                capabilities.addAll(caps);
                result.setCapabilities(capabilities);
                result.setTotal(capabilities.size());
                result.setSource("skill-index");
                result.setTimestamp(System.currentTimeMillis());
                return ResultModel.success(result);
            }
            log.warn("[discoverLocal] MvpSkillIndexLoader returned empty result");
        }
        
        if (localDiscoveryService != null) {
            log.info("[discoverLocal] Falling back to LocalDiscoveryService");
            DiscoveryResultDTO discovery = localDiscoveryService.discover();
            
            result.setCapabilities(capabilities);
            result.setTotal(discovery.getTotal());
            result.setStats(discovery.getStats());
            result.setSource(discovery.getSource());
            result.setTimestamp(discovery.getTimestamp());
            
            return ResultModel.success(result);
        }
        
        log.error("[discoverLocal] No discovery service available");
        result.setCapabilities(capabilities);
        result.setTotal(0);
        result.setSource("none");
        result.setErrorMessage("无法获取技能数据：SE SDK 服务不可用，请检查配置");
        result.setTimestamp(System.currentTimeMillis());
        
        return ResultModel.error("无法获取技能数据: " + result.getErrorMessage());
    }
    
    private Map<String, Object> convertSkillPackageToMap(SkillPackage pkg) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", pkg.getSkillId());
        map.put("name", pkg.getName());
        map.put("version", pkg.getVersion());
        map.put("skillId", pkg.getSkillId());
        map.put("source", pkg.getSource());
        map.put("downloadUrl", pkg.getDownloadUrl());
        map.put("category", pkg.getCategory());
        map.put("tags", pkg.getTags());
        map.put("dependencies", pkg.getDependencies());
        
        Map<String, Object> metadata = pkg.getMetadata();
        if (metadata != null) {
            map.put("description", metadata.get("description"));
        }
        
        List<?> caps = pkg.getCapabilities();
        if (caps != null) {
            List<String> capNames = new ArrayList<>();
            for (Object cap : caps) {
                if (cap != null) {
                    capNames.add(cap.toString());
                }
            }
            map.put("capabilities", capNames);
        }
        
        boolean installed = checkIfInstalled(pkg.getSkillId());
        map.put("installed", installed);
        map.put("status", installed ? "installed" : "available");
        
        return map;
    }
    
    private Map<String, Object> convertInstalledSkillToMap(InstalledSkill skill) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", skill.getSkillId());
        map.put("name", skill.getName());
        map.put("version", skill.getVersion());
        map.put("skillId", skill.getSkillId());
        map.put("sceneId", skill.getSceneId());
        map.put("installPath", skill.getInstallPath());
        map.put("status", skill.getStatus());
        map.put("installTime", skill.getInstallTime());
        map.put("dependencies", skill.getDependencies());
        map.put("installed", true);
        
        return map;
    }
    
    private CapabilityDTO convertInstalledSkillToCapabilityDTO(InstalledSkill skill) {
        CapabilityDTO dto = new CapabilityDTO();
        dto.setId(skill.getSkillId());
        dto.setName(skill.getName());
        dto.setVersion(skill.getVersion());
        dto.setSkillId(skill.getSkillId());
        dto.setSceneType(skill.getSceneId());
        dto.setStatus(skill.getStatus());
        dto.setDependencies(skill.getDependencies());
        dto.setInstalled(true);
        return dto;
    }
    
    private boolean checkIfInstalled(String skillId) {
        if (skillPackageManager == null || skillId == null) {
            return false;
        }
        try {
            CompletableFuture<Boolean> future = skillPackageManager.isInstalled(skillId);
            return Boolean.TRUE.equals(future.get(5, TimeUnit.SECONDS));
        } catch (Exception e) {
            log.debug("[checkIfInstalled] Could not check install status for {}: {}", skillId, e.getMessage());
            return false;
        }
    }

    @PostMapping("/sync")
    public ResultModel<SyncResultDTO> syncFromSkills() {
        log.info("[syncFromSkills] Starting manual sync");
        
        SyncResult syncResult = localDiscoveryService.sync();
        
        SyncResultDTO result = new SyncResultDTO();
        result.setSynced(syncResult.getSynced());
        result.setSkipped(syncResult.getSkipped());
        result.setErrors(syncResult.getErrors());
        result.setTimestamp(syncResult.getTimestamp());
        result.setMessage(String.format("同步完成: 成功 %d, 跳过 %d, 错误 %d", 
                syncResult.getSynced(), syncResult.getSkipped(), syncResult.getErrors()));
        
        return ResultModel.success(result);
    }

    @GetMapping("/capability/{capabilityId}")
    public ResultModel<CapabilityDetailDTO> getCapabilityDetail(@PathVariable String capabilityId) {
        log.info("[getCapabilityDetail] Getting detail for: {}", capabilityId);
        
        CapabilityDetailDTO detail = localDiscoveryService.getCapabilityDetail(capabilityId);
        if (detail == null) {
            return ResultModel.error("能力不存在: " + capabilityId);
        }
        
        return ResultModel.success(detail);
    }

    @PostMapping("/github")
    public ResultModel<GitDiscoveryResultDTO> discoverFromGitHub(@RequestBody(required = false) GitDiscoveryRequestDTO request) {
        log.info("[discoverFromGitHub] Starting GitHub discovery, useSeSdk: {}", useSeSdk);
        
        GitDiscoveryResultDTO result = new GitDiscoveryResultDTO();
        List<CapabilityDTO> capabilities = new ArrayList<>();
        
        String repoUrl = request != null && request.getRepoUrl() != null ? request.getRepoUrl() : "https://github.com/ooderCN/skills";
        String branch = request != null && request.getBranch() != null ? request.getBranch() : "main";
        
        result.setRepoUrl(repoUrl);
        result.setBranch(branch);
        result.setSource("github");
        
        if (useSeSdk && skillPackageManager != null) {
            log.info("[discoverFromGitHub] Using SE SDK SkillPackageManager for GitHub");
            try {
                CompletableFuture<List<SkillPackage>> future = skillPackageManager.discoverAll(DiscoveryMethod.GITHUB);
                List<SkillPackage> packages = future.get(60, TimeUnit.SECONDS);
                log.info("[discoverFromGitHub] Discovered {} skills from GitHub via SE SDK", packages.size());
                
                for (SkillPackage pkg : packages) {
                    capabilities.add(convertSkillPackageToCapabilityDTO(pkg));
                }
                
                result.setCapabilities(capabilities);
                result.setTotal(capabilities.size());
                result.setTimestamp(System.currentTimeMillis());
                
                return ResultModel.success(result);
            } catch (Exception e) {
                log.error("[discoverFromGitHub] SE SDK GitHub discovery failed: {}", e.getMessage());
                result.setErrorMessage(e.getMessage());
            }
        }
        
        if (useIndexFirst && mvpSkillIndexLoader != null) {
            List<CapabilityDTO> caps = mvpSkillIndexLoader.getSkillsFromIndex("GITHUB");
            if (caps != null) {
                capabilities.addAll(caps);
            }
            log.info("[discoverFromGitHub] Found {} capabilities from skill-index", capabilities.size());
        }
        
        if (capabilities.isEmpty() && mockEnabled) {
            log.warn("[discoverFromGitHub] No capabilities found and mock is enabled");
        }
        
        if (capabilities.isEmpty()) {
            log.error("[discoverFromGitHub] No capabilities found from any source");
            result.setErrorMessage("无法从 GitHub 获取技能数据，请检查网络连接和配置");
            return ResultModel.error("无法从 GitHub 获取技能数据: " + result.getErrorMessage());
        }
        
        result.setCapabilities(capabilities);
        result.setTotal(capabilities.size());
        result.setTimestamp(System.currentTimeMillis());
        
        return ResultModel.success(result);
    }

    @PostMapping("/gitee")
    public ResultModel<GitDiscoveryResultDTO> discoverFromGitee(@RequestBody(required = false) GitDiscoveryRequestDTO request) {
        log.info("[discoverFromGitee] Starting Gitee discovery");
        
        GitDiscoveryResultDTO result = new GitDiscoveryResultDTO();
        List<CapabilityDTO> capabilities = new ArrayList<>();
        
        String repoUrl = request != null && request.getRepoUrl() != null ? request.getRepoUrl() : "https://gitee.com/ooderCN/skills";
        String branch = request != null && request.getBranch() != null ? request.getBranch() : "master";
        
        result.setRepoUrl(repoUrl);
        result.setBranch(branch);
        result.setSource("gitee");
        
        if (unifiedDiscoveryService != null) {
            log.info("[discoverFromGitee] Using UnifiedDiscoveryService from SE SDK");
            try {
                CompletableFuture<List<SkillPackage>> future = unifiedDiscoveryService.discoverSkills(repoUrl);
                List<SkillPackage> packages = future.get(60, TimeUnit.SECONDS);
                log.info("[discoverFromGitee] Discovered {} skills from Gitee via UnifiedDiscoveryService", packages.size());
                
                for (SkillPackage pkg : packages) {
                    capabilities.add(convertSkillPackageToCapabilityDTO(pkg));
                }
                
                if (!capabilities.isEmpty()) {
                    result.setCapabilities(capabilities);
                    result.setTotal(capabilities.size());
                    result.setTimestamp(System.currentTimeMillis());
                    return ResultModel.success(result);
                }
                log.warn("[discoverFromGitee] UnifiedDiscoveryService returned empty result, trying next source");
            } catch (Exception e) {
                log.error("[discoverFromGitee] UnifiedDiscoveryService failed: {}", e.getMessage());
                result.setErrorMessage(e.getMessage());
            }
        }
        
        if (useSeSdk && skillPackageManager != null) {
            log.info("[discoverFromGitee] Using SE SDK SkillPackageManager for Gitee");
            try {
                CompletableFuture<List<SkillPackage>> future = skillPackageManager.discoverAll(DiscoveryMethod.GITEE);
                List<SkillPackage> packages = future.get(60, TimeUnit.SECONDS);
                log.info("[discoverFromGitee] Discovered {} skills from Gitee via SE SDK", packages.size());
                
                for (SkillPackage pkg : packages) {
                    capabilities.add(convertSkillPackageToCapabilityDTO(pkg));
                }
                
                if (!capabilities.isEmpty()) {
                    result.setCapabilities(capabilities);
                    result.setTotal(capabilities.size());
                    result.setTimestamp(System.currentTimeMillis());
                    return ResultModel.success(result);
                }
                log.warn("[discoverFromGitee] SE SDK Gitee discovery returned empty result, trying next source");
            } catch (Exception e) {
                log.error("[discoverFromGitee] SE SDK Gitee discovery failed: {}", e.getMessage());
                result.setErrorMessage(e.getMessage());
            }
        }
        
        if (mvpSkillIndexLoader != null) {
            log.info("[discoverFromGitee] Using MvpSkillIndexLoader as fallback");
            List<CapabilityDTO> caps = mvpSkillIndexLoader.getAllCapabilities("GITEE");
            if (caps != null && !caps.isEmpty()) {
                capabilities.addAll(caps);
                log.info("[discoverFromGitee] Found {} capabilities from skill-index", caps.size());
            }
        }
        
        if (capabilities.isEmpty() && mockEnabled) {
            log.warn("[discoverFromGitee] No capabilities found and mock is enabled");
        }
        
        if (capabilities.isEmpty()) {
            log.error("[discoverFromGitee] No capabilities found from any source");
            result.setErrorMessage("无法从 Gitee 获取技能数据，请检查网络连接和配置");
            return ResultModel.error("无法从 Gitee 获取技能数据: " + result.getErrorMessage());
        }
        
        result.setCapabilities(capabilities);
        result.setTotal(capabilities.size());
        result.setTimestamp(System.currentTimeMillis());
        
        return ResultModel.success(result);
    }

    private List<Map<String, Object>> convertCapabilitiesToMaps(List<CapabilityDTO> caps) {
        return caps.stream().map(this::convertCapabilityDTOToMap).collect(Collectors.toList());
    }
    
    private Map<String, Object> convertCapabilityDTOToMap(CapabilityDTO cap) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", cap.getId());
        map.put("name", cap.getName());
        map.put("description", cap.getDescription());
        map.put("version", cap.getVersion());
        map.put("source", cap.getSource());
        map.put("status", cap.getStatus());
        map.put("type", cap.getType());
        map.put("sceneCapability", cap.isSceneCapability());
        map.put("skillForm", cap.getSkillForm());
        map.put("sceneType", cap.getSceneType());
        map.put("category", cap.getCategory());
        map.put("capabilityCategory", cap.getCapabilityCategory());
        map.put("businessCategory", cap.getBusinessCategory());
        map.put("visibility", cap.getVisibility());
        map.put("installed", cap.isInstalled());
        map.put("capabilities", cap.getCapabilities());
        map.put("dependencies", cap.getDependencies());
        map.put("tags", cap.getTags());
        map.put("driverConditions", cap.getDriverConditions());
        map.put("participants", cap.getParticipants());
        return map;
    }

    @PostMapping("/install")
    public ResultModel<InstallResultDTO> installSkill(@RequestBody InstallSkillRequestDTO request) {
        String skillId = request.getSkillId();
        String source = request.getSource() != null ? request.getSource() : "local";
        
        log.info("[installSkill] Installing skill: {} from {}, useSeSdk: {}", skillId, source, useSeSdk);
        
        InstallResultDTO result = new InstallResultDTO();
        result.setSkillId(skillId);
        
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
                return ResultModel.success(result);
            }
            
            String downloadUrl = null;
            String repoUrl = null;
            if (mvpSkillIndexLoader != null) {
                downloadUrl = mvpSkillIndexLoader.getDownloadUrl(skillId);
                repoUrl = mvpSkillIndexLoader.getRepoUrl(skillId);
                if (downloadUrl != null) {
                    log.info("[installSkill] Download URL found: {}", downloadUrl);
                }
                if (repoUrl != null) {
                    log.info("[installSkill] Repo URL found: {}", repoUrl);
                }
            }
            
            boolean shouldTryGitClone = false;
            
            if (downloadUrl != null && !downloadUrl.isEmpty()) {
                log.info("[installSkill] Attempting to download from URL: {}", downloadUrl);
            } else {
                log.info("[installSkill] No direct download URL, will try git clone");
                shouldTryGitClone = true;
            }
            
            if (shouldTryGitClone && repoUrl != null && !repoUrl.isEmpty()) {
                log.info("[installSkill] Trying git clone from: {}", repoUrl);
                try {
                    SkillDownloadService downloadService = getBean(SkillDownloadService.class);
                    if (downloadService != null) {
                        SkillDownloadService.DownloadResult cloneResult = downloadService.cloneFromGit(repoUrl, skillId, "master");
                        if (cloneResult.isSuccess()) {
                            log.info("[installSkill] Git clone successful for: {}", skillId);
                            registerSkillInRegistry(skillId);
                            result.setStatus("installed");
                            result.setMessage("通过 Git clone 安装成功");
                            result.setInstallTime(System.currentTimeMillis());
                            return ResultModel.success(result);
                        } else {
                            log.warn("[installSkill] Git clone failed: {}", cloneResult.getMessage());
                        }
                    }
                } catch (Exception e) {
                    log.warn("[installSkill] Git clone attempt failed: {}", e.getMessage());
                }
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

    private CapabilityDTO convertSkillPackageToCapabilityDTO(SkillPackage pkg) {
        CapabilityDTO dto = new CapabilityDTO();
        dto.setId(pkg.getSkillId());
        dto.setName(pkg.getName());
        dto.setVersion(pkg.getVersion());
        dto.setSkillId(pkg.getSkillId());
        dto.setSource(pkg.getSource());
        dto.setCategory(pkg.getCategory());
        dto.setTags(pkg.getTags());
        dto.setDependencies(pkg.getDependencies());
        
        Map<String, Object> metadata = pkg.getMetadata();
        if (metadata != null) {
            dto.setDescription((String) metadata.get("description"));
        }
        
        List<?> caps = pkg.getCapabilities();
        if (caps != null) {
            List<String> capNames = new ArrayList<>();
            for (Object cap : caps) {
                if (cap != null) {
                    capNames.add(cap.toString());
                }
            }
            dto.setCapabilities(capNames);
        }
        
        // 推断 skillForm
        String skillForm = inferSkillForm(pkg.getSkillId(), pkg.getCategory(), metadata);
        dto.setSkillForm(skillForm);
        
        boolean installed = checkIfInstalled(pkg.getSkillId());
        dto.setInstalled(installed);
        dto.setStatus(installed ? "installed" : "available");
        
        return dto;
    }
    
    /**
     * 推断技能的 skillForm
     * 基于技能ID、category和metadata进行推断
     */
    private String inferSkillForm(String skillId, String category, Map<String, Object> metadata) {
        if (skillId == null) {
            return "PROVIDER";
        }

        String skillIdLower = skillId.toLowerCase();

        // 已知场景技能列表（来自 Gitee 的技能，SE SDK 未正确读取 spec.skillForm）
        String[] knownSceneSkills = {
            "skill-approval-form", "skill-recruitment-management", "skill-real-estate-form",
            "skill-meeting-minutes", "skill-daily-report", "skill-recording-qa",
            "skill-knowledge-management", "skill-knowledge-qa", "skill-knowledge-share",
            "skill-document-assistant", "skill-onboarding-assistant", "skill-business",
            "skill-collaboration", "skill-platform-bind", "skill-project-knowledge",
            "skill-scene", "skill-form-builder", "skill-workflow-engine"
        };

        for (String known : knownSceneSkills) {
            if (skillIdLower.equals(known)) {
                log.debug("[inferSkillForm] Skill {} matched known scene skill", skillId);
                return "SCENE";
            }
        }

        // 场景技能关键字
        String[] sceneKeywords = {
            "scene", "approval", "recruitment", "real-estate", "meeting",
            "daily-report", "knowledge", "collaboration", "business",
            "onboarding", "project", "recording", "document", "platform-bind",
            "workflow", "form"
        };

        for (String keyword : sceneKeywords) {
            if (skillIdLower.contains(keyword)) {
                log.debug("[inferSkillForm] Skill {} matched scene keyword: {}", skillId, keyword);
                return "SCENE";
            }
        }

        // 检查 metadata 中的 type 字段
        if (metadata != null) {
            Object type = metadata.get("type");
            if (type != null) {
                String typeStr = type.toString().toLowerCase();
                if (typeStr.contains("scene") || typeStr.contains("scene-skill")) {
                    log.debug("[inferSkillForm] Skill {} matched type: {}", skillId, typeStr);
                    return "SCENE";
                }
            }

            // 检查 metadata 中的 form 字段
            Object form = metadata.get("form");
            if (form != null) {
                String formStr = form.toString().toUpperCase();
                if ("SCENE".equals(formStr) || "PROVIDER".equals(formStr) || "DRIVER".equals(formStr)) {
                    log.debug("[inferSkillForm] Skill {} has explicit form: {}", skillId, formStr);
                    return formStr;
                }
            }
        }

        log.debug("[inferSkillForm] Skill {} defaulted to PROVIDER", skillId);
        return "PROVIDER";
    }
    
    @GetMapping("/categories/stats")
    public ResultModel<CategoryStatsDTO> getCategoryStats() {
        log.info("[getCategoryStats] Getting category statistics, useSeSdk: {}", useSeSdk);
        
        if (useSeSdk && skillRegistry != null) {
            try {
                List<InstalledSkill> skills = skillRegistry.getInstalledSkills();
                Map<String, Long> categoryCount = new HashMap<>();
                long installed = 0;
                
                for (InstalledSkill skill : skills) {
                    String category = skill.getSceneId() != null ? "scene" : "provider";
                    categoryCount.merge(category, 1L, Long::sum);
                    installed++;
                }
                
                CategoryStatsDTO stats = new CategoryStatsDTO();
                stats.setTotal(skills.size());
                stats.setCategories(categoryCount);
                stats.setInstalled(installed);
                
                return ResultModel.success(stats);
            } catch (Throwable e) {
                log.error("[getCategoryStats] Failed to get stats from SE SDK: {}", e.getMessage());
            }
        }
        
        if (mvpSkillIndexLoader != null) {
            Map<String, Object> rawStats = mvpSkillIndexLoader.getCategoryStats();
            CategoryStatsDTO stats = new CategoryStatsDTO();
            if (rawStats.get("total") != null) {
                stats.setTotal(((Number) rawStats.get("total")).intValue());
            }
            return ResultModel.success(stats);
        }
        
        CategoryStatsDTO emptyStats = new CategoryStatsDTO();
        emptyStats.setTotal(0);
        emptyStats.setMessage("No statistics available");
        return ResultModel.success(emptyStats);
    }
    
    @GetMapping("/categories/user-facing")
    public ResultModel<List<Map<String, Object>>> getUserFacingCategories() {
        log.info("[getUserFacingCategories] Getting user facing categories");
        if (mvpSkillIndexLoader != null) {
            List<Map<String, Object>> categories = mvpSkillIndexLoader.getUserFacingCategories();
            return ResultModel.success(categories);
        }
        return ResultModel.success(new ArrayList<>());
    }
    
    @GetMapping("/categories/all")
    public ResultModel<List<Map<String, Object>>> getAllCategories() {
        log.info("[getAllCategories] Getting all categories with stats");
        if (mvpSkillIndexLoader != null) {
            List<Map<String, Object>> categories = mvpSkillIndexLoader.getAllCategoriesWithStats();
            return ResultModel.success(categories);
        }
        return ResultModel.success(new ArrayList<>());
    }
    
    @GetMapping("/categories/{categoryId}/subcategories")
    public ResultModel<List<Map<String, Object>>> getSubCategories(@PathVariable String categoryId) {
        log.info("[getSubCategories] Getting subcategories for: {}", categoryId);
        if (mvpSkillIndexLoader != null) {
            List<Map<String, Object>> subCategories = mvpSkillIndexLoader.getSubCategories(categoryId);
            return ResultModel.success(subCategories);
        }
        return ResultModel.success(new ArrayList<>());
    }
    
    @PostMapping("/refresh")
    public ResultModel<RefreshResultDTO> refreshDiscovery() {
        log.info("[refreshDiscovery] Refreshing discovery - clearing cache and reloading");
        
        RefreshResultDTO result = new RefreshResultDTO();
        
        if (mvpSkillIndexLoader != null) {
            mvpSkillIndexLoader.reload();
            List<CapabilityDTO> caps = mvpSkillIndexLoader.getAllCapabilities("LOCAL");
            log.info("[refreshDiscovery] Reloaded {} capabilities from skill index", caps.size());
            result.setDiscovered(caps.size());
            result.setSource("skill-index-reloaded");
            result.setTimestamp(System.currentTimeMillis());
        }
        
        if (skillDiscoverer != null) {
            try {
                CompletableFuture<List<SkillPackage>> future = skillDiscoverer.discover();
                List<SkillPackage> packages = future.get(30, TimeUnit.SECONDS);
                log.info("[refreshDiscovery] Discovered {} skills from SE SDK", packages.size());
                result.setDiscovered(packages.size());
                result.setSource("se-sdk");
                result.setTimestamp(System.currentTimeMillis());
                return ResultModel.success(result);
            } catch (Throwable e) {
                log.error("[refreshDiscovery] Failed to refresh from SE SDK: {}", e.getMessage());
                result.setError(e.getMessage());
            }
        }
        
        if (skillPackageManager != null) {
            try {
                CompletableFuture<List<SkillPackage>> future = skillPackageManager.discoverAll(DiscoveryMethod.LOCAL_FS);
                List<SkillPackage> packages = future.get(30, TimeUnit.SECONDS);
                log.info("[refreshDiscovery] Found {} skill packages", packages.size());
                if (packages.size() > 0) {
                    result.setDiscovered(packages.size());
                    result.setSource("se-package-manager");
                    result.setTimestamp(System.currentTimeMillis());
                    return ResultModel.success(result);
                }
            } catch (Throwable e) {
                log.error("[refreshDiscovery] Failed to get from package manager: {}", e.getMessage());
                result.setError(e.getMessage());
            }
        }
        
        if (skillRegistry != null) {
            try {
                List<InstalledSkill> skills = skillRegistry.getInstalledSkills();
                log.info("[refreshDiscovery] Found {} installed skills", skills.size());
                result.setRegistered(skills.size());
                result.setSource("se-registry");
                result.setTimestamp(System.currentTimeMillis());
                return ResultModel.success(result);
            } catch (Throwable e) {
                log.error("[refreshDiscovery] Failed to get from registry: {}", e.getMessage());
                result.setError(e.getMessage());
            }
        }
        
        result.setTimestamp(System.currentTimeMillis());
        return ResultModel.success(result);
    }
    
    @GetMapping("/methods")
    public ResultModel<List<DiscoveryMethodDTO>> getDiscoveryMethods() {
        log.info("[getDiscoveryMethods] Getting available discovery methods");
        
        List<DiscoveryMethodDTO> methods = new ArrayList<>();
        
        methods.add(new DiscoveryMethodDTO("LOCAL_FS", "本地文件系统", "ri-folder-line", 
            "扫描本地已安装的能力包", "#3b82f6", false));
        methods.add(new DiscoveryMethodDTO("SKILL_CENTER", "能力中心", "ri-cloud-line",
            "从能力中心发现可用能力", "#10b981", true));
        methods.add(new DiscoveryMethodDTO("GITHUB", "GitHub仓库", "ri-github-fill",
            "从GitHub仓库发现能力", "#6366f1", true));
        methods.add(new DiscoveryMethodDTO("GITEE", "Gitee仓库", "ri-git-repository-line",
            "从Gitee仓库发现能力", "#ef4444", true));
        methods.add(new DiscoveryMethodDTO("GIT_REPOSITORY", "Git仓库", "ri-git-branch-line",
            "从任意Git仓库发现能力", "#f59e0b", true));
        methods.add(new DiscoveryMethodDTO("AUTO", "自动检测", "ri-magic-line",
            "自动选择最佳发现方式", "#64748b", false));
        
        for (DiscoveryMethodDTO method : methods) {
            method.setConfigFields(getConfigFields(method.getId()));
        }
        
        return ResultModel.success(methods);
    }
    
    private List<ConfigFieldDTO> getConfigFields(String methodId) {
        List<ConfigFieldDTO> fields = new ArrayList<>();
        
        switch (methodId) {
            case "GITHUB":
                fields.add(new ConfigFieldDTO("token", "GitHub Token", "password", "GitHub Personal Access Token"));
                fields.add(new ConfigFieldDTO("owner", "仓库所有者", "text", "ooderCN"));
                fields.add(new ConfigFieldDTO("repo", "仓库名称", "text", "skills"));
                break;
            case "GITEE":
                fields.add(new ConfigFieldDTO("token", "Gitee Token", "password", "Gitee 私人令牌"));
                fields.add(new ConfigFieldDTO("owner", "仓库所有者", "text", "ooderCN"));
                fields.add(new ConfigFieldDTO("repo", "仓库名称", "text", "skills"));
                fields.add(new ConfigFieldDTO("branch", "分支", "text", "master"));
                break;
            case "SKILL_CENTER":
                fields.add(new ConfigFieldDTO("url", "能力中心地址", "text", "https://skill.ooder.net"));
                fields.add(new ConfigFieldDTO("token", "访问令牌", "password", "可选"));
                break;
            case "GIT_REPOSITORY":
                fields.add(new ConfigFieldDTO("repoUrl", "仓库地址", "text", "https://github.com/user/repo.git"));
                fields.add(new ConfigFieldDTO("branch", "分支", "text", "main"));
                fields.add(new ConfigFieldDTO("token", "访问令牌", "password", "可选"));
                break;
            default:
                break;
        }
        
        return fields;
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
            
            if (mvpSkillIndexLoader != null) {
                mvpSkillIndexLoader.markAsInstalled(skillId);
            }
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
