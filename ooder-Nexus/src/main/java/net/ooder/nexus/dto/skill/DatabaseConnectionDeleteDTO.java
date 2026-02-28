package net.ooder.nexus.dto.skill;

import java.io.Serializable;

public class DatabaseConnectionDeleteDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String connectionId;

    public String getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(String connectionId) {
        this.connectionId = connectionId;
    }
}
