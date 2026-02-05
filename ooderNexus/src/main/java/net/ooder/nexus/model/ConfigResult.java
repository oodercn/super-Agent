package net.ooder.nexus.model;

public class ConfigResult {
    private boolean success;
    private String message;
    private String configType;
    private String timestamp;

    public ConfigResult() {
    }

    public ConfigResult(boolean success, String message, String configType, String timestamp) {
        this.success = success;
        this.message = message;
        this.configType = configType;
        this.timestamp = timestamp;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getConfigType() {
        return configType;
    }

    public void setConfigType(String configType) {
        this.configType = configType;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}