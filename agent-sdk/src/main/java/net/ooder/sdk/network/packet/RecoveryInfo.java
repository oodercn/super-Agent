package net.ooder.sdk.network.packet;

public class RecoveryInfo {
    private boolean autoRecovery;
    private String recoveryAction;
    private Long estimatedRecoveryTime;

    // Getter and Setter methods
    public boolean isAutoRecovery() {
        return autoRecovery;
    }

    public void setAutoRecovery(boolean autoRecovery) {
        this.autoRecovery = autoRecovery;
    }

    public String getRecoveryAction() {
        return recoveryAction;
    }

    public void setRecoveryAction(String recoveryAction) {
        this.recoveryAction = recoveryAction;
    }

    public Long getEstimatedRecoveryTime() {
        return estimatedRecoveryTime;
    }

    public void setEstimatedRecoveryTime(Long estimatedRecoveryTime) {
        this.estimatedRecoveryTime = estimatedRecoveryTime;
    }
}
