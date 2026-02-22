package net.ooder.skillcenter.discovery;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.ooder.skillcenter.dto.scene.SkillInfoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GitDiscoveryService {

    private static final Logger logger = LoggerFactory.getLogger(GitDiscoveryService.class);

    @Value("${skill.discovery.github.enabled:false}")
    private boolean githubEnabled;

    @Value("${skill.discovery.github.api-url:https://api.github.com}")
    private String githubApiUrl;

    @Value("${skill.discovery.github.token:}")
    private String githubToken;

    @Value("${skill.discovery.github.default-owner:}")
    private String githubDefaultOwner;

    @Value("${skill.discovery.github.skills-path:skills}")
    private String githubSkillsPath;

    @Value("${skill.discovery.github.skills-repo:skills}")
    private String githubSkillsRepo;

    @Value("${skill.discovery.gitee.enabled:false}")
    private boolean giteeEnabled;

    @Value("${skill.discovery.gitee.api-url:https://gitee.com/api/v5}")
    private String giteeApiUrl;

    @Value("${skill.discovery.gitee.token:}")
    private String giteeToken;

    @Value("${skill.discovery.gitee.default-owner:}")
    private String giteeDefaultOwner;

    @Value("${skill.discovery.gitee.skills-path:skills}")
    private String giteeSkillsPath;

    @Value("${skill.discovery.gitee.skills-repo:skills}")
    private String giteeSkillsRepo;

    @Value("${skill.discovery.cache-ttl:3600000}")
    private long cacheTtl;

    private RestTemplate restTemplate;
    private final Map<String, List<SkillInfoDTO>> skillCache = new ConcurrentHashMap<>();
    private final Map<String, Long> cacheTimestamp = new ConcurrentHashMap<>();

    private static final String SOURCE_GITHUB = "github";
    private static final String SOURCE_GITEE = "gitee";

    @PostConstruct
    public void init() {
        restTemplate = new RestTemplate();
        logger.info("GitDiscoveryService initialized. GitHub: {}, Gitee: {}", githubEnabled, giteeEnabled);
    }

    private HttpHeaders createGitHubHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/vnd.github.v3+json");
        if (githubToken != null && !githubToken.isEmpty()) {
            headers.set("Authorization", "token " + githubToken);
        }
        return headers;
    }

    private HttpHeaders createGiteeHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        return headers;
    }

    public CompletableFuture<List<SkillInfoDTO>> discoverFromGitHub() {
        return discoverFromGitHub(githubDefaultOwner);
    }

    public CompletableFuture<List<SkillInfoDTO>> discoverFromGitHub(String owner) {
        return CompletableFuture.supplyAsync(() -> {
            if (!githubEnabled || owner == null || owner.isEmpty()) {
                return new ArrayList<SkillInfoDTO>();
            }

            String cacheKey = SOURCE_GITHUB + ":" + owner;
            if (isCacheValid(cacheKey)) {
                return skillCache.get(cacheKey);
            }

            try {
                List<SkillInfoDTO> skills = new ArrayList<>();
                String url = githubApiUrl + "/repos/" + owner + "/" + githubSkillsRepo + "/contents/" + githubSkillsPath;
                
                HttpEntity<String> entity = new HttpEntity<>(createGitHubHeaders());
                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
                
                if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                    JSONArray contents = JSON.parseArray(response.getBody());
                    for (int i = 0; i < contents.size(); i++) {
                        JSONObject item = contents.getJSONObject(i);
                        if ("dir".equals(item.getString("type"))) {
                            SkillInfoDTO skill = new SkillInfoDTO();
                            skill.setSkillId(item.getString("name"));
                            skill.setName(item.getString("name"));
                            skill.setDescription("Discovered from GitHub: " + owner + "/" + item.getString("name"));
                            skill.setStatus("discovered");
                            skills.add(skill);
                        }
                    }
                }

                updateCache(cacheKey, skills);
                return skills;
            } catch (Exception e) {
                logger.error("Failed to discover skills from GitHub: {}", e.getMessage());
                return new ArrayList<SkillInfoDTO>();
            }
        });
    }

    public CompletableFuture<List<SkillInfoDTO>> discoverFromGitHub(String owner, String skillsPath) {
        return CompletableFuture.supplyAsync(() -> {
            if (!githubEnabled || owner == null || owner.isEmpty()) {
                return new ArrayList<SkillInfoDTO>();
            }

            try {
                List<SkillInfoDTO> skills = new ArrayList<>();
                String url = githubApiUrl + "/repos/" + owner + "/" + githubSkillsRepo + "/contents/" + skillsPath;
                
                HttpEntity<String> entity = new HttpEntity<>(createGitHubHeaders());
                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
                
                if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                    JSONArray contents = JSON.parseArray(response.getBody());
                    for (int i = 0; i < contents.size(); i++) {
                        JSONObject item = contents.getJSONObject(i);
                        if ("dir".equals(item.getString("type"))) {
                            SkillInfoDTO skill = new SkillInfoDTO();
                            skill.setSkillId(item.getString("name"));
                            skill.setName(item.getString("name"));
                            skill.setDescription("Discovered from GitHub: " + owner + "/" + item.getString("name"));
                            skill.setStatus("discovered");
                            skills.add(skill);
                        }
                    }
                }

                return skills;
            } catch (Exception e) {
                logger.error("Failed to discover skills from GitHub: {}", e.getMessage());
                return new ArrayList<SkillInfoDTO>();
            }
        });
    }

    public CompletableFuture<SkillInfoDTO> discoverGitHubSkill(String owner, String repoName) {
        return CompletableFuture.supplyAsync(() -> {
            if (!githubEnabled) {
                return null;
            }

            try {
                String url = githubApiUrl + "/repos/" + owner + "/" + repoName;
                
                HttpEntity<String> entity = new HttpEntity<>(createGitHubHeaders());
                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
                
                if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                    JSONObject repo = JSON.parseObject(response.getBody());
                    SkillInfoDTO skill = new SkillInfoDTO();
                    skill.setSkillId(repoName);
                    skill.setName(repo.getString("name"));
                    skill.setDescription(repo.getString("description"));
                    skill.setStatus("discovered");
                    return skill;
                }

                return null;
            } catch (Exception e) {
                logger.error("Failed to discover skill from GitHub: {}", e.getMessage());
                return null;
            }
        });
    }

    public CompletableFuture<List<SkillInfoDTO>> discoverFromGitee() {
        return discoverFromGitee(giteeDefaultOwner);
    }

    public CompletableFuture<List<SkillInfoDTO>> discoverFromGitee(String owner) {
        return CompletableFuture.supplyAsync(() -> {
            if (!giteeEnabled || owner == null || owner.isEmpty()) {
                return new ArrayList<SkillInfoDTO>();
            }

            String cacheKey = SOURCE_GITEE + ":" + owner;
            if (isCacheValid(cacheKey)) {
                return skillCache.get(cacheKey);
            }

            try {
                List<SkillInfoDTO> skills = new ArrayList<>();
                String url = giteeApiUrl + "/repos/" + owner + "/" + giteeSkillsRepo + "/contents/" + giteeSkillsPath;
                if (giteeToken != null && !giteeToken.isEmpty()) {
                    url += "?access_token=" + giteeToken;
                }
                
                HttpEntity<String> entity = new HttpEntity<>(createGiteeHeaders());
                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
                
                if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                    JSONArray contents = JSON.parseArray(response.getBody());
                    for (int i = 0; i < contents.size(); i++) {
                        JSONObject item = contents.getJSONObject(i);
                        if ("dir".equals(item.getString("type"))) {
                            SkillInfoDTO skill = new SkillInfoDTO();
                            skill.setSkillId(item.getString("name"));
                            skill.setName(item.getString("name"));
                            skill.setDescription("Discovered from Gitee: " + owner + "/" + item.getString("name"));
                            skill.setStatus("discovered");
                            skills.add(skill);
                        }
                    }
                }

                updateCache(cacheKey, skills);
                return skills;
            } catch (Exception e) {
                logger.error("Failed to discover skills from Gitee: {}", e.getMessage());
                return new ArrayList<SkillInfoDTO>();
            }
        });
    }

    public CompletableFuture<List<SkillInfoDTO>> discoverFromGitee(String owner, String skillsPath) {
        return CompletableFuture.supplyAsync(() -> {
            if (!giteeEnabled || owner == null || owner.isEmpty()) {
                return new ArrayList<SkillInfoDTO>();
            }

            try {
                List<SkillInfoDTO> skills = new ArrayList<>();
                String url = giteeApiUrl + "/repos/" + owner + "/" + giteeSkillsRepo + "/contents/" + skillsPath;
                if (giteeToken != null && !giteeToken.isEmpty()) {
                    url += "?access_token=" + giteeToken;
                }
                
                HttpEntity<String> entity = new HttpEntity<>(createGiteeHeaders());
                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
                
                if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                    JSONArray contents = JSON.parseArray(response.getBody());
                    for (int i = 0; i < contents.size(); i++) {
                        JSONObject item = contents.getJSONObject(i);
                        if ("dir".equals(item.getString("type"))) {
                            SkillInfoDTO skill = new SkillInfoDTO();
                            skill.setSkillId(item.getString("name"));
                            skill.setName(item.getString("name"));
                            skill.setDescription("Discovered from Gitee: " + owner + "/" + item.getString("name"));
                            skill.setStatus("discovered");
                            skills.add(skill);
                        }
                    }
                }

                return skills;
            } catch (Exception e) {
                logger.error("Failed to discover skills from Gitee: {}", e.getMessage());
                return new ArrayList<SkillInfoDTO>();
            }
        });
    }

    public CompletableFuture<SkillInfoDTO> discoverGiteeSkill(String owner, String repoName) {
        return CompletableFuture.supplyAsync(() -> {
            if (!giteeEnabled) {
                return null;
            }

            try {
                String url = giteeApiUrl + "/repos/" + owner + "/" + repoName;
                if (giteeToken != null && !giteeToken.isEmpty()) {
                    url += "?access_token=" + giteeToken;
                }
                
                HttpEntity<String> entity = new HttpEntity<>(createGiteeHeaders());
                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
                
                if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                    JSONObject repo = JSON.parseObject(response.getBody());
                    SkillInfoDTO skill = new SkillInfoDTO();
                    skill.setSkillId(repoName);
                    skill.setName(repo.getString("name"));
                    skill.setDescription(repo.getString("description"));
                    skill.setStatus("discovered");
                    return skill;
                }

                return null;
            } catch (Exception e) {
                logger.error("Failed to discover skill from Gitee: {}", e.getMessage());
                return null;
            }
        });
    }

    public CompletableFuture<List<SkillInfoDTO>> discoverFromAll() {
        List<CompletableFuture<List<SkillInfoDTO>>> futures = new ArrayList<>();
        
        if (githubEnabled) {
            futures.add(discoverFromGitHub());
        }
        if (giteeEnabled) {
            futures.add(discoverFromGitee());
        }

        if (futures.isEmpty()) {
            return CompletableFuture.completedFuture(new ArrayList<>());
        }

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
            .thenApply(v -> {
                List<SkillInfoDTO> allPackages = new ArrayList<>();
                for (CompletableFuture<List<SkillInfoDTO>> future : futures) {
                    try {
                        allPackages.addAll(future.get());
                    } catch (Exception e) {
                        logger.error("Failed to get discovery results", e);
                    }
                }
                return allPackages;
            });
    }

    public CompletableFuture<List<SkillDirectory>> listGitHubSkillDirectories(String owner) {
        return CompletableFuture.supplyAsync(() -> {
            List<SkillDirectory> directories = new ArrayList<>();
            if (!githubEnabled || owner == null || owner.isEmpty()) {
                return directories;
            }

            try {
                String url = githubApiUrl + "/repos/" + owner + "/" + githubSkillsRepo + "/contents/" + githubSkillsPath;
                
                HttpEntity<String> entity = new HttpEntity<>(createGitHubHeaders());
                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
                
                if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                    JSONArray contents = JSON.parseArray(response.getBody());
                    for (int i = 0; i < contents.size(); i++) {
                        JSONObject item = contents.getJSONObject(i);
                        if ("dir".equals(item.getString("type"))) {
                            SkillDirectory dir = new SkillDirectory();
                            dir.setName(item.getString("name"));
                            dir.setPath(item.getString("path"));
                            dir.setUrl(item.getString("url"));
                            directories.add(dir);
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("Failed to list GitHub skill directories: {}", e.getMessage());
            }

            return directories;
        });
    }

    public CompletableFuture<List<SkillDirectory>> listGiteeSkillDirectories(String owner) {
        return CompletableFuture.supplyAsync(() -> {
            List<SkillDirectory> directories = new ArrayList<>();
            if (!giteeEnabled || owner == null || owner.isEmpty()) {
                return directories;
            }

            try {
                String url = giteeApiUrl + "/repos/" + owner + "/" + giteeSkillsRepo + "/contents/" + giteeSkillsPath;
                if (giteeToken != null && !giteeToken.isEmpty()) {
                    url += "?access_token=" + giteeToken;
                }
                
                HttpEntity<String> entity = new HttpEntity<>(createGiteeHeaders());
                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
                
                if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                    JSONArray contents = JSON.parseArray(response.getBody());
                    for (int i = 0; i < contents.size(); i++) {
                        JSONObject item = contents.getJSONObject(i);
                        if ("dir".equals(item.getString("type"))) {
                            SkillDirectory dir = new SkillDirectory();
                            dir.setName(item.getString("name"));
                            dir.setPath(item.getString("path"));
                            dir.setUrl(item.getString("url"));
                            directories.add(dir);
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("Failed to list Gitee skill directories: {}", e.getMessage());
            }

            return directories;
        });
    }

    public CompletableFuture<ReleaseInfo> getGitHubRelease(String owner, String repoName) {
        return CompletableFuture.supplyAsync(() -> {
            if (!githubEnabled) {
                return null;
            }

            try {
                String url = githubApiUrl + "/repos/" + owner + "/" + repoName + "/releases/latest";
                
                HttpEntity<String> entity = new HttpEntity<>(createGitHubHeaders());
                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
                
                if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                    JSONObject release = JSON.parseObject(response.getBody());
                    ReleaseInfo info = new ReleaseInfo();
                    info.setTagName(release.getString("tag_name"));
                    info.setName(release.getString("name"));
                    info.setBody(release.getString("body"));
                    info.setHtmlUrl(release.getString("html_url"));
                    return info;
                }

                return null;
            } catch (Exception e) {
                logger.error("Failed to get GitHub release: {}", e.getMessage());
                return null;
            }
        });
    }

    public CompletableFuture<ReleaseInfo> getGiteeRelease(String owner, String repoName) {
        return CompletableFuture.supplyAsync(() -> {
            if (!giteeEnabled) {
                return null;
            }

            try {
                String url = giteeApiUrl + "/repos/" + owner + "/" + repoName + "/releases/latest";
                if (giteeToken != null && !giteeToken.isEmpty()) {
                    url += "?access_token=" + giteeToken;
                }
                
                HttpEntity<String> entity = new HttpEntity<>(createGiteeHeaders());
                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
                
                if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                    JSONObject release = JSON.parseObject(response.getBody());
                    ReleaseInfo info = new ReleaseInfo();
                    info.setTagName(release.getString("tag_name"));
                    info.setName(release.getString("name"));
                    info.setBody(release.getString("body"));
                    info.setHtmlUrl(release.getString("html_url"));
                    return info;
                }

                return null;
            } catch (Exception e) {
                logger.error("Failed to get Gitee release: {}", e.getMessage());
                return null;
            }
        });
    }

    public CompletableFuture<String> getGitHubManifestContent(String owner, String repoName) {
        return CompletableFuture.supplyAsync(() -> {
            if (!githubEnabled) {
                return null;
            }

            try {
                String url = githubApiUrl + "/repos/" + owner + "/" + repoName + "/contents/skill.yaml";
                
                HttpEntity<String> entity = new HttpEntity<>(createGitHubHeaders());
                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
                
                if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                    JSONObject content = JSON.parseObject(response.getBody());
                    String downloadUrl = content.getString("download_url");
                    if (downloadUrl != null) {
                        ResponseEntity<String> contentResponse = restTemplate.exchange(downloadUrl, HttpMethod.GET, null, String.class);
                        return contentResponse.getBody();
                    }
                }

                return null;
            } catch (Exception e) {
                logger.error("Failed to get GitHub manifest: {}", e.getMessage());
                return null;
            }
        });
    }

    public CompletableFuture<String> getGiteeManifestContent(String owner, String repoName) {
        return CompletableFuture.supplyAsync(() -> {
            if (!giteeEnabled) {
                return null;
            }

            try {
                String url = giteeApiUrl + "/repos/" + owner + "/" + repoName + "/contents/skill.yaml";
                if (giteeToken != null && !giteeToken.isEmpty()) {
                    url += "?access_token=" + giteeToken;
                }
                
                HttpEntity<String> entity = new HttpEntity<>(createGiteeHeaders());
                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
                
                if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                    JSONObject content = JSON.parseObject(response.getBody());
                    String downloadUrl = content.getString("download_url");
                    if (downloadUrl != null) {
                        ResponseEntity<String> contentResponse = restTemplate.exchange(downloadUrl, HttpMethod.GET, null, String.class);
                        return contentResponse.getBody();
                    }
                }

                return null;
            } catch (Exception e) {
                logger.error("Failed to get Gitee manifest: {}", e.getMessage());
                return null;
            }
        });
    }

    private boolean isCacheValid(String cacheKey) {
        if (!skillCache.containsKey(cacheKey) || !cacheTimestamp.containsKey(cacheKey)) {
            return false;
        }
        long timestamp = cacheTimestamp.get(cacheKey);
        return System.currentTimeMillis() - timestamp < cacheTtl;
    }

    private void updateCache(String cacheKey, List<SkillInfoDTO> skills) {
        skillCache.put(cacheKey, skills);
        cacheTimestamp.put(cacheKey, System.currentTimeMillis());
    }

    public boolean isGitHubEnabled() {
        return githubEnabled;
    }

    public boolean isGiteeEnabled() {
        return giteeEnabled;
    }

    public static class SkillDirectory {
        private String name;
        private String path;
        private String url;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getPath() { return path; }
        public void setPath(String path) { this.path = path; }
        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }
    }

    public static class ReleaseInfo {
        private String tagName;
        private String name;
        private String body;
        private String htmlUrl;

        public String getTagName() { return tagName; }
        public void setTagName(String tagName) { this.tagName = tagName; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getBody() { return body; }
        public void setBody(String body) { this.body = body; }
        public String getHtmlUrl() { return htmlUrl; }
        public void setHtmlUrl(String htmlUrl) { this.htmlUrl = htmlUrl; }
    }
}
