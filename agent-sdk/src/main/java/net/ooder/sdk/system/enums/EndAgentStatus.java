package net.ooder.sdk.system.enums;

public enum EndAgentStatus {
    ONLINE("online"),
    OFFLINE("offline"),
    BUSY("busy"),
    ERROR("error");

    private final String value;

    EndAgentStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static EndAgentStatus fromValue(String value) {
        for (EndAgentStatus status : values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown end agent status: " + value);
    }
}
