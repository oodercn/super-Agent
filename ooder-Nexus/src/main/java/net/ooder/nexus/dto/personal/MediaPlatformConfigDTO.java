package net.ooder.nexus.dto.personal;

import java.io.Serializable;
import java.util.Map;

public class MediaPlatformConfigDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String appId;
    private String appSecret;
    private String accessToken;
    private Map<String, String> additionalConfig;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Map<String, String> getAdditionalConfig() {
        return additionalConfig;
    }

    public void setAdditionalConfig(Map<String, String> additionalConfig) {
        this.additionalConfig = additionalConfig;
    }
}
