package net.ooder.nexus.domain.end.model;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 终端代理实体类
 */
public class EndAgent {
    
    private String id;
    private String name;
    private String type;
    private String ip;
    private String mac;
    private String status;
    private LocalDateTime lastUpdate;
    private LocalDateTime createTime;
    
    // 设备特定属性（使用Map存储不同设备的特有属性）
    private Map<String, Object> properties;
    
    public EndAgent() {
        this.createTime = LocalDateTime.now();
        this.lastUpdate = LocalDateTime.now();
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
    
    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }
    
    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
    
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    
    public Map<String, Object> getProperties() {
        return properties;
    }
    
    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }
    
    @Override
    public String toString() {
        return "EndAgent{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", ip='" + ip + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
