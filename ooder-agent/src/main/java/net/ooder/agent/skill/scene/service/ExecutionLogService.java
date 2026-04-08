package net.ooder.mvp.skill.scene.service;

import net.ooder.mvp.skill.scene.dto.PageResult;
import net.ooder.mvp.skill.scene.dto.scene.ExecutionLogDTO;

import java.util.List;

public interface ExecutionLogService {
    
    void log(String sceneGroupId, String action, String status, String message);
    
    List<ExecutionLogDTO> listLogs(String sceneGroupId, int limit);
    
    PageResult<ExecutionLogDTO> listLogs(String sceneGroupId, long startTime, long endTime, String level, int pageNum, int pageSize);
}
