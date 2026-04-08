package net.ooder.mvp.skill.scene.service;

import net.ooder.mvp.skill.scene.dto.llm.*;

import java.util.List;

public interface LlmStatsService {
    
    void recordCall(LlmCallLogDTO log);
    
    LlmCallLogDTO getLogById(String logId);
    
    List<LlmCallLogDTO> getLogs(LlmLogQuery query);
    
    int getTotalCount(LlmLogQuery query);
    
    CompanyLlmStatsDTO getCompanyStats(String companyId, StatsTimeRange range);
    
    DepartmentLlmStatsDTO getDepartmentStats(String departmentId, StatsTimeRange range);
    
    UserLlmStatsDTO getUserStats(String userId, StatsTimeRange range);
    
    ModuleLlmStatsDTO getModuleStats(String moduleId, String userId, StatsTimeRange range);
    
    List<DepartmentLlmStatsDTO> getDepartmentRanking(String companyId, int topN, String orderBy);
    
    List<UserLlmStatsDTO> getUserRanking(String departmentId, int topN, String orderBy);
    
    List<ModuleLlmStatsDTO> getModuleRanking(String userId, int topN, String orderBy);
    
    OverallStatsDTO getOverallStats(String companyId, StatsTimeRange range);
    
    void clearLogs(String companyId);
    
    public static class LlmLogQuery {
        private String companyId;
        private String departmentId;
        private String userId;
        private String moduleId;
        private String providerId;
        private String status;
        private String model;
        private Long startTime;
        private Long endTime;
        private Integer pageNum;
        private Integer pageSize;
        
        public String getCompanyId() { return companyId; }
        public void setCompanyId(String companyId) { this.companyId = companyId; }
        public String getDepartmentId() { return departmentId; }
        public void setDepartmentId(String departmentId) { this.departmentId = departmentId; }
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        public String getModuleId() { return moduleId; }
        public void setModuleId(String moduleId) { this.moduleId = moduleId; }
        public String getProviderId() { return providerId; }
        public void setProviderId(String providerId) { this.providerId = providerId; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getModel() { return model; }
        public void setModel(String model) { this.model = model; }
        public Long getStartTime() { return startTime; }
        public void setStartTime(Long startTime) { this.startTime = startTime; }
        public Long getEndTime() { return endTime; }
        public void setEndTime(Long endTime) { this.endTime = endTime; }
        public Integer getPageNum() { return pageNum; }
        public void setPageNum(Integer pageNum) { this.pageNum = pageNum; }
        public Integer getPageSize() { return pageSize; }
        public void setPageSize(Integer pageSize) { this.pageSize = pageSize; }
    }
}
