package net.ooder.nexus.model;

public class ConfigExportResult {
    private boolean success;
    private String filePath;
    private String fileName;
    private long fileSize;
    private String exportTime;
    private String configType;
    private String error;

    public ConfigExportResult() {
    }

    public ConfigExportResult(boolean success, String filePath, String fileName, long fileSize, String exportTime, String configType, String error) {
        this.success = success;
        this.filePath = filePath;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.exportTime = exportTime;
        this.configType = configType;
        this.error = error;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getExportTime() {
        return exportTime;
    }

    public void setExportTime(String exportTime) {
        this.exportTime = exportTime;
    }

    public String getConfigType() {
        return configType;
    }

    public void setConfigType(String configType) {
        this.configType = configType;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}