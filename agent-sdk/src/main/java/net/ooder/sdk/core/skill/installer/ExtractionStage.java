
package net.ooder.sdk.core.skill.installer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ooder.sdk.api.skill.SkillPackage;

public class ExtractionStage implements InstallStage {
    
    private static final Logger log = LoggerFactory.getLogger(ExtractionStage.class);
    
    private static final int BUFFER_SIZE = 8192;
    private static final String DEFAULT_INSTALL_BASE = System.getProperty("user.home") + 
        File.separator + ".ooder" + File.separator + "skills";
    
    @Override
    public String getName() {
        return "extraction";
    }
    
    @Override
    public void execute(InstallContext context) throws Exception {
        log.debug("Extracting skill package for: {}", context.getSkillId());
        
        context.setStatus(InstallStatus.EXTRACTING);
        
        SkillPackage skillPackage = context.getSkillPackage();
        String skillId = context.getSkillId();
        
        String installBasePath = getInstallBasePath(context);
        Path installPath = Paths.get(installBasePath, skillId);
        
        Files.createDirectories(installPath);
        context.setInstallPath(installPath.toString());
        
        List<String> extractedFiles = new ArrayList<>();
        
        if (skillPackage != null && skillPackage.getDownloadUrl() != null) {
            String packagePath = downloadOrGetPackagePath(skillPackage);
            if (packagePath != null) {
                extractedFiles = extractPackage(packagePath, installPath);
            }
        }
        
        if (extractedFiles.isEmpty()) {
            extractedFiles = createDefaultStructure(installPath, skillPackage);
        }
        
        for (String file : extractedFiles) {
            context.addInstalledFile(file);
        }
        
        log.debug("Extraction completed to: {}, {} files extracted", installPath, extractedFiles.size());
    }
    
    private String getInstallBasePath(InstallContext context) {
        Object basePath = context.getProperty("installPath");
        if (basePath != null && !basePath.toString().isEmpty()) {
            return basePath.toString();
        }
        return DEFAULT_INSTALL_BASE;
    }
    
    private String downloadOrGetPackagePath(SkillPackage skillPackage) {
        String downloadUrl = skillPackage.getDownloadUrl();
        if (downloadUrl == null || downloadUrl.isEmpty()) {
            return null;
        }
        
        if (downloadUrl.startsWith("file://")) {
            return downloadUrl.substring(7);
        }
        
        if (downloadUrl.startsWith("local:")) {
            return downloadUrl.substring(6);
        }
        
        if (new File(downloadUrl).exists()) {
            return downloadUrl;
        }
        
        return null;
    }
    
    private List<String> extractPackage(String packagePath, Path installPath) throws IOException {
        List<String> extractedFiles = new ArrayList<>();
        File packageFile = new File(packagePath);
        
        if (!packageFile.exists()) {
            log.warn("Package file not found: {}", packagePath);
            return extractedFiles;
        }
        
        String fileName = packageFile.getName().toLowerCase();
        
        if (fileName.endsWith(".zip")) {
            extractedFiles = extractZip(packageFile, installPath);
        } else if (fileName.endsWith(".tar.gz") || fileName.endsWith(".tgz")) {
            extractedFiles = extractTarGz(packageFile, installPath);
        } else if (fileName.endsWith(".tar")) {
            extractedFiles = extractTar(packageFile, installPath);
        } else if (fileName.endsWith(".jar")) {
            extractedFiles = extractZip(packageFile, installPath);
        } else if (isDirectory(packageFile)) {
            extractedFiles = copyDirectory(packageFile, installPath);
        } else {
            log.warn("Unknown package format: {}", fileName);
        }
        
        return extractedFiles;
    }
    
    private List<String> extractZip(File zipFile, Path destPath) throws IOException {
        List<String> extractedFiles = new ArrayList<>();
        
        try (ZipInputStream zis = new ZipInputStream(
                new BufferedInputStream(new FileInputStream(zipFile)), StandardCharsets.UTF_8)) {
            
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                Path entryPath = destPath.resolve(entry.getName());
                
                if (entry.isDirectory()) {
                    Files.createDirectories(entryPath);
                    extractedFiles.add(entryPath.toString() + File.separator);
                } else {
                    Files.createDirectories(entryPath.getParent());
                    extractFile(zis, entryPath);
                    extractedFiles.add(entryPath.toString());
                    
                    if (entry.getName().startsWith("bin/") || entry.getName().endsWith(".sh")) {
                        setExecutable(entryPath);
                    }
                }
                
                zis.closeEntry();
            }
        }
        
