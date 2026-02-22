package net.ooder.nexus.model;

public class DashboardResult {
    private int totalDevices;
    private int onlineDevices;
    private int offlineDevices;
    private String systemStatus;
    private int activeAlerts;
    private int pendingTasks;
    private String lastUpdateTime;
    private String message;

    public DashboardResult() {
    }

    public DashboardResult(int totalDevices, int onlineDevices, int offlineDevices, String systemStatus, int activeAlerts, int pendingTasks, String lastUpdateTime, String message) {
        this.totalDevices = totalDevices;
        this.onlineDevices = onlineDevices;
        this.offlineDevices = offlineDevices;
        this.systemStatus = systemStatus;
        this.activeAlerts = activeAlerts;
        this.pendingTasks = pendingTasks;
        this.lastUpdateTime = lastUpdateTime;
        this.message = message;
    }

    public int getTotalDevices() {
        return totalDevices;
    }

    public void setTotalDevices(int totalDevices) {
        this.totalDevices = totalDevices;
    }

    public int getOnlineDevices() {
        return onlineDevices;
    }

    public void setOnlineDevices(int onlineDevices) {
        this.onlineDevices = onlineDevices;
    }

    public int getOfflineDevices() {
        return offlineDevices;
    }

    public void setOfflineDevices(int offlineDevices) {
        this.offlineDevices = offlineDevices;
    }

    public String getSystemStatus() {
        return systemStatus;
    }

    public void setSystemStatus(String systemStatus) {
        this.systemStatus = systemStatus;
    }

    public int getActiveAlerts() {
        return activeAlerts;
    }

    public void setActiveAlerts(int activeAlerts) {
        this.activeAlerts = activeAlerts;
    }

    public int getPendingTasks() {
        return pendingTasks;
    }

    public void setPendingTasks(int pendingTasks) {
        this.pendingTasks = pendingTasks;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}