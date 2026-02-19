
package net.ooder.sdk.service.discovery;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ooder.sdk.api.skill.SkillDiscoverer;
import net.ooder.sdk.api.skill.SkillPackage;
import net.ooder.sdk.common.enums.DiscoveryMethod;
import net.ooder.sdk.core.skill.discovery.LocalDiscoverer;
import net.ooder.sdk.core.skill.discovery.SkillCenterDiscoverer;
import net.ooder.sdk.core.skill.discovery.UdpDiscoverer;

public class DiscoveryService {
    
    private static final Logger log = LoggerFactory.getLogger(DiscoveryService.class);
    
    private final Map<DiscoveryMethod, SkillDiscoverer> discoverers;
    private DiscoveryMethod defaultMethod = DiscoveryMethod.LOCAL_FS;
    
    public DiscoveryService() {
        this.discoverers = new ConcurrentHashMap<>();
        initializeDiscoverers();
    }
    
    private void initializeDiscoverers() {
        discoverers.put(DiscoveryMethod.LOCAL_FS, new LocalDiscoverer());
        discoverers.put(DiscoveryMethod.UDP_BROADCAST, new UdpDiscoverer());
        discoverers.put(DiscoveryMethod.SKILL_CENTER, new SkillCenterDiscoverer());
    }
    
    public void registerDiscoverer(DiscoveryMethod method, SkillDiscoverer discoverer) {
        discoverers.put(method, discoverer);
        log.info("Registered discoverer for method: {}", method);
    }
    
    public void unregisterDiscoverer(DiscoveryMethod method) {
        discoverers.remove(method);
        log.info("Unregistered discoverer for method: {}", method);
    }
    
    public SkillDiscoverer getDiscoverer(DiscoveryMethod method) {
        return discoverers.get(method);
    }
    
    public CompletableFuture<List<SkillPackage>> discoverAll() {
        return discoverAll(defaultMethod);
    }
    
    public CompletableFuture<List<SkillPackage>> discoverAll(DiscoveryMethod method) {
        log.info("Discovering all skills via {}", method);
        SkillDiscoverer discoverer = discoverers.get(method);
        if (discoverer == null) {
            log.warn("Discoverer not available for method: {}", method);
            return CompletableFuture.completedFuture(new ArrayList<>());
        }
        return discoverer.discover();
    }
    
    public CompletableFuture<SkillPackage> discover(String skillId) {
        return discover(skillId, defaultMethod);
    }
    
    public CompletableFuture<SkillPackage> discover(String skillId, DiscoveryMethod method) {
        log.info("Discovering skill {} via {}", skillId, method);
        SkillDiscoverer discoverer = discoverers.get(method);
        if (discoverer == null) {
            log.warn("Discoverer not available for method: {}", method);
            return CompletableFuture.completedFuture(null);
        }
        return discoverer.discover(skillId);
    }
    
    public CompletableFuture<List<SkillPackage>> discoverByScene(String sceneId) {
        return discoverByScene(sceneId, defaultMethod);
    }
    
    public CompletableFuture<List<SkillPackage>> discoverByScene(String sceneId, DiscoveryMethod method) {
        log.info("Discovering skills for scene {} via {}", sceneId, method);
        SkillDiscoverer discoverer = discoverers.get(method);
        if (discoverer == null) {
            return CompletableFuture.completedFuture(new ArrayList<>());
        }
        return discoverer.discoverByScene(sceneId);
    }
    
    public CompletableFuture<List<SkillPackage>> search(String query) {
        return search(query, defaultMethod);
    }
    
    public CompletableFuture<List<SkillPackage>> search(String query, DiscoveryMethod method) {
        log.info("Searching skills with query '{}' via {}", query, method);
        SkillDiscoverer discoverer = discoverers.get(method);
        if (discoverer == null) {
            return CompletableFuture.completedFuture(new ArrayList<>());
        }
        return discoverer.search(query);
    }
    
    public CompletableFuture<List<SkillPackage>> searchByCapability(String capabilityId) {
        return searchByCapability(capabilityId, defaultMethod);
    }
    
    public CompletableFuture<List<SkillPackage>> searchByCapability(String capabilityId, DiscoveryMethod method) {
        log.info("Searching skills by capability {} via {}", capabilityId, method);
        SkillDiscoverer discoverer = discoverers.get(method);
        if (discoverer == null) {
            return CompletableFuture.completedFuture(new ArrayList<>());
        }
        return discoverer.searchByCapability(capabilityId);
    }
    
    public boolean isMethodAvailable(DiscoveryMethod method) {
        SkillDiscoverer discoverer = discoverers.get(method);
        return discoverer != null && discoverer.isAvailable();
    }
    
    public List<DiscoveryMethod> getAvailableMethods() {
        List<DiscoveryMethod> available = new ArrayList<>();
        for (Map.Entry<DiscoveryMethod, SkillDiscoverer> entry : discoverers.entrySet()) {
            if (entry.getValue().isAvailable()) {
                available.add(entry.getKey());
            }
        }
        return available;
    }
    
    public void setDefaultMethod(DiscoveryMethod method) {
        this.defaultMethod = method;
        log.info("Default discovery method set to: {}", method);
    }
    
    public DiscoveryMethod getDefaultMethod() {
        return defaultMethod;
    }
    
    public void setSkillCenterEndpoint(String endpoint) {
        SkillDiscoverer discoverer = discoverers.get(DiscoveryMethod.SKILL_CENTER);
        if (discoverer instanceof SkillCenterDiscoverer) {
            ((SkillCenterDiscoverer) discoverer).setEndpoint(endpoint);
            log.info("SkillCenter endpoint set to: {}", endpoint);
        }
    }
}
