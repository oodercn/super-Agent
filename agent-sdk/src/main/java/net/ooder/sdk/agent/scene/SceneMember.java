package net.ooder.sdk.agent.scene;

import java.util.List;
import java.util.Map;

public class SceneMember {
    private String agentId;
    private String role;
    private List<String> capabilities;
    private Map<String, String> endpoints;
    private String techStack;

    public SceneMember() {
    }

    public SceneMember(String agentId, String role, List<String> capabilities, Map<String, String> endpoints) {
        this.agentId = agentId;
        this.role = role;
        this.capabilities = capabilities;
        this.endpoints = endpoints;
    }

    public SceneMember(String agentId, String role, List<String> capabilities, Map<String, String> endpoints, String techStack) {
        this.agentId = agentId;
        this.role = role;
        this.capabilities = capabilities;
        this.endpoints = endpoints;
        this.techStack = techStack;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<String> getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(List<String> capabilities) {
        this.capabilities = capabilities;
    }

    public Map<String, String> getEndpoints() {
        return endpoints;
    }

    public void setEndpoints(Map<String, String> endpoints) {
        this.endpoints = endpoints;
    }

    public String getTechStack() {
        return techStack;
    }

    public void setTechStack(String techStack) {
        this.techStack = techStack;
    }
}