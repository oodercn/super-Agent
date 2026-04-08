package net.ooder.mvp.skill.scene.dto.discovery;

public class GitDiscoveryRequestDTO {
    
    private String repoUrl = "https://github.com/ooderCN/skills";
    
    private String branch = "main";
    
    private String token;
    
    private String username;
    
    private String password;

    public GitDiscoveryRequestDTO() {}

    public String getRepoUrl() {
        return repoUrl;
    }

    public void setRepoUrl(String repoUrl) {
        this.repoUrl = repoUrl;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
