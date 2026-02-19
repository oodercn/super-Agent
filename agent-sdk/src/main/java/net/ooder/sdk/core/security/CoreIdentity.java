package net.ooder.sdk.core.security;

import java.util.Map;

public interface CoreIdentity {
    
    String getIdentityId();
    
    String getName();
    
    IdentityType getType();
    
    Map<String, Object> getAttributes();
    
    void setAttribute(String key, Object value);
    
    Object getAttribute(String key);
    
    boolean hasAttribute(String key);
}
