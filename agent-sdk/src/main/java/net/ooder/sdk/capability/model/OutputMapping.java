package net.ooder.sdk.capability.model;

public class OutputMapping {
    
    private String targetVariable;
    private String sourcePath;
    private String transform;
    
    public String getTargetVariable() { return targetVariable; }
    public void setTargetVariable(String targetVariable) { this.targetVariable = targetVariable; }
    
    public String getSourcePath() { return sourcePath; }
    public void setSourcePath(String sourcePath) { this.sourcePath = sourcePath; }
    
    public String getTransform() { return transform; }
    public void setTransform(String transform) { this.transform = transform; }
}
