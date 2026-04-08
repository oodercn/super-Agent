package net.ooder.skill.llm;

public class LlmModelDTO {
    private String id;
    private String name;
    private String providerId;
    private String description;
    private int maxTokens;
    private boolean supportsStreaming;
    private boolean supportsFunctionCalling;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getProviderId() { return providerId; }
    public void setProviderId(String providerId) { this.providerId = providerId; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getMaxTokens() { return maxTokens; }
    public void setMaxTokens(int maxTokens) { this.maxTokens = maxTokens; }
    public boolean isSupportsStreaming() { return supportsStreaming; }
    public void setSupportsStreaming(boolean supportsStreaming) { this.supportsStreaming = supportsStreaming; }
    public boolean isSupportsFunctionCalling() { return supportsFunctionCalling; }
    public void setSupportsFunctionCalling(boolean supportsFunctionCalling) { this.supportsFunctionCalling = supportsFunctionCalling; }
}
