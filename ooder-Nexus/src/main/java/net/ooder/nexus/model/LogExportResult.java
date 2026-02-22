package net.ooder.nexus.model;

public class LogExportResult {
    private boolean success;
    private String filePath;
    private long fileSize;
    private String fileName;
    private String exportTime;
    private String error;

    public LogExportResult() {
    }

    public LogExportResult(boolean success, String filePath, long fileSize, String fileName, String exportTime, String error) {
        this.success = success;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.fileName = fileName;
        this.exportTime = exportTime;
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

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getExportTime() {
        return exportTime;
    }

    public void setExportTime(String exportTime) {
        this.exportTime = exportTime;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}