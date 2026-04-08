package net.ooder.mvp.skill.scene.dto.knowledge;

import java.util.*;

public class LlmKnowledgeConfigDTO {
    
    private String configId;
    private String name;
    private String type;
    private String description;
    private String kbId;
    private Map<String, Object> config;
    private Date createdAt;
    private Date updatedAt;
    
    public LlmKnowledgeConfigDTO() {
        this.configId = UUID.randomUUID().toString();
        this.createdAt = new Date();
        this.updatedAt = new Date();
        this.config = new HashMap<>();
    }
    
    public String getConfigId() { return configId; }
    public void setConfigId(String configId) { this.configId = configId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getKbId() { return kbId; }
    public void setKbId(String kbId) { this.kbId = kbId; }
    
    public Map<String, Object> getConfig() { return config; }
    public void setConfig(Map<String, Object> config) { this.config = config; }
    
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    
    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
    
    public static class DictionaryTerm {
        private String termId;
        private String term;
        private String fullName;
        private String description;
        private String category;
        private String kbId;
        private String kbName;
        private Date createdAt;
        
        public DictionaryTerm() {
            this.termId = UUID.randomUUID().toString();
            this.createdAt = new Date();
        }
        
        public String getTermId() { return termId; }
        public void setTermId(String termId) { this.termId = termId; }
        
        public String getTerm() { return term; }
        public void setTerm(String term) { this.term = term; }
        
        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        
        public String getKbId() { return kbId; }
        public void setKbId(String kbId) { this.kbId = kbId; }
        
        public String getKbName() { return kbName; }
        public void setKbName(String kbName) { this.kbName = kbName; }
        
        public Date getCreatedAt() { return createdAt; }
        public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    }
    
    public static class SynonymMapping {
        private String mappingId;
        private String mainTerm;
        private List<String> synonyms;
        private String kbId;
        private Date createdAt;
        
        public SynonymMapping() {
            this.mappingId = UUID.randomUUID().toString();
            this.synonyms = new ArrayList<>();
            this.createdAt = new Date();
        }
        
        public String getMappingId() { return mappingId; }
        public void setMappingId(String mappingId) { this.mappingId = mappingId; }
        
        public String getMainTerm() { return mainTerm; }
        public void setMainTerm(String mainTerm) { this.mainTerm = mainTerm; }
        
        public List<String> getSynonyms() { return synonyms; }
        public void setSynonyms(List<String> synonyms) { this.synonyms = synonyms; }
        
        public String getKbId() { return kbId; }
        public void setKbId(String kbId) { this.kbId = kbId; }
        
        public Date getCreatedAt() { return createdAt; }
        public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    }
    
    public static class LlmInterface {
        private String interfaceId;
        private String method;
        private String path;
        private String name;
        private String description;
        private List<InterfaceParam> parameters;
        private String llmPrompt;
        private boolean enabled;
        private Date createdAt;
        
        public LlmInterface() {
            this.interfaceId = UUID.randomUUID().toString();
            this.parameters = new ArrayList<>();
            this.enabled = true;
            this.createdAt = new Date();
        }
        
        public String getInterfaceId() { return interfaceId; }
        public void setInterfaceId(String interfaceId) { this.interfaceId = interfaceId; }
        
        public String getMethod() { return method; }
        public void setMethod(String method) { this.method = method; }
        
        public String getPath() { return path; }
        public void setPath(String path) { this.path = path; }
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        
        public List<InterfaceParam> getParameters() { return parameters; }
        public void setParameters(List<InterfaceParam> parameters) { this.parameters = parameters; }
        
        public String getLlmPrompt() { return llmPrompt; }
        public void setLlmPrompt(String llmPrompt) { this.llmPrompt = llmPrompt; }
        
        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }
        
        public Date getCreatedAt() { return createdAt; }
        public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    }
    
    public static class InterfaceParam {
        private String name;
        private String type;
        private boolean required;
        private String description;
        private String defaultValue;
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        
        public boolean isRequired() { return required; }
        public void setRequired(boolean required) { this.required = required; }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        
        public String getDefaultValue() { return defaultValue; }
        public void setDefaultValue(String defaultValue) { this.defaultValue = defaultValue; }
    }
    
    public static class PromptTemplate {
        private String templateId;
        private String name;
        private String description;
        private String content;
        private List<String> variables;
        private boolean isDefault;
        private Date createdAt;
        private Date updatedAt;
        
        public PromptTemplate() {
            this.templateId = UUID.randomUUID().toString();
            this.variables = new ArrayList<>();
            this.createdAt = new Date();
            this.updatedAt = new Date();
        }
        
        public String getTemplateId() { return templateId; }
        public void setTemplateId(String templateId) { this.templateId = templateId; }
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        
        public List<String> getVariables() { return variables; }
        public void setVariables(List<String> variables) { this.variables = variables; }
        
        public boolean isDefault() { return isDefault; }
        public void setDefault(boolean isDefault) { this.isDefault = isDefault; }
        
        public Date getCreatedAt() { return createdAt; }
        public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
        
        public Date getUpdatedAt() { return updatedAt; }
        public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
    }
}
