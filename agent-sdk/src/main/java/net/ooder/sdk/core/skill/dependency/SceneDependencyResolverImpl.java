
package net.ooder.sdk.core.skill.dependency;

import net.ooder.sdk.api.skill.LocalCapabilityRepository;
import net.ooder.sdk.api.skill.SceneDependency;
import net.ooder.sdk.api.skill.SceneDependencyResolver;
import net.ooder.sdk.api.skill.SkillManifest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class SceneDependencyResolverImpl implements SceneDependencyResolver {
    
    private static final Logger log = LoggerFactory.getLogger(SceneDependencyResolverImpl.class);
    
    private final LocalCapabilityRepository capabilityRepository;
    private final Map<String, SceneDependency> dependencies = new ConcurrentHashMap<>();
    private final List<DependencyListener> listeners = new CopyOnWriteArrayList<>();
    
    public SceneDependencyResolverImpl() {
        this.capabilityRepository = null;
    }
    
    public SceneDependencyResolverImpl(LocalCapabilityRepository capabilityRepository) {
        this.capabilityRepository = capabilityRepository;
    }
    
    @Override
    public List<SceneDependency> resolve(SkillManifest manifest) {
        List<SceneDependency> result = new ArrayList<>();
        
        if (manifest == null) {
            return result;
        }
        
        List<String> collaborativeScenes = manifest.getCollaborativeScenes();
        if (collaborativeScenes != null) {
            for (String sceneName : collaborativeScenes) {
                SceneDependencyImpl dep = new SceneDependencyImpl(sceneName);
                dep.setRequired(true);
                dep.setStatus(SceneDependency.DependencyStatus.PENDING);
                dependencies.put(sceneName, dep);
                result.add(dep);
            }
        }
        
        List<SkillManifest.Dependency> deps = manifest.getDependencies();
        if (deps != null) {
            for (SkillManifest.Dependency d : deps) {
                SceneDependencyImpl dep = new SceneDependencyImpl(d.getSkillId());
                dep.setRequired(d.isRequired());
                dep.setStatus(SceneDependency.DependencyStatus.PENDING);
                dependencies.put(d.getSkillId(), dep);
                result.add(dep);
            }
        }
        
        log.info("Resolved {} dependencies for skill: {}", result.size(), manifest.getSkillId());
        return result;
    }
    
    @Override
    public boolean checkDependencySatisfied(String sceneName) {
        SceneDependency dep = dependencies.get(sceneName);
        if (dep == null) {
            return false;
        }
        
        if (capabilityRepository != null) {
            for (String capId : dep.getRequiredCapabilities()) {
                if (!capabilityRepository.hasCapability(capId)) {
                    return false;
                }
            }
        }
        
        dep.setStatus(SceneDependency.DependencyStatus.RESOLVED);
        notifyResolved(sceneName);
        return true;
    }
    
    @Override
    public List<SceneDependency> getUnsatisfiedDependencies() {
        List<SceneDependency> result = new ArrayList<>();
        for (SceneDependency dep : dependencies.values()) {
            if (dep.getStatus() != SceneDependency.DependencyStatus.RESOLVED) {
                result.add(dep);
            }
        }
        return result;
    }
    
    @Override
    public List<String> getDependencyOrder() {
        List<String> order = new ArrayList<>();
        
        List<SceneDependency> pending = new ArrayList<>();
        List<SceneDependency> resolved = new ArrayList<>();
        
        for (SceneDependency dep : dependencies.values()) {
            if (dep.getStatus() == SceneDependency.DependencyStatus.RESOLVED) {
                resolved.add(dep);
            } else {
                pending.add(dep);
            }
        }
        
        for (SceneDependency dep : resolved) {
            order.add(dep.getSceneName());
        }
        for (SceneDependency dep : pending) {
            order.add(dep.getSceneName());
        }
        
        return order;
    }
    
    @Override
    public void addDependencyListener(DependencyListener listener) {
        listeners.add(listener);
    }
    
    @Override
    public void removeDependencyListener(DependencyListener listener) {
        listeners.remove(listener);
    }
    
    private void notifyResolved(String sceneName) {
        for (DependencyListener listener : listeners) {
            try {
                listener.onDependencyResolved(sceneName);
            } catch (Exception e) {
                log.warn("DependencyListener error", e);
            }
        }
    }
    
    private void notifyFailed(String sceneName, String reason) {
        for (DependencyListener listener : listeners) {
            try {
                listener.onDependencyFailed(sceneName, reason);
            } catch (Exception e) {
                log.warn("DependencyListener error", e);
            }
        }
    }
}
