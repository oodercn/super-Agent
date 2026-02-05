package net.ooder.nexus.model.mcp;

import java.util.Date;
import java.util.List;

public class LogExportResult {
    private String id;
    private String status;
    private String message;
    private String fileName;
    private long fileSize;
    private String fileType;
    private String downloadUrl;
    private int logCount;
    private List<String> logLevels;
    private Date startTime;
    private Date endTime;
    private Date exportTime;
    private long duration;

    public LogExportResult() {
    }

    public LogExportResult(String id, String status, String message, String fileName, long fileSize, String fileType, String downloadUrl, int logCount, List<String> logLevels, Date startTime, Date endTime, Date exportTime, long duration) {
        this.id = id;
        this.status = status;
        this.message = message;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.fileType = fileType;
        this.downloadUrl = downloadUrl;
        this.logCount = logCount;
        this.logLevels = logLevels;
        this.startTime = startTime;
        this.endTime = endTime;
        this.exportTime = exportTime;
        this.duration = duration;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public int getLogCount() {
        return logCount;
    }

    public void setLogCount(int logCount) {
        this.logCount = logCount;
    }

    public List<String> getLogLevels() {
        return logLevels;
    }

    public void setLogLevels(List<String> logLevels) {
        this.logLevels = logLevels;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getExportTime() {
        return exportTime;
    }

    public void setExportTime(Date exportTime) {
        this.exportTime = exportTime;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}