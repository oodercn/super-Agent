package net.ooder.skillcenter.personalai;

/**
 * 个人AI中心状态类
 */
public class PersonalAIStatus {
    private String userId;
    private int deviceCount;
    private int skillCount;
    private int onlineDevices;
    private PrivacyManager.PrivacyLevel privacyLevel;
    private boolean localFirstExecution;
    private boolean autoSyncEnabled;
    private String p2pNetworkStatus;
    private String did;
    
    /**
     * 获取用户ID
     * @return 用户ID
     */
    public String getUserId() {
        return userId;
    }
    
    /**
     * 设置用户ID
     * @param userId 用户ID
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    /**
     * 获取设备数量
     * @return 设备数量
     */
    public int getDeviceCount() {
        return deviceCount;
    }
    
    /**
     * 设置设备数量
     * @param deviceCount 设备数量
     */
    public void setDeviceCount(int deviceCount) {
        this.deviceCount = deviceCount;
    }
    
    /**
     * 获取技能数量
     * @return 技能数量
     */
    public int getSkillCount() {
        return skillCount;
    }
    
    /**
     * 设置技能数量
     * @param skillCount 技能数量
     */
    public void setSkillCount(int skillCount) {
        this.skillCount = skillCount;
    }
    
    /**
     * 获取在线设备数量
     * @return 在线设备数量
     */
    public int getOnlineDevices() {
        return onlineDevices;
    }
    
    /**
     * 设置在线设备数量
     * @param onlineDevices 在线设备数量
     */
    public void setOnlineDevices(int onlineDevices) {
        this.onlineDevices = onlineDevices;
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
     * 获取P2P网络状态
     * @return P2P网络状态
     */
    public String getP2pNetworkStatus() {
        return p2pNetworkStatus;
    }
    
    /**
     * 设置P2P网络状态
     * @param p2pNetworkStatus P2P网络状态
     */
    public void setP2PNetworkStatus(String p2pNetworkStatus) {
        this.p2pNetworkStatus = p2pNetworkStatus;
    }
    
    /**
     * 获取DID
     * @return DID
     */
    public String getDid() {
        return did;
    }
    
    /**
     * 设置DID
     * @param did DID
     */
    public void setDid(String did) {
        this.did = did;
    }
}