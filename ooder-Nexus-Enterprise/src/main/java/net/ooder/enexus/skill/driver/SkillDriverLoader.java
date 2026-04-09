package net.ooder.enexus.skill.driver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SkillDriverLoader {

    private static final Logger log = LoggerFactory.getLogger(SkillDriverLoader.class);

    private static final String DRIVERS_PATH = "./skills/_drivers";
    private static final String SPI_PREFIX = "META-INF/services/";

    private final Map<String, Object> driverInstances = new ConcurrentHashMap<>();
    private final Map<String, Class<?>> driverInterfaces = new ConcurrentHashMap<>();

    public <T> T loadDriver(String skillId, Class<T> serviceInterface) {
        String cacheKey = skillId + ":" + serviceInterface.getName();
        
        @SuppressWarnings("unchecked")
        T cached = (T) driverInstances.get(cacheKey);
        if (cached != null) {
            return cached;
        }

        try {
            Path skillDir = findSkillDir(skillId);
            if (skillDir == null) {
                log.warn("[SkillDriverLoader] Skill directory not found: {}", skillId);
                return null;
            }

            Path jarFile = findJarFile(skillDir);
            if (jarFile == null) {
                log.warn("[SkillDriverLoader] JAR file not found for skill: {}", skillId);
                return null;
            }

            URL jarUrl = jarFile.toUri().toURL();
            URLClassLoader classLoader = new URLClassLoader(new URL[]{jarUrl}, getClass().getClassLoader());

            String serviceName = serviceInterface.getName();
            String spiFile = SPI_PREFIX + serviceName;

            InputStream spiStream = classLoader.getResourceAsStream(spiFile);
            if (spiStream == null) {
                log.warn("[SkillDriverLoader] SPI file not found: {}", spiFile);
                return null;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(spiStream));
            String implClassName = reader.readLine();
            reader.close();

            if (implClassName == null || implClassName.trim().isEmpty()) {
                log.warn("[SkillDriverLoader] No implementation class defined in SPI file");
                return null;
            }

            Class<?> implClass = classLoader.loadClass(implClassName.trim());
            Object instance = implClass.getDeclaredConstructor().newInstance();

            @SuppressWarnings("unchecked")
            T typedInstance = (T) instance;
            driverInstances.put(cacheKey, typedInstance);
            driverInterfaces.put(cacheKey, implClass);

            log.info("[SkillDriverLoader] Loaded driver: {} -> {}", skillId, implClassName);
            return typedInstance;

        } catch (Exception e) {
            log.error("[SkillDriverLoader] Failed to load driver: {} - {}", skillId, e.getMessage());
            return null;
        }
    }

    private Path findSkillDir(String skillId) {
        Path driversPath = Paths.get(DRIVERS_PATH);
        if (!Files.exists(driversPath)) {
            return null;
        }

        try {
            return Files.walk(driversPath)
                .filter(Files::isDirectory)
                .filter(p -> p.getFileName().toString().equals(skillId))
                .findFirst()
                .orElse(null);
        } catch (Exception e) {
            log.error("[SkillDriverLoader] Error finding skill directory: {}", e.getMessage());
            return null;
        }
    }

    private Path findJarFile(Path skillDir) {
        Path targetDir = skillDir.resolve("target");
        if (!Files.exists(targetDir)) {
            return null;
        }

        try {
            return Files.list(targetDir)
                .filter(p -> p.toString().endsWith(".jar"))
                .filter(p -> !p.getFileName().toString().contains("original"))
                .findFirst()
                .orElse(null);
        } catch (Exception e) {
            log.error("[SkillDriverLoader] Error finding JAR file: {}", e.getMessage());
            return null;
        }
    }

    public <T> T getDriver(String skillId, Class<T> serviceInterface) {
        return loadDriver(skillId, serviceInterface);
    }

    public boolean isDriverLoaded(String skillId, Class<?> serviceInterface) {
        String cacheKey = skillId + ":" + serviceInterface.getName();
        return driverInstances.containsKey(cacheKey);
    }

    public void unloadDriver(String skillId) {
        driverInstances.keySet().removeIf(key -> key.startsWith(skillId + ":"));
        log.info("[SkillDriverLoader] Unloaded driver: {}", skillId);
    }

    public void unloadAllDrivers() {
        driverInstances.clear();
        log.info("[SkillDriverLoader] Unloaded all drivers");
    }
}
