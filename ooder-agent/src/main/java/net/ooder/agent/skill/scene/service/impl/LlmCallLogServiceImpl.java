package net.ooder.mvp.skill.scene.service.impl;

import net.ooder.mvp.skill.scene.dto.llm.LlmCallLogDTO;
import net.ooder.mvp.skill.scene.dto.llm.LlmStatsSummaryDTO;
import net.ooder.mvp.skill.scene.dto.llm.ProviderStatsDTO;
import net.ooder.mvp.skill.scene.service.LlmCallLogService;
import net.ooder.skill.common.storage.JsonStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class LlmCallLogServiceImpl implements LlmCallLogService {

    private static final Logger log = LoggerFactory.getLogger(LlmCallLogServiceImpl.class);
    
    private static final int MAX_LOG_SIZE = 10000;
    private static final String STORAGE_KEY = "llm-call-logs";
    
    private final Deque<LlmCallLogDTO> callLogs = new ConcurrentLinkedDeque<LlmCallLogDTO>();
    private final AtomicLong logIdCounter = new AtomicLong(0);
    
    @Autowired(required = false)
    private JsonStorageService jsonStorageService;
    
    @PostConstruct
    public void init() {
        loadFromStorage();
    }
    
    @PreDestroy
    public void shutdown() {
        persistToStorage();
    }
    
    private void loadFromStorage() {
        if (jsonStorageService == null) {
            log.info("[loadFromStorage] JsonStorageService not available, using memory-only mode");
            return;
        }
        
        try {
            List<LlmCallLogDTO> stored = jsonStorageService.loadList(STORAGE_KEY, LlmCallLogDTO.class);
            if (stored != null && !stored.isEmpty()) {
                callLogs.clear();
                callLogs.addAll(stored);
                
                long maxId = 0;
                for (LlmCallLogDTO logEntry : callLogs) {
                    if (logEntry.getLogId() != null && logEntry.getLogId().startsWith("log-")) {
                        try {
                            long id = Long.parseLong(logEntry.getLogId().substring(4));
                            if (id > maxId) maxId = id;
                        } catch (NumberFormatException e) {
                        }
                    }
                }
                logIdCounter.set(maxId);
                log.info("[loadFromStorage] Loaded {} LLM call logs from storage, maxId={}", callLogs.size(), maxId);
            }
        } catch (Exception e) {
            log.error("[loadFromStorage] Failed to load LLM call logs: {}", e.getMessage());
        }
    }
    
    private void persistToStorage() {
        if (jsonStorageService == null) {
            return;
        }
        
        try {
            List<LlmCallLogDTO> toSave = new ArrayList<LlmCallLogDTO>(callLogs);
            jsonStorageService.saveList(STORAGE_KEY, toSave);
            log.info("[persistToStorage] Saved {} LLM call logs to storage", toSave.size());
        } catch (Exception e) {
            log.error("[persistToStorage] Failed to persist LLM call logs: {}", e.getMessage());
        }
    }
    
    @Override
    public void recordCall(LlmCallLogDTO logEntry) {
        if (logEntry == null) {
            return;
        }
        
        if (logEntry.getLogId() == null || logEntry.getLogId().isEmpty()) {
            logEntry.setLogId("log-" + logIdCounter.incrementAndGet());
        }
        
        if (logEntry.getCreateTime() == 0) {
            logEntry.setCreateTime(System.currentTimeMillis());
        }
        
        callLogs.addFirst(logEntry);
        
        while (callLogs.size() > MAX_LOG_SIZE) {
            callLogs.removeLast();
        }
        
        log.debug("Saved LLM call log: provider={}, model={}, status={}", 
            logEntry.getProviderId(), logEntry.getModel(), logEntry.getStatus());
        
        if (callLogs.size() % 10 == 0) {
            persistToStorage();
        }
    }
    
    @Override
    public LlmCallLogDTO getLogById(String logId) {
        if (logId == null || logId.isEmpty()) {
            return null;
        }
        
        for (LlmCallLogDTO logEntry : callLogs) {
            if (logId.equals(logEntry.getLogId())) {
                return logEntry;
            }
        }
        
        return null;
    }
    
    @Override
    public List<LlmCallLogDTO> getLogs(String providerId, String status, String model, int pageNum, int pageSize) {
        List<LlmCallLogDTO> filtered = new ArrayList<LlmCallLogDTO>();
        
        for (LlmCallLogDTO logEntry : callLogs) {
            if (providerId != null && !providerId.isEmpty() && !providerId.equals(logEntry.getProviderId())) {
                continue;
            }
            if (status != null && !status.isEmpty() && !status.equals(logEntry.getStatus())) {
                continue;
            }
            if (model != null && !model.isEmpty() && !logEntry.getModel().toLowerCase().contains(model.toLowerCase())) {
                continue;
            }
            filtered.add(logEntry);
        }
        
        Collections.sort(filtered, new Comparator<LlmCallLogDTO>() {
            @Override
            public int compare(LlmCallLogDTO a, LlmCallLogDTO b) {
                return Long.compare(b.getCreateTime(), a.getCreateTime());
            }
        });
        
        int start = (pageNum - 1) * pageSize;
        int end = Math.min(start + pageSize, filtered.size());
        
        if (start >= filtered.size()) {
            return new ArrayList<LlmCallLogDTO>();
        }
        
        return new ArrayList<LlmCallLogDTO>(filtered.subList(start, end));
    }
    
    @Override
    public int getTotalCount(String providerId, String status, String model) {
        int count = 0;
        
        for (LlmCallLogDTO logEntry : callLogs) {
            if (providerId != null && !providerId.isEmpty() && !providerId.equals(logEntry.getProviderId())) {
                continue;
            }
            if (status != null && !status.isEmpty() && !status.equals(logEntry.getStatus())) {
                continue;
            }
            if (model != null && !model.isEmpty() && !logEntry.getModel().toLowerCase().contains(model.toLowerCase())) {
                continue;
            }
            count++;
        }
        
        return count;
    }
    
    @Override
    public LlmStatsSummaryDTO getStats(String providerId) {
        LlmStatsSummaryDTO stats = new LlmStatsSummaryDTO();
        
        long totalCalls = 0;
        long totalTokens = 0;
        double totalCost = 0;
        long totalLatency = 0;
        long successCount = 0;
        
        for (LlmCallLogDTO logEntry : callLogs) {
            if (providerId != null && !providerId.isEmpty() && !providerId.equals(logEntry.getProviderId())) {
                continue;
            }
            
            totalCalls++;
            totalTokens += logEntry.getTotalTokens();
            totalCost += logEntry.getCost();
            totalLatency += logEntry.getLatency();
            
            if ("success".equals(logEntry.getStatus())) {
                successCount++;
            }
        }
        
        stats.setTotalCalls(totalCalls);
        stats.setTotalTokens(totalTokens);
        stats.setTotalCost(Math.round(totalCost * 100.0) / 100.0);
        stats.setAvgLatency(totalCalls > 0 ? totalLatency / totalCalls : 0);
        stats.setSuccessRate(totalCalls > 0 ? Math.round(successCount * 1000.0 / totalCalls) / 10.0 : 0);
        stats.setErrorCount(totalCalls - successCount);
        
        return stats;
    }
    
    @Override
    public List<ProviderStatsDTO> getProviderStats() {
        Map<String, ProviderStatsDTO> providerStatsMap = new HashMap<String, ProviderStatsDTO>();
        
        for (LlmCallLogDTO logEntry : callLogs) {
            String providerId = logEntry.getProviderId();
            ProviderStatsDTO stats = providerStatsMap.get(providerId);
            
            if (stats == null) {
                stats = new ProviderStatsDTO();
                stats.setProviderId(providerId);
                stats.setProviderName(logEntry.getProviderName());
                stats.setTotalCalls(0L);
                stats.setTotalTokens(0L);
                stats.setTotalCost(0.0);
                providerStatsMap.put(providerId, stats);
            }
            
            stats.setTotalCalls(stats.getTotalCalls() + 1);
            stats.setTotalTokens(stats.getTotalTokens() + logEntry.getTotalTokens());
            stats.setTotalCost(stats.getTotalCost() + logEntry.getCost());
        }
        
        return new ArrayList<ProviderStatsDTO>(providerStatsMap.values());
    }
    
    @Override
    public void clearLogs() {
        callLogs.clear();
        logIdCounter.set(0);
        persistToStorage();
        log.info("All LLM call logs cleared");
    }
    
    @Override
    public List<LlmCallLogDTO> getAllLogs() {
        return new ArrayList<LlmCallLogDTO>(callLogs);
    }
}
