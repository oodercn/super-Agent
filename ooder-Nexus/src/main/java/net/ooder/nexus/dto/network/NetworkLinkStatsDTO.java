package net.ooder.nexus.dto.network;

import java.io.Serializable;
import java.util.Map;

public class NetworkLinkStatsDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer totalLinks;
    private Map<String, Integer> statusSummary;
    private Map<String, Integer> typeSummary;
    private Double averageLatency;
    private Double averageBandwidth;
    private Double averageReliability;
    private Double healthScore;

    public Integer getTotalLinks() {
        return totalLinks;
    }

    public void setTotalLinks(Integer totalLinks) {
        this.totalLinks = totalLinks;
    }

    public Map<String, Integer> getStatusSummary() {
        return statusSummary;
    }

    public void setStatusSummary(Map<String, Integer> statusSummary) {
        this.statusSummary = statusSummary;
    }

    public Map<String, Integer> getTypeSummary() {
        return typeSummary;
    }

    public void setTypeSummary(Map<String, Integer> typeSummary) {
        this.typeSummary = typeSummary;
    }

    public Double getAverageLatency() {
        return averageLatency;
    }

    public void setAverageLatency(Double averageLatency) {
        this.averageLatency = averageLatency;
    }

    public Double getAverageBandwidth() {
        return averageBandwidth;
    }

    public void setAverageBandwidth(Double averageBandwidth) {
        this.averageBandwidth = averageBandwidth;
    }

    public Double getAverageReliability() {
        return averageReliability;
    }

    public void setAverageReliability(Double averageReliability) {
        this.averageReliability = averageReliability;
    }

    public Double getHealthScore() {
        return healthScore;
    }

    public void setHealthScore(Double healthScore) {
        this.healthScore = healthScore;
    }
}
