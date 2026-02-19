
package net.ooder.sdk.core.skill.discovery;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ooder.sdk.api.skill.SkillDiscoverer;
import net.ooder.sdk.api.skill.SkillManifest;
import net.ooder.sdk.api.skill.SkillPackage;
import net.ooder.sdk.common.enums.DiscoveryMethod;

public class GitRepositoryDiscovererAdapter implements SkillDiscoverer {
    
    private static final Logger log = LoggerFactory.getLogger(GitRepositoryDiscovererAdapter.class);
    
    private long timeout = 60000;
    private DiscoveryFilter filter;
    private String defaultOwner;
    private String defaultRepo;
    private String defaultBranch = "main";
    private String githubToken;
    private String giteeToken;
    private String source = "github";
    private final Map<String, GitRepositoryConfig> repositoryConfigs = new ConcurrentHashMap<String, GitRepositoryConfig>();
    
    public GitRepositoryDiscovererAdapter() {
    }
    
    public GitRepositoryDiscovererAdapter(String source) {
        this.source = source;
    }
    
    @Override
    public CompletableFuture<List<SkillPackage>> discover() {
        return CompletableFuture.supplyAsync(() -> {
            List<SkillPackage> packages = new ArrayList<SkillPackage>();
            log.info("Discovering skills from Git repository: {}/{}", defaultOwner, defaultRepo);
            return packages;
        });
    }
    
    @Override
    public CompletableFuture<SkillPackage> discover(String skillId) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("Discovering skill from Git repository: {}", skillId);
            
            SkillPackage pkg = new SkillPackage();
            pkg.setSkillId(skillId);
            pkg.setName(skillId);
            pkg.setVersion("1.0.0");
            pkg.setSource(source + ":" + defaultOwner + "/" + defaultRepo);
            pkg.setSceneId("default");
            
            SkillManifest manifest = new SkillManifest();
            manifest.setSkillId(skillId);
            manifest.setName(skillId);
            manifest.setVersion("1.0.0");
            pkg.setManifest(manifest);
            
            return pkg;
        });
    }
    
    @Override
    public CompletableFuture<List<SkillPackage>> discoverByScene(String sceneId) {
        return CompletableFuture.supplyAsync(() -> {
            List<SkillPackage> packages = new ArrayList<SkillPackage>();
            log.info("Discovering skills for scene from Git repository: {}", sceneId);
            return packages;
        });
    }
    
    @Override
    public CompletableFuture<List<SkillPackage>> search(String query) {
        return CompletableFuture.supplyAsync(() -> {
            List<SkillPackage> packages = new ArrayList<SkillPackage>();
            log.info("Searching skills from Git repository: {}", query);
            return packages;
        });
    }
    
    @Override
    public CompletableFuture<List<SkillPackage>> searchByCapability(String capabilityId) {
        return CompletableFuture.supplyAsync(() -> {
            List<SkillPackage> packages = new ArrayList<SkillPackage>();
            log.info("Searching skills by capability from Git repository: {}", capabilityId);
            return packages;
        });
    }
    
    @Override
    public DiscoveryMethod getMethod() {
        if ("gitee".equalsIgnoreCase(source)) {
            return DiscoveryMethod.GITEE;
        } else if ("github".equalsIgnoreCase(source)) {
            return DiscoveryMethod.GITHUB;
        }
        return DiscoveryMethod.GIT_REPOSITORY;
    }
    
    @Override
    public boolean isAvailable() {
        return true;
    }
    
    @Override
    public void setTimeout(long timeoutMs) {
        this.timeout = timeoutMs;
    }
    
    @Override
    public long getTimeout() {
        return timeout;
    }
    
    @Override
    public void setFilter(DiscoveryFilter filter) {
        this.filter = filter;
    }
    
    @Override
    public DiscoveryFilter getFilter() {
        return filter;
    }
    
    public void setDefaultOwner(String owner) {
        this.defaultOwner = owner;
    }
    
    public String getDefaultOwner() {
        return defaultOwner;
    }
    
    public void setDefaultRepo(String repo) {
        this.defaultRepo = repo;
    }
    
    public String getDefaultRepo() {
        return defaultRepo;
    }
    
    public void setDefaultBranch(String branch) {
        this.defaultBranch = branch;
    }
    
    public String getDefaultBranch() {
        return defaultBranch;
    }
    
    public void setGithubToken(String token) {
        this.githubToken = token;
    }
    
    public String getGithubToken() {
        return githubToken;
    }
    
    public void setGiteeToken(String token) {
        this.giteeToken = token;
    }
    
    public String getGiteeToken() {
        return giteeToken;
    }
    
    public void setSource(String source) {
        this.source = source;
    }
    
    public String getSource() {
        return source;
    }
    
    public void addRepositoryConfig(String name, GitRepositoryConfig config) {
        repositoryConfigs.put(name, config);
    }
    
    public GitRepositoryConfig getRepositoryConfig(String name) {
        return repositoryConfigs.get(name);
    }
    
    public static class GitRepositoryConfig {
        private String owner;
        private String repo;
        private String branch = "main";
        private String token;
        private String baseUrl;
        
        public String getOwner() { return owner; }
        public void setOwner(String owner) { this.owner = owner; }
        
        public String getRepo() { return repo; }
        public void setRepo(String repo) { this.repo = repo; }
        
        public String getBranch() { return branch; }
        public void setBranch(String branch) { this.branch = branch; }
        
        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
        
        public String getBaseUrl() { return baseUrl; }
        public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }
    }
}
