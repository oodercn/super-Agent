package net.ooder.mvp.skill.scene.dto.discovery;

import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

public class InstallSkillRequestDTO {
    
    @NotBlank(message = "skillId不能为空")
    private String skillId;
    
    private String name;
    
    private String type;
    
    private String description;
    
    private String source;
    
    private String repoUrl;
    
    private String selectedRole;
    
    private ParticipantConfig participants;
    
    private List<String> driverConditions;
    
    private LLMConfig llmConfig;

    public InstallSkillRequestDTO() {}

    public String getSkillId() {
        return skillId;
    }

    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getRepoUrl() {
        return repoUrl;
    }

    public void setRepoUrl(String repoUrl) {
        this.repoUrl = repoUrl;
    }

    public String getSelectedRole() {
        return selectedRole;
    }

    public void setSelectedRole(String selectedRole) {
        this.selectedRole = selectedRole;
    }

    public ParticipantConfig getParticipants() {
        return participants;
    }

    public void setParticipants(ParticipantConfig participants) {
        this.participants = participants;
    }

    public List<String> getDriverConditions() {
        return driverConditions;
    }

    public void setDriverConditions(List<String> driverConditions) {
        this.driverConditions = driverConditions;
    }

    public LLMConfig getLlmConfig() {
        return llmConfig;
    }

    public void setLlmConfig(LLMConfig llmConfig) {
        this.llmConfig = llmConfig;
    }
    
    public static class ParticipantConfig {
        private String leader;
        private List<String> collaborators;
        private String pushType;
        
        public ParticipantConfig() {}
        
        public String getLeader() {
            return leader;
        }
        
        public void setLeader(String leader) {
            this.leader = leader;
        }
        
        public List<String> getCollaborators() {
            return collaborators;
        }
        
        public void setCollaborators(List<String> collaborators) {
            this.collaborators = collaborators;
        }
        
        public String getPushType() {
            return pushType;
        }
        
        public void setPushType(String pushType) {
            this.pushType = pushType;
        }
    }
    
    public static class LLMConfig {
        private String provider;
        private String model;
        private String systemPrompt;
        private Boolean enableFunctionCall;
        private List<String> functionTools;
        private KnowledgeConfig knowledge;
        private Map<String, Object> parameters;
        
        public LLMConfig() {}
        
        public String getProvider() {
            return provider;
        }
        
        public void setProvider(String provider) {
            this.provider = provider;
        }
        
        public String getModel() {
            return model;
        }
        
        public void setModel(String model) {
            this.model = model;
        }
        
        public String getSystemPrompt() {
            return systemPrompt;
        }
        
        public void setSystemPrompt(String systemPrompt) {
            this.systemPrompt = systemPrompt;
        }
        
        public Boolean getEnableFunctionCall() {
            return enableFunctionCall;
        }
        
        public void setEnableFunctionCall(Boolean enableFunctionCall) {
            this.enableFunctionCall = enableFunctionCall;
        }
        
        public List<String> getFunctionTools() {
            return functionTools;
        }
        
        public void setFunctionTools(List<String> functionTools) {
            this.functionTools = functionTools;
        }
        
        public KnowledgeConfig getKnowledge() {
            return knowledge;
        }
        
        public void setKnowledge(KnowledgeConfig knowledge) {
            this.knowledge = knowledge;
        }
        
        public Map<String, Object> getParameters() {
            return parameters;
        }
        
        public void setParameters(Map<String, Object> parameters) {
            this.parameters = parameters;
        }
    }
    
    public static class KnowledgeConfig {
        private Boolean enabled;
        private Integer topK;
        private Double scoreThreshold;
        private List<String> bases;
        
        public KnowledgeConfig() {}
        
        public Boolean getEnabled() {
            return enabled;
        }
        
        public void setEnabled(Boolean enabled) {
            this.enabled = enabled;
        }
        
        public Integer getTopK() {
            return topK;
        }
        
        public void setTopK(Integer topK) {
            this.topK = topK;
        }
        
        public Double getScoreThreshold() {
            return scoreThreshold;
        }
        
        public void setScoreThreshold(Double scoreThreshold) {
            this.scoreThreshold = scoreThreshold;
        }
        
        public List<String> getBases() {
            return bases;
        }
        
        public void setBases(List<String> bases) {
            this.bases = bases;
        }
    }
}
