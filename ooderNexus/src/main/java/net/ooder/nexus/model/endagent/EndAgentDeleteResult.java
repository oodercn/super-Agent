package net.ooder.nexus.model.endagent;

import java.io.Serializable;

/**
 * EndAgent删除结果
 * 用于EndAgentController中deleteEndAgent方法的返回类型
 */
public class EndAgentDeleteResult implements Serializable {
    private String agentId;

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }
}