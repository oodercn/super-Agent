package net.ooder.nexus.model.network;

/**
 * 终端代理发现结果实体Bean
 * 用于EndAgentController中discoverEndAgents方法的返回类型
 */
public class EndAgentDiscoverResult {
    
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
