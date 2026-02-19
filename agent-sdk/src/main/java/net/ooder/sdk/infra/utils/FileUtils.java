
package net.ooder.sdk.infra.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public final class FileUtils {
    
    private FileUtils() {
    }
    
    public static String readFileAsString(String filePath) throws IOException {
        return readFileAsString(Paths.get(filePath));
    }
    
    public static String readFileAsString(Path path) throws IOException {
        byte[] bytes = Files.readAllBytes(path);
        return new String(bytes, StandardCharsets.UTF_8);
    }
    
    public static void writeStringToFile(String content, String filePath) throws IOException {
        writeStringToFile(content, Paths.get(filePath));
    }
    
    public static void writeStringToFile(String content, Path path) throws IOException {
        Files.createDirectories(path.getParent());
        Files.write(path, content.getBytes(StandardCharsets.UTF_8), 
            StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }
    
    public static void appendToFile(String content, String filePath) throws IOException {
        appendToFile(content, Paths.get(filePath));
    }
    
    public static void appendToFile(String content, Path path) throws IOException {
        Files.createDirectories(path.getParent());
        Files.write(path, content.getBytes(StandardCharsets.UTF_8), 
            StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }
    
    public static byte[] readFileAsBytes(String filePath) throws IOException {
        return Files.readAllBytes(Paths.get(filePath));
    }
    
    public static void writeBytesToFile(byte[] bytes, String filePath) throws IOException {
        Path path = Paths.get(filePath);
        Files.createDirectories(path.getParent());
        Files.write(path, bytes, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }
    
    public static List<String> readLines(String filePath) throws IOException {
        return Files.readAllLines(Paths.get(filePath), StandardCharsets.UTF_8);
    }
    
    public static void writeLines(List<String> lines, String filePath) throws IOException {
        Path path = Paths.get(filePath);
        Files.createDirectories(path.getParent());
        Files.write(path, lines, StandardCharsets.UTF_8);
    }
    
    public static Properties loadProperties(String filePath) throws IOException {
        Properties props = new Properties();
        try (InputStream is = Files.newInputStream(Paths.get(filePath))) {
            props.load(is);
        }
        return props;
    }
    
    public static void saveProperties(Properties props, String filePath, String comment) throws IOException {
        Path path = Paths.get(filePath);
        Files.createDirectories(path.getParent());
        try (OutputStream os = Files.newOutputStream(path, 
            StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            props.store(os, comment);
        }
    }
    
    public static boolean exists(String filePath) {
        return Files.exists(Paths.get(filePath));
    }
    
    public static boolean isDirectory(String filePath) {
        return Files.isDirectory(Paths.get(filePath));
    }
    
    public static boolean isRegularFile(String filePath) {
        return Files.isRegularFile(Paths.get(filePath));
    }
    
    public static void createDirectory(String dirPath) throws IOException {
        Files.createDirectories(Paths.get(dirPath));
    }
    
    public static void deleteFile(String filePath) throws IOException {
        Files.deleteIfExists(Paths.get(filePath));
    }
    
    public static void deleteDirectory(String dirPath) throws IOException {
        Path path = Paths.get(dirPath);
        if (Files.exists(path)) {
            Files.walk(path)
                .sorted((a, b) -> b.compareTo(a))
                .forEach(p -> {
                    try {
                        Files.delete(p);
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to delete: " + p, e);
                    }
                });
        }
    }
    
    public static List<String> listFiles(String dirPath, String extension) throws IOException {
        List<String> files = new ArrayList<>();
        Path path = Paths.get(dirPath);
        if (Files.isDirectory(path)) {
            Files.walk(path)
                .filter(Files::isRegularFile)
                .filter(p -> extension == null || p.toString().endsWith(extension))
                .forEach(p -> files.add(p.toString()));
        }
        return files;
    }
    
    public static String getExtension(String filePath) {
        int lastDot = filePath.lastIndexOf('.');
        if (lastDot > 0 && lastDot < filePath.length() - 1) {
            return filePath.substring(lastDot + 1);
        }
        return "";
    }
    
    public static String getFileName(String filePath) {
        return Paths.get(filePath).getFileName().toString();
    }
    
    public static String getFileNameWithoutExtension(String filePath) {
        String fileName = getFileName(filePath);
        int lastDot = fileName.lastIndexOf('.');
        if (lastDot > 0) {
            return fileName.substring(0, lastDot);
        }
        return fileName;
    }
    
    public static long getFileSize(String filePath) throws IOException {
        return Files.size(Paths.get(filePath));
    }
    
    public static void copyFile(String source, String target) throws IOException {
        Path sourcePath = Paths.get(source);
        Path targetPath = Paths.get(target);
        Files.createDirectories(targetPath.getParent());
        Files.copy(sourcePath, targetPath, 
            java.nio.file.StandardCopyOption.REPLACE_EXISTING);
    }
    
    public static void moveFile(String source, String target) throws IOException {
        Path sourcePath = Paths.get(source);
        Path targetPath = Paths.get(target);
        Files.createDirectories(targetPath.getParent());
        Files.move(sourcePath, targetPath, 
            java.nio.file.StandardCopyOption.REPLACE_EXISTING);
    }
}
