package net.ooder.nexus.model.endagent;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import net.ooder.nexus.model.network.EndAgent;

/**
 * EndAgent详情结果
 * 用于EndAgentController中getEndAgentDetail方法的返回类型
 */
public class EndAgentDetailResult implements Serializable {
    private EndAgent agent;
    private List<Map<String, Object>> history;

    public EndAgent getAgent() {
        return agent;
    }

    public void setAgent(EndAgent agent) {
        this.agent = agent;
    }

    public List<Map<String, Object>> getHistory() {
        return history;
    }

    public void setHistory(List<Map<String, Object>> history) {
        this.history = history;
    }
}