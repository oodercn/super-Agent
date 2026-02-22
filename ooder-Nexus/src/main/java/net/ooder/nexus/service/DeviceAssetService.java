package net.ooder.nexus.service;

import net.ooder.nexus.domain.device.model.DeviceAsset;

import java.util.List;

/**
 * 设备资产服务接口
 */
public interface DeviceAssetService {
    
    /**
     * 创建设备
     */
    DeviceAsset createDevice(DeviceAsset device);
    
    /**
     * 更新设备
     */
    DeviceAsset updateDevice(String id, DeviceAsset device);
    
    /**
     * 删除设备
     */
    boolean deleteDevice(String id);
    
    /**
     * 根据ID获取设备
     */
    DeviceAsset getDeviceById(String id);
    
    /**
     * 获取所有设备
     */
    List<DeviceAsset> getAllDevices();
    
    /**
     * 根据类型获取设备
     */
    List<DeviceAsset> getDevicesByType(String type);
    
    /**
     * 根据状态获取设备
     */
    List<DeviceAsset> getDevicesByStatus(String status);
    
    /**
     * 初始化默认数据
     */
    void initDefaultData();
    
    /**
     * 获取设备统计信息
     */
    DeviceStats getDeviceStats();
    
    /**
     * 设备统计信息
     */
    class DeviceStats {
        private int totalCount;
        private int onlineCount;
        private int offlineCount;
        
        public int getTotalCount() {
            return totalCount;
        }
        
        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }
        
        public int getOnlineCount() {
            return onlineCount;
        }
        
        public void setOnlineCount(int onlineCount) {
            this.onlineCount = onlineCount;
        }
        
        public int getOfflineCount() {
            return offlineCount;
        }
        
        public void setOfflineCount(int offlineCount) {
            this.offlineCount = offlineCount;
        }
    }
}
