package net.ooder.nexus.domain.end.model;

import java.io.Serializable;

/**
 * EndAgent Discovery Result
 */
public class EndAgentDiscoverResult implements Serializable {
    private String status;
    private String message;

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
