package net.ooder.skill.config.dto;

import java.io.Serializable;
import java.util.Map;

public class CliMockRequestDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String action;
    private Map<String, Object> params;
    private boolean dryRun;
    private String callbackUrl;

    public CliMockRequestDTO() {}

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public boolean isDryRun() {
        return dryRun;
    }

    public void setDryRun(boolean dryRun) {
        this.dryRun = dryRun;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }
}
