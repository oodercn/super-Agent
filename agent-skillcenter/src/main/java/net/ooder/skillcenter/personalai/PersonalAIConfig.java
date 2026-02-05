package net.ooder.skillcenter.personalai;

/**
 * 个人AI中心配置类
 */
public class PersonalAIConfig {
    private boolean localFirstExecution;
    private boolean autoSyncEnabled;
    private PrivacyManager.PrivacyLevel privacyLevel;
    private int maxDevices;
    private int maxSkills;
    
    /**
     * 是否本地优先执行
     * @return 是否本地优先执行
     */
    public boolean isLocalFirstExecution() {
        return localFirstExecution;
    }
    
    /**
     * 设置是否本地优先执行
     * @param localFirstExecution 是否本地优先执行
     */
    public void setLocalFirstExecution(boolean localFirstExecution) {
        this.localFirstExecution = localFirstExecution;
    }
    
    /**
     * 是否自动同步
     * @return 是否自动同步
     */
    public boolean isAutoSyncEnabled() {
        return autoSyncEnabled;
    }
    
    /**
     * 设置是否自动同步
     * @param autoSyncEnabled 是否自动同步
     */
    public void setAutoSyncEnabled(boolean autoSyncEnabled) {
        this.autoSyncEnabled = autoSyncEnabled;
    }
    
    /**
     * 获取隐私级别
     * @return 隐私级别
     */
    public PrivacyManager.PrivacyLevel getPrivacyLevel() {
        return privacyLevel;
    }
    
    /**
     * 设置隐私级别
     * @param privacyLevel 隐私级别
     */
    public void setPrivacyLevel(PrivacyManager.PrivacyLevel privacyLevel) {
        this.privacyLevel = privacyLevel;
    }
    
    /**
     * 获取最大设备数量
     * @return 最大设备数量
     */
    public int getMaxDevices() {
        return maxDevices;
    }
    
    /**
     * 设置最大设备数量
     * @param maxDevices 最大设备数量
     */
    public void setMaxDevices(int maxDevices) {
        this.maxDevices = maxDevices;
    }
    
    /**
     * 获取最大技能数量
     * @return 最大技能数量
     */
    public int getMaxSkills() {
        return maxSkills;
    }
    
    /**
     * 设置最大技能数量
     * @param maxSkills 最大技能数量
     */
    public void setMaxSkills(int maxSkills) {
        this.maxSkills = maxSkills;
    }
}