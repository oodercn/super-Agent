package net.ooder.nexus.model.device;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 设备日志结果
 * 用于DeviceController中getDeviceLogs方法的返回类型
 */
public class DeviceLogsResult implements Serializable {
    private List<Map<String, Object>> logs;
    private int count;

    public DeviceLogsResult() {
    }

    public DeviceLogsResult(List<Map<String, Object>> logs, int count) {
        this.logs = logs;
        this.count = count;
    }

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