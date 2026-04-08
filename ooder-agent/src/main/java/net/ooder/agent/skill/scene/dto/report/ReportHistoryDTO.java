package net.ooder.mvp.skill.scene.dto.report;

public class ReportHistoryDTO {
    private String reportId;
    private String date;
    private String status;
    private String summary;

    public String getReportId() { return reportId; }
    public void setReportId(String reportId) { this.reportId = reportId; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }
}
