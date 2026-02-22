package net.ooder.nexus.domain.network.model;

import java.time.LocalDateTime;

/**
 * 流量统计实体类
 */
public class TrafficStats {
    
    private String id;
    private String deviceName;
    private String deviceIp;
    private double uploadSpeed;
    private double downloadSpeed;
    private double totalTraffic;
    private double usagePercentage;
    private LocalDateTime updateTime;
    
    public TrafficStats() {
        this.updateTime = LocalDateTime.now();
    }
    
    public TrafficStats(String id, String deviceName, String deviceIp, 
                       double uploadSpeed, double downloadSpeed, 
                       double totalTraffic, double usagePercentage) {
        this();
        this.id = id;
        this.deviceName = deviceName;
        this.deviceIp = deviceIp;
        this.uploadSpeed = uploadSpeed;
        this.downloadSpeed = downloadSpeed;
        this.totalTraffic = totalTraffic;
        this.usagePercentage = usagePercentage;
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
    
    public String getDeviceIp() {
        return deviceIp;
    }
    
    public void setDeviceIp(String deviceIp) {
        this.deviceIp = deviceIp;
    }
    
    public double getUploadSpeed() {
        return uploadSpeed;
    }
    
    public void setUploadSpeed(double uploadSpeed) {
        this.uploadSpeed = uploadSpeed;
    }
    
    public double getDownloadSpeed() {
        return downloadSpeed;
    }
    
    public void setDownloadSpeed(double downloadSpeed) {
        this.downloadSpeed = downloadSpeed;
    }
    
    public double getTotalTraffic() {
        return totalTraffic;
    }
    
    public void setTotalTraffic(double totalTraffic) {
        this.totalTraffic = totalTraffic;
    }
    
    public double getUsagePercentage() {
        return usagePercentage;
    }
    
    public void setUsagePercentage(double usagePercentage) {
        this.usagePercentage = usagePercentage;
    }
    
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}
