package net.ooder.sdk.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "terminal.discovery")
public class TerminalDiscoveryProperties {
    
    private int scanInterval = 30000;
    
    public int getScanInterval() {
        return scanInterval;
    }
    
    public void setScanInterval(int scanInterval) {
        this.scanInterval = scanInterval;
    }
}
