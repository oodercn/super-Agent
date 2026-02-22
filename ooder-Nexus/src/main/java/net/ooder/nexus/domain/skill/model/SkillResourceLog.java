package net.ooder.nexus.domain.skill.model;

import java.util.HashMap;
import java.util.Map;

/**
 * 技能资源使用日志
 * 
 * @author ooder Team
 * @version 0.7.3
 * @since SDK 0.7.3
 */
public class SkillResourceLog {
    
    private String logId;
    private String skillId;
    private String skillName;
    private String userId;
    private String action;
    private String resourceType;
    private String resourceId;
    private String resourceName;
    private String sceneId;
    private String sceneName;
    private String groupId;
    private String detail;
    private int status;
    private long timestamp;
    private long duration;
    private String ipAddress;
    private String userAgent;
    
    public static final String ACTION_READ = "read";
    public static final String ACTION_WRITE = "write";
    public static final String ACTION_DELETE = "delete";
    public static final String ACTION_SHARE = "share";
    public static final String ACTION_EXECUTE = "execute";
    public static final String ACTION_JOIN_GROUP = "join_group";
    public static final String ACTION_LEAVE_GROUP = "leave_group";
    public static final String ACTION_SEND_MESSAGE = "send_message";
    public static final String ACTION_UPLOAD_FILE = "upload_file";
    public static final String ACTION_DOWNLOAD_FILE = "download_file";
    
    public static final int STATUS_SUCCESS = 1;
    public static final int STATUS_FAILED = 0;
    public static final int STATUS_DENIED = 2;
    
    public SkillResourceLog() {
        this.timestamp = System.currentTimeMillis();
    }
    
    public String getLogId() {
        return logId;
    }
    
    public void setLogId(String logId) {
        this.logId = logId;
    }
    
    public String getSkillId() {
        return skillId;
    }
    
    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }
    
    public String getSkillName() {
        return skillName;
    }
    
    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public String getAction() {
        return action;
    }
    
    public void setAction(String action) {
        this.action = action;
    }
    
    public String getResourceType() {
        return resourceType;
    }
    
    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }
    
    public String getResourceId() {
        return resourceId;
    }
    
    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }
    
    public String getResourceName() {
        return resourceName;
    }
    
    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }
    
    public String getSceneId() {
        return sceneId;
    }
    
    public void setSceneId(String sceneId) {
        this.sceneId = sceneId;
    }
    
    public String getSceneName() {
        return sceneName;
    }
    
    public void setSceneName(String sceneName) {
        this.sceneName = sceneName;
    }
    
    public String getGroupId() {
        return groupId;
    }
    
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
    
    public String getDetail() {
        return detail;
    }
    
    public void setDetail(String detail) {
        this.detail = detail;
    }
    
    public int getStatus() {
        return status;
    }
    
    public void setStatus(int status) {
        this.status = status;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    
    public long getDuration() {
        return duration;
    }
    
    public void setDuration(long duration) {
        this.duration = duration;
    }
    
    public String getIpAddress() {
        return ipAddress;
    }
    
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
    
    public String getUserAgent() {
        return userAgent;
    }
    
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
    
    public String getActionText() {
        switch (action) {
            case ACTION_READ: return "读取";
            case ACTION_WRITE: return "写入";
            case ACTION_DELETE: return "删除";
            case ACTION_SHARE: return "分享";
            case ACTION_EXECUTE: return "执行";
            case ACTION_JOIN_GROUP: return "加入群组";
            case ACTION_LEAVE_GROUP: return "退出群组";
            case ACTION_SEND_MESSAGE: return "发送消息";
            case ACTION_UPLOAD_FILE: return "上传文件";
            case ACTION_DOWNLOAD_FILE: return "下载文件";
            default: return action;
        }
    }
    
    public String getStatusText() {
        switch (status) {
            case STATUS_SUCCESS: return "成功";
            case STATUS_FAILED: return "失败";
            case STATUS_DENIED: return "拒绝";
            default: return "未知";
        }
    }
    
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("logId", logId);
        map.put("skillId", skillId);
        map.put("skillName", skillName);
        map.put("userId", userId);
        map.put("action", action);
        map.put("actionText", getActionText());
        map.put("resourceType", resourceType);
        map.put("resourceId", resourceId);
        map.put("resourceName", resourceName);
        map.put("sceneId", sceneId);
        map.put("sceneName", sceneName);
        map.put("groupId", groupId);
        map.put("detail", detail);
        map.put("status", status);
        map.put("statusText", getStatusText());
        map.put("timestamp", timestamp);
        map.put("duration", duration);
        map.put("ipAddress", ipAddress);
        return map;
    }
}
