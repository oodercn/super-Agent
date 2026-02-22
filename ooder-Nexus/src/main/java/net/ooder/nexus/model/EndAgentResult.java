package net.ooder.nexus.model;

public class EndAgentResult {
    private String agentId;
    private String agentName;
    private String status;
    private String ipAddress;
    private String macAddress;
    private String lastOnline;
    private String agentVersion;
    private boolean success;
    private String message;

    public EndAgentResult() {
    }

    public EndAgentResult(String agentId, String agentName, String status, String ipAddress, String macAddress, String lastOnline, String agentVersion, boolean success, String message) {
        this.agentId = agentId;
        this.agentName = agentName;
        this.status = status;
        this.ipAddress = ipAddress;
        this.macAddress = macAddress;
        this.lastOnline = lastOnline;
        this.agentVersion = agentVersion;
        this.success = success;
        this.message = message;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getLastOnline() {
        return lastOnline;
    }

    public void setLastOnline(String lastOnline) {
        this.lastOnline = lastOnline;
    }

    public String getAgentVersion() {
        return agentVersion;
    }

    public void setAgentVersion(String agentVersion) {
        this.agentVersion = agentVersion;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}