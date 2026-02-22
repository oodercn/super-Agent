package net.ooder.skillcenter.scene.model;

/**
 * 安全策略枚举 - 符合v0.7.0协议规范
 */
public enum SecurityPolicy {
    
    API_KEY("api-key", "API密钥认证"),
    OAUTH2("oauth2", "OAuth2认证"),
    JWT("jwt", "JWT令牌认证"),
    BASIC("basic", "基础认证"),
    NONE("none", "无认证");
    
    private final String value;
    private final String description;
    
    SecurityPolicy(String value, String description) {
        this.value = value;
        this.description = description;
    }
    
    public String getValue() { return value; }
    public String getDescription() { return description; }
    
    public static SecurityPolicy fromValue(String value) {
        if (value == null) return null;
        for (SecurityPolicy policy : values()) {
            if (policy.value.equals(value)) {
                return policy;
            }
        }
        return null;
    }
}
