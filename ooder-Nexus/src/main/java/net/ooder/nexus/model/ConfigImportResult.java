package net.ooder.nexus.model;

public class ConfigImportResult {
    private boolean success;
    private String configType;
    private String importedConfigId;
    private String importTime;
    private String message;
    private String error;

    public ConfigImportResult() {
    }

    public ConfigImportResult(boolean success, String configType, String importedConfigId, String importTime, String message, String error) {
        this.success = success;
        this.configType = configType;
        this.importedConfigId = importedConfigId;
        this.importTime = importTime;
        this.message = message;
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

    public String getImportedConfigId() {
        return importedConfigId;
    }

    public void setImportedConfigId(String importedConfigId) {
        this.importedConfigId = importedConfigId;
    }

    public String getImportTime() {
        return importTime;
    }

    public void setImportTime(String importTime) {
        this.importTime = importTime;
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