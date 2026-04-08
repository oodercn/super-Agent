package net.ooder.mvp.skill.scene.capability.driver;

import java.util.List;
import java.util.Map;

public class DriverCondition {
    
    private String conditionId;
    private String name;
    private String description;
    private String sceneType;
    private List<Trigger> triggers;
    private ParticipantConfig participants;
    private CapabilityRequirements capabilities;
    private Map<String, Object> config;
    
    public static class Trigger {
        private TriggerType type;
        private String cron;
        private String event;
        private String intent;
        private Map<String, Object> params;
        
        public TriggerType getType() { return type; }
        public void setType(TriggerType type) { this.type = type; }
        public String getCron() { return cron; }
        public void setCron(String cron) { this.cron = cron; }
        public String getEvent() { return event; }
        public void setEvent(String event) { this.event = event; }
        public String getIntent() { return intent; }
        public void setIntent(String intent) { this.intent = intent; }
        public Map<String, Object> getParams() { return params; }
        public void setParams(Map<String, Object> params) { this.params = params; }
    }
    
    public enum TriggerType {
        SCHEDULE,
        EVENT,
        INTENT,
        MANUAL
    }
    
    public static class ParticipantConfig {
        private ParticipantRole leader;
        private ParticipantRole collaborators;
        
        public ParticipantRole getLeader() { return leader; }
        public void setLeader(ParticipantRole leader) { this.leader = leader; }
        public ParticipantRole getCollaborators() { return collaborators; }
        public void setCollaborators(ParticipantRole collaborators) { this.collaborators = collaborators; }
    }
    
    public static class ParticipantRole {
        private boolean required;
        private List<String> permissions;
        
        public boolean isRequired() { return required; }
        public void setRequired(boolean required) { this.required = required; }
        public List<String> getPermissions() { return permissions; }
        public void setPermissions(List<String> permissions) { this.permissions = permissions; }
    }
    
    public static class CapabilityRequirements {
        private List<String> required;
        private List<String> optional;
        
        public List<String> getRequired() { return required; }
        public void setRequired(List<String> required) { this.required = required; }
        public List<String> getOptional() { return optional; }
        public void setOptional(List<String> optional) { this.optional = optional; }
    }
    
    public String getConditionId() { return conditionId; }
    public void setConditionId(String conditionId) { this.conditionId = conditionId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getSceneType() { return sceneType; }
    public void setSceneType(String sceneType) { this.sceneType = sceneType; }
    public List<Trigger> getTriggers() { return triggers; }
    public void setTriggers(List<Trigger> triggers) { this.triggers = triggers; }
    public ParticipantConfig getParticipants() { return participants; }
    public void setParticipants(ParticipantConfig participants) { this.participants = participants; }
    public CapabilityRequirements getCapabilities() { return capabilities; }
    public void setCapabilities(CapabilityRequirements capabilities) { this.capabilities = capabilities; }
    public Map<String, Object> getConfig() { return config; }
    public void setConfig(Map<String, Object> config) { this.config = config; }
}
