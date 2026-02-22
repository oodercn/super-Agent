package net.ooder.nexus.domain.network.model;

import java.io.Serializable;

public class NetworkDevice implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String type;
    private String ipAddress;
    private String macAddress;
    private String status;
    private String vendor;
    private String model;
    private String firmwareVersion;
    private long lastSeen;

    public NetworkDevice() {
    }

    public NetworkDevice(String id, String name, String type, String ipAddress, String macAddress,
                       String status, String vendor, String model, String firmwareVersion, long lastSeen) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.ipAddress = ipAddress;
        this.macAddress = macAddress;
        this.status = status;
        this.vendor = vendor;
        this.model = model;
        this.firmwareVersion = firmwareVersion;
        this.lastSeen = lastSeen;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    public void setFirmwareVersion(String firmwareVersion) {
        this.firmwareVersion = firmwareVersion;
    }

    public long getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(long lastSeen) {
        this.lastSeen = lastSeen;
    }
}
