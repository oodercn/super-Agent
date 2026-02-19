package net.ooder.sdk.discovery.git;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.ooder.sdk.api.skill.SkillPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * GitHub Repository Discoverer Implementation
 * 
 * Supports two modes:
 * 1. Single Repository Mode (default): All skills in one repository (e.g., oodercn/skills)
 * 2. Multi Repository Mode: Each skill is a separate repository
 *
 * @author ooder Team
 * @since 2.0
 */
public class GitHubDiscoverer implements GitRepositoryDiscoverer {

    private static final Logger logger = LoggerFactory.getLogger(GitHubDiscoverer.class);
    private static final String MANIFEST_FILE = "skill-manifest.yaml";

    private final GitDiscoveryConfig config;
    private final ObjectMapper objectMapper;
    private final ConcurrentHashMap<String, CacheEntry> cache;

    public GitHubDiscoverer(GitDiscoveryConfig config) {
        this.config = config;
        this.objectMapper = new ObjectMapper();
        this.cache = new ConcurrentHashMap<>();
    }

    public GitHubDiscoverer() {
        this(GitDiscoveryConfig.forGitHub());
    }

    public GitHubDiscoverer(String token) {
        this(GitDiscoveryConfig.forGitHub(token));
    }

    public GitHubDiscoverer(String token, String defaultOwner) {
        this(GitDiscoveryConfig.forGitHub(token, defaultOwner));
    }

    public GitHubDiscoverer(String token, String defaultOwner, String skillsRepo) {
        this(GitDiscoveryConfig.forGitHub(token, defaultOwner, skillsRepo));
    }

    @Override
    public GitDiscoveryConfig getConfig() {
        return config;
    }

    @Override
    public CompletableFuture<List<SkillPackage>> discoverSkills() {
        if (config.getDefaultOwner() == null || config.getDefaultOwner().isEmpty()) {
            return CompletableFuture.completedFuture(new ArrayList<>());
        }
        return discoverSkills(config.getDefaultOwner());
    }

    @Override
    public CompletableFuture<List<SkillPackage>> discoverSkills(String owner) {
        return discoverSkills(owner, config.getSkillsPath());
    }

