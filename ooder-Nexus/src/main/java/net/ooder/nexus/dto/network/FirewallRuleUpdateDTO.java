package net.ooder.nexus.dto.network;

import java.io.Serializable;

public class FirewallRuleUpdateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Boolean enabled;

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
