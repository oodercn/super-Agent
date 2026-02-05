package net.ooder.sdk.system.enums;

public enum CommandStatus {
    PENDING("pending"),
    BROADCASTING("broadcasting"),
    BROADCASTED("broadcasted"),
    EXECUTING("executing"),
    COMPLETED("completed"),
    FAILED("failed"),
    CANCELLED("cancelled"),
    TIMEOUT("timeout");

    private final String value;

    CommandStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static CommandStatus fromValue(String value) {
        for (CommandStatus status : values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown command status: " + value);
    }
}
