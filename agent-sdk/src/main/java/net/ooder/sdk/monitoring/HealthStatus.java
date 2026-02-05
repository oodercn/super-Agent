package net.ooder.sdk.monitoring;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Map;

public class HealthStatus {
    @JSONField(name = "status")
    private Status status;
    
    @JSONField(name = "level")
    private HealthLevel level;
    
    @JSONField(name = "message")
    private String message;
    
    @JSONField(name = "timestamp")
    private long timestamp;
    
    @JSONField(name = "details")
    private Map<String, Object> details;
    
    @JSONField(name = "component")
    private String component;
    
    @JSONField(name = "componentId")
    private String componentId;
    
    public HealthStatus() {
        this.timestamp = System.currentTimeMillis();
        this.status = Status.UNKNOWN;
        this.level = HealthLevel.UNKNOWN;
    }
    
    public HealthStatus(Status status, HealthLevel level, String message) {
        this();
        this.status = status;
        this.level = level;
        this.message = message;
    }
    
    public HealthStatus(Status status, HealthLevel level, String message, String component) {
        this(status, level, message);
        this.component = component;
    }
    
    public HealthStatus(Status status, HealthLevel level, String message, String component, Map<String, Object> details) {
        this(status, level, message, component);
        this.details = details;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public HealthLevel getLevel() {
        return level;
    }

    public void setLevel(HealthLevel level) {
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

    public Map<String, Object> getDetails() {
        return details;
    }

    public void setDetails(Map<String, Object> details) {
        this.details = details;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    @Override
    public String toString() {
        return "HealthStatus{" +
                "status=" + status +
                ", level=" + level +
                ", message='" + message + '\'' +
                ", component='" + component + '\'' +
                '}';
    }
    
    public enum Status {
        UNKNOWN("unknown", "未知"),
        HEALTHY("healthy", "健康"),
        DEGRADED("degraded", "降级"),
        UNHEALTHY("unhealthy", "不健康"),
        CRITICAL("critical", "严重"),
        FAILING("failing", "故障");
        
        private final String value;
        private final String description;
        
        Status(String value, String description) {
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
    
    public enum HealthLevel {
        UNKNOWN("unknown", "未知", 0),
        EXCELLENT("excellent", "优秀", 5),
        GOOD("good", "良好", 4),
        FAIR("fair", "一般", 3),
        POOR("poor", "较差", 2),
        BAD("bad", "差", 1),
        CRITICAL("critical", "严重", 0);
        
        private final String value;
        private final String description;
        private final int score;
        
        HealthLevel(String value, String description, int score) {
            this.value = value;
            this.description = description;
            this.score = score;
        }
        
        public String getValue() {
            return value;
        }
        
        public String getDescription() {
            return description;
        }
        
        public int getScore() {
            return score;
        }
        
        @Override
        public String toString() {
            return value;
        }
    }
}
