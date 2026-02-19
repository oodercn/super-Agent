package net.ooder.sdk.discovery.git;

import java.util.ArrayList;
import java.util.List;

/**
 * Git Repository Discovery Configuration
 *
 * @author ooder Team
 * @since 2.0
 */
public class GitDiscoveryConfig {

    private String platform;
    private String apiBaseUrl;
    private String webBaseUrl;
    private String token;
    private String defaultOwner;
    private String skillsRepo;
    private String skillsPath;
    private boolean singleRepoMode;
    private long cacheTtlMs;
    private int requestTimeoutMs;
    private int maxRetries;
    private boolean enabled;
    private List<String> additionalOwners;

    public GitDiscoveryConfig() {
        this.cacheTtlMs = 3600000;
        this.requestTimeoutMs = 30000;
        this.maxRetries = 3;
        this.enabled = true;
        this.skillsRepo = "skills";
        this.skillsPath = "skills";
        this.singleRepoMode = true;
        this.additionalOwners = new ArrayList<>();
    }

    public static GitDiscoveryConfig forGitHub() {
        GitDiscoveryConfig config = new GitDiscoveryConfig();
        config.setPlatform("github");
        config.setApiBaseUrl("https://api.github.com");
        config.setWebBaseUrl("https://github.com");
        return config;
    }

    public static GitDiscoveryConfig forGitHub(String token) {
        GitDiscoveryConfig config = forGitHub();
        config.setToken(token);
        return config;
    }

    public static GitDiscoveryConfig forGitHub(String token, String defaultOwner) {
        GitDiscoveryConfig config = forGitHub(token);
        config.setDefaultOwner(defaultOwner);
        return config;
    }

    public static GitDiscoveryConfig forGitHub(String token, String defaultOwner, String skillsRepo) {
        GitDiscoveryConfig config = forGitHub(token, defaultOwner);
        config.setSkillsRepo(skillsRepo);
        return config;
    }

    public static GitDiscoveryConfig forGitee() {
        GitDiscoveryConfig config = new GitDiscoveryConfig();
        config.setPlatform("gitee");
        config.setApiBaseUrl("https://gitee.com/api/v5");
        config.setWebBaseUrl("https://gitee.com");
        return config;
    }

    public static GitDiscoveryConfig forGitee(String token) {
        GitDiscoveryConfig config = forGitee();
        config.setToken(token);
        return config;
    }

    public static GitDiscoveryConfig forGitee(String token, String defaultOwner) {
        GitDiscoveryConfig config = forGitee(token);
        config.setDefaultOwner(defaultOwner);
        return config;
    }

    public static GitDiscoveryConfig forGitee(String token, String defaultOwner, String skillsRepo) {
        GitDiscoveryConfig config = forGitee(token, defaultOwner);
        config.setSkillsRepo(skillsRepo);
        return config;
    }

    public String getPlatform() { return platform; }
    public void setPlatform(String platform) { this.platform = platform; }
    public String getApiBaseUrl() { return apiBaseUrl; }
    public void setApiBaseUrl(String apiBaseUrl) { this.apiBaseUrl = apiBaseUrl; }
    public String getWebBaseUrl() { return webBaseUrl; }
    public void setWebBaseUrl(String webBaseUrl) { this.webBaseUrl = webBaseUrl; }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getDefaultOwner() { return defaultOwner; }
    public void setDefaultOwner(String defaultOwner) { this.defaultOwner = defaultOwner; }
    public String getSkillsRepo() { return skillsRepo; }
    public void setSkillsRepo(String skillsRepo) { this.skillsRepo = skillsRepo; }
    public String getSkillsPath() { return skillsPath; }
    public void setSkillsPath(String skillsPath) { this.skillsPath = skillsPath; }
    public boolean isSingleRepoMode() { return singleRepoMode; }
    public void setSingleRepoMode(boolean singleRepoMode) { this.singleRepoMode = singleRepoMode; }
    public long getCacheTtlMs() { return cacheTtlMs; }
    public void setCacheTtlMs(long cacheTtlMs) { this.cacheTtlMs = cacheTtlMs; }
    public int getRequestTimeoutMs() { return requestTimeoutMs; }
    public void setRequestTimeoutMs(int requestTimeoutMs) { this.requestTimeoutMs = requestTimeoutMs; }
    public int getMaxRetries() { return maxRetries; }
    public void setMaxRetries(int maxRetries) { this.maxRetries = maxRetries; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public List<String> getAdditionalOwners() { return additionalOwners; }
    public void setAdditionalOwners(List<String> additionalOwners) { this.additionalOwners = additionalOwners; }
}
