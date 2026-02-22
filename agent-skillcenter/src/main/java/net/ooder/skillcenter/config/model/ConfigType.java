package net.ooder.skillcenter.config.model;

/**
 * 配置类型枚举 - 符合v0.7.0协议规范
 */
public enum ConfigType {
    
    STRING("string", "字符串"),
    NUMBER("number", "数字"),
    BOOLEAN("boolean", "布尔值"),
    OBJECT("object", "对象"),
    ARRAY("array", "数组"),
    SECRET("secret", "敏感信息");
    
    private final String value;
    private final String description;
    
    ConfigType(String value, String description) {
        this.value = value;
        this.description = description;
    }
    
    public String getValue() { return value; }
    public String getDescription() { return description; }
    
    public static ConfigType fromValue(String value) {
        if (value == null) return null;
        for (ConfigType type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        return null;
    }
}
