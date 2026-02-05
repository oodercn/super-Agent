package net.ooder.sdk.network.udp;

/**
 * UDP配置类
 */
public class UDPConfig {
    private int port = 8080;
    private int bufferSize = 8192;
    private int timeout = 30000;
    private int maxPacketSize = 65536;
    private boolean strongTypeEnforcement = true;
    private boolean allowCustomFormat = false;
    private PacketValidation packetValidation;
    
    private UDPConfig() {
    }
    
    /**
     * 创建UDP配置构建器
     * @return 构建器
     */
    public static Builder builder() {
        return new Builder();
    }
    
    /**
     * UDP配置构建器
     */
    public static class Builder {
        private UDPConfig config = new UDPConfig();
        
        public Builder port(int port) {
            config.port = port;
            return this;
        }
        
        public Builder bufferSize(int bufferSize) {
            config.bufferSize = bufferSize;
            return this;
        }
        
        public Builder timeout(int timeout) {
            config.timeout = timeout;
            return this;
        }
        
        public Builder maxPacketSize(int maxPacketSize) {
            config.maxPacketSize = maxPacketSize;
            return this;
        }
        
        public Builder strongTypeEnforcement(boolean strongTypeEnforcement) {
            config.strongTypeEnforcement = strongTypeEnforcement;
            return this;
        }
        
        public Builder allowCustomFormat(boolean allowCustomFormat) {
            config.allowCustomFormat = allowCustomFormat;
            return this;
        }
        
        public Builder packetValidation(PacketValidation packetValidation) {
            config.packetValidation = packetValidation;
            return this;
        }
        
        public UDPConfig build() {
            return config;
        }
    }
    
    // Getters
    public int getPort() {
        return port;
    }
    
    public int getBufferSize() {
        return bufferSize;
    }
    
    public int getTimeout() {
        return timeout;
    }
    
    public int getMaxPacketSize() {
        return maxPacketSize;
    }
    
    public boolean isStrongTypeEnforcement() {
        return strongTypeEnforcement;
    }
    
    public boolean isAllowCustomFormat() {
        return allowCustomFormat;
    }
    
    public PacketValidation getPacketValidation() {
        return packetValidation;
    }
}
