package net.ooder.mvp.skill.scene.llm.context;

import java.util.Map;

public class ContextUpdate {
    
    private String type;
    private String targetId;
    private Map<String, Object> data;
    private boolean replace;
    private long timestamp;

    public ContextUpdate() {
        this.timestamp = System.currentTimeMillis();
    }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getTargetId() { return targetId; }
    public void setTargetId(String targetId) { this.targetId = targetId; }
    
    public Map<String, Object> getData() { return data; }
    public void setData(Map<String, Object> data) { this.data = data; }
    
    public boolean isReplace() { return replace; }
    public void setReplace(boolean replace) { this.replace = replace; }
    
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
