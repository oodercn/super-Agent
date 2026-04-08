package net.ooder.mvp.skill.scene.dto.knowledge;

import java.util.List;
import java.util.Map;

public class KnowledgeBaseDTO {
    
    private String kbId;
    private String name;
    private String description;
    private String ownerId;
    private String visibility;
    private String embeddingModel;
    private int chunkSize;
    private int chunkOverlap;
    private List<String> tags;
    private KnowledgeLayerConfig layerConfig;
    private IndexStatusDTO indexStatus;
    private long documentCount;
    private long createTime;
    private long updatedAt;
    
    public String getKbId() { return kbId; }
    public void setKbId(String kbId) { this.kbId = kbId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getOwnerId() { return ownerId; }
    public void setOwnerId(String ownerId) { this.ownerId = ownerId; }
    public String getVisibility() { return visibility; }
    public void setVisibility(String visibility) { this.visibility = visibility; }
    public String getEmbeddingModel() { return embeddingModel; }
    public void setEmbeddingModel(String embeddingModel) { this.embeddingModel = embeddingModel; }
    public int getChunkSize() { return chunkSize; }
    public void setChunkSize(int chunkSize) { this.chunkSize = chunkSize; }
    public int getChunkOverlap() { return chunkOverlap; }
    public void setChunkOverlap(int chunkOverlap) { this.chunkOverlap = chunkOverlap; }
    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }
    public KnowledgeLayerConfig getLayerConfig() { return layerConfig; }
    public void setLayerConfig(KnowledgeLayerConfig layerConfig) { this.layerConfig = layerConfig; }
    public IndexStatusDTO getIndexStatus() { return indexStatus; }
    public void setIndexStatus(IndexStatusDTO indexStatus) { this.indexStatus = indexStatus; }
    public long getDocumentCount() { return documentCount; }
    public void setDocumentCount(long documentCount) { this.documentCount = documentCount; }
    public long getCreateTime() { return createTime; }
    public void setCreateTime(long createTime) { this.createTime = createTime; }
    public void setCreatedAt(long createdAt) { this.createTime = createdAt; }
    public long getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(long updatedAt) { this.updatedAt = updatedAt; }
    
    public static class KnowledgeLayerConfig {
        private String layer;
        private int priority;
        private boolean enabled;
        private List<String> knowledgeBaseIds;
        
        public String getLayer() { return layer; }
        public void setLayer(String layer) { this.layer = layer; }
        public int getPriority() { return priority; }
        public void setPriority(int priority) { this.priority = priority; }
        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }
        public List<String> getKnowledgeBaseIds() { return knowledgeBaseIds; }
        public void setKnowledgeBaseIds(List<String> knowledgeBaseIds) { this.knowledgeBaseIds = knowledgeBaseIds; }
    }
    
    public static class IndexStatusDTO {
        private String status;
        private int progress;
        private long lastIndexTime;
        private String errorMessage;
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public int getProgress() { return progress; }
        public void setProgress(int progress) { this.progress = progress; }
        public long getLastIndexTime() { return lastIndexTime; }
        public void setLastIndexTime(long lastIndexTime) { this.lastIndexTime = lastIndexTime; }
        public String getErrorMessage() { return errorMessage; }
        public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    }
}
