package net.ooder.nexus.skillcenter.dto.security;

public class ThreatInfoDTO {

    private String threatId;
    private String threatType;
    private String severity;
    private String source;
    private String description;
    private String status;
    private String recommendation;
    private Long detectedAt;
    private Long resolvedAt;

    public ThreatInfoDTO() {}

    public String getThreatId() {
        return threatId;
    }

    public void setThreatId(String threatId) {
        this.threatId = threatId;
    }

    public String getThreatType() {
        return threatType;
    }

    public void setThreatType(String threatType) {
        this.threatType = threatType;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }

    public Long getDetectedAt() {
        return detectedAt;
    }

    public void setDetectedAt(Long detectedAt) {
        this.detectedAt = detectedAt;
    }

    public Long getResolvedAt() {
        return resolvedAt;
    }

    public void setResolvedAt(Long resolvedAt) {
        this.resolvedAt = resolvedAt;
    }
}
