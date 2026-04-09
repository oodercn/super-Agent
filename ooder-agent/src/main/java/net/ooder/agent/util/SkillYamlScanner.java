package net.ooder.agent.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * 技能 YAML 扫描工具类，扫描技能目录并解析 skill.yaml 文件
 */
public final class SkillYamlScanner {

    private static final Logger log = LoggerFactory.getLogger(SkillYamlScanner.class);

    private SkillYamlScanner() {}

    /**
     * 扫描开发目录下的所有技能
     */
    public static List<Map<String, Object>> scanDevDirectory() {
        List<Map<String, Object>> skills = new ArrayList<>();
        String userDir = System.getProperty("user.dir");
        Path devPath = Paths.get(userDir, ".ooder", "dev");

        if (!Files.exists(devPath)) {
            devPath = Paths.get(userDir, "skills");
        }

        if (!Files.exists(devPath)) {
            log.warn("[SkillYamlScanner] Dev directory not found: {}", devPath);
            return skills;
        }

        scanForSkillYamlFiles(devPath, skills);
        return skills;
    }

    /**
     * 递归扫描目录下的 skill.yaml 文件
     */
    public static void scanForSkillYamlFiles(Path dir, List<Map<String, Object>> skills) {
        try (var stream = Files.walk(dir, 3)) {
            stream.filter(Files::isRegularFile)
                  .filter(p -> p.getFileName().toString().equals("skill.yaml"))
                  .forEach(p -> {
                      try {
                          Map<String, Object> skill = parseSkillYaml(p);
                          if (skill != null && !skill.isEmpty()) {
                              skill.put("_sourcePath", p.getParent().toString());
                              skills.add(skill);
                          }
                      } catch (Exception e) {
                          log.warn("[SkillYamlScanner] Failed to parse {}: {}", p, e.getMessage());
                      }
                  });
        } catch (IOException e) {
            log.warn("[SkillYamlScanner] Failed to scan directory {}: {}", dir, e.getMessage());
        }
    }

    /**
     * 解析 skill.yaml 文件
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> parseSkillYaml(Path yamlPath) {
        try (InputStream is = Files.newInputStream(yamlPath)) {
            Yaml yaml = new Yaml();
            Map<String, Object> data = yaml.load(is);
            if (data == null) return null;

            Map<String, Object> result = new HashMap<>();
            result.put("id", data.getOrDefault("id", "unknown"));
            result.put("name", data.getOrDefault("name", "Unknown"));
            result.put("version", data.getOrDefault("version", "0.0.0"));
            result.put("description", data.getOrDefault("description", ""));
            result.put("category", data.getOrDefault("category", "other"));
            result.put("form", data.getOrDefault("form", "skill"));
            result.put("author", data.getOrDefault("author", "unknown"));

            Object capabilities = data.get("capabilities");
            if (capabilities instanceof List) {
                result.put("capabilityCount", ((List<?>) capabilities).size());
            } else {
                result.put("capabilityCount", 0);
            }

            return result;
        } catch (Exception e) {
            log.warn("[SkillYamlScanner] Failed to parse YAML {}: {}", yamlPath, e.getMessage());
            return null;
        }
    }
}
