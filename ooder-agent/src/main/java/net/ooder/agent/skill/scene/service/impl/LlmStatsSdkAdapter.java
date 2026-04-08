package net.ooder.mvp.skill.scene.service.impl;

import net.ooder.mvp.skill.scene.dto.llm.*;
import net.ooder.mvp.skill.scene.service.LlmStatsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Primary
public class LlmStatsSdkAdapter implements LlmStatsService {
    
    private static final Logger log = LoggerFactory.getLogger(LlmStatsSdkAdapter.class);
    
    @Autowired(required = false)
    private net.ooder.scene.llm.audit.LlmAuditService sdkAuditService;
    
    @Autowired(required = false)
    private net.ooder.scene.llm.stats.LlmStatsAggregationService sdkStatsService;
    
    @Autowired
    private LlmStatsServiceImpl localStatsService;
    
    private boolean isSdkAvailable() {
        return sdkAuditService != null && sdkStatsService != null;
    }
    
    @Override
    public void recordCall(LlmCallLogDTO logEntry) {
        localStatsService.recordCall(logEntry);
        
        if (isSdkAvailable()) {
            try {
                net.ooder.scene.llm.audit.LlmCallContext context = convertToSdkContext(logEntry);
                net.ooder.scene.llm.audit.LlmCallResult result = convertToSdkResult(logEntry);
                sdkAuditService.logLlmCall(context, result);
                log.debug("Recorded LLM call to SDK: {}", logEntry.getLogId());
            } catch (Exception e) {
                log.warn("Failed to record LLM call to SDK: {}", e.getMessage());
            }
        }
    }
    
    @Override
    public LlmCallLogDTO getLogById(String logId) {
        return localStatsService.getLogById(logId);
    }
    
    @Override
    public List<LlmCallLogDTO> getLogs(LlmLogQuery query) {
        return localStatsService.getLogs(query);
    }
    
    @Override
    public int getTotalCount(LlmLogQuery query) {
        return localStatsService.getTotalCount(query);
    }
    
    @Override
    public CompanyLlmStatsDTO getCompanyStats(String companyId, StatsTimeRange range) {
        if (isSdkAvailable()) {
            try {
                net.ooder.scene.llm.stats.StatsTimeRange sdkRange = convertToSdkRange(range);
                CompletableFuture<net.ooder.scene.llm.stats.LlmCompanyStats> future = 
                    sdkStatsService.getCompanyStats(companyId, sdkRange);
                net.ooder.scene.llm.stats.LlmCompanyStats sdkStats = future.get();
                if (sdkStats != null) {
                    return convertFromSdkCompanyStats(sdkStats);
                }
            } catch (Exception e) {
                log.warn("Failed to get company stats from SDK, falling back to local: {}", e.getMessage());
            }
        }
        return localStatsService.getCompanyStats(companyId, range);
    }
    
    @Override
    public DepartmentLlmStatsDTO getDepartmentStats(String departmentId, StatsTimeRange range) {
        if (isSdkAvailable()) {
            try {
                net.ooder.scene.llm.stats.StatsTimeRange sdkRange = convertToSdkRange(range);
                CompletableFuture<net.ooder.scene.llm.stats.LlmDepartmentStats> future = 
                    sdkStatsService.getDepartmentStats(departmentId, sdkRange);
                net.ooder.scene.llm.stats.LlmDepartmentStats sdkStats = future.get();
                if (sdkStats != null) {
                    return convertFromSdkDepartmentStats(sdkStats);
                }
            } catch (Exception e) {
                log.warn("Failed to get department stats from SDK: {}", e.getMessage());
            }
        }
        return localStatsService.getDepartmentStats(departmentId, range);
    }
    
