package net.ooder.mvp.skill.scene.dto.knowledge;

public class EmbeddingTestRequestDTO {
    
    private String modelId;
    
    private String text;

    public EmbeddingTestRequestDTO() {}

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
