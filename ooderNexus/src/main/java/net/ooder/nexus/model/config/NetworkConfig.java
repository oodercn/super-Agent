package net.ooder.nexus.model.config;

import java.io.Serializable;

public class NetworkConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    private String networkName;
    private String domainName;
    private String interfaceSpeed;
    private String primaryDns;
    private String secondaryDns;
    private String dnsSuffix;
    private boolean dnsCacheEnabled;
    private int dnsCacheSize;
    private long lastUpdated;

    public NetworkConfig() {
    }

    public NetworkConfig(String networkName, String domainName, String interfaceSpeed, String primaryDns, String secondaryDns, String dnsSuffix, boolean dnsCacheEnabled, int dnsCacheSize) {
        this.networkName = networkName;
        this.domainName = domainName;
        this.interfaceSpeed = interfaceSpeed;
        this.primaryDns = primaryDns;
        this.secondaryDns = secondaryDns;
        this.dnsSuffix = dnsSuffix;
        this.dnsCacheEnabled = dnsCacheEnabled;
        this.dnsCacheSize = dnsCacheSize;
        this.lastUpdated = System.currentTimeMillis();
    }

    public NetworkConfig(String networkName, String domainName, String interfaceSpeed, String primaryDns, String secondaryDns, String dnsSuffix, boolean dnsCacheEnabled, int dnsCacheSize, long lastUpdated) {
        this.networkName = networkName;
        this.domainName = domainName;
        this.interfaceSpeed = interfaceSpeed;
        this.primaryDns = primaryDns;
        this.secondaryDns = secondaryDns;
        this.dnsSuffix = dnsSuffix;
        this.dnsCacheEnabled = dnsCacheEnabled;
        this.dnsCacheSize = dnsCacheSize;
        this.lastUpdated = lastUpdated;
    }

    public String getNetworkName() {
        return networkName;
    }

    public void setNetworkName(String networkName) {
        this.networkName = networkName;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getInterfaceSpeed() {
        return interfaceSpeed;
    }

    public void setInterfaceSpeed(String interfaceSpeed) {
        this.interfaceSpeed = interfaceSpeed;
    }

    public String getPrimaryDns() {
        return primaryDns;
    }

    public void setPrimaryDns(String primaryDns) {
        this.primaryDns = primaryDns;
    }

    public String getSecondaryDns() {
        return secondaryDns;
    }

    public void setSecondaryDns(String secondaryDns) {
        this.secondaryDns = secondaryDns;
    }

    public String getDnsSuffix() {
        return dnsSuffix;
    }

    public void setDnsSuffix(String dnsSuffix) {
        this.dnsSuffix = dnsSuffix;
    }

    public boolean isDnsCacheEnabled() {
        return dnsCacheEnabled;
    }

    public void setDnsCacheEnabled(boolean dnsCacheEnabled) {
        this.dnsCacheEnabled = dnsCacheEnabled;
    }

    public int getDnsCacheSize() {
        return dnsCacheSize;
    }

    public void setDnsCacheSize(int dnsCacheSize) {
        this.dnsCacheSize = dnsCacheSize;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
