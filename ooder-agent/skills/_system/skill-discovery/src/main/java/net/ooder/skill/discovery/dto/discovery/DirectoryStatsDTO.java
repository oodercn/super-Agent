package net.ooder.skill.discovery.dto.discovery;

public class DirectoryStatsDTO {
    
    private String directory;
    private String description;
    private int count;
    private int percentage;
    
    public DirectoryStatsDTO() {}
    
    public DirectoryStatsDTO(String directory, String description, int count, int total) {
        this.directory = directory;
        this.description = description;
        this.count = count;
        this.percentage = total > 0 ? Math.round((count * 100.0f) / total) : 0;
    }
    
    public String getDirectory() { return directory; }
    public void setDirectory(String directory) { this.directory = directory; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public int getCount() { return count; }
    public void setCount(int count) { this.count = count; }
    
    public int getPercentage() { return percentage; }
    public void setPercentage(int percentage) { this.percentage = percentage; }
}