    @Override
    public UserLlmStatsDTO getUserStats(String userId, StatsTimeRange range) {
        if (isSdkAvailable()) {
            try {
                net.ooder.scene.llm.stats.StatsTimeRange sdkRange = convertToSdkRange(range);
                CompletableFuture<net.ooder.scene.llm.stats.LlmUserStats> future = 
                    sdkStatsService.getUserStats(userId, sdkRange);
                net.ooder.scene.llm.stats.LlmUserStats sdkStats = future.get();
                if (sdkStats != null) {
                    return convertFromSdkUserStats(sdkStats);
                }
            } catch (Exception e) {
                log.warn("Failed to get user stats from SDK: {}", e.getMessage());
            }
        }
        return localStatsService.getUserStats(userId, range);
    }
    
    @Override
    public ModuleLlmStatsDTO getModuleStats(String moduleId, String userId, StatsTimeRange range) {
        if (isSdkAvailable()) {
            try {
                net.ooder.scene.llm.stats.StatsTimeRange sdkRange = convertToSdkRange(range);
                CompletableFuture<net.ooder.scene.llm.stats.LlmModuleStats> future = 
                    sdkStatsService.getModuleStats(moduleId, userId, sdkRange);
                net.ooder.scene.llm.stats.LlmModuleStats sdkStats = future.get();
                if (sdkStats != null) {
                    return convertFromSdkModuleStats(sdkStats);
                }
            } catch (Exception e) {
                log.warn("Failed to get module stats from SDK: {}", e.getMessage());
            }
        }
        return localStatsService.getModuleStats(moduleId, userId, range);
    }
    
    @Override
    public List<DepartmentLlmStatsDTO> getDepartmentRanking(String companyId, int topN, String orderBy) {
        if (isSdkAvailable()) {
            try {
                net.ooder.scene.llm.stats.StatsTimeRange sdkRange = new net.ooder.scene.llm.stats.StatsTimeRange();
                CompletableFuture<List<net.ooder.scene.llm.stats.LlmDepartmentStats>> future = 
                    sdkStatsService.getDepartmentRanking(companyId, sdkRange, topN);
                List<net.ooder.scene.llm.stats.LlmDepartmentStats> sdkStats = future.get();
                if (sdkStats != null) {
                    return sdkStats.stream()
                        .map(this::convertFromSdkDepartmentStats)
                        .collect(Collectors.toList());
                }
            } catch (Exception e) {
                log.warn("Failed to get department ranking from SDK: {}", e.getMessage());
            }
        }
        return localStatsService.getDepartmentRanking(companyId, topN, orderBy);
    }
    
    @Override
    public List<UserLlmStatsDTO> getUserRanking(String departmentId, int topN, String orderBy) {
        if (isSdkAvailable()) {
            try {
                net.ooder.scene.llm.stats.StatsTimeRange sdkRange = new net.ooder.scene.llm.stats.StatsTimeRange();
                CompletableFuture<List<net.ooder.scene.llm.stats.LlmUserStats>> future = 
                    sdkStatsService.getUserRanking(departmentId, sdkRange, topN);
                List<net.ooder.scene.llm.stats.LlmUserStats> sdkStats = future.get();
                if (sdkStats != null) {
                    return sdkStats.stream()
                        .map(this::convertFromSdkUserStats)
                        .collect(Collectors.toList());
                }
            } catch (Exception e) {
                log.warn("Failed to get user ranking from SDK: {}", e.getMessage());
            }
        }
        return localStatsService.getUserRanking(departmentId, topN, orderBy);
    }
    
    @Override
    public List<ModuleLlmStatsDTO> getModuleRanking(String userId, int topN, String orderBy) {
        if (isSdkAvailable()) {
            try {
                net.ooder.scene.llm.stats.StatsTimeRange sdkRange = new net.ooder.scene.llm.stats.StatsTimeRange();
                CompletableFuture<List<net.ooder.scene.llm.stats.LlmModuleStats>> future = 
                    sdkStatsService.getModuleRanking(userId, sdkRange, topN);
                List<net.ooder.scene.llm.stats.LlmModuleStats> sdkStats = future.get();
                if (sdkStats != null) {
                    return sdkStats.stream()
                        .map(this::convertFromSdkModuleStats)
                        .collect(Collectors.toList());
                }
            } catch (Exception e) {
                log.warn("Failed to get module ranking from SDK: {}", e.getMessage());
            }
        }
        return localStatsService.getModuleRanking(userId, topN, orderBy);
    }
    
