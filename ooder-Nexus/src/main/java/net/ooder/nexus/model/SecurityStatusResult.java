package net.ooder.nexus.model;

public class SecurityStatusResult {
    private String status;
    private boolean isSecure;
    private int activeThreats;
    private int pendingUpdates;
    private String lastScanTime;
    private String message;

    public SecurityStatusResult() {
    }

    public SecurityStatusResult(String status, boolean isSecure, int activeThreats, int pendingUpdates, String lastScanTime, String message) {
        this.status = status;
        this.isSecure = isSecure;
        this.activeThreats = activeThreats;
        this.pendingUpdates = pendingUpdates;
        this.lastScanTime = lastScanTime;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isSecure() {
        return isSecure;
    }

    public void setSecure(boolean secure) {
        isSecure = secure;
    }

    public int getActiveThreats() {
        return activeThreats;
    }

    public void setActiveThreats(int activeThreats) {
        this.activeThreats = activeThreats;
    }

    public int getPendingUpdates() {
        return pendingUpdates;
    }

    public void setPendingUpdates(int pendingUpdates) {
        this.pendingUpdates = pendingUpdates;
    }

    public String getLastScanTime() {
        return lastScanTime;
    }

    public void setLastScanTime(String lastScanTime) {
        this.lastScanTime = lastScanTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}