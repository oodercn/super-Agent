
package net.ooder.sdk.core.skill.installer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ooder.sdk.api.skill.Capability;
import net.ooder.sdk.api.skill.SkillManifest;
import net.ooder.sdk.api.skill.SkillPackage;

public class ConfigurationStage implements InstallStage {
    
    private static final Logger log = LoggerFactory.getLogger(ConfigurationStage.class);
    
    private static final String CONFIG_DIR = "config";
    private static final String CONFIG_FILE = "skill.properties";
    private static final String ENV_FILE = "environment.properties";
    private static final String CAPABILITIES_FILE = "capabilities.json";
    
    @Override
    public String getName() {
        return "configuration";
    }
    
    @Override
    public void execute(InstallContext context) throws Exception {
        log.debug("Configuring skill: {}", context.getSkillId());
        
        context.setStatus(InstallStatus.CONFIGURING);
        
        String installPath = context.getInstallPath();
        if (installPath == null || installPath.isEmpty()) {
            throw new InstallException("Install path not set");
        }
        
        Path skillDir = Paths.get(installPath);
        if (!Files.exists(skillDir)) {
            throw new InstallException("Skill directory does not exist: " + installPath);
        }
        
        Path configDir = skillDir.resolve(CONFIG_DIR);
        Files.createDirectories(configDir);
        
        SkillPackage skillPackage = context.getSkillPackage();
        SkillManifest manifest = skillPackage != null ? skillPackage.getManifest() : null;
        
        createSkillConfig(configDir, context, manifest);
        
        createEnvironmentConfig(configDir, context);
        
        createCapabilitiesConfig(configDir, skillPackage);
        
        applyUserConfigOverrides(configDir, context);
        
        validateConfiguration(configDir, context);
        
        context.setProperty("configured", true);
        context.setProperty("configTime", System.currentTimeMillis());
        context.setProperty("configPath", configDir.toString());
        
        log.debug("Configuration completed for: {}", context.getSkillId());
    }
    
