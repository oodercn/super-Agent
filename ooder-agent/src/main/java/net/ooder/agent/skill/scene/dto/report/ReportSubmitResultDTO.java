package net.ooder.mvp.skill.scene.dto.report;

public class ReportSubmitResultDTO {
    private String reportId;
    private String status;
    private String message;

    public String getReportId() { return reportId; }
    public void setReportId(String reportId) { this.reportId = reportId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
