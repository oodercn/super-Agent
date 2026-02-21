
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
    
    CompletableFuture<List<SkillPackage>> discoverByCategory(String category);
    
    CompletableFuture<List<SkillPackage>> discoverByCategory(String category, String subCategory);
    
    CompletableFuture<List<SkillPackage>> searchByTags(List<String> tags);
    
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
        private String category;
        private String subCategory;
        private List<String> tags;
        
        public String getSceneId() { return sceneId; }
        public void setSceneId(String sceneId) { this.sceneId = sceneId; }
        public String getVersion() { return version; }
        public void setVersion(String version) { this.version = version; }
        public List<String> getCapabilities() { return capabilities; }
        public void setCapabilities(List<String> capabilities) { this.capabilities = capabilities; }
        public Map<String, String> getLabels() { return labels; }
        public void setLabels(Map<String, String> labels) { this.labels = labels; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public String getSubCategory() { return subCategory; }
        public void setSubCategory(String subCategory) { this.subCategory = subCategory; }
        public List<String> getTags() { return tags; }
        public void setTags(List<String> tags) { this.tags = tags; }
    }
}
