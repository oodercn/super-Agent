package net.ooder.sdk.api.llm;

import java.util.List;
import java.util.Map;

/**
 * Chat Request for LLM Service
 *
 * @author ooder Team
 * @since 0.7.1
 */
public class ChatRequest {

    private String prompt;
    private String systemPrompt;
    private List<Message> messages;
    private Map<String, Object> parameters;
    private String model;
    private Double temperature;
    private Integer maxTokens;
    private List<FunctionDef> functions;

    public ChatRequest() {
        this.parameters = new java.util.HashMap<String, Object>();
    }

    public static ChatRequest of(String prompt) {
        ChatRequest request = new ChatRequest();
        request.setPrompt(prompt);
        return request;
    }

    public static ChatRequest of(String prompt, String systemPrompt) {
        ChatRequest request = new ChatRequest();
        request.setPrompt(prompt);
        request.setSystemPrompt(systemPrompt);
        return request;
    }

    public String getPrompt() { return prompt; }
    public void setPrompt(String prompt) { this.prompt = prompt; }

    public String getSystemPrompt() { return systemPrompt; }
    public void setSystemPrompt(String systemPrompt) { this.systemPrompt = systemPrompt; }

    public List<Message> getMessages() { return messages; }
    public void setMessages(List<Message> messages) { this.messages = messages; }

    public Map<String, Object> getParameters() { return parameters; }
    public void setParameters(Map<String, Object> parameters) { this.parameters = parameters; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public Double getTemperature() { return temperature; }
    public void setTemperature(Double temperature) { this.temperature = temperature; }

    public Integer getMaxTokens() { return maxTokens; }
    public void setMaxTokens(Integer maxTokens) { this.maxTokens = maxTokens; }

    public List<FunctionDef> getFunctions() { return functions; }
    public void setFunctions(List<FunctionDef> functions) { this.functions = functions; }

    public ChatRequest addParameter(String key, Object value) {
        this.parameters.put(key, value);
        return this;
    }

    public static class Message {
        private String role;
        private String content;
        private String name;

        public Message() {}

        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }

        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }

        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }
}
