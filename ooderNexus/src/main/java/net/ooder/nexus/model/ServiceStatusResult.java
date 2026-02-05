package net.ooder.nexus.model;

import net.ooder.nexus.controller.HealthCheckController;
import java.util.List;

/**
 * 服务状态结果实体Bean
 * 用于HealthCheckController中getServiceStatus方法的返回类型
 */
public class ServiceStatusResult {
    
    private List<HealthCheckController.ServiceStatus> services;
    private int total;
    private int running;

    public List<HealthCheckController.ServiceStatus> getServices() {
        return services;
    }

    public void setServices(List<HealthCheckController.ServiceStatus> services) {
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
