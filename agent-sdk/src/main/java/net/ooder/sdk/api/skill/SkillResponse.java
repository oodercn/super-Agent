package net.ooder.sdk.api.skill;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SkillResponse implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String requestId;
    private String skillId;
    private boolean success;
    private Object result;
    private String errorCode;
    private String errorMessage;
    private Map<String, Object> metadata;
    private long startTime;
    private long endTime;
    private int retryCount;
    
    public SkillResponse() {
        this.metadata = new HashMap<>();
        this.startTime = System.currentTimeMillis();
    }
    
    public static SkillResponse success(String requestId, Object result) {
        SkillResponse response = new SkillResponse();
        response.setRequestId(requestId);
        response.setSuccess(true);
        response.setResult(result);
        response.setEndTime(System.currentTimeMillis());
        return response;
    }
    
    public static SkillResponse error(String requestId, String errorCode, String errorMessage) {
        SkillResponse response = new SkillResponse();
        response.setRequestId(requestId);
        response.setSuccess(false);
        response.setErrorCode(errorCode);
        response.setErrorMessage(errorMessage);
        response.setEndTime(System.currentTimeMillis());
        return response;
    }
    
    public static SkillResponse error(String requestId, String errorCode, String errorMessage, Throwable cause) {
        SkillResponse response = error(requestId, errorCode, errorMessage);
        if (cause != null) {
            response.getMetadata().put("exception", cause.getClass().getName());
            response.getMetadata().put("exceptionMessage", cause.getMessage());
        }
        return response;
    }
    
    public static SkillResponse timeout(String requestId) {
        SkillResponse response = new SkillResponse();
        response.setRequestId(requestId);
        response.setSuccess(false);
        response.setErrorCode("TIMEOUT");
        response.setErrorMessage("Request timed out");
        response.setEndTime(System.currentTimeMillis());
        return response;
    }
    
    public String getRequestId() {
        return requestId;
    }
    
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
    
    public String getSkillId() {
        return skillId;
    }
    
    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public Object getResult() {
        return result;
    }
    
    public void setResult(Object result) {
        this.result = result;
    }
    
    @SuppressWarnings("unchecked")
    public <T> T getResultAs(Class<T> type) {
        if (result != null && type.isInstance(result)) {
            return (T) result;
        }
        return null;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    public Map<String, Object> getMetadata() {
        return metadata;
    }
    
    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata != null ? metadata : new HashMap<>();
    }
    
    public Object getMetadata(String key) {
        return metadata.get(key);
    }
    
    public void setMetadata(String key, Object value) {
        this.metadata.put(key, value);
    }
    
    public long getStartTime() {
        return startTime;
    }
    
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
    
    public long getEndTime() {
        return endTime;
    }
    
    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
    
    public long getDuration() {
        return endTime - startTime;
    }
    
    public int getRetryCount() {
        return retryCount;
    }
    
    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }
    
    public boolean isTimeout() {
        return "TIMEOUT".equals(errorCode);
    }
    
    public boolean hasError() {
        return !success;
    }
    
    @Override
    public String toString() {
        return "SkillResponse{" +
                "requestId='" + requestId + '\'' +
                ", success=" + success +
                ", errorCode='" + errorCode + '\'' +
                ", duration=" + getDuration() + "ms" +
                '}';
    }
}
