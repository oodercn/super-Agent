package net.ooder.nexus.model;

import java.util.List;
import java.util.Map;

/**
 * 服务状态结果实体Bean
 */
public class ServiceStatusResult {
    
    private List<Map<String, Object>> services;
    private int total;
    private int running;

    public List<Map<String, Object>> getServices() {
        return services;
    }

    public void setServices(List<Map<String, Object>> services) {
        this.services = services;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getRunning() {
        return running;
    }

    public void setRunning(int running) {
        this.running = running;
    }
}
