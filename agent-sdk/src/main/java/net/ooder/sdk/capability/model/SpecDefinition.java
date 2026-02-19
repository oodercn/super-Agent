package net.ooder.sdk.capability.model;

import java.util.List;
import java.util.Map;

public class SpecDefinition {
    
    private String name;
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
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
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
}
