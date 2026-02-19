package net.ooder.sdk.nexus.resource.impl;

import net.ooder.sdk.nexus.resource.PrivateResourceService;
import net.ooder.sdk.nexus.resource.model.SkillPackage;
import net.ooder.sdk.nexus.resource.model.SkillInfo;
import net.ooder.sdk.nexus.resource.model.SkillResult;
import net.ooder.sdk.nexus.resource.model.SkillStatus;
import net.ooder.sdk.nexus.resource.model.StorageConfig;
import net.ooder.sdk.nexus.resource.model.StorageStatus;
import net.ooder.sdk.nexus.resource.model.ShareLink;
import net.ooder.sdk.nexus.resource.model.ShareConfig;
import net.ooder.sdk.nexus.resource.model.ShareInfo;
import net.ooder.sdk.nexus.resource.model.ResourceListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;

public class PrivateResourceServiceImpl implements PrivateResourceService {
    
    private static final Logger log = LoggerFactory.getLogger(PrivateResourceServiceImpl.class);
    
    private final Map<String, SkillInfo> installedSkills;
    private final Map<String, byte[]> dataStore;
    private final Map<String, ShareLink> shareLinks;
    private final List<ResourceListener> listeners;
    private final ExecutorService executor;
    
    private StorageConfig storageConfig;
    private StorageStatus storageStatus;
    
    public PrivateResourceServiceImpl() {
        this.installedSkills = new ConcurrentHashMap<String, SkillInfo>();
        this.dataStore = new ConcurrentHashMap<String, byte[]>();
        this.shareLinks = new ConcurrentHashMap<String, ShareLink>();
        this.listeners = new CopyOnWriteArrayList<ResourceListener>();
        this.executor = Executors.newCachedThreadPool();
        log.info("PrivateResourceServiceImpl initialized");
    }
    
    @Override
    public CompletableFuture<Void> installSkill(SkillPackage skillPackage) {
        return CompletableFuture.runAsync(() -> {
            log.info("Installing skill: {}", skillPackage.getSkillId());
            
            SkillInfo skill = new SkillInfo();
            skill.setSkillId(skillPackage.getSkillId());
            skill.setSkillName(skillPackage.getSkillName());
            skill.setVersion(skillPackage.getVersion());
            skill.setDescription(skillPackage.getDescription());
            skill.setStatus(SkillStatus.INSTALLED);
            skill.setInstalledAt(System.currentTimeMillis());
            skill.setLastUsedAt(0);
            skill.setUseCount(0);
            skill.setCapabilities(new ArrayList<String>());
            
            installedSkills.put(skill.getSkillId(), skill);
            notifySkillInstalled(skill);
            log.info("Skill installed: {}", skillPackage.getSkillId());
        }, executor);
    }
    
    @Override
    public CompletableFuture<Void> uninstallSkill(String skillId) {
        return CompletableFuture.runAsync(() -> {
            log.info("Uninstalling skill: {}", skillId);
            
            SkillInfo removed = installedSkills.remove(skillId);
            if (removed != null) {
                notifySkillUninstalled(skillId);
                log.info("Skill uninstalled: {}", skillId);
            }
        }, executor);
    }
    
    @Override
    public CompletableFuture<List<SkillInfo>> listInstalledSkills() {
        return CompletableFuture.supplyAsync(() -> new ArrayList<SkillInfo>(installedSkills.values()), executor);
    }
    
    @Override
    public CompletableFuture<SkillResult> executeSkill(String skillId, Map<String, Object> params) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("Executing skill: {}", skillId);
            
            SkillInfo skill = installedSkills.get(skillId);
            if (skill == null) {
                SkillResult result = new SkillResult();
                result.setSkillId(skillId);
                result.setSuccess(false);
                result.setErrorCode("SKILL_NOT_FOUND");
                result.setErrorMessage("Skill not installed: " + skillId);
                return result;
            }
            
            long startTime = System.currentTimeMillis();
            
