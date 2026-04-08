package net.ooder.mvp.skill.scene.dto.scene;

import java.util.List;

public class DeviceBindingDefDTO {
    private String deviceId;
    private String type;
    private List<String> capabilities;
    private String location;

    public String getDeviceId() { return deviceId; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public List<String> getCapabilities() { return capabilities; }
    public void setCapabilities(List<String> capabilities) { this.capabilities = capabilities; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
}
