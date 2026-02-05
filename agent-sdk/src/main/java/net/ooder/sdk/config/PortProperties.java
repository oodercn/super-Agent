package net.ooder.sdk.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "ooder.sdk.port")
public class PortProperties {
    
    private AllocationStrategy allocationStrategy = AllocationStrategy.DYNAMIC;
    
    private int localStart = 8080;
    
    private int localEnd = 8192;
    
    private int lanStart = 9000;
    
    private int lanEnd = 9100;
    
    private int intranetStart = 10000;
    
    private int intranetEnd = 10100;
    
    private int globalStart = 1024;
    
    private int globalEnd = 65535;
    
    private boolean smartAllocationEnabled = true;
    
    private int historySize = 1000;
    
    private long cleanupIntervalMs = 3600000;
    
    private Map<String, Integer> servicePorts = new HashMap<>();
    
    public enum AllocationStrategy {
        DYNAMIC,
        FIXED,
        SMART
    }
    
    public AllocationStrategy getAllocationStrategy() {
        return allocationStrategy;
    }
    
    public void setAllocationStrategy(AllocationStrategy allocationStrategy) {
        this.allocationStrategy = allocationStrategy;
    }
    
    public int getLocalStart() {
        return localStart;
    }
    
    public void setLocalStart(int localStart) {
        this.localStart = localStart;
    }
    
    public int getLocalEnd() {
        return localEnd;
    }
    
    public void setLocalEnd(int localEnd) {
        this.localEnd = localEnd;
    }
    
    public int getLanStart() {
        return lanStart;
    }
    
    public void setLanStart(int lanStart) {
        this.lanStart = lanStart;
    }
    
    public int getLanEnd() {
        return lanEnd;
    }
    
    public void setLanEnd(int lanEnd) {
        this.lanEnd = lanEnd;
    }
    
    public int getIntranetStart() {
        return intranetStart;
    }
    
    public void setIntranetStart(int intranetStart) {
        this.intranetStart = intranetStart;
    }
    
    public int getIntranetEnd() {
        return intranetEnd;
    }
    
    public void setIntranetEnd(int intranetEnd) {
        this.intranetEnd = intranetEnd;
    }
    
    public int getGlobalStart() {
        return globalStart;
    }
    
    public void setGlobalStart(int globalStart) {
        this.globalStart = globalStart;
    }
    
    public int getGlobalEnd() {
        return globalEnd;
    }
    
    public void setGlobalEnd(int globalEnd) {
        this.globalEnd = globalEnd;
    }
    
    public boolean isSmartAllocationEnabled() {
        return smartAllocationEnabled;
    }
    
    public void setSmartAllocationEnabled(boolean smartAllocationEnabled) {
        this.smartAllocationEnabled = smartAllocationEnabled;
    }
    
    public int getHistorySize() {
        return historySize;
    }
    
    public void setHistorySize(int historySize) {
        this.historySize = historySize;
    }
    
    public long getCleanupIntervalMs() {
        return cleanupIntervalMs;
    }
    
    public void setCleanupIntervalMs(long cleanupIntervalMs) {
        this.cleanupIntervalMs = cleanupIntervalMs;
    }
    
    public Map<String, Integer> getServicePorts() {
        return servicePorts;
    }
    
    public void setServicePorts(Map<String, Integer> servicePorts) {
        this.servicePorts = servicePorts;
    }
}
