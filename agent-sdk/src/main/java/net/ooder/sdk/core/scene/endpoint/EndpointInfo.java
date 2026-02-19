package net.ooder.sdk.core.scene.endpoint;

public class EndpointInfo {
    
    private String endpoint;
    private String skillId;
    private String protocol;
    private String host;
    private int port;
    private long allocateTime;
    private String status;
    
    public EndpointInfo() {
        this.allocateTime = System.currentTimeMillis();
        this.status = "allocated";
    }
    
    public EndpointInfo(String endpoint, String skillId, String protocol) {
        this();
        this.endpoint = endpoint;
        this.skillId = skillId;
        this.protocol = protocol;
        parseEndpoint();
    }
    
    private void parseEndpoint() {
        if (endpoint != null && endpoint.contains(":")) {
            String[] parts = endpoint.split(":");
            if (parts.length == 2) {
                this.host = parts[0];
                try {
                    this.port = Integer.parseInt(parts[1]);
                } catch (NumberFormatException e) {
                    this.port = 0;
                }
            }
        }
    }
    
    public String getEndpoint() {
        return endpoint;
    }
    
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
        parseEndpoint();
    }
    
    public String getSkillId() {
        return skillId;
    }
    
    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }
    
    public String getProtocol() {
        return protocol;
    }
    
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
    
    public String getHost() {
        return host;
    }
    
    public void setHost(String host) {
        this.host = host;
    }
    
    public int getPort() {
        return port;
    }
    
    public void setPort(int port) {
        this.port = port;
    }
    
    public long getAllocateTime() {
        return allocateTime;
    }
    
    public void setAllocateTime(long allocateTime) {
        this.allocateTime = allocateTime;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public boolean isAllocated() {
        return "allocated".equals(status);
    }
    
    @Override
    public String toString() {
        return "EndpointInfo{" +
                "endpoint='" + endpoint + '\'' +
                ", skillId='" + skillId + '\'' +
                ", protocol='" + protocol + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