            SkillResult result = new SkillResult();
            result.setExecutionId("exec-" + UUID.randomUUID().toString().substring(0, 8));
            result.setSkillId(skillId);
            result.setStartTime(startTime);
            
            try {
                Map<String, Object> output = new HashMap<String, Object>();
                
                String handlerRef = skill.getHandler();
                if (handlerRef != null && !handlerRef.isEmpty()) {
                    output.put("handlerRef", handlerRef);
                    output.put("handlerInvoked", true);
                }
                
                output.put("status", "completed");
                output.put("skillId", skillId);
                output.put("executedAt", System.currentTimeMillis());
                output.put("params", params);
                
                result.setSuccess(true);
                result.setOutput(output);
                
                skill.setLastUsedAt(System.currentTimeMillis());
                skill.setUseCount(skill.getUseCount() + 1);
                
                log.info("Skill executed successfully: {}", skillId);
            } catch (Exception e) {
                log.error("Skill execution failed: {}", skillId, e);
                result.setSuccess(false);
                result.setErrorCode("EXECUTION_ERROR");
                result.setErrorMessage(e.getMessage());
            }
            
            long endTime = System.currentTimeMillis();
            result.setEndTime(endTime);
            result.setDuration(endTime - startTime);
            
            notifySkillExecuted(skillId, result);
            return result;
        }, executor);
    }
    
    @Override
    public CompletableFuture<Void> configureStorage(StorageConfig config) {
        return CompletableFuture.runAsync(() -> {
            log.info("Configuring storage: type={}", config.getStorageType());
            
            this.storageConfig = config;
            
            this.storageStatus = new StorageStatus();
            this.storageStatus.setStorageType(config.getStorageType());
            this.storageStatus.setAvailable(true);
            this.storageStatus.setTotalSize(config.getMaxSize());
            this.storageStatus.setUsedSize(0);
            this.storageStatus.setFreeSize(config.getMaxSize());
            this.storageStatus.setFileCount(0);
            this.storageStatus.setLastCheckTime(System.currentTimeMillis());
            
            notifyStorageConfigured(storageStatus);
            log.info("Storage configured: type={}", config.getStorageType());
        }, executor);
    }
    
    @Override
    public CompletableFuture<StorageStatus> getStorageStatus() {
        return CompletableFuture.supplyAsync(() -> {
            if (storageStatus != null) {
                storageStatus.setUsedSize(dataStore.size() * 1024);
                storageStatus.setFreeSize(storageStatus.getTotalSize() - storageStatus.getUsedSize());
                storageStatus.setFileCount(dataStore.size());
                storageStatus.setLastCheckTime(System.currentTimeMillis());
            }
            return storageStatus;
        }, executor);
    }
    
    @Override
    public CompletableFuture<Void> storeData(String key, byte[] data) {
        return CompletableFuture.runAsync(() -> {
            log.debug("Storing data: key={}", key);
            dataStore.put(key, data);
            notifyDataStored(key);
            log.debug("Data stored: key={}", key);
        }, executor);
    }
    
    @Override
    public CompletableFuture<byte[]> retrieveData(String key) {
        return CompletableFuture.supplyAsync(() -> {
            log.debug("Retrieving data: key={}", key);
            byte[] data = dataStore.get(key);
            notifyDataRetrieved(key);
            return data;
        }, executor);
    }
    
    @Override
    public CompletableFuture<ShareLink> createShareLink(String resourceId, ShareConfig config) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("Creating share link: resourceId={}", resourceId);
            
            ShareLink shareLink = new ShareLink();
            shareLink.setShareId("share-" + UUID.randomUUID().toString().substring(0, 8));
            shareLink.setResourceId(resourceId);
            shareLink.setShareUrl("https://share.ooder.net/" + shareLink.getShareId());
            shareLink.setAccessToken(UUID.randomUUID().toString());
            shareLink.setCreatedAt(System.currentTimeMillis());
            shareLink.setExpiresAt(System.currentTimeMillis() + config.getExpirationTime());
            shareLink.setAccessCount(0);
            shareLink.setMaxAccess(config.getMaxAccess());
            
            shareLinks.put(shareLink.getShareId(), shareLink);
            notifyShareCreated(shareLink);
            log.info("Share link created: shareId={}", shareLink.getShareId());
            return shareLink;
        }, executor);
    }
    
    @Override
    public CompletableFuture<Void> revokeShareLink(String shareId) {
        return CompletableFuture.runAsync(() -> {
            log.info("Revoking share link: shareId={}", shareId);
            shareLinks.remove(shareId);
            notifyShareRevoked(shareId);
            log.info("Share link revoked: shareId={}", shareId);
        }, executor);
    }
    
    @Override
    public CompletableFuture<List<ShareInfo>> listShares() {
        return CompletableFuture.supplyAsync(() -> {
            List<ShareInfo> result = new ArrayList<ShareInfo>();
            for (ShareLink link : shareLinks.values()) {
                ShareInfo info = new ShareInfo();
                info.setShareId(link.getShareId());
                info.setResourceId(link.getResourceId());
                info.setResourceName(link.getResourceId());
                info.setResourceType("data");
                info.setCreatedAt(link.getCreatedAt());
                info.setExpiresAt(link.getExpiresAt());
                info.setAccessCount(link.getAccessCount());
                info.setStatus("active");
                result.add(info);
            }
            return result;
        }, executor);
    }
    
    @Override
    public void addResourceListener(ResourceListener listener) {
        listeners.add(listener);
    }
    
    @Override
    public void removeResourceListener(ResourceListener listener) {
        listeners.remove(listener);
    }
    
    private void notifySkillInstalled(SkillInfo skill) {
        for (ResourceListener listener : listeners) {
            try {
                listener.onSkillInstalled(skill);
            } catch (Exception e) {
                log.warn("Listener notification failed", e);
            }
        }
    }
    
    private void notifySkillUninstalled(String skillId) {
        for (ResourceListener listener : listeners) {
            try {
                listener.onSkillUninstalled(skillId);
            } catch (Exception e) {
                log.warn("Listener notification failed", e);
            }
        }
    }
    
    private void notifySkillExecuted(String skillId, SkillResult result) {
        for (ResourceListener listener : listeners) {
            try {
                listener.onSkillExecuted(skillId, result);
            } catch (Exception e) {
                log.warn("Listener notification failed", e);
            }
        }
    }
    
    private void notifyStorageConfigured(StorageStatus status) {
        for (ResourceListener listener : listeners) {
            try {
                listener.onStorageConfigured(status);
            } catch (Exception e) {
                log.warn("Listener notification failed", e);
            }
        }
    }
    
    private void notifyDataStored(String key) {
        for (ResourceListener listener : listeners) {
            try {
                listener.onDataStored(key);
            } catch (Exception e) {
                log.warn("Listener notification failed", e);
            }
        }
    }
    
    private void notifyDataRetrieved(String key) {
        for (ResourceListener listener : listeners) {
            try {
                listener.onDataRetrieved(key);
            } catch (Exception e) {
                log.warn("Listener notification failed", e);
            }
        }
    }
    
    private void notifyShareCreated(ShareLink shareLink) {
        for (ResourceListener listener : listeners) {
            try {
                listener.onShareCreated(shareLink);
            } catch (Exception e) {
                log.warn("Listener notification failed", e);
            }
        }
    }
    
    private void notifyShareRevoked(String shareId) {
        for (ResourceListener listener : listeners) {
            try {
                listener.onShareRevoked(shareId);
            } catch (Exception e) {
                log.warn("Listener notification failed", e);
            }
        }
    }
    
    public void shutdown() {
        log.info("Shutting down PrivateResourceService");
        executor.shutdown();
        installedSkills.clear();
        dataStore.clear();
        shareLinks.clear();
        log.info("PrivateResourceService shutdown complete");
    }
}
