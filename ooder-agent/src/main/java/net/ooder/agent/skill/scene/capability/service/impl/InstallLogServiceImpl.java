package net.ooder.mvp.skill.scene.capability.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.ooder.mvp.skill.scene.capability.service.InstallLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class InstallLogServiceImpl implements InstallLogService {

    private static final Logger log = LoggerFactory.getLogger(InstallLogServiceImpl.class);

    private static final String LOG_DIR = "data/logs/install";
    private static final String LOG_FILE_PREFIX = "install-";
    private static final String LOG_FILE_SUFFIX = ".json";
    
    private Map<String, List<InstallLogEntry>> logCache = new ConcurrentHashMap<>();
    private Map<String, InstallLogSummary> summaryCache = new ConcurrentHashMap<>();
    
    @PostConstruct
    public void init() {
        try {
            Path logPath = Paths.get(LOG_DIR);
            if (!Files.exists(logPath)) {
                Files.createDirectories(logPath);
            }
            log.info("[InstallLogService] Initialized, log directory: {}", LOG_DIR);
        } catch (Exception e) {
            log.error("[InstallLogService] Failed to initialize: {}", e.getMessage());
        }
    }

    @Override
    public InstallLogEntry createLog(CreateLogRequest request) {
        InstallLogEntry entry = new InstallLogEntry();
        entry.setLogId("log-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8));
        entry.setInstallId(request.getInstallId());
        entry.setCapabilityId(request.getCapabilityId());
        entry.setUserId(request.getUserId());
        entry.setSceneGroupId(request.getSceneGroupId());
        entry.setLevel(InstallLogEntry.Level.INFO.name());
        entry.setAction("INSTALL_STARTED");
        entry.setMessage("安装开始");
        entry.setTimestamp(System.currentTimeMillis());
        
        addLogToCache(entry);
        persistLog(entry);
        
        log.info("[createLog] Created install log: {}", entry.getLogId());
        return entry;
    }

    @Override
    public InstallLogEntry appendLog(String installId, String level, String action, String message) {
        return appendLog(installId, level, action, message, null);
    }

    @Override
    public InstallLogEntry appendLog(String installId, String level, String action, String message, Map<String, Object> details) {
        List<InstallLogEntry> logs = logCache.get(installId);
        String capabilityId = null;
        String userId = null;
        String sceneGroupId = null;
        
        if (logs != null && !logs.isEmpty()) {
            InstallLogEntry first = logs.get(0);
            capabilityId = first.getCapabilityId();
            userId = first.getUserId();
            sceneGroupId = first.getSceneGroupId();
        }
        
        InstallLogEntry entry = new InstallLogEntry();
        entry.setLogId("log-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8));
        entry.setInstallId(installId);
        entry.setCapabilityId(capabilityId);
        entry.setUserId(userId);
        entry.setSceneGroupId(sceneGroupId);
        entry.setLevel(level);
        entry.setAction(action);
        entry.setMessage(message);
        entry.setDetails(details);
        entry.setTimestamp(System.currentTimeMillis());
        
        addLogToCache(entry);
        persistLog(entry);
        
        updateSummary(installId, entry);
        
        return entry;
    }

    @Override
    public List<InstallLogEntry> getLogsByInstall(String installId) {
        return getLogsByInstall(installId, 1000);
    }

    @Override
    public List<InstallLogEntry> getLogsByInstall(String installId, int limit) {
        List<InstallLogEntry> cached = logCache.get(installId);
        if (cached != null) {
            return cached.stream().limit(limit).collect(Collectors.toList());
        }
        
        List<InstallLogEntry> loaded = loadLogsFromFile(installId);
        if (loaded != null) {
            return loaded.stream().limit(limit).collect(Collectors.toList());
        }
        
        return new ArrayList<>();
    }

    @Override
    public List<InstallLogEntry> getLogsByUser(String userId) {
        List<InstallLogEntry> result = new ArrayList<>();
        
        for (List<InstallLogEntry> logs : logCache.values()) {
            for (InstallLogEntry entry : logs) {
                if (userId.equals(entry.getUserId())) {
                    result.add(entry);
                }
            }
        }
        
        result.sort((a, b) -> Long.compare(b.getTimestamp(), a.getTimestamp()));
        return result;
    }

    @Override
    public List<InstallLogEntry> getLogsByCapability(String capabilityId) {
        List<InstallLogEntry> result = new ArrayList<>();
        
        for (List<InstallLogEntry> logs : logCache.values()) {
            for (InstallLogEntry entry : logs) {
                if (capabilityId.equals(entry.getCapabilityId())) {
                    result.add(entry);
                }
            }
        }
        
        result.sort((a, b) -> Long.compare(b.getTimestamp(), a.getTimestamp()));
        return result;
    }

    @Override
    public List<InstallLogEntry> searchLogs(LogSearchRequest request) {
        List<InstallLogEntry> result = new ArrayList<>();
        
        if (request.getInstallId() != null) {
            result.addAll(getLogsByInstall(request.getInstallId()));
        } else {
            for (List<InstallLogEntry> logs : logCache.values()) {
                result.addAll(logs);
            }
        }
        
        if (request.getLevel() != null) {
            result = result.stream()
                .filter(e -> request.getLevel().equals(e.getLevel()))
                .collect(Collectors.toList());
        }
        
        if (request.getAction() != null) {
            result = result.stream()
                .filter(e -> request.getAction().equals(e.getAction()))
                .collect(Collectors.toList());
        }
        
        if (request.getUserId() != null) {
            result = result.stream()
                .filter(e -> request.getUserId().equals(e.getUserId()))
                .collect(Collectors.toList());
        }
        
        if (request.getCapabilityId() != null) {
            result = result.stream()
                .filter(e -> request.getCapabilityId().equals(e.getCapabilityId()))
                .collect(Collectors.toList());
        }
        
        if (request.getStartTime() != null) {
            result = result.stream()
                .filter(e -> e.getTimestamp() >= request.getStartTime())
                .collect(Collectors.toList());
        }
        
        if (request.getEndTime() != null) {
            result = result.stream()
                .filter(e -> e.getTimestamp() <= request.getEndTime())
                .collect(Collectors.toList());
        }
        
        result.sort((a, b) -> Long.compare(b.getTimestamp(), a.getTimestamp()));
        
        int offset = request.getOffset();
        int limit = request.getLimit();
        
        if (offset >= result.size()) {
            return new ArrayList<>();
        }
        
        int end = Math.min(offset + limit, result.size());
        return result.subList(offset, end);
    }

    @Override
    public InstallLogSummary getSummary(String installId) {
        InstallLogSummary summary = summaryCache.get(installId);
        if (summary != null) {
            return summary;
        }
        
        List<InstallLogEntry> logs = getLogsByInstall(installId);
        summary = calculateSummary(installId, logs);
        summaryCache.put(installId, summary);
        
        return summary;
    }

    @Override
    public void clearLogs(String installId) {
        logCache.remove(installId);
        summaryCache.remove(installId);
        
        try {
            Path logFile = getLogFilePath(installId);
            if (Files.exists(logFile)) {
                Files.delete(logFile);
                log.info("[clearLogs] Deleted log file for: {}", installId);
            }
        } catch (Exception e) {
            log.error("[clearLogs] Failed to delete log file: {}", e.getMessage());
        }
    }

    @Override
    public void clearOldLogs(long beforeTime) {
        int cleared = 0;
        
        for (String installId : logCache.keySet()) {
            List<InstallLogEntry> logs = logCache.get(installId);
            if (logs != null && !logs.isEmpty()) {
                InstallLogEntry last = logs.get(logs.size() - 1);
                if (last.getTimestamp() < beforeTime) {
                    clearLogs(installId);
                    cleared++;
                }
            }
        }
        
        log.info("[clearOldLogs] Cleared {} old log entries", cleared);
    }
    
    private void addLogToCache(InstallLogEntry entry) {
        logCache.computeIfAbsent(entry.getInstallId(), k -> new ArrayList<>()).add(entry);
    }
    
    private void persistLog(InstallLogEntry entry) {
        try {
            Path logFile = getLogFilePath(entry.getInstallId());
            
            JSONArray logs;
            if (Files.exists(logFile)) {
                String content = new String(Files.readAllBytes(logFile), StandardCharsets.UTF_8);
                logs = JSON.parseArray(content);
                if (logs == null) {
                    logs = new JSONArray();
                }
            } else {
                logs = new JSONArray();
            }
            
            JSONObject logJson = new JSONObject();
            logJson.put("logId", entry.getLogId());
            logJson.put("installId", entry.getInstallId());
            logJson.put("capabilityId", entry.getCapabilityId());
            logJson.put("userId", entry.getUserId());
            logJson.put("sceneGroupId", entry.getSceneGroupId());
            logJson.put("level", entry.getLevel());
            logJson.put("action", entry.getAction());
            logJson.put("message", entry.getMessage());
            logJson.put("details", entry.getDetails());
            logJson.put("timestamp", entry.getTimestamp());
            
            logs.add(logJson);
            
            Files.write(logFile, logs.toJSONString().getBytes(StandardCharsets.UTF_8));
            
        } catch (Exception e) {
            log.error("[persistLog] Failed to persist log: {}", e.getMessage());
        }
    }
    
    private List<InstallLogEntry> loadLogsFromFile(String installId) {
        try {
            Path logFile = getLogFilePath(installId);
            if (!Files.exists(logFile)) {
                return null;
            }
            
            String content = new String(Files.readAllBytes(logFile), StandardCharsets.UTF_8);
            JSONArray logs = JSON.parseArray(content);
            
            List<InstallLogEntry> result = new ArrayList<>();
            if (logs != null) {
                for (int i = 0; i < logs.size(); i++) {
                    JSONObject logJson = logs.getJSONObject(i);
                    InstallLogEntry entry = new InstallLogEntry();
                    entry.setLogId(logJson.getString("logId"));
                    entry.setInstallId(logJson.getString("installId"));
                    entry.setCapabilityId(logJson.getString("capabilityId"));
                    entry.setUserId(logJson.getString("userId"));
                    entry.setSceneGroupId(logJson.getString("sceneGroupId"));
                    entry.setLevel(logJson.getString("level"));
                    entry.setAction(logJson.getString("action"));
                    entry.setMessage(logJson.getString("message"));
                    entry.setDetails(logJson.getJSONObject("details") != null 
                        ? logJson.getJSONObject("details").getInnerMap() : null);
                    entry.setTimestamp(logJson.getLongValue("timestamp"));
                    result.add(entry);
                }
            }
            
            logCache.put(installId, result);
            return result;
            
        } catch (Exception e) {
            log.error("[loadLogsFromFile] Failed to load logs: {}", e.getMessage());
            return null;
        }
    }
    
    private Path getLogFilePath(String installId) {
        return Paths.get(LOG_DIR, LOG_FILE_PREFIX + installId + LOG_FILE_SUFFIX);
    }
    
    private void updateSummary(String installId, InstallLogEntry entry) {
        InstallLogSummary summary = summaryCache.computeIfAbsent(installId, k -> {
            InstallLogSummary s = new InstallLogSummary();
            s.setInstallId(installId);
            s.setStartTime(entry.getTimestamp());
            s.setInstalledCapabilities(new ArrayList<>());
            s.setFailedCapabilities(new ArrayList<>());
            return s;
        });
        
        summary.setTotalLogs(summary.getTotalLogs() + 1);
        summary.setEndTime(entry.getTimestamp());
        
        switch (entry.getLevel()) {
            case "ERROR":
                summary.setErrorCount(summary.getErrorCount() + 1);
                break;
            case "WARN":
                summary.setWarnCount(summary.getWarnCount() + 1);
                break;
            case "INFO":
                summary.setInfoCount(summary.getInfoCount() + 1);
                break;
            case "DEBUG":
                summary.setDebugCount(summary.getDebugCount() + 1);
                break;
        }
        
        if ("DEPENDENCY_INSTALLED".equals(entry.getAction())) {
            if (entry.getDetails() != null && entry.getDetails().get("capabilityId") != null) {
                summary.getInstalledCapabilities().add(entry.getDetails().get("capabilityId").toString());
            }
        } else if ("DEPENDENCY_FAILED".equals(entry.getAction())) {
            if (entry.getDetails() != null && entry.getDetails().get("capabilityId") != null) {
                summary.getFailedCapabilities().add(entry.getDetails().get("capabilityId").toString());
            }
        } else if ("INSTALL_COMPLETED".equals(entry.getAction())) {
            summary.setStatus("COMPLETED");
        } else if ("INSTALL_FAILED".equals(entry.getAction())) {
            summary.setStatus("FAILED");
        } else if ("INSTALL_ROLLBACK".equals(entry.getAction())) {
            summary.setStatus("ROLLBACK");
        }
    }
    
    private InstallLogSummary calculateSummary(String installId, List<InstallLogEntry> logs) {
        InstallLogSummary summary = new InstallLogSummary();
        summary.setInstallId(installId);
        summary.setTotalLogs(logs.size());
        summary.setInstalledCapabilities(new ArrayList<>());
        summary.setFailedCapabilities(new ArrayList<>());
        
        for (InstallLogEntry entry : logs) {
            if (summary.getStartTime() == 0 || entry.getTimestamp() < summary.getStartTime()) {
                summary.setStartTime(entry.getTimestamp());
            }
            if (entry.getTimestamp() > summary.getEndTime()) {
                summary.setEndTime(entry.getTimestamp());
            }
            
            switch (entry.getLevel()) {
                case "ERROR":
                    summary.setErrorCount(summary.getErrorCount() + 1);
                    break;
                case "WARN":
                    summary.setWarnCount(summary.getWarnCount() + 1);
                    break;
                case "INFO":
                    summary.setInfoCount(summary.getInfoCount() + 1);
                    break;
                case "DEBUG":
                    summary.setDebugCount(summary.getDebugCount() + 1);
                    break;
            }
            
            if ("DEPENDENCY_INSTALLED".equals(entry.getAction())) {
                if (entry.getDetails() != null && entry.getDetails().get("capabilityId") != null) {
                    summary.getInstalledCapabilities().add(entry.getDetails().get("capabilityId").toString());
                }
            } else if ("DEPENDENCY_FAILED".equals(entry.getAction())) {
                if (entry.getDetails() != null && entry.getDetails().get("capabilityId") != null) {
                    summary.getFailedCapabilities().add(entry.getDetails().get("capabilityId").toString());
                }
            }
        }
        
        return summary;
    }
}
