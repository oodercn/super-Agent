package net.ooder.skillcenter.personalai;

import net.ooder.skillcenter.identity.DecentralizedIdentityManager;
import net.ooder.skillcenter.ide.AIIDEIntegrationManager;
import net.ooder.skillcenter.manager.SkillManager;
import net.ooder.skillcenter.model.Skill;
import net.ooder.skillcenter.model.SkillContext;
import net.ooder.skillcenter.model.SkillException;
import net.ooder.skillcenter.model.SkillResult;
import net.ooder.skillcenter.p2p.P2PNodeManager;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 个人AI中心，管理个人的AI能力、设备和数据
 */
public class PersonalAICenter {
    // 单例实例
    private static PersonalAICenter instance;
    
    // 用户身份管理器
    private DecentralizedIdentityManager identityManager;
    
    // 个人技能管理器
    private PersonalSkillManager skillManager;
    
    // 设备管理器
    private DeviceManager deviceManager;
    
    // 数据同步管理器
    private DataSyncManager syncManager;
    
    // 隐私控制管理器
    private PrivacyManager privacyManager;
    
    // P2P节点管理器
    private P2PNodeManager p2pManager;
    
    // AI-IDE集成管理器
    private AIIDEIntegrationManager ideIntegrationManager;
    
    // 个人AI中心配置
    private PersonalAIConfig config;
    
    /**
     * 私有构造方法，初始化个人AI中心
     */
    private PersonalAICenter() {
        this.identityManager = DecentralizedIdentityManager.getInstance();
        this.skillManager = new PersonalSkillManager();
        this.deviceManager = new DeviceManager();
        this.syncManager = new DataSyncManager();
        this.privacyManager = new PrivacyManager();
        this.p2pManager = P2PNodeManager.getInstance();
        this.ideIntegrationManager = AIIDEIntegrationManager.getInstance();
        this.config = new PersonalAIConfig();
        
        // 初始化配置
        loadConfig();
    }
    
    /**
     * 获取个人AI中心实例
     * @return 个人AI中心实例
     */
    public static synchronized PersonalAICenter getInstance() {
        if (instance == null) {
            instance = new PersonalAICenter();
        }
        return instance;
    }
    
    /**
     * 加载配置
     */
    private void loadConfig() {
        // 加载默认配置
        config.setLocalFirstExecution(true);
        config.setAutoSyncEnabled(true);
        config.setPrivacyLevel(PrivacyManager.PrivacyLevel.MEDIUM);
        config.setMaxDevices(10);
        config.setMaxSkills(50);
        
        System.out.println("Personal AI Center initialized with default config");
    }
    
    /**
     * 启动个人AI中心
     */
    public void start() {
        System.out.println("Starting Personal AI Center...");
        
        // 启动各个管理器
        identityManager.start();
        deviceManager.start();
        syncManager.start();
        privacyManager.start();
        ideIntegrationManager.start();
        
        // 启动P2P服务
        try {
            p2pManager.start();
            System.out.println("P2P service started successfully");
        } catch (Exception e) {
            System.err.println("Failed to start P2P service: " + e.getMessage());
        }
        
        // 加载个人技能
        loadPersonalSkills();
        
        System.out.println("Personal AI Center started successfully");
    }
    
    /**
     * 停止个人AI中心
     */
    public void stop() {
        System.out.println("Stopping Personal AI Center...");
        
        // 停止各个管理器
        syncManager.stop();
        deviceManager.stop();
        identityManager.stop();
        privacyManager.stop();
        ideIntegrationManager.stop();
        
        // 停止P2P服务
        try {
            p2pManager.stop();
            System.out.println("P2P service stopped successfully");
        } catch (Exception e) {
            System.err.println("Failed to stop P2P service: " + e.getMessage());
        }
        
        System.out.println("Personal AI Center stopped successfully");
    }
    
    /**
     * 加载个人技能
     */
    private void loadPersonalSkills() {
        // 加载本地个人技能
        List<PersonalSkillManager.PersonalSkill> personalSkills = skillManager.loadPersonalSkills();
        System.out.println("Loaded " + personalSkills.size() + " personal skills");
        
        // 注册到全局技能管理器
        SkillManager globalSkillManager = SkillManager.getInstance();
        for (PersonalSkillManager.PersonalSkill skill : personalSkills) {
            globalSkillManager.registerSkill(skill);
            System.out.println("Registered personal skill: " + skill.getName());
        }
    }
    
    /**
     * 执行个人技能
     * @param skillId 技能ID
     * @param context 执行上下文
     * @return 技能执行结果
     * @throws SkillException 技能执行异常
     */
    public SkillResult executePersonalSkill(String skillId, SkillContext context) throws SkillException {
        // 检查技能是否为个人技能
        PersonalSkillManager.PersonalSkill personalSkill = skillManager.getPersonalSkill(skillId);
        if (personalSkill != null) {
            // 执行个人技能
            return personalSkill.execute(context);
        }
        
        // 否则使用全局技能管理器执行
        SkillManager globalSkillManager = SkillManager.getInstance();
        return globalSkillManager.executeSkill(skillId, context);
    }
    
    /**
     * 注册个人技能
     * @param skill 个人技能
     */
    public void registerPersonalSkill(PersonalSkillManager.PersonalSkill skill) {
        skillManager.registerPersonalSkill(skill);
        
        // 注册到全局技能管理器
        SkillManager globalSkillManager = SkillManager.getInstance();
        globalSkillManager.registerSkill(skill);
        
        // 可选：共享到P2P网络
        if (skill.isShareable()) {
            p2pManager.shareSkill(skill.getId());
        }
        
        System.out.println("Registered personal skill: " + skill.getName());
    }
    
