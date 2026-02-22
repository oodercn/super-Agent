package net.ooder.skillcenter.capability.model;

/**
 * 能力分类枚举 - 符合v0.7.0协议规范
 */
public enum CapabilityCategory {
    
    DATA_ACCESS("data-access", "数据访问", new String[]{"org-data-read", "file-read", "database-read"}),
    AUTHENTICATION("authentication", "认证服务", new String[]{"user-auth", "sso-auth", "token-validate"}),
    COMMUNICATION("communication", "通信消息", new String[]{"send-message", "send-email", "send-notification"}),
    INTEGRATION("integration", "系统集成", new String[]{"sync-data", "import-data", "export-data"}),
    PROCESSING("processing", "数据处理", new String[]{"transform-data", "analyze-data", "validate-data"}),
    STORAGE("storage", "存储操作", new String[]{"file-upload", "file-download", "file-delete"});
    
    private final String value;
    private final String description;
    private final String[] examples;
    
    CapabilityCategory(String value, String description, String[] examples) {
        this.value = value;
        this.description = description;
        this.examples = examples;
    }
    
    public String getValue() { return value; }
    public String getDescription() { return description; }
    public String[] getExamples() { return examples; }
    
    public static CapabilityCategory fromValue(String value) {
        if (value == null) return null;
        for (CapabilityCategory category : values()) {
            if (category.value.equals(value)) {
                return category;
            }
        }
        return null;
    }
}
