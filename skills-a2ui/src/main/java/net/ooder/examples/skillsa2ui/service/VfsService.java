package net.ooder.examples.skillsa2ui.service;

import net.ooder.examples.skillsa2ui.config.VfsConfig;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class VfsService {
    private static final Logger log = LoggerFactory.getLogger(VfsService.class);
    private static final long SYNC_INTERVAL = 300; // 5分钟同步一次

    private final VfsConfig config;
    private final VfsClient vfsClient;
    private final ScheduledExecutorService syncExecutor;
    private volatile boolean isRunning = false;
    private volatile boolean isVfsAvailable = false;

    @Autowired
    public VfsService(VfsConfig config) {
        this.config = config;
        this.vfsClient = new VfsClient(config);
        this.syncExecutor = Executors.newSingleThreadScheduledExecutor();
    }

    public void start() {
        if (!isRunning && config.isEnabled()) {
            isRunning = true;
            log.info("Starting VFS service with sync interval: {} seconds", SYNC_INTERVAL);
            syncExecutor.scheduleAtFixedRate(this::syncWebDirectory, 0, SYNC_INTERVAL, TimeUnit.SECONDS);
        }
    }

    public void stop() {
        if (isRunning) {
            isRunning = false;
            syncExecutor.shutdown();
            log.info("VFS service stopped");
        }
    }

    public boolean isVfsAvailable() {
        if (!config.isEnabled()) {
            return false;
        }

        isVfsAvailable = vfsClient.isVfsAvailable();
        return isVfsAvailable;
    }

    public void syncWebDirectory() {
        if (!config.isEnabled()) {
            return;
        }

        log.info("Starting web directory sync with VFS");

        // 检查VFS可用性
        if (!isVfsAvailable()) {
            log.warn("VFS server unavailable, skipping sync");
            return;
        }

        // 获取web目录
        File webDir = new File(config.getWebDirectory());
        if (!webDir.exists() || !webDir.isDirectory()) {
            log.warn("Web directory not found: {}", config.getWebDirectory());
            return;
        }

        try {
            // 同步到VFS
            syncToVfs(webDir);
            
            // 从VFS同步
            syncFromVfs(webDir);

            log.info("Web directory sync completed successfully");
        } catch (Exception e) {
            log.error("Error syncing web directory: {}", e.getMessage(), e);
        }
    }

    private void syncToVfs(File directory) throws IOException {
        // 遍历目录中的所有文件
        File[] files = directory.listFiles();
        if (files == null) {
            return;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                // 递归处理子目录
                syncToVfs(file);
            } else {
                // 同步文件到VFS
                syncFileToVfs(file);
            }
        }
    }

    private void syncFileToVfs(File file) {
        try {
            String relativePath = getRelativePath(file);
            byte[] fileContent = FileUtils.readFileToByteArray(file);
            
            // 调用VFS API上传文件
            vfsClient.uploadFile(config.getGroupName(), relativePath, fileContent);
            
            log.debug("Synced file to VFS: {}", relativePath);
        } catch (Exception e) {
            log.error("Error syncing file to VFS: {}", file.getAbsolutePath(), e);
        }
    }

    private void syncFromVfs(File directory) {
        try {
            // 调用VFS API获取文件列表
            List<String> files = vfsClient.listFiles(config.getGroupName(), getRelativePath(directory));
            
            // 遍历文件列表并下载
            for (String file : files) {
                try {
                    byte[] content = vfsClient.downloadFile(config.getGroupName(), file);
                    File localFile = new File(directory, file);
                    // 确保目录存在
                    localFile.getParentFile().mkdirs();
                    FileUtils.writeByteArrayToFile(localFile, content);
                    log.debug("Downloaded file from VFS: {}", file);
                } catch (Exception e) {
                    log.error("Error downloading file from VFS: {}", file, e);
                }
            }
            
            log.debug("Synced files from VFS to: {}", directory.getAbsolutePath());
        } catch (Exception e) {
            log.error("Error syncing files from VFS: {}", e.getMessage(), e);
        }
    }

    private String getRelativePath(File file) {
        String webDirPath = new File(config.getWebDirectory()).getAbsolutePath();
        String filePath = file.getAbsolutePath();
        if (filePath.startsWith(webDirPath)) {
            int startIndex = webDirPath.length();
            if (startIndex < filePath.length() && filePath.charAt(startIndex) == File.separatorChar) {
                startIndex++;
            }
            return filePath.substring(startIndex).replace(File.separator, "/");
        }
        // 如果文件不在web目录中，返回文件的绝对路径
        return filePath.replace(File.separator, "/");
    }

    public void updateWebPage(String pagePath, String content) {
        // 1. 更新本地文件
        // 2. 同步到VFS
        try {
            File pageFile = new File(config.getWebDirectory(), pagePath);
            // 确保目录存在
            pageFile.getParentFile().mkdirs();
            FileUtils.writeStringToFile(pageFile, content, "UTF-8");
            log.info("Updated web page: {}", pagePath);

            // 同步到VFS
            if (isVfsAvailable()) {
                syncFileToVfs(pageFile);
            }
        } catch (Exception e) {
            log.error("Error updating web page: {}", pagePath, e);
        }
    }

    public void deleteWebPage(String pagePath) {
        // 1. 删除本地文件
        // 2. 从VFS删除
        try {
            File pageFile = new File(config.getWebDirectory(), pagePath);
            if (pageFile.exists()) {
                pageFile.delete();
                log.info("Deleted web page: {}", pagePath);
            }

            // 从VFS删除
            if (isVfsAvailable()) {
                vfsClient.deleteFile(config.getGroupName(), pagePath);
            }
        } catch (Exception e) {
            log.error("Error deleting web page: {}", pagePath, e);
        }
    }

    public byte[] getWebPageContent(String pagePath) throws IOException {
        // 首先尝试从本地获取
        File pageFile = new File(config.getWebDirectory(), pagePath);
        if (pageFile.exists()) {
            return FileUtils.readFileToByteArray(pageFile);
        }

        // 如果本地不存在，从VFS获取
        if (isVfsAvailable()) {
            return vfsClient.downloadFile(config.getGroupName(), pagePath);
        }

        throw new IOException("Web page not found: " + pagePath);
    }
}
