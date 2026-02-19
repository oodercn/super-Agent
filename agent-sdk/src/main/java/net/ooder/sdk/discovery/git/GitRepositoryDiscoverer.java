package net.ooder.sdk.discovery.git;

import net.ooder.sdk.api.skill.SkillPackage;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Git Repository Skill Discoverer Interface
 *
 * @author ooder Team
 * @since 2.0
 */
public interface GitRepositoryDiscoverer {

    /**
     * Get discovery configuration
     */
    GitDiscoveryConfig getConfig();

    /**
     * Discover all skills from default owner's skills path
     * P2P mode: public access without token
     */
    CompletableFuture<List<SkillPackage>> discoverSkills();

    /**
     * Discover skills from specified owner's skills path
     * Organization mode: requires token for private repos
     */
    CompletableFuture<List<SkillPackage>> discoverSkills(String owner);

    /**
     * Discover skills from specified path under owner
     */
    CompletableFuture<List<SkillPackage>> discoverSkills(String owner, String skillsPath);

    /**
     * Discover a single skill by owner and repo name
     */
    CompletableFuture<SkillPackage> discoverSkill(String owner, String repoName);

    /**
     * Get all directories under skills path
     */
    CompletableFuture<List<SkillDirectory>> listSkillDirectories(String owner);

    /**
     * Get all directories under skills path with custom path
     */
    CompletableFuture<List<SkillDirectory>> listSkillDirectories(String owner, String skillsPath);

    /**
     * Check if repository contains skill manifest
     */
    CompletableFuture<Boolean> hasSkillManifest(String owner, String repoName);

    /**
     * Get skill manifest content
     */
    CompletableFuture<String> getSkillManifestContent(String owner, String repoName);

    /**
     * Get repository releases
     */
    CompletableFuture<List<ReleaseInfo>> getReleases(String owner, String repoName);

    /**
     * Get latest release
     */
    CompletableFuture<ReleaseInfo> getLatestRelease(String owner, String repoName);

    /**
     * Skill directory info
     */
    class SkillDirectory {
        private String name;
        private String path;
        private String owner;
        private String repoName;
        private String htmlUrl;
        private boolean hasManifest;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getPath() { return path; }
        public void setPath(String path) { this.path = path; }
        public String getOwner() { return owner; }
        public void setOwner(String owner) { this.owner = owner; }
        public String getRepoName() { return repoName; }
        public void setRepoName(String repoName) { this.repoName = repoName; }
        public String getHtmlUrl() { return htmlUrl; }
        public void setHtmlUrl(String htmlUrl) { this.htmlUrl = htmlUrl; }
        public boolean isHasManifest() { return hasManifest; }
        public void setHasManifest(boolean hasManifest) { this.hasManifest = hasManifest; }
    }

    /**
     * Release info
     */
    class ReleaseInfo {
        private String tagName;
        private String name;
        private String description;
        private long publishedAt;
        private List<AssetInfo> assets;
        private boolean prerelease;
        private boolean draft;

        public String getTagName() { return tagName; }
        public void setTagName(String tagName) { this.tagName = tagName; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public long getPublishedAt() { return publishedAt; }
        public void setPublishedAt(long publishedAt) { this.publishedAt = publishedAt; }
        public List<AssetInfo> getAssets() { return assets; }
        public void setAssets(List<AssetInfo> assets) { this.assets = assets; }
        public boolean isPrerelease() { return prerelease; }
        public void setPrerelease(boolean prerelease) { this.prerelease = prerelease; }
        public boolean isDraft() { return draft; }
        public void setDraft(boolean draft) { this.draft = draft; }
    }

    /**
     * Asset info
     */
    class AssetInfo {
        private String assetId;
        private String name;
        private String downloadUrl;
        private long size;
        private String contentType;

        public String getAssetId() { return assetId; }
        public void setAssetId(String assetId) { this.assetId = assetId; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDownloadUrl() { return downloadUrl; }
        public void setDownloadUrl(String downloadUrl) { this.downloadUrl = downloadUrl; }
        public long getSize() { return size; }
        public void setSize(long size) { this.size = size; }
        public String getContentType() { return contentType; }
        public void setContentType(String contentType) { this.contentType = contentType; }
    }
}
