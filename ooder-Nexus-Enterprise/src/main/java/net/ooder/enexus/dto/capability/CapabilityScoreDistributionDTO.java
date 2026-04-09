package net.ooder.enexus.dto.capability;

import java.util.List;

public class CapabilityScoreDistributionDTO {
    private Double avgScore;
    private Integer highCount;
    private Integer mediumCount;
    private Integer lowCount;
    private List<Integer> distribution;

    public CapabilityScoreDistributionDTO() {}

    public Double getAvgScore() {
        return avgScore;
    }

    public void setAvgScore(Double avgScore) {
        this.avgScore = avgScore;
    }

    public Integer getHighCount() {
        return highCount;
    }

    public void setHighCount(Integer highCount) {
        this.highCount = highCount;
    }

    public Integer getMediumCount() {
        return mediumCount;
    }

    public void setMediumCount(Integer mediumCount) {
        this.mediumCount = mediumCount;
    }

    public Integer getLowCount() {
        return lowCount;
    }

    public void setLowCount(Integer lowCount) {
        this.lowCount = lowCount;
    }

    public List<Integer> getDistribution() {
        return distribution;
    }

    public void setDistribution(List<Integer> distribution) {
        this.distribution = distribution;
    }
}