    private void createSkillConfig(Path configDir, InstallContext context, SkillManifest manifest) throws IOException {
        Path configFile = configDir.resolve(CONFIG_FILE);
        
        Properties props = new Properties();
        
        props.setProperty("skill.id", context.getSkillId());
        props.setProperty("skill.installed.at", String.valueOf(System.currentTimeMillis()));
        props.setProperty("skill.install.path", context.getInstallPath());
        
        if (manifest != null) {
            if (manifest.getName() != null) {
                props.setProperty("skill.name", manifest.getName());
            }
            if (manifest.getVersion() != null) {
                props.setProperty("skill.version", manifest.getVersion());
            }
            if (manifest.getMainClass() != null) {
                props.setProperty("skill.main.class", manifest.getMainClass());
            }
            if (manifest.getAuthor() != null) {
                props.setProperty("skill.author", manifest.getAuthor());
            }
            if (manifest.getSceneId() != null) {
                props.setProperty("skill.scene.id", manifest.getSceneId());
            }
        }
        
        props.setProperty("skill.log.level", "INFO");
        props.setProperty("skill.log.path", "logs");
        props.setProperty("skill.data.path", "data");
        props.setProperty("skill.temp.path", "temp");
        props.setProperty("skill.thread.pool.size", "4");
        props.setProperty("skill.timeout.ms", "30000");
        props.setProperty("skill.retry.count", "3");
        
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(configFile.toFile()), StandardCharsets.UTF_8))) {
            props.store(writer, "Skill Configuration - " + context.getSkillId());
        }
        
        context.addInstalledFile(configFile.toString());
        log.debug("Created skill configuration: {}", configFile);
    }
    
    private void createEnvironmentConfig(Path configDir, InstallContext context) throws IOException {
        Path envFile = configDir.resolve(ENV_FILE);
        
        Properties envProps = new Properties();
        
        envProps.setProperty("JAVA_OPTS", "-Xms128m -Xmx512m");
        envProps.setProperty("SKILL_HOME", context.getInstallPath());
        envProps.setProperty("SKILL_CONFIG_DIR", configDir.toString());
        envProps.setProperty("SKILL_LOG_DIR", context.getInstallPath() + File.separator + "logs");
        envProps.setProperty("SKILL_DATA_DIR", context.getInstallPath() + File.separator + "data");
        
        String javaHome = System.getProperty("java.home");
        if (javaHome != null) {
            envProps.setProperty("JAVA_HOME", javaHome);
        }
        
        String userHome = System.getProperty("user.home");
        if (userHome != null) {
            envProps.setProperty("USER_HOME", userHome);
        }
        
        String osName = System.getProperty("os.name");
        if (osName != null) {
            envProps.setProperty("OS_NAME", osName);
        }
        
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(envFile.toFile()), StandardCharsets.UTF_8))) {
            envProps.store(writer, "Environment Configuration - " + context.getSkillId());
        }
        
        context.addInstalledFile(envFile.toString());
        log.debug("Created environment configuration: {}", envFile);
    }
    
    private void createCapabilitiesConfig(Path configDir, SkillPackage skillPackage) throws IOException {
        Path capFile = configDir.resolve(CAPABILITIES_FILE);
        
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"capabilities\": [\n");
        
        if (skillPackage != null && skillPackage.getCapabilities() != null) {
            List<Capability> capabilities = skillPackage.getCapabilities();
            for (int i = 0; i < capabilities.size(); i++) {
                Capability cap = capabilities.get(i);
                sb.append("    {\n");
                sb.append("      \"capId\": \"").append(escapeJson(cap.getCapId())).append("\",\n");
                sb.append("      \"name\": \"").append(escapeJson(cap.getName())).append("\",\n");
                sb.append("      \"description\": \"").append(escapeJson(cap.getDescription())).append("\",\n");
                sb.append("      \"returnType\": \"").append(escapeJson(cap.getReturnType() != null ? cap.getReturnType() : "")).append("\",\n");
                sb.append("      \"async\": ").append(cap.isAsync()).append("\n");
                sb.append("    }");
                if (i < capabilities.size() - 1) {
                    sb.append(",");
                }
                sb.append("\n");
            }
        }
        
        sb.append("  ],\n");
        sb.append("  \"registeredAt\": ").append(System.currentTimeMillis()).append("\n");
        sb.append("}\n");
        
        Files.write(capFile, sb.toString().getBytes(StandardCharsets.UTF_8));
        log.debug("Created capabilities configuration: {}", capFile);
    }
    
    private void applyUserConfigOverrides(Path configDir, InstallContext context) throws IOException {
        Map<String, Object> userConfig = context.getProperties();
        if (userConfig == null || userConfig.isEmpty()) {
            return;
        }
        
        Path configFile = configDir.resolve(CONFIG_FILE);
        if (!Files.exists(configFile)) {
            return;
        }
        
        Properties props = new Properties();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(configFile.toFile()), StandardCharsets.UTF_8))) {
            props.load(reader);
        }
        
        boolean modified = false;
        for (Map.Entry<String, Object> entry : userConfig.entrySet()) {
            String key = entry.getKey();
            if (key.startsWith("config.")) {
                String configKey = key.substring(7);
                Object value = entry.getValue();
                if (value != null) {
                    props.setProperty(configKey, value.toString());
                    modified = true;
                    log.debug("Applied config override: {} = {}", configKey, value);
                }
            }
        }
        
        if (modified) {
            try (BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(configFile.toFile()), StandardCharsets.UTF_8))) {
                props.store(writer, "Skill Configuration (with user overrides) - " + context.getSkillId());
            }
        }
    }
    
    private void validateConfiguration(Path configDir, InstallContext context) throws InstallException {
        Path configFile = configDir.resolve(CONFIG_FILE);
        
        if (!Files.exists(configFile)) {
            throw new InstallException("Configuration file not created: " + configFile);
        }
        
        try {
            Properties props = new Properties();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(configFile.toFile()), StandardCharsets.UTF_8))) {
                props.load(reader);
            }
            
            String skillId = props.getProperty("skill.id");
            if (skillId == null || skillId.isEmpty()) {
                throw new InstallException("Configuration missing required property: skill.id");
            }
            
            if (!skillId.equals(context.getSkillId())) {
                throw new InstallException("Configuration skill.id mismatch: expected " + 
                    context.getSkillId() + ", found " + skillId);
            }
            
            String mainClass = props.getProperty("skill.main.class");
            if (mainClass != null && !mainClass.isEmpty()) {
                try {
                    Class.forName(mainClass);
                    log.debug("Main class validated: {}", mainClass);
                } catch (ClassNotFoundException e) {
                    log.warn("Main class not found in classpath: {}", mainClass);
                }
            }
            
            log.debug("Configuration validated successfully");
            
        } catch (IOException e) {
            throw new InstallException("Failed to validate configuration: " + e.getMessage(), e);
        }
    }
    
    private String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
