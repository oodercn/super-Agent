package net.ooder.sdk.security;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Map;

public class AuthorizationResult {
    @JSONField(name = "granted")
    private boolean granted;
    
    @JSONField(name = "principal")
    private String principal;
    
    @JSONField(name = "resource")
    private String resource;
    
    @JSONField(name = "action")
    private String action;
    
    @JSONField(name = "error")
    private String error;
    
    @JSONField(name = "errorCode")
    private String errorCode;
    
    @JSONField(name = "details")
    private Map<String, Object> details;
    
    @JSONField(name = "policyId")
    private String policyId;
    
    @JSONField(name = "decisionTime")
    private long decisionTime;
    
    public AuthorizationResult() {
        this.decisionTime = System.currentTimeMillis();
    }
    
    public AuthorizationResult(boolean granted) {
        this();
        this.granted = granted;
    }
    
    public AuthorizationResult(boolean granted, String principal, String resource, String action) {
        this(granted);
        this.principal = principal;
        this.resource = resource;
        this.action = action;
    }
    
    public AuthorizationResult(boolean granted, String principal, String resource, String action, String error, String errorCode) {
        this(granted, principal, resource, action);
        this.error = error;
        this.errorCode = errorCode;
    }

    public boolean isGranted() {
        return granted;
    }

    public void setGranted(boolean granted) {
        this.granted = granted;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public Map<String, Object> getDetails() {
        return details;
    }

    public void setDetails(Map<String, Object> details) {
        this.details = details;
    }

    public String getPolicyId() {
        return policyId;
    }

    public void setPolicyId(String policyId) {
        this.policyId = policyId;
    }

    public long getDecisionTime() {
        return decisionTime;
    }

    public void setDecisionTime(long decisionTime) {
        this.decisionTime = decisionTime;
    }

    @Override
    public String toString() {
        return "AuthorizationResult{" +
                "granted=" + granted +
                ", principal='" + principal + '\'' +
                ", resource='" + resource + '\'' +
                ", action='" + action + '\'' +
                ", error='" + error + '\'' +
                ", errorCode='" + errorCode + '\'' +
                '}';
    }
}
