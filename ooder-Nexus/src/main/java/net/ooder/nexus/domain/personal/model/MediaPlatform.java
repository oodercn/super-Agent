package net.ooder.nexus.domain.personal.model;

import java.util.Date;
import java.util.List;

public class MediaPlatform {
    private String platformId;
    private String name;
    private String icon;
    private String status;
    private String accountName;
    private Date authExpireAt;
    private List<String> features;

    public MediaPlatform() {}

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public Date getAuthExpireAt() {
        return authExpireAt;
    }

    public void setAuthExpireAt(Date authExpireAt) {
        this.authExpireAt = authExpireAt;
    }

    public List<String> getFeatures() {
        return features;
    }

    public void setFeatures(List<String> features) {
        this.features = features;
    }
}
