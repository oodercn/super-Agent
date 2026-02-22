package net.ooder.nexus.provider;

import net.ooder.scene.skill.StorageProvider;
import net.ooder.scene.core.SceneEngine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * StorageProvider 实现
 *
 * <p>基于本地文件系统实现 StorageProvider 接口</p>
 *
 * @author ooder Team
 * @version 0.7.3
 * @since SDK 0.7.3
 */
@Component
public class NexusStorageProvider implements StorageProvider {

    private static final Logger log = LoggerFactory.getLogger(NexusStorageProvider.class);
    private static final String PROVIDER_TYPE = "nexus-local";

    private String baseDirectory = System.getProperty("java.io.tmpdir") + File.separator + "nexus-storage";
    private SceneEngine sceneEngine;
    private boolean initialized = false;
    private boolean running = false;

    private final Map<String, FileMetadata> metadataStore = new ConcurrentHashMap<String, FileMetadata>();

    public NexusStorageProvider() {
        initBaseDirectory();
    }

    private void initBaseDirectory() {
        File baseDir = new File(baseDirectory);
        if (!baseDir.exists()) {
            boolean created = baseDir.mkdirs();
            if (created) {
                log.info("Created storage directory: {}", baseDirectory);
            }
        }
    }

    public void initialize(SceneEngine engine) {
        this.sceneEngine = engine;
        this.initialized = true;
        log.info("NexusStorageProvider initialized with base directory: {}", baseDirectory);
    }

    public void start() {
        this.running = true;
        log.info("NexusStorageProvider started");
    }

    public void stop() {
        this.running = false;
        log.info("NexusStorageProvider stopped");
    }

    public boolean isInitialized() {
        return initialized;
    }

    public boolean isRunning() {
        return running;
    }

    @Override
    public String getProviderType() {
        return PROVIDER_TYPE;
    }

    @Override
    public byte[] readFile(String filePath) {
        log.debug("Reading file: {}", filePath);
        try {
            Path path = getAbsolutePath(filePath);
            if (Files.exists(path)) {
                return Files.readAllBytes(path);
            }
            return null;
        } catch (IOException e) {
            log.error("Failed to read file: {}", filePath, e);
            return null;
        }
    }

