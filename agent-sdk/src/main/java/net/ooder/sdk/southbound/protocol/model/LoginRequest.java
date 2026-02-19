package net.ooder.sdk.southbound.protocol.model;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class LoginRequest {
    
    private String username;
    private String password;
    private String domain;
    private boolean rememberMe;
    private Map<String, Object> extra;
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getDomain() { return domain; }
    public void setDomain(String domain) { this.domain = domain; }
    
    public boolean isRememberMe() { return rememberMe; }
    public void setRememberMe(boolean rememberMe) { this.rememberMe = rememberMe; }
    
    public Map<String, Object> getExtra() { return extra; }
    public void setExtra(Map<String, Object> extra) { this.extra = extra; }
}
