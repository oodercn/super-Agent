package net.ooder.skillcenter.resources.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 网络策略 - 符合v0.7.0协议规范
 */
public class NetworkPolicy {
    
    private boolean ingress;
    private boolean egress;
    private List<PortConfig> ports;
    
    public NetworkPolicy() {
        this.ports = new ArrayList<>();
    }
    
    public static NetworkPolicy fullAccess() {
        NetworkPolicy policy = new NetworkPolicy();
        policy.setIngress(true);
        policy.setEgress(true);
        return policy;
    }
    
    public static NetworkPolicy ingressOnly() {
        NetworkPolicy policy = new NetworkPolicy();
        policy.setIngress(true);
        policy.setEgress(false);
        return policy;
    }
    
    public static NetworkPolicy egressOnly() {
        NetworkPolicy policy = new NetworkPolicy();
        policy.setIngress(false);
        policy.setEgress(true);
        return policy;
    }
    
    public static NetworkPolicy isolated() {
        NetworkPolicy policy = new NetworkPolicy();
        policy.setIngress(false);
        policy.setEgress(false);
        return policy;
    }
    
    public void addPort(int port, String protocol) {
        ports.add(new PortConfig(port, protocol));
    }
    
    public void addPort(int port) {
        ports.add(new PortConfig(port, "TCP"));
    }
    
    public boolean isIngress() { return ingress; }
    public void setIngress(boolean ingress) { this.ingress = ingress; }
    
    public boolean isEgress() { return egress; }
    public void setEgress(boolean egress) { this.egress = egress; }
    
    public List<PortConfig> getPorts() { return ports; }
    public void setPorts(List<PortConfig> ports) { this.ports = ports; }
    
    public static class PortConfig {
        private int port;
        private String protocol;
        private String name;
        
        public PortConfig(int port, String protocol) {
            this.port = port;
            this.protocol = protocol;
        }
        
        public int getPort() { return port; }
        public void setPort(int port) { this.port = port; }
        
        public String getProtocol() { return protocol; }
        public void setProtocol(String protocol) { this.protocol = protocol; }
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }
}
