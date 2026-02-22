package net.ooder.nexus.skillcenter.dto.kubernetes;

public class NodeInfoDTO {
    private String name;
    private String status;
    private String role;
    private String version;
    private String kernelVersion;
    private String os;
    private String architecture;
    private double cpuCapacity;
    private double cpuUsage;
    private long memoryCapacity;
    private long memoryUsage;
    private int podCapacity;
    private int podCount;
    private long createdAt;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getKernelVersion() {
        return kernelVersion;
    }

    public void setKernelVersion(String kernelVersion) {
        this.kernelVersion = kernelVersion;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getArchitecture() {
        return architecture;
    }

    public void setArchitecture(String architecture) {
        this.architecture = architecture;
    }

    public double getCpuCapacity() {
        return cpuCapacity;
    }

    public void setCpuCapacity(double cpuCapacity) {
        this.cpuCapacity = cpuCapacity;
    }

    public double getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(double cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public long getMemoryCapacity() {
        return memoryCapacity;
    }

    public void setMemoryCapacity(long memoryCapacity) {
        this.memoryCapacity = memoryCapacity;
    }

    public long getMemoryUsage() {
        return memoryUsage;
    }

    public void setMemoryUsage(long memoryUsage) {
        this.memoryUsage = memoryUsage;
    }

    public int getPodCapacity() {
        return podCapacity;
    }

    public void setPodCapacity(int podCapacity) {
        this.podCapacity = podCapacity;
    }

    public int getPodCount() {
        return podCount;
    }

    public void setPodCount(int podCount) {
        this.podCount = podCount;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}
