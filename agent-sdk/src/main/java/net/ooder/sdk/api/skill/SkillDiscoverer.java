
package net.ooder.sdk.api.skill;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import net.ooder.sdk.common.enums.DiscoveryMethod;

public interface SkillDiscoverer {
    
    CompletableFuture<List<SkillPackage>> discover();
    
    CompletableFuture<SkillPackage> discover(String skillId);
    
    CompletableFuture<List<SkillPackage>> discoverByScene(String sceneId);
    
    CompletableFuture<List<SkillPackage>> search(String query);
    
    CompletableFuture<List<SkillPackage>> searchByCapability(String capabilityId);
    
    DiscoveryMethod getMethod();
    
    boolean isAvailable();
    
    void setTimeout(long timeoutMs);
    
    long getTimeout();
    
    void setFilter(DiscoveryFilter filter);
    
    DiscoveryFilter getFilter();
    
    class DiscoveryFilter {
        private String sceneId;
        private String version;
        private List<String> capabilities;
        private Map<String, String> labels;
        
        public String getSceneId() { return sceneId; }
        public void setSceneId(String sceneId) { this.sceneId = sceneId; }
        public String getVersion() { return version; }
        public void setVersion(String version) { this.version = version; }
        public List<String> getCapabilities() { return capabilities; }
        public void setCapabilities(List<String> capabilities) { this.capabilities = capabilities; }
        public Map<String, String> getLabels() { return labels; }
        public void setLabels(Map<String, String> labels) { this.labels = labels; }
    }
}
