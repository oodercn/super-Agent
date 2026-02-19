
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ooder.sdk.api.skill.SkillPackage;

public class DependencyCheckStage implements InstallStage {
    
    private static final Logger log = LoggerFactory.getLogger(DependencyCheckStage.class);
    
    private static final Pattern DEPENDENCY_PATTERN = Pattern.compile(
        "^\\s*([^:\\s]+)\\s*(?::\\s*([^:\\s]+)\\s*)?(?::\\s*([^:\\s]+)\\s*)?$"
    );
    
    private static final String SKILLS_BASE_PATH = System.getProperty("user.home") + 
        File.separator + ".ooder" + File.separator + "skills";
    
    private final Map<String, Boolean> dependencyCache = new HashMap<>();
    
    @Override
    public String getName() {
        return "dependency-check";
    }
    
    @Override
    public void execute(InstallContext context) throws Exception {
        log.debug("Checking dependencies for: {}", context.getSkillId());
        
        SkillPackage skillPackage = context.getSkillPackage();
        if (skillPackage == null) {
            log.warn("No skill package in context, skipping dependency check");
            return;
        }
        
        List<String> dependencies = skillPackage.getDependencies();
        if (dependencies == null || dependencies.isEmpty()) {
            log.debug("No dependencies declared for: {}", context.getSkillId());
            return;
        }
        
        List<String> unsatisfied = new ArrayList<>();
        
        for (String dep : dependencies) {
            DependencyInfo depInfo = parseDependency(dep);
            
            if (!isDependencySatisfied(depInfo)) {
                unsatisfied.add(dep);
                log.warn("Dependency not satisfied: {} (type={}, version={})", 
                    dep, depInfo.type, depInfo.version);
            } else {
                context.addInstalledDependency(dep);
                log.debug("Dependency satisfied: {}", dep);
            }
        }
        
        if (!unsatisfied.isEmpty()) {
            throw new InstallException("Dependencies not satisfied: " + String.join(", ", unsatisfied));
        }
        
        log.debug("All dependencies satisfied for: {}", context.getSkillId());
    }
    
    private DependencyInfo parseDependency(String dependency) {
        DependencyInfo info = new DependencyInfo();
        info.raw = dependency;
        
        Matcher matcher = DEPENDENCY_PATTERN.matcher(dependency);
        if (matcher.matches()) {
            info.name = matcher.group(1) != null ? matcher.group(1).trim() : dependency;
            info.type = matcher.group(2) != null ? matcher.group(2).trim() : "skill";
            info.version = matcher.group(3) != null ? matcher.group(3).trim() : null;
        } else {
            info.name = dependency;
            info.type = "skill";
        }
        
        return info;
    }
    
    private boolean isDependencySatisfied(DependencyInfo depInfo) {
        String cacheKey = depInfo.name + ":" + depInfo.type + ":" + 
            (depInfo.version != null ? depInfo.version : "*");
        
        if (dependencyCache.containsKey(cacheKey)) {
            return dependencyCache.get(cacheKey);
        }
        
        boolean satisfied = checkDependency(depInfo);
        dependencyCache.put(cacheKey, satisfied);
        
        return satisfied;
    }
    
    private boolean checkDependency(DependencyInfo depInfo) {
        switch (depInfo.type.toLowerCase()) {
            case "skill":
                return checkSkillDependency(depInfo);
            case "library":
                return checkLibraryDependency(depInfo);
            case "system":
                return checkSystemDependency(depInfo);
            case "java":
                return checkJavaDependency(depInfo);
            case "native":
                return checkNativeDependency(depInfo);
            default:
                log.warn("Unknown dependency type: {}, assuming satisfied", depInfo.type);
                return true;
        }
    }
    
    private boolean checkSkillDependency(DependencyInfo depInfo) {
        Path skillPath = Paths.get(SKILLS_BASE_PATH, depInfo.name);
        
        if (!Files.exists(skillPath)) {
            log.debug("Skill dependency not found at: {}", skillPath);
            return false;
        }
        
        if (depInfo.version != null) {
            Path manifestPath = skillPath.resolve("skill.json");
            if (Files.exists(manifestPath)) {
                try {
                    String version = readVersionFromManifest(manifestPath);
                    if (version != null && !versionMatches(version, depInfo.version)) {
                        log.debug("Skill version mismatch: expected {}, found {}", 
                            depInfo.version, version);
                        return false;
                    }
                } catch (IOException e) {
                    log.warn("Failed to read manifest for skill: {}", depInfo.name);
                }
            }
        }
        
        log.debug("Skill dependency satisfied: {}", depInfo.name);
        return true;
    }
    
    private boolean checkLibraryDependency(DependencyInfo depInfo) {
        String libraryPath = System.getProperty("java.library.path", "");
        String[] paths = libraryPath.split(File.pathSeparator);
        
        String libFileName = getLibraryFileName(depInfo.name);
        
        for (String path : paths) {
            File libFile = new File(path, libFileName);
            if (libFile.exists()) {
                log.debug("Library dependency found: {} at {}", depInfo.name, path);
                return true;
            }
        }
        
        try {
            String classpath = System.getProperty("java.class.path", "");
            if (classpath.contains(depInfo.name)) {
                log.debug("Library found in classpath: {}", depInfo.name);
                return true;
            }
        } catch (Exception e) {
            // Ignore
        }
        
        log.debug("Library dependency not found: {}", depInfo.name);
        return false;
    }
    
