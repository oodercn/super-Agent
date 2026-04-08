package net.ooder.mvp.skill.scene.dto.llm;

public class StatsTimeRange {
    
    private long startTime;
    private long endTime;
    private String granularity;

    public StatsTimeRange() {}

    public StatsTimeRange(long startTime, long endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public StatsTimeRange(long startTime, long endTime, String granularity) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.granularity = granularity;
    }

    public long getStartTime() { return startTime; }
    public void setStartTime(long startTime) { this.startTime = startTime; }
    public long getEndTime() { return endTime; }
    public void setEndTime(long endTime) { this.endTime = endTime; }
    public String getGranularity() { return granularity; }
    public void setGranularity(String granularity) { this.granularity = granularity; }
}
