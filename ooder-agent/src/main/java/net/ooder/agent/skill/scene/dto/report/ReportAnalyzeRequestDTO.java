package net.ooder.mvp.skill.scene.dto.report;

public class ReportAnalyzeRequestDTO {
    private String sceneGroupId;
    private String date;
    private String analyzeType;

    public String getSceneGroupId() { return sceneGroupId; }
    public void setSceneGroupId(String sceneGroupId) { this.sceneGroupId = sceneGroupId; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getAnalyzeType() { return analyzeType; }
    public void setAnalyzeType(String analyzeType) { this.analyzeType = analyzeType; }
}
