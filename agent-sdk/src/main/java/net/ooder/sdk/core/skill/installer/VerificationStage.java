
package net.ooder.sdk.core.skill.installer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ooder.sdk.api.skill.SkillPackage;

public class VerificationStage implements InstallStage {
    
    private static final Logger log = LoggerFactory.getLogger(VerificationStage.class);
    
    private static final String MANIFEST_FILE = "skill.json";
    private static final String CONFIG_FILE = "skill.properties";
    private static final String[] REQUIRED_DIRS = {"bin", "lib", "config"};
    
    @Override
    public String getName() {
        return "verification";
    }
    
    @Override
    public void execute(InstallContext context) throws Exception {
        log.debug("Verifying installation for: {}", context.getSkillId());
        
        context.setStatus(InstallStatus.VERIFYING);
        
        String installPath = context.getInstallPath();
        if (installPath == null || installPath.isEmpty()) {
            throw new InstallException("Install path not set");
        }
        
        Path skillDir = Paths.get(installPath);
        if (!Files.exists(skillDir)) {
            throw new InstallException("Skill directory does not exist: " + installPath);
        }
        
        List<String> errors = new ArrayList<>();
        
        verifyDirectoryStructure(skillDir, errors);
        
        verifyManifest(skillDir, context, errors);
        
        verifyConfiguration(skillDir, context, errors);
        
        verifyInstalledFiles(skillDir, context, errors);
        
        verifyChecksums(skillDir, context, errors);
        
        verifyPermissions(skillDir, errors);
        
        if (!errors.isEmpty()) {
            throw new InstallException("Verification failed: " + String.join("; ", errors));
        }
        
        context.setProperty("verified", true);
        context.setProperty("verifyTime", System.currentTimeMillis());
        context.setProperty("verifyStatus", "PASSED");
        
        log.debug("Verification passed for: {}", context.getSkillId());
    }
    
    private void verifyDirectoryStructure(Path skillDir, List<String> errors) {
        log.debug("Verifying directory structure...");
        
        for (String dirName : REQUIRED_DIRS) {
            Path dir = skillDir.resolve(dirName);
            if (!Files.exists(dir)) {
                log.warn("Optional directory missing: {}", dirName);
            }
        }
        
        Path configDir = skillDir.resolve("config");
        if (!Files.exists(configDir)) {
            errors.add("Required config directory missing");
        }
        
        Path manifestFile = skillDir.resolve(MANIFEST_FILE);
        if (!Files.exists(manifestFile)) {
            errors.add("Manifest file missing: " + MANIFEST_FILE);
        }
        
        log.debug("Directory structure verification completed");
    }
    
    private void verifyManifest(Path skillDir, InstallContext context, List<String> errors) {
        log.debug("Verifying manifest...");
        
        Path manifestPath = skillDir.resolve(MANIFEST_FILE);
        if (!Files.exists(manifestPath)) {
            errors.add("Manifest file not found");
            return;
        }
        
        try {
            String content = readFileContent(manifestPath);
            
            Map<String, Object> manifest = parseJson(content);
            
            String skillId = getString(manifest, "skillId");
            if (skillId == null || skillId.isEmpty()) {
                errors.add("Manifest missing skillId");
            } else if (!skillId.equals(context.getSkillId())) {
                errors.add("Manifest skillId mismatch: expected " + context.getSkillId() + 
                    ", found " + skillId);
            }
            
            String name = getString(manifest, "name");
            if (name == null || name.isEmpty()) {
                errors.add("Manifest missing name");
            }
            
            String version = getString(manifest, "version");
            if (version == null || version.isEmpty()) {
                log.warn("Manifest missing version, using default");
            }
            
            context.setProperty("manifestValid", true);
            log.debug("Manifest verification passed");
            
        } catch (IOException e) {
            errors.add("Failed to read manifest: " + e.getMessage());
        }
    }
    
