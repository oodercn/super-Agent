package net.ooder.nexus.dto.enterprise;

import java.io.Serializable;

/**
 * Feishu (Lark) configuration DTO
 */
public class FeishuConfigDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * App ID
     */
    private String appId;

    /**
     * App secret
     */
    private String appSecret;

    /**
     * Encrypt key (optional)
     */
    private String encryptKey;

    /**
     * Verification token (optional)
     */
    private String verificationToken;

    /**
     * Callback URL (optional)
     */
    private String callbackUrl;

    /**
     * Tenant key (optional)
     */
    private String tenantKey;

    public String getAppId() { return appId; }
    public void setAppId(String appId) { this.appId = appId; }
    public String getAppSecret() { return appSecret; }
    public void setAppSecret(String appSecret) { this.appSecret = appSecret; }
    public String getEncryptKey() { return encryptKey; }
    public void setEncryptKey(String encryptKey) { this.encryptKey = encryptKey; }
    public String getVerificationToken() { return verificationToken; }
    public void setVerificationToken(String verificationToken) { this.verificationToken = verificationToken; }
    public String getCallbackUrl() { return callbackUrl; }
    public void setCallbackUrl(String callbackUrl) { this.callbackUrl = callbackUrl; }
    public String getTenantKey() { return tenantKey; }
    public void setTenantKey(String tenantKey) { this.tenantKey = tenantKey; }
}
