package net.ooder.nexus.dto.enterprise;

import java.io.Serializable;

/**
 * DingTalk configuration DTO
 */
public class DingTalkConfigDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * App key
     */
    private String appkey;

    /**
     * App secret
     */
    private String appsecret;

    /**
     * Agent ID
     */
    private String agentId;

    /**
     * Corp ID (optional)
     */
    private String corpId;

    /**
     * Callback URL (optional)
     */
    private String callbackUrl;

    /**
     * Token (optional)
     */
    private String token;

    /**
     * Encoding AES key (optional)
     */
    private String encodingAesKey;

    public String getAppkey() { return appkey; }
    public void setAppkey(String appkey) { this.appkey = appkey; }
    public String getAppsecret() { return appsecret; }
    public void setAppsecret(String appsecret) { this.appsecret = appsecret; }
    public String getAgentId() { return agentId; }
    public void setAgentId(String agentId) { this.agentId = agentId; }
    public String getCorpId() { return corpId; }
    public void setCorpId(String corpId) { this.corpId = corpId; }
    public String getCallbackUrl() { return callbackUrl; }
    public void setCallbackUrl(String callbackUrl) { this.callbackUrl = callbackUrl; }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getEncodingAesKey() { return encodingAesKey; }
    public void setEncodingAesKey(String encodingAesKey) { this.encodingAesKey = encodingAesKey; }
}
