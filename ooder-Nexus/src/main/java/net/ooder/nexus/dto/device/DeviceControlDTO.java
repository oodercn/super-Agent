package net.ooder.nexus.dto.device;

import java.io.Serializable;
import java.util.Map;

public class DeviceControlDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String deviceId;
    private String command;
    private Map<String, Object> parameters;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }
}
