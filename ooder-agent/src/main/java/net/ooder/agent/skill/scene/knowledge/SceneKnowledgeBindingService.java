package net.ooder.mvp.skill.scene.knowledge;

import net.ooder.scene.skill.knowledge.KnowledgeBaseService;
import net.ooder.scene.skill.knowledge.KnowledgeBase;
import net.ooder.scene.skill.knowledge.KnowledgeSearchRequest;
import net.ooder.scene.skill.knowledge.KnowledgeSearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class SceneKnowledgeBindingService {

    private static final Logger log = LoggerFactory.getLogger(SceneKnowledgeBindingService.class);

    @Autowired(required = false)
    private KnowledgeBaseService knowledgeBaseService;

    private final Map<String, List<KnowledgeBinding>> sceneBindings = new ConcurrentHashMap<>();
    private final Map<String, KnowledgeLayerConfig> layerConfigs = new ConcurrentHashMap<>();

    public static class KnowledgeBinding {
        private String kbId;
        private String kbName;
        private String layer;
        private int priority;
        private boolean enabled;

        public String getKbId() { return kbId; }
        public void setKbId(String kbId) { this.kbId = kbId; }
        public String getKbName() { return kbName; }
        public void setKbName(String kbName) { this.kbName = kbName; }
        public String getLayer() { return layer; }
        public void setLayer(String layer) { this.layer = layer; }
        public int getPriority() { return priority; }
        public void setPriority(int priority) { this.priority = priority; }
        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }
    }

    public static class KnowledgeLayerConfig {
        private int topK = 5;
        private double threshold = 0.7;
        private boolean crossLayerSearch = true;
        private List<String> searchLayers = Arrays.asList("SCENE", "PROFESSIONAL", "GENERAL");

        public int getTopK() { return topK; }
        public void setTopK(int topK) { this.topK = topK; }
        public double getThreshold() { return threshold; }
        public void setThreshold(double threshold) { this.threshold = threshold; }
        public boolean isCrossLayerSearch() { return crossLayerSearch; }
        public void setCrossLayerSearch(boolean crossLayerSearch) { this.crossLayerSearch = crossLayerSearch; }
        public List<String> getSearchLayers() { return searchLayers; }
        public void setSearchLayers(List<String> searchLayers) { this.searchLayers = searchLayers; }
    }

    public void bindToScene(String sceneGroupId, String kbId, String layer, int priority) {
        log.info("[bindToScene] Binding kb {} to scene {} at layer {} with priority {}", 
            kbId, sceneGroupId, layer, priority);
        
        List<KnowledgeBinding> bindings = sceneBindings.computeIfAbsent(sceneGroupId, k -> new ArrayList<>());
        
        for (KnowledgeBinding binding : bindings) {
            if (binding.getKbId().equals(kbId) && binding.getLayer().equals(layer)) {
                log.warn("[bindToScene] Knowledge base already bound to this layer");
                return;
            }
        }
        
        KnowledgeBinding binding = new KnowledgeBinding();
        binding.setKbId(kbId);
        binding.setLayer(layer);
        binding.setPriority(priority);
        binding.setEnabled(true);
        
        if (knowledgeBaseService != null) {
            try {
                KnowledgeBase kb = knowledgeBaseService.get(kbId);
                if (kb != null) {
                    binding.setKbName(kb.getName());
                }
            } catch (Exception e) {
                log.warn("[bindToScene] Failed to get kb name: {}", e.getMessage());
            }
        }
        
        bindings.add(binding);
        bindings.sort(Comparator.comparingInt(KnowledgeBinding::getPriority));
        
        log.info("[bindToScene] Successfully bound kb {} to scene {}", kbId, sceneGroupId);
    }

    public void unbindFromScene(String sceneGroupId, String kbId, String layer) {
        log.info("[unbindFromScene] Unbinding kb {} from scene {} at layer {}", kbId, sceneGroupId, layer);
        
        List<KnowledgeBinding> bindings = sceneBindings.get(sceneGroupId);
        if (bindings != null) {
            bindings.removeIf(b -> b.getKbId().equals(kbId) && b.getLayer().equals(layer));
            log.info("[unbindFromScene] Successfully unbound kb {} from scene {}", kbId, sceneGroupId);
        }
    }

    public List<KnowledgeBinding> getBindings(String sceneGroupId) {
        return new ArrayList<>(sceneBindings.getOrDefault(sceneGroupId, new ArrayList<>()));
    }

    public List<Map<String, Object>> searchKnowledge(String sceneGroupId, String query, int topK) {
        log.info("[searchKnowledge] Searching in scene {} for: {}", sceneGroupId, query);
        
        List<Map<String, Object>> results = new ArrayList<>();
        
        List<KnowledgeBinding> bindings = getBindings(sceneGroupId);
        if (bindings.isEmpty()) {
            log.info("[searchKnowledge] No knowledge bindings for scene {}", sceneGroupId);
            return results;
        }
        
        KnowledgeLayerConfig config = layerConfigs.getOrDefault(sceneGroupId, new KnowledgeLayerConfig());
        int actualTopK = topK > 0 ? topK : config.getTopK();
        
        for (KnowledgeBinding binding : bindings) {
            if (!binding.isEnabled()) {
                continue;
            }
            
            if (knowledgeBaseService == null) {
                log.warn("[searchKnowledge] KnowledgeBaseService not available");
                continue;
            }
            
            try {
                KnowledgeSearchRequest request = new KnowledgeSearchRequest();
                request.setQuery(query);
                request.setTopK(actualTopK);
                request.setThreshold((float) config.getThreshold());
                
                List<KnowledgeSearchResult> kbResults = knowledgeBaseService.search(binding.getKbId(), request);
                
                for (KnowledgeSearchResult kr : kbResults) {
                    Map<String, Object> result = new HashMap<>();
                    result.put("content", kr.getContent());
                    result.put("score", kr.getScore());
                    result.put("docId", kr.getDocId());
                    result.put("title", kr.getTitle());
                    result.put("kbId", binding.getKbId());
                    result.put("kbName", binding.getKbName());
                    result.put("layer", binding.getLayer());
                    result.put("priority", binding.getPriority());
                    results.add(result);
                }
            } catch (Exception e) {
                log.error("[searchKnowledge] Error searching kb {}: {}", binding.getKbId(), e.getMessage());
            }
        }
        
        results.sort((a, b) -> {
            Double scoreA = (Double) a.get("score");
            Double scoreB = (Double) b.get("score");
            return Double.compare(scoreB != null ? scoreB : 0, scoreA != null ? scoreA : 0);
        });
        
        if (results.size() > actualTopK) {
            results = results.subList(0, actualTopK);
        }
        
        log.info("[searchKnowledge] Found {} results for query in scene {}", results.size(), sceneGroupId);
        return results;
    }

    public List<Map<String, Object>> crossLayerSearch(String sceneGroupId, String query, 
                                                       List<String> layers, int topK) {
        log.info("[crossLayerSearch] Cross-layer search in scene {} for: {}", sceneGroupId, query);
        
        List<Map<String, Object>> allResults = searchKnowledge(sceneGroupId, query, topK * 2);
        
        if (layers != null && !layers.isEmpty()) {
            allResults = allResults.stream()
                .filter(r -> layers.contains(r.get("layer")))
                .collect(Collectors.toList());
        }
        
        if (allResults.size() > topK) {
            allResults = allResults.subList(0, topK);
        }
        
        return allResults;
    }

    public void setLayerConfig(String sceneGroupId, KnowledgeLayerConfig config) {
        layerConfigs.put(sceneGroupId, config);
        log.info("[setLayerConfig] Set layer config for scene {}", sceneGroupId);
    }

    public KnowledgeLayerConfig getLayerConfig(String sceneGroupId) {
        KnowledgeLayerConfig config = layerConfigs.get(sceneGroupId);
        if (config == null) {
            config = new KnowledgeLayerConfig();
            log.info("[getLayerConfig] Using default layer config for new scene group: {}", sceneGroupId);
        }
        return config;
    }
    
    public void initDefaultBindingsForScene(String sceneGroupId) {
        if (!sceneBindings.containsKey(sceneGroupId)) {
            sceneBindings.put(sceneGroupId, new ArrayList<>());
            log.info("[initDefaultBindingsForScene] Initialized empty bindings for scene: {}", sceneGroupId);
        }
        if (!layerConfigs.containsKey(sceneGroupId)) {
            layerConfigs.put(sceneGroupId, new KnowledgeLayerConfig());
            log.info("[initDefaultBindingsForScene] Initialized default config for scene: {}", sceneGroupId);
        }
    }
}
