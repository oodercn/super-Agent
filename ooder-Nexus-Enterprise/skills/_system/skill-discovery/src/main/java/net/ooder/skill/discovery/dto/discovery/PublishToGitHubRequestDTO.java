package net.ooder.skill.discovery.dto.discovery;

public class PublishToGitHubRequestDTO {
    
    private String repoUrl;
    private String branch;
    private String token;
    private String commitMessage;
    private Boolean createPullRequest;
    
    public PublishToGitHubRequestDTO() {
    }
    
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
    
    public String getCommitMessage() {
        return commitMessage;
    }
    
    public void setCommitMessage(String commitMessage) {
        this.commitMessage = commitMessage;
    }
    
    public Boolean getCreatePullRequest() {
        return createPullRequest;
    }
    
    public void setCreatePullRequest(Boolean createPullRequest) {
        this.createPullRequest = createPullRequest;
    }
}
