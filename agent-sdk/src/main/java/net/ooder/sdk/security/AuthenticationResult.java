package net.ooder.sdk.security;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Map;

public class AuthenticationResult {
    @JSONField(name = "success")
    private boolean success;
    
    @JSONField(name = "principal")
    private String principal;
    
    @JSONField(name = "token")
    private String token;
    
    @JSONField(name = "expiresAt")
    private Long expiresAt;
    
    @JSONField(name = "claims")
    private Map<String, Object> claims;
    
    @JSONField(name = "error")
    private String error;
    
    @JSONField(name = "errorCode")
    private String errorCode;
    
    @JSONField(name = "details")
    private Map<String, Object> details;
    
    public AuthenticationResult() {
    }
    
    public AuthenticationResult(boolean success) {
        this.success = success;
    }
    
    public AuthenticationResult(boolean success, String principal) {
        this(success);
        this.principal = principal;
    }
    
    public AuthenticationResult(boolean success, String principal, String token) {
        this(success, principal);
        this.token = token;
    }
    
    public AuthenticationResult(boolean success, String principal, String token, Map<String, Object> claims) {
        this(success, principal, token);
        this.claims = claims;
    }
    
    public AuthenticationResult(boolean success, String error, String errorCode, String errorDetails) {
        this(success);
        this.error = error;
        this.errorCode = errorCode;
        // 可以将errorDetails存储到details中
        if (errorDetails != null) {
            if (this.details == null) {
                this.details = new java.util.HashMap<>();
            }
            this.details.put("errorDetails", errorDetails);
        }
    }
    
    // 为了保持向后兼容，添加一个静态方法来创建错误结果
    public static AuthenticationResult errorResult(boolean success, String error, String errorCode) {
        AuthenticationResult result = new AuthenticationResult(success);
        result.setError(error);
        result.setErrorCode(errorCode);
        return result;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Long expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Map<String, Object> getClaims() {
        return claims;
    }

    public void setClaims(Map<String, Object> claims) {
        this.claims = claims;
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

    @Override
    public String toString() {
        return "AuthenticationResult{" +
                "success=" + success +
                ", principal='" + principal + '\'' +
                ", token='" + token + '\'' +
                ", error='" + error + '\'' +
                ", errorCode='" + errorCode + '\'' +
                '}';
    }
}
