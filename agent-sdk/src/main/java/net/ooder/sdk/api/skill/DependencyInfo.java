
package net.ooder.sdk.api.skill;

import java.util.List;

public interface DependencyInfo {
    
    String getSkillId();
    
    List<DependencyItem> getDependencies();
    
    List<DependencyItem> getMissing();
    
    List<DependencyItem> getOutdated();
    
    boolean isSatisfied();
}
