package net.ooder.skill.config.dto;

public class CliConfigDTO {
    
    private String cliId;
    private String name;
    private String type;
    private boolean enabled;
    private String appId;
    private String appSecret;
    private String baseUrl;
    private String callbackUrl;
    private String token;
    private String encodingAesKey;

    public String getCliId() { return cliId; }
    public void setCliId(String cliId) { this.cliId = cliId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public String getAppId() { return appId; }
    public void setAppId(String appId) { this.appId = appId; }
    public String getAppSecret() { return appSecret; }
    public void setAppSecret(String appSecret) { this.appSecret = appSecret; }
    public String getBaseUrl() { return baseUrl; }
    public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }
    public String getCallbackUrl() { return callbackUrl; }
    public void setCallbackUrl(String callbackUrl) { this.callbackUrl = callbackUrl; }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getEncodingAesKey() { return encodingAesKey; }
    public void setEncodingAesKey(String encodingAesKey) { this.encodingAesKey = encodingAesKey; }
}
