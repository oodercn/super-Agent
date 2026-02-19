package net.ooder.sdk.api.skill;

import java.util.ArrayList;
import java.util.List;

public class DependencyResult {
    
    private String skillId;
    private boolean success;
    private int totalCount;
    private int installedCount;
    private int skippedCount;
    private int failedCount;
    private List<DependencyItemResult> items = new ArrayList<DependencyItemResult>();
    private String errorMessage;
    private long processingTime;
    
    public DependencyResult() {
        this.success = true;
    }
    
    public DependencyResult(String skillId) {
        this();
        this.skillId = skillId;
    }
    
    public String getSkillId() { return skillId; }
    public void setSkillId(String skillId) { this.skillId = skillId; }
    
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    
    public int getTotalCount() { return totalCount; }
    public void setTotalCount(int totalCount) { this.totalCount = totalCount; }
    
    public int getInstalledCount() { return installedCount; }
    public void setInstalledCount(int installedCount) { this.installedCount = installedCount; }
    
    public int getSkippedCount() { return skippedCount; }
    public void setSkippedCount(int skippedCount) { this.skippedCount = skippedCount; }
    
    public int getFailedCount() { return failedCount; }
    public void setFailedCount(int failedCount) { this.failedCount = failedCount; }
    
    public List<DependencyItemResult> getItems() { return items; }
    public void setItems(List<DependencyItemResult> items) { this.items = items; }
    
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    
    public long getProcessingTime() { return processingTime; }
    public void setProcessingTime(long processingTime) { this.processingTime = processingTime; }
    
    public void incrementInstalled() { installedCount++; }
    public void incrementSkipped() { skippedCount++; }
    public void incrementFailed() { failedCount++; }
    
    public void addItem(DependencyItemResult item) { items.add(item); }
    
    public boolean isAllInstalled() { return failedCount == 0 && skippedCount == 0; }
    
    public boolean hasFailures() { return failedCount > 0; }
    
    public static class DependencyItemResult {
        private String dependencyId;
        private String name;
        private String version;
        private DependencyAction action;
        private boolean success;
        private String message;
        private Throwable error;
        
        public enum DependencyAction {
            INSTALLED,
            UPDATED,
            SKIPPED,
            FAILED
        }
        
        public DependencyItemResult() {}
        
        public DependencyItemResult(String dependencyId, String name, String version) {
            this.dependencyId = dependencyId;
            this.name = name;
            this.version = version;
        }
        
        public String getDependencyId() { return dependencyId; }
        public void setDependencyId(String dependencyId) { this.dependencyId = dependencyId; }
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getVersion() { return version; }
        public void setVersion(String version) { this.version = version; }
        
        public DependencyAction getAction() { return action; }
        public void setAction(DependencyAction action) { this.action = action; }
        
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        
        public Throwable getError() { return error; }
        public void setError(Throwable error) { this.error = error; }
    }
}