    @Override
    public OverallStatsDTO getOverallStats(String companyId, StatsTimeRange range) {
        return localStatsService.getOverallStats(companyId, range);
    }
    
    @Override
    public void clearLogs(String companyId) {
        localStatsService.clearLogs(companyId);
    }
    
    private net.ooder.scene.llm.audit.LlmCallContext convertToSdkContext(LlmCallLogDTO dto) {
        net.ooder.scene.llm.audit.LlmCallContext context = new net.ooder.scene.llm.audit.LlmCallContext();
        context.setCompanyId(dto.getCompanyId());
        context.setCompanyName(dto.getCompanyName());
        context.setDepartmentId(dto.getDepartmentId());
        context.setDepartmentName(dto.getDepartmentName());
        context.setUserId(dto.getUserId());
        context.setUserName(dto.getUserName());
        context.setSceneId(dto.getSceneId());
        context.setSceneName(dto.getSceneName());
        context.setCapabilityId(dto.getCapabilityId());
        context.setCapabilityName(dto.getCapabilityName());
        context.setModuleId(dto.getModuleId());
        context.setModuleName(dto.getModuleName());
        context.setBusinessType(dto.getBusinessType());
        context.setClientIp(dto.getClientIp());
        context.setSessionId(dto.getSessionId());
        context.setRequestId(dto.getRequestId());
        return context;
    }
    
    private net.ooder.scene.llm.audit.LlmCallResult convertToSdkResult(LlmCallLogDTO dto) {
        net.ooder.scene.llm.audit.LlmCallResult result = new net.ooder.scene.llm.audit.LlmCallResult();
        result.setProviderId(dto.getProviderId());
        result.setProviderName(dto.getProviderName());
        result.setModel(dto.getModel());
        result.setRequestType(dto.getRequestType());
        result.setInputTokens(dto.getInputTokens());
        result.setOutputTokens(dto.getOutputTokens());
        result.setTotalTokens(dto.getTotalTokens());
        result.setCost(dto.getCost());
        result.setLatency(dto.getLatency());
        result.setStatus(dto.getStatus());
        result.setErrorMessage(dto.getErrorMessage());
        return result;
    }
    
    private net.ooder.scene.llm.stats.StatsTimeRange convertToSdkRange(StatsTimeRange range) {
        if (range == null) {
            return new net.ooder.scene.llm.stats.StatsTimeRange();
        }
        net.ooder.scene.llm.stats.StatsTimeRange sdkRange = new net.ooder.scene.llm.stats.StatsTimeRange();
        sdkRange.setStartTime(range.getStartTime());
        sdkRange.setEndTime(range.getEndTime());
        return sdkRange;
    }
    
    private CompanyLlmStatsDTO convertFromSdkCompanyStats(net.ooder.scene.llm.stats.LlmCompanyStats sdk) {
        CompanyLlmStatsDTO dto = new CompanyLlmStatsDTO();
        dto.setCompanyId(sdk.getCompanyId());
        dto.setCompanyName(sdk.getCompanyName());
        dto.setTotalCalls(sdk.getTotalCalls());
        dto.setSuccessCalls(sdk.getSuccessCalls());
        dto.setFailedCalls(sdk.getFailedCalls());
        dto.setSuccessRate(sdk.getSuccessRate());
        dto.setTotalInputTokens(sdk.getTotalInputTokens());
        dto.setTotalOutputTokens(sdk.getTotalOutputTokens());
        dto.setTotalTokens(sdk.getTotalTokens());
        dto.setTotalCost(sdk.getTotalCost());
        dto.setMonthToDateCost(sdk.getMonthToDateCost());
        dto.setBudgetLimit(sdk.getBudgetLimit());
        dto.setBudgetUsedPercent(sdk.getBudgetUsedPercent());
        dto.setAvgLatency(sdk.getAvgLatency());
        dto.setMaxLatency(sdk.getMaxLatency());
        dto.setMinLatency(sdk.getMinLatency());
        dto.setTodayCalls(sdk.getTodayCalls());
        dto.setWeekCalls(sdk.getWeekCalls());
        dto.setMonthCalls(sdk.getMonthCalls());
        dto.setStatsTime(sdk.getStatsTime());
        dto.setStartTime(sdk.getStartTime());
        dto.setEndTime(sdk.getEndTime());
        return dto;
    }
    
