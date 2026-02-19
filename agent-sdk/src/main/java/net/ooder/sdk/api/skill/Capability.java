
package net.ooder.sdk.api.skill;

import java.util.List;

public class Capability {
    
    private String capId;
    private String name;
    private String description;
    private List<Parameter> parameters;
    private String returnType;
    private boolean async;
    
    public String getCapId() {
        return capId;
    }
    
    public void setCapId(String capId) {
        this.capId = capId;
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
    
    public boolean isAsync() {
        return async;
    }
    
    public void setAsync(boolean async) {
        this.async = async;
    }
}
