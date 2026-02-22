package net.ooder.nexus.model;

public class ConfigStatsResult {
    private int totalConfigs;
    private int activeConfigs;
    private int pendingChanges;
    private int configHistoryCount;
    private String lastConfigUpdate;
    private int failedConfigAttempts;
    private String message;

    public ConfigStatsResult() {
    }

    public ConfigStatsResult(int totalConfigs, int activeConfigs, int pendingChanges, int configHistoryCount, String lastConfigUpdate, int failedConfigAttempts, String message) {
        this.totalConfigs = totalConfigs;
        this.activeConfigs = activeConfigs;
        this.pendingChanges = pendingChanges;
        this.configHistoryCount = configHistoryCount;
        this.lastConfigUpdate = lastConfigUpdate;
        this.failedConfigAttempts = failedConfigAttempts;
        this.message = message;
    }

    public int getTotalConfigs() {
        return totalConfigs;
    }

    public void setTotalConfigs(int totalConfigs) {
        this.totalConfigs = totalConfigs;
    }

    public int getActiveConfigs() {
        return activeConfigs;
    }

    public void setActiveConfigs(int activeConfigs) {
        this.activeConfigs = activeConfigs;
    }

    public int getPendingChanges() {
        return pendingChanges;
    }

    public void setPendingChanges(int pendingChanges) {
        this.pendingChanges = pendingChanges;
    }

    public int getConfigHistoryCount() {
        return configHistoryCount;
    }

    public void setConfigHistoryCount(int configHistoryCount) {
        this.configHistoryCount = configHistoryCount;
    }

    public String getLastConfigUpdate() {
        return lastConfigUpdate;
    }

    public void setLastConfigUpdate(String lastConfigUpdate) {
        this.lastConfigUpdate = lastConfigUpdate;
    }

    public int getFailedConfigAttempts() {
        return failedConfigAttempts;
    }

    public void setFailedConfigAttempts(int failedConfigAttempts) {
        this.failedConfigAttempts = failedConfigAttempts;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}