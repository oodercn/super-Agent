package net.ooder.mvp.skill.scene.dto.scene;

import net.ooder.mvp.skill.scene.dto.discovery.CapabilityDTO;
import java.util.List;

public class SuperAgentParticipantDTO {
    private String superAgentId;
    private String name;
    private String description;
    
    private List<SubAgentRefDTO> subAgents;
    private CoordinationConfigDTO coordination;
    private List<CapabilityDTO> emergentCapabilities;
    private boolean selfDefined;
    
    private String status;

    public String getSuperAgentId() { return superAgentId; }
    public void setSuperAgentId(String superAgentId) { this.superAgentId = superAgentId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public List<SubAgentRefDTO> getSubAgents() { return subAgents; }
    public void setSubAgents(List<SubAgentRefDTO> subAgents) { this.subAgents = subAgents; }
    public CoordinationConfigDTO getCoordination() { return coordination; }
    public void setCoordination(CoordinationConfigDTO coordination) { this.coordination = coordination; }
    public List<CapabilityDTO> getEmergentCapabilities() { return emergentCapabilities; }
    public void setEmergentCapabilities(List<CapabilityDTO> emergentCapabilities) { this.emergentCapabilities = emergentCapabilities; }
    public boolean isSelfDefined() { return selfDefined; }
    public void setSelfDefined(boolean selfDefined) { this.selfDefined = selfDefined; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
