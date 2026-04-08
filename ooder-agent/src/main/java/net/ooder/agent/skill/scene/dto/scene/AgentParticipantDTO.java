package net.ooder.mvp.skill.scene.dto.scene;

import java.util.List;

public class AgentParticipantDTO {
    private String agentId;
    private String agentName;
    private AgentType agentType;
    private List<String> capabilities;
    private String preferredDevice;
    private String status;

    public String getAgentId() { return agentId; }
    public void setAgentId(String agentId) { this.agentId = agentId; }
    public String getAgentName() { return agentName; }
    public void setAgentName(String agentName) { this.agentName = agentName; }
    public AgentType getAgentType() { return agentType; }
    public void setAgentType(AgentType agentType) { this.agentType = agentType; }
    public List<String> getCapabilities() { return capabilities; }
    public void setCapabilities(List<String> capabilities) { this.capabilities = capabilities; }
    public String getPreferredDevice() { return preferredDevice; }
    public void setPreferredDevice(String preferredDevice) { this.preferredDevice = preferredDevice; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
