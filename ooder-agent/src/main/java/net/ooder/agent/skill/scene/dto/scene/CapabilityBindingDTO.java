package net.ooder.mvp.skill.scene.dto.scene;

import java.util.List;
import java.util.Map;

public class CapabilityBindingDTO {
    private String bindingId;
    private String sceneGroupId;
    private String capId;
    private String capName;
    private CapabilityProviderType providerType;
    private String providerId;
    private ConnectorType connectorType;
    private Map<String, Object> connectorConfig;
    private int priority;
    private boolean fallback;
    private CapabilityBindingStatus status;
    
    private String selectedRole;
    private ParticipantConfig participants;
    private List<String> driverConditions;
    private LLMConfig llmConfig;

    public String getBindingId() { return bindingId; }
    public void setBindingId(String bindingId) { this.bindingId = bindingId; }
    public String getSceneGroupId() { return sceneGroupId; }
    public void setSceneGroupId(String sceneGroupId) { this.sceneGroupId = sceneGroupId; }
    public String getCapId() { return capId; }
    public void setCapId(String capId) { this.capId = capId; }
    public String getCapName() { return capName; }
    public void setCapName(String capName) { this.capName = capName; }
    public CapabilityProviderType getProviderType() { return providerType; }
    public void setProviderType(CapabilityProviderType providerType) { this.providerType = providerType; }
    public String getProviderId() { return providerId; }
    public void setProviderId(String providerId) { this.providerId = providerId; }
    public ConnectorType getConnectorType() { return connectorType; }
    public void setConnectorType(ConnectorType connectorType) { this.connectorType = connectorType; }
    public Map<String, Object> getConnectorConfig() { return connectorConfig; }
    public void setConnectorConfig(Map<String, Object> connectorConfig) { this.connectorConfig = connectorConfig; }
    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }
    public boolean isFallback() { return fallback; }
    public void setFallback(boolean fallback) { this.fallback = fallback; }
    public CapabilityBindingStatus getStatus() { return status; }
    public void setStatus(CapabilityBindingStatus status) { this.status = status; }
    
    public String getSelectedRole() { return selectedRole; }
    public void setSelectedRole(String selectedRole) { this.selectedRole = selectedRole; }
    public ParticipantConfig getParticipants() { return participants; }
    public void setParticipants(ParticipantConfig participants) { this.participants = participants; }
    public List<String> getDriverConditions() { return driverConditions; }
    public void setDriverConditions(List<String> driverConditions) { this.driverConditions = driverConditions; }
    public LLMConfig getLlmConfig() { return llmConfig; }
    public void setLlmConfig(LLMConfig llmConfig) { this.llmConfig = llmConfig; }
    
    public static class ParticipantConfig {
        private String leader;
        private List<String> collaborators;
        private String pushType;
        
        public String getLeader() { return leader; }
        public void setLeader(String leader) { this.leader = leader; }
        public List<String> getCollaborators() { return collaborators; }
        public void setCollaborators(List<String> collaborators) { this.collaborators = collaborators; }
        public String getPushType() { return pushType; }
        public void setPushType(String pushType) { this.pushType = pushType; }
    }
    
    public static class LLMConfig {
        private String provider;
        private String model;
        private String systemPrompt;
        private Boolean enableFunctionCall;
        private List<String> functionTools;
        private Map<String, Object> parameters;
        private KnowledgeConfig knowledge;
        
        public String getProvider() { return provider; }
        public void setProvider(String provider) { this.provider = provider; }
        public String getModel() { return model; }
        public void setModel(String model) { this.model = model; }
        public String getSystemPrompt() { return systemPrompt; }
        public void setSystemPrompt(String systemPrompt) { this.systemPrompt = systemPrompt; }
        public Boolean getEnableFunctionCall() { return enableFunctionCall; }
        public void setEnableFunctionCall(Boolean enableFunctionCall) { this.enableFunctionCall = enableFunctionCall; }
        public List<String> getFunctionTools() { return functionTools; }
        public void setFunctionTools(List<String> functionTools) { this.functionTools = functionTools; }
        public Map<String, Object> getParameters() { return parameters; }
        public void setParameters(Map<String, Object> parameters) { this.parameters = parameters; }
        public KnowledgeConfig getKnowledge() { return knowledge; }
        public void setKnowledge(KnowledgeConfig knowledge) { this.knowledge = knowledge; }
    }
    
    public static class KnowledgeConfig {
        private Boolean enabled;
        private Integer topK;
        private Double scoreThreshold;
        private List<String> bases;
        
        public Boolean getEnabled() { return enabled; }
        public void setEnabled(Boolean enabled) { this.enabled = enabled; }
        public Integer getTopK() { return topK; }
        public void setTopK(Integer topK) { this.topK = topK; }
        public Double getScoreThreshold() { return scoreThreshold; }
        public void setScoreThreshold(Double scoreThreshold) { this.scoreThreshold = scoreThreshold; }
        public List<String> getBases() { return bases; }
        public void setBases(List<String> bases) { this.bases = bases; }
    }
}
