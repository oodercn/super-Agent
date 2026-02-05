package net.ooder.sdk.security;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Map;
import java.util.Objects;

public class Permission {
    @JSONField(name = "resource")
    private String resource;
    
    @JSONField(name = "action")
    private String action;
    
    @JSONField(name = "condition")
    private Map<String, Object> condition;
    
    @JSONField(name = "effect")
    private Effect effect;
    
    @JSONField(name = "description")
    private String description;
    
    @JSONField(name = "createdAt")
    private long createdAt;
    
    @JSONField(name = "updatedAt")
    private long updatedAt;
    
    public Permission() {
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
        this.effect = Effect.ALLOW;
    }
    
    public Permission(String resource, String action) {
        this();
        this.resource = resource;
        this.action = action;
    }
    
    public Permission(String resource, String action, Effect effect) {
        this(resource, action);
        this.effect = effect;
    }
    
    public Permission(String resource, String action, Effect effect, Map<String, Object> condition) {
        this(resource, action, effect);
        this.condition = condition;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
        this.updatedAt = System.currentTimeMillis();
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
        this.updatedAt = System.currentTimeMillis();
    }

    public Map<String, Object> getCondition() {
        return condition;
    }

    public void setCondition(Map<String, Object> condition) {
        this.condition = condition;
        this.updatedAt = System.currentTimeMillis();
    }

    public Effect getEffect() {
        return effect;
    }

    public void setEffect(Effect effect) {
        this.effect = effect;
        this.updatedAt = System.currentTimeMillis();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        this.updatedAt = System.currentTimeMillis();
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Permission that = (Permission) o;
        return Objects.equals(resource, that.resource) &&
                Objects.equals(action, that.action) &&
                effect == that.effect;
    }

    @Override
    public int hashCode() {
        return Objects.hash(resource, action, effect);
    }

    @Override
    public String toString() {
        return "Permission{" +
                "resource='" + resource + '\'' +
                ", action='" + action + '\'' +
                ", effect=" + effect +
                '}';
    }
    
    public enum Effect {
        ALLOW("allow", "允许"),
        DENY("deny", "拒绝");
        
        private final String value;
        private final String description;
        
        Effect(String value, String description) {
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
}
