package net.ooder.mvp.skill.scene.service;

import net.ooder.mvp.skill.scene.dto.PageResult;
import net.ooder.mvp.skill.scene.dto.scene.SceneWorkflowInstanceDTO;
import net.ooder.mvp.skill.scene.dto.scene.ExecutionLogDTO;

import java.util.List;

public interface SceneWorkflowService {
    
    SceneWorkflowInstanceDTO start(String sceneGroupId, String triggerType);
    
    boolean cancel(String workflowId);
    
    SceneWorkflowInstanceDTO get(String workflowId);
    
    PageResult<SceneWorkflowInstanceDTO> listBySceneGroup(String sceneGroupId, int pageNum, int pageSize);
    
    boolean pause(String workflowId);
    
    boolean resume(String workflowId);
    
    boolean retryStep(String workflowId, String stepId);
    
    List<ExecutionLogDTO> getExecutionLogs(String workflowId);
}
