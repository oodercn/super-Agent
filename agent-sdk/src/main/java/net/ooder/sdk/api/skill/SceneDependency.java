
package net.ooder.sdk.api.skill;

import java.util.List;
import java.util.Map;

public interface SceneDependency {
    
    String getSceneName();
    
    String getDescription();
    
    boolean isRequired();
    
    List<String> getRequiredCapabilities();
    
    Map<String, Object> getConfig();
    
    DependencyStatus getStatus();
    
    void setStatus(DependencyStatus status);
    
    enum DependencyStatus {
        PENDING,
        RESOLVING,
        RESOLVED,
        FAILED
    }
}
