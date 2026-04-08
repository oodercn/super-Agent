package net.ooder.skill.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "auth.qrcode")
public class QrcodeLoginConfig {
    
    private Boolean enabled = true;
    private Map<String, PlatformConfig> platforms = new HashMap<>();
    private Integer qrcodeExpireTime = 300;
    private Integer checkInterval = 2000;

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Map<String, PlatformConfig> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(Map<String, PlatformConfig> platforms) {
        this.platforms = platforms;
    }

    public Integer getQrcodeExpireTime() {
        return qrcodeExpireTime;
    }

    public void setQrcodeExpireTime(Integer qrcodeExpireTime) {
        this.qrcodeExpireTime = qrcodeExpireTime;
    }

    public Integer getCheckInterval() {
        return checkInterval;
    }

    public void setCheckInterval(Integer checkInterval) {
        this.checkInterval = checkInterval;
    }

    public static class PlatformConfig {
        private Boolean enabled = true;
        private String name;
        private String icon;
        private String appId;
        private String appSecret;
        private String callbackUrl;
        private String authUrl;
        private String qrcodeUrl;
        private String description;

        public Boolean getEnabled() {
            return enabled;
        }

        public void setEnabled(Boolean enabled) {
            this.enabled = enabled;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

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

        public String getCallbackUrl() {
            return callbackUrl;
        }

        public void setCallbackUrl(String callbackUrl) {
            this.callbackUrl = callbackUrl;
        }

        public String getAuthUrl() {
            return authUrl;
        }

        public void setAuthUrl(String authUrl) {
            this.authUrl = authUrl;
        }

        public String getQrcodeUrl() {
            return qrcodeUrl;
        }

        public void setQrcodeUrl(String qrcodeUrl) {
            this.qrcodeUrl = qrcodeUrl;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
