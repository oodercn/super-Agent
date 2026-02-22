package net.ooder.nexus.domain.network.model;

/**
 * IP池配置实体类
 */
public class IpPool {
    
    private String id;
    private String startIp;
    private String endIp;
    private String subnetMask;
    private String gateway;
    private String dns;
    private int totalCount;
    private int allocatedCount;
    private int availableCount;
    
    public IpPool() {
    }
    
    public IpPool(String id, String startIp, String endIp, String subnetMask) {
        this.id = id;
        this.startIp = startIp;
        this.endIp = endIp;
        this.subnetMask = subnetMask;
        calculateCounts();
    }
    
    private void calculateCounts() {
        if (startIp != null && endIp != null) {
            String[] startParts = startIp.split("\\.");
            String[] endParts = endIp.split("\\.");
            if (startParts.length == 4 && endParts.length == 4) {
                int start = (Integer.parseInt(startParts[3]) << 24) |
                           (Integer.parseInt(startParts[2]) << 16) |
                           (Integer.parseInt(startParts[1]) << 8) |
                           Integer.parseInt(startParts[0]);
                int end = (Integer.parseInt(endParts[3]) << 24) |
                         (Integer.parseInt(endParts[2]) << 16) |
                         (Integer.parseInt(endParts[1]) << 8) |
                         Integer.parseInt(endParts[0]);
                totalCount = Math.abs(end - start) + 1;
            }
        }
        if (allocatedCount > 0) {
            availableCount = totalCount - allocatedCount;
        } else {
            availableCount = totalCount;
        }
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getStartIp() {
        return startIp;
    }
    
    public void setStartIp(String startIp) {
        this.startIp = startIp;
        calculateCounts();
    }
    
    public String getEndIp() {
        return endIp;
    }
    
    public void setEndIp(String endIp) {
        this.endIp = endIp;
        calculateCounts();
    }
    
    public String getSubnetMask() {
        return subnetMask;
    }
    
    public void setSubnetMask(String subnetMask) {
        this.subnetMask = subnetMask;
    }
    
    public String getGateway() {
        return gateway;
    }
    
    public void setGateway(String gateway) {
        this.gateway = gateway;
    }
    
    public String getDns() {
        return dns;
    }
    
    public void setDns(String dns) {
        this.dns = dns;
    }
    
    public int getTotalCount() {
        return totalCount;
    }
    
    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
    
    public int getAllocatedCount() {
        return allocatedCount;
    }
    
    public void setAllocatedCount(int allocatedCount) {
        this.allocatedCount = allocatedCount;
        calculateCounts();
    }
    
    public int getAvailableCount() {
        return availableCount;
    }
    
    public void setAvailableCount(int availableCount) {
        this.availableCount = availableCount;
        totalCount = allocatedCount + availableCount;
    }
    
    public double getAllocationRate() {
        return totalCount > 0 ? (double) allocatedCount / totalCount * 100 : 0;
    }
    
    /**
     * 空 setter 用于 JSON 反序列化时忽略 allocationRate 字段
     * 实际值通过 getAllocationRate() 方法动态计算
     */
    public void setAllocationRate(double allocationRate) {
        // 忽略传入的值，由 getAllocationRate() 动态计算
    }
}
