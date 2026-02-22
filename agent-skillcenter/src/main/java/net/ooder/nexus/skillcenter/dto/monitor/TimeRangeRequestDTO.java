package net.ooder.nexus.skillcenter.dto.monitor;

public class TimeRangeRequestDTO {
    private long startTime;
    private long endTime;
    private String resolution;

    public long getStartTime() { return startTime; }
    public void setStartTime(long startTime) { this.startTime = startTime; }
    public long getEndTime() { return endTime; }
    public void setEndTime(long endTime) { this.endTime = endTime; }
    public String getResolution() { return resolution; }
    public void setResolution(String resolution) { this.resolution = resolution; }
}
