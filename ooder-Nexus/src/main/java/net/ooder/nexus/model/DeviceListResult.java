package net.ooder.nexus.model;

import java.util.List;
import java.util.Map;

/**
 * 设备列表结果实体Bean
 * 用于DeviceController中getDevices方法的返回类型
 */
public class DeviceListResult {
    
    private List<Map<String, Object>> devices;
    private Map<String, Object> stats;

    public List<Map<String, Object>> getDevices() {
        return devices;
    }

    public void setDevices(List<Map<String, Object>> devices) {
        this.devices = devices;
    }

    public Map<String, Object> getStats() {
        return stats;
    }

    public void setStats(Map<String, Object> stats) {
        this.stats = stats;
    }
}
