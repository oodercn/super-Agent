package net.ooder.nexus.model;

import java.util.List;
import java.util.Map;

/**
 * 设备操作日志结果实体Bean
 * 用于DeviceController中getDeviceLogs方法的返回类型
 */
public class DeviceLogsResult {
    
    private List<Map<String, Object>> logs;
    private int count;

    public List<Map<String, Object>> getLogs() {
        return logs;
    }

    public void setLogs(List<Map<String, Object>> logs) {
        this.logs = logs;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
