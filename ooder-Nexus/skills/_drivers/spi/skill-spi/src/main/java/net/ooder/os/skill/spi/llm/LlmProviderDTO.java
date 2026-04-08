package net.ooder.os.skill.spi.llm;

import java.util.List;

public class LlmProviderDTO {
    private String id;
    private String name;
    private String type;
    private boolean enabled;
    private List<LlmModelDTO> models;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public List<LlmModelDTO> getModels() { return models; }
    public void setModels(List<LlmModelDTO> models) { this.models = models; }
}
