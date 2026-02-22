package net.ooder.skillcenter.capability.model;

/**
 * 能力返回值 - 符合v0.7.0协议规范
 */
public class CapabilityReturn {
    
    private String name;
    private String type;
    private String description;
    private boolean required;

    public CapabilityReturn() {
        this.required = true;
    }

    public static CapabilityReturn of(String name, String type, String description) {
        CapabilityReturn ret = new CapabilityReturn();
        ret.setName(name);
        ret.setType(type);
        ret.setDescription(description);
        return ret;
    }

    public static CapabilityReturn data(String description) {
        return of("data", "object", description);
    }

    public static CapabilityReturn success() {
        return of("success", "boolean", "操作是否成功");
    }

    public static CapabilityReturn message() {
        return of("message", "string", "返回消息");
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isRequired() { return required; }
    public void setRequired(boolean required) { this.required = required; }
}
