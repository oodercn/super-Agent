package net.ooder.mvp.skill.scene.dto.llm;

public class SetModelRequestDTO {
    
    private String modelId;
    
    private String provider;

    public SetModelRequestDTO() {}

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }
}
