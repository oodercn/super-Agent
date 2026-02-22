package net.ooder.nexus.skillcenter.dto.system;

import net.ooder.nexus.skillcenter.dto.BaseDTO;

public class SystemResourcesDTO extends BaseDTO {

    private int availableProcessors;
    private long totalMemory;
    private long freeMemory;
    private long usedMemory;
    private long maxMemory;
    private String totalMemoryHuman;
    private String freeMemoryHuman;
    private String usedMemoryHuman;
    private String maxMemoryHuman;
    private long uptime;
    private String uptimeHuman;

    public SystemResourcesDTO() {}

    public int getAvailableProcessors() {
        return availableProcessors;
    }

    public void setAvailableProcessors(int availableProcessors) {
        this.availableProcessors = availableProcessors;
    }

    public long getTotalMemory() {
        return totalMemory;
    }

    public void setTotalMemory(long totalMemory) {
        this.totalMemory = totalMemory;
    }

    public long getFreeMemory() {
        return freeMemory;
    }

    public void setFreeMemory(long freeMemory) {
        this.freeMemory = freeMemory;
    }

    public long getUsedMemory() {
        return usedMemory;
    }

    public void setUsedMemory(long usedMemory) {
        this.usedMemory = usedMemory;
    }

    public long getMaxMemory() {
        return maxMemory;
    }

    public void setMaxMemory(long maxMemory) {
        this.maxMemory = maxMemory;
    }

    public String getTotalMemoryHuman() {
        return totalMemoryHuman;
    }

    public void setTotalMemoryHuman(String totalMemoryHuman) {
        this.totalMemoryHuman = totalMemoryHuman;
    }

    public String getFreeMemoryHuman() {
        return freeMemoryHuman;
    }

    public void setFreeMemoryHuman(String freeMemoryHuman) {
        this.freeMemoryHuman = freeMemoryHuman;
    }

    public String getUsedMemoryHuman() {
        return usedMemoryHuman;
    }

    public void setUsedMemoryHuman(String usedMemoryHuman) {
        this.usedMemoryHuman = usedMemoryHuman;
    }

    public String getMaxMemoryHuman() {
        return maxMemoryHuman;
    }

    public void setMaxMemoryHuman(String maxMemoryHuman) {
        this.maxMemoryHuman = maxMemoryHuman;
    }

    public long getUptime() {
        return uptime;
    }

    public void setUptime(long uptime) {
        this.uptime = uptime;
    }

    public String getUptimeHuman() {
        return uptimeHuman;
    }

    public void setUptimeHuman(String uptimeHuman) {
        this.uptimeHuman = uptimeHuman;
    }
}