    /**
     * 添加设备到个人AI中心
     * @param device 设备信息
     */
    public void addDevice(DeviceManager.DeviceInfo device) {
        deviceManager.addDevice(device);
        System.out.println("Added device: " + device.getName());
    }
    
    /**
     * 移除设备
     * @param deviceId 设备ID
     */
    public void removeDevice(String deviceId) {
        deviceManager.removeDevice(deviceId);
        System.out.println("Removed device: " + deviceId);
    }
    
    /**
     * 获取所有设备
     * @return 设备列表
     */
    public List<DeviceManager.DeviceInfo> getAllDevices() {
        return deviceManager.getAllDevices();
    }
    
    /**
     * 同步数据到设备
     * @param deviceId 设备ID
     * @param dataType 数据类型
     * @param data 数据
     */
    public void syncData(String deviceId, String dataType, Map<String, Object> data) {
        syncManager.syncData(deviceId, dataType, data);
        System.out.println("Synced data to device: " + deviceId + ", dataType: " + dataType);
    }
    
    /**
     * 设置隐私级别
     * @param level 隐私级别
     */
    public void setPrivacyLevel(PrivacyManager.PrivacyLevel level) {
        privacyManager.setPrivacyLevel(level);
        config.setPrivacyLevel(level);
        System.out.println("Set privacy level to: " + level);
    }
    
    /**
     * 共享技能到指定设备
     * @param skillId 技能ID
     * @param deviceId 设备ID
     */
    public void shareSkillToDevice(String skillId, String deviceId) {
        DeviceManager.DeviceInfo device = deviceManager.getDevice(deviceId);
        if (device != null) {
            // 检查设备是否在线
            if (device.isOnline()) {
                // 共享技能到设备
                syncManager.shareSkill(skillId, deviceId);
                System.out.println("Shared skill " + skillId + " to device " + device.getName());
            } else {
                System.out.println("Device " + device.getName() + " is offline, cannot share skill");
            }
        } else {
            System.out.println("Device " + deviceId + " not found");
        }
    }
    
    /**
     * 生成代码
     * @param language 编程语言
     * @param prompt 代码生成提示
     * @param context 生成上下文
     * @return 生成的代码
     */
    public String generateCode(String language, String prompt, Map<String, Object> context) {
        return ideIntegrationManager.generateCode(language, prompt, context);
    }
    
    /**
     * 开始技能开发
     * @param skillId 技能ID
     * @return 开发上下文ID
     */
    public String startSkillDevelopment(String skillId) {
        return ideIntegrationManager.startSkillDevelopment(skillId);
    }
    
    /**
     * 调试技能
     * @param skillId 技能ID
     * @param context 执行上下文
     * @return 调试结果
     * @throws SkillException 调试异常
     */
    public SkillResult debugSkill(String skillId, SkillContext context) throws SkillException {
        return ideIntegrationManager.debugSkill(skillId, context);
    }
    
    /**
     * 测试技能
     * @param skillId 技能ID
     * @param testCases 测试用例
     * @return 测试结果
     */
    public Object testSkill(String skillId, Map<String, Object> testCases) {
        return ideIntegrationManager.testSkill(skillId, testCases);
    }
    
    /**
     * 部署技能
     * @param skillId 技能ID
     * @param environment 部署环境
     * @return 部署结果
     */
    public boolean deploySkill(String skillId, String environment) {
        return ideIntegrationManager.deploySkill(skillId, environment);
    }
    
    /**
     * 获取个人AI中心状态
     * @return 状态信息
     */
    public PersonalAIStatus getStatus() {
        PersonalAIStatus status = new PersonalAIStatus();
        status.setUserId(identityManager.getCurrentUserId());
        status.setDeviceCount(deviceManager.getDeviceCount());
        status.setSkillCount(skillManager.getSkillCount());
        status.setOnlineDevices(deviceManager.getOnlineDeviceCount());
        status.setPrivacyLevel(config.getPrivacyLevel());
        status.setLocalFirstExecution(config.isLocalFirstExecution());
        status.setAutoSyncEnabled(config.isAutoSyncEnabled());
        status.setP2PNetworkStatus(p2pManager.getLocalNode().getStatus().toString());
        status.setDid(identityManager.getDID());
        
        return status;
    }
    
    /**
     * 获取用户身份管理器
     * @return 用户身份管理器
     */
    public DecentralizedIdentityManager getIdentityManager() {
        return identityManager;
    }
    
    /**
     * 获取个人技能管理器
     * @return 个人技能管理器
     */
    public PersonalSkillManager getSkillManager() {
        return skillManager;
    }
    
    /**
     * 获取设备管理器
     * @return 设备管理器
     */
    public DeviceManager getDeviceManager() {
        return deviceManager;
    }
    
    /**
     * 获取数据同步管理器
     * @return 数据同步管理器
     */
    public DataSyncManager getDataSyncManager() {
        return syncManager;
    }
    
    /**
     * 获取隐私控制管理器
     * @return 隐私控制管理器
     */
    public PrivacyManager getPrivacyManager() {
        return privacyManager;
    }
    
    /**
     * 获取AI-IDE集成管理器
     * @return AI-IDE集成管理器
     */
    public AIIDEIntegrationManager getIdeIntegrationManager() {
        return ideIntegrationManager;
    }
    
    /**
     * 获取配置
     * @return 个人AI中心配置
     */
    public PersonalAIConfig getConfig() {
        return config;
    }
    
    /**
     * 设置配置
     * @param config 个人AI中心配置
     */
    public void setConfig(PersonalAIConfig config) {
        this.config = config;
    }
    

}