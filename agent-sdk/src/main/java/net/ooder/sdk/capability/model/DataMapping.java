package net.ooder.sdk.capability.model;

public class DataMapping {
    
    private String sourcePath;
    private String targetPath;
    private String transform;
    
    public String getSourcePath() { return sourcePath; }
    public void setSourcePath(String sourcePath) { this.sourcePath = sourcePath; }
    
    public String getTargetPath() { return targetPath; }
    public void setTargetPath(String targetPath) { this.targetPath = targetPath; }
    
    public String getTransform() { return transform; }
    public void setTransform(String transform) { this.transform = transform; }
}
