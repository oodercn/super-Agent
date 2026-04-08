package net.ooder.mvp.skill.scene.dto.history;

public class HistoryStatisticsDTO {
    
    private int totalScenes;
    private int participateCount;
    private int successRate;
    private int avgDuration;

    public HistoryStatisticsDTO() {
    }

    public HistoryStatisticsDTO(int totalScenes, int participateCount, int successRate, int avgDuration) {
        this.totalScenes = totalScenes;
        this.participateCount = participateCount;
        this.successRate = successRate;
        this.avgDuration = avgDuration;
    }

    public int getTotalScenes() {
        return totalScenes;
    }

    public void setTotalScenes(int totalScenes) {
        this.totalScenes = totalScenes;
    }

    public int getParticipateCount() {
        return participateCount;
    }

    public void setParticipateCount(int participateCount) {
        this.participateCount = participateCount;
    }

    public int getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(int successRate) {
        this.successRate = successRate;
    }

    public int getAvgDuration() {
        return avgDuration;
    }

    public void setAvgDuration(int avgDuration) {
        this.avgDuration = avgDuration;
    }
}
