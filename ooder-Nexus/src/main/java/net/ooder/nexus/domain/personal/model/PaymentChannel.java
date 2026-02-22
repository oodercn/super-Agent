package net.ooder.nexus.domain.personal.model;

import java.util.Date;
import java.util.List;

public class PaymentChannel {
    private String channelId;
    private String name;
    private String icon;
    private String status;
    private List<ConfigFieldInfo> configFields;
    private boolean connected;
    private Date lastChecked;
    private Date authExpireAt;

    public PaymentChannel() {}

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
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

    public List<ConfigFieldInfo> getConfigFields() {
        return configFields;
    }

    public void setConfigFields(List<ConfigFieldInfo> configFields) {
        this.configFields = configFields;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public Date getLastChecked() {
        return lastChecked;
    }

    public void setLastChecked(Date lastChecked) {
        this.lastChecked = lastChecked;
    }

    public Date getAuthExpireAt() {
        return authExpireAt;
    }

    public void setAuthExpireAt(Date authExpireAt) {
        this.authExpireAt = authExpireAt;
    }
}
