package net.ooder.sdk.capability.model;

import java.util.List;
import java.util.Map;

public class CapabilitySpec {
    
    private String specId;
    private String specName;
    private CapabilityType type;
    private String version;
    private String description;
    private String author;
    private List<String> tags;
    private Map<String, Object> metadata;
    private List<Dependency> dependencies;
    private List<Parameter> parameters;
    private List<Output> outputs;
    private ExecutionConfig executionConfig;
    private SecurityConfig securityConfig;
    private long createdTime;
    private long updatedTime;
    private SpecStatus status;
    
    public String getSpecId() { return specId; }
    public void setSpecId(String specId) { this.specId = specId; }
    
    public String getSpecName() { return specName; }
    public void setSpecName(String specName) { this.specName = specName; }
    
    public CapabilityType getType() { return type; }
    public void setType(CapabilityType type) { this.type = type; }
    
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    
    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }
    
    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
    
    public List<Dependency> getDependencies() { return dependencies; }
    public void setDependencies(List<Dependency> dependencies) { this.dependencies = dependencies; }
    
    public List<Parameter> getParameters() { return parameters; }
    public void setParameters(List<Parameter> parameters) { this.parameters = parameters; }
    
    public List<Output> getOutputs() { return outputs; }
    public void setOutputs(List<Output> outputs) { this.outputs = outputs; }
    
    public ExecutionConfig getExecutionConfig() { return executionConfig; }
    public void setExecutionConfig(ExecutionConfig executionConfig) { this.executionConfig = executionConfig; }
    
    public SecurityConfig getSecurityConfig() { return securityConfig; }
    public void setSecurityConfig(SecurityConfig securityConfig) { this.securityConfig = securityConfig; }
    
    public long getCreatedTime() { return createdTime; }
    public void setCreatedTime(long createdTime) { this.createdTime = createdTime; }
    
    public long getUpdatedTime() { return updatedTime; }
    public void setUpdatedTime(long updatedTime) { this.updatedTime = updatedTime; }
    
    public SpecStatus getStatus() { return status; }
    public void setStatus(SpecStatus status) { this.status = status; }
}
