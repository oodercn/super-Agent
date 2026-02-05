package net.ooder.nexus.model.endagent;

import java.io.Serializable;

/**
 * EndAgent发现结果
 * 用于EndAgentController中discoverEndAgents方法的返回类型
 */
public class EndAgentDiscoverResult implements Serializable {
    private String status;
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}