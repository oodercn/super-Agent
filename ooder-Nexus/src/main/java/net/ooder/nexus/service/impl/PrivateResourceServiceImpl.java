package net.ooder.nexus.service.impl;

import net.ooder.nexus.service.*;
import net.ooder.sdk.api.scene.CapabilityInvoker;
import net.ooder.sdk.api.skill.InstallRequest;
import net.ooder.sdk.api.skill.SkillPackageManager;
import net.ooder.sdk.api.storage.StorageService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
public class PrivateResourceServiceImpl implements PrivateResourceService {

    private static final Logger log = LoggerFactory.getLogger(PrivateResourceServiceImpl.class);

    private final SkillPackageManager skillPackageManager;
    private final CapabilityInvoker capabilityInvoker;
    private final StorageService storageService;

    @Autowired
    public PrivateResourceServiceImpl(
            @Autowired(required = false) SkillPackageManager skillPackageManager,
            @Autowired(required = false) CapabilityInvoker capabilityInvoker,
            @Autowired(required = false) StorageService storageService) {
        this.skillPackageManager = skillPackageManager;
        this.capabilityInvoker = capabilityInvoker;
        this.storageService = storageService;
        log.info("PrivateResourceServiceImpl initialized - SkillPackageManager: {}, CapabilityInvoker: {}, StorageService: {}",
            skillPackageManager != null ? "available" : "not available",
            capabilityInvoker != null ? "available" : "not available",
            storageService != null ? "available" : "not available");
    }

    @Override
    public CompletableFuture<Void> installSkill(SkillPackage skillPackage) {
        log.info("Installing skill: {}", skillPackage.getName());
        
        return CompletableFuture.runAsync(() -> {
            if (skillPackageManager != null) {
                try {
                    InstallRequest request = new InstallRequest();
                    request.setSkillId(skillPackage.getPackageId());
                    skillPackageManager.install(request).join();
                    log.info("Skill installed successfully: {}", skillPackage.getName());
                } catch (Exception e) {
                    log.error("Failed to install skill: {}", skillPackage.getName(), e);
                    throw new RuntimeException("Failed to install skill: " + e.getMessage());
                }
            } else {
                log.warn("SkillPackageManager not available");
            }
        });
    }

    @Override
    public CompletableFuture<Void> uninstallSkill(String skillId) {
        log.info("Uninstalling skill: {}", skillId);
        
        if (skillPackageManager != null) {
            return skillPackageManager.uninstall(skillId).thenApply(r -> null);
        }
        
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<List<SkillInfo>> listInstalledSkills() {
        log.info("Listing installed skills");
        
        return CompletableFuture.supplyAsync(() -> {
            List<SkillInfo> skills = new ArrayList<SkillInfo>();
            
            if (skillPackageManager != null) {
                try {
                    skillPackageManager.listInstalled().join().forEach(skill -> {
                        SkillInfo info = new SkillInfo();
                        info.setSkillId(skill.getSkillId());
                        info.setName(skill.getName());
                        info.setVersion(skill.getVersion());
                        skills.add(info);
                    });
                } catch (Exception e) {
                    log.error("Failed to list installed skills from SDK", e);
                }
            }
            
            return skills;
        });
    }

    @Override
    public CompletableFuture<SkillResult> executeSkill(String skillId, Map<String, Object> params) {
        log.info("Executing skill: {}", skillId);
        
        return CompletableFuture.supplyAsync(() -> {
            SkillResult result = new SkillResult();
            result.setSuccess(false);
            
            if (capabilityInvoker != null) {
                try {
                    long startTime = System.currentTimeMillis();
                    Object output = capabilityInvoker.invoke(skillId, params).join();
                    result.setSuccess(true);
                    result.setData(output);
                    result.setExecutionTime(System.currentTimeMillis() - startTime);
                    log.info("Skill executed successfully: {}", skillId);
                } catch (Exception e) {
                    log.error("Failed to execute skill: {}", skillId, e);
                    result.setMessage("Failed to execute skill: " + e.getMessage());
                }
            } else {
                result.setSuccess(true);
                result.setData("Skill execution simulated");
                result.setExecutionTime(100);
                log.info("Skill executed (simulated): {}", skillId);
            }
            
            return result;
        });
    }

    @Override
    public CompletableFuture<Void> configureStorage(StorageConfig config) {
        log.info("Configuring storage");
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<StorageStatus> getStorageStatus() {
        log.info("Getting storage status");
        
        return CompletableFuture.supplyAsync(() -> {
            StorageStatus status = new StorageStatus();
            status.setTotalSpace(1024L * 1024 * 1024 * 100);
            status.setUsedSpace(1024L * 1024 * 1024 * 10);
            return status;
        });
    }

    @Override
    public CompletableFuture<Void> storeData(String key, byte[] data) {
        log.info("Storing data with key: {}", key);
        
        return CompletableFuture.runAsync(() -> {
            if (storageService != null) {
                try {
                    storageService.save(key, data);
                    log.info("Data stored successfully: {}", key);
                } catch (Exception e) {
                    log.error("Failed to store data: {}", key, e);
                    throw new RuntimeException("Failed to store data: " + e.getMessage());
                }
            } else {
                log.warn("StorageService not available");
            }
        });
    }

    @Override
    public CompletableFuture<byte[]> retrieveData(String key) {
        log.info("Retrieving data with key: {}", key);
        
        return CompletableFuture.supplyAsync(() -> {
            if (storageService != null) {
                try {
                    Optional<byte[]> data = storageService.load(key, byte[].class);
                    return data.orElse(null);
                } catch (Exception e) {
                    log.error("Failed to retrieve data: {}", key, e);
                    return null;
                }
            }
            return null;
        });
    }

    @Override
    public CompletableFuture<ShareLink> createShareLink(String resourceId, ShareConfig config) {
        log.info("Creating share link for resource: {}", resourceId);
        
        return CompletableFuture.supplyAsync(() -> {
            ShareLink link = new ShareLink();
            link.setShareId(UUID.randomUUID().toString());
            link.setResourceId(resourceId);
            return link;
        });
    }

    @Override
    public CompletableFuture<Void> revokeShareLink(String shareId) {
        log.info("Revoking share link: {}", shareId);
        return CompletableFuture.completedFuture(null);
    }
}
