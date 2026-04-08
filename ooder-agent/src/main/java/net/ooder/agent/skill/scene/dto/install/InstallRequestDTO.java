package net.ooder.mvp.skill.scene.dto.install;

import java.util.Map;

public class InstallRequestDTO {
    
    private String capabilityId;
    private String driverCondition;
    private ParticipantsDTO participants;
    private Map<String, Object> config;
    private PushType pushType;
    private String pushTarget;
    
    public InstallRequestDTO() {
    }
    
    public String getCapabilityId() { return capabilityId; }
    public void setCapabilityId(String capabilityId) { this.capabilityId = capabilityId; }
    
    public String getDriverCondition() { return driverCondition; }
    public void setDriverCondition(String driverCondition) { this.driverCondition = driverCondition; }
    
    public ParticipantsDTO getParticipants() { return participants; }
    public void setParticipants(ParticipantsDTO participants) { this.participants = participants; }
    
    public Map<String, Object> getConfig() { return config; }
    public void setConfig(Map<String, Object> config) { this.config = config; }
    
    public PushType getPushType() { return pushType; }
    public void setPushType(PushType pushType) { this.pushType = pushType; }
    
    public String getPushTarget() { return pushTarget; }
    public void setPushTarget(String pushTarget) { this.pushTarget = pushTarget; }
    
    public static class ParticipantsDTO {
        private String mode;
        private java.util.List<ParticipantDTO> participants;
        
        public String getMode() { return mode; }
        public void setMode(String mode) { this.mode = mode; }
        
        public java.util.List<ParticipantDTO> getParticipants() { return participants; }
        public void setParticipants(java.util.List<ParticipantDTO> participants) { this.participants = participants; }
    }
    
    public static class ParticipantDTO {
        private String type;
        private String id;
        private String name;
        
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }
    
    public enum PushType {
        NONE,
        PUSH,
        PULL
    }
}
