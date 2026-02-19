package net.ooder.sdk.nexus.resource.model;

public interface ResourceListener {
    
    void onSkillInstalled(SkillInfo skill);
    
    void onSkillUninstalled(String skillId);
    
    void onSkillExecuted(String skillId, SkillResult result);
    
    void onStorageConfigured(StorageStatus status);
    
    void onDataStored(String key);
    
    void onDataRetrieved(String key);
    
    void onShareCreated(ShareLink shareLink);
    
    void onShareRevoked(String shareId);
}
