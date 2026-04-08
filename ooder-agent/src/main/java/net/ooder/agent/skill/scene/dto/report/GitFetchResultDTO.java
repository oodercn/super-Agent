package net.ooder.mvp.skill.scene.dto.report;

import java.util.List;

public class GitFetchResultDTO {
    private String userId;
    private String repoUrl;
    private List<CommitItem> commits;
    private String summary;
    private List<String> workItems;

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getRepoUrl() { return repoUrl; }
    public void setRepoUrl(String repoUrl) { this.repoUrl = repoUrl; }
    public List<CommitItem> getCommits() { return commits; }
    public void setCommits(List<CommitItem> commits) { this.commits = commits; }
    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }
    public List<String> getWorkItems() { return workItems; }
    public void setWorkItems(List<String> workItems) { this.workItems = workItems; }

    public static class CommitItem {
        private String commitId;
        private String message;
        private String branch;
        private Long time;
        private Integer filesChanged;

        public String getCommitId() { return commitId; }
        public void setCommitId(String commitId) { this.commitId = commitId; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public String getBranch() { return branch; }
        public void setBranch(String branch) { this.branch = branch; }
        public Long getTime() { return time; }
        public void setTime(Long time) { this.time = time; }
        public Integer getFilesChanged() { return filesChanged; }
        public void setFilesChanged(Integer filesChanged) { this.filesChanged = filesChanged; }
    }
}
