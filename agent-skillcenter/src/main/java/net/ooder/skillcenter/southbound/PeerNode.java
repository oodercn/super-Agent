package net.ooder.skillcenter.southbound;

import java.util.Map;

public class PeerNode {
    private String peerId;
    private String peerName;
    private String peerType;
    private String ipAddress;
    private int port;
    private long lastSeen;
    private boolean online;
    private Map<String, String> capabilities;

    public String getPeerId() { return peerId; }
    public void setPeerId(String peerId) { this.peerId = peerId; }

    public String getPeerName() { return peerName; }
    public void setPeerName(String peerName) { this.peerName = peerName; }

    public String getPeerType() { return peerType; }
    public void setPeerType(String peerType) { this.peerType = peerType; }

    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }

    public int getPort() { return port; }
    public void setPort(int port) { this.port = port; }

    public long getLastSeen() { return lastSeen; }
    public void setLastSeen(long lastSeen) { this.lastSeen = lastSeen; }

    public boolean isOnline() { return online; }
    public void setOnline(boolean online) { this.online = online; }

    public Map<String, String> getCapabilities() { return capabilities; }
    public void setCapabilities(Map<String, String> capabilities) { this.capabilities = capabilities; }
}
