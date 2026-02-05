package net.ooder.agent.metadata.enums;

/**
 * 组类型枚举
 */
public enum GroupTypeEnum {
    GENERAL("general", "通用组"),
    COORDINATION("coordination", "协调组"),
    DATA_FLOW("data-flow", "数据流组"),
    SECURITY("security", "安全组"),
    RESOURCE("resource", "资源组");

    private String code;
    private String description;

    GroupTypeEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static GroupTypeEnum fromCode(String code) {
        for (GroupTypeEnum type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }
}