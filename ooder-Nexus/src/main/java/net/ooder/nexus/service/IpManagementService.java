package net.ooder.nexus.service;

import net.ooder.nexus.domain.network.model.IpAllocation;
import net.ooder.nexus.domain.network.model.IpPool;

import java.util.List;

/**
 * IP管理服务接口
 */
public interface IpManagementService {
    
    /**
     * 获取所有IP分配
     */
    List<IpAllocation> getAllAllocations();
    
    /**
     * 根据类型获取IP分配
     */
    List<IpAllocation> getAllocationsByType(String type);
    
    /**
     * 根据状态获取IP分配
     */
    List<IpAllocation> getAllocationsByStatus(String status);
    
    /**
     * 创建IP分配
     */
    IpAllocation createAllocation(IpAllocation allocation);
    
    /**
     * 更新IP分配
     */
    IpAllocation updateAllocation(String id, IpAllocation allocation);
    
    /**
     * 删除IP分配
     */
    boolean deleteAllocation(String id);
    
    /**
     * 获取IP池配置
     */
    IpPool getIpPool();
    
    /**
     * 更新IP池配置
     */
    IpPool updateIpPool(IpPool pool);
    
    /**
     * 获取IP统计信息
     */
    IpStatistics getStatistics();
    
    /**
     * 初始化默认数据
     */
    void initDefaultData();
    
    /**
     * IP统计信息
     */
    class IpStatistics {
        private int totalAllocated;
        private int totalAvailable;
        private double allocationRate;
        private int staticCount;
        private int dhcpCount;
        private int activeCount;
        private int inactiveCount;
        
        public int getTotalAllocated() {
            return totalAllocated;
        }
        
        public void setTotalAllocated(int totalAllocated) {
            this.totalAllocated = totalAllocated;
        }
        
        public int getTotalAvailable() {
            return totalAvailable;
        }
        
        public void setTotalAvailable(int totalAvailable) {
            this.totalAvailable = totalAvailable;
        }
        
        public double getAllocationRate() {
            return allocationRate;
        }
        
        public void setAllocationRate(double allocationRate) {
            this.allocationRate = allocationRate;
        }
        
        public int getStaticCount() {
            return staticCount;
        }
        
        public void setStaticCount(int staticCount) {
            this.staticCount = staticCount;
        }
        
        public int getDhcpCount() {
            return dhcpCount;
        }
        
        public void setDhcpCount(int dhcpCount) {
            this.dhcpCount = dhcpCount;
        }
        
        public int getActiveCount() {
            return activeCount;
        }
        
        public void setActiveCount(int activeCount) {
            this.activeCount = activeCount;
        }
        
        public int getInactiveCount() {
            return inactiveCount;
        }
        
        public void setInactiveCount(int inactiveCount) {
            this.inactiveCount = inactiveCount;
        }
    }
}
