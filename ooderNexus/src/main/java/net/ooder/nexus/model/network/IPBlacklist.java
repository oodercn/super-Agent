package net.ooder.nexus.model.network;

import java.io.Serializable;

public class IPBlacklist implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String ipAddress;
    private String reason;
    private String source;
    private long addedAt;

    public IPBlacklist() {
    }

    public IPBlacklist(String id, String ipAddress, String reason, String source) {
        this.id = id;
        this.ipAddress = ipAddress;
        this.reason = reason;
        this.source = source;
        this.addedAt = System.currentTimeMillis();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public long getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(long addedAt) {
        this.addedAt = addedAt;
    }
}
