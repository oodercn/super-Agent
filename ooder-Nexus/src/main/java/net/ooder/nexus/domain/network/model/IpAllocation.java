package net.ooder.nexus.domain.network.model;

import java.time.LocalDateTime;

/**
 * IP分配实体类
 */
public class IpAllocation {
    
    private String id;
    private String deviceName;
    private String macAddress;
    private String ipAddress;
    private String status; // active, inactive
    private String allocationType; // static, dhcp
    private LocalDateTime allocateTime;
    private LocalDateTime expireTime;
    private String description;
    
    public IpAllocation() {
        this.allocateTime = LocalDateTime.now();
    }
    
    public IpAllocation(String id, String deviceName, String macAddress, String ipAddress, 
                       String status, String allocationType, String description) {
        this();
        this.id = id;
        this.deviceName = deviceName;
        this.macAddress = macAddress;
        this.ipAddress = ipAddress;
        this.status = status;
        this.allocationType = allocationType;
        this.description = description;
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getDeviceName() {
        return deviceName;
    }
    
    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
    
    public String getMacAddress() {
        return macAddress;
    }
    
    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }
    
    public String getIpAddress() {
        return ipAddress;
    }
    
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getAllocationType() {
        return allocationType;
    }
    
    public void setAllocationType(String allocationType) {
        this.allocationType = allocationType;
    }
    
    public LocalDateTime getAllocateTime() {
        return allocateTime;
    }
    
    public void setAllocateTime(LocalDateTime allocateTime) {
        this.allocateTime = allocateTime;
    }
    
    public LocalDateTime getExpireTime() {
        return expireTime;
    }
    
    public void setExpireTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
}
