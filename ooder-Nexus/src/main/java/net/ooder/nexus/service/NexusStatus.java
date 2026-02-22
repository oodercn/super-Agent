package net.ooder.nexus.service;

/**
 * Nexus 状态
 */
public class NexusStatus {
    
    public enum State {
        STOPPED,
        STARTING,
        RUNNING,
        STOPPING,
        ERROR
    }
    
    private State state;
    private String message;
    private long startTime;
    private int peerCount;
    private int activeSceneGroups;

    public NexusStatus() {
        this.state = State.STOPPED;
    }

    public State getState() { return state; }
    public void setState(State state) { this.state = state; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public long getStartTime() { return startTime; }
    public void setStartTime(long startTime) { this.startTime = startTime; }
    
    public int getPeerCount() { return peerCount; }
    public void setPeerCount(int peerCount) { this.peerCount = peerCount; }
    
    public int getActiveSceneGroups() { return activeSceneGroups; }
    public void setActiveSceneGroups(int activeSceneGroups) { this.activeSceneGroups = activeSceneGroups; }
}
