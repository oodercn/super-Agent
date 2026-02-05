package net.ooder.examples.skillsa2ui.service;

import net.ooder.examples.skillsa2ui.config.VfsConfig;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class VfsClient {
    private static final Logger log = LoggerFactory.getLogger(VfsClient.class);

    private final VfsConfig config;

    public VfsClient(VfsConfig config) {
        this.config = config;
    }

    public boolean uploadFile(String groupName, String path, byte[] content) throws IOException {
        // 直接操作本地文件系统，模拟VFS上传
        File file = new File(config.getWebDirectory(), path);
        // 确保目录存在
        file.getParentFile().mkdirs();
        FileUtils.writeByteArrayToFile(file, content);
        log.debug("Uploaded file to local VFS: {}", path);
        return true;
    }

    public byte[] downloadFile(String groupName, String path) throws IOException {
        // 直接操作本地文件系统，模拟VFS下载
        File file = new File(config.getWebDirectory(), path);
        if (!file.exists()) {
            throw new IOException("File not found: " + path);
        }
        byte[] content = FileUtils.readFileToByteArray(file);
        log.debug("Downloaded file from local VFS: {}", path);
        return content;
    }

    public List<String> listFiles(String groupName, String directory) throws IOException {
        // 直接操作本地文件系统，模拟VFS列出文件
        File dir = new File(config.getWebDirectory(), directory != null ? directory : "");
        List<String> files = new ArrayList<>();
        
        if (dir.exists() && dir.isDirectory()) {
            File[] fileList = dir.listFiles();
            if (fileList != null) {
                for (File file : fileList) {
                    String relativePath = getRelativePath(file, dir);
                    files.add(relativePath);
                }
            }
        }
        
        log.debug("Listed files from local VFS: {}", files);
        return files;
    }

    public boolean deleteFile(String groupName, String path) throws IOException {
        // 直接操作本地文件系统，模拟VFS删除
        File file = new File(config.getWebDirectory(), path);
        boolean success = file.delete();
        if (success) {
            log.debug("Deleted file from local VFS: {}", path);
        } else {
            log.error("Failed to delete file from local VFS: {}", path);
        }
        return success;
    }

    public boolean isVfsAvailable() {
        // 本地VFS总是可用的
        return true;
    }

    private String getRelativePath(File file, File baseDir) {
        String basePath = baseDir.getAbsolutePath();
        String filePath = file.getAbsolutePath();
        return filePath.substring(basePath.length() + 1).replace(File.separator, "/");
    }
}
