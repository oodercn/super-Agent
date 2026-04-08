package net.ooder.mvp.skill.scene.dto.knowledge;

import java.util.List;

public class EmbeddingModelsResultDTO {
    
    private List<EmbeddingModelDTO> models;
    
    private String currentModel;

    public EmbeddingModelsResultDTO() {}

    public List<EmbeddingModelDTO> getModels() {
        return models;
    }

    public void setModels(List<EmbeddingModelDTO> models) {
        this.models = models;
    }

    public String getCurrentModel() {
        return currentModel;
    }

    public void setCurrentModel(String currentModel) {
        this.currentModel = currentModel;
    }
}
