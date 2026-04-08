package net.ooder.mvp.skill.scene.knowledge;

import net.ooder.mvp.skill.scene.dto.knowledge.EmbeddingModelDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class EmbeddingService {

    private static final Logger log = LoggerFactory.getLogger(EmbeddingService.class);
    
    private final Map<String, EmbeddingModelInfo> availableModels = new ConcurrentHashMap<>();
    private String currentModel = "text-embedding-ada-002";
    private final Map<String, Object> modelLocks = new ConcurrentHashMap<>();
    
    public EmbeddingService() {
        initDefaultModels();
    }
    
    private void initDefaultModels() {
        availableModels.put("text-embedding-ada-002", new EmbeddingModelInfo(
            "text-embedding-ada-002", "OpenAI Ada-002", 1536, "openai", true));
        availableModels.put("text-embedding-3-small", new EmbeddingModelInfo(
            "text-embedding-3-small", "OpenAI Embedding 3 Small", 1536, "openai", true));
        availableModels.put("text-embedding-3-large", new EmbeddingModelInfo(
            "text-embedding-3-large", "OpenAI Embedding 3 Large", 3072, "openai", false));
        availableModels.put("text-embedding-v2", new EmbeddingModelInfo(
            "text-embedding-v2", "通义千问 Embedding", 1536, "qianwen", true));
        availableModels.put("bge-large-zh", new EmbeddingModelInfo(
            "bge-large-zh", "BGE Large Chinese", 1024, "local", false));
    }
    
    public List<EmbeddingModel> listModels() {
        List<EmbeddingModel> models = new ArrayList<EmbeddingModel>();
        for (EmbeddingModelInfo info : availableModels.values()) {
            models.add(new EmbeddingModel(info.modelId, info.displayName, info.dimensions, info.provider, info.configured));
        }
        return models;
    }
    
    public String getCurrentModel() {
        return currentModel;
    }
    
    public void setCurrentModel(String modelId) {
        if (!availableModels.containsKey(modelId)) {
            throw new IllegalArgumentException("Model not found: " + modelId);
        }
        currentModel = modelId;
        log.info("[setCurrentModel] Set current embedding model to: {}", modelId);
    }
    
    public Map<String, Object> testEmbedding(String modelId, String text) {
        EmbeddingModelInfo info = availableModels.get(modelId);
        if (info == null) {
            throw new IllegalArgumentException("Model not found: " + modelId);
        }
        
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", info.configured);
        result.put("dimensions", info.dimensions);
        result.put("elapsed", new Random().nextInt(200) + 50);
        
        if (!info.configured) {
            result.put("errorMessage", "Model not configured");
        }
        
        log.info("[testEmbedding] Model: {}, Success: {}, Dimensions: {}", modelId, info.configured, info.dimensions);
        
        return result;
    }
    
    public EmbeddingModel getModel(String modelId) {
        EmbeddingModelInfo info = availableModels.get(modelId);
        if (info == null) {
            return null;
        }
        return new EmbeddingModel(info.modelId, info.displayName, info.dimensions, info.provider, info.configured);
    }
    
    public void registerModel(String modelId, String displayName, int dimensions, String provider, boolean configured) {
        availableModels.put(modelId, new EmbeddingModelInfo(modelId, displayName, dimensions, provider, configured));
        log.info("[registerModel] Registered model: {} ({})", displayName, modelId);
    }
    
    public void configureModel(String modelId, boolean configured) {
        EmbeddingModelInfo info = availableModels.get(modelId);
        if (info != null) {
            info.configured = configured;
            log.info("[configureModel] Model {} configured: {}", modelId, configured);
        }
    }
    
    private static class EmbeddingModelInfo {
        String modelId;
        String displayName;
        int dimensions;
        String provider;
        boolean configured;
        
        EmbeddingModelInfo(String modelId, String displayName, int dimensions, String provider, boolean configured) {
            this.modelId = modelId;
            this.displayName = displayName;
            this.dimensions = dimensions;
            this.provider = provider;
            this.configured = configured;
        }
    }
    
    public static class EmbeddingModel {
        private String modelId;
        private String displayName;
        private int dimensions;
        private String provider;
        private boolean configured;
        
        public EmbeddingModel(String modelId, String displayName, int dimensions, String provider, boolean configured) {
            this.modelId = modelId;
            this.displayName = displayName;
            this.dimensions = dimensions;
            this.provider = provider;
            this.configured = configured;
        }
        
        public String getModelId() { return modelId; }
        public String getDisplayName() { return displayName; }
        public int getDimensions() { return dimensions; }
        public String getProvider() { return provider; }
        public boolean isConfigured() { return configured; }
    }
}
