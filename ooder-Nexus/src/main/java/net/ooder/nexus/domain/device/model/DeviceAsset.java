package net.ooder.nexus.domain.device.model;

import java.time.LocalDateTime;

/**
 * 设备资产实体类
 */
public class DeviceAsset {
    
    private String id;
    private String name;
    private String type;
    private String ip;
    private String mac;
    private String status;
    private String description;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    public DeviceAsset() {
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }
    
    public DeviceAsset(String id, String name, String type, String ip, String mac, String status, String description) {
        this();
        this.id = id;
        this.name = name;
        this.type = type;
        this.ip = ip;
        this.mac = mac;
        this.status = status;
        this.description = description;
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getIp() {
        return ip;
    }
    
    public void setIp(String ip) {
        this.ip = ip;
    }
    
    public String getMac() {
        return mac;
    }
    
    public void setMac(String mac) {
        this.mac = mac;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
    
    @Override
    public String toString() {
        return "DeviceAsset{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", ip='" + ip + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
