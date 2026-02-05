package net.ooder.sdk.skill;

/**
 * 技能状态枚举
 */
public enum SkillStatus {
    
    /**
     * 未初始化
     */
    UNINITIALIZED("uninitialized"),
    
    /**
     * 初始化中
     */
    INITIALIZING("initializing"),
    
    /**
     * 就绪
     */
    READY("ready"),
    
    /**
     * 执行中
     */
    EXECUTING("executing"),
    
    /**
     * 暂停
     */
    PAUSED("paused"),
    
    /**
     * 错误
     */
    ERROR("error"),
    
    /**
     * 销毁中
     */
    DESTROYING("destroying"),
    
    /**
     * 已销毁
     */
    DESTROYED("destroyed");
    
    private final String value;
    
    SkillStatus(String value) {
        this.value = value;
    }
    
    /**
     * 获取状态值
     * @return 状态值
     */
    public String getValue() {
        return value;
    }
    
    /**
     * 根据值获取状态枚举
     * @param value 状态值
     * @return 状态枚举
     */
    public static SkillStatus fromValue(String value) {
        for (SkillStatus status : SkillStatus.values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid skill status: " + value);
    }
}