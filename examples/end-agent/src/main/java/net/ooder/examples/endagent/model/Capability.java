package net.ooder.examples.endagent.model;

import java.util.List;

public class Capability {
    private String id;
    private String name;
    private String description;
    private List<Parameter> parameters;
    private String returnType;
    private String status; // 对应协议中的cap状态
    private List<String> supportedScenes; // 对应协议中的supported_scenes

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getSupportedScenes() {
        return supportedScenes;
    }

    public void setSupportedScenes(List<String> supportedScenes) {
        this.supportedScenes = supportedScenes;
    }
}
