package net.ooder.nexus.dto.network;

import java.io.Serializable;

/**
 * IP address create request DTO
 */
public class IPAddressCreateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String ipAddress;
    private String type;
    private String description;

    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
