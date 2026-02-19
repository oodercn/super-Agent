
package net.ooder.sdk.api.skill;

import java.util.List;

public interface SceneDependencyResolver {
    
    List<SceneDependency> resolve(SkillManifest manifest);
    
    boolean checkDependencySatisfied(String sceneName);
    
    List<SceneDependency> getUnsatisfiedDependencies();
    
    List<String> getDependencyOrder();
    
    void addDependencyListener(DependencyListener listener);
    
    void removeDependencyListener(DependencyListener listener);
    
    interface DependencyListener {
        void onDependencyResolved(String sceneName);
        void onDependencyFailed(String sceneName, String reason);
    }
}