    @Override
    public CompletableFuture<List<SkillPackage>> discoverSkills(String owner, String skillsPath) {
        return CompletableFuture.supplyAsync(() -> {
            List<SkillPackage> packages = new ArrayList<>();
            try {
                List<SkillDirectory> directories = listSkillDirectories(owner, skillsPath).get();
                for (SkillDirectory dir : directories) {
                    if (dir.isHasManifest()) {
                        SkillPackage pkg = buildSkillPackage(owner, dir);
                        if (pkg != null) {
                            packages.add(pkg);
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("Failed to discover skills from {}/{}", owner, skillsPath, e);
            }
            return packages;
        });
    }

    @Override
    public CompletableFuture<SkillPackage> discoverSkill(String owner, String skillName) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String manifestPath;
                if (config.isSingleRepoMode()) {
                    manifestPath = config.getSkillsPath() + "/" + skillName + "/" + MANIFEST_FILE;
                } else {
                    manifestPath = MANIFEST_FILE;
                }
                
                boolean hasManifest = hasSkillManifest(owner, skillName).get();
                if (!hasManifest) {
                    return null;
                }
                
                SkillDirectory dir = new SkillDirectory();
                dir.setName(skillName);
                dir.setOwner(owner);
                dir.setRepoName(config.isSingleRepoMode() ? config.getSkillsRepo() : skillName);
                dir.setPath(config.isSingleRepoMode() ? config.getSkillsPath() + "/" + skillName : skillName);
                dir.setHasManifest(true);
                
                if (config.isSingleRepoMode()) {
                    dir.setHtmlUrl(config.getWebBaseUrl() + "/" + owner + "/" + config.getSkillsRepo() + "/tree/main/" + config.getSkillsPath() + "/" + skillName);
                } else {
                    dir.setHtmlUrl(config.getWebBaseUrl() + "/" + owner + "/" + skillName);
                }
                
                return buildSkillPackage(owner, dir);
            } catch (Exception e) {
                logger.error("Failed to discover skill {}/{}", owner, skillName, e);
                return null;
            }
        });
    }

    @Override
    public CompletableFuture<List<SkillDirectory>> listSkillDirectories(String owner) {
        return listSkillDirectories(owner, config.getSkillsPath());
    }

    @Override
    public CompletableFuture<List<SkillDirectory>> listSkillDirectories(String owner, String skillsPath) {
        String cacheKey = owner + "/" + skillsPath;
        CacheEntry cached = cache.get(cacheKey);
        if (cached != null && !cached.isExpired()) {
            return CompletableFuture.completedFuture(cached.getDirectories());
        }

        return CompletableFuture.supplyAsync(() -> {
            List<SkillDirectory> directories = new ArrayList<>();
            try {
                String apiUrl;
                if (config.isSingleRepoMode()) {
                    apiUrl = config.getApiBaseUrl() + "/repos/" + owner + "/" + config.getSkillsRepo() + "/contents/" + skillsPath;
                } else {
                    apiUrl = config.getApiBaseUrl() + "/repos/" + owner + "/contents/" + skillsPath;
                }
                
                JsonNode response = executeRequest(apiUrl);

                if (response != null && response.isArray()) {
                    for (JsonNode item : response) {
                        if ("dir".equals(item.get("type").asText())) {
                            SkillDirectory dir = new SkillDirectory();
                            dir.setName(item.get("name").asText());
                            dir.setPath(item.get("path").asText());
                            dir.setOwner(owner);
                            dir.setRepoName(config.isSingleRepoMode() ? config.getSkillsRepo() : item.get("name").asText());
                            dir.setHtmlUrl(item.get("html_url").asText());

                            String manifestUrl = buildManifestUrl(owner, dir.getName());
                            dir.setHasManifest(checkFileExists(manifestUrl));

                            directories.add(dir);
                        }
                    }
                }

                cache.put(cacheKey, new CacheEntry(directories, config.getCacheTtlMs()));
            } catch (Exception e) {
                logger.error("Failed to list skill directories from {}/{}", owner, skillsPath, e);
            }
            return directories;
        });
    }

    @Override
    public CompletableFuture<Boolean> hasSkillManifest(String owner, String skillName) {
        return CompletableFuture.supplyAsync(() -> {
            String manifestUrl = buildManifestUrl(owner, skillName);
            return checkFileExists(manifestUrl);
        });
    }

    @Override
    public CompletableFuture<String> getSkillManifestContent(String owner, String skillName) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String manifestUrl = buildManifestUrl(owner, skillName);
                JsonNode response = executeRequest(manifestUrl);
                if (response != null && response.has("content")) {
                    String content = response.get("content").asText();
                    return new String(Base64.getDecoder().decode(content.replace("\n", "")), StandardCharsets.UTF_8);
                }
            } catch (Exception e) {
                logger.error("Failed to get skill manifest from {}/{}", owner, skillName, e);
            }
            return null;
        });
    }

    @Override
    public CompletableFuture<List<ReleaseInfo>> getReleases(String owner, String repoName) {
        return CompletableFuture.supplyAsync(() -> {
            List<ReleaseInfo> releases = new ArrayList<>();
            try {
                String releasesUrl = config.getApiBaseUrl() + "/repos/" + owner + "/" + repoName + "/releases";
                JsonNode response = executeRequest(releasesUrl);

                if (response != null && response.isArray()) {
                    for (JsonNode release : response) {
                        ReleaseInfo info = parseReleaseInfo(release);
                        releases.add(info);
                    }
                }
            } catch (Exception e) {
                logger.error("Failed to get releases from {}/{}", owner, repoName, e);
            }
            return releases;
        });
    }

    @Override
    public CompletableFuture<ReleaseInfo> getLatestRelease(String owner, String repoName) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String releaseUrl = config.getApiBaseUrl() + "/repos/" + owner + "/" + repoName + "/releases/latest";
                JsonNode response = executeRequest(releaseUrl);
                if (response != null) {
                    return parseReleaseInfo(response);
                }
            } catch (Exception e) {
                logger.error("Failed to get latest release from {}/{}", owner, repoName, e);
            }
            return null;
        });
    }

    private String buildManifestUrl(String owner, String skillName) {
        if (config.isSingleRepoMode()) {
            return config.getApiBaseUrl() + "/repos/" + owner + "/" + config.getSkillsRepo() 
                + "/contents/" + config.getSkillsPath() + "/" + skillName + "/" + MANIFEST_FILE;
        } else {
            return config.getApiBaseUrl() + "/repos/" + owner + "/" + skillName + "/contents/" + MANIFEST_FILE;
        }
    }

    private JsonNode executeRequest(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/vnd.github.v3+json");
            conn.setRequestProperty("User-Agent", "Ooder-SDK");

            if (config.getToken() != null && !config.getToken().isEmpty()) {
                conn.setRequestProperty("Authorization", "token " + config.getToken());
            }

            conn.setConnectTimeout(config.getRequestTimeoutMs());
            conn.setReadTimeout(config.getRequestTimeoutMs());

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                return objectMapper.readTree(response.toString());
            } else if (responseCode == 404) {
                return null;
            } else {
                logger.warn("GitHub API request failed: {} - {}", responseCode, urlString);
            }
        } catch (Exception e) {
            logger.error("Failed to execute request: {}", urlString, e);
        }
        return null;
    }

    private boolean checkFileExists(String fileUrl) {
        try {
            URL url = new URL(fileUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("HEAD");
            conn.setRequestProperty("Accept", "application/vnd.github.v3+json");
            conn.setRequestProperty("User-Agent", "Ooder-SDK");

            if (config.getToken() != null && !config.getToken().isEmpty()) {
                conn.setRequestProperty("Authorization", "token " + config.getToken());
            }

            int responseCode = conn.getResponseCode();
            return responseCode == 200;
        } catch (Exception e) {
            return false;
        }
    }

    private SkillPackage buildSkillPackage(String owner, SkillDirectory dir) {
        try {
            SkillPackage pkg = new SkillPackage();
            pkg.setSkillId(dir.getName());
            pkg.setName(dir.getName());
            pkg.setSource(config.getPlatform());
            
            if (config.isSingleRepoMode()) {
                pkg.setDownloadUrl(config.getWebBaseUrl() + "/" + owner + "/" + config.getSkillsRepo() 
                    + "/archive/refs/heads/main.zip");
                
                ReleaseInfo latestRelease = getLatestRelease(owner, config.getSkillsRepo()).get();
                if (latestRelease != null) {
                    pkg.setVersion(latestRelease.getTagName());
                    if (latestRelease.getAssets() != null) {
                        for (AssetInfo asset : latestRelease.getAssets()) {
                            if (asset.getName().contains(dir.getName())) {
                                pkg.setDownloadUrl(asset.getDownloadUrl());
                                pkg.setSize(asset.getSize());
                                break;
                            }
                        }
                    }
                } else {
                    pkg.setVersion("latest");
                }
            } else {
                pkg.setDownloadUrl(config.getWebBaseUrl() + "/" + owner + "/" + dir.getName() 
                    + "/archive/refs/heads/main.zip");
                
                ReleaseInfo latestRelease = getLatestRelease(owner, dir.getName()).get();
                if (latestRelease != null) {
                    pkg.setVersion(latestRelease.getTagName());
                    if (latestRelease.getAssets() != null && !latestRelease.getAssets().isEmpty()) {
                        AssetInfo asset = latestRelease.getAssets().get(0);
                        pkg.setDownloadUrl(asset.getDownloadUrl());
                        pkg.setSize(asset.getSize());
                    }
                } else {
                    pkg.setVersion("latest");
                }
            }

            return pkg;
        } catch (Exception e) {
            logger.error("Failed to build skill package for {}/{}", owner, dir.getName(), e);
            return null;
        }
    }

    private ReleaseInfo parseReleaseInfo(JsonNode release) {
        ReleaseInfo info = new ReleaseInfo();
        info.setTagName(release.has("tag_name") ? release.get("tag_name").asText() : "");
        info.setName(release.has("name") ? release.get("name").asText() : "");
        info.setDescription(release.has("body") ? release.get("body").asText() : "");
        info.setPrerelease(release.has("prerelease") && release.get("prerelease").asBoolean());
        info.setDraft(release.has("draft") && release.get("draft").asBoolean());

        if (release.has("published_at")) {
            try {
                info.setPublishedAt(java.time.Instant.parse(release.get("published_at").asText()).toEpochMilli());
            } catch (Exception ignored) {}
        }

        List<AssetInfo> assets = new ArrayList<>();
        if (release.has("assets") && release.get("assets").isArray()) {
            for (JsonNode asset : release.get("assets")) {
                AssetInfo assetInfo = new AssetInfo();
                assetInfo.setAssetId(asset.has("id") ? asset.get("id").asText() : "");
                assetInfo.setName(asset.has("name") ? asset.get("name").asText() : "");
                assetInfo.setDownloadUrl(asset.has("browser_download_url") ? asset.get("browser_download_url").asText() : "");
                assetInfo.setSize(asset.has("size") ? asset.get("size").asLong() : 0);
                assetInfo.setContentType(asset.has("content_type") ? asset.get("content_type").asText() : "");
                assets.add(assetInfo);
            }
        }
        info.setAssets(assets);

        return info;
    }

    private static class CacheEntry {
        private final List<SkillDirectory> directories;
        private final long expireTime;

        public CacheEntry(List<SkillDirectory> directories, long ttlMs) {
            this.directories = directories;
            this.expireTime = System.currentTimeMillis() + ttlMs;
        }

        public boolean isExpired() {
            return System.currentTimeMillis() > expireTime;
        }

        public List<SkillDirectory> getDirectories() {
            return directories;
        }
    }
}
