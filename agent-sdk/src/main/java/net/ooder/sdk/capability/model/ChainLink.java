package net.ooder.sdk.capability.model;

import java.util.Map;

public class ChainLink {
    
    private String linkId;
    private String name;
    private String capabilityId;
    private Map<String, Object> parameters;
    private DataMapping inputMapping;
    private DataMapping outputMapping;
    
    public String getLinkId() { return linkId; }
    public void setLinkId(String linkId) { this.linkId = linkId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getCapabilityId() { return capabilityId; }
    public void setCapabilityId(String capabilityId) { this.capabilityId = capabilityId; }
    
    public Map<String, Object> getParameters() { return parameters; }
    public void setParameters(Map<String, Object> parameters) { this.parameters = parameters; }
    
    public DataMapping getInputMapping() { return inputMapping; }
    public void setInputMapping(DataMapping inputMapping) { this.inputMapping = inputMapping; }
    
    public DataMapping getOutputMapping() { return outputMapping; }
    public void setOutputMapping(DataMapping outputMapping) { this.outputMapping = outputMapping; }
}
