package net.ooder.nexus.dto.network;

import java.io.Serializable;

public class NetworkLinkUpdateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String status;
    private String type;
    private String description;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
