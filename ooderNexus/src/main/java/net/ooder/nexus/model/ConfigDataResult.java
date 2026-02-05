package net.ooder.nexus.model;

public class ConfigDataResult {
    private boolean success;
    private String configType;
    private String configId;
    private String message;
    private String timestamp;
    private String error;

    public ConfigDataResult() {
    }

    public ConfigDataResult(boolean success, String configType, String configId, String message, String timestamp, String error) {
        this.success = success;
        this.configType = configType;
        this.configId = configId;
        this.message = message;
        this.timestamp = timestamp;
        this.error = error;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getConfigType() {
        return configType;
    }

    public void setConfigType(String configType) {
        this.configType = configType;
    }

    public String getConfigId() {
        return configId;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}