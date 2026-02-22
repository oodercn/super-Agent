package net.ooder.skillcenter.sdk.cloud;

public class ServiceEndpoint {
    private String instanceId;
    private String endpoint;
    private String protocol;
    private String host;
    private int port;
    private String dnsName;
    private String type;
    private String url;

    public String getInstanceId() { return instanceId; }
    public void setInstanceId(String instanceId) { this.instanceId = instanceId; }
    public String getEndpoint() { return endpoint; }
    public void setEndpoint(String endpoint) { this.endpoint = endpoint; }
    public String getProtocol() { return protocol; }
    public void setProtocol(String protocol) { this.protocol = protocol; }
    public String getHost() { return host; }
    public void setHost(String host) { this.host = host; }
    public int getPort() { return port; }
    public void setPort(int port) { this.port = port; }
    public String getDnsName() { return dnsName; }
    public void setDnsName(String dnsName) { this.dnsName = dnsName; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
}
