package net.ooder.nexus.skillcenter.dto.skill;

public class DiscoverySourceDTO {
    private String id;
    private String name;
    private String description;
    private boolean available;
    private String status;
    private int skillCount;
    private long lastScanTime;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public int getSkillCount() { return skillCount; }
    public void setSkillCount(int skillCount) { this.skillCount = skillCount; }
    public long getLastScanTime() { return lastScanTime; }
    public void setLastScanTime(long lastScanTime) { this.lastScanTime = lastScanTime; }
}
