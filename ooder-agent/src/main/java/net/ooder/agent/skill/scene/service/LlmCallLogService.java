package net.ooder.mvp.skill.scene.service;

import net.ooder.mvp.skill.scene.dto.llm.LlmCallLogDTO;
import net.ooder.mvp.skill.scene.dto.llm.LlmStatsSummaryDTO;
import net.ooder.mvp.skill.scene.dto.llm.ProviderStatsDTO;

import java.util.List;

public interface LlmCallLogService {
    
    void recordCall(LlmCallLogDTO log);
    
    LlmCallLogDTO getLogById(String logId);
    
    List<LlmCallLogDTO> getLogs(String providerId, String status, String model, int pageNum, int pageSize);
    
    int getTotalCount(String providerId, String status, String model);
    
    LlmStatsSummaryDTO getStats(String providerId);
    
    List<ProviderStatsDTO> getProviderStats();
    
    void clearLogs();
    
    List<LlmCallLogDTO> getAllLogs();
}
