
package net.ooder.sdk.core.agent.model;

import net.ooder.sdk.api.agent.Agent;

public class AgentStateInfo {
    
    private String agentId;
    private Agent.AgentState state;
    private long createTime;
    private long startTime;
    private long lastActiveTime;
    private int errorCount;
    private String lastError;
    
    public AgentStateInfo() {
        this.state = Agent.AgentState.CREATED;
        this.createTime = System.currentTimeMillis();
    }
    
    public AgentStateInfo(String agentId) {
        this();
        this.agentId = agentId;
    }
    
    public String getAgentId() {
        return agentId;
    }
    
    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }
    
    public Agent.AgentState getState() {
        return state;
    }
    
    public void setState(Agent.AgentState state) {
        this.state = state;
    }
    
    public long getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
    
    public long getStartTime() {
        return startTime;
    }
    
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
    
    public long getLastActiveTime() {
        return lastActiveTime;
    }
    
    public void setLastActiveTime(long lastActiveTime) {
        this.lastActiveTime = lastActiveTime;
    }
    
    public int getErrorCount() {
        return errorCount;
    }
    
    public void setErrorCount(int errorCount) {
        this.errorCount = errorCount;
    }
    
    public String getLastError() {
        return lastError;
    }
    
    public void setLastError(String lastError) {
        this.lastError = lastError;
    }
    
    public void recordActivity() {
        this.lastActiveTime = System.currentTimeMillis();
    }
    
    public void recordError(String error) {
        this.errorCount++;
        this.lastError = error;
    }
    
    public void transitionTo(Agent.AgentState newState) {
        this.state = newState;
        if (newState == Agent.AgentState.RUNNING) {
            this.startTime = System.currentTimeMillis();
        }
        recordActivity();
    }
    
    public boolean isRunning() {
        return state == Agent.AgentState.RUNNING;
    }
    
    public boolean isHealthy() {
        return state == Agent.AgentState.RUNNING || state == Agent.AgentState.INITIALIZED;
    }
    
    public long getUptime() {
        if (startTime > 0 && isRunning()) {
            return System.currentTimeMillis() - startTime;
        }
        return 0;
    }
}