    private DepartmentLlmStatsDTO convertFromSdkDepartmentStats(net.ooder.scene.llm.stats.LlmDepartmentStats sdk) {
        DepartmentLlmStatsDTO dto = new DepartmentLlmStatsDTO();
        dto.setDepartmentId(sdk.getDepartmentId());
        dto.setDepartmentName(sdk.getDepartmentName());
        dto.setCompanyId(sdk.getCompanyId());
        dto.setTotalCalls(sdk.getTotalCalls());
        dto.setSuccessCalls(sdk.getSuccessCalls());
        dto.setFailedCalls(sdk.getFailedCalls());
        dto.setSuccessRate(sdk.getSuccessRate());
        dto.setTotalTokens(sdk.getTotalTokens());
        dto.setTotalInputTokens(sdk.getTotalInputTokens());
        dto.setTotalOutputTokens(sdk.getTotalOutputTokens());
        dto.setTotalCost(sdk.getTotalCost());
        dto.setBudgetLimit(sdk.getBudgetLimit());
        dto.setStatsTime(sdk.getStatsTime());
        return dto;
    }
    
    private UserLlmStatsDTO convertFromSdkUserStats(net.ooder.scene.llm.stats.LlmUserStats sdk) {
        UserLlmStatsDTO dto = new UserLlmStatsDTO();
        dto.setUserId(sdk.getUserId());
        dto.setUserName(sdk.getUserName());
        dto.setDepartmentId(sdk.getDepartmentId());
        dto.setDepartmentName(sdk.getDepartmentName());
        dto.setTotalCalls(sdk.getTotalCalls());
        dto.setSuccessCalls(sdk.getSuccessCalls());
        dto.setFailedCalls(sdk.getFailedCalls());
        dto.setSuccessRate(sdk.getSuccessRate());
        dto.setTotalTokens(sdk.getTotalTokens());
        dto.setTotalInputTokens(sdk.getTotalInputTokens());
        dto.setTotalOutputTokens(sdk.getTotalOutputTokens());
        dto.setTotalCost(sdk.getTotalCost());
        dto.setQuotaLimit(sdk.getQuotaLimit());
        dto.setQuotaUsed(sdk.getQuotaUsed());
        dto.setStatsTime(sdk.getStatsTime());
        return dto;
    }
    
    private ModuleLlmStatsDTO convertFromSdkModuleStats(net.ooder.scene.llm.stats.LlmModuleStats sdk) {
        ModuleLlmStatsDTO dto = new ModuleLlmStatsDTO();
        dto.setModuleId(sdk.getModuleId());
        dto.setModuleName(sdk.getModuleName());
        dto.setModuleType(sdk.getModuleType());
        dto.setUserId(sdk.getUserId());
        dto.setTotalCalls(sdk.getTotalCalls());
        dto.setSuccessCalls(sdk.getSuccessCalls());
        dto.setFailedCalls(sdk.getFailedCalls());
        dto.setSuccessRate(sdk.getSuccessRate());
        dto.setTotalTokens(sdk.getTotalTokens());
        dto.setTotalInputTokens(sdk.getTotalInputTokens());
        dto.setTotalOutputTokens(sdk.getTotalOutputTokens());
        dto.setTotalCost(sdk.getTotalCost());
        dto.setAvgLatency(sdk.getAvgLatency());
        dto.setProviderDistribution(sdk.getProviderDistribution());
        dto.setModelDistribution(sdk.getModelDistribution());
        dto.setStatsTime(sdk.getStatsTime());
        return dto;
    }
}
