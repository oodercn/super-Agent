
package net.ooder.sdk.api.skill;

import net.ooder.sdk.api.skill.InstallResult;

public interface SkillPackageObserver {
    
    void onInstalling(String skillId);
    
    void onInstalled(String skillId, InstallResult result);
    
    void onUninstalling(String skillId);
    
    void onUninstalled(String skillId);
    
    void onUpdateStarted(String skillId, String targetVersion);
    
    void onUpdateCompleted(String skillId, String newVersion);
    
    void onError(String skillId, String error);
}
