package net.ooder.sdk.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "ooder.sdk.network")
public class NetworkProperties {
    
    private String broadcastAddress = "255.255.255.255";
    
    private int defaultPort = 8080;
    
    private int bufferSize = 8192;
    
    private int maxPacketSize = 65536;
    
    private int timeout = 30000;
    
    private int ackTimeout = 5000;
    
    private boolean socketReuse = true;
    
    private boolean socketBroadcast = true;
    
    public String getBroadcastAddress() {
        return broadcastAddress;
    }
    
    public void setBroadcastAddress(String broadcastAddress) {
        this.broadcastAddress = broadcastAddress;
    }
    
    public int getDefaultPort() {
        return defaultPort;
    }
    
    public void setDefaultPort(int defaultPort) {
        this.defaultPort = defaultPort;
    }
    
    public int getBufferSize() {
        return bufferSize;
    }
    
    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }
    
    public int getMaxPacketSize() {
        return maxPacketSize;
    }
    
    public void setMaxPacketSize(int maxPacketSize) {
        this.maxPacketSize = maxPacketSize;
    }
    
    public int getTimeout() {
        return timeout;
    }
    
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
    
    public int getAckTimeout() {
        return ackTimeout;
    }
    
    public void setAckTimeout(int ackTimeout) {
        this.ackTimeout = ackTimeout;
    }
    
    public boolean isSocketReuse() {
        return socketReuse;
    }
    
    public void setSocketReuse(boolean socketReuse) {
        this.socketReuse = socketReuse;
    }
    
    public boolean isSocketBroadcast() {
        return socketBroadcast;
    }
    
    public void setSocketBroadcast(boolean socketBroadcast) {
        this.socketBroadcast = socketBroadcast;
    }
}