    @Override
    public boolean writeFile(String filePath, byte[] content, boolean overwrite) {
        log.debug("Writing file: {}, size: {}", filePath, content != null ? content.length : 0);
        try {
            Path path = getAbsolutePath(filePath);
            Path parent = path.getParent();
            if (parent != null && !Files.exists(parent)) {
                Files.createDirectories(parent);
            }

            if (overwrite) {
                Files.write(path, content, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            } else {
                if (Files.exists(path)) {
                    return false;
                }
                Files.write(path, content, StandardOpenOption.CREATE);
            }

            metadataStore.put(filePath, new FileMetadata(filePath, content.length, System.currentTimeMillis()));

            return true;
        } catch (IOException e) {
            log.error("Failed to write file: {}", filePath, e);
            return false;
        }
    }

    @Override
    public boolean deleteFile(String filePath, boolean recursive) {
        log.debug("Deleting file: {}, recursive: {}", filePath, recursive);
        try {
            Path path = getAbsolutePath(filePath);
            if (!Files.exists(path)) {
                return false;
            }

            if (recursive) {
                Files.walk(path)
                    .sorted(Comparator.reverseOrder())
                    .forEach(p -> {
                        try {
                            Files.delete(p);
                        } catch (IOException e) {
                            log.warn("Failed to delete: {}", p);
                        }
                    });
            } else {
                Files.delete(path);
            }

            metadataStore.remove(filePath);
            return true;
        } catch (IOException e) {
            log.error("Failed to delete file: {}", filePath, e);
            return false;
        }
    }

    @Override
    public List<FileInfo> listFiles(String directoryPath, String pattern, boolean recursive) {
        log.debug("Listing files: {}, pattern: {}, recursive: {}", directoryPath, pattern, recursive);
        List<FileInfo> result = new ArrayList<FileInfo>();
        try {
            Path path = getAbsolutePath(directoryPath);
            if (!Files.exists(path) || !Files.isDirectory(path)) {
                return result;
            }

            final String globPattern = pattern != null ? pattern : "*";

            if (recursive) {
                Files.walk(path)
                    .filter(p -> !p.equals(path))
                    .filter(p -> matchesPattern(p.getFileName().toString(), globPattern))
                    .forEach(p -> result.add(createFileInfo(p)));
            } else {
                Files.list(path)
                    .filter(p -> matchesPattern(p.getFileName().toString(), globPattern))
                    .forEach(p -> result.add(createFileInfo(p)));
            }
        } catch (IOException e) {
            log.error("Failed to list files: {}", directoryPath, e);
        }
        return result;
    }

    @Override
    public boolean fileExists(String filePath) {
        Path path = getAbsolutePath(filePath);
        return Files.exists(path);
    }

    @Override
    public boolean createDirectory(String directoryPath) {
        log.debug("Creating directory: {}", directoryPath);
        try {
            Path path = getAbsolutePath(directoryPath);
            if (Files.exists(path)) {
                return true;
            }
            Files.createDirectories(path);
            return true;
        } catch (IOException e) {
            log.error("Failed to create directory: {}", directoryPath, e);
            return false;
        }
    }

    @Override
    public FileInfo getFileInfo(String filePath) {
        log.debug("Getting file info: {}", filePath);
        Path path = getAbsolutePath(filePath);
        if (!Files.exists(path)) {
            return null;
        }
        return createFileInfo(path);
    }

    @Override
    public boolean copyFile(String sourcePath, String targetPath) {
        log.debug("Copying file: {} -> {}", sourcePath, targetPath);
        try {
            Path source = getAbsolutePath(sourcePath);
            Path target = getAbsolutePath(targetPath);

            if (!Files.exists(source)) {
                return false;
            }

            Path parent = target.getParent();
            if (parent != null && !Files.exists(parent)) {
                Files.createDirectories(parent);
            }

            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            log.error("Failed to copy file: {} -> {}", sourcePath, targetPath, e);
            return false;
        }
    }

    @Override
    public boolean moveFile(String sourcePath, String targetPath) {
        log.debug("Moving file: {} -> {}", sourcePath, targetPath);
        try {
            Path source = getAbsolutePath(sourcePath);
            Path target = getAbsolutePath(targetPath);

            if (!Files.exists(source)) {
                return false;
            }

            Path parent = target.getParent();
            if (parent != null && !Files.exists(parent)) {
                Files.createDirectories(parent);
            }

            Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);

            metadataStore.remove(sourcePath);
            metadataStore.put(targetPath, new FileMetadata(targetPath, Files.size(target), System.currentTimeMillis()));

            return true;
        } catch (IOException e) {
            log.error("Failed to move file: {} -> {}", sourcePath, targetPath, e);
            return false;
        }
    }

    @Override
    public StorageQuota getQuota() {
        log.debug("Getting storage quota");
        StorageQuota quota = new StorageQuota();

        File baseDir = new File(baseDirectory);
        quota.setTotalSpace(baseDir.getTotalSpace());
        quota.setUsedSpace(getDirectorySize(baseDir));
        quota.setAvailableSpace(baseDir.getFreeSpace());
        quota.setFileCount(countFiles(baseDir));

        return quota;
    }

    private Path getAbsolutePath(String relativePath) {
        String normalized = relativePath;
        if (normalized.startsWith("/")) {
            normalized = normalized.substring(1);
        }
        return Paths.get(baseDirectory, normalized);
    }

    private FileInfo createFileInfo(Path path) {
        FileInfo info = new FileInfo();
        try {
            info.setPath(path.toString().substring(baseDirectory.length()));
            info.setName(path.getFileName().toString());
            info.setSize(Files.size(path));
            info.setDirectory(Files.isDirectory(path));
            info.setLastModified(Files.getLastModifiedTime(path).toMillis());
            info.setContentType(Files.probeContentType(path));
            info.setMetadata(new HashMap<String, String>());
        } catch (IOException e) {
            log.warn("Failed to get file info for: {}", path);
        }
        return info;
    }

    private boolean matchesPattern(String fileName, String pattern) {
        if (pattern == null || pattern.equals("*") || pattern.equals("*.*")) {
            return true;
        }
        if (pattern.startsWith("*.")) {
            String ext = pattern.substring(2);
            return fileName.toLowerCase().endsWith(ext.toLowerCase());
        }
        return fileName.equals(pattern);
    }

    private long getDirectorySize(File directory) {
        long size = 0;
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        size += getDirectorySize(file);
                    } else {
                        size += file.length();
                    }
                }
            }
        }
        return size;
    }

    private int countFiles(File directory) {
        int count = 0;
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        count += countFiles(file);
                    } else {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    private static class FileMetadata {
        String path;
        long size;
        long timestamp;

        FileMetadata(String path, long size, long timestamp) {
            this.path = path;
            this.size = size;
            this.timestamp = timestamp;
        }
    }
}
