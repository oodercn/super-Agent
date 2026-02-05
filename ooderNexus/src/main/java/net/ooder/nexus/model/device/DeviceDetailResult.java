package net.ooder.nexus.model.device;

import java.io.Serializable;
import java.util.Map;

/**
 * 设备详情结果
 * 用于DeviceController中getDeviceDetail方法的返回类型
 */
public class DeviceDetailResult implements Serializable {
    private String id;
    private String name;
    private String type;
    private String status;
    private String location;
    private double powerConsumption;
    private boolean poweredOn;
    private Map<String, Object> properties;
    private String installationDate;
    private String manufacturer;
    private String firmwareVersion;
    private long lastUpdated;

    public DeviceDetailResult() {
    }

    public DeviceDetailResult(String id, String name, String type, String status, String location, 
                             double powerConsumption, boolean poweredOn, Map<String, Object> properties, 
                             String installationDate, String manufacturer, String firmwareVersion, long lastUpdated) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.status = status;
        this.location = location;
        this.powerConsumption = powerConsumption;
        this.poweredOn = poweredOn;
        this.properties = properties;
        this.installationDate = installationDate;
        this.manufacturer = manufacturer;
        this.firmwareVersion = firmwareVersion;
        this.lastUpdated = lastUpdated;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getPowerConsumption() {
        return powerConsumption;
    }

    public void setPowerConsumption(double powerConsumption) {
        this.powerConsumption = powerConsumption;
    }

    public boolean isPoweredOn() {
        return poweredOn;
    }

    public void setPoweredOn(boolean poweredOn) {
        this.poweredOn = poweredOn;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public String getInstallationDate() {
        return installationDate;
    }

    public void setInstallationDate(String installationDate) {
        this.installationDate = installationDate;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    public void setFirmwareVersion(String firmwareVersion) {
        this.firmwareVersion = firmwareVersion;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}