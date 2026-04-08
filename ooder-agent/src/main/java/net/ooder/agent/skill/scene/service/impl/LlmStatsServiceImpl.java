package net.ooder.mvp.skill.scene.service.impl;

import net.ooder.mvp.skill.scene.dto.llm.*;
import net.ooder.mvp.skill.scene.service.LlmCallLogService;
import net.ooder.mvp.skill.scene.service.LlmStatsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class LlmStatsServiceImpl implements LlmStatsService {
    
    private static final Logger log = LoggerFactory.getLogger(LlmStatsServiceImpl.class);
    
    @Autowired
    private LlmCallLogService llmCallLogService;
    
    @Override
    public void recordCall(LlmCallLogDTO logEntry) {
        llmCallLogService.recordCall(logEntry);
        log.debug("Recorded LLM call log: {}", logEntry.getLogId());
    }
    
    @Override
    public LlmCallLogDTO getLogById(String logId) {
        return llmCallLogService.getLogById(logId);
    }
    
    @Override
    public List<LlmCallLogDTO> getLogs(LlmLogQuery query) {
        return llmCallLogService.getAllLogs().stream()
            .filter(l -> query.getCompanyId() == null || query.getCompanyId().equals(l.getCompanyId()))
            .filter(l -> query.getDepartmentId() == null || query.getDepartmentId().equals(l.getDepartmentId()))
            .filter(l -> query.getUserId() == null || query.getUserId().equals(l.getUserId()))
            .filter(l -> query.getModuleId() == null || query.getModuleId().equals(l.getModuleId()))
            .filter(l -> query.getProviderId() == null || query.getProviderId().equals(l.getProviderId()))
            .filter(l -> query.getStatus() == null || query.getStatus().equals(l.getStatus()))
            .filter(l -> query.getModel() == null || query.getModel().equals(l.getModel()))
            .filter(l -> query.getStartTime() == null || l.getCreateTime() >= query.getStartTime())
            .filter(l -> query.getEndTime() == null || l.getCreateTime() <= query.getEndTime())
            .skip((query.getPageNum() != null && query.getPageNum() > 1) 
                ? (long)(query.getPageNum() - 1) * (query.getPageSize() != null ? query.getPageSize() : 20) : 0)
            .limit(query.getPageSize() != null ? query.getPageSize() : 20)
            .collect(Collectors.toList());
    }
    
    @Override
    public int getTotalCount(LlmLogQuery query) {
        return (int) llmCallLogService.getAllLogs().stream()
            .filter(l -> query.getCompanyId() == null || query.getCompanyId().equals(l.getCompanyId()))
            .filter(l -> query.getDepartmentId() == null || query.getDepartmentId().equals(l.getDepartmentId()))
            .filter(l -> query.getUserId() == null || query.getUserId().equals(l.getUserId()))
            .filter(l -> query.getModuleId() == null || query.getModuleId().equals(l.getModuleId()))
            .filter(l -> query.getProviderId() == null || query.getProviderId().equals(l.getProviderId()))
            .filter(l -> query.getStatus() == null || query.getStatus().equals(l.getStatus()))
            .filter(l -> query.getModel() == null || query.getModel().equals(l.getModel()))
            .filter(l -> query.getStartTime() == null || l.getCreateTime() >= query.getStartTime())
            .filter(l -> query.getEndTime() == null || l.getCreateTime() <= query.getEndTime())
            .count();
    }
    
    @Override
    public CompanyLlmStatsDTO getCompanyStats(String companyId, StatsTimeRange range) {
        List<LlmCallLogDTO> logs = filterByTimeAndCompany(range, companyId);
        
        CompanyLlmStatsDTO stats = new CompanyLlmStatsDTO();
        stats.setCompanyId(companyId);
        stats.setStatsTime(System.currentTimeMillis());
        stats.setStartTime(range != null ? range.getStartTime() : 0);
        stats.setEndTime(range != null ? range.getEndTime() : System.currentTimeMillis());
        
        if (logs.isEmpty()) {
            return stats;
        }
        
        stats.setTotalCalls(logs.size());
        stats.setSuccessCalls(logs.stream().filter(l -> "success".equals(l.getStatus())).count());
        stats.setFailedCalls(logs.stream().filter(l -> "error".equals(l.getStatus())).count());
        stats.setSuccessRate(stats.getTotalCalls() > 0 
            ? (double) stats.getSuccessCalls() / stats.getTotalCalls() * 100 : 0);
        
        stats.setTotalInputTokens(logs.stream().mapToLong(LlmCallLogDTO::getInputTokens).sum());
        stats.setTotalOutputTokens(logs.stream().mapToLong(LlmCallLogDTO::getOutputTokens).sum());
        stats.setTotalTokens(logs.stream().mapToLong(LlmCallLogDTO::getTotalTokens).sum());
        
        stats.setTotalCost(logs.stream().mapToDouble(LlmCallLogDTO::getCost).sum());
        
        DoubleSummaryStatistics latencyStats = logs.stream()
            .mapToDouble(l -> (double) l.getLatency())
            .summaryStatistics();
        stats.setAvgLatency(latencyStats.getAverage());
        stats.setMaxLatency((long) latencyStats.getMax());
        stats.setMinLatency((long) latencyStats.getMin());
        
        long now = System.currentTimeMillis();
        long todayStart = now - (now % 86400000);
        long weekStart = now - 7 * 86400000;
        long monthStart = now - 30 * 86400000;
        
        stats.setTodayCalls(logs.stream().filter(l -> l.getCreateTime() >= todayStart).count());
        stats.setWeekCalls(logs.stream().filter(l -> l.getCreateTime() >= weekStart).count());
        stats.setMonthCalls(logs.stream().filter(l -> l.getCreateTime() >= monthStart).count());
        
        List<DepartmentLlmStatsDTO> topDepts = getDepartmentRanking(companyId, 5, "calls");
        stats.setTopDepartments(topDepts);
        
        return stats;
    }
    
    @Override
    public DepartmentLlmStatsDTO getDepartmentStats(String departmentId, StatsTimeRange range) {
        List<LlmCallLogDTO> logs = llmCallLogService.getAllLogs().stream()
            .filter(l -> departmentId.equals(l.getDepartmentId()))
            .filter(l -> range == null || (l.getCreateTime() >= range.getStartTime() && l.getCreateTime() <= range.getEndTime()))
            .collect(Collectors.toList());
        
        DepartmentLlmStatsDTO stats = new DepartmentLlmStatsDTO();
        stats.setDepartmentId(departmentId);
        stats.setStatsTime(System.currentTimeMillis());
        
        if (!logs.isEmpty()) {
            stats.setCompanyId(logs.get(0).getCompanyId());
            stats.setDepartmentName(logs.get(0).getDepartmentName());
        }
        
        if (logs.isEmpty()) {
            return stats;
        }
        
        stats.setTotalCalls(logs.size());
        stats.setSuccessCalls(logs.stream().filter(l -> "success".equals(l.getStatus())).count());
        stats.setFailedCalls(logs.stream().filter(l -> "error".equals(l.getStatus())).count());
        stats.setSuccessRate(stats.getTotalCalls() > 0 
            ? (double) stats.getSuccessCalls() / stats.getTotalCalls() * 100 : 0);
        
        stats.setTotalTokens(logs.stream().mapToLong(LlmCallLogDTO::getTotalTokens).sum());
        stats.setTotalInputTokens(logs.stream().mapToLong(LlmCallLogDTO::getInputTokens).sum());
        stats.setTotalOutputTokens(logs.stream().mapToLong(LlmCallLogDTO::getOutputTokens).sum());
        stats.setTotalCost(logs.stream().mapToDouble(LlmCallLogDTO::getCost).sum());
        
        List<UserLlmStatsDTO> topUsers = getUserRanking(departmentId, 5, "calls");
        stats.setTopUsers(topUsers);
        
        return stats;
    }
    
    @Override
    public UserLlmStatsDTO getUserStats(String userId, StatsTimeRange range) {
        List<LlmCallLogDTO> logs = llmCallLogService.getAllLogs().stream()
            .filter(l -> userId.equals(l.getUserId()))
            .filter(l -> range == null || (l.getCreateTime() >= range.getStartTime() && l.getCreateTime() <= range.getEndTime()))
            .collect(Collectors.toList());
        
        UserLlmStatsDTO stats = new UserLlmStatsDTO();
        stats.setUserId(userId);
        stats.setStatsTime(System.currentTimeMillis());
        
        if (!logs.isEmpty()) {
            stats.setDepartmentId(logs.get(0).getDepartmentId());
            stats.setDepartmentName(logs.get(0).getDepartmentName());
            stats.setUserName(logs.get(0).getUserName());
        }
        
        if (logs.isEmpty()) {
            return stats;
        }
        
        stats.setTotalCalls(logs.size());
        stats.setSuccessCalls(logs.stream().filter(l -> "success".equals(l.getStatus())).count());
        stats.setFailedCalls(logs.stream().filter(l -> "error".equals(l.getStatus())).count());
        stats.setSuccessRate(stats.getTotalCalls() > 0 
            ? (double) stats.getSuccessCalls() / stats.getTotalCalls() * 100 : 0);
        
        stats.setTotalTokens(logs.stream().mapToLong(LlmCallLogDTO::getTotalTokens).sum());
        stats.setTotalInputTokens(logs.stream().mapToLong(LlmCallLogDTO::getInputTokens).sum());
        stats.setTotalOutputTokens(logs.stream().mapToLong(LlmCallLogDTO::getOutputTokens).sum());
        stats.setTotalCost(logs.stream().mapToDouble(LlmCallLogDTO::getCost).sum());
        
        List<ModuleLlmStatsDTO> moduleStats = getModuleRanking(userId, 10, "calls");
        stats.setModuleStats(moduleStats);
        
        return stats;
    }
    
    @Override
    public ModuleLlmStatsDTO getModuleStats(String moduleId, String userId, StatsTimeRange range) {
        List<LlmCallLogDTO> logs = llmCallLogService.getAllLogs().stream()
            .filter(l -> moduleId.equals(l.getModuleId()))
            .filter(l -> userId == null || userId.equals(l.getUserId()))
            .filter(l -> range == null || (l.getCreateTime() >= range.getStartTime() && l.getCreateTime() <= range.getEndTime()))
            .collect(Collectors.toList());
        
        ModuleLlmStatsDTO stats = new ModuleLlmStatsDTO();
        stats.setModuleId(moduleId);
        stats.setUserId(userId);
        stats.setStatsTime(System.currentTimeMillis());
        
        if (!logs.isEmpty()) {
            stats.setModuleName(logs.get(0).getModuleName());
            stats.setModuleType(logs.get(0).getModuleType());
        }
        
        if (logs.isEmpty()) {
            return stats;
        }
        
        stats.setTotalCalls(logs.size());
        stats.setSuccessCalls(logs.stream().filter(l -> "success".equals(l.getStatus())).count());
        stats.setFailedCalls(logs.stream().filter(l -> "error".equals(l.getStatus())).count());
        stats.setSuccessRate(stats.getTotalCalls() > 0 
            ? (double) stats.getSuccessCalls() / stats.getTotalCalls() * 100 : 0);
        
        stats.setTotalTokens(logs.stream().mapToLong(LlmCallLogDTO::getTotalTokens).sum());
        stats.setTotalInputTokens(logs.stream().mapToLong(LlmCallLogDTO::getInputTokens).sum());
        stats.setTotalOutputTokens(logs.stream().mapToLong(LlmCallLogDTO::getOutputTokens).sum());
        stats.setTotalCost(logs.stream().mapToDouble(LlmCallLogDTO::getCost).sum());
        stats.setAvgLatency(logs.stream().mapToLong(LlmCallLogDTO::getLatency).average().orElse(0));
        
        Map<String, Long> providerDist = logs.stream()
            .collect(Collectors.groupingBy(LlmCallLogDTO::getProviderId, Collectors.counting()));
        stats.setProviderDistribution(providerDist);
        
        Map<String, Long> modelDist = logs.stream()
            .collect(Collectors.groupingBy(LlmCallLogDTO::getModel, Collectors.counting()));
        stats.setModelDistribution(modelDist);
        
        return stats;
    }
    
    @Override
    public List<DepartmentLlmStatsDTO> getDepartmentRanking(String companyId, int topN, String orderBy) {
        Map<String, List<LlmCallLogDTO>> deptLogs = llmCallLogService.getAllLogs().stream()
            .filter(l -> companyId == null || companyId.equals(l.getCompanyId()))
            .filter(l -> l.getDepartmentId() != null)
            .collect(Collectors.groupingBy(LlmCallLogDTO::getDepartmentId));
        
        return deptLogs.entrySet().stream()
            .map(entry -> {
                String deptId = entry.getKey();
                List<LlmCallLogDTO> logs = entry.getValue();
                
                DepartmentLlmStatsDTO stats = new DepartmentLlmStatsDTO();
                stats.setDepartmentId(deptId);
                stats.setCompanyId(companyId);
                if (!logs.isEmpty()) {
                    stats.setDepartmentName(logs.get(0).getDepartmentName());
                }
                stats.setTotalCalls(logs.size());
                stats.setTotalTokens(logs.stream().mapToLong(LlmCallLogDTO::getTotalTokens).sum());
                stats.setTotalCost(logs.stream().mapToDouble(LlmCallLogDTO::getCost).sum());
                stats.setStatsTime(System.currentTimeMillis());
                
                return stats;
            })
            .sorted((a, b) -> {
                switch (orderBy) {
                    case "tokens": return Long.compare(b.getTotalTokens(), a.getTotalTokens());
                    case "cost": return Double.compare(b.getTotalCost(), a.getTotalCost());
                    default: return Long.compare(b.getTotalCalls(), a.getTotalCalls());
                }
            })
            .limit(topN)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<UserLlmStatsDTO> getUserRanking(String departmentId, int topN, String orderBy) {
        Map<String, List<LlmCallLogDTO>> userLogs = llmCallLogService.getAllLogs().stream()
            .filter(l -> departmentId == null || departmentId.equals(l.getDepartmentId()))
            .filter(l -> l.getUserId() != null)
            .collect(Collectors.groupingBy(LlmCallLogDTO::getUserId));
        
        return userLogs.entrySet().stream()
            .map(entry -> {
                String userId = entry.getKey();
                List<LlmCallLogDTO> logs = entry.getValue();
                
                UserLlmStatsDTO stats = new UserLlmStatsDTO();
                stats.setUserId(userId);
                if (!logs.isEmpty()) {
                    stats.setUserName(logs.get(0).getUserName());
                    stats.setDepartmentId(logs.get(0).getDepartmentId());
                    stats.setDepartmentName(logs.get(0).getDepartmentName());
                }
                stats.setTotalCalls(logs.size());
                stats.setTotalTokens(logs.stream().mapToLong(LlmCallLogDTO::getTotalTokens).sum());
                stats.setTotalCost(logs.stream().mapToDouble(LlmCallLogDTO::getCost).sum());
                stats.setStatsTime(System.currentTimeMillis());
                
                return stats;
            })
            .sorted((a, b) -> {
                switch (orderBy) {
                    case "tokens": return Long.compare(b.getTotalTokens(), a.getTotalTokens());
                    case "cost": return Double.compare(b.getTotalCost(), a.getTotalCost());
                    default: return Long.compare(b.getTotalCalls(), a.getTotalCalls());
                }
            })
            .limit(topN)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<ModuleLlmStatsDTO> getModuleRanking(String userId, int topN, String orderBy) {
        Map<String, List<LlmCallLogDTO>> moduleLogs = llmCallLogService.getAllLogs().stream()
            .filter(l -> userId == null || userId.equals(l.getUserId()))
            .filter(l -> l.getModuleId() != null)
            .collect(Collectors.groupingBy(LlmCallLogDTO::getModuleId));
        
        return moduleLogs.entrySet().stream()
            .map(entry -> {
                String moduleId = entry.getKey();
                List<LlmCallLogDTO> logs = entry.getValue();
                
                ModuleLlmStatsDTO stats = new ModuleLlmStatsDTO();
                stats.setModuleId(moduleId);
                if (!logs.isEmpty()) {
                    stats.setModuleName(logs.get(0).getModuleName());
                    stats.setModuleType(logs.get(0).getModuleType());
                }
                stats.setTotalCalls(logs.size());
                stats.setTotalTokens(logs.stream().mapToLong(LlmCallLogDTO::getTotalTokens).sum());
                stats.setTotalCost(logs.stream().mapToDouble(LlmCallLogDTO::getCost).sum());
                stats.setStatsTime(System.currentTimeMillis());
                
                return stats;
            })
            .sorted((a, b) -> {
                switch (orderBy) {
                    case "tokens": return Long.compare(b.getTotalTokens(), a.getTotalTokens());
                    case "cost": return Double.compare(b.getTotalCost(), a.getTotalCost());
                    default: return Long.compare(b.getTotalCalls(), a.getTotalCalls());
                }
            })
            .limit(topN)
            .collect(Collectors.toList());
    }
    
    @Override
    public OverallStatsDTO getOverallStats(String companyId, StatsTimeRange range) {
        List<LlmCallLogDTO> logs = filterByTimeAndCompany(range, companyId);
        
        OverallStatsDTO stats = new OverallStatsDTO();
        stats.setTotalCalls((long) logs.size());
        stats.setSuccessCalls(logs.stream().filter(l -> "success".equals(l.getStatus())).count());
        stats.setFailedCalls(logs.stream().filter(l -> "error".equals(l.getStatus())).count());
        stats.setTotalTokens(logs.stream().mapToLong(LlmCallLogDTO::getTotalTokens).sum());
        stats.setTotalCost(logs.stream().mapToDouble(LlmCallLogDTO::getCost).sum());
        stats.setAvgLatency((long) logs.stream().mapToLong(LlmCallLogDTO::getLatency).average().orElse(0));
        stats.setSuccessRate(logs.size() > 0 
            ? (double) logs.stream().filter(l -> "success".equals(l.getStatus())).count() / logs.size() * 100 : 0);
        
        Map<String, Long> providerStats = logs.stream()
            .collect(Collectors.groupingBy(LlmCallLogDTO::getProviderId, Collectors.counting()));
        stats.setProviderDistribution(providerStats);
        
        return stats;
    }
    
    @Override
    public void clearLogs(String companyId) {
        llmCallLogService.clearLogs();
    }
    
    private List<LlmCallLogDTO> filterByTimeAndCompany(StatsTimeRange range, String companyId) {
        return llmCallLogService.getAllLogs().stream()
            .filter(l -> companyId == null || companyId.equals(l.getCompanyId()))
            .filter(l -> range == null || (l.getCreateTime() >= range.getStartTime() && l.getCreateTime() <= range.getEndTime()))
            .collect(Collectors.toList());
    }
}
