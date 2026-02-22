package net.ooder.nexus.domain.personal.model;

public class PlatformResult {
    private String platformId;
    private String status;
    private String articleId;
    private String url;
    private String error;
    private ContentStats stats;

    public PlatformResult() {}

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public ContentStats getStats() {
        return stats;
    }

    public void setStats(ContentStats stats) {
        this.stats = stats;
    }
}
