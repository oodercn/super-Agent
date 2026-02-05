package net.ooder.skillcenter.model;

import java.util.Map;

public class SkillResult {
    private Map<String, Object> data;
    private boolean success;
    private String message;
    private String executionId;
    private String status;
    private Exception exception;
    
    public SkillResult() {
        this.data = new java.util.HashMap<>();
        this.success = false;
        this.message = "";
        this.executionId = "";
        this.status = "";
        this.exception = null;
    }
    
    public SkillResult(String status, String message) {
        this();
        this.status = status;
        this.message = message;
    }
    
    public Map<String, Object> getData() {
        return data;
    }
    
    public void setData(Map<String, Object> data) {
        this.data = data;
    }
    
    public void addData(String key, Object value) {
        if (data == null) {
            data = new java.util.HashMap<>();
        }
        data.put(key, value);
    }
    
    public void addData(String key, String value) {
        addData(key, (Object)value);
    }
    
    public void addData(String key, int value) {
        addData(key, (Object)value);
    }
    
    public void addData(String key, Map<String, Object> value) {
        addData(key, (Object)value);
    }
    
    public Object getData(String key) {
        if (data == null) {
            return null;
        }
        return data.get(key);
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getExecutionId() {
        return executionId;
    }
    
    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Exception getException() {
        return exception;
    }
    
    public void setException(Exception exception) {
        this.exception = exception;
    }
    
    public void setException(net.ooder.skillcenter.model.SkillException exception) {
        this.exception = exception;
    }
    
    public static class Status {
        public static final String SUCCESS = "success";
        public static final String FAILED = "failed";
        public static final String PENDING = "pending";
        public static final String RUNNING = "running";
        public static final String CANCELLED = "cancelled";
    }
}
