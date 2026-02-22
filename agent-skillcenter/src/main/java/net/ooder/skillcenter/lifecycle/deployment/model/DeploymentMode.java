package net.ooder.skillcenter.lifecycle.deployment.model;

/**
 * 部署模式枚举 - 符合v0.7.0协议规范
 */
public enum DeploymentMode {
    
    REMOTE_HOSTED("remote-hosted", "远程托管"),
    LOCAL_DEPLOYED("local-deployed", "本地部署"),
    HYBRID("hybrid", "混合模式");
    
    private final String value;
    private final String description;
    
    DeploymentMode(String value, String description) {
        this.value = value;
        this.description = description;
    }
    
    public String getValue() { return value; }
    public String getDescription() { return description; }
    
    public static DeploymentMode fromValue(String value) {
        if (value == null) return null;
        for (DeploymentMode mode : values()) {
            if (mode.value.equals(value)) {
                return mode;
            }
        }
        return null;
    }
}
