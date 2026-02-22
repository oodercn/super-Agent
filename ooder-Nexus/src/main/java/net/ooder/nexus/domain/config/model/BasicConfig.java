package net.ooder.nexus.domain.config.model;

import java.io.Serializable;

public class BasicConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    private String agentId;
    private String agentName;
    private String agentType;
    private String environment;
    private String timezone;
    private String ntpServer;
    private long lastUpdated;

    public BasicConfig() {
    }

    public BasicConfig(String agentId, String agentName, String agentType, String environment, String timezone, String ntpServer) {
        this.agentId = agentId;
        this.agentName = agentName;
        this.agentType = agentType;
        this.environment = environment;
        this.timezone = timezone;
        this.ntpServer = ntpServer;
        this.lastUpdated = System.currentTimeMillis();
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getAgentType() {
        return agentType;
    }

    public void setAgentType(String agentType) {
        this.agentType = agentType;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getNtpServer() {
        return ntpServer;
    }

    public void setNtpServer(String ntpServer) {
        this.ntpServer = ntpServer;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
