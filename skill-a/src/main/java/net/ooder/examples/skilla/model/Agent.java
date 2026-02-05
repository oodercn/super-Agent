package net.ooder.examples.skilla.model;

import java.io.Serializable;

public class Agent implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String type;
    private String network;
    private String description;
    private String status;
    private long joinTime;

    // Getters and setters
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

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(long joinTime) {
        this.joinTime = joinTime;
    }

    @Override
    public String toString() {
        return "Agent{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", network='" + network + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", joinTime=" + joinTime +
                '}';
    }
}
