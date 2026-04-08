package net.ooder.mvp.skill.scene.capability.install;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class SkillDirectoryConfig {

    @Value("${ooder.skills.directories.downloads:./.ooder/downloads}")
    private String downloadsPath;

    @Value("${ooder.skills.directories.installed:./.ooder/installed}")
    private String installedPath;

    @Value("${ooder.skills.directories.activated:./.ooder/activated}")
    private String activatedPath;

    @Value("${ooder.skills.directories.dev:./.ooder/dev}")
    private String devPath;

    @Value("${ooder.skills.directories.cache:./.ooder/cache}")
    private String cachePath;

    @Value("${ooder.skills.path:../skills}")
    private String sourcePath;

    @PostConstruct
    public void init() {
        ensureDirectoryExists(getDownloadsDir(), "downloads");
        ensureDirectoryExists(getInstalledDir(), "installed");
        ensureDirectoryExists(getActivatedDir(), "activated");
        ensureDirectoryExists(getDevDir(), "dev");
        ensureDirectoryExists(getCacheDir(), "cache");
    }

    private void ensureDirectoryExists(Path dir, String name) {
        try {
            if (!Files.exists(dir)) {
                Files.createDirectories(dir);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to create " + name + " directory: " + dir, e);
        }
    }

    public Path getDownloadsDir() {
        return Paths.get(downloadsPath).toAbsolutePath();
    }

    public Path getInstalledDir() {
        return Paths.get(installedPath).toAbsolutePath();
    }

    public Path getActivatedDir() {
        return Paths.get(activatedPath).toAbsolutePath();
    }

    public Path getDevDir() {
        return Paths.get(devPath).toAbsolutePath();
    }

    public Path getCacheDir() {
        return Paths.get(cachePath).toAbsolutePath();
    }

    public Path getSourceDir() {
        return Paths.get(sourcePath).toAbsolutePath();
    }

    public Path getSkillDownloadPath(String skillId) {
        return getDownloadsDir().resolve(skillId);
    }

    public Path getSkillInstalledPath(String skillId) {
        return getInstalledDir().resolve(skillId);
    }

    public Path getSkillActivatedPath(String skillId) {
        return getActivatedDir().resolve(skillId);
    }

    public Path getSkillDevPath(String skillId) {
        return getDevDir().resolve(skillId);
    }

    public Path getSkillConfigPath(String skillId) {
        return getSkillActivatedPath(skillId).resolve("config");
    }

    public Path getSkillDataPath(String skillId) {
        return getSkillActivatedPath(skillId).resolve("data");
    }

    public Path getSkillLogsPath(String skillId) {
        return getSkillActivatedPath(skillId).resolve("logs");
    }

    public String getDownloadsPath() {
        return downloadsPath;
    }

    public void setDownloadsPath(String downloadsPath) {
        this.downloadsPath = downloadsPath;
    }

    public String getInstalledPath() {
        return installedPath;
    }

    public void setInstalledPath(String installedPath) {
        this.installedPath = installedPath;
    }

    public String getActivatedPath() {
        return activatedPath;
    }

    public void setActivatedPath(String activatedPath) {
        this.activatedPath = activatedPath;
    }

    public String getDevPath() {
        return devPath;
    }

    public void setDevPath(String devPath) {
        this.devPath = devPath;
    }

    public String getCachePath() {
        return cachePath;
    }

    public void setCachePath(String cachePath) {
        this.cachePath = cachePath;
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    public enum SkillDirectoryType {
        DOWNLOADS("downloads", "下载目录"),
        INSTALLED("installed", "已安装目录"),
        ACTIVATED("activated", "已激活目录"),
        DEV("dev", "开发目录"),
        SOURCE("source", "源码目录"),
        CACHE("cache", "缓存目录");

        private final String code;
        private final String name;

        SkillDirectoryType(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }
}
