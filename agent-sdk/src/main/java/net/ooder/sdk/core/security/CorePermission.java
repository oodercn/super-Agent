package net.ooder.sdk.core.security;

import java.util.List;

public interface CorePermission {
    
    String getPermissionId();
    
    String getResource();
    
    String getAction();
    
    boolean check(CoreIdentity identity);
    
    List<String> getConditions();
    
    void addCondition(String condition);
    
    void removeCondition(String condition);
}
