
package net.ooder.sdk.core.metadata.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SceneGroupInstance implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    public enum InstanceState {
        INITIALIZING,
        RUNNING,
        COMPLETED,
        FAILED,
        TERMINATED
    }
    
    private String sceneGroupId;
    private String sceneId;
    private InstanceState state;
    private long createTime;
    private long startTime;
    private long endTime;
    
    private InstanceResult result;
    private InstanceContext context;
    private List<RecoveryPoint> recoveryPoints;
    
    public SceneGroupInstance() {
        this.state = InstanceState.INITIALIZING;
        this.recoveryPoints = new ArrayList<>();
        this.createTime = System.currentTimeMillis();
    }
    
    public SceneGroupInstance(String sceneGroupId, String sceneId) {
        this();
        this.sceneGroupId = sceneGroupId;
        this.sceneId = sceneId;
    }
    
    public String getSceneGroupId() { return sceneGroupId; }
    public void setSceneGroupId(String sceneGroupId) { this.sceneGroupId = sceneGroupId; }
    
    public String getSceneId() { return sceneId; }
    public void setSceneId(String sceneId) { this.sceneId = sceneId; }
    
    public InstanceState getState() { return state; }
    public void setState(InstanceState state) { this.state = state; }
    
    public long getCreateTime() { return createTime; }
    public void setCreateTime(long createTime) { this.createTime = createTime; }
    
    public long getStartTime() { return startTime; }
    public void setStartTime(long startTime) { this.startTime = startTime; }
    
    public long getEndTime() { return endTime; }
    public void setEndTime(long endTime) { this.endTime = endTime; }
    
    public InstanceResult getResult() { return result; }
    public void setResult(InstanceResult result) { this.result = result; }
    
    public InstanceContext getContext() { return context; }
    public void setContext(InstanceContext context) { this.context = context; }
    
    public List<RecoveryPoint> getRecoveryPoints() { return recoveryPoints; }
    public void setRecoveryPoints(List<RecoveryPoint> recoveryPoints) { 
        this.recoveryPoints = recoveryPoints != null ? recoveryPoints : new ArrayList<>(); 
    }
    
    public boolean isRunning() {
        return state == InstanceState.RUNNING || state == InstanceState.INITIALIZING;
    }
    
    public boolean isCompleted() {
        return state == InstanceState.COMPLETED || state == InstanceState.FAILED || state == InstanceState.TERMINATED;
    }
    
    public boolean isSuccessful() {
        return state == InstanceState.COMPLETED && (result == null || result.isSuccess());
    }
    
    public void start() {
        this.state = InstanceState.RUNNING;
        this.startTime = System.currentTimeMillis();
    }
    
    public void complete(boolean success, String message) {
        this.state = success ? InstanceState.COMPLETED : InstanceState.FAILED;
        this.endTime = System.currentTimeMillis();
        
        this.result = new InstanceResult();
        this.result.setSuccess(success);
        this.result.setMessage(message);
        this.result.setDuration(endTime - startTime);
        
        this.context = null;
        this.recoveryPoints.clear();
    }
    
    public void terminate(String reason) {
        this.state = InstanceState.TERMINATED;
        this.endTime = System.currentTimeMillis();
        
        this.result = new InstanceResult();
        this.result.setSuccess(false);
        this.result.setMessage(reason);
        this.result.setDuration(endTime - startTime);
    }
    
    public void createRecoveryPoint(String name) {
        if (!isRunning()) {
            return;
        }
        
        RecoveryPoint point = new RecoveryPoint();
        point.setName(name);
        point.setTimestamp(System.currentTimeMillis());
        if (context != null) {
            point.setContextSnapshot(context.snapshot());
        }
        
        recoveryPoints.add(point);
    }
    
    public RecoveryPoint getLatestRecoveryPoint() {
        if (recoveryPoints.isEmpty()) {
            return null;
        }
        return recoveryPoints.get(recoveryPoints.size() - 1);
    }
    
    public long getDuration() {
        if (startTime <= 0) {
            return 0;
        }
        long end = endTime > 0 ? endTime : System.currentTimeMillis();
        return end - startTime;
    }
    
    public String getDurationFormatted() {
        long duration = getDuration();
        long seconds = duration / 1000;
        if (seconds < 60) return seconds + "s";
        long minutes = seconds / 60;
        if (minutes < 60) return minutes + "m " + (seconds % 60) + "s";
        long hours = minutes / 60;
        return hours + "h " + (minutes % 60) + "m";
    }
    
    public static class InstanceResult implements Serializable {
        
        private static final long serialVersionUID = 1L;
        
        private boolean success;
        private String message;
        private String errorCode;
        private Map<String, Object> data;
        private long duration;
        
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        
        public String getErrorCode() { return errorCode; }
        public void setErrorCode(String errorCode) { this.errorCode = errorCode; }
        
        public Map<String, Object> getData() { return data; }
        public void setData(Map<String, Object> data) { this.data = data; }
        
        public long getDuration() { return duration; }
        public void setDuration(long duration) { this.duration = duration; }
    }
    
    public static class InstanceContext implements Serializable {
        
        private static final long serialVersionUID = 1L;
        
        private String primaryAgentId;
        private List<String> memberAgentIds;
        private Map<String, String> agentRoles;
        private Map<String, Object> configuration;
        private Timeline executionTimeline;
        
        public InstanceContext() {
            this.memberAgentIds = new ArrayList<>();
            this.agentRoles = new HashMap<>();
            this.configuration = new HashMap<>();
            this.executionTimeline = new Timeline();
        }
        
        public String getPrimaryAgentId() { return primaryAgentId; }
        public void setPrimaryAgentId(String primaryAgentId) { this.primaryAgentId = primaryAgentId; }
        
        public List<String> getMemberAgentIds() { return memberAgentIds; }
        public void setMemberAgentIds(List<String> memberAgentIds) { 
            this.memberAgentIds = memberAgentIds != null ? memberAgentIds : new ArrayList<>(); 
        }
        
        public void addMember(String agentId) {
            if (!memberAgentIds.contains(agentId)) {
                memberAgentIds.add(agentId);
            }
        }
        
        public void removeMember(String agentId) {
            memberAgentIds.remove(agentId);
        }
        
        public Map<String, String> getAgentRoles() { return agentRoles; }
        public void setAgentRoles(Map<String, String> agentRoles) { 
            this.agentRoles = agentRoles != null ? agentRoles : new HashMap<>(); 
        }
        
        public void setAgentRole(String agentId, String role) {
            agentRoles.put(agentId, role);
        }
        
        public String getAgentRole(String agentId) {
            return agentRoles.get(agentId);
        }
        
        public Map<String, Object> getConfiguration() { return configuration; }
        public void setConfiguration(Map<String, Object> configuration) { 
            this.configuration = configuration != null ? configuration : new HashMap<>(); 
        }
        
        public Timeline getExecutionTimeline() { return executionTimeline; }
        public void setExecutionTimeline(Timeline executionTimeline) { 
            this.executionTimeline = executionTimeline != null ? executionTimeline : new Timeline(); 
        }
        
        public void recordEvent(String event) {
            executionTimeline.record(event);
        }
        
        public InstanceContextSnapshot snapshot() {
            InstanceContextSnapshot snapshot = new InstanceContextSnapshot();
            snapshot.setPrimaryAgentId(primaryAgentId);
            snapshot.setMemberAgentIds(new ArrayList<>(memberAgentIds));
            snapshot.setAgentRoles(new HashMap<>(agentRoles));
            snapshot.setConfiguration(new HashMap<>(configuration));
            snapshot.setSnapshotTime(System.currentTimeMillis());
            return snapshot;
        }
    }
    
    public static class InstanceContextSnapshot implements Serializable {
        
        private static final long serialVersionUID = 1L;
        
        private String primaryAgentId;
        private List<String> memberAgentIds;
        private Map<String, String> agentRoles;
        private Map<String, Object> configuration;
        private long snapshotTime;
        
        public String getPrimaryAgentId() { return primaryAgentId; }
        public void setPrimaryAgentId(String primaryAgentId) { this.primaryAgentId = primaryAgentId; }
        
        public List<String> getMemberAgentIds() { return memberAgentIds; }
        public void setMemberAgentIds(List<String> memberAgentIds) { this.memberAgentIds = memberAgentIds; }
        
        public Map<String, String> getAgentRoles() { return agentRoles; }
        public void setAgentRoles(Map<String, String> agentRoles) { this.agentRoles = agentRoles; }
        
        public Map<String, Object> getConfiguration() { return configuration; }
        public void setConfiguration(Map<String, Object> configuration) { this.configuration = configuration; }
        
        public long getSnapshotTime() { return snapshotTime; }
        public void setSnapshotTime(long snapshotTime) { this.snapshotTime = snapshotTime; }
    }
    
    public static class RecoveryPoint implements Serializable {
        
        private static final long serialVersionUID = 1L;
        
        private String name;
        private long timestamp;
        private InstanceContextSnapshot contextSnapshot;
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public long getTimestamp() { return timestamp; }
        public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
        
        public InstanceContextSnapshot getContextSnapshot() { return contextSnapshot; }
        public void setContextSnapshot(InstanceContextSnapshot contextSnapshot) { 
            this.contextSnapshot = contextSnapshot; 
        }
    }
}
