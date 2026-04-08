package net.ooder.mvp.skill.scene.capability.model;

import java.io.Serializable;

public class CapabilityState implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String capabilityId;
    private boolean installed;
    private CapabilityStatus status;
    private long installTime;
    private long updateTime;
    private String installedBy;
    private String installSource;
    private String sceneGroupId;
    
    public CapabilityState() {
        this.installed = false;
        this.status = CapabilityStatus.DRAFT;
        this.updateTime = System.currentTimeMillis();
    }
    
    public CapabilityState(String capabilityId) {
        this();
        this.capabilityId = capabilityId;
    }
    
    public String getCapabilityId() {
        return capabilityId;
    }
    
    public void setCapabilityId(String capabilityId) {
        this.capabilityId = capabilityId;
    }
    
    public boolean isInstalled() {
        return installed;
    }
    
    public void setInstalled(boolean installed) {
        this.installed = installed;
        this.updateTime = System.currentTimeMillis();
    }
    
    public CapabilityStatus getStatus() {
        return status;
    }
    
    public void setStatus(CapabilityStatus status) {
        this.status = status;
        this.updateTime = System.currentTimeMillis();
    }
    
    public long getInstallTime() {
        return installTime;
    }
    
    public void setInstallTime(long installTime) {
        this.installTime = installTime;
    }
    
    public long getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }
    
    public String getInstalledBy() {
        return installedBy;
    }
    
    public void setInstalledBy(String installedBy) {
        this.installedBy = installedBy;
    }
    
    public String getInstallSource() {
        return installSource;
    }
    
    public void setInstallSource(String installSource) {
        this.installSource = installSource;
    }
    
    public String getSceneGroupId() {
        return sceneGroupId;
    }
    
    public void setSceneGroupId(String sceneGroupId) {
        this.sceneGroupId = sceneGroupId;
    }
    
    public void markInstalled(String userId, String source) {
        this.installed = true;
        this.installTime = System.currentTimeMillis();
        this.installedBy = userId;
        this.installSource = source;
        this.updateTime = this.installTime;
    }
    
    public void markUninstalled() {
        this.installed = false;
        this.status = CapabilityStatus.DRAFT;
        this.updateTime = System.currentTimeMillis();
    }
}
