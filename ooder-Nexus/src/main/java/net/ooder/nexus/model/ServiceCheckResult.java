package net.ooder.nexus.model;

import java.util.Date;

/**
 * 服务检查结果实体Bean
 * 用于HealthCheckController中checkServiceStatus方法的返回类型
 */
public class ServiceCheckResult {
    
    private String serviceType;
    private String status;
    private Date checkTime;
    private String message;

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
