package net.ooder.nexus.domain.sync.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 同步任务实体类
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SyncTask {
    
    private String id;
    private String name;
    private String type; // upload, download, bidirectional
    private String status; // pending, running, completed, failed, cancelled
    private String source;
    private String target;
    private int totalItems;
    private int processedItems;
    private int failedItems;
    private String errorMessage;
    private LocalDateTime createTime;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private List<SyncItem> items;
    
    public SyncTask() {
        this.createTime = LocalDateTime.now();
        this.status = "pending";
        this.totalItems = 0;
        this.processedItems = 0;
        this.failedItems = 0;
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getSource() {
        return source;
    }
    
    public void setSource(String source) {
        this.source = source;
    }
    
    public String getTarget() {
        return target;
    }
    
    public void setTarget(String target) {
        this.target = target;
    }
    
    public int getTotalItems() {
        return totalItems;
    }
    
    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }
    
    public int getProcessedItems() {
        return processedItems;
    }
    
    public void setProcessedItems(int processedItems) {
        this.processedItems = processedItems;
    }
    
    public int getFailedItems() {
        return failedItems;
    }
    
    public void setFailedItems(int failedItems) {
        this.failedItems = failedItems;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    
    public LocalDateTime getStartTime() {
        return startTime;
    }
    
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
    
    public LocalDateTime getEndTime() {
        return endTime;
    }
    
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
    
    public List<SyncItem> getItems() {
        return items;
    }
    
    public void setItems(List<SyncItem> items) {
        this.items = items;
    }
    
    public double getProgress() {
        return totalItems > 0 ? (double) processedItems / totalItems * 100 : 0;
    }
    
    /**
     * 同步项
     */
    public static class SyncItem {
        private String id;
        private String name;
        private String type; // skill, category, config
        private String status; // pending, syncing, completed, failed
        private String errorMessage;
        private LocalDateTime syncTime;
        
        public String getId() {
            return id;
        }
        
        public void setId(String id) {
            this.id = id;
        }
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public String getType() {
            return type;
        }
        
        public void setType(String type) {
            this.type = type;
        }
        
        public String getStatus() {
            return status;
        }
        
        public void setStatus(String status) {
            this.status = status;
        }
        
        public String getErrorMessage() {
            return errorMessage;
        }
        
        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }
        
        public LocalDateTime getSyncTime() {
            return syncTime;
        }
        
        public void setSyncTime(LocalDateTime syncTime) {
            this.syncTime = syncTime;
        }
    }
}
