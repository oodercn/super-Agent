package net.ooder.nexus.domain.config.model;

import java.io.Serializable;

/**
 * Config Reset Result
 */
public class ConfigResetResult implements Serializable {
    private String configType;
    private boolean success;
    private NexusConfigResult nexus;
    private RouteConfigResult route;
    private EndConfigResult end;

    public String getConfigType() { return configType; }
    public void setConfigType(String configType) { this.configType = configType; }
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public NexusConfigResult getNexus() { return nexus; }
    public void setNexus(NexusConfigResult nexus) { this.nexus = nexus; }
    public RouteConfigResult getRoute() { return route; }
    public void setRoute(RouteConfigResult route) { this.route = route; }
    public EndConfigResult getEnd() { return end; }
    public void setEnd(EndConfigResult end) { this.end = end; }
}
