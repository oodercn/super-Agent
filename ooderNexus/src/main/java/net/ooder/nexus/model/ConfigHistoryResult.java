package net.ooder.nexus.model;

public class ConfigHistoryResult {
    private boolean success;
    private String historyId;
    private String configType;
    private String appliedTime;
    private String message;
    private String error;

    public ConfigHistoryResult() {
    }

    public ConfigHistoryResult(boolean success, String historyId, String configType, String appliedTime, String message, String error) {
        this.success = success;
        this.historyId = historyId;
        this.configType = configType;
        this.appliedTime = appliedTime;
        this.message = message;
        this.error = error;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getHistoryId() {
        return historyId;
    }

    public void setHistoryId(String historyId) {
        this.historyId = historyId;
    }

    public String getConfigType() {
        return configType;
    }

    public void setConfigType(String configType) {
        this.configType = configType;
    }

    public String getAppliedTime() {
        return appliedTime;
    }

    public void setAppliedTime(String appliedTime) {
        this.appliedTime = appliedTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}