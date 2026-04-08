package net.ooder.mvp.skill.scene.service;

import net.ooder.mvp.skill.scene.dto.PageResult;
import net.ooder.mvp.skill.scene.dto.history.HistoryDTO;
import net.ooder.mvp.skill.scene.dto.history.HistoryStatisticsDTO;

public interface HistoryService {
    
    PageResult<HistoryDTO> listMyHistory(String userId, Integer days, String category, 
            String status, String keyword, int pageNum, int pageSize);
    
    HistoryDTO getExecutionDetail(String executionId, String userId);
    
    HistoryStatisticsDTO getStatistics(String userId, Integer days);
    
    boolean rerunScene(String userId, String sceneGroupId);
    
    byte[] exportHistory(String userId, Integer days, String category, String status);
}
