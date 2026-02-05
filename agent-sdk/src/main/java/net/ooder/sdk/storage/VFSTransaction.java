
package net.ooder.sdk.storage;

public class VFSTransaction {
    private final String transactionId;
    private final String commandId;
    private TransactionStatus status;
    private final long startTime;
    private long endTime;
    private String errorMessage;

    public VFSTransaction(String commandId) {
        this.transactionId = java.util.UUID.randomUUID().toString();
        this.commandId = commandId;
        this.status = TransactionStatus.STARTED;
        this.startTime = System.currentTimeMillis();
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getCommandId() {
        return commandId;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
        if (status == TransactionStatus.COMMITTED || status == TransactionStatus.ROLLED_BACK) {
            this.endTime = System.currentTimeMillis();
        }
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isActive() {
        return status == TransactionStatus.STARTED || status == TransactionStatus.IN_PROGRESS;
    }

    public boolean isCompleted() {
        return status == TransactionStatus.COMMITTED || status == TransactionStatus.ROLLED_BACK;
    }

    public long getDuration() {
        if (endTime == 0) {
            return System.currentTimeMillis() - startTime;
        }
        return endTime - startTime;
    }

    @Override
    public String toString() {
        return "VFSTransaction{" +
                "transactionId='" + transactionId + '\'' +
                ", commandId='" + commandId + '\'' +
                ", status=" + status +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }

    public enum TransactionStatus {
        STARTED("started"),
        IN_PROGRESS("in_progress"),
        COMMITTED("committed"),
        ROLLED_BACK("rolled_back");

        private final String value;

        TransactionStatus(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return value;
        }

        public static TransactionStatus fromValue(String value) {
            for (TransactionStatus status : values()) {
                if (status.value.equals(value)) {
                    return status;
                }
            }
            throw new IllegalArgumentException("Unknown transaction status: " + value);
        }
    }
}

