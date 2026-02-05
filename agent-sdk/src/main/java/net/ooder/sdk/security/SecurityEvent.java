package net.ooder.sdk.security;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Map;
import java.util.UUID;

public class SecurityEvent {
    @JSONField(name = "eventId")
    private String eventId;
    
    @JSONField(name = "eventType")
    private EventType eventType;
    
    @JSONField(name = "level")
    private EventLevel level;
    
    @JSONField(name = "message")
    private String message;
    
    @JSONField(name = "timestamp")
    private long timestamp;
    
    @JSONField(name = "principal")
    private String principal;
    
    @JSONField(name = "resource")
    private String resource;
    
    @JSONField(name = "action")
    private String action;
    
    @JSONField(name = "details")
    private Map<String, Object> details;
    
    @JSONField(name = "source")
    private String source;
    
    @JSONField(name = "processed")
    private boolean processed;
    
    public SecurityEvent() {
        this.eventId = UUID.randomUUID().toString();
        this.timestamp = System.currentTimeMillis();
        this.processed = false;
        this.level = EventLevel.INFO;
    }
    
    public SecurityEvent(EventType eventType, EventLevel level, String message) {
        this();
        this.eventType = eventType;
        this.level = level;
        this.message = message;
    }
    
    public SecurityEvent(EventType eventType, EventLevel level, String message, String principal) {
        this(eventType, level, message);
        this.principal = principal;
    }
    
    public SecurityEvent(EventType eventType, EventLevel level, String message, String principal, Map<String, Object> details) {
        this(eventType, level, message, principal);
        this.details = details;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public EventLevel getLevel() {
        return level;
    }

    public void setLevel(EventLevel level) {
        this.level = level;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Map<String, Object> getDetails() {
        return details;
    }

    public void setDetails(Map<String, Object> details) {
        this.details = details;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    @Override
    public String toString() {
        return "SecurityEvent{" +
                "eventId='" + eventId + '\'' +
                ", eventType=" + eventType +
                ", level=" + level +
                ", message='" + message + '\'' +
                ", timestamp=" + timestamp +
                ", principal='" + principal + '\'' +
                ", processed=" + processed +
                '}';
    }
    
    public enum EventType {
        AUTHENTICATION_SUCCESS("authentication.success", "认证成功"),
        AUTHENTICATION_FAILURE("authentication.failure", "认证失败"),
        AUTHORIZATION_GRANTED("authorization.granted", "授权通过"),
        AUTHORIZATION_DENIED("authorization.denied", "授权拒绝"),
        TOKEN_GENERATED("token.generated", "令牌生成"),
        TOKEN_VALIDATED("token.validated", "令牌验证"),
        TOKEN_EXPIRED("token.expired", "令牌过期"),
        TOKEN_REVOKED("token.revoked", "令牌吊销"),
        PERMISSION_ADDED("permission.added", "权限添加"),
        PERMISSION_REMOVED("permission.removed", "权限移除"),
        POLICY_CREATED("policy.created", "策略创建"),
        POLICY_UPDATED("policy.updated", "策略更新"),
        POLICY_DELETED("policy.deleted", "策略删除"),
        ENCRYPTION_OPERATION("encryption.operation", "加密操作"),
        DECRYPTION_OPERATION("decryption.operation", "解密操作"),
        SECURE_CONFIG_SAVED("secure.config.saved", "安全配置保存"),
        SECURE_CONFIG_LOADED("secure.config.loaded", "安全配置加载"),
        SECURE_CONFIG_DELETED("secure.config.deleted", "安全配置删除"),
        SECURITY_VIOLATION("security.violation", "安全违反"),
        SECURITY_AUDIT("security.audit", "安全审计"),
        NETWORK_SECURITY_EVENT("network.security.event", "网络安全事件"),
        TERMINAL_SECURITY_EVENT("terminal.security.event", "终端安全事件");
        
        private final String value;
        private final String description;
        
        EventType(String value, String description) {
            this.value = value;
            this.description = description;
        }
        
        public String getValue() {
            return value;
        }
        
        public String getDescription() {
            return description;
        }
        
        @Override
        public String toString() {
            return value;
        }
    }
    
    public enum EventLevel {
        INFO("info", "信息", 1),
        WARNING("warning", "警告", 2),
        ERROR("error", "错误", 3),
        CRITICAL("critical", "严重", 4),
        EMERGENCY("emergency", "紧急", 5);
        
        private final String value;
        private final String description;
        private final int severity;
        
        EventLevel(String value, String description, int severity) {
            this.value = value;
            this.description = description;
            this.severity = severity;
        }
        
        public String getValue() {
            return value;
        }
        
        public String getDescription() {
            return description;
        }
        
        public int getSeverity() {
            return severity;
        }
        
        @Override
        public String toString() {
            return value;
        }
    }
}
