package net.ooder.nexus.dto.enterprise;

import java.io.Serializable;

/**
 * WeChat Work configuration DTO
 */
public class WechatWorkConfigDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Enterprise ID
     */
    private String corpid;

    /**
     * Agent ID
     */
    private String agentid;

    /**
     * Secret key
     */
    private String corpsecret;

    /**
     * Token (optional)
     */
    private String token;

    /**
     * Encoding AES key (optional)
     */
    private String encodingAesKey;

    public String getCorpid() { return corpid; }
    public void setCorpid(String corpid) { this.corpid = corpid; }
    public String getAgentid() { return agentid; }
    public void setAgentid(String agentid) { this.agentid = agentid; }
    public String getCorpsecret() { return corpsecret; }
    public void setCorpsecret(String corpsecret) { this.corpsecret = corpsecret; }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getEncodingAesKey() { return encodingAesKey; }
    public void setEncodingAesKey(String encodingAesKey) { this.encodingAesKey = encodingAesKey; }
}
