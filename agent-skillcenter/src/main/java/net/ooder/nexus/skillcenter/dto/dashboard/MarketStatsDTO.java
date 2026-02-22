package net.ooder.nexus.skillcenter.dto.dashboard;

import net.ooder.nexus.skillcenter.dto.BaseDTO;
import java.util.Map;

public class MarketStatsDTO extends BaseDTO {

    private int totalMarketSkills;
    private int totalDownloads;
    private int totalReviews;
    private double averageRating;
    private Map<String, Integer> topDownloadedSkills;
    private Map<String, Integer> marketTrend;
    private Map<String, Integer> categoryDistribution;

    public MarketStatsDTO() {}

    public int getTotalMarketSkills() {
        return totalMarketSkills;
    }

    public void setTotalMarketSkills(int totalMarketSkills) {
        this.totalMarketSkills = totalMarketSkills;
    }

    public int getTotalDownloads() {
        return totalDownloads;
    }

    public void setTotalDownloads(int totalDownloads) {
        this.totalDownloads = totalDownloads;
    }

    public int getTotalReviews() {
        return totalReviews;
    }

    public void setTotalReviews(int totalReviews) {
        this.totalReviews = totalReviews;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public Map<String, Integer> getTopDownloadedSkills() {
        return topDownloadedSkills;
    }

    public void setTopDownloadedSkills(Map<String, Integer> topDownloadedSkills) {
        this.topDownloadedSkills = topDownloadedSkills;
    }

    public Map<String, Integer> getMarketTrend() {
        return marketTrend;
    }

    public void setMarketTrend(Map<String, Integer> marketTrend) {
        this.marketTrend = marketTrend;
    }

    public Map<String, Integer> getCategoryDistribution() {
        return categoryDistribution;
    }

    public void setCategoryDistribution(Map<String, Integer> categoryDistribution) {
        this.categoryDistribution = categoryDistribution;
    }
}
