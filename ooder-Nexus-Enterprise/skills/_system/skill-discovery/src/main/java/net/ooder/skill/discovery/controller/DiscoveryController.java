package net.ooder.skill.discovery.controller;

import net.ooder.skill.discovery.model.ResultModel;
import net.ooder.skill.discovery.model.SkillDirectory;
import net.ooder.skill.discovery.model.CapabilityCategory;
import net.ooder.skill.discovery.dto.discovery.*;
import net.ooder.skill.discovery.dto.discovery.PageResult;
import net.ooder.skill.discovery.controller.converter.DiscoveryConverter;
import net.ooder.skill.discovery.service.SkillDirectoryDetector;
import net.ooder.skill.discovery.service.DiscoveryHelperService;
import net.ooder.skills.api.SkillPackageManager;
import net.ooder.skills.api.SkillRegistry;
import net.ooder.skills.api.SkillDiscoverer;
import net.ooder.skills.api.SkillPackage;
import net.ooder.skills.api.InstalledSkill;
import net.ooder.skills.api.InstallRequest;
import net.ooder.skills.api.InstallResultWithDependencies;
import net.ooder.skills.api.DependencyResult;
import net.ooder.skills.common.enums.DiscoveryMethod;
import net.ooder.skill.common.discovery.DiscoveryOrchestrator;
import net.ooder.skill.common.discovery.DiscoveryResult;
import net.ooder.skill.common.discovery.CapabilityDTO;
import net.ooder.scene.discovery.coordinator.DiscoveryCoordinator;
import net.ooder.skill.hotplug.PluginManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/v1/discovery")
@CrossOrigin(originPatterns = "*", allowCredentials = "true", allowedHeaders = "*")
public class DiscoveryController {

    private static final Logger log = LoggerFactory.getLogger(DiscoveryController.class);

    @Autowired(required = false)
    private SkillPackageManager skillPackageManager;
    
    @Autowired(required = false)
    private SkillRegistry skillRegistry;
    
    @Autowired(required = false)
    private SkillDiscoverer skillDiscoverer;
    
    @Autowired(required = false)
    private DiscoveryOrchestrator discoveryOrchestrator;
    
    @Autowired(required = false)
    private DiscoveryCoordinator discoveryCoordinator;
    
    @Autowired(required = false)
    private PluginManager pluginManager;
    
    @Autowired
    private SkillDirectoryDetector directoryDetector;
    
    @Autowired
    private DiscoveryHelperService discoveryHelper;

    @Value("${ooder.mock.enabled:false}")
    private boolean mockEnabled;

    @Value("${ooder.discovery.use-se-sdk:true}")
    private boolean useSeSdk;

