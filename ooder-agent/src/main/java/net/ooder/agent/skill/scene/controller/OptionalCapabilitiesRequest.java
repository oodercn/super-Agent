package net.ooder.mvp.skill.scene.controller;

import java.util.List;

public class OptionalCapabilitiesRequest {
    private List<String> capabilities;

    public OptionalCapabilitiesRequest() {}

    public List<String> getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(List<String> capabilities) {
        this.capabilities = capabilities;
    }
}