        return extractedFiles;
    }
    
    private List<String> extractTarGz(File tarGzFile, Path destPath) throws IOException {
        List<String> extractedFiles = new ArrayList<>();
        
        try (GZIPInputStream gzis = new GZIPInputStream(
                new BufferedInputStream(new FileInputStream(tarGzFile)));
             TarInputStream tis = new TarInputStream(gzis)) {
            
            TarEntry entry;
            while ((entry = tis.getNextEntry()) != null) {
                Path entryPath = destPath.resolve(entry.getName());
                
                if (entry.isDirectory()) {
                    Files.createDirectories(entryPath);
                    extractedFiles.add(entryPath.toString() + File.separator);
                } else {
                    Files.createDirectories(entryPath.getParent());
                    extractFile(tis, entryPath, entry.getSize());
                    extractedFiles.add(entryPath.toString());
                    
                    if (entry.getName().startsWith("bin/") || entry.getName().endsWith(".sh")) {
                        setExecutable(entryPath);
                    }
                }
            }
        }
        
        return extractedFiles;
    }
    
    private List<String> extractTar(File tarFile, Path destPath) throws IOException {
        List<String> extractedFiles = new ArrayList<>();
        
        try (TarInputStream tis = new TarInputStream(
                new BufferedInputStream(new FileInputStream(tarFile)))) {
            
            TarEntry entry;
            while ((entry = tis.getNextEntry()) != null) {
                Path entryPath = destPath.resolve(entry.getName());
                
                if (entry.isDirectory()) {
                    Files.createDirectories(entryPath);
                    extractedFiles.add(entryPath.toString() + File.separator);
                } else {
                    Files.createDirectories(entryPath.getParent());
                    extractFile(tis, entryPath, entry.getSize());
                    extractedFiles.add(entryPath.toString());
                }
            }
        }
        
        return extractedFiles;
    }
    
    private void extractFile(ZipInputStream zis, Path destPath) throws IOException {
        try (OutputStream os = new BufferedOutputStream(new FileOutputStream(destPath.toFile()))) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int len;
            while ((len = zis.read(buffer)) > 0) {
                os.write(buffer, 0, len);
            }
        }
    }
    
    private void extractFile(InputStream is, Path destPath, long size) throws IOException {
        try (OutputStream os = new BufferedOutputStream(new FileOutputStream(destPath.toFile()))) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int len;
            long remaining = size > 0 ? size : Long.MAX_VALUE;
            while (remaining > 0 && (len = is.read(buffer, 0, (int) Math.min(buffer.length, remaining))) > 0) {
                os.write(buffer, 0, len);
                if (size > 0) {
                    remaining -= len;
                }
            }
        }
    }
    
    private List<String> copyDirectory(File sourceDir, Path destPath) throws IOException {
        List<String> copiedFiles = new ArrayList<>();
        Path sourcePath = sourceDir.toPath();
        
        Files.walk(sourcePath).forEach(source -> {
            try {
                Path relative = sourcePath.relativize(source);
                Path target = destPath.resolve(relative);
                
                if (Files.isDirectory(source)) {
                    Files.createDirectories(target);
                    copiedFiles.add(target.toString() + File.separator);
                } else {
                    Files.createDirectories(target.getParent());
                    Files.copy(source, target);
                    copiedFiles.add(target.toString());
                }
            } catch (IOException e) {
                log.warn("Failed to copy: {}", source, e);
            }
        });
        
        return copiedFiles;
    }
    
    private List<String> createDefaultStructure(Path installPath, SkillPackage skillPackage) throws IOException {
        List<String> files = new ArrayList<>();
        
        Path binPath = installPath.resolve("bin");
        Path libPath = installPath.resolve("lib");
        Path configPath = installPath.resolve("config");
        Path dataPath = installPath.resolve("data");
        
        Files.createDirectories(binPath);
        Files.createDirectories(libPath);
        Files.createDirectories(configPath);
        Files.createDirectories(dataPath);
        
        files.add(binPath.toString() + File.separator);
        files.add(libPath.toString() + File.separator);
        files.add(configPath.toString() + File.separator);
        files.add(dataPath.toString() + File.separator);
        
        Path manifestPath = installPath.resolve("skill.json");
        createManifest(manifestPath, skillPackage);
        files.add(manifestPath.toString());
        
        Path readmePath = installPath.resolve("README.md");
        createReadme(readmePath, skillPackage);
        files.add(readmePath.toString());
        
        return files;
    }
    
    private void createManifest(Path manifestPath, SkillPackage skillPackage) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"skillId\": \"").append(escapeJson(skillPackage != null ? skillPackage.getSkillId() : "unknown")).append("\",\n");
        sb.append("  \"name\": \"").append(escapeJson(skillPackage != null ? skillPackage.getName() : "Unknown Skill")).append("\",\n");
        sb.append("  \"description\": \"").append(escapeJson(skillPackage != null ? skillPackage.getDescription() : "")).append("\",\n");
        sb.append("  \"version\": \"").append(escapeJson(skillPackage != null ? skillPackage.getVersion() : "1.0.0")).append("\",\n");
        sb.append("  \"sceneId\": \"").append(escapeJson(skillPackage != null ? skillPackage.getSceneId() : "")).append("\",\n");
        sb.append("  \"mainClass\": \"").append(skillPackage != null && skillPackage.getManifest() != null ? 
            escapeJson(skillPackage.getManifest().getMainClass()) : "").append("\",\n");
        sb.append("  \"installedAt\": ").append(System.currentTimeMillis()).append("\n");
        sb.append("}\n");
        
        Files.write(manifestPath, sb.toString().getBytes(StandardCharsets.UTF_8));
    }
    
    private void createReadme(Path readmePath, SkillPackage skillPackage) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("# ").append(skillPackage != null ? skillPackage.getName() : "Unknown Skill").append("\n\n");
        
        if (skillPackage != null && skillPackage.getDescription() != null) {
            sb.append(skillPackage.getDescription()).append("\n\n");
        }
        
        sb.append("## Installation\n\n");
        sb.append("- Version: ").append(skillPackage != null ? skillPackage.getVersion() : "1.0.0").append("\n");
        sb.append("- Scene: ").append(skillPackage != null ? skillPackage.getSceneId() : "N/A").append("\n");
        sb.append("- Installed: ").append(new java.util.Date()).append("\n");
        
        Files.write(readmePath, sb.toString().getBytes(StandardCharsets.UTF_8));
    }
    
    private void setExecutable(Path path) {
        try {
            Set<PosixFilePermission> perms = new HashSet<>();
            perms.add(PosixFilePermission.OWNER_READ);
            perms.add(PosixFilePermission.OWNER_WRITE);
            perms.add(PosixFilePermission.OWNER_EXECUTE);
            perms.add(PosixFilePermission.GROUP_READ);
            perms.add(PosixFilePermission.GROUP_EXECUTE);
            perms.add(PosixFilePermission.OTHERS_READ);
            perms.add(PosixFilePermission.OTHERS_EXECUTE);
            
            try {
                Files.setPosixFilePermissions(path, perms);
            } catch (UnsupportedOperationException e) {
                path.toFile().setExecutable(true);
            }
        } catch (IOException e) {
            log.warn("Failed to set executable permission: {}", path);
        }
    }
    
    private boolean isDirectory(File file) {
        return file.isDirectory();
    }
    
    private String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
    
    private static class TarInputStream extends InputStream {
        private final InputStream is;
        private long entrySize = 0;
        private long bytesRead = 0;
        
        TarInputStream(InputStream is) {
            this.is = is;
        }
        
        TarEntry getNextEntry() throws IOException {
            skipToEntryBoundary();
            
            byte[] header = new byte[512];
            int read = readFully(is, header, 0, 512);
            
            if (read == -1 || header[0] == 0) {
                return null;
            }
            
            String name = new String(header, 0, 100, StandardCharsets.UTF_8).trim();
            String sizeStr = new String(header, 124, 12, StandardCharsets.UTF_8).trim();
            
            long size = 0;
            try {
                size = Long.parseLong(sizeStr, 8);
            } catch (NumberFormatException e) {
                size = 0;
            }
            
            boolean isDir = header[156] == '5';
            
            entrySize = size;
            bytesRead = 0;
            
            return new TarEntry(name, size, isDir);
        }
        
        @Override
        public int read() throws IOException {
            if (bytesRead >= entrySize) {
                return -1;
            }
            int b = is.read();
            if (b != -1) {
                bytesRead++;
            }
            return b;
        }
        
        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            if (bytesRead >= entrySize) {
                return -1;
            }
            int maxLen = (int) Math.min(len, entrySize - bytesRead);
            int read = is.read(b, off, maxLen);
            if (read > 0) {
                bytesRead += read;
            }
            return read;
        }
        
        private void skipToEntryBoundary() throws IOException {
            long remaining = entrySize - bytesRead;
            while (remaining > 0) {
                long skipped = is.skip(remaining);
                if (skipped <= 0) break;
                remaining -= skipped;
                bytesRead += skipped;
            }
            
            int padding = (int) ((512 - (entrySize % 512)) % 512);
            for (int i = 0; i < padding; i++) {
                is.read();
            }
        }
        
        private int readFully(InputStream is, byte[] buffer, int offset, int length) throws IOException {
            int total = 0;
            while (total < length) {
                int read = is.read(buffer, offset + total, length - total);
                if (read == -1) {
                    return total == 0 ? -1 : total;
                }
                total += read;
            }
            return total;
        }
    }
    
    private static class TarEntry {
        private final String name;
        private final long size;
        private final boolean directory;
        
        TarEntry(String name, long size, boolean directory) {
            this.name = name;
            this.size = size;
            this.directory = directory;
        }
        
        String getName() { return name; }
        long getSize() { return size; }
        boolean isDirectory() { return directory; }
    }
}
