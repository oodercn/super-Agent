package net.ooder.skillcenter.personalai;

/**
 * 隐私控制管理器，管理个人AI中心的隐私设置
 */
public class PrivacyManager {
    // 当前隐私级别
    private PrivacyLevel privacyLevel;
    
    /**
     * 构造方法
     */
    public PrivacyManager() {
        this.privacyLevel = PrivacyLevel.MEDIUM;
    }
    
    /**
     * 启动隐私管理器
     */
    public void start() {
        System.out.println("Privacy Manager started with level: " + privacyLevel);
    }
    
    /**
     * 停止隐私管理器
     */
    public void stop() {
        System.out.println("Privacy Manager stopped");
    }
    
    /**
     * 获取当前隐私级别
     * @return 当前隐私级别
     */
    public PrivacyLevel getPrivacyLevel() {
        return privacyLevel;
    }
    
    /**
     * 设置隐私级别
     * @param level 隐私级别
     */
    public void setPrivacyLevel(PrivacyLevel level) {
        this.privacyLevel = level;
        System.out.println("Privacy level set to: " + level);
    }
    
    /**
     * 检查是否允许数据共享
     * @param dataType 数据类型
     * @return 是否允许数据共享
     */
    public boolean isDataSharingAllowed(String dataType) {
        switch (privacyLevel) {
            case LOW:
                return true; // 低隐私级别，允许所有数据共享
            case MEDIUM:
                // 中等隐私级别，允许非敏感数据共享
                return !isSensitiveDataType(dataType);
            case HIGH:
                return false; // 高隐私级别，不允许任何数据共享
            default:
                return false;
        }
    }
    
    /**
     * 检查数据类型是否敏感
     * @param dataType 数据类型
     * @return 是否敏感数据类型
     */
    private boolean isSensitiveDataType(String dataType) {
        // 定义敏感数据类型
        String[] sensitiveTypes = {"personal", "financial", "health", "location"};
        for (String type : sensitiveTypes) {
            if (type.equalsIgnoreCase(dataType)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 检查是否允许技能共享
     * @param skillId 技能ID
     * @return 是否允许技能共享
     */
    public boolean isSkillSharingAllowed(String skillId) {
        switch (privacyLevel) {
            case LOW:
                return true; // 低隐私级别，允许所有技能共享
            case MEDIUM:
                return true; // 中等隐私级别，允许技能共享
            case HIGH:
                return false; // 高隐私级别，不允许技能共享
            default:
                return false;
        }
    }
    
    /**
     * 检查是否允许设备访问
     * @param deviceId 设备ID
     * @return 是否允许设备访问
     */
    public boolean isDeviceAccessAllowed(String deviceId) {
        switch (privacyLevel) {
            case LOW:
                return true; // 低隐私级别，允许所有设备访问
            case MEDIUM:
                return true; // 中等隐私级别，允许设备访问
            case HIGH:
                return false; // 高隐私级别，不允许设备访问
            default:
                return false;
        }
    }
    
    /**
     * 隐私级别枚举
     */
    public enum PrivacyLevel {
        LOW("低", "允许大部分数据和技能共享"),
        MEDIUM("中", "允许非敏感数据和技能共享"),
        HIGH("高", "不允许任何数据和技能共享");
        
        private String name;
        private String description;
        
        PrivacyLevel(String name, String description) {
            this.name = name;
            this.description = description;
        }
        
        public String getName() {
            return name;
        }
        
        public String getDescription() {
            return description;
        }
        
        @Override
        public String toString() {
            return name;
        }
    }
}