    private boolean checkSystemDependency(DependencyInfo depInfo) {
        String[] commands = getCheckCommands(depInfo.name);
        
        for (String cmd : commands) {
            try {
                ProcessBuilder pb = new ProcessBuilder(cmd.split("\\s+"));
                pb.redirectErrorStream(true);
                Process process = pb.start();
                
                boolean completed = process.waitFor(5, java.util.concurrent.TimeUnit.SECONDS);
                if (completed && process.exitValue() == 0) {
                    log.debug("System dependency satisfied: {}", depInfo.name);
                    return true;
                }
            } catch (Exception e) {
                log.debug("System dependency check failed for {}: {}", depInfo.name, e.getMessage());
            }
        }
        
        log.debug("System dependency not found: {}", depInfo.name);
        return false;
    }
    
    private boolean checkJavaDependency(DependencyInfo depInfo) {
        String className = depInfo.name;
        
        try {
            Class<?> clazz = Class.forName(className);
            log.debug("Java class dependency satisfied: {}", className);
            return true;
        } catch (ClassNotFoundException e) {
            log.debug("Java class dependency not found: {}", className);
            return false;
        }
    }
    
    private boolean checkNativeDependency(DependencyInfo depInfo) {
        try {
            System.loadLibrary(depInfo.name);
            log.debug("Native library dependency satisfied: {}", depInfo.name);
            return true;
        } catch (UnsatisfiedLinkError e) {
            log.debug("Native library dependency not found: {}", depInfo.name);
            return false;
        }
    }
    
    private String readVersionFromManifest(Path manifestPath) throws IOException {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(manifestPath.toFile()), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("\"version\"") || line.startsWith("version")) {
                    int colonIndex = line.indexOf(':');
                    if (colonIndex > 0) {
                        String version = line.substring(colonIndex + 1).trim();
                        version = version.replace("\"", "").replace(",", "").trim();
                        return version.isEmpty() ? null : version;
                    }
                }
            }
        }
        return null;
    }
    
    private boolean versionMatches(String actual, String required) {
        if (required == null || required.isEmpty() || "*".equals(required)) {
            return true;
        }
        
        if (required.startsWith(">=")) {
            String minVersion = required.substring(2).trim();
            return compareVersions(actual, minVersion) >= 0;
        }
        
        if (required.startsWith(">")) {
            String minVersion = required.substring(1).trim();
            return compareVersions(actual, minVersion) > 0;
        }
        
        if (required.startsWith("<=")) {
            String maxVersion = required.substring(2).trim();
            return compareVersions(actual, maxVersion) <= 0;
        }
        
        if (required.startsWith("<")) {
            String maxVersion = required.substring(1).trim();
            return compareVersions(actual, maxVersion) < 0;
        }
        
        if (required.startsWith("^")) {
            String baseVersion = required.substring(1).trim();
            return isCompatibleVersion(actual, baseVersion);
        }
        
        if (required.startsWith("~")) {
            String baseVersion = required.substring(1).trim();
            return isPatchCompatible(actual, baseVersion);
        }
        
        return actual.equals(required);
    }
    
    private int compareVersions(String v1, String v2) {
        String[] parts1 = v1.split("\\.");
        String[] parts2 = v2.split("\\.");
        
        int maxLen = Math.max(parts1.length, parts2.length);
        for (int i = 0; i < maxLen; i++) {
            int num1 = i < parts1.length ? parseVersionPart(parts1[i]) : 0;
            int num2 = i < parts2.length ? parseVersionPart(parts2[i]) : 0;
            
            if (num1 != num2) {
                return Integer.compare(num1, num2);
            }
        }
        return 0;
    }
    
    private int parseVersionPart(String part) {
        try {
            return Integer.parseInt(part.replaceAll("[^0-9]", ""));
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    
    private boolean isCompatibleVersion(String actual, String base) {
        String[] actualParts = actual.split("\\.");
        String[] baseParts = base.split("\\.");
        
        if (actualParts.length < 1 || baseParts.length < 1) {
            return false;
        }
        
        int actualMajor = parseVersionPart(actualParts[0]);
        int baseMajor = parseVersionPart(baseParts[0]);
        
        if (actualMajor != baseMajor) {
            return false;
        }
        
        if (actualMajor == 0) {
            if (actualParts.length < 2 || baseParts.length < 2) {
                return false;
            }
            int actualMinor = parseVersionPart(actualParts[1]);
            int baseMinor = parseVersionPart(baseParts[1]);
            return actualMinor == baseMinor;
        }
        
        return compareVersions(actual, base) >= 0;
    }
    
    private boolean isPatchCompatible(String actual, String base) {
        String[] actualParts = actual.split("\\.");
        String[] baseParts = base.split("\\.");
        
        if (actualParts.length < 2 || baseParts.length < 2) {
            return compareVersions(actual, base) >= 0;
        }
        
        int actualMajor = parseVersionPart(actualParts[0]);
        int baseMajor = parseVersionPart(baseParts[0]);
        int actualMinor = parseVersionPart(actualParts[1]);
        int baseMinor = parseVersionPart(baseParts[1]);
        
        return actualMajor == baseMajor && actualMinor == baseMinor && 
            compareVersions(actual, base) >= 0;
    }
    
    private String getLibraryFileName(String name) {
        String os = System.getProperty("os.name", "").toLowerCase();
        if (os.contains("win")) {
            return name + ".dll";
        } else if (os.contains("mac")) {
            return "lib" + name + ".dylib";
        } else {
            return "lib" + name + ".so";
        }
    }
    
    private String[] getCheckCommands(String name) {
        String os = System.getProperty("os.name", "").toLowerCase();
        if (os.contains("win")) {
            return new String[] { "where " + name, name + " --version" };
        } else {
            return new String[] { "which " + name, name + " --version", name + " -v" };
        }
    }
    
    private static class DependencyInfo {
        String raw;
        String name;
        String type;
        String version;
    }
}
