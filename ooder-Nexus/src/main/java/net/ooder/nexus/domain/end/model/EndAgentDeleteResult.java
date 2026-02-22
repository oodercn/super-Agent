package net.ooder.nexus.domain.end.model;

import java.io.Serializable;

/**
 * EndAgent Delete Result
 */
public class EndAgentDeleteResult implements Serializable {
    private String agentId;

    public String getAgentId() { return agentId; }
    public void setAgentId(String agentId) { this.agentId = agentId; }
}
