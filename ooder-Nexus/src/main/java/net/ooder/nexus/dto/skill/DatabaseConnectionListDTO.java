package net.ooder.nexus.dto.skill;

import net.ooder.nexus.domain.skill.model.DatabaseConnection;

import java.io.Serializable;
import java.util.List;

public class DatabaseConnectionListDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<DatabaseConnection> connections;

    public List<DatabaseConnection> getConnections() {
        return connections;
    }

    public void setConnections(List<DatabaseConnection> connections) {
        this.connections = connections;
    }
}
