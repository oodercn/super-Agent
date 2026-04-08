package net.ooder.agent.dto.llm;

public class LlmHealthDTO {
    private Boolean healthy;
    private String currentProvider;
    private String currentModel;
    private Boolean providerManagerAvailable;

    public Boolean getHealthy() {
        return healthy;
    }

    public void setHealthy(Boolean healthy) {
        this.healthy = healthy;
    }

    public String getCurrentProvider() {
        return currentProvider;
    }

    public void setCurrentProvider(String currentProvider) {
        this.currentProvider = currentProvider;
    }

    public String getCurrentModel() {
        return currentModel;
    }

    public void setCurrentModel(String currentModel) {
        this.currentModel = currentModel;
    }

    public Boolean getProviderManagerAvailable() {
        return providerManagerAvailable;
    }

    public void setProviderManagerAvailable(Boolean providerManagerAvailable) {
        this.providerManagerAvailable = providerManagerAvailable;
    }
}
