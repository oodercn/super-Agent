package net.ooder.skillcenter.lifecycle.execution.impl;

import net.ooder.skillcenter.lifecycle.execution.SkillExecutionManager;
import net.ooder.skillcenter.model.SkillContext;
import java.util.*;

public class SkillExecutionManagerImpl implements SkillExecutionManager {
    
    private static SkillExecutionManagerImpl instance;
    private Map<String, ExecutionRecord> executionRecords;
    private Map<String, ExecutionStatus> executionStatuses;
    
    public SkillExecutionManagerImpl() {
        this.executionRecords = new HashMap<>();
        this.executionStatuses = new HashMap<>();
        loadSampleData();
    }
    
    public static SkillExecutionManager getInstance() {
        if (instance == null) {
            instance = new SkillExecutionManagerImpl();
        }
        return instance;
    }
    
    @Override
    public ExecutionResult executeSkill(String skillId, SkillContext context) {
        ExecutionResult result = new ExecutionResult();
        String executionId = "exec-" + System.currentTimeMillis();
        
        result.setExecutionId(executionId);
        result.setSkillId(skillId);
        result.setStartTime(System.currentTimeMillis());
        
        try {
            Thread.sleep(100);
            
            Map<String, Object> output = new HashMap<>();
            output.put("status", "success");
            output.put("message", "Skill executed successfully");
            output.put("timestamp", System.currentTimeMillis());
            
            result.setSuccess(true);
            result.setResult(output);
            result.setEndTime(System.currentTimeMillis());
            result.setDuration(result.getEndTime() - result.getStartTime());
            
            ExecutionRecord record = new ExecutionRecord();
            record.setExecutionId(executionId);
            record.setSkillId(skillId);
            record.setStatus("completed");
            record.setStartTime(result.getStartTime());
            record.setEndTime(result.getEndTime());
            record.setDuration(result.getDuration());
            record.setSuccess(true);
            record.setInput(context != null ? context.getParams() : new HashMap<>());
            record.setOutput(output);
            
            executionRecords.put(executionId, record);
            
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrorMessage(e.getMessage());
            result.setEndTime(System.currentTimeMillis());
            result.setDuration(result.getEndTime() - result.getStartTime());
            
            ExecutionRecord record = new ExecutionRecord();
            record.setExecutionId(executionId);
            record.setSkillId(skillId);
            record.setStatus("failed");
            record.setStartTime(result.getStartTime());
            record.setEndTime(result.getEndTime());
            record.setDuration(result.getDuration());
            record.setSuccess(false);
            record.setErrorMessage(e.getMessage());
            record.setInput(context != null ? context.getParams() : new HashMap<>());
            
            executionRecords.put(executionId, record);
        }
        
        return result;
    }
    
    @Override
    public void executeSkillAsync(String skillId, SkillContext context, ExecutionCallback callback) {
        new Thread(() -> {
            try {
                ExecutionResult result = executeSkill(skillId, context);
                if (callback != null) {
                    callback.onSuccess(result);
                }
            } catch (Exception e) {
                ExecutionResult result = new ExecutionResult();
                result.setExecutionId("exec-" + System.currentTimeMillis());
                result.setSkillId(skillId);
                result.setSuccess(false);
                result.setErrorMessage(e.getMessage());
                
                if (callback != null) {
                    callback.onFailure(result);
                }
            }
        }).start();
    }
    
    @Override
    public ExecutionStatus getExecutionStatus(String executionId) {
        return executionStatuses.get(executionId);
    }
    
    @Override
    public List<ExecutionRecord> getExecutionHistory(String skillId) {
        List<ExecutionRecord> result = new ArrayList<>();
        for (ExecutionRecord record : executionRecords.values()) {
            if (record.getSkillId().equals(skillId)) {
                result.add(record);
            }
        }
        return result;
    }
    
    @Override
    public List<ExecutionRecord> getExecutionHistory(String skillId, long startTime, long endTime) {
        List<ExecutionRecord> result = new ArrayList<>();
        for (ExecutionRecord record : executionRecords.values()) {
            if (record.getSkillId().equals(skillId) 
                && record.getStartTime() >= startTime 
                && record.getEndTime() <= endTime) {
                result.add(record);
            }
        }
        return result;
    }
    
    @Override
    public void cancelExecution(String executionId) {
        ExecutionStatus status = executionStatuses.get(executionId);
        if (status != null) {
            status.setStatus("cancelled");
        }
    }
    
    @Override
    public void pauseExecution(String executionId) {
        ExecutionStatus status = executionStatuses.get(executionId);
        if (status != null) {
            status.setStatus("paused");
        }
    }
    
    @Override
    public void resumeExecution(String executionId) {
        ExecutionStatus status = executionStatuses.get(executionId);
        if (status != null) {
            status.setStatus("running");
        }
    }
    
    private void loadSampleData() {
        ExecutionRecord record1 = new ExecutionRecord();
        record1.setExecutionId("exec-sample-001");
        record1.setSkillId("skill-sample-001");
        record1.setStatus("completed");
        record1.setStartTime(System.currentTimeMillis() - 3600000L);
        record1.setEndTime(System.currentTimeMillis() - 3500000L);
        record1.setDuration(100000L);
        record1.setSuccess(true);
        
        Map<String, Object> input1 = new HashMap<>();
        input1.put("location", "Beijing");
        record1.setInput(input1);
        
        Map<String, Object> output1 = new HashMap<>();
        output1.put("temperature", "25");
        output1.put("humidity", "60");
        record1.setOutput(output1);
        
        executionRecords.put(record1.getExecutionId(), record1);
        
        ExecutionStatus status1 = new ExecutionStatus();
        status1.setExecutionId(record1.getExecutionId());
        status1.setSkillId(record1.getSkillId());
        status1.setStatus("completed");
        status1.setProgress(1.0);
        status1.setStartTime(record1.getStartTime());
        status1.setEstimatedEndTime(record1.getEndTime());
        
        executionStatuses.put(status1.getExecutionId(), status1);
    }
}
