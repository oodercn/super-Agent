package net.ooder.sdk.api.protocol;

import java.util.ArrayList;
import java.util.List;

public class BatchCommandResult {
    
    private int totalCount;
    private int successCount;
    private int failedCount;
    private List<String> failedPacketIds = new ArrayList<String>();
    private long processingTime;
    
    public int getTotalCount() { return totalCount; }
    public void setTotalCount(int totalCount) { this.totalCount = totalCount; }
    
    public int getSuccessCount() { return successCount; }
    public void setSuccessCount(int successCount) { this.successCount = successCount; }
    
    public int getFailedCount() { return failedCount; }
    public void setFailedCount(int failedCount) { this.failedCount = failedCount; }
    
    public List<String> getFailedPacketIds() { return failedPacketIds; }
    public void setFailedPacketIds(List<String> failedPacketIds) { this.failedPacketIds = failedPacketIds; }
    
    public long getProcessingTime() { return processingTime; }
    public void setProcessingTime(long processingTime) { this.processingTime = processingTime; }
    
    public void incrementSuccess() { successCount++; }
    
    public void incrementFailed() { failedCount++; }
    
    public void addFailedPacketId(String packetId) { failedPacketIds.add(packetId); }
    
    public boolean isAllSuccess() { return failedCount == 0; }
    
    public double getSuccessRate() {
        return totalCount > 0 ? (double) successCount / totalCount : 0.0;
    }
}
