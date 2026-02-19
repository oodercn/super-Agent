package net.ooder.sdk.nexus.resource;

import net.ooder.sdk.nexus.resource.model.SkillPackage;
import net.ooder.sdk.nexus.resource.model.SkillInfo;
import net.ooder.sdk.nexus.resource.model.SkillResult;
import net.ooder.sdk.nexus.resource.model.StorageConfig;
import net.ooder.sdk.nexus.resource.model.StorageStatus;
import net.ooder.sdk.nexus.resource.model.ShareLink;
import net.ooder.sdk.nexus.resource.model.ShareConfig;
import net.ooder.sdk.nexus.resource.model.ShareInfo;
import net.ooder.sdk.nexus.resource.model.ResourceListener;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface PrivateResourceService {
    
    CompletableFuture<Void> installSkill(SkillPackage skillPackage);
    
    CompletableFuture<Void> uninstallSkill(String skillId);
    
    CompletableFuture<List<SkillInfo>> listInstalledSkills();
    
    CompletableFuture<SkillResult> executeSkill(String skillId, Map<String, Object> params);
    
    CompletableFuture<Void> configureStorage(StorageConfig config);
    
    CompletableFuture<StorageStatus> getStorageStatus();
    
    CompletableFuture<Void> storeData(String key, byte[] data);
    
    CompletableFuture<byte[]> retrieveData(String key);
    
    CompletableFuture<ShareLink> createShareLink(String resourceId, ShareConfig config);
    
    CompletableFuture<Void> revokeShareLink(String shareId);
    
    CompletableFuture<List<ShareInfo>> listShares();
    
    void addResourceListener(ResourceListener listener);
    
    void removeResourceListener(ResourceListener listener);
}
