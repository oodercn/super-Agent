package net.ooder.mvp.skill.scene.dto.dailyreport;

import java.util.List;
import java.util.Map;

public class DailyReportResponseDTO {
    
    private boolean success;
    private String message;
    private String reminderId;
    private String reportId;
    private Integer sentCount;
    private Integer totalReports;
    private Map<String, Integer> userReportCount;
    private List<?> reports;
    private Long aggregateTime;
    private Map<String, Object> analysis;
    private Integer total;
    
    public DailyReportResponseDTO() {}
    
    public static DailyReportResponseDTO success(String message) {
        DailyReportResponseDTO dto = new DailyReportResponseDTO();
        dto.setSuccess(true);
        dto.setMessage(message);
        return dto;
    }
    
    public static DailyReportResponseDTO error(String message) {
        DailyReportResponseDTO dto = new DailyReportResponseDTO();
        dto.setSuccess(false);
        dto.setMessage(message);
        return dto;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReminderId() {
        return reminderId;
    }

    public void setReminderId(String reminderId) {
        this.reminderId = reminderId;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public Integer getSentCount() {
        return sentCount;
    }

    public void setSentCount(Integer sentCount) {
        this.sentCount = sentCount;
    }

    public Integer getTotalReports() {
        return totalReports;
    }

    public void setTotalReports(Integer totalReports) {
        this.totalReports = totalReports;
    }

    public Map<String, Integer> getUserReportCount() {
        return userReportCount;
    }

    public void setUserReportCount(Map<String, Integer> userReportCount) {
        this.userReportCount = userReportCount;
    }

    public List<?> getReports() {
        return reports;
    }

    public void setReports(List<?> reports) {
        this.reports = reports;
    }

    public Long getAggregateTime() {
        return aggregateTime;
    }

    public void setAggregateTime(Long aggregateTime) {
        this.aggregateTime = aggregateTime;
    }

    public Map<String, Object> getAnalysis() {
        return analysis;
    }

    public void setAnalysis(Map<String, Object> analysis) {
        this.analysis = analysis;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
