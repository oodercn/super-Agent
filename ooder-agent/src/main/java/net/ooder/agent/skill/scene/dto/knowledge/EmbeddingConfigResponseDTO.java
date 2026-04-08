package net.ooder.mvp.skill.scene.dto.knowledge;

public class EmbeddingConfigResponseDTO {
    
    private String currentModel;
    private Integer dimensions;
    private String provider;
    private Integer defaultChunkSize;
    private Integer defaultChunkOverlap;
    
    public EmbeddingConfigResponseDTO() {
    }
    
    public String getCurrentModel() { return currentModel; }
    public void setCurrentModel(String currentModel) { this.currentModel = currentModel; }
    
    public Integer getDimensions() { return dimensions; }
    public void setDimensions(Integer dimensions) { this.dimensions = dimensions; }
    
    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }
    
    public Integer getDefaultChunkSize() { return defaultChunkSize; }
    public void setDefaultChunkSize(Integer defaultChunkSize) { this.defaultChunkSize = defaultChunkSize; }
    
    public Integer getDefaultChunkOverlap() { return defaultChunkOverlap; }
    public void setDefaultChunkOverlap(Integer defaultChunkOverlap) { this.defaultChunkOverlap = defaultChunkOverlap; }
}
