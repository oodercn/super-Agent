package net.ooder.sdk.network.packet;

public class SleepInfo {
    private boolean enabled;
    private String sleepMode;
    private Long sleepStartTime;
    private Long estimatedWakeTime;
    private boolean canWakeImmediately;
    private String wakeReason;
    private boolean canAcceptCommands;
    private long sleepDuration;

    // Getter and Setter methods
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getSleepMode() {
        return sleepMode;
    }

    public void setSleepMode(String sleepMode) {
        this.sleepMode = sleepMode;
    }

    public Long getSleepStartTime() {
        return sleepStartTime;
    }

    public void setSleepStartTime(Long sleepStartTime) {
        this.sleepStartTime = sleepStartTime;
    }

    public Long getEstimatedWakeTime() {
        return estimatedWakeTime;
    }

    public void setEstimatedWakeTime(Long estimatedWakeTime) {
        this.estimatedWakeTime = estimatedWakeTime;
    }

    public boolean isCanWakeImmediately() {
        return canWakeImmediately;
    }

    public void setCanWakeImmediately(boolean canWakeImmediately) {
        this.canWakeImmediately = canWakeImmediately;
    }

    public String getWakeReason() {
        return wakeReason;
    }

    public void setWakeReason(String wakeReason) {
        this.wakeReason = wakeReason;
    }

    public boolean isCanAcceptCommands() {
        return canAcceptCommands;
    }

    public void setCanAcceptCommands(boolean canAcceptCommands) {
        this.canAcceptCommands = canAcceptCommands;
    }

    public long getSleepDuration() {
        return sleepDuration;
    }

    public void setSleepDuration(long sleepDuration) {
        this.sleepDuration = sleepDuration;
    }
}
