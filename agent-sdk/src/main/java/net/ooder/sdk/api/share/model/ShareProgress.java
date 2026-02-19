package net.ooder.sdk.api.share.model;

public class ShareProgress {
    
    private String shareId;
    private String status;
    private int progress;
    private long bytesTransferred;
    private long totalBytes;
    private long startTime;
    private long estimatedTimeRemaining;
    private String currentPhase;
    private String errorMessage;
    
    public String getShareId() { return shareId; }
    public void setShareId(String shareId) { this.shareId = shareId; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public int getProgress() { return progress; }
    public void setProgress(int progress) { this.progress = progress; }
    
    public long getBytesTransferred() { return bytesTransferred; }
    public void setBytesTransferred(long bytesTransferred) { this.bytesTransferred = bytesTransferred; }
    
    public long getTotalBytes() { return totalBytes; }
    public void setTotalBytes(long totalBytes) { this.totalBytes = totalBytes; }
    
    public long getStartTime() { return startTime; }
    public void setStartTime(long startTime) { this.startTime = startTime; }
    
    public long getEstimatedTimeRemaining() { return estimatedTimeRemaining; }
    public void setEstimatedTimeRemaining(long estimatedTimeRemaining) { this.estimatedTimeRemaining = estimatedTimeRemaining; }
    
    public String getCurrentPhase() { return currentPhase; }
    public void setCurrentPhase(String currentPhase) { this.currentPhase = currentPhase; }
    
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
}
