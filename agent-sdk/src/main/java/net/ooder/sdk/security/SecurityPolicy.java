package net.ooder.sdk.security;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SecurityPolicy {
    @JSONField(name = "policyId")
    private String policyId;
    
    @JSONField(name = "name")
    private String name;
    
    @JSONField(name = "description")
    private String description;
    
    @JSONField(name = "type")
    private PolicyType type;
    
    @JSONField(name = "enabled")
    private boolean enabled;
    
    @JSONField(name = "priority")
    private int priority;
    
    @JSONField(name = "rules")
    private List<PolicyRule> rules;
    
    @JSONField(name = "conditions")
    private Map<String, Object> conditions;
    
    @JSONField(name = "createdAt")
    private long createdAt;
    
    @JSONField(name = "updatedAt")
    private long updatedAt;
    
    public SecurityPolicy() {
        this.policyId = UUID.randomUUID().toString();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
        this.enabled = true;
        this.priority = 0;
    }
    
    public SecurityPolicy(String name, PolicyType type) {
        this();
        this.name = name;
        this.type = type;
    }
    
    public SecurityPolicy(String name, PolicyType type, List<PolicyRule> rules) {
        this(name, type);
        this.rules = rules;
    }

    public String getPolicyId() {
        return policyId;
    }

    public void setPolicyId(String policyId) {
        this.policyId = policyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.updatedAt = System.currentTimeMillis();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        this.updatedAt = System.currentTimeMillis();
    }

    public PolicyType getType() {
        return type;
    }

    public void setType(PolicyType type) {
        this.type = type;
        this.updatedAt = System.currentTimeMillis();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        this.updatedAt = System.currentTimeMillis();
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
        this.updatedAt = System.currentTimeMillis();
    }

    public List<PolicyRule> getRules() {
        return rules;
    }

    public void setRules(List<PolicyRule> rules) {
        this.rules = rules;
        this.updatedAt = System.currentTimeMillis();
    }

    public Map<String, Object> getConditions() {
        return conditions;
    }

    public void setConditions(Map<String, Object> conditions) {
        this.conditions = conditions;
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
    public String toString() {
        return "SecurityPolicy{" +
                "policyId='" + policyId + '\'' +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", enabled=" + enabled +
                ", priority=" + priority +
                '}';
    }
    
    public enum PolicyType {
        AUTHENTICATION("authentication", "认证策略"),
        AUTHORIZATION("authorization", "授权策略"),
        ENCRYPTION("encryption", "加密策略"),
        AUDIT("audit", "审计策略"),
        NETWORK("network", "网络安全策略"),
        TERMINAL("terminal", "终端安全策略");
        
        private final String value;
        private final String description;
        
        PolicyType(String value, String description) {
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
    
    public static class PolicyRule {
        @JSONField(name = "ruleId")
        private String ruleId;
        
        @JSONField(name = "effect")
        private Permission.Effect effect;
        
        @JSONField(name = "resource")
        private String resource;
        
        @JSONField(name = "action")
        private String action;
        
        @JSONField(name = "conditions")
        private Map<String, Object> conditions;
        
        public PolicyRule() {
            this.ruleId = UUID.randomUUID().toString();
        }
        
        public PolicyRule(Permission.Effect effect, String resource, String action) {
            this();
            this.effect = effect;
            this.resource = resource;
            this.action = action;
        }
        
        public PolicyRule(Permission.Effect effect, String resource, String action, Map<String, Object> conditions) {
            this(effect, resource, action);
            this.conditions = conditions;
        }

        public String getRuleId() {
            return ruleId;
        }

        public void setRuleId(String ruleId) {
            this.ruleId = ruleId;
        }

        public Permission.Effect getEffect() {
            return effect;
        }

        public void setEffect(Permission.Effect effect) {
            this.effect = effect;
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

        public Map<String, Object> getConditions() {
            return conditions;
        }

        public void setConditions(Map<String, Object> conditions) {
            this.conditions = conditions;
        }

        @Override
        public String toString() {
            return "PolicyRule{" +
                    "ruleId='" + ruleId + '\'' +
                    ", effect=" + effect +
                    ", resource='" + resource + '\'' +
                    ", action='" + action + '\'' +
                    '}';
        }
    }
}
