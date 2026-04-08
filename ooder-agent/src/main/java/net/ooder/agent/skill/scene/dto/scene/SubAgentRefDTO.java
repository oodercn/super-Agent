package net.ooder.mvp.skill.scene.dto.scene;

import java.util.List;

public class SubAgentRefDTO {
    private String agentId;
    private String role;
    private List<String> capabilities;

    public String getAgentId() { return agentId; }
    public void setAgentId(String agentId) { this.agentId = agentId; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public List<String> getCapabilities() { return capabilities; }
    public void setCapabilities(List<String> capabilities) { this.capabilities = capabilities; }
}
