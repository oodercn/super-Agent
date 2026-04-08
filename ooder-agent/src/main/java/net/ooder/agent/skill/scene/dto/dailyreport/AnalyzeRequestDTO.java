package net.ooder.mvp.skill.scene.dto.dailyreport;

import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

public class AnalyzeRequestDTO {
    
    @NotBlank(message = "场景组ID不能为空")
    private String sceneGroupId;
    
    private List<Map<String, Object>> reports;
    
    private String analyzeType;
    
    public AnalyzeRequestDTO() {}
    
    public String getSceneGroupId() {
        return sceneGroupId;
    }
    
    public void setSceneGroupId(String sceneGroupId) {
        this.sceneGroupId = sceneGroupId;
    }
    
    public List<Map<String, Object>> getReports() {
        return reports;
    }
    
    public void setReports(List<Map<String, Object>> reports) {
        this.reports = reports;
    }
    
    public String getAnalyzeType() {
        return analyzeType;
    }
    
    public void setAnalyzeType(String analyzeType) {
        this.analyzeType = analyzeType;
    }
}
