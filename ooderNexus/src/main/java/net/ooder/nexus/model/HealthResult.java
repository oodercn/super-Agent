package net.ooder.nexus.model;

public class HealthResult {
    private String status;
    private String systemStatus;
    private String networkStatus;
    private String serviceStatus;
    private int activeAlerts;
    private String lastCheckTime;
    private String message;

    public HealthResult() {
    }

    public HealthResult(String status, String systemStatus, String networkStatus, String serviceStatus, int activeAlerts, String lastCheckTime, String message) {
        this.status = status;
        this.systemStatus = systemStatus;
        this.networkStatus = networkStatus;
        this.serviceStatus = serviceStatus;
        this.activeAlerts = activeAlerts;
        this.lastCheckTime = lastCheckTime;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSystemStatus() {
        return systemStatus;
    }

    public void setSystemStatus(String systemStatus) {
        this.systemStatus = systemStatus;
    }

    public String getNetworkStatus() {
        return networkStatus;
    }

    public void setNetworkStatus(String networkStatus) {
        this.networkStatus = networkStatus;
    }

    public String getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(String serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public int getActiveAlerts() {
        return activeAlerts;
    }

    public void setActiveAlerts(int activeAlerts) {
        this.activeAlerts = activeAlerts;
    }

    public String getLastCheckTime() {
        return lastCheckTime;
    }

    public void setLastCheckTime(String lastCheckTime) {
        this.lastCheckTime = lastCheckTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}