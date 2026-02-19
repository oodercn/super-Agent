package net.ooder.sdk.api.scene.store;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LinkConfig implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String linkId;
    private String sceneId;
    private String sourceId;
    private String targetId;
    private String linkType;
    private LinkDirection direction;
    private Map<String, Object> config;
    private long createTime;
    private long updateTime;
    private String status;
    
    public LinkConfig() {
        this.config = new ConcurrentHashMap<>();
        this.createTime = System.currentTimeMillis();
        this.updateTime = System.currentTimeMillis();
        this.status = "active";
    }
    
    public LinkConfig(String linkId, String sceneId, String sourceId, String targetId) {
        this();
        this.linkId = linkId;
        this.sceneId = sceneId;
        this.sourceId = sourceId;
        this.targetId = targetId;
    }
    
    public String getLinkId() {
        return linkId;
    }
    
    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }
    
    public String getSceneId() {
        return sceneId;
    }
    
    public void setSceneId(String sceneId) {
        this.sceneId = sceneId;
    }
    
    public String getSourceId() {
        return sourceId;
    }
    
    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }
    
    public String getTargetId() {
        return targetId;
    }
    
    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }
    
    public String getLinkType() {
        return linkType;
    }
    
    public void setLinkType(String linkType) {
        this.linkType = linkType;
    }
    
    public LinkDirection getDirection() {
        return direction;
    }
    
    public void setDirection(LinkDirection direction) {
        this.direction = direction;
    }
    
    public Map<String, Object> getConfig() {
        return config;
    }
    
    public void setConfig(Map<String, Object> config) {
        this.config = config != null ? config : new ConcurrentHashMap<>();
    }
    
    public void setConfigProperty(String key, Object value) {
        this.config.put(key, value);
        this.updateTime = System.currentTimeMillis();
    }
    
    public Object getConfigProperty(String key) {
        return config.get(key);
    }
    
    public long getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
    
    public long getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
        this.updateTime = System.currentTimeMillis();
    }
    
    public boolean isActive() {
        return "active".equals(status);
    }
    
    @Override
    public String toString() {
        return "LinkConfig{" +
                "linkId='" + linkId + '\'' +
                ", sceneId='" + sceneId + '\'' +
                ", sourceId='" + sourceId + '\'' +
                ", targetId='" + targetId + '\'' +
                ", linkType='" + linkType + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
    
    public enum LinkDirection {
        OUTBOUND,
        INBOUND,
        BIDIRECTIONAL
    }
}
