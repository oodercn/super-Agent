package net.ooder.sdk.api.skill;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SkillRequest implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String requestId;
    private String skillId;
    private String skillType;
    private String operation;
    private Map<String, Object> params;
    private Map<String, String> headers;
    private long timestamp;
    private int timeout;
    private int retryCount;
    private String traceId;
    private String sourceId;
    
    public SkillRequest() {
        this.params = new HashMap<>();
        this.headers = new HashMap<>();
        this.timestamp = System.currentTimeMillis();
        this.timeout = 30000;
        this.retryCount = 0;
    }
    
    public static SkillRequest create() {
        SkillRequest request = new SkillRequest();
        request.setRequestId(generateRequestId());
        return request;
    }
    
    public static SkillRequest create(String skillType, String operation) {
        SkillRequest request = create();
        request.setSkillType(skillType);
        request.setOperation(operation);
        return request;
    }
    
    public static SkillRequest create(String skillId, String skillType, String operation) {
        SkillRequest request = create(skillType, operation);
        request.setSkillId(skillId);
        return request;
    }
    
    private static String generateRequestId() {
        return "req_" + System.currentTimeMillis() + "_" + UUID.randomUUID().toString().substring(0, 8);
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
    
    public String getSkillType() {
        return skillType;
    }
    
    public void setSkillType(String skillType) {
        this.skillType = skillType;
    }
    
    public String getOperation() {
        return operation;
    }
    
    public void setOperation(String operation) {
        this.operation = operation;
    }
    
    public Map<String, Object> getParams() {
        return params;
    }
    
    public void setParams(Map<String, Object> params) {
        this.params = params != null ? params : new HashMap<>();
    }
    
    public Object getParam(String key) {
        return params.get(key);
    }
    
    public String getParamString(String key) {
        Object value = params.get(key);
        return value != null ? value.toString() : null;
    }
    
    public SkillRequest param(String key, Object value) {
        this.params.put(key, value);
        return this;
    }
    
    public Map<String, String> getHeaders() {
        return headers;
    }
    
    public void setHeaders(Map<String, String> headers) {
        this.headers = headers != null ? headers : new HashMap<>();
    }
    
    public String getHeader(String key) {
        return headers.get(key);
    }
    
    public SkillRequest header(String key, String value) {
        this.headers.put(key, value);
        return this;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    
    public int getTimeout() {
        return timeout;
    }
    
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
    
    public SkillRequest timeout(int timeout) {
        this.timeout = timeout;
        return this;
    }
    
    public int getRetryCount() {
        return retryCount;
    }
    
    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }
    
    public void incrementRetry() {
        this.retryCount++;
    }
    
    public String getTraceId() {
        return traceId;
    }
    
    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }
    
    public SkillRequest traceId(String traceId) {
        this.traceId = traceId;
        return this;
    }
    
    public String getSourceId() {
        return sourceId;
    }
    
    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }
    
    public SkillRequest sourceId(String sourceId) {
        this.sourceId = sourceId;
        return this;
    }
    
    public boolean isExpired() {
        return System.currentTimeMillis() > (timestamp + timeout);
    }
    
    public long getElapsedTime() {
        return System.currentTimeMillis() - timestamp;
    }
    
    @Override
    public String toString() {
        return "SkillRequest{" +
                "requestId='" + requestId + '\'' +
                ", skillId='" + skillId + '\'' +
                ", skillType='" + skillType + '\'' +
                ", operation='" + operation + '\'' +
                '}';
    }
}
