package net.ooder.agent.metadata.enums;

/**
 * 场景类型枚举
 */
public enum SceneTypeEnum {
    GENERAL("general", "通用场景"),
    DATA_REPORTING("data-reporting", "数据上报场景"),
    TASK_COORDINATION("task-coordination", "任务协调场景"),
    RESOURCE_SHARING("resource-sharing", "资源共享场景"),
    SECURITY_MONITORING("security-monitoring", "安全监控场景");

    private String code;
    private String description;

    SceneTypeEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static SceneTypeEnum fromCode(String code) {
        for (SceneTypeEnum type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }
}