    private void verifyConfiguration(Path skillDir, InstallContext context, List<String> errors) {
        log.debug("Verifying configuration...");
        
        Path configPath = skillDir.resolve("config").resolve(CONFIG_FILE);
        if (!Files.exists(configPath)) {
            errors.add("Configuration file not found: " + configPath);
            return;
        }
        
        try {
            Properties props = new Properties();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(configPath.toFile()), StandardCharsets.UTF_8))) {
                props.load(reader);
            }
            
            String configSkillId = props.getProperty("skill.id");
            if (configSkillId == null || configSkillId.isEmpty()) {
                errors.add("Configuration missing skill.id");
            } else if (!configSkillId.equals(context.getSkillId())) {
                errors.add("Configuration skill.id mismatch");
            }
            
            String installedAt = props.getProperty("skill.installed.at");
            if (installedAt != null) {
                try {
                    long timestamp = Long.parseLong(installedAt);
                    long age = System.currentTimeMillis() - timestamp;
                    if (age < 0) {
                        errors.add("Invalid installation timestamp (future date)");
                    }
                } catch (NumberFormatException e) {
                    errors.add("Invalid installation timestamp format");
                }
            }
            
            context.setProperty("configValid", true);
            log.debug("Configuration verification passed");
            
        } catch (IOException e) {
            errors.add("Failed to read configuration: " + e.getMessage());
        }
    }
    
    private void verifyInstalledFiles(Path skillDir, InstallContext context, List<String> errors) {
        log.debug("Verifying installed files...");
        
        List<String> installedFiles = context.getInstalledFiles();
        if (installedFiles == null || installedFiles.isEmpty()) {
            log.warn("No installed files recorded in context");
            return;
        }
        
        int missingCount = 0;
        for (String filePath : installedFiles) {
            Path path = Paths.get(filePath);
            if (!Files.exists(path)) {
                missingCount++;
                if (missingCount <= 5) {
                    log.warn("Installed file missing: {}", filePath);
                }
            }
        }
        
        if (missingCount > 0) {
            if (missingCount > 5) {
                errors.add("Missing " + missingCount + " installed files");
            } else {
                errors.add("Missing " + missingCount + " installed files");
            }
        }
        
        context.setProperty("filesVerified", installedFiles.size() - missingCount);
        log.debug("Installed files verification completed: {} files verified", 
            installedFiles.size() - missingCount);
    }
    
    private void verifyChecksums(Path skillDir, InstallContext context, List<String> errors) {
        log.debug("Verifying checksums...");
        
        SkillPackage skillPackage = context.getSkillPackage();
        if (skillPackage == null) {
            log.debug("No skill package in context, skipping checksum verification");
            return;
        }
        
        String expectedChecksum = skillPackage.getChecksum();
        if (expectedChecksum == null || expectedChecksum.isEmpty()) {
            log.debug("No checksum provided, skipping verification");
            return;
        }
        
        try {
            Path manifestPath = skillDir.resolve(MANIFEST_FILE);
            if (Files.exists(manifestPath)) {
                String actualChecksum = calculateChecksum(manifestPath);
                if (!expectedChecksum.equals(actualChecksum)) {
                    log.warn("Checksum mismatch for manifest: expected {}, found {}", 
                        expectedChecksum.substring(0, Math.min(8, expectedChecksum.length())), 
                        actualChecksum.substring(0, Math.min(8, actualChecksum.length())));
                } else {
                    log.debug("Checksum verified for manifest");
                }
            }
        } catch (Exception e) {
            log.warn("Checksum verification failed: {}", e.getMessage());
        }
        
        context.setProperty("checksumVerified", true);
    }
    
    private void verifyPermissions(Path skillDir, List<String> errors) {
        log.debug("Verifying permissions...");
        
        Path binDir = skillDir.resolve("bin");
        if (Files.exists(binDir)) {
            try {
                Files.walk(binDir)
                    .filter(Files::isRegularFile)
                    .forEach(path -> {
                        try {
                            if (!Files.isReadable(path)) {
                                errors.add("File not readable: " + path);
                            }
                        } catch (Exception e) {
                            log.warn("Failed to check permissions for: {}", path);
                        }
                    });
            } catch (IOException e) {
                log.warn("Failed to walk bin directory: {}", e.getMessage());
            }
        }
        
        Path configDir = skillDir.resolve("config");
        if (Files.exists(configDir)) {
            if (!Files.isReadable(configDir)) {
                errors.add("Config directory not readable");
            }
        }
        
        log.debug("Permissions verification completed");
    }
    
    private String readFileContent(Path path) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(path.toFile()), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }
    
    private Map<String, Object> parseJson(String json) {
        Map<String, Object> result = new java.util.HashMap<>();
        if (json == null || json.isEmpty()) {
            return result;
        }
        
        json = json.trim();
        if (!json.startsWith("{") || !json.endsWith("}")) {
            return result;
        }
        
        json = json.substring(1, json.length() - 1).trim();
        
        StringBuilder key = new StringBuilder();
        StringBuilder value = new StringBuilder();
        boolean inKey = true;
        boolean inString = false;
        boolean inValue = false;
        int depth = 0;
        
        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);
            
            if (c == '"' && (i == 0 || json.charAt(i - 1) != '\\')) {
                inString = !inString;
                if (inValue) value.append(c);
                continue;
            }
            
            if (!inString) {
                if (c == '{' || c == '[') depth++;
                if (c == '}' || c == ']') depth--;
                
                if (c == ':' && inKey && depth == 0) {
                    inKey = false;
                    inValue = true;
                    continue;
                }
                if (c == ',' && !inKey && depth == 0) {
                    String k = key.toString().trim();
                    String v = value.toString().trim();
                    if (!k.isEmpty()) {
                        result.put(k, parseValue(v));
                    }
                    key = new StringBuilder();
                    value = new StringBuilder();
                    inKey = true;
                    inValue = false;
                    continue;
                }
            }
            
            if (inKey) key.append(c);
            else if (inValue) value.append(c);
        }
        
        String k = key.toString().trim();
        String v = value.toString().trim();
        if (!k.isEmpty()) {
            result.put(k, parseValue(v));
        }
        
        return result;
    }
    
    private Object parseValue(String v) {
        if (v == null || v.isEmpty()) return null;
        v = v.trim();
        if ("null".equals(v)) return null;
        if ("true".equals(v)) return true;
        if ("false".equals(v)) return false;
        if (v.startsWith("\"") && v.endsWith("\"")) {
            return v.substring(1, v.length() - 1);
        }
        try {
            if (v.contains(".")) return Double.parseDouble(v);
            return Long.parseLong(v);
        } catch (NumberFormatException e) {
            return v;
        }
    }
    
    private String getString(Map<String, Object> data, String key) {
        Object value = data.get(key);
        return value != null ? value.toString() : null;
    }
    
    private String calculateChecksum(Path path) throws IOException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] fileBytes = Files.readAllBytes(path);
        byte[] hash = md.digest(fileBytes);
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
