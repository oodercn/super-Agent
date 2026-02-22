package net.ooder.nexus.domain.config.model;

import java.io.Serializable;

public class SystemConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    private String javaVersion;
    private String osName;
    private String osVersion;
    private String hostname;
    private String ipAddress;
    private int maxHeapSize;
    private int minHeapSize;
    private String gcAlgorithm;
    private long lastUpdated;

    public SystemConfig() {
    }

    public SystemConfig(String javaVersion, String osName, String osVersion, String hostname, String ipAddress, int maxHeapSize, int minHeapSize, String gcAlgorithm) {
        this.javaVersion = javaVersion;
        this.osName = osName;
        this.osVersion = osVersion;
        this.hostname = hostname;
        this.ipAddress = ipAddress;
        this.maxHeapSize = maxHeapSize;
        this.minHeapSize = minHeapSize;
        this.gcAlgorithm = gcAlgorithm;
        this.lastUpdated = System.currentTimeMillis();
    }

    public SystemConfig(String javaVersion, String osName, String osVersion, String hostname, String ipAddress, long maxHeapSize, long minHeapSize, String gcAlgorithm, long lastUpdated) {
        this.javaVersion = javaVersion;
        this.osName = osName;
        this.osVersion = osVersion;
        this.hostname = hostname;
        this.ipAddress = ipAddress;
        this.maxHeapSize = (int) maxHeapSize;
        this.minHeapSize = (int) minHeapSize;
        this.gcAlgorithm = gcAlgorithm;
        this.lastUpdated = lastUpdated;
    }

    public String getJavaVersion() {
        return javaVersion;
    }

    public void setJavaVersion(String javaVersion) {
        this.javaVersion = javaVersion;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getMaxHeapSize() {
        return maxHeapSize;
    }

    public void setMaxHeapSize(int maxHeapSize) {
        this.maxHeapSize = maxHeapSize;
    }

    public int getMinHeapSize() {
        return minHeapSize;
    }

    public void setMinHeapSize(int minHeapSize) {
        this.minHeapSize = minHeapSize;
    }

    public String getGcAlgorithm() {
        return gcAlgorithm;
    }

    public void setGcAlgorithm(String gcAlgorithm) {
        this.gcAlgorithm = gcAlgorithm;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
