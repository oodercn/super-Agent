package net.ooder.skillcenter.southbound;

public class SouthboundStatus {
    private String mode;
    private boolean networkReady;
    private boolean storageReady;
    private int peerCount;
    private int installedSkillCount;
    private int activeGroupCount;
    private long uptime;

    public String getMode() { return mode; }
    public void setMode(String mode) { this.mode = mode; }

    public boolean isNetworkReady() { return networkReady; }
    public void setNetworkReady(boolean networkReady) { this.networkReady = networkReady; }

    public boolean isStorageReady() { return storageReady; }
    public void setStorageReady(boolean storageReady) { this.storageReady = storageReady; }

    public int getPeerCount() { return peerCount; }
    public void setPeerCount(int peerCount) { this.peerCount = peerCount; }

    public int getInstalledSkillCount() { return installedSkillCount; }
    public void setInstalledSkillCount(int installedSkillCount) { this.installedSkillCount = installedSkillCount; }

    public int getActiveGroupCount() { return activeGroupCount; }
    public void setActiveGroupCount(int activeGroupCount) { this.activeGroupCount = activeGroupCount; }

    public long getUptime() { return uptime; }
    public void setUptime(long uptime) { this.uptime = uptime; }
}
