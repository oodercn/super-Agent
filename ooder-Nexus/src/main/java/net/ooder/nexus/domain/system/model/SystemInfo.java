package net.ooder.nexus.domain.system.model;

import java.io.Serializable;

/**
 * 系统信息实体类
 * 
 * 本类用于表示系统的基本信息，包括版本、名称、描述等
 * 
 * @version 1.0.0
 */
public class SystemInfo implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String version;
    private String name;
    private String description;
    private long startTime;
    private long uptime;
    private String environment;
    private String javaVersion;
    private String osName;
    private String osVersion;
    private String hostname;
    private String ipAddress;
    
    public SystemInfo() {
    }
    
    public SystemInfo(String version, String name, String description, long startTime, 
                     String environment, String javaVersion, String osName, 
                     String osVersion, String hostname, String ipAddress) {
        this.version = version;
        this.name = name;
        this.description = description;
        this.startTime = startTime;
        this.uptime = 0;
        this.environment = environment;
        this.javaVersion = javaVersion;
        this.osName = osName;
        this.osVersion = osVersion;
        this.hostname = hostname;
        this.ipAddress = ipAddress;
    }
    
    // Getters and Setters
    public String getVersion() {
        return version;
    }
    
    public void setVersion(String version) {
        this.version = version;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public long getStartTime() {
        return startTime;
    }
    
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
    
    public long getUptime() {
        return uptime;
    }
    
    public void setUptime(long uptime) {
        this.uptime = uptime;
    }
    
    public String getEnvironment() {
        return environment;
    }
    
    public void setEnvironment(String environment) {
        this.environment = environment;
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
}
