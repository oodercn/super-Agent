package net.ooder.nexus.domain.network.model;

import java.util.List;
import java.util.Map;

/**
 * 终端代理详情结果实体Bean
 * 用于EndAgentController中getEndAgentDetail方法的返回类型
 */
public class EndAgentDetailResult {
    
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
