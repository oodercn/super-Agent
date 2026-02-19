
package net.ooder.sdk.service.network.link;

import java.util.Map;

public class Link {
    
    private String linkId;
    private String sourceId;
    private String targetId;
    private LinkType type;
    private LinkStatus status;
    private long createTime;
    private long lastActive;
    private Map<String, Object> metadata;
    
    public String getLinkId() { return linkId; }
    public void setLinkId(String linkId) { this.linkId = linkId; }
    
    public String getSourceId() { return sourceId; }
    public void setSourceId(String sourceId) { this.sourceId = sourceId; }
    
    public String getTargetId() { return targetId; }
    public void setTargetId(String targetId) { this.targetId = targetId; }
    
    public LinkType getType() { return type; }
    public void setType(LinkType type) { this.type = type; }
    
    public LinkStatus getStatus() { return status; }
    public void setStatus(LinkStatus status) { this.status = status; }
    
    public long getCreateTime() { return createTime; }
    public void setCreateTime(long createTime) { this.createTime = createTime; }
    
    public long getLastActive() { return lastActive; }
    public void setLastActive(long lastActive) { this.lastActive = lastActive; }
    
    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
}
