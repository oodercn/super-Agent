package net.ooder.sdk.core.transport;

import java.util.ArrayList;
import java.util.List;

public class BatchResult {
    
    private int totalCount;
    private int successCount;
    private int failedCount;
    private List<String> failedMessageIds = new ArrayList<String>();
    private long processingTime;
    
    public int getTotalCount() { return totalCount; }
    public void setTotalCount(int totalCount) { this.totalCount = totalCount; }
    
    public int getSuccessCount() { return successCount; }
    public void setSuccessCount(int successCount) { this.successCount = successCount; }
    
    public int getFailedCount() { return failedCount; }
    public void setFailedCount(int failedCount) { this.failedCount = failedCount; }
    
    public List<String> getFailedMessageIds() { return failedMessageIds; }
    public void setFailedMessageIds(List<String> failedMessageIds) { this.failedMessageIds = failedMessageIds; }
    
    public long getProcessingTime() { return processingTime; }
    public void setProcessingTime(long processingTime) { this.processingTime = processingTime; }
    
    public void incrementSuccess() { successCount++; }
    
    public void incrementFailed() { failedCount++; }
    
    public void addFailedMessageId(String messageId) { failedMessageIds.add(messageId); }
    
    public boolean isAllSuccess() { return failedCount == 0; }
    
    public double getSuccessRate() {
        return totalCount > 0 ? (double) successCount / totalCount : 0.0;
    }
}
