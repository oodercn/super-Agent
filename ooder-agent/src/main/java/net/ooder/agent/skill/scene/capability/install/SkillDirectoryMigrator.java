package net.ooder.mvp.skill.scene.capability.install;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.*;
import java.util.Comparator;

@Component
public class SkillDirectoryMigrator {

    private static final Logger log = LoggerFactory.getLogger(SkillDirectoryMigrator.class);

    @Autowired
    private SkillDirectoryConfig directoryConfig;

    public static class MigrationResult {
        private boolean success;
        private String skillId;
        private Path sourcePath;
        private Path targetPath;
        private String message;

        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public String getSkillId() { return skillId; }
        public void setSkillId(String skillId) { this.skillId = skillId; }
        public Path getSourcePath() { return sourcePath; }
        public void setSourcePath(Path sourcePath) { this.sourcePath = sourcePath; }
        public Path getTargetPath() { return targetPath; }
        public void setTargetPath(Path targetPath) { this.targetPath = targetPath; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }

    public MigrationResult moveToDownloads(String skillId, Path sourcePath) {
        log.info("[moveToDownloads] Moving skill {} to downloads from {}", skillId, sourcePath);
        return migrate(sourcePath, directoryConfig.getSkillDownloadPath(skillId), skillId, "downloads");
    }

    public MigrationResult moveToInstalled(String skillId) {
        log.info("[moveToInstalled] Moving skill {} from downloads to installed", skillId);
        Path sourcePath = directoryConfig.getSkillDownloadPath(skillId);
        Path targetPath = directoryConfig.getSkillInstalledPath(skillId);
        return migrate(sourcePath, targetPath, skillId, "installed");
    }

    public MigrationResult moveToActivated(String skillId) {
        log.info("[moveToActivated] Moving skill {} from installed to activated", skillId);
        Path sourcePath = directoryConfig.getSkillInstalledPath(skillId);
        Path targetPath = directoryConfig.getSkillActivatedPath(skillId);
        
        MigrationResult result = migrate(sourcePath, targetPath, skillId, "activated");
        
        if (result.isSuccess()) {
            createRuntimeDirectories(skillId);
        }
        
        return result;
    }

    public MigrationResult moveToDev(String skillId) {
        log.info("[moveToDev] Moving skill {} to dev", skillId);
        Path sourcePath = directoryConfig.getSkillInstalledPath(skillId);
        Path targetPath = directoryConfig.getSkillDevPath(skillId);
        return migrate(sourcePath, targetPath, skillId, "dev");
    }

    public MigrationResult rollbackToDownloads(String skillId) {
        log.info("[rollbackToDownloads] Rolling back skill {} to downloads", skillId);
        Path sourcePath = directoryConfig.getSkillInstalledPath(skillId);
        Path targetPath = directoryConfig.getSkillDownloadPath(skillId);
        return migrate(sourcePath, targetPath, skillId, "downloads");
    }

    public MigrationResult rollbackToInstalled(String skillId) {
        log.info("[rollbackToInstalled] Rolling back skill {} to installed", skillId);
        Path sourcePath = directoryConfig.getSkillActivatedPath(skillId);
        Path targetPath = directoryConfig.getSkillInstalledPath(skillId);
        return migrate(sourcePath, targetPath, skillId, "installed");
    }

    private MigrationResult migrate(Path sourcePath, Path targetPath, String skillId, String targetDirName) {
        MigrationResult result = new MigrationResult();
        result.setSkillId(skillId);
        result.setSourcePath(sourcePath);
        result.setTargetPath(targetPath);

        if (!Files.exists(sourcePath)) {
            result.setSuccess(false);
            result.setMessage("源目录不存在: " + sourcePath);
            log.error("[migrate] Source path does not exist: {}", sourcePath);
            return result;
        }

        try {
            if (Files.exists(targetPath)) {
                log.info("[migrate] Target path exists, removing: {}", targetPath);
                deleteDirectory(targetPath);
            }

            Files.createDirectories(targetPath.getParent());
            
            Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
            
            result.setSuccess(true);
            result.setMessage("迁移成功到 " + targetDirName);
            log.info("[migrate] Successfully moved {} to {}", skillId, targetPath);

        } catch (IOException e) {
            result.setSuccess(false);
            result.setMessage("迁移失败: " + e.getMessage());
            log.error("[migrate] Failed to migrate {}: {}", skillId, e.getMessage());
        }

        return result;
    }

    private void createRuntimeDirectories(String skillId) {
        try {
            Path configPath = directoryConfig.getSkillConfigPath(skillId);
            Path dataPath = directoryConfig.getSkillDataPath(skillId);
            Path logsPath = directoryConfig.getSkillLogsPath(skillId);

            Files.createDirectories(configPath);
            Files.createDirectories(dataPath);
            Files.createDirectories(logsPath);

            log.info("[createRuntimeDirectories] Created runtime directories for: {}", skillId);
        } catch (IOException e) {
            log.error("[createRuntimeDirectories] Failed to create runtime directories: {}", e.getMessage());
        }
    }

    public boolean deleteSkillDirectory(String skillId, SkillDirectoryConfig.SkillDirectoryType dirType) {
        Path skillPath = getDirectoryPath(skillId, dirType);
        
        if (skillPath == null || !Files.exists(skillPath)) {
            return true;
        }

        try {
            deleteDirectory(skillPath);
            log.info("[deleteSkillDirectory] Deleted skill {} from {}", skillId, dirType.getCode());
            return true;
        } catch (IOException e) {
            log.error("[deleteSkillDirectory] Failed to delete: {}", e.getMessage());
            return false;
        }
    }

    private Path getDirectoryPath(String skillId, SkillDirectoryConfig.SkillDirectoryType dirType) {
        switch (dirType) {
            case DOWNLOADS:
                return directoryConfig.getSkillDownloadPath(skillId);
            case INSTALLED:
                return directoryConfig.getSkillInstalledPath(skillId);
            case ACTIVATED:
                return directoryConfig.getSkillActivatedPath(skillId);
            case DEV:
                return directoryConfig.getSkillDevPath(skillId);
            default:
                return null;
        }
    }

    private void deleteDirectory(Path path) throws IOException {
        if (Files.exists(path)) {
            Files.walk(path)
                .sorted(Comparator.reverseOrder())
                .forEach(p -> {
                    try {
                        Files.delete(p);
                    } catch (IOException e) {
                        log.warn("[deleteDirectory] Failed to delete: {}", p);
                    }
                });
        }
    }

    public SkillDirectoryConfig.SkillDirectoryType detectSkillLocation(String skillId) {
        if (Files.exists(directoryConfig.getSkillActivatedPath(skillId))) {
            return SkillDirectoryConfig.SkillDirectoryType.ACTIVATED;
        }
        if (Files.exists(directoryConfig.getSkillInstalledPath(skillId))) {
            return SkillDirectoryConfig.SkillDirectoryType.INSTALLED;
        }
        if (Files.exists(directoryConfig.getSkillDownloadPath(skillId))) {
            return SkillDirectoryConfig.SkillDirectoryType.DOWNLOADS;
        }
        if (Files.exists(directoryConfig.getSkillDevPath(skillId))) {
            return SkillDirectoryConfig.SkillDirectoryType.DEV;
        }
        return null;
    }

    public Path getSkillYamlPath(String skillId) {
        SkillDirectoryConfig.SkillDirectoryType location = detectSkillLocation(skillId);
        if (location != null) {
            Path dirPath = getDirectoryPath(skillId, location);
            if (dirPath != null) {
                return dirPath.resolve("skill.yaml");
            }
        }
        return null;
    }
}
