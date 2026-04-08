package net.ooder.nexus.dto.llm;

import java.util.List;
import java.util.Map;

public class LlmModelsDTO {
    private List<String> providers;
    private Map<String, List<String>> modelsByProvider;
    private String currentProvider;
    private String currentModel;
    private List<LlmProviderDTO> providerDetails;

    public List<String> getProviders() {
        return providers;
    }

    public void setProviders(List<String> providers) {
        this.providers = providers;
    }

    public Map<String, List<String>> getModelsByProvider() {
        return modelsByProvider;
    }

    public void setModelsByProvider(Map<String, List<String>> modelsByProvider) {
        this.modelsByProvider = modelsByProvider;
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

    public List<LlmProviderDTO> getProviderDetails() {
        return providerDetails;
    }

    public void setProviderDetails(List<LlmProviderDTO> providerDetails) {
        this.providerDetails = providerDetails;
    }
}
