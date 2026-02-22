package net.ooder.nexus.domain.end.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import net.ooder.nexus.domain.network.model.EndAgent;

/**
 * EndAgent Detail Result
 */
public class EndAgentDetailResult implements Serializable {
    private EndAgent agent;
    private List<Map<String, Object>> history;

    public EndAgent getAgent() { return agent; }
    public void setAgent(EndAgent agent) { this.agent = agent; }
    public List<Map<String, Object>> getHistory() { return history; }
    public void setHistory(List<Map<String, Object>> history) { this.history = history; }
}
