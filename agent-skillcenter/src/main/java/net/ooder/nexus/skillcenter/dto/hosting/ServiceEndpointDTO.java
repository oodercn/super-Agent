package net.ooder.nexus.skillcenter.dto.hosting;

import java.util.List;

public class ServiceEndpointDTO {
    private String serviceId;
    private String instanceId;
    private String serviceName;
    private String endpoint;
    private String protocol;
    private String host;
    private int port;
    private String status;
    private List<String> aliases;
    private LoadBalancerConfig loadBalancer;

    public String getServiceId() { return serviceId; }
    public void setServiceId(String serviceId) { this.serviceId = serviceId; }
    public String getInstanceId() { return instanceId; }
    public void setInstanceId(String instanceId) { this.instanceId = instanceId; }
    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }
    public String getEndpoint() { return endpoint; }
    public void setEndpoint(String endpoint) { this.endpoint = endpoint; }
    public String getProtocol() { return protocol; }
    public void setProtocol(String protocol) { this.protocol = protocol; }
    public String getHost() { return host; }
    public void setHost(String host) { this.host = host; }
    public int getPort() { return port; }
    public void setPort(int port) { this.port = port; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public List<String> getAliases() { return aliases; }
    public void setAliases(List<String> aliases) { this.aliases = aliases; }
    public LoadBalancerConfig getLoadBalancer() { return loadBalancer; }
    public void setLoadBalancer(LoadBalancerConfig loadBalancer) { this.loadBalancer = loadBalancer; }

    public static class LoadBalancerConfig {
        private String strategy;
        private int healthCheckInterval;
        private int healthCheckTimeout;
        private String healthCheckPath;

        public String getStrategy() { return strategy; }
        public void setStrategy(String strategy) { this.strategy = strategy; }
        public int getHealthCheckInterval() { return healthCheckInterval; }
        public void setHealthCheckInterval(int healthCheckInterval) { this.healthCheckInterval = healthCheckInterval; }
        public int getHealthCheckTimeout() { return healthCheckTimeout; }
        public void setHealthCheckTimeout(int healthCheckTimeout) { this.healthCheckTimeout = healthCheckTimeout; }
        public String getHealthCheckPath() { return healthCheckPath; }
        public void setHealthCheckPath(String healthCheckPath) { this.healthCheckPath = healthCheckPath; }
    }
}
