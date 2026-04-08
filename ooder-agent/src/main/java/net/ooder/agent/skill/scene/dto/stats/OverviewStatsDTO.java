package net.ooder.mvp.skill.scene.dto.stats;

public class OverviewStatsDTO {
    
    private Integer capabilities;
    private Integer users;
    private Integer scenes;
    private Integer activeScenes;
    private Integer totalCalls;
    
    public OverviewStatsDTO() {
    }
    
    public Integer getCapabilities() { return capabilities; }
    public void setCapabilities(Integer capabilities) { this.capabilities = capabilities; }
    
    public Integer getUsers() { return users; }
    public void setUsers(Integer users) { this.users = users; }
    
    public Integer getScenes() { return scenes; }
    public void setScenes(Integer scenes) { this.scenes = scenes; }
    
    public Integer getActiveScenes() { return activeScenes; }
    public void setActiveScenes(Integer activeScenes) { this.activeScenes = activeScenes; }
    
    public Integer getTotalCalls() { return totalCalls; }
    public void setTotalCalls(Integer totalCalls) { this.totalCalls = totalCalls; }
}
