package net.ooder.skill.discovery.dto.discovery;

import java.util.List;

public class TestSuggestionDTO {
    
    private List<String> highPriority;
    private List<String> mediumPriority;
    private List<String> lowPriority;
    private int estimatedTestCases;
    private String message;
    
    public List<String> getHighPriority() { return highPriority; }
    public void setHighPriority(List<String> highPriority) { this.highPriority = highPriority; }
    
    public List<String> getMediumPriority() { return mediumPriority; }
    public void setMediumPriority(List<String> mediumPriority) { this.mediumPriority = mediumPriority; }
    
    public List<String> getLowPriority() { return lowPriority; }
    public void setLowPriority(List<String> lowPriority) { this.lowPriority = lowPriority; }
    
    public int getEstimatedTestCases() { return estimatedTestCases; }
    public void setEstimatedTestCases(int estimatedTestCases) { this.estimatedTestCases = estimatedTestCases; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
