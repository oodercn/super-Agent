package net.ooder.sdk.enums;

public enum ComponentStatus {
    HEALTHY("healthy"),
    DEGRADED("degraded"),
    UNHEALTHY("unhealthy");

    private final String value;

    ComponentStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static ComponentStatus fromValue(String value) {
        for (ComponentStatus status : values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown component status: " + value);
    }
}
