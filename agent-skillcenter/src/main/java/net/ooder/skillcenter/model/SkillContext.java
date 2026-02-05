package net.ooder.skillcenter.model;

import java.util.Map;

public class SkillContext {
    private Map<String, Object> params;
    private Map<String, Object> attributes;
    private long timeout;
    private String executionId;
    
    public Map<String, Object> getParams() {
        return params;
    }
    
    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
    
    public Map<String, Object> getAttributes() {
        return attributes;
    }
    
    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
    
    public Object getParameter(String key) {
        if (params == null) {
            return null;
        }
        return params.get(key);
    }
    
    public void addParameter(String key, Object value) {
        if (params == null) {
            params = new java.util.HashMap<>();
        }
        params.put(key, value);
    }
    
    public void setParameters(Map<String, Object> params) {
        this.params = params;
    }
    
    public void addAttribute(String key, Object value) {
        if (attributes == null) {
            attributes = new java.util.HashMap<>();
        }
        attributes.put(key, value);
    }
    
    public long getTimeout() {
        return timeout;
    }
    
    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }
    
    public String getExecutionId() {
        return executionId;
    }
    
    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }
}