    @PostMapping("/local")
    public ResultModel<LocalDiscoveryResultDTO> discoverLocal(@RequestBody(required = false) Map<String, Object> request) {
        log.info("[discoverLocal] Starting local discovery");
        log.info("[discoverLocal] skillPackageManager={}, skillRegistry={}, skillDiscoverer={}", 
            skillPackageManager != null ? "available" : "null",
            skillRegistry != null ? "available" : "null",
            skillDiscoverer != null ? "available" : "null");
        
        LocalDiscoveryResultDTO result = new LocalDiscoveryResultDTO();
        List<net.ooder.skill.discovery.dto.discovery.CapabilityDTO> capabilities = new ArrayList<>();
        
        if (discoveryCoordinator != null) {
            log.info("[discoverLocal] Using DiscoveryCoordinator from scene-engine");
            try {
                CompletableFuture<List<net.ooder.scene.skill.model.RichSkill>> future = 
                    discoveryCoordinator.discover("LOCAL");
                List<net.ooder.scene.skill.model.RichSkill> skills = future.get(30, TimeUnit.SECONDS);
                log.info("[discoverLocal] DiscoveryCoordinator discovered {} skills", skills.size());
                
                if (skills != null && !skills.isEmpty()) {
                    for (net.ooder.scene.skill.model.RichSkill skill : skills) {
                        capabilities.add(convertRichSkillToCapabilityDTO(skill));
                    }
                    
                    capabilities = filterInternalCapabilities(capabilities);
                    
                    result.setCapabilities(capabilities);
                    result.setTotal(capabilities.size());
                    result.setSource("discovery-coordinator");
                    result.setTimestamp(System.currentTimeMillis());
                    
                    return ResultModel.success(result);
                }
                log.warn("[discoverLocal] DiscoveryCoordinator returned empty result, trying fallback");
            } catch (Throwable e) {
                log.error("[discoverLocal] DiscoveryCoordinator failed: {}", e.getMessage());
                result.setErrorMessage(e.getMessage());
            }
        }
        
        if (discoveryOrchestrator != null) {
            log.info("[discoverLocal] Using DiscoveryOrchestrator");
            try {
                DiscoveryResult discoveryResult = discoveryOrchestrator.discoverLocal();
                log.info("[discoverLocal] DiscoveryOrchestrator result: success={}, total={}", 
                    discoveryResult.isSuccess(), discoveryResult.getTotalCount());
                
                if (discoveryResult.isSuccess() && discoveryResult.getCapabilities() != null) {
                    for (CapabilityDTO cap : discoveryResult.getCapabilities()) {
                        capabilities.add(convertToDiscoveryCapabilityDTO(cap));
                    }
                    
                    capabilities = filterInternalCapabilities(capabilities);
                    
                    result.setCapabilities(capabilities);
                    result.setTotal(capabilities.size());
                    result.setSource("discovery-orchestrator");
                    result.setTimestamp(System.currentTimeMillis());
                    
                    return ResultModel.success(result);
                }
                log.warn("[discoverLocal] DiscoveryOrchestrator returned empty or failed result: {}", 
                    discoveryResult.getErrorMessage());
            } catch (Throwable e) {
                log.error("[discoverLocal] DiscoveryOrchestrator failed: {}", e.getMessage());
                result.setErrorMessage(e.getMessage());
            }
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
                    
                    capabilities = filterInternalCapabilities(capabilities);
                    
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
                    
                    capabilities = filterInternalCapabilities(capabilities);
                    
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
        
        if (skillPackageManager != null) {
            log.info("[discoverLocal] Using SE SDK SkillPackageManager.listInstalled()");
            try {
                CompletableFuture<List<InstalledSkill>> future = skillPackageManager.listInstalled();
                List<InstalledSkill> installedSkills = future.get(30, TimeUnit.SECONDS);
                log.info("[discoverLocal] Found {} installed skills from SkillPackageManager", 
                    installedSkills != null ? installedSkills.size() : 0);
                
                if (installedSkills != null && !installedSkills.isEmpty()) {
                    for (InstalledSkill skill : installedSkills) {
                        capabilities.add(convertInstalledSkillToCapabilityDTO(skill));
                    }
                    
                    capabilities = filterInternalCapabilities(capabilities);
                    
                    result.setCapabilities(capabilities);
                    result.setTotal(capabilities.size());
                    result.setSource("se-package-manager-installed");
                    result.setTimestamp(System.currentTimeMillis());
                    
                    return ResultModel.success(result);
                }
                log.warn("[discoverLocal] SkillPackageManager.listInstalled() returned empty result, trying next source");
            } catch (Throwable e) {
                log.error("[discoverLocal] SkillPackageManager.listInstalled() failed: {}", e.getMessage());
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
                    
                    capabilities = filterInternalCapabilities(capabilities);
                    
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
        
        log.error("[discoverLocal] No discovery service available, trying direct scan");
        
        String skillsPath = System.getProperty("user.dir") + "/skills";
        java.io.File skillsDir = new java.io.File(skillsPath);
        
        if (skillsDir.exists() && skillsDir.isDirectory()) {
            log.info("[discoverLocal] Trying direct scan of skills directory: {}", skillsPath);
            List<net.ooder.skill.discovery.dto.discovery.CapabilityDTO> directCapabilities = scanAllSkillDirectories(skillsDir);
            
            if (!directCapabilities.isEmpty()) {
                capabilities.addAll(directCapabilities);
                capabilities = filterInternalCapabilities(capabilities);
                
                result.setCapabilities(capabilities);
                result.setTotal(capabilities.size());
                result.setSource("direct-scan");
                result.setTimestamp(System.currentTimeMillis());
                
                log.info("[discoverLocal] Direct scan found {} capabilities", capabilities.size());
                return ResultModel.success(result);
            }
        }
        
        result.setCapabilities(capabilities);
        result.setTotal(0);
        result.setSource("none");
        result.setErrorMessage("无法获取技能数据：SE SDK 服务不可用，请检查配置");
        result.setTimestamp(System.currentTimeMillis());
        
        return ResultModel.error("无法获取技能数据: " + result.getErrorMessage());
    }
    
    private List<net.ooder.skill.discovery.dto.discovery.CapabilityDTO> scanAllSkillDirectories(java.io.File rootDir) {
        List<net.ooder.skill.discovery.dto.discovery.CapabilityDTO> capabilities = new ArrayList<>();
        java.io.File[] subDirs = rootDir.listFiles(java.io.File::isDirectory);
        
        if (subDirs != null) {
            for (java.io.File subDir : subDirs) {
                scanSkillDirectoryRecursive(subDir, capabilities);
            }
        }
        
        return capabilities;
    }
    
    private void scanSkillDirectoryRecursive(java.io.File dir, List<net.ooder.skill.discovery.dto.discovery.CapabilityDTO> capabilities) {
        java.io.File skillYaml = new java.io.File(dir, "skill.yaml");
        if (skillYaml.exists()) {
            try {
                net.ooder.skill.discovery.dto.discovery.CapabilityDTO cap = parseSkillYaml(skillYaml);
                if (cap != null) {
                    capabilities.add(cap);
                }
            } catch (Exception e) {
                log.warn("[scanSkillDirectoryRecursive] Failed to parse {}: {}", 
                    skillYaml.getAbsolutePath(), e.getMessage());
            }
        }
        
        java.io.File[] subDirs = dir.listFiles(java.io.File::isDirectory);
        if (subDirs != null) {
            for (java.io.File subDir : subDirs) {
                if (!subDir.getName().equals("target") && !subDir.getName().equals("test")) {
                    scanSkillDirectoryRecursive(subDir, capabilities);
                }
            }
        }
    }

    @PostMapping("/github")
    public ResultModel<GitDiscoveryResultDTO> discoverFromGitHub(@RequestBody(required = false) GitDiscoveryRequestDTO request) {
        log.info("[discoverFromGitHub] Starting GitHub discovery");
        
        GitDiscoveryResultDTO result = new GitDiscoveryResultDTO();
        List<net.ooder.skill.discovery.dto.discovery.CapabilityDTO> capabilities = new ArrayList<>();
        
        String repoUrl = request != null && request.getRepoUrl() != null ? request.getRepoUrl() : "https://github.com/ooderCN/skills";
        String branch = request != null && request.getBranch() != null ? request.getBranch() : "main";
        
        result.setRepoUrl(repoUrl);
        result.setBranch(branch);
        result.setSource("github");
        
        if (discoveryCoordinator != null) {
            log.info("[discoverFromGitHub] Using DiscoveryCoordinator from scene-engine");
            try {
                CompletableFuture<List<net.ooder.scene.skill.model.RichSkill>> future = 
                    discoveryCoordinator.discover("GITHUB");
                List<net.ooder.scene.skill.model.RichSkill> skills = future.get(60, TimeUnit.SECONDS);
                log.info("[discoverFromGitHub] DiscoveryCoordinator discovered {} skills from GitHub", skills.size());
                
                if (skills != null && !skills.isEmpty()) {
                    for (net.ooder.scene.skill.model.RichSkill skill : skills) {
                        capabilities.add(convertRichSkillToCapabilityDTO(skill));
                    }
                    
                    result.setCapabilities(capabilities);
                    result.setTotal(capabilities.size());
                    result.setTimestamp(System.currentTimeMillis());
                    return ResultModel.success(result);
                }
                log.warn("[discoverFromGitHub] DiscoveryCoordinator returned empty result, trying fallback");
            } catch (Exception e) {
                log.error("[discoverFromGitHub] DiscoveryCoordinator GitHub discovery failed: {}", e.getMessage());
                result.setErrorMessage(e.getMessage());
            }
        }
        
        if (discoveryOrchestrator != null) {
            log.info("[discoverFromGitHub] Using DiscoveryOrchestrator for GitHub");
            try {
                DiscoveryResult discoveryResult = discoveryOrchestrator.discoverFromGitHub(repoUrl, branch);
                log.info("[discoverFromGitHub] DiscoveryOrchestrator result: success={}, total={}", 
                    discoveryResult.isSuccess(), discoveryResult.getTotalCount());
                
                if (discoveryResult.isSuccess() && discoveryResult.getCapabilities() != null) {
                    for (CapabilityDTO cap : discoveryResult.getCapabilities()) {
                        capabilities.add(convertToDiscoveryCapabilityDTO(cap));
                    }
                    
                    result.setCapabilities(capabilities);
                    result.setTotal(capabilities.size());
                    result.setTimestamp(System.currentTimeMillis());
                    return ResultModel.success(result);
                }
                log.warn("[discoverFromGitHub] DiscoveryOrchestrator returned empty or failed result: {}", 
                    discoveryResult.getErrorMessage());
            } catch (Exception e) {
                log.error("[discoverFromGitHub] DiscoveryOrchestrator GitHub discovery failed: {}", e.getMessage());
                result.setErrorMessage(e.getMessage());
            }
        }
        
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
        List<net.ooder.skill.discovery.dto.discovery.CapabilityDTO> capabilities = new ArrayList<>();
        
        String repoUrl = request != null && request.getRepoUrl() != null ? request.getRepoUrl() : "https://gitee.com/ooderCN/skills";
        String branch = request != null && request.getBranch() != null ? request.getBranch() : "master";
        
        result.setRepoUrl(repoUrl);
        result.setBranch(branch);
        result.setSource("gitee");
        
        if (discoveryCoordinator != null) {
            log.info("[discoverFromGitee] Using DiscoveryCoordinator from scene-engine");
            try {
                CompletableFuture<List<net.ooder.scene.skill.model.RichSkill>> future = 
                    discoveryCoordinator.discover("GITEE");
                List<net.ooder.scene.skill.model.RichSkill> skills = future.get(60, TimeUnit.SECONDS);
                log.info("[discoverFromGitee] DiscoveryCoordinator discovered {} skills from Gitee", skills.size());
                
                if (skills != null && !skills.isEmpty()) {
                    for (net.ooder.scene.skill.model.RichSkill skill : skills) {
                        capabilities.add(convertRichSkillToCapabilityDTO(skill));
                    }
                    
                    result.setCapabilities(capabilities);
                    result.setTotal(capabilities.size());
                    result.setTimestamp(System.currentTimeMillis());
                    return ResultModel.success(result);
                }
                log.warn("[discoverFromGitee] DiscoveryCoordinator returned empty result, trying fallback");
            } catch (Exception e) {
                log.error("[discoverFromGitee] DiscoveryCoordinator Gitee discovery failed: {}", e.getMessage());
                result.setErrorMessage(e.getMessage());
            }
        }
        
        if (discoveryOrchestrator != null) {
            log.info("[discoverFromGitee] Using DiscoveryOrchestrator for Gitee");
            try {
                DiscoveryResult discoveryResult = discoveryOrchestrator.discoverFromGitee(repoUrl, branch);
                log.info("[discoverFromGitee] DiscoveryOrchestrator result: success={}, total={}", 
                    discoveryResult.isSuccess(), discoveryResult.getTotalCount());
                
                if (discoveryResult.isSuccess() && discoveryResult.getCapabilities() != null) {
                    for (CapabilityDTO cap : discoveryResult.getCapabilities()) {
                        capabilities.add(convertToDiscoveryCapabilityDTO(cap));
                    }
                    
                    result.setCapabilities(capabilities);
                    result.setTotal(capabilities.size());
                    result.setTimestamp(System.currentTimeMillis());
                    return ResultModel.success(result);
                }
                log.warn("[discoverFromGitee] DiscoveryOrchestrator returned empty or failed result: {}", 
                    discoveryResult.getErrorMessage());
            } catch (Exception e) {
                log.error("[discoverFromGitee] DiscoveryOrchestrator Gitee discovery failed: {}", e.getMessage());
                result.setErrorMessage(e.getMessage());
            }
        }
        
        if (useSeSdk && skillPackageManager != null) {
            log.info("[discoverFromGitee] Using SE SDK SkillPackageManager for Gitee");
            try {
                CompletableFuture<List<SkillPackage>> future = skillPackageManager.discoverAll(DiscoveryMethod.GITEE);
                List<SkillPackage> packages = future.get(60, TimeUnit.SECONDS);
                log.info("[discoverFromGitee] Discovered {} skills from Gitee via SE SDK", packages.size());
                
                // 验证 packages 列表
                if (packages == null || packages.isEmpty()) {
                    log.warn("[discoverFromGitee] Packages list is null or empty");
                } else {
                    log.info("[discoverFromGitee] First package: {}", packages.get(0).getSkillId());
                    log.info("[discoverFromGitee] Starting conversion of {} packages", packages.size());
                }
                
                int successCount = 0;
                int failCount = 0;
                int duplicateCount = 0;
                int filteredCount = 0;
                java.util.Map<String, net.ooder.skill.discovery.dto.discovery.CapabilityDTO> dedupMap = new java.util.LinkedHashMap<>();
                for (int i = 0; i < packages.size(); i++) {
                    SkillPackage pkg = packages.get(i);
                    try {
                        log.info("[discoverFromGitee] Processing package {}/{}: {}", i + 1, packages.size(), pkg.getSkillId());
                        net.ooder.skill.discovery.dto.discovery.CapabilityDTO dto = convertSkillPackageToCapabilityDTO(pkg);
                        String skillId = dto.getId();
                        if (dedupMap.containsKey(skillId)) {
                            net.ooder.skill.discovery.dto.discovery.CapabilityDTO existing = dedupMap.get(skillId);
                            if ("SCENE".equals(existing.getSkillForm()) && !"SCENE".equals(dto.getSkillForm())) {
                                log.info("[discoverFromGitee] Skipping duplicate {} (keeping SCENE version, ignoring PROVIDER)", skillId);
                                duplicateCount++;
                                continue;
                            } else if (!"SCENE".equals(existing.getSkillForm()) && "SCENE".equals(dto.getSkillForm())) {
                                log.info("[discoverFromGitee] Replacing {} with SCENE version", skillId);
                                dedupMap.put(skillId, dto);
                                duplicateCount++;
                                continue;
                            } else {
                                log.info("[discoverFromGitee] Keeping first occurrence of {}", skillId);
                                duplicateCount++;
                                continue;
                            }
                        }
                        dedupMap.put(skillId, dto);
                        successCount++;
                        log.info("[discoverFromGitee] Successfully converted package {}: category={}, skillForm={}",
                            pkg.getSkillId(), dto.getCategory(), dto.getSkillForm());
                    } catch (Exception e) {
                        failCount++;
                        log.error("[discoverFromGitee] Failed to convert package {}: {}", pkg.getSkillId(), e.getMessage(), e);
                    }
                }
                capabilities.addAll(dedupMap.values());

                java.util.Set<String> userFacingCategories = java.util.Set.of(
                    "llm", "knowledge", "biz", "util", "org", "msg", "vfs", "ui", "sys"
                );
                capabilities.removeIf(cap -> {
                    String skillForm = cap.getSkillForm();
                    String category = cap.getCategory();
                    if ("SCENE".equals(skillForm) || "DRIVER".equals(skillForm)) {
                        return false;
                    }
                    if ("PROVIDER".equals(skillForm) || "SKILL".equals(skillForm) || skillForm == null) {
                        if (category == null) {
                            cap.setCategory("util");
                            cap.setCapabilityCategory("util");
                            cap.setBusinessCategory("util");
                            return false;
                        }
                        return !userFacingCategories.contains(category.toLowerCase());
                    }
                    return false;
                });
                filteredCount = dedupMap.size() - capabilities.size();

                log.info("[discoverFromGitee] Conversion complete: success={}, duplicate={}, filtered={}, failed={}, total={}",
                    successCount, duplicateCount, filteredCount, failCount, capabilities.size());
                
                if (!capabilities.isEmpty()) {
                    result.setCapabilities(capabilities);
                    result.setTotal(capabilities.size());
                    result.setTimestamp(System.currentTimeMillis());
                    return ResultModel.success(result);
                }
                log.warn("[discoverFromGitee] SE SDK Gitee discovery returned empty result, trying next source");
            } catch (Exception e) {
                log.error("[discoverFromGitee] SE SDK Gitee discovery failed: {}", e.getMessage(), e);
                result.setErrorMessage(e.getMessage());
            }
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

    @PostMapping("/skill-center")
    public ResultModel<GitDiscoveryResultDTO> discoverFromSkillCenter(@RequestBody(required = false) GitDiscoveryRequestDTO request) {
        log.info("[discoverFromSkillCenter] Starting Skill Center discovery");
        
        GitDiscoveryResultDTO result = new GitDiscoveryResultDTO();
        List<net.ooder.skill.discovery.dto.discovery.CapabilityDTO> capabilities = new ArrayList<>();
        
        String skillCenterUrl = request != null && request.getRepoUrl() != null ? 
            request.getRepoUrl() : "https://skill.ooder.net/api/v1/skills";
        
        result.setRepoUrl(skillCenterUrl);
        result.setSource("skill-center");
        
        if (discoveryCoordinator != null) {
            log.info("[discoverFromSkillCenter] Using DiscoveryCoordinator from scene-engine");
            try {
                CompletableFuture<List<net.ooder.scene.skill.model.RichSkill>> future = 
                    discoveryCoordinator.discover("SKILL_CENTER");
                List<net.ooder.scene.skill.model.RichSkill> skills = future.get(60, TimeUnit.SECONDS);
                log.info("[discoverFromSkillCenter] DiscoveryCoordinator discovered {} skills from Skill Center", skills.size());
                
                if (skills != null && !skills.isEmpty()) {
                    for (net.ooder.scene.skill.model.RichSkill skill : skills) {
                        capabilities.add(convertRichSkillToCapabilityDTO(skill));
                    }
                    
                    result.setCapabilities(capabilities);
                    result.setTotal(capabilities.size());
                    result.setTimestamp(System.currentTimeMillis());
                    return ResultModel.success(result);
                }
            } catch (Exception e) {
                log.error("[discoverFromSkillCenter] DiscoveryCoordinator Skill Center discovery failed: {}", e.getMessage());
                result.setErrorMessage(e.getMessage());
            }
        }
        
        if (capabilities.isEmpty()) {
            log.warn("[discoverFromSkillCenter] Skill Center discovery returned no results, returning empty list");
            result.setCapabilities(capabilities);
            result.setTotal(0);
            result.setTimestamp(System.currentTimeMillis());
            result.setErrorMessage("能力中心暂未配置或无可用能力");
            return ResultModel.success(result);
        }
        
        result.setCapabilities(capabilities);
        result.setTotal(capabilities.size());
        result.setTimestamp(System.currentTimeMillis());
        
        return ResultModel.success(result);
    }

    @PostMapping("/git-repository")
    public ResultModel<GitDiscoveryResultDTO> discoverFromGitRepository(@RequestBody(required = false) GitDiscoveryRequestDTO request) {
        log.info("[discoverFromGitRepository] Starting generic Git repository discovery");
        
        GitDiscoveryResultDTO result = new GitDiscoveryResultDTO();
        List<net.ooder.skill.discovery.dto.discovery.CapabilityDTO> capabilities = new ArrayList<>();
        
        if (request == null || request.getRepoUrl() == null || request.getRepoUrl().isEmpty()) {
            log.error("[discoverFromGitRepository] No repository URL provided");
            result.setErrorMessage("请提供 Git 仓库地址");
            return ResultModel.error("请提供 Git 仓库地址");
        }
        
        String repoUrl = request.getRepoUrl();
        String branch = request.getBranch() != null ? request.getBranch() : "main";
        String token = request.getToken();
        
        result.setRepoUrl(repoUrl);
        result.setBranch(branch);
        result.setSource("git-repository");
        
        if (discoveryCoordinator != null) {
            log.info("[discoverFromGitRepository] Using DiscoveryCoordinator from scene-engine");
            try {
                CompletableFuture<List<net.ooder.scene.skill.model.RichSkill>> future = 
                    discoveryCoordinator.discover("GIT_REPOSITORY");
                List<net.ooder.scene.skill.model.RichSkill> skills = future.get(60, TimeUnit.SECONDS);
                log.info("[discoverFromGitRepository] DiscoveryCoordinator discovered {} skills from Git repository", skills.size());
                
                if (skills != null && !skills.isEmpty()) {
                    for (net.ooder.scene.skill.model.RichSkill skill : skills) {
                        capabilities.add(convertRichSkillToCapabilityDTO(skill));
                    }
                    
                    result.setCapabilities(capabilities);
                    result.setTotal(capabilities.size());
                    result.setTimestamp(System.currentTimeMillis());
                    return ResultModel.success(result);
                }
            } catch (Exception e) {
                log.error("[discoverFromGitRepository] DiscoveryCoordinator Git repository discovery failed: {}", e.getMessage());
                result.setErrorMessage(e.getMessage());
            }
        }
        
        if (useSeSdk && skillPackageManager != null) {
            log.info("[discoverFromGitRepository] Using SE SDK SkillPackageManager for Git repository");
            try {
                CompletableFuture<List<SkillPackage>> future = skillPackageManager.discoverAll(DiscoveryMethod.GIT_REPOSITORY);
                List<SkillPackage> packages = future.get(60, TimeUnit.SECONDS);
                log.info("[discoverFromGitRepository] Discovered {} skills from Git repository via SE SDK", packages.size());
                
                for (SkillPackage pkg : packages) {
                    capabilities.add(convertSkillPackageToCapabilityDTO(pkg));
                }
                
                result.setCapabilities(capabilities);
                result.setTotal(capabilities.size());
                result.setTimestamp(System.currentTimeMillis());
                
                return ResultModel.success(result);
            } catch (Exception e) {
                log.error("[discoverFromGitRepository] SE SDK Git repository discovery failed: {}", e.getMessage());
                result.setErrorMessage(e.getMessage());
            }
        }
        
        if (capabilities.isEmpty()) {
            log.error("[discoverFromGitRepository] No capabilities found from any source");
            result.setErrorMessage("无法从 Git 仓库获取技能数据，请检查仓库地址和访问权限");
            return ResultModel.error("无法从 Git 仓库获取技能数据: " + result.getErrorMessage());
        }
        
        result.setCapabilities(capabilities);
        result.setTotal(capabilities.size());
        result.setTimestamp(System.currentTimeMillis());
        
        return ResultModel.success(result);
    }

    @PostMapping("/auto")
    public ResultModel<LocalDiscoveryResultDTO> discoverAuto(@RequestBody(required = false) Map<String, Object> request) {
        log.info("[discoverAuto] Starting automatic discovery");
        
        LocalDiscoveryResultDTO result = new LocalDiscoveryResultDTO();
        List<net.ooder.skill.discovery.dto.discovery.CapabilityDTO> allCapabilities = new ArrayList<>();
        java.util.Set<String> seenSkillIds = new java.util.HashSet<>();
        
        if (discoveryCoordinator != null) {
            log.info("[discoverAuto] Using DiscoveryCoordinator for auto discovery");
            try {
                CompletableFuture<List<net.ooder.scene.skill.model.RichSkill>> future = 
                    discoveryCoordinator.discover("AUTO");
                List<net.ooder.scene.skill.model.RichSkill> skills = future.get(90, TimeUnit.SECONDS);
                log.info("[discoverAuto] DiscoveryCoordinator discovered {} skills via AUTO", skills.size());
                
                if (skills != null && !skills.isEmpty()) {
                    for (net.ooder.scene.skill.model.RichSkill skill : skills) {
                        if (!seenSkillIds.contains(skill.getSkillId())) {
                            allCapabilities.add(convertRichSkillToCapabilityDTO(skill));
                            seenSkillIds.add(skill.getSkillId());
                        }
                    }
                }
            } catch (Exception e) {
                log.error("[discoverAuto] DiscoveryCoordinator auto discovery failed: {}", e.getMessage());
            }
        }
        
        if (allCapabilities.isEmpty() && skillPackageManager != null) {
            log.info("[discoverAuto] Trying local discovery as fallback");
            try {
                CompletableFuture<List<SkillPackage>> future = skillPackageManager.discoverAll(DiscoveryMethod.LOCAL_FS);
                List<SkillPackage> packages = future.get(30, TimeUnit.SECONDS);
                
                for (SkillPackage pkg : packages) {
                    if (!seenSkillIds.contains(pkg.getSkillId())) {
                        allCapabilities.add(convertSkillPackageToCapabilityDTO(pkg));
                        seenSkillIds.add(pkg.getSkillId());
                    }
                }
            } catch (Exception e) {
                log.error("[discoverAuto] Local discovery fallback failed: {}", e.getMessage());
            }
        }
        
        if (allCapabilities.isEmpty() && skillRegistry != null) {
            log.info("[discoverAuto] Trying registry as last resort");
            try {
                List<InstalledSkill> installedSkills = skillRegistry.getInstalledSkills();
                for (InstalledSkill skill : installedSkills) {
                    if (!seenSkillIds.contains(skill.getSkillId())) {
                        allCapabilities.add(convertInstalledSkillToCapabilityDTO(skill));
                        seenSkillIds.add(skill.getSkillId());
                    }
                }
            } catch (Exception e) {
                log.error("[discoverAuto] Registry fallback failed: {}", e.getMessage());
            }
        }
        
        allCapabilities = filterInternalCapabilities(allCapabilities);
        
        result.setCapabilities(allCapabilities);
        result.setTotal(allCapabilities.size());
        result.setSource("auto");
        result.setTimestamp(System.currentTimeMillis());
        
        log.info("[discoverAuto] Auto discovery completed with {} capabilities", allCapabilities.size());
        
        return ResultModel.success(result);
    }

    @GetMapping("/categories/stats")
    public ResultModel<CategoryStatsDTO> getCategoryStats() {
        log.info("[getCategoryStats] Getting category statistics");
        
        if (skillRegistry != null) {
            try {
                List<InstalledSkill> skills = skillRegistry.getInstalledSkills();
                Map<String, Long> categoryCount = new HashMap<>();
                long installed = 0;
                long sceneCount = 0;
                long providerCount = 0;
                long driverCount = 0;
                
                for (InstalledSkill skill : skills) {
                    String category = skill.getSceneId() != null ? "scene" : "provider";
                    categoryCount.merge(category, 1L, Long::sum);
                    installed++;
                    
                    String skillForm = skill.getSkillForm();
                    if (skillForm == null || skillForm.isEmpty()) {
                        skillForm = "PROVIDER";
                    }
                    if ("SCENE".equals(skillForm)) {
                        sceneCount++;
                    } else if ("DRIVER".equals(skillForm)) {
                        driverCount++;
                    } else {
                        providerCount++;
                    }
                }
                
                CategoryStatsDTO stats = new CategoryStatsDTO();
                stats.setTotal(skills.size());
                stats.setCategories(categoryCount);
                stats.setInstalled(installed);
                stats.setNotInstalled(0L);
                stats.setSceneCount(sceneCount);
                stats.setProviderCount(providerCount);
                stats.setDriverCount(driverCount);
                
                return ResultModel.success(stats);
            } catch (Throwable e) {
                log.error("[getCategoryStats] Failed to get stats: {}", e.getMessage());
            }
        }
        
        CategoryStatsDTO emptyStats = new CategoryStatsDTO();
        emptyStats.setTotal(0);
        emptyStats.setMessage("No statistics available");
        return ResultModel.success(emptyStats);
    }
    
    @GetMapping("/categories/user-facing")
    public ResultModel<List<CategoryDTO>> getUserFacingCategories() {
        log.info("[getUserFacingCategories] Getting user facing categories");
        List<CategoryDTO> categories = new ArrayList<>();
        
        String[][] userCategories = {
            {"llm", "大模型", "ri-robot-line", "#6366f1"},
            {"knowledge", "知识库", "ri-book-2-line", "#10b981"},
            {"biz", "业务场景", "ri-briefcase-line", "#f59e0b"},
            {"org", "组织管理", "ri-team-line", "#3b82f6"},
            {"msg", "消息通知", "ri-message-3-line", "#ef4444"},
            {"vfs", "文件存储", "ri-folder-line", "#8b5cf6"},
            {"ui", "界面组件", "ri-layout-line", "#06b6d4"},
            {"sys", "系统管理", "ri-settings-3-line", "#64748b"}
        };
        
        for (String[] cat : userCategories) {
            CategoryDTO category = new CategoryDTO();
            category.setId(cat[0]);
            category.setName(cat[1]);
            category.setIcon(cat[2]);
            category.setColor(cat[3]);
            category.setUserFacing(true);
            categories.add(category);
        }
        
        return ResultModel.success(categories);
    }
    
    @GetMapping("/categories/all")
    public ResultModel<List<CategoryDTO>> getAllCategories() {
        log.info("[getAllCategories] Getting all categories with stats");
        List<CategoryDTO> categories = new ArrayList<>();
        
        String[][] allCategories = {
            {"llm", "大模型", "ri-robot-line", "#6366f1"},
            {"knowledge", "知识库", "ri-book-2-line", "#10b981"},
            {"biz", "业务场景", "ri-briefcase-line", "#f59e0b"},
            {"org", "组织管理", "ri-team-line", "#3b82f6"},
            {"msg", "消息通知", "ri-message-3-line", "#ef4444"},
            {"vfs", "文件存储", "ri-folder-line", "#8b5cf6"},
            {"ui", "界面组件", "ri-layout-line", "#06b6d4"},
            {"sys", "系统管理", "ri-settings-3-line", "#64748b"},
            {"payment", "支付", "ri-bank-card-line", "#22c55e"},
            {"media", "媒体", "ri-video-line", "#ec4899"},
            {"util", "工具", "ri-tools-line", "#78716c"}
        };
        
        for (String[] cat : allCategories) {
            CategoryDTO category = new CategoryDTO();
            category.setId(cat[0]);
            category.setName(cat[1]);
            category.setIcon(cat[2]);
            category.setColor(cat[3]);
            category.setCount(0);
            categories.add(category);
        }
        
        return ResultModel.success(categories);
    }
    
    @GetMapping("/categories/{categoryId}/subcategories")
    public ResultModel<List<Map<String, Object>>> getSubCategories(@PathVariable String categoryId) {
        log.info("[getSubCategories] Getting subcategories for: {}", categoryId);
        List<Map<String, Object>> subCategories = new ArrayList<>();
        
        return ResultModel.success(subCategories);
    }
    
    @GetMapping("/capability/{capabilityId}")
    public ResultModel<CapabilityDetailDTO> getCapabilityDetail(@PathVariable String capabilityId) {
        log.info("[getCapabilityDetail] Getting detail for: {}", capabilityId);
        
        CapabilityDetailDTO detail = new CapabilityDetailDTO();
        detail.setId(capabilityId);
        detail.setSkillId(capabilityId);
        detail.setName(capabilityId);
        detail.setInstalled(false);
        
        if (skillRegistry != null) {
            try {
                List<InstalledSkill> skills = skillRegistry.getInstalledSkills();
                for (InstalledSkill skill : skills) {
                    if (capabilityId.equals(skill.getSkillId())) {
                        detail.setName(skill.getName());
                        detail.setInstalled(true);
                        detail.setSkillForm(skill.getSkillForm());
                        detail.setSceneType(skill.getSceneId());
                        break;
                    }
                }
            } catch (Throwable e) {
                log.error("[getCapabilityDetail] Failed to get detail: {}", e.getMessage());
            }
        }
        
        return ResultModel.success(detail);
    }
    
    @PostMapping("/capability/{skillId}/activate")
    public ResultModel<Map<String, Object>> activateCapability(@PathVariable String skillId) {
        log.info("[activateCapability] Activating capability: {}", skillId);
        
        Map<String, Object> result = new HashMap<>();
        result.put("skillId", skillId);
        result.put("activated", true);
        result.put("timestamp", System.currentTimeMillis());
        
        if (pluginManager != null && pluginManager.isInstalled(skillId)) {
            log.info("[activateCapability] Plugin is installed, activation simulated: {}", skillId);
        }
        
        result.put("message", "激活成功");
        return ResultModel.success(result);
    }
    
    @PostMapping("/capability/{skillId}/deactivate")
    public ResultModel<Map<String, Object>> deactivateCapability(@PathVariable String skillId) {
        log.info("[deactivateCapability] Deactivating capability: {}", skillId);
        
        Map<String, Object> result = new HashMap<>();
        result.put("skillId", skillId);
        result.put("activated", false);
        result.put("timestamp", System.currentTimeMillis());
        
        result.put("message", "停用成功");
        return ResultModel.success(result);
    }
    
    @DeleteMapping("/dev/{skillId}")
    public ResultModel<Map<String, Object>> deleteDevCapability(@PathVariable String skillId) {
        log.info("[deleteDevCapability] Deleting dev capability: {}", skillId);
        
        Map<String, Object> result = new HashMap<>();
        result.put("skillId", skillId);
        result.put("timestamp", System.currentTimeMillis());
        
        String basePath = System.getProperty("user.dir");
        java.io.File devDir = new java.io.File(basePath, ".ooder/dev");
        
        java.io.File skillDir = findSkillDirectory(devDir, skillId);
        
        if (skillDir == null) {
            log.warn("[deleteDevCapability] Skill directory not found: {}", skillId);
            return ResultModel.error("未找到能力目录: " + skillId);
        }
        
        try {
            boolean deleted = deleteDirectory(skillDir);
            if (deleted) {
                log.info("[deleteDevCapability] Successfully deleted: {}", skillDir.getAbsolutePath());
                result.put("message", "删除成功");
                result.put("deletedPath", skillDir.getAbsolutePath());
                return ResultModel.success(result);
            } else {
                log.warn("[deleteDevCapability] Failed to delete: {}", skillDir.getAbsolutePath());
                return ResultModel.error("删除失败");
            }
        } catch (Exception e) {
            log.error("[deleteDevCapability] Error deleting {}: {}", skillId, e.getMessage());
            return ResultModel.error("删除异常: " + e.getMessage());
        }
    }
    
    private java.io.File findSkillDirectory(java.io.File parentDir, String skillId) {
        java.io.File[] files = parentDir.listFiles();
        if (files == null) return null;
        
        for (java.io.File file : files) {
            if (file.isDirectory()) {
                java.io.File skillYaml = new java.io.File(file, "skill.yaml");
                if (skillYaml.exists()) {
                    try {
                        org.yaml.snakeyaml.Yaml yaml = new org.yaml.snakeyaml.Yaml();
                        java.io.InputStream is = new java.io.FileInputStream(skillYaml);
                        Map<String, Object> data = yaml.load(is);
                        is.close();
                        
                        String id = (String) data.get("id");
                        if (id == null) {
                            Map<String, Object> metadata = (Map<String, Object>) data.get("metadata");
                            if (metadata != null) {
                                id = (String) metadata.get("id");
                            }
                        }
                        
                        if (skillId.equals(id)) {
                            return file;
                        }
                    } catch (Exception e) {
                        // ignore
                    }
                }
                
                java.io.File found = findSkillDirectory(file, skillId);
                if (found != null) return found;
            }
        }
        return null;
    }
    
    private boolean deleteDirectory(java.io.File directory) {
        if (directory.isDirectory()) {
            java.io.File[] files = directory.listFiles();
            if (files != null) {
                for (java.io.File file : files) {
                    deleteDirectory(file);
                }
            }
        }
        return directory.delete();
    }
    
    @PostMapping("/capability/{skillId}/share")
    public ResultModel<Map<String, Object>> shareCapability(
            @PathVariable String skillId,
            @RequestBody(required = false) Map<String, Object> request) {
        log.info("[shareCapability] Sharing capability: {} to {}", skillId, request);
        
        Map<String, Object> result = new HashMap<>();
        result.put("skillId", skillId);
        result.put("timestamp", System.currentTimeMillis());
        
        String target = request != null ? (String) request.get("target") : "gitee";
        
        result.put("target", target);
        result.put("message", "分享到 " + target + " 成功");
        result.put("shareUrl", "https://" + target + ".com/ooderCN/skills/" + skillId);
        
        return ResultModel.success(result);
    }
    
    @DeleteMapping("/capability/{skillId}/uninstall")
    public ResultModel<Map<String, Object>> uninstallCapability(@PathVariable String skillId) {
        log.info("[uninstallCapability] Uninstalling capability: {}", skillId);
        
        Map<String, Object> result = new HashMap<>();
        result.put("skillId", skillId);
        result.put("timestamp", System.currentTimeMillis());
        
        String basePath = System.getProperty("user.dir");
        java.io.File devDir = new java.io.File(basePath, ".ooder/dev");
        java.io.File skillsDir = new java.io.File(basePath, "skills");
        
        java.io.File skillDir = findSkillDirectory(devDir, skillId);
        if (skillDir == null) {
            skillDir = findSkillDirectory(skillsDir, skillId);
        }
        
        if (skillDir == null) {
            log.warn("[uninstallCapability] Skill directory not found: {}", skillId);
            return ResultModel.error("未找到能力目录: " + skillId);
        }
        
        try {
            boolean deleted = deleteDirectory(skillDir);
            if (deleted) {
                log.info("[uninstallCapability] Successfully uninstalled: {}", skillDir.getAbsolutePath());
                result.put("message", "卸载成功");
                result.put("deletedPath", skillDir.getAbsolutePath());
                return ResultModel.success(result);
            } else {
                log.warn("[uninstallCapability] Failed to uninstall: {}", skillDir.getAbsolutePath());
                return ResultModel.error("卸载失败");
            }
        } catch (Exception e) {
            log.error("[uninstallCapability] Error uninstalling {}: {}", skillId, e.getMessage());
            return ResultModel.error("卸载异常: " + e.getMessage());
        }
    }
    
    @PostMapping("/sync")
    public ResultModel<SyncResultDTO> syncFromSkills() {
        log.info("[syncFromSkills] Starting manual sync");
        
        SyncResultDTO result = new SyncResultDTO();
        
        if (discoveryCoordinator != null) {
            try {
                CompletableFuture<List<net.ooder.scene.skill.model.RichSkill>> future = 
                    discoveryCoordinator.discover("LOCAL");
                List<net.ooder.scene.skill.model.RichSkill> skills = future.get(30, TimeUnit.SECONDS);
                log.info("[syncFromSkills] DiscoveryCoordinator discovered {} skills", skills.size());
                result.setSynced(skills.size());
                result.setSkipped(0);
                result.setErrors(0);
                result.setTimestamp(System.currentTimeMillis());
                result.setMessage(String.format("同步完成: 成功 %d", skills.size()));
                return ResultModel.success(result);
            } catch (Throwable e) {
                log.error("[syncFromSkills] DiscoveryCoordinator sync failed: {}", e.getMessage());
            }
        }
        
        if (discoveryOrchestrator != null) {
            try {
                discoveryOrchestrator.reloadIndex();
                DiscoveryResult discoveryResult = discoveryOrchestrator.discoverLocal();
                result.setSynced(discoveryResult.getTotalCount());
                result.setSkipped(0);
                result.setErrors(0);
                result.setTimestamp(System.currentTimeMillis());
                result.setMessage(String.format("同步完成: 成功 %d", discoveryResult.getTotalCount()));
                return ResultModel.success(result);
            } catch (Throwable e) {
                log.error("[syncFromSkills] Failed to sync: {}", e.getMessage());
                result.setErrors(1);
                result.setMessage("同步失败: " + e.getMessage());
            }
        }
        
        if (skillDiscoverer != null) {
            try {
                CompletableFuture<List<SkillPackage>> future = skillDiscoverer.discover();
                List<SkillPackage> packages = future.get(30, TimeUnit.SECONDS);
                result.setSynced(packages.size());
                result.setSkipped(0);
                result.setErrors(0);
                result.setTimestamp(System.currentTimeMillis());
                result.setMessage(String.format("同步完成: 成功 %d", packages.size()));
                return ResultModel.success(result);
            } catch (Throwable e) {
                log.error("[syncFromSkills] Failed to sync from SE SDK: {}", e.getMessage());
                result.setErrors(1);
                result.setMessage("同步失败: " + e.getMessage());
            }
        }
        
        result.setTimestamp(System.currentTimeMillis());
        return ResultModel.success(result);
    }
    
    @GetMapping("/methods")
    public ResultModel<List<DiscoveryMethodDTO>> getDiscoveryMethods() {
        log.info("[getDiscoveryMethods] Getting available discovery methods");
        
        List<DiscoveryMethodDTO> methods = new ArrayList<>();
        
        methods.add(new DiscoveryMethodDTO("GITEE", "Gitee仓库", "ri-git-repository-line",
            "从Gitee仓库发现能力", "#ef4444", true));
        methods.add(new DiscoveryMethodDTO("GITHUB", "GitHub仓库", "ri-github-fill",
            "从GitHub仓库发现能力", "#6366f1", true));
        methods.add(new DiscoveryMethodDTO("LOCAL_FS", "本地文件系统", "ri-folder-line", 
            "扫描本地已安装的能力包", "#3b82f6", false));
        methods.add(new DiscoveryMethodDTO("SKILL_CENTER", "能力中心", "ri-cloud-line",
            "从能力中心发现可用能力", "#10b981", true));
        methods.add(new DiscoveryMethodDTO("GIT_REPOSITORY", "Git仓库", "ri-git-branch-line",
            "从任意Git仓库发现能力", "#f59e0b", true));
        methods.add(new DiscoveryMethodDTO("AUTO", "自动检测", "ri-magic-line",
            "自动选择最佳发现方式", "#64748b", false));
        
        for (DiscoveryMethodDTO method : methods) {
            method.setConfigFields(getConfigFields(method.getId()));
        }
        
        return ResultModel.success(methods);
    }

    @GetMapping("/config")
    public ResultModel<DiscoveryConfigDTO> getConfig() {
        log.info("[getConfig] Getting discovery config");
        DiscoveryConfigDTO config = new DiscoveryConfigDTO();
        config.setAutoScan(false);
        config.setScanInterval(3600);
        config.setSources(Arrays.asList("local", "github", "gitee"));
        return ResultModel.success(config);
    }

    @PutMapping("/config")
    public ResultModel<DiscoveryConfigDTO> updateConfig(@RequestBody DiscoveryConfigDTO config) {
        log.info("[updateConfig] Updating discovery config");
        return ResultModel.success(config);
    }
    
    @PostMapping("/install")
    public ResultModel<InstallResultDTO> installCapability(@RequestBody InstallSkillRequestDTO request) {
        String skillId = request.getSkillId();
        String source = request.getSource() != null ? request.getSource() : "local";
        
        log.info("[installCapability] Installing skill: {} from {}, type: {}", skillId, source, request.getType());
        
        InstallResultDTO result = new InstallResultDTO();
        result.setSkillId(skillId);
        result.setCapabilityId(skillId);
        result.setInstallSource(source);

        if (skillPackageManager == null) {
            log.error("[installCapability] SkillPackageManager not available");
            result.setStatus("failed");
            result.setMessage("SkillPackageManager 不可用，请检查 skill-hotplug-starter 配置");
            return ResultModel.error("SkillPackageManager 不可用，无法安装 skill");
        }

        try {
            CompletableFuture<Boolean> isInstalledFuture = skillPackageManager.isInstalled(skillId);
            Boolean isInstalled = isInstalledFuture.get(10, TimeUnit.SECONDS);

            if (Boolean.TRUE.equals(isInstalled)) {
                log.info("[installCapability] Skill already installed: {}", skillId);
                result.setStatus("installed");
                result.setMessage("已安装，跳过");
                result.setInstallTime(System.currentTimeMillis());
                return ResultModel.success(result);
            }

            DiscoveryMethod discoveryMethod = resolveDiscoveryMethod(source);
            log.info("[installCapability] Using discovery method: {} for skill: {}", discoveryMethod, skillId);

            try {
                SkillPackage skillPackage = skillPackageManager.discover(skillId, discoveryMethod).get(60, TimeUnit.SECONDS);
                if (skillPackage == null) {
                    log.error("[installCapability] Skill package not found: {} via {}", skillId, discoveryMethod);
                    result.setStatus("failed");
                    result.setMessage("未找到技能包: " + skillId);
                    return ResultModel.error("未找到技能包: " + skillId);
                }
                log.info("[installCapability] Discovered skill package: {} v{}", skillId, skillPackage.getVersion());
            } catch (Exception discoverEx) {
                log.warn("[installCapability] Discovery failed for {}, will try install anyway: {}", skillId, discoverEx.getMessage());
            }

            InstallRequest installRequest = new InstallRequest();
            installRequest.setSkillId(skillId);
            installRequest.setDiscoveryMethod(discoveryMethod);
            installRequest.setMode(InstallRequest.InstallMode.FULL_INSTALL);

            net.ooder.skills.api.InstallResult installResult = skillPackageManager.install(installRequest).get(120, TimeUnit.SECONDS);

            if (installResult != null && installResult.isSuccess()) {
                log.info("[installCapability] Skill installed successfully: {}", skillId);
                
                try {
                    net.ooder.skills.api.DependencyResult depResult = skillPackageManager.installDependencies(skillId).get(120, TimeUnit.SECONDS);
                    if (depResult != null && depResult.hasFailures()) {
                        log.warn("[installCapability] Some dependencies failed for {}: {}", skillId, depResult.getErrorMessage());
                    }
                    if (depResult != null && depResult.getInstalledCount() > 0) {
                        List<String> installedDeps = new ArrayList<>();
                        for (net.ooder.skills.api.DependencyResult.DependencyItemResult item : depResult.getItems()) {
                            if (item.isSuccess() && item.getAction() == net.ooder.skills.api.DependencyResult.DependencyItemResult.DependencyAction.INSTALLED) {
                                installedDeps.add(item.getDependencyId());
                            }
                        }
                        if (!installedDeps.isEmpty()) {
                            result.setInstalledDependencies(installedDeps);
                        }
                    }
                } catch (Exception depEx) {
                    log.warn("[installCapability] Failed to install dependencies for {}: {}", skillId, depEx.getMessage());
                }

                result.setStatus("installed");
                result.setMessage("安装成功");
                result.setInstallTime(System.currentTimeMillis());
                return ResultModel.success(result);
            } else {
                String error = installResult != null ? installResult.getError() : "未知错误";
                log.error("[installCapability] Failed to install {}: {}", skillId, error);
                result.setStatus("failed");
                result.setMessage("安装失败: " + error);
                return ResultModel.error("安装失败: " + error);
            }
        } catch (Exception e) {
            log.error("[installCapability] Error installing {}: {}", skillId, e.getMessage(), e);
            result.setStatus("failed");
            result.setMessage("安装异常: " + e.getMessage());
            return ResultModel.error("安装异常: " + e.getMessage());
        }
    }

    private DiscoveryMethod resolveDiscoveryMethod(String source) {
        if (source == null || source.isEmpty()) {
            return DiscoveryMethod.LOCAL_FS;
        }
        switch (source.toUpperCase()) {
            case "GITEE":
                return DiscoveryMethod.GITEE;
            case "GITHUB":
                return DiscoveryMethod.GITHUB;
            case "LOCAL":
            case "LOCAL_FS":
                return DiscoveryMethod.LOCAL_FS;
            default:
                return DiscoveryMethod.LOCAL_FS;
        }
    }

    @PostMapping("/refresh")
    public ResultModel<RefreshResultDTO> refreshDiscovery() {
        log.info("[refreshDiscovery] Refreshing discovery");
        
        RefreshResultDTO result = new RefreshResultDTO();
        
        if (discoveryCoordinator != null) {
            try {
                CompletableFuture<List<net.ooder.scene.skill.model.RichSkill>> future = 
                    discoveryCoordinator.discover("LOCAL");
                List<net.ooder.scene.skill.model.RichSkill> skills = future.get(30, TimeUnit.SECONDS);
                log.info("[refreshDiscovery] DiscoveryCoordinator discovered {} skills", skills.size());
                result.setDiscovered(skills.size());
                result.setSource("discovery-coordinator");
                result.setTimestamp(System.currentTimeMillis());
                return ResultModel.success(result);
            } catch (Throwable e) {
                log.error("[refreshDiscovery] DiscoveryCoordinator failed: {}", e.getMessage());
                result.setError(e.getMessage());
            }
        }
        
        if (discoveryOrchestrator != null) {
            try {
                discoveryOrchestrator.reloadIndex();
                DiscoveryResult discoveryResult = discoveryOrchestrator.discoverLocal();
                log.info("[refreshDiscovery] Discovered {} skills", discoveryResult.getTotalCount());
                result.setDiscovered(discoveryResult.getTotalCount());
                result.setSource("discovery-orchestrator");
                result.setTimestamp(System.currentTimeMillis());
                return ResultModel.success(result);
            } catch (Throwable e) {
                log.error("[refreshDiscovery] Failed to refresh: {}", e.getMessage());
                result.setError(e.getMessage());
            }
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

    private net.ooder.skill.discovery.dto.discovery.CapabilityDTO convertToDiscoveryCapabilityDTO(CapabilityDTO cap) {
        net.ooder.skill.discovery.dto.discovery.CapabilityDTO dto = new net.ooder.skill.discovery.dto.discovery.CapabilityDTO();
        dto.setId(cap.getId());
        dto.setName(cap.getName());
        dto.setVersion(cap.getVersion());
        dto.setSkillId(cap.getSkillId());
        dto.setSceneType(cap.getSceneType());
        dto.setDescription(cap.getDescription());
        dto.setTags(cap.getTags());
        dto.setSkillForm(cap.getSkillForm());
        dto.setSceneCapability(cap.isSceneCapability());
        dto.setCategory(cap.getCategory());
        dto.setCapabilityCategory(cap.getCategory());
        dto.setBusinessCategory(cap.getCategory());
        
        boolean installed = checkIfInstalled(cap.getSkillId());
        dto.setInstalled(installed);
        dto.setStatus(installed ? "installed" : (cap.getStatus() != null ? cap.getStatus() : "available"));
        
        return dto;
    }

    private net.ooder.skill.discovery.dto.discovery.CapabilityDTO convertRichSkillToCapabilityDTO(net.ooder.scene.skill.model.RichSkill skill) {
        net.ooder.skill.discovery.dto.discovery.CapabilityDTO dto = new net.ooder.skill.discovery.dto.discovery.CapabilityDTO();
        dto.setId(skill.getSkillId());
        dto.setName(skill.getName());
        dto.setVersion(skill.getVersion());
        dto.setSkillId(skill.getSkillId());
        dto.setDescription(skill.getDescription());
        
        String category = skill.getCategory() != null ? skill.getCategory().getCode() : null;
        String skillForm = skill.getForm() != null ? skill.getForm().name() : null;
        
        dto.setCategory(category);
        dto.setCapabilityCategory(category);
        dto.setBusinessCategory(category);
        dto.setSkillForm(skillForm);
        dto.setSceneCapability("SCENE".equals(skillForm));
        dto.setType("SCENE".equals(skillForm) ? "SCENE" : "SKILL");
        
        boolean installed = checkIfInstalled(skill.getSkillId());
        dto.setInstalled(installed);
        dto.setStatus(installed ? "installed" : "available");
        
        return dto;
    }

    private net.ooder.skill.discovery.dto.discovery.CapabilityDTO convertSkillPackageToCapabilityDTO(SkillPackage pkg) {
        log.debug("[convertSkillPackageToCapabilityDTO] Converting package: {}", pkg.getSkillId());
        
        // 使用 SDK 提供的 DiscoveryConverter 进行转换
        net.ooder.skill.discovery.dto.discovery.CapabilityDTO dto = DiscoveryConverter.toCapabilityDTO(pkg);
        
        // 检查安装状态
        boolean installed = checkIfInstalled(pkg.getSkillId());
        dto.setInstalled(installed);
        dto.setStatus(installed ? "installed" : "available");
        
        // SDK 3.0.1+ 已经自动推断 category 和 skillForm，无需 OS 层再次推断
        // 直接使用 SDK 返回的值
        String category = dto.getCategory();
        String skillForm = dto.getSkillForm();
        
        log.debug("[convertSkillPackageToCapabilityDTO] Package {}: category={}, skillForm={}", 
            pkg.getSkillId(), category, skillForm);
        
        // 确保所有相关字段一致
        dto.setCapabilityCategory(category);
        dto.setBusinessCategory(category);
        
        boolean isScene = "SCENE".equals(skillForm);
        dto.setSceneCapability(isScene);
        dto.setType(isScene ? "SCENE" : "SKILL");
        
        return dto;
    }
    
    private net.ooder.skill.discovery.dto.discovery.CapabilityDTO convertInstalledSkillToCapabilityDTO(InstalledSkill skill) {
        net.ooder.skill.discovery.dto.discovery.CapabilityDTO dto = new net.ooder.skill.discovery.dto.discovery.CapabilityDTO();
        dto.setId(skill.getSkillId());
        dto.setName(skill.getName());
        dto.setVersion(skill.getVersion());
        dto.setSkillId(skill.getSkillId());
        dto.setSceneType(skill.getSceneId());
        dto.setStatus(skill.getStatus());
        dto.setDependencies(skill.getDependencies());
        dto.setInstalled(true);
        
        // 使用 SDK 提供的推断值
        String category = skill.getCategory();
        String skillForm = skill.getSkillForm();
        
        dto.setCategory(category);
        dto.setCapabilityCategory(category);
        dto.setBusinessCategory(category);
        dto.setSkillForm(skillForm);
        
        boolean isScene = "SCENE".equals(skillForm);
        dto.setSceneCapability(isScene);
        dto.setType(isScene ? "SCENE" : "SKILL");
        
        return dto;
    }
    
    private boolean checkIfInstalled(String skillId) {
        if (skillId == null) {
            return false;
        }
        
        if (pluginManager != null) {
            boolean installed = pluginManager.isInstalled(skillId);
            log.info("[checkIfInstalled] skillId={}, installed={} (from PluginManager)", skillId, installed);
            return installed;
        }
        
        if (skillPackageManager == null) {
            log.info("[checkIfInstalled] skillPackageManager is null, skillId={}, returning false", skillId);
            return false;
        }
        try {
            CompletableFuture<Boolean> future = skillPackageManager.isInstalled(skillId);
            Boolean result = future.get(5, TimeUnit.SECONDS);
            log.info("[checkIfInstalled] skillId={}, installed={} (from SkillPackageManager)", skillId, result);
            return Boolean.TRUE.equals(result);
        } catch (Exception e) {
            log.warn("[checkIfInstalled] Could not check install status for {}: {}", skillId, e.getMessage());
            return false;
        }
    }
    
    private List<net.ooder.skill.discovery.dto.discovery.CapabilityDTO> filterInternalCapabilities(
            List<net.ooder.skill.discovery.dto.discovery.CapabilityDTO> capabilities) {
        if (capabilities == null || capabilities.isEmpty()) {
            return capabilities;
        }
        
        int originalSize = capabilities.size();
        List<net.ooder.skill.discovery.dto.discovery.CapabilityDTO> filtered = new ArrayList<>();
        
        for (net.ooder.skill.discovery.dto.discovery.CapabilityDTO cap : capabilities) {
            if (isInternalCapability(cap)) {
                log.debug("[filterInternalCapabilities] Filtering out internal capability: {}", cap.getSkillId());
                continue;
            }
            filtered.add(cap);
        }
        
        int filteredCount = originalSize - filtered.size();
        if (filteredCount > 0) {
            log.info("[filterInternalCapabilities] Filtered {} internal capabilities, remaining {}", 
                filteredCount, filtered.size());
        }
        
        return filtered;
    }
    
    private boolean isInternalCapability(net.ooder.skill.discovery.dto.discovery.CapabilityDTO cap) {
        if (cap == null) {
            return false;
        }
        
        if ("internal".equals(cap.getVisibility())) {
            return true;
        }
        
        if ("INTERNAL".equals(cap.getSkillForm())) {
            return true;
        }
        
        if ("SCENE_INTERNAL".equals(cap.getOwnership())) {
            return true;
        }
        
        String skillId = cap.getSkillId();
        if (skillId != null) {
            if (skillId.startsWith("skill-") && !isUserFacingSkill(cap)) {
                return true;
            }
        }
        
        return false;
    }
    
    private boolean isUserFacingSkill(net.ooder.skill.discovery.dto.discovery.CapabilityDTO cap) {
        String businessCategory = cap.getBusinessCategory();
        if (businessCategory != null && !businessCategory.isEmpty()) {
            String categoryLower = businessCategory.toLowerCase();
            if ("ai".equals(categoryLower) 
                || "service".equals(categoryLower)
                || "communication".equals(categoryLower)
                || "storage".equals(categoryLower)
                || "custom".equals(categoryLower)
                || "llm".equals(categoryLower)
                || "knowledge".equals(categoryLower)
                || "biz".equals(categoryLower)
                || "scene".equals(categoryLower)
                || "org".equals(categoryLower)
                || "msg".equals(categoryLower)
                || "vfs".equals(categoryLower)
                || "ui".equals(categoryLower)
                || "sys".equals(categoryLower)
                || "util".equals(categoryLower)) {
                return true;
            }
        }
        
        String category = cap.getCategory();
        if (category != null && !category.isEmpty()) {
            String categoryLower = category.toLowerCase();
            return "llm".equals(categoryLower) 
                || "knowledge".equals(categoryLower)
                || "biz".equals(categoryLower)
                || "scene".equals(categoryLower)
                || "org".equals(categoryLower)
                || "msg".equals(categoryLower)
                || "vfs".equals(categoryLower)
                || "ui".equals(categoryLower)
                || "sys".equals(categoryLower)
                || "ai".equals(categoryLower)
                || "service".equals(categoryLower)
                || "communication".equals(categoryLower)
                || "storage".equals(categoryLower)
                || "custom".equals(categoryLower)
                || "util".equals(categoryLower);
        }
        
        String skillForm = cap.getSkillForm();
        if ("SCENE".equals(skillForm) || "PROVIDER".equals(skillForm) || "DRIVER".equals(skillForm)) {
            return true;
        }
        
        return false;
    }

    @GetMapping("/capabilities")
    public ResultModel<PageResult<net.ooder.skill.discovery.dto.discovery.CapabilityDTO>> discoverCapabilities(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String keyword) {
        
        log.info("[discoverCapabilities] Discovering capabilities - pageNum: {}, pageSize: {}", pageNum, pageSize);
        
        List<net.ooder.skill.discovery.dto.discovery.CapabilityDTO> capabilities = new ArrayList<>();
        
        PageResult<net.ooder.skill.discovery.dto.discovery.CapabilityDTO> result = new PageResult<>();
        result.setList(capabilities);
        result.setTotal(0);
        result.setPageNum(pageNum);
        result.setPageSize(pageSize);
        
        return ResultModel.success(result);
    }

    @GetMapping("/capabilities/types")
    public ResultModel<List<CapabilityTypeDTO>> getCapabilityTypes() {
        log.info("[getCapabilityTypes] Getting capability types");
        
        List<CapabilityTypeDTO> types = new ArrayList<>();
        
        CapabilityTypeDTO type1 = new CapabilityTypeDTO();
        type1.setId("skill");
        type1.setName("技能");
        type1.setDescription("可安装的功能模块");
        types.add(type1);
        
        CapabilityTypeDTO type2 = new CapabilityTypeDTO();
        type2.setId("driver");
        type2.setName("驱动");
        type2.setDescription("外部系统集成驱动");
        types.add(type2);
        
        CapabilityTypeDTO type3 = new CapabilityTypeDTO();
        type3.setId("connector");
        type3.setName("连接器");
        type3.setDescription("数据源连接器");
        types.add(type3);
        
        return ResultModel.success(types);
    }

    @GetMapping("/report")
    public ResultModel<SkillReportDTO> getFullReport() {
        log.info("[getFullReport] Generating full skill report");
        
        SkillReportDTO report = new SkillReportDTO();
        report.setTimestamp(System.currentTimeMillis());
        
        List<SkillPathInfoDTO> allSkills = new ArrayList<>();
        List<InstalledSkill> installedSkills = new ArrayList<>();
        
        if (skillRegistry != null) {
            try {
                installedSkills = skillRegistry.getInstalledSkills();
                log.info("[getFullReport] Found {} installed skills from registry", installedSkills.size());
            } catch (Throwable e) {
                log.error("[getFullReport] Failed to get installed skills: {}", e.getMessage());
            }
        }
        
        if (skillPackageManager != null) {
            try {
                CompletableFuture<List<SkillPackage>> future = skillPackageManager.discoverAll(DiscoveryMethod.LOCAL_FS);
                List<SkillPackage> packages = future.get(30, TimeUnit.SECONDS);
                log.info("[getFullReport] Discovered {} skill packages from local FS", packages.size());
                
                for (SkillPackage pkg : packages) {
                    SkillPathInfoDTO info = new SkillPathInfoDTO();
                    info.setSkillId(pkg.getSkillId());
                    info.setName(pkg.getName());
                    info.setVersion(pkg.getVersion() != null ? pkg.getVersion().toString() : "1.0.0");
                    info.setDescription(pkg.getDescription());
                    info.setCategory(pkg.getCategory());
                    info.setSkillForm(pkg.getForm() != null ? pkg.getForm().name() : "PROVIDER");
                    info.setInstalled(checkIfInstalled(pkg.getSkillId()));
                    
                    String basePath = System.getProperty("user.dir");
                    String skillPath = basePath + "\\skills";
                    if (pkg.getSkillId() != null) {
                        if (pkg.getSkillId().contains("skill-")) {
                            info.setDirectory(detectDirectory(pkg.getSkillId()));
                            info.setAbsolutePath(skillPath + "\\" + info.getDirectory() + "\\" + pkg.getSkillId());
                        }
                    }
                    allSkills.add(info);
                }
            } catch (Throwable e) {
                log.error("[getFullReport] Failed to discover from local FS: {}", e.getMessage());
            }
        }
        
        if (allSkills.isEmpty() && !installedSkills.isEmpty()) {
            for (InstalledSkill skill : installedSkills) {
                SkillPathInfoDTO info = new SkillPathInfoDTO();
                info.setSkillId(skill.getSkillId());
                info.setName(skill.getName());
                info.setVersion(skill.getVersion());
                info.setCategory(skill.getCategory());
                info.setSkillForm(skill.getSkillForm());
                info.setInstalled(true);
                info.setDirectory(detectDirectory(skill.getSkillId()));
                String basePath = System.getProperty("user.dir");
                info.setAbsolutePath(basePath + "\\skills\\" + info.getDirectory() + "\\" + skill.getSkillId());
                allSkills.add(info);
            }
        }
        
        report.setTotal(allSkills.size());
        report.setSkillPaths(allSkills);
        
        report.setDirectoryStats(buildDirectoryStats(allSkills));
        report.setByBusinessCategory(buildBusinessCategoryStats(allSkills));
        report.setBySkillForm(buildSkillFormStats(allSkills));
        report.setBySkillCategory(buildSkillCategoryStats(allSkills));
        report.setByCapabilityCategory(buildCapabilityCategoryStats(allSkills));
        report.setByVisibility(buildVisibilityStats(allSkills));
        report.setDimensionComparison(buildDimensionComparison(allSkills));
        report.setTestSuggestion(buildTestSuggestion(allSkills));
        
        return ResultModel.success(report);
    }
    
    @GetMapping("/report/category/{category}")
    public ResultModel<CategoryDetailDTO> getCategoryDetailReport(@PathVariable String category) {
        log.info("[getCategoryDetailReport] Getting detail for category: {}", category);
        
        List<SkillPathInfoDTO> allSkills = getAllSkillPaths();
        
        CategoryDetailDTO detail = new CategoryDetailDTO();
        detail.setCode(category);
        detail.setName(getCategoryName(category));
        detail.setIcon(getCategoryIcon(category));
        detail.setColor(getCategoryColor(category));
        
        List<String> skillIds = new ArrayList<>();
        List<String> paths = new ArrayList<>();
        
        for (SkillPathInfoDTO skill : allSkills) {
            if (category.equalsIgnoreCase(skill.getCategory()) || 
                category.equalsIgnoreCase(skill.getSkillForm())) {
                skillIds.add(skill.getSkillId());
                if (skill.getAbsolutePath() != null) {
                    paths.add(skill.getAbsolutePath());
                }
            }
        }
        
        detail.setCount(skillIds.size());
        detail.setPercentage(allSkills.size() > 0 ? Math.round((skillIds.size() * 100.0f) / allSkills.size()) : 0);
        detail.setSkillIds(skillIds);
        detail.setAbsolutePaths(paths);
        
        return ResultModel.success(detail);
    }
    
    @GetMapping("/report/directory-tree")
    public ResultModel<List<DirectoryTreeNodeDTO>> getDirectoryTree() {
        log.info("[getDirectoryTree] Getting directory tree structure");
        
        List<DirectoryTreeNodeDTO> tree = new ArrayList<>();
        List<SkillPathInfoDTO> allSkills = getAllSkillPaths();
        
        for (SkillDirectory dir : SkillDirectory.getDisplayOrder()) {
            DirectoryTreeNodeDTO node = new DirectoryTreeNodeDTO();
            node.setName(dir.getCode());
            node.setDescription(dir.getDisplayName());
            node.setIcon(dir.getIcon());
            node.setColor(dir.getColor());
            
            List<DirectoryTreeChildDTO> children = new ArrayList<>();
            for (SkillPathInfoDTO skill : allSkills) {
                if (dir.getCode().equals(skill.getDirectory())) {
                    DirectoryTreeChildDTO child = new DirectoryTreeChildDTO();
                    child.setSkillId(skill.getSkillId());
                    child.setName(skill.getName());
                    child.setAbsolutePath(skill.getAbsolutePath());
                    child.setCategory(skill.getCategory());
                    child.setSkillForm(skill.getSkillForm());
                    children.add(child);
                }
            }
            node.setChildren(children);
            node.setCount(children.size());
            tree.add(node);
        }
        
        return ResultModel.success(tree);
    }
    
    @GetMapping("/report/export")
    public ResultModel<String> exportReport(@RequestParam(defaultValue = "markdown") String format) {
        log.info("[exportReport] Exporting report in format: {}", format);
        
        SkillReportDTO report = getFullReport().getData();
        if (report == null) {
            return ResultModel.error("Failed to generate report");
        }
        
        String content;
        if ("json".equalsIgnoreCase(format)) {
            content = exportAsJson(report);
        } else if ("csv".equalsIgnoreCase(format)) {
            content = exportAsCsv(report);
        } else {
            content = exportAsMarkdown(report);
        }
        
        return ResultModel.success(content);
    }
    
    private List<SkillPathInfoDTO> getAllSkillPaths() {
        List<SkillPathInfoDTO> allSkills = new ArrayList<>();
        
        if (skillPackageManager != null) {
            try {
                CompletableFuture<List<SkillPackage>> future = skillPackageManager.discoverAll(DiscoveryMethod.LOCAL_FS);
                List<SkillPackage> packages = future.get(30, TimeUnit.SECONDS);
                
                for (SkillPackage pkg : packages) {
                    SkillPathInfoDTO info = new SkillPathInfoDTO();
                    info.setSkillId(pkg.getSkillId());
                    info.setName(pkg.getName());
                    info.setVersion(pkg.getVersion() != null ? pkg.getVersion().toString() : "1.0.0");
                    info.setDescription(pkg.getDescription());
                    info.setCategory(pkg.getCategory());
                    info.setSkillForm(pkg.getForm() != null ? pkg.getForm().name() : "PROVIDER");
                    info.setInstalled(checkIfInstalled(pkg.getSkillId()));
                    info.setDirectory(detectDirectory(pkg.getSkillId()));
                    String basePath = System.getProperty("user.dir");
                    info.setAbsolutePath(basePath + "\\skills\\" + info.getDirectory() + "\\" + pkg.getSkillId());
                    allSkills.add(info);
                }
            } catch (Throwable e) {
                log.error("[getAllSkillPaths] Failed: {}", e.getMessage());
            }
        }
        
        return allSkills;
    }
    
    private String detectDirectory(String skillId) {
        return directoryDetector.detect(skillId).getCode();
    }
    
    private List<DirectoryStatsDTO> buildDirectoryStats(List<SkillPathInfoDTO> skills) {
        List<DirectoryStatsDTO> stats = new ArrayList<>();
        Map<String, Integer> counts = new HashMap<>();
        
        for (SkillPathInfoDTO skill : skills) {
            String dir = skill.getDirectory() != null ? skill.getDirectory() : "unknown";
            counts.merge(dir, 1, Integer::sum);
        }
        
        for (SkillDirectory dir : SkillDirectory.getDisplayOrder()) {
            int count = counts.getOrDefault(dir.getCode(), 0);
            if (count > 0) {
                stats.add(new DirectoryStatsDTO(dir.getCode(), dir.getDisplayName(), count, skills.size()));
            }
        }
        
        return stats;
    }
    
    private List<CategoryDetailDTO> buildBusinessCategoryStats(List<SkillPathInfoDTO> skills) {
        Map<String, List<SkillPathInfoDTO>> categoryMap = new HashMap<>();
        
        for (SkillPathInfoDTO skill : skills) {
            String cat = skill.getCategory() != null ? skill.getCategory().toLowerCase() : "other";
            categoryMap.computeIfAbsent(cat, k -> new ArrayList<>()).add(skill);
        }
        
        List<CategoryDetailDTO> result = new ArrayList<>();
        for (CapabilityCategory cat : CapabilityCategory.values()) {
            List<SkillPathInfoDTO> catSkills = categoryMap.get(cat.getCode());
            if (catSkills != null && !catSkills.isEmpty()) {
                CategoryDetailDTO detail = new CategoryDetailDTO(
                    cat.getCode(), 
                    cat.getDisplayName(), 
                    catSkills.size(), 
                    skills.size()
                );
                detail.setIcon(cat.getIcon());
                detail.setColor(getCategoryColor(cat));
                
                List<String> skillIds = new ArrayList<>();
                List<String> paths = new ArrayList<>();
                for (SkillPathInfoDTO s : catSkills) {
                    skillIds.add(s.getSkillId());
                    if (s.getAbsolutePath() != null) {
                        paths.add(s.getAbsolutePath());
                    }
                }
                detail.setSkillIds(skillIds);
                detail.setAbsolutePaths(paths);
                result.add(detail);
            }
        }
        
        return result;
    }
    
    private String getCategoryColor(CapabilityCategory category) {
        switch (category) {
            case SYS: return "#64748b";
            case BIZ: return "#f59e0b";
            case LLM: return "#6366f1";
            case MSG: return "#ef4444";
            case ORG: return "#3b82f6";
            case VFS: return "#8b5cf6";
            case KNOWLEDGE: return "#10b981";
            case PAYMENT: return "#22c55e";
            case MEDIA: return "#ec4899";
            case UTIL: return "#78716c";
            default: return "#8c8c8c";
        }
    }
    
    private List<CategoryDetailDTO> buildSkillFormStats(List<SkillPathInfoDTO> skills) {
        Map<String, List<SkillPathInfoDTO>> formMap = new HashMap<>();
        
        for (SkillPathInfoDTO skill : skills) {
            String form = skill.getSkillForm() != null ? skill.getSkillForm() : "PROVIDER";
            formMap.computeIfAbsent(form, k -> new ArrayList<>()).add(skill);
        }
        
        String[][] formInfo = {
            {"PROVIDER", "服务提供者", "ri-cpu-line", "#1890ff"},
            {"DRIVER", "驱动适配器", "ri-steering-line", "#52c41a"},
            {"SCENE", "场景应用", "ri-layout-grid-line", "#722ed1"}
        };
        
        List<CategoryDetailDTO> result = new ArrayList<>();
        for (String[] info : formInfo) {
            List<SkillPathInfoDTO> formSkills = formMap.get(info[0]);
            if (formSkills != null && !formSkills.isEmpty()) {
                CategoryDetailDTO detail = new CategoryDetailDTO(info[0], info[1], formSkills.size(), skills.size());
                detail.setIcon(info[2]);
                detail.setColor(info[3]);
                
                List<String> skillIds = new ArrayList<>();
                List<String> paths = new ArrayList<>();
                for (SkillPathInfoDTO s : formSkills) {
                    skillIds.add(s.getSkillId());
                    if (s.getAbsolutePath() != null) {
                        paths.add(s.getAbsolutePath());
                    }
                }
                detail.setSkillIds(skillIds);
                detail.setAbsolutePaths(paths);
                result.add(detail);
            }
        }
        
        return result;
    }
    
    private List<CategoryDetailDTO> buildSkillCategoryStats(List<SkillPathInfoDTO> skills) {
        return buildBusinessCategoryStats(skills);
    }
    
    private List<CategoryDetailDTO> buildCapabilityCategoryStats(List<SkillPathInfoDTO> skills) {
        return buildBusinessCategoryStats(skills);
    }
    
    private List<CategoryDetailDTO> buildVisibilityStats(List<SkillPathInfoDTO> skills) {
        List<CategoryDetailDTO> result = new ArrayList<>();
        
        int publicCount = 0;
        int internalCount = 0;
        
        for (SkillPathInfoDTO skill : skills) {
            String dir = skill.getDirectory();
            if ("_system".equals(dir)) {
                internalCount++;
            } else {
                publicCount++;
            }
        }
        
        if (publicCount > 0) {
            CategoryDetailDTO publicDetail = new CategoryDetailDTO("public", "公开", publicCount, skills.size());
            publicDetail.setIcon("ri-global-line");
            publicDetail.setColor("#10b981");
            result.add(publicDetail);
        }
        
        if (internalCount > 0) {
            CategoryDetailDTO internalDetail = new CategoryDetailDTO("internal", "内部", internalCount, skills.size());
            internalDetail.setIcon("ri-lock-line");
            internalDetail.setColor("#64748b");
            result.add(internalDetail);
        }
        
        return result;
    }
    
    private List<DimensionComparisonDTO> buildDimensionComparison(List<SkillPathInfoDTO> skills) {
        List<DimensionComparisonDTO> result = new ArrayList<>();
        
        for (SkillPathInfoDTO skill : skills) {
            DimensionComparisonDTO comp = new DimensionComparisonDTO();
            comp.setSkillId(skill.getSkillId());
            comp.setName(skill.getName());
            comp.setBusinessCategory(skill.getCategory());
            comp.setSkillForm(skill.getSkillForm());
            comp.setSkillCategory(skill.getCategory());
            comp.setCapabilityCategory(skill.getCategory());
            comp.setVisibility("_system".equals(skill.getDirectory()) ? "internal" : "public");
            comp.setDirectory(skill.getDirectory());
            result.add(comp);
        }
        
        return result;
    }
    
    private TestSuggestionDTO buildTestSuggestion(List<SkillPathInfoDTO> skills) {
        TestSuggestionDTO suggestion = new TestSuggestionDTO();
        
        List<String> highPriority = new ArrayList<>();
        List<String> mediumPriority = new ArrayList<>();
        List<String> lowPriority = new ArrayList<>();
        
        for (SkillPathInfoDTO skill : skills) {
            String skillId = skill.getSkillId();
            if (skillId == null) continue;
            
            if (skillId.contains("skill-auth") || skillId.contains("skill-capability") ||
                skillId.contains("skill-dict") || skillId.contains("skill-menu") ||
                skillId.contains("skill-role")) {
                highPriority.add(skillId + " - " + skill.getName());
            } else if (skillId.contains("skill-discovery") || skillId.contains("skill-install") ||
                       skillId.contains("skill-history") || skillId.contains("skill-template")) {
                mediumPriority.add(skillId + " - " + skill.getName());
            } else if (skillId.contains("skill-notification") || skillId.contains("skill-config")) {
                lowPriority.add(skillId + " - " + skill.getName());
            }
        }
        
        suggestion.setHighPriority(highPriority);
        suggestion.setMediumPriority(mediumPriority);
        suggestion.setLowPriority(lowPriority);
        suggestion.setEstimatedTestCases(skills.size() * 5);
        suggestion.setMessage("建议按优先级进行测试覆盖，预估测试用例数约为技能数的5倍");
        
        return suggestion;
    }
    
    private String getCategoryName(String code) {
        Map<String, String> names = new HashMap<>();
        names.put("sys", "系统管理");
        names.put("biz", "业务场景");
        names.put("llm", "大模型");
        names.put("msg", "消息通知");
        names.put("org", "组织管理");
        names.put("vfs", "文件存储");
        names.put("knowledge", "知识库");
        names.put("monitor", "监控");
        names.put("PROVIDER", "服务提供者");
        names.put("DRIVER", "驱动适配器");
        names.put("SCENE", "场景应用");
        return names.getOrDefault(code, code);
    }
    
    private String getCategoryIcon(String code) {
        Map<String, String> icons = new HashMap<>();
        icons.put("sys", "ri-settings-3-line");
        icons.put("biz", "ri-briefcase-line");
        icons.put("llm", "ri-robot-line");
        icons.put("msg", "ri-message-3-line");
        icons.put("org", "ri-team-line");
        icons.put("vfs", "ri-folder-line");
        icons.put("knowledge", "ri-book-2-line");
        icons.put("PROVIDER", "ri-cpu-line");
        icons.put("DRIVER", "ri-steering-line");
        icons.put("SCENE", "ri-layout-grid-line");
        return icons.getOrDefault(code, "ri-puzzle-line");
    }
    
    private String getCategoryColor(String code) {
        Map<String, String> colors = new HashMap<>();
        colors.put("sys", "#64748b");
        colors.put("biz", "#f59e0b");
        colors.put("llm", "#6366f1");
        colors.put("msg", "#ef4444");
        colors.put("org", "#3b82f6");
        colors.put("vfs", "#8b5cf6");
        colors.put("knowledge", "#10b981");
        colors.put("PROVIDER", "#1890ff");
        colors.put("DRIVER", "#52c41a");
        colors.put("SCENE", "#722ed1");
        return colors.getOrDefault(code, "#8c8c8c");
    }
    
    private String exportAsMarkdown(SkillReportDTO report) {
        StringBuilder sb = new StringBuilder();
        sb.append("# Skills 统计报告\n\n");
        sb.append("**生成时间**: ").append(new java.util.Date(report.getTimestamp())).append("\n\n");
        
        sb.append("## 📊 总体统计\n\n");
        sb.append("| 指标 | 数量 |\n");
        sb.append("|------|------|\n");
        sb.append("| 总技能数 | ").append(report.getTotal()).append(" |\n\n");
        
        sb.append("## 📁 目录结构统计\n\n");
        sb.append("| 目录 | 说明 | 数量 | 占比 |\n");
        sb.append("|------|------|------|------|\n");
        for (DirectoryStatsDTO stat : report.getDirectoryStats()) {
            sb.append("| `").append(stat.getDirectory()).append("` | ")
              .append(stat.getDescription()).append(" | ")
              .append(stat.getCount()).append(" | ")
              .append(stat.getPercentage()).append("% |\n");
        }
        sb.append("\n");
        
        sb.append("## 🏷️ 业务分类统计\n\n");
        sb.append("| 分类 | 数量 | 占比 |\n");
        sb.append("|------|------|------|\n");
        for (CategoryDetailDTO cat : report.getByBusinessCategory()) {
            sb.append("| ").append(cat.getName()).append(" | ")
              .append(cat.getCount()).append(" | ")
              .append(cat.getPercentage()).append("% |\n");
        }
        sb.append("\n");
        
        sb.append("## 🔧 技能形态统计\n\n");
        sb.append("| 形态 | 数量 | 占比 |\n");
        sb.append("|------|------|------|\n");
        for (CategoryDetailDTO form : report.getBySkillForm()) {
            sb.append("| ").append(form.getName()).append(" | ")
              .append(form.getCount()).append(" | ")
              .append(form.getPercentage()).append("% |\n");
        }
        sb.append("\n");
        
        if (report.getTestSuggestion() != null) {
            sb.append("## 🧪 测试建议\n\n");
            TestSuggestionDTO test = report.getTestSuggestion();
            sb.append("**预估测试用例数**: ").append(test.getEstimatedTestCases()).append("\n\n");
            
            if (!test.getHighPriority().isEmpty()) {
                sb.append("### 高优先级\n");
                for (String item : test.getHighPriority()) {
                    sb.append("- ").append(item).append("\n");
                }
                sb.append("\n");
            }
        }
        
        return sb.toString();
    }
    
    private String exportAsJson(SkillReportDTO report) {
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            mapper.enable(com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT);
            return mapper.writeValueAsString(report);
        } catch (Exception e) {
            return "{}";
        }
    }
    
    private String exportAsCsv(SkillReportDTO report) {
        StringBuilder sb = new StringBuilder();
        sb.append("Skill ID,Name,Category,Skill Form,Directory,Absolute Path\n");
        
        if (report.getSkillPaths() != null) {
            for (SkillPathInfoDTO skill : report.getSkillPaths()) {
                sb.append("\"").append(skill.getSkillId()).append("\",")
                  .append("\"").append(skill.getName() != null ? skill.getName() : "").append("\",")
                  .append("\"").append(skill.getCategory() != null ? skill.getCategory() : "").append("\",")
                  .append("\"").append(skill.getSkillForm() != null ? skill.getSkillForm() : "").append("\",")
                  .append("\"").append(skill.getDirectory() != null ? skill.getDirectory() : "").append("\",")
                  .append("\"").append(skill.getAbsolutePath() != null ? skill.getAbsolutePath() : "").append("\"\n");
            }
        }
        
        return sb.toString();
    }
    
    @PostMapping("/dev")
    public ResultModel<LocalDiscoveryResultDTO> discoverDev() {
        log.info("[discoverDev] Starting dev directory discovery");
        
        LocalDiscoveryResultDTO result = new LocalDiscoveryResultDTO();
        List<net.ooder.skill.discovery.dto.discovery.CapabilityDTO> capabilities = new ArrayList<>();
        
        String devPath = System.getProperty("user.dir") + "/.ooder/dev";
        java.io.File devDir = new java.io.File(devPath);
        
        if (!devDir.exists() || !devDir.isDirectory()) {
            log.info("[discoverDev] Dev directory does not exist: {}", devPath);
            result.setCapabilities(capabilities);
            result.setTotal(0);
            result.setSource("dev-directory");
            result.setTimestamp(System.currentTimeMillis());
            result.setErrorMessage("Dev directory does not exist: " + devPath);
            return ResultModel.success(result);
        }
        
        log.info("[discoverDev] Scanning dev directory: {}", devPath);
        
        try {
            List<java.io.File> skillYamlFiles = scanForSkillYamlFiles(devDir);
            log.info("[discoverDev] Found {} skill.yaml files", skillYamlFiles.size());
            
            for (java.io.File skillYaml : skillYamlFiles) {
                try {
                    net.ooder.skill.discovery.dto.discovery.CapabilityDTO cap = parseSkillYaml(skillYaml);
                    if (cap != null) {
                        cap.setStatus("DEV");
                        cap.setSkillForm("DEV");
                        cap.setInstallSource("dev");
                        capabilities.add(cap);
                    }
                } catch (Exception e) {
                    log.error("[discoverDev] Failed to parse {}: {}", skillYaml.getAbsolutePath(), e.getMessage());
                }
            }
            
            result.setCapabilities(capabilities);
            result.setTotal(capabilities.size());
            result.setSource("dev-directory");
            result.setTimestamp(System.currentTimeMillis());
            
            log.info("[discoverDev] Discovered {} dev capabilities", capabilities.size());
            return ResultModel.success(result);
            
        } catch (Exception e) {
            log.error("[discoverDev] Failed to scan dev directory: {}", e.getMessage(), e);
            result.setCapabilities(capabilities);
            result.setTotal(0);
            result.setSource("dev-directory");
            result.setTimestamp(System.currentTimeMillis());
            result.setErrorMessage("Failed to scan dev directory: " + e.getMessage());
            return ResultModel.error("Failed to scan dev directory: " + e.getMessage());
        }
    }
    
    private List<java.io.File> scanForSkillYamlFiles(java.io.File directory) {
        List<java.io.File> skillYamlFiles = new ArrayList<>();
        
        java.io.File[] files = directory.listFiles();
        if (files == null) {
            return skillYamlFiles;
        }
        
        for (java.io.File file : files) {
            if (file.isDirectory()) {
                java.io.File skillYaml = new java.io.File(file, "skill.yaml");
                if (skillYaml.exists()) {
                    skillYamlFiles.add(skillYaml);
                }
                skillYamlFiles.addAll(scanForSkillYamlFiles(file));
            }
        }
        
        return skillYamlFiles;
    }
    
    private net.ooder.skill.discovery.dto.discovery.CapabilityDTO parseSkillYaml(java.io.File skillYamlFile) {
        try {
            org.yaml.snakeyaml.Yaml yaml = new org.yaml.snakeyaml.Yaml();
            java.io.InputStream inputStream = new java.io.FileInputStream(skillYamlFile);
            Map<String, Object> data = yaml.load(inputStream);
            inputStream.close();
            
            net.ooder.skill.discovery.dto.discovery.CapabilityDTO cap = new net.ooder.skill.discovery.dto.discovery.CapabilityDTO();
            
            String skillId = null;
            String name = null;
            String version = null;
            String description = null;
            String category = null;
            String icon = null;
            String skillForm = null;
            
            Map<String, Object> metadata = (Map<String, Object>) data.get("metadata");
            if (metadata != null) {
                skillId = (String) metadata.get("id");
                name = (String) metadata.get("name");
                version = (String) metadata.get("version");
                description = (String) metadata.get("description");
                category = (String) metadata.get("category");
                icon = (String) metadata.get("icon");
            }
            
            if (skillId == null) {
                skillId = (String) data.get("id");
            }
            if (skillId == null) {
                skillId = (String) data.get("skillId");
            }
            if (name == null) {
                name = (String) data.get("name");
            }
            if (version == null) {
                version = (String) data.get("version");
            }
            if (description == null) {
                description = (String) data.get("description");
            }
            if (category == null) {
                category = (String) data.get("category");
            }
            if (icon == null) {
                icon = (String) data.get("icon");
            }
            
            Map<String, Object> spec = (Map<String, Object>) data.get("spec");
            if (spec != null) {
                skillForm = (String) spec.get("skillForm");
                if (category == null) {
                    java.util.List<Map<String, Object>> capabilities = 
                        (java.util.List<Map<String, Object>>) spec.get("capabilities");
                    if (capabilities != null && !capabilities.isEmpty()) {
                        category = (String) capabilities.get(0).get("category");
                    }
                }
            }
            if (skillForm == null) {
                skillForm = (String) data.get("skillForm");
            }
            
            cap.setSkillId(skillId);
            cap.setCapabilityId(skillId);
            cap.setName(name);
            cap.setDescription(description);
            cap.setVersion(version);
            cap.setCategory(category);
            cap.setSkillForm(skillForm);
            cap.setIcon(icon);
            
            String basePath = skillYamlFile.getParentFile().getAbsolutePath();
            cap.setInstallPath(basePath);
            
            return cap;
        } catch (Exception e) {
            log.error("[parseSkillYaml] Failed to parse {}: {}", skillYamlFile.getAbsolutePath(), e.getMessage());
            return null;
        }
    }
}
