package net.ooder.enexus.config;

import net.ooder.scene.skill.knowledge.KnowledgeBinding;
import net.ooder.scene.skill.knowledge.KnowledgeBindingManager;
import net.ooder.scene.skill.knowledge.KnowledgeChunk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryKnowledgeBindingManager implements KnowledgeBindingManager {

    private static final Logger log = LoggerFactory.getLogger(InMemoryKnowledgeBindingManager.class);

    private final Map<String, List<KnowledgeBinding>> bindings = new ConcurrentHashMap<>();

    @Override
    public String bind(String sceneGroupId, KnowledgeBinding binding) {
        String bindingId = "kb-" + System.currentTimeMillis();
        binding.setBindingId(bindingId);
        binding.setSceneGroupId(sceneGroupId);
        
        bindings.computeIfAbsent(sceneGroupId, k -> new ArrayList<>()).add(binding);
        log.info("Bound knowledge base {} to scene group {}", binding.getKnowledgeBaseId(), sceneGroupId);
        return bindingId;
    }

    @Override
    public String bind(String sceneGroupId, String knowledgeBaseId, String knowledgeBaseName, String description) {
        KnowledgeBinding binding = new KnowledgeBinding();
        binding.setKnowledgeBaseId(knowledgeBaseId);
        binding.setKnowledgeBaseName(knowledgeBaseName);
        return bind(sceneGroupId, binding);
    }

    @Override
    public boolean unbind(String sceneGroupId, String knowledgeBaseId) {
        List<KnowledgeBinding> groupBindings = bindings.get(sceneGroupId);
        if (groupBindings == null) {
            return false;
        }
        boolean removed = groupBindings.removeIf(b -> knowledgeBaseId.equals(b.getKnowledgeBaseId()));
        if (removed) {
            log.info("Unbound knowledge base {} from scene group {}", knowledgeBaseId, sceneGroupId);
        }
        return removed;
    }

    @Override
    public List<KnowledgeBinding> getBindings(String sceneGroupId) {
        return bindings.getOrDefault(sceneGroupId, Collections.emptyList());
    }

    @Override
    public KnowledgeBinding getBinding(String sceneGroupId, String knowledgeBaseId) {
        List<KnowledgeBinding> groupBindings = bindings.get(sceneGroupId);
        if (groupBindings == null) {
            return null;
        }
        return groupBindings.stream()
            .filter(b -> knowledgeBaseId.equals(b.getKnowledgeBaseId()))
            .findFirst()
            .orElse(null);
    }

    @Override
    public boolean hasBinding(String sceneGroupId, String knowledgeBaseId) {
        return getBinding(sceneGroupId, knowledgeBaseId) != null;
    }

    @Override
    public boolean setPriority(String sceneGroupId, String knowledgeBaseId, int priority) {
        KnowledgeBinding binding = getBinding(sceneGroupId, knowledgeBaseId);
        if (binding != null) {
            binding.setPriority(priority);
            return true;
        }
        return false;
    }

    @Override
    public int clearAllBindings(String sceneGroupId) {
        List<KnowledgeBinding> removed = bindings.remove(sceneGroupId);
        return removed != null ? removed.size() : 0;
    }

    @Override
    public List<KnowledgeChunk> searchKnowledge(String sceneGroupId, String query, int topK) {
        log.debug("searchKnowledge not implemented in memory version");
        return Collections.emptyList();
    }

    @Override
    public List<KnowledgeChunk> crossLayerSearch(String sceneGroupId, String query, List<String> layers, int topK) {
        log.debug("crossLayerSearch not implemented in memory version");
        return Collections.emptyList();
    }

    @Override
    public int getBindingCount(String sceneGroupId) {
        return getBindings(sceneGroupId).size();
    }

    @Override
    public long getTotalBindingCount() {
        return bindings.values().stream()
            .mapToInt(List::size)
            .sum();
    }
}
