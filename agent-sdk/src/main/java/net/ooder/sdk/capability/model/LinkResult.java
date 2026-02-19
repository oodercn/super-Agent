package net.ooder.sdk.capability.model;

import java.util.Map;

public class LinkResult {
    
    private String linkId;
    private boolean success;
    private Map<String, Object> output;
    private String errorMessage;
    private long startTime;
    private long endTime;
    
    public String getLinkId() { return linkId; }
    public void setLinkId(String linkId) { this.linkId = linkId; }
    
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    
    public Map<String, Object> getOutput() { return output; }
    public void setOutput(Map<String, Object> output) { this.output = output; }
    
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    
    public long getStartTime() { return startTime; }
    public void setStartTime(long startTime) { this.startTime = startTime; }
    
    public long getEndTime() { return endTime; }
    public void setEndTime(long endTime) { this.endTime = endTime; }
    
    private int linkIndex;
    private long duration;
    
    public int getLinkIndex() { return linkIndex; }
    public void setLinkIndex(int linkIndex) { this.linkIndex = linkIndex; }
    
    public long getDuration() { return duration; }
    public void setDuration(long duration) { this.duration = duration; }
}
