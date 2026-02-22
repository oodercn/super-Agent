package net.ooder.nexus.dto.network;

import java.io.Serializable;

/**
 * IP blacklist create request DTO
 */
public class IPBlacklistCreateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String ipAddress;
    private String reason;
    private Long expiryTime;

    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public Long getExpiryTime() { return expiryTime; }
    public void setExpiryTime(Long expiryTime) { this.expiryTime = expiryTime; }
}
