package net.ooder.sdk.infra.observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class ConfigObserver {
    
    private static final Logger log = LoggerFactory.getLogger(ConfigObserver.class);
    
    private final Path configBasePath;
    private final Path sceneConfigPath;
    private final Path groupConfigPath;
    private final Path skillConfigPath;
    private final Path runtimePath;
    
    private final Map<String, InterfaceCallRecord> interfaceCalls = new ConcurrentHashMap<>();
    private final List<ConfigWriteRecord> configWrites = new CopyOnWriteArrayList<>();
    private final List<ConfigReadRecord> configReads = new CopyOnWriteArrayList<>();
    private final List<ConfigChangeListener> listeners = new CopyOnWriteArrayList<>();
    
    private volatile boolean observing = false;
    
    public ConfigObserver(String basePath) {
        this.configBasePath = Paths.get(basePath);
        this.sceneConfigPath = configBasePath.resolve("scenes");
        this.groupConfigPath = configBasePath.resolve("groups");
        this.skillConfigPath = configBasePath.resolve("skills");
        this.runtimePath = configBasePath.resolve("runtime");
        
        try {
            Files.createDirectories(sceneConfigPath);
            Files.createDirectories(groupConfigPath);
            Files.createDirectories(skillConfigPath);
            Files.createDirectories(runtimePath);
        } catch (IOException e) {
            log.error("Failed to create config directories", e);
        }
    }
    
    public void startObserving() {
        log.info("Starting ConfigObserver at: {}", configBasePath);
        observing = true;
    }
    
    public void stopObserving() {
        log.info("Stopping ConfigObserver");
        observing = false;
    }
    
    public void addListener(ConfigChangeListener listener) {
        listeners.add(listener);
    }
    
    public void removeListener(ConfigChangeListener listener) {
        listeners.remove(listener);
    }
    
    public void recordInterfaceCall(String interfaceName, String methodName, 
                                    Map<String, Object> params, Object result) {
        String key = interfaceName + "." + methodName;
        InterfaceCallRecord record = interfaceCalls.computeIfAbsent(
            key, k -> new InterfaceCallRecord(interfaceName, methodName)
        );
        record.recordCall(params, result);
        log.debug("Interface call recorded: {} - {} times", key, record.getCallCount());
    }
    
    public boolean writeSceneConfig(String sceneId, Map<String, Object> config) {
        String fileName = sceneId + ".yaml";
        Path filePath = sceneConfigPath.resolve(fileName);
        
        try {
            String content = mapToYaml(config);
            Files.write(filePath, content.getBytes(StandardCharsets.UTF_8), 
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            
            ConfigWriteRecord record = new ConfigWriteRecord(
                "scene", sceneId, filePath.toString(), true, content
            );
            configWrites.add(record);
            
            notifyConfigWritten("scene", sceneId, config);
            
            log.info("Scene config written: {} -> {}", sceneId, filePath);
            return true;
        } catch (IOException e) {
            ConfigWriteRecord record = new ConfigWriteRecord(
                "scene", sceneId, filePath.toString(), false, null
            );
            configWrites.add(record);
            
            log.error("Failed to write scene config: {}", sceneId, e);
            return false;
        }
    }
    
    public Map<String, Object> readSceneConfig(String sceneId) {
        String fileName = sceneId + ".yaml";
        Path filePath = sceneConfigPath.resolve(fileName);
        
        try {
            if (Files.exists(filePath)) {
                String content = new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8);
                Map<String, Object> config = yamlToMap(content);
                
                ConfigReadRecord record = new ConfigReadRecord(
                    "scene", sceneId, filePath.toString(), true, content
                );
                configReads.add(record);
                
                notifyConfigRead("scene", sceneId, config);
                
                log.info("Scene config read: {} <- {}", sceneId, filePath);
                return config;
            }
        } catch (IOException e) {
            ConfigReadRecord record = new ConfigReadRecord(
                "scene", sceneId, filePath.toString(), false, null
            );
            configReads.add(record);
            
            log.error("Failed to read scene config: {}", sceneId, e);
        }
        return null;
    }
    
    public boolean writeGroupConfig(String sceneId, String groupId, Map<String, Object> config) {
        String fileName = sceneId + "_" + groupId + ".yaml";
        Path filePath = groupConfigPath.resolve(fileName);
        
        try {
            String content = mapToYaml(config);
            Files.write(filePath, content.getBytes(StandardCharsets.UTF_8), 
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            
            ConfigWriteRecord record = new ConfigWriteRecord(
                "group", sceneId + ":" + groupId, filePath.toString(), true, content
            );
            configWrites.add(record);
            
            notifyConfigWritten("group", sceneId + ":" + groupId, config);
            
            log.info("Group config written: {}:{} -> {}", sceneId, groupId, filePath);
            return true;
        } catch (IOException e) {
            ConfigWriteRecord record = new ConfigWriteRecord(
                "group", sceneId + ":" + groupId, filePath.toString(), false, null
            );
            configWrites.add(record);
            
            log.error("Failed to write group config: {}:{}", sceneId, groupId, e);
            return false;
        }
    }
    
    public Map<String, Object> readGroupConfig(String sceneId, String groupId) {
        String fileName = sceneId + "_" + groupId + ".yaml";
        Path filePath = groupConfigPath.resolve(fileName);
        
        try {
            if (Files.exists(filePath)) {
                String content = new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8);
                Map<String, Object> config = yamlToMap(content);
                
                ConfigReadRecord record = new ConfigReadRecord(
                    "group", sceneId + ":" + groupId, filePath.toString(), true, content
                );
                configReads.add(record);
                
                notifyConfigRead("group", sceneId + ":" + groupId, config);
                
                log.info("Group config read: {}:{} <- {}", sceneId, groupId, filePath);
                return config;
            }
        } catch (IOException e) {
            ConfigReadRecord record = new ConfigReadRecord(
                "group", sceneId + ":" + groupId, filePath.toString(), false, null
            );
            configReads.add(record);
            
            log.error("Failed to read group config: {}:{}", sceneId, groupId, e);
        }
        return null;
    }
    
    public boolean writeSkillConfig(String skillId, Map<String, Object> config) {
        String fileName = skillId + ".yaml";
        Path filePath = skillConfigPath.resolve(fileName);
        
        try {
            String content = mapToYaml(config);
            Files.write(filePath, content.getBytes(StandardCharsets.UTF_8), 
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            
            ConfigWriteRecord record = new ConfigWriteRecord(
                "skill", skillId, filePath.toString(), true, content
            );
            configWrites.add(record);
            
            notifyConfigWritten("skill", skillId, config);
            
            log.info("Skill config written: {} -> {}", skillId, filePath);
            return true;
        } catch (IOException e) {
            ConfigWriteRecord record = new ConfigWriteRecord(
                "skill", skillId, filePath.toString(), false, null
            );
            configWrites.add(record);
            
            log.error("Failed to write skill config: {}", skillId, e);
            return false;
        }
    }
    
    public Map<String, Object> readSkillConfig(String skillId) {
        String fileName = skillId + ".yaml";
        Path filePath = skillConfigPath.resolve(fileName);
        
        try {
            if (Files.exists(filePath)) {
                String content = new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8);
                Map<String, Object> config = yamlToMap(content);
                
                ConfigReadRecord record = new ConfigReadRecord(
                    "skill", skillId, filePath.toString(), true, content
                );
                configReads.add(record);
                
                notifyConfigRead("skill", skillId, config);
                
                log.info("Skill config read: {} <- {}", skillId, filePath);
                return config;
            }
        } catch (IOException e) {
            ConfigReadRecord record = new ConfigReadRecord(
                "skill", skillId, filePath.toString(), false, null
            );
            configReads.add(record);
            
            log.error("Failed to read skill config: {}", skillId, e);
        }
        return null;
    }
    
    public boolean writeRuntimeConfig(String configId, Map<String, Object> config) {
        String fileName = configId + ".yaml";
        Path filePath = runtimePath.resolve(fileName);
        
        try {
            String content = mapToYaml(config);
            Files.write(filePath, content.getBytes(StandardCharsets.UTF_8), 
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            
            ConfigWriteRecord record = new ConfigWriteRecord(
                "runtime", configId, filePath.toString(), true, content
            );
            configWrites.add(record);
            
            notifyConfigWritten("runtime", configId, config);
            
            log.info("Runtime config written: {} -> {}", configId, filePath);
            return true;
        } catch (IOException e) {
            ConfigWriteRecord record = new ConfigWriteRecord(
                "runtime", configId, filePath.toString(), false, null
            );
            configWrites.add(record);
            
            log.error("Failed to write runtime config: {}", configId, e);
            return false;
        }
    }
    
    public Map<String, Object> getObserverReport() {
        Map<String, Object> report = new LinkedHashMap<>();
        
        report.put("observerPath", configBasePath.toString());
        report.put("observing", observing);
        
        Map<String, Object> interfaceStats = new LinkedHashMap<>();
        for (Map.Entry<String, InterfaceCallRecord> entry : interfaceCalls.entrySet()) {
            Map<String, Object> stat = new LinkedHashMap<>();
            stat.put("interfaceName", entry.getValue().interfaceName);
            stat.put("methodName", entry.getValue().methodName);
            stat.put("callCount", entry.getValue().getCallCount());
            stat.put("lastCallTime", entry.getValue().getLastCallTime());
            interfaceStats.put(entry.getKey(), stat);
        }
        report.put("interfaceCalls", interfaceStats);
        report.put("totalInterfaceCalls", 
            interfaceCalls.values().stream().mapToInt(InterfaceCallRecord::getCallCount).sum());
        
        Map<String, Object> writeStats = new LinkedHashMap<>();
        writeStats.put("totalWrites", configWrites.size());
        writeStats.put("successWrites", configWrites.stream().filter(r -> r.success).count());
        writeStats.put("failedWrites", configWrites.stream().filter(r -> !r.success).count());
        
        Map<String, Long> writesByType = new LinkedHashMap<>();
        for (ConfigWriteRecord record : configWrites) {
            writesByType.merge(record.configType, 1L, Long::sum);
        }
        writeStats.put("writesByType", writesByType);
        report.put("configWrites", writeStats);
        
        Map<String, Object> readStats = new LinkedHashMap<>();
        readStats.put("totalReads", configReads.size());
        readStats.put("successReads", configReads.stream().filter(r -> r.success).count());
        readStats.put("failedReads", configReads.stream().filter(r -> !r.success).count());
        report.put("configReads", readStats);
        
        try {
            Map<String, Object> fileStats = new LinkedHashMap<>();
            fileStats.put("sceneConfigFiles", Files.list(sceneConfigPath).count());
            fileStats.put("groupConfigFiles", Files.list(groupConfigPath).count());
            fileStats.put("skillConfigFiles", Files.list(skillConfigPath).count());
            fileStats.put("runtimeConfigFiles", Files.list(runtimePath).count());
            report.put("fileStats", fileStats);
        } catch (IOException e) {
            log.error("Failed to count files", e);
        }
        
        return report;
    }
    
    public List<Map<String, Object>> getConfigWriteDetails() {
        List<Map<String, Object>> details = new ArrayList<>();
        for (ConfigWriteRecord record : configWrites) {
            Map<String, Object> detail = new LinkedHashMap<>();
            detail.put("configType", record.configType);
            detail.put("configId", record.configId);
            detail.put("filePath", record.filePath);
            detail.put("writeTime", record.writeTime);
            detail.put("success", record.success);
            detail.put("contentLength", record.content != null ? record.content.length() : 0);
            details.add(detail);
        }
        return details;
    }
    
    public List<Map<String, Object>> getInterfaceCallDetails(String interfaceName) {
        List<Map<String, Object>> details = new ArrayList<>();
        for (Map.Entry<String, InterfaceCallRecord> entry : interfaceCalls.entrySet()) {
            if (entry.getKey().startsWith(interfaceName)) {
                InterfaceCallRecord record = entry.getValue();
                for (int i = 0; i < record.callTimes.size(); i++) {
                    Map<String, Object> detail = new LinkedHashMap<>();
                    detail.put("interfaceName", record.interfaceName);
                    detail.put("methodName", record.methodName);
                    detail.put("callTime", record.callTimes.get(i));
                    detail.put("params", record.params.get(i));
                    detail.put("result", record.results.get(i));
                    details.add(detail);
                }
            }
        }
        return details;
    }
    
    public boolean verifyConfigFileExists(String configType, String configId) {
        Path filePath = getConfigPath(configType, configId);
        if (filePath != null) {
            boolean exists = Files.exists(filePath);
            log.info("Config file existence check: {} -> {}", filePath, exists);
            return exists;
        }
        return false;
    }
    
    public String readConfigFileContent(String configType, String configId) {
        Path filePath = getConfigPath(configType, configId);
        if (filePath != null && Files.exists(filePath)) {
            try {
                return new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8);
            } catch (IOException e) {
                log.error("Failed to read config file: {}", filePath, e);
            }
        }
        return null;
    }
    
    private Path getConfigPath(String configType, String configId) {
        switch (configType) {
            case "scene":
                return sceneConfigPath.resolve(configId + ".yaml");
            case "group":
                return groupConfigPath.resolve(configId.replace(":", "_") + ".yaml");
            case "skill":
                return skillConfigPath.resolve(configId + ".yaml");
            case "runtime":
                return runtimePath.resolve(configId + ".yaml");
            default:
                return null;
        }
    }
    
    private void notifyConfigWritten(String configType, String configId, Map<String, Object> config) {
        for (ConfigChangeListener listener : listeners) {
            try {
                listener.onConfigWritten(configType, configId, config);
            } catch (Exception e) {
                log.warn("ConfigChangeListener error", e);
            }
        }
    }
    
    private void notifyConfigRead(String configType, String configId, Map<String, Object> config) {
        for (ConfigChangeListener listener : listeners) {
            try {
                listener.onConfigRead(configType, configId, config);
            } catch (Exception e) {
                log.warn("ConfigChangeListener error", e);
            }
        }
    }
    
    private String mapToYaml(Map<String, Object> map) {
        StringBuilder sb = new StringBuilder();
        mapToYaml(map, sb, 0);
        return sb.toString();
    }
    
    @SuppressWarnings("unchecked")
    private void mapToYaml(Map<String, Object> map, StringBuilder sb, int indent) {
        String indentStr = repeat("  ", indent);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object value = entry.getValue();
            if (value instanceof Map) {
                sb.append(indentStr).append(entry.getKey()).append(":\n");
                mapToYaml((Map<String, Object>) value, sb, indent + 1);
            } else if (value instanceof List) {
                sb.append(indentStr).append(entry.getKey()).append(":\n");
                for (Object item : (List<?>) value) {
                    if (item instanceof Map) {
                        sb.append(indentStr).append("  -\n");
                        mapToYaml((Map<String, Object>) item, sb, indent + 2);
                    } else {
                        sb.append(indentStr).append("  - ").append(item).append("\n");
                    }
                }
            } else if (value != null) {
                sb.append(indentStr).append(entry.getKey()).append(": ").append(value).append("\n");
            }
        }
    }
    
    private Map<String, Object> yamlToMap(String yaml) {
        Map<String, Object> result = new LinkedHashMap<>();
        String[] lines = yaml.split("\n");
        for (String line : lines) {
            if (line.contains(":") && !line.trim().startsWith("-")) {
                String[] parts = line.split(":", 2);
                if (parts.length == 2) {
                    String key = parts[0].trim();
                    String value = parts[1].trim();
                    if (!value.isEmpty()) {
                        result.put(key, parseValue(value));
                    }
                }
            }
        }
        return result;
    }
    
    private Object parseValue(String value) {
        if (value == null || value.isEmpty()) return "";
        if (value.startsWith("\"") && value.endsWith("\"")) {
            return value.substring(1, value.length() - 1);
        }
        if ("true".equalsIgnoreCase(value)) return true;
        if ("false".equalsIgnoreCase(value)) return false;
        try {
            if (value.contains(".")) {
                return Double.parseDouble(value);
            } else {
                return Long.parseLong(value);
            }
        } catch (NumberFormatException e) {
            return value;
        }
    }
    
    private String repeat(String str, int times) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < times; i++) {
            sb.append(str);
        }
        return sb.toString();
    }
    
    public Path getConfigBasePath() { return configBasePath; }
    public Path getSceneConfigPath() { return sceneConfigPath; }
    public Path getGroupConfigPath() { return groupConfigPath; }
    public Path getSkillConfigPath() { return skillConfigPath; }
    public Path getRuntimePath() { return runtimePath; }
    public boolean isObserving() { return observing; }
    
    private static class InterfaceCallRecord {
        String interfaceName;
        String methodName;
        List<Long> callTimes = new ArrayList<>();
        List<Map<String, Object>> params = new ArrayList<>();
        List<Object> results = new ArrayList<>();
        
        InterfaceCallRecord(String interfaceName, String methodName) {
            this.interfaceName = interfaceName;
            this.methodName = methodName;
        }
        
        void recordCall(Map<String, Object> param, Object result) {
            callTimes.add(System.currentTimeMillis());
            params.add(param);
            results.add(result);
        }
        
        int getCallCount() { return callTimes.size(); }
        Long getLastCallTime() { return callTimes.isEmpty() ? null : callTimes.get(callTimes.size() - 1); }
    }
    
    private static class ConfigWriteRecord {
        String configType;
        String configId;
        String filePath;
        long writeTime;
        boolean success;
        String content;
        
        ConfigWriteRecord(String configType, String configId, String filePath, boolean success, String content) {
            this.configType = configType;
            this.configId = configId;
            this.filePath = filePath;
            this.writeTime = System.currentTimeMillis();
            this.success = success;
            this.content = content;
        }
    }
    
    private static class ConfigReadRecord {
        String configType;
        String configId;
        String filePath;
        long readTime;
        boolean success;
        String content;
        
        ConfigReadRecord(String configType, String configId, String filePath, boolean success, String content) {
            this.configType = configType;
            this.configId = configId;
            this.filePath = filePath;
            this.readTime = System.currentTimeMillis();
            this.success = success;
            this.content = content;
        }
    }
